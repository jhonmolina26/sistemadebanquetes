package controller;

import dao.EventoDAO;
import models.Evento;
import java.util.List;
import models.Anfitrion;

public class EventoController {

    private EventoDAO dao;
    private AnfitrionController anfitrionController;

    public EventoController() {
        this.dao = new EventoDAO();
        this.anfitrionController = new AnfitrionController();
    }

    public List<Evento> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public boolean guardarEvento(Evento ev, boolean esNuevo) {
        if (esNuevo) {
            return dao.insertar(ev);
        } else {
            return dao.actualizar(ev);
        }
    }

    public boolean eliminarEvento(int id) {
        return dao.eliminar(id);
    }
    
    public List<Anfitrion> obtenerAnfitriones() {
        return anfitrionController.obtenerTodos();
    }
}