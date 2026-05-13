package Vista;

import controller.MenuController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import models.Menu;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class MenusPanel extends JPanel {

    private final MenuController controller = new MenuController();

    private JComboBox<String> cbCategoria;
    private PlaceholderTextField txtPlato;
    private PlaceholderTextField txtPaquete;
    private PlaceholderTextField txtRestriccion;
    private PlaceholderTextField txtPrecioPax;

    private JTable table;
    private DefaultTableModel model;

    private RoundedButton btnNuevo;
    private RoundedButton btnGuardar;
    private RoundedButton btnEliminar;

    private List<Menu> menus;
    private int selectedIndex = -1;
    private int selectedId = -1;

    private final String[] categorias = {"Entradas", "Fuertes", "Postres", "Bebidas"};
    private final String[] columnNames = {"Categoria", "Plato", "Paquete", "Restriccion", "Precio", "Accion"};

    public MenusPanel() {
        menus = controller.obtenerTodos();

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
        header.add(UiStyle.title("Menus"), BorderLayout.NORTH);
        header.add(UiStyle.subtitle("Gestion del Catálogo Gastronómico."), BorderLayout.CENTER);
        return header;
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
        card.setPreferredSize(new Dimension(420, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Ficha del menu"), gbc);

        cbCategoria = new JComboBox<>(categorias);
        txtPlato = new PlaceholderTextField("Nombre del plato", 18);
        txtPaquete = new PlaceholderTextField("Paquete comercial", 18);
        txtRestriccion = new PlaceholderTextField("Alergias o restriccion alimentaria", 18);
        txtPrecioPax = new PlaceholderTextField("0.00", 18);

        addField(card, gbc, 1, "Categoria", cbCategoria);
        addField(card, gbc, 2, "Plato", txtPlato);
        addField(card, gbc, 3, "Paquete", txtPaquete);
        addField(card, gbc, 4, "Restriccion", txtRestriccion);
        addField(card, gbc, 5, "Precio", txtPrecioPax);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        btnNuevo = new RoundedButton("Nuevo");
        btnGuardar = new RoundedButton("Guardar menu");
        btnEliminar = new RoundedButton("Eliminar", new java.awt.Color(202, 62, 71), java.awt.Color.WHITE);

        actions.add(btnNuevo);
        actions.add(btnGuardar);
        actions.add(btnEliminar);
        card.add(actions, gbc);

        btnNuevo.addActionListener(e -> {
            deselectTable();
            clearForm();
            selectedIndex = -1;
            selectedId = -1;
            updateButtonStates();
        });

        btnGuardar.addActionListener(e -> guardarMenu());
        btnEliminar.addActionListener(e -> eliminarMenu());

        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Menus registrados"), BorderLayout.NORTH);

        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UiStyle.styleTable(table);
        table.getColumnModel().getColumn(5).setCellRenderer(new DeleteButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new DeleteButtonEditor());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                selectedIndex = viewRow;
                selectedId = menus.get(viewRow).getId();
                loadMenuFromRow(viewRow);
            } else {
                selectedIndex = -1;
                selectedId = -1;
            }
            updateButtonStates();
        });

        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void guardarMenu() {
        String plato = txtPlato.getText().trim();
        if (plato.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El plato es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal precioPax = parsePrecio(txtPrecioPax.getText().trim());
        if (precioPax == null) {
            JOptionPane.showMessageDialog(this, "Ingrese un precio valido para precio pax.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Menu menu = new Menu();
        menu.setCategoria((String) cbCategoria.getSelectedItem());
        menu.setPlato(plato);
        menu.setPaquete(txtPaquete.getText().trim());
        menu.setRestriccion(txtRestriccion.getText().trim());
        menu.setPrecioPax(precioPax);

        boolean esNuevo = selectedId == -1;
        if (!esNuevo) {
            menu.setId(selectedId);
        }

        boolean exito = controller.guardarMenu(menu, esNuevo);
        if (!exito) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el menu. Revise la conexion o los datos.", "Error", JOptionPane.ERROR_MESSAGE);
            menus = controller.obtenerTodos();
            refreshTable();
            return;
        }

        menus = controller.obtenerTodos();
        refreshTable();
        clearForm();
        selectedIndex = -1;
        selectedId = -1;
        updateButtonStates();
    }

    private void eliminarMenu() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un menu para eliminar.", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el menu seleccionado?", "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarMenu(selectedId)) {
                menus = controller.obtenerTodos();
                refreshTable();
                clearForm();
                selectedIndex = -1;
                selectedId = -1;
                updateButtonStates();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el menu.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadMenuFromRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < menus.size()) {
            Menu menu = menus.get(rowIndex);
            cbCategoria.setSelectedItem(menu.getCategoria());
            txtPlato.setText(menu.getPlato());
            txtPaquete.setText(menu.getPaquete());
            txtRestriccion.setText(menu.getRestriccion());
            txtPrecioPax.setText(menu.getPrecioPax() != null ? menu.getPrecioPax().toPlainString() : "");
        }
    }

    private void clearForm() {
        cbCategoria.setSelectedIndex(0);
        txtPlato.setText("");
        txtPaquete.setText("");
        txtRestriccion.setText("");
        txtPrecioPax.setText("");
        selectedId = -1;
    }

    private void deselectTable() {
        table.clearSelection();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Menu menu : menus) {
            model.addRow(new Object[]{
                menu.getCategoria(),
                menu.getPlato(),
                menu.getPaquete(),
                menu.getRestriccion(),
                menu.getPrecioPax(),
                "Eliminar"
            });
        }
    }

    private void updateButtonStates() {
        if (selectedIndex >= 0) {
            btnGuardar.setText("Actualizar menu");
            btnEliminar.setEnabled(true);
        } else {
            btnGuardar.setText("Guardar menu");
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

    private BigDecimal parsePrecio(String texto) {
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    class DeleteButtonRenderer implements TableCellRenderer {
        private final JButton button = new JButton("Eliminar");

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
            button = new JButton("Eliminar");
            button.setForeground(Color.RED);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    if (row >= 0 && row < menus.size()) {
                        selectedIndex = row;
                        selectedId = menus.get(row).getId();
                        loadMenuFromRow(row);
                        updateButtonStates();
                        eliminarMenu();
                    }
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
