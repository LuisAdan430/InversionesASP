package net.cero.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AhorroCuentaMiDebitoOBJ implements Serializable {

	private Integer id;
	private String cuenta;
	private Integer estatusId;
	private String personaId;
	private Integer productoAhorroId;
	private Date fechaApertura;
	private String montoApertura;
	private Integer sucursalId;
	private String rendimiento;
	private Integer monedaId;
	private String gatNominal;
	private Integer asesorId;
	private Integer comoEnteroId;
	private String comoEnteroObs;
	private String clabeInterbancaria;
	private String referencia;
	private Timestamp fechaCreacion;
	private Integer usuarioCreacion;
	private Timestamp fechaModificacion;
	private Integer usuarioModificacion;
	private String gatReal;
	private String pan;
	private String productoClave;
	private String producto;
	private Integer datosAhorroId;
	private Integer conceptoId;
	private String conceptoClave;
	private String concepto;
	private Integer ahorroDatosEstatusId;
	private String ahorroDatosEstatus;
	private String tienePlastico;
	private String proveedor;
	private String tipo_tarjeta;
	
	@Override
	public String toString() {
		return "AhorroCuentaMiDebitoOBJ [id=" + id + ", cuenta=" + cuenta + ", estatusId=" + estatusId + ", personaId="
				+ personaId + ", productoAhorroId=" + productoAhorroId + ", fechaApertura=" + fechaApertura
				+ ", montoApertura=" + montoApertura + ", sucursalId=" + sucursalId + ", rendimiento=" + rendimiento
				+ ", monedaId=" + monedaId + ", gatNominal=" + gatNominal + ", asesorId=" + asesorId + ", comoEnteroId="
				+ comoEnteroId + ", comoEnteroObs=" + comoEnteroObs + ", clabeInterbancaria=" + clabeInterbancaria
				+ ", referencia=" + referencia + ", fechaCreacion=" + fechaCreacion + ", usuarioCreacion="
				+ usuarioCreacion + ", fechaModificacion=" + fechaModificacion + ", usuarioModificacion="
				+ usuarioModificacion + ", gatReal=" + gatReal + ", pan=" + pan + ", productoClave=" + productoClave
				+ ", producto=" + producto + ", datosAhorroId=" + datosAhorroId + ", conceptoId=" + conceptoId
				+ ", conceptoClave=" + conceptoClave + ", concepto=" + concepto + ", ahorroDatosEstatusId="
				+ ahorroDatosEstatusId + ", ahorroDatosEstatus=" + ahorroDatosEstatus + ", tienePlastico="
				+ tienePlastico + ", proveedor=" + proveedor + ", tipo_tarjeta=" + tipo_tarjeta + "]";
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}
	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	/**
	 * @return the estatusId
	 */
	public Integer getEstatusId() {
		return estatusId;
	}
	/**
	 * @param estatusId the estatusId to set
	 */
	public void setEstatusId(Integer estatusId) {
		this.estatusId = estatusId;
	}
	/**
	 * @return the personaId
	 */
	public String getPersonaId() {
		return personaId;
	}
	/**
	 * @param personaId the personaId to set
	 */
	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}
	/**
	 * @return the productoAhorroId
	 */
	public Integer getProductoAhorroId() {
		return productoAhorroId;
	}
	/**
	 * @param productoAhorroId the productoAhorroId to set
	 */
	public void setProductoAhorroId(Integer productoAhorroId) {
		this.productoAhorroId = productoAhorroId;
	}
	/**
	 * @return the fechaApertura
	 */
	public Date getFechaApertura() {
		return fechaApertura;
	}
	/**
	 * @param fechaApertura the fechaApertura to set
	 */
	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	/**
	 * @return the montoApertura
	 */
	public String getMontoApertura() {
		return montoApertura;
	}
	/**
	 * @param montoApertura the montoApertura to set
	 */
	public void setMontoApertura(String montoApertura) {
		this.montoApertura = montoApertura;
	}
	/**
	 * @return the sucursalId
	 */
	public Integer getSucursalId() {
		return sucursalId;
	}
	/**
	 * @param sucursalId the sucursalId to set
	 */
	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}
	/**
	 * @return the rendimiento
	 */
	public String getRendimiento() {
		return rendimiento;
	}
	/**
	 * @param rendimiento the rendimiento to set
	 */
	public void setRendimiento(String rendimiento) {
		this.rendimiento = rendimiento;
	}
	/**
	 * @return the monedaId
	 */
	public Integer getMonedaId() {
		return monedaId;
	}
	/**
	 * @param monedaId the monedaId to set
	 */
	public void setMonedaId(Integer monedaId) {
		this.monedaId = monedaId;
	}
	/**
	 * @return the gatNominal
	 */
	public String getGatNominal() {
		return gatNominal;
	}
	/**
	 * @param gatNominal the gatNominal to set
	 */
	public void setGatNominal(String gatNominal) {
		this.gatNominal = gatNominal;
	}
	/**
	 * @return the asesorId
	 */
	public Integer getAsesorId() {
		return asesorId;
	}
	/**
	 * @param asesorId the asesorId to set
	 */
	public void setAsesorId(Integer asesorId) {
		this.asesorId = asesorId;
	}
	/**
	 * @return the comoEnteroId
	 */
	public Integer getComoEnteroId() {
		return comoEnteroId;
	}
	/**
	 * @param comoEnteroId the comoEnteroId to set
	 */
	public void setComoEnteroId(Integer comoEnteroId) {
		this.comoEnteroId = comoEnteroId;
	}
	/**
	 * @return the comoEnteroObs
	 */
	public String getComoEnteroObs() {
		return comoEnteroObs;
	}
	/**
	 * @param comoEnteroObs the comoEnteroObs to set
	 */
	public void setComoEnteroObs(String comoEnteroObs) {
		this.comoEnteroObs = comoEnteroObs;
	}
	/**
	 * @return the clabeInterbancaria
	 */
	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}
	/**
	 * @param clabeInterbancaria the clabeInterbancaria to set
	 */
	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}
	/**
	 * @return the referencia
	 */
	public String getReferencia() {
		return referencia;
	}
	/**
	 * @param referencia the referencia to set
	 */
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	/**
	 * @return the fechaCreacion
	 */
	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}
	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	/**
	 * @return the usuarioCreacion
	 */
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	/**
	 * @param usuarioCreacion the usuarioCreacion to set
	 */
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	/**
	 * @return the fechaModificacion
	 */
	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the usuarioModificacion
	 */
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	/**
	 * @return the gatReal
	 */
	public String getGatReal() {
		return gatReal;
	}
	/**
	 * @param gatReal the gatReal to set
	 */
	public void setGatReal(String gatReal) {
		this.gatReal = gatReal;
	}
	/**
	 * @return the pan
	 */
	public String getPan() {
		return pan;
	}
	/**
	 * @param pan the pan to set
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}
	/**
	 * @return the productoClabe
	 */
	public String getProductoClave() {
		return productoClave;
	}
	/**
	 * @param productoClabe the productoClabe to set
	 */
	public void setProductoClave(String productoClave) {
		this.productoClave = productoClave;
	}
	/**
	 * @return the producto
	 */
	public String getProducto() {
		return producto;
	}
	/**
	 * @param producto the producto to set
	 */
	public void setProducto(String producto) {
		this.producto = producto;
	}
	/**
	 * @return the datosAhorroId
	 */
	public Integer getDatosAhorroId() {
		return datosAhorroId;
	}
	/**
	 * @param datosAhorroId the datosAhorroId to set
	 */
	public void setDatosAhorroId(Integer datosAhorroId) {
		this.datosAhorroId = datosAhorroId;
	}
	/**
	 * @return the conceptoId
	 */
	public Integer getConceptoId() {
		return conceptoId;
	}
	/**
	 * @param conceptoId the conceptoId to set
	 */
	public void setConceptoId(Integer conceptoId) {
		this.conceptoId = conceptoId;
	}
	/**
	 * @return the conceptoClave
	 */
	public String getConceptoClave() {
		return conceptoClave;
	}
	/**
	 * @param conceptoClave the conceptoClave to set
	 */
	public void setConceptoClave(String conceptoClave) {
		this.conceptoClave = conceptoClave;
	}
	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}
	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	/**
	 * @return the ahorroDatosEstatusId
	 */
	public Integer getAhorroDatosEstatusId() {
		return ahorroDatosEstatusId;
	}
	/**
	 * @param ahorroDatosEstatusId the ahorroDatosEstatusId to set
	 */
	public void setAhorroDatosEstatusId(Integer ahorroDatosEstatusId) {
		this.ahorroDatosEstatusId = ahorroDatosEstatusId;
	}
	/**
	 * @return the ahorroDatosEstatus
	 */
	public String getAhorroDatosEstatus() {
		return ahorroDatosEstatus;
	}
	/**
	 * @param ahorroDatosEstatus the ahorroDatosEstatus to set
	 */
	public void setAhorroDatosEstatus(String ahorroDatosEstatus) {
		this.ahorroDatosEstatus = ahorroDatosEstatus;
	}
	/**
	 * @return the tienePlastico
	 */
	public String getTienePlastico() {
		return tienePlastico;
	}
	/**
	 * @param tienePlastico the tienePlastico to set
	 */
	public void setTienePlastico(String tienePlastico) {
		this.tienePlastico = tienePlastico;
	}
	public String getProveedor() {
		return proveedor;
	}
	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}
	public String getTipo_tarjeta() {
		return tipo_tarjeta;
	}
	public void setTipo_tarjeta(String tipo_tarjeta) {
		this.tipo_tarjeta = tipo_tarjeta;
	}
	
	
}