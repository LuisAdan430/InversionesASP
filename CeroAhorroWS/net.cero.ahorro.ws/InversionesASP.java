package net.cero.ahorro.ws;


import java.io.IOException;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
	
	


	@RequestMapping(value= "/crearInversionASP", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> crearInversion(@RequestBody String peticion) throws IOException {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
	    if (!authenticate.isAuthenticated()) {
	        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	    }

	    ResponseEntity<String> response;
	    String jsonResponse;
	    ObjectMapper objectMapper = new ObjectMapper();
	    Respuesta resp = new Respuesta();

	    try {
	        if (peticion == null || peticion.isEmpty()) {
	            resp.setCodigo(-1);
	            resp.setMensaje("La petición JSON está vacía.");
	            jsonResponse = objectMapper.writeValueAsString(resp);
	            return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST);
	        }

	        NuevaInversionASPReq nira = objectMapper.readValue(peticion, NuevaInversionASPReq.class);
	        if (nira == null) {
	            resp.setCodigo(-1);
	            resp.setMensaje("El JSON de la petición no se pudo deserializar correctamente.");
	            jsonResponse = objectMapper.writeValueAsString(resp);
	            return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST);
	        }

	        InversionesASPLogic AspInv =  new InversionesASPLogic();
	        resp = AspInv.crearInversionASP(nira);
	    } catch (JsonParseException | JsonMappingException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error en el formato JSON de la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error en JSON ]");
	    } catch (IOException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error de E/S al procesar la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error de E/S ]");
	    }
	    
	    jsonResponse = objectMapper.writeValueAsString(resp);
	    response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
	    return response;
	}
	
	@RequestMapping(value= "/crearReinversionASP",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> crearReinversion(@RequestBody String peticion) throws IOException{
		SecurityContext securityContext  = SecurityContextHolder.getContext();
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
		ResponseEntity<String> response;
		String jsonResponse;
		ObjectMapper objectMapper = new ObjectMapper();
		Respuesta resp  = new Respuesta();
		
		try {
			if(peticion == null || peticion.isEmpty()) {
				resp.setCodigo(-1);
				resp.setMensaje("La peticion JSON esta vacía");
				jsonResponse = objectMapper.writeValueAsString(resp);
				return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST);
			}
			NuevaInversionASPReq nira = objectMapper.readValue(peticion, NuevaInversionASPReq.class);
			if(nira == null) {
				resp.setCodigo(-1);
				resp.setMensaje("El JSON de la petición no se pudo deserializar correctamente");
				jsonResponse = objectMapper.writeValueAsString(resp);
				return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST); 
				
			}
			InversionesASPLogic AspInv =  new InversionesASPLogic();
			resp = AspInv.realizarReinversion(nira);
		} catch (JsonParseException | JsonMappingException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error en el formato JSON de la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error en JSON ]");
	    } catch (IOException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error de E/S al procesar la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error de E/S ]");
	    }
		    jsonResponse = objectMapper.writeValueAsString(resp);
		    response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		    return response;
		
	}
	
	
	
	@RequestMapping(value= "/consultarInversionASP",method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> consultarInversionASP(@RequestBody String peticion) throws IOException{
		SecurityContext securityContext  = SecurityContextHolder.getContext();
		Authentication authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
		if(!authenticate.isAuthenticated()) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}
		ResponseEntity<String> response;
		String jsonResponse;
		ObjectMapper objectMapper = new ObjectMapper();
		Respuesta resp  = new Respuesta();
		
		try {
			if(peticion == null || peticion.isEmpty()) {
				resp.setCodigo(-1);
				resp.setMensaje("La peticion JSON esta vacía");
				jsonResponse = objectMapper.writeValueAsString(resp);
				return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST);
			}
			InformacionInversionASPReq nira = objectMapper.readValue(peticion, InformacionInversionASPReq.class);
			if(nira == null) {
				resp.setCodigo(-1);
				resp.setMensaje("El JSON de la petición no se pudo deserializar correctamente");
				jsonResponse = objectMapper.writeValueAsString(resp);
				return new ResponseEntity<String>(jsonResponse, HttpStatus.BAD_REQUEST); 
				
			}
			ObtenerInformacionInversionASPLogic infoInversionLogic = new ObtenerInformacionInversionASPLogic();
			resp = infoInversionLogic.consultarInversionASP(nira);
		} catch (JsonParseException | JsonMappingException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error en el formato JSON de la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error en JSON ]");
	    } catch (IOException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error de E/S al procesar la petición: " + e.getMessage());
	        log.error("Error [ InversionesASP ] [ Error de E/S ]");
	    }
		    jsonResponse = objectMapper.writeValueAsString(resp);
		    response = new ResponseEntity<String>(jsonResponse, HttpStatus.OK);
		    return response;
		
	}
	
	@RequestMapping(value = "realizarPagos", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> consultarPagos() {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authenticate;
	    ResponseEntity<String> response;
	    String jsonResponse;
	    Gson gson = new Gson();
	    Respuesta resp = new Respuesta();
	    authenticate = authenticationManager.authenticate(securityContext.getAuthentication());
	    if (!authenticate.isAuthenticated()) {
	        response = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        return response;
	    }
	    try {
	        RealizarPagosASPLogic pagosASPLogic = new RealizarPagosASPLogic();
	        pagosASPLogic.revisarPagos();
	        jsonResponse = gson.toJson(resp);
	        response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	        return response;
	    } catch (RuntimeException e) {
	        resp.setCodigo(-1);
	        resp.setMensaje("Error en tiempo de ejecución: " + e.getMessage());
	        resp.setData(null);
	        jsonResponse = gson.toJson(resp);
	        response = new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	        return response;
	    }
	}


	
}
