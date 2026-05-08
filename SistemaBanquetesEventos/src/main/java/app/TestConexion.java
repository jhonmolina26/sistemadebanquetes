package app;

import conexion.ConexionBD;

public class TestConexion {
    public static void main(String[] args) {

        if (ConexionBD.conectar() != null) {
            System.out.println("✅ CONEXIÓN EXITOSA A MYSQL");
        } else {
            System.out.println("❌ NO CONECTA");
        }

    }
}
