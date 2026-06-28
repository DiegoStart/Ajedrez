package core;

public class Torre extends Pieza {
    public Torre(int fila, int columna, boolean esBlanca) {
        super("Torre", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        return filaDestino == fila || colDestino == columna;
    }

    @Override
    public boolean ataca(int filaDestino, int colDestino) {
        return movimiento(filaDestino, colDestino);
    }

    @Override
    public boolean movimiento(int filaDestino, int colDestino) {
        return (filaDestino == fila || colDestino == columna) && !(filaDestino == fila && colDestino == columna);
    }
}
