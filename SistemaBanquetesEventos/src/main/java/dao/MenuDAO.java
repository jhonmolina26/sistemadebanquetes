package dao;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Menu;

public class MenuDAO {

    public List<Menu> obtenerTodos() {
        List<Menu> lista = new ArrayList<>();
        String sql = "SELECT id, categoria, plato, paquete, restriccion, precio_pax FROM menus ORDER BY id";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(rs.getInt("id"));
                menu.setCategoria(rs.getString("categoria"));
                menu.setPlato(rs.getString("plato"));
                menu.setPaquete(rs.getString("paquete"));
                menu.setRestriccion(rs.getString("restriccion"));
                menu.setPrecioPax(rs.getBigDecimal("precio_pax"));
                lista.add(menu);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener menus: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Menu menu) {
        String sql = "INSERT INTO menus (categoria, plato, paquete, restriccion, precio_pax) VALUES (?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, menu.getCategoria());
            pstmt.setString(2, menu.getPlato());
            pstmt.setString(3, menu.getPaquete());
            pstmt.setString(4, menu.getRestriccion());
            pstmt.setBigDecimal(5, menu.getPrecioPax());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar menu: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Menu menu) {
        String sql = "UPDATE menus SET categoria=?, plato=?, paquete=?, restriccion=?, precio_pax=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, menu.getCategoria());
            pstmt.setString(2, menu.getPlato());
            pstmt.setString(3, menu.getPaquete());
            pstmt.setString(4, menu.getRestriccion());
            pstmt.setBigDecimal(5, menu.getPrecioPax());
            pstmt.setInt(6, menu.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar menu: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM menus WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar menu: " + e.getMessage());
            return false;
        }
    }
}
