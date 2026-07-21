package core;

import java.util.ArrayList;
import java.util.List;

public class Informe {
    private String pieza;
    private boolean esBlanca;
    private int filaOrigen; 
    private int colOrigen; 
    private int filaDestino; 
    private int colDestino;
    private boolean captura;
    private String piezaCapturada;
    private boolean enroqueLargoCorto;
    private boolean capturaAlPaso;
    private boolean promocionPeon;
    private boolean jaque;
    private boolean jaqueMate;
    private int numeroMovimiento;
    private String mensaje;
    private String fen;
    private String causaTablas;
    private String notacionAlgebraica;
    private char piezaPromocion;
    private List<int[]> candidatas;

    public Informe (String pieza, boolean esBlanca, int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        this.pieza = pieza;
        this.esBlanca = esBlanca;
        this.filaOrigen = filaOrigen;
        this.colOrigen = colOrigen;
        this.filaDestino = filaDestino;
        this.colDestino = colDestino;
        candidatas = new ArrayList<>();
    }

    public String getPieza() {
        return pieza;
    }

    public boolean getEsBlanca() {
        return esBlanca;
    }

    public int getFilaOrigen() {
        return filaOrigen;
    }

    public int getColOrigen() {
        return colOrigen;
    }

    public int getFilaDestino() {
        return filaDestino;
    }

    public int getColDestino() {
        return colDestino;
    }

    public void setCaptura(boolean captura) {
        this.captura = captura;
    }

    public boolean getCaptura() {
        return captura;
    }

    public void setPiezaCapturada(String piezaCapturada) {
        this.piezaCapturada = piezaCapturada;
    }

    public String getPiezaCapturada() {
        return piezaCapturada;
    }

    public void setEnroqueLargoCorto(boolean enroqueLargoCorto) {
        this.enroqueLargoCorto = enroqueLargoCorto;
    }

    public boolean getEnroqueLargoCorto() {
        return enroqueLargoCorto;
    }

    public void setCapturaAlPaso(boolean capturaAlPaso) {
        this.capturaAlPaso = capturaAlPaso;
    }

    public boolean getCapturaAlPaso() {
        return capturaAlPaso;
    }

    public void setPromocionPeon(boolean promocionPeon) {
        this.promocionPeon = promocionPeon;
    }

    public boolean getPromocionPeon() {
        return promocionPeon;
    }

    public void setJaque(boolean jaque) {
        this.jaque = jaque;
    }

    public boolean getJaque() {
        return jaque;
    }

    public void setJaqueMate(boolean jaqueMate) {
        this.jaqueMate = jaqueMate;
    }

    public boolean getJaqueMate() {
        return jaqueMate;
    }

    public void setNumeroMovimiento(int numeroMovimiento) {
        this.numeroMovimiento = numeroMovimiento;
    }

    public int getNumeroMovimiento() {
        return numeroMovimiento;
    }

    public String getOrigenTexto() {
        return "" + (char)('A' + colOrigen) + (8 - filaOrigen);
    }

    public String getDestinoTexto() {
        return "" + (char)('A' + colDestino) + (8 - filaDestino);
    }

    public String getColorTexto() {
        return esBlanca ? "Blanco" : "Negro";
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setMensajeFormato(String formato, Object... args) {
        this.mensaje = String.format(formato, args);
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean tieneMensaje() {
        return mensaje != null && !mensaje.isBlank();
    }

    public String tipoMovimiento() {
        if (enroqueLargoCorto) {
            return colDestino == 6 ? "Enroque Corto" : "Enroque Largo";
        }      
        if (capturaAlPaso) {
            return "Captura al paso";
        }    
        if (promocionPeon) {
            return "Promoción";
        }
        if (captura) {
            return "Captura";
        }
        return "Normal";
    }

    public void setFEN(String fen) {
        this.fen = fen;
    }

    public String getFEN() {
        return fen;
    }

    public void setCausaTablas(String causaTablas) {
        this.causaTablas = causaTablas;
    }

    public String getCausaTablas() {
        return causaTablas;
    }

    public boolean hayTablas() {
        return causaTablas != null && !causaTablas.isBlank();
    }

    public void setPiezaPromocion(char piezaPromocion) {
        this.piezaPromocion = piezaPromocion;
    }

    public char getPiezaPromocion() {
        return piezaPromocion;
    }

    public int[] ultimoMovimiento() {
        if (pieza.equals("Peon") && Math.abs(filaDestino - filaOrigen) == 2) {
            return new int[] {filaOrigen, colOrigen, filaDestino, colDestino};
        }
        return null;
    }
    
    public String descripcion(String blancas, String negras) {
        String jugador = esBlanca ? blancas : negras;

        if (jaqueMate) {
            return jugador + " dio jaque mate.";
        }
        if (jaque) {
            return jugador + " dio jaque.";
        }
        if (enroqueLargoCorto) {
            return jugador + " realizó un " + (colDestino == 6 ? "enroque corto." : "enroque largo.");
        }
        if (promocionPeon) {
            return jugador + " promovió un peón a " + piezaPromocion + ".";
        }
        if (capturaAlPaso) {
            return jugador + " capturó al paso.";
        }
        if (captura) {
            return jugador + " capturó un " + piezaCapturada + ".";
        }
        return jugador + " jugó " + notacionAlgebraica() + ".";
    }

    public void setCadidatas(int[] candidata) { 
        this.candidatas.add(candidata); 
    }

    private String Desambiguacion() {
        if (candidatas == null || candidatas.isEmpty()) {
            return "";
        }

        boolean columna = false;
        boolean fila = false;
        for (int[] candidata : candidatas) {
            int filaCandidata = candidata[0];
            int colCandidata = candidata[1];
            if (colCandidata == colOrigen) {
                columna = true;
            }
            if (filaCandidata == filaOrigen) {
                fila = true;
            }
        }

        char columnaOrigen = (char) ('a' + colOrigen);
        int filOrigen = 8 - filaOrigen;
        if (!columna) {
            return "" + columnaOrigen;
        }
        if (!fila) {
            return "" + filOrigen;
        }
        return "" + columnaOrigen + filOrigen;
    }

    private String inicialPieza() {
        switch (pieza) {
            case "Rey":
                return "K";
            case "Reina":
                return "Q";
            case "Torre":
                return "R";
            case "Alfil":
                return "B";
            case "Caballo":
                return "N";
            case "Peon":
                return "";
            default:
                return "";
        }
    }

    public void setNotacionAlgebraica(String notacionAlgebraica) {
        this.notacionAlgebraica = notacionAlgebraica;
    }

    public String getNotacionAlgebraica() {
        return notacionAlgebraica;
    }

    public String notacionAlgebraica() {
        String desambiguacion = Desambiguacion();
        char columnaDestino = (char) ('a' + colDestino);
        char columnaOrigen = (char) ('a' + colOrigen);
        int fila = 8 - filaDestino;

        if (enroqueLargoCorto) {
            notacionAlgebraica = colDestino == 6 ? "O-O" : "O-O-O";
        } else if (capturaAlPaso) {
            notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + fila;
        } else if (promocionPeon) {
            if (captura) {
                notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + fila + "=" + piezaPromocion;
            } else {
                notacionAlgebraica = "" + columnaDestino + fila + "=" + piezaPromocion;
            }
        } else if (captura) {
            if (pieza.equals("Peon")) {
                notacionAlgebraica = "" + columnaOrigen + "x" + columnaDestino + fila;
            } else {
                notacionAlgebraica = inicialPieza() + desambiguacion + "x" + columnaDestino + fila;
            }  
        } else if (pieza.equals("Peon")) {
            notacionAlgebraica = "" + columnaDestino + fila;
        } else {
            notacionAlgebraica = inicialPieza() + desambiguacion + columnaDestino + fila;
        }

        if (jaqueMate) {
            notacionAlgebraica += "#";
        } else if (jaque) {
            notacionAlgebraica += "+";
        }
        return notacionAlgebraica;
    }
}