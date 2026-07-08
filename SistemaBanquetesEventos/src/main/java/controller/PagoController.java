package controller;

import dao.PagoDAO;
import java.util.List;
import models.Evento;
import models.Pago;

public class PagoController {

    private final PagoDAO dao;
    private final EventoController eventoController;

    public PagoController() {
        this.dao = new PagoDAO();
        this.eventoController = new EventoController();
    }

    public List<Pago> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public List<Evento> obtenerContrataciones() {
        return eventoController.obtenerTodos();
    }

    public boolean guardarPago(Pago pago, boolean esNuevo) {
        if (esNuevo) {
            return dao.insertar(pago);
        }
        return dao.actualizar(pago);
    }

    public boolean eliminarPago(int id) {
        return dao.eliminar(id);
    }
}
