/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class CiudadAux implements IIdentificable {
    private long idCiudad;
    private String nomCiudad;
    private String nomConcatenado;

    // Constructor vacío
    public CiudadAux() {}

    // Constructor con parámetros
    public CiudadAux(long idCiudad, String nomCiudad, String nomConcatenado) {
        this.idCiudad = idCiudad;
        this.nomCiudad = nomCiudad;
        this.nomConcatenado = nomConcatenado;
    }

    // Métodos getter y setter
    public long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(long idCiudad) {
        this.idCiudad = idCiudad;
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

    // Método toString para representar la clase en texto
    @Override
    public String toString() {
        return "CiudadAux{idCiudad=" + idCiudad + ", nomCiudad='" + nomCiudad + "', nomConcatenado='" + nomConcatenado + "'}";
    }

     @Override
    public long getId() {
        return idCiudad;
    }

    @Override
    public String getConcatenado() {
        return nomCiudad;
    }

}
