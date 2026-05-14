package dao;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Pago;

public class PagoDAO {

    public List<Pago> obtenerTodos() {
        List<Pago> lista = new ArrayList<>();
       
        String sql = "SELECT p.id, p.evento_id, e.codigo AS codigo_evento, p.total, "
                   + "p.anticipo, p.saldo, p.factura, p.metodo, p.estado "
                   + "FROM pagos p INNER JOIN eventos e ON p.evento_id = e.id ORDER BY p.id";
                   
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pago pago = new Pago();
                pago.setId(rs.getInt("id"));
                pago.setEventoId(rs.getInt("evento_id"));
                pago.setCodigoEvento(rs.getString("codigo_evento"));
                pago.setTotal(rs.getBigDecimal("total"));
                pago.setAnticipo(rs.getBigDecimal("anticipo"));
                pago.setSaldo(rs.getBigDecimal("saldo")); // Lo leemos, porque SQL Server lo calculó
                pago.setFactura(rs.getString("factura"));
                pago.setMetodo(rs.getString("metodo"));
                pago.setEstado(rs.getString("estado"));
                lista.add(pago);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Pago pago) {
       
        String sql = "INSERT INTO pagos (evento_id, total, anticipo, factura, metodo, estado) VALUES (?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, pago.getEventoId());
            pstmt.setBigDecimal(2, pago.getTotal());
            pstmt.setBigDecimal(3, pago.getAnticipo());
            pstmt.setString(4, pago.getFactura());
            pstmt.setString(5, pago.getMetodo());
            pstmt.setString(6, pago.getEstado());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Pago pago) {
      
        String sql = "UPDATE pagos SET evento_id=?, total=?, anticipo=?, factura=?, metodo=?, estado=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, pago.getEventoId());
            pstmt.setBigDecimal(2, pago.getTotal());
            pstmt.setBigDecimal(3, pago.getAnticipo());
            pstmt.setString(4, pago.getFactura());
            pstmt.setString(5, pago.getMetodo());
            pstmt.setString(6, pago.getEstado());
            pstmt.setInt(7, pago.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pagos WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
            return false;
        }
    }
}