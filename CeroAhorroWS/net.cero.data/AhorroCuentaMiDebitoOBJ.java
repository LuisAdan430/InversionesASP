package net.cero.data;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class AhorroCuentaMiDebitoOBJ implements Serializable {

    private transient Integer id; // Marking id as transient to exclude it from serialization
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

    public AhorroCuentaMiDebitoOBJ() {
        // Default constructor
    }

    // Getter and setter methods

    // Override readResolve to return a new instance, ensuring no sensitive fields are exposed
    private Object readResolve() throws ObjectStreamException {
        AhorroCuentaMiDebitoOBJ proxy = new AhorroCuentaMiDebitoOBJ();
        proxy.setCuenta(this.cuenta);
        proxy.setEstatusId(this.estatusId);
        proxy.setPersonaId(this.personaId);
        proxy.setProductoAhorroId(this.productoAhorroId);
        proxy.setFechaApertura(this.fechaApertura);
        proxy.setMontoApertura(this.montoApertura);
        proxy.setSucursalId(this.sucursalId);
        proxy.setRendimiento(this.rendimiento);
        proxy.setMonedaId(this.monedaId);
        proxy.setGatNominal(this.gatNominal);
        proxy.setAsesorId(this.asesorId);
        proxy.setComoEnteroId(this.comoEnteroId);
        proxy.setComoEnteroObs(this.comoEnteroObs);
        proxy.setClabeInterbancaria(this.clabeInterbancaria);
        proxy.setReferencia(this.referencia);
        proxy.setFechaCreacion(this.fechaCreacion);
        proxy.setUsuarioCreacion(this.usuarioCreacion);
        proxy.setFechaModificacion(this.fechaModificacion);
        proxy.setUsuarioModificacion(this.usuarioModificacion);
        proxy.setGatReal(this.gatReal);
        proxy.setPan(this.pan);
        proxy.setProductoClave(this.productoClave);
        proxy.setProducto(this.producto);
        proxy.setDatosAhorroId(this.datosAhorroId);
        proxy.setConceptoId(this.conceptoId);
        proxy.setConceptoClave(this.conceptoClave);
        proxy.setConcepto(this.concepto);
        proxy.setAhorroDatosEstatusId(this.ahorroDatosEstatusId);
        proxy.setAhorroDatosEstatus(this.ahorroDatosEstatus);
        proxy.setTienePlastico(this.tienePlastico);
        return proxy;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public Integer getEstatusId() {
		return estatusId;
	}

	public void setEstatusId(Integer estatusId) {
		this.estatusId = estatusId;
	}

	public String getPersonaId() {
		return personaId;
	}

	public void setPersonaId(String personaId) {
		this.personaId = personaId;
	}

	public Integer getProductoAhorroId() {
		return productoAhorroId;
	}

	public void setProductoAhorroId(Integer productoAhorroId) {
		this.productoAhorroId = productoAhorroId;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getMontoApertura() {
		return montoApertura;
	}

	public void setMontoApertura(String montoApertura) {
		this.montoApertura = montoApertura;
	}

	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getRendimiento() {
		return rendimiento;
	}

	public void setRendimiento(String rendimiento) {
		this.rendimiento = rendimiento;
	}

	public Integer getMonedaId() {
		return monedaId;
	}

	public void setMonedaId(Integer monedaId) {
		this.monedaId = monedaId;
	}

	public String getGatNominal() {
		return gatNominal;
	}

	public void setGatNominal(String gatNominal) {
		this.gatNominal = gatNominal;
	}

	public Integer getAsesorId() {
		return asesorId;
	}

	public void setAsesorId(Integer asesorId) {
		this.asesorId = asesorId;
	}

	public Integer getComoEnteroId() {
		return comoEnteroId;
	}

	public void setComoEnteroId(Integer comoEnteroId) {
		this.comoEnteroId = comoEnteroId;
	}

	public String getComoEnteroObs() {
		return comoEnteroObs;
	}

	public void setComoEnteroObs(String comoEnteroObs) {
		this.comoEnteroObs = comoEnteroObs;
	}

	public String getClabeInterbancaria() {
		return clabeInterbancaria;
	}

	public void setClabeInterbancaria(String clabeInterbancaria) {
		this.clabeInterbancaria = clabeInterbancaria;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public String getGatReal() {
		return gatReal;
	}

	public void setGatReal(String gatReal) {
		this.gatReal = gatReal;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getProductoClave() {
		return productoClave;
	}

	public void setProductoClave(String productoClave) {
		this.productoClave = productoClave;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Integer getDatosAhorroId() {
		return datosAhorroId;
	}

	public void setDatosAhorroId(Integer datosAhorroId) {
		this.datosAhorroId = datosAhorroId;
	}

	public Integer getConceptoId() {
		return conceptoId;
	}

	public void setConceptoId(Integer conceptoId) {
		this.conceptoId = conceptoId;
	}

	public String getConceptoClave() {
		return conceptoClave;
	}

	public void setConceptoClave(String conceptoClave) {
		this.conceptoClave = conceptoClave;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Integer getAhorroDatosEstatusId() {
		return ahorroDatosEstatusId;
	}

	public void setAhorroDatosEstatusId(Integer ahorroDatosEstatusId) {
		this.ahorroDatosEstatusId = ahorroDatosEstatusId;
	}

	public String getAhorroDatosEstatus() {
		return ahorroDatosEstatus;
	}

	public void setAhorroDatosEstatus(String ahorroDatosEstatus) {
		this.ahorroDatosEstatus = ahorroDatosEstatus;
	}

	public String getTienePlastico() {
		return tienePlastico;
	}

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
