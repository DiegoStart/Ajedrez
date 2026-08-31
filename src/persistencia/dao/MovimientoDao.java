package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import consola.Consola;
import modelo.Movimiento;
import persistencia.Conexion;

public class MovimientoDao {
    private Conexion conexion;
    private PartidaDao partidaDao;
    private String sql;

    public MovimientoDao() {
        conexion = new Conexion();
        partidaDao = new PartidaDao();
    }

    public void registrar(Movimiento movimiento) {
        sql = "INSERT INTO ajedrez.movimiento(id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movimiento.getPartida().getIdPartida());
            pstmt.setInt(2, movimiento.getNumeroMovimiento());
            pstmt.setBoolean(3, movimiento.getColor());
            pstmt.setString(4, movimiento.getPieza());
            pstmt.setString(5, Arrays.toString(movimiento.getOrigen()));
            pstmt.setString(6, Arrays.toString(movimiento.getDestino()));
            pstmt.setString(7, movimiento.getPiezaCapturada());
            pstmt.setString(8, movimiento.getTipoMovimiento());
            pstmt.setBoolean(9, movimiento.getJaque());
            pstmt.setBoolean(10, movimiento.getJaqueMate());
            pstmt.setString(11, movimiento.getCausaTablas());
            pstmt.setString(12, movimiento.getNotacionAlgebraica());
            pstmt.setString(13, movimiento.getPiezaPromocion());
            pstmt.setString(14, movimiento.getFen());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Consola.error("Error al registrar movimiento: " + e.getMessage());
        }
    }

    public void eliminar(int idMovimiento) {
        sql = "DELETE FROM ajedrez.movimiento WHERE id_movimiento = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMovimiento); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Consola.error("Error al eliminar movimiento: " + e.getMessage());
        }
    }

    public List<Movimiento> mostrar() {
        List<Movimiento> movimientos = new ArrayList<>();
        sql = "SELECT id_movimiento, id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen, fecha_movimiento FROM ajedrez.movimiento ORDER BY id_movimiento";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return movimientos;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
                movimiento.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                movimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                movimiento.setColor(rs.getBoolean("color"));
                movimiento.setPieza(rs.getString("pieza"));
                movimiento.setOrigen(convertirCoordenada(rs.getString("origen")));
                movimiento.setDestino(convertirCoordenada(rs.getString("destino")));
                movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
                movimiento.setJaque(rs.getBoolean("jaque"));
                movimiento.setJaqueMate(rs.getBoolean("jaque_mate"));
                movimiento.setCausaTablas(rs.getString("causa_tablas"));
                movimiento.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                movimiento.setPiezaPromocion(rs.getString("pieza_promocion"));
                movimiento.setFen(rs.getString("fen"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                movimientos.add(movimiento);
            }
        } catch (SQLException e) {
            Consola.error("Error al mostrar movimientos guardados: " + e.getMessage());
        }
        return movimientos;
    }

    public Movimiento buscar(int idMovimiento) {
        sql = "SELECT id_movimiento, id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen, fecha_movimiento FROM ajedrez.movimiento WHERE id_movimiento = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMovimiento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
                movimiento.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                movimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                movimiento.setColor(rs.getBoolean("color"));
                movimiento.setPieza(rs.getString("pieza"));
                movimiento.setOrigen(convertirCoordenada(rs.getString("origen")));
                movimiento.setDestino(convertirCoordenada(rs.getString("destino")));movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
                movimiento.setJaque(rs.getBoolean("jaque"));
                movimiento.setJaqueMate(rs.getBoolean("jaque_mate"));
                movimiento.setCausaTablas(rs.getString("causa_tablas"));
                movimiento.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                movimiento.setPiezaPromocion(rs.getString("pieza_promocion"));
                movimiento.setFen(rs.getString("fen"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                return movimiento;
            }
        } catch (SQLException e) {
            Consola.error("Error al buscar movimiento: " + e.getMessage());
        }
        return null;
    }

    public List<Movimiento> buscarPorPartida(int idPartida) {
        List<Movimiento> movimientos = new ArrayList<>();
        sql = "SELECT id_movimiento, id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen, fecha_movimiento FROM ajedrez.movimiento WHERE id_partida = ? ORDER BY numero_movimiento";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return movimientos;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
                movimiento.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                movimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                movimiento.setColor(rs.getBoolean("color"));
                movimiento.setPieza(rs.getString("pieza"));
                movimiento.setOrigen(convertirCoordenada(rs.getString("origen")));
                movimiento.setDestino(convertirCoordenada(rs.getString("destino")));movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
                movimiento.setJaque(rs.getBoolean("jaque"));
                movimiento.setJaqueMate(rs.getBoolean("jaque_mate"));
                movimiento.setCausaTablas(rs.getString("causa_tablas"));
                movimiento.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                movimiento.setPiezaPromocion(rs.getString("pieza_promocion"));
                movimiento.setFen(rs.getString("fen"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                movimientos.add(movimiento);
            }
        } catch (SQLException e) {
            Consola.error("Error al buscar movimiento: " + e.getMessage());
        }
        return movimientos;
    }

    public Movimiento buscarUltimoMovimiento(int idPartida) {
        sql = "SELECT id_movimiento, id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen, fecha_movimiento FROM ajedrez.movimiento WHERE id_partida = ? ORDER BY numero_movimiento DESC LIMIT 1";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
                movimiento.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                movimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                movimiento.setColor(rs.getBoolean("color"));
                movimiento.setPieza(rs.getString("pieza"));
                movimiento.setOrigen(convertirCoordenada(rs.getString("origen")));
                movimiento.setDestino(convertirCoordenada(rs.getString("destino")));movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
                movimiento.setJaque(rs.getBoolean("jaque"));
                movimiento.setJaqueMate(rs.getBoolean("jaque_mate"));
                movimiento.setCausaTablas(rs.getString("causa_tablas"));
                movimiento.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                movimiento.setPiezaPromocion(rs.getString("pieza_promocion"));
                movimiento.setFen(rs.getString("fen"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                return movimiento;
            }
        } catch (SQLException e) {
            Consola.error("Error al buscar movimiento: " + e.getMessage());
        }
        return null;
    }

    public Movimiento buscarPorNumeroMovimiento(int idPartida, int numeroMovimiento) {
        sql = "SELECT id_movimiento, id_partida, numero_movimiento, color, pieza, origen, destino, pieza_capturada, tipo_movimiento, jaque, jaque_mate, causa_tablas, notacion_algebraica, pieza_promocion, fen, fecha_movimiento FROM ajedrez.movimiento WHERE id_partida = ? AND numero_movimiento = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            pstmt.setInt(2, numeroMovimiento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
                movimiento.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                movimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
                movimiento.setColor(rs.getBoolean("color"));
                movimiento.setPieza(rs.getString("pieza"));
                movimiento.setOrigen(convertirCoordenada(rs.getString("origen")));
                movimiento.setDestino(convertirCoordenada(rs.getString("destino")));movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setPiezaCapturada(rs.getString("pieza_capturada"));
                movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
                movimiento.setJaque(rs.getBoolean("jaque"));
                movimiento.setJaqueMate(rs.getBoolean("jaque_mate"));
                movimiento.setCausaTablas(rs.getString("causa_tablas"));
                movimiento.setNotacionAlgebraica(rs.getString("notacion_algebraica"));
                movimiento.setPiezaPromocion(rs.getString("pieza_promocion"));
                movimiento.setFen(rs.getString("fen"));
                movimiento.setFechaMovimiento(rs.getTimestamp("fecha_movimiento").toLocalDateTime());
                return movimiento;
            }
        } catch (SQLException e) {
            Consola.error("Error al buscar movimiento: " + e.getMessage());
        }
        return null;
    }

    public int contarMovimientos(int idPartida) {
        sql = "SELECT COUNT(*) FROM ajedrez.movimiento WHERE id_partida = ?";
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
            Consola.error("Error al contar movimientos: " + e.getMessage());
        }
        return 0;
    }

    public void eliminarPorPartida(int idPartida) {
        sql = "DELETE FROM ajedrez.movimiento WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Consola.error("Error al eliminar movimientos: " + e.getMessage());
        }
    }

    private int[] convertirCoordenada(String coordenada) {
        return Arrays.stream(coordenada.replaceAll("[\\[\\]\\s]", "").split(",")).mapToInt(Integer::parseInt).toArray();
    }
}
