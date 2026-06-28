import java.util.Scanner;

public class Excepciones {
    public static Scanner sc = new Scanner(System.in);
    private static Consola consola = new Consola();

    public static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    public static int leerNumero(String mensaje, int limInf, int limSup) {
        int numero = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.println(mensaje);
                numero = Integer.parseInt(sc.nextLine());

                if (numero >= limInf && numero <= limSup) {
                    valido = true;
                } else {
                    consola.mensaje("Error: Ingresa una opción válida.");
                }

            } catch (NumberFormatException e) {
                consola.mensaje("Error: Ingresa un número entero válido.");
            }
        }

        return numero;
    }

    public static String leerString(String mensaje) {
        System.out.println(mensaje);
        return sc.nextLine();
    }

    public static String leerNombre(String nombre) {
        String texto = "";
        boolean valido = false;

        String[] palabrasProhibidas = {
            "PENE", "PUTA", "VERGA", "CULO", "MAMADA", "VAGINA"
        };

        while (!valido) {
            texto = leerString(nombre).trim();
            String textoNormalizado = texto.toUpperCase();

            if (texto.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+") &&
                texto.replaceAll("\\s+", "").length() >= 3) {

                boolean contieneProhibida = false;

                for (String prohibida : palabrasProhibidas) {
                    if (textoNormalizado.contains(prohibida)) {
                        contieneProhibida = true;
                        break;
                    }
                }

                if (contieneProhibida) {
                    consola.mensaje("Nombre no permitido. Se ha detectado lenguaje ofensivo. Intenta de nuevo.");
                } else {
                    valido = true;
                }

            } else {
                consola.mensaje("Error: Solo se permiten letras y debe tener al menos 3 caracteres.");
            }
        }

        return texto;
    }

    public static String[] leerMovimiento(String mensaje) {
        String linea = "";
        String[] partes = null;
        boolean valido = false;

        while (!valido) {
            System.out.println(mensaje);
            linea = sc.nextLine().trim().toUpperCase();

            if (linea.equals("MENU")) {
                partes = new String[] {"MENU"};
                valido = true;
            } else if (linea.equals("SI")) {
                partes = new String[] {"SI"};
                valido = true;
            } else if (linea.equals("RENDIRSE")) {
                partes = new String[] {"RENDIRSE"};
                valido = true;
            } else if (linea.equals("TABLAS")) {
                partes = new String[] {"TABLAS"};
                valido = true;
            } else if (linea.equals("ACEPTO")) {
                partes = new String[] {"ACEPTO"};
                valido = true;
            } else if (linea.equals("RECHAZO")) {
                partes = new String[] {"RECHAZO"};
                valido = true;
            } else if (linea.equals("NO")) {
                partes = new String[] {"NO"};
                valido = true;
            } else {
                partes = linea.split(" ");

                if (partes.length == 2 &&
                    esCasillaValida(partes[0]) &&
                    esCasillaValida(partes[1])) {

                    valido = true;
                } else {
                    consola.mensaje("Error: Ingresa un movimiento válido (ej: E2 E4) o escribe MENU, RENDIRSE, TABLAS, ACEPTO o RECHAZO.");
                }
            }
        }

        return partes;
    }

    public static boolean esCasilla(String casilla) {
        if (casilla == null || casilla.length() != 2) {
            return false;
        }

        char letra = Character.toUpperCase(casilla.charAt(0));
        char numero = casilla.charAt(1);

        return letra >= 'A' && letra <= 'H' && numero >= '1' && numero <= '8';
    }

    private static boolean esCasillaValida(String casilla) {
        return casilla.matches("^[A-H][1-8]$");
    }
}