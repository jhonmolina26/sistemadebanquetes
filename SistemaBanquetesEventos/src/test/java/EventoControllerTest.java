import controller.EventoController;
import models.Evento;
import java.sql.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventoControllerTest {

    private EventoController controller;

    @Before
    public void setUp() {
        controller = new EventoController();
    }

    // ================== guardarEvento (CC=2) ==================

    // CP1: Insertar nuevo evento (CORRECTO)
    @Test
    public void testGuardarEvento_InsertarCorrecto() {
        Evento ev = new Evento();
        ev.setCodigo("EVT-050");
        ev.setTipoEvento("Boda");
        ev.setFecha(Date.valueOf("2026-06-15"));
        ev.setSalonId(1);
        ev.setInvitados(100);
        ev.setHorario("18:00 - 01:00");
        ev.setPaquete("Premium");
        ev.setContacto("Juan Pérez");
        ev.setServicios("Catering, DJ");
        ev.setEstado("Confirmado");
        ev.setAnfitrionId(1);

        boolean resultado = controller.guardarEvento(ev, true);
        assertTrue("CP1 - Debería insertar correctamente", resultado);
        
        // Limpiar
        List<Evento> lista = controller.obtenerTodos();
        for (Evento e : lista) {
            if ("EVT-TEST-001".equals(e.getCodigo())) {
                controller.eliminarEvento(e.getId());
            }
        }
    }

    // CP2: Insertar con código duplicado (ERROR)
    @Test
    public void testGuardarEvento_InsertarDuplicado() {
        Evento ev1 = new Evento();
        ev1.setCodigo("EVT-DUP-001");
        ev1.setTipoEvento("Corporativo");
        ev1.setFecha(Date.valueOf("2026-07-20"));
        ev1.setSalonId(2);
        ev1.setInvitados(50);
        ev1.setHorario("09:00 - 13:00");
        ev1.setPaquete("Corporativo");
        ev1.setContacto("Ana López");
        ev1.setServicios("Coffee break");
        ev1.setEstado("En propuesta");
        ev1.setAnfitrionId(2);
        controller.guardarEvento(ev1, true);

        Evento ev2 = new Evento();
        ev2.setCodigo("EVT-DUP-001");
        ev2.setTipoEvento("Graduación");
        ev2.setFecha(Date.valueOf("2026-08-10"));
        ev2.setSalonId(3);
        ev2.setInvitados(200);
        ev2.setHorario("19:00 - 02:00");
        ev2.setPaquete("Tradicional");
        ev2.setContacto("Carlos Ruiz");
        ev2.setServicios("Decoración");
        ev2.setEstado("Pendiente anticipo");
        ev2.setAnfitrionId(3);

        boolean resultado = controller.guardarEvento(ev2, true);
        assertFalse("CP2 - Debería fallar por código duplicado", resultado);
        
        // Limpiar
        List<Evento> lista = controller.obtenerTodos();
        for (Evento e : lista) {
            if ("EVT-DUP-001".equals(e.getCodigo())) {
                controller.eliminarEvento(e.getId());
            }
        }
    }

    // CP3: Actualizar evento existente (CORRECTO)
    @Test
    public void testGuardarEvento_ActualizarCorrecto() {
        // Insertar nuevo
        Evento ev = new Evento();
        ev.setCodigo("EVT-UPD-001");
        ev.setTipoEvento("Cena privada");
        ev.setFecha(Date.valueOf("2026-09-01"));
        ev.setSalonId(4);
        ev.setInvitados(30);
        ev.setHorario("20:00 - 23:00");
        ev.setPaquete("Personalizado");
        ev.setContacto("María Gómez");
        ev.setServicios("Cena tres tiempos");
        ev.setEstado("En propuesta");
        ev.setAnfitrionId(1);
        controller.guardarEvento(ev, true);

        // Obtener ID real
        List<Evento> lista = controller.obtenerTodos();
        int idReal = -1;
        for (Evento e : lista) {
            if ("EVT-UPD-001".equals(e.getCodigo())) {
                idReal = e.getId();
                break;
            }
        }

        // Actualizar con ID real
        ev.setId(idReal);
        ev.setEstado("Confirmado");
        ev.setInvitados(50);

        boolean resultado = controller.guardarEvento(ev, false);
        assertTrue("CP3 - Debería actualizar correctamente", resultado);
        
        // Limpiar
        controller.eliminarEvento(idReal);
    }

    // CP4: Actualizar con id inexistente (ERROR)
    @Test
    public void testGuardarEvento_ActualizarInexistente() {
        Evento ev = new Evento();
        ev.setId(9999);
        ev.setCodigo("EVT-FALLO-001");
        ev.setTipoEvento("Boda");
        ev.setFecha(Date.valueOf("2026-10-10"));
        ev.setSalonId(1);
        ev.setInvitados(80);
        ev.setHorario("17:00 - 22:00");
        ev.setPaquete("Premium");
        ev.setContacto("Pedro Infante");
        ev.setServicios("Full");
        ev.setEstado("Confirmado");
        ev.setAnfitrionId(1);

        boolean resultado = controller.guardarEvento(ev, false);
        assertFalse("CP4 - Debería fallar por id inexistente", resultado);
    }

    // ================== eliminarEvento (CC=1) ==================

    // CP5: Eliminar evento existente (CORRECTO)
    @Test
    public void testEliminarEvento_Correcto() {
        // Insertar nuevo
        Evento ev = new Evento();
        ev.setCodigo("EVT-DEL-001");
        ev.setTipoEvento("Graduacion");
        ev.setFecha(Date.valueOf("2026-11-20"));
        ev.setSalonId(3);
        ev.setInvitados(150);
        ev.setHorario("19:00 - 1:00");
        ev.setPaquete("Tradicional");
        ev.setContacto("Luisa Fernanda");
        ev.setServicios("DJ, buffet");
        ev.setEstado("Pendiente anticipo");
        ev.setAnfitrionId(1);
        controller.guardarEvento(ev, true);

        // Obtener ID real
        List<Evento> lista = controller.obtenerTodos();
        int idReal = -1;
        for (Evento e : lista) {
            if ("EVT-DEL-001".equals(e.getCodigo())) {
                idReal = e.getId();
                break;
            }
        }

        // Eliminar con ID real
        boolean resultado = controller.eliminarEvento(idReal);
        assertTrue("CP5 - Debería eliminar correctamente", resultado);
    }

    // CP6: Eliminar con id inexistente (ERROR)
    @Test
    public void testEliminarEvento_Inexistente() {
        boolean resultado = controller.eliminarEvento(9999);
        assertFalse("CP6 - Debería fallar por id inexistente", resultado);
    }
}