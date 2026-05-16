package controller;

import dao.MenuDAO;
import java.math.BigDecimal;
import models.Menu;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuControllerTest {

    @Test
    public void guardarMenu_debeInsertarCuandoEsNuevoYRetornarTrue() {
        MenuDAOFake daoFake = new MenuDAOFake(true, false, false);
        MenuController controller = new MenuController(daoFake);
        Menu menu = crearMenuBase();

        boolean resultado = controller.guardarMenu(menu, true);

        assertTrue(resultado);
        assertTrue(daoFake.insertarLlamado);
        assertFalse(daoFake.actualizarLlamado);
        assertSame(menu, daoFake.menuInsertado);
    }

    @Test
    public void guardarMenu_debeRetornarFalseCuandoActualizacionFalla() {
        MenuDAOFake daoFake = new MenuDAOFake(false, false, false);
        MenuController controller = new MenuController(daoFake);
        Menu menu = crearMenuBase();

        boolean resultado = controller.guardarMenu(menu, false);

        assertFalse(resultado);
        assertTrue(daoFake.actualizarLlamado);
        assertFalse(daoFake.insertarLlamado);
        assertSame(menu, daoFake.menuActualizado);
    }

    @Test
    public void eliminarMenu_debeRetornarTrueCuandoElDaoElimina() {
        MenuDAOFake daoFake = new MenuDAOFake(false, false, true);
        MenuController controller = new MenuController(daoFake);

        boolean resultado = controller.eliminarMenu(5);

        assertTrue(resultado);
        assertTrue(daoFake.eliminarLlamado);
        assertEquals(5, daoFake.idEliminado);
    }

    @Test
    public void eliminarMenu_debeRetornarFalseCuandoElDaoNoElimina() {
        MenuDAOFake daoFake = new MenuDAOFake(false, false, false);
        MenuController controller = new MenuController(daoFake);

        boolean resultado = controller.eliminarMenu(999);

        assertFalse(resultado);
        assertTrue(daoFake.eliminarLlamado);
        assertEquals(999, daoFake.idEliminado);
    }

    private Menu crearMenuBase() {
        Menu menu = new Menu();
        menu.setId(1);
        menu.setCategoria("Entradas");
        menu.setPlato("Bruschetta");
        menu.setPaquete("Ejecutivo");
        menu.setRestriccion("Sin gluten");
        menu.setPrecioPax(new BigDecimal("8.50"));
        return menu;
    }

    private static class MenuDAOFake extends MenuDAO {
        private final boolean retornoInsertar;
        private final boolean retornoActualizar;
        private final boolean retornoEliminar;

        private boolean insertarLlamado;
        private boolean actualizarLlamado;
        private boolean eliminarLlamado;

        private Menu menuInsertado;
        private Menu menuActualizado;
        private int idEliminado;

        MenuDAOFake(boolean retornoInsertar, boolean retornoActualizar, boolean retornoEliminar) {
            this.retornoInsertar = retornoInsertar;
            this.retornoActualizar = retornoActualizar;
            this.retornoEliminar = retornoEliminar;
        }

        @Override
        public boolean insertar(Menu menu) {
            insertarLlamado = true;
            menuInsertado = menu;
            return retornoInsertar;
        }

        @Override
        public boolean actualizar(Menu menu) {
            actualizarLlamado = true;
            menuActualizado = menu;
            return retornoActualizar;
        }

        @Override
        public boolean eliminar(int id) {
            eliminarLlamado = true;
            idEliminado = id;
            return retornoEliminar;
        }
    }
}
