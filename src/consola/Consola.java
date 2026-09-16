package consola;

import java.util.List;

import core.Tablero;
import core.Temporizador;
import core.piezas.Alfil;
import core.piezas.Caballo;
import core.piezas.Peon;
import core.piezas.Pieza;
import core.piezas.Reina;
import core.piezas.Rey;
import core.piezas.Torre;
import modelo.Jugador;
import modelo.Movimiento;
import modelo.Partida;

public class Consola {
    private Tablero tablero;

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }
    
    // Mensajes Genericos
    public void mensaje(String texto) {
        System.out.println(texto);
    }

    public void mensaje(String formato, Object... args) {
        System.out.printf(formato + "%n", args);
    }

    public String casilla(int fila, int columna) {
        return "" + (char) ('A' + columna) + (8 - fila);
    }

    public static void error(String texto) {
        System.err.println(texto);
    }

    // Menús
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
    
    public void menuPrincipal() {
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

    public void menuJugador() {
        mensaje("""
            ┌─ MENÚ DE PAUSA ──────────┐
            │  [ 1 ]  Guardar y salir  │
            │  [ 2 ]  Reiniciar        │
            │  [ 3 ]  Regresar         │
            └──────────────────────────┘""");
    }

    public void menuContrincantes(Jugador blanca, Jugador negra) {
        mensaje("""
            ┌─ JUGADORES ─────────────────────────────────────────────┐
            │  Blancas (B) > %-40s │
            │  Negras  (N) > %-40s │
            └─────────────────────────────────────────────────────────┘
            ¿Están listos para jugar? """, blanca.getNombreJugador(), negra.getNombreJugador());
    }

    public void menuPromocion() {
        mensaje("""
            ┌─ PROMOCIÓN DE PEÓN ────────┐
            │  [ 1 ]  Reina   (Q)        │
            │  [ 2 ]  Torre   (T)        │
            │  [ 3 ]  Alfil   (A)        │
            │  [ 4 ]  Caballo (C)        │
            └────────────────────────────┘"""
        );
    }

    // Tablero
    public void tablero(String nombreBlanco, String nombreNegro, boolean esTurnoBlanco, String estado, Temporizador tiempo) {
        String turnoActual = esTurnoBlanco ? "BLANCAS" : "NEGRAS";
        mensaje("┌─ PARTIDA: %-10s ────────────────── TURNO: %-7s ─┐", estado, turnoActual);
        mensaje("│ Blancas: %-18s  Negras: %-18s │", nombreBlanco, nombreNegro);
        mensaje("└─────────────────────────────────────────────────────────┘");
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
        ultimoMovimientoTiempo(tiempo);
    }

    private void ultimoMovimientoTiempo(Temporizador tiempo) {
        int minutos = tiempo.getSegundos() / 60;
        int segundos = tiempo.getSegundos() % 60;
        if (tablero.getUltimoMovimiento() == null) {
            mensaje("┌─ ÚLTIMO MOVIMIENTO ────┐┌─ TIEMPO ─────────────┐");
            mensaje("│ Ninguno                ││ Quedan: %02d:%02d min    │", minutos, segundos);
            mensaje("└────────────────────────┘└──────────────────────┘");
            return;
        }

        int[] ultimo = tablero.getUltimoMovimiento();
        mensaje("┌─ ÚLTIMO MOVIMIENTO ────┐┌ TIEMPO ──────────────┐");
        mensaje("│ %s -> %-15s  ││ Quedan: %02d:%02d min    │", casilla(8 - ultimo[0], ultimo[1]), casilla(8 - ultimo[2], ultimo[3]), minutos, segundos);
        mensaje("└────────────────────────┘└──────────────────────┘");
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

    // Instantanea
    
    // Jugador
    
    // Movimiento
    public void sinMovimientosGuardados() {
        mensaje("""
        ┌─ HISTORIAL DE MOVIMIENTOS ────────────────────────────────────────────────────┐
        │                     -- No hay movimientos guardadas --                        │
        └───────────────────────────────────────────────────────────────────────────────┘""");
    }

    public void movimientosRealizados(List<Movimiento> movimientos) {
        if (movimientos == null || movimientos.isEmpty()) {
            sinMovimientosGuardados();
            return;
        }

        mensaje("┌─ HISTORIAL DE MOVIMIENTOS ──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐");
        mensaje("│ N°  │ COLOR   │ PIEZA     │ JUGADA   │ NOTACIÓN │ CAPTURA   │ TIPO             │ ESTADO                                                                 │");
        mensaje("├─────┼─────────┼───────────┼──────────┼──────────┼───────────┼──────────────────┼────────────────────────────────────────────────────────────────────────┤");
    
        for (Movimiento movimiento : movimientos) {
            String color = movimiento.getColor() ? "Blancas" : "Negras";
            String origenDestino = (movimiento.getOrigen() != null && movimiento.getDestino() != null) ? casilla(movimiento.getOrigen()[0], movimiento.getOrigen()[1]) + " -> " + casilla(movimiento.getDestino()[0], movimiento.getDestino()[1]) : "N/A";   
            String pieza = (movimiento.getPieza() != null) ? movimiento.getPieza() : "N/A";
            String notacion = (movimiento.getNotacionAlgebraica() != null) ? movimiento.getNotacionAlgebraica() : "N/A";
            String captura = (movimiento.getPiezaCapturada() != null) ? movimiento.getPiezaCapturada() : "-";
            String tipo = (movimiento.getTipoMovimiento() != null) ? movimiento.getTipoMovimiento() : "Normal";
            String estado = movimiento.getFen();
            
            if (movimiento.getJaqueMate()) {
                estado = "Jaque Mate";
            } else if (movimiento.getJaque()) {
                estado = "Jaque";
            } else if (movimiento.getCausaTablas() != null) {
                estado = "Tablas (" + movimiento.getCausaTablas() + ")";
            }
            mensaje("│ %-3d │ %-7s │ %-9s │ %-8s │ %-8s │ %-9s │ %-16s │ %-70s │", movimiento.getNumeroMovimiento(), color, pieza, origenDestino, notacion, captura, tipo, estado);
        }
        mensaje("└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
    }
    
    // Participacion
    
    // Partida
    public void sinPartidasGuardadas() {
        mensaje("""
        ┌─ PARTIDAS GUARDADAS ─────────────────────────────────────────────────────────┐
        │                      -- No hay partidas guardadas --                         │
        └──────────────────────────────────────────────────────────────────────────────┘""");
    }

    public void partidasPendientes(List<Partida> partidas) {
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

    public void partidasFinalizadas(List<Partida> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            sinPartidasGuardadas();
            return;
        }

        mensaje("┌─ HISTORIAL DE PARTIDAS FINALIZADAS ───────────────────────────────────────┐");
        mensaje("│ ID  │ FECHA FIN         │ TIPO     │ RESULTADO    │ CAUSA DE FINALIZACIÓN │");
        mensaje("├─────┼───────────────────┼──────────┼──────────────┼───────────────────────┤");
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Partida p : partidas) {
            String fecha = (p.getFechaFin() != null) ? p.getFechaFin().format(fmt) : (p.getFechaInicio() != null) ? p.getFechaInicio().format(fmt) : "N/A";
            String tipo = (p.getTipoPartida() != null) ? p.getTipoPartida() : "LOCAL";
            String resultado = (p.getResultado() != null) ? p.getResultado() : "N/A";
            String causa = (p.getCausaFinalizacion() != null) ? p.getCausaFinalizacion() : "N/A";
            mensaje("│ %-3d │ %-17s │ %-8s │ %-12s │ %-21s │", p.getIdPartida(), fecha, tipo, resultado, causa);
        }
        mensaje("└───────────────────────────────────────────────────────────────────────────┘");
    }

    // Usuario
}