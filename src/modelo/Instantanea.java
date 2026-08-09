package modelo;

public class Instantanea {
    private int idInstantanea;
    private Partida partida;
    private String estadoActual;
    private boolean turnoActual;
    private int tiempoBlancas;
    private int tiempoNegras;
    private int[] ultimoMovimiento;
    private int contadorMovimientos;
    private int cincuentaMovimientos;

    public Instantanea() {
        contadorMovimientos = 1;
        cincuentaMovimientos = 0;
    }

    public int getIdInstantanea() { 
        return idInstantanea; 
    }

    public void setIdInstantanea(int idInstantanea) { 
        this.idInstantanea = idInstantanea; 
    }

    public Partida getPartida() { 
        return partida; 
    }

    public void setPartida(Partida partida) { 
        this.partida = partida; 
    }

    public String getEstadoActual() { 
        return estadoActual; 
    }

    public void setEstadoActual(String estadoActual) { 
        this.estadoActual = estadoActual; 
    }

    public boolean getTurnoActual() { 
        return turnoActual; 
    }

    public void setTurnoActual(boolean turnoActual) { 
        this.turnoActual = turnoActual; 
    }

    public int getTiempoBlancas() { 
        return tiempoBlancas; 
    }

    public void setTiempoBlancas(int tiempoBlancas) { 
        this.tiempoBlancas = tiempoBlancas; 
    }

    public int getTiempoNegras() { 
        return tiempoNegras; 
    }

    public void setTiempoNegras(int tiempoNegras) { 
        this.tiempoNegras = tiempoNegras; 
    }

    public int[] getUltimoMovimiento() {
        return ultimoMovimiento;
    }

    public void setUltimoMovimiento(int[] ultimoMovimiento) {
        this.ultimoMovimiento = ultimoMovimiento;
    }

    public int getContadorMovimientos() { 
        return contadorMovimientos; 
    }

    public void setContadorMovimientos(int contadorMovimientos) { 
        this.contadorMovimientos = contadorMovimientos; 
    }

    public int getCincuentaMovimientos() { 
        return cincuentaMovimientos; 
    }

    public void setCincuentaMovimientos(int cincuentaMovimientos) { 
        this.cincuentaMovimientos = cincuentaMovimientos; 
    }
}