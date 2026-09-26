package consola;

import java.util.List;

import core.Tablero;
import core.piezas.*;
import modelo.*;

public class Consola {
    private Tablero tablero;

    public Consola() {
        
    }

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
    public void tablero(String nombreBlanco, String nombreNegro, boolean esTurnoBlanco, String estado, int tiempo) {
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

    private void ultimoMovimientoTiempo(int tiempo) {
        int minutos = tiempo / 60;
        int segundos = tiempo % 60;
        if (tablero.getUltimoMovimiento() == null) {
            mensaje("┌─ ÚLTIMO MOVIMIENTO ────┐┌─ TIEMPO ─────────────┐");
            mensaje("│ Ninguno                ││ Quedan: %02d:%02d min    │", minutos, segundos);
            mensaje("└────────────────────────┘└──────────────────────┘");
            return;
        }

        int[] ultimo = tablero.getUltimoMovimiento();
        mensaje("┌─ ÚLTIMO MOVIMIENTO ────┐┌ TIEMPO ──────────────┐");
        mensaje("│ %s -> %-15s  ││ Quedan: %02d:%02d min    │", casilla(ultimo[0], ultimo[1]), casilla(ultimo[2], ultimo[3]), minutos, segundos);
        mensaje("└────────────────────────┘└──────────────────────┘");
    }

    public void alertaJaque(String nombreJugador, int[][] coordenadas) {
        StringBuilder casillas = new StringBuilder();
        String posicionRey = "??";
        int contador = 0;
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                if (coordenadas[fila][col] == 2) {
                    posicionRey = casilla(fila, col);
                } else if (coordenadas[fila][col] == 1) {
                    casillas.append("[").append(casilla(fila, col)).append("] ");
                    contador++;
                }
            }
        }

        String aviso = String.format("¡Cuidado %s! Tu Rey está en jaque.", nombreJugador);
        String salidas = (contador == 0) ? "NO TIENES ESCAPATORIA" : casillas.toString().trim();    
        String estado = String.format("Rey en [%s] | Salidas (%02d): %s", posicionRey, contador, salidas);
        mensaje("┌─ ¡ALERTA DE JAQUE! ──────────────────────────────────────┐");
        mensaje("│  %-55s │", aviso);
        mensaje("│  %-55s │", estado);
        mensaje("└──────────────────────────────────────────────────────────┘");
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
    public void sinJugadoresGuardados() {
        mensaje("""
        ┌─ REGISTRO DE JUGADORES ───────────────────────────────────────────────────────┐
        │                       -- No hay jugadores guardados --                        │
        └───────────────────────────────────────────────────────────────────────────────┘""");
    }

    public void mostrarJugadoresUsuario(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            sinJugadoresGuardados();
            return;
        }

        int[] resultados = new int[5];
        for (Jugador jugador : jugadores) {
            resultados[0] += jugador.getVictorias();
            resultados[1] += jugador.getDerrotas();
            resultados[2] += jugador.getTablas();
            resultados[3] += jugador.getRendiciones();
            resultados[4] += jugador.getAbandonos();
        }
        int totalResultados = 0;
        for (int resultado : resultados) {
            totalResultados += resultado;
        }

        boolean[][] rendimiento = new boolean[4][5];
        if (totalResultados > 0) {
            for (int columna = 0; columna < 5; columna++) {
                double porcentaje = resultados[columna] * 100.0 / totalResultados;
                int altura = (int) Math.round((porcentaje / 100.0) * 4);
                for (int fila = 0; fila < altura; fila++) {
                    rendimiento[3 - fila][columna] = true;
                }
            }
        }

        int[] rangos = new int[5];
        for (Jugador jugador : jugadores) {
            int elo = jugador.getElo();
            if (elo < 1000) {
                rangos[0]++;
            } else if (elo < 1200) {
                rangos[1]++;
            } else if (elo < 1400) {
                rangos[2]++;
            } else if (elo < 1600) {
                rangos[3]++;
            } else {
                rangos[4]++;
            }
        }

        int maximo = 0;
        for (int rango : rangos) {
            if (rango > maximo) {
                maximo = rango;
            }
        }
        if (maximo == 0) {
            maximo = 1;
        }

        boolean[][] distribucion = new boolean[4][5];
        for (int columna = 0; columna < 5; columna++) {
            int altura = (int) Math.round((rangos[columna] * 4.0) / maximo);
            for (int fila = 0; fila < altura; fila++) {
                distribucion[3 - fila][columna] = true;
            }
        }

        mensaje("┌─ Rendimiento Global ────────────────────┬─ Distribución por ELO ─────────────────┐");
        String[] niveles = {"100%", " 75%", " 50%", " 25%"};
        for (int fila = 0; fila < 4; fila++) {
            StringBuilder izquierda = new StringBuilder();
            izquierda.append(String.format("%4s ┤ ", niveles[fila]));
            for (int columna = 0; columna < 5; columna++) {
                izquierda.append(rendimiento[fila][columna] ? "█" : " ");
                izquierda.append("    ");
            }
            StringBuilder derecha = new StringBuilder();
            derecha.append("    "); 
            for (int columna = 0; columna < 5; columna++) {
                String simbolo = distribucion[fila][columna] ? "█" : " ";
                derecha.append("  ").append(simbolo).append("   "); 
            }
            derecha.append("  ");
            mensaje("│ %-39s │ %-38s │", izquierda, derecha);
        }
        mensaje("│       VIC  DER  TAB  REN  ABA           │    <1000  1000  1200  1400  1600+      │");
        mensaje("├─────────────────────────────────────────┴────────────────────────────────────────┤");
        mensaje("│ # REGISTRO DE JUGADORES                                                          │");
        mensaje("├──────────────────────────────────────────────────────────────────────────────────┤");

        for (int x = 0; x < jugadores.size(); x++) {
            Jugador jugador = jugadores.get(x);
            int partidas = jugador.getVictorias() + jugador.getDerrotas() + jugador.getTablas() + jugador.getRendiciones() + jugador.getAbandonos();
            double porcentaje = partidas > 0 ? jugador.getVictorias() * 100.0 / partidas : 0.0;
            int bloques = (int) Math.round((porcentaje / 100.0) * 10);
            String barra = "█".repeat(bloques) + "░".repeat(10 - bloques);
            String nombre = jugador.getNombreJugador() != null ? jugador.getNombreJugador().toUpperCase() : "SIN_NOMBRE";
            if (nombre.length() > 8) {
                nombre = nombre.substring(0, 6) + "..";
            }
            int progreso = (int) Math.round(porcentaje);
            String informacion = String.format(" > %-8s ─ %4d ELO ─ [%s] %3d%% ─ (%3dV · %3dD · %3dT · %3dR · %3dA)",
                nombre, jugador.getElo(), barra, progreso, jugador.getVictorias(), jugador.getDerrotas(), jugador.getTablas(), jugador.getRendiciones(), jugador.getAbandonos());
            mensaje("│ %-80s │", informacion);
        }
        mensaje("└──────────────────────────────────────────────────────────────────────────────────┘");
    }

    // Movimiento
    public void sinMovimientosGuardados() {
        mensaje("""
        ┌─ HISTORIAL DE MOVIMIENTOS ────────────────────────────────────────────────────┐
        │                     -- No hay movimientos realizados --                       │
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
            int numeroMovimiento = movimiento.getColor() ? movimiento.getNumeroMovimiento() : movimiento.getNumeroMovimiento() - 1;
            String color = movimiento.getColor() ? "Blancas" : "Negras";
            String origenDestino = (movimiento.getOrigen() != null && movimiento.getDestino() != null) ? casilla(movimiento.getOrigen()[0], movimiento.getOrigen()[1]) + " -> " + casilla(movimiento.getDestino()[0], movimiento.getDestino()[1]) : "N/A";   
            String pieza = (movimiento.getPieza() != null) ? movimiento.getPieza() : "N/A";
            String notacion = (movimiento.getNotacionAlgebraica() != null) ? movimiento.getNotacionAlgebraica() : "N/A";
            String captura = (movimiento.getPiezaCapturada() != null) ? movimiento.getPiezaCapturada() : "-";
            String tipo = (movimiento.getTipoMovimiento() != null) ? movimiento.getTipoMovimiento() : "Normal";
            String estado = movimiento.getFen();  
            mensaje("│ %-3d │ %-7s │ %-9s │ %-8s │ %-8s │ %-9s │ %-16s │ %-70s │", numeroMovimiento, color, pieza, origenDestino, notacion, captura, tipo, estado);
        }
        mensaje("└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
    }
    
    public void alertaMovimiento(Movimiento movimiento) {
        if (movimiento == null) {
            return;
        }
        String caso = movimiento.getTipoMovimiento();
        if ("NORMAL".equalsIgnoreCase(caso)) {
            return;
        }

        String informacion = "";
        switch (caso) {
            case "CAPTURA":
                informacion = movimiento.getPieza() + (movimiento.getColor() ? " Blanco" : " Negro") + " capturó al " + movimiento.getPiezaCapturada() + (movimiento.getColor() ? " Negro" : " Blanco");
                break;
            case "ENROQUE_LARGO":
                informacion = "Se realizó enroque largo (0-0-0)";
                break;
            case "ENROQUE_CORTO":
                informacion = "Se realizó enroque corto (0-0)";
                break;
            case "CAPTURA_AL_PASO":
                informacion = movimiento.getPieza() + (movimiento.getColor() ? " Blanco" : " Negro") + " realizó captura al paso";
                break;
            case "PROMOCION":
                informacion = "¡El Peón se coronó exitosamente!";
                break;
        }
        if (!informacion.isEmpty()) {
            mensaje("┌─ EVENTO DE JUGADA ──────────────────────────────────────┐");
            mensaje("│  >> %-51s │", informacion);
            mensaje("└─────────────────────────────────────────────────────────┘");
        }
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

        mensaje("┌─ PARTIDAS GUARDADAS ──────────────────────────────────────────────────────┐");
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

    public void mostrarFinPartida(String causa) {
        String texto = String.format("PARTIDA FINALIZADA ── CAUSA: %s", causa);
        mensaje("┌──────────────────────────────────────────────────────────┐");
        mensaje("│  %-56s│", texto);
        mensaje("└──────────────────────────────────────────────────────────┘");
    }

    public void mostrarEstadoActualPartida() {
        mensaje("┌──────────────────────────────────────────────────────────┐");
        mensaje("│  %-56s│", "ESTADO ACTUAL DEL TABLERO");
        mensaje("└──────────────────────────────────────────────────────────┘");
    }

    // Usuario
}