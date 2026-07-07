package dao;

import conexion.ConexionBD;
import models.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public List<Evento> obtenerTodos() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.codigo, e.tipo_evento, e.fecha, e.salon_id, s.nombre AS salon_nombre, " +
                     "e.invitados, e.horario, e.paquete, e.contacto, e.servicios, e.estado, " +
                     "e.anfitrion_id, a.nombre AS anfitrion_nombre " +
                     "FROM eventos e " +
                     "LEFT JOIN salones s ON e.salon_id = s.id " +
                     "LEFT JOIN anfitriones a ON e.anfitrion_id = a.id " +
                     "ORDER BY e.fecha DESC";

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evento ev = new Evento();
                ev.setId(rs.getInt("id"));
                ev.setCodigo(rs.getString("codigo"));
                ev.setTipoEvento(rs.getString("tipo_evento"));
                ev.setFecha(rs.getDate("fecha"));
                ev.setSalonId(rs.getInt("salon_id"));
                ev.setSalonNombre(rs.getString("salon_nombre"));
                ev.setInvitados(rs.getInt("invitados"));
                ev.setHorario(rs.getString("horario"));
                ev.setPaquete(rs.getString("paquete"));
                ev.setContacto(rs.getString("contacto"));
                ev.setServicios(rs.getString("servicios"));
                ev.setEstado(rs.getString("estado"));
                ev.setAnfitrionId(rs.getInt("anfitrion_id"));
                ev.setAnfitrionNombre(rs.getString("anfitrion_nombre"));
                lista.add(ev);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener eventos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Evento ev) {
        String sql = "INSERT INTO eventos (codigo, tipo_evento, fecha, salon_id, invitados, horario, paquete, contacto, servicios, estado, anfitrion_id) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ev.getCodigo());
            pstmt.setString(2, ev.getTipoEvento());
            pstmt.setDate(3, ev.getFecha());
            pstmt.setInt(4, ev.getSalonId());
            pstmt.setInt(5, ev.getInvitados());
            pstmt.setString(6, ev.getHorario());
            pstmt.setString(7, ev.getPaquete());
            pstmt.setString(8, ev.getContacto());
            pstmt.setString(9, ev.getServicios());
            pstmt.setString(10, ev.getEstado());
            pstmt.setInt(11, ev.getAnfitrionId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar evento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Evento ev) {
        String sql = "UPDATE eventos SET codigo=?, tipo_evento=?, fecha=?, salon_id=?, invitados=?, horario=?, " +
                     "paquete=?, contacto=?, servicios=?, estado=?, anfitrion_id=? WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ev.getCodigo());
            pstmt.setString(2, ev.getTipoEvento());
            pstmt.setDate(3, ev.getFecha());
            pstmt.setInt(4, ev.getSalonId());
            pstmt.setInt(5, ev.getInvitados());
            pstmt.setString(6, ev.getHorario());
            pstmt.setString(7, ev.getPaquete());
            pstmt.setString(8, ev.getContacto());
            pstmt.setString(9, ev.getServicios());
            pstmt.setString(10, ev.getEstado());
            pstmt.setInt(11, ev.getAnfitrionId());
            pstmt.setInt(12, ev.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM eventos WHERE id=?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
            return false;
        }
    }
    
    public boolean verificarDisponibilidadSalon(int salonId, Date fecha, String horario, int eventoIdExcluir) {

    String sql = "SELECT COUNT(*) FROM eventos " +
                 "WHERE salon_id = ? " +
                 "AND fecha = ? " +
                 "AND estado <> 'Bloqueado' " +
                 "AND id <> ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, salonId);
        pstmt.setDate(2, fecha);
        pstmt.setInt(3, eventoIdExcluir);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al verificar disponibilidad: " + e.getMessage());
    }

    return false;
}
    
    // meetodo que nos sirve para buscar por medio del tipoEvent, fecha y el estado
    public List<Evento> buscar(String tipoEvento, String fecha, String estado) {
    List<Evento> lista = new ArrayList<>();
    
    StringBuilder sql = new StringBuilder(
        "SELECT e.id, e.codigo, e.tipo_evento, e.fecha, e.salon_id, s.nombre AS salon_nombre, " +
        "e.invitados, e.horario, e.paquete, e.contacto, e.servicios, e.estado, " +
        "e.anfitrion_id, a.nombre AS anfitrion_nombre " +
        "FROM eventos e " +
        "LEFT JOIN salones s ON e.salon_id = s.id " +
        "LEFT JOIN anfitriones a ON e.anfitrion_id = a.id " +
        "WHERE 1=1 ");
    
    List<Object> parametros = new ArrayList<>();
    
    if (tipoEvento != null && !tipoEvento.isEmpty() && !tipoEvento.equals("Todos")) {
        sql.append("AND e.tipo_evento = ? ");
        parametros.add(tipoEvento);
    }
    
    if (fecha != null && !fecha.isEmpty()) {
        sql.append("AND e.fecha = ? ");
        parametros.add(Date.valueOf(fecha));
    }
    
    if (estado != null && !estado.isEmpty() && !estado.equals("Todos")) {
        sql.append("AND e.estado = ? ");
        parametros.add(estado);
    }
    
    sql.append("ORDER BY e.fecha DESC");
    
    try (Connection conn = ConexionBD.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
        
        for (int i = 0; i < parametros.size(); i++) {
            pstmt.setObject(i + 1, parametros.get(i));
        }
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Evento ev = new Evento();
                ev.setId(rs.getInt("id"));
                ev.setCodigo(rs.getString("codigo"));
                ev.setTipoEvento(rs.getString("tipo_evento"));
                ev.setFecha(rs.getDate("fecha"));
                ev.setSalonId(rs.getInt("salon_id"));
                ev.setSalonNombre(rs.getString("salon_nombre"));
                ev.setInvitados(rs.getInt("invitados"));
                ev.setHorario(rs.getString("horario"));
                ev.setPaquete(rs.getString("paquete"));
                ev.setContacto(rs.getString("contacto"));
                ev.setServicios(rs.getString("servicios"));
                ev.setEstado(rs.getString("estado"));
                ev.setAnfitrionId(rs.getInt("anfitrion_id"));
                ev.setAnfitrionNombre(rs.getString("anfitrion_nombre"));
                lista.add(ev);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar eventos: " + e.getMessage());
    }
        return lista;
    }
}
