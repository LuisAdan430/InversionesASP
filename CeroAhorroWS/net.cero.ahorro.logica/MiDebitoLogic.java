package net.cero.ahorro.logica;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.cero.data.AhorroCuentaMiDebitoOBJ;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.EntradaConsultarTarjeta;
import net.cero.data.HeaderWS;
import net.cero.data.Respuesta;
import net.cero.data.RespuestaSVC;
import net.cero.data.WSRespuestaConsultarTarjeta;
import net.cero.plastico.data.DatosPlasticoOBJ;
import net.cero.plastico.data.DatosPlasticoREQ;
import net.cero.plastico.obtenersaldo.ObtenerSaldoIfz;
import net.cero.plastico.obtenersaldo.ObtenerSaldoIfzService;
import net.cero.plastico.obtenersaldo.ObtenerSaldoRequest;
import net.cero.plastico.obtenersaldo.ObtenerSaldoResponse;
import net.cero.seguridad.utilidades.ConceptosUtilBanca;
//import net.cero.plasticos.data.RespuestaSVC;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.TransaccionesDAO;
import net.cero.ws.data.PlaHeaderWS;
import net.cero.ws.data.RespuestaXML;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Logica de negocio para las operaciones con Mi Debito.
 * @author Israel
 * @version 1.0 09/24/18 
 */
public class MiDebitoLogic {
	private static Apps apps = null;
	private static final Logger log = LogManager.getLogger(MiDebitoLogic.class);
	private static final String FECHA_FORMATO = "yyyy-MM-dd"; 
	private static TransaccionesDAO dao = null;
	private static Gson gson = null;
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			dao = (TransaccionesDAO) s.getApplicationContext().getBean("TransaccionesDAO");
			gson = new Gson();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	private Double consultaSaldoAhorroMiDebitoConPlastico(String plastico){
		Double saldoDisponible = 0.00;
		try {
			URL wsdl = null;
			try {
				wsdl = new URL(ObtenerSaldoIfzService.WSDL_LOCATION.toString().replace("localhost",  ConstantesUtil.SERVICIO_BASE_PLASTICOS));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				log.error("Error " + " [ Mi Debito Logic ] " + " [ Consultar URL obtener Saldo Ifz Service ] ");
			}
			ObtenerSaldoIfzService service1 = new ObtenerSaldoIfzService(wsdl);
			ObtenerSaldoIfz port1 = service1.getObtenerSaldoIfzPort();

			PlaHeaderWS header = new PlaHeaderWS();
			header.setIdEmpresa(1L);
			header.setIdCanalAtencion(2L);
			header.setIdSucursal(1L);
			header.setIdUsuario(9L);

			ObtenerSaldoRequest req = new ObtenerSaldoRequest();
			DatosPlasticoREQ pla = new DatosPlasticoREQ();
			pla.setPlastico(plastico);

			req.setHeader(header);
			req.setDatosPlasticoREQ(pla);

			ObtenerSaldoResponse response = port1.procesar(req);
			RespuestaXML respuesta = response.getReturn();

			if(response.getReturn().getErrores().getCodigoError() == 0){
				if(gson == null) initialized();

				DatosPlasticoOBJ obj = (DatosPlasticoOBJ)gson.fromJson(respuesta.getBody().obtenerParametro("DATOS_PLASTICO_OBJ"), DatosPlasticoOBJ.class);
				String resultado = (String)respuesta.getBody().obtenerParametro("RESULTADO");
				String respuestaXML = (String)respuesta.getBody().obtenerParametro("RESPUESTA_XML");

				if(obj != null) {
					System.out.println(obj.getCodigo());
					System.out.println(obj.getDescripcion());
					System.out.println(obj.getMontoDisponible());
					saldoDisponible = obj.getMontoDisponible();
				}

				System.out.println(resultado);
				System.out.println(respuestaXML);
			}else{
				System.out.println("Codigo Error: " + response.getReturn().getErrores().getCodigoError());
				System.out.println("       Error: " + response.getReturn().getErrores().getDescError());
				
				///////////////temporal para pruebas
			}
		}catch(Exception e) {
			log.error("Error " + " [ Mi Debito Logic ] " + " [ Consulta Saldo Ahorro Mi Debito Con Plastico ] ");
		}
		return saldoDisponible;
	}
	public Double consultaSaldoAhorroMiDebito(AhorroCuentaMiDebitoOBJ miDebito){
		initialized();
		Double saldoDisponible = 0.00;
		//NCProveedorTarjeta datosNC;
		try {
			if(miDebito.getTienePlastico().equals("SI")) {
				//datosNC = dao.consultaPlasticoProveedor(miDebito.getCuenta(), miDebito.getPan());
				switch(miDebito.getProveedor()) {
					case "SISCOOP": //return 0.0;
						saldoDisponible = consultaSaldoAhorroMiDebitoConPlastico(miDebito.getPan());
					break;
					case "MASTERCARD-CACAO": saldoDisponible = consultaSaldoAhorroMiDebitoConPlasticoCacao(miDebito.getPan(), miDebito.getTipo_tarjeta());
					break;
				}
				
			}else {
				saldoDisponible = consultaSaldoAhorroMiDebitoSinPlastico(miDebito.getCuenta());
			}
		}catch(Exception e) {
			log.error("Error " + " [ Mi Debito Logic ] " + " [ Consulta Saldo Ahorro Mi Debito ] ");
		}
		return saldoDisponible;
	}
	public Double consultaSaldoAhorroMiDebitoSinPlastico(String cuenta){
		Double saldoDisponible = 0.00;
		try {
			if(dao == null) initialized();
			RespuestaSVC respuestaSvc = dao.leerSaldoActualByCuentaDao(cuenta);
			if(respuestaSvc != null) {
				if(respuestaSvc.getBody().getValor("MONTO_DISPONIBLE") != null) {
					String saldo = (String) respuestaSvc.getBody().getValor("MONTO_DISPONIBLE");
					saldoDisponible = Double.parseDouble(saldo);
				}
			}
			
		}catch(Exception e) {
			log.error("Error " + " [ Mi Debito Logic ] " + " [ Consulta Saldo Ahorro Mi Debito Sin Plastico ] ");

		}
		return saldoDisponible;
	}
	public Double consultaSaldoAhorroMiDebito(String plastico){
		Double saldoDisponible = 0.00;
		try {
			URL wsdl = null;
			try {
				wsdl = new URL(ObtenerSaldoIfzService.WSDL_LOCATION.toString().replace("localhost", ""));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				log.error("Error " + " [ Mi Debito Logic ] " + " [ Obtener Saldo Ifz Service ] ");
			}
			ObtenerSaldoIfzService service1 = new ObtenerSaldoIfzService(wsdl);
			ObtenerSaldoIfz port1 = service1.getObtenerSaldoIfzPort();

			PlaHeaderWS header = new PlaHeaderWS();
			header.setIdEmpresa(1L);
			header.setIdCanalAtencion(2L);
			header.setIdSucursal(1L);
			header.setIdUsuario(9L);

			ObtenerSaldoRequest req = new ObtenerSaldoRequest();
			DatosPlasticoREQ pla = new DatosPlasticoREQ();
			pla.setPlastico(plastico);

			req.setHeader(header);
			req.setDatosPlasticoREQ(pla);

			ObtenerSaldoResponse response = port1.procesar(req);
			net.cero.ws.data.RespuestaXML respuesta = response.getReturn();

			if(response.getReturn().getErrores().getCodigoError() == 0){
				Gson gson = new Gson();

				DatosPlasticoOBJ obj = (DatosPlasticoOBJ)gson.fromJson(respuesta.getBody().obtenerParametro("DATOS_PLASTICO_OBJ"), DatosPlasticoOBJ.class);
				String resultado = (String)respuesta.getBody().obtenerParametro("RESULTADO");
				String respuestaXML = (String)respuesta.getBody().obtenerParametro("RESPUESTA_XML");

				if(obj != null) {
					System.out.println(obj.getCodigo());
					System.out.println(obj.getDescripcion());
					System.out.println(obj.getMontoDisponible());
					saldoDisponible = obj.getMontoDisponible();
				}

				System.out.println(resultado);
				System.out.println(respuestaXML);
			}else{
				System.out.println("Codigo Error: " + response.getReturn().getErrores().getCodigoError());
				System.out.println("       Error: " + response.getReturn().getErrores().getDescError());
				
				///////////////temporal para pruebas
			}
		}catch(Exception e) {
			log.error("Error " + " [ Mi Debito Logic ] " + " [ Consulta Saldo Ahorro Mi Debito ] ");
		}
		return saldoDisponible;
	}
	
	public Respuesta depositoMiDebito(String cuenta, Double monto, String observaciones,String uuidGlobal) {
		String result = "";
		Respuesta resp = new Respuesta();
		
		String HOST = ConstantesUtil.SWITCHER_WS + "/depositarAhorro";

		log.info("{}: depositarAhorro ws:{}",uuidGlobal,HOST);
		Gson gson = new Gson();
		String auth = Credentials.basic(ConstantesUtil.USER, ConstantesUtil.PASSWORD);
		MediaType media = MediaType.parse("application/json; charset=utf-8");

		Map<String, Object> map = new HashMap<>();

		map.put("cuenta", cuenta);
		map.put("tipoTransaccionID", "25");
		map.put("tipoClave", "DEP_TRASPASO");
		map.put("descripcion", observaciones);
		map.put("monto", String.valueOf(monto));
		map.put("estatusID", "1");
		map.put("formaPagoID", String.valueOf(ConceptosUtilBanca.FORMA_APGO_DEP_TRANSFERENCIA_AHO));
		map.put("conciliado", "N");
		map.put("fecha", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		map.put("banco_id", String.valueOf(ConceptosUtilBanca.BANCO_DEP_TRANSFERENCIA_AHORRO));
		map.put("usuarioID", ConstantesUtil.USUARIO_SISTEMAS);
		map.put("host", ConstantesUtil.HOST);
		map.put("canalID", ConstantesUtil.CANAL);
		map.put("sucursalID", "1");

		String body = gson.toJson(map);

		log.info("{}: RETIRO BODY:: {}",uuidGlobal,body);

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(HOST).post(okhttp3.RequestBody.create(media, body))
				.header("Authorization", auth).build();

		try {
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				Respuesta rsp = gson.fromJson(response.body().string(), Respuesta.class);
				log.info("{}: RETIRO RESPONSE:: {}",uuidGlobal,rsp);
				if (rsp.getCodigo() != 0) {
					return rsp;
				} else {
					RespuestaSVC rsp2 = gson.fromJson(rsp.getData(), RespuestaSVC.class);

					AhorroTransferenciaOBJ obj = new AhorroTransferenciaOBJ();
					obj.setDepositoId(Integer.parseInt(rsp2.getBody().getValor("ID_MOVIMIENTO").toString()));
					obj.setCuentaDestino(cuenta);
					obj.setCreadoPor(Integer.parseInt(ConstantesUtil.USUARIO_SISTEMAS));
					obj.setFecha(new Date());
					obj.setMonto(monto);
					resp.setData(gson.toJson(obj));
				}
			} else {
				resp.setCodigo(response.code());
				resp.setMensaje(response.message());

				log.info("{}: Termina depositarAhorro, resp {}",uuidGlobal,resp);

				return resp;
			}

		} catch (IOException e) {
			log.error(e);
			resp.setCodigo(-1);
			resp.setMensaje(e.getMessage());
			log.info("{}: Termina depositarAhorro, resp {}",uuidGlobal,resp);
			return resp;
		}

		log.info("{}: Termina depositarAhorro, resp {}",uuidGlobal,resp);
		return resp;
	}
	
	public Respuesta retirarMiDebito(String cuenta, Double monto, String observaciones, String uuidGlobal) {
		String result = "";
		Respuesta resp = new Respuesta();

		//String HOST = ConstantesUtil.SWITCHER_WS + "/retirarAhorro";
		String HOST = "http://localhost:8080/CEROAhorroWS/rest/retirarAhorro";
		//log.info("{}: retirarMiDebito ws:{}",uuidGlobal,HOST);
		Gson gson = new Gson();
		String auth = Credentials.basic(ConstantesUtil.USER, ConstantesUtil.PASSWORD);
		MediaType media = MediaType.parse("application/json; charset=utf-8");

		Map<String, Object> map = new HashMap<>();
		map.put("cuenta", cuenta);
		map.put("tipoTransaccionID", "24");
		map.put("tipoClave", "RET_TRASPASO");
		map.put("descripcion", observaciones);
		map.put("monto", String.valueOf(monto));
		map.put("estatusID", "1");
		map.put("formaPagoID", String.valueOf(ConceptosUtilBanca.FORMA_APGO_DISP_TRANSFERENCIA_AHO));
		map.put("conciliado", "N");
		map.put("fecha", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		map.put("banco_id", String.valueOf(ConceptosUtilBanca.BANCO_DISP_TRANSFERENCIA_AHORRO));
		map.put("usuarioID", ConstantesUtil.USUARIO_SISTEMAS);
		map.put("host", ConstantesUtil.HOST);
		map.put("canalID", ConstantesUtil.CANAL);
		map.put("sucursalID", "1");

		String body = gson.toJson(map);
		
		//log.info("{}: RETIRO BODY:: {}",uuidGlobal,body);
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(HOST).post(okhttp3.RequestBody.create(media, body))
				.header("Authorization", auth).build();

		try {
			Response response = client.newCall(request).execute();
			Respuesta rsp = gson.fromJson(response.body().string(), Respuesta.class);
			log.info("{}: RETIRO RESPONSE:: {}",uuidGlobal,rsp);
			if (response.isSuccessful()) {
				if (rsp.getCodigo() == 0) {
					RespuestaSVC rsp2 = gson.fromJson(rsp.getData(), RespuestaSVC.class);
					AhorroTransferenciaOBJ obj = new AhorroTransferenciaOBJ();
					obj.setDisposicionId(Integer.parseInt(rsp2.getBody().getValor("ID_MOVIMIENTO").toString()));
					obj.setCuentaOrigen(cuenta);
					obj.setCreadoPor(Integer.parseInt(ConstantesUtil.USUARIO_SISTEMAS));
					obj.setFecha(new Date());
					obj.setMonto(monto);
					resp.setData(gson.toJson(obj));

					log.info("{}: Termina retirarMiDebito, resp {}",uuidGlobal,resp);
					return resp;
				} else {
					RespuestaSVC rsp2 = gson.fromJson(rsp.getData(), RespuestaSVC.class);
					resp.setData(gson.toJson(rsp2.getBody()));

				}
			} else {
				resp.setCodigo(response.code());
				resp.setMensaje(response.message());

				log.info("{}: Termina retirarMiDebito, resp {}",uuidGlobal,resp);
				
				return resp;
			}

		} catch (IOException e) {
			//log.error("El error es: " + e.getMessage(),e);
			//e.printStackTrace();
			resp.setCodigo(-1);
			resp.setMensaje(e.getMessage());
			return resp;
		}

		resp.setCodigo(1);
		resp.setMensaje("Error al realizar el registro");
		log.info("{}: Termina retirarMiDebito, resp {}",uuidGlobal,resp);
		return resp;
	}
	private Double consultaSaldoAhorroMiDebitoConPlasticoCacao(String tarjeta, String tipoTarjeta) {
		double saldo = 0.0;
		Gson gson = new Gson();
		HeaderWS header = new HeaderWS();
		EntradaConsultarTarjeta entrada = new EntradaConsultarTarjeta();
		header.setIdEmpresa(1L);
		header.setIdCanalAtencion(2L);
		header.setIdSucursal(1L);
		header.setIdUsuario(9L);
		header.setIpHost(ConstantesUtil.consultaSaldoIp);
		header.setIdTransaccion(2);
		entrada.setHeader(header);
		entrada.setTipoTarjeta(tipoTarjeta);
		entrada.setNumeroTarjeta(tarjeta);
		entrada.setToken("");
		MediaType media = MediaType.parse("application/json; charset=utf-8");
		OkHttpClient cliente = new OkHttpClient();
		String auth = Credentials.basic(ConstantesUtil.USER, ConstantesUtil.PASSWORD);
		String url =ConstantesUtil.WS_ADMIN_PLA + "/consultar-tarjeta";
		String body = gson.toJson(entrada);
		Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
		try {
			Response response = cliente.newCall(request).execute();
			String json = response.body().string();
			log.debug("Respuesta de consultar-tarjeta: " + json);
			Respuesta respuesta = gson.fromJson(json, Respuesta.class);
			if (respuesta.getCodigo() == 0) {
				WSRespuestaConsultarTarjeta datos = gson.fromJson(respuesta.getData(), WSRespuestaConsultarTarjeta.class);
		        for(int i=0; i < datos.getSaldoActual().size(); i++){
		            if(datos.getSaldoActual().get(i).getDescripcionTipoCuenta().equals("Cuenta Autorización")){
		                saldo = Double.parseDouble(datos.getSaldoActual().get(i).getSaldo());
		                i = datos.getSaldoActual().size();
		            }
		        }
			}else {
				return 0.0;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Error " + " [ Mi Debito Logic ] " + " [ Consulta Saldo Ahorro Mi Debito Con Plastico Cacao ] ");
		}
		
		return saldo;
	}
}
