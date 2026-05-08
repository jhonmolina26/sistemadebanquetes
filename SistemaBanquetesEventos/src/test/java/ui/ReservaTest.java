package ui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ReservaTest {

    private Reserva reserva;

    @Before
    public void setUp() {
        reserva = new Reserva();
    }

    @Test
    public void testPrecioPorLugar() {
        assertEquals(25, reserva.precioPorLugar("Playa Tonsupa"), 0.01);
        assertEquals(40, reserva.precioPorLugar("Montañita"), 0.01);
        assertEquals(100, reserva.precioPorLugar("Isla Santa Cruz"), 0.01);
        assertEquals(35, reserva.precioPorLugar("Cotopaxi"), 0.01);
        assertEquals(20, reserva.precioPorLugar("ABC"), 0.01);
    }

    @Test
    public void testCalcularTotal() {
        assertEquals(50, reserva.calcularTotal("Playa Tonsupa", 2), 0.01);
        assertEquals(200, reserva.calcularTotal("Montañita", 5), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcularTotalPersonasInvalidas() {
        reserva.calcularTotal("Montañita", -1);
    }
}
