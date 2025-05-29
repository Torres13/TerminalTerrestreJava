package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class SalidaAux implements IIdentificable {
    private long idSalida;
    private long idTransporte;
    private long idOperador;
    private long idItinerario;
    private float precioSalida;
    private String salidaConcatenada;

    // Constructor
    public SalidaAux(long idSalida, long idTransporte, long idOperador, long idItinerario, float precioSalida, String salidaConcatenada) {
        this.idSalida = idSalida;
        this.idTransporte = idTransporte;
        this.idOperador = idOperador;
        this.idItinerario = idItinerario;
        this.precioSalida = precioSalida;
        this.salidaConcatenada = salidaConcatenada;
    }

    public SalidaAux () {}

    // Getters y Setters
    public long getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(long idSalida) {
        this.idSalida = idSalida;
    }

    public long getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public long getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(long idOperador) {
        this.idOperador = idOperador;
    }

    public long getIdItinerario() {
        return idItinerario;
    }

    public void setIdItinerario(long idItinerario) {
        this.idItinerario = idItinerario;
    }

    public float getPrecioSalida() {
        return precioSalida;
    }

    public void setPrecioSalida(float precioSalida) {
        this.precioSalida = precioSalida;
    }

    public String getSalidaConcatenada() {
        return salidaConcatenada;
    }

    public void setSalidaConcatenada(String salidaConcatenada) {
        this.salidaConcatenada = salidaConcatenada;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idSalida;
    }

    public String getConcatenado() {
        return salidaConcatenada;
    }
}
