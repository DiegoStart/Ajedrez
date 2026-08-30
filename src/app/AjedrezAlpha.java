package app;

import consola.Consola;
import consola.Excepciones;
import controlador.Controlador;
import core.Generador;
import core.Tablero;
import core.Temporizador;
import modelo.*;

public class AjedrezAlpha {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        Consola consola = new Consola();

        while (true) {
            consola.mostrarMenuPrincipal();
            int opcion = Excepciones.leerNumero("Elige una opción", 0, 2);
            switch (opcion) {
                case 0:
                    consola.mensaje("Saliendo....... Gracias por jugar.");
                    return;
                case 1:
                    iniciarNuevaPartida(controlador, consola);
                    break;
                case 2:
                    cargarPartida(controlador, consola);
                    break;
            }
        }
    }

    private static void iniciarNuevaPartida(Controlador controlador, Consola consola) {
        Jugador jugadorBlanco = new Jugador();
        Jugador jugadorNegro = new Jugador();
        Partida partida = new Partida();
        boolean listo = false;
        
        while (!listo) {
            String nombreBlanco = Excepciones.leerNombre("Nombre del jugador blanco:");
            String nombreNegro = Excepciones.leerNombre("Nombre del jugador negro:");
            jugadorBlanco.setNombreJugador(nombreBlanco);
            jugadorNegro.setNombreJugador(nombreNegro);
            consola.mostrarContrincantes(jugadorBlanco, jugadorNegro);
            int confirmacion = Excepciones.leerNumero("1. Sí  2. No", 1, 2);
            
            if (confirmacion == 1) {
                controlador.registrarJugador(jugadorBlanco);
                controlador.registrarJugador(jugadorNegro);
                controlador.registrarPartida(partida);
                
                Participacion participacionBlancas = new Participacion();
                participacionBlancas.setPartida(partida);
                participacionBlancas.setJugador(jugadorBlanco);
                participacionBlancas.setColor(true);
                participacionBlancas.setTiempoRestante(600);

                Participacion participacionNegras = new Participacion();
                participacionNegras.setPartida(partida);
                participacionNegras.setJugador(jugadorNegro);
                participacionNegras.setColor(false);
                participacionNegras.setTiempoRestante(600);

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
        int idPartida = 0;
        consola.mostrarPartidasEnCurso(controlador.mostrarPartidas());

        if (idPartida == 0) {
            idPartida = Excepciones.leerNumero("Elige una partida", 0, controlador.mostrarPartidas().size());
        }

        if (idPartida == 0) {
            return;
        }
        
        Partida partida = controlador.buscarPartida(idPartida);
        Instantanea instantanea = controlador.buscarPorPartidaInstantanea(idPartida);

        if (instantanea == null) {
            consola.mensaje("La partida no tiene una instantánea guardada.");
            return;
        }
        
        Tablero tablero = generador.tablero(instantanea.getEstadoActual());
        if (tablero != null) {
            jugarPartida(controlador, consola, tablero, partida, instantanea);
        } else {
            consola.mensaje("Error a cargar tablero");
        }
    }

    private static void jugarPartida(Controlador controlador, Consola consola, Tablero tablero, Partida partida, Instantanea instantanea) {
        consola.setTablero(tablero);
        partida.setEstado("EN_CURSO");
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

        while (true) {
            consola.mostrarTablero();
            Jugador jugador = tablero.getEsTurnoBlanco() ? participacionBlancas.getJugador() : participacionNegras.getJugador();
            Jugador blanco = participacionBlancas.getJugador();
            Jugador negro = participacionNegras.getJugador();

            if (verificarFin(controlador, consola, tablero, partida, instantanea)) {
                break;
            }

            if (negras.tiempoAgotado()) {
                int eloGanador = blanco.getElo();
                int eloPerdedor = negro.getElo();
                consola.mensaje("¡Se Acabó! %s pierde por tiempo.", negro.getNombreJugador());
                consola.mensaje("Ganador: %s", blanco.getNombreJugador());
                partida.setCausaFinalizacion("TIEMPO_AGOTADO");
                partida.setResultado("1-0");
                partida.setEstado("FINALIZADA");
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
                consola.mensaje("¡Se Acabó! %s pierde por tiempo.", blanco);
                consola.mensaje("Ganador: %s", negro);
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

            String[] partes = Excepciones.leerMovimiento("Turno de " + jugador.getNombreJugador() + ", mueve (ej: E2 E4 | RENDIRSE | TABLAS | MENU):");
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
            
            boolean movimientoRealizado = mover(controlador, consola, partes, tablero, partida);
            if (!movimientoRealizado) {
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

    private static boolean verificarFin(Controlador controlador, Consola consola, Tablero tablero, Partida partida, Instantanea instantanea) {
        /*Jugador blanco = tablero.getJugadorBlanco();
        Jugador negro = tablero.getJugadorNegro();
        int eloBlanco = blanco.getElo();
            int eloNegro = negro.getElo();
        if (tablero.tablasAhogado(tablero.getEsTurnoBlanco())) {
            consola.mensaje("¡Alto! Ya no hay movimientos validos");
            consola.mensaje("¡Fin del Juego! Tablas por ahogado.");
            partida.setCausaFinalizacion("AHOGADO");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.tablasMaterialInsuficiente()) {
            consola.mensaje("¡Alto! Ya no hay piezas suficientes para continuar");
            consola.mensaje("¡Fin del Juego! Tablas por material insuficiente.");
            partida.setCausaFinalizacion("MATERIAL_INSUFICIENTE");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.tablasCincuentaMovimientos()) {
            consola.mensaje("¡Alto! Han transcurrido 50 movimientos sin capturas ni movimientos de peón.");
            consola.mensaje("¡Fin del Juego! Tablas por regla de los 50 movimientos.");
            partida.setCausaFinalizacion("50_MOVIMIENTOS");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.tablasTripleRepeticion()) {
            consola.mensaje("¡Alto! Ya no hay movimientos válidos.");
            consola.mensaje("¡Fin del Juego! Tablas por triple repetición.");
            partida.setCausaFinalizacion("TRIPLE_REPETICION");
            partida.setResultado("1/2-1/2");
            blanco.registrarTablas();
            negro.registrarTablas();
            blanco.modificarElo(eloNegro, 0.5);
            negro.modificarElo(eloBlanco, 0.5);
            actualizarInstantanea(controlador, tablero, instantanea);
            controlador.actualizarPartida(partida);
            controlador.actualizarEstadisticas(blanco);
            controlador.actualizarEstadisticas(negro);
            controlador.finalizarPartidaPartida(partida);
            return true;
        }

        if (tablero.estaEnJaque(tablero.getEsTurnoBlanco())) {
            Jugador jugador = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco() : tablero.getJugadorNegro();
            consola.mensaje("¡Cuidado %s! tu Rey está en jaque.", jugador.getNombreJugador());
            consola.mensaje(tablero.EscapeDelRey(tablero.getEsTurnoBlanco()));

            if (tablero.estaJaqueMate(tablero.getEsTurnoBlanco())) {
                Jugador ganador = tablero.getEsTurnoBlanco() ? tablero.getJugadorNegro() : tablero.getJugadorBlanco();
                String resultado = tablero.getEsTurnoBlanco() ? "0-1" : "1-0";
                int eloGanador = ganador.getElo();
                int eloPerdedor = jugador.getElo();
                consola.mensaje("¡Jaque mate! %s ha perdido.", jugador.getNombreJugador());
                consola.mensaje("Ganador: %s", ganador.getNombreJugador());

                partida.setCausaFinalizacion("JAQUE_MATE");
                partida.setResultado(resultado);
                ganador.registrarVictoria();
                jugador.registrarDerrota();
                ganador.modificarElo(eloPerdedor, 1.0);
                jugador.modificarElo(eloGanador, 0.0);

                actualizarInstantanea(controlador, tablero, instantanea);
                controlador.actualizarPartida(partida);
                controlador.actualizarEstadisticas(ganador);
                controlador.actualizarEstadisticas(jugador);
                controlador.finalizarPartidaPartida(partida);
                return true;
            }
        }*/
        return false;
    }

    private static boolean comandosDelMenu(Controlador controlador, Consola consola, Jugador jugador, Tablero tablero, Partida partida, Instantanea instantanea, Participacion participacionBlancas, Participacion participacionNegras, Temporizador blancas, Temporizador negras) {
        Jugador proponente = tablero.getEsTurnoBlanco() ? participacionBlancas.getJugador() : participacionNegras.getJugador();
        Jugador oponente = tablero.getEsTurnoBlanco() ? participacionNegras.getJugador() : participacionBlancas.getJugador();
        consola.mostrarMenuJugador();

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
                consola.mensaje("Partida %d guardada.", partida.getIdPartida());
                return true;
            case 2 :
                consola.mensaje("%s ha propuesto REINICIAR.", proponente.getNombreJugador());
                String[] respuesta = Excepciones.leerMovimiento(oponente.getNombreJugador() + " ¿Aceptas? (ACEPTO/RECHAZO)");

                if (respuesta[0].equals("ACEPTO")) {
                    tablero.iniciarPartidaEnBlanco();
                    tablero.iniciarPartida();
                    tablero.setEsTurnoBlanco(true);
                    participacionBlancas.setTiempoRestante(600);
                    participacionNegras.setTiempoRestante(600);
                    blancas.setSegundos(600);
                    negras.setSegundos(600);
                    blancas.reanudar();
                    negras.pausar();
                    partida.setEstado("EN_CURSO");
                    tablero.setUltimoMovimiento(null);
                    tablero.setContadorMovimientos(1);
                    tablero.setCincuentaMovimientos(0);
                    controlador.eliminarPorPartidaMovimiento(partida.getIdPartida());
                    actualizarInstantanea(controlador, tablero, instantanea, blancas, negras);
                    consola.mensaje("Reiniciando la partida...");
                } else {
                    consola.mensaje("%s ha rechazado REINICIAR.", oponente.getNombreJugador());
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
                consola.mensaje("¡Fin del Juego! %s se ha rendido.", perdedor.getNombreJugador());
                consola.mensaje("Ganador: %s", ganador.getNombreJugador());

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
                consola.mensaje("%s ha propuesto TABLAS.", proponente.getNombreJugador());
                String[] respuesta = Excepciones.leerMovimiento(oponente.getNombreJugador() + " ¿Aceptas? (ACEPTO/RECHAZO)");

                if (respuesta[0].equals("ACEPTO")) {
                    consola.mensaje("¡Se acabó! Tablas por acuerdo mutuo.");
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
                consola.mensaje("%s ha rechazado TABLAS.", oponente.getNombreJugador());
            return false;
        }
        return false;
    }   

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

    private static boolean mover(Controlador controlador, Consola consola, String[] partes, Tablero tablero, Partida partida) {
        try {
            int filaOrigen = 8 - Character.getNumericValue(partes[0].charAt(1));
            int colOrigen = partes[0].charAt(0) - 'A';
            int filaDestino = 8 - Character.getNumericValue(partes[1].charAt(1));
            int colDestino = partes[1].charAt(0) - 'A';
            Generador generador = new Generador();
            Movimiento movimiento = tablero.moverPieza(filaOrigen, colOrigen, filaDestino, colDestino);

            if (movimiento == null) {
                consola.mensaje("Movimiento inválido.");
                return false;
            }
            if (tablero.getHayPromocion()) {
                consola.mostrarMenuPromocion();
                int tipo = Excepciones.leerNumero("Elige una pieza:", 1, 4);
                movimiento.setPiezaPromocion(tablero.promocionPeon(tipo).getNombre());
            }
            //movimiento.setNotacionAlgebraica(generador.notacionAlgebraica(tablero, movimiento));
            movimiento.setPartida(partida);
            controlador.registrarMovimiento(movimiento);
            
            if (!movimiento.getColor()) {
                tablero.aumentarContadorMovimientos();
            }           
            controlador.actualizarPartida(partida);
            return true;
        } catch (Exception e) {
            consola.mensaje("Error al mover: " + e.getMessage());
            return false;
        }
    }
}

