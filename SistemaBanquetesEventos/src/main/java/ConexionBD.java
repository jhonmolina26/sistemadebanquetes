package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/viajero360?useSSL=false&serverTimezone=UTC",
                "root",
                ""
            );
        } catch (SQLException e) {
            System.out.println("❌ Error BD: " + e.getMessage());
            return null;
        }
    }
}
