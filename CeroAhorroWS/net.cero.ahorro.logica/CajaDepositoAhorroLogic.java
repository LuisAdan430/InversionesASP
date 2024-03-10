package net.cero.ahorro.logica;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroDeposito;
import net.cero.data.AhorroMovimiento;
import net.cero.data.AhorroPagare;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaOBJ;
import net.cero.data.CajaDepositoAhorroReq;
import net.cero.data.MovimientosCaja;
import net.cero.data.Respuesta;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroDepositoDAO;
//import net.cero.spring.dao.AhorroIdeValoresDAO;
import net.cero.spring.dao.AhorroMovimientosDAO;
import net.cero.spring.dao.AhorroPagareDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.MovimientosCajaDAO;



/**
 * Logica de negocio para registrar un deposito de ahorro.
 * @author Israel
 * @version 1.0 04/07/18
 */
public class CajaDepositoAhorroLogic {
	private static final Logger log = LogManager.getLogger(CajaDepositoAhorroLogic.class);
	
	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroPagareDAO pdao;
	private static AhorroMovimientosDAO mdao;
	private static MovimientosCajaDAO mcdao;
	private static AhorroDepositoDAO addao;
	private static AhorroSaldosDAO asdao;
	
	private static AhorroActualizaSaldoLogic ahorroActualizaSaldoL;
	
	private static Gson gson;
	//private static AhorroRendimientoVigenteDAO vdao;
	
	
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
			mcdao = (MovimientosCajaDAO) s.getApplicationContext().getBean("MovimientosCajaDAO");
			addao = (AhorroDepositoDAO) s.getApplicationContext().getBean("AhorroDepositoDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			
			ahorroActualizaSaldoL = new AhorroActualizaSaldoLogic();
			
			gson = new Gson();
			//avdao = (AhorroIdeValoresDAO) s.getApplicationContext().getBean("AhorroIdeValoresDAO");
			//vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Respuesta registrarDeposito(CajaDepositoAhorroReq req){
		initialized();
		Respuesta respuesta = new Respuesta();
		Integer vMovimientoCaja = 0;
		AhorroMovimiento ahorroMovimiento = new AhorroMovimiento();
		AhorroContrato ahorroContrato = new AhorroContrato();
		AhorroDeposito ahorroDeposito = new AhorroDeposito();
		MovimientosCaja movimientoCajaResult = new MovimientosCaja();
		AhorroTransferenciaOBJ ahorroTransferencia = new AhorroTransferenciaOBJ();
		AhorroPagare ahorroPagaresNuevo = new AhorroPagare();
		
		Timestamp vFecha = new Timestamp(System.currentTimeMillis());
		
		log.info(req.toString());
		
		//log.info("Cuenta -> " + req.getCuentaAhorro());
		ahorroContrato = adao.buscarByCuenta(req.getCuentaAhorro());
		//log.info("Json -> " +  gson.toJson(ahorroContrato));
		AhorroPagare ahorroPagares = pdao.buscarByCuenta(req.getCuentaAhorro());
		//log.info(ahorroPagares.toString());
		ahorroMovimiento = mdao.buscarById(req.getMovtoId());
		//log.info(ahorroMovimiento.toString());
		
		
		//El crislog.info("saldo -> " +ahorroContrato.getSaldo());
		if(ahorroContrato.getSaldo() == 0){
			
			ahorroPagaresNuevo.setCuenta(req.getCuentaAhorro());
			
			if(ahorroPagares != null){
				if(ahorroPagares.getNumero() != null){
					ahorroPagaresNuevo.setNumero(ahorroPagares.getNumero() + 1);
				}else{
					ahorroPagaresNuevo.setNumero((long) 1);
				}
			}else{
				ahorroPagaresNuevo.setNumero((long) 1);
			}
			
			ahorroPagaresNuevo.setMonto(req.getMonto());
			ahorroPagaresNuevo.setFechaInicio(req.getFecha());
			ahorroPagaresNuevo.setCreadoPor((int) req.getUsuarioId());
			ahorroPagaresNuevo.setFechaCreacion(vFecha);
			ahorroPagaresNuevo.setModificadoPor((int) req.getUsuarioId());
			ahorroPagaresNuevo.setFechaModificacion(vFecha);
			
			
			ahorroPagaresNuevo.setPagareId(pdao.nuevo(ahorroPagaresNuevo));
		}
		log.info(ahorroPagaresNuevo.toString());
		
		
		vMovimientoCaja = null;
		if(req.getParaConciliar() == 1){
			vMovimientoCaja = mcdao.obtenerMovimientoId(req.getCuentaAhorro(), req.getMonto(), req.getFecha());
		}
		
		if(vMovimientoCaja == null){
			MovimientosCaja movimientoCaja = new MovimientosCaja();
			movimientoCaja.setControl(req.getControl());
			movimientoCaja.setCajaId(req.getCajaId());
			movimientoCaja.setCajeroId((int) req.getUsuarioId());
			movimientoCaja.setFecha(req.getFecha());
			movimientoCaja.setTipoMovId(req.getMovtoId());
			movimientoCaja.setMonedaId(1);
			movimientoCaja.setMonto(req.getMonto());
			movimientoCaja.setCuenta(req.getCuentaAhorro());
			movimientoCaja.setFormaPago(req.getFormaPago());
			movimientoCaja.setObs(req.getObservacion());
			movimientoCaja.setBancoId(req.getBancoId());
			movimientoCaja.setBancoOrigen(req.getBancoId());
			movimientoCaja.setRegionId((int) req.getSucursalId());
			movimientoCaja.setCreadoPor((int) req.getUsuarioId());
			movimientoCaja.setModificadoPor((int) req.getUsuarioId());
			movimientoCaja.setEstatus("C");
			movimientoCaja.setFechaCreacion(vFecha);
			movimientoCaja.setFechaDeposito(vFecha);
			
			movimientoCaja.setMovimientoId(mcdao.nuevo(movimientoCaja));
			movimientoCajaResult = movimientoCaja;
			if(req.getMovtoId() == 2){
				ahorroDeposito.setDepositoId(movimientoCaja.getMovimientoId());
				ahorroDeposito.setCuenta(req.getCuentaAhorro());
				ahorroDeposito.setMonto(req.getMonto());
				ahorroDeposito.setFecha(vFecha);
				ahorroDeposito.setFormaPago(req.getFormaPago());
				ahorroDeposito.setBanco(req.getBancoId());
				ahorroDeposito.setNoCheque(req.getCheque());
				ahorroDeposito.setObservaciones(req.getObservacion());
				ahorroDeposito.setCreadoPor((int) req.getUsuarioId());
				ahorroDeposito.setFechaCreacion(vFecha);
				ahorroDeposito.setModificadoPor((int) req.getUsuarioId());
				ahorroDeposito.setFechaModificacion(vFecha);
			}
			
			if(ahorroMovimiento.getSalvoBuenCobro() == null){
				ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
			}else{
				if(!ahorroMovimiento.getSalvoBuenCobro().equals("S")){
					ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
				}
			}
		}else{
			MovimientosCaja movimientoCajaExistente = new MovimientosCaja();
			movimientoCajaExistente = mcdao.findMovimientoById(vMovimientoCaja);
			movimientoCajaExistente.setEstatus("C");
			movimientoCajaExistente.setBancoId(req.getBancoId());
			movimientoCajaExistente.setBancoOrigen(req.getBancoId());
			movimientoCajaExistente.setFechaDeposito(req.getFechaDeposito());
			movimientoCajaExistente.setModificadoPor((int) req.getUsuarioId());
			mcdao.actualizaMovimiento(movimientoCajaExistente);
			
			movimientoCajaResult = movimientoCajaExistente;
			if(ahorroMovimiento.getSalvoBuenCobro() == null){
				ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
			}else{
				if(!ahorroMovimiento.getSalvoBuenCobro().equals("S")){
					ahorroActualizaSaldoL.ahorroActualizasaldo(req.getFecha(),req.getCuentaAhorro(), req.getMonto(), ahorroMovimiento.getOperacion());
				}
			}
		}
		
		if(req.getMovtoId() != 38){
			Double  vide = ahorroIde(req.getCuentaAhorro(),req.getFecha(),req.getFecha());
			
			if(vide > 0){
				MovimientosCaja movimientoCajaNuevo = new MovimientosCaja();
				movimientoCajaNuevo.setCajaId(req.getCajaId());
				movimientoCajaNuevo.setCajeroId((int) req.getUsuarioId());
				movimientoCajaNuevo.setFecha(vFecha);
				movimientoCajaNuevo.setTipoMovId(14);
				movimientoCajaNuevo.setMonedaId(1);
				movimientoCajaNuevo.setMonto(vide);
				movimientoCajaNuevo.setCuenta(req.getCuentaAhorro());
				movimientoCajaNuevo.setFormaPago(4);
				movimientoCajaNuevo.setCreadoPor((int) req.getUsuarioId());
				movimientoCajaNuevo.setFechaCreacion(vFecha);
				movimientoCajaNuevo.setModificadoPor((int) req.getUsuarioId());
				movimientoCajaNuevo.setFechaModificacion(vFecha);
				movimientoCajaNuevo.setObs("ide");
				movimientoCajaNuevo.setRegionId((int) req.getSucursalId());
				
				movimientoCajaNuevo.setMovimientoId(mcdao.nuevo(movimientoCajaNuevo));
				movimientoCajaResult = movimientoCajaNuevo;
				AhorroSaldos ahorroSaldos = new AhorroSaldos();
				ahorroSaldos = asdao.buscarByCuenta(req.getCuentaAhorro());
				ahorroSaldos.setSaldoReal((ahorroSaldos.getSaldoReal() - vide));
				ahorroSaldos.setSaldoDisponible((ahorroSaldos.getSaldoDisponible() - vide));
				
				asdao.actualizar(ahorroSaldos);
				
				ahorroContrato.setSaldo(ahorroContrato.getSaldo() - vide);
				adao.actualizar(ahorroContrato);
			}
			
		}
		
		ahorroTransferencia.setDepositoId(movimientoCajaResult.getMovimientoId());
		ahorroTransferencia.setCuentaDestino(req.getCuentaAhorro());
		
		respuesta.setCodigo(0);
		respuesta.setMensaje("Deposito registrado");
		respuesta.setData(gson.toJson(ahorroTransferencia));
		return respuesta;
	}

	private Double ahorroIde(String cuenta, Date fechaDesde, Date fechaHasta) {
		Double valor = (double) 0;
		return valor;
	}
	
	
}
