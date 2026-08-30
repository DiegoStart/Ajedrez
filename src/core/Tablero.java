package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.piezas.Alfil;
import core.piezas.Caballo;
import core.piezas.Peon;
import core.piezas.Pieza;
import core.piezas.Reina;
import core.piezas.Rey;
import core.piezas.Torre;
import modelo.Movimiento;

public class Tablero {
    // TABLERO
    private Pieza[][] tablero;
    // Estado del juego de ajedrez
    private boolean esTurnoBlanco;
    private int contadorMovimientos;
    private int cincuentaMovimientos;
    private int[] ultimoMovimiento;
    // Promoción
    private boolean hayPromocion;
    private int filaPromocion;
    private int colPromocion;
    // Repetición
    private HashMap<String, Integer> registro;

    // CONSTRUCTOR
    public Tablero() {
        this.tablero = new Pieza[8][8];
        this.esTurnoBlanco = true; // blanco comienza
        this.ultimoMovimiento = null;
        this.contadorMovimientos = 1;
        this.registro = new HashMap<>();
    }

    // GETTERS Y SETTERS
    public void setTablero(Pieza[][] tablero) {
        this.tablero = tablero;
    }
    
    public Pieza[][] getTablero() {
        return tablero;
    }

    public Pieza getPieza(int fila, int cola) {
        return tablero[fila][cola];
    }

    public void setEsTurnoBlanco(boolean esTurnoBlanco) {
        this.esTurnoBlanco = esTurnoBlanco;
    }

    public boolean getEsTurnoBlanco() {
        return esTurnoBlanco;
    }

    public void setContadorMovimientos(int contadorMovimientos) {
        this.contadorMovimientos = contadorMovimientos;
    }

    public int getContadorMovimientos() {
        return contadorMovimientos;
    }

    public void setCincuentaMovimientos(int cincuentaMovimientos) {
        this.cincuentaMovimientos = cincuentaMovimientos;
    }

    public int getCincuentaMovimientos() {
        return cincuentaMovimientos;
    }

    public void setUltimoMovimiento(int[] ultimoMovimiento) {
        this.ultimoMovimiento = ultimoMovimiento;
    }

    public int[] getUltimoMovimiento() {
        return ultimoMovimiento;
    }

    public void setHayPromocion(boolean hayPromocion) {
        this.hayPromocion = hayPromocion;
    }

    public boolean getHayPromocion() {
        return hayPromocion;
    }

    public void setFilaPromocion(int filaPromocion) {
        this.filaPromocion = filaPromocion;
    }

    public int getFilaPromocion() {
        return filaPromocion;
    }

    public void setColPromocion(int colPromocion) {
        this.colPromocion = colPromocion;
    }

    public int getColPromocion() {
        return colPromocion;
    }

    public void setRegistro(HashMap<String, Integer> registro) {
        this.registro = registro;
    }

    public HashMap<String, Integer> getRegistro() {
        return registro;
    }

    public void actualizarRegistro(String fen) {
        registro.put(fen, registro.getOrDefault(fen, 0) + 1);
    }

    // INICIALIZACIÓN 
    public void iniciarPartida() {
        tablero[7][0] = new Torre(7, 0, true);
        tablero[7][1] = new Caballo(7, 1, true);
        tablero[7][2] = new Alfil(7, 2, true);
        tablero[7][3] = new Reina(7, 3, true);
        tablero[7][4] = new Rey(7, 4, true);
        tablero[7][5] = new Alfil(7, 5, true);
        tablero[7][6] = new Caballo(7, 6, true);
        tablero[7][7] = new Torre(7, 7, true);
        for (int x = 0; x < 8; x++) {
            tablero[6][x] = new Peon(6, x, true);
        }

        tablero[0][0] = new Torre(0, 0, false);
        tablero[0][1] = new Caballo(0, 1, false);
        tablero[0][2] = new Alfil(0, 2, false);
        tablero[0][3] = new Reina(0, 3, false);
        tablero[0][4] = new Rey(0, 4, false);;
        tablero[0][5] = new Alfil(0, 5, false);
        tablero[0][6] = new Caballo(0, 6, false);
        tablero[0][7] = new Torre(0, 7, false);
        for (int x = 0; x < 8; x++) {
            tablero[1][x] = new Peon(1, x, false);
        }
    }

    public void iniciarPartidaEnBlanco() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                tablero[x][y] = null;
            }
        }
    }
    
    // MOVIMIENTOS Y JUGADAS
    public Movimiento moverPieza(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
    // 1. Validar coordenadas
        if (!casillaValida(filaOrigen, colOrigen) || !casillaValida(filaDestino, colDestino)) {
            return null;
        }
        Pieza origen = tablero[filaOrigen][colOrigen];
        Pieza destino = tablero[filaDestino][colDestino];
    // 2. Verificar que exista una pieza
        if (origen == null) {
            return null;
        }
    // 3. Verificar turno
        if (origen.getEsBlanca() != esTurnoBlanco) {
            return null;
        }
    // 4. Crear movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setNumeroMovimiento(contadorMovimientos);
        movimiento.setColor(origen.getEsBlanca());
        movimiento.setPieza(origen.getNombre());
        movimiento.setOrigen(new int[]{filaOrigen, colOrigen});
        movimiento.setDestino(new int[]{filaDestino, colDestino});
    // 5. Enroque
        if (origen instanceof Rey && colOrigen == 4 && (colDestino == 2 || colDestino == 6)) {
            if (!enroqueLargoCorto(origen.getEsBlanca(), filaDestino, colDestino)) {
                return null;
            }
            Pieza torre;
            tablero[filaOrigen][colOrigen] = null;
            tablero[filaDestino][colDestino] = origen;
            origen.setFila(filaDestino);
            origen.setColumna(colDestino);

            if (colDestino == 2) {
                torre = tablero[filaOrigen][0];
                tablero[filaOrigen][0] = null;
                tablero[filaDestino][3] = torre;
                torre.setFila(filaDestino);
                torre.setColumna(3);
            } else {
                torre = tablero[filaOrigen][7];
                tablero[filaOrigen][7] = null;
                tablero[filaDestino][5] = torre;
                torre.setFila(filaDestino);
                torre.setColumna(5);
            }
            origen.setSeMovio(true);
            torre.setSeMovio(true);
            ultimoMovimiento = null;
            esTurnoBlanco = !esTurnoBlanco;
            movimiento.setTipoMovimiento(colDestino == 2 ? "ENROQUE_LARGO" : "ENROQUE_CORTO");
            cincuentaMovimientos++;
            return movimiento;
        }
    // 6. Captura al paso
        if (origen instanceof Peon && capturaAlPaso(filaOrigen, colOrigen, filaDestino, colDestino)) {
            int filaCapturada = origen.getEsBlanca() ? filaDestino + 1 : filaDestino - 1;
            tablero[filaOrigen][colOrigen] = null;
            tablero[filaDestino][colDestino] = origen;
            origen.setFila(filaDestino);
            origen.setColumna(colDestino);
            tablero[filaCapturada][colDestino] = null;
            origen.setSeMovio(true);
            ultimoMovimiento = null;
            esTurnoBlanco = !esTurnoBlanco;
            movimiento.setTipoMovimiento("CAPTURA_AL_PASO");
            movimiento.setPiezaCapturada("Peon");
            cincuentaMovimientos = 0;
            return movimiento;
        }
    // 7. Validar movimiento normal o captura
        if (destino == null) {
            if (!origen.movimiento(filaDestino, colDestino)) {
                return null;
            }
        } else {
            if (destino.getEsBlanca() == origen.getEsBlanca()) {
                return null;
            }
            if (!origen.ataca(filaDestino, colDestino)) {
                return null;
            }
        }
    // 8. Verificar trayectoria
        if (origen instanceof Torre || origen instanceof Alfil || origen instanceof Reina) {
            if (!caminoLibre(origen, filaDestino, colDestino)) {
                return null;
            }
        }
    // 9. Verificar que no deje al rey en jaque
        if (!Simulacion(filaOrigen, colOrigen, filaDestino, colDestino)) {
            return null;
        }
    // 10. Guardar información de captura
        if (destino != null) {
            movimiento.setPiezaCapturada(destino.getNombre());
            movimiento.setTipoMovimiento("CAPTURA");
            cincuentaMovimientos = 0;
        } else {
            movimiento.setTipoMovimiento("NORMAL");
            if (origen instanceof Peon) {
                cincuentaMovimientos = 0;
            } else {
                cincuentaMovimientos++;
            }
        }
    // 11. Registrar movimiento de peón de dos casillas
        if (origen instanceof Peon && Math.abs(filaDestino - filaOrigen) == 2) {
            ultimoMovimiento = new int[]{filaOrigen, colOrigen, filaDestino, colDestino};
        } else {
            ultimoMovimiento = null;
        }
    // 12. Ejecutar movimiento
        tablero[filaOrigen][colOrigen] = null;
        tablero[filaDestino][colDestino] = origen;
        origen.setFila(filaDestino);
        origen.setColumna(colDestino);
        origen.setSeMovio(true);
    // 13. Comprobar promoción
        if (origen instanceof Peon && (filaDestino == 0 || filaDestino == 7)) {
            hayPromocion = true;
            filaPromocion = filaDestino;
            colPromocion = colDestino;
            movimiento.setTipoMovimiento("PROMOCION");
        } else {
            hayPromocion = false;
        }
    // 14. Cambiar turno
        esTurnoBlanco = !esTurnoBlanco;
    // 15. Registrar posición
        if (!hayPromocion) {
        }
        return movimiento;
    }

    public Pieza promocionPeon(int tipo) {
        Pieza origen = tablero[filaPromocion][colPromocion];
        Pieza nueva;
        switch (tipo) {
            case 1:
                nueva = new Torre(filaPromocion, colPromocion, origen.getEsBlanca());
                break;
            case 2:
                nueva = new Alfil(filaPromocion, colPromocion, origen.getEsBlanca());
                break;
            case 3:
                nueva = new Caballo(filaPromocion, colPromocion, origen.getEsBlanca());
                break;
            default:
                nueva = new Reina(filaPromocion, colPromocion, origen.getEsBlanca());
                break;
        }
        nueva.setSeMovio(true);
        tablero[filaPromocion][colPromocion] = nueva;
        hayPromocion = false;
        return nueva;
    }

    public void aumentarContadorMovimientos() {
        this.contadorMovimientos++;
    }

    // REGLAS ESPECIALES
    private boolean enroqueLargoCorto(boolean esBlanca, int filaDestino, int colDestino) {
        if (estaEnJaque(esBlanca)) {
            return false; // No se puede enrocar estando en jaque
        }
        int filaRey = esBlanca ? 7 : 0; 
        Pieza rey = tablero[filaRey][4]; 
        Pieza torre = null;
        if (!(rey instanceof Rey) || rey.getSeMovio()) {
            return false;
        }
        if (filaDestino != filaRey) {
            return false; 
        }   
        if (colDestino == 6) {
            torre = tablero[filaRey][7]; 
            if (!(torre instanceof Torre) || torre.getSeMovio()) {
                return false; // La torre ya se ha movido o no es una torre
            }
            if (tablero[filaRey][5] != null || tablero[filaRey][6] != null) {
                return false; // Hay piezas entre el rey y la torre
            }
            for (int columna = 5; columna <= 6; columna++) {
                if (!Simulacion(filaRey, 4, filaRey, columna)) {
                    return false; // El rey pasaría por una casilla atacada
                }
            }
            return true; // Enroque corto válido
        } 
        if (colDestino == 2) {
            torre = tablero[filaRey][0]; 
            if (!(torre instanceof Torre) || torre.getSeMovio()) {
                return false;
            }
            if (tablero[filaRey][1] != null || tablero[filaRey][2] != null || tablero[filaRey][3] != null) {
                return false;
            }
            for (int columna = 2; columna <= 3; columna++) {
                if (!Simulacion(filaRey, 4, filaRey, columna)) {
                    return false; // El rey pasaría por una casilla atacada
                }
            }
            return true; // Enroque largo válido
        }
        return false; // No es un movimiento de enroque válido
    }

    private boolean capturaAlPaso(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        if (ultimoMovimiento == null) {
            return false; // No hay movimiento previo
        }
        Pieza origen = tablero[filaOrigen][colOrigen];
        if (!(origen instanceof Peon)) {
            return false; // Solo los peones pueden capturar al paso
        }
        if (tablero[filaDestino][colDestino] != null) {
            return false; // La casilla debe estar vacía
        }
        if (Math.abs(colOrigen - colDestino) != 1) {
            return false; // El peón debe moverse diagonalmente una columna
        }
        if ((origen.getEsBlanca() && filaDestino != filaOrigen - 1) || (!origen.getEsBlanca() && filaDestino != filaOrigen + 1)) {
            return false; // El peón debe moverse una fila hacia adelante
        }
        int fila = origen.getEsBlanca() ? filaDestino + 1 : filaDestino - 1;
        Pieza destino = tablero[fila][colDestino];
        boolean legal = true;
        if (destino instanceof Peon && destino.getEsBlanca() != origen.getEsBlanca() && Math.abs(ultimoMovimiento[0] - ultimoMovimiento[2]) == 2) {
            if (ultimoMovimiento[2] == fila && ultimoMovimiento[3] == colDestino) { // Valida que el peon rival este al lado
                tablero[filaOrigen][colOrigen] = null;
                tablero[filaDestino][colDestino] = origen; // Muevo el peon del origen a la casilla destino
                origen.setFila(filaDestino);
                origen.setColumna(colDestino);
                tablero[fila][colDestino] = null;  // Elimino el peon capturado al paso
                if (estaEnJaque(origen.getEsBlanca())) {
                    legal = false; 
                }
                tablero[filaOrigen][colOrigen] = origen;
                tablero[filaDestino][colDestino] = null; // Revierto el movimiento
                destino.setFila(fila);
                destino.setColumna(colDestino);
                tablero[fila][colDestino] = destino; // Revierto la captura
            }
        }
        return legal; // Retorno si el movimiento es legal o no
    }

    // VALIDACIONES BÁSICAS
    private boolean casillaValida(int filaOrigen, int colOrigen) {
        return filaOrigen >= 0 && filaOrigen < 8 && colOrigen >= 0 && colOrigen < 8;
    }

    private boolean caminoLibre(Pieza pieza, int filaDestino, int colDestino) {
        int fila = Integer.signum(filaDestino - pieza.getFila());
        int columna = Integer.signum(colDestino - pieza.getColumna());
        int filaOrigen = pieza.getFila() + fila;
        int columnaOrigen = pieza.getColumna() + columna;
        while (filaOrigen != filaDestino || columnaOrigen != colDestino) {
            if (tablero[filaOrigen][columnaOrigen] != null) {
                return false;
            }
            filaOrigen += fila;
            columnaOrigen += columna;
        }
        return true; //Casilla Libre
    }

    // ANÁLISIS DE POSICIÓN
    private boolean casillaControlada(int filaDestino, int colDestino, boolean esBlanca) { //casillaControlada = detector de amenazas enemigas
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza origen = tablero[fila][columna];
                if (origen == null || origen.getEsBlanca() == esBlanca) {
                    continue;
                }
                if (origen instanceof Peon || origen instanceof Caballo) {
                    if (origen.controla(filaDestino, colDestino)) { 
                        return true;
                    }
                    continue;
                }
                if (origen instanceof Rey) {
                    int filaRey = Math.abs(origen.getFila() - filaDestino);
                    int colRey = Math.abs(origen.getColumna() - colDestino);
                    if (filaRey <= 1 && colRey <= 1) {
                        return true;
                    }
                    continue;
                }
                if (origen.controla(filaDestino, colDestino)) {
                    if (origen instanceof Torre || origen instanceof Alfil || origen instanceof Reina) {
                        if (!caminoLibre(origen, filaDestino, colDestino)) { 
                            continue;
                        }
                    }
                    return true;
                }
            }
        }
        return false; // No hay nada que controle
    }

    private boolean Simulacion(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        Pieza origen = tablero[filaOrigen][colOrigen]; 
        Pieza destino = tablero[filaDestino][colDestino];
        boolean seMovio = origen.getSeMovio();
        int[] movimiento = (ultimoMovimiento != null) ? ultimoMovimiento.clone() : null;
        boolean legal = true;
        tablero[filaOrigen][colOrigen] = null;
        tablero[filaDestino][colDestino] = origen;
        origen.setFila(filaDestino);
        origen.setColumna(colDestino);
        if (estaEnJaque(origen.getEsBlanca())) {
            legal = false;
        }
        if (legal && origen instanceof Rey) {
            if (casillaControlada(filaDestino, colDestino, origen.getEsBlanca())) {
                legal = false;
            }
        }
        tablero[filaOrigen][colOrigen] = origen;
        tablero[filaDestino][colDestino] = destino;
        origen.setFila(filaOrigen);
        origen.setColumna(colOrigen);
        origen.setSeMovio(seMovio);
        ultimoMovimiento = movimiento;
        return legal;
    }

    public boolean estaEnJaque(boolean esBlanca) {
        int filaRey = -1;
        int colRey = -1;
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza rey = tablero[fila][columna];
                if (rey instanceof Rey && rey.getEsBlanca() == esBlanca) {
                    filaRey = fila;
                    colRey = columna; // Se encontro al rey 
                    break;
                }
            }
        }
        if (filaRey == -1 || colRey == -1) {
            return false; // El rey no está en el tablero
        }
        return casillaControlada(filaRey, colRey, esBlanca); // Devuelve true si controla y false si no 
    }

    public boolean estaJaqueMate(boolean esBlanca) {
        if (!estaEnJaque(esBlanca)) { 
            return false;
        }
        for (int fila = 0; fila < 8; fila++) {   
            for (int columna = 0; columna < 8; columna++) {     
                Pieza pieza = tablero[fila][columna]; 
                if (pieza == null || pieza.getEsBlanca() != esBlanca) {    
                    continue;
                }
                for (int filaDestino = 0; filaDestino < 8; filaDestino++) {
                    for (int colDestino = 0; colDestino < 8; colDestino++) {
                        Pieza destino = tablero[filaDestino][colDestino];
                        // Movimiento normal
                        if (destino == null) {
                            if (!pieza.movimiento(filaDestino, colDestino)) {
                                continue;
                            }
                        } else {
                            if (destino.getEsBlanca() == esBlanca) {
                                continue;
                            }
                            if (!pieza.ataca(filaDestino, colDestino)) {
                                continue;
                            }
                        }  
                        if (pieza instanceof Torre || pieza instanceof Alfil || pieza instanceof Reina) {  
                            if (!caminoLibre(pieza, filaDestino, colDestino)) {
                                continue;
                            }
                        }
                        if (Simulacion(fila, columna, filaDestino, colDestino)) {
                            return false; // Puede escapar
                        }
                    }
                }
            }
        }
        return true; // JAQUE MATE
    }

    public String EscapeDelRey(boolean esBlanca) {
        int filaRey = -1;
        int colRey = -1;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza rey = tablero[fila][columna];

                if (rey instanceof Rey && rey.getEsBlanca() == esBlanca) {
                    filaRey = fila;
                    colRey = columna;
                    break;
                }
            }
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Posición del rey ").append(esBlanca ? "blanco" : "negro").append(": ")
            .append((char) ('A' + colRey)).append(8 - filaRey).append("\n");
        mensaje.append(" # Posibles movimientos:\n");

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                if (!casillaValida(fila, columna)) {
                    continue;
                }
                int filaDestino = Math.abs(fila - filaRey);
                int colDestino = Math.abs(columna - colRey);
                if (filaDestino > 1 || colDestino > 1) {
                    continue;
                }
                Pieza destino = tablero[fila][columna];
                if (destino != null && destino.getEsBlanca() == esBlanca) {
                    continue;
                }
                if (Simulacion(filaRey, colRey, fila, columna)) {
                    mensaje.append((char) ('A' + columna)).append(8 - fila).append("\n");
                }
            }
        }
        return mensaje.toString();
    }
    
    // TABLAS Y FINALIZACIÓN
    public boolean tablasAhogado(boolean esTurnoBlanco) {
        if (estaEnJaque(esTurnoBlanco)) {     
            return false;
        }
        for (int fila = 0; fila < 8; fila++) {      
            for (int columna = 0; columna < 8; columna++) {    
                Pieza pieza = tablero[fila][columna];
                if (pieza == null || pieza.getEsBlanca() != esTurnoBlanco) {       
                    continue; 
                }
                for (int filaDestino = 0; filaDestino < 8; filaDestino++) {  
                    for (int colDestino = 0; colDestino < 8; colDestino++) {   
                        Pieza destino = tablero[filaDestino][colDestino];
                        if (destino == null) {
                            if (!pieza.movimiento(filaDestino, colDestino)) {
                                continue;
                            }
                        } else {
                            if (destino.getEsBlanca() == esTurnoBlanco) {
                                continue;
                            }
                            if (!pieza.ataca(filaDestino, colDestino)) {
                                continue;
                            }
                        }
                        if (pieza instanceof Torre || pieza instanceof Alfil || pieza instanceof Reina) {
                            if (!caminoLibre(pieza, filaDestino, colDestino)) {
                                continue;
                            }
                        }
                        if (pieza instanceof Peon) {
                            if (!caminoLibre(pieza, filaDestino, colDestino)) {
                                continue;
                            }
                        }
                        if (Simulacion(fila, columna, filaDestino, colDestino)) {
                            return false; // Existe un movimiento legal
                        }
                    }
                }
            }
        }
        return true; // Se valida el ahogado, no hay movimientos legales y el rey no está en jaque
    }

    public boolean tablasMaterialInsuficiente() {
        int piezas = 0;
        int rey = 0;
        int alfil = 0;
        int caballo = 0;

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Pieza pieza = tablero[fila][columna];
                if (pieza != null) {
                    piezas++;
                    if (pieza instanceof Alfil) {
                        alfil++;
                    }
                    if (pieza instanceof Caballo) {
                        caballo++;
                    }
                    if (pieza instanceof Rey) {
                        rey++;
                    }
                }
            }
        }

        if (piezas == 3 || piezas == 2) {
            if ((rey == 2) && (alfil == 0 && caballo == 0)) {
                return true; // Rey contra rey
            }
            if ((rey == 2) && (alfil ==1)) {
                return true; // Rey contra rey + alfil
            }
            if (rey == 2 && caballo == 1) {
                return true; // Rey contra rey + caballo
            }
        }
        return false;
    }

    public boolean tablasCincuentaMovimientos() {
        return cincuentaMovimientos >= 100;
    }

    public boolean tablasTripleRepeticion(String fen) {
        return registro.getOrDefault(fen, 0) >= 3;
    }

    // DATOS DEL MOVIMIENTO
    public List<Pieza> desambiguacion(Pieza pieza, int filaDestino, int colDestino) {
        List<Pieza> candidatas = new ArrayList<>();
        for (int filaOrigen = 0; filaOrigen < 8; filaOrigen++) {
            for (int colOrigen = 0; colOrigen < 8; colOrigen++) {
                if (pieza.getFila() == filaOrigen && pieza.getColumna() == colOrigen) {
                    continue;
                }
                Pieza candidata = tablero[filaOrigen][colOrigen];
                if (candidata == null || candidata.getClass() != pieza.getClass() || candidata.getEsBlanca() != pieza.getEsBlanca()) {
                    continue;
                }
                if (tablero[filaDestino][colDestino] == null) { // Si esta vacio Movimiento normal
                    if (!candidata.movimiento(filaDestino, colDestino)) {
                        continue; // Movimiento no válido
                    }
                } else { // Si hay una pieza en la casilla destino Captura
                    if (tablero[filaDestino][colDestino].getEsBlanca() == candidata.getEsBlanca()) {
                        continue; // No puede capturar una pieza del mismo color
                    }
                    if (!candidata.ataca(filaDestino, colDestino)) {
                        continue; // No puede capturar esa pieza
                    }
                }
                if (candidata instanceof Peon) {
                    continue; // No es valido para los peones
                }
                if (candidata instanceof Torre || candidata instanceof Alfil || candidata instanceof Reina) {
                    if (!caminoLibre(candidata, filaDestino, colDestino)) {  
                        continue; // El camino no está libre
                    }
                }    
                if (!Simulacion(filaOrigen, colOrigen, filaDestino, colDestino)) {
                    continue; // Movimiento ilegal, deja en jaque al propio rey
                }
                candidatas.add(candidata);
            }
        }
        return candidatas;
    }
}