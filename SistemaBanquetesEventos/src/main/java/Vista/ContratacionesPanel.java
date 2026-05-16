package Vista;

import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;
import controller.EventoController;
import models.Evento;
import models.Anfitrion;

public class ContratacionesPanel extends JPanel {

    private PlaceholderTextField txtCodigo;
    private PlaceholderTextField txtFecha;
    private PlaceholderTextField txtInvitados;
    private PlaceholderTextField txtHorario;
    private PlaceholderTextField txtContacto;
    private PlaceholderTextField txtServicios;
    private JComboBox<String> cbTipoEvento;
    private JComboBox<String> cbPaquete;
    private JComboBox<String> cbEstado;
    private JComboBox<Anfitrion> cbAnfitrion;
    private JComboBox<String> cbFiltroTipoEvento;
    private PlaceholderTextField txtFiltroFecha;
    private JComboBox<String> cbFiltroEstado;
    private RoundedButton btnBuscar;
    private RoundedButton btnLimpiar;

    private JTable table;
    private DefaultTableModel model;

    private RoundedButton btnGuardar;
    private RoundedButton btnNuevo;
    private RoundedButton btnEliminar;

    private List<Evento> eventos;
    private List<Anfitrion> anfitriones;
    private int selectedIndex = -1;

    private EventoController controller = new EventoController();
    private int selectedId = -1;

    private final String[] tiposEvento = {"Boda", "Corporativo", "XV Años", "Graduación", "Cena privada"};
    private final String[] paquetes = {"Premium", "Tradicional", "Corporativo", "Personalizado"};
    private final String[] estados = {"Pendiente anticipo", "Confirmado", "En propuesta", "Bloqueado"};

    private final String[] columnNames = {"Código", "Evento", "Fecha", "Salón", "Invitados", "Estado"};

    public ContratacionesPanel() {
        eventos = controller.obtenerTodos();
        anfitriones = controller.obtenerAnfitriones();

        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel panel = UiStyle.createCard();
        panel.setLayout(new BorderLayout(10, 6));
        panel.add(UiStyle.title("Contrataciones"), BorderLayout.NORTH);
        panel.add(UiStyle.subtitle("Registro visual de eventos, fechas, servicios y configuración operativa."), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildFormCard(), BorderLayout.WEST);
        content.add(buildTableCard(), BorderLayout.CENTER);
        return content;
    }

    private JPanel buildFormCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(430, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Ficha de contratación"), gbc);

        txtCodigo = new PlaceholderTextField("Ej. EVT-2026-042", 18);
        txtFecha = new PlaceholderTextField("YYYY-MM-DD", 18);
        txtInvitados = new PlaceholderTextField("180 invitados", 18);
        txtHorario = new PlaceholderTextField("18:00 - 01:00", 18);
        txtContacto = new PlaceholderTextField("Nombre del coordinador", 18);
        txtServicios = new PlaceholderTextField("Catering, DJ, decoración...", 18);
        cbTipoEvento = new JComboBox<>(tiposEvento);
        cbPaquete = new JComboBox<>(paquetes);
        cbEstado = new JComboBox<>(estados);

        cbAnfitrion = new JComboBox<>();
        for (Anfitrion a : anfitriones) {
            cbAnfitrion.addItem(a);
        }

        addField(card, gbc, 1, "Código", txtCodigo);
        addField(card, gbc, 2, "Fecha", txtFecha);
        addField(card, gbc, 3, "Tipo de evento", cbTipoEvento);
        addField(card, gbc, 4, "Anfitrión", cbAnfitrion);
        addField(card, gbc, 5, "Invitados", txtInvitados);
        addField(card, gbc, 6, "Horario", txtHorario);
        addField(card, gbc, 7, "Paquete", cbPaquete);
        addField(card, gbc, 8, "Contacto", txtContacto);
        addField(card, gbc, 9, "Servicios", txtServicios);
        addField(card, gbc, 10, "Estado", cbEstado);

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        btnNuevo = new RoundedButton("Nuevo");
        btnGuardar = new RoundedButton("Guardar borrador");
        btnEliminar = new RoundedButton("Eliminar", new Color(202, 62, 71), Color.WHITE);

        actions.add(btnNuevo);
        actions.add(btnGuardar);
        actions.add(btnEliminar);
        card.add(actions, gbc);

        btnNuevo.addActionListener(e -> {
            deselectTable();
            clearForm();
            selectedIndex = -1;
            updateButtonStates();
        });

        btnGuardar.addActionListener(e -> guardarEvento());
        btnEliminar.addActionListener(e -> eliminarEvento());

        return card;
    }

    private JPanel buildTableCard() {
    JPanel card = UiStyle.createCard();
    card.setLayout(new BorderLayout(12, 12));
    
    // Panel superior: título + filtros
    JPanel topPanel = new JPanel(new BorderLayout(12, 12));
    topPanel.setOpaque(false);
    topPanel.add(UiStyle.sectionTitle("Agenda de eventos"), BorderLayout.NORTH);
    
    // Panel de filtros
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    filterPanel.setOpaque(false);
    
    String[] filtroTipos = {"Todos", "Boda", "Corporativo", "XV Años", "Graduación", "Cena privada"};
    String[] filtroEstados = {"Todos", "Pendiente anticipo", "Confirmado", "En propuesta", "Bloqueado"};
    
    cbFiltroTipoEvento = new JComboBox<>(filtroTipos);
    txtFiltroFecha = new PlaceholderTextField("YYYY-MM-DD", 12);
    cbFiltroEstado = new JComboBox<>(filtroEstados);
    
    btnBuscar = new RoundedButton("Buscar");
    btnLimpiar = new RoundedButton("Limpiar");
    
    filterPanel.add(new JLabel("Tipo:"));
    filterPanel.add(cbFiltroTipoEvento);
    filterPanel.add(new JLabel("Fecha:"));
    filterPanel.add(txtFiltroFecha);
    filterPanel.add(new JLabel("Estado:"));
    filterPanel.add(cbFiltroEstado);
    filterPanel.add(btnBuscar);
    filterPanel.add(btnLimpiar);
    
    topPanel.add(filterPanel, BorderLayout.CENTER);
    card.add(topPanel, BorderLayout.NORTH);
    
    // Tabla
    model = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    UiStyle.styleTable(table);

    table.getSelectionModel().addListSelectionListener(e -> {
        if (e.getValueIsAdjusting()) return;
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            selectedIndex = viewRow;
            selectedId = eventos.get(viewRow).getId();
            loadEventoFromRow(selectedIndex);
        } else {
            selectedIndex = -1;
            selectedId = -1;
        }
        updateButtonStates();
    });
    
    // Listeners de filtros
    btnBuscar.addActionListener(e -> buscarEventos());
    btnLimpiar.addActionListener(e -> limpiarFiltros());

    refreshTable();

    JScrollPane scrollPane = new JScrollPane(table);
    card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    //  CRUD 

    private void guardarEvento() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código del evento es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fecha = parseFecha(txtFecha.getText().trim());
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "La fecha no es válida. Use formato YYYY-MM-DD.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invitados = parseInt(txtInvitados.getText().trim(), 0);
        if (invitados <= 0) {
            JOptionPane.showMessageDialog(this, "El número de invitados debe ser mayor a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String horario = txtHorario.getText().trim();
        if (horario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El horario es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento ev = new Evento();
        ev.setCodigo(codigo);
        ev.setTipoEvento((String) cbTipoEvento.getSelectedItem());
        ev.setFecha(fecha);
        ev.setSalonId(1);  // Temporal: hasta que agregen el módulo de Salones

        Anfitrion anfitrionSeleccionado = (Anfitrion) cbAnfitrion.getSelectedItem();
        ev.setAnfitrionId(anfitrionSeleccionado != null ? anfitrionSeleccionado.getId() : 1);

        ev.setInvitados(invitados);
        ev.setHorario(horario);
        ev.setPaquete((String) cbPaquete.getSelectedItem());
        ev.setContacto(txtContacto.getText().trim());
        ev.setServicios(txtServicios.getText().trim());
        ev.setEstado((String) cbEstado.getSelectedItem());

        boolean esNuevo = (selectedId == -1);

        if (!esNuevo) {
            // Validar que no esté bloqueado
            Evento eventoActual = null;
            for (Evento e : eventos) {
                if (e.getId() == selectedId) {
                    eventoActual = e;
                    break;
                }
            }

            if (eventoActual != null && "Bloqueado".equals(eventoActual.getEstado())) {
                JOptionPane.showMessageDialog(this,
                    "No se puede modificar un evento en estado 'Bloqueado'.",
                    "Restricción", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar disponibilidad del salón si cambió fecha u horario
            boolean fechaCambio = !fecha.equals(eventoActual.getFecha());
            boolean horarioCambio = !horario.equals(eventoActual.getHorario());

            if (fechaCambio || horarioCambio) {
                boolean disponible = controller.verificarDisponibilidadSalon(
                    ev.getSalonId(), fecha, horario, selectedId);
                if (!disponible) {
                    JOptionPane.showMessageDialog(this,
                        "El salón no está disponible en esa fecha y horario.",
                        "Conflicto de disponibilidad", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            ev.setId(selectedId);
        }

        boolean exito = controller.guardarEvento(ev, esNuevo);
        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el evento.", "Error", JOptionPane.ERROR_MESSAGE);
            eventos = controller.obtenerTodos();
            refreshTable();
            return;
        }

        eventos = controller.obtenerTodos();
        refreshTable();
        clearForm();
        selectedIndex = -1;
        selectedId = -1;
        updateButtonStates();
    }

    private void eliminarEvento() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento para eliminar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el evento seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarEvento(selectedId)) {
                eventos = controller.obtenerTodos();
                refreshTable();
                clearForm();
                selectedIndex = -1;
                selectedId = -1;
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el evento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadEventoFromRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < eventos.size()) {
            Evento ev = eventos.get(rowIndex);
            txtCodigo.setText(ev.getCodigo());
            txtFecha.setText(ev.getFecha() != null ? ev.getFecha().toString() : "");
            txtInvitados.setText(String.valueOf(ev.getInvitados()));
            txtHorario.setText(ev.getHorario());
            txtContacto.setText(ev.getContacto());
            txtServicios.setText(ev.getServicios());
            cbTipoEvento.setSelectedItem(ev.getTipoEvento());
            cbPaquete.setSelectedItem(ev.getPaquete());
            cbEstado.setSelectedItem(ev.getEstado());

            for (int i = 0; i < cbAnfitrion.getItemCount(); i++) {
                if (cbAnfitrion.getItemAt(i).getId() == ev.getAnfitrionId()) {
                    cbAnfitrion.setSelectedIndex(i);
                    break;
                }
            }

            // Validar si está bloqueado para deshabilitar campos
            boolean bloqueado = "Bloqueado".equals(ev.getEstado());
            txtCodigo.setEnabled(false); // El código nunca se modifica
            txtFecha.setEnabled(!bloqueado);
            txtInvitados.setEnabled(!bloqueado);
            txtHorario.setEnabled(!bloqueado);
            txtContacto.setEnabled(!bloqueado);
            txtServicios.setEnabled(!bloqueado);
            cbTipoEvento.setEnabled(!bloqueado);
            cbPaquete.setEnabled(!bloqueado);
            cbAnfitrion.setEnabled(!bloqueado);

            if (bloqueado) {
                btnGuardar.setText("EVENTO BLOQUEADO");
                btnGuardar.setEnabled(false);
            }
        }
    }

    private void clearForm() {
        txtCodigo.setText("");
        txtFecha.setText("");
        txtInvitados.setText("");
        txtHorario.setText("");
        txtContacto.setText("");
        txtServicios.setText("");
        cbTipoEvento.setSelectedIndex(0);
        cbPaquete.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        if (cbAnfitrion.getItemCount() > 0) cbAnfitrion.setSelectedIndex(0);
        selectedId = -1;

        // Re-habilitar todos los campos
        txtCodigo.setEnabled(true);
        txtFecha.setEnabled(true);
        txtInvitados.setEnabled(true);
        txtHorario.setEnabled(true);
        txtContacto.setEnabled(true);
        txtServicios.setEnabled(true);
        cbTipoEvento.setEnabled(true);
        cbPaquete.setEnabled(true);
        cbEstado.setEnabled(true);
        cbAnfitrion.setEnabled(true);
        btnGuardar.setEnabled(true);
    }

    private void deselectTable() {
        table.clearSelection();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Evento ev : eventos) {
            model.addRow(new Object[]{
                ev.getCodigo(),
                ev.getTipoEvento(),
                ev.getFecha(),
                ev.getSalonNombre() != null ? ev.getSalonNombre() : "Sin salón",
                ev.getInvitados(),
                ev.getEstado()
            });
        }
    }

    private void updateButtonStates() {
        if (selectedIndex >= 0) {
            btnGuardar.setText("Actualizar evento");
            btnEliminar.setEnabled(true);
        } else {
            btnGuardar.setText("Guardar borrador");
            btnEliminar.setEnabled(false);
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component component) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private Date parseFecha(String texto) {
        try {
            return Date.valueOf(texto);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private int parseInt(String texto, int valorDefault) {
        try {
            return Integer.parseInt(texto.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return valorDefault;
        }
    }
    
    private void buscarEventos() {
        String tipoEvento = (String) cbFiltroTipoEvento.getSelectedItem();
        String fecha = txtFiltroFecha.getText().trim();
        String estado = (String) cbFiltroEstado.getSelectedItem();

        eventos = controller.buscar(tipoEvento, fecha, estado);
        refreshTable();
        clearForm();
        selectedIndex = -1;
        selectedId = -1;
        updateButtonStates();
    }

    private void limpiarFiltros() {
        cbFiltroTipoEvento.setSelectedIndex(0);
        txtFiltroFecha.setText("");
        cbFiltroEstado.setSelectedIndex(0);
        eventos = controller.obtenerTodos();
        refreshTable();
        clearForm();
        selectedIndex = -1;
        selectedId = -1;
        updateButtonStates();
    }
}