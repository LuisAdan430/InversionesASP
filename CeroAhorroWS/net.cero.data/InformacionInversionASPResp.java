package net.cero.data;


public class InformacionInversionASPResp {
	 private Double montoInicial; 
	 private String nombreInversion;
	 private String finalizacionInversion;
	 private String diaCreacionInversion;
	 private long tiempoRestante;
	 private Integer plazo;
	 private Double montoFinal;
	 private Double interes;
	 
	
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Double getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(Double montoInicial) {
		this.montoInicial = montoInicial;
	}
	public String getNombreInversion() {
		return nombreInversion;
	}
	public void setNombreInversion(String nombreInversion) {
		this.nombreInversion = nombreInversion;
	}
	public String getFinalizacionInversion() {
		return finalizacionInversion;
	}
	public void setFinalizacionInversion(String finalizacionInversion) {
		this.finalizacionInversion = finalizacionInversion;
	}
	
	
	public String getDiaCreacionInversion() {
		return diaCreacionInversion;
	}
	public void setDiaCreacionInversion(String diaCreacionInversion) {
		this.diaCreacionInversion = diaCreacionInversion;
	}
	

	public Double getMontoFinal() {
		return montoFinal;
	}
	public void setMontoFinal(Double montoFinal) {
		this.montoFinal = montoFinal;
	}
	public long getTiempoRestante() {
		return tiempoRestante;
	}
	public void setTiempoRestante(long tiempoRestante) {
		this.tiempoRestante = tiempoRestante;
	}
	
	public Integer getPlazo() {
		return plazo;
	}
	public void setPlazo(Integer plazo) {
		this.plazo = plazo;
	}
	 
}
