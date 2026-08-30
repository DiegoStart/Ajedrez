package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modelo.Instantanea;
import persistencia.Conexion;

public class InstantaneaDao {
    private Conexion conexion;
    private PartidaDao partidaDao;
    private String sql;

    public InstantaneaDao() {
        conexion = new Conexion();
        partidaDao = new PartidaDao();
    }

    public void registrar(Instantanea instantanea) {
        sql = "INSERT INTO ajedrez.instantanea(id_partida, estado_actual, turno_actual, tiempo_blancas, tiempo_negras, ultimo_movimiento, contador_movimientos, cincuenta_movimientos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, instantanea.getPartida().getIdPartida());
            pstmt.setString(2, instantanea.getEstadoActual());
            pstmt.setBoolean(3, instantanea.getTurnoActual());
            pstmt.setInt(4, instantanea.getTiempoBlancas());
            pstmt.setInt(5, instantanea.getTiempoNegras());
            pstmt.setString(6, Arrays.toString(instantanea.getUltimoMovimiento()));
            pstmt.setInt(7, instantanea.getContadorMovimientos());
            pstmt.setInt(8, instantanea.getCincuentaMovimientos());
        } catch (SQLException e) {
            System.out.println("Error al registrar instantanea: " + e.getMessage());
        }
    }

    public void eliminar(int idInstantanea) {
        sql = "DELETE FROM ajedrez.instantanea WHERE id_instantanea = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInstantanea); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar instantanea: " + e.getMessage());
        }
    }

    public List<Instantanea> mostrar() {
        List<Instantanea> instantaneas = new ArrayList<>();
        sql = "SELECT id_instantanea, id_partida, estado_actual, turno_actual, tiempo_blancas, tiempo_negras, ultimo_movimiento, contador_movimientos, cincuenta_movimientos FROM ajedrez.instantanea ORDER BY id_instantanea";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return instantaneas;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Instantanea instantanea = new Instantanea();
                instantanea.setIdInstantanea(rs.getInt("id_instantanea"));
                instantanea.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                instantanea.setEstadoActual(rs.getString("estado_actual"));
                instantanea.setTurnoActual(rs.getBoolean("turno_actual"));
                instantanea.setTiempoBlancas(rs.getInt("tiempo_blancas"));
                instantanea.setTiempoNegras(rs.getInt("tiempo_negras"));
                instantanea.setUltimoMovimiento(convertirMovimiento(rs.getString("ultimo_movimiento")));
                instantanea.setContadorMovimientos(rs.getInt("contador_movimientos"));
                instantanea.setCincuentaMovimientos(rs.getInt("cincuenta_movimientos"));
                instantaneas.add(instantanea);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar instantaneas guardadas: " + e.getMessage());
        }
        return instantaneas;
    }

    public void actualizar(Instantanea instantanea) {
        sql = "UPDATE ajedrez.instantanea SET estado_actual = ?, turno_actual = ?, tiempo_blancas = ?, tiempo_negras = ?, ultimo_movimiento = ?, contador_movimientos = ?, cincuenta_movimientos = ? WHERE id_instantanea = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instantanea.getEstadoActual());
            pstmt.setBoolean(2, instantanea.getTurnoActual());
            pstmt.setInt(3, instantanea.getTiempoBlancas());
            pstmt.setInt(4, instantanea.getTiempoNegras());
            pstmt.setString(5, Arrays.toString(instantanea.getUltimoMovimiento()));
            pstmt.setInt(6, instantanea.getContadorMovimientos());
            pstmt.setInt(7, instantanea.getCincuentaMovimientos());
            pstmt.setInt(8, instantanea.getIdInstantanea());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar instantanea: " + e.getMessage());
        }
    }

    public Instantanea buscar(int idInstantanea) {
        sql = "SELECT id_instantanea, id_partida, estado_actual, turno_actual, tiempo_blancas, tiempo_negras, ultimo_movimiento, contador_movimientos, cincuenta_movimientos FROM ajedrez.instantanea WHERE id_instantanea = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idInstantanea);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Instantanea instantanea = new Instantanea();
                instantanea.setIdInstantanea(rs.getInt("id_instantanea"));
                instantanea.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                instantanea.setEstadoActual(rs.getString("estado_actual"));
                instantanea.setTurnoActual(rs.getBoolean("turno_actual"));
                instantanea.setTiempoBlancas(rs.getInt("tiempo_blancas"));
                instantanea.setTiempoNegras(rs.getInt("tiempo_negras"));
                instantanea.setUltimoMovimiento(convertirMovimiento(rs.getString("ultimo_movimiento")));
                instantanea.setContadorMovimientos(rs.getInt("contador_movimientos"));
                instantanea.setCincuentaMovimientos(rs.getInt("cincuenta_movimientos")); 
                return instantanea;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar instantanea: " + e.getMessage());
        }
        return null;
    }

    public Instantanea buscarPorPartida(int idPartida) {
        sql = "SELECT id_instantanea, id_partida, estado_actual, turno_actual, tiempo_blancas, tiempo_negras, ultimo_movimiento, contador_movimientos, cincuenta_movimientos FROM ajedrez.instantanea WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Instantanea instantanea = new Instantanea();
                instantanea.setIdInstantanea(rs.getInt("id_instantanea"));
                instantanea.setPartida(partidaDao.buscar(rs.getInt("id_partida")));
                instantanea.setEstadoActual(rs.getString("estado_actual"));
                instantanea.setTurnoActual(rs.getBoolean("turno_actual"));
                instantanea.setTiempoBlancas(rs.getInt("tiempo_blancas"));
                instantanea.setTiempoNegras(rs.getInt("tiempo_negras"));
                instantanea.setUltimoMovimiento(convertirMovimiento(rs.getString("ultimo_movimiento")));
                instantanea.setContadorMovimientos(rs.getInt("contador_movimientos"));
                instantanea.setCincuentaMovimientos(rs.getInt("cincuenta_movimientos")); 
                return instantanea;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar instantanea: " + e.getMessage());
        }
        return null;
    }

    public void actualizarPorPartida(Instantanea instantanea) {
        sql = "UPDATE ajedrez.instantanea SET estado_actual = ?, turno_actual = ?, tiempo_blancas = ?, tiempo_negras = ?, ultimo_movimiento = ?, contador_movimientos = ?, cincuenta_movimientos = ? WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, instantanea.getEstadoActual());
            pstmt.setBoolean(2, instantanea.getTurnoActual());
            pstmt.setInt(3, instantanea.getTiempoBlancas());
            pstmt.setInt(4, instantanea.getTiempoNegras());
            pstmt.setString(6, Arrays.toString(instantanea.getUltimoMovimiento()));
            pstmt.setInt(6, instantanea.getContadorMovimientos());
            pstmt.setInt(7, instantanea.getCincuentaMovimientos());
            pstmt.setInt(8, instantanea.getPartida().getIdPartida());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar instantanea: " + e.getMessage());
        }
    }

    public void eliminarPorPartida(int idPartida) {
        sql = "DELETE FROM ajedrez.instantanea WHERE id_partida = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPartida); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar instantanea: " + e.getMessage());
        }
    }

    private int[] convertirMovimiento(String movimiento) {
        if (movimiento.isEmpty() || movimiento.equals("null")) {
            return null;
        }

        String[] valores = movimiento.replace("[", "").replace("]", "").split(", ");
        int[] resultado = new int[valores.length];
        for (int x = 0; x < valores.length; x++) {
            resultado[x] = Integer.parseInt(valores[x]);
        }
        return resultado;
    }
}
