
import model.Salon;
import org.junit.Test;
import static org.junit.Assert.*;

public class SalonTest {

    @Test
    public void testCrearSalon() {

        Salon salon = new Salon();

        assertNotNull(salon);
    }

}