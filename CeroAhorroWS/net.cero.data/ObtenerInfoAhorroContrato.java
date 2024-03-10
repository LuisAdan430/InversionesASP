package net.cero.data;

import java.util.Date;

public class ObtenerInfoAhorroContrato {
	private Double monto_apertura;
	private Date fecha_Apertura;
	private Integer rendimientoId;
	
	
	public ObtenerInfoAhorroContrato() {
		super();
	}

	
	
	public ObtenerInfoAhorroContrato(Double monto_apertura, Date fecha_Apertura, Integer rendimientoId) {
		this.monto_apertura = monto_apertura;
		this.fecha_Apertura = fecha_Apertura;
		this.rendimientoId = rendimientoId;
	}

	public Integer getRendimientoId() {
		return rendimientoId;
	}
	public void setRendimientoId(Integer rendimientoId) {
		this.rendimientoId = rendimientoId;
	}



	
	public Double getMonto_apertura() {
		return monto_apertura;
	}



	public void setMonto_apertura(Double monto_apertura) {
		this.monto_apertura = monto_apertura;
	}



	public Date getFecha_Apertura() {
		return fecha_Apertura;
	}
	public void setFecha_Apertura(Date fecha_Apertura) {
		this.fecha_Apertura = fecha_Apertura;
	}
	
	
	

}
