package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import modelo.Partida;
import persistencia.Conexion;

public class PartidaDao {
    private Conexion conexion;
    private String sql;

    public PartidaDao() {
        conexion = new Conexion();
    }

    public void registrar(Partida partida) {
        sql = "INSERT INTO ajedrez.partida(estado, resultado, causa_finalizacion, tipo_partida, tiempo_control, duracion) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, partida.getEstado());
            pstmt.setString(2, partida.getResultado());
            pstmt.setString(3, partida.getCausaFinalizacion());
            pstmt.setString(4, partida.getTipoPartida());
            pstmt.setString(5, partida.getTiempoControl());
            pstmt.setInt(6, partida.getDuracion());
            if (pstmt.executeUpdate() > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        partida.setIdPartida(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar partida: " + e.getMessage());
        }
    }

    public void eliminar(int idPartida) {
        sql = "DELETE FROM ajedrez.partida WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar partida: " + e.getMessage());
        }
    }

    public List<Partida> mostrar() {
        List<Partida> partidas = new ArrayList<>();
        sql = "SELECT id_partida, estado, resultado, causa_finalizacion, tipo_partida, tiempo_control, duracion, fecha_inicio, fecha_fin FROM ajedrez.partida ORDER BY id_partida";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return partidas;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setEstado(rs.getString("estado"));
                partida.setResultado(rs.getString("resultado"));
                partida.setCausaFinalizacion(rs.getString("causa_finalizacion"));
                partida.setTipoPartida(rs.getString("tipo_partida"));
                partida.setTiempoControl(rs.getString("tiempo_control"));
                partida.setDuracion(rs.getInt("duracion"));
                partida.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
                if (rs.getTimestamp("fecha_fin") != null) {
                    partida.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
                }
                partidas.add(partida);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar partidas guardadas: " + e.getMessage());
        }
        return partidas;
    }

    public void actualizar(Partida partida) {
        sql = "UPDATE ajedrez.partida SET estado = ?, resultado = ?, causa_finalizacion = ?, fecha_fin = ? WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, partida.getEstado());
            pstmt.setString(2, partida.getResultado());
            pstmt.setString(3, partida.getCausaFinalizacion());
            if (partida.getFechaFin() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(partida.getFechaFin()));
            } else {
                pstmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            pstmt.setInt(5, partida.getIdPartida());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar partida: " + e.getMessage());
        }
    }

    public Partida buscar(int idPartida) {
        sql = "SELECT id_partida, estado, resultado, causa_finalizacion, tipo_partida, tiempo_control, duracion, fecha_inicio, fecha_fin FROM ajedrez.partida WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setEstado(rs.getString("estado"));
                partida.setResultado(rs.getString("resultado"));
                partida.setCausaFinalizacion(rs.getString("causa_finalizacion"));
                partida.setTipoPartida(rs.getString("tipo_partida"));
                partida.setTiempoControl(rs.getString("tiempo_control"));
                partida.setDuracion(rs.getInt("duracion"));
                partida.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
                if (rs.getTimestamp("fecha_fin") != null) {
                    partida.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
                }
                return partida;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar partida: " + e.getMessage());
        }
        return null;
    }

    public List<Partida> buscarPorEstado(String estado) {
        List<Partida> partidas = new ArrayList<>();
        sql = "SELECT id_partida, estado, resultado, causa_finalizacion, tipo_partida, tiempo_control, duracion, fecha_inicio, fecha_fin FROM ajedrez.partida WHERE estado = ? ORDER BY id_partida";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return partidas;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setEstado(rs.getString("estado"));
                partida.setResultado(rs.getString("resultado"));
                partida.setCausaFinalizacion(rs.getString("causa_finalizacion"));
                partida.setTipoPartida(rs.getString("tipo_partida"));
                partida.setTiempoControl(rs.getString("tiempo_control"));
                partida.setDuracion(rs.getInt("duracion"));
                partida.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
                if (rs.getTimestamp("fecha_fin") != null) {
                    partida.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
                }
                partidas.add(partida);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar partida: " + e.getMessage());
        }
        return partidas;
    }

    public List<Partida> buscarPorJugador(int idJugador) {
        List<Partida> partidas = new ArrayList<>();
        sql = "SELECT p.id_partida, p.estado, p.resultado, p.causa_finalizacion, p.tipo_partida, p.tiempo_control, p.duracion, p.fecha_inicio, p.fecha_fin FROM ajedrez.partida p INNER JOIN ajedrez.participacion pa ON p.id_partida = pa.id_partida WHERE pa.id_jugador = ? ORDER BY p.fecha_inicio DESC";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return partidas;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Partida partida = new Partida();
                partida.setIdPartida(rs.getInt("id_partida"));
                partida.setEstado(rs.getString("estado"));
                partida.setResultado(rs.getString("resultado"));
                partida.setCausaFinalizacion(rs.getString("causa_finalizacion"));
                partida.setTipoPartida(rs.getString("tipo_partida"));
                partida.setTiempoControl(rs.getString("tiempo_control"));
                partida.setDuracion(rs.getInt("duracion"));
                partida.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
                if (rs.getTimestamp("fecha_fin") != null) {
                    partida.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
                }
                partidas.add(partida);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar partida: " + e.getMessage());
        }
        return partidas;
    }

    public void finalizarPartida(Partida partida) {
        sql = "UPDATE ajedrez.partida SET estado = ?, resultado = ?, causa_finalizacion = ?, duracion = ?, fecha_fin = CURRENT_TIMESTAMP WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, partida.getEstado());
            pstmt.setString(2, partida.getResultado());
            pstmt.setString(3, partida.getCausaFinalizacion());
            pstmt.setInt(4, partida.getDuracion());
            pstmt.setInt(5, partida.getIdPartida());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar partida: " + e.getMessage());
        }
    }

    public void actualizarEstado(int idPartida, String estado) {
        sql = "UPDATE ajedrez.partida SET estado = ? WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setInt(2, idPartida);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar partida: " + e.getMessage());
        }
    }

    public List<Partida> buscarPartidasEnCurso() {
        return buscarPorEstado("EN_CURSO");
    }
}
