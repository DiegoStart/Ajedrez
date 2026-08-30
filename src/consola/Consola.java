package consola;

import java.util.List;

import core.Tablero;
import core.piezas.Alfil;
import core.piezas.Caballo;
import core.piezas.Peon;
import core.piezas.Pieza;
import core.piezas.Reina;
import core.piezas.Rey;
import core.piezas.Torre;
import modelo.Instantanea;
import modelo.Jugador;
import modelo.Movimiento;
import modelo.Participacion;
import modelo.Partida;
import modelo.Usuario;

public class Consola {
    private Tablero tablero;

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
        System.out.println((char) (columna + 'A') + "" + (8 - fila));
    }

    // =========================
    // MENÚS
    // =========================

    public void mostrarMenuPrincipal() {
        System.out.println("+------------------------------+");
        System.out.println("|   Bienvenido al ajedrez      |");
        System.out.println("+----+-------------------------+");
        System.out.println("| 1  | Nueva partida           |");
        System.out.println("| 2  | Cargar partida          |");
        System.out.println("| 3  | Historial de partida    |");
        System.out.println("| 4  | Recrear partida         |");
        System.out.println("| 5  | Exportar PGN            |");
        System.out.println("| 6  | Estadisticas            |");
        System.out.println("| 0  | Salir del juego         |");
        System.out.println("+----+-------------------------+");
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

    public void mostrarContrincantes(Jugador blanca, Jugador negra) {
        System.out.println("+----+--------------------------+");
        System.out.println("|    | Jugador                  |");
        System.out.println("+----+--------------------------+");

        String nombreBlanco = blanca.getNombreJugador();
        String nombreNegro = negra.getNombreJugador();
        System.out.printf("| B  | %-24s |%n", nombreBlanco);
        System.out.printf("| N  | %-24s |%n", nombreNegro);
        System.out.println("+----+--------------------------+");
        System.out.println("¿Están listos para jugar?");
        System.out.println();
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

    public void mostrarInformacionPartida(Partida partida) {
        System.out.println("========================================");
        System.out.println("         MODO REPRODUCCIÓN");
        System.out.println("========================================");
        System.out.println("ID: " + partida.getIdPartida());
        System.out.println("Estado: " + partida.getEstado());
        System.out.println("Resultado: " + partida.getResultado());

        if (partida.getCausaFinalizacion() != null) {
            System.out.println("Causa   : " + partida.getCausaFinalizacion());
        }
        System.out.println("Tipo de partida: " + partida.getTipoPartida());
        System.out.println("Control de tiempo: " + partida.getTiempoControl());
        System.out.println("========================================");
        System.out.println();
    }

    public void mostrarEstadisticasJugador(Jugador jugador) {
        System.out.println("========================================");
        System.out.println("         MODO ESTADISTICO");
        System.out.println("========================================");
        System.out.println("Nombre: " + jugador.getNombreJugador());
        System.out.println("Id: " + jugador.getIdJugador());
        System.out.println("ELO: " + jugador.getElo());
        System.out.println("Ganadas: " + jugador.getVictorias());
        System.out.println("Perdidas: " + jugador.getDerrotas());
        System.out.println("Tablas: " + jugador.getTablas());
        System.out.println("Rendiciones: " + jugador.getRendiciones());
        System.out.println("Abandonos: " + jugador.getAbandonos());
        System.out.println("========================================");
        System.out.println();
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

    public void filaPartida(Partida partida) {

        System.out.printf(
            "| %-2d | %-28s | %-10s |%n",
            partida.getIdPartida(),
            "Partida " + partida.getIdPartida(),
            partida.getEstado()
        );
    }

    public void sinPartidasGuardadas() {
        System.out.println(
            "|   --      No hay partidas guardadas      --    |"
        );
        System.out.println(
            "+----+------------------------------+------------+"
        );
        System.out.println();
    }

    public void piePartidasGuardadas() {
        System.out.println(
            "+----+------------------------------+------------+"
        );
        System.out.println(
            "| 0  | Regresar                                  |"
        );
        System.out.println(
            "+----+------------------------------+------------+"
        );
    }

    public void mostrarPartidas(List<Partida> partidas) {

        if (partidas == null || partidas.isEmpty()) {
            sinPartidasGuardadas();
            return;
        }

        encabezadoPartidasGuardadas();

        for (Partida partida : partidas) {
            filaPartida(partida);
        }

        piePartidasGuardadas();
    }

    // =========================
    // TABLAS DE MOVIMIENTOS
    // =========================

    public void encabezadoMovimientos() {
        System.out.println(
            "+-----+--------+----------+----------+----------+----------------+----------------+"
        );

        System.out.println(
            "| No. | Color  | Origen   | Destino  | Pieza    | Captura        | Notación       |"
        );

        System.out.println(
            "+-----+--------+----------+----------+----------+----------------+----------------+"
        );
    }

    public void filaMovimiento(Movimiento movimiento) {

        String color = movimiento.getColor()
                ? "Blancas"
                : "Negras";

        String origen = convertirCoordenada(
            movimiento.getOrigen()
        );

        String destino = convertirCoordenada(
            movimiento.getDestino()
        );

        String captura = movimiento.getPiezaCapturada();

        if (captura == null) {
            captura = "-";
        }

        System.out.printf(
            "| %-3d | %-6s | %-8s | %-8s | %-8s | %-14s | %-14s |%n",
            movimiento.getNumeroMovimiento(),
            color,
            origen,
            destino,
            movimiento.getPieza(),
            captura,
            movimiento.getNotacionAlgebraica()
        );
    }

    public void sinMovimientos() {
        System.out.println(
            "|                      No hay movimientos registrados                      |"
        );
    }

    public void pieMovimientos() {
        System.out.println(
            "+-----+--------+----------+----------+----------+----------------+----------------+"
        );
        System.out.println();
    }

    public void mostrarMovimientos(List<Movimiento> movimientos) {

        if (movimientos == null || movimientos.isEmpty()) {
            sinMovimientos();
            return;
        }

        encabezadoMovimientos();

        for (Movimiento movimiento : movimientos) {
            filaMovimiento(movimiento);
        }

        pieMovimientos();
    }

    // =========================
    // TABLAS DE JUGADORES
    // =========================

    public void encabezadoJugadores() {
        System.out.println("+----+------------------------------+");
        System.out.println("| ID | Nombre                       |");
        System.out.println("+----+------------------------------+");
    }

    public void filaJugador(Jugador jugador) {

        System.out.printf(
            "| %-2d | %-28s |%n",
            jugador.getIdJugador(),
            jugador.getNombreJugador()
        );
    }

    public void sinJugadores() {
        System.out.println(
            "| -- | No hay jugadores registrados |"
        );
    }

    public void pieJugadores() {
        System.out.println(
            "+----+------------------------------+"
        );
        System.out.println(
            "| 0  | Regresar                     |"
        );
        System.out.println(
            "+----+------------------------------+"
        );
    }

    public void mostrarJugadores(List<Jugador> jugadores) {

        if (jugadores == null || jugadores.isEmpty()) {
            sinJugadores();
            return;
        }

        encabezadoJugadores();

        for (Jugador jugador : jugadores) {
            filaJugador(jugador);
        }

        pieJugadores();
    }

    // =========================
    // PARTICIPACIONES
    // =========================

    public void mostrarParticipaciones(
            List<Participacion> participaciones) {

        if (participaciones == null || participaciones.isEmpty()) {
            System.out.println("No hay participaciones registradas.");
            return;
        }

        System.out.println(
            "\n========== PARTICIPACIONES =========="
        );

        for (Participacion participacion : participaciones) {

            System.out.println("----------------------------------------");

            System.out.println(
                "ID: " + participacion.getIdParticipacion()
            );

            if (participacion.getPartida() != null) {
                System.out.println(
                    "Partida: "
                    + participacion.getPartida().getIdPartida()
                );
            }

            if (participacion.getJugador() != null) {
                System.out.println(
                    "Jugador: "
                    + participacion.getJugador().getNombreJugador()
                );
            }

            System.out.println(
                "Color: "
                + (participacion.getColor()
                    ? "Blancas"
                    : "Negras")
            );

            System.out.println(
                "Resultado: "
                + participacion.getResultadoIndividual()
            );

            System.out.println(
                "Tiempo restante: "
                + participacion.getTiempoRestante()
            );
        }

        System.out.println("----------------------------------------");
    }

    // =========================
    // INSTANTÁNEAS
    // =========================

    public void mostrarInstantaneas(
            List<Instantanea> instantaneas) {

        if (instantaneas == null || instantaneas.isEmpty()) {
            System.out.println("No hay instantáneas registradas.");
            return;
        }

        System.out.println(
            "\n========== INSTANTÁNEAS =========="
        );

        for (Instantanea instantanea : instantaneas) {

            System.out.println("----------------------------------------");

            System.out.println(
                "ID: " + instantanea.getIdInstantanea()
            );

            if (instantanea.getPartida() != null) {
                System.out.println(
                    "Partida: "
                    + instantanea.getPartida().getIdPartida()
                );
            }

            System.out.println(
                "Estado: "
                + instantanea.getEstadoActual()
            );

            System.out.println(
                "Turno: "
                + (instantanea.getTurnoActual()
                    ? "Blancas"
                    : "Negras")
            );

            System.out.println(
                "Tiempo blancas: "
                + instantanea.getTiempoBlancas()
            );

            System.out.println(
                "Tiempo negras: "
                + instantanea.getTiempoNegras()
            );

            System.out.println(
                "Contador movimientos: "
                + instantanea.getContadorMovimientos()
            );

            System.out.println(
                "50 movimientos: "
                + instantanea.getCincuentaMovimientos()
            );
        }

        System.out.println("----------------------------------------");
    }

    // =========================
    // USUARIOS
    // =========================

    public void mostrarUsuarios(List<Usuario> usuarios) {

        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        System.out.println(
            "\n========== USUARIOS =========="
        );

        for (Usuario usuario : usuarios) {

            System.out.println("----------------------------------------");

            System.out.println(
                "ID: " + usuario.getIdUsuario()
            );

            System.out.println(
                "Nombre: " + usuario.getNombreUsuario()
            );

            System.out.println(
                "Correo: " + usuario.getCorreo()
            );

            System.out.println(
                "Fecha de nacimiento: "
                + usuario.getFechaNacimiento()
            );

            System.out.println(
                "Género: " + usuario.getGenero()
            );

            System.out.println(
                "Estado: " + usuario.getEstado()
            );

            System.out.println(
                "Fecha de registro: "
                + usuario.getFechaRegistro()
            );

            System.out.println(
                "Último acceso: "
                + usuario.getUltimoAcceso()
            );
        }

        System.out.println("----------------------------------------");
    }

    // =========================
    // PARTIDAS EN CURSO
    // =========================

    public void mostrarPartidasEnCurso(List<Partida> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            System.out.println("No hay partidas en curso.");
            return;
        }

        System.out.println("\n========== PARTIDAS EN CURSO ==========");
        for (Partida partida : partidas) {
            System.out.println(
                "----------------------------------------"
            );

            System.out.println(
                "ID: " + partida.getIdPartida()
            );

            System.out.println(
                "Estado: " + partida.getEstado()
            );

            System.out.println(
                "Resultado: " + partida.getResultado()
            );

            System.out.println(
                "Causa finalización: "
                + partida.getCausaFinalizacion()
            );

            System.out.println(
                "Tipo de partida: "
                + partida.getTipoPartida()
            );

            System.out.println(
                "Control de tiempo: "
                + partida.getTiempoControl()
            );

            System.out.println(
                "Duración: "
                + partida.getDuracion()
                + " segundos"
            );

            System.out.println(
                "Fecha de inicio: "
                + partida.getFechaInicio()
            );

            System.out.println(
                "Fecha de fin: "
                + partida.getFechaFin()
            );
        }

        System.out.println(
            "----------------------------------------"
        );
    }

    // =========================
    // CONVERSIONES
    // =========================

    private String convertirCoordenada(int[] coordenada) {

        if (coordenada == null || coordenada.length < 2) {
            return "-";
        }

        char columna = (char) ('A' + coordenada[1]);
        int fila = 8 - coordenada[0];

        return "" + columna + fila;
    }
}