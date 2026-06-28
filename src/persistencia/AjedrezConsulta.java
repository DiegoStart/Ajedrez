package persistencia;

import consola.*;
import core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AjedrezConsulta {
    Conexion conexion = new Conexion();
    Consola consola = new Consola();
    String sql = "";

    public int registrarJugador(String nombre) {
        sql = "INSERT INTO jugador (nombre) VALUES (?) RETURNING id_jugador";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return -1;
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_jugador");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar jugador: " + e.getMessage());
        }
        return -1;
    }

    public int registrarPartida(int idBlanco, int idNegro) {
        sql = "INSERT INTO partida (id_jugador_blanco, id_jugador_negro) VALUES (?, ?) RETURNING id_partida";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return -1;
        }
        
        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBlanco);
            pstmt.setInt(2, idNegro);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_partida");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar partida: " + e.getMessage());
        }
        return -1;
    }

    public int registrarMovimiento(int idPartida, Informe informe) {
        sql = "INSERT INTO movimiento " +
            "(id_partida, numero_movimiento, color, origen, destino, pieza, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_movimiento";

        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return -1;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            pstmt.setInt(2, informe.getNumeroMovimiento());
            pstmt.setString(3, informe.getColorTexto());
            pstmt.setString(4, informe.getOrigenTexto());
            pstmt.setString(5, informe.getDestinoTexto());
            pstmt.setString(6, informe.getPieza());
            pstmt.setString(7, informe.getPiezaCapturada());
            pstmt.setString(8, informe.tipoMovimiento());
            pstmt.setBoolean(9, informe.getJaque());
            pstmt.setBoolean(10, informe.getJaqueMate());
            pstmt.setString(11, informe.getCausaTablas());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_movimiento");
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
        }
        return -1;
    }
    
    public void actualizarPartida(int idPartida, String estado, String resultado, String causa) {
        sql = "UPDATE partida SET estado = ?, resultado = ?, causa_finalizacion = ?, fecha_fin = CURRENT_TIMESTAMP WHERE id_partida = ?";
   
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setString(2, resultado);
            pstmt.setString(3, causa);
            pstmt.setInt(4, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de partida: " + e.getMessage());
        }
    }

    public void guardarPartida(int idPartida, String tablero, boolean turno, int tiempoBlancas, int tiempoNegras, int[] ultimoMovimiento, int contadorMovimientos, int cincuentaMovimientos) {
        sql = "INSERT INTO tablero (id_partida, tablero_actual, turno_actual, tiempo_blancas, tiempo_negras, ultimo_movimiento, contador_movimientos, cincuenta_movimientos) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id_partida) DO UPDATE SET tablero_actual = EXCLUDED.tablero_actual, " +
            "turno_actual = EXCLUDED.turno_actual, tiempo_blancas = EXCLUDED.tiempo_blancas, tiempo_negras = EXCLUDED.tiempo_negras, ultimo_movimiento = EXCLUDED.ultimo_movimiento, " +
            "contador_movimientos = EXCLUDED.contador_movimientos, cincuenta_movimientos = EXCLUDED.cincuenta_movimientos";

      Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }
        String turnoActual = turno ? "Blanco" : "Negro";
        String movimiento = null;
        if (ultimoMovimiento != null && ultimoMovimiento.length == 4 && ultimoMovimiento[0] >= 0) {
            movimiento = ultimoMovimiento[0] + "," + ultimoMovimiento[1] + "," + ultimoMovimiento[2] + "," + ultimoMovimiento[3];
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idPartida);
                pstmt.setString(2, tablero);
                pstmt.setString(3, turnoActual);
                pstmt.setInt(4, tiempoBlancas);
                pstmt.setInt(5, tiempoNegras);
                pstmt.setString(6, movimiento);
                pstmt.setInt(7, contadorMovimientos);
                pstmt.setInt(8, cincuentaMovimientos);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar tablero: " + e.getMessage());
        }
    }

    public Tablero cargarPartida(int idPartida) {
        sql = "SELECT t.tablero_actual, t.turno_actual, t.tiempo_blancas, t.tiempo_negras, t.ultimo_movimiento, t.contador_movimientos, t.cincuenta_movimientos, " +
            "jb.nombre AS blanco, jn.nombre AS negro FROM tablero t JOIN partida p ON t.id_partida = p.id_partida " +
            "JOIN jugador jb ON p.id_jugador_blanco = jb.id_jugador JOIN jugador jn ON p.id_jugador_negro = jn.id_jugador " +
            "WHERE t.id_partida = ? AND p.estado = 'En curso'";
        
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return null;
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idPartida);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String respaldo = rs.getString("tablero_actual");
                    String turno = rs.getString("turno_actual");
                    int tiempoBlancas = rs.getInt("tiempo_blancas");
                    int tiempoNegras = rs.getInt("tiempo_negras");
                    String ultimoMovimientoTexto = rs.getString("ultimo_movimiento");
                    int contadorMovimientos = rs.getInt("contador_movimientos");
                    int cincuentaMovimientos = rs.getInt("cincuenta_movimientos");

                    String nombreBlanco = rs.getString("blanco");
                    String nombreNegro = rs.getString("negro");

                    Jugador blanco = new Jugador(nombreBlanco, true);
                    Jugador negro = new Jugador(nombreNegro, false);

                    Tablero tablero = new Tablero(blanco, negro);

                    tablero.restaurarPartida(respaldo);

                    int[] ultimoMovimiento = null;
                    if (ultimoMovimientoTexto != null && !ultimoMovimientoTexto.equals("-1,-1,-1,-1")) {
                        String[] partes = ultimoMovimientoTexto.split(",");
                        
                        ultimoMovimiento = new int[4];
                        ultimoMovimiento[0] = Integer.parseInt(partes[0]);
                        ultimoMovimiento[1] = Integer.parseInt(partes[1]);
                        ultimoMovimiento[2] = Integer.parseInt(partes[2]);
                        ultimoMovimiento[3] = Integer.parseInt(partes[3]);
                    }
                    tablero.restaurarEstado(turno.equals("Blanco"), ultimoMovimiento, tiempoBlancas, tiempoNegras, contadorMovimientos, cincuentaMovimientos);
                    return tablero;
                } else {
                    consola.mensaje("La partida no existe o ya fue finalizada.");
                    return null;
                }
        } catch (SQLException e) {
            System.out.println("Error al cargar tablero: " + e.getMessage());
        }
        return null;
    }

    public void eliminarMovimientos(int idPartida) {
        if (idPartida == 0) {
            return;
        }
        sql = "DELETE FROM movimiento WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar movimientos de la partida: " + e.getMessage());
        }
    }

    public void eliminarPartidas(int idPartida) {
        mostrarPartidasGuardadas();
        sql = "DELETE FROM partida WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn;
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar partida: " + e.getMessage());
        }
    }

    public boolean mostrarPartidasGuardadas() {
        boolean hayPartidas = false;
        sql = "SELECT p.id_partida, p.estado, jb.nombre AS blanco, jn.nombre AS negro FROM partida p " +
            "JOIN tablero t ON p.id_partida = t.id_partida JOIN jugador jb ON p.id_jugador_blanco = jb.id_jugador " +
            "JOIN jugador jn ON p.id_jugador_negro = jn.id_jugador WHERE p.estado = 'En curso' ORDER BY p.id_partida";

        consola.encabezadoPartidasGuardadas();
        Connection conn = conexion.establecerConexion();

        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return false;
        }

        try (conn;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idPartida = rs.getInt("id_partida");
                String estado = rs.getString("estado");
                String blanco = rs.getString("blanco");
                String negro = rs.getString("negro");
                String partida = blanco + " vs " + negro;

                consola.filaPartida(idPartida, partida, estado);
                hayPartidas = true;
            }

            if (!hayPartidas) {
                consola.sinPartidasGuardadas();
                return false;
            }
            consola.piePartidasGuardadas();
        } catch (SQLException e) {
            System.out.println("Error al mostrar partidas guardadas: " + e.getMessage());
        }   
        return true;
    }

    public void mostrarJugadores() {
        boolean hayJugadores = false;
        sql = "SELECT * FROM jugador ORDER BY id_jugador";

        consola.encabezadoJugadores();
        Connection conn = conexion.establecerConexion();

        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int idJugador = rs.getInt("id_jugador");
                String nombre = rs.getString("nombre");

                consola.filaJugador(idJugador, nombre);
                hayJugadores = true;
            }

            if (!hayJugadores) {
                consola.sinJugadores();
                return;
            }
            consola.pieJugadores();
            } catch (SQLException e) {
            System.out.println("Error al mostrar jugadores: " + e.getMessage());
        }
    }

    public void mostrarMovimientos(int idPartida) {
        boolean hayMovimientos = false;
        sql = "SELECT numero_movimiento, color, origen, destino, pieza, pieza_capturada " +
            "FROM movimiento WHERE id_partida = ? ORDER BY numero_movimiento, id_movimiento";

        consola.encabezadoMovimientos();
        Connection conn = conexion.establecerConexion();

        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int numeroMovimiento = rs.getInt("numero_movimiento");
                String color = rs.getString("color");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                String pieza = rs.getString("pieza");
                String piezaCapturada = rs.getString("pieza_capturada");

                if (piezaCapturada == null) {
                    piezaCapturada = "-";
                }
                consola.filaMovimiento(numeroMovimiento, color, origen, destino, pieza, piezaCapturada);
                hayMovimientos = true;
            }

            if (!hayMovimientos) {
                consola.sinMovimientos();
            }
            consola.pieMovimientos();
        } catch (SQLException e) {
            System.out.println("Error al mostrar movimientos: " + e.getMessage());
        }
    }
}
        