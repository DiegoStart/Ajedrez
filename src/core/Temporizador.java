package core;

import consola.Consola;

public class Temporizador extends Thread {
    private volatile int segundos;
    private volatile boolean pausado;
    private volatile boolean activo;

    public Temporizador(int segundos) {
        this.segundos = segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getSegundos() {
        return segundos;
    }

    public boolean tiempoAgotado() {
        return segundos <= 0;
    }

    public void pausar() {
        pausado = true;
    }

    public void reanudar() {
        pausado = false;
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        try {
            while (activo) {
                if (!pausado && segundos > 0) {
                    segundos--;
                }   
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Consola.error("Temporizador interrumpido");
        }
    }
}