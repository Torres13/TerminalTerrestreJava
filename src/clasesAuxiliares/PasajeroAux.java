package clasesAuxiliares;

import java.sql.Date;
import java.time.LocalDate;

import terminalterrestre.Estacion.IIdentificable;

public class PasajeroAux implements IIdentificable {
    private long idPasajero;
    private String nombrePasajero;
    private Date fechaNacimiento;
    private String pasajeroConcatenado;
    private String tipoPasajero;

    // Constructor
    public PasajeroAux(long idPasajero, String nombrePasajero, Date fechaNacimiento, String pasajeroConcatenado, String tipoPasajero) {
        this.idPasajero = idPasajero;
        this.nombrePasajero = nombrePasajero;
        this.fechaNacimiento = fechaNacimiento;
        this.pasajeroConcatenado = pasajeroConcatenado;
        this.tipoPasajero = tipoPasajero;
    }

    public PasajeroAux() {}

    // Getters y Setters
    public long getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(long idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPasajeroConcatenado() {
        return pasajeroConcatenado;
    }

    public void setPasajeroConcatenado(String pasajeroConcatenado) {
        this.pasajeroConcatenado = pasajeroConcatenado;
    }

    public String getTipoPasajero() {
        return tipoPasajero;
    }

    public void setTipoPasajero(String tipoPasajero) {
        this.tipoPasajero = tipoPasajero;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idPasajero;
    }

    public String getConcatenado() {
        return pasajeroConcatenado;
    }
}
