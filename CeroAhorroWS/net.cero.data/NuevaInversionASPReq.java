package net.cero.data;

import java.math.BigDecimal;

public class NuevaInversionASPReq {
	private String nombreInversion;
	private String cuentaOriginal;
	private Integer idRendimiento;
	private Integer plazo;
	private BigDecimal monto;
	private Integer tipoReinversionId;
	private Integer modalidad;
	
	
	public Integer getModalidad() {
		return modalidad;
	}
	public void setModalidad(Integer modalidad) {
		this.modalidad = modalidad;
	}
	public Integer getPlazo() {
		return plazo;
	}
	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}
	public String getNombreInversion() {
		return nombreInversion;
	}
	public void setNombreInversion(String nombreInversion) {
		this.nombreInversion = nombreInversion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	public String getCuentaOriginal() {
		return cuentaOriginal;
	}
	public void setCuentaOriginal(String cuentaOriginal) {
		this.cuentaOriginal = cuentaOriginal;
	}
	
	public Integer getTipoReinversionId() {
		return tipoReinversionId;
	}
	public void setTipoReinversionId(Integer tipoReinversionId) {
		this.tipoReinversionId = tipoReinversionId;
	}
	public Integer getIdRendimiento() {
		return idRendimiento;
	}
	public void setIdRendimiento(Integer idRendimiento) {
		this.idRendimiento = idRendimiento;
	}
	
	
}
