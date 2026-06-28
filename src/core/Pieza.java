public abstract class Pieza {
    protected String nombre;
    protected int fila;
    protected int columna;
    protected boolean esBlanca;
    protected boolean seMovio = false;

    public Pieza(String nombre, int fila, int columna, boolean esBlanca) {
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.esBlanca = esBlanca;
    }

    public void setFila(int fila) { 
        this.fila = fila; 
    }

    public void setColumna(int columna) { 
        this.columna = columna; 
    }

    public void setSeMovio(boolean seMovio) { 
        this.seMovio = seMovio; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public int getFila() { 
        return fila; 
    }

    public int getColumna() { 
        return columna; 
    }

    public boolean getEsBlanca() { 
        return esBlanca; 
    }
    
    public boolean getSeMovio() { 
        return seMovio; 
    }

    public abstract boolean controla(int filaDestino, int colDestino);

    public abstract boolean ataca(int filaDestino, int colDestino);

    public abstract boolean movimiento(int filaDestino, int colDestino);
}
