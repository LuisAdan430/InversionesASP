package net.cero.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the ahorro_contrato database table.
 * 
 */
public class AhorroContratoOBJ implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ahorroContratoId;
	private Integer actividadId;
	private Integer asociacionId;
	private String centroTrabajo;
	private Integer coloniaId;
	private String contrato;
	private String correoEdocuenta;
	private Integer creadoPor;
	private String ctaContable;
	private String cuenta;
	private String cuentaClabe;
	private String cuentaDestinoCap;
	private String cuentaDestinoRen;
	private String cuentaPadre;
	private Integer cveDestino;
	private String domicilio;
	private Integer errores;
	private String estatus;
	private Integer externaBanco;
	private String externaClabe;
	private String externaCuenta;
	private String externaTarjeta;
	private Date fechaApertura;
	private Date fechaCancelacion;
	private Timestamp fechaCreacion;
	private Date fechaDeposito;
	private Timestamp fechaModificacion;
	private Double gat;
	private Integer giroId;
	private Timestamp horaBloqueo;
	private Timestamp horaPrimerError;
	private Integer idComoEntero;
	private Double ingresos;
	private Double metaAportacion;
	private Integer metaDestinoId;
	private Date metaFecha;
	private Double metaMonto;
	private String metaMotivo;
	private String metaPeriodo;
	private Integer modificadoPor;
	private Integer monedaId;
	private Double montoApertura;
	private Double montoMaxAhorro;
	private Integer numBloqueo;
	private String numeroCasa;
	private Integer ocupacionId;
	private Integer oficialId;
	private String pin;
	private String pinAuto;
	private Timestamp pinFechaCambio;
	private Integer pinUsuarioCambio;
	private String provCentroTrabajo;
	private Integer provOcupacionId;
	private String provPuesto;
	private String provRecId;
	private Double provRecIngresos;
	private Double provRecMontoMaxAhorro;
	private Integer provRecRelId;
	private String puesto;
	private String referencia;
	private Integer rendimientoId;
	private Double requiereIdentificador;
	private String respaldoMd5;
	private Double saldo;
	private Integer statusBloqueo;
	private String sucursalApertura;
	private Integer tipoAhorroId;
	private String titularId;
	private String solicitante;
	private String solicitanteNombre;
	private Integer sucursalId;
	private String rfc;
	private String telefonoCoDi;
	private String fechaNacimiento;
	
	/**
	 * @return the ahorroContratoId
	 */
	public Integer getAhorroContratoId() {
		return ahorroContratoId;
	}
	/**
	 * @param ahorroContratoId the ahorroContratoId to set
	 */
	public void setAhorroContratoId(Integer ahorroContratoId) {
		this.ahorroContratoId = ahorroContratoId;
	}
	/**
	 * @return the actividadId
	 */
	public Integer getActividadId() {
		return actividadId;
	}
	/**
	 * @param actividadId the actividadId to set
	 */
	public void setActividadId(Integer actividadId) {
		this.actividadId = actividadId;
	}
	/**
	 * @return the asociacionId
	 */
	public Integer getAsociacionId() {
		return asociacionId;
	}
	/**
	 * @param asociacionId the asociacionId to set
	 */
	public void setAsociacionId(Integer asociacionId) {
		this.asociacionId = asociacionId;
	}
	/**
	 * @return the centroTrabajo
	 */
	public String getCentroTrabajo() {
		return centroTrabajo;
	}
	/**
	 * @param centroTrabajo the centroTrabajo to set
	 */
	public void setCentroTrabajo(String centroTrabajo) {
		this.centroTrabajo = centroTrabajo;
	}
	/**
	 * @return the coloniaId
	 */
	public Integer getColoniaId() {
		return coloniaId;
	}
	/**
	 * @param coloniaId the coloniaId to set
	 */
	public void setColoniaId(Integer coloniaId) {
		this.coloniaId = coloniaId;
	}
	/**
	 * @return the contrato
	 */
	public String getContrato() {
		return contrato;
	}
	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	/**
	 * @return the correoEdocuenta
	 */
	public String getCorreoEdocuenta() {
		return correoEdocuenta;
	}
	/**
	 * @param correoEdocuenta the correoEdocuenta to set
	 */
	public void setCorreoEdocuenta(String correoEdocuenta) {
		this.correoEdocuenta = correoEdocuenta;
	}
	/**
	 * @return the creadoPor
	 */
	public Integer getCreadoPor() {
		return creadoPor;
	}
	/**
	 * @param creadoPor the creadoPor to set
	 */
	public void setCreadoPor(Integer creadoPor) {
		this.creadoPor = creadoPor;
	}
	/**
	 * @return the ctaContable
	 */
	public String getCtaContable() {
		return ctaContable;
	}
	/**
	 * @param ctaContable the ctaContable to set
	 */
	public void setCtaContable(String ctaContable) {
		this.ctaContable = ctaContable;
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
	 * @return the cuentaClabe
	 */
	public String getCuentaClabe() {
		return cuentaClabe;
	}
	/**
	 * @param cuentaClabe the cuentaClabe to set
	 */
	public void setCuentaClabe(String cuentaClabe) {
		this.cuentaClabe = cuentaClabe;
	}
	/**
	 * @return the cuentaDestinoCap
	 */
	public String getCuentaDestinoCap() {
		return cuentaDestinoCap;
	}
	/**
	 * @param cuentaDestinoCap the cuentaDestinoCap to set
	 */
	public void setCuentaDestinoCap(String cuentaDestinoCap) {
		this.cuentaDestinoCap = cuentaDestinoCap;
	}
	/**
	 * @return the cuentaDestinoRen
	 */
	public String getCuentaDestinoRen() {
		return cuentaDestinoRen;
	}
	/**
	 * @param cuentaDestinoRen the cuentaDestinoRen to set
	 */
	public void setCuentaDestinoRen(String cuentaDestinoRen) {
		this.cuentaDestinoRen = cuentaDestinoRen;
	}
	/**
	 * @return the cuentaPadre
	 */
	public String getCuentaPadre() {
		return cuentaPadre;
	}
	/**
	 * @param cuentaPadre the cuentaPadre to set
	 */
	public void setCuentaPadre(String cuentaPadre) {
		this.cuentaPadre = cuentaPadre;
	}
	/**
	 * @return the cveDestino
	 */
	public Integer getCveDestino() {
		return cveDestino;
	}
	/**
	 * @param cveDestino the cveDestino to set
	 */
	public void setCveDestino(Integer cveDestino) {
		this.cveDestino = cveDestino;
	}
	/**
	 * @return the domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}
	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	/**
	 * @return the errores
	 */
	public Integer getErrores() {
		return errores;
	}
	/**
	 * @param errores the errores to set
	 */
	public void setErrores(Integer errores) {
		this.errores = errores;
	}
	/**
	 * @return the estatus
	 */
	public String getEstatus() {
		return estatus;
	}
	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	/**
	 * @return the externaBanco
	 */
	public Integer getExternaBanco() {
		return externaBanco;
	}
	/**
	 * @param externaBanco the externaBanco to set
	 */
	public void setExternaBanco(Integer externaBanco) {
		this.externaBanco = externaBanco;
	}
	/**
	 * @return the externaClabe
	 */
	public String getExternaClabe() {
		return externaClabe;
	}
	/**
	 * @param externaClabe the externaClabe to set
	 */
	public void setExternaClabe(String externaClabe) {
		this.externaClabe = externaClabe;
	}
	/**
	 * @return the externaCuenta
	 */
	public String getExternaCuenta() {
		return externaCuenta;
	}
	/**
	 * @param externaCuenta the externaCuenta to set
	 */
	public void setExternaCuenta(String externaCuenta) {
		this.externaCuenta = externaCuenta;
	}
	/**
	 * @return the externaTarjeta
	 */
	public String getExternaTarjeta() {
		return externaTarjeta;
	}
	/**
	 * @param externaTarjeta the externaTarjeta to set
	 */
	public void setExternaTarjeta(String externaTarjeta) {
		this.externaTarjeta = externaTarjeta;
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
	 * @return the fechaCancelacion
	 */
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}
	/**
	 * @param fechaCancelacion the fechaCancelacion to set
	 */
	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
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
	 * @return the fechaDeposito
	 */
	public Date getFechaDeposito() {
		return fechaDeposito;
	}
	/**
	 * @param fechaDeposito the fechaDeposito to set
	 */
	public void setFechaDeposito(Date fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
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
	 * @return the gat
	 */
	public Double getGat() {
		return gat;
	}
	/**
	 * @param gat the gat to set
	 */
	public void setGat(Double gat) {
		this.gat = gat;
	}
	/**
	 * @return the giroId
	 */
	public Integer getGiroId() {
		return giroId;
	}
	/**
	 * @param giroId the giroId to set
	 */
	public void setGiroId(Integer giroId) {
		this.giroId = giroId;
	}
	/**
	 * @return the horaBloqueo
	 */
	public Timestamp getHoraBloqueo() {
		return horaBloqueo;
	}
	/**
	 * @param horaBloqueo the horaBloqueo to set
	 */
	public void setHoraBloqueo(Timestamp horaBloqueo) {
		this.horaBloqueo = horaBloqueo;
	}
	/**
	 * @return the horaPrimerError
	 */
	public Timestamp getHoraPrimerError() {
		return horaPrimerError;
	}
	/**
	 * @param horaPrimerError the horaPrimerError to set
	 */
	public void setHoraPrimerError(Timestamp horaPrimerError) {
		this.horaPrimerError = horaPrimerError;
	}
	/**
	 * @return the idComoEntero
	 */
	public Integer getIdComoEntero() {
		return idComoEntero;
	}
	/**
	 * @param idComoEntero the idComoEntero to set
	 */
	public void setIdComoEntero(Integer idComoEntero) {
		this.idComoEntero = idComoEntero;
	}
	/**
	 * @return the ingresos
	 */
	public Double getIngresos() {
		return ingresos;
	}
	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(Double ingresos) {
		this.ingresos = ingresos;
	}
	/**
	 * @return the metaAportacion
	 */
	public Double getMetaAportacion() {
		return metaAportacion;
	}
	/**
	 * @param metaAportacion the metaAportacion to set
	 */
	public void setMetaAportacion(Double metaAportacion) {
		this.metaAportacion = metaAportacion;
	}
	/**
	 * @return the metaDestinoId
	 */
	public Integer getMetaDestinoId() {
		return metaDestinoId;
	}
	/**
	 * @param metaDestinoId the metaDestinoId to set
	 */
	public void setMetaDestinoId(Integer metaDestinoId) {
		this.metaDestinoId = metaDestinoId;
	}
	/**
	 * @return the metaFecha
	 */
	public Date getMetaFecha() {
		return metaFecha;
	}
	/**
	 * @param metaFecha the metaFecha to set
	 */
	public void setMetaFecha(Date metaFecha) {
		this.metaFecha = metaFecha;
	}
	/**
	 * @return the metaMonto
	 */
	public Double getMetaMonto() {
		return metaMonto;
	}
	/**
	 * @param metaMonto the metaMonto to set
	 */
	public void setMetaMonto(Double metaMonto) {
		this.metaMonto = metaMonto;
	}
	/**
	 * @return the metaMotivo
	 */
	public String getMetaMotivo() {
		return metaMotivo;
	}
	/**
	 * @param metaMotivo the metaMotivo to set
	 */
	public void setMetaMotivo(String metaMotivo) {
		this.metaMotivo = metaMotivo;
	}
	/**
	 * @return the metaPeriodo
	 */
	public String getMetaPeriodo() {
		return metaPeriodo;
	}
	/**
	 * @param metaPeriodo the metaPeriodo to set
	 */
	public void setMetaPeriodo(String metaPeriodo) {
		this.metaPeriodo = metaPeriodo;
	}
	/**
	 * @return the modificadoPor
	 */
	public Integer getModificadoPor() {
		return modificadoPor;
	}
	/**
	 * @param modificadoPor the modificadoPor to set
	 */
	public void setModificadoPor(Integer modificadoPor) {
		this.modificadoPor = modificadoPor;
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
	 * @return the montoApertura
	 */
	public Double getMontoApertura() {
		return montoApertura;
	}
	/**
	 * @param montoApertura the montoApertura to set
	 */
	public void setMontoApertura(Double montoApertura) {
		this.montoApertura = montoApertura;
	}
	/**
	 * @return the montoMaxAhorro
	 */
	public Double getMontoMaxAhorro() {
		return montoMaxAhorro;
	}
	/**
	 * @param montoMaxAhorro the montoMaxAhorro to set
	 */
	public void setMontoMaxAhorro(Double montoMaxAhorro) {
		this.montoMaxAhorro = montoMaxAhorro;
	}
	/**
	 * @return the numBloqueo
	 */
	public Integer getNumBloqueo() {
		return numBloqueo;
	}
	/**
	 * @param numBloqueo the numBloqueo to set
	 */
	public void setNumBloqueo(Integer numBloqueo) {
		this.numBloqueo = numBloqueo;
	}
	/**
	 * @return the numeroCasa
	 */
	public String getNumeroCasa() {
		return numeroCasa;
	}
	/**
	 * @param numeroCasa the numeroCasa to set
	 */
	public void setNumeroCasa(String numeroCasa) {
		this.numeroCasa = numeroCasa;
	}
	/**
	 * @return the ocupacionId
	 */
	public Integer getOcupacionId() {
		return ocupacionId;
	}
	/**
	 * @param ocupacionId the ocupacionId to set
	 */
	public void setOcupacionId(Integer ocupacionId) {
		this.ocupacionId = ocupacionId;
	}
	/**
	 * @return the oficialId
	 */
	public Integer getOficialId() {
		return oficialId;
	}
	/**
	 * @param oficialId the oficialId to set
	 */
	public void setOficialId(Integer oficialId) {
		this.oficialId = oficialId;
	}
	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}
	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}
	/**
	 * @return the pinAuto
	 */
	public String getPinAuto() {
		return pinAuto;
	}
	/**
	 * @param pinAuto the pinAuto to set
	 */
	public void setPinAuto(String pinAuto) {
		this.pinAuto = pinAuto;
	}
	/**
	 * @return the pinFechaCambio
	 */
	public Timestamp getPinFechaCambio() {
		return pinFechaCambio;
	}
	/**
	 * @param pinFechaCambio the pinFechaCambio to set
	 */
	public void setPinFechaCambio(Timestamp pinFechaCambio) {
		this.pinFechaCambio = pinFechaCambio;
	}
	/**
	 * @return the pinUsuarioCambio
	 */
	public Integer getPinUsuarioCambio() {
		return pinUsuarioCambio;
	}
	/**
	 * @param pinUsuarioCambio the pinUsuarioCambio to set
	 */
	public void setPinUsuarioCambio(Integer pinUsuarioCambio) {
		this.pinUsuarioCambio = pinUsuarioCambio;
	}
	/**
	 * @return the provCentroTrabajo
	 */
	public String getProvCentroTrabajo() {
		return provCentroTrabajo;
	}
	/**
	 * @param provCentroTrabajo the provCentroTrabajo to set
	 */
	public void setProvCentroTrabajo(String provCentroTrabajo) {
		this.provCentroTrabajo = provCentroTrabajo;
	}
	/**
	 * @return the provOcupacionId
	 */
	public Integer getProvOcupacionId() {
		return provOcupacionId;
	}
	/**
	 * @param provOcupacionId the provOcupacionId to set
	 */
	public void setProvOcupacionId(Integer provOcupacionId) {
		this.provOcupacionId = provOcupacionId;
	}
	/**
	 * @return the provPuesto
	 */
	public String getProvPuesto() {
		return provPuesto;
	}
	/**
	 * @param provPuesto the provPuesto to set
	 */
	public void setProvPuesto(String provPuesto) {
		this.provPuesto = provPuesto;
	}
	/**
	 * @return the provRecId
	 */
	public String getProvRecId() {
		return provRecId;
	}
	/**
	 * @param provRecId the provRecId to set
	 */
	public void setProvRecId(String provRecId) {
		this.provRecId = provRecId;
	}
	/**
	 * @return the provRecIngresos
	 */
	public Double getProvRecIngresos() {
		return provRecIngresos;
	}
	/**
	 * @param provRecIngresos the provRecIngresos to set
	 */
	public void setProvRecIngresos(Double provRecIngresos) {
		this.provRecIngresos = provRecIngresos;
	}
	/**
	 * @return the provRecMontoMaxAhorro
	 */
	public Double getProvRecMontoMaxAhorro() {
		return provRecMontoMaxAhorro;
	}
	/**
	 * @param provRecMontoMaxAhorro the provRecMontoMaxAhorro to set
	 */
	public void setProvRecMontoMaxAhorro(Double provRecMontoMaxAhorro) {
		this.provRecMontoMaxAhorro = provRecMontoMaxAhorro;
	}
	/**
	 * @return the provRecRelId
	 */
	public Integer getProvRecRelId() {
		return provRecRelId;
	}
	/**
	 * @param provRecRelId the provRecRelId to set
	 */
	public void setProvRecRelId(Integer provRecRelId) {
		this.provRecRelId = provRecRelId;
	}
	/**
	 * @return the puesto
	 */
	public String getPuesto() {
		return puesto;
	}
	/**
	 * @param puesto the puesto to set
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
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
	 * @return the rendimientoId
	 */
	public Integer getRendimientoId() {
		return rendimientoId;
	}
	/**
	 * @param rendimientoId the rendimientoId to set
	 */
	public void setRendimientoId(Integer rendimientoId) {
		this.rendimientoId = rendimientoId;
	}
	/**
	 * @return the requiereIdentificador
	 */
	public Double getRequiereIdentificador() {
		return requiereIdentificador;
	}
	/**
	 * @param requiereIdentificador the requiereIdentificador to set
	 */
	public void setRequiereIdentificador(Double requiereIdentificador) {
		this.requiereIdentificador = requiereIdentificador;
	}
	/**
	 * @return the respaldoMd5
	 */
	public String getRespaldoMd5() {
		return respaldoMd5;
	}
	/**
	 * @param respaldoMd5 the respaldoMd5 to set
	 */
	public void setRespaldoMd5(String respaldoMd5) {
		this.respaldoMd5 = respaldoMd5;
	}
	/**
	 * @return the saldo
	 */
	public Double getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	/**
	 * @return the statusBloqueo
	 */
	public Integer getStatusBloqueo() {
		return statusBloqueo;
	}
	/**
	 * @param statusBloqueo the statusBloqueo to set
	 */
	public void setStatusBloqueo(Integer statusBloqueo) {
		this.statusBloqueo = statusBloqueo;
	}
	/**
	 * @return the sucursalApertura
	 */
	public String getSucursalApertura() {
		return sucursalApertura;
	}
	/**
	 * @param sucursalApertura the sucursalApertura to set
	 */
	public void setSucursalApertura(String sucursalApertura) {
		this.sucursalApertura = sucursalApertura;
	}
	/**
	 * @return the tipoAhorroId
	 */
	public Integer getTipoAhorroId() {
		return tipoAhorroId;
	}
	/**
	 * @param tipoAhorroId the tipoAhorroId to set
	 */
	public void setTipoAhorroId(Integer tipoAhorroId) {
		this.tipoAhorroId = tipoAhorroId;
	}
	/**
	 * @return the titularId
	 */
	public String getTitularId() {
		return titularId;
	}
	/**
	 * @param titularId the titularId to set
	 */
	public void setTitularId(String titularId) {
		this.titularId = titularId;
	}
	/**
	 * @return the solicitante
	 */
	public String getSolicitante() {
		return solicitante;
	}
	/**
	 * @param solicitante the solicitante to set
	 */
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	/**
	 * @return the solicitanteNombre
	 */
	public String getSolicitanteNombre() {
		return solicitanteNombre;
	}
	/**
	 * @param solicitanteNombre the solicitanteNombre to set
	 */
	public void setSolicitanteNombre(String solicitanteNombre) {
		this.solicitanteNombre = solicitanteNombre;
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
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}
	/**
	 * @param rfc the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	/**
	 * @return the telefonoCoDi
	 */
	public String getTelefonoCoDi() {
		return telefonoCoDi;
	}
	/**
	 * @param telefonoCoDi the telefonoCoDi to set
	 */
	public void setTelefonoCoDi(String telefonoCoDi) {
		this.telefonoCoDi = telefonoCoDi;
	}
	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AhorroContratoOBJ [ahorroContratoId=");
		builder.append(ahorroContratoId);
		builder.append(", actividadId=");
		builder.append(actividadId);
		builder.append(", asociacionId=");
		builder.append(asociacionId);
		builder.append(", centroTrabajo=");
		builder.append(centroTrabajo);
		builder.append(", coloniaId=");
		builder.append(coloniaId);
		builder.append(", contrato=");
		builder.append(contrato);
		builder.append(", correoEdocuenta=");
		builder.append(correoEdocuenta);
		builder.append(", creadoPor=");
		builder.append(creadoPor);
		builder.append(", ctaContable=");
		builder.append(ctaContable);
		builder.append(", cuenta=");
		builder.append(cuenta);
		builder.append(", cuentaClabe=");
		builder.append(cuentaClabe);
		builder.append(", cuentaDestinoCap=");
		builder.append(cuentaDestinoCap);
		builder.append(", cuentaDestinoRen=");
		builder.append(cuentaDestinoRen);
		builder.append(", cuentaPadre=");
		builder.append(cuentaPadre);
		builder.append(", cveDestino=");
		builder.append(cveDestino);
		builder.append(", domicilio=");
		builder.append(domicilio);
		builder.append(", errores=");
		builder.append(errores);
		builder.append(", estatus=");
		builder.append(estatus);
		builder.append(", externaBanco=");
		builder.append(externaBanco);
		builder.append(", externaClabe=");
		builder.append(externaClabe);
		builder.append(", externaCuenta=");
		builder.append(externaCuenta);
		builder.append(", externaTarjeta=");
		builder.append(externaTarjeta);
		builder.append(", fechaApertura=");
		builder.append(fechaApertura);
		builder.append(", fechaCancelacion=");
		builder.append(fechaCancelacion);
		builder.append(", fechaCreacion=");
		builder.append(fechaCreacion);
		builder.append(", fechaDeposito=");
		builder.append(fechaDeposito);
		builder.append(", fechaModificacion=");
		builder.append(fechaModificacion);
		builder.append(", gat=");
		builder.append(gat);
		builder.append(", giroId=");
		builder.append(giroId);
		builder.append(", horaBloqueo=");
		builder.append(horaBloqueo);
		builder.append(", horaPrimerError=");
		builder.append(horaPrimerError);
		builder.append(", idComoEntero=");
		builder.append(idComoEntero);
		builder.append(", ingresos=");
		builder.append(ingresos);
		builder.append(", metaAportacion=");
		builder.append(metaAportacion);
		builder.append(", metaDestinoId=");
		builder.append(metaDestinoId);
		builder.append(", metaFecha=");
		builder.append(metaFecha);
		builder.append(", metaMonto=");
		builder.append(metaMonto);
		builder.append(", metaMotivo=");
		builder.append(metaMotivo);
		builder.append(", metaPeriodo=");
		builder.append(metaPeriodo);
		builder.append(", modificadoPor=");
		builder.append(modificadoPor);
		builder.append(", monedaId=");
		builder.append(monedaId);
		builder.append(", montoApertura=");
		builder.append(montoApertura);
		builder.append(", montoMaxAhorro=");
		builder.append(montoMaxAhorro);
		builder.append(", numBloqueo=");
		builder.append(numBloqueo);
		builder.append(", numeroCasa=");
		builder.append(numeroCasa);
		builder.append(", ocupacionId=");
		builder.append(ocupacionId);
		builder.append(", oficialId=");
		builder.append(oficialId);
		builder.append(", pin=");
		builder.append(pin);
		builder.append(", pinAuto=");
		builder.append(pinAuto);
		builder.append(", pinFechaCambio=");
		builder.append(pinFechaCambio);
		builder.append(", pinUsuarioCambio=");
		builder.append(pinUsuarioCambio);
		builder.append(", provCentroTrabajo=");
		builder.append(provCentroTrabajo);
		builder.append(", provOcupacionId=");
		builder.append(provOcupacionId);
		builder.append(", provPuesto=");
		builder.append(provPuesto);
		builder.append(", provRecId=");
		builder.append(provRecId);
		builder.append(", provRecIngresos=");
		builder.append(provRecIngresos);
		builder.append(", provRecMontoMaxAhorro=");
		builder.append(provRecMontoMaxAhorro);
		builder.append(", provRecRelId=");
		builder.append(provRecRelId);
		builder.append(", puesto=");
		builder.append(puesto);
		builder.append(", referencia=");
		builder.append(referencia);
		builder.append(", rendimientoId=");
		builder.append(rendimientoId);
		builder.append(", requiereIdentificador=");
		builder.append(requiereIdentificador);
		builder.append(", respaldoMd5=");
		builder.append(respaldoMd5);
		builder.append(", saldo=");
		builder.append(saldo);
		builder.append(", statusBloqueo=");
		builder.append(statusBloqueo);
		builder.append(", sucursalApertura=");
		builder.append(sucursalApertura);
		builder.append(", tipoAhorroId=");
		builder.append(tipoAhorroId);
		builder.append(", titularId=");
		builder.append(titularId);
		builder.append(", solicitante=");
		builder.append(solicitante);
		builder.append(", solicitanteNombre=");
		builder.append(solicitanteNombre);
		builder.append(", sucursalId=");
		builder.append(sucursalId);
		builder.append(", rfc=");
		builder.append(rfc);
		builder.append(", telefonoCoDi=");
		builder.append(telefonoCoDi);
		builder.append(", fechaNacimiento=");
		builder.append(fechaNacimiento);
		builder.append("]");
		return builder.toString();
	}
	

}