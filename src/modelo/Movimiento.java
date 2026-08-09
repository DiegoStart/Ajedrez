package modelo;

import java.time.LocalDateTime;

public class Movimiento {
    private int idMovimiento;
    private Partida partida;
    private int numeroMovimiento;
    private boolean color;
    private String pieza;
    private int[] origen;
    private int[] destino;
    private String piezaCapturada;
    private String tipoMovimiento;
    private boolean jaque;
    private boolean jaqueMate;
    private String causaTablas;
    private String notacionAlgebraica;
    private String piezaPromocion;
    private String fen;
    private LocalDateTime fechaMovimiento;

    public Movimiento() {
        origen = new int[2];
        destino = new int[2];
        tipoMovimiento = "Normal";
        jaque = false;
        jaqueMate = false;
    }

    public int getIdMovimiento() { 
        return idMovimiento; 
    }

    public void setIdMovimiento(int idMovimiento) { 
        this.idMovimiento = idMovimiento; 
    }

    public Partida getPartida() { 
        return partida; 
    }

    public void setPartida(Partida partida) { 
        this.partida = partida; 
    }

    public int getNumeroMovimiento() { 
        return numeroMovimiento; 
    }

    public void setNumeroMovimiento(int numeroMovimiento) { 
        this.numeroMovimiento = numeroMovimiento; 
    }

    public boolean getColor() { 
        return color; 
    }

    public void setColor(boolean color) { 
        this.color = color; 
    }

    public String getPieza() { 
        return pieza; 
    }

    public void setPieza(String pieza) { 
        this.pieza = pieza; 
    }

    public int[] getOrigen() { 
        return origen; 
    }

    public void setOrigen(int[] origen) { 
        this.origen = origen; 
    }

    public int[] getDestino() { 
        return destino; 
    }

    public void setDestino(int[] destino) { 
        this.destino = destino; 
    }

    public String getPiezaCapturada() { 
        return piezaCapturada; 
    }

    public void setPiezaCapturada(String piezaCapturada) { 
        this.piezaCapturada = piezaCapturada; 
    }

    public String getTipoMovimiento() { 
        return tipoMovimiento; 
    }

    public void setTipoMovimiento(String tipoMovimiento) { 
        this.tipoMovimiento = tipoMovimiento; 
    }

    public boolean getJaque() { 
        return jaque; 
    }

    public void setJaque(boolean jaque) { 
        this.jaque = jaque; 
    }

    public boolean getJaqueMate() { 
        return jaqueMate; 
    }

    public void setJaqueMate(boolean jaqueMate) { 
        this.jaqueMate = jaqueMate; 
    }

    public String getCausaTablas() { 
        return causaTablas; 
    }

    public void setCausaTablas(String causaTablas) { 
        this.causaTablas = causaTablas; 
    }

    public String getNotacionAlgebraica() { 
        return notacionAlgebraica; 
    }

    public void setNotacionAlgebraica(String notacionAlgebraica) { 
        this.notacionAlgebraica = notacionAlgebraica; 
    }

    public String getPiezaPromocion() { 
        return piezaPromocion; 
    }

    public void setPiezaPromocion(String piezaPromocion) { 
        this.piezaPromocion = piezaPromocion; 
    }

    public String getFen() { 
        return fen; 
    }

    public void setFen(String fen) { 
        this.fen = fen; 
    }

    public LocalDateTime getFechaMovimiento() { 
        return fechaMovimiento; 
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { 
        this.fechaMovimiento = fechaMovimiento; 
    }
}