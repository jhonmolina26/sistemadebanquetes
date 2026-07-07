package controller;

import conexion.ConexionBD;
import dao.EventoDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.Evento;
import java.util.List;
import model.Salon;
import models.Anfitrion;
//import model.Salon;

/**
 * Controlador del modulo de Contrataciones.
 * Maneja la logica de negocio para gestionar eventos.
 */
public class EventoController {

    private final EventoDAO dao;
    private final AnfitrionController anfitrionController;
    //private final SalonController salonController;

    public EventoController() {
        this.dao = new EventoDAO();
        this.anfitrionController = new AnfitrionController();
        //this.salonController = new SalonController();
    }

    /**
    * Obtiene todos los eventos de la base de datos.
    *
    * @return lista de eventos
    */
    public List<Evento> obtenerTodos() {
        return dao.obtenerTodos();
    }

    /**
    * Guarda un evento en la base de datos.
    * Si es nuevo lo inserta, si ya existe lo actualiza.
    *
    * @param ev       Objeto Evento con los datos
    * @param esNuevo  true para insertar, false para actualizar
    * @return         true si se guardo correctamente
    */
    public boolean guardarEvento(Evento ev, boolean esNuevo) {
        if (!ev.getCodigo().matches("^EVT-\\d{3}$")) {
            return false;
        }

        if (esNuevo) {
            return dao.insertar(ev);
        } else {
            return dao.actualizar(ev);
        }
    }

    /**
    * Elimina un evento por su ID.
    *
    * @param id  ID del evento
    * @return    true si se elimino correctamente
    */
    public boolean eliminarEvento(int id) {
        return dao.eliminar(id);
    }
    
    /**
    * Obtiene todos los anfitriones para llenar el combo.
    *
    * @return lista de anfitriones
    */
    public List<Anfitrion> obtenerAnfitriones() {
        return anfitrionController.obtenerTodos();
    }
    
    /**
    * Obtiene todos los salones desde la base de datos directamente.
    *
    * @return lista de salones
    */
    public List<Salon> obtenerSalones() {
        List<Salon> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, capacidad, ubicacion, montaje_ideal, estado FROM salones ORDER BY nombre";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Salon(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("capacidad"),
                    rs.getString("ubicacion"),
                    rs.getString("montaje_ideal"),
                    rs.getString("estado"),
                    ""  // descripcion no existe en la tabla
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar salones: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    
    // Método para actualizar solo los campos permitidos
    public boolean actualizarCamposPermitidos(Evento ev) {
        // Primero verificamos que el evento no esté bloqueado
        List<Evento> todos = dao.obtenerTodos();
        Evento existente = null;
        for (Evento e : todos) {
            if (e.getId() == ev.getId()) {
                existente = e;
                break;
            }
        }

        if (existente != null && "Bloqueado".equals(existente.getEstado())) {
            return false; // No podemos modificar un evento bloqueado
        }

        return dao.actualizar(ev);
    }
    
    /**
    * Verifica la disponibilidad de un salon en fecha y horario.
    *
    * @param salonId          ID del salon
    * @param fecha            fecha a verificar
    * @param horario          horario a verificar
    * @param eventoIdExcluir  ID del evento a excluir
    * @return                 true si esta disponible
    */
    public boolean verificarDisponibilidadSalon(int salonId, Date fecha, String horario, int eventoIdExcluir) {
        return dao.verificarDisponibilidadSalon(salonId, fecha, horario, eventoIdExcluir);
    }
    
    /**
    * Busca eventos con filtros.
    *
    * @param tipoEvento  tipo de evento o "Todos"
    * @param fecha       fecha o vacio
    * @param estado      estado o "Todos"
    * @return            lista de eventos filtrados
    */
    public List<Evento> buscar(String tipoEvento, String fecha, String estado) {
        return dao.buscar(tipoEvento, fecha, estado);
    }
}