package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private String genero;
    private String fotoPerfil;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    private String estado;

    public Usuario() {
        estado = "ACTIVO";
        ultimoAcceso = LocalDateTime.now();
    }

    public int getIdUsuario() { 
        return idUsuario; 
    }

    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }

    public String getNombreUsuario() { 
        return nombreUsuario; 
    }

    public void setNombreUsuario(String nombreUsuario) { 
        this.nombreUsuario = nombreUsuario; 
    }

    public String getCorreo() { 
        return correo; 
    }

    public void setCorreo(String correo) { 
        this.correo = correo; 
    }

    public String getContrasena() { 
        return contrasena; 
    }

    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }

    public LocalDate getFechaNacimiento() { 
        return fechaNacimiento; 
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }

    public String getGenero() { 
        return genero; 
    }

    public void setGenero(String genero) { 
        this.genero = genero; 
    }

    public String getFotoPerfil() { 
        return fotoPerfil; 
    }

    public void setFotoPerfil(String fotoPerfil) { 
        this.fotoPerfil = fotoPerfil; 
    }

    public LocalDateTime getFechaRegistro() { 
        return fechaRegistro; 
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) { 
        this.fechaRegistro = fechaRegistro; 
    }

    public LocalDateTime getUltimoAcceso() { 
        return ultimoAcceso; 
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { 
        this.ultimoAcceso = ultimoAcceso; 
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}