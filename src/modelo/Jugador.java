package modelo;

import java.time.LocalDateTime;

public class Jugador {
    private int idJugador;
    private Usuario usuario;
    private String nombreJugador;
    private String tipo;
    private int elo;
    private int victorias;
    private int derrotas;
    private int tablas;
    private int rendiciones;
    private int abandonos;
    private LocalDateTime fechaCreacion;

    public Jugador() {
        tipo = "HUMANO";
        elo = 1200;
        victorias = 0;
        derrotas = 0;
        tablas = 0;
        rendiciones = 0;
        abandonos = 0;
    }

    public int getIdJugador() { 
        return idJugador; 
    }

    public void setIdJugador(int idJugador) { 
        this.idJugador = idJugador; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }

    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public String getNombreJugador() { 
        return nombreJugador; 
    }

    public void setNombreJugador(String nombreJugador) { 
        this.nombreJugador = nombreJugador; 
    }

    public String getTipo() { 
        return tipo; 
    }

    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }

    public int getElo() { 
        return elo; 
    }

    public void setElo(int elo) { 
        this.elo = elo; 
    }

    public int getVictorias() { 
        return victorias; 
    }

    public void setVictorias(int victorias) { 
        this.victorias = victorias; 
    }

    public int getDerrotas() { 
        return derrotas; 
    }

    public void setDerrotas(int derrotas) { 
        this.derrotas = derrotas; 
    }

    public int getTablas() { 
        return tablas; 
    }

    public void setTablas(int tablas) { 
        this.tablas = tablas; 
    }

    public int getRendiciones() { 
        return rendiciones; 
    }

    public void setRendiciones(int rendiciones) { 
        this.rendiciones = rendiciones; 
    }

    public int getAbandonos() { 
        return abandonos; 
    }

    public void setAbandonos(int abandonos) { 
        this.abandonos = abandonos; 
    }

    public LocalDateTime getFechaCreacion() { 
        return fechaCreacion; 
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { 
        this.fechaCreacion = fechaCreacion; 
    }

    public void registrarVictoria() {
        victorias++;
    }

    public void registrarDerrota() {
        derrotas++;
    }

    public void registrarTablas() {
        tablas++;
    }

    public void registrarRendicion() {
        rendiciones++;
    }

    public void registrarAbandonos() {
        abandonos++;
    }
    
    
    public void modificarElo(int eloContrario, double resultado) {
        double esperado = 1.0 / (1 + Math.pow(10, (eloContrario - elo) / 400.0));
        int k = 32;
        elo += Math.round(k * (resultado - esperado));
    }
}