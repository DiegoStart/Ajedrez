package controlador;

import java.util.List;

import modelo.*;
import persistencia.dao.*;

public class Controlador {
    private InstantaneaDao instantaneaDao;
    private JugadorDao jugadorDao;
    private MovimientoDao movimientoDao;
    private ParticipacionDao participacionDao;
    private PartidaDao partidaDao;
    private UsuarioDao usuarioDao;

    public Controlador() {
        instantaneaDao = new InstantaneaDao();
        jugadorDao = new JugadorDao();
        movimientoDao = new MovimientoDao();
        participacionDao = new ParticipacionDao();
        partidaDao = new PartidaDao();
        usuarioDao = new UsuarioDao();
    }

    public boolean registrarInstantanea(Instantanea instantanea) {
        return instantaneaDao.registrar(instantanea);
    }

    public boolean registrarJugador(Jugador jugador) {
        return jugadorDao.registrar(jugador);
    }

    public boolean registrarMovimiento(Movimiento movimiento) {
        return movimientoDao.registrar(movimiento);
    }

    public boolean registrarParticipacion(Participacion participacion) {
        return participacionDao.registrar(participacion);
    }

    public boolean registrarPartida(Partida partida) {
        return partidaDao.registrar(partida);
    }

    public boolean registrarUsuario(Usuario usuario) {
        return usuarioDao.registrar(usuario);
    }

    public boolean eliminarInstantanea(int idInstantanea) {
        return instantaneaDao.eliminar(idInstantanea);
    }

    public boolean eliminarJugador(int idJugador) {
        return jugadorDao.eliminar(idJugador);
    }

    public boolean eliminarMovimiento(int idMovimiento) {
        return movimientoDao.eliminar(idMovimiento);
    }

    public boolean eliminarParticipacion(int idParticipacion) {
        return participacionDao.eliminar(idParticipacion);
    }

    public boolean eliminarPartida(int idPartida) {
        return partidaDao.eliminar(idPartida);
    }

    public boolean eliminarUsuario(int idUsuario) {
        return usuarioDao.eliminar(idUsuario);
    }

    public List<Instantanea> mostrarInstantaneas() {
        return instantaneaDao.mostrar();
    }

    public List<Jugador> mostrarJugadores() {
        return jugadorDao.mostrar();
    }

    public List<Movimiento> mostrarMovimientos() {
        return movimientoDao.mostrar();
    }

    public List<Participacion> mostrarParticipaciones() {
        return participacionDao.mostrar();
    }

    public List<Partida> mostrarPartidas() {
        return partidaDao.mostrar();
    }

    public List<Usuario> mostrarUsuarios() {
        return usuarioDao.mostrar();
    }

    public boolean actualizarInstantanea(Instantanea instantanea) {
        return instantaneaDao.actualizar(instantanea);
    }

    public boolean actualizarJugador(Jugador jugador) {
        return jugadorDao.actualizar(jugador);
    }

    public boolean actualizarParticipacion(Participacion participacion) {
        return participacionDao.actualizar(participacion);
    }

    public boolean actualizarPartida(Partida partida) {
        return partidaDao.actualizar(partida);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        return usuarioDao.actualizar(usuario);
    }

    public Instantanea buscarInstantanea(int idInstantanea) {
        return instantaneaDao.buscar(idInstantanea);
    }

    public Jugador buscarJugador(int idJugador) {
        return jugadorDao.buscar(idJugador);
    }

    public Movimiento buscarMovimiento(int idMovimiento) {
        return movimientoDao.buscar(idMovimiento);
    }

    public Participacion buscarParticipacion(int idParticipacion) {
        return participacionDao.buscar(idParticipacion);
    }

    public Partida buscarPartida(int idPartida) {
        return partidaDao.buscar(idPartida);
    }

    public Usuario buscarUsuario(int idUsuario) {
        return usuarioDao.buscar(idUsuario);
    }
    //Metodos unicos de InstantaneaDao
    public Instantanea buscarPorPartidaInstantanea(int idPartida) {
        return instantaneaDao.buscarPorPartida(idPartida);
    }

    public boolean actualizarPorPartidaInstantanea(Instantanea instantanea) {
        return instantaneaDao.actualizarPorPartida(instantanea);
    }

    public boolean eliminarPorPartidaInstantanea(int idPartida) {
        return instantaneaDao.eliminarPorPartida(idPartida);
    }
    //Metodos unicos de JugadorDao
    public List<Jugador> buscarPorUsuarioJugador(int idUsuario) {
        return jugadorDao.buscarPorUsuario(idUsuario);
    }

    public boolean actualizarElo(int idJugador, int elo) {
        return jugadorDao.actualizarElo(idJugador, elo);
    }

    public boolean actualizarEstadisticas(Jugador jugador) {
        return jugadorDao.actualizarEstadisticas(jugador);
    }
    //Metodos unicos de MovimientoDao
    public List<Movimiento> buscarPorPartidaMovimiento(int idPartida) {
        return movimientoDao.buscarPorPartida(idPartida);
    }

    public Movimiento buscarUltimoMovimientoMovimiento(int idPartida) {
        return movimientoDao.buscarUltimoMovimiento(idPartida);
    }

    public Movimiento buscarPorNumeroMovimientoMovimiento(int idPartida, int numeroMovimiento) {
        return movimientoDao.buscarPorNumeroMovimiento(idPartida, numeroMovimiento);
    }

    public int contarMovimientosMovimiento(int idPartida) {
        return movimientoDao.contarMovimientos(idPartida);
    }

    public boolean eliminarPorPartidaMovimiento(int idPartida) {
        return movimientoDao.eliminarPorPartida(idPartida);
    }
    //Metodos unicos de ParticipacionDao
    public List<Participacion> buscarPorPartidaParticipacion(int idPartida) {
        return participacionDao.buscarPorPartida(idPartida);
    }

    public List<Participacion> buscarPorJugadorParticipacion(int idJugador) {
        return participacionDao.buscarPorJugador(idJugador);
    }

    public Participacion buscarPorPartidaYColorParticipacion(int idPartida, boolean color) {
        return participacionDao.buscarPorPartidaYColor(idPartida, color);
    }

    public int contarParticipantesParticipacion(int idPartida) {
        return participacionDao.contarParticipantes(idPartida);
    }

    public boolean actualizarResultadoParticipacion(Participacion participacion) {
        return participacionDao.actualizarResultado(participacion);
    }

    public boolean actualizarTiempoRestanteParticipacion(Participacion participacion) {
        return participacionDao.actualizarTiempoRestante(participacion);
    }
    //Metodos unicos de PartidaDao
    public List<Partida> buscarPorEstadoPartida(String estado) {
        return partidaDao.buscarPorEstado(estado);
    }

    public List<Partida> buscarPorJugadorPartida(int idJugador) {
        return partidaDao.buscarPorJugador(idJugador);
    }

    public boolean finalizarPartidaPartida(Partida partida) {
        return partidaDao.finalizarPartida(partida);
    }

    public boolean actualizarEstadoPartida(int idPartida, String estado) {
        return partidaDao.actualizarEstado(idPartida, estado);
    }

    public List<Partida> buscarPartidasEnCurso() {
        return partidaDao.buscarPorEstado("EN_CURSO");
    }

    public List<Partida> buscarPartidasEnPausa() {
        return partidaDao.buscarPorEstado("EN_PAUSA");
    }

    public List<Partida> buscarPartidasFinalizada() {
        return partidaDao.buscarPorEstado("FINALIZADA");
    }
    //Metodos unicos de UsuarioDao
    public Usuario buscarPorCorreoUsuario(String correo) {
        return usuarioDao.buscarPorCorreo(correo);
    }

    public Usuario buscarPorNombreUsuarioUsuario(String nombreUsuario) {
        return usuarioDao.buscarPorNombreUsuario(nombreUsuario);
    }

    public boolean buscarUsuarioExisteUsuario(String nombreUsuario) {
        return usuarioDao.buscarUsuarioExiste(nombreUsuario);
    }

    public boolean buscarCorreoExisteUsuario(String correo) {
        return usuarioDao.buscarCorreoExiste(correo);
    }

    public boolean actualizarUltimoAccesoUsuario(int idUsuario) {
        return usuarioDao.actualizarUltimoAcceso(idUsuario);
    }
    //Eliminar Todo
    public void eliminarTodo() {
        partidaDao.eliminarTodo();
    }
}
