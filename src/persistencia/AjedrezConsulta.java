package persistencia;

import consola.*;
import core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AjedrezConsulta {
    Conexion conexion = new Conexion();
    Consola consola = new Consola();
    String sql = "";

    public int registrarJugador(String nombre) {
        sql = "INSERT INTO jugador (nombre, elo, ganadas, perdidas, tablas) VALUES (?, ?, ?, ?, ?) RETURNING id_jugador";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return -1;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setInt(2, 2403);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, 2);
            pstmt.setInt(5, 3);
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
        sql = "INSERT INTO partida (id_jugador_blanco, id_jugador_negro, time_control) VALUES (?, ?, ?) RETURNING id_partida";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return -1;
        }
        
        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idBlanco);
            pstmt.setInt(2, idNegro);
            pstmt.setInt(3, 600);
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
        sql = "INSERT INTO movimiento (id_partida, numero_movimiento, color, origen, destino, pieza, " +
            "pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_movimiento";
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
            pstmt.setString(12, informe.notacionAlgebraica());
            if (informe.getPromocionPeon()) {
                pstmt.setString(13, String.valueOf(informe.getPiezaPromocion()));
            } else {
                pstmt.setNull(13, java.sql.Types.VARCHAR);
            }
            pstmt.setString(14, informe.getFEN());

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
        sql = "UPDATE partida SET estado = ?, resultado = ?, causa_finalizacion = ?, fecha_fin = CURRENT_TIMESTAMP, duracion = ? WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setString(2, resultado);
            pstmt.setString(3, causa);
            pstmt.setInt(4, 400);
            pstmt.setInt(5, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de partida: " + e.getMessage());
        }
    }

    public void guardarPartida(int idPartida, String fen, boolean turno, int tiempoBlancas, int tiempoNegras, int[] ultimoMovimiento, int contadorMovimientos, int cincuentaMovimientos) {
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

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idPartida);
                pstmt.setString(2, fen);
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
            "jb.nombre AS blanco, jn.nombre AS negro FROM tablero t JOIN partida p ON t.id_partida = p.id_partida JOIN jugador jb ON p.id_jugador_blanco = jb.id_jugador JOIN jugador jn ON p.id_jugador_negro = jn.id_jugador " +
            "WHERE t.id_partida = ? AND p.estado = 'En curso'";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                HashMap<String, Integer> registro = cargarFENPosteriores(idPartida);
                tablero.restaurarRegistro(registro);
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

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar movimientos de la partida: " + e.getMessage());
        }
    }

    public void eliminarPartidas(int idPartida) {
        if (!mostrarPartidasEnCurso()) {
            return;
        } else if (!mostrarPartidasFinalizadas()) {
            return;
        }
        sql = "DELETE FROM partida WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar partida: " + e.getMessage());
        }
    }

    public boolean mostrarPartidasEnCurso() {
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

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
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

    public boolean mostrarPartidasFinalizadas() {
        boolean hayPartidas = false;
        sql = "SELECT p.id_partida, p.estado, jb.nombre AS blanco, jn.nombre AS negro FROM partida p " +
            "JOIN tablero t ON p.id_partida = t.id_partida JOIN jugador jb ON p.id_jugador_blanco = jb.id_jugador " +
            "JOIN jugador jn ON p.id_jugador_negro = jn.id_jugador WHERE p.estado = 'Finalizada' ORDER BY p.id_partida";
        consola.encabezadoPartidasGuardadas();
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return false;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
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
        sql = "SELECT numero_movimiento, color, origen, destino, pieza, pieza_capturada, notacion_algebraica " +
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
                String notacionAlgebraica = rs.getString("notacion_algebraica");

                if (piezaCapturada == null) {
                    piezaCapturada = "-";
                }
                consola.filaMovimiento(numeroMovimiento, color, origen, destino, pieza, piezaCapturada, notacionAlgebraica);
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

    public HashMap<String, Integer> cargarFENPosteriores(int idPartida) {
        HashMap<String, Integer> registro = new HashMap<>();
        sql = "SELECT fen FROM movimiento WHERE id_partida = ? AND fen IS NOT NULL ORDER BY id_movimiento";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return registro;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String fen = rs.getString("fen");
                registro.put(fen, registro.getOrDefault(fen, 0) + 1);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar posiciones: " + e.getMessage());
        }
        return registro;
    }

    public List<Informe> obtenerHistorial(int idPartida) {
        List<Informe> historial = new ArrayList<>();
        sql = "SELECT numero_movimiento, color, pieza, origen, destino, pieza_capturada, fen, " +
            "jaque, jaque_mate, causa_tablas, notacion_algebraica FROM movimiento WHERE id_partida = ? ORDER BY id_movimiento";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return historial;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int filaOrigen = 8 - Character.getNumericValue(rs.getString("origen").charAt(1));
                int colOrigen = rs.getString("origen").charAt(0) - 'A';
                int filaDestino = 8 - Character.getNumericValue(rs.getString("destino").charAt(1));
                int colDestino = rs.getString("destino").charAt(0) - 'A';
                Informe informe = new Informe(rs.getString("pieza"), rs.getString("color").equals("Blanco"), filaOrigen, colOrigen, filaDestino, colDestino);
                informe.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                informe.setPiezaCapturada(rs.getString("pieza_capturada"));
                informe.setFEN(rs.getString("fen"));
                informe.setJaque(rs.getBoolean("jaque"));
                informe.setJaqueMate(rs.getBoolean("jaque_mate"));
                informe.setCausaTablas(rs.getString("causa_tablas"));
                informe.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                historial.add(informe);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar historial: " + e.getMessage());
        }
        return historial;
    }

    public String[] obtenerInformacion(int idPartida) {
        String[] informacion = new String[9];
        sql = "SELECT jb.nombre AS blancas, jn.nombre AS negras, p.resultado, p.causa_finalizacion, jb.elo AS white_elo, jn.elo AS black_elo, p.time_control, TO_CHAR(p.fecha_inicio, 'YYYY.MM.DD') AS fecha, p.duracion FROM partida p JOIN jugador jb ON p.id_jugador_blanco = jb.id_jugador JOIN jugador jn ON p.id_jugador_negro = jn.id_jugador WHERE p.id_partida = ?;";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return informacion;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                informacion[0] = rs.getString("blancas");
                informacion[1] = rs.getString("negras");
                informacion[2] = rs.getString("resultado");
                informacion[3] = rs.getString("causa_finalizacion");
                informacion[4] = String.valueOf(rs.getInt("white_elo"));
                informacion[5] = String.valueOf(rs.getInt("black_elo"));
                informacion[6] = rs.getString("time_control");
                informacion[7] = rs.getString("fecha");
                informacion[8] = String.valueOf(rs.getInt("duracion"));
            }
        } catch (SQLException e) {
            consola.mensaje("Error al obtener jugadores: " + e.getMessage());
        }
        return informacion;
    }

    public String generarPGN(int idPartida) {
        String[] informacion = obtenerInformacion(idPartida);
        List<Informe> historial = obtenerHistorial(idPartida);
        StringBuilder pgn = new StringBuilder();
        // Seven Tag Roster (Obligatorios)
        pgn.append("[Event \"Ajedrez Alpha\"]\n");
        pgn.append("[Site \"Oaxaca, México\"]\n");
        pgn.append("[Date \"" + (informacion[7] == null ? "?" : informacion[7]) + "\"]\n");
        pgn.append("[Round \"?\"]\n");
        pgn.append("[White \"" + informacion[0] + "\"]\n");
        pgn.append("[Black \"" + informacion[1] + "\"]\n");
        pgn.append("[Result \"" + informacion[2] + "\"]\n");
        pgn.append("\n");
        // Información adicional
        pgn.append("[WhiteElo \"" + (informacion[4] == null ? "?" : informacion[4]) + "\"]\n");
        pgn.append("[BlackElo \"" + (informacion[5] == null ? "?" : informacion[5]) + "\"]\n");
        pgn.append("[WhiteTitle \"-\"]\n");
        pgn.append("[BlackTitle \"-\"]\n");
        pgn.append("[TimeControl \"" + (informacion[6] == null ? "?" : informacion[6]) + "\"]\n");
        pgn.append("[Termination \"Normal\"]\n");
        pgn.append("[Annotator \"Ajedrez Alpha\"]\n");
        pgn.append("[ECO \"-\"]\n");
        pgn.append("[Opening \"-\"]\n");
        pgn.append("[Variation \"-\"]\n");
        pgn.append("[PlyCount \"" + historial.size() + "\"]\n");
        pgn.append("[SetUp \"0\"]\n");
        pgn.append("[Link \"-\"]\n");
        pgn.append("\n");

        int actual = -1;
        for (Informe informe : historial) {
            if (informe.getNumeroMovimiento() != actual) {
                actual = informe.getNumeroMovimiento();
                pgn.append(actual);
                pgn.append(". ");
            }
            pgn.append(informe.getNotacionAlgebraica());
            pgn.append(" ");
        }
        pgn.append(informacion[2]);
        return pgn.toString();
    }

    public void actualizarVictorias(int idPartida, boolean ganoBlanco) {
        sql = "UPDATE jugador SET ganadas = ganadas + 1 WHERE id_jugador = (SELECT CASE WHEN ? THEN id_jugador_blanco ELSE id_jugador_negro END FROM partida WHERE id_partida = ?)";

        try (Connection conn = conexion.establecerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, ganoBlanco);
            pstmt.setInt(2, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar victorias: " + e.getMessage());
        }
    }

    public void actualizarPerdidas(int idPartida, boolean ganoBlanco) {
        sql = "UPDATE jugador SET perdidas = perdidas + 1 WHERE id_jugador = (SELECT CASE WHEN ? THEN id_jugador_blanco ELSE id_jugador_negro END FROM partida WHERE id_partida = ?)";

        try (Connection conn = conexion.establecerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, ganoBlanco);
            pstmt.setInt(2, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar perdidas: " + e.getMessage());
        }
    }

    public void actualizarTablas(int idPartida, boolean tablas) {
        sql = "UPDATE jugador SET tablas = tablas + 1 WHERE id_jugador = (SELECT CASE WHEN ? THEN id_jugador_blanco ELSE id_jugador_negro END FROM partida WHERE id_partida = ?)";

        try (Connection conn = conexion.establecerConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, tablas);
            pstmt.setInt(2, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar tablas: " + e.getMessage());
        }
    }

    public void mostrarEstadisticasJugador(int idJugador) {
        sql = "SELECT id_jugador, nombre, elo, ganadas, perdidas, tablas FROM jugador WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            consola.mensaje("No se pudo conectar a la base de datos.");
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();while (rs.next()) {
                consola.mostrarEstadisticasJugador(rs.getInt("id_jugador"), rs.getString("nombre"), rs.getInt("elo"), rs.getInt("ganadas"), rs.getInt("perdidas"), rs.getInt("tablas"));
            }
            } catch (SQLException e) {
            System.out.println("Error al mostrar jugadores: " + e.getMessage());
        }
    }
}