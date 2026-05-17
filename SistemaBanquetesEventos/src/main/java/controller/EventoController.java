package controller;

import dao.EventoDAO;
import java.sql.Date;
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

    // Método para actualizar solo los campos permitidos
    public boolean actualizarCamposPermitidos(Evento ev) {
        // Primero verificamos que el evento no esté bloqueado
        List<Evento> todos = dao.obtenerTodos();
        Evento existente = null;
        for (Evento e : todos) {
            if (e.getId() == ev.getId()) {
                existente = e;
                break;
            }
        }

        if (existente != null && "Bloqueado".equals(existente.getEstado())) {
            return false; // No podemos modificar un evento bloqueado
        }

        return dao.actualizar(ev);
    }
    
    public boolean verificarDisponibilidadSalon(int salonId, Date fecha, String horario, int eventoIdExcluir) {
        return dao.verificarDisponibilidadSalon(salonId, fecha, horario, eventoIdExcluir);
    }
    
    public List<Evento> buscar(String tipoEvento, String fecha, String estado) {
        return dao.buscar(tipoEvento, fecha, estado);
    }
}