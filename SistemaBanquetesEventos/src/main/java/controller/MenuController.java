    package controller;

import dao.MenuDAO;
import java.util.List;
import models.Menu;

public class MenuController {

    private final MenuDAO dao;  

    public MenuController() {
        this.dao = new MenuDAO();
    }

    public MenuController(MenuDAO dao) {
        this.dao = dao;
    }

    public List<Menu> obtenerTodos() {
        return dao.obtenerTodos();
    }

    public boolean guardarMenu(Menu menu, boolean esNuevo) {
        if (esNuevo) {
            return dao.insertar(menu);
        }
        return dao.actualizar(menu);
    }

    public boolean eliminarMenu(int id) {
        return dao.eliminar(id);
    }
}
