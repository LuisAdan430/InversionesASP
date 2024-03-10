package net.cero.data;


public class TazasPlazosOBJ {

	private Long id;
	private Integer rendimientoId;
	private String descripcion;
	private Short plazo;
	private Double porcentaje;
	private String canal;
	
	public TazasPlazosOBJ() {}
	
	@Override
	public String toString() {
		return "{id: " + id + ", rendimientoId: " + rendimientoId + ", descripcion: " + descripcion
				+ ", plazo: " + plazo + ", porcentaje: " + porcentaje + ", canal: " + canal + "}";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Short getPlazo() {
		return plazo;
	}
	public void setPlazo(Short plazo) {
		this.plazo = plazo;
	}
	

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}
	
}
