
package util;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {
    private static final int FACTOR_COSTE = 12;

    public static String generarHash(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(FACTOR_COSTE));
    }

    public static boolean verificarContrasena(String contrasena, String hash) {
        if (contrasena == null || hash == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(contrasena, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}