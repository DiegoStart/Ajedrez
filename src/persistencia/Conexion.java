package persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import consola.Consola;

public class Conexion {
    private String url;
    private String user;
    private String password;

    public Conexion() {
        Properties propiedades = new Properties();

        try (FileInputStream archivo = new FileInputStream("config.properties")) {
            propiedades.load(archivo);
            url = propiedades.getProperty("db.url");
            user = propiedades.getProperty("db.user");
            password = propiedades.getProperty("db.password");

        } catch (IOException e) {
            Consola.error("No se pudo leer config.properties");
        }
    }

    public Connection establecerConexion() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Consola.error("Error en la conexión: " + e.getMessage());
            return null;
        }
    }
}