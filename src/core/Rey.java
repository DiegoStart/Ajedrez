package core;

public class Rey extends Pieza {
    public Rey(int fila, int columna, boolean esBlanca) {
        super("Rey", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        int df = Math.abs(filaDestino - fila);
        int dc = Math.abs(colDestino - columna);
        return (df <= 1 && dc <= 1) && !(df == 0 && dc == 0);
    }

    @Override
    public boolean ataca(int filaDestino, int colDestino) {
        return controla(filaDestino, colDestino);
    }

    @Override
    public boolean movimiento(int filaDestino, int colDestino) {
        return controla(filaDestino, colDestino);
    }
}

