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
    
    // MENSAJES GENÉRICOS
    public void mensaje(String texto) {
        System.out.println(texto);
    }

    public void mensaje(String formato, Object... args) {
        System.out.printf(formato + "%n", args);
    }

    public void casilla(int fila, int columna) {
        mensaje("%c%d", (char) (columna + 'A'), 8 - fila);
    }

    public static void error(String texto) {
        System.err.println(texto);
    }

    // MENÚS
    public void titulo() {
        mensaje("""

         █████╗      ██╗███████╗██████╗ ██████╗ ███████╗███████╗       █████╗  ██╗     ██████╗ ██╗  ██╗  █████╗ 
        ██╔══██╗     ██║██╔════╝██╔══██╗██╔══██╗██╔════╝╚══███╔╝      ██╔══██╗ ██║     ██╔══██╗██║  ██║ ██╔══██╗
        ███████║     ██║█████╗  ██║  ██║██████╔╝█████╗    ███╔╝  ██║  ███████║ ██║     ██████╔╝███████║ ███████║
        ██╔══██║██╗  ██║██╔══╝  ██║  ██║██╔══██╗██╔══╝   ███╔╝        ██╔══██║ ██║     ██╔═══╝ ██╔══██║ ██╔══██║
        ██║  ██║╚█████╔╝███████╗██████╔╝██║  ██║███████╗███████╗      ██║  ██║ ███████╗██║     ██║  ██║ ██║  ██║
        ╚═╝  ╚═╝ ╚════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═╝ ╚══════╝╚═╝     ╚═╝  ╚═╝ ╚═╝  ╚═╝
        """);
    }
    
    public void mostrarMenuPrincipal() {
        titulo();
        mensaje("""
            ┌─ MENÚ PRINCIPAL ───────────────────────── ESTADO: OFFLINE ─┐                                                 
            │  [ 1 ]  NUEVA PARTIDA                                      │
            │  [ 2 ]  CARGAR PARTIDA                                     │
            │  [ 3 ]  HISTORIAL DE PARTIDAS                              │
            │  [ 4 ]  RECREAR PARTIDA                                    │
            │  [ 5 ]  EXPORTAR PGN                                       │
            │  [ 6 ]  ESTADÍSTICAS                                       │
            │                                                            │
            │  [ 0 ]  SALIR DEL JUEGO                                    │
            └────────────────────────────────────────────────────────────┘""");
    }

    public void mostrarMenuJugador() {
        mensaje("""
            ┌─ MENÚ DE PAUSA ──────────┐
            │  [ 1 ]  Guardar y salir  │
            │  [ 2 ]  Reiniciar        │
            │  [ 3 ]  Regresar         │
            └──────────────────────────┘""");
    }

    public void mostrarContrincantes(Jugador blanca, Jugador negra) {
        mensaje("""
            ┌─ JUGADORES ─────────────────────────────────────────────┐
            │  Blancas (B) > %-40s │
            │  Negras  (N) > %-40s │
            └─────────────────────────────────────────────────────────┘
            ¿Están listos para jugar? """, blanca.getNombreJugador(), negra.getNombreJugador());
    }

    public void mostrarMenuPromocion() {
        mensaje("""
            ┌─ PROMOCIÓN DE PEÓN ────────┐
            │  [ 1 ]  Reina   (Q)        │
            │  [ 2 ]  Torre   (T)        │
            │  [ 3 ]  Alfil   (A)        │
            │  [ 4 ]  Caballo (C)        │
            └────────────────────────────┘"""
        );
    }

    public void mostrarInformacionPartida(Partida partida) {
        mensaje("========================================");
        mensaje("         MODO REPRODUCCIÓN");
        mensaje("========================================");
        mensaje("ID: %d", partida.getIdPartida());
        mensaje("Estado: %s", partida.getEstado());
        mensaje("Resultado: %s", partida.getResultado());

        if (partida.getCausaFinalizacion() != null) {
            mensaje("Causa   : %s", partida.getCausaFinalizacion());
        }

        mensaje("Tipo de partida: %s", partida.getTipoPartida());
        mensaje("Control de tiempo: %s", partida.getTiempoControl());
        mensaje("========================================");
        mensaje("");
    }

    public void mostrarEstadisticasJugador(Jugador jugador) {
        mensaje("========================================");
        mensaje("         MODO ESTADISTICO");
        mensaje("========================================");
        mensaje("Nombre: %s", jugador.getNombreJugador());
        mensaje("Id: %d", jugador.getIdJugador());
        mensaje("ELO: %d", jugador.getElo());
        mensaje("Ganadas: %d", jugador.getVictorias());
        mensaje("Perdidas: %d", jugador.getDerrotas());
        mensaje("Tablas: %d", jugador.getTablas());
        mensaje("Rendiciones: %d", jugador.getRendiciones());
        mensaje("Abandonos: %d", jugador.getAbandonos());
        mensaje("========================================");
        mensaje("");
    }

    // TABLERO VISUAL
    public void mostrarTablero(String nombreBlanco, String nombreNegro, boolean esTurnoBlanco, String estado) {
        String turnoActual = esTurnoBlanco ? "BLANCAS" : "NEGRAS";
        String jugador = esTurnoBlanco ? nombreBlanco : nombreNegro;
        mensaje("┌─ PARTIDA: %-10s ────────────────── TURNO: %-7s ─┐", estado, turnoActual);
        mensaje("│ Blancas: %-18s  Negras: %-18s │", nombreBlanco, nombreNegro);
        mensaje("└─────────────────────────────────────────────────────────┘\n");
        mensaje("     A   B   C   D   E   F   G   H");
        mensaje("   ┌───┬───┬───┬───┬───┬───┬───┬───┐");

        for (int fila = 0; fila < 8; fila++) {
            StringBuilder linea = new StringBuilder();
            linea.append(String.format(" %d │", 8 - fila));
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = tablero.getPieza(fila, columna);
                String contenido;
                if (pieza == null) {
                    contenido = " ";
                } else {
                    contenido = obtenerSimbolo(pieza);
                }
                linea.append(" ").append(contenido).append(" │");
            }
            linea.append(String.format(" %d", 8 - fila));
            mensaje(linea.toString());
            if (fila < 7) {
                mensaje("   ├───┼───┼───┼───┼───┼───┼───┼───┤");
            } else {
                mensaje("   └───┴───┴───┴───┴───┴───┴───┴───┘");
            }
        }
        mensaje("     A   B   C   D   E   F   G   H");
        mostrarUltimoMovimiento();
        mensaje("Comandos: MENU | TABLAS | RENDIRSE");
        mensaje("[" + jugador + "] > ");
    }

    private void mostrarUltimoMovimiento() {
        if (tablero.getUltimoMovimiento() == null) {
            mensaje("┌─ ÚLTIMO MOVIMIENTO ───────────────────────┐");
            mensaje("│ Ninguno                                   │");
            mensaje("└───────────────────────────────────────────┘");
            return;
        }

        int[] ultimo = tablero.getUltimoMovimiento();
        char colOrigen = (char) ('A' + ultimo[1]);
        int filaOrigen = 8 - ultimo[0];
        char colDestino = (char) ('A' + ultimo[3]);
        int filaDestino = 8 - ultimo[2];
        mensaje("┌─ ÚLTIMO MOVIMIENTO ───────────────────────┐");
        mensaje("│ %c%d -> %c%d                                  │", colOrigen, filaOrigen, colDestino, filaDestino);
        mensaje("└───────────────────────────────────────────┘");
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

    // TABLAS DE PARTIDAS
    public void sinPartidasGuardadas() {
        mensaje("""
        ┌─ PARTIDAS GUARDADAS ─────────────────────────────────────────────────────────┐
        │                      -- No hay partidas guardadas --                         │
        └──────────────────────────────────────────────────────────────────────────────┘""");
    }

    public void mostrarPartidas(List<Partida> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            sinPartidasGuardadas();
            return;
        }

        mensaje("┌─ PARTIDAS GUARDADAS ─────────────────────────────────────────────────────────┐");
        mensaje("│ ID  │ FECHA Y HORA        │ TIPO     │ TIEMPO          │ ESTADO              │");
        mensaje("├─────┼─────────────────────┼──────────┼─────────────────┼─────────────────────┤");
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Partida p : partidas) {
            String fecha = (p.getFechaInicio() != null) ? p.getFechaInicio().format(fmt) : "N/A";
            String tipo = (p.getTipoPartida() != null) ? p.getTipoPartida().toString() : "LOCAL";
            String tiempo = p.getTiempoControl();
            String estado = (p.getEstado() != null) ? p.getEstado().toString() : "EN PAUSA";
            mensaje("│ %-3d │ %-19s │ %-8s │ %-15s │ %-19s │", p.getIdPartida(), fecha, tipo, tiempo, estado);
        }
        mensaje("└──────────────────────────────────────────────────────────────────────────────┘");
    }

    // TABLAS DE MOVIMIENTOS
    public void encabezadoMovimientos() {
        mensaje("+-----+--------+----------+----------+----------+----------------+----------------+");
        mensaje("| No. | Color  | Origen   | Destino  | Pieza    | Captura        | Notación       |");
        mensaje("+-----+--------+----------+----------+----------+----------------+----------------+");
    }

    public void filaMovimiento(Movimiento movimiento) {
        String color = movimiento.getColor() ? "Blancas" : "Negras";
        String origen = convertirCoordenada(movimiento.getOrigen());
        String destino = convertirCoordenada(movimiento.getDestino());
        String captura = movimiento.getPiezaCapturada();

        if (captura == null) {
            captura = "-";
        }
        mensaje("| %-3d | %-6s | %-8s | %-8s | %-8s | %-14s | %-14s |", movimiento.getNumeroMovimiento(), color, origen, destino, movimiento.getPieza(), captura, movimiento.getNotacionAlgebraica());
    }

    public void sinMovimientos() {
        mensaje("|                      No hay movimientos registrados                      |");
    }

    public void pieMovimientos() {
        mensaje("+-----+--------+----------+----------+----------+----------------+----------------+");
        mensaje("");
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

    // TABLAS DE JUGADORES
    public void encabezadoJugadores() {
        mensaje("+----+------------------------------+");
        mensaje("| ID | Nombre                       |");
        mensaje("+----+------------------------------+");
    }

    public void filaJugador(Jugador jugador) {
        mensaje("| %-2d | %-28s |", jugador.getIdJugador(), jugador.getNombreJugador());
    }

    public void sinJugadores() {
        mensaje("| -- | No hay jugadores registrados |");
    }

    public void pieJugadores() {
        mensaje("+----+------------------------------+");
        mensaje("| 0  | Regresar                     |");
        mensaje("+----+------------------------------+");
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

    // PARTICIPACIONES
    public void mostrarParticipaciones(List<Participacion> participaciones) {
        if (participaciones == null || participaciones.isEmpty()) {
            mensaje("No hay participaciones registradas.");
            return;
        }
        mensaje("");
        mensaje("========== PARTICIPACIONES ==========");

        for (Participacion participacion : participaciones) {
            mensaje("----------------------------------------");
            mensaje("ID: %d", participacion.getIdParticipacion());
            if (participacion.getPartida() != null) {
                mensaje("Partida: %d", participacion.getPartida().getIdPartida());
            }
            if (participacion.getJugador() != null) {
                mensaje("Jugador: %s", participacion.getJugador().getNombreJugador());
            }
            mensaje("Color: %s", participacion.getColor() ? "Blancas" : "Negras");
            mensaje("Resultado: %s", participacion.getResultadoIndividual());
            mensaje("Tiempo restante: %s", participacion.getTiempoRestante());
        }

        mensaje("----------------------------------------");
    }

    // INSTANTÁNEAS
    public void mostrarInstantaneas(List<Instantanea> instantaneas) {
        if (instantaneas == null || instantaneas.isEmpty()) {
            mensaje("No hay instantáneas registradas.");
            return;
        }
        mensaje("");
        mensaje("========== INSTANTÁNEAS ==========");

        for (Instantanea instantanea : instantaneas) {
            mensaje("----------------------------------------");
            mensaje("ID: %d", instantanea.getIdInstantanea());
            if (instantanea.getPartida() != null) {
                mensaje("Partida: %d", instantanea.getPartida().getIdPartida());
            }
            mensaje("Estado: %s", instantanea.getEstadoActual());
            mensaje("Turno: %s", instantanea.getTurnoActual() ? "Blancas" : "Negras");
            mensaje("Tiempo blancas: %s", instantanea.getTiempoBlancas());
            mensaje("Tiempo negras: %s", instantanea.getTiempoNegras());
            mensaje("Contador movimientos: %d", instantanea.getContadorMovimientos());
            mensaje("50 movimientos: %s", instantanea.getCincuentaMovimientos());
        }
        mensaje("----------------------------------------");
    }

    // USUARIOS
    public void mostrarUsuarios(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            mensaje("No hay usuarios registrados.");
            return;
        }
        mensaje("");
        mensaje("========== USUARIOS ==========");

        for (Usuario usuario : usuarios) {
            mensaje("----------------------------------------");
            mensaje("ID: %d", usuario.getIdUsuario());
            mensaje("Nombre: %s", usuario.getNombreUsuario());
            mensaje("Correo: %s", usuario.getCorreo());
            mensaje("Fecha de nacimiento: %s", usuario.getFechaNacimiento());
            mensaje("Género: %s", usuario.getGenero());
            mensaje("Estado: %s", usuario.getEstado());
            mensaje("Fecha de registro: %s", usuario.getFechaRegistro());
            mensaje("Último acceso: %s", usuario.getUltimoAcceso());
        }
        mensaje("----------------------------------------");
    }

    // PARTIDAS EN CURSO
    public void mostrarPartidasEnCurso(List<Partida> partidas) {
        mostrarPartidas(partidas);
    }

    // CONVERSIONES
    private String convertirCoordenada(int[] coordenada) {
        if (coordenada == null || coordenada.length < 2) {
            return "-";
        }
        char columna = (char) ('A' + coordenada[1]);
        int fila = 8 - coordenada[0];
        return "" + columna + fila;
    }
}