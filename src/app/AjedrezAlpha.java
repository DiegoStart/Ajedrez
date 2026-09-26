package app;

import java.util.List;

import consola.*;
import controlador.Controlador;
import core.*;
import modelo.*;

public class AjedrezAlpha {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        Consola consola = new Consola();

        while (true) {
            consola.menuPrincipal();
            int opcion = Excepciones.leerNumero("Elige una opción", -1, 6);
            switch (opcion) {
                case -1:
                    eliminar(controlador, consola);
                    break;
                case 0:
                    consola.mensaje("┌─ APAGANDO CONSOLA ─────────────────────────────────────────┐");
                    consola.mensaje("│  >> Desconectando...                                       │");
                    consola.mensaje("│  >> ¡Gracias por jugar!                                    │");
                    consola.mensaje("└────────────────────────────────────────────────────────────┘");
                    return;
                case 1:
                    iniciarNuevaPartida(controlador, consola);
                    break;
                case 2:
                    cargarPartida(controlador, consola);
                    break;
                case 3:
                    historialPartida(controlador, consola);
                    break;
                case 4:
                    recrearPartida(controlador, consola);
                    break;
                case 5:
                    exportarPartida(controlador, consola);
                    break;
                case 6:
                    mostrarEstadisticas(controlador, consola);
                    break;
            }
        }
    }

    //Gestion del menu
    private static void iniciarNuevaPartida(Controlador controlador, Consola consola) {
        Jugador jugadorBlanco = new Jugador();
        Jugador jugadorNegro = new Jugador();
        Partida partida = new Partida();
        boolean listo = false;
        int tiempo = 600;
        
        while (!listo) {
            String nombreBlanco = Excepciones.leerNombre("Nombre del jugador blanco:");
            String nombreNegro = Excepciones.leerNombre("Nombre del jugador negro:");
            jugadorBlanco.setNombreJugador(nombreBlanco);
            jugadorNegro.setNombreJugador(nombreNegro);
            consola.menuContrincantes(jugadorBlanco, jugadorNegro);
            int confirmacion = Excepciones.leerNumero("1. Sí  2. No", 1, 2);
            
            if (confirmacion == 1) {
                controlador.registrarJugador(jugadorBlanco);
                controlador.registrarJugador(jugadorNegro);
                controlador.registrarPartida(partida);
                
                Participacion participacionBlancas = new Participacion();
                participacionBlancas.setPartida(partida);
                participacionBlancas.setJugador(jugadorBlanco);
                participacionBlancas.setColor(true);
                participacionBlancas.setTiempoRestante(tiempo);

                Participacion participacionNegras = new Participacion();
                participacionNegras.setPartida(partida);
                participacionNegras.setJugador(jugadorNegro);
                participacionNegras.setColor(false);
                participacionNegras.setTiempoRestante(tiempo);

                controlador.registrarParticipacion(participacionBlancas);
                controlador.registrarParticipacion(participacionNegras);
                listo = true;

                Tablero tablero = new Tablero();
                tablero.iniciarPartida();
                Generador generador = new Generador();

                Instantanea instantanea = new Instantanea();
                instantanea.setPartida(partida);
                instantanea.setEstadoActual(generador.fen(tablero));
                instantanea.setTurnoActual(tablero.getEsTurnoBlanco());
                instantanea.setUltimoMovimiento(tablero.getUltimoMovimiento());
                instantanea.setContadorMovimientos(tablero.getContadorMovimientos());
                instantanea.setCincuentaMovimientos(tablero.getCincuentaMovimientos());
                instantanea.setTiempoBlancas(participacionBlancas.getTiempoRestante());
                instantanea.setTiempoNegras(participacionNegras.getTiempoRestante());
                controlador.registrarInstantanea(instantanea);

                jugarPartida(controlador, consola, tablero, partida, instantanea);
            }   
        }
    }

    private static void cargarPartida(Controlador controlador, Consola consola) {
        Generador generador = new Generador();
        Partida partida = null;
        int idPartida = 0;
        consola.partidasPendientes(controlador.buscarPartidasEnPausa());

        if (controlador.buscarPartidasEnPausa().isEmpty()) {
            return;
        } else {
            do {
                idPartida = Excepciones.leerNumero("Elige una partida", 0, controlador.mostrarPartidas().size());
                if (idPartida == 0) {
                    return;
                } else {
                    partida = controlador.buscarPartida(idPartida);
                    if (partida.getEstado().equals("FINALIZADA")) {
                        consola.mensaje("── [ PARTIDA FINALIZADA ] ──────────────────────────────────");
                    }   
                }
            } while (partida.getEstado().equals("FINALIZADA"));
        }

        Instantanea instantanea = controlador.buscarPorPartidaInstantanea(idPartida);
        if (instantanea == null) {
            Consola.error("La partida no tiene una instantánea guardada.");
            return;
        }
        
        Tablero tablero = generador.tablero(instantanea.getEstadoActual());
        if (tablero != null) {
            for (Movimiento movimiento : controlador.buscarPorPartidaMovimiento(idPartida)) {
                tablero.actualizarRegistro(movimiento.getFen());
            }
            jugarPartida(controlador, consola, tablero, partida, instantanea);
        } else {
            Consola.error("Error a cargar tablero");
        }
    }

    public static void historialPartida(Controlador controlador, Consola consola) {
        int idPartida = 0;
        consola.partidasFinalizadas(controlador.buscarPartidasFinalizada());

        if (controlador.buscarPartidasFinalizada().isEmpty()) {
            return;
        } else {  
            do {
                idPartida = Excepciones.leerNumero("Elige una partida", 0, controlador.mostrarPartidas().size());
                if (idPartida == 0) {
                    return;
                } else {
                    if (!controlador.buscarPartida(idPartida).getEstado().equals("FINALIZADA")) {
                        consola.mensaje("── [ PARTIDA PENDIENTE ] ──────────────────────────────────");
                    }   
                }
            } while (!controlador.buscarPartida(idPartida).getEstado().equals("FINALIZADA"));
        }
        consola.movimientosRealizados(controlador.buscarPorPartidaMovimiento(idPartida));
    }

    public static void recrearPartida(Controlador controlador, Consola consola) {
        Generador generador = new Generador();
        Partida partida = null;
        int idPartida = 0;
        int posicion = 0;
        consola.partidasFinalizadas(controlador.mostrarPartidas());
        if (controlador.mostrarPartidas().isEmpty()) {
            return;
        } else {   
            idPartida = Excepciones.leerNumero("Elige una partida", 0, controlador.mostrarPartidas().size());
            if (idPartida == 0) {
                return;
            } else {
                partida = controlador.buscarPartida(idPartida);    
            }
        }

        Jugador blanco = controlador.buscarPorPartidaYColorParticipacion(idPartida, true).getJugador();
        Jugador negro = controlador.buscarPorPartidaYColorParticipacion(idPartida, false).getJugador();   
        List<Movimiento> movimientos = controlador.buscarPorPartidaMovimiento(idPartida);
        if (movimientos.isEmpty()) {
            consola.sinMovimientosGuardados();
            return;
        }

        while (posicion < movimientos.size()) {
            consola.setTablero(generador.tablero(movimientos.get(posicion).getFen()));
            consola.tablero(blanco.getNombreJugador(), negro.getNombreJugador(), movimientos.get(posicion).getColor(), partida.getEstado(), 0);
            if (posicion == movimientos.size() - 1 && partida.getEstado().equals("FINALIZADA")) {
                consola.mostrarFinPartida(partida.getCausaFinalizacion());
            } else if (posicion == movimientos.size() - 1) {
                consola.mostrarEstadoActualPartida();
            }
            int opcion = Excepciones.leerNumero("[1] Siguiente [2] Anterior [3] Ir al inicio [4] Ir al final [0] Salir", 0, 4);
        
            switch (opcion) {
                case 0:
                    return ;
                case 1:
                    if (posicion < movimientos.size() - 1) {
                        posicion++;
                    }
                    break;
                case 2:
                    if (posicion > 0) {
                        posicion--;
                    }
                    break;
                case 3:
                    posicion = 0;
                    break;
                case 4:
                    posicion = movimientos.size() - 1;
                    break;
            }
        }
    }
    
    public static void exportarPartida(Controlador controlador, Consola consola) {
        Generador generador = new Generador();
        Partida partida = null;
        int idPartida = 0;
        consola.partidasFinalizadas(controlador.mostrarPartidas());

        if (controlador.mostrarPartidas().isEmpty()) {
            return;
        } else {   
            idPartida = Excepciones.leerNumero("Elige una partida", 0, controlador.mostrarPartidas().size());
            if (idPartida == 0) {
                return;
            } else {
                partida = controlador.buscarPartida(idPartida);     
            }
        }

        List<Participacion> participaciones = controlador.buscarPorPartidaParticipacion(idPartida);
        List<Movimiento> movimientos = controlador.buscarPorPartidaMovimiento(idPartida);
        consola.mensaje(generador.pgn(partida, participaciones, movimientos));
    }

    public static void mostrarEstadisticas(Controlador controlador, Consola consola) {
        consola.mostrarJugadoresUsuario(controlador.mostrarJugadores());
    }

    private static void eliminar(Controlador controlador, Consola consola) {
        controlador.eliminarTodo();
        consola.mensaje("Se elimino toda informacion guardada.");
    }

    //Flujo y jugabilidad
    private static void jugarPartida(Controlador controlador, Consola consola, Tablero tablero, Partida partida, Instantanea instantanea) {
        consola.setTablero(tablero);
        Participacion participacionBlancas = controlador.buscarPorPartidaYColorParticipacion(partida.getIdPartida(), true);
        Participacion participacionNegras = controlador.buscarPorPartidaYColorParticipacion(partida.getIdPartida(), false);
        Temporizador blancas = new Temporizador(instantanea.getTiempoBlancas());
        Temporizador negras = new Temporizador(instantanea.getTiempoNegras());
        blancas.start();
        negras.start();

        if (tablero.getEsTurnoBlanco()) {
            blancas.reanudar();
            negras.pausar();
        } else {
            negras.reanudar();
            blancas.pausar();
        }
        
        Jugador blanco = participacionBlancas.getJugador();
        Jugador negro = participacionNegras.getJugador();
        while (true) {
            Jugador jugador = tablero.getEsTurnoBlanco() ? participacionBlancas.getJugador() : participacionNegras.getJugador();
            controlador.actualizarEstadoPartida(partida.getIdPartida(), "EN_CURSO");
            partida.setEstado("EN_CURSO");
            Temporizador tiempo = tablero.getEsTurnoBlanco() ? blancas : negras;
            consola.tablero(blanco.getNombreJugador(), negro.getNombreJugador(), tablero.getEsTurnoBlanco(), partida.getEstado(), tiempo.getSegundos());

            if (verificarFin(controlador, consola, tablero, partida, participacionBlancas, participacionNegras,instantanea, blancas, negras)) {
                break;
            }

            if (negras.tiempoAgotado()) {
                int eloGanador = blanco.getElo();
                int eloPerdedor = negro.getElo();
                consola.mensaje("── [ TIEMPO AGOTADO ] ──────────────────────────────────────");
                consola.mensaje(">> %s pierde por tiempo | GANADOR: %s", negro.getNombreJugador(), blanco.getNombreJugador());
                consola.mensaje("────────────────────────────────────────────────────────────");
                partida.setCausaFinalizacion("TIEMPO_AGOTADO");
                partida.setResultado("1-0");
                blanco.registrarVictoria();
                negro.registrarDerrota();
                blanco.modificarElo(eloPerdedor, 1.0);
                negro.modificarElo(eloGanador, 0.0);
                controlador.actualizarEstadisticas(blanco);
                controlador.actualizarEstadisticas(negro);
                controlador.finalizarPartidaPartida(partida);
                break;
            }

            if (blancas.tiempoAgotado()) {
                int eloGanador = negro.getElo();
                int eloPerdedor = blanco.getElo();
                consola.mensaje("── [ TIEMPO AGOTADO ] ──────────────────────────────────────");
                consola.mensaje(">> %s pierde por tiempo | GANADOR: %s", blanco.getNombreJugador(), negro.getNombreJugador());
                consola.mensaje("────────────────────────────────────────────────────────────");
                partida.setCausaFinalizacion("TIEMPO_AGOTADO");
                partida.setResultado("0-1");
                partida.setEstado("FINALIZADA");
                blanco.registrarDerrota();
                negro.registrarVictoria();
                blanco.modificarElo(eloPerdedor, 0.0);
                negro.modificarElo(eloGanador, 1.0);
                controlador.actualizarEstadisticas(blanco);
                controlador.actualizarEstadisticas(negro);
                controlador.finalizarPartidaPartida(partida);
                break;
            }

            String[] partes = Excepciones.leerMovimiento("[" + jugador.getNombreJugador() + "] > mueve (ej: E2 E4 | RENDIRSE | TABLAS | MENU):");
            if (partes[0].equals("MENU")) {
                if (comandosDelMenu(controlador, consola, jugador, tablero, partida, instantanea, participacionBlancas, participacionNegras, blancas, negras)) {
                    break;
                }
                continue;
            }

            if (partes[0].equals("RENDIRSE") || partes[0].equals("TABLAS")) {
                if (comandosDelJuego(controlador, consola, partes, tablero, partida, participacionBlancas, participacionNegras)) {
                    break;
                }
                continue;
            }
            
            if (!mover(controlador, consola, partes, tablero, partida)) {
                continue;
            }

            if (tablero.getEsTurnoBlanco()) {
                blancas.reanudar();
                negras.pausar();
            } else {
                negras.reanudar();
                blancas.pausar();
            }
            actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
        }
        blancas.detener();
        negras.detener();
    }

    private static boolean verificarFin(Controlador controlador, Consola consola, Tablero tablero, Partida partida, Participacion participacionBlanca, Participacion participacionNegra, Instantanea instantanea, Temporizador blancas, Temporizador negras) {
        Jugador blanco = controlador.buscarJugador(participacionBlanca.getJugador().getIdJugador());
        Jugador negro = controlador.buscarJugador(participacionNegra.getJugador().getIdJugador());
        int eloBlanco = blanco.getElo();
        int eloNegro = negro.getElo();
        if (tablero.tablasAhogado(tablero.getEsTurnoBlanco())) {
            consola.mensaje("── [ AHOGADO: TABLAS ] ─────────────────────────────────────");
            consola.mensaje(">> ¡Alto! Sin movimientos válidos disponibles. Fin del juego.");
            consola.mensaje("────────────────────────────────────────────────────────────");
            partida.setCausaFinalizacion("AHOGADO");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.tablasMaterialInsuficiente()) {
            consola.mensaje("── [ MATERIAL INSUFICIENTE: TABLAS ] ───────────────────────");
            consola.mensaje(">> ¡Alto! No hay piezas suficientes. Fin del juego.");
            consola.mensaje("────────────────────────────────────────────────────────────");
            partida.setCausaFinalizacion("MATERIAL_INSUFICIENTE");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.tablasCincuentaMovimientos()) {
            consola.mensaje("── [ REGLA DE LOS 50 MOVIMIENTOS: TABLAS ] ─────────────────");
            consola.mensaje(">> ¡Alto! 50 jugadas sin capturas ni avance de peón. Fin.");
            consola.mensaje("────────────────────────────────────────────────────────────");
            partida.setCausaFinalizacion("50_MOVIMIENTOS");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }
        
        if (tablero.tablasTripleRepeticion(instantanea.getEstadoActual())) {
            consola.mensaje("── [ TRIPLE REPETICIÓN: TABLAS ] ──────────────────────────");
            consola.mensaje(">> ¡Alto! Posición repetida 3 veces. Fin del juego.");
            consola.mensaje("────────────────────────────────────────────────────────────");
            partida.setCausaFinalizacion("TRIPLE_REPETICION");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.estaEnJaque(tablero.getEsTurnoBlanco())) {
            Jugador jugador = tablero.getEsTurnoBlanco() ? blanco : negro;
            consola.alertaJaque(jugador.getNombreJugador(), tablero.EscapeDelRey(tablero.getEsTurnoBlanco()));
            if (tablero.estaJaqueMate(tablero.getEsTurnoBlanco())) {
                Jugador ganador = tablero.getEsTurnoBlanco() ? negro : blanco;
                String resultado = tablero.getEsTurnoBlanco() ? "0-1" : "1-0";
                int eloGanador = ganador.getElo();
                int eloPerdedor = jugador.getElo();
                consola.mensaje("── [ ¡JAQUE MATE! ] ────────────────────────────────────────");
                consola.mensaje(">> %s ha perdido | GANADOR: %s", jugador.getNombreJugador(), ganador.getNombreJugador());
                consola.mensaje("────────────────────────────────────────────────────────────");
                partida.setCausaFinalizacion("JAQUE_MATE");
                partida.setResultado(resultado);
                ganador.registrarVictoria();
                jugador.registrarDerrota();
                ganador.modificarElo(eloPerdedor, 1.0);
                jugador.modificarElo(eloGanador, 0.0);

                actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
                controlador.actualizarPartida(partida);
                controlador.actualizarEstadisticas(ganador);
                controlador.actualizarEstadisticas(jugador);
                controlador.finalizarPartidaPartida(partida);
                return true;
            }
        }
        return false;
    }

    private static boolean mover(Controlador controlador, Consola consola, String[] partes, Tablero tablero, Partida partida) {
        try {
            int filaOrigen = 8 - Character.getNumericValue(partes[0].charAt(1));
            int colOrigen = partes[0].charAt(0) - 'A';
            int filaDestino = 8 - Character.getNumericValue(partes[1].charAt(1));
            int colDestino = partes[1].charAt(0) - 'A';
            Generador generador = new Generador();
            Movimiento movimiento = tablero.moverPieza(filaOrigen, colOrigen, filaDestino, colDestino);

            if (movimiento == null) {
                consola.mensaje("── [ ERROR: MOVIMIENTO INVÁLIDO ] ─────────────────────────");
                return false;
            }
            consola.alertaMovimiento(movimiento);
            if (tablero.getHayPromocion()) {
                consola.menuPromocion();
                int tipo = Excepciones.leerNumero("Elige una pieza:", 1, 4);
                movimiento.setPiezaPromocion(tablero.promocionPeon(tipo).getNombre());
            }
            if (!movimiento.getColor()) {
                tablero.aumentarContadorMovimientos();
            } 

            movimiento.setJaque(tablero.estaEnJaque(!movimiento.getColor()));
            movimiento.setJaqueMate(tablero.estaJaqueMate(!movimiento.getColor()));
            movimiento.setNumeroMovimiento(tablero.getContadorMovimientos());    
            movimiento.setNotacionAlgebraica(generador.notacionAlgebraica(tablero.getCandidatas(), movimiento));
            movimiento.setPartida(partida);
            movimiento.setFen(generador.fen(tablero));
            tablero.actualizarRegistro(generador.fen(tablero));
            controlador.registrarMovimiento(movimiento);  
            controlador.actualizarPartida(partida);
            return true;
        } catch (Exception e) {
            Consola.error("Error al mover: " + e.getMessage());
            return false;
        }
    }

    //Comandos
    private static boolean comandosDelMenu(Controlador controlador, Consola consola, Jugador jugador, Tablero tablero, Partida partida, Instantanea instantanea, Participacion participacionBlancas, Participacion participacionNegras, Temporizador blancas, Temporizador negras) {
        Jugador proponente = tablero.getEsTurnoBlanco() ? participacionBlancas.getJugador() : participacionNegras.getJugador();
        Jugador oponente = tablero.getEsTurnoBlanco() ? participacionNegras.getJugador() : participacionBlancas.getJugador();
        consola.menuJugador();

        int opcion = Excepciones.leerNumero("Menu de " + jugador.getNombreJugador() + ", elige una opción:", 1, 3);
        switch (opcion) {
            case 1:
                blancas.pausar();
                negras.pausar();
                participacionBlancas.setTiempoRestante(blancas.getSegundos());
                participacionNegras.setTiempoRestante(negras.getSegundos());

                partida.setEstado("EN_PAUSA");
                actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
                controlador.actualizarPartida(partida);
                consola.mensaje("┌─ GUARDADO EXITOSO ───────────────────────────────────────┐");
                consola.mensaje("│  >> Partida #%d guardada correctamente.                   │", partida.getIdPartida());
                consola.mensaje("└──────────────────────────────────────────────────────────┘");
                return true;
            case 2 :
                consola.mensaje("── [ PROPUESTA DE REINICIO: %s ] ─────────────────", proponente.getNombreJugador());
                String[] respuesta = Excepciones.leerMovimiento(oponente.getNombreJugador() + " ¿Aceptas? (ACEPTO/RECHAZO)");
                if (respuesta[0].equals("ACEPTO")) {
                    int tiempo = 600;
                    tablero.iniciarPartidaEnBlanco();
                    tablero.iniciarPartida();
                    tablero.setEsTurnoBlanco(true);
                    participacionBlancas.setTiempoRestante(tiempo);
                    participacionNegras.setTiempoRestante(tiempo);
                    blancas.setSegundos(tiempo);
                    negras.setSegundos(tiempo);
                    blancas.reanudar();
                    negras.pausar();
                    partida.setEstado("EN_CURSO");
                    tablero.setUltimoMovimiento(null);
                    tablero.setContadorMovimientos(1);
                    tablero.setCincuentaMovimientos(0);
                    controlador.eliminarPorPartidaMovimiento(partida.getIdPartida());
                    actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
                    consola.mensaje("── [ REINICIANDO PARTIDA... ] ──────────────────────────────");
                } else {
                    consola.mensaje("── [ REINICIO RECHAZADO: %s ] ─────────────────", oponente.getNombreJugador());
                }
                return false;
            case 3:
                return false;
        }
        return false;
    }

    private static boolean comandosDelJuego(Controlador controlador, Consola consola, String[] partes, Tablero tablero, Partida partida, Participacion participacionBlancas, Participacion participacionNegras) {
        Jugador proponente = tablero.getEsTurnoBlanco() ? participacionBlancas.getJugador() : participacionNegras.getJugador();
        Jugador oponente = tablero.getEsTurnoBlanco() ? participacionNegras.getJugador() : participacionBlancas.getJugador();
        
        switch (partes[0]) {
            case "RENDIRSE":
                Jugador perdedor = proponente;
                Jugador ganador = oponente;

                String resultado = tablero.getEsTurnoBlanco() ? "0-1" : "1-0";
                consola.mensaje("── [ RENDICIÓN: FIN DEL JUEGO ] ────────────────────────────");
                consola.mensaje(">> %s se ha rendido | GANADOR: %s", perdedor.getNombreJugador(), ganador.getNombreJugador());
                consola.mensaje("────────────────────────────────────────────────────────────");
                partida.setCausaFinalizacion("RENDICION");
                partida.setResultado(resultado);
                partida.setEstado("FINALIZADA");
                ganador.registrarVictoria();
                perdedor.registrarRendicion();
                int eloGanador = ganador.getElo();
                int eloPerdedor = perdedor.getElo();
                ganador.modificarElo(eloPerdedor, 1.0);
                perdedor.modificarElo(eloGanador, 0.0);

                controlador.actualizarPartida(partida);
                controlador.actualizarEstadisticas(ganador);
                controlador.actualizarEstadisticas(perdedor);
                controlador.finalizarPartidaPartida(partida);
                return true;
            case "TABLAS":
                consola.mensaje("── [ PROPUESTA DE TABLAS: %s ] ──────────────────", proponente.getNombreJugador());
                String[] respuesta = Excepciones.leerMovimiento(oponente.getNombreJugador() + " ¿Aceptas? (ACEPTO/RECHAZO)");

                if (respuesta[0].equals("ACEPTO")) {
                    consola.mensaje("── [ TABLAS POR ACUERDO MUTUO ] ────────────────────────────");
                    proponente.registrarTablas();
                    oponente.registrarTablas();
                    int eloProponente = proponente.getElo();
                    int eloOponente = oponente.getElo();
                    proponente.modificarElo(eloOponente, 0.5);
                    oponente.modificarElo(eloProponente, 0.5);

                    partida.setCausaFinalizacion("ACUERDO_MUTUO");
                    partida.setResultado("1/2-1/2");
                    partida.setEstado("FINALIZADA");

                    controlador.actualizarPartida(partida);
                    controlador.actualizarEstadisticas(proponente);
                    controlador.actualizarEstadisticas(oponente);
                    controlador.finalizarPartidaPartida(partida);
                    return true;
                }
                consola.mensaje("── [ TABLAS RECHAZADAS: %s ] ─────────────────", oponente.getNombreJugador());
            return false;
        }
        return false;
    }   

    //Persistencia
    private static void actualizarInstantanea(Controlador controlador, Tablero tablero, Instantanea instantanea, Temporizador blancas, Temporizador negras) {
        Generador generador = new Generador();

        instantanea.setEstadoActual(generador.fen(tablero));
        instantanea.setTurnoActual(tablero.getEsTurnoBlanco());
        instantanea.setUltimoMovimiento(tablero.getUltimoMovimiento());
        instantanea.setContadorMovimientos(tablero.getContadorMovimientos());
        instantanea.setCincuentaMovimientos(tablero.getCincuentaMovimientos());
        instantanea.setTiempoBlancas(blancas.getSegundos());
        instantanea.setTiempoNegras(negras.getSegundos());
        controlador.actualizarInstantanea(instantanea);
    }
}