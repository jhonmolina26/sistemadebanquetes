package app;

import conexion.ConexionBD;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DE CONEXIÓN ===");
        
        try {
            if (ConexionBD.conectar() != null) {
                System.out.println("✅ CONEXIÓN EXITOSA A SQL SERVER");
            } else {
                System.out.println("❌ NO CONECTA (retornó null)");
            }
        } catch (Exception e) {
            System.out.println("❌ EXCEPCIÓN: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== FIN ===");
    }
}