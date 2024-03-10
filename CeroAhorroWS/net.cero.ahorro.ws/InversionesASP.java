package net.cero.ahorro.ws;


import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import net.cero.ahorro.logica.InversionesASPLogic;
import net.cero.ahorro.logica.ObtenerInformacionInversionASPLogic;
import net.cero.ahorro.logica.RealizarPagosASPLogic;
import net.cero.data.InformacionInversionASPReq;
import net.cero.data.NuevaInversionASPReq;
import net.cero.data.Respuesta;
import net.cero.seguridad.utilidades.ConstantesInversiones;
import net.cero.spring.config.IPAuthenticationProvider;

import org.apache.logging.log4j.Logger;

@RestController
public class InversionesASP {
	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	private static final Logger log = LogManager.getLogger(InversionesASP.class);
	@RequestMapping(value= "/crearInversionASP",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> crearInversion(@RequestBody String peticion){
		SecurityContext securityContext = null;
		securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = null;
		gson = new Gson();
		Respuesta resp = null;
		resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		try {
			InversionesASPLogic AspInv = null;
			AspInv = new InversionesASPLogic();
			NuevaInversionASPReq nira;
			nira = gson.fromJson(peticion,NuevaInversionASPReq.class);
			resp = AspInv.crearInversionASP(nira);
			
			log.info(ConstantesInversiones.EXITO_PASO_A_LOGICA);
		} catch (Exception e) {
		    resp.setCodigo(-1);
		    resp.setMensaje("Error en el proceso: " + e.getMessage());
		    resp.setData(null);
		}
		jsonResponse = gson.toJson(resp);
		response = new ResponseEntity<String>(jsonResponse,HttpStatus.OK);
		return response;
		
	}
	@RequestMapping(value= "/crearReinversionASP",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> crearReinversion(@RequestBody String peticion){
		SecurityContext securityContext = null;
		securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = null;
		gson = new Gson();
		Respuesta resp = null;
		resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		try {
			InversionesASPLogic AspInv = null;
			AspInv = new InversionesASPLogic();
			NuevaInversionASPReq nira;
			nira = gson.fromJson(peticion,NuevaInversionASPReq.class);
			resp = AspInv.realizarReinversion(nira);
			
			log.info(ConstantesInversiones.EXITO_PASO_A_LOGICA);
		} catch (Exception e) {
		    resp.setCodigo(-1);
		    resp.setMensaje("Error en el proceso: " + e.getMessage());
		    resp.setData(null);
		}
		jsonResponse = gson.toJson(resp);
		response = new ResponseEntity<String>(jsonResponse,HttpStatus.OK);
		return response;
		
	}
	
	/* Consultar inversion por nombre de la inversion */
	
	@RequestMapping(value= "/consultarInversionASP",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> consultarInversionASP(@RequestBody String datos){
		SecurityContext securityContext = null;

		log.info("Status: peticion consultarInversionASP");
		
		securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = null;
		gson = new Gson();
		Respuesta resp = null;
		resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		try {
			ObtenerInformacionInversionASPLogic infoInversionLogic = new ObtenerInformacionInversionASPLogic();
			InformacionInversionASPReq infoInversionReq;
			infoInversionReq = gson.fromJson(datos, InformacionInversionASPReq.class);
			resp = infoInversionLogic.consultarInversionASP(infoInversionReq);
		} catch (Exception e) {
		    resp.setCodigo(-1);
		    resp.setMensaje("Error en el proceso: " + e.getMessage());
		    resp.setData(null);
		}
		jsonResponse = gson.toJson(resp);
		response = new ResponseEntity<String>(jsonResponse,HttpStatus.OK);
		return response;
		
	}
	
	@RequestMapping(value= "realizarPagos",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> consultarPagos(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authenticate;
		ResponseEntity<String> response;
		String jsonResponse;
		Gson gson = new Gson();
		Respuesta resp = new Respuesta();
		authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			response = new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
			return response;
		}
		try {
			RealizarPagosASPLogic pagosASPLogic = new RealizarPagosASPLogic();
			pagosASPLogic.revisarPagos();
			
		} catch (Exception e) {
		    resp.setCodigo(-1);
		    resp.setMensaje("Error en el proceso: " + e.getMessage());
		    resp.setData(null);
		}
		jsonResponse = gson.toJson(resp);
		response = new ResponseEntity<String>(jsonResponse,HttpStatus.OK);
		return response;
		
	}
		
}
