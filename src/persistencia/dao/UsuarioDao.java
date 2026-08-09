package persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import modelo.Usuario;
import persistencia.Conexion;

public class UsuarioDao {
    private Conexion conexion;
    private String sql;

    public UsuarioDao() {
        conexion = new Conexion();
    }

    public void registrar(Usuario usuario) {
        sql = "INSERT INTO ajedrez.usuario(nombre_usuario, correo, contrasena, fecha_nacimiento, genero, foto_perfil, ultimo_acceso, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            if (usuario.getFechaNacimiento() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(usuario.getFechaNacimiento()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            pstmt.setString(5, usuario.getGenero());
            pstmt.setString(6, usuario.getFotoPerfil());
            pstmt.setTimestamp(7, Timestamp.valueOf(usuario.getUltimoAcceso()));
            pstmt.setString(8, usuario.getEstado());
            if (pstmt.executeUpdate() > 0) {
                System.out.println("Usuario registrado con éxito.");
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    public void eliminar(int idUsuario) {
        sql = "DELETE FROM ajedrez.usuario WHERE id_usuario = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public List<Usuario> mostrar() {
        List<Usuario> usuarios = new ArrayList<>();
        sql = "SELECT id_usuario, nombre_usuario, correo, contrasena, fecha_nacimiento, genero, foto_perfil, fecha_registro, ultimo_acceso, estado FROM ajedrez.usuario ORDER BY id_usuario";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return usuarios;
        }

        try (conn; Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                if (rs.getDate("fecha_nacimiento") != null) {
                    usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                }
                usuario.setGenero(rs.getString("genero"));
                usuario.setFotoPerfil(rs.getString("foto_perfil"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso").toLocalDateTime());
                usuario.setEstado(rs.getString("estado"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar usuarios guardados: " + e.getMessage());
        }
        return usuarios;
    }

    public void actualizar(Usuario usuario) {
        sql = "UPDATE ajedrez.usuario SET nombre_usuario = ?, correo = ?, contrasena = ?, fecha_nacimiento = ?, genero = ?, foto_perfil = ?, ultimo_acceso = ?, estado = ? WHERE id_usuario = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            if (usuario.getFechaNacimiento() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(usuario.getFechaNacimiento()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            pstmt.setString(5, usuario.getGenero());
            pstmt.setString(6, usuario.getFotoPerfil());
            pstmt.setTimestamp(7, Timestamp.valueOf(usuario.getUltimoAcceso()));
            pstmt.setString(8, usuario.getEstado());
            pstmt.setInt(9, usuario.getIdUsuario());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public Usuario buscar(int idUsuario) {
        sql = "SELECT id_usuario, nombre_usuario, correo, contrasena, fecha_nacimiento, genero, foto_perfil, fecha_registro, ultimo_acceso, estado FROM ajedrez.usuario WHERE id_usuario = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                if (rs.getDate("fecha_nacimiento") != null) {
                    usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                }
                usuario.setGenero(rs.getString("genero"));
                usuario.setFotoPerfil(rs.getString("foto_perfil"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso").toLocalDateTime());
                usuario.setEstado(rs.getString("estado"));
                return usuario;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarPorCorreo(String correo) {
        sql = "SELECT id_usuario, nombre_usuario, correo, contrasena, fecha_nacimiento, genero, foto_perfil, fecha_registro, ultimo_acceso, estado FROM ajedrez.usuario WHERE correo = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                if (rs.getDate("fecha_nacimiento") != null) {
                    usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                }
                usuario.setGenero(rs.getString("genero"));
                usuario.setFotoPerfil(rs.getString("foto_perfil"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso").toLocalDateTime());
                usuario.setEstado(rs.getString("estado"));
                return usuario;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por correo: " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        sql = "SELECT id_usuario, nombre_usuario, correo, contrasena, fecha_nacimiento, genero, foto_perfil, fecha_registro, ultimo_acceso, estado FROM ajedrez.usuario WHERE nombre_usuario = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return null;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setContrasena(rs.getString("contrasena"));
                if (rs.getDate("fecha_nacimiento") != null) {
                    usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                }
                usuario.setGenero(rs.getString("genero"));
                usuario.setFotoPerfil(rs.getString("foto_perfil"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso").toLocalDateTime());
                usuario.setEstado(rs.getString("estado"));
                return usuario;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario por nombre: " + e.getMessage());
        }
        return null;
    }

    public void actualizarUltimoAcceso(int idUsuario) {
        sql = "UPDATE ajedrez.usuario SET ultimo_acceso = ? WHERE id_usuario = ?";
        Connection conn = conexion.establecerConexion();
        if (conn == null) {
            return;
        }

        try (conn; PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar ultimo acceso: " + e.getMessage());
        }
    }
}
