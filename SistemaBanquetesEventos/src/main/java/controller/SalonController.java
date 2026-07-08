package controller;

import dao.SalonDAO;
import model.Salon;
import java.util.List;

public class SalonController {
    
    

    private final SalonDAO dao = new SalonDAO();

    // ── Listar ────────────────────────────────────────────────────────────────
    public List<Salon> obtenerTodos() { return dao.listar(); }

    public List<Salon> obtenerDisponibles() { return dao.listarDisponibles(); }

    // ── Guardar (insertar o actualizar) ───────────────────────────────────────
    public String guardar(int idSalon, String nombre, String capacidadStr,
                          String ubicacion, String montaje, String estado, String descripcion) {
        
        if (nombre == null || nombre.isBlank())
            return "ERROR: El nombre del salón es requerido.";
        if (capacidadStr == null || capacidadStr.isBlank())
            return "ERROR: La capacidad es requerida.";
        
        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadStr.trim());
            
            if (capacidad < 1) return "ERROR: La capacidad debe ser mayor a 0.";
            
            
        } catch (NumberFormatException e) {
            return "ERROR: La capacidad debe ser un número entero.";
        }

        Salon s = new Salon(idSalon, nombre.trim(), capacidad, ubicacion, montaje, estado,
                            descripcion == null ? "" : descripcion.trim());

        if (idSalon == 0) {
            return dao.insertar(s) ? "OK: Salón registrado correctamente." : "ERROR: No se pudo registrar el salón.";
        } else {
            return dao.actualizar(s) ? "OK: Salón actualizado correctamente." : "ERROR: No se pudo actualizar el salón.";
        }
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    public String eliminar(int idSalon) {
        return dao.eliminar(idSalon)
            ? "OK: Salón eliminado correctamente."
            : "ERROR: No se pudo eliminar el salón.";
    }

    // ── Cambiar estado ────────────────────────────────────────────────────────
    public String bloquearFecha(int idSalon) {
        return dao.actualizarEstado(idSalon, "Reservado")
            ? "OK: Salón marcado como Reservado."
            : "ERROR: No se pudo actualizar el estado.";
    }

    public String liberarSalon(int idSalon) {
        return dao.actualizarEstado(idSalon, "Disponible")
            ? "OK: Salón disponible."
            : "ERROR: No se pudo actualizar el estado.";
    }

    public String marcarMantenimiento(int idSalon) {
        return dao.actualizarEstado(idSalon, "Mantenimiento")
            ? "OK: Salón en mantenimiento."
            : "ERROR: No se pudo actualizar el estado.";
    }
}
