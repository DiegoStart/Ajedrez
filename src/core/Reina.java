public class Reina extends Pieza {
    public Reina(int fila, int columna, boolean esBlanca) {
        super("Reina", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        boolean Fila = filaDestino == fila;
        boolean Columna = colDestino == columna;
        boolean Diagonal = Math.abs(filaDestino - fila) == Math.abs(colDestino - columna);
        return Fila || Columna || Diagonal;
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
