package core;

import java.time.format.DateTimeFormatter;
import java.util.List;

import core.piezas.*;
import modelo.*;

public class Generador {

    public Generador() {
    }

    public String fen(Tablero tablero) {
        Pieza[][] piezas = tablero.getTablero();
        //Posiciones
        StringBuilder posiciones = new StringBuilder();
        for (int fila = 0; fila < 8; fila++) {
            int espacio = 0;
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = piezas[fila][columna];
                if (pieza == null) {
                    espacio++;
                } else {
                    if (espacio > 0) {
                        posiciones.append(espacio);
                        espacio = 0;
                    }

                    String letra = "";
                    if (pieza instanceof Rey) {
                        letra = pieza.getEsBlanca() ? "K" : "k";
                    } else if (pieza instanceof Reina) {
                        letra = pieza.getEsBlanca() ? "Q" : "q";
                    } else if (pieza instanceof Torre) {
                        letra = pieza.getEsBlanca() ? "R" : "r";
                    } else if (pieza instanceof Alfil) {
                        letra = pieza.getEsBlanca() ? "B" : "b";
                    } else if (pieza instanceof Caballo) {
                        letra = pieza.getEsBlanca() ? "N" : "n";
                    } else if (pieza instanceof Peon) {
                        letra = pieza.getEsBlanca() ? "P" : "p";
                    }
                    posiciones.append(letra);
                }
            }
            if (espacio > 0) {
                posiciones.append(espacio);
            }
            if (fila < 7) {
                posiciones.append("/");
            }
        }

        //Turno
        String turno = tablero.getEsTurnoBlanco() ? "w" : "b";

        //Enroque
        String enroque = "";
        if (piezas[7][4] instanceof Rey && !piezas[7][4].getSeMovio()) {
            if (piezas[7][7] instanceof Torre && !piezas[7][7].getSeMovio()) {
                enroque += "K";
            }
            if (piezas[7][0] instanceof Torre && !piezas[7][0].getSeMovio()) {
                enroque += "Q";
            }
        }

        if (piezas[0][4] instanceof Rey && !piezas[0][4].getSeMovio()) {
            if (piezas[0][7] instanceof Torre && !piezas[0][7].getSeMovio()) { 
                enroque += "k";
            }
            if (piezas[0][0] instanceof Torre && !piezas[0][0].getSeMovio()) {
                enroque += "q";
            }
        }

        if (enroque.isEmpty()) {
            enroque = "-";
        }

        //Captura al paso
        String capturaAlPaso = "-";
        int[] ultimoMovimiento = tablero.getUltimoMovimiento();
        if (ultimoMovimiento != null) {
            capturaAlPaso = (char)(ultimoMovimiento[3] + 'a') + "" + ((8 - ultimoMovimiento[2]) + (tablero.getEsTurnoBlanco() ? 1 : -1));
        }
        return posiciones + " " + turno + " " + enroque + " " + capturaAlPaso + " " + tablero.getCincuentaMovimientos() + " " + tablero.getContadorMovimientos();
    }

    public Tablero tablero(String fen) {
        Tablero tablero = new Tablero();
        Pieza[][] piezas = new Pieza[8][8];
        String[] partes = fen.split(" ");
        //Posiciones
        String posiciones = partes[0];
        int fila = 0;
        int columna = 0;
        for (char c : posiciones.toCharArray()) {
            if (c == '/') {
                fila++;
                columna = 0;
                continue;
            }
            if (Character.isDigit(c)) {
                int espacios = Character.getNumericValue(c);
                for (int x = 0; x < espacios; x++) {
                    piezas[fila][columna] = null;
                    columna++;
                }
                continue;
            }
            boolean esBlanca = Character.isUpperCase(c);
            Pieza pieza = null;
            switch (Character.toUpperCase(c)) {
                case 'K':
                    pieza = new Rey(fila, columna, esBlanca);
                    break;
                case 'Q':
                    pieza = new Reina(fila, columna, esBlanca);
                    break;
                case 'R':
                    pieza = new Torre(fila, columna, esBlanca);
                    break;
                case 'B':
                    pieza = new Alfil(fila, columna, esBlanca);
                    break;
                case 'N':
                    pieza = new Caballo(fila, columna, esBlanca);
                    break;
                case 'P':
                    pieza = new Peon(fila, columna, esBlanca);
                    break;
            }
            piezas[fila][columna] = pieza;
            columna++;
        }

        //Turno
        boolean turno = partes[1].equals("w");
        tablero.setEsTurnoBlanco(turno);

        //Enroque
        String enroques = partes[2];
        if (piezas[7][4] instanceof Rey) {
            piezas[7][4].setSeMovio(!(enroques.contains("K") || enroques.contains("Q")));
        }
        if (piezas[7][7] instanceof Torre) {
            piezas[7][7].setSeMovio(!enroques.contains("K"));
        }
        if (piezas[7][0] instanceof Torre) {
            piezas[7][0].setSeMovio(!enroques.contains("Q"));
        }
        if (piezas[0][4] instanceof Rey) {
            piezas[0][4].setSeMovio(!(enroques.contains("k") || enroques.contains("q")));
        }
        if (piezas[0][7] instanceof Torre) {
            piezas[0][7].setSeMovio(!enroques.contains("k"));
        }
        if (piezas[0][0] instanceof Torre) {
            piezas[0][0].setSeMovio(!enroques.contains("q"));
        }
        tablero.setTablero(piezas);

        //Captura al paso
        int[] ultimoMovimiento = new int[4];
        if (partes[3].equals("-")) {
            ultimoMovimiento = null;
        } else {
            int columnaAlPaso = Character.toUpperCase(partes[3].charAt(0)) - 'A';
            int filaDestino = (8 - Character.getNumericValue(partes[3].charAt(1))) + (turno ? 1 : -1);
            int filaOrigen = turno ? filaDestino - 2 : filaDestino + 2;
            ultimoMovimiento = new int[] {filaOrigen , columnaAlPaso, filaDestino, columnaAlPaso};
        }
        tablero.setUltimoMovimiento(ultimoMovimiento);

        //50 movimientos
        tablero.setCincuentaMovimientos(Integer.parseInt(partes[4]));

        //Contador movimientos
        tablero.setContadorMovimientos(Integer.parseInt(partes[5]));

        return tablero;
    }

    public String notacionAlgebraica(List<Pieza> candidatas, Movimiento movimiento) {
        String notacionAlgebraica = "";
        String desambiguacion = "";
        String inicial = "";
        
        // 1. Mapeo de coordenadas de la matriz 
        char columnaDestino = (char) ('a' + movimiento.getDestino()[1]);
        char columnaOrigen = (char) ('a' + movimiento.getOrigen()[1]);
        int filaDestino = 8 - movimiento.getDestino()[0];

        // 2. Desambiguacion
        boolean columna = false;
        boolean fila = false;
        if (candidatas == null || candidatas.isEmpty()) {
            desambiguacion = "";
        } else {
            for (Pieza candidata : candidatas) {
                if (candidata.getColumna() == movimiento.getOrigen()[1]) {
                    columna = true;
                }
                if (candidata.getFila() == movimiento.getOrigen()[0]) {
                    fila = true;
                }
            }

            int filaOrigen = 8 - movimiento.getOrigen()[0];
            if (!columna) {
                desambiguacion = "" + columnaOrigen;
            } else if (!fila) {
                desambiguacion = "" + filaOrigen;
            } else {
                desambiguacion = "" + columnaOrigen + filaOrigen;
            }
        }

        // 3. Elegir inicial
        switch (movimiento.getPieza()) {
            case "Rey":
                inicial = "K";
                break;
            case "Reina":
                inicial = "Q";
                break;
            case "Torre":
                inicial = "R";
                break;
            case "Alfil":
                inicial = "B";
                break;
            case "Caballo":
                inicial = "N";
                break;
        }

        // 4. Evaluación del tipo de jugada
        if (movimiento.getTipoMovimiento().equals("ENROQUE_LARGO")) {
            notacionAlgebraica = "O-O-O";
        } else if (movimiento.getTipoMovimiento().equals("ENROQUE_CORTO")) {
            notacionAlgebraica = "O-O";
        } else if (movimiento.getTipoMovimiento().equals("CAPTURA_AL_PASO")) {
            notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + filaDestino;
        } else if (movimiento.getTipoMovimiento().equals("PROMOCION")) {
            if (movimiento.getPiezaCapturada() != null) {
                notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + filaDestino + "=" + movimiento.getPiezaPromocion();
            } else {
                notacionAlgebraica = "" + columnaDestino + filaDestino + "=" + movimiento.getPiezaPromocion();
            }
        } else if (movimiento.getPiezaCapturada() != null) {
            if (movimiento.getPieza().equals("Peon")) {
                notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + filaDestino;
            } else {
                notacionAlgebraica = inicial + desambiguacion + "x" + columnaDestino + filaDestino;
            }  
        } else if (movimiento.getPieza().equals("Peon")) {
            notacionAlgebraica = "" + columnaDestino + filaDestino;
        } else {
            notacionAlgebraica = inicial + desambiguacion + columnaDestino + filaDestino;
        }

        // 5. Adición de sufijo por Jaque (+) o Jaque Mate (#)
        if (movimiento.getJaqueMate()) {
            notacionAlgebraica += "#";
        } else if (movimiento.getJaque()) {
            notacionAlgebraica += "+";
        }
        return notacionAlgebraica;
    }

    public String pgn(Partida partida, List<Participacion> participaciones, List<Movimiento> movimientos) {
        StringBuilder pgn = new StringBuilder();
        // Seven Tag Roster (Obligatorios)
        pgn.append("[Event \"Ajedrez Alpha\"]\n");
        pgn.append("[Site \"Oaxaca, México\"]\n");
        pgn.append("[Date \"" + partida.getFechaInicio().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + "\"]\n");
        pgn.append("[Round \"?\"]\n");
        pgn.append("[White \"" + participaciones.get(0).getJugador().getNombreJugador() + "\"]\n");
        pgn.append("[Black \"" + participaciones.get(1).getJugador().getNombreJugador() + "\"]\n");
        pgn.append("[Result \"" + (partida.getResultado() == null ? "*" : partida.getResultado()) + "\"]\n");
        pgn.append("\n");
        // Información adicional
        pgn.append("[WhiteElo \"" + participaciones.get(0).getJugador().getElo() + "\"]\n");
        pgn.append("[BlackElo \"" + participaciones.get(1).getJugador().getElo() + "\"]\n");
        pgn.append("[WhiteTitle \"-\"]\n");
        pgn.append("[BlackTitle \"-\"]\n");
        pgn.append("[TimeControl \"" + partida.getTiempoControl() + "\"]\n");
        if (partida.getCausaFinalizacion() != null) {
            pgn.append("[Termination \"" + partida.getCausaFinalizacion() + "\"]\n");
        }
        pgn.append("[Annotator \"Ajedrez Alpha\"]\n");
        pgn.append("[ECO \"-\"]\n");
        pgn.append("[Opening \"-\"]\n");
        pgn.append("[Variation \"-\"]\n");
        pgn.append("[PlyCount \"" + movimientos.size() + "\"]\n");
        pgn.append("[SetUp \"0\"]\n");
        pgn.append("[Link \"https://github.com/DiegoStart/Ajedrez\"]\n");
        pgn.append("\n");

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getColor()) {
                pgn.append(movimiento.getNumeroMovimiento());
                pgn.append(". ");
            }          
            pgn.append(movimiento.getNotacionAlgebraica());
            pgn.append(" ");
        }
        pgn.append(partida.getResultado() == null ? "*" : partida.getResultado());
        return pgn.toString();
    }
}
