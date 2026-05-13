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

    private JTable table;
    private DefaultTableModel model;

    private RoundedButton btnGuardar;
    private RoundedButton btnNuevo;
    private RoundedButton btnEliminar;

    private List<Evento> eventos;
    private int selectedIndex = -1;

    private EventoController controller = new EventoController();
    private int selectedId = -1;

    // Datos fijos para los combos (ya que salones y anfitriones se cargan de BD en el futuro)
    private final String[] tiposEvento = {"Boda", "Corporativo", "XV Años", "Graduación", "Cena privada"};
    private final String[] paquetes = {"Premium", "Tradicional", "Corporativo", "Personalizado"};
    private final String[] estados = {"Confirmado", "En propuesta", "Pendiente anticipo", "Bloqueado"};

    private final String[] columnNames = {"Código", "Evento", "Fecha", "Salón", "Invitados", "Estado"};

    public ContratacionesPanel() {
        eventos = controller.obtenerTodos();

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

        addField(card, gbc, 1, "Código", txtCodigo);
        addField(card, gbc, 2, "Fecha", txtFecha);
        addField(card, gbc, 3, "Tipo de evento", cbTipoEvento);
        addField(card, gbc, 4, "Invitados", txtInvitados);
        addField(card, gbc, 5, "Horario", txtHorario);
        addField(card, gbc, 6, "Paquete", cbPaquete);
        addField(card, gbc, 7, "Contacto", txtContacto);
        addField(card, gbc, 8, "Servicios", txtServicios);
        addField(card, gbc, 9, "Estado", cbEstado);

        gbc.gridy = 10;
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
        card.add(UiStyle.sectionTitle("Agenda de eventos"), BorderLayout.NORTH);

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

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // ================== CRUD ==================

    private void guardarEvento() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código del evento es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento ev = new Evento();
        ev.setCodigo(codigo);
        ev.setTipoEvento((String) cbTipoEvento.getSelectedItem());
        ev.setFecha(parseFecha(txtFecha.getText().trim()));
        ev.setSalonId(1);  // Temporal: puedes añadir un combo para salones después
        ev.setInvitados(parseInt(txtInvitados.getText().trim(), 0));
        ev.setHorario(txtHorario.getText().trim());
        ev.setPaquete((String) cbPaquete.getSelectedItem());
        ev.setContacto(txtContacto.getText().trim());
        ev.setServicios(txtServicios.getText().trim());
        ev.setEstado((String) cbEstado.getSelectedItem());
        ev.setAnfitrionId(1); // Temporal: luego se añade combo de anfitriones

        boolean esNuevo = (selectedId == -1);
        if (!esNuevo) {
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
        selectedId = -1;
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

    // Utilidades
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
}
