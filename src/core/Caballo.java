public class Caballo extends Pieza {
    public Caballo(int fila, int columna, boolean esBlanca) {
        super("Caballo", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        int df = Math.abs(filaDestino - fila);
        int dc = Math.abs(colDestino - columna);
        return (df == 2 && dc == 1) || (df == 1 && dc == 2);
    }

    @Override
    public boolean ataca(int filaDestino, int colDestino) {
        return movimiento(filaDestino, colDestino);
    }

    @Override
    public boolean movimiento(int filaDestino, int colDestino) {
        return controla(filaDestino, colDestino)
               && !(filaDestino == fila && colDestino == columna);
    }
}
