package ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class PagosTest {

    private Pagos pagos;

    @Before
    public void setUp() {
        pagos = new Pagos();
    }

    @Test
    public void testAddLugar() {
        // Verificar que la provincia "Esmeraldas" tenga los lugares cargados correctamente
        int count = pagos.testCantidadLugaresEnProvincia("Esmeraldas");

        // Según tu código original, Esmeraldas tiene 2 lugares
        assertEquals(2, count);
    }

    @Test
    public void testCargarLugares() throws Exception {
        // Simulamos seleccionar la provincia
        pagos.cbProvincia.setSelectedItem("Manabí");

        // Ejecutamos cargarLugares como lo hace la interfaz
        pagos.cbLugar.removeAllItems();
        pagos.cbProvincia.getActionListeners()[0].actionPerformed(null);

        // Obtenemos los lugares desde el mapa
        List<String> lugares = pagos.testObtenerLugaresDeProvincia("Manabí");

        // Validamos que los lugares existan
        assertTrue(lugares.contains("Montañita"));
        assertTrue(lugares.contains("Bahía de Caráquez"));
    }
}
