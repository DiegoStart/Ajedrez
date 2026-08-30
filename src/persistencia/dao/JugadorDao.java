package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Jugador;
import persistencia.Conexion;

public class JugadorDao {
    private Conexion conexion;
    private UsuarioDao usuarioDao;
    private String sql;

    public JugadorDao() {
        conexion = new Conexion();
        usuarioDao = new UsuarioDao();
    }

    public void registrar(Jugador jugador) {
        sql = "INSERT INTO ajedrez.jugador(id_usuario, nombre_jugador, tipo, elo, victorias, derrotas, tablas, rendiciones, abandonos) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (jugador.getUsuario() != null) {
                pstmt.setInt(1, jugador.getUsuario().getIdUsuario());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, jugador.getNombreJugador());
            pstmt.setString(3, jugador.getTipo());
            pstmt.setInt(4, jugador.getElo());
            pstmt.setInt(5, jugador.getVictorias());
            pstmt.setInt(6, jugador.getDerrotas());
            pstmt.setInt(7, jugador.getTablas());
            pstmt.setInt(8, jugador.getRendiciones());
            pstmt.setInt(9, jugador.getAbandonos());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        jugador.setIdJugador(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar jugador: " + e.getMessage());
        }
        return;
    }

    public void eliminar(int idJugador) {
        sql = "DELETE FROM ajedrez.jugador WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar jugador: " + e.getMessage());
        }
        return;
    }

    public List<Jugador> mostrar() {
        List<Jugador> jugadores = new ArrayList<>();
        sql = "SELECT id_jugador, id_usuario, nombre_jugador, tipo, elo, victorias, derrotas, tablas, rendiciones, abandonos, fecha_creacion FROM ajedrez.jugador ORDER BY id_jugador";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return jugadores;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                if (!rs.wasNull()) {
                    jugador.setUsuario(usuarioDao.buscar(rs.getInt("id_usuario"))); 
                }
                jugador.setNombreJugador(rs.getString("nombre_jugador"));
                jugador.setTipo(rs.getString("tipo"));
                jugador.setElo(rs.getInt("elo"));
                jugador.setVictorias(rs.getInt("victorias"));
                jugador.setDerrotas(rs.getInt("derrotas"));
                jugador.setTablas(rs.getInt("tablas"));
                jugador.setRendiciones(rs.getInt("rendiciones"));
                jugador.setAbandonos(rs.getInt("abandonos"));
                jugador.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime()); 
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar jugadores guardados: " + e.getMessage());
        }
        return jugadores;
    }

    public void actualizar(Jugador jugador) {
        sql = "UPDATE ajedrez.jugador SET nombre_jugador = ?, tipo = ?, elo = ?, victorias = ?, derrotas = ?, tablas = ?, rendiciones = ?, abandonos = ? WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jugador.getNombreJugador());
            pstmt.setString(2, jugador.getTipo());
            pstmt.setInt(3, jugador.getElo());
            pstmt.setInt(4, jugador.getVictorias());
            pstmt.setInt(5, jugador.getDerrotas());
            pstmt.setInt(6, jugador.getTablas());
            pstmt.setInt(7, jugador.getRendiciones());
            pstmt.setInt(8, jugador.getAbandonos());
            pstmt.setInt(9, jugador.getIdJugador());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
        }
    }

    public Jugador buscar(int idJugador) {
        sql = "SELECT id_jugador, id_usuario, nombre_jugador, tipo, elo, victorias, derrotas, tablas, rendiciones, abandonos, fecha_creacion FROM ajedrez.jugador WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                if (!rs.wasNull()) {
                    jugador.setUsuario(usuarioDao.buscar(rs.getInt("id_usuario"))); 
                }
                jugador.setNombreJugador(rs.getString("nombre_jugador"));
                jugador.setTipo(rs.getString("tipo"));
                jugador.setElo(rs.getInt("elo"));
                jugador.setVictorias(rs.getInt("victorias"));
                jugador.setDerrotas(rs.getInt("derrotas"));
                jugador.setTablas(rs.getInt("tablas"));
                jugador.setRendiciones(rs.getInt("rendiciones"));
                jugador.setAbandonos(rs.getInt("abandonos"));
                jugador.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime()); 
                return jugador;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar jugador: " + e.getMessage());
        }
        return null;
    }

    public List<Jugador> buscarPorUsuario(int idUsuario) {
        List<Jugador> jugadores = new ArrayList<>();
        sql = "SELECT id_jugador, id_usuario, nombre_jugador, tipo, elo, victorias, derrotas, tablas, rendiciones, abandonos, fecha_creacion FROM ajedrez.jugador WHERE id_usuario = ? ORDER BY fecha_creacion";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return jugadores;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setIdJugador(rs.getInt("id_jugador"));
                if (!rs.wasNull()) {
                    jugador.setUsuario(usuarioDao.buscar(rs.getInt("id_usuario"))); 
                }
                jugador.setNombreJugador(rs.getString("nombre_jugador"));
                jugador.setTipo(rs.getString("tipo"));
                jugador.setElo(rs.getInt("elo"));
                jugador.setVictorias(rs.getInt("victorias"));
                jugador.setDerrotas(rs.getInt("derrotas"));
                jugador.setTablas(rs.getInt("tablas"));
                jugador.setRendiciones(rs.getInt("rendiciones"));
                jugador.setAbandonos(rs.getInt("abandonos"));
                jugador.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime()); 
                jugadores.add(jugador);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar jugadores: " + e.getMessage());
        }
        return jugadores;
    }

    public void actualizarElo(int idJugador, int elo) {
        sql = "UPDATE ajedrez.jugador SET elo = ? WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, elo);
            pstmt.setInt(2, idJugador);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
        }
    }

    public void actualizarEstadisticas(Jugador jugador) {
        sql = "UPDATE ajedrez.jugador SET elo = ?, victorias = ?, derrotas = ?, tablas = ?, rendiciones = ?, abandonos = ? WHERE id_jugador = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jugador.getElo());
            pstmt.setInt(2, jugador.getVictorias());
            pstmt.setInt(3, jugador.getDerrotas());
            pstmt.setInt(4, jugador.getTablas());
            pstmt.setInt(5, jugador.getRendiciones());
            pstmt.setInt(6, jugador.getAbandonos());
            pstmt.setInt(7, jugador.getIdJugador());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
        }
    }
}
