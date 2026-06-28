public class Peon extends Pieza {
    public Peon(int fila, int columna, boolean esBlanca) {
        super("Peon", fila, columna, esBlanca);
    }

    @Override
    public boolean controla(int filaDestino, int colDestino) {
        int direccion = esBlanca ? -1 : 1;
        return filaDestino == fila + direccion && Math.abs(colDestino - columna) == 1;
    }
    
    @Override
    public boolean ataca(int filaDestino, int colDestino) {
        return controla(filaDestino, colDestino);
    }

    @Override
    public boolean movimiento(int filaDestino, int colDestino) {
        int direccion = esBlanca ? -1 : 1;
        if (colDestino == columna && filaDestino == fila + direccion) {
            return true;
        }
        if (!seMovio && colDestino == columna && filaDestino == fila + 2 * direccion) {
            return true;
        }
        return false;
    }
}
