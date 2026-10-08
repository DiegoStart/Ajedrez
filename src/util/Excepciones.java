package util;

import java.util.Scanner;

import consola.Consola;

public class Excepciones {
    private static final Scanner SC = new Scanner(System.in);
    private static final Consola consola = new Consola();

    private static final String[] PALABRAS_PROHIBIDAS = {"PENE", "PUTA", "VERGA", "CULO", "MAMADA", "VAGINA"};
    private static final String[] COMANDOS_VALIDOS = {"MENU", "SI", "RENDIRSE", "RECLAMAR", "TABLAS", "ACEPTO", "RECHAZO", "NO"};

    public static String leerLinea(String mensaje) {
        consola.mensaje(mensaje);
        return SC.nextLine();
    }

    public static int leerNumero(String mensaje, int limInf, int limSup) {
        while (true) {
            try {
                int numero = Integer.parseInt(leerLinea(mensaje));
                if (numero >= limInf && numero <= limSup) {
                    return numero;
                }

                Consola.error("Error: Ingresa una opción válida.");
            } catch (NumberFormatException e) {
                Consola.error("Error: Ingresa un número entero válido.");
            }
        }
    }

    public static String leerString(String mensaje) {
        return leerLinea(mensaje);
    }

    public static String leerNombre(String mensaje) {
        while (true) {
            String nombre = leerString(mensaje).trim();
            if (!nombre.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") || nombre.replaceAll("\\s+", "").length() < 3) {
                Consola.error("Error: Solo se permiten letras y debe tener al menos 3 caracteres.");
                continue;
            }

            if (contienePalabraProhibida(nombre)) {
                Consola.error("Nombre no permitido. Se ha detectado lenguaje ofensivo.");
                continue;
            }
            return nombre;
        }
    }

    private static boolean contienePalabraProhibida(String texto) {
        for (String palabra : PALABRAS_PROHIBIDAS) {
            if (texto.toUpperCase().contains(palabra)) {
                return true;
            }
        }
        return false;
    }

    public static String[] leerMovimiento(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje).trim().toUpperCase();
            if (comandos(linea)) {
                return new String[]{linea};
            }

            String[] partes = linea.split("\\s+");
            if (partes.length == 2 && casillaValida(partes[0]) && casillaValida(partes[1])) {
                return partes;
            }
            Consola.error("Error: Ingresa un movimiento válido (ej: E2 E4) " + "o un comando permitido.");
        }
    }

    private static boolean comandos(String comando) {
        for (String valido : COMANDOS_VALIDOS) {
            if (valido.equals(comando)) {
                return true;
            }
        }
        return false;
    }

    public static boolean esCasilla(String casilla) {
        return casillaValida(casilla == null ? null : casilla.toUpperCase());
    }

    private static boolean casillaValida(String casilla) {
        return casilla != null && casilla.matches("[A-H][1-8]");
    }
}