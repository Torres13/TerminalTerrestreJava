package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class AsientoAux implements IIdentificable {
    private long idAsiento;
    private long idSalida;
    private int disponibilidad;
    private int numAsiento;
    private String asientoConcatenado;

    // Constructor
    public AsientoAux(long idAsiento, long idSalida, int disponibilidad, int numAsiento, String asientoConcatenado) {
        this.idAsiento = idAsiento;
        this.idSalida = idSalida;
        this.disponibilidad = disponibilidad;
        this.numAsiento = numAsiento;
        this.asientoConcatenado = asientoConcatenado;
    }

    public AsientoAux() {}

    // Getters
    public long getIdAsiento() {
        return idAsiento;
    }

    public long getIdSalida() {
        return idSalida;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public int getNumAsiento() {
        return numAsiento;
    }

    public String getAsientoConcatenado() {
        return asientoConcatenado;
    }

    // Setters
    public void setIdAsiento(long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public void setIdSalida(long idSalida) {
        this.idSalida = idSalida;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setNumAsiento(int numAsiento) {
        this.numAsiento = numAsiento;
    }

    public void setAsientoConcatenado(String asientoConcatenado) {
        this.asientoConcatenado = asientoConcatenado;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idAsiento;
    }

    @Override
    public String getConcatenado() {
        return asientoConcatenado;
    }
}
