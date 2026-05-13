package controller;

import dao.AnfitrionDAO;
import models.Anfitrion;
import java.util.List;

public class AnfitrionController {

    private AnfitrionDAO dao;

    public AnfitrionController() {
        this.dao = new AnfitrionDAO();
    }

    // Obtiene todos los anfitriones desde la BD
    public List<Anfitrion> obtenerTodos() {
        return dao.obtenerTodos();
    }

    // Guarda un anfitrión nuevo o actualiza uno existente.
    // Retorna true si tuvo éxito, false si hubo error.
    public boolean guardarAnfitrion(Anfitrion a, boolean esNuevo) {
        if (esNuevo) {
            return dao.insertar(a);
        } else {
            return dao.actualizar(a);
        }
    }

    // Elimina un anfitrión por su id
    public boolean eliminarAnfitrion(int id) {
        return dao.eliminar(id);
    }

    // Cambia el estado VIP de un anfitrión
    public boolean cambiarVip(int id, boolean nuevoEstado) {
        return dao.toggleVip(id, nuevoEstado);
    }
}