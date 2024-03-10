package net.cero.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CapitalizarRendimientosOBJ {
	
	private Long id;
	private Integer tipoId;
	private Integer rendimientoId;
	private String descripcion;
	private String cuentaContable;
	private BigDecimal montoMin;
	private BigDecimal montoMax;
	private Timestamp fechaCreacion;
	private Integer usuarioModificacion;
	private Timestamp fechaModificacion;
	
	public CapitalizarRendimientosOBJ() {}
	
	public CapitalizarRendimientosOBJ(Long id, Integer tipoId, Integer rendimientoId, String descripcion, 
			String cuentaContable, BigDecimal montoMin, BigDecimal montoMax) {
		
		this.id = id;
		this.tipoId = tipoId;
		this.rendimientoId = rendimientoId;
		this.descripcion = descripcion;
		this.cuentaContable = cuentaContable;
		this.montoMin = montoMin;
		this.montoMax = montoMax;
		
	}
	
	@Override
	public String toString() {
		
		return "{id: " + id + ", tipoId: " + tipoId + ", rendimientoId: " + rendimientoId
				+ ", descripcion: " + descripcion + ", cuentaContable: " + cuentaContable + ", montoMin: " + montoMin
				+ ", montoMax: " + montoMax + "}";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getTipoId() {
		return tipoId;
	}
	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}
	public Integer getRendimientoId() {
		return rendimientoId;
	}
	public void setRendimientoId(Integer rendimientoId) {
		this.rendimientoId = rendimientoId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	public BigDecimal getMontoMin() {
		return montoMin;
	}
	public void setMontoMin(BigDecimal montoMin) {
		this.montoMin = montoMin;
	}
	public BigDecimal getMontoMax() {
		return montoMax;
	}
	public void setMontoMax(BigDecimal montoMax) {
		this.montoMax = montoMax;
	}
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
}
