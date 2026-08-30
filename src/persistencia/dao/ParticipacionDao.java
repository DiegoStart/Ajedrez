package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Participacion;
import persistencia.Conexion;

public class ParticipacionDao {
    private Conexion conexion;
    private PartidaDao partidaDao;
    private JugadorDao jugadorDao;
    private String sql;

    public ParticipacionDao() {
        conexion = new Conexion();
        partidaDao = new PartidaDao();
        jugadorDao = new JugadorDao();
    }

    public void registrar(Participacion participacion) {
        sql = "INSERT INTO ajedrez.participacion(id_partida, id_jugador, color, resultado_individual, tiempo_restante) VALUES (?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, participacion.getPartida().getIdPartida());
            pstmt.setInt(2, participacion.getJugador().getIdJugador());
            pstmt.setBoolean(3, participacion.getColor());
            pstmt.setString(4, participacion.getResultadoIndividual());
            pstmt.setInt(5, participacion.getTiempoRestante());
        } catch (SQLException e) {
            System.out.println("Error al registrar participacion: " + e.getMessage());
        }
    }

    public void eliminar(int idParticipacion) {
        sql = "DELETE FROM ajedrez.participacion WHERE id_participacion = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idParticipacion); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar participacion: " + e.getMessage());
        }
    }

    public List<Participacion> mostrar() {
        List<Participacion> participaciones = new ArrayList<>();
        sql = "SELECT id_participacion, id_partida, id_jugador, color, resultado_individual, tiempo_restante FROM ajedrez.participacion ORDER BY id_participacion";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return participaciones;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Participacion participacion = new Participacion();
                participacion.setIdParticipacion(rs.getInt("id_participacion"));
                participacion.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                participacion.setJugador(jugadorDao.buscar(rs.getInt("id_jugador")));
                participacion.setColor(rs.getBoolean("color"));
                participacion.setResultadoIndividual(rs.getString("resultado_individual"));
                participacion.setTiempoRestante(rs.getInt("tiempo_restante"));
                participaciones.add(participacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar participaciones guardados: " + e.getMessage());
        }
        return participaciones;
    }

    public void actualizar(Participacion participacion) {
        sql = "UPDATE ajedrez.participacion SET resultado_individual = ?, tiempo_restante = ? WHERE id_participacion = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, participacion.getResultadoIndividual());
            pstmt.setInt(2, participacion.getTiempoRestante());
            pstmt.setInt(3, participacion.getIdParticipacion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar participacion: " + e.getMessage());
        }
    }

    public Participacion buscar(int idParticipacion) {
        sql = "SELECT id_participacion, id_partida, id_jugador, color, resultado_individual, tiempo_restante FROM ajedrez.participacion WHERE id_participacion = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idParticipacion);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Participacion participacion = new Participacion();
                participacion.setIdParticipacion(rs.getInt("id_participacion"));
                participacion.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                participacion.setJugador(jugadorDao.buscar(rs.getInt("id_jugador")));
                participacion.setColor(rs.getBoolean("color"));
                participacion.setResultadoIndividual(rs.getString("resultado_individual"));
                participacion.setTiempoRestante(rs.getInt("tiempo_restante"));
                return participacion;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar participacion: " + e.getMessage());
        }
        return null;
    }

    public List<Participacion> buscarPorPartida(int idPartida) {
        List<Participacion> participaciones = new ArrayList<>();
        sql = "SELECT id_participacion, id_partida, id_jugador, color, resultado_individual, tiempo_restante FROM ajedrez.participacion WHERE id_partida = ? ORDER BY id_participacion";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return participaciones;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Participacion participacion = new Participacion();
                participacion.setIdParticipacion(rs.getInt("id_participacion"));
                participacion.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                participacion.setJugador(jugadorDao.buscar(rs.getInt("id_jugador")));
                participacion.setColor(rs.getBoolean("color"));
                participacion.setResultadoIndividual(rs.getString("resultado_individual"));
                participacion.setTiempoRestante(rs.getInt("tiempo_restante"));
                participaciones.add(participacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar participacion: " + e.getMessage());
        }
        return participaciones;
    }

    public List<Participacion> buscarPorJugador(int idJugador) {
        List<Participacion> participaciones = new ArrayList<>();
        sql = "SELECT id_participacion, id_partida, id_jugador, color, resultado_individual, tiempo_restante FROM ajedrez.participacion WHERE id_jugador = ? ORDER BY id_participacion";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return participaciones;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Participacion participacion = new Participacion();
                participacion.setIdParticipacion(rs.getInt("id_participacion"));
                participacion.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                participacion.setJugador(jugadorDao.buscar(rs.getInt("id_jugador")));
                participacion.setColor(rs.getBoolean("color"));
                participacion.setResultadoIndividual(rs.getString("resultado_individual"));
                participacion.setTiempoRestante(rs.getInt("tiempo_restante"));
                participaciones.add(participacion);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar participacion: " + e.getMessage());
        }
        return participaciones;
    }

    public Participacion buscarPorPartidaYColor(int idPartida, boolean color) {
        sql = "SELECT id_participacion, id_partida, id_jugador, color, resultado_individual, tiempo_restante FROM ajedrez.participacion WHERE id_partida = ? AND color = ? ORDER BY id_participacion";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            pstmt.setBoolean(2, color);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Participacion participacion = new Participacion();
                participacion.setIdParticipacion(rs.getInt("id_participacion"));
                participacion.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                participacion.setJugador(jugadorDao.buscar(rs.getInt("id_jugador")));
                participacion.setColor(rs.getBoolean("color"));
                participacion.setResultadoIndividual(rs.getString("resultado_individual"));
                participacion.setTiempoRestante(rs.getInt("tiempo_restante"));
                return participacion;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar participacion: " + e.getMessage());
        }
        return null;
    }

    public int contarParticipantes(int idPartida) {
        sql = "SELECT COUNT(*) FROM ajedrez.participacion WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return 0;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar participantes: " + e.getMessage());
        }
        return 0;
    }

    public void actualizarResultado(Participacion participacion) {
        sql = "UPDATE ajedrez.participacion SET resultado_individual = ? WHERE id_participacion = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, participacion.getResultadoIndividual());
            pstmt.setInt(2, participacion.getIdParticipacion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar participacion: " + e.getMessage());
        }
    }

    public void actualizarTiempoRestante(Participacion participacion) {
        sql = "UPDATE ajedrez.participacion SET tiempo_restante = ? WHERE id_participacion = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, participacion.getTiempoRestante());
            pstmt.setInt(2, participacion.getIdParticipacion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar participacion: " + e.getMessage());
        }
    }
}
