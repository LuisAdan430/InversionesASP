package net.cero.ahorro.logica;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.cero.data.AhorroContrato;
import net.cero.data.AhorroRendimientoVigentes;
import net.cero.data.AhorroSaldos;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroContratoDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.AhorroSaldosDAO;

public class AhorroActualizaSaldoLogic {
	private static final Logger log = LogManager.getLogger(AhorroActualizaSaldoLogic.class);
	
	private static Apps apps = null;
	
	private static AhorroContratoDAO adao;
	private static AhorroSaldosDAO asdao;
	private static AhorroRendimientoVigenteDAO vdao;
	
	
	private static void initialized() {

		try {
			Apps s = Apps.getInstance();
			synchronized (Apps.class) {
				if (apps == null) // si la referencia es null ...
					apps = s; // ... agrega la clase singleton
			}
			adao = (AhorroContratoDAO) s.getApplicationContext().getBean("AhorroContratoDAO");
			asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
			vdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean("AhorroRendimientoVigenteDAO");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public void ahorroActualizasaldo(Date fecha, String cuenta, Double monto, String operacion) {
		initialized();
		
		Boolean continua;
		Boolean actualiza;
		
		AhorroSaldos saldos = new AhorroSaldos();
		AhorroContrato ahorroContrato = new AhorroContrato();
		AhorroRendimientoVigentes ahorroRendimientoVigentes = new AhorroRendimientoVigentes();
		
		
		ahorroContrato = adao.buscarByCuenta(cuenta);
		//log.info(":::::" + ahorroContrato.toString());
		
		saldos = asdao.buscarByCuenta(cuenta);
		//log.info(":::::" + saldos.toString());
		
		
		continua = false;
		if(saldos != null)
			if(saldos.getCuenta() != null)
				continua = true;
		
		//log.info("::::" + continua + "::: " + operacion);
		
		if((!continua) && operacion.equals("+")){
			saldos = new AhorroSaldos();
			saldos.setCuenta(cuenta);
			saldos.setSolicitanteId(ahorroContrato.getSolicitante());
			saldos.setSaldoReal(monto);
			saldos.setSaldoPromedio((double) 0);
			saldos.setSaldoAcumulado((double) 0);
			saldos.setDias(0);
			saldos.setIntereses((double) 0);
			saldos.setIva((double) 0);
			saldos.setIsr((double) 0);
			saldos.setRetenciones((double) 0);
			saldos.setSaldoDisponible(monto);
			saldos.setFechaDeposito(fecha);
			
			
			saldos.setAhorroSaldosId(asdao.nuevo(saldos));
			
			ahorroContrato.setSaldo((ahorroContrato.getSaldo() != null ? ahorroContrato.getSaldo() : 0d) + monto);

			actualiza = adao.actualizar(ahorroContrato);
			
			ahorroRendimientoVigentes = vdao.buscarByCuenta(cuenta);
			if(ahorroRendimientoVigentes.getEstatus().equals("P")){
				ahorroRendimientoVigentes.setFechaDeposito(fecha);
				ahorroRendimientoVigentes.setCapital(monto);
				ahorroRendimientoVigentes.setFechaInicio(fecha);
				Date fechaFinal = diaHabil(fecha, ahorroRendimientoVigentes.getPlazo(), "D");
				ahorroRendimientoVigentes.setFechaFinal(fechaFinal);
				ahorroRendimientoVigentes.setEstatus("A");
				
				vdao.actualizar(ahorroRendimientoVigentes);
			}
		}else{
			if(operacion.equals("+")){
				ahorroContrato.setSaldo(ahorroContrato.getSaldo() + monto);
				actualiza = adao.actualizar(ahorroContrato);
				
				saldos.setSaldoDisponible((saldos.getSaldoDisponible() != null ? saldos.getSaldoDisponible() : 0d) + monto);
				saldos.setSaldoReal((saldos.getSaldoReal() != null ? saldos.getSaldoReal() : 0d) + monto);
				saldos.setSaldoAcumulado((saldos.getSaldoAcumulado() != null ? saldos.getSaldoAcumulado() : 0d) + monto);
				asdao.actualizar(saldos);
				
				ahorroRendimientoVigentes = vdao.buscarByCuenta(cuenta);
				if(ahorroRendimientoVigentes.getEstatus() != null){
					if(ahorroRendimientoVigentes.getEstatus().equals("P")){
						ahorroRendimientoVigentes.setFechaDeposito(fecha);
						ahorroRendimientoVigentes.setCapital(monto);
						ahorroRendimientoVigentes.setFechaInicio(fecha);
						Date fechaFinal = diaHabil(fecha, ahorroRendimientoVigentes.getPlazo(), "D");
						ahorroRendimientoVigentes.setFechaFinal(fechaFinal);
						ahorroRendimientoVigentes.setEstatus("A");
						
						vdao.actualizar(ahorroRendimientoVigentes);
					}
				}
			}else{
				//log.info(ahorroContrato.getSaldo());
				ahorroContrato.setSaldo((ahorroContrato.getSaldo() != null ? ahorroContrato.getSaldo() : 0d) - monto);
				//log.info(ahorroContrato.getSaldo());
				//log.info(ahorroContrato.toString());
				adao.actualizar(ahorroContrato);
				
				saldos.setSaldoDisponible((saldos.getSaldoDisponible() != null ? saldos.getSaldoDisponible() : 0d) - monto);
				saldos.setSaldoReal((saldos.getSaldoReal() != null ? saldos.getSaldoReal() : 0d) - monto);
				saldos.setSaldoAcumulado((saldos.getSaldoAcumulado() != null ? saldos.getSaldoAcumulado() : null) - monto);
				
				//log.info(saldos.toString());
				
				asdao.actualizar(saldos);
			}
		}
	}
	
	private Date diaHabil(Date fecha, Integer plazo, String string) {
		Calendar calendar = Calendar.getInstance();
		List<Date> fechasInhabiles = new ArrayList<Date>();
		calendar.setTime(fecha);
		Boolean continua = false;
		while(!continua){
			if(calendar.get(calendar.DAY_OF_WEEK) == 6){
				calendar.add(Calendar.DAY_OF_YEAR, (2));
			}else if(calendar.get(calendar.DAY_OF_WEEK) == 0){
				calendar.add(Calendar.DAY_OF_YEAR, (1));
			}
			if(fechasInhabiles.contains(calendar.getTime())){
				calendar.add(Calendar.DAY_OF_YEAR, (1));
			}else{
				continua = true;
			}
		}
		return calendar.getTime();
	}
		
}
