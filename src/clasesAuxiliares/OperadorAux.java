package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class OperadorAux implements IIdentificable {
    private long idOperador;
    private String nombre;
    private String RFC;
    private String operadorConcatenado;

    // Constructor vacío
    public OperadorAux() {}

    // Constructor con parámetros
    public OperadorAux(long idOperador, String nombre, String RFC, String operadorConcatenado) {
        this.idOperador = idOperador;
        this.nombre = nombre;
        this.RFC = RFC;
        this.operadorConcatenado = operadorConcatenado;
    }

    // Métodos getter y setter
    public long getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(long idOperador) {
        this.idOperador = idOperador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getOperadorConcatenado() {
        return operadorConcatenado;
    }

    public void setOperadorConcatenado(String operadorConcatenado) {
        this.operadorConcatenado = operadorConcatenado;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idOperador;
    }

    @Override
    public String getConcatenado() {
        return operadorConcatenado;
    }

    // Método toString para representación en texto
    @Override
    public String toString() {
        return "OperadorAuxiliar{idOperador=" + idOperador + ", nombre='" + nombre + 
               "', RFC='" + RFC + "', operadorConcatenado='" + operadorConcatenado + "'}";
    }
}
