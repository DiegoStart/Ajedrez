package modelo;

public class Participacion {
    private int idParticipacion;
    private Partida partida;
    private Jugador jugador;
    private boolean color;
    private String resultadoIndividual;
    private int tiempoRestante;

    public Participacion() {

    }

    public int getIdParticipacion() { 
        return idParticipacion; 
    }

    public void setIdParticipacion(int idParticipacion) { 
        this.idParticipacion = idParticipacion; 
    }

    public Partida getPartida() { 
        return partida; 
    }

    public void setPartida(Partida partida) { 
        this.partida = partida; 
    }

    public Jugador getJugador() { 
        return jugador; 
    }

    public void setJugador(Jugador jugador) { 
        this.jugador = jugador; 
    }

    public boolean getColor() { 
        return color; 
    }

    public void setColor(boolean color) { 
        this.color = color; 
    }

    public String getResultadoIndividual() { 
        return resultadoIndividual; 
    }

    public void setResultadoIndividual(String resultadoIndividual) { 
        this.resultadoIndividual = resultadoIndividual; 
    }

    public int getTiempoRestante() { 
        return tiempoRestante; 
    }

    public void setTiempoRestante(int tiempoRestante) { 
        this.tiempoRestante = tiempoRestante; 
    }
}