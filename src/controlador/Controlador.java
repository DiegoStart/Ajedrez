package controlador;

import java.util.List;

import modelo.Instantanea;
import modelo.Jugador;
import modelo.Movimiento;
import modelo.Participacion;
import modelo.Partida;
import modelo.Usuario;
import persistencia.dao.InstantaneaDao;
import persistencia.dao.JugadorDao;
import persistencia.dao.MovimientoDao;
import persistencia.dao.ParticipacionDao;
import persistencia.dao.PartidaDao;
import persistencia.dao.UsuarioDao;

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

    public void registrarInstantanea(Instantanea instantanea) {
        instantaneaDao.registrar(instantanea);
    }

    public void registrarJugador(Jugador jugador) {
        jugadorDao.registrar(jugador);
    }

    public void registrarMovimiento(Movimiento movimiento) {
        movimientoDao.registrar(movimiento);
    }

    public void registrarParticipacion(Participacion participacion) {
        participacionDao.registrar(participacion);
    }

    public void registrarPartida(Partida partida) {
        partidaDao.registrar(partida);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarioDao.registrar(usuario);
    }

    public void eliminarInstantanea(int idInstantanea) {
        instantaneaDao.eliminar(idInstantanea);
    }

    public void eliminarJugador(int idJugador) {
        jugadorDao.eliminar(idJugador);
    }

    public void eliminarMovimiento(int idMovimiento) {
        movimientoDao.eliminar(idMovimiento);
    }

    public void eliminarParticipacion(int idParticipacion) {
        participacionDao.eliminar(idParticipacion);
    }

    public void eliminarPartida(int idPartida) {
        partidaDao.eliminar(idPartida);
    }

    public void eliminarUsuario(int idUsuario) {
        usuarioDao.eliminar(idUsuario);
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

    public void actualizarInstantanea(Instantanea instantanea) {
        instantaneaDao.actualizar(instantanea);
    }

    public void actualizarJugador(Jugador jugador) {
        jugadorDao.actualizar(jugador);
    }

    public void actualizarParticipacion(Participacion participacion) {
        participacionDao.actualizar(participacion);
    }

    public void actualizarPartida(Partida partida) {
        partidaDao.actualizar(partida);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDao.actualizar(usuario);
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

    public void actualizarPorPartidaInstantanea(Instantanea instantanea) {
        instantaneaDao.actualizarPorPartida(instantanea);
    }

    public void eliminarPorPartidaInstantanea(int idPartida) {
        instantaneaDao.eliminarPorPartida(idPartida);
    }
    //Metodos unicos de JugadorDao
    public List<Jugador> buscarPorUsuarioJugador(int idUsuario) {
        return jugadorDao.buscarPorUsuario(idUsuario);
    }

    public void actualizarElo(int idJugador, int elo) {
        jugadorDao.actualizarElo(idJugador, elo);
    }

    public void actualizarEstadisticas(Jugador jugador) {
        jugadorDao.actualizarEstadisticas(jugador);
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

    public void eliminarPorPartidaMovimiento(int idPartida) {
        movimientoDao.eliminarPorPartida(idPartida);
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

    public void actualizarResultadoParticipacion(Participacion participacion) {
        participacionDao.actualizarResultado(participacion);
    }

    public void actualizarTiempoRestanteParticipacion(Participacion participacion) {
        participacionDao.actualizarTiempoRestante(participacion);
    }
    //Metodos unicos de PartidaDao
    public List<Partida> buscarPorEstadoPartida(String estado) {
        return partidaDao.buscarPorEstado(estado);
    }

    public List<Partida> buscarPorJugadorPartida(int idJugador) {
        return partidaDao.buscarPorJugador(idJugador);
    }

    public void finalizarPartidaPartida(Partida partida) {
        partidaDao.finalizarPartida(partida);
    }

    public void actualizarEstadoPartida(int idPartida, String estado) {
        partidaDao.actualizarEstado(idPartida, estado);
    }

    public List<Partida> buscarPartidasEnCurso() {
        return partidaDao.buscarPorEstado("EN_CURSO");
    }

    public List<Partida> buscarPartidasEnPausa() {
        return partidaDao.buscarPorEstado("EN_PAUSA");
    }
    //Metodos unicos de UsuarioDao
    public Usuario buscarPorCorreoUsuario(String correo) {
        return usuarioDao.buscarPorNombreUsuario(correo);
    }

    public Usuario buscarPorNombreUsuarioUsuario(String nombreUsuario) {
        return usuarioDao.buscarPorCorreo(nombreUsuario);
    }

    public void actualizarUltimoAccesoUsuario(int idUsuario) {
        usuarioDao.actualizarUltimoAcceso(idUsuario);
    }
}
