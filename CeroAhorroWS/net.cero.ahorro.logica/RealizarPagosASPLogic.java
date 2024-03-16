package net.cero.ahorro.logica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.InitializationException;
import org.springframework.beans.BeansException;

import net.cero.seguridad.utilidades.ConstantesInversiones;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.InversionesPagosDAO;

public class RealizarPagosASPLogic {
	private static final Logger log = LogManager.getLogger(RealizarPagosASPLogic.class);
	private static Apps apps = null;
	private static InversionesPagosDAO invpDAO = null;
	
	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized(Apps.class) {
				if(apps == null)
					apps = s;
			}
			invpDAO = (InversionesPagosDAO) s.getApplicationContext().getBean(PAGOS_DAO);
		} catch (BeansException e) {
	        log.error("Error al inicializar la aplicación: " + e.getMessage());
	        throw new InitializationException("Error al inicializar la aplicación", e);
	    }
	}
	
	public void revisarPagos() {
		
		initialized();
		try {
			//LocalDate fechaActual = LocalDate.now();
			//log.info(fechaActual);
			LocalDate fechaActual = LocalDate.parse("2024-02-08");
			List<List<Object>> informacion = invpDAO.pagosHoy(fechaActual);
			if(informacion == null || informacion.isEmpty()) {
				log.info("Vacio");
			}else {
				//log.info(informacion);
				Integer contador = 0;
				for(Object elemento : informacion) {
					if(elemento instanceof List) {
						contador++;
					}
				}
				//log.info(contador);
				for(Integer conteo = 0; conteo < contador; conteo ++) {
					List<Object> contrato = (List<Object>) informacion.get(conteo);
					log.info(contrato);
					//Integer id = (Integer) contrato.get(0);
					//Integer cuentaId = (Integer) contrato.get(1);
					//Date fechaCorte = (Date) contrato.get(2);
					//BigDecimal tasa = (BigDecimal) contrato.get(3);
					Integer dias = (Integer) contrato.get(4);
					BigDecimal monto = (BigDecimal) contrato.get(5);
					//String poliza = (String) contrato.get(6);
					//Date fechaCreacion = (Date) contrato.get(7);
					//Integer usuarioCreacion = (Integer) contrato.get(8);
					//Date fechaModificacion = (Date) contrato.get(9);
					//Integer usuarioModificacion = (Integer) contrato.get(10);
					Boolean estatus_activo = (Boolean) contrato.get(11);
					String idAhorro = (String) contrato.get(12);
					if(estatus_activo == true) {
						Integer usuarioModificacionNew = 1;
						Date fechaModificacionNew = new Date();
						List<Boolean> resultados = invpDAO.editarPagos(fechaModificacionNew, usuarioModificacionNew,idAhorro,fechaActual);
						log.info(resultados);

						
						//log.info(":::" + id + ":::" + cuentaId + ":::" + fechaCorte + ":::" + tasa +":::" + dias +":::" + monto + ":::" + poliza + ":::" + fechaCreacion 
						//		+ ":::" + usuarioCreacion + ":::" + fechaModificacion + ":::" + usuarioModificacion +":::" + estatus_activo + ":::" + idAhorro);
						
						Integer idAhorroContrato = Integer.parseInt(idAhorro);
						List<String> infoAhorroContrato = invpDAO.informacionAhorroContrato(idAhorro);
						String cuentaPadre = infoAhorroContrato.get(0);
						String cuenta = infoAhorroContrato.get(1);
						String referencia = infoAhorroContrato.get(2);
						
						InversionesASPLogic inversiones = new InversionesASPLogic();
						Integer realizarDeposito =
								inversiones.realizarDeposito_Metodo(cuentaPadre, cuenta, monto,dias,referencia,6,idAhorroContrato) ;
						log.info(realizarDeposito);
						
						
					}
					
				}
				
			}
		} catch (DateTimeParseException e) {
	        log.error("Error al parsear la fecha actual: " + e.getMessage());
	    } catch (NumberFormatException e) {
	        log.error("Error al convertir el ID de ahorro a entero: " + e.getMessage());
	    } 
		
		
		
		
		
	}
	private static final String PAGOS_DAO = "InversionesPagosDAO";
}
