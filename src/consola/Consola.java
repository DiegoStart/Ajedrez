public class Consola {
    private Tablero tablero;

    // =========================
    // TABLERO ASOCIADO
    // =========================

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }

    // =========================
    // MENSAJES GENÉRICOS
    // =========================

    public void mensaje(String texto) {
        System.out.println(texto);
    }

    public void mensaje(String formato, Object... args) {
        System.out.printf(formato + "%n", args);
    }

    public void mensajeCasilla(int fila, int columna) {
        System.out.println((char)(columna + 'A') + "" + (8 - fila));
    }

    // =========================
    // MENÚS
    // =========================

    public void mostrarMenuPrincipal() {
        System.out.println("+----+-----------------------+");
        System.out.println("|   Bienvenido al ajedrez    |");
        System.out.println("+----+-----------------------+");
        System.out.println("| 1  | Nueva partida         |");
        System.out.println("| 2  | Cargar partida        |");
        System.out.println("| 0  | Salir del juego       |");
        System.out.println("+----+-----------------------+");
    }

    public void mostrarMenuJugador() {
        System.out.println("+----+-------------------+");
        System.out.println("| No | Opción            |");
        System.out.println("+----+-------------------+");
        System.out.println("| 1  | Guardar y salir   |");
        System.out.println("| 2  | Reiniciar partida |");
        System.out.println("| 3  | Regresar          |");
        System.out.println("+----+-------------------+");
    }

    public void mostrarContrincantes(String blanco, String negro) {
        System.out.println("+----+--------------------------+");
        System.out.println("|    | Jugador                  |");
        System.out.println("+----+--------------------------+");
        System.out.printf("| B  | %-24s |\n", blanco);
        System.out.printf("| N  | %-24s |\n", negro);
        System.out.println("+----+--------------------------+");
        System.out.println("¿Están listos para jugar?");
    }

    public void mostrarMenuPromocion() {
        System.out.println("+-----+------------------+");
        System.out.println("| Opc | ¡Peón promovido! |");
        System.out.println("+-----+------------------+");
        System.out.println("|  1  | Torre            |");
        System.out.println("|  2  | Alfil            |");
        System.out.println("|  3  | Caballo          |");
        System.out.println("|  4  | Reina            |");
        System.out.println("+-----+------------------+");
    }

    // =========================
    // TABLERO VISUAL
    // =========================

    public void mostrarTablero() {
        System.out.println("             Jugador Negro");
        System.out.println("     A   B   C   D   E   F   G   H");
        System.out.println("   ┌───┬───┬───┬───┬───┬───┬───┬───┐");

        for (int fila = 0; fila < 8; fila++) {
            System.out.print((8 - fila) + "  ");

            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = tablero.getPieza(fila, columna);
                String contenido = (pieza == null) ? " " : obtenerSimbolo(pieza);
                System.out.print("│ " + contenido + " ");
            }

            System.out.println("│  " + (8 - fila));

            if (fila < 7) {
                System.out.println("   ├───┼───┼───┼───┼───┼───┼───┼───┤");
            } else {
                System.out.println("   └───┴───┴───┴───┴───┴───┴───┴───┘");
            }
        }

        System.out.println("     A   B   C   D   E   F   G   H");
        System.out.println("             Jugador Blanco");

        mostrarUltimoMovimiento();
    }

    private void mostrarUltimoMovimiento() {
        if (tablero.getUltimoMovimiento() == null) {
            System.out.println("No hay último movimiento registrado.");
            return;
        }

        int[] ultimo = tablero.getUltimoMovimiento();
        char colOrigen = (char) ('A' + ultimo[1]);
        int filaOrigen = 8 - ultimo[0];
        char colDestino = (char) ('A' + ultimo[3]);
        int filaDestino = 8 - ultimo[2];
        System.out.println("Movimiento anterior: " + colOrigen + filaOrigen + " A " + colDestino + filaDestino);
    }

    private String obtenerSimbolo(Pieza pieza) {
        String simbolo = "?";

        if (pieza instanceof Rey) {
            simbolo = "K";
        } else if (pieza instanceof Reina) {
            simbolo = "Q";
        } else if (pieza instanceof Torre) {
            simbolo = "T";
        } else if (pieza instanceof Alfil) {
            simbolo = "A";
        } else if (pieza instanceof Caballo) {
            simbolo = "C";
        } else if (pieza instanceof Peon) {
            simbolo = "P";
        }

        return pieza.getEsBlanca() ? simbolo : simbolo.toLowerCase();
    }

    // =========================
    // TABLAS DE PARTIDAS
    // =========================

    public void encabezadoPartidasGuardadas() {
        System.out.println("+----+------------------------------+------------+");
        System.out.println("| No | Partida                      | Estado     |");
        System.out.println("+----+------------------------------+------------+");
    }

    public void filaPartida(int idPartida, String partida, String estado) {
        System.out.printf("| %-2d | %-28s | %-10s |%n", idPartida, partida, estado);
    }

    public void sinPartidasGuardadas() {
        System.out.println("|   --      No hay partidas guardadas      --    |");
        System.out.println("+----+------------------------------+------------+");
    }

    public void piePartidasGuardadas() {
        System.out.println("+----+------------------------------+------------+");
        System.out.println("| 0  | Regresar                                  |");
        System.out.println("+----+------------------------------+------------+");
    }

    // =========================
    // TABLAS DE MOVIMIENTOS
    // =========================

    public void encabezadoMovimientos() {
        System.out.println("+-----+--------+----------+----------+----------+----------------+");
        System.out.println("| No. | Color  | Origen   | Destino  | Pieza    | Captura        |");
        System.out.println("+-----+--------+----------+----------+----------+----------------+");
    }

    public void filaMovimiento(int numero, String color, String origen, String destino, String pieza, String captura) {
        System.out.printf("| %-3d | %-6s | %-8s | %-8s | %-8s | %-14s |%n",
                numero, color, origen, destino, pieza, captura);
    }

    public void sinMovimientos() {
        System.out.println("|             No hay movimientos registrados                    |");
    }

    public void pieMovimientos() {
        System.out.println("+-----+--------+----------+----------+----------+----------------+");
    }

    // =========================
    // TABLAS DE JUGADORES
    // =========================

    public void encabezadoJugadores() {
        System.out.println("+----+-------------------------+");
        System.out.println("| ID | Jugador                 |");
        System.out.println("+----+-------------------------+");
    }

    public void filaJugador(int idJugador, String nombre) {
        System.out.printf("| %-2d | %-28s |%n", idJugador, nombre);
    }

    public void sinJugadores() {
        System.out.println("|   --     No hay jugadores registrados     --   |");
        System.out.println("+----+------------------------------+------------+");
    }

    public void pieJugadores() {
        System.out.println("+----+------------------------------+------------+");
        System.out.println("| 0  | Regresar                                  |");
        System.out.println("+----+------------------------------+------------+");
    }
}