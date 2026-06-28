package core;

public class Jugador {
    private String nombre;
    private boolean esBlanco;

    public Jugador(String nombre, boolean esBlanco) {
        this.nombre = nombre;
        this.esBlanco = esBlanco;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getEsBlanco() {
        return esBlanco;
    }
}