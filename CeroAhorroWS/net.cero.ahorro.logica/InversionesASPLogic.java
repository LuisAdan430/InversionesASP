package net.cero.ahorro.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.InitializationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.dao.DataAccessException;

import com.ibm.icu.util.Calendar;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroCuentaMiDebitoOBJ;
import net.cero.data.AhorroSaldos;
import net.cero.data.AhorroTransferenciaReqOBJ;
import net.cero.data.CapitalizarRendimientosOBJ;
import net.cero.data.FechasDepositosInversionesOBJ;
import net.cero.data.NuevaInversionASPReq;
import net.cero.data.Respuesta;
import net.cero.data.TazasPlazosOBJ;
import net.cero.seguridad.utilidades.ConceptosUtil;
import net.cero.seguridad.utilidades.ConstantesUtil;
import net.cero.seguridad.utilidades.CustomException;
import net.cero.seguridad.utilidades.HeaderWS;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.AhorroCuentasDAO;
import net.cero.spring.dao.AhorroCuentasMiDebitoDAO;
import net.cero.spring.dao.AhorroDAO;
import net.cero.spring.dao.AhorroRendimientoVigenteDAO;
import net.cero.spring.dao.AhorroSaldosDAO;
import net.cero.spring.dao.InversionesDAO;
import net.cero.spring.dao.InversionesPagosDAO;
import net.cero.seguridad.utilidades.ConstantesInversiones;
public class InversionesASPLogic {
	private static final Logger log = LogManager.getLogger(InversionesASPLogic.class);
	private static Apps apps = null;

	private static AhorroDAO adao = null;
	private static AhorroCuentasMiDebitoDAO acmdao  = null;
	private static AhorroRendimientoVigenteDAO arvdao = null;
	private static InversionesDAO invdao = null;
	private static AhorroCuentasDAO ahcd = null;
	private static InversionesPagosDAO invpDAO = null;
	private static AhorroSaldosDAO asdao;
	private static final String PAGOS_DAO = "InversionesPagosDAO";
	private static void initialized() throws InitializationException {
	    try {
	        Apps s = Apps.getInstance();
	        synchronized(Apps.class) {
	            if(apps == null) 
	                apps = s;
	        }
	        adao = (AhorroDAO) s.getApplicationContext().getBean(ConstantesInversiones.AHORRO_DAO);
	        acmdao = (AhorroCuentasMiDebitoDAO) s.getApplicationContext().getBean(ConstantesInversiones.AHORRO_CUENTAS_DEBITO);
	        arvdao = (AhorroRendimientoVigenteDAO) s.getApplicationContext().getBean(ConstantesInversiones.AHORRO_RENDIMIENTO_VIGENTE);
	        invdao = (InversionesDAO) s.getApplicationContext().getBean(ConstantesInversiones.INVERSIONES_DAO);
	        ahcd = (AhorroCuentasDAO) s.getApplicationContext().getBean(ConstantesInversiones.AHORRO_CUENTAS_DAO);
	        invpDAO = (InversionesPagosDAO) s.getApplicationContext().getBean(PAGOS_DAO);
	        asdao = (AhorroSaldosDAO) s.getApplicationContext().getBean("AhorroSaldosDAO");
	    } catch (BeansException e) {
	        log.error("Error al inicializar la aplicación: " + e.getMessage());
	        throw new InitializationException("Error al inicializar la aplicación", e);
	    }
	}


	public Respuesta respuestas(Integer codigo) {
		Respuesta resp = new Respuesta();
		if(codigo == 1) {
			resp.setCodigo(1);
			resp.setMensaje("Error en la peticion datos invalidos");
			resp.setData(null);
		}else if(codigo == 2) {
			resp.setCodigo(2);
			resp.setMensaje("Error la cuenta no es de inversiones");
			resp.setData(null);
			
		}else if(codigo == 3) {
			resp.setCodigo(2);
			resp.setMensaje("Error la cuenta no esta asociada a ningun solicitante");
			resp.setData(null);
		}else if(codigo == 4) {
			resp.setCodigo(2);
			resp.setMensaje("No existe el rendimiento con ese ID");
			resp.setData(null);
		}else if(codigo == 5) {
			resp.setCodigo(2);
			resp.setMensaje("No existe la cuenta contable");
			resp.setData(null);
		}else if(codigo == 6) {
			resp.setCodigo(2);
			resp.setMensaje("El monto no es valido");
			resp.setData(null);
		}else if(codigo == 7) {
			resp.setCodigo(2);
			resp.setMensaje("No se obtuvo ninguna taza y plazo ");
			resp.setData(null);
		}else if(codigo == 8) {
			resp.setCodigo(3);
			resp.setMensaje("Error al crear el contrato ");
			resp.setData(null);
		}else if(codigo == 9) {
			resp.setCodigo(3);
			resp.setMensaje("Error al hacer el deposito ");
			resp.setData(null);
		}else if(codigo == 10) {
			resp.setCodigo(3);
			resp.setMensaje("Error al realizar la reinversion ");
			resp.setData(null);
		}else if(codigo == 11) {
			resp.setCodigo(0);
			resp.setMensaje("Exito proceso concluido ");
			resp.setData(null);
		}else if(codigo == 12) {
			resp.setCodigo(-3);
			resp.setMensaje("Error en la conexion de la base  ");
			resp.setData(null);
		}else if(codigo == 13) {
			resp.setCodigo(2);
			resp.setMensaje("Saldo Insuficiente en la cuenta");
			resp.setData(null);
		}else if(codigo == 14) {
			resp.setCodigo(3);
			resp.setMensaje("Error en modalidad");
			resp.setData(null);
		}
		return resp;
	}
	public void consultarSaldo(String cuenta) {
		AhorroSaldos saldos = new AhorroSaldos();
		saldos = asdao.buscarByCuenta(cuenta);
		//log.info(":::::" + saldos.toString());
	}
	
	public Respuesta crearInversionASP(NuevaInversionASPReq req)  throws CustomException {
		initialized();
		Respuesta resp = new Respuesta();
		try {
			/*
			if(obtenerSaldo(req.getCuentaOriginal(),req.getMonto()) == false ) {
				resp = respuestas(13);
				return resp;
			}
			*/
			
			consultarSaldo(req.getCuentaOriginal());
			
			
			Boolean validacionDatosResp = Validaciones(req);
			if(validacionDatosResp == false) {
				resp = respuestas(1);
				return resp;
			}
			
			Integer tipoCuenta = tipoCuentaCero(req.getCuentaOriginal());
			if(tipoCuenta != 6) {
				resp = respuestas(12);
				return resp;
			}
			AhorroContrato contratoA = ahcd.obtenerSolicitanteId(req.getCuentaOriginal());
			if(contratoA == null) {
				resp = respuestas(3);
				return resp;
			}
				
			Integer idSolicitante = Integer.parseInt(contratoA.getSolicitante());
			List<CapitalizarRendimientosOBJ> capitalizarRendimientosObjList = new ArrayList<>(); 
			capitalizarRendimientosObjList = capitalizarRendimientos(Long.valueOf(req.getIdRendimiento()));
			if(capitalizarRendimientosObjList == null) {
				resp = respuestas(4);
				return resp;
			}
			
			Long idModalidad = Long.valueOf(capitalizarRendimientosObjList.get(0).getId());
			String cuentaContable = cuentaContable(idModalidad);
			if(cuentaContable == null || cuentaContable.equals(ConstantesInversiones.COMILLAS_VACIAS)) {
				resp = respuestas(5);
				return resp;
			}
			BigDecimal montoMin = (BigDecimal) capitalizarRendimientosObjList.get(0).getMontoMin();
			BigDecimal montoMax = (BigDecimal) capitalizarRendimientosObjList.get(0).getMontoMax();
			if( validarMonto(montoMin,montoMax,req.getMonto())) {
			String cuenta = existeEnProcrea(idSolicitante);
			String contrato = obtenerContrato(idSolicitante);
			Integer plazo = req.getPlazo();
			String referencia = referencia(cuenta);
			List<TazasPlazosOBJ> tazasPlazosOBJ = new ArrayList<>();
			tazasPlazosOBJ = obtenerTazasPlazosByCanalAndPlazo(req.getPlazo());
			if(tazasPlazosOBJ == null) {
				resp = respuestas(7);
				return resp;
			}
			Double interesPorcentaje = (Double)tazasPlazosOBJ.get(0).getPorcentaje();
			Integer idRendimientoInsert = (Integer)tazasPlazosOBJ.get(0).getRendimientoId();
			
			Integer idAhorro = idAhorro(cuenta,idSolicitante,contrato,idRendimientoInsert,cuentaContable,
												referencia,req.getCuentaOriginal(),req.getMonto(),interesPorcentaje); 
			if(idAhorro == 0) {
			resp = respuestas(8);
			return resp;
			}
			Integer modalidadInt = req.getModalidad();
			switch(modalidadInt) {
				case 1:
					Boolean respuestaPa = pagosMensuales(plazo,idAhorro,interesPorcentaje,req.getMonto());
					if(respuestaPa) {
						resp = respuestas(11);
						return resp;
					}else {
						resp = respuestas(14);
						return resp;
					}
				case 2:
					Integer respuestaDeposito = realizarDeposito_Metodo(req.getCuentaOriginal(),cuenta,req.getMonto(),plazo,referencia,tipoCuenta,idAhorro);
					if(respuestaDeposito == 11) {
						resp = respuestas(11);
						return resp;
					}else {
						resp = respuestas(14);
						return resp;
					}
				default:
					resp = respuestas(14);
					return resp;
					
			}
			
			
			} else {
			resp = respuestas(6);
			return resp;
			}
		} catch ( BeansException e) {
	        log.error("Error durante la creación de la inversión ASP: " + e.getMessage());
	        throw new CustomException("Error durante la creación de la inversión ASP", e);
	    } catch (ValidationException e) {
	        log.error("Error de validación durante la creación de la inversión ASP: " + e.getMessage());
	        throw new CustomException("Error de validación durante la creación de la inversión ASP", e);
	    } catch (DataAccessException e) {
	        log.error("Error de acceso a datos durante la creación de la inversión ASP: " + e.getMessage());
	        throw new CustomException("Error de acceso a datos durante la creación de la inversión ASP", e);
	    } catch (Exception e) {
	        log.error("Error desconocido durante la creación de la inversión ASP: " + e.getMessage());
	        throw new CustomException("Error desconocido durante la creación de la inversión ASP", e);
	    }
	}
	
	public LocalDate validarDiasHabiles(LocalDate fechaValidarB) {
		    LocalDate fechaValidar = fechaValidarB.plusDays(1);
			DayOfWeek diaSemana = fechaValidar.getDayOfWeek();
			LocalDate fechaFinal = fechaValidar;
			if(diaSemana == DayOfWeek.SATURDAY) {
				fechaFinal = fechaFinal.plusDays(2);
			}else if(diaSemana == DayOfWeek.SUNDAY){
				fechaFinal = fechaFinal.plusDays(1);
			}else {
				fechaFinal = fechaFinal.plusDays(0);
			}
			return fechaFinal;
		
		
	}
	public Boolean pagosMensuales(Integer plazo,Integer idAhorro,Double interesPorcentaje,BigDecimal monto) {
		try {
			Integer cuentaId = 1;
			String poliza = "MODALIDAD_1";
			Double montoF = monto.doubleValue();
			Integer usuarioCreacion = 1;
			Double interesPorcentajeF = (interesPorcentaje /100);
			FechasDepositosInversionesOBJ fechasDepositos = new FechasDepositosInversionesOBJ();
			fechasDepositos = asignacionFechas(plazo);
			log.info(interesPorcentajeF);
			Integer conteoFechas = fechasDepositos.getFechas().size();
			
			for(Integer contador = 0 ; contador < conteoFechas; contador ++ ) {
				LocalDate fechaPrevia = fechasDepositos.getFechas().get(contador);
				LocalDate fechaInicial = fechasDepositos.getFechasIniciales().get(contador);
				LocalDate fechaFinal = validarDiasHabiles(fechaPrevia);
				
				Date fechaFinalFormatoBase = Date.from(fechaFinal.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Date fechaInicialFormatoBase = Date.from(fechaInicial.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				
				Integer plazoModalidad = fechasDepositos.getDiasContablesPagos().get(contador);
				//Boolean respuesta = invpDAO.insertarPagos(cuentaId,poliza,idAhorro.toString(), fechaFinalFormatoBase,interesPorcentajeF ,plazoModalidad, montoF, fechaInicialFormatoBase, usuarioCreacion, true);
				invpDAO.insertarPagos(cuentaId,poliza,idAhorro.toString(), fechaFinalFormatoBase,interesPorcentajeF ,plazoModalidad, montoF, fechaInicialFormatoBase, usuarioCreacion, true);
				//log.info(respuesta);
			}
			return true;
		}catch(Exception e) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Pagos Mensuales  ] ");
			return false;
		}
		
	}
	public List<TazasPlazosOBJ> obtenerTazasPlazosByCanalAndPlazo(Integer plazo) {
		initialized();
		
		List<TazasPlazosOBJ> tazasPlazosOBJ = new ArrayList<>();
		String canal = ConstantesInversiones.CANAL_ASP_PAGO;
		try {
			tazasPlazosOBJ = invdao.obtenerTazasPlazosByCanalAndPlazo(canal, plazo);
			return tazasPlazosOBJ;
		} catch (Exception ex) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Obtener Tazas Plazos By Canal And Plazo ] ");
		}
		
		return tazasPlazosOBJ;
		
	}
	public FechasDepositosInversionesOBJ asignacionFechas(Integer plazo) {
		
		
		FechasDepositosInversionesOBJ objeto = new FechasDepositosInversionesOBJ();
		List<LocalDate> fechas = new ArrayList<>();
		List<Integer> diasContablesPagos = new ArrayList<>();
		List<LocalDate> fechasIniciales = new ArrayList<>();
		
		Integer plazoProceso = plazo;
		
	    LocalDate fechaActual = LocalDate.now();
	    fechasIniciales.add(fechaActual);
	    // LocalDate fechaFinalPago = fechaActual.plusDays(plazo);
	   
	    
	    LocalDate ultimoDiaMesActual = fechaActual.withDayOfMonth(fechaActual.lengthOfMonth());
	    fechas.add(ultimoDiaMesActual);
	    
	    Integer diasRestantes = ultimoDiaMesActual.getDayOfMonth() - fechaActual.getDayOfMonth();
	    diasContablesPagos.add(diasRestantes);

	    Integer diasRestantesPlazo = plazoProceso - diasRestantes;
	   
	    
	    Month mesActual = LocalDate.now().getMonth();
	    Integer numeroMesInicial = mesActual.getValue();
	    Integer yearActual = LocalDate.now().getYear();
	    
	    Integer mesesPasados = 0;
	    
	    Integer x =  1;
	   
	    
	    do {
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.set(Calendar.DAY_OF_MONTH, 1); 
		    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+ x);
		    Integer ultimoDiaDelSiguienteMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		    Date fechaInicialDate = calendar.getTime();
		    LocalDate fechaInicial = fechaInicialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    diasRestantesPlazo = diasRestantesPlazo - ultimoDiaDelSiguienteMes;
	        if(diasRestantesPlazo >= 0) {
	        	  diasContablesPagos.add(ultimoDiaDelSiguienteMes);
	        	  fechasIniciales.add(fechaInicial);
	        }
		  
	    	mesesPasados ++;
	    	x ++;
	    }while(diasRestantesPlazo >= 0);
	    
	 
	 
	   		int t = numeroMesInicial + 1;
	   		
	   		Integer repetirMeses = mesesPasados ;
	        for(int i = 1; i < repetirMeses; i++) {
	        	Calendar calendar = Calendar.getInstance();
		    	calendar.set(Calendar.DAY_OF_MONTH, 1);
		    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+ i);
		    	Integer ultimoDiaDelSiguienteMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		    	String numeroString= "";
		    		if(t < 10) {
		    			numeroString = String.format("%02d", t);
		    		}else if(t>12 && t<=24){
		    			Integer meses =12;
			        	Integer a =  0;
			        	a = meses - t; 
			        	Integer b = 1*-a;
			        	String c = String.format("%02d", b);
			        	numeroString = c;
		    		}else if(t>24) {
		    			Integer meses =24;
			        	Integer a =  0;
			        	a = meses - t; 
			        	Integer b = 1*-a;
			        	String c = String.format("%02d", b);
			        	numeroString = c;
		    		}else {
		    			numeroString = Integer.toString(t);
		    		}
		    		String year = "";
		    		if(t>12) {
		    			year = Integer.toString(yearActual + 1);
		    		}else {
		    			year = Integer.toString(yearActual);
		    		}
			    	String fechaString = ultimoDiaDelSiguienteMes+"/"+numeroString+"/"+year;
			    	DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			    	try {
			    		LocalDate fecha = LocalDate.parse(fechaString,formato);
			    		fechas.add(fecha);
			    	}catch(Exception e) {
			    		log.info("La fecha no es valida");
			    	}
			    	t++;
			    }
	        
	        
	        
	        	Integer mesFinal  = mesesPasados +numeroMesInicial;
	         	
	        	Calendar calendar = Calendar.getInstance();
	        	calendar.set(Calendar.DAY_OF_MONTH,1);
	        	calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) + mesesPasados);
	        	Integer ultimoDiaMesFinal = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        	Integer descuentoDeMesFinal = ultimoDiaMesFinal + diasRestantesPlazo;
	        	diasContablesPagos.add(descuentoDeMesFinal);
	        	String descuentoDeMesFinalC = "";
	        	if(descuentoDeMesFinal <10) {
	        		descuentoDeMesFinalC = String.format("%02d",descuentoDeMesFinal);
	        	}else {
	        		descuentoDeMesFinalC = Integer.toString(descuentoDeMesFinal);
	        	}
	        	
	        	String year = "";
	        	if(mesFinal > 12) {
	        		
	        		year = Integer.toString(yearActual + 1);
	        	}else {
	        		year = Integer.toString(yearActual);
	        	}
	        	String mesFinalC = "";
	        	if(mesFinal < 10 ) {
	        		mesFinalC = String.format("%02d", mesFinal);
	        	}else if(mesFinal > 12 && mesFinal <= 24){
	        		Integer meses =12;
		        	Integer a =  0;
		        	a = meses - mesFinal;  
		        	Integer b = 1*-a;
		        	String c = String.format("%02d", b);
		        	mesFinalC = c;
	        	}else if(mesFinal > 24) {
	        		Integer meses =24;
		        	Integer a =  0;
		        	a = meses - mesFinal; 
		        	Integer b = 1*-a;
		        	String c = String.format("%02d", b);
		        	mesFinalC = c;
	        	}else {
	        		mesFinalC = Integer.toString(mesFinal);
	        	}
	        	String fechaString = descuentoDeMesFinalC+"/"+mesFinalC+"/"+year;
	        	DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		    	try {
		    		Date fechaInicialDate = calendar.getTime();
		    	    LocalDate fechaInicial = fechaInicialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    	    fechasIniciales.add(fechaInicial);
		    		LocalDate fecha = LocalDate.parse(fechaString,formato);
		    		fechas.add(fecha);
		    	}catch(Exception e) {
		    		log.info("La fecha no es valida");
		    	}
	      
		  objeto.setDiasContablesPagos(diasContablesPagos);
		  objeto.setFechas(fechas);
		  objeto.setFechasIniciales(fechasIniciales);  	
		  return objeto;
	
	
 }
	public Float generarNuevoMonto(BigDecimal monto,Integer tipoDeInversion,Float interes,Integer meses) {
		Float montoFinal = null;
		Double montoConversionDouble = monto.doubleValue();
		Float montoConversionFloat = montoConversionDouble.floatValue();
		Float conversionInteresDecimal = interes /100;
		switch(tipoDeInversion) {
		case 1:
			Float montoPorInteres = montoConversionFloat * conversionInteresDecimal;
			Float montoEntreYear = montoPorInteres / 365;
			Float montoPorElPlazo = montoEntreYear * meses;
			montoFinal = montoConversionFloat + montoPorElPlazo;
			break;
		case 2:
			montoFinal = monto.floatValue();
			
			break;
		case 3:
			Float moontoAMultiplicar = (montoConversionFloat * conversionInteresDecimal) / 365 ;
			montoFinal = moontoAMultiplicar * meses;
			break;
		default:
			break;
		}
		
		return montoFinal;
	}
	public Boolean obtenerSaldo(String cuentaOriginal,BigDecimal monto) {
		BigDecimal saldo = obtenerSaldo(cuentaOriginal);
		log.info("[InversionesASPLogic]:::::::"+"[OBTENER SALDO]:::::" + saldo);
		if(saldo.doubleValue() < monto.doubleValue()) {
			return false;
		}else {
			return true;
		}
		
	}
	private BigDecimal obtenerSaldo(String cuentaOriginal) {
		Double saldo = 0.0;
		MiDebitoLogic miDebitoLogic = null;
		miDebitoLogic = new MiDebitoLogic();
		try {
			AhorroCuentaMiDebitoOBJ miDebito =  new AhorroCuentaMiDebitoOBJ();
			miDebito = acmdao.buscarMiDebitoByCuenta(cuentaOriginal, ConceptosUtil.CLAVE_CONCEPTO_DATOS_CUENTA, ConceptosUtil.ESTATUS_CONCEPTO_DATOS_CUENTA);
			
			log.info(miDebito.toString());
			if(miDebito.getProveedor() != null) {
				log.info(miDebito.getProveedor());
				saldo = miDebitoLogic.consultaSaldoAhorroMiDebito(miDebito);
			}else {
				miDebito.setProveedor("NO");
				log.info(miDebito.getProveedor());
				saldo = miDebitoLogic.consultaSaldoAhorroMiDebito(miDebito);
			}
		}catch(Exception e) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Obtener Saldo ] ");
		}
		return new BigDecimal(saldo.doubleValue());
	}
	public Respuesta  realizarReinversion(NuevaInversionASPReq req) {
		initialized();
		Respuesta resp = new Respuesta();
		try {
		Boolean validacionDatosResp = Validaciones(req);
		if(validacionDatosResp == false) {
		List<TazasPlazosOBJ> tazasPlazosOBJ = new ArrayList<>();
		tazasPlazosOBJ = obtenerTazasPlazosByCanalAndPlazo(req.getPlazo());
		
		Integer usuarioCreacion = 1;
		Float montoFinalReinversion = null;
		if(tazasPlazosOBJ != null) {
			Double interesPorcentaje = (Double)tazasPlazosOBJ.get(0).getPorcentaje();
			Integer idRendimientoInsert = (Integer)tazasPlazosOBJ.get(0).getRendimientoId();
			float interesFloat = (float) interesPorcentaje.doubleValue();
			Float interesC = (float) interesFloat;
			Integer meses = req.getPlazo();
			montoFinalReinversion = generarNuevoMonto(req.getMonto(),req.getTipoReinversionId(),interesC,meses);
			Integer ahorroContratoId = invdao.ahorroContratoId(req.getCuentaOriginal());
			
			Integer idReinversion  = invdao.insertarReinversion(req.getCuentaOriginal(), ahorroContratoId, idRendimientoInsert,
					 req.getTipoReinversionId(),  montoFinalReinversion, req.getTipoReinversionId(), usuarioCreacion);
			 if(idReinversion == null) {
				 resp = respuestas(10);
					return resp;
			 }else {
				 resp = respuestas(11);
					return resp;
			 }
		}else {
			resp = respuestas(7);
			return resp;
		}
			}else {
				resp = respuestas(1);
				return resp;
			}
		}catch(Exception e) {
			resp = respuestas(12);
		    return resp;
		}
	}
	public Integer realizarDeposito_Metodo(String cuentaOriginal_S,String cuenta_S,BigDecimal monto_BD,Integer plazo_I,String referencia_S,Integer tipoCuenta_I,Integer idAhorro_I) {
		/*
		String tipoCuentaOriginal = "DEBITO";
		String tipoCuentaDestino = "AHORRO";
		*/
		String tipoCuentaOriginal = "AHORRO";
		//String tipoCuentaDestino = "DEBITO";
		String tipoCuentaDestino = "AHORRO";
		
		Respuesta rsp = realizarDeposito(cuentaOriginal_S,cuenta_S,monto_BD,tipoCuentaOriginal,tipoCuentaDestino);
		
		if(rsp.getCodigo() == 0) {
			/*
			actualizarEstatus(plazo_I,cuenta_S);
			Double gat = regresarCalculoGat(cuenta_S);
			String clabe = generaCuentaClabe(referencia_S, tipoCuenta_I, ConstantesUtil.STR_SUCURSAL_VIRTUAL);
			adao.actualizaAhorroContrato(clabe, gat, idAhorro_I);
			*/
			return 11;
			
		}else {
			return 0;
		}
		
		
	}
	public boolean validarMonto(BigDecimal montoMin,BigDecimal montoMax,BigDecimal monto) {
		if(monto.compareTo(montoMin) >= 0 && monto.compareTo(montoMax)<= 0) {
			return true;
		}else {
			return false;
		}
		
		
		
	}
	public String cuentaContable(Long idModalidad) {
		Integer capitalizarId = Math.toIntExact(idModalidad);
		String cuentaContable = adao.regresaCuentaContable(capitalizarId, 1);
		return cuentaContable;
	}
	public List<CapitalizarRendimientosOBJ> capitalizarRendimientos(Long id) {
		initialized();
		
		List<CapitalizarRendimientosOBJ> capitalizarRendimientosObjList = new ArrayList<>();
		
		try {
			capitalizarRendimientosObjList = invdao.obtenerCapitalizarRendimientosOBJ(id);
		} catch (Exception ex) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Capitalizar Rendimientos ] ");
		}
		
		return capitalizarRendimientosObjList;
		
	}
	public Float calcularInteres(Integer idRendimiento) {
		Float interes = null;
		interes = invdao.consultarRendimiento(idRendimiento);
		return interes;
	}
	private String generaCuentaClabe(String referencia, int ahorroId, String sucursalApertura) {
		try {
			GenerarClabeLogic generaClabe = new GenerarClabeLogic();
			String cuentaClabe = ConstantesInversiones.COMILLAS_VACIAS;
			cuentaClabe = generaClabe.generarClabe(referencia,
					ahorroId, sucursalApertura);
			return cuentaClabe;
		} catch (Exception e) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Genera Cuenta Clabe ] ");
			log.error(ConstantesInversiones.ERROR_GENERAR_CLAVE_CUENTA);
			return null;
		}
	}
	public Double regresarCalculoGat(String cuenta) {
		Double gat = adao.regresaCalculoGAT(cuenta);
		return gat;
	}
	public void actualizarEstatus(Integer plazo,String cuenta) {
		Calendar fechaAct = Calendar.getInstance();
		Calendar fecha = Calendar.getInstance();
		fecha.add(Calendar.DAY_OF_YEAR, plazo);
		arvdao.actualizarEstatus(ConstantesInversiones.A,
				new java.sql.Date(fechaAct.getTimeInMillis()),
				new java.sql.Date(fechaAct.getTimeInMillis()),
				new java.sql.Date(fechaAct.getTimeInMillis()),
				Integer.parseInt(ConstantesUtil.CAPITALIZAR_ID),
				cuenta, ConstantesInversiones.P);
	}
	public Respuesta realizarDeposito(String cuentaOrigen, String cuentaDestino, BigDecimal monto,String tipoCuentaOrigen,String tipoCuentaDestino) {
		AhorroTransferenciaLogic logic = new AhorroTransferenciaLogic();
		
		//log.info("Metodo de realizar deposito");
		
		String uuidGlobal = UUID.randomUUID().toString();
		Respuesta resp = new Respuesta();
		try{
			AhorroTransferenciaReqOBJ req = new AhorroTransferenciaReqOBJ();
			req.setCuentaOrigen(cuentaOrigen);
			req.setCuentaDestino(cuentaDestino);
			req.setFecha(new Date());
			req.setMonto(monto.doubleValue());
			req.setUsuarioId(Integer.valueOf(ConstantesUtil.USUARIO_SISTEMAS));
			req.setTipoCuentaOrigen(tipoCuentaOrigen);
			req.setTipoCuentaDestino(tipoCuentaDestino);
			req.setConceptoOrigen(ConstantesInversiones.TRANSFERENCIA_CUENTA + req.getCuentaDestino());
			req.setConceptoDestino(ConstantesInversiones.DEPOSITO_PLAZO + cuentaDestino);
			HeaderWS header = new HeaderWS();
			//Que llegara al siguiente punto del realizar deposito 
			//log.info(":::: " + "[HEADER] " + header + "::::" + "[REQ]" + req + "::::" + "[UUIDGLOBAL]" + uuidGlobal);
			resp = logic.procesaTransferencia(header, req, uuidGlobal);
			
		}catch(Exception e) {
			resp.setCodigo(1);
			resp.setMensaje(ConstantesInversiones.ERROR_REALIZAR_DEPOSITO_INVERSION);
		}
		

		return resp;
	}
	public Double interes(Integer rendimientoId, BigDecimal monto) {
		Double interes = adao.obtenerInteresRendimiento(rendimientoId, monto.doubleValue());
		return interes;
		
	}
	
	
	public Integer idAhorro(String cuenta, Integer idSolicitante,String contrato,Integer rendimientoId,
			String cuentaContable,String referencia, String cuentaOriginal,BigDecimal monto,Double interes) throws SQLException {
		String idSolicitantev2 = idSolicitante.toString();
		Integer idUsuario = 1;
		Integer idAhorro = adao.insertaRegistroAhorro(cuenta,idSolicitantev2, contrato, rendimientoId,
				ConstantesUtil.STR_SUCURSAL_VIRTUAL, cuentaContable, referencia, idUsuario,
				cuentaOriginal, cuenta, cuenta, null, monto.doubleValue(),ConstantesInversiones.INVERSION_POR_ASP_PAGO,interes,1);
		return idAhorro;
	}
	public String referencia(String cuenta) {
		String referencia = adao.generaAhorroReferencia(cuenta,Integer.valueOf(ConstantesUtil.USUARIO_SISTEMAS) , ConstantesUtil.STR_SUCURSAL_VIRTUAL);
		return referencia;
	}
	public Integer tipoCuentaCero(String cuentaOriginal) {
		
		Integer tipoCuenta = adao.tipoCuentaCero(cuentaOriginal);
		if(tipoCuenta == 1) {
			return null;
		}else {
			return tipoCuenta;
		}
		
	}
	

	
	public String existeEnProcrea(Integer idSolicitante) {
		long cuentaExistente = 0;
		cuentaExistente = adao.existeEnProcrea(idSolicitante.toString());
		
		String cuenta = ConstantesInversiones.COMILLAS_VACIAS;
		if(cuentaExistente == 0) {
			cuenta = generarCuentaNueva();
		}else {
			cuentaExistente += 1;
			cuenta = String.format(ConstantesInversiones.FORMATO, Long.valueOf(cuentaExistente));
		}
		return cuenta;
	}
	
	public String obtenerContrato(Integer var) {
		String contrato = ConstantesInversiones.COMILLAS_VACIAS;
		contrato = adao.obtenerContratoAhorro(var.toString());
		if(contrato.equals(ConstantesInversiones.COMILLAS_VACIAS) || contrato.isEmpty()) {
			contrato = generarContrato();
		}
		return contrato;
		
	}
	private String generarContrato() {
		String secuencia = adao.obtenerSecuenciaContrato();
		String secV2 = ConstantesInversiones.NUEVE_CEROS + secuencia;
		secV2 = secV2.substring(secV2.length() - 10, secV2.length());
		String contrato = secV2;
		return contrato;
		
	}
	private String generarCuentaNueva() {
		String cuenta = null;
		cuenta = ConstantesInversiones.CINCO_CEROS + adao.obtenerSecuenciaCuenta();
		cuenta = cuenta.substring(cuenta.length() -5,cuenta.length());
		String S = ConstantesInversiones.TRES_CEROS + ConstantesUtil.SUCURSAL_VIRTUAL;
		S = S.substring(S.length()-3,S.length());
		cuenta = S + cuenta;
		cuenta = cuenta + ConstantesInversiones.DOS_CEROS;
		long nuevaCuenta;
		nuevaCuenta = (Long.parseLong(cuenta)+ 1);
		cuenta = String.format(ConstantesInversiones.FORMATO, Long.valueOf(nuevaCuenta));
		return cuenta;
	}
	
	public Boolean Validaciones(NuevaInversionASPReq req) {
		 if(
			validarString(req.getNombreInversion())&&
			validarStrings(req.getCuentaOriginal())&&
			validarIntegers(req.getIdRendimiento())&&
			validarIntegers(req.getPlazo())&&
			validarBigDecimals(req.getMonto())&&
			validarModalidad(req.getModalidad())
			) {
			 
			 return true;
		 }else {
			 return false;
		 }
		 
		 
	}
	public Boolean validarStrings(String var) {
		boolean respValid = validarCadenas(var);
		if(respValid == false || var.equals(ConstantesInversiones.COMILLAS_VACIAS) || var == null ) {
			return false;
		}else {
			return true;
		}
	}
	public Boolean validarString(String var) {
		if( var.equals(ConstantesInversiones.COMILLAS_VACIAS) || var == null ) {
			return false;
		}else {
			return true;
		}
	}
	public Integer validarDescripciones(String var) {
		Integer Codigo = null;
		if(var.equals(ConstantesInversiones.COMILLAS_VACIAS) || var == null) {
			Codigo = 1;
		}else {
			Codigo = 0;
		}
		return Codigo;
	}
	public Boolean validarIntegers(Integer var) {
		if(var <= 0 || var == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public Boolean validarModalidad(Integer var) {
		if(var <= 0 || var == null || var > 2) {
			return false;
		}else {
			return true;
		}
	}
	
	public Integer validarFloats(Float var) {
		Integer Codigo = null;
		if(var <= 0.0 || var == null) {
			Codigo = 1;
		}else {
			Codigo = 0;
		}
		return Codigo;
	}
	public Boolean validarBigDecimals(BigDecimal var) {
		if( var == null || var.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean validarCadenas(String cadena) {
    	boolean esValida = Pattern.matches(ConstantesInversiones.REGEX, cadena);
    	return esValida;
    }
	
	
	

	public List<TazasPlazosOBJ> obtenerTazasPlazos(String canal) {
		initialized();
		
		try {
			List<TazasPlazosOBJ> tazasPlazosOBJ = invdao.obtenerTazasPlazos(canal);
			return tazasPlazosOBJ;
		} catch (Exception ex) {
			log.error("Error " + " [ Inversiones ASP Logic ] " + " [ Obtener Tazas Plazos ] ");
		}
		
		return null;
		
	}
	
	
	
	
}
