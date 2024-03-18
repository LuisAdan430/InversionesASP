/*
 * @(#)DepositoAhorro.java 1.0 04/07/18 
 * 
 */
package net.cero.ahorro.logica;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import net.cero.ahorro.abstracts.InvokeLogsWsJob;
import net.cero.ahorro.ws.util.LogWsUtil;
import net.cero.spring.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.InitializationException;
import org.springframework.beans.BeansException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroContratoOBJ;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.AhorroTransferenciaReqOBJ;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.CajaDisposicionAhorroReq;
import net.cero.data.Respuesta;
import net.cero.data.TransaccionTarjetaOrquestadorOBJ;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.seguridad.utilidades.ConceptosUtilBanca;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.HeaderWS;
import net.cero.spring.config.Apps;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Logica de negocio para registrar un deposito de ahorro.
 * @author Israel
 * @version 1.0 04/07/18
 */
public class AhorroTransferenciaLogic extends InvokeLogsWsJob {
	private static final Logger log = LogManager.getLogger(AhorroTransferenciaLogic.class);
	
	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static AhorroDepositoDAO addao;
	private static AhorroSaldosDAO asdao;
	//private static AhorroIdeValoresDAO avdao;	
	private static AhorroRendimientoVigenteDAO vdao;
	private static TransaccionesDAO tdao;
	private static AhorroTransferenciasDAO atdao;
	private static CajaDepositoAhorroLogic cajaDepositoAhorroLogic;
	private static CajaDisposicionAhorroLogic cajaDisposicionAhorroLogic;
	private static ObjectMapper objectMapper;
	private static MiDebitoLogic miDebitoLogic;

	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			pdao = (AhorroPagareDAO) s.getApplicationContext().getBean("AhorroPagareDAO");
			mdao = (AhorroMovimientosDAO) s.getApplicationContext().getBean("AhorroMovimientosDAO");
			addao = (AhorroDepositoDAO) s.getApplicationContext().getBean("AhorroDepositoDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			//avdao = (AhorroIdeValoresDAO) s.getApplicationContext().getBean("AhorroIdeValoresDAO");
			vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
			atdao = (AhorroTransferenciasDAO) s.getApplicationContext().getBean("AhorroTransferenciasDAO");
			tdao = (TransaccionesDAO) s.getApplicationContext().getBean("TransaccionesDAO");
			cajaDepositoAhorroLogic = new CajaDepositoAhorroLogic();
			cajaDisposicionAhorroLogic = new CajaDisposicionAhorroLogic();
			miDebitoLogic = new MiDebitoLogic();
			objectMapper = new ObjectMapper();
		} catch (BeansException e) {
	        log.error("Error al inicializar la aplicación: " + e.getMessage());
	        throw new InitializationException("Error al inicializar la aplicación", e);
	    }
	}
	
	public Respuesta ahorroTransferencia(AhorroTransferenciaReqOBJ req){
		initialized();
		Respuesta respuesta = new Respuesta();
		String uuid = UUID.randomUUID().toString();
		respuesta = procesaTransferencia(req.getHeader(),req,uuid);
		return respuesta;
	}
	public Respuesta procesaTransferencia(HeaderWS header, AhorroTransferenciaReqOBJ req, String uuidGlobal){
		//log.info("Procesa Transferencia");
		initialized();
		Respuesta resp = new Respuesta();
		resp.setCodigo(0);
		Respuesta resultTransferenciaDeposito = new Respuesta();
		Respuesta resultTransferenciaDisposicion = new Respuesta();

		//log.info("{}: Inicia proceso de transferencia",uuidGlobal);
		
			
			if(req.getTipoCuentaOrigen() == null) {
				resp.setCodigo(-2);
				resp.setMensaje("No se pudo procesar el pago, no se pudo obtener informacion de la cuenta origen");
				resp.setData("");
				return resp;
			}else if(req.getTipoCuentaOrigen().isEmpty()) {
				resp.setCodigo(-3);
				resp.setMensaje("No se pudo procesar el pago, no se pudo obtener informacion de la cuenta origen");
				resp.setData("");
				return resp;
			}
			
			if(req.getTipoCuentaDestino() == null) {
				resp.setCodigo(-4);
				resp.setMensaje("No se pudo procesar el pago, no se pudo obtener informacion de la cuenta destino");
				resp.setData("");
				return resp;
			}else if(req.getTipoCuentaDestino().isEmpty()) {
				resp.setCodigo(-5);
				resp.setMensaje("No se pudo procesar el pago, no se pudo obtener informacion de la cuenta destino");
				resp.setData("");
				return resp;
			}
			//log.info("{}:: Antes de realizar la transaccion de disposición de la cuenta origen",uuidGlobal);
			MiDebitoLogic logic = new MiDebitoLogic();
			if(req.getTipoCuentaOrigen().equals("AHORRO")) {
				/*
				log.info("ProcesaDisposicionTransferenciaProcrea");
				log.info(":::: " + req.toString() + ":::" + uuidGlobal);
				*/
				resultTransferenciaDisposicion = procesaDisposicionTransferenciaProcrea(req,uuidGlobal);
				
				
			}else {
				//log.info("Retirar Mi debito");
				/*log.info(" :::::" + req.getCuentaOrigen() 
						+ "::::: " + req.getMonto() 
						+ "::::: " + req.getConceptoOrigen() 
						+ "::::: " + uuidGlobal);*/
				resultTransferenciaDisposicion = logic.retirarMiDebito(req.getCuentaOrigen(),req.getMonto(),req.getConceptoOrigen(),uuidGlobal);
			}
			//log.info("{}:: Despues de realizar la transaccion de disposición de la cuenta origen",uuidGlobal);
			//log.info("{}:: Antes de realizar la transaccion de deposito de la cuenta destino",uuidGlobal);
			
			log.info(resultTransferenciaDisposicion.toString());
			
			
			if(resultTransferenciaDeposito.getCodigo() == 0) {
				if(req.getTipoCuentaDestino().equals("AHORRO")) {
					
					resultTransferenciaDeposito = procesaDepositoTransferenciaProcrea(req,uuidGlobal);
				}else {
					
					resultTransferenciaDeposito = logic.depositoMiDebito(req.getCuentaDestino(),req.getMonto(),req.getConceptoDestino(),uuidGlobal);
				}
			}else {
				return resultTransferenciaDeposito;
			}
			
			
			log.info("{}:: Despues de realizar la transaccion de deposito de la cuenta destino",uuidGlobal);
			
			if(resultTransferenciaDeposito!=null && resultTransferenciaDeposito.getCodigo()!=0){
				resp.setCodigo(resultTransferenciaDeposito.getCodigo());
				resp.setMensaje(resultTransferenciaDeposito.getMensaje());
			}
			
			if(req.getTipoCuentaOrigen().equals("AHORRO") || req.getTipoCuentaDestino().equals("AHORRO")) {
				log.info("******** REGISTRA AHORRO TRANSFERENCIA *********");
				log.info("## resultTransferenciaDeposito :: " + resultTransferenciaDeposito.getData());
				log.info("## resultTransferenciaDisposicion :: " + resultTransferenciaDisposicion.getData());
				registraAhorroTransferencia(resultTransferenciaDeposito.getData(), resultTransferenciaDisposicion.getData());
			}else {
				
			}
		
		log.info("{}: Finaliza proceso de transferencia",uuidGlobal);
		
		
		
		
		return resp;
	}
	public Respuesta procesaDepositoTransferenciaProcrea(AhorroTransferenciaReqOBJ req, String uuidGlonbal){
		Respuesta resultTransferenciaDeposito = new Respuesta();
		CajaDepositoAhorroReq cajaDepositoAhorroReq = new CajaDepositoAhorroReq();
		String uuidIndividual = UUID.randomUUID().toString();
		
			AhorroContratoOBJ ahorroContrato = new AhorroContratoOBJ();
			ahorroContrato = adao.buscaByCuenta(req.getCuentaDestino());
			
			//log.info(ahorroContrato.toString());
			
			cajaDepositoAhorroReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setFecha(req.getFecha());
			cajaDepositoAhorroReq.setCuentaAhorro(req.getCuentaDestino());
			cajaDepositoAhorroReq.setMonto(req.getMonto());
			cajaDepositoAhorroReq.setFormaPago(ConceptosUtil.FORMA_APGO_DEP_TRANSFERENCIA_AHO);
			cajaDepositoAhorroReq.setBancoId(null);
			cajaDepositoAhorroReq.setObservacion(req.getConceptoDestino());
			cajaDepositoAhorroReq.setCheque("");
			cajaDepositoAhorroReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setTransaccionId(null);
			cajaDepositoAhorroReq.setTarjetaOperativaId(null);
			cajaDepositoAhorroReq.setApp(null);
			cajaDepositoAhorroReq.setTransaccionVersionId(null);
			cajaDepositoAhorroReq.setParaConciliar(1);
			cajaDepositoAhorroReq.setFechaDeposito(Calendar.getInstance().getTime());
			cajaDepositoAhorroReq.setUsuarioId(Integer.parseInt(ConstantesUtil.USUARIO_SISTEMAS));
			cajaDepositoAhorroReq.setControl("");
			cajaDepositoAhorroReq.setSucursalId(ahorroContrato.getSucursalId());
			//System.out.println("******** REGISTTRA DEPOSITO *********");
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().inicializaRegistroBitacora(uuidIndividual,req.getCuentaOrigen(),BitacoraUtil.REALIZA_DEPOSITO_TRANSFERENCIA);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			//log.info(cajaDepositoAhorroReq.toString());
			resultTransferenciaDeposito = cajaDepositoAhorroLogic.registrarDeposito(cajaDepositoAhorroReq);
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().finalizaRegistroBitacora(uuidIndividual,uuidGlonbal);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            
		
		
		return resultTransferenciaDeposito;
	}
	public Respuesta procesaTransferencia(AhorroTransferenciaReqOBJ req){
		initialized();
		String uuid = UUID.randomUUID().toString();
		log.info("{}: Inicia proceso de transferencia {}",uuid,req);
		Respuesta resp = new Respuesta();
		resp.setCodigo(0);
		CajaDepositoAhorroReq cajaDepositoAhorroReq = new CajaDepositoAhorroReq();
		
		CajaDisposicionAhorroReq cajaDisposicionReq = new CajaDisposicionAhorroReq();
		
			AhorroContrato ahorroContrato = adao.buscarByCuenta(req.getCuentaDestino());
			
			cajaDepositoAhorroReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setFecha(req.getFecha());
			cajaDepositoAhorroReq.setCuentaAhorro(req.getCuentaDestino());
			cajaDepositoAhorroReq.setMonto(req.getMonto());
			cajaDepositoAhorroReq.setFormaPago(ConceptosUtil.FORMA_APGO_DEP_TRANSFERENCIA_AHO);
			cajaDepositoAhorroReq.setBancoId(ConceptosUtil.BANCO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setObservacion(req.getConceptoDestino());
			cajaDepositoAhorroReq.setCheque("");
			cajaDepositoAhorroReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setTransaccionId(null);
			cajaDepositoAhorroReq.setTarjetaOperativaId(null);
			cajaDepositoAhorroReq.setApp(null);
			cajaDepositoAhorroReq.setTransaccionVersionId(null);
			cajaDepositoAhorroReq.setParaConciliar(1);
			cajaDepositoAhorroReq.setFechaDeposito(Calendar.getInstance().getTime());
			cajaDepositoAhorroReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
			cajaDepositoAhorroReq.setControl("");
			cajaDepositoAhorroReq.setSucursalId(ahorroContrato.getSucursalId());
			log.info("{}: Datos del cajaDepositoAhorroReq: {} ",uuid,cajaDepositoAhorroReq);
			//log.info("******** REGISTTRA DEPOSITO *********");
			

			log.info("{}: Inicia proceso de registro de deposito",uuid);
			String resultTransferenciaDeposito = cajaDepositoAhorroLogic.registrarDeposito(cajaDepositoAhorroReq).getData();
			log.info("{}: Finaliza proceso de registro de deposito",uuid);

			cajaDisposicionReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setFecha(req.getFecha());
			cajaDisposicionReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setCuentaAhorro(req.getCuentaOrigen());
			cajaDisposicionReq.setMonto(req.getMonto());
			cajaDisposicionReq.setFormaPago(ConceptosUtil.FORMA_APGO_DISP_TRANSFERENCIA_AHO);
			cajaDisposicionReq.setBancoId(ConceptosUtil.BANCO_DISP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setObservacion(req.getConceptoOrigen());
			cajaDisposicionReq.setCheque("");
			cajaDisposicionReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DISP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setTransaccionId(null);
			cajaDisposicionReq.setTarjetaOperativaId(null);
			cajaDisposicionReq.setApp(null);
			cajaDisposicionReq.setTransaccionVersionId(null);
			cajaDisposicionReq.setBancoClie(null);
			cajaDisposicionReq.setAvisoId(null);
			cajaDisposicionReq.setSpeiTransferenciaId(null);
			log.info("{}: Datos del cajaDisposicionReq: {} ",uuid,cajaDisposicionReq);

			log.info("{}: Inicia proceso de registro de disposicion",uuid);
			String resultTransferenciaDisposicion = cajaDisposicionAhorroLogic.disposicionAhorro(cajaDisposicionReq).getData();
			log.info("{}: Fin proceso de registro de disposicion",uuid);

			
			log.info("******** REGISTRA AHORRO TRANSFERENCIA *********");
			log.info("## resultTransferenciaDeposito :: " + resultTransferenciaDeposito);
			log.info("## resultTransferenciaDisposicion :: " + resultTransferenciaDisposicion);
			log.info("{}: Inicia registro de datos del ahorroTransacciones",uuid);
			registraAhorroTransferencia(resultTransferenciaDeposito, resultTransferenciaDisposicion);
			log.info("{}: Fin registro de datos del ahorroTransacciones",uuid);
		
		
		return resp;
	}
	public Respuesta procesaDepositoTransferenciaMiDebito(HeaderWS header, AhorroTransferenciaReqOBJ req, String uuidGlobal) throws IOException{
		Respuesta resultTransferenciaDeposito = new Respuesta();
		String uuidIndividual = UUID.randomUUID().toString();
		
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().inicializaRegistroBitacora(uuidIndividual,req.getCuentaOrigen(),BitacoraUtil.REALIZA_DEPOSITO_TRANSFERENCIA);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			TransaccionTarjetaOrquestadorOBJ reqTra = new TransaccionTarjetaOrquestadorOBJ();
			header.setIpHost(ConstantesUtil.HOST);
			header.setIdBanco(String.valueOf(ConceptosUtil.BANCO_DEP_TRANSFERENCIA_AHORRO));
			reqTra.setHeader(header);
			reqTra.setCuenta(req.getCuentaDestino());
			reqTra.setImporte(String.valueOf(req.getMonto()));
			reqTra.setMedioPago(String.valueOf(ConceptosUtilBanca.FORMA_APGO_DEP_TRANSFERENCIA_AHO));
			reqTra.setConcepto(req.getConceptoDestino());
			reqTra.setReferenciaNumerica(generaReferencia());
			MediaType media = MediaType.parse("application/json; charset=utf-8");
			OkHttpClient cliente = new OkHttpClient();
			String auth = Credentials.basic(ConstantesUtil.USER, ConstantesUtil.PASSWORD);
			String url = ConstantesUtil.WS_ADMIN_PLA + "/fondearTarjetaOrquestador";
			String body = objectMapper.writeValueAsString(reqTra);
			Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
			Response response = cliente.newCall(request).execute();
			
			resultTransferenciaDeposito = objectMapper.readValue(response.body().string(), Respuesta.class);
		//	resultTransferenciaDeposito = miDebitoLogic.depositoMiDebito(req.getCuentaDestino(),req.getMonto());
			
			/*resultTransferenciaDeposito.setCodigo(0);
			resultTransferenciaDeposito.setMensaje("OK");*/
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().finalizaRegistroBitacora(uuidIndividual,uuidGlobal);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            
		
		
		return resultTransferenciaDeposito;
	}

	
	public Respuesta procesaDisposicionTransferenciaProcrea(AhorroTransferenciaReqOBJ req, String uuidGlobal){
		String resultTransferenciaDisposicion = "";
		Respuesta resp = new Respuesta();
		CajaDisposicionAhorroReq cajaDisposicionReq = new CajaDisposicionAhorroReq();
		String uuidIndividual = UUID.randomUUID().toString();
		
			cajaDisposicionReq.setCajaId(ConceptosUtil.CAJA_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setFecha(req.getFecha());
			cajaDisposicionReq.setUsuarioId(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setCuentaAhorro(req.getCuentaOrigen());
			cajaDisposicionReq.setMonto(req.getMonto());
			cajaDisposicionReq.setFormaPago(ConceptosUtil.FORMA_APGO_DISP_TRANSFERENCIA_AHO);
			cajaDisposicionReq.setBancoId(null);
			cajaDisposicionReq.setObservacion(req.getConceptoOrigen());
			cajaDisposicionReq.setCheque("");
			cajaDisposicionReq.setMovtoId(ConceptosUtil.MOVIMIENTO_DISP_TRANSFERENCIA_AHORRO);
			cajaDisposicionReq.setTransaccionId(null);
			cajaDisposicionReq.setTarjetaOperativaId(null);
			cajaDisposicionReq.setApp(null);
			cajaDisposicionReq.setTransaccionVersionId(null);
			cajaDisposicionReq.setBancoClie(null);
			cajaDisposicionReq.setAvisoId(null);
			cajaDisposicionReq.setSpeiTransferenciaId(null);
			
			//log.info(":::" + cajaDisposicionReq.toString());
			resp = cajaDisposicionAhorroLogic.disposicionAhorro(cajaDisposicionReq);
			//log.info(resp);

			
		
		
		return resp;
	}
	
	public Respuesta procesaDisposicionTransferenciaMiDebito(HeaderWS header, AhorroTransferenciaReqOBJ req, String uuidGlobal) throws IOException{
		Respuesta resultTransferenciaDisposicion = new Respuesta();
		String uuidIndividual = UUID.randomUUID().toString();
		
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().inicializaRegistroBitacora(uuidIndividual,req.getCuentaDestino(),BitacoraUtil.REALIZA_DISPOSICION_TRANSFERENCIA);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			TransaccionTarjetaOrquestadorOBJ reqTra = new TransaccionTarjetaOrquestadorOBJ();
			header.setIpHost(ConstantesUtil.HOST);
			header.setIdBanco(String.valueOf(ConceptosUtil.BANCO_DISP_TRANSFERENCIA_AHORRO));
			reqTra.setHeader(header);
			reqTra.setCuenta(req.getCuentaOrigen());
			reqTra.setImporte(String.valueOf(req.getMonto()));
			//reqTra.setMedioPago(String.valueOf(ConceptosUtilBanca.FORMA_APGO_DISP_TRANSFERENCIA_AHO));
			reqTra.setMedioPago(ConceptosUtil.MEDIO_PAGO_CACAO);
			reqTra.setConcepto(req.getConceptoOrigen());
			reqTra.setReferenciaNumerica(generaReferencia());
			MediaType media = MediaType.parse("application/json; charset=utf-8");
			OkHttpClient cliente = new OkHttpClient();
			String auth = Credentials.basic(ConstantesUtil.USER, ConstantesUtil.PASSWORD);
			String url = ConstantesUtil.WS_ADMIN_PLA + "/retirarTarjetaOrquestador";
			String body = objectMapper.writeValueAsString(reqTra);
			log.info("Request para retirarTarjetaOrquestador:: " + body);
			Request request = new Request.Builder().header("Authorization", auth).url(url).post(okhttp3.RequestBody.create(media, body)).build();
			String respuesta = cliente.newCall(request).execute().body().string();
			resultTransferenciaDisposicion = objectMapper.readValue(respuesta, Respuesta.class);
			if (Integer.valueOf(resultTransferenciaDisposicion.getCodigo()) == null) {
				return null;
			}
			//resultTransferenciaDisposicion = miDebitoLogic.retirarMiDebito(req.getCuentaOrigen(), req.getMonto());
			log.info("Respuesta de la transaccion:: " + respuesta);

			/*resultTransferenciaDisposicion.setCodigo(0);
			resultTransferenciaDisposicion.setMensaje("OK");*/
			
			/*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
            //BitacoraProcesamiento.getInstance().finalizaRegistroBitacora(uuidIndividual,uuidGlobal);
            /*/////////////////REGISTRO DE BITACORA\\\\\\\\\\\\\\\\\\\\\\\\*/
			
		
		
		return resultTransferenciaDisposicion;
	}
	private void registraAhorroTransferencia(String resultTransferenciaDeposito, String resultTransferenciaDisposicion) {
		try {
			AhorroTransferenciaOBJ movimientoCajaDeposito = new AhorroTransferenciaOBJ();
			AhorroTransferenciaOBJ movimientoCajaDisposicion = new AhorroTransferenciaOBJ();
			AhorroTransferenciaOBJ ahorroTransferencia = new AhorroTransferenciaOBJ();
			if(resultTransferenciaDeposito != null) {
				if(!resultTransferenciaDeposito.isEmpty()) {
					if(resultTransferenciaDisposicion != null) {
						if(!resultTransferenciaDisposicion.isEmpty()){
							String jsonString = resultTransferenciaDeposito;
							
							movimientoCajaDeposito = objectMapper.readValue(jsonString , AhorroTransferenciaOBJ.class);
							movimientoCajaDisposicion = objectMapper.readValue(resultTransferenciaDisposicion, AhorroTransferenciaOBJ.class);
							
							if(movimientoCajaDeposito != null) {
								if(movimientoCajaDisposicion != null) {
									log.info("## LLENA EL REGISTRO DE AHORRO TRANSFERENCIA ");
									
									ahorroTransferencia.setCuentaOrigen(movimientoCajaDisposicion.getCuentaOrigen());
									ahorroTransferencia.setCuentaDestino(movimientoCajaDeposito.getCuentaDestino());
									ahorroTransferencia.setFecha(Calendar.getInstance().getTime());
									ahorroTransferencia.setMonto(movimientoCajaDisposicion.getMonto());
									ahorroTransferencia.setCreadoPor(ConceptosUtil.USUARIO_DEP_TRANSFERENCIA_AHORRO);
									
									ahorroTransferencia.setMovimientoId(movimientoCajaDisposicion.getMovimientoId());
									ahorroTransferencia.setDisposicionId(movimientoCajaDisposicion.getDisposicionId());
									ahorroTransferencia.setDepositoId(movimientoCajaDeposito.getDepositoId());
									
									log.info("## DATOS DE TRANSFERENCIA A INSERTAR :: " + objectMapper.writeValueAsString(ahorroTransferencia));
									ahorroTransferencia.setId(atdao.nuevoAT(ahorroTransferencia));
									if(ahorroTransferencia.getId() > 0) {
										log.info("## TRANSFERENCIA REGISTRADA");
									}
								}
							}
						}							
					}
				}
			}
		}catch(IOException e) {
			log.error("Error " + " [ Ahorro Transferencia Logic ] " + " [ AL REGISTRAR AHORRO TRANSFERENCIA  ] ");
		}
		
	}
	
	private String generaReferencia() {
		Integer numeroReferencia = tdao.referenciaQR();
		String numeroReferenciaTMP = numeroReferencia.toString();
		log.info("numeroReferenciaTMP :: " + numeroReferenciaTMP);
		if(numeroReferenciaTMP.length() <= 6) {
			for(int x=0;numeroReferenciaTMP.length()<6;x++) {
				numeroReferenciaTMP = "0" + numeroReferenciaTMP;
			}
			numeroReferenciaTMP = "1" + numeroReferenciaTMP;
		}
		return numeroReferenciaTMP;
	}
	
}
