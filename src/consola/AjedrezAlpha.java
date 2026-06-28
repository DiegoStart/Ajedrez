public class AjedrezAlpha {
    public static void main(String[] args) {
        AjedrezConsulta consulta = new AjedrezConsulta();
        Consola consola = new Consola();

        while (true) {
            consola.mostrarMenuPrincipal();
            int opcion = Excepciones.leerNumero("Elige una opción", 0, 3);
            switch (opcion) {
                case 0:
                    consola.mensaje("Saliendo....... Gracias por jugar.");
                    return;
                case 1:
                    iniciarNuevaPartida(consulta, consola);
                    break;
                case 2:
                    cargarPartida(consulta, consola);
                    break;
                case 3:
                    mostrarMovimientos(consulta);
                    break;
            }
        }
    }

    private static void iniciarNuevaPartida(AjedrezConsulta consulta, Consola consola) {
        String nombreBlanco = "";
        String nombreNegro = "";
        int idPartida = -1;
        boolean listo = false;
        while (!listo) {
            nombreBlanco = Excepciones.leerNombre("Nombre del jugador blanco:");
            nombreNegro = Excepciones.leerNombre("Nombre del jugador negro:");
            consola.mostrarContrincantes(nombreBlanco, nombreNegro);
            int confirmacion = Excepciones.leerNumero("1. Sí  2. No", 1, 2);
            if (confirmacion == 1) {
                int idBlanco = consulta.registrarJugador(nombreBlanco);
                int idNegro = consulta.registrarJugador(nombreNegro);
                idPartida = consulta.registrarPartida(idBlanco, idNegro);
                listo = true;
            }
        }

        Jugador blanco = new Jugador(nombreBlanco, true);
        Jugador negro = new Jugador(nombreNegro, false);
        Tablero tablero = new Tablero(blanco, negro);
        tablero.iniciarPartida();
        consulta.guardarPartida(idPartida, tablero.getRespaldo(), tablero.getEsTurnoBlanco(), tablero.getTiempoBlancas(), tablero.getTiempoNegras(), tablero.getUltimoMovimiento(), tablero.getContadorMovimientos(), tablero.getCincuentaMovimientos());
        jugarPartida(consulta, consola, tablero, idPartida);
    }

    private static void cargarPartida(AjedrezConsulta consulta, Consola consola) {
        int idPartida = 0;

        if (consulta.mostrarPartidasGuardadas()) {
            idPartida = Excepciones.leerNumero("Elige una partida", 0, 30);
        }

        if (idPartida == 0) {
            return;
        }

        Tablero tablero = consulta.cargarPartida(idPartida);
        if (tablero != null) {
            jugarPartida(consulta, consola, tablero, idPartida);
        }
    }

    private static void mostrarMovimientos(AjedrezConsulta consulta) {
        int idPartida = 0;

        if (consulta.mostrarPartidasGuardadas()) {
            idPartida = Excepciones.leerNumero("Elige una partida", 0, 30);
        }

        if (idPartida == 0) {
            return;
        }

        consulta.mostrarMovimientos(idPartida);
    }

    private static void jugarPartida(AjedrezConsulta consulta, Consola consola, Tablero tablero, int partida) {
        consola.setTablero(tablero);
        Temporizador blancas = new Temporizador(tablero.getTiempoBlancas());
        Temporizador negras = new Temporizador(tablero.getTiempoNegras());
        tablero.setRelojes(blancas, negras);
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
            String jugador = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco().getNombre() : tablero.getJugadorNegro().getNombre();
            String blanco = tablero.getJugadorBlanco().getNombre();
            String negro = tablero.getJugadorNegro().getNombre();

            if (verificarFin(consulta, consola, tablero, partida)) {
                break;
            }

            if (negras.tiempoAgotado()) {
                consola.mensaje("¡Se Acabó! %s pierde por tiempo.", negro);
                consola.mensaje("Ganador: %s", blanco);
                consulta.actualizarPartida(partida, "Finalizada", "1 - 0", "Tiempo Agotado");
                break;
            }
            if (blancas.tiempoAgotado()) {
                consola.mensaje("¡Se Acabó! %s pierde por tiempo.", blanco);
                consola.mensaje("Ganador: %s", negro);
                consulta.actualizarPartida(partida, "Finalizada", "0 - 1", "Tiempo Agotado");
                break;
            }

            String[] partes = Excepciones.leerMovimiento("Turno de " + jugador + ", mueve (ej: E2 E4 | RENDIRSE | TABLAS | MENU):");
            if (partes[0].equals("MENU")) {
                if (comandosDelMenu(consulta, consola, jugador, tablero, partida, blancas, negras)) {
                    break;
                }
                continue;
            }

            if (partes[0].equals("RENDIRSE") || partes[0].equals("TABLAS")) {
                if (comandosDelJuego(consulta, consola, partes, tablero, partida)) {
                    break;
                }
                continue;
            }
            mover(consulta, consola, partes, tablero, partida);
        }
        blancas.detener();
        negras.detener();
    }

    private static boolean verificarFin(AjedrezConsulta consulta, Consola consola, Tablero tablero, int partida) {
        Informe informe = tablero.getInforme();

        if (tablero.tablasAhogado(tablero.getEsTurnoBlanco())) {
            consola.mensaje("¡Alto! Ya no hay movimientos validos");
            consola.mensaje("¡Fin del Juego! Tablas por ahogado.");
            consulta.actualizarPartida(partida, "Finalizada", "1/2 - 1/2", "Ahogado");
            if (informe != null) {
                informe.setCausaTablas("Ahogado");
            }
            return true;
        }

        if (tablero.tablasMaterialInsuficiente()) {
            consola.mensaje("¡Alto! Ya no hay piezas suficientes para continuar");
            consola.mensaje("¡Fin del Juego! Tablas por material insuficiente.");
            consulta.actualizarPartida(partida, "Finalizada", "1/2 - 1/2", "Material Insuficiente");
            if (informe != null) {
                informe.setCausaTablas("Material Insuficiente");
            }
            return true;
        }

        if (tablero.tablasCincuentaMovimientos()) {
            consola.mensaje("¡Alto! Han transcurrido 50 movimientos sin capturas ni movimientos de peón.");
            consola.mensaje("¡Fin del Juego! Tablas por regla de los 50 movimientos.");
            consulta.actualizarPartida(partida, "Finalizada", "1/2 - 1/2", "50 Movimientos");
            if (informe != null) {
                informe.setCausaTablas("50 Movimientos");
            }
            return true;
        }

        if (tablero.tablasTripleRepeticion()) {
            consola.mensaje("¡Alto! Ya no hay movimientos válidos.");
            consola.mensaje("¡Fin del Juego! Tablas por triple repetición.");
            consulta.actualizarPartida(partida, "Finalizada", "1/2 - 1/2", "Triple Repetición");
            if (informe != null) {
                informe.setCausaTablas("Triple Repetición");
            }
            return true;
        }

        if (tablero.estaEnJaque(tablero.getEsTurnoBlanco())) {
            String jugador = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco().getNombre() : tablero.getJugadorNegro().getNombre();
            consola.mensaje("¡Cuidado %s! tu Rey está en jaque.", jugador);
            consola.mensaje(tablero.EscapeDelRey(tablero.getEsTurnoBlanco()));
            if (informe != null) {
                informe.setJaque(true);
            }

            if (tablero.estaJaqueMate(tablero.getEsTurnoBlanco())) {
                String ganador = tablero.getEsTurnoBlanco() ? tablero.getJugadorNegro().getNombre() : tablero.getJugadorBlanco().getNombre();
                String resultado = tablero.getEsTurnoBlanco() ? "0 - 1" : "1 - 0";
                consola.mensaje("¡Jaque mate! %s ha perdido.", jugador);
                consola.mensaje("Ganador: %s", ganador);
                consulta.actualizarPartida(partida, "Finalizada", resultado, "Jaque Mate");
                if (informe != null) {
                    informe.setJaqueMate(true);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean comandosDelJuego(AjedrezConsulta consulta, Consola consola, String[] partes, Tablero tablero, int partida) {
        String proponente = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco().getNombre() : tablero.getJugadorNegro().getNombre();
        String oponente = tablero.getEsTurnoBlanco() ? tablero.getJugadorNegro().getNombre() : tablero.getJugadorBlanco().getNombre();

        switch (partes[0]) {
            case "RENDIRSE":
                String perdedor = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco().getNombre() : tablero.getJugadorNegro().getNombre();
                String ganador = tablero.getEsTurnoBlanco() ? tablero.getJugadorNegro().getNombre() : tablero.getJugadorBlanco().getNombre();
                String resultado = tablero.getEsTurnoBlanco() ? "0 - 1" : "1 - 0";
                consola.mensaje("¡Fin del Juego! %s se ha rendido.", perdedor);
                consola.mensaje("Ganador: %s", ganador);
                consulta.actualizarPartida(partida, "Finalizada", resultado, "Rendicion");
                return true;

            case "TABLAS":
                consola.mensaje("%s ha propuesto TABLAS.", proponente);
                String[] respuesta = Excepciones.leerMovimiento(oponente + " ¿Aceptas? (ACEPTO/RECHAZO)");

                if (respuesta[0].equals("ACEPTO")) {
                    consola.mensaje("¡Se acabó! Tablas por acuerdo mutuo.");
                    consulta.actualizarPartida(partida, "Finalizada", "1/2 - 1/2", "Acuerdo Mutuo");
                    return true;
                }
                consola.mensaje("%s ha rechazado TABLAS.", oponente);
                return false;
        }
        return false;
    }

    private static boolean comandosDelMenu(AjedrezConsulta consulta, Consola consola, String jugador, Tablero tablero, int partida, Temporizador blancas, Temporizador negras) {
        String proponente = tablero.getEsTurnoBlanco() ? tablero.getJugadorBlanco().getNombre() : tablero.getJugadorNegro().getNombre();
        String oponente = tablero.getEsTurnoBlanco() ? tablero.getJugadorNegro().getNombre() : tablero.getJugadorBlanco().getNombre();
        consola.mostrarMenuJugador();

        int opcion = Excepciones.leerNumero("Menu de " + jugador + ", elige una opción:", 1, 3);
        switch (opcion) {
            case 1:
                tablero.setTiempoBlancas(blancas.getSegundos());
                tablero.setTiempoNegras(negras.getSegundos());
                consulta.guardarPartida(partida, tablero.getRespaldo(), tablero.getEsTurnoBlanco(), tablero.getTiempoBlancas(), tablero.getTiempoNegras(), tablero.getUltimoMovimiento(), tablero.getContadorMovimientos(), tablero.getCincuentaMovimientos());
                consola.mensaje("Partida %d guardada.", partida);
                return true;

            case 2:
                consola.mensaje("%s ha propuesto REINICIAR.", proponente);
                String[] respuesta = Excepciones.leerMovimiento(oponente + " ¿Aceptas? (ACEPTO/RECHAZO)");

                if (respuesta[0].equals("ACEPTO")) {
                    tablero.iniciarPartidaEnBlanco();
                    tablero.iniciarPartida();
                    tablero.setEsTurnoBlanco(true);
                    tablero.setTiempoBlancas(600);
                    tablero.setTiempoNegras(600);
                    tablero.setUltimoMovimiento(null);
                    tablero.setContadorMovimientos(1);
                    consola.mensaje("Reiniciando la partida...");
                    consulta.eliminarMovimientos(partida);
                    consulta.guardarPartida(partida, tablero.getRespaldo(), tablero.getEsTurnoBlanco(), tablero.getTiempoBlancas(), tablero.getTiempoNegras(), tablero.getUltimoMovimiento(), tablero.getContadorMovimientos(), tablero.getCincuentaMovimientos());
                } else {
                    consola.mensaje("%s ha rechazado REINICIAR.", oponente);
                }
                return false;

            case 3:
                return false;
        }
        return false;
    }
 
    private static void mover(AjedrezConsulta consulta, Consola consola, String[] partes, Tablero tablero, int partida) {
        try {
            int filaOrigen = 8 - Character.getNumericValue(partes[0].charAt(1));
            int colOrigen = partes[0].charAt(0) - 'A';
            int filaDestino = 8 - Character.getNumericValue(partes[1].charAt(1));
            int colDestino = partes[1].charAt(0) - 'A';

            if (!tablero.moverPieza(filaOrigen, colOrigen, filaDestino, colDestino)) {
                consola.mensaje("Movimiento inválido.");
                return;
            }
            if (tablero.getHayPromocion()) {
                consola.mostrarMenuPromocion();
                int tipo = Excepciones.leerNumero("Elige una pieza:", 1, 4);
                tablero.promocionPeon(tipo);
            }
              
            Informe informe = tablero.getInforme();
            consulta.registrarMovimiento(partida, informe);
            
            if (!informe.getEsBlanca()) {
                tablero.aumentarContadorMovimientos();
            }           
            consulta.guardarPartida(partida, tablero.getRespaldo(), tablero.getEsTurnoBlanco(), tablero.getTiempoBlancas(), tablero.getTiempoNegras(), tablero.getUltimoMovimiento(), tablero.getContadorMovimientos(), tablero.getCincuentaMovimientos());
            return;
        } catch (Exception e) {
            consola.mensaje("Clase AjedrezAlpha: Aprende a jugar.");
            return;
        }
    }
}
