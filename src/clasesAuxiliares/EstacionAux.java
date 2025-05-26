package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class EstacionAux implements IIdentificable {
    private long idEstacion;
    private long idCiudad;
    private String nomEstacion;
    private String nomCiudad;
    private String nomConcatenado;
    private String Direccion;

    // Constructor vacío
    public EstacionAux()  {}

    // Constructor con parámetros
    public EstacionAux(long idEstacion, long idCiudad, String nomEstacion, String nomCiudad, String nomConcatenado, String Direccion) {
        this.idEstacion = idEstacion;
        this.idCiudad = idCiudad;
        this.nomEstacion = nomEstacion;
        this.nomCiudad = nomCiudad;
        this.nomConcatenado = nomConcatenado;
        this.Direccion = Direccion;
    }

    // Métodos getter y setter
    public long getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(long idEstacion) {
        this.idEstacion = idEstacion;
    }

    public long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNomEstacion() {
        return nomEstacion;
    }

    public void setNomEstacion(String nomEstacion) {
        this.nomEstacion = nomEstacion;
    }

    public String getNomCiudad() {
        return nomCiudad;
    }

    public void setNomCiudad(String nomCiudad) {
        this.nomCiudad = nomCiudad;
    }

    public String getNomConcatenado() {
        return nomConcatenado;
    }

    public void setNomConcatenado(String nomConcatenado) {
        this.nomConcatenado = nomConcatenado;
    }

     public String getDireccion() {
        return Direccion;
    }

    public void SetDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    // Método toString para representación en texto
    @Override
    public String toString() {
        return "EstacionAux{idEstacion=" + idEstacion + ", idCiudad=" + idCiudad +
               ", nomEstacion='" + nomEstacion + "', nomCiudad='" + nomCiudad +
               "', nomConcatenado='" + nomConcatenado + "'}";
    }

      @Override
    public long getId() {
        return idEstacion;
    }

    @Override
    public String getConcatenado() {
        return nomConcatenado;
    }
}
