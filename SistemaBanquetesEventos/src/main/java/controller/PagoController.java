package controller;

import dao.PagoDAO;
import java.util.List;
import models.Pago;

public class PagoController {

    private final PagoDAO dao;

    public PagoController() {
        this.dao = new PagoDAO();
    }

    public List<Pago> obtenerTodos() {
        return dao.obtenerTodos();
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