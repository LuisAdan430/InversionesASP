package net.cero.ahorro.logica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.ibm.icu.util.Calendar;

import net.cero.data.InformacionInversionASPReq;
import net.cero.data.InformacionInversionASPResp;
import net.cero.data.ObtenerInfoAhorroContrato;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesInversiones;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.InversionesDAO;

public class ObtenerInformacionInversionASPLogic {
	private static final Logger log = LogManager.getLogger(ObtenerInformacionInversionASPLogic.class);
	public static Apps apps = null;
	private static InversionesDAO invdao = null;
	
	public Respuesta consultarInversionASP(InformacionInversionASPReq datos) {
		initialized();
		Respuesta resp = new Respuesta();
		try {
		InformacionInversionASPResp respInfoI = new InformacionInversionASPResp();
		if(validarVariables(datos)) {
			List<ObtenerInfoAhorroContrato> infoTablaAhorroContrato = new ArrayList<>();
			infoTablaAhorroContrato = invdao.ahorroContrato(datos.getNombreInversion(), datos.getCuenta());
			if(infoTablaAhorroContrato != null) {
				Double montoInicial = (Double)infoTablaAhorroContrato.get(0).getMonto_apertura();
				Date fecha_AperturaC = (Date)infoTablaAhorroContrato.get(0).getFecha_Apertura();
				Integer rendimientoId = (Integer)infoTablaAhorroContrato.get(0).getRendimientoId();
				String fecha_AperturaF = formatoFecha(fecha_AperturaC);
				Integer plazoReinversionV = invdao.plazoReinversionM(rendimientoId);
				Date finalizacionInversionC = calculoFechaFinal(fecha_AperturaC,plazoReinversionV);
				String finalizacionInversionF = formatoFecha(finalizacionInversionC);
				long diasFaltantes = tiempoRestante(fecha_AperturaC,finalizacionInversionC);
				Double interes = invdao.porcentajeDAO(rendimientoId);
				Double montoFinal = montoFinal(montoInicial,interes,plazoReinversionV);
				respInfoI = informacion(montoInicial,fecha_AperturaF,plazoReinversionV,finalizacionInversionF,diasFaltantes,interes,montoFinal);
				resp = respuestas(1,respInfoI);
				return resp;
				
			}else {
				resp = respuestas(3,null);
				return resp;
			}
		}else {
			resp = respuestas(2,null);
			return resp;
		}
		}catch(Exception e) {
			resp = respuestas(4,null);
		    return resp;
		}
		
		
	}
	public Boolean validarVariables(InformacionInversionASPReq datos) {
		InversionesASPLogic soloValidaciones = new InversionesASPLogic();
		Integer nombreInversion = soloValidaciones.validarDescripciones(datos.getNombreInversion());
		Boolean cuentaXS = soloValidaciones.validarStrings(datos.getCuenta());
		Integer cuenta = null;
		if(cuentaXS) {
			cuenta = 0;
		}else {
			cuenta = 1;
		}
		Integer sumaValidaciones = nombreInversion + cuenta;
		if(sumaValidaciones > 0) {
			return false;
		}else {
			return true;
		}
	}
	public Double montoFinal (Double montoInicial, Double interes,Integer plazoReinversionV) {
		Double interesConvDecimales = interes/100;
		Double interesFinDePlazo =  (montoInicial * interesConvDecimales * plazoReinversionV)/100;
		Double montoFinal = montoInicial + interesFinDePlazo;
		return montoFinal;
	}
	public long tiempoRestante(Date fechaInicio,Date fechaFinal) {
		long diferenciaMilisegundos =  fechaFinal.getTime() - fechaInicio.getTime();
		long diferenciaDias = diferenciaMilisegundos/86400000;
		return diferenciaDias;
	}
	public Date calculoFechaFinal(Date fecha_Apertura,Integer plazoReinversionV) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha_Apertura);
		calendar.add(Calendar.DAY_OF_MONTH, plazoReinversionV);
		Date fechaFinal = calendar.getTime();
		return fechaFinal;
	}
	public String formatoFecha(Date fechaConvertir) {
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		String fechaConvertida = formatoFecha.format(fechaConvertir);
		return fechaConvertida;
	}
	public InformacionInversionASPResp informacion(Double montoInicial,
			String fecha_Apertura,
			Integer plazoReinversionV, String finalizacionInversion, long tiempoRestante,Double interes,Double montoFinal) {
		
		String nombreInversion = "cumpleaños Nath";
		
		InformacionInversionASPResp respInfoI = new InformacionInversionASPResp();
		respInfoI.setMontoInicial(montoInicial);
		respInfoI.setNombreInversion(nombreInversion);
		respInfoI.setFinalizacionInversion(finalizacionInversion);
		respInfoI.setDiaCreacionInversion(fecha_Apertura);
		respInfoI.setTiempoRestante(tiempoRestante);
		respInfoI.setPlazo(plazoReinversionV);
		respInfoI.setMontoFinal(montoFinal);
		respInfoI.setInteres(interes);
		
		return respInfoI;
	}
	public Respuesta respuestas(Integer codigo,InformacionInversionASPResp data) {
		Respuesta resp = new Respuesta(); 
		Gson gson =  new Gson();
		String jsonData = gson.toJson(data);
		if(codigo == 1) {
			resp.setCodigo(1);
			resp.setMensaje("La inversion existe");
			resp.setData(jsonData);
		}else if(codigo == 2) {
			resp.setCodigo(2);
			resp.setMensaje("Los valores enviados estan mal");
			resp.setData(jsonData);
		}else if(codigo == 3) {
			resp.setCodigo(3);
			resp.setMensaje("No existe la inversion por favor revisar datos");
			resp.setData(jsonData);
		}
		else if(codigo == 4) {
			resp.setCodigo(-1);
			resp.setMensaje("Error en la logica");
			resp.setData(jsonData);
		}
		return resp;
	}
	
	
	private static void initialized() {
		try {
			Apps s = Apps.getInstance();
			synchronized(Apps.class) {
				if(apps == null)
					apps = s;
			}
			invdao = (InversionesDAO) s.getApplicationContext().getBean(ConstantesInversiones.INVERSIONES_DAO);
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
