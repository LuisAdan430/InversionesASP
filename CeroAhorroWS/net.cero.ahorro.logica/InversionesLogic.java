package net.cero.ahorro.logica;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.mx.url.RutasProperties;

import net.cero.data.AhorroCuentaMiDebitoOBJ;
import net.cero.data.AhorroOBJ;
import net.cero.data.Atachments;
import net.cero.data.CustomMultipartFile;
import net.cero.data.ExpedienteOBJ;
import net.cero.data.NuevaInversionReq;
import net.cero.data.NuevaInversionResponse;
import net.cero.data.Premio;
import net.cero.data.ProductoOBJ;
import net.cero.data.Respuesta;
import net.cero.data.Solicitante;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.HeaderWS;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroCuentasDAO;
import net.cero.spring.dao.AhorroCuentasMiDebitoDAO;
import net.cero.spring.dao.AhorroDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.ProductosDAO;
import net.cero.spring.dao.SolicitanteDAO;
import net.cero.spring.dao.SorteoDAO;
import net.cero.spring.dao.ValidacionesServiciosDAO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InversionesLogic {
	
	private static Apps apps = null;
	private static final Logger log = Logger.getLogger(InversionesLogic.class);
	private static DriverManagerDataSource ds = null;
	private static DriverManagerDataSource cero = null;
	private static ValidacionesServiciosDAO vsdao = null;
	private static AhorroDAO adao = null;
	private static SolicitanteDAO sdao = null;
	private static SorteoDAO sodao = null;
	private static AhorroCuentasDAO ahdao= null;
	private static AhorroCuentasMiDebitoDAO acmdao  = null;
	String correo = "";
	private static ProductosDAO pdao = null;
	private static AhorroRendimientoVigenteDAO arvdao = null;
	private static String REMITENTE_NOTIFICACIONES = "procesos@aspintegraopciones.com";
	private static String ASUNTO_CORREO_CODIGO = "Código de promoción utilizado";
	private static String ASUNTO_CORREO_CONTRATO = "Documentación de cuenta de inversión";
	private static String ASUNTO_CORREO_BOLETOS = "Boletos para sorteo ASP";
	private boolean sinBoletosDisponiblesARegalar = false;
	List<Atachments> archivos = new ArrayList<>();
	List<byte[]> lista = new ArrayList<>();
	private static Gson gson;

	private byte[] generarReporte(String reporte, Map<String, Object> params, DriverManagerDataSource dataSource,
			String nombre_a) throws JRException, SQLException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		JasperPrint print = JasperFillManager.fillReport(
				RutasProperties.getRuta() + "/Reportes/contrato_ahorro/" + reporte + ".jasper", params,
				dataSource.getConnection());
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		exporter.exportReport();
		Atachments archivo = new Atachments();
		archivo.setTheAttachmentBytes(output.toByteArray());
		archivo.setTheContentType("application/pdf");
		archivo.setTheFilename(nombre_a);
		archivos.add(archivo);
		return output.toByteArray();
	}

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			
			
			adao = (AhorroDAO) s.getApplicationContext().getBean("AhorroDAO");
			sdao = (SolicitanteDAO) s.getApplicationContext().getBean("SolicitanteDAO");
			sodao = (SorteoDAO) s.getApplicationContext().getBean("SorteoDAO");
			pdao = (ProductosDAO) s.getApplicationContext().getBean("ProductosDAO");
			arvdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			ds = (DriverManagerDataSource) s.getApplicationContext().getBean("dsPr");
			cero = (DriverManagerDataSource) s.getApplicationContext().getBean("ds");
			ahdao = (AhorroCuentasDAO) s.getApplicationContext().getBean("AhorroCuentasDAO");
			acmdao = (AhorroCuentasMiDebitoDAO) s.getApplicationContext().getBean("AhorroCuentasMiDebitoDAO");
			gson = new Gson();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getName());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public Respuesta crearInversion(String nombreEndpoint, String sessionId, NuevaInversionReq req) {
		initialized();
		EnviaNotificacionesMail mail = new EnviaNotificacionesMail();
		String Textoresultado;
		Respuesta resp = new Respuesta();
		if (req.getTipo() == null || req.getTipo().length() == 0) 
			Textoresultado = "Felicidades, has creado una inversión";
		else
			Textoresultado = "¡¡Felicidades!!/Has creado una inversión/Invita a amigos y familiares a realizar una o más inversiones y participa en el sorteo.Invierte y Gana un auto con ASP/";
		//String strSucVirtual = ConstantesUtil.STR_SUCURSAL_VIRTUAL;
			try {
				if (!sodao.consultaBoletosDisponibles()) {
					resp.setCodigo(10);
					resp.setMensaje("No hay boletos disponibles");
					return resp;
				}
				
				resp = validarCamposVacios(req);//Validación de que ningún dato venga vacío//
				if (resp.getCodigo() != 0) {
					return resp;
				}
				
				List<Premio> premios = sodao.getPremios(req.getTipo());
				if (!premios.isEmpty()) {
					resp = validarCodigoPromocion(req, premios);//Validación de los códigos de promoción//
					if (resp.getCodigo() != 0) {
						return resp;
					}
				}
				//AhorroOBJ ahorro = adao.buscarAhorroProcrea(req.getCuentaOriginal()); //La variable ahorro solo se utiliza para mostrarse en consola
					
				Integer tipo_cuenta = adao.tipoCuentaCero(req.getCuentaOriginal());//Validación del tipo de cuenta en cero//
				if (tipo_cuenta != 6) {
					resp.setMensaje("Solo se permiten inversiones para cuentas simplificadas");
					resp.setData(null);
					resp.setCodigo(-3);
					return resp;
				}
					
					//BigDecimal saldo = adao.buscarSaldoCero(req.getCuentaOriginal());//Obtiene el saldo en la base de datos cero
				BigDecimal saldo = obtenerSaldo(req.getCuentaOriginal());	
				if (saldo.doubleValue() < req.getMonto().doubleValue()) {
						resp.setMensaje("La cuenta seleccionada no tiene el suficiente saldo prueba");
						resp.setData(null);
						resp.setCodigo(-1);
						return resp;
					}
				

				//Integer idTipoAhorro = ConstantesUtil.PRODUCTO;
				//AhorroOBJ ahoAux = null; // no se utiliza
				//Integer cuenta_id = -1; // no se utiliza
				//Integer cont = 0;
				
				//Verifica si existe una cuenta, si la cuenta existe, solo le suma 1//
				long cuentaExistente = 0;
				cuentaExistente = adao.existeEnProcrea(req.getSolicitante_id());
				String cuenta = "";
				if (cuentaExistente == 0) {
					cuenta = generarCuentaNueva();
				}else {
					cuentaExistente += 1;
					cuenta = String.format("%010d", Long.valueOf(cuentaExistente));
				}
				/*do {
					cont++;
					cuenta = cuentaOriginal.substring(0, 8) + String.format("%02d", Integer.valueOf(cont));
					cuentaExistente = adao.existeEnProcrea(cuenta);
				} while (cuentaExistente > 0);//Cambiar por el código para generar la cuenta
				*/
		       
				//String contrato = adao.obtenerContratoAhorro(req.getCuentaOriginal());//Mandar la cuenta original
				

				
				//cuenta_id = adao.obtenerIdCuentaAhorro(req.getCuentaOriginal()); // no se utiliza
				
				
				ProductoOBJ tipoInversion = pdao.buscarProductoInversion(
						Integer.valueOf(String.valueOf(req.getHeader().getIdCanalAtencion())), req.getMeses_plazo());//Obtener tipo de inversion//
				Integer rendimientoId = Integer.valueOf(ConstantesUtil.RENDIMIENTO_ID_36);
				Integer plazo = 360;
				if (tipoInversion != null) {
					rendimientoId = tipoInversion.getRendimientoId();
					plazo = tipoInversion.getPlazo();
				}
				Double interes = adao.obtenerInteresRendimiento(rendimientoId, req.getMonto().doubleValue());
				// Integer idAhorro =
				// adao.insertarDatosEnAhorroContrato(Integer.valueOf(String.valueOf(req.getHeader().getIdUsuario())),
				// cuentaOriginal, cuentaContable, cuenta, rendimientoId,
				// req.getSolicitante_id(), cuenta_id,
				// req.getMonto().doubleValue(), contrato);
				String contrato;
				
				contrato = adao.obtenerContratoAhorro(req.getSolicitante_id());
				
				if (contrato.equals("")||contrato.isEmpty()) {
					contrato = generarContrato();
				}
				Solicitante solicitante = sdao.obtenerNombreSolicitanteByNumero(req.getSolicitante_id());
				String codigo_promocion = sodao.codigoDePromicion(req.getSolicitante_id(), solicitante,
						Integer.valueOf(String.valueOf(req.getHeader().getIdUsuario())), cuenta,
						premios.get(0).getSorteo_id());
				
				
				String cuentaContable = adao.regresaCuentaContable(Integer.valueOf(ConstantesUtil.CAPITALIZAR_ID), 1);
				String referencia = adao.generaAhorroReferencia(cuenta,
						Integer.valueOf(ConstantesUtil.USUARIO_SISTEMAS), ConstantesUtil.STR_SUCURSAL_VIRTUAL);
				Integer idAhorro = adao.insertaRegistroAhorro(cuenta, req.getSolicitante_id(), contrato, rendimientoId,
						ConstantesUtil.STR_SUCURSAL_VIRTUAL,
						cuentaContable, referencia, Integer.valueOf(String.valueOf(req.getHeader().getIdUsuario())),
						req.getCuentaOriginal(), cuenta, cuenta, null, req.getMonto().doubleValue(), "INVERSION POR ASP PAGO",
						interes, 1);
				
				if (idAhorro == 0) {//valida si se creó la inversión
					log.error("Error al Crear Ahorro Contrato");
					resp.setMensaje("Error al crear el contrato");
					resp.setCodigo(-2);
					resp.setData(null);
					return resp;
				}
				Respuesta rsp = realizarDeposito(req.getHeader(), req.getCuentaOriginal(), cuenta, req.getMonto(),"DEBITO","AHORRO");//////////AQUI LA CUENTA ORIGEN PUEDE SER O SERA DE MI DEBITO
				if (rsp.getCodigo() == 0) {
					Calendar fechaAct = Calendar.getInstance();
					Calendar fecha = Calendar.getInstance();
					fecha.add(Calendar.DAY_OF_MONTH, plazo);
					arvdao.actualizarEstatus("A", new java.sql.Date(fechaAct.getTimeInMillis()),
							new java.sql.Date(fecha.getTimeInMillis()), new java.sql.Date(fechaAct.getTimeInMillis()),
							Integer.parseInt(ConstantesUtil.CAPITALIZAR_ID), cuenta, "P");
					Double gat = adao.regresaCalculoGAT(cuenta);

					String clabe = generaCuentaClabe(referencia, tipo_cuenta, ConstantesUtil.STR_SUCURSAL_VIRTUAL);
					adao.actualizaAhorroContrato(clabe, gat, idAhorro);
					resp.setMensaje("OK");
					SorteoLogic sl = new SorteoLogic();
					if(req.getCodigo_promocion()!= null && !req.getCodigo_promocion().equals("") && !sinBoletosDisponiblesARegalar){
						//buscar la el numero y el correo de la persona que es dueña del codigo
						String [] solicitanteCorreoCodigo = sodao.obtenerCorreo(req.getCodigo_promocion());
						//buscar nombre de la persona que uso el codigo
						Gson gson = new Gson();
						//Solicitante solicitante = sdao.obtenerNombreSolicitanteByNumero(req.getSolicitante_id());
						String nombre_completo = solicitante.getNombreP() + " " + solicitante.getApellidos() + " " + solicitante.getApellidoM();
						//asignar el boleto a la persona que genero el codigo
						Respuesta respuestaVeces = sl.actualizarVecesUtilizadoCodigo(req.getCodigo_promocion());
						if(respuestaVeces.getCodigo() != 0){
							return resp;
						}
						resp = sl.asignarBoletosSorteo(premios.get(0).getSorteo_id(),solicitanteCorreoCodigo[1], req.getCodigo_promocion(), Integer.valueOf(String.valueOf(req.getHeader().getIdUsuario())) ,Integer.valueOf(1),null);
						//actualizar los boletos regulates
					//	boolean seActualizaronLosBoletos = sodao.actualizarBoletosRegaladosSorteo(premios.get(0).getSorteo_id()); // no se utiliza
						if(resp.getData() != null) {
							Integer [] boletos = gson.fromJson(resp.getData(), Integer[].class);
							
							//notificar al dueño del codigo que el codigo se utilizo y tiene un boleto extra
							new Thread(()->{
								mail.sendMail(REMITENTE_NOTIFICACIONES, solicitanteCorreoCodigo[0], ASUNTO_CORREO_CODIGO, generaCuerpoCorreoCodigo(premios.get(0).getDescripcion_sorteo(), boletos[0], nombre_completo), null);
							}).start();
						}
					}
					
					Respuesta asignarBoletosResp = sl.asignarBoletosSorteo(premios.get(0).getSorteo_id(),
							req.getSolicitante_id(), null, 9,
							(int) req.getMonto().doubleValue() / 500,cuenta);
					resp.setCodigo(asignarBoletosResp.getCodigo());
					resp.setData(asignarBoletosResp.getData());
					resp.setMensaje(asignarBoletosResp.getMensaje());
					if (asignarBoletosResp.getCodigo() == 0) {
						Map<String, Object> params = new HashMap<>();
						params.put("SUBREPORT_DIR", ConstantesUtil.REPORTES_CONTRATO_AHORRO);
						params.put("pCuenta", cuenta);
						params.put("pImagen", RutasProperties.getRuta() + "Reportes/img/asp.png");
						lista.add(generarReporte("Registro_Contrato", params, ds, "REGISTRO_CONTRATO"));
						lista.add(generarReporte("ComprobanteDep", params, ds, "Constancia de deposito"));
						lista.add(
								generarReporte("disposiciones_legales", params, ds, "ANEXO A: DISPOSICIONES LEGALES"));
						List<String> archivosNombres = new ArrayList<>();
						archivosNombres.add("Registro Contrato.pdf");
						archivosNombres.add("Constancia de deposito.pdf");
						archivosNombres.add("Anexo A: Disposiciones legales.pdf");
						//List<Integer> identificadores = new ArrayList<>();
						int identificador = 35;
						Gson gson = new Gson();
						ExpedienteOBJ ex = null;
						HeaderWS header = req.getHeader();
						String[] expediente = new String[archivosNombres.size()];
						MultipartFile[] archivosMultipart = new MultipartFile[lista.size()];
						int i = 0;
						Respuesta respuesta = new Respuesta();
						for (byte[] archivo : lista) {
							ex = new ExpedienteOBJ();
							MultipartFile customMultipartFile = new CustomMultipartFile(archivo,
									archivosNombres.get(i));
							archivosMultipart[i] = customMultipartFile;
							ex.setExtension(archivosNombres.get(i).split("[.]")[1]);
							ex.setNombre(archivosNombres.get(i).split("[.]")[0]);
							ex.setIdentificador_documento(identificador);
							ex.setDocumento("");
							expediente[i] = gson.toJson(ex);
							identificador++;
							i++;
						}
						try {
							String auth = Credentials.basic("ASP", "a5p2017$");
							Builder builder = new Builder();
							builder.setType(MultipartBody.FORM);
							for (i = 0; i < expediente.length; i++) {
								builder.addFormDataPart("expediente", expediente[i]);
							}
							builder.addFormDataPart("header", gson.toJson(header)).addFormDataPart("cuenta", cuenta)
									.addFormDataPart("modulo", "INVERSIONES")
									.addFormDataPart("ruta",
											req.getSolicitante_id() + "/" + "INVERSIONES" + "/" + cuenta)
									.addFormDataPart("persona_id", req.getSolicitante_id());
							for (MultipartFile f : archivosMultipart) {
								File newFile = convertMultiPartToFile(f);
								builder.addFormDataPart("file", f.getOriginalFilename(),
										okhttp3.RequestBody.create(MediaType.parse("application/pdf"), newFile));
							}
							MultipartBody body = builder.build();
							Request request = new Request.Builder()
									.url(ConstantesUtil.NUCLEO_CENTRAL_CARTERA_WS + "/subirArchivos")
									.post(body).header("Authorization", auth).build();
							OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
									.readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
							Response response = client.newCall(request).execute();

							resp = gson.fromJson(response.body().string(), Respuesta.class);
							log.info("Respuesta de subir archivos: " + resp.getMensaje());
							if (resp.getCodigo() == 0) {
								//String imagen = "";
								correo = sdao.getCorreo(req.getSolicitante_id());
								log.info("El correo es: " + correo);
								boolean cuentaVigente = true;
								if (cuentaVigente) {
									new Thread(() -> {
										mail.sendMail(REMITENTE_NOTIFICACIONES, correo, ASUNTO_CORREO_CONTRATO,
												generaNuevoCuerpoCorreo(), archivos);
									}).start();
									new Thread(() -> {
										mail.sendMail(REMITENTE_NOTIFICACIONES, correo, ASUNTO_CORREO_BOLETOS,
												generaCuerpoCorreoBoletos(premios.get(0).getDescripcion_sorteo(),
														gson.fromJson(asignarBoletosResp.getData(), Integer[].class),
														codigo_promocion),
												null);
									}).start();
									new Thread(() -> {
									sdao.copiaDatosCeroProcrea(req.getSolicitante_id());	
									}).start();
									NuevaInversionResponse res = new NuevaInversionResponse();
									res.setCuenta(cuenta);
									res.setCodigo_promocion(codigo_promocion);
									res.setResultado(Textoresultado);
									res.setImagen(premios.get(0).getSorteo_imagen());
									res.setBoletos(gson.fromJson(asignarBoletosResp.getData(), Integer[].class));
									resp.setData(gson.toJson(res));
									resp.setCodigo(0);
									resp.setMensaje("OK");
									log.info(res.getResultado());
									return resp;
								}
							} else {
								respuesta.setCodigo(resp.getCodigo());
								respuesta.setMensaje(resp.getMensaje());
								respuesta.setData(null);
								return respuesta;
							}
						} catch (Exception e) {
							log.error("error en: " + e.getMessage());
							e.printStackTrace();
							respuesta.setCodigo(-2);
							respuesta.setMensaje("Error de comunicacion 1");
							return respuesta;

						}

					}
				} else {
					rsp.setCodigo(1);
					rsp.setMensaje("Error al realizar la transacción entre cuentas ");
					log.info("Error al realizar la transaccion entre cuentas");
					return rsp;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Error al Crear la Cuenta de Inversion: " + e.getMessage());
				resp.setCodigo(-2);
				resp.setMensaje("Error de comunicacion 2 "+e.getMessage());
				return resp;
			}
			return resp;
	}
	private String generaCuerpoCorreoCodigo(String nombre_sorteo,Integer boleto,String nombre_persona){
		String body ="";
		
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\"><meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">"
				+ "<style>"
				+ "<!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	"
				+ "{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face	"
				+ "{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}"
				+ "@font-face	{font-family:\"Century Gothic\";	panose-1:2 11 5 2 2 2 2 2 2 4;}"
				+ "@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} "
				+ "p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}"
				+ "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}"
				+ "span.EstiloCorreo17	{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}"
				+ "span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}"
				+ ".MsoChpDefault	{mso-style-type:export-only;}"
				+ "@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}"
				+ "div.Section1	{page:Section1;}-->"
				+ "</style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]><xml> <o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" /> </o:shapelayout></xml><![endif]--></head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'><img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span>"
				+ "</p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Cliente;<o:p></o:p></span>"
				+ "</p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Su c&oacute;digo de promoci&oacute;n ha sido utilizado por el cliente "+nombre_persona+" y ha ganado un nuevo boleto.<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Siga participando en el sorteo \""+nombre_sorteo+"\" y gane m&aacute;s boletos realizando nuevas inversiones o compartiendo su c&oacute;digo de promoci&oacute;n<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt; font-size:13.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Su boleto es: "+boleto+"<o:p></o:p></span></p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Le recordamos que puede consultar las bases del sorteo \""+nombre_sorteo+"\" en www.aspintegraopciones.com y encontrar informaci&oacute;n relevante en nuestras redes sociales: <o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>FB ASP Pago \n FB ASP Integra Opciones<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p>\r\n"
				+ "	</span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Por su atenci&#243;n, Gracias.<o:p></o:p>\r\n"
				+ "	</span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>ASP Integra Opciones.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en <a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a>  o en tu Sucursal m&#225;s cercana.<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>Consulta los costos y comisiones de nuestros productos en <a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> <o:p></o:p></span></p><p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		return body;
	}
	

	private String generarCuentaNueva() {
		String cuenta;
        cuenta = "00000" + adao.obtenerSecuenciaCuenta();
        cuenta = cuenta.substring(cuenta.length() -5, cuenta.length());
        System.out.println(cuenta.substring(cuenta.length() -5, cuenta.length()));
        String S = "000" + ConstantesUtil.SUCURSAL_VIRTUAL;
        S = S.substring(S.length()-3, S.length());
        cuenta = S + cuenta;
        cuenta = cuenta + "00";
        long nuevacuenta;
        nuevacuenta = (Long.parseLong(cuenta) + 1);
        cuenta = String.format("%010d", Long.valueOf(nuevacuenta));
		return cuenta;
	}
	private String generaCuerpoCorreoBoletos(String nombre_sorteo, Integer[] boletos, String codigo_promocion) {
		String body = "";
		String boletosAString = "";
		Integer cont = 0;
		for (Integer b : boletos) {
			if (cont < boletos.length - 1)
				boletosAString += String.valueOf(b) + ", ";
			else
				boletosAString += String.valueOf(b);
			cont++;
		}

		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\"><meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">"
				+ "<style>"
				+ "<!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	"
				+ "{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face	"
				+ "{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}"
				+ "@font-face	{font-family:\"Century Gothic\";	panose-1:2 11 5 2 2 2 2 2 2 4;}"
				+ "@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} "
				+ "p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}"
				+ "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}"
				+ "span.EstiloCorreo17	{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}"
				+ "span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}"
				+ ".MsoChpDefault	{mso-style-type:export-only;}"
				+ "@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}"
				+ "div.Section1	{page:Section1;}-->"
				+ "</style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]><xml> <o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" /> </o:shapelayout></xml><![endif]--></head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'><img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span>"
				+ "</p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Cliente;<o:p></o:p></span>"
				+ "</p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&#161;Bienvenido a ASP Integra Opciones&#33;<o:p></o:p></span></p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Gracias por participar en el sorteo \""
				+ nombre_sorteo + "\".<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Le compartimos el(los) folio(s) de el(los) boleto(s) generados por su inversion(es):<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt; text-align:center; font-weight:bold; color:orange; font-size:13.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ boletosAString + "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Le recordamos que puede ganar m&#225;s boletos invitando a amigos y familiares a realizar una o m&#225;s inversiones compartiendo su c&#243;digo de promoci&#243;n:<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-align:center; font-weight:bold; color:orange;'><span lang=ES style='color:orange;font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ codigo_promocion + "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Por cada c&#243;digo compartido y efectivamente utilizado al abrir una inversi&#243;n usted recibir&#225; un boleto adicional. Para mayor informaci&#243;n consulte las bases del sorteo en www.aspintegraopciones.com<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p>\r\n"
				+ "	</span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Por su atenci&#243;n, Gracias.<o:p></o:p>\r\n"
				+ "	</span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>ASP Integra Opciones.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en <a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a>  o en tu Sucursal m&#225;s cercana.<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>Consulta los costos y comisiones de nuestros productos en <a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> <o:p></o:p></span></p><p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		return body;

	}

	private String generaNuevoCuerpoCorreo() {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\"><meta name=Generator content=\"Microsoft Word 12 (filtered medium)\"><style><!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}@font-face	{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}@font-face	{font-family:\"Century Gothic\";	panose-1:2 11 5 2 2 2 2 2 2 4;}@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}span.EstiloCorreo17	{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}.MsoChpDefault	{mso-style-type:export-only;}@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}div.Section1	{page:Section1;}--></style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]><xml> <o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" /> </o:shapelayout></xml><![endif]--></head><body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\"><div class=Section1><p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'><img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Cliente;<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Ha realizado la apertura de su inversi&oacute;n, en el presente correo se adjunta su Contrato de apertura de inversi&oacute;n <o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Le recordamos que si; existen dudas sobre el clausulado en general de su contrato o el manejo de su cuenta, est&#225; disponible la l&#237;nea 800 462 73 73.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Siempre ser&#225; un gusto atenderle.<o:p></o:p>\r\n"
				+ "	</span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p>\r\n"
				+ "	</span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Por su atenci&#243;n, Gracias.<o:p></o:p>\r\n"
				+ "	</span></p><p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>ASP Integra Opciones.<o:p></o:p></span></p><p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en <a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a>  o en tu Sucursal m&#225;s cercana.<o:p></o:p></span></p><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>Consulta los costos y comisiones de nuestros productos en <a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> <o:p></o:p></span></p><p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p><p class=MsoNormal><o:p>&nbsp;</o:p></p><p class=MsoNormal><o:p>&nbsp;</o:p></p></div></body></html>";
		return body;
	}

	public Respuesta realizarDeposito(HeaderWS header, String cuentaOrigen, String cuentaDestino, BigDecimal monto,String tipoCuentaOrigen,String tipoCuentaDestino) {
		AhorroTransferenciaLogic logic = new AhorroTransferenciaLogic();
		String uuidGlobal = UUID.randomUUID().toString();
		Respuesta resp = new Respuesta();
		try{
			net.cero.data.AhorroTransferenciaReqOBJ req = new net.cero.data.AhorroTransferenciaReqOBJ();
			req.setCuentaOrigen(cuentaOrigen);
			req.setCuentaDestino(cuentaDestino);
			req.setFecha(new Date());
			req.setMonto(monto.doubleValue());
			req.setUsuarioId(Integer.valueOf(ConstantesUtil.USUARIO_SISTEMAS));
			req.setTipoCuentaOrigen(tipoCuentaOrigen);
			req.setTipoCuentaDestino(tipoCuentaDestino);
			req.setConceptoOrigen("Transferencia a cuenta " + req.getCuentaDestino());
			req.setConceptoDestino("Deposito a plazo de " + cuentaDestino);

			resp = logic.procesaTransferencia(header, req, uuidGlobal);
			
		}catch(Exception e) {
			log.error("Error en realizar deposito: " + e.getMessage());
			resp.setCodigo(1);
			resp.setMensaje("Error al realizar el deposito a la cuenta de inversión");
		}
		

		return resp;
	}

	
	private String generarContrato() {
		String secuencia = adao.obtenerSecuenciaContrato();
		log.info("#Secuencia de contrato obtenida :: " + secuencia);
		String secV2 = "000000000" + secuencia;
		secV2 = secV2.substring(secV2.length() - 10, secV2.length());
		String contrato = secV2;
		log.info("#Contrato calculado :: " + contrato);
		//ahorroContratoNuevo.setContrato(contrato);
		return contrato;
	}

	private String obtenerTipoCuenta(String cuenta) {
		AhorroOBJ obj = null;

		obj = adao.buscarAhorroProcrea(cuenta);
		if (obj == null) {
			obj = adao.buscarAhorroCero(cuenta);
			if (obj == null) {
				List<AhorroOBJ> lst = adao.buscarAhorroPlastico(cuenta);
				if (lst.isEmpty())
					return "";
				else
					return "DEBITO";
			}
		}

		if (obj.getProductoClave().equals("DEBITO"))
			return "DEBITO";
		else
			return "AHORRO";
	}
	
	private Respuesta validarCamposVacios(NuevaInversionReq req) {
		Respuesta resp = new Respuesta();
		if (req.getHeader() == null) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos obligatorios (header)");
			resp.setData(null);
			return resp;
		} else if (req.getHeader().getIdUsuario() == 0) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos obligatorios (id usuario)");
			resp.setData(null);
			return resp;
		} else if (req.getHeader().getIdCanalAtencion() == 0) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos obligatorios (canal de atencion)");
			resp.setData(null);
			return resp;
		} else if (req.getCuentaOriginal() == null ||req.getCuentaOriginal().equals("")) {
			resp.setCodigo(-2);
			resp.setData(null);
			resp.setMensaje("Faltan datos obligatorios (cuenta original)");
			return resp;
		} else if (req.getMeses_plazo() == null) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos obligatorios (meses de plazo)");
			return resp;
		} else if (req.getMonto() == null) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos obligatorios (monto)");
			return resp;
		} else if (req.getMonto().doubleValue() % 500d != 0 || req.getMonto().doubleValue() / 500d < 1){
			resp.setCodigo(-2);
			resp.setMensaje("El monto para crear una inversión debe ser un múltiplo de 500");
			return resp;
		} else if (req.getSolicitante_id() == null || req.getSolicitante_id().equals("")) {
			resp.setCodigo(-2);
			resp.setMensaje("Faltan datos oligatorios (solicitante id)");
			return resp;
		}else {
			resp.setCodigo(0);
			return resp;
		}
	}
	private Respuesta validarCodigoPromocion(NuevaInversionReq req, List<Premio> premios) {
		Respuesta resp = new Respuesta();
		resp.setCodigo(0);
		if (req.getCodigo_promocion() != null) {
			int boletos_disponibles = sodao.informacionBoletos(premios.get(0).getSorteo_id());
			if(boletos_disponibles == 0){
				sinBoletosDisponiblesARegalar = true;
			}

			boolean elCodigoExiste = sodao.elCodigoExisteYEsParaSorteoVigente(req.getCodigo_promocion(), premios.get(0).getSorteo_id());
			//System.out.println("elCodigoExiste :: " + elCodigoExiste);
			if(!elCodigoExiste){
				resp.setCodigo(-6);
				resp.setData(null);
				resp.setMensaje("El código de promoción no es válido. Para invertir, pruebe con otro código");
				return resp;
			}
			boolean mismoCodigo = sodao.validarCodigoMismoCliente(req.getSolicitante_id(), req.getCodigo_promocion()) > 0;
			//System.out.println("mismoCodigo :: " + mismoCodigo);
			if(mismoCodigo){
				resp.setCodigo(-5);
				resp.setData(null);
				resp.setMensaje("Este código pertenece a tu cuenta. Para invertir, cambia el código o quítalo");
				return resp;
			}
			boolean codigoYaUsado = sodao.validarCodigoUtilizado(req.getCodigo_promocion(),req.getSolicitante_id());
			//System.out.println("codigoYaUsado :: " + codigoYaUsado);
			if(codigoYaUsado){
				resp.setCodigo(-4);
				resp.setData(null);
				resp.setMensaje("Este código ya ha sido utilizado. Para invertir, cambia el código o quítalo");
				return resp;
			}
		}
		return resp;
	}
	private String generaCuentaClabe(String referencia, int ahorroId, String sucursalApertura) {
		try {
			GenerarClabeLogic generaClabe = new GenerarClabeLogic();
			String cuentaClabe = "";
			cuentaClabe = generaClabe.generarClabe(referencia,
					ahorroId, sucursalApertura);
			return cuentaClabe;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error al generar la clabe de la cuenta");
			return null;
		}
	}
	
	private BigDecimal obtenerSaldo(String cuenta) {
		Double saldo = 0.0;
		String cuentaClabe;
		MiDebitoLogic midebitologic = new MiDebitoLogic();
		try {
			//cuentaClabe = ahdao.cuentaClabe(cuenta);
			AhorroCuentaMiDebitoOBJ miDebito = new AhorroCuentaMiDebitoOBJ();
			miDebito = acmdao.buscarMiDebitoByCuenta(cuenta,
					ConceptosUtil.CLAVE_CONCEPTO_DATOS_CUENTA, ConceptosUtil.ESTATUS_CONCEPTO_DATOS_CUENTA);
			saldo = midebitologic.consultaSaldoAhorroMiDebito(miDebito);
		}catch(Exception e) {
			log.error("Error al consultar el saldo");
			e.printStackTrace();
		}
		return new BigDecimal(saldo.doubleValue());
	}
	
}


