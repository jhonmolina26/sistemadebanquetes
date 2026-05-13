package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = 
        "jdbc:sqlserver://localhost;databaseName=Banquetes360;user=banquetes_user;password=1234;encrypt=true;trustServerCertificate=true;";

    public static Connection conectar() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}