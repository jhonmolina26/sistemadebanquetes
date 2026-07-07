import controller.AnfitrionController;
import models.Anfitrion;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnfitrionControllerTest {

    @Test
    public void testGuardarAnfitrionCorrecto() {

        System.out.println("Test: guardarAnfitrion CORRECTO");

        AnfitrionController controller =
                new AnfitrionController();

        Anfitrion a = new Anfitrion();

        a.setId(5);
        a.setNombre("hector");
        a.setEmpresa("sarcoscorps");
        a.setDocumento("0956204388");
        a.setCorreo("hector@gmail.com");
        a.setTelefono("0961923897");
        a.setSegmento("Institucional");
        a.setVip(false);
        a.setProximoEvento("Evento actualizado");

        boolean resultado =
                controller.guardarAnfitrion(a, false);

        System.out.println("Resultado: " + resultado);

        assertTrue(
                "El anfitrión debería actualizarse correctamente",
                resultado
        );
    }

    @Test
    public void testGuardarAnfitrionError() {

        System.out.println("Test: guardarAnfitrion ERROR");

        AnfitrionController controller =
                new AnfitrionController();

        boolean resultado =
                controller.guardarAnfitrion(null, true);

        System.out.println("Resultado: " + resultado);

        assertTrue(
                "La prueba debe fallar al enviar null",
                resultado
        );
    }

    @Test
    public void testCambiarVipCorrecto() {

        System.out.println("Test: cambiarVip CORRECTO");

        AnfitrionController controller =
                new AnfitrionController();

        boolean resultado =
                controller.cambiarVip(5, true);

        System.out.println("Resultado: " + resultado);

        assertTrue(
                "El estado VIP debería cambiar correctamente",
                resultado
        );
    }

    @Test
    public void testCambiarVipError() {

        System.out.println("Test: cambiarVip ERROR");

        AnfitrionController controller =
                new AnfitrionController();

        boolean resultado =
                controller.cambiarVip(-1, true);

        System.out.println("Resultado: " + resultado);

        assertTrue(
                "La prueba debe fallar por ID inválido",
                resultado
        );
    }
}