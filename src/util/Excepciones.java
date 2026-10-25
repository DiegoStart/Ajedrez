package util;

import java.time.LocalDate;
import java.util.Scanner;

import consola.Consola;
import controlador.Controlador;

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
            } catch (NumberFormatException e) {
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
                continue;
            }

            if (contienePalabraProhibida(nombre)) {
                continue;
            }
            return nombre;
        }
    }

    public static String leerUsuario(Controlador controlador) {
        while (true) {
            String nombre = leerString(">> Nombre de usuario: ").trim();
            if (nombre.isEmpty()) {
                consola.errorCaja("El nombre de usuario no puede estar vacío.");
                continue;
            }
            if (nombre.length() > 50) {
                consola.errorCaja("El nombre no puede superar los 50 caracteres.");
                continue;
            }
            if (controlador.buscarUsuarioExisteUsuario(nombre)) {
                consola.errorCaja("Ese nombre de usuario ya está ocupado.");
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
        }
    }

    public static java.time.LocalDate leerFecha() {
        while (true) {
            int anio = leerNumero(">> Año (YYYY): ", 1900, LocalDate.now().getYear());
            int mes = leerNumero(">> Mes (1-12):", 1, 12);
            int dia = leerNumero(">> Día (1-31):", 1, 31);

            try {
                LocalDate fecha = LocalDate.of(anio, mes, dia);
                if (fecha.isAfter(LocalDate.now())) {
                    consola.errorCaja("La fecha no puede ser futura.");
                    continue;
                }
                return fecha;
            } catch (java.time.DateTimeException e) {
                consola.errorCaja("Fecha Invalida. Intentalo de nuevo.");
            }
        }
    }

    public static String leerCorreo(Controlador controlador) {
        while (true) {
            String correo = leerString(">> Correo electrónico: ").trim();
            if (correo.length() > 100) {
                consola.errorCaja("El correo no puede superar los 100 caracteres.");
                continue;
            }

            String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!correo.matches(regex)) {
                consola.errorCaja("El formato no es válido. Ejemplo: usuario@dominio.com");
                continue;
            }

            correo = correo.toLowerCase(java.util.Locale.ROOT);
            if (controlador.buscarCorreoExisteUsuario(correo)) {
                consola.errorCaja("Este correo electrónico ya está registrado.");
                continue;
            }
            return correo;
        }
    }

    public static String leerContrasena() {
        while (true) {
            String contrasena = leerString(">> Contraseña: ");
            if (contrasena.length() < 12) {
                consola.errorCaja("La contraseña debe tener al menos 12 caracteres.");
                continue;
            }
            if (contrasena.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
                consola.errorCaja("La contraseña supera el límite admitido por BCrypt.");
                continue;
            }

            String confirmacion = leerString(">> Confirma tu contraseña: ");
            if (!contrasena.equals(confirmacion)) {
                consola.errorCaja("Las contraseñas no coinciden.");
                continue;
            }
            return contrasena;
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