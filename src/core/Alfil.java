package core;

public class Alfil extends Pieza {
    public Alfil(int fila, int columna, boolean esBlanca) {
        super("Alfil", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        return Math.abs(filaDestino - fila) == Math.abs(colDestino - columna);
    }

    @Override
    public boolean ataca(int filaDestino, int colDestino) {
        return movimiento(filaDestino, colDestino);
    }

    @Override
    public boolean movimiento(int filaDestino, int colDestino) {
        return controla(filaDestino, colDestino) && !(filaDestino == fila && colDestino == columna);
    }
}
