package controller;

import dao.EventoDAO;
import models.Evento;
import java.util.List;

public class EventoController {

    private EventoDAO dao;

    public EventoController() {
        this.dao = new EventoDAO();
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
}