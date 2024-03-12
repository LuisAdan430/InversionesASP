package net.cero.spring.dao;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;



public class InversionesPagosDAO {
	private static final Logger log = Logger.getLogger(InversionesPagosDAO.class);
	private JdbcTemplate jdbcTemplate;
	private String consultaAhProvision;
	private String insertarPago;
	private JdbcTemplate jdbcTemplatePr;
	private String consultarDatosAhorroContrato;
	private String desactivarContrato;
	public List<List<Object>> pagosHoy(LocalDate fechaHoyL){
		Integer id= null;
		Integer cuentaId = null;
		Date fechaCorte = null;
		BigDecimal tasa = null;
		Integer dias = null;
		BigDecimal monto = null;
		String poliza = null;
		Date fechaCreacion = null;
		Integer usuarioCreacion = null;
		Date fechaModificacion = null;
		Integer usuarioModificacion = null;
		Boolean estatus_activo = null;
		String idAhorro = null;
	
		List<List<Object>> infoPagos = new ArrayList<>();
		try {
			List<Map<String,Object>> rows = jdbcTemplate.queryForList(consultaAhProvision,fechaHoyL.toString());
			for(Map<String,Object> row: rows) {
				List<Object> infoPagosHoy = new ArrayList<>();
				id= (Integer) row.get("id"); cuentaId = (Integer) row.get("cuenta_id"); fechaCorte = (Date) row.get("fecha_corte"); tasa = (BigDecimal) row.get("tasa");
				dias = (Integer) row.get("dias"); monto = (BigDecimal) row.get("monto"); poliza = (String) row.get("poliza"); fechaCreacion = (Date) row.get("fecha_creacion");
				usuarioCreacion = (Integer) row.get("usuario_creacion"); fechaModificacion = (Date) row.get("fecha_modificacion"); usuarioModificacion = (Integer) row.get("usuario_modificacion");
				estatus_activo = (Boolean) row.get("estatus_activo"); idAhorro = (String) row.get("idahorro");
				
				infoPagosHoy.add(id); infoPagosHoy.add(cuentaId); infoPagosHoy.add(fechaCorte); infoPagosHoy.add(tasa); infoPagosHoy.add(dias); infoPagosHoy.add(monto);
				infoPagosHoy.add(poliza); infoPagosHoy.add(fechaCreacion); infoPagosHoy.add(usuarioCreacion); infoPagosHoy.add(fechaModificacion); infoPagosHoy.add(usuarioModificacion);
				infoPagosHoy.add(estatus_activo); infoPagosHoy.add(idAhorro);
				infoPagos.add(infoPagosHoy);
			}
			return infoPagos;
		}catch(Exception e) {
			log.error("Error " + " [ Inversiones Pagos DAO ] " + " [ pagos Hoy ] ");
			return null;
		}
	}
	public List<String> informacionAhorroContrato(String idAhorroContrato){
		String cuentaPadre = "";
		String cuenta = "";
		String referencia = "";
		Map<String,Object> row;
		List<String> infoAhorroContrato = new ArrayList<>();
		try {
			 row = jdbcTemplatePr.queryForMap(consultarDatosAhorroContrato,idAhorroContrato);
			 cuentaPadre =  (String) row.get("cuenta_padre");
			 cuenta = (String) row.get("cuenta"); 
			 referencia = (String) row.get("referencia");
			 infoAhorroContrato.add(cuentaPadre);
			 infoAhorroContrato.add(cuenta);
			 infoAhorroContrato.add(referencia);
			 return infoAhorroContrato;
		 }catch(Exception e) {
			 log.error("Error " + " [ Inversiones Pagos DAO ] " + " [ Informacion Ahorro Contrato ] ");
			 return null;
		 }
	}
	
	public List<Boolean> editarPagos(Date fechaModificacion,Integer usuarioModificacion,String idAhorro,LocalDate fechaHoy) {
		//Boolean respuesta = false;
		try{
			//respuesta = jdbcTemplate.queryForObject(desactivarContrato,Boolean.class,fechaModificacion,usuarioModificacion,idAhorro);
			//log.info("se ha realizado la actualizacion del contrato");
			
			List<Boolean> resultados = jdbcTemplate.query(desactivarContrato,(rs,rowNum) -> {
				return rs.getBoolean(1);
			},fechaModificacion,usuarioModificacion,idAhorro,fechaHoy.toString() );
			/*
			if(resultados.isEmpty()) {
				log.info("No se encontraron resultados");
			}else {
				for(Boolean resultado : resultados) {
					log.info("Resultado: " + resultado);
				}
			}
			*/
			return resultados;
		}catch(EmptyResultDataAccessException e) {
			log.error("Error " + " [ Inversiones Pagos DAO ] " + " [ Editar Pagos ] ");
			return null;
		}
	}
	public Boolean insertarPagos(Integer cuentaId,String poliza,String idAhorro,Date fechaCorte,Double tasa,Integer dias,Double monto,Date fechaCreacion,
			Integer usuarioCreacion,Boolean estatusActivo) {
			Boolean respuesta = false ;
		try {
			respuesta = jdbcTemplate.queryForObject(insertarPago,Boolean.class,cuentaId,poliza,idAhorro,fechaCorte,tasa,dias,monto,fechaCreacion,usuarioCreacion,estatusActivo);
			//log.info("Se realizo la insercion en pagos por modalidad");
			return respuesta;
			
		}catch(EmptyResultDataAccessException e) {
			log.error("Error " + " [ Inversiones Pagos DAO ] " + " [ Insertar Pagos ] ");
			return respuesta;
		}
		
	}
	
	
	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}
	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}
	public String getConsultarDatosAhorroContrato() {
		return consultarDatosAhorroContrato;
	}
	public void setConsultarDatosAhorroContrato(String consultarDatosAhorroContrato) {
		this.consultarDatosAhorroContrato = consultarDatosAhorroContrato;
	}
	public String getInsertarPago() {
		return insertarPago;
	}

	public void setInsertarPago(String insertarPago) {
		this.insertarPago = insertarPago;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public String getConsultaAhProvision() {
		return consultaAhProvision;
	}
	public void setConsultaAhProvision(String consultaAhProvision) {
		this.consultaAhProvision = consultaAhProvision;
	}
	public static Logger getLog() {
		return log;
	}
	public String getDesactivarContrato() {
		return desactivarContrato;
	}
	public void setDesactivarContrato(String desactivarContrato) {
		this.desactivarContrato = desactivarContrato;
	}
	
	
	
}
