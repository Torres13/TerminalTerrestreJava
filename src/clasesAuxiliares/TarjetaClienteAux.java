package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class TarjetaClienteAux implements IIdentificable {
    private long idTarjeta;
    private long idCliente;
    private String nomCliente;
    private String banco;
    private long numTarjeta;
    private String tarjetaConcatenada;

    public TarjetaClienteAux() {}

    public TarjetaClienteAux(long idTarjeta, long idCliente, String nomCliente, String banco, long numTarjeta, String tarjetaConcatenada) {
        this.idTarjeta = idTarjeta;
        this.idCliente = idCliente;
        this.nomCliente = nomCliente;
        this.banco = banco;
        this.numTarjeta = numTarjeta;
        this.tarjetaConcatenada = tarjetaConcatenada;
    }

    // Getters y Setters
    public long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

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

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public long getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(long numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getTarjetaConcatenada() {
        return tarjetaConcatenada;
    }

    public void setTarjetaConcatenada(String tarjetaConcatenada) {
        this.tarjetaConcatenada = tarjetaConcatenada;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idTarjeta;
    }

    public String getConcatenado() {
        return tarjetaConcatenada;
    }
}
