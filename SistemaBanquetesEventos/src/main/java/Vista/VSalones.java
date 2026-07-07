package vista;

import Vista.UiStyle;
import controller.SalonController;
import model.Salon;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VSalones extends JPanel {

    private final SalonController ctrl = new SalonController();

    // Campos del formulario
    private PlaceholderTextField txtNombre, txtCapacidad, txtDescripcion;
    private JComboBox<String>    cbUbicacion, cbMontaje, cbEstado;

    // Tabla
    private DefaultTableModel modelo;
    private JTable            tabla;

    // Feedback
    private JLabel lblMensaje;

    // ID del salón seleccionado (0 = ninguno / modo insertar)
    private int idSeleccionado = 0;

    public VSalones() {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        cargarTabla();
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = UiStyle.createCard();
        h.setLayout(new BorderLayout());
        h.add(UiStyle.title("Salones"), BorderLayout.NORTH);
        h.add(UiStyle.subtitle("Disponibilidad, capacidad y montajes sugeridos para cada tipo de evento."),
              BorderLayout.CENTER);
        return h;
    }

    // ── Contenido principal ───────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel c = new JPanel(new BorderLayout(18, 18));
        c.setOpaque(false);
        c.add(buildFormCard(),  BorderLayout.WEST);
        c.add(buildTableCard(), BorderLayout.CENTER);
        return c;
    }

    // ── Formulario ────────────────────────────────────────────────────────────
    private JPanel buildFormCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(390, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Datos del salón"), gbc);

        txtNombre      = new PlaceholderTextField("Nombre del salón", 18);
        txtCapacidad   = new PlaceholderTextField("Ej. 220", 18);
        txtDescripcion = new PlaceholderTextField("Descripción opcional", 18);

        cbUbicacion = new JComboBox<>(new String[]{"Interior", "Jardin", "Terraza", "VIP"});
        cbMontaje   = new JComboBox<>(new String[]{"Banquete", "Auditorio", "Escuela", "Imperial", "Coctel"});
        cbEstado    = new JComboBox<>(new String[]{"Disponible", "Reservado", "Mantenimiento"});

        addField(card, gbc, 1, "Nombre",      txtNombre);
        addField(card, gbc, 2, "Capacidad",   txtCapacidad);
        addField(card, gbc, 3, "Ubicación",   cbUbicacion);
        addField(card, gbc, 4, "Montaje",     cbMontaje);
        addField(card, gbc, 5, "Estado",      cbEstado);
        addField(card, gbc, 6, "Descripción", txtDescripcion);

        // Mensaje de feedback
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        card.add(lblMensaje, gbc);

        // Botones de acción
        gbc.gridy = 8;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        RoundedButton btnGuardar  = new RoundedButton("Guardar salón", UiStyle.ACCENT, Color.WHITE);
        RoundedButton btnEliminar = new RoundedButton("Eliminar",       new Color(202, 62, 71), Color.WHITE);
        RoundedButton btnLimpiar  = new RoundedButton("Limpiar");

        btnGuardar.addActionListener(e -> accionGuardar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        actions.add(btnGuardar);
        actions.add(btnEliminar);
        actions.add(btnLimpiar);
        card.add(actions, gbc);
        return card;
    }

    // ── Tabla ─────────────────────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Mapa de ocupación"), BorderLayout.NORTH);

        modelo = new DefaultTableModel(
            new String[]{"ID", "Salón", "Capacidad", "Montaje", "Ubicación", "Estado", "Descripción"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        UiStyle.styleTable(tabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);
        tabla.getColumnModel().getColumn(2).setMaxWidth(80);

        // Al seleccionar una fila se carga en el formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) cargarEnFormulario();
        });

        // Botones de cambio rápido de estado
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        footer.setOpaque(false);

        RoundedButton btnBloquear     = new RoundedButton("Marcar Reservado",     UiStyle.BRAND, Color.WHITE);
        RoundedButton btnLiberar      = new RoundedButton("Marcar Disponible",     UiStyle.ACCENT, Color.WHITE);
        RoundedButton btnMantenimiento = new RoundedButton("Mantenimiento",         new Color(180, 83, 9), Color.WHITE);

        btnBloquear.addActionListener(e     -> accionCambiarEstado("Reservado"));
        btnLiberar.addActionListener(e      -> accionCambiarEstado("Disponible"));
        btnMantenimiento.addActionListener(e -> accionCambiarEstado("Mantenimiento"));

        footer.add(btnBloquear);
        footer.add(btnLiberar);
        footer.add(btnMantenimiento);

        card.add(new JScrollPane(tabla), BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    /** Insertar o actualizar según si hay un salón seleccionado */
    private void accionGuardar() {
        String msg = ctrl.guardar(
            idSeleccionado,
            txtNombre.getText().trim(),
            txtCapacidad.getText().trim(),
            (String) cbUbicacion.getSelectedItem(),
            (String) cbMontaje.getSelectedItem(),
            (String) cbEstado.getSelectedItem(),
            txtDescripcion.getText().trim()
        );
        mostrarMensaje(msg);
        if (msg.startsWith("OK")) { limpiarFormulario(); cargarTabla(); }
    }

    /** Eliminar el salón seleccionado en la tabla */
    private void accionEliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) { mostrarMensaje("ERROR: Selecciona un salón de la tabla."); return; }
        int id     = (int) modelo.getValueAt(row, 0);
        String nom = (String) modelo.getValueAt(row, 1);
        int ok = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el salón \"" + nom + "\"?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            mostrarMensaje(ctrl.eliminar(id));
            limpiarFormulario();
            cargarTabla();
        }
    }

    /** Cambio rápido de estado desde los botones de la tabla */
    private void accionCambiarEstado(String nuevoEstado) {
        int row = tabla.getSelectedRow();
        if (row < 0) { mostrarMensaje("ERROR: Selecciona un salón de la tabla."); return; }
        int id = (int) modelo.getValueAt(row, 0);
        String msg = switch (nuevoEstado) {
            case "Reservado"     -> ctrl.bloquearFecha(id);
            case "Disponible"    -> ctrl.liberarSalon(id);
            case "Mantenimiento" -> ctrl.marcarMantenimiento(id);
            default              -> "ERROR: Estado desconocido.";
        };
        mostrarMensaje(msg);
        cargarTabla();
    }

    /** Carga el salón seleccionado de la tabla en el formulario (modo edición) */
    private void cargarEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        idSeleccionado = (int) modelo.getValueAt(row, 0);
        txtNombre.setText((String) modelo.getValueAt(row, 1));
        txtCapacidad.setText(String.valueOf(modelo.getValueAt(row, 2)));
        cbMontaje.setSelectedItem(modelo.getValueAt(row, 3));
        cbUbicacion.setSelectedItem(modelo.getValueAt(row, 4));
        cbEstado.setSelectedItem(modelo.getValueAt(row, 5));
        txtDescripcion.setText((String) modelo.getValueAt(row, 6));
        lblMensaje.setText(" ");
    }

    /** Recarga todos los salones en la tabla */
    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Salon> lista = ctrl.obtenerTodos();
        for (Salon s : lista) {
            modelo.addRow(new Object[]{
                s.getIdSalon(), s.getNombre(), s.getCapacidad(),
                s.getTipoMontajePrincipal(), s.getUbicacion(),
                s.getEstado(), s.getDescripcion()
            });
        }
    }

    /** Limpia el formulario y vuelve a modo insertar */
    private void limpiarFormulario() {
        idSeleccionado = 0;
        txtNombre.setText("");
        txtCapacidad.setText("");
        txtDescripcion.setText("");
        cbUbicacion.setSelectedIndex(0);
        cbMontaje.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        lblMensaje.setText(" ");
        tabla.clearSelection();
    }

    private void mostrarMensaje(String msg) {
        lblMensaje.setForeground(msg.startsWith("OK") ? UiStyle.ACCENT : new Color(202, 62, 71));
        lblMensaje.setText(msg.replaceFirst("^(OK|ERROR): ", ""));
    }

    private void addField(JPanel p, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 1;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(comp, gbc);
    }
}
