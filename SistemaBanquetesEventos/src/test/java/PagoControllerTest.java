package controller;

import java.math.BigDecimal;
import java.util.List;
import models.Pago;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PagoControllerTest {
    
    public PagoControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void testObtenerTodos() {
        System.out.println("Test: obtenerTodos");
        PagoController instance = new PagoController();
        List<Pago> result = instance.obtenerTodos();
        assertNotNull("La lista de pagos no debe ser nula", result);
    }
    @Test
    public void testGuardarPago_Correcto() {
        System.out.println("Test: guardarPago (CAMINO CORRECTO)");
        
        Pago pago = new Pago();
        pago.setEventoId(1); 
        pago.setCodigoEvento("EVT-TEST");
        pago.setTotal(new BigDecimal("150.00"));
        pago.setAnticipo(new BigDecimal("100.00"));
        pago.setFactura("FAC-TEST-001");
        pago.setMetodo("Efectivo");
        pago.setEstado("Pendiente");
        
        boolean esNuevo = true;
        PagoController instance = new PagoController();
        boolean result = instance.guardarPago(pago, esNuevo);
        
        assertTrue("El test fallo. Revisa que el EventoId exista en tu base de datos.", result);
    }

    @Test
    public void testGuardarPago_Error() {
        System.out.println("Test: guardarPago (CAMINO ERROR )");
        
        Pago pago = new Pago();
        pago.setEventoId(-999); 
        pago.setAnticipo(new BigDecimal("100.00"));
        
        boolean esNuevo = true;
        PagoController instance = new PagoController();
        boolean result = instance.guardarPago(pago, esNuevo);
        assertTrue("El test falló a propósito porque el ID del evento no existe.", result);
    }


    @Test
    public void testEliminarPago_Correcto() {
        System.out.println("Test: eliminarPago");
        PagoController instance = new PagoController();
        
        List<Pago> lista = instance.obtenerTodos();
        
        int idDinamico = lista.get(lista.size() - 1).getId(); 
        
        boolean result = instance.eliminarPago(idDinamico);
        
        assertTrue("Debería retornar true al eliminar", result);
    }
    
    @Test
    public void testEliminarPago_Error() {
        System.out.println("Test: eliminarPago (CAMINO ERROR)");
        
        int id = -500; 
        PagoController instance = new PagoController();
        boolean result = instance.eliminarPago(id);

        assertTrue("El test fallo a propósito porque no encontro el ID para borrar", result);
    }
}