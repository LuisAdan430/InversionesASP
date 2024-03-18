package net.cero.spring.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import net.cero.data.CapitalizarRendimientosOBJ;
import net.cero.data.ObtenerInfoAhorroContrato;
import net.cero.data.TazasPlazosOBJ;

public class InversionesDAO {
	private static final Logger log = Logger.getLogger(InversionesDAO.class);
	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplatePr;
	private String insertarReinversion;
	private String sigSecAhreinversion;
	private String consultarRendimiento;
	private String consultarMeses;
	private String consultarCuentasCero;
	private String consultarCuentasProcrea;
	private String capitalizarRendimientos;
	private String tazasPlazos;
	private String tazasPlazosByCanalAndPlazo;
	private String obtenerInformacionTablaAhorroContrato;
	private String plazoReinversion;
	private String porcentaje;
	private String ahorroContratoId;
	
	public Integer ahorroContratoId(String cuenta) {
		Integer contratoId = null;
		try {
			contratoId = jdbcTemplatePr.queryForObject(ahorroContratoId,Integer.class,cuenta);
		 } catch (DataAccessException e) {
		        log.error("Error " + " [ InversionesDAO ] " + " [ Ahorro Contrato Id ] "+ e.getMessage());
		        throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Ahorro Contrato Id ] ", e);
		 }
		return contratoId;
	}
	public Integer plazoReinversionM(Integer rendimientoId) {
		Integer plazoReinversionV = null;
		try {
			plazoReinversionV = jdbcTemplatePr.queryForObject(plazoReinversion,Integer.class,rendimientoId);
		
		} catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Plazo Reinversion M ] "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Plazo Reinversion M ] ", e);
		}
		return  plazoReinversionV;
	}
	
	public List<ObtenerInfoAhorroContrato> ahorroContrato(String nombreInversion,String cuenta) {
		try {
			List<ObtenerInfoAhorroContrato> infoTablaAhorroContrato =
					jdbcTemplatePr.query(obtenerInformacionTablaAhorroContrato,
							new Object[] {cuenta}, 
							new BeanPropertyRowMapper<ObtenerInfoAhorroContrato>(ObtenerInfoAhorroContrato.class));
			return infoTablaAhorroContrato;		
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Ahorro Contrato ]  "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Ahorro Contrato ]  ", e);
		}
		
	}
	
	
	
	  public List<TazasPlazosOBJ> obtenerTazasPlazosByCanalAndPlazo(String canal, Integer plazo) {
		try {
			List<TazasPlazosOBJ> tazasPlazosList = 
					jdbcTemplatePr.query(tazasPlazosByCanalAndPlazo, 
							new Object[] {canal, plazo}, 
							new BeanPropertyRowMapper<TazasPlazosOBJ>(TazasPlazosOBJ.class));
			
			return tazasPlazosList;
			
		
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Obtener Tazas Plazos By Canal And Plazo ]  "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Obtener Tazas Plazos By Canal And Plazo ]  ", e);
		}
		
		
	}
	
	public Double porcentajeDAO(Integer rendimientoId) {
		Double porcentajeReturn = 0.0;
		try {
			porcentajeReturn = jdbcTemplatePr.queryForObject(porcentaje,Double.class,rendimientoId);
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Porcentaje DAO ]  "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Porcentaje DAO ]  ", e);
		}
		return  porcentajeReturn;
	}
	
	
	public List<TazasPlazosOBJ> obtenerTazasPlazos(String canal) {
		
		try {
			List<TazasPlazosOBJ> tazasPlazosList = 
					jdbcTemplatePr.query(tazasPlazos, 
							new Object[] {canal}, 
							new BeanPropertyRowMapper<TazasPlazosOBJ>(TazasPlazosOBJ.class));
			
			return tazasPlazosList;
			
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Obtener Tazas Plazos   "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + " [ Porcentaje DAO ]  ", e);
		}
		
	}
	
	public String getTazasPlazos() {
		return tazasPlazos;
	}


	public void setTazasPlazos(String tazasPlazos) {
		this.tazasPlazos = tazasPlazos;
	}


	public List<CapitalizarRendimientosOBJ> obtenerCapitalizarRendimientosOBJ(Long id) {
		
		List<CapitalizarRendimientosOBJ> capitalizarRendimientosObjList = new ArrayList<>();
		
		try {
			capitalizarRendimientosObjList = 
					jdbcTemplatePr.query(capitalizarRendimientos, 
							new Object[] {id},
							new BeanPropertyRowMapper<CapitalizarRendimientosOBJ>(CapitalizarRendimientosOBJ.class));
			
		
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + "  [ Obtener Capitalizar Rendimientos OBJ ]  "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + "  [ Obtener Capitalizar Rendimientos OBJ ]  ", e);
		}
		
		return capitalizarRendimientosObjList;
		
	}
	
	
	public String getCapitalizarRendimientos() {
		return capitalizarRendimientos;
	}

	public void setCapitalizarRendimientos(String capitalizarRendimientos) {
		this.capitalizarRendimientos = capitalizarRendimientos;
	}

	public String[] obtenerCuentasProcrea() {
		String [] cuentasProcreaAprovadas = jdbcTemplatePr.queryForList(consultarCuentasProcrea,String.class).toArray(new String[0]);
		return cuentasProcreaAprovadas;
	
	}
	
	public String[] obtenerCuentasCero() {
		String [] cuentasCeroAprovadas = jdbcTemplate.queryForList(consultarCuentasCero,String.class).toArray(new String[0]);
		return cuentasCeroAprovadas;
	
	}
	
	public Integer sigSecAhreinversion() {
		try {
			return jdbcTemplatePr.queryForObject(sigSecAhreinversion, Integer.class);
		
		}catch (DataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + "  [ Sig Sec AhReinversion ]]  "+ e.getMessage());
			throw new RuntimeException("Error " + " [ InversionesDAO ] " + "  [ Sig Sec AhReinversion ]  ", e);
		}
	}
	
	public Float consultarRendimiento(Integer idRendimiento) {
		Float resultado = null;
		try {
			resultado = jdbcTemplatePr.queryForObject(consultarRendimiento, Float.class,idRendimiento);
		
		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ consultar Rendimiento ] ");
		}
		return resultado;
		
	}
	public Integer consultarMeses(Integer idRendimiento) {
		Integer resultado = null;
		try {
			resultado = jdbcTemplatePr.queryForObject(consultarMeses, Integer.class,idRendimiento);
		
		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ consultar Meses  ] ");
		}
		return resultado;
		
	}
	
	public Integer insertarReinversion(String cuenta,Integer ahorroContratoId,Integer rendimiento
			,Integer tipoCapitalizarId,Float monto,Integer tipoReinversionId,Integer usuarioCreacion) throws SQLException{
		
		//Integer id = sigSecAhreinversion();
		Integer result = 0;
		try {
			result = jdbcTemplatePr.queryForObject(insertarReinversion, Integer.class,sigSecAhreinversion(),cuenta,ahorroContratoId,rendimiento,tipoCapitalizarId,
					monto,tipoReinversionId,usuarioCreacion);
			
			
			log.info("Se realizo la insercion");
			
			
		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ InversionesDAO ] " + " [ Insertar Reinversion ] ");
		}
		
		
		return result;
		
	}


	public String getConsultarMeses() {
		return consultarMeses;
	}
	public void setConsultarMeses(String consultarMeses) {
		this.consultarMeses = consultarMeses;
	}
	public String getConsultarRendimiento() {
		return consultarRendimiento;
	}
	public void setConsultarRendimiento(String consultarRendimiento) {
		this.consultarRendimiento = consultarRendimiento;
	}
	public static Logger getLog() {
		return log;
	}
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getInsertarReinversion() {
		return insertarReinversion;
	}
	public void setInsertarReinversion(String insertarReinversion) {
		this.insertarReinversion = insertarReinversion;
	}
	public String getSigSecAhreinversion() {
		return sigSecAhreinversion;
	}
    public void setSigSecAhreinversion(String sigSecAhreinversion) {
		this.sigSecAhreinversion = sigSecAhreinversion;
	}
    public String getConsultarCuentasCero() {
		return consultarCuentasCero;
	}
	public void setConsultarCuentasCero(String consultarCuentasCero) {
		this.consultarCuentasCero = consultarCuentasCero;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public String getConsultarCuentasProcrea() {
		return consultarCuentasProcrea;
	}
	public void setConsultarCuentasProcrea(String consultarCuentasProcrea) {
		this.consultarCuentasProcrea = consultarCuentasProcrea;
	}
	public String getObtenerInformacionTablaAhorroContrato() {
		return obtenerInformacionTablaAhorroContrato;
	}
	public void setObtenerInformacionTablaAhorroContrato(String obtenerInformacionTablaAhorroContrato) {
		this.obtenerInformacionTablaAhorroContrato = obtenerInformacionTablaAhorroContrato;
	}
	public String getPlazoReinversion() {
		return plazoReinversion;
	}
	public void setPlazoReinversion(String plazoReinversion) {
		this.plazoReinversion = plazoReinversion;
	}
	public String getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	public String getTazasPlazosByCanalAndPlazo() {
		return tazasPlazosByCanalAndPlazo;
	}
	public void setTazasPlazosByCanalAndPlazo(String tazasPlazosByCanalAndPlazo) {
		this.tazasPlazosByCanalAndPlazo = tazasPlazosByCanalAndPlazo;
	}
	public String getAhorroContratoId() {
		return ahorroContratoId;
	}
	public void setAhorroContratoId(String ahorroContratoId) {
		this.ahorroContratoId = ahorroContratoId;
	}
	
	
	
    
}
