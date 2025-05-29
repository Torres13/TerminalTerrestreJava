package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class TransporteAux implements IIdentificable {
    private long idTransporte;
    private String tipo;
    private String matricula;
    private String modelo;
    private String transporteConcatenado;

    // Constructor
    public TransporteAux(long idTransporte, String tipo, String matricula, String modelo, String transporteConcatenado) {
        this.idTransporte = idTransporte;
        this.tipo = tipo;
        this.matricula = matricula;
        this.modelo = modelo;
        this.transporteConcatenado = transporteConcatenado;
    }

    public TransporteAux() {}

    // Getters y Setters
    public long getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(long idTransporte) {
        this.idTransporte = idTransporte;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTransporteConcatenado() {
        return transporteConcatenado;
    }

    public void setTransporteConcatenado(String transporteConcatenado) {
        this.transporteConcatenado = transporteConcatenado;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idTransporte;
    }

    public String getConcatenado() {
        return transporteConcatenado;
    }
}