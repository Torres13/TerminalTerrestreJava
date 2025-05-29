package clasesAuxiliares;

import java.sql.Date;


import terminalterrestre.Estacion.IIdentificable;

public class TransaccionAux implements IIdentificable {
    private long idTransaccion;
    private long idSalida;
    private long idTarjeta;
    private String fechaTransaccion;
    private Date fecha;
    private float total;
    private String transConcatenada;

    // Constructor
    public TransaccionAux(long idTransaccion, long idSalida, long idTarjeta, String fechaTransaccion, Date fecha, float total, String transConcatenada) {
        this.idTransaccion = idTransaccion;
        this.idSalida = idSalida;
        this.idTarjeta = idTarjeta;
        this.fechaTransaccion = fechaTransaccion;
        this.fecha = fecha;
        this.total = total;
        this.transConcatenada = transConcatenada;
    }
    public TransaccionAux() {}


    // Getters
    public long getIdTransaccion() {
        return idTransaccion;
    }

    public long getIdSalida() {
        return idSalida;
    }

    public long getIdTarjeta() {
        return idTarjeta;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public float getTotal() {
        return total;
    }

    public String getTransConcatenada() {
        return transConcatenada;
    }

    public void setIdTransaccion(long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setIdSalida(long idSalida) {
        this.idSalida = idSalida;
    }

    public void setIdTarjeta(long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setTransConcatenada(String transConcatenada) {
        this.transConcatenada = transConcatenada;
    }


    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idTransaccion;
    }

    @Override
    public String getConcatenado() {
        return transConcatenada;
    }
}
