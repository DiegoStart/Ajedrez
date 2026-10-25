package consola;

import java.util.List;

import core.Tablero;
import core.piezas.*;
import modelo.*;

public class Consola {
    public String RESET;
    //Estilos
    public String NEGRITA;
    // Paleta (Retro Vector Arcade)
    public String rojo;     // Carmesí / Rojo neón
    public String amarillo;  // Amarillo brillante
    public String azul;
    public String blanco; // Blanco puro
    // Colores de piezas
    public String blancas;
    public String negras;
    private Tablero tablero;

    public Consola() {
        RESET = "\u001B[0m";
        NEGRITA = "\u001B[1m";
        rojo = "\u001B[38;2;255;30;86m";
        amarillo = "\u001B[38;2;255;221;0m";
        blanco = "\u001B[38;2;255;255;255m";
        azul = "\u001B[38;2;0;140;255m";
        blancas = blanco + NEGRITA;
        negras = rojo + NEGRITA;
    }

    public String getReset() {
        return RESET;
    }

    public String getNegrita() {
        return NEGRITA;
    }

    public String getRojo() {
        return rojo;
    }

    public String getAmarillo() {
        return amarillo;
    }

    public String getBlanco() {
        return blanco;
    }

    public String getAzul() {
        return azul;
    }

    public String getBlancas() {
        return blancas;
    }

    public String getNegras() {
        return negras;
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

    public void error(String texto) {
        System.err.println(azul + texto + RESET);
    }

    //Notificaciones
    public void fin(String caso, String ganador, String perdedor) {
        switch (caso) {
            case "JAQUE_MATE":
                mensaje(rojo + "── [ ¡JAQUE MATE! ] ────────────────────────────────────────" + RESET);
                mensaje(rojo + ">> " + blanco + "%s ha perdido" + rojo + " | " + amarillo + "GANADOR: %s" + RESET, perdedor, ganador);
                mensaje(rojo + "────────────────────────────────────────────────────────────" + RESET);
                break;
            case "TIEMPO_AGOTADO":
                mensaje(rojo + "── [ TIEMPO AGOTADO ] ──────────────────────────────────────" + RESET);
                mensaje(rojo + ">> " + blanco + "%s pierde por tiempo" + rojo + " | " + amarillo + "GANADOR: %s" + RESET, perdedor, ganador);
                mensaje(rojo + "────────────────────────────────────────────────────────────" + RESET);
                break; 
            case "RENDICION":
                mensaje(rojo + "── [ RENDICIÓN: FIN DEL JUEGO ] ────────────────────────────" + RESET);
                mensaje(rojo + ">> " + blanco + "%s se ha rendido" + rojo + " | " + amarillo + "GANADOR: %s" + RESET, perdedor, ganador);
                mensaje(rojo + "────────────────────────────────────────────────────────────" + RESET);
                break; 
            case "ACUERDO_MUTUO":
                mensaje(amarillo + "── [ TABLAS POR ACUERDO MUTUO ] ────────────────────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! Fin del juego. %s y %s acordaron tablas." + RESET, ganador, perdedor);
                mensaje(amarillo + "────────────────────────────────────────────────────────────" + RESET);
                break;
            case "50_MOVIMIENTOS":
                mensaje(amarillo + "── [ REGLA DE LOS 50 MOVIMIENTOS: TABLAS RECLAMADAS ] ─────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! 50 jugadas sin capturas ni avance de peón. Fin del juego." + RESET);
                mensaje(amarillo + "───────────────────────────────────────────────────────────────────" + RESET);
                break;
            case "TRIPLE_REPETICION":
                mensaje(amarillo + "── [ TRIPLE REPETICIÓN: TABLAS RECLAMADAS ] ────────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! Posición repetida 3 veces. Fin del juego." + RESET);
                mensaje(amarillo + "────────────────────────────────────────────────────────────" + RESET);
                break;
            case "AHOGADO":
                mensaje(amarillo + "── [ AHOGADO: TABLAS ] ─────────────────────────────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! Sin movimientos válidos. Fin del juego." + RESET);
                mensaje(amarillo + "────────────────────────────────────────────────────────────" + RESET);
                break;
            case "MATERIAL_INSUFICIENTE":
                mensaje(amarillo + "── [ MATERIAL INSUFICIENTE: TABLAS ] ───────────────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! No hay piezas suficientes. Fin del juego." + RESET);
                mensaje(amarillo + "────────────────────────────────────────────────────────────" + RESET);
                break;
            case "75_MOVIMIENTOS":
                mensaje(amarillo + "── [ REGLA DE LOS 75 MOVIMIENTOS: TABLAS RECLAMADAS ] ─────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! 75 jugadas sin capturas ni avance de peón. Fin del juego." + RESET);
                mensaje(amarillo + "───────────────────────────────────────────────────────────────────" + RESET);
                break;
            case "QUINTA_REPETICION":
                mensaje(amarillo + "── [ QUINTA REPETICIÓN: TABLAS RECLAMADAS ] ────────────────" + RESET);
                mensaje(amarillo + ">> " + blanco + "¡Alto! Posición repetida 5 veces. Fin del juego." + RESET);
                mensaje(amarillo + "────────────────────────────────────────────────────────────" + RESET);
                break;
        }
    }

    public void guardado(int idPartida) {
        mensaje(rojo + "┌─ " + amarillo + "GUARDADO EXITOSO " + rojo + "───────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│  " + blanco + ">> Partida #%d guardada correctamente.                   " + rojo + "│", idPartida);
        mensaje(rojo + "└──────────────────────────────────────────────────────────┘" + RESET);
    }

    // Menús
    public void titulo() {
        mensaje((rojo + NEGRITA) + """

         █████╗      ██╗███████╗██████╗ ██████╗ ███████╗███████╗       █████╗  ██╗     ██████╗ ██╗  ██╗  █████╗ 
        ██╔══██╗     ██║██╔════╝██╔══██╗██╔══██╗██╔════╝╚══███╔╝      ██╔══██╗ ██║     ██╔══██╗██║  ██║ ██╔══██╗
        ███████║     ██║█████╗  ██║  ██║██████╔╝█████╗    ███╔╝  ██║  ███████║ ██║     ██████╔╝███████║ ███████║
        ██╔══██║██╗  ██║██╔══╝  ██║  ██║██╔══██╗██╔══╝   ███╔╝        ██╔══██║ ██║     ██╔═══╝ ██╔══██║ ██╔══██║
        ██║  ██║╚█████╔╝███████╗██████╔╝██║  ██║███████╗███████╗      ██║  ██║ ███████╗██║     ██║  ██║ ██║  ██║
        ╚═╝  ╚═╝ ╚════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═╝ ╚══════╝╚═╝     ╚═╝  ╚═╝ ╚═╝  ╚═╝
        """ + RESET);
    }
    
    public void menuInicio() {
        titulo();
        mensaje(rojo + "┌─" + amarillo + " ¡A JUGAR AJEDREZ!" + rojo + " ────────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "[ 1 ]  Soy nuevo por aquí                                 " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 2 ]  Ya tengo cuenta                                    " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 3 ]  Solo quiero una partida rápida                     " + rojo + "│");
        mensaje(rojo + "│                                                            │");
        mensaje(rojo + "│  " + blanco + "[ 0 ]  Cerrar                                             " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }
    
    public void menuPrincipal(Usuario usuario) {
        String nombre = usuario != null ? usuario.getNombreUsuario() : "INVITADO";

        mensaje(rojo + "┌─" + amarillo + " MENÚ PRINCIPAL " + rojo + "─".repeat(28 - nombre.length()) + blanco + " BIENVENIDO: " + amarillo + nombre + rojo + " ─┐");                                              
        mensaje(rojo + "│  " + blanco + "[ 1 ]  NUEVA PARTIDA                                      " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 2 ]  CARGAR PARTIDA                                     " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 3 ]  HISTORIAL DE PARTIDAS                              " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 4 ]  RECREAR PARTIDA                                    " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 5 ]  EXPORTAR PGN                                       " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 6 ]  ESTADÍSTICAS                                       " + rojo + "│");
        mensaje(rojo + "│                                                            │");
        mensaje(rojo + "│  " + blanco + "[ 0 ]  CERRAR SESIÓN                                      " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void menuInvitado() {
        String nombre = "INVITADO";

        mensaje(rojo + "┌─" + amarillo + " PARITDA RAPIDA " + rojo + "─".repeat(28 - nombre.length()) + blanco + " BIENVENIDO: " + amarillo + nombre + rojo + " ─┐");                                              
        mensaje(rojo + "│  " + blanco + "[ 1 ]  NUEVA PARTIDA                                      " + rojo + "│");
        mensaje(rojo + "│                                                            │");
        mensaje(rojo + "│  " + blanco + "[ 0 ]  SALIR DEL JUEGO                                    " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void menuJugador() {
        mensaje(rojo + "┌─ " + amarillo + "MENÚ DE PAUSA" + rojo + " ──────────┐");
        mensaje(rojo + "│ " + blanco + " [ 1 ]  Guardar y salir  " + rojo + "│");
        mensaje(rojo + "│ " + blanco + " [ 2 ]  Reiniciar        " + rojo + "│");
        mensaje(rojo + "│ " + blanco + " [ 3 ]  Regresar         " + rojo + "│");
        mensaje(rojo + "└──────────────────────────┘" + RESET);
    }

    public void menuContrincantes(Jugador blanca, Jugador negra) {
        mensaje(rojo + "┌─ " + amarillo + "JUGADORES" + rojo + " ─────────────────────────────────────────────┐");
        mensaje(rojo + "│ " + blancas + " Blancas (B) > %-40s" + rojo + " │", blanca.getNombreJugador());
        mensaje(rojo + "│ " + negras + " Negras  (N) > %-40s" + rojo + " │" + RESET, negra.getNombreJugador());
        mensaje(rojo + "└─────────────────────────────────────────────────────────┘" + RESET);
        mensaje(amarillo + ">> ¿Están listos para jugar? " + RESET);
    }

    public void menuPromocion() {
        mensaje(rojo + "┌─ " + amarillo + "PROMOCIÓN DE PEÓN" + rojo + " ────────┐");
        mensaje(rojo + "│ " + blanco + " [ 1 ]  Reina   (Q)        " + rojo + "│");
        mensaje(rojo + "│ " + blanco + " [ 2 ]  Torre   (T)        " + rojo + "│");
        mensaje(rojo + "│ " + blanco + " [ 3 ]  Alfil   (A)        " + rojo + "│");
        mensaje(rojo + "│ " + blanco + " [ 4 ]  Caballo (C)        " + rojo + "│");
        mensaje(rojo + "└────────────────────────────┘" + RESET);
    }

    public void menuSalida() {
        mensaje(rojo + "┌─ " + amarillo + "APAGANDO CONSOLA " + rojo + "─────────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│  " + amarillo + ">> Desconectando...                                       " + rojo + "│" + RESET);
        mensaje(rojo + "│  " + amarillo + ">> ¡Gracias por jugar!                                    " + rojo + "│" + RESET);
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void menuGenero() {
        mensaje(rojo + "┌─" + amarillo + " SELECCIONA TU GÉNERO" + rojo + " ─────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "[ 1 ]  MASCULINO                                          " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 2 ]  FEMENINO                                           " + rojo + "│");
        mensaje(rojo + "│  " + blanco + "[ 3 ]  OTRO / PREFIERO NO ESPECIFICAR                     " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void bannerFechaNacimiento() {
        mensaje(rojo + "┌─" + amarillo + " FECHA DE NACIMIENTO" + rojo + " ──────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "Ingresa los datos solicitados a continuación.             " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void bannerRegistro() {
        mensaje(rojo + "┌─" + amarillo + " CREAR NUEVO PERFIL " + rojo + "───────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "Ingresa tus datos para completar el registro.             " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void exitoRegistro(String nombre) {
        mensaje(rojo + "┌─" + amarillo + " REGISTRO EXITOSO " + rojo + "─────────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + String.format("¡Bienvenido al juego %-37s", nombre + "!") + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void bannerIniciarSesion() {
        mensaje(rojo + "┌─" + amarillo + " INICIAR SESIÓN " + rojo + "───────────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "Ingresa tus credenciales de acceso.                       " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void exitoIniciarSesion(String nombre) {
        mensaje(rojo + "┌─" + amarillo + " SESIÓN INICIADA " + rojo + "──────────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + String.format("Hola de nuevo %-44s", nombre) + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void exitoCerrarSesion() {
        mensaje(rojo + "┌─" + amarillo + " CERRAR SESIÓN " + rojo + "────────────────────────────────────────────┐");
        mensaje(rojo + "│  " + blanco + "Sesión cerrada correctamente. ¡Hasta pronto!              " + rojo + "│");
        mensaje(rojo + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void errorCaja(String Error) {
        mensaje(azul + "┌─ ERROR ────────────────────────────────────────────────────┐");
        mensaje(azul + "│  " + blanco + String.format("%-58s", Error) + azul + "│");
        mensaje(azul + "└────────────────────────────────────────────────────────────┘" + RESET);
    }

    // Tablero
    public void tablero(String nombreBlanco, String nombreNegro, boolean esTurnoBlanco, String estado, int tiempo) {
        String turnoActual = esTurnoBlanco ? blancas + "BLANCAS" : negras + "NEGRAS ";
        mensaje(rojo + "┌─ PARTIDA: " + amarillo + "%-10s " + rojo + "────────────────── TURNO: %-7s" + rojo + " ─┐" + RESET, estado, turnoActual);
        mensaje(rojo + "│ " + blanco + "Blancas: " + blancas + "%-18s  " + rojo + "Negras: " + negras + "%-18s " + rojo + "│" + RESET, nombreBlanco, nombreNegro);
        mensaje(rojo + "└─────────────────────────────────────────────────────────┘" + RESET);
        mensaje(rojo + "     A   B   C   D   E   F   G   H" + RESET);
        mensaje(amarillo + "   ┌───┬───┬───┬───┬───┬───┬───┬───┐" + RESET);

        for (int fila = 0; fila < 8; fila++) {
            StringBuilder linea = new StringBuilder();
            linea.append(amarillo).append(String.format(" %d │", 8 - fila)).append(RESET);
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = tablero.getPieza(fila, columna);
                String contenido;
                if (pieza == null) {
                    contenido = " ";
                } else {
                    if (pieza.getEsBlanca()) {
                        contenido = blancas + obtenerSimbolo(pieza) + RESET;
                    } else {
                        contenido = negras + obtenerSimbolo(pieza) + RESET;
                    }
                }
                linea.append(" ").append(contenido).append(amarillo).append(" │").append(RESET);
            }
            linea.append(amarillo).append(String.format(" %d", 8 - fila)).append(RESET);
            mensaje(linea.toString());
            if (fila < 7) {
                mensaje(amarillo + "   ├───┼───┼───┼───┼───┼───┼───┼───┤" + RESET);
            } else {
                mensaje(amarillo + "   └───┴───┴───┴───┴───┴───┴───┴───┘" + RESET);
            }
        }
        mensaje(rojo + "     A   B   C   D   E   F   G   H" + RESET);
        ultimoMovimientoTiempo(tiempo);
    }

    private void ultimoMovimientoTiempo(int tiempo) {
        int minutos = tiempo / 60;
        int segundos = tiempo % 60;
        if (tablero.getUltimoMovimiento() == null) {
            mensaje(rojo + "┌─ ÚLTIMO MOVIMIENTO ────┐┌─ TIEMPO ─────────────┐" + RESET);
            mensaje(rojo + "│ " + blanco + "Ninguno                " + rojo + "││ " + amarillo + "Quedan: %02d:%02d min    " + rojo + "│" + RESET, minutos, segundos);
            mensaje(rojo + "└────────────────────────┘└──────────────────────┘" + RESET);
            return;
        }

        int[] ultimo = tablero.getUltimoMovimiento();
        mensaje(rojo + "┌─ ÚLTIMO MOVIMIENTO ────┐┌ TIEMPO ──────────────┐" + RESET);
        mensaje(rojo + "│ " + amarillo + "%s -> %-15s  " + rojo + "││ " + amarillo + "Quedan: %02d:%02d min    " + rojo + "│" + RESET, casilla(ultimo[0], ultimo[1]), casilla(ultimo[2], ultimo[3]), minutos, segundos);
        mensaje(rojo + "└────────────────────────┘└──────────────────────┘" + RESET);
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
        mensaje((rojo + NEGRITA) + "┌─ ¡ALERTA DE JAQUE! ──────────────────────────────────────┐");
        mensaje("│  " + blanco + "%-55s " + rojo + "│", aviso);
        mensaje("│  " + blanco + "%-55s " + rojo + "│", estado);
        mensaje("└──────────────────────────────────────────────────────────┘" + RESET);
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
        mensaje(rojo + "┌─ " + amarillo + "REGISTRO DE JUGADORES " + rojo + "───────────────────────────────────────────────────────┐");
        mensaje("│                       " + blanco + "-- No hay jugadores guardados --                        " + rojo + "│");
        mensaje("└───────────────────────────────────────────────────────────────────────────────┘" + RESET);
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

        mensaje(rojo + "┌─ " + amarillo + "RENDIMIENTO GLOBAL " + rojo + "────────────────────┬─ " + blanco + "DISTRIBUCIÓN POR ELO " + rojo + "─────────────────┐" + RESET);
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
            mensaje(rojo + "│" + blanco + " %-39s " + rojo + "│" + amarillo + " %-38s " + rojo + "│", izquierda, derecha);
        }
        mensaje(rojo + "│       " + amarillo + "VIC  DER  TAB  REN  ABA" + rojo + "           │    " + blanco + "<1000  1000  1200  1400  1600+" + rojo + "      │" + RESET);
        mensaje(rojo + "├─────────────────────────────────────────┴────────────────────────────────────────┤" + RESET);
        mensaje(rojo + "│ " + blanco + "# REGISTRO DE JUGADORES                                                          " + rojo + "│");
        mensaje(rojo + "├──────────────────────────────────────────────────────────────────────────────────┤" + RESET);

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
            String informacion = String.format(" > %-8s ─ %4d ELO ─ [%s] %3d%% ─ (%3dV · %3dD · %3dT · %3dR · %3dA)", nombre, jugador.getElo(), barra, progreso, jugador.getVictorias(), jugador.getDerrotas(), jugador.getTablas(), jugador.getRendiciones(), jugador.getAbandonos());
            mensaje(rojo + "│ " + amarillo + "%-80s " + rojo + "│", informacion);
        }
        mensaje(rojo + "└──────────────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    // Movimiento
    public void sinMovimientosGuardados() {
        mensaje(rojo + "┌─ " + amarillo + "HISTORIAL DE MOVIMIENTOS " + rojo + "────────────────────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│                     " + blanco + "-- No hay movimientos realizados --                       " + rojo + "│");
        mensaje(rojo + "└───────────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void movimientosRealizados(List<Movimiento> movimientos) {
        if (movimientos == null || movimientos.isEmpty()) {
            sinMovimientosGuardados();
            return;
        }

        mensaje(rojo + "┌─ " + amarillo + "HISTORIAL DE MOVIMIENTOS " + rojo + "──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│ " + amarillo + "N°  " + rojo + "│ " + amarillo + "COLOR   " + rojo + "│ " + amarillo + "PIEZA     " + rojo + "│ " + amarillo + "JUGADA   " + rojo + "│ " + amarillo + "NOTACIÓN " + rojo + "│ " + amarillo + "CAPTURA   " + rojo + "│ " + amarillo + "TIPO             " + rojo + "│ " + amarillo + "ESTADO                                                                 " + rojo + "│" + RESET);
        mensaje(rojo + "├─────┼─────────┼───────────┼──────────┼──────────┼───────────┼──────────────────┼────────────────────────────────────────────────────────────────────────┤" + RESET);
        for (Movimiento movimiento : movimientos) {
            String tinte = movimiento.getColor() ? blancas : negras;
            int numeroMovimiento = movimiento.getColor() ? movimiento.getNumeroMovimiento() : movimiento.getNumeroMovimiento() - 1;
            String color = movimiento.getColor() ? "Blancas" : "Negras";
            String origenDestino = (movimiento.getOrigen() != null && movimiento.getDestino() != null) ? casilla(movimiento.getOrigen()[0], movimiento.getOrigen()[1]) + " -> " + casilla(movimiento.getDestino()[0], movimiento.getDestino()[1]) : "N/A";   
            String pieza = (movimiento.getPieza() != null) ? movimiento.getPieza() : "N/A";
            String notacion = (movimiento.getNotacionAlgebraica() != null) ? movimiento.getNotacionAlgebraica() : "N/A";
            String captura = (movimiento.getPiezaCapturada() != null) ? movimiento.getPiezaCapturada() : "-";
            String tipo = (movimiento.getTipoMovimiento() != null) ? movimiento.getTipoMovimiento() : "Normal";
            String estado = movimiento.getFen();  
            mensaje(rojo + "│" + tinte + " %-3d " + rojo + "│" + tinte + " %-7s " + rojo + "│" + tinte + " %-9s " + rojo + "│" + tinte + " %-8s " + rojo + "│" + tinte + " %-8s " + rojo + "│" + tinte + " %-9s " + rojo + "│" + tinte + " %-16s " + rojo + "│" + tinte + " %-70s " + rojo + "│" + RESET, numeroMovimiento, color, pieza, origenDestino, notacion, captura, tipo, estado);
        }
        mensaje(rojo + "└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
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
            mensaje(amarillo + "┌─ EVENTO DE JUGADA ──────────────────────────────────────┐");
            mensaje("│  " + blanco + ">> %-51s " + amarillo + "│", informacion);
            mensaje("└─────────────────────────────────────────────────────────┘" + RESET);
        }
    }
    
    // Participacion
    
    // Partida
    public void sinPartidasGuardadas() {
        mensaje(rojo + "┌─ " + amarillo + "PARTIDAS GUARDADAS " + rojo + "─────────────────────────────────────────────────────────┐");
        mensaje("│                      " + blanco + "-- No hay partidas guardadas --                         " + rojo + "│");
        mensaje("└──────────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void partidasPendientes(List<Partida> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            sinPartidasGuardadas();
            return;
        }

        mensaje(rojo + "┌─ " + amarillo + "PARTIDAS GUARDADAS " + rojo + "─────────────────────────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│" + amarillo + " ID  " + rojo + "│" + amarillo + " FECHA Y HORA        " + rojo + "│" + amarillo + " TIPO     " + rojo + "│" + amarillo + " TIEMPO          " + rojo + "│" + amarillo + " ESTADO              " + rojo + "│" + RESET);
        mensaje(rojo + "├─────┼─────────────────────┼──────────┼─────────────────┼─────────────────────┤" + RESET);
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Partida p : partidas) {
            String fecha = (p.getFechaInicio() != null) ? p.getFechaInicio().format(fmt) : "N/A";
            String tipo = (p.getTipoPartida() != null) ? p.getTipoPartida().toString() : "LOCAL";
            String tiempo = p.getTiempoControl();
            String estado = (p.getEstado() != null) ? p.getEstado().toString() : "EN PAUSA";
            mensaje(rojo + "│" + blanco + " %-3d " + rojo + "│" + blanco + " %-19s " + rojo + "│" + blanco + " %-8s " + rojo + "│" + blanco + " %-15s " + rojo + "│" + blanco + " %-19s " + rojo + "│" + RESET, p.getIdPartida(), fecha, tipo, tiempo, estado);
        }
        mensaje(rojo + "└──────────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void partidasFinalizadas(List<Partida> partidas) {
        if (partidas == null || partidas.isEmpty()) {
            sinPartidasGuardadas();
            return;
        }

        mensaje(rojo + "┌─ " + amarillo + "PARTIDAS GUARDADAS " + rojo + "──────────────────────────────────────────────────────┐" + RESET);
        mensaje(rojo + "│ " + amarillo + "ID  " + rojo + "│" + amarillo + " FECHA FIN         " + rojo + "│" + amarillo + " TIPO     " + rojo + "│" + amarillo + " RESULTADO    " + rojo + "│" + amarillo + " CAUSA DE FINALIZACIÓN " + rojo + "│");
        mensaje("├─────┼───────────────────┼──────────┼──────────────┼───────────────────────┤" + RESET);
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Partida p : partidas) {
            String fecha = (p.getFechaFin() != null) ? p.getFechaFin().format(fmt) : (p.getFechaInicio() != null) ? p.getFechaInicio().format(fmt) : "N/A";
            String tipo = (p.getTipoPartida() != null) ? p.getTipoPartida() : "LOCAL";
            String resultado = (p.getResultado() != null) ? p.getResultado() : "N/A";
            String causa = (p.getCausaFinalizacion() != null) ? p.getCausaFinalizacion() : "N/A";
            mensaje(rojo + "│" + blanco + " %-3d " + rojo + "│" + blanco + " %-17s " + rojo + "│" + blanco + " %-8s " + rojo + "│" + blanco + " %-12s " + rojo + "│" + blanco + " %-21s " + rojo + "│", p.getIdPartida(), fecha, tipo, resultado, causa);
        }
        mensaje(rojo + "└───────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    public void mostrarFinPartida(String causa) {
        String texto = String.format("PARTIDA FINALIZADA ── CAUSA: %s", causa);
        mensaje(amarillo + "┌──────────────────────────────────────────────────────────┐" + RESET);
        mensaje(amarillo + "│" + blanco + "  %-56s" + amarillo + "│" + RESET, texto);
        mensaje(amarillo + "└──────────────────────────────────────────────────────────┘" + RESET);
    }

    public void mostrarEstadoActualPartida() {
        mensaje(rojo + "┌──────────────────────────────────────────────────────────┐");
        mensaje("│" + amarillo + "  %-56s" + rojo + "│", "ESTADO ACTUAL DEL TABLERO");
        mensaje("└──────────────────────────────────────────────────────────┘" + RESET);
    }

    // Usuario
}