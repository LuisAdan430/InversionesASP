package net.cero.ahorro.logica;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.cero.ahorro.spei.data.HeaderWS;
import net.cero.ahorro.spei.data.ParametroBody;
import net.cero.ahorro.spei.generaclabe.GeneraClabeIfz;
import net.cero.ahorro.spei.generaclabe.GeneraClabeIfzService;
import net.cero.ahorro.spei.generaclabe.GeneraClabeRequest;
import net.cero.ahorro.spei.generaclabe.GeneraClabeResponse;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.spring.config.IPAuthenticationProvider;
/**
 * invoca al servicio MULE para generar la cuenta clabe
 * @author RGMZ
 *
 *modificacion 01/06/2022
 */
public class GenerarClabeLogic {

	@Autowired
	protected IPAuthenticationProvider authenticationManager;
	public static final Logger log = LogManager.getLogger(GenerarClabeLogic.class);

	/**
	 * utilizando el servicio SOA de MULE obtiene una cuenta clabe
	 * @param referencia la referencia para la generacion
	 * @param productoId tipo de producto
	 * @param sucursalApertura la sucursal de la cuenta
	 * @return la cuenta clabe en forma de cadena
	 */
	public String generarClabe(String referencia, Integer productoId, String sucursalApertura) {
		String clabe = null;

		try {
			URL wsdl = new URL(GeneraClabeIfzService.WSDL_LOCATION.toString().replace("localhost", ConstantesUtil.DNS_MULE_DEBITO));
			//URL wsdl = new URL(GeneraClabeIfzService.WSDL_LOCATION.toString().replace("localhost", "172.17.7.107"));
			// "sr-mule-sti.integraopciones.mx"));
			log.info(wsdl);
			long starTime = 0L;
			long endTime = 0L;
			long executionTime = 0L;
			double minutos = 0.0;
			
			starTime = System.currentTimeMillis();
			GeneraClabeIfzService service1 = new GeneraClabeIfzService(wsdl);
			endTime = System.currentTimeMillis();
			executionTime = endTime - starTime;
			minutos = (executionTime / 1000.0) / 60.0;
			log.info(":::: [ GenerarClabeLogic] ::::" + "Tiempo de ejecucion del metodo GeneraClabeIfzService  :::: " + minutos);
			/******************************************************************************************************/
			
			starTime = System.currentTimeMillis();
			GeneraClabeIfz port1 = service1.getGeneraClabeIfzPort();
			endTime = System.currentTimeMillis();
			executionTime = endTime - starTime;
			minutos = (executionTime / 1000.0) / 60.0;
			log.info(":::: [ GenerarClabeLogic] ::::" + "Tiempo de ejecucion de la obtencion del genera Clabe IfzPort  :::: " + minutos);
			/********************************************************************************************************************/
			
			GeneraClabeRequest req = new GeneraClabeRequest();
			HeaderWS header = new HeaderWS();
			header.setIdEmpresa(1L);
			header.setIdUbicacion(1l);

			req.setHeader(header);
			req.setProductoId(Long.valueOf(productoId));
			req.setClaveAplicacion("8");
			req.setReferencia(referencia);
			req.setClaveRegion(sucursalApertura);
			req.setObtenerClabe(1);
			
			starTime = System.currentTimeMillis();
			GeneraClabeResponse response = port1.procesar(req);
			endTime = System.currentTimeMillis();
			executionTime = endTime - starTime;
			minutos = (executionTime / 1000.0) / 60.0;
			log.info(":::: [ GenerarClabeLogic] ::::" + "Tiempo de ejecucion del metodo procesar req  :::: " + minutos);
			/********************************************************************************************************************/
			
			if (response.getReturn().getErrores().getCodigoError() != 0) {
				log.info(String.format("%s: %s", response.getReturn().getErrores().getCodigoError(),
						response.getReturn().getErrores().getDescError()));
			} else {
				for (ParametroBody res : response.getReturn().getBody().getParams()) {
					log.info(res.getNombre() + ": " + res.getValor());
					if (res.getNombre().equals("clabe")) {
						clabe = res.getValor();
						break;
					} 
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return clabe;
	}

}
