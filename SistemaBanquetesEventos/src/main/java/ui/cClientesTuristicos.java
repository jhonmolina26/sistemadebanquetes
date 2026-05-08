package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class cClientesTuristicos extends JPanel {

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

    private List<Cliente> clientes;
    private int selectedIndex = -1;

    // Columnas de la tabla
    private final String[] columnNames = {"Anfitrion", "Segmento", "Telefono", "Correo", "Próximo evento", "★ VIP", "Acción"};

    public cClientesTuristicos() {
        clientes = new ArrayList<>();
        // Datos iniciales de ejemplo
        clientes.add(new Cliente("Daniela Mite", "", "0900000001", "daniela@correo.com", "0992011001", "Social", false, "Boda 27/04"));
        clientes.add(new Cliente("Grupo Armonia", "Grupo Armonia", "1790000002001", "eventos@armonia.ec", "042333991", "Corporativo", false, "Cena 04/05"));
        clientes.add(new Cliente("Colegio Horizonte", "Colegio Horizonte", "1790000003001", "secretaria@horizonte.edu", "0981004455", "Institucional", false, "Graduacion 02/05"));

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

        // Solo dos botones: Nuevo y Guardar
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
                // Solo las columnas de botones son editables (para poder hacer clic)
                return column == 5 || column == 6;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(table);

        // Renderizador y editor para la columna VIP (índice 5)
        table.getColumnModel().getColumn(5).setCellRenderer(new VipButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new VipButtonEditor());

        // Renderizador y editor para la columna Eliminar (índice 6)
        table.getColumnModel().getColumn(6).setCellRenderer(new DeleteButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new DeleteButtonEditor());

        // Listener de selección: al seleccionar fila, cargar datos en formulario
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    selectedIndex = viewRow;
                    loadClienteFromRow(selectedIndex);
                } else {
                    selectedIndex = -1;
                }
                updateButtonStates();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // ================== CRUD ==================

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del anfitrión es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = new Cliente(
                nombre,
                txtEmpresa.getText().trim(),
                txtDocumento.getText().trim(),
                txtCorreo.getText().trim(),
                txtTelefono.getText().trim(),
                (String) cbSegmento.getSelectedItem(),
                false,
                ""
        );

        if (selectedIndex >= 0 && selectedIndex < clientes.size()) {
            // Actualizar cliente existente (conserva VIP y próximo evento)
            Cliente existente = clientes.get(selectedIndex);
            existente.setNombre(nombre);
            existente.setEmpresa(c.getEmpresa());
            existente.setDocumento(c.getDocumento());
            existente.setCorreo(c.getCorreo());
            existente.setTelefono(c.getTelefono());
            existente.setSegmento(c.getSegmento());
        } else {
            // Insertar nuevo cliente
            clientes.add(c);
        }

        refreshTable();
        clearForm();
        selectedIndex = -1;
        updateButtonStates();
    }

    private void toggleVip(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            Cliente c = clientes.get(rowIndex);
            c.setVip(!c.isVip());
            refreshTable();
        }
    }

    private void eliminarCliente(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar al anfitrión seleccionado?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clientes.remove(rowIndex);
                refreshTable();
                clearForm();
                selectedIndex = -1;
                updateButtonStates();
            }
        }
    }

    private void loadClienteFromRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < clientes.size()) {
            Cliente c = clientes.get(rowIndex);
            txtNombre.setText(c.getNombre());
            txtEmpresa.setText(c.getEmpresa());
            txtDocumento.setText(c.getDocumento());
            txtCorreo.setText(c.getCorreo());
            txtTelefono.setText(c.getTelefono());
            cbSegmento.setSelectedItem(c.getSegmento());
        }
    }

    private void clearForm() {
        txtNombre.setText("");
        txtEmpresa.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        cbSegmento.setSelectedIndex(0);
    }

    private void deselectTable() {
        table.clearSelection();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Cliente c : clientes) {
            model.addRow(new Object[]{
                c.getNombre(),
                c.getSegmento(),
                c.getTelefono(),
                c.getCorreo(),
                c.getProximoEvento(),
                c.isVip() ? "★" : "☆",   // solo para mostrar, el botón se renderiza con esto
                "Eliminar"
            });
        }
    }

    private void updateButtonStates() {
        // Aquí podrías cambiar el texto del botón Guardar según el modo
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

    // ================== Clase interna Cliente ==================
    static class Cliente {
        private String nombre;
        private String empresa;
        private String documento;
        private String correo;
        private String telefono;
        private String segmento;
        private boolean vip;
        private String proximoEvento;

        public Cliente(String nombre, String empresa, String documento, String correo,
                       String telefono, String segmento, boolean vip, String proximoEvento) {
            this.nombre = nombre;
            this.empresa = empresa;
            this.documento = documento;
            this.correo = correo;
            this.telefono = telefono;
            this.segmento = segmento;
            this.vip = vip;
            this.proximoEvento = proximoEvento;
        }

        // Getters y setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmpresa() { return empresa; }
        public void setEmpresa(String empresa) { this.empresa = empresa; }
        public String getDocumento() { return documento; }
        public void setDocumento(String documento) { this.documento = documento; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
        public String getSegmento() { return segmento; }
        public void setSegmento(String segmento) { this.segmento = segmento; }
        public boolean isVip() { return vip; }
        public void setVip(boolean vip) { this.vip = vip; }
        public String getProximoEvento() { return proximoEvento; }
        public void setProximoEvento(String proximoEvento) { this.proximoEvento = proximoEvento; }
    }

    // ================== Renderers y Editors para botones en la tabla ==================

    // --- Botón VIP (estrella) ---
    class VipButtonRenderer implements TableCellRenderer {
        private final JButton button = new JButton();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            boolean isVip = clientes.get(row).isVip();
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
            boolean isVip = clientes.get(row).isVip();
            button.setText(isVip ? "★" : "☆");
            button.setForeground(isVip ? Color.ORANGE : Color.GRAY);
            button.setBackground(table.getSelectionBackground());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return clientes.get(row).isVip() ? "★" : "☆";
        }
    }

    // --- Botón Eliminar ---
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