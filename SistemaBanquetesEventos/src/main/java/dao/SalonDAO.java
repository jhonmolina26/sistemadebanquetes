package dao;

import conexion.ConexionBD;
import model.Salon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalonDAO {

    // ── Listar todos ──────────────────────────────────────────────────────────
    public List<Salon> listar() {
        List<Salon> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capacidad, ubicacion, montaje_ideal, estado FROM salones ORDER BY nombre";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("SalonDAO.listar: " + e.getMessage());
        }
        return lista;
    }

    // ── Listar disponibles ────────────────────────────────────────────────────
    public List<Salon> listarDisponibles() {
        List<Salon> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capacidad, ubicacion, montaje_ideal, estado "
                   + "FROM salones WHERE estado = 'Disponible' ORDER BY nombre";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("SalonDAO.listarDisponibles: " + e.getMessage());
        }
        return lista;
    }

    // ── Insertar ──────────────────────────────────────────────────────────────
    public boolean insertar(Salon s) {
        String sql = "INSERT INTO salones (nombre, capacidad, ubicacion, montaje_ideal, estado) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setInt   (2, s.getCapacidad());
            ps.setString(3, s.getUbicacion());
            ps.setString(4, s.getTipoMontajePrincipal());
            ps.setString(5, s.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SalonDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    // ── Actualizar ────────────────────────────────────────────────────────────
    public boolean actualizar(Salon s) {
        String sql = "UPDATE salones SET nombre=?, capacidad=?, ubicacion=?, "
                   + "montaje_ideal=?, estado=? "
                   + "WHERE id=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre());
            ps.setInt   (2, s.getCapacidad());
            ps.setString(3, s.getUbicacion());
            ps.setString(4, s.getTipoMontajePrincipal());
            ps.setString(5, s.getEstado());
            ps.setInt   (6, s.getIdSalon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SalonDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    // ── Actualizar solo el estado ─────────────────────────────────────────────
    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE salones SET estado=? WHERE id=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt   (2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SalonDAO.actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM salones WHERE id=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SalonDAO.eliminar: " + e.getMessage());
            return false;
        }
    }

    // ── Mapeo ─────────────────────────────────────────────────────────────────
    private Salon mapear(ResultSet rs) throws SQLException {
        return new Salon(
            rs.getInt   ("id"),
            rs.getString("nombre"),
            rs.getInt   ("capacidad"),
            rs.getString("ubicacion"),
            rs.getString("montaje_ideal"),
            rs.getString("estado"),
            ""
        );
    }
}
