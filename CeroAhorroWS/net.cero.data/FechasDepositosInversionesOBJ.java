package net.cero.data;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
public class FechasDepositosInversionesOBJ {
	private List<LocalDate> fechas = new ArrayList<>();
	private List<LocalDate> fechasIniciales = new ArrayList<>();
	private List<Integer> diasContablesPagos = new ArrayList<>();
	
	public List<LocalDate> getFechasIniciales() {
		return fechasIniciales;
	}
	public void setFechasIniciales(List<LocalDate> fechasIniciales) {
		this.fechasIniciales = fechasIniciales;
	}
	public List<LocalDate> getFechas() {
		return fechas;
	}
	public void setFechas(List<LocalDate> fechas) {
		this.fechas = fechas;
	}
	
	public List<Integer> getDiasContablesPagos() {
		return diasContablesPagos;
	}
	public void setDiasContablesPagos(List<Integer> diasContablesPagos) {
		this.diasContablesPagos = diasContablesPagos;
	}
	public FechasDepositosInversionesOBJ() {
	}
	public FechasDepositosInversionesOBJ(List<LocalDate> fechas, List<LocalDate> fechasIniciales,
			List<Integer> diasContablesPagos) {
		super();
		this.fechas = fechas;
		this.fechasIniciales = fechasIniciales;
		this.diasContablesPagos = diasContablesPagos;
	}
	@Override
	public String toString() {
		return "FechasDepositosInversionesOBJ [fechas=" + fechas + ", fechasIniciales=" + fechasIniciales
				+ ", diasContablesPagos=" + diasContablesPagos + "]";
	}
	
	
	
}
