package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class ItinerarioAux implements IIdentificable {
    private long idItinerario;
    private long idTerSal;
    private long idTerLleg;
    private String dia;
    private String horaLlegada;
    private String horaSalida;
    private int kilometros;
    private String itinerarioConcatenado;
    private String nomTerSal;
    private String nomTerLleg;

    // Constructor
    public ItinerarioAux(long idItinerario, long idTerSal, long idTerLleg, String dia, String horaLlegada, String horaSalida, int kilometros, String itinerarioConcatenado, String nomTerSal, String nomTerLleg) {
        this.idItinerario = idItinerario;
        this.idTerSal = idTerSal;
        this.idTerLleg = idTerLleg;
        this.dia = dia;
        this.horaLlegada = horaLlegada;
        this.horaSalida = horaSalida;
        this.kilometros = kilometros;
        this.itinerarioConcatenado = itinerarioConcatenado;
        this.nomTerSal = nomTerSal;
        this.nomTerLleg = nomTerLleg;
    }

    public ItinerarioAux() {}

    // Getters y Setters
    public long getIdItinerario() {
        return idItinerario;
    }

    public void setIdItinerario(long idItinerario) {
        this.idItinerario = idItinerario;
    }

    public long getIdTerSal() {
        return idTerSal;
    }

    public void setIdTerSal(long idTerSal) {
        this.idTerSal = idTerSal;
    }

    public long getIdTerLleg() {
        return idTerLleg;
    }

    public void setIdTerLleg(long idTerLleg) {
        this.idTerLleg = idTerLleg;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getKilometros() {
        return kilometros;
    }

    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }

    public String getItinerarioConcatenado() {
        return itinerarioConcatenado;
    }

    public void setItinerarioConcatenado(String itinerarioConcatenado) {
        this.itinerarioConcatenado = itinerarioConcatenado;
    }

    public String getNomTerSal() {
        return nomTerSal;
    }

    public void setNomTerSal(String nomTerSal) {
        this.nomTerSal = nomTerSal;
    }

    public String getNomTerLleg() {
        return nomTerLleg;
    }

    public void setNomTerLleg(String nomTerLleg) {
        this.nomTerLleg = nomTerLleg;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idItinerario;
    }

    public String getConcatenado() {
        return itinerarioConcatenado;
    }
}
