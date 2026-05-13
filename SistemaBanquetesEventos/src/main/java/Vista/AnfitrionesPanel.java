package Vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;
import controller.AnfitrionController;
import models.Anfitrion;

public class AnfitrionesPanel extends JPanel {

    private PlaceholderTextField txtNombre;
    private PlaceholderTextField txtEmpresa;
    private PlaceholderTextField txtDocumento;
    private PlaceholderTextField txtCorreo;
    private PlaceholderTextField txtTelefono;
    private JComboBox<String> cbSegmento;

    private JTable table;
    private DefaultTableModel model;

    private RoundedButton btnGuardar;
    private RoundedButton btnNuevo;

    private List<Anfitrion> anfitriones;
    private int selectedIndex = -1;

    // --- USO DEL CONTROLADOR EN VEZ DEL DAO ---
    private AnfitrionController controller = new AnfitrionController();
    private int selectedId = -1;

    private final String[] columnNames = {"Anfitrion", "Segmento", "Telefono", "Correo", "Próximo evento", "★ VIP", "Acción"};

    public AnfitrionesPanel() {
        anfitriones = controller.obtenerTodos();

        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        refreshTable();
        updateButtonStates();
        clearForm();
    }

    private JPanel buildHeader() {
        JPanel header = UiStyle.createCard();
        header.setLayout(new BorderLayout());
        header.add(UiStyle.title("Anfitriones"), BorderLayout.NORTH);
        header.add(UiStyle.subtitle("Datos del cliente u organizador con enfoque comercial y operativo."), BorderLayout.CENTER);
        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildProfileCard(), BorderLayout.WEST);
        content.add(buildTableCard(), BorderLayout.CENTER);
        return content;
    }

    private JPanel buildProfileCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Perfil del anfitrión"), gbc);

        txtNombre = new PlaceholderTextField("Nombre del organizador", 18);
        txtEmpresa = new PlaceholderTextField("Empresa o familia", 18);
        txtDocumento = new PlaceholderTextField("Cedula o RUC", 18);
        txtCorreo = new PlaceholderTextField("correo@evento.com", 18);
        txtTelefono = new PlaceholderTextField("0990000000", 18);
        cbSegmento = new JComboBox<>(new String[]{"Corporativo", "Social", "Institucional", "Agencia aliada"});

        addField(card, gbc, 1, "Nombre", txtNombre);
        addField(card, gbc, 2, "Empresa / familia", txtEmpresa);
        addField(card, gbc, 3, "Documento", txtDocumento);
        addField(card, gbc, 4, "Correo", txtCorreo);
        addField(card, gbc, 5, "Telefono", txtTelefono);
        addField(card, gbc, 6, "Segmento", cbSegmento);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        btnNuevo = new RoundedButton("Nuevo");
        btnGuardar = new RoundedButton("Guardar perfil");

        actions.add(btnNuevo);
        actions.add(btnGuardar);
        card.add(actions, gbc);

        btnNuevo.addActionListener(e -> {
            deselectTable();
            clearForm();
            selectedIndex = -1;
            updateButtonStates();
        });

        btnGuardar.addActionListener(e -> guardarCliente());

        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Cartera de clientes"), BorderLayout.NORTH);

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(table);

        table.getColumnModel().getColumn(5).setCellRenderer(new VipButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new VipButtonEditor());

        table.getColumnModel().getColumn(6).setCellRenderer(new DeleteButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new DeleteButtonEditor());

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    selectedIndex = viewRow;
                    selectedId = anfitriones.get(viewRow).getId();
                    loadAnfitrionFromRow(selectedIndex);
                } else {
                    selectedIndex = -1;
                    selectedId = -1;
                }
                updateButtonStates();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // ================== CRUD (usa el controlador) ==================

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del anfitrión es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anfitrion a = new Anfitrion();
        a.setNombre(nombre);
        a.setEmpresa(txtEmpresa.getText().trim());
        a.setDocumento(txtDocumento.getText().trim());
        a.setCorreo(txtCorreo.getText().trim());
        a.setTelefono(txtTelefono.getText().trim());
        a.setSegmento((String) cbSegmento.getSelectedItem());
        a.setVip(false);
        a.setProximoEvento("");

        boolean esNuevo = (selectedId == -1);
        if (!esNuevo) {
            a.setId(selectedId);
        }

        boolean exito = controller.guardarAnfitrion(a, esNuevo);
        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el anfitrión. Revise la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            anfitriones = controller.obtenerTodos();
            refreshTable();
            return;
        }

        anfitriones = controller.obtenerTodos();
        refreshTable();
        clearForm();
        selectedIndex = -1;
        selectedId = -1;
        updateButtonStates();
    }

    private void toggleVip(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < anfitriones.size()) {
            Anfitrion a = anfitriones.get(rowIndex);
            boolean nuevoEstado = !a.isVip();
            if (controller.cambiarVip(a.getId(), nuevoEstado)) {
                a.setVip(nuevoEstado);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado VIP.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCliente(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < anfitriones.size()) {
            int id = anfitriones.get(rowIndex).getId();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar al anfitrión seleccionado?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.eliminarAnfitrion(id)) {
                    anfitriones = controller.obtenerTodos();
                    refreshTable();
                    clearForm();
                    selectedIndex = -1;
                    selectedId = -1;
                    updateButtonStates();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el anfitrión.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void loadAnfitrionFromRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < anfitriones.size()) {
            Anfitrion a = anfitriones.get(rowIndex);
            txtNombre.setText(a.getNombre());
            txtEmpresa.setText(a.getEmpresa());
            txtDocumento.setText(a.getDocumento());
            txtCorreo.setText(a.getCorreo());
            txtTelefono.setText(a.getTelefono());
            cbSegmento.setSelectedItem(a.getSegmento());
        }
    }

    private void clearForm() {
        txtNombre.setText("");
        txtEmpresa.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        cbSegmento.setSelectedIndex(0);
        selectedId = -1;
    }

    private void deselectTable() {
        table.clearSelection();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Anfitrion a : anfitriones) {
            model.addRow(new Object[]{
                a.getNombre(),
                a.getSegmento(),
                a.getTelefono(),
                a.getCorreo(),
                a.getProximoEvento(),
                a.isVip() ? "★" : "☆",
                "Eliminar"
            });
        }
    }

    private void updateButtonStates() {
        if (selectedIndex >= 0) {
            btnGuardar.setText("Actualizar perfil");
        } else {
            btnGuardar.setText("Guardar perfil");
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

    // ================== Renderers y Editors ==================

    class VipButtonRenderer implements TableCellRenderer {
        private final JButton button = new JButton();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            boolean isVip = anfitriones.get(row).isVip();
            button.setText(isVip ? "★" : "☆");
            button.setForeground(isVip ? Color.ORANGE : Color.GRAY);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return button;
        }
    }

    class VipButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int row;

        public VipButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleVip(row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            boolean isVip = anfitriones.get(row).isVip();
            button.setText(isVip ? "★" : "☆");
            button.setForeground(isVip ? Color.ORANGE : Color.GRAY);
            button.setBackground(table.getSelectionBackground());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return anfitriones.get(row).isVip() ? "★" : "☆";
        }
    }

    class DeleteButtonRenderer implements TableCellRenderer {
        private final JButton button = new JButton("🗑 Eliminar");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            button.setForeground(Color.RED);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            return button;
        }
    }

    class DeleteButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int row;

        public DeleteButtonEditor() {
            super(new JCheckBox());
            button = new JButton("🗑 Eliminar");
            button.setForeground(Color.RED);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarCliente(row);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            button.setBackground(table.getSelectionBackground());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Eliminar";
        }
    }
}
