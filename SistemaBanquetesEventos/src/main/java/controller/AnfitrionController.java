package controller;

import dao.AnfitrionDAO;
import models.Anfitrion;
import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones
 * relacionadas con los anfitriones.
 */
public class AnfitrionController {

    /**
     * Objeto de acceso a datos para los anfitriones.
     */
    private final AnfitrionDAO dao;

    /**
     * Constructor de la clase AnfitrionController.
     */
    public AnfitrionController() {
        this.dao = new AnfitrionDAO();
    }

    /**
     * Obtiene todos los anfitriones registrados.
     *
     * @return lista de anfitriones registrados.
     */
    public List<Anfitrion> obtenerTodos() {
        return dao.obtenerTodos();
    }

    /**
     * Guarda un anfitrión nuevo o actualiza uno existente.
     *
     * @param a objeto anfitrión que será almacenado.
     * @param esNuevo indica si el anfitrión es nuevo o existente.
     * @return true si la operación fue exitosa.
     */
    public boolean guardarAnfitrion(
            Anfitrion a,
            boolean esNuevo) {

        if (esNuevo) {
            return dao.insertar(a);
        } else {
            return dao.actualizar(a);
        }
    }

    /**
     * Elimina un anfitrión utilizando su identificador.
     *
     * @param id identificador único del anfitrión.
     * @return true si la eliminación fue exitosa.
     */
    public boolean eliminarAnfitrion(int id) {
        return dao.eliminar(id);
    }

    /**
     * Cambia el estado VIP de un anfitrión.
     *
     * @param id identificador del anfitrión.
     * @param nuevoEstado nuevo estado VIP.
     * @return true si la actualización fue exitosa.
     */
    public boolean cambiarVip(
            int id,
            boolean nuevoEstado) {

        return dao.toggleVip(id, nuevoEstado);
    }
}