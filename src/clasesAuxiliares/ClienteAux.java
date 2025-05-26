package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class ClienteAux implements IIdentificable {
    private long idCliente;
    private String nomCliente;
    private long telefono;
    private String email;
    private String clienteConcatenado;

    // Constructor vacío
    public ClienteAux() {}

    // Constructor con parámetros
    public ClienteAux(long idCliente, String nomCliente, long telefono, String email, String clienteConcatenado) {
        this.idCliente = idCliente;
        this.nomCliente = nomCliente;
        this.telefono = telefono;
        this.email = email;
        this.clienteConcatenado = clienteConcatenado;
    }

    // Métodos getter y setter
    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClienteConcatenado() {
        return clienteConcatenado;
    }

    public void setClienteConcatenado(String clienteConcatenado) {
        this.clienteConcatenado = clienteConcatenado;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idCliente;
    }

    @Override
    public String getConcatenado() {
        return clienteConcatenado;
    }

    // Método toString para representación en texto
    @Override
    public String toString() {
        return "ClienteAux{idCliente=" + idCliente + ", nomCliente='" + nomCliente +
               "', telefono=" + telefono + ", email='" + email +
               "', clienteConcatenado='" + clienteConcatenado + "'}";
    }
}
