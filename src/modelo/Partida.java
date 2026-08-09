package modelo;

import java.time.LocalDateTime;

public class Partida {
    private int idPartida;
    private String estado;
    private String resultado;
    private String causaFinalizacion;
    private String tipoPartida;
    private String tiempoControl;
    private int duracion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public Partida() {
        estado = "ESPERANDO";
        tipoPartida = "LOCAL";
        tiempoControl = "600";
    }

    public int getIdPartida() { 
        return idPartida; 
    }

    public void setIdPartida(int idPartida) { 
        this.idPartida = idPartida; 
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public String getResultado() { 
        return resultado; 
    }

    public void setResultado(String resultado) { 
        this.resultado = resultado; 
    }

    public String getCausaFinalizacion() { 
        return causaFinalizacion; 
    }

    public void setCausaFinalizacion(String causaFinalizacion) { 
        this.causaFinalizacion = causaFinalizacion; 
    }

    public String getTipoPartida() { 
        return tipoPartida; 
    }

    public void setTipoPartida(String tipoPartida) { 
        this.tipoPartida = tipoPartida; 
    }

    public String getTiempoControl() { 
        return tiempoControl; 
    }

    public void setTiempoControl(String tiempoControl) { 
        this.tiempoControl = tiempoControl; 
    }

    public int getDuracion() { 
        return duracion; 
    }

    public void setDuracion(int duracion) { 
        this.duracion = duracion; 
    }

    public LocalDateTime getFechaInicio() { 
        return fechaInicio; 
    }

    public void setFechaInicio(LocalDateTime fechaInicio) { 
        this.fechaInicio = fechaInicio; 
    }

    public LocalDateTime getFechaFin() { 
        return fechaFin; 
    }

    public void setFechaFin(LocalDateTime fechaFin) { 
        this.fechaFin = fechaFin; 
    }
}
