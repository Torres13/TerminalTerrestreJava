package clasesAuxiliares;

import terminalterrestre.Estacion.IIdentificable;

public class TerminalAux implements IIdentificable {
    private long idTerminal;
    private long idEstacion;
    private String nomTerminal;
    private String nomEstacion;
    private String terminalConcatenada;

    // Constructor vacío
    public TerminalAux() {}

    // Constructor con parámetros
    public TerminalAux(long idTerminal, long idEstacion, String nomTerminal, String nomEstacion, String terminalConcatenada) {
        this.idTerminal = idTerminal;
        this.idEstacion = idEstacion;
        this.nomTerminal = nomTerminal;
        this.nomEstacion = nomEstacion;
        this.terminalConcatenada = terminalConcatenada;
    }

    // Métodos getter y setter
    public long getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(long idTerminal) {
        this.idTerminal = idTerminal;
    }

    public long getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(long idEstacion) {
        this.idEstacion = idEstacion;
    }

    public String getNomTerminal() {
        return nomTerminal;
    }

    public void setNomTerminal(String nomTerminal) {
        this.nomTerminal = nomTerminal;
    }

    public String getNomEstacion() {
        return nomEstacion;
    }

    public void setNomEstacion(String nomEstacion) {
        this.nomEstacion = nomEstacion;
    }

    public String getTerminalConcatenada() {
        return terminalConcatenada;
    }

    public void setTerminalConcatenada(String terminalConcatenada) {
        this.terminalConcatenada = terminalConcatenada;
    }

    // Implementación de IIdentificable
    @Override
    public long getId() {
        return idTerminal;
    }

    @Override
    public String getConcatenado() {
        return terminalConcatenada;
    }

    // Método toString para representación en texto
    @Override
    public String toString() {
        return "TerminalAux{idTerminal=" + idTerminal + ", idEstacion=" + idEstacion +
               ", nomTerminal='" + nomTerminal + "', nomEstacion='" + nomEstacion +
               "', terminalConcatenada='" + terminalConcatenada + "'}";
    }
}