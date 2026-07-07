package dao;

import conexion.ConexionBD;
import models.Anfitrion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AnfitrionDAO {

    public List<Anfitrion> obtenerTodos() {
        List<Anfitrion> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, empresa, documento, correo, telefono, segmento, vip, proximo_evento FROM anfitriones";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Anfitrion a = new Anfitrion(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("empresa"),
                    rs.getString("documento"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getString("segmento"),
                    rs.getBoolean("vip"),
                    rs.getString("proximo_evento")
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener anfitriones: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Anfitrion a) {
        String sql = "INSERT INTO anfitriones (nombre, empresa, documento, correo, telefono, segmento, vip, proximo_evento) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getEmpresa());
            pstmt.setString(3, a.getDocumento());
            pstmt.setString(4, a.getCorreo());
            pstmt.setString(5, a.getTelefono());
            pstmt.setString(6, a.getSegmento());
            pstmt.setBoolean(7, a.isVip());
            pstmt.setString(8, a.getProximoEvento());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Anfitrion a) {
        String sql = "UPDATE anfitriones SET nombre=?, empresa=?, documento=?, correo=?, telefono=?, segmento=?, vip=?, proximo_evento=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getEmpresa());
            pstmt.setString(3, a.getDocumento());
            pstmt.setString(4, a.getCorreo());
            pstmt.setString(5, a.getTelefono());
            pstmt.setString(6, a.getSegmento());
            pstmt.setBoolean(7, a.isVip());
            pstmt.setString(8, a.getProximoEvento());
            pstmt.setInt(9, a.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

public boolean eliminar(int id) {

    String sql = "DELETE FROM anfitriones WHERE id=?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, id);

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {

        if (e.getErrorCode() == 547) {

            JOptionPane.showMessageDialog(
                    null,
                    "No se puede eliminar el anfitrión porque tiene eventos asociados.",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al eliminar:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return false;
    }
}
    public boolean toggleVip(int id, boolean nuevoEstado) {
        String sql = "UPDATE anfitriones SET vip=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, nuevoEstado);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar VIP: " + e.getMessage());
            return false;
        }
    }
}