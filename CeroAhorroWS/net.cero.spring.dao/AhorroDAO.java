package net.cero.spring.dao;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import net.cero.data.AhorroContrato;
import net.cero.data.AhorroCuenta;
import net.cero.data.AhorroOBJ;
import net.cero.data.ConceptoPld;
import net.cero.data.DatosCuentaInversionCeroOBJ;
import net.cero.data.DatosCuentaInversionProcreaOBJ;
import net.cero.data.DebitoOBJ;
import net.cero.data.InformacionPldCuentaAhorro;
import net.cero.data.SaldoOBJ;
import net.cero.data.Solicitante;
import net.cero.data.TasaRendimientoOBJ;

public class AhorroDAO {

	private static final Logger log = Logger.getLogger(AhorroDAO.class);

	private JdbcTemplate jdbcTemplate;
	private JdbcTemplate jdbcTemplatePr;
	private JdbcTemplate jdbcTemplateSti;
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	private NamedParameterJdbcTemplate namedJdbcTemplatePr;
	private String buscarAhorroProcrea;
	private String buscarAhorroCero;
	private String buscarAhorroProcreaSolicitante;
	private String buscarAhorroCeroSolicitante;
	private String buscarSaldoDisponibleProcrea;
	private String buscarSaldoDisponibleCero;
	private String buscarTarjetaDebito;
	private String buscarAhorroProcreaClabe;
	private String buscarAhorroCeroClabe;
	private String buscarAhorroPlastico;
	private String buscarAhorroPlasticoCuenta;
	private String obtenerSecuenciaCuenta;
	private String generaSecuenciaTabla;
	private String regresaCuentaContable;
	private String generaAhorroReferencia;
	private String regresaCalculoGAT;
	private String insertaRegistroAhorro;
	private String regresaDatosAhorroContrato;
	private String ahorroCopiarRendimiento;
	private String insertaAhorroContratoDatos;
	private String actualizaAhorroContrato;
	private String buscarAhorroProcreaCuentaTipo;
	private String buscarAhorroCeroCuentaTipo;
	private String obtenerCuentaContable;
	private String obtenerSecuenciaContrato;
	private String ahorroGeneraReferencia;
	private String buscarCuentasCliente;
	private String nuevoAhorroContratoS;
	private String leerSaldoActualByCuentaTra;
	private String leerByCuentaByFechaSaldoTra;
	private String insertarEnAhorroContrato;
	private String buscarInfoSolicitante;
	private String buscarInfoCuenta;
	private String buscarConceptosPld;
	private String obtenerMoneda;
	private String obtenerDatosCuentaAhorro;
	private String obtenerIdCuenta;
	private String obtenerContrato;
	private String tipoCuentaCero;
	private String obtenerCuenta;
	private String obtenerDatosCuentaInversion;
	private String obtenerDatosProcreaCuentaInversion;
	private String existeEnIzel;
	
	


	public String getExisteEnIzel() {
		return existeEnIzel;
	}

	public void setExisteEnIzel(String existeEnIzel) {
		this.existeEnIzel = existeEnIzel;
	}

	public String getObtenerDatosProcreaCuentaInversion() {
		return obtenerDatosProcreaCuentaInversion;
	}

	public void setObtenerDatosProcreaCuentaInversion(String obtenerDatosProcreaCuentaInversion) {
		this.obtenerDatosProcreaCuentaInversion = obtenerDatosProcreaCuentaInversion;
	}

	public String getObtenerDatosCuentaInversion() {
		return obtenerDatosCuentaInversion;
	}

	public void setObtenerDatosCuentaInversion(String obtenerDatosCuentaInversion) {
		this.obtenerDatosCuentaInversion = obtenerDatosCuentaInversion;
	}

	public String getObtenerCuenta() {
		return obtenerCuenta;
	}

	public void setObtenerCuenta(String obtenerCuenta) {
		this.obtenerCuenta = obtenerCuenta;
	}

	public String getTipoCuentaCero() {
		return tipoCuentaCero;
	}

	public void setTipoCuentaCero(String tipoCuentaCero) {
		this.tipoCuentaCero = tipoCuentaCero;
	}

	public String getObtenerContrato() {
		return obtenerContrato;
	}

	public void setObtenerContrato(String obtenerContrato) {
		this.obtenerContrato = obtenerContrato;
	}

	public String getObtenerIdCuenta() {
		return obtenerIdCuenta;
	}

	public void setObtenerIdCuenta(String obtenerIdCuenta) {
		this.obtenerIdCuenta = obtenerIdCuenta;
	}

	public String getObtenerDatosCuentaAhorro() {
		return obtenerDatosCuentaAhorro;
	}

	public void setObtenerDatosCuentaAhorro(String obtenerDatosCuentaAhorro) {
		this.obtenerDatosCuentaAhorro = obtenerDatosCuentaAhorro;
	}

	public String getObtenerMoneda() {
		return obtenerMoneda;
	}

	public void setObtenerMoneda(String obtenerMoneda) {
		this.obtenerMoneda = obtenerMoneda;
	}

	public String getBuscarConceptosPld() {
		return buscarConceptosPld;
	}

	public void setBuscarConceptosPld(String buscarConceptosPld) {
		this.buscarConceptosPld = buscarConceptosPld;
	}

	public String getBuscarInfoCuenta() {
		return buscarInfoCuenta;
	}

	public void setBuscarInfoCuenta(String buscarInfoCuenta) {
		this.buscarInfoCuenta = buscarInfoCuenta;
	}

	public String getBuscarInfoSolicitante() {
		return buscarInfoSolicitante;
	}

	public void setBuscarInfoSolicitante(String buscarInfoSolicitante) {
		this.buscarInfoSolicitante = buscarInfoSolicitante;
	}

	public String getInsertarEnAhorroContrato() {
		return insertarEnAhorroContrato;
	}

	public void setInsertarEnAhorroContrato(String insertarEnAhorroContrato) {
		this.insertarEnAhorroContrato = insertarEnAhorroContrato;
	}

	public String getLeerByCuentaByFechaSaldoTra() {
		return leerByCuentaByFechaSaldoTra;
	}

	public void setLeerByCuentaByFechaSaldoTra(String leerByCuentaByFechaSaldoTra) {
		this.leerByCuentaByFechaSaldoTra = leerByCuentaByFechaSaldoTra;
	}

	public String getLeerSaldoActualByCuentaTra() {
		return leerSaldoActualByCuentaTra;
	}

	public void setLeerSaldoActualByCuentaTra(String leerSaldoActualByCuentaTra) {
		this.leerSaldoActualByCuentaTra = leerSaldoActualByCuentaTra;
	}

	public String getNuevoAhorroContratoS() {
		return nuevoAhorroContratoS;
	}

	public void setNuevoAhorroContratoS(String nuevoAhorroContratoS) {
		this.nuevoAhorroContratoS = nuevoAhorroContratoS;
	}

	private String sigSecAhorroContrato;
	private String ahorroCopiaRendimientos;
	private String actualizaAhorroRendimientosVigntes;
	private String actualizaCuentaClabe;
	private String calculoGatByCuenta;
	private String actualizaGatAhorroContrato;
	private String borraAhorroContrato;
	private String sigSecAcceso;
	private String generaTarjetaAhorro;
	private String obtenerInteresRendimiento;
	private String buscarTasasRendimiento;
	private String actualizarEstatusPan;
	private String buscarAhorroProcreaClabeCuenta;
	private String buscarAhorroCeroClabeCuenta;
	private String buscarCuentaCero;
	private String obtenerInfoCuenta;
	private String updateTelefonoIzel;
	private String consultarNip;
	
	public String getUpdateTelefonoIzel() {
		return updateTelefonoIzel;
	}

	public void setUpdateTelefonoIzel(String updateTelefonoIzel) {
		this.updateTelefonoIzel = updateTelefonoIzel;
	}

	public String getObtenerInfoCuenta() {
		return obtenerInfoCuenta;
	}

	public void setObtenerInfoCuenta(String obtenerInfoCuenta) {
		this.obtenerInfoCuenta = obtenerInfoCuenta;
	}

	public String getBuscarCuentaCero() {
		return buscarCuentaCero;
	}

	public void setBuscarCuentaCero(String buscarCuentaCero) {
		this.buscarCuentaCero = buscarCuentaCero;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplatePr() {
		return jdbcTemplatePr;
	}

	public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
		this.jdbcTemplatePr = jdbcTemplatePr;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplatePr() {
		return namedJdbcTemplatePr;
	}

	public void setNamedJdbcTemplatePr(NamedParameterJdbcTemplate namedJdbcTemplatePr) {
		this.namedJdbcTemplatePr = namedJdbcTemplatePr;
	}

	public String getBuscarAhorroProcrea() {
		return buscarAhorroProcrea;
	}

	public void setBuscarAhorroProcrea(String buscarAhorroProcrea) {
		this.buscarAhorroProcrea = buscarAhorroProcrea;
	}

	public String getBuscarAhorroCero() {
		return buscarAhorroCero;
	}

	public void setBuscarAhorroCero(String buscarAhorroCero) {
		this.buscarAhorroCero = buscarAhorroCero;
	}

	public String getBuscarAhorroProcreaSolicitante() {
		return buscarAhorroProcreaSolicitante;
	}

	public void setBuscarAhorroProcreaSolicitante(String buscarAhorroProcreaSolicitante) {
		this.buscarAhorroProcreaSolicitante = buscarAhorroProcreaSolicitante;
	}

	public String getBuscarAhorroCeroSolicitante() {
		return buscarAhorroCeroSolicitante;
	}

	public void setBuscarAhorroCeroSolicitante(String buscarAhorroCeroSolicitante) {
		this.buscarAhorroCeroSolicitante = buscarAhorroCeroSolicitante;
	}

	public String getBuscarSaldoDisponibleProcrea() {
		return buscarSaldoDisponibleProcrea;
	}

	public void setBuscarSaldoDisponibleProcrea(String buscarSaldoDisponibleProcrea) {
		this.buscarSaldoDisponibleProcrea = buscarSaldoDisponibleProcrea;
	}

	public String getBuscarSaldoDisponibleCero() {
		return buscarSaldoDisponibleCero;
	}

	public void setBuscarSaldoDisponibleCero(String buscarSaldoDisponibleCero) {
		this.buscarSaldoDisponibleCero = buscarSaldoDisponibleCero;
	}

	public String getBuscarTarjetaDebito() {
		return buscarTarjetaDebito;
	}

	public void setBuscarTarjetaDebito(String buscarTarjetaDebito) {
		this.buscarTarjetaDebito = buscarTarjetaDebito;
	}

	public String getBuscarAhorroProcreaClabe() {
		return buscarAhorroProcreaClabe;
	}

	public void setBuscarAhorroProcreaClabe(String buscarAhorroProcreaClabe) {
		this.buscarAhorroProcreaClabe = buscarAhorroProcreaClabe;
	}

	public String getBuscarAhorroCeroClabe() {
		return buscarAhorroCeroClabe;
	}

	public void setBuscarAhorroCeroClabe(String buscarAhorroCeroClabe) {
		this.buscarAhorroCeroClabe = buscarAhorroCeroClabe;
	}

	public String getBuscarAhorroPlastico() {
		return buscarAhorroPlastico;
	}

	public void setBuscarAhorroPlastico(String buscarAhorroPlastico) {
		this.buscarAhorroPlastico = buscarAhorroPlastico;
	}

	public String getObtenerSecuenciaCuenta() {
		return obtenerSecuenciaCuenta;
	}

	public void setObtenerSecuenciaCuenta(String obtenerSecuenciaCuenta) {
		this.obtenerSecuenciaCuenta = obtenerSecuenciaCuenta;
	}

	public String getObtenerCuentaContable() {
		return obtenerCuentaContable;
	}

	public void setObtenerCuentaContable(String obtenerCuentaContable) {
		this.obtenerCuentaContable = obtenerCuentaContable;
	}

	public String getObtenerSecuenciaContrato() {
		return obtenerSecuenciaContrato;
	}

	public void setObtenerSecuenciaContrato(String obtenerSecuenciaContrato) {
		this.obtenerSecuenciaContrato = obtenerSecuenciaContrato;
	}

	public String getAhorroGeneraReferencia() {
		return ahorroGeneraReferencia;
	}

	public void setAhorroGeneraReferencia(String ahorroGeneraReferencia) {
		this.ahorroGeneraReferencia = ahorroGeneraReferencia;
	}

	public String getBuscarCuentasCliente() {
		return buscarCuentasCliente;
	}

	public void setBuscarCuentasCliente(String buscarCuentasCliente) {
		this.buscarCuentasCliente = buscarCuentasCliente;
	}

	public String getSigSecAhorroContrato() {
		return sigSecAhorroContrato;
	}

	public void setSigSecAhorroContrato(String sigSecAhorroContrato) {
		this.sigSecAhorroContrato = sigSecAhorroContrato;
	}

	public String getAhorroCopiaRendimientos() {
		return ahorroCopiaRendimientos;
	}

	public void setAhorroCopiaRendimientos(String ahorroCopiaRendimientos) {
		this.ahorroCopiaRendimientos = ahorroCopiaRendimientos;
	}

	public String getActualizaAhorroRendimientosVigntes() {
		return actualizaAhorroRendimientosVigntes;
	}

	public void setActualizaAhorroRendimientosVigntes(String actualizaAhorroRendimientosVigntes) {
		this.actualizaAhorroRendimientosVigntes = actualizaAhorroRendimientosVigntes;
	}

	public String getActualizaCuentaClabe() {
		return actualizaCuentaClabe;
	}

	public void setActualizaCuentaClabe(String actualizaCuentaClabe) {
		this.actualizaCuentaClabe = actualizaCuentaClabe;
	}

	public String getCalculoGatByCuenta() {
		return calculoGatByCuenta;
	}

	public void setCalculoGatByCuenta(String calculoGatByCuenta) {
		this.calculoGatByCuenta = calculoGatByCuenta;
	}

	public String getActualizaGatAhorroContrato() {
		return actualizaGatAhorroContrato;
	}

	public void setActualizaGatAhorroContrato(String actualizaGatAhorroContrato) {
		this.actualizaGatAhorroContrato = actualizaGatAhorroContrato;
	}

	public String getBorraAhorroContrato() {
		return borraAhorroContrato;
	}

	public void setBorraAhorroContrato(String borraAhorroContrato) {
		this.borraAhorroContrato = borraAhorroContrato;
	}

	public String getSigSecAcceso() {
		return sigSecAcceso;
	}

	public void setSigSecAcceso(String sigSecAcceso) {
		this.sigSecAcceso = sigSecAcceso;
	}

	public String getGeneraTarjetaAhorro() {
		return generaTarjetaAhorro;
	}

	public void setGeneraTarjetaAhorro(String generaTarjetaAhorro) {
		this.generaTarjetaAhorro = generaTarjetaAhorro;
	}

	public String getBuscarAhorroPlasticoCuenta() {
		return buscarAhorroPlasticoCuenta;
	}

	public void setBuscarAhorroPlasticoCuenta(String buscarAhorroPlasticoCuenta) {
		this.buscarAhorroPlasticoCuenta = buscarAhorroPlasticoCuenta;
	}

	public String getGeneraSecuenciaTabla() {
		return generaSecuenciaTabla;
	}

	public void setGeneraSecuenciaTabla(String generaSecuenciaTabla) {
		this.generaSecuenciaTabla = generaSecuenciaTabla;
	}

	public String getRegresaCuentaContable() {
		return regresaCuentaContable;
	}

	public void setRegresaCuentaContable(String regresaCuentaContable) {
		this.regresaCuentaContable = regresaCuentaContable;
	}

	public String getGeneraAhorroReferencia() {
		return generaAhorroReferencia;
	}

	public void setGeneraAhorroReferencia(String generaAhorroReferencia) {
		this.generaAhorroReferencia = generaAhorroReferencia;
	}

	public String getRegresaCalculoGAT() {
		return regresaCalculoGAT;
	}

	public void setRegresaCalculoGAT(String regresaCalculoGAT) {
		this.regresaCalculoGAT = regresaCalculoGAT;
	}

	public String getInsertaRegistroAhorro() {
		return insertaRegistroAhorro;
	}

	public void setInsertaRegistroAhorro(String insertaRegistroAhorro) {
		this.insertaRegistroAhorro = insertaRegistroAhorro;
	}

	public String getRegresaDatosAhorroContrato() {
		return regresaDatosAhorroContrato;
	}

	public void setRegresaDatosAhorroContrato(String regresaDatosAhorroContrato) {
		this.regresaDatosAhorroContrato = regresaDatosAhorroContrato;
	}

	public String getAhorroCopiarRendimiento() {
		return ahorroCopiarRendimiento;
	}

	public void setAhorroCopiarRendimiento(String ahorroCopiarRendimiento) {
		this.ahorroCopiarRendimiento = ahorroCopiarRendimiento;
	}

	public String getInsertaAhorroContratoDatos() {
		return insertaAhorroContratoDatos;
	}

	public void setInsertaAhorroContratoDatos(String insertaAhorroContratoDatos) {
		this.insertaAhorroContratoDatos = insertaAhorroContratoDatos;
	}

	public String getActualizaAhorroContrato() {
		return actualizaAhorroContrato;
	}

	public void setActualizaAhorroContrato(String actualizaAhorroContrato) {
		this.actualizaAhorroContrato = actualizaAhorroContrato;
	}

	public String getBuscarAhorroProcreaCuentaTipo() {
		return buscarAhorroProcreaCuentaTipo;
	}

	public void setBuscarAhorroProcreaCuentaTipo(String buscarAhorroProcreaCuentaTipo) {
		this.buscarAhorroProcreaCuentaTipo = buscarAhorroProcreaCuentaTipo;
	}

	public String getBuscarAhorroCeroCuentaTipo() {
		return buscarAhorroCeroCuentaTipo;
	}

	public void setBuscarAhorroCeroCuentaTipo(String buscarAhorroCeroCuentaTipo) {
		this.buscarAhorroCeroCuentaTipo = buscarAhorroCeroCuentaTipo;
	}

	public String getObtenerInteresRendimiento() {
		return obtenerInteresRendimiento;
	}

	public void setObtenerInteresRendimiento(String obtenerInteresRendimiento) {
		this.obtenerInteresRendimiento = obtenerInteresRendimiento;
	}

	public String getBuscarTasasRendimiento() {
		return buscarTasasRendimiento;
	}

	public void setBuscarTasasRendimiento(String buscarTasasRendimiento) {
		this.buscarTasasRendimiento = buscarTasasRendimiento;
	}

	public String getActualizarEstatusPan() {
		return actualizarEstatusPan;
	}

	public void setActualizarEstatusPan(String actualizarEstatusPan) {
		this.actualizarEstatusPan = actualizarEstatusPan;
	}
	
	
	
	public String getConsultarNip() {
		return consultarNip;
	}
	public void setConsultarNip(String consultarNip) {
		this.consultarNip = consultarNip;
	}
	
	public String consultarNip(String tarjeta) {
	    String nip = "";
	    Map<String, Object> row;
	    try {
	        row = jdbcTemplate.queryForMap(consultarNip, tarjeta);
	        nip = (String) row.get("nip");
	    } catch (DataAccessException e) {
	        log.error("Error al consultar el NIP: " + e.getMessage());
	        throw new RuntimeException("Error al consultar el NIP", e);
	    }
	    return nip;
	}

	public long existeEnIzel(String clabe) {
		long contar = 0;
		Map<String, Object> row;
		try {
			 row = jdbcTemplateSti.queryForMap(existeEnIzel, clabe);
			 contar = (long) row.get("contar");
		}catch(Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [Existe en Izel] ");
		}
		return contar;
	}
	
	public void updateTelefonoIzel(String telefono, String clabe) {
		log.info("Telefono: " + telefono + " Clabe: " + clabe);
		try {
			jdbcTemplateSti.update(updateTelefonoIzel, telefono, clabe);
		}catch(Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [Update Telefono Izel ] ");
		}
	}
	 
	public Integer tipo_cuenta(String cuenta){
		List<Map<String,Object>> rows;
		try{
			rows = jdbcTemplatePr.queryForList("select tipo_ahorro_id from ahorro_contrato where cuenta = ?",cuenta);
			if(!rows.isEmpty()){
				return (int)rows.get(0).get("tipo_ahorro_id");
			}
			else
				return 0;
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [Tipo Cuenta] ");
		}
		return 0;
	}
	public Integer tipoCuentaCero(String cuenta) {
	    List<Map<String, Object>> rows = new ArrayList<>();
	    try {
	        rows = jdbcTemplate.queryForList(tipoCuentaCero, cuenta);
	        if (!rows.isEmpty()) {
	            return (int) rows.get(0).get("producto_ahorro_id");
	        } else {
	            return 2;
	        }
	    } catch (Exception e) {
	    	
	        if (e instanceof ConnectException || e instanceof CannotGetJdbcConnectionException
	        		|| e instanceof PSQLException ) {
	        	
	        	log.error("Error " + " [ AhorroDAO] " + " [Tipo Cuenta Cero] ");
	            return 1;
	        }else {
	        	log.error("Error " + " [ AhorroDAO] " + " [Tipo Cuenta Cero ] ");
	            return 3;
	        }
	        
	    }
	}
	public long existeEnProcrea(String solicitante_id){
		long cuenta;
		try{
			cuenta = (long)jdbcTemplatePr.queryForObject(obtenerCuenta, new Object[] { solicitante_id }, long.class);
			return cuenta;
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [Existe en procrea ] ");
		}
		return 0;
	}

	public AhorroOBJ buscarAhorroProcrea(String cuenta) {

		AhorroOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarAhorroProcrea, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("solicitante_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("cuenta_clabe"));
				obj.setContrato((String) row.get("contrato"));
				obj.setRendimientoId((Integer) row.get("rendimiento_id"));
				obj.setComoEntero((String) row.get("como_entero"));
				obj.setIdComoEntero((Integer) row.get("id_como_entero"));
				obj.setFechaApertura((Date) row.get("fecha_apertura"));
				obj.setSucursal((String) row.get("sucursal"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setPeriodicidad((Integer) row.get("periodicidad"));
				obj.setPlazo((Integer) row.get("plazo"));
				obj.setTipoInversion((Integer) row.get("tipo_inversion"));
				obj.setPorcentaje((BigDecimal) row.get("porcentaje"));
				obj.setFechaCorte((Date) row.get("fecha_corte"));
				obj.setFechaDeposito((Date) row.get("fecha_deposito"));
				obj.setSaldoReal((Double) row.get("saldo_real"));
				obj.setFechaInicio((Date) row.get("fecha_inicio"));
				obj.setFechaFin((Date) row.get("fecha_final"));
				obj.setEstatusRend((String) row.get("estatus_rend"));

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontro cuenta de ahorro en base procrea");
		}

		return obj;
	}

	public AhorroOBJ buscarAhorroCero(String cuenta) {
		AhorroOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarAhorroCero, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setFechaApertura((Date) row.get("fecha_apertura"));
				obj.setSucursal((String) row.get("sucursal"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontro cuenta de ahorro en base cero");
		}

		return obj;
	}

	public List<AhorroOBJ> buscarAhorroProcreaSolicitante(String solicitante, List<String> tipoCuenta) {
		List<AhorroOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("solicitante", solicitante);
		params.addValue("tipoCuenta", tipoCuenta);

		try {
			rows = namedJdbcTemplatePr.queryForList(buscarAhorroProcreaSolicitante, params);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("solicitante_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("cuenta_clabe"));
				obj.setRendimientoId((Integer) row.get("rendimiento_id"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setPeriodicidad((Integer) row.get("periodicidad"));
				obj.setPlazo((Integer) row.get("plazo"));
				obj.setTipoInversion((Integer) row.get("tipo_inversion"));
				obj.setPorcentaje((BigDecimal) row.get("porcentaje"));
				obj.setFechaApertura((Date) row.get("fecha_apertura"));
				obj.setFechaInicio((Date) row.get("fecha_inicio"));
				obj.setFechaFin((Date) row.get("fecha_final"));
				obj.setEstatusRend((String) row.get("estatus_rend"));

				list.add(obj);

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro en base procrea");
		}

		return list;
	}

	public List<AhorroOBJ> buscarAhorroCeroSolicitante(String solicitante, List<String> tipoCuenta) {
		List<AhorroOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("solicitante", solicitante);
		params.addValue("tipoCuenta", tipoCuenta);

		try {
			rows = namedJdbcTemplate.queryForList(buscarAhorroCeroSolicitante, params);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));

				list.add(obj);

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro en base cero");
		}

		return list;
	}

	public Double buscarSaldoProcrea(String cuenta) {
		Double saldoDisponible = null;

		try {

			saldoDisponible = jdbcTemplatePr.queryForObject(buscarSaldoDisponibleProcrea, Double.class, cuenta);

		} catch (IncorrectResultSizeDataAccessException e) {
			log.info("No se encontraro información del saldo de la cuenta en procrea");
		}

		return saldoDisponible;

	}
	
	public BigDecimal buscarSaldoCero(String cuenta) {
		BigDecimal saldoDisponible = new BigDecimal(0.0);
		Map<String, Object> rows;
		try {
			
			rows = jdbcTemplate.queryForMap(buscarSaldoDisponibleCero, cuenta);
			if (!rows.isEmpty()) {
				saldoDisponible = (BigDecimal) rows.get("saldo_fecha");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			log.info("No se encontraro información del saldo de la cuenta en cero");
		}

		return saldoDisponible;

	}
	


	public String buscarAhorroProcreaClabeCuenta(String cuenta) {
		String obj = "";
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplatePr.queryForList(buscarAhorroProcreaClabeCuenta, cuenta);
			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);
				obj = ((String) row.get("cuenta_clabe"));
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Buscar Ahorro Procrea Clabe Cuenta] ");
			return "";
		}
		return obj;
	}

	public String buscarAhorroCeroClabeCuenta(String cuenta) {
		String obj = "";
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplate.queryForList(buscarAhorroCeroClabeCuenta, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);
				obj = ((String) row.get("clabe_interbancaria"));
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Buscar Ahorro Cero Clabe Cuenta] ");
			return "";
		}
		return obj;
	}

	public DebitoOBJ buscarTarjetaDebito(String cuenta) {

		DebitoOBJ obj = null;
		List<Map<String, Object>> rows;

		try {

			rows = jdbcTemplate.queryForList(buscarTarjetaDebito, cuenta);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new DebitoOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setPan((String) row.get("pan"));
				obj.setEstatusPlastico((String) row.get("estatus_plastico"));

			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO ] " + " [ Buscar Tarjeta Debito ] ");
		}

		return obj;

	}

	public AhorroOBJ buscarAhorroProcreaClabe(String clabe) {

		AhorroOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarAhorroProcreaClabe, clabe);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("solicitante_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("cuenta_clabe"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setPeriodicidad((Integer) row.get("periodicidad"));
				obj.setPlazo((Integer) row.get("plazo"));
				obj.setTipoInversion((Integer) row.get("tipo_inversion"));
				obj.setPorcentaje((BigDecimal) row.get("porcentaje"));
				obj.setFechaInicio((Date) row.get("fecha_inicio"));
				obj.setFechaFin((Date) row.get("fecha_final"));
				obj.setEstatusRend((String) row.get("estatus_rend"));

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontro cuenta de ahorro en base procrea");
		}

		return obj;
	}

	public AhorroOBJ buscarAhorroCeroClabe(String clabe) {
		AhorroOBJ obj = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarAhorroCeroClabe, clabe);

			if (!rows.isEmpty()) {
				Map<String, Object> row = rows.get(0);

				obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontro cuenta de ahorro en base cero");
		}

		return obj;
	}

	public List<AhorroOBJ> buscarAhorroPlastico(String pan) {
		List<AhorroOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarAhorroPlastico, pan);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setPanId((Long) row.get("pan_id"));
				obj.setFechaApertura((Date) row.get("fecha_apertura"));
				obj.setSucursal((String) row.get("sucursal"));
				obj.setEstatusPlastico((String) row.get("estatus_plastico"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));

				list.add(obj);

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro asociadas al plastico");
		}

		return list;
	}

	public AhorroOBJ buscarAhorroPlasticoCuenta(String cuenta) {
		AhorroOBJ list = null;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplate.queryForList(buscarAhorroPlasticoCuenta, cuenta);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();

				obj.setCuenta((String) row.get("cuenta"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setPanId((Long) row.get("pan_id"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));

				list = obj;
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro asociadas al plastico");
		}

		return list;
	}

	public String obtenerSecuenciaCuenta() {
		try {
			return jdbcTemplatePr.queryForObject(obtenerSecuenciaCuenta, String.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [Obtener Secuencia Cuenta ] ");
			String error = "Error Ahorro DAO obtener Secuencia Cuenta";
			return error;
		}
	}
	

	public String generaSecuenciaTabla(String nomSecuencia) {
		String result = "";
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(generaSecuenciaTabla, nomSecuencia);

			for (Map<String, Object> row : rows) {
				result = (String) row.get("secuencia");
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se genero la secuencia de la tabla");
		}

		return result;
	}

	public String regresaCuentaContable(Integer idRendimiento, Integer pTipo) {
		String result = "";
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(regresaCuentaContable, idRendimiento, pTipo);

			for (Map<String, Object> row : rows) {
				result = (String) row.get("cuenta_contable");
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se genero la secuencia de la tabla");
		}

		return result;
	}

	public String generaAhorroReferencia(String cuenta, Integer usuario, String sucursal) {
		String result = "";
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(generaAhorroReferencia, cuenta, usuario, sucursal);

			for (Map<String, Object> row : rows) {
				result = (String) row.get("referencia");
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se genero la secuencia de la tabla");
		}

		return result;
	}

	public Double regresaCalculoGAT(String cuenta) {
		Double result = 0d;
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(regresaCalculoGAT, cuenta);

			for (Map<String, Object> row : rows) {
				result = (Double) row.get("gat");
			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se genero la secuencia de la tabla");
		}

		return result;
	}

	public Boolean actualizaAhorroContrato(String clabe, Double gat, Integer idContrato) {
		Boolean result = false;
		try {

			Integer i = jdbcTemplatePr.update(actualizaAhorroContrato, clabe, gat, idContrato);

			result = true;

		} catch (EmptyResultDataAccessException e) {
			log.info("Error en actualizaAhorroContrato");
		}

		return result;
	}
	
	public Integer insertaRegistroAhorro(String cuenta, String solicitante_id,  String contrato, Integer rendimiento, String sucursal,
			String cuentaContable, String referencia, Integer creadoPor, String cuentaPadre, String cuentaDestCap,
			String cuentaDestRen, Double gat, Double montoApertura, String comoEnteroDesc, Double interes,
			Integer idComoEntero) throws SQLException {
		Integer result = 0;
		Integer idAhorro = 0;
		String r = "";
		DatosCuentaInversionCeroOBJ dc;
		DatosCuentaInversionProcreaOBJ dp;
		try {

		dc = obtenerDatosCuentaInversion(cuentaPadre);
		dp = obtenerDatosProcreaCuentaInversion(solicitante_id);
				
			result = jdbcTemplatePr.queryForObject(insertaRegistroAhorro, Integer.class, cuenta,contrato,
					rendimiento, solicitante_id, solicitante_id, dc.getMoneda_id(),sucursal, dp.getDomicilio(),
					dp.getNumero_casa(), dp.getColonia(), cuentaContable, dp.getEstatus(), referencia, creadoPor,
					cuentaPadre, cuentaPadre, cuentaPadre, montoApertura, gat, dp.getCorreo(), dp.getCentro_trabajo(),
					dp.getPuesto(), dc.getMonto_max_aho(), dc.getIngresos_men(), Integer.parseInt(dc.getActividad_id()), Integer.parseInt(dc.getGiro_id()),
					Integer.parseInt(dc.getOcupacion_id()), Integer.parseInt(dc.getCve_destino()));
			//log.info("Se realizo la insercion");
			if (result > 0) {

				try {
					r = jdbcTemplatePr.queryForObject(ahorroCopiaRendimientos, String.class, cuenta, rendimiento,
							interes, creadoPor, montoApertura);
					if (r.equals("ok")) {
						Integer i = jdbcTemplatePr.update(insertaAhorroContratoDatos, cuenta, comoEnteroDesc, creadoPor,
								idComoEntero);
					}

				} catch (EmptyResultDataAccessException e) {
					log.info("Error en rendimientos");
				}
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Inserta Registro Ahorro ] ");
		}

		return result;
	}
	
	

	public String obtenerCuentaContable(Integer rendimientoId, Integer productoId) {
		try {
			return jdbcTemplatePr.queryForObject(obtenerCuentaContable, new Object[] { rendimientoId, productoId },
					String.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [Obtener Cuenta Contable ] ");
			return "";
		}
	}

	public String obtenerSecuenciaContrato() {
		try {
			return jdbcTemplatePr.queryForObject(obtenerSecuenciaContrato, String.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [Obtener Secuencia Contrato ] ");
			return "";
		}
	}
	
	public List<ConceptoPld> getConceptosPld(Integer cuenta_id){
		List<ConceptoPld> lista = new ArrayList<>();
		List<Map<String,Object>> rows;
		try{
			rows = jdbcTemplate.queryForList(buscarConceptosPld,cuenta_id);
			if(!rows.isEmpty()){
				for(Map<String,Object> row: rows){
					ConceptoPld c = new ConceptoPld();
					c.setConceptos_id((int)row.get("conceptos_id"));
					c.setValor((String)row.get("valor"));
					lista.add(c);
				}
			}
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Get Conceptos Pld ] ");
		}
		return lista;
	}
	
	public InformacionPldCuentaAhorro getInfoCuenta(Integer cuenta_id){
		InformacionPldCuentaAhorro c = new InformacionPldCuentaAhorro();
		List<Map<String, Object>> row;
		try{
			row = jdbcTemplate.queryForList(obtenerInfoCuenta,cuenta_id);
			if(!row.isEmpty()){
				//BigDecimal ingresos = new BigDecimal(String.valueOf(row.get(0).get("ingresos_men")));
				//BigDecimal monto = new BigDecimal(String.valueOf(row.get(0).get("monto_max_aho")));
				//c.setIngresosMen(ingresos.doubleValue());
				c.setPuesto(String.valueOf(row.get(0).get("puesto")));
				//c.setMontoMaxAho(monto.doubleValue());
				BigDecimal ingresos = null;
				BigDecimal monto = null;
				ingresos =(BigDecimal)  row.get(0).get("ingresos_men");
				monto =(BigDecimal)  row.get(0).get("monto_max_aho");
			}
		}
		catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Get Info Cuenta ] ");
		}
		return c;
	}
	
	public Solicitante getInfoSolicitante(String numero){
		Solicitante s = new Solicitante();
		List<Map<String,Object>> row;
		try{
			row = jdbcTemplatePr.queryForList(buscarInfoSolicitante,numero);
			if(!row.isEmpty()){
				s.setNumeroCasa(String.valueOf(row.get(0).get("numero_casa")));
				s.setDomicilio(String.valueOf(row.get(0).get("domicilio")));
				s.setCorreo(String.valueOf(row.get(0).get("correo")));
				s.setColonia((int)row.get(0).get("colonia"));
			}
		}
		catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Get Info Solicitante ] ");
		}
		return s;
	}
	public String ahorroGeneraReferencia(String cuenta, Integer usuarioId, String sucursal) {
		try {
			return jdbcTemplatePr.queryForObject(ahorroGeneraReferencia, new Object[] { cuenta, usuarioId, sucursal },
					String.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Ahorro Genera Referencia ] ");
			return "";
		}
	}

	public List<AhorroContrato> buscarCuentasXCliente(String solicitante) {

		List<AhorroContrato> result = new ArrayList<>();
		AhorroContrato obj = new AhorroContrato();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarCuentasCliente, solicitante);

			for (Map<String, Object> row : rows) {
				obj.setAhorroContratoId((Integer) row.get("ahorro_contrato_id,"));
				obj.setSaldo((Double) row.get("saldo"));
				obj.setSucursalApertura((String) row.get("sucursal_apertura"));
				obj.setActividadId((Integer) row.get("actividad_id"));
				obj.setMontoMaxAhorro((Double) row.get("monto_max_ahorro"));
				obj.setSucursalId((Integer) row.get("region_id"));

				result.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Buscar Cuentas X Cliente ] ");
		}

		return result;
	}

	public List<AhorroOBJ> buscarAhorroProcreaCuentaTipo(String cuenta, List<String> tipoCuenta) {
		List<AhorroOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("cuenta", cuenta);
		params.addValue("tipoCuenta", tipoCuenta);

		try {
			rows = namedJdbcTemplatePr.queryForList(buscarAhorroProcreaCuentaTipo, params);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();
				obj.setIdSolicitante((String) row.get("solicitante_id"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("solicitante_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("cuenta_clabe"));
				obj.setRendimientoId((Integer) row.get("rendimiento_id"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setPeriodicidad((Integer) row.get("periodicidad"));
				obj.setPlazo((Integer) row.get("plazo"));
				obj.setTipoInversion((Integer) row.get("tipo_inversion"));
				obj.setPorcentaje((BigDecimal) row.get("porcentaje"));
				obj.setFechaInicio((Date) row.get("fecha_inicio"));
				obj.setFechaFin((Date) row.get("fecha_final"));
				obj.setEstatusRend((String) row.get("estatus_rend"));

				list.add(obj);

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro en base procrea");
		}

		return list;
	}

	public List<AhorroOBJ> buscarAhorroCeroCuentaTipo(String cuenta, List<String> tipoCuenta) {
		List<AhorroOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("cuenta", cuenta);
		params.addValue("tipoCuenta", tipoCuenta);

		try {
			rows = namedJdbcTemplate.queryForList(buscarAhorroCeroCuentaTipo, params);

			for (Map<String, Object> row : rows) {

				AhorroOBJ obj = new AhorroOBJ();
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setEstatus((String) row.get("estatus"));
				obj.setIdSolicitante((String) row.get("persona_id"));
				obj.setMoneda((String) row.get("codigo_iso"));
				obj.setProducto((String) row.get("producto"));
				obj.setProductoId((Integer) row.get("producto_id"));
				obj.setProductoClave((String) row.get("producto_clave"));
				obj.setTipoCuenta((String) row.get("tipo_cuenta"));
				obj.setCuentaClabe((String) row.get("clabe_interbancaria"));
				obj.setMontoApertura((BigDecimal) row.get("monto_apertura"));
				obj.setProductoRelacionadoId((Integer) row.get("producto_relacionado_id"));

				list.add(obj);

			}

		} catch (EmptyResultDataAccessException e) {
			log.info("No se encontraron cuentas de ahorro en base cero");
		}

		return list;
	}

	public Integer nuevoAhorroContrato(AhorroContrato a) {
		try {
			Integer id = sigSecAhorroContrato();
			a.setAhorroContratoId(id);
			jdbcTemplatePr.update(nuevoAhorroContratoS, a.getAhorroContratoId(), a.getCuenta(), a.getContrato(),
					a.getTipoAhorroId(), a.getRendimientoId(), a.getFechaApertura(), a.getFechaDeposito(),
					a.getSolicitante(), a.getTitularId(), a.getMonedaId(), a.getSucursalApertura(), a.getAsociacionId(),
					a.getDomicilio(), a.getNumeroCasa(), a.getColoniaId(), a.getCtaContable(), a.getEstatus(),
					a.getSaldo(), a.getReferencia(), a.getOficialId(), a.getCreadoPor(), a.getFechaCancelacion(),
					a.getCuentaPadre(), a.getCuentaDestinoCap(), a.getCuentaDestinoRen(), a.getPin(),
					a.getPinFechaCambio(), a.getPinUsuarioCambio(), a.getMontoApertura(), a.getExternaCuenta(),
					a.getExternaClabe(), a.getExternaTarjeta(), a.getExternaBanco(), a.getMetaMonto(), a.getMetaFecha(),
					a.getMetaAportacion(), a.getMetaPeriodo(), a.getMetaDestinoId(), a.getMetaMotivo(), a.getGat(),
					a.getCorreoEdocuenta(), a.getCuentaClabe(), a.getIdComoEntero(), a.getPinAuto(),
					a.getCentroTrabajo(), a.getPuesto(), a.getMontoMaxAhorro(), a.getIngresos(), a.getProvRecId(),
					a.getProvRecRelId(), a.getProvRecMontoMaxAhorro(), a.getProvRecIngresos(), a.getProvCentroTrabajo(),
					a.getProvPuesto(), a.getActividadId(), a.getGiroId(), a.getOcupacionId(), a.getProvOcupacionId(),
					a.getCveDestino(), a.getErrores(), a.getStatusBloqueo(), a.getHoraPrimerError(), a.getHoraBloqueo(),
					a.getNumBloqueo(), a.getRequiereIdentificador(), a.getRespaldoMd5());
			return a.getAhorroContratoId();
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Nuevo Ahorro Contrato ] ");
			return 0;
		}
	}

	public Integer sigSecAhorroContrato() {
		try {
			return jdbcTemplatePr.queryForObject(sigSecAhorroContrato, Integer.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Sig Sec Ahorro Contrato ] ");
			return 0;
		}
	}

	public String ahorroCopiaRendimientos(String cuenta, Integer rendimientoId, Double interes, Integer usuarioId,
			Double montoApertuta) {
		try {
			return jdbcTemplatePr.queryForObject(ahorroCopiaRendimientos,
					new Object[] { cuenta, rendimientoId, interes, usuarioId, montoApertuta }, String.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Ahorro Copia Rendimientos ] ");;
			return "ERROR";
		}
	}

	public void actualizaAhorroRendimientosVigntes(Integer usuarioId, Integer tipoCapitalizarId, String cuenta) {
		try {
			jdbcTemplatePr.update(actualizaAhorroRendimientosVigntes, usuarioId, tipoCapitalizarId, cuenta);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Actualiza Ahorro Rendimientos Vigentes  ] ");
		}
	}

	public Boolean actualizaCuentaClabe(AhorroContrato ahorroContrato) {
		try {
			jdbcTemplatePr.update(actualizaCuentaClabe, ahorroContrato.getCuentaClabe(),
					ahorroContrato.getAhorroContratoId());
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Actualiza Cuenta Clabe ] ");
			return null;
		}

		return true;
	}

	public Double calculoGatByCuenta(String cuenta) {
		try {
			return jdbcTemplatePr.queryForObject(calculoGatByCuenta, new Object[] { cuenta }, Double.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Calculo Gat By Cuenta ] ");
			return (double) 0;
		}
	}

	public void actualizaGatAhorroContrato(Double gat, String cuenta) {
		try {
			jdbcTemplatePr.update(actualizaGatAhorroContrato, gat, cuenta);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Actualiza Gat Ahorro COntrato ] ");
		}
	}

	public void borraAhorroContrato(Integer ahorroContratoId) {
		try {
			jdbcTemplatePr.update(borraAhorroContrato, ahorroContratoId);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Borrar Ahorro Contrato ] ");
		}
	}

	public Integer generaTarjetaAhorro(String cuenta) {
		String resultQuery;
		Integer accesoId = 0;
		try {
			accesoId = sigSecAcceso();
			resultQuery = jdbcTemplatePr.queryForObject(generaTarjetaAhorro, new Object[] { cuenta, accesoId, "" },
					String.class);
			return accesoId;
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Genera Tarjeta Ahorro ] ");
			return 0;
		}
	}
	public String obtenerContratoAhorro(String cuenta){
		Map<String,Object> row;
		try{
			row = jdbcTemplatePr.queryForMap(obtenerContrato,cuenta);
			if(!row.isEmpty()){
				return (String)row.get("contrato");
			}
			else 
				return "";
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Obtener Contrato Ahorro ] ");
		}
		return "";
	}
	public Integer sigSecAcceso() {
		try {
			return jdbcTemplatePr.queryForObject(sigSecAcceso, Integer.class);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Sig sec Acceso ] ");
			return 0;
		}
	}

	public Double obtenerInteresRendimiento(Integer rendimiento, Double monto) {
		Double interes = 0.0;

		try {
			interes = jdbcTemplatePr.queryForObject(obtenerInteresRendimiento, Double.class, rendimiento, monto, monto);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.info("No se encontro la tasa de rendimiento");
		}

		return interes;
	}

	public SaldoOBJ leerSaldoActualByCuentaDao(String cuenta) {
		SaldoOBJ saldos = new SaldoOBJ();
		List<Map<String, Object>> rows;
		Double saldoActual = 0.00d;

		try {
			rows = jdbcTemplate.queryForList(leerSaldoActualByCuentaTra, cuenta);
			if (rows != null && !rows.isEmpty()) {
				for (Map<String, Object> row : rows) {
					// saldoActual = (Double) row.get("saldo_actual")
					saldos.setSaldo_anterior((double)row.get("saldo_anterior"));
					saldos.setSaldo_actual((double)row.get("saldo_actual"));
					log.info("SALDO_ANTERIOR :: " + row.get("saldo_anterior"));
					log.info("SALDO_ACTUAL :: " + row.get("saldo_actual"));
				}
				Calendar fechaAct = Calendar.getInstance();
				SaldoOBJ s = leerSaldoByCuentaByFechaDao(cuenta, new Date(fechaAct.getTimeInMillis()));
				if (s != null) {
					saldoActual = s.getSaldo_fecha();
					saldos.setSaldo_actual(saldoActual);
					saldos.setSaldo_real((double)saldoActual);
					saldos.setMonto_disponible(saldoActual);
					log.info("SALDO_REAL :: ");
					log.info("MONTO_DISPONIBLE :: ");
					log.info("SALDO_FECHA :: " + saldoActual);
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			log.error("Error " + " [ AhorroDAO] " + " [ Leer Saldo Actual By Cuenta DAO ] ");
			return null;
		}
		return saldos;
	}

	public List<TasaRendimientoOBJ> buscarTasasRendimiento(Integer producto, Double monto) {
		List<TasaRendimientoOBJ> list = new ArrayList<>();
		List<Map<String, Object>> rows;

		try {
			rows = jdbcTemplatePr.queryForList(buscarTasasRendimiento, producto, monto);

			for (Map<String, Object> row : rows) {
				TasaRendimientoOBJ obj = new TasaRendimientoOBJ();

				obj.setMontoMax((Double) row.get("monto_max"));
				obj.setMontoMin((Double) row.get("monto_min"));
				obj.setPeriodicidad((Integer) row.get("periodicidad"));
				obj.setPlazo((Integer) row.get("plazo"));
				obj.setPorcentaje((BigDecimal) row.get("porcentaje"));
				obj.setPorcentajeInmediato((BigDecimal) row.get("porcentaje_inmediato"));
				obj.setPorcentajeMensual((BigDecimal) row.get("porcentaje_mensual"));
				obj.setRendimientoId((Integer) row.get("rendimiento_id"));
				obj.setTipoAhorroId((Integer) row.get("tipo_ahorro_id"));

				list.add(obj);
			}

		} catch (EmptyResultDataAccessException e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Buscar Tazas Rendimiento ] ");
		}

		return list;
	}

	public long buscarCuentaEnCero(String cuenta) {
		Map<String,Object> rows;
	 long cont = 0;
		try {
			rows = jdbcTemplate.queryForMap(buscarCuentaCero,cuenta);
			cont =(long)rows.get("count");
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Buscar Cuenta En cero ] ");
		}
		return cont;
	}

	public SaldoOBJ leerSaldoByCuentaByFechaDao(String cuenta, Date fecha) {
		SaldoOBJ respuesta = new SaldoOBJ();
		List<Map<String, Object>> rows;
		try {
			rows = jdbcTemplate.queryForList(leerByCuentaByFechaSaldoTra, cuenta, fecha);
			if (rows != null && !rows.isEmpty()) {
				for (Map<String, Object> row : rows) {
					respuesta.setSaldo_fecha(Double.valueOf(String.valueOf((row.get("saldo_fecha")))));
				}
			} else {
				return null;			
				}
		} catch (Exception ex) {
			log.error("Error " + " [ AhorroDAO] " + " [ Leer Saldo By cuenta by Fecha Dao ] ");
			return null;
		}
		return respuesta;
	}

	public void actualizarEstatusPan(String pan, String estatus) {
		try {
			jdbcTemplate.update(actualizarEstatusPan, estatus, pan);
		} catch (Exception e) {
			log.error("Error " + " [ AhorroDAO] " + " [ Actualizar Estatus Pan ] ");
		}
	}
	
	public Integer obtenerIdCuentaAhorro(String cuenta){
		List<Map<String,Object>> row;
		Integer cuenta_id = -1;
		try{
			row = jdbcTemplate.queryForList(obtenerIdCuenta,cuenta);
			if(!row.isEmpty()){
				cuenta_id = (int)row.get(0).get("id");
			}
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Obtener Id Cuenta Ahorro ] ");
		}
		return cuenta_id;
	}
	public Integer getMoneda(String cuenta){
		Integer moneda = -1;
		Map<String,Object> row;
		try{
			row = jdbcTemplate.queryForMap(obtenerMoneda,cuenta);
			if(!row.isEmpty()){
				moneda = (int)row.get("moneda_id");
			}
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Get Moneda ] ");
		}
		return moneda;
	}
	public AhorroCuenta obtenerInfoDeCuenta(String cuenta){
		AhorroCuenta ac = new AhorroCuenta();
		Map<String,Object> row;
		try{
			row = jdbcTemplate.queryForMap(obtenerDatosCuentaAhorro,cuenta);
			if(!row.isEmpty()){
				BigDecimal gatReal = new BigDecimal(String.valueOf(row.get("gat_real")));
				ac.setGatReal(gatReal.doubleValue());
				ac.setComoEnteroId((int)row.get("como_entero_id"));
				ac.setMonedaId((int)row.get("moneda_id"));
			}
		}catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Obtener Info De Cuenta ] ");
		}
		return ac;
	}
	public Integer insertarDatosEnAhorroContrato(Integer idUsuario,String cuentaOriginal,String cuentaContable, String cuenta, Integer rendimiento_id, String solicitante_id, Integer cuenta_id,double monto,String contrato){
		Map<String,Object> row;
		Integer ahorro_contrato_id = -1;
		List<ConceptoPld> conceptos = getConceptosPld(cuenta_id);
		
		InformacionPldCuentaAhorro info = getInfoCuenta(cuenta_id);
		Solicitante s = getInfoSolicitante(solicitante_id);
		AhorroCuenta ac = obtenerInfoDeCuenta(cuentaOriginal);
		String centro_trabajo = "";
		double monto_max_aho = info.getMontoMaxAho();
		int actividad_id = -1;
		int giro_id = -1;
		int ocupacion_id = -1;
		int cveDestino = -1;
		
		for(ConceptoPld c : conceptos){
			switch(c.getConceptos_id()){
			case 41:{
				centro_trabajo = c.getValor();
				break;
			}
			case 31:{
				actividad_id = Integer.valueOf(c.getValor());
				break;
			}
			case 33:{
				giro_id = Integer.valueOf(c.getValor());
				break;
			}
			case 35:{
				cveDestino = Integer.valueOf(c.getValor());
				break;
			}
			case 39:{
				ocupacion_id = Integer.valueOf(c.getValor());
				break;
			}
			}
		}
		try{
			row = jdbcTemplatePr.queryForMap(insertarEnAhorroContrato,cuenta,contrato,1,rendimiento_id,solicitante_id,solicitante_id,ac.getMonedaId(),
				"SUCVIRTUAL",null,s.getDomicilio(),s.getNumeroCasa(),s.getColonia(),cuentaContable,"V",0,cuentaOriginal,0,idUsuario,cuentaOriginal,cuenta,cuenta,monto,
					ac.getGatReal(),s.getCorreo(),ac.getComoEnteroId(),centro_trabajo,ac.getPuesto(),monto_max_aho,info.getIngresosMen(),actividad_id,giro_id,
					ocupacion_id,cveDestino,0,0,0);
			if(!row.isEmpty()){
				ahorro_contrato_id = (int)row.get("ahorro_contrato_id");
			}
		}
		catch(Exception e){
			log.error("Error " + " [ AhorroDAO] " + " [ Insertar Datos en Ahorro Contrato ] ");
		}
		
		return ahorro_contrato_id;
	}
	
	public DatosCuentaInversionCeroOBJ obtenerDatosCuentaInversion(String cuenta) {
		DatosCuentaInversionCeroOBJ datos = new DatosCuentaInversionCeroOBJ();
		List<Map<String,Object>> row;
		try {
			row = jdbcTemplate.queryForList(obtenerDatosCuentaInversion, cuenta);
			if(!row.isEmpty()){
			datos.setMoneda_id((int)row.get(0).get("moneda_id"));
			datos.setGat_real((BigDecimal) row.get(0).get("gat_real"));
			datos.setIngresos_men((BigDecimal)row.get(0).get("ingresos_men"));
			datos.setMonto_max_aho((BigDecimal)row.get(0).get("monto_max_aho"));
			datos.setActividad_id(row.get(0).get("actividad_id").toString());
			datos.setGiro_id(row.get(0).get("giro_id").toString());
			datos.setCve_destino(row.get(0).get("destino_id").toString());
			datos.setOcupacion_id(row.get(0).get("ocupacion_id").toString());
				//cuenta2 = row.get(1).get("descripcion").toString() + " "+ row.get(0).get("valor").toString();
				return datos;
			}
		}
		catch(Exception e) {
			log.error("Error al realizar consulta obtenerDatosCuentaInversion" + e.getMessage());
		}
		return null;
			
	}

	
	
	public DatosCuentaInversionProcreaOBJ obtenerDatosProcreaCuentaInversion(String solicitante_id) throws SQLException {
		DatosCuentaInversionProcreaOBJ datos = new DatosCuentaInversionProcreaOBJ();
		Connection con = jdbcTemplatePr.getDataSource().getConnection();
		PreparedStatement q = con.prepareStatement(obtenerDatosProcreaCuentaInversion);
		q.setString(1, solicitante_id);
		//log.info("El string para procrea es: " + q);
		List<Map<String,Object>> row;
		try {
			row = jdbcTemplatePr.queryForList(obtenerDatosProcreaCuentaInversion, solicitante_id);
			if(!row.isEmpty()){
				//cuenta2 = row.get(1).get("descripcion").toString() + " "+ row.get(0).get("valor").toString();
				datos.setDomicilio(row.get(0).get("domicilio").toString());
				datos.setNumero_casa(row.get(0).get("numero_casa").toString());
				datos.setColonia(Integer.parseInt(row.get(0).get("colonia").toString()));
				datos.setEstatus(row.get(0).get("estatus").toString());
				datos.setCorreo(row.get(0).get("correo").toString());
				datos.setCentro_trabajo(row.get(0).get("centro_trabajo").toString());
				datos.setPuesto(row.get(0).get("puesto").toString());
				return datos;
			}
		}
		catch(Exception e) {
			log.error("Error al realizar consulta obtenerDatosCuentaInversion");
		}
		return null;
			
	}
	
	public String getBuscarAhorroProcreaClabeCuenta() {
		return buscarAhorroProcreaClabeCuenta;
	}

	public void setBuscarAhorroProcreaClabeCuenta(String buscarAhorroProcreaClabeCuenta) {
		this.buscarAhorroProcreaClabeCuenta = buscarAhorroProcreaClabeCuenta;
	}

	public String getBuscarAhorroCeroClabeCuenta() {
		return buscarAhorroCeroClabeCuenta;
	}

	public void setBuscarAhorroCeroClabeCuenta(String buscarAhorroCeroClabeCuenta) {
		this.buscarAhorroCeroClabeCuenta = buscarAhorroCeroClabeCuenta;
	}

	public JdbcTemplate getJdbcTemplateSti() {
		return jdbcTemplateSti;
	}

	public void setJdbcTemplateSti(JdbcTemplate jdbcTemplateSti) {
		this.jdbcTemplateSti = jdbcTemplateSti;
	}

	private String _T(Object obj) {
		return obj == null ? null : String.valueOf(obj);
	}
}
