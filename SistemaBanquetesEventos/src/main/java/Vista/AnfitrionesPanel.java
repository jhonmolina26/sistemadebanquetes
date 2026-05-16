package Vista;

import dao.AnfitrionDAO;
import models.Anfitrion;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class AnfitrionesPanel extends JPanel {

    private PlaceholderTextField txtNombre;
    private PlaceholderTextField txtEmpresa;
    private PlaceholderTextField txtDocumento;
    private PlaceholderTextField txtCorreo;
    private PlaceholderTextField txtTelefono;
    private JComboBox<String> cmbSegmento;
    private JCheckBox chkVip;
    private PlaceholderTextField txtProximoEvento;

    private RoundedButton btnNuevo;
    private RoundedButton btnGuardar;

    private JTable tablaAnfitriones;
    private DefaultTableModel modeloTabla;

    private AnfitrionDAO anfitrionDAO;

    private int idSeleccionado = -1;

    public AnfitrionesPanel() {

        anfitrionDAO = new AnfitrionDAO();

        initComponents();

        cargarTabla();
    }

    private void initComponents() {

        setLayout(new BorderLayout(10, 10));

        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        setBackground(new Color(245, 247, 250));

        JLabel lblTitulo = new JLabel("Anfitriones");

        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        lblTitulo.setForeground(new Color(33, 37, 41));

        JLabel lblSubtitulo = new JLabel(
                "Datos del cliente u organizador con enfoque comercial y operativo."
        );

        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblSubtitulo.setForeground(new Color(108, 117, 125));

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1, 0, 2));

        panelTitulo.setOpaque(false);

        panelTitulo.add(lblTitulo);

        panelTitulo.add(lblSubtitulo);

        JPanel panelHeader = new JPanel(new BorderLayout());

        panelHeader.setOpaque(false);

        panelHeader.add(panelTitulo, BorderLayout.WEST);

        add(panelHeader, BorderLayout.NORTH);

        // =========================
        // PANEL CENTRAL MEJORADO
        // =========================

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelFormulario(),
                crearPanelTabla()
        );

        splitPane.setDividerLocation(500);

        splitPane.setResizeWeight(0.35);

        splitPane.setBorder(null);

        splitPane.setOpaque(false);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.anchor = GridBagConstraints.WEST;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTituloForm = new JLabel("Perfil del anfitrión");

        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblTituloForm.setForeground(new Color(33, 37, 41));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(lblTituloForm, gbc);

        gbc.gridwidth = 1;

        txtNombre = new PlaceholderTextField("Nombre del organizador", 20);

        txtEmpresa = new PlaceholderTextField("Empresa o familia", 20);

        txtDocumento = new PlaceholderTextField("Cédula o RUC", 20);

        txtCorreo = new PlaceholderTextField("correo@evento.com", 20);

        txtTelefono = new PlaceholderTextField("0990000000", 20);

        txtProximoEvento = new PlaceholderTextField("Próximo evento", 20);

        cmbSegmento = new JComboBox<>(new String[]{
            "Corporativo",
            "Social",
            "Institucional",
            "Agencia aliada"
        });

        chkVip = new JCheckBox("VIP");

        Font campoFont = new Font("Segoe UI", Font.PLAIN, 12);

        txtNombre.setFont(campoFont);
        txtEmpresa.setFont(campoFont);
        txtDocumento.setFont(campoFont);
        txtCorreo.setFont(campoFont);
        txtTelefono.setFont(campoFont);
        txtProximoEvento.setFont(campoFont);

        cmbSegmento.setFont(campoFont);

        int fila = 1;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Nombre"), gbc);

        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Empresa / familia"), gbc);

        gbc.gridx = 1;
        panel.add(txtEmpresa, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Documento"), gbc);

        gbc.gridx = 1;
        panel.add(txtDocumento, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Correo"), gbc);

        gbc.gridx = 1;
        panel.add(txtCorreo, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Teléfono"), gbc);

        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Segmento"), gbc);

        gbc.gridx = 1;
        panel.add(cmbSegmento, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Próximo evento"), gbc);

        gbc.gridx = 1;
        panel.add(txtProximoEvento, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("VIP"), gbc);

        gbc.gridx = 1;
        panel.add(chkVip, gbc);

        fila++;

        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));

        panelBotones.setOpaque(false);

        btnNuevo = new RoundedButton("Nuevo");

        btnGuardar = new RoundedButton("Guardar perfil");

        btnNuevo.addActionListener(e -> {

            limpiarFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Formulario listo para un nuevo registro.",
                    "Nuevo perfil",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnGuardar.addActionListener(e -> guardarAnfitrion());

        panelBotones.add(btnNuevo);

        panelBotones.add(btnGuardar);

        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {

        JPanel panel = new JPanel(new BorderLayout(0, 10));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTituloTabla = new JLabel("Cartera de clientes");

        lblTituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblTituloTabla.setForeground(new Color(33, 37, 41));

        panel.add(lblTituloTabla, BorderLayout.NORTH);

        String[] columnas = {
            "Anfitrión",
            "Empresa",
            "Documento",
            "Segmento",
            "Teléfono",
            "Correo",
            "Próximo evento",
            "VIP",
            "Acción"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 8;
            }
        };

        tablaAnfitriones = new JTable(modeloTabla);

        // =========================
        // ESTILOS DE TABLA
        // =========================

        tablaAnfitriones.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        tablaAnfitriones.setRowHeight(35);

        tablaAnfitriones.setSelectionBackground(new Color(0, 123, 255));

        tablaAnfitriones.setSelectionForeground(Color.WHITE);

        tablaAnfitriones.setGridColor(new Color(230, 230, 230));

        tablaAnfitriones.setIntercellSpacing(new Dimension(10, 8));

        tablaAnfitriones.setShowGrid(false);

        tablaAnfitriones.setFillsViewportHeight(true);

        tablaAnfitriones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // =========================
        // HEADER
        // =========================

        tablaAnfitriones.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        tablaAnfitriones.getTableHeader().setBackground(
                new Color(248, 249, 250)
        );

        tablaAnfitriones.getTableHeader().setForeground(
                new Color(33, 37, 41)
        );

        tablaAnfitriones.getTableHeader().setReorderingAllowed(false);

        tablaAnfitriones.getTableHeader().setPreferredSize(
                new Dimension(0, 35)
        );

        // =========================
        // ANCHOS DE COLUMNAS
        // =========================

        tablaAnfitriones.getColumnModel().getColumn(0).setPreferredWidth(160);

        tablaAnfitriones.getColumnModel().getColumn(1).setPreferredWidth(160);

        tablaAnfitriones.getColumnModel().getColumn(2).setPreferredWidth(130);

        tablaAnfitriones.getColumnModel().getColumn(3).setPreferredWidth(130);

        tablaAnfitriones.getColumnModel().getColumn(4).setPreferredWidth(130);

        tablaAnfitriones.getColumnModel().getColumn(5).setPreferredWidth(240);

        tablaAnfitriones.getColumnModel().getColumn(6).setPreferredWidth(170);

        tablaAnfitriones.getColumnModel().getColumn(7).setPreferredWidth(90);

        tablaAnfitriones.getColumnModel().getColumn(8).setPreferredWidth(120);

        // =========================
        // BOTÓN ELIMINAR
        // =========================

        tablaAnfitriones.getColumnModel().getColumn(8)
                .setCellRenderer(new ButtonRenderer());

        tablaAnfitriones.getColumnModel().getColumn(8)
                .setCellEditor(new ButtonEditor(new JCheckBox()));

        // =========================
        // EVENTO SELECCIÓN
        // =========================

        tablaAnfitriones.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tablaAnfitriones.getSelectedRow();

                if (fila >= 0) {

                    cargarAnfitrionEnFormulario(fila);
                }
            }
        });

        // =========================
        // SCROLL
        // =========================

        JScrollPane scroll = new JScrollPane(
                tablaAnfitriones,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.getViewport().setBackground(Color.WHITE);

        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(220, 220, 220)
        ));

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Anfitrion> lista = anfitrionDAO.obtenerTodos();

        for (Anfitrion a : lista) {

            Object[] fila = {
                    a.getNombre(),
                    a.getEmpresa(),
                    a.getDocumento(),
                    a.getSegmento(),
                    a.getTelefono(),
                    a.getCorreo(),
                    a.getProximoEvento(),
                    a.isVip() ? "★ VIP" : "Normal",
                    "Eliminar"
            };

            modeloTabla.addRow(fila);
        }
    }

    private void cargarAnfitrionEnFormulario(int fila) {

        List<Anfitrion> lista = anfitrionDAO.obtenerTodos();

        if (fila >= 0 && fila < lista.size()) {

            Anfitrion a = lista.get(fila);

            idSeleccionado = a.getId();

            txtNombre.setText(a.getNombre());

            txtEmpresa.setText(a.getEmpresa());

            txtDocumento.setText(a.getDocumento());

            txtCorreo.setText(a.getCorreo());

            txtTelefono.setText(a.getTelefono());

            cmbSegmento.setSelectedItem(a.getSegmento());

            chkVip.setSelected(a.isVip());

            txtProximoEvento.setText(a.getProximoEvento());

            btnGuardar.setText("Actualizar perfil");
        }
    }

    private void guardarAnfitrion() {

        if (txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!txtCorreo.getText().contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (txtTelefono.getText().trim().length() < 10) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un número de teléfono válido.",
                    "Teléfono inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Anfitrion a = new Anfitrion();

        if (idSeleccionado != -1) {

            a.setId(idSeleccionado);
        }

        a.setNombre(txtNombre.getText().trim());

        a.setEmpresa(txtEmpresa.getText().trim());

        a.setDocumento(txtDocumento.getText().trim());

        a.setCorreo(txtCorreo.getText().trim());

        a.setTelefono(txtTelefono.getText().trim());

        a.setSegmento(cmbSegmento.getSelectedItem().toString());

        a.setVip(chkVip.isSelected());

        a.setProximoEvento(txtProximoEvento.getText().trim());

        boolean exito;

        boolean esNuevo = idSeleccionado == -1;

        if (esNuevo) {

            exito = anfitrionDAO.insertar(a);

        } else {

            exito = anfitrionDAO.actualizar(a);
        }

        if (exito) {

            JOptionPane.showMessageDialog(
                    this,
                    esNuevo
                            ? "Perfil creado correctamente."
                            : "Perfil actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar el anfitrión.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarFormulario() {

        idSeleccionado = -1;

        txtNombre.setText("");

        txtEmpresa.setText("");

        txtDocumento.setText("");

        txtCorreo.setText("");

        txtTelefono.setText("");

        cmbSegmento.setSelectedIndex(0);

        chkVip.setSelected(false);

        txtProximoEvento.setText("");

        tablaAnfitriones.clearSelection();

        btnGuardar.setText("Guardar perfil");
    }

    private void eliminarAnfitrion(int id) {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este anfitrión?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            if (anfitrionDAO.eliminar(id)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Anfitrión eliminado correctamente.",
                        "Eliminado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarTabla();

                limpiarFormulario();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {

            setOpaque(true);

            setFocusPainted(false);

            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            setText("Eliminar");

            setForeground(Color.WHITE);

            setBackground(new Color(220, 53, 69));

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private JButton button;

        private String label;

        private int fila;

        public ButtonEditor(JCheckBox checkBox) {

            super(checkBox);

            button = new JButton();

            button.setOpaque(true);

            button.setForeground(Color.WHITE);

            button.setBackground(new Color(220, 53, 69));

            button.setFocusPainted(false);

            button.addActionListener(e -> {

                List<Anfitrion> lista = anfitrionDAO.obtenerTodos();

                if (fila >= 0 && fila < lista.size()) {

                    eliminarAnfitrion(lista.get(fila).getId());
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {

            this.fila = row;

            label = "Eliminar";

            button.setText(label);

            return button;
        }

        @Override
        public Object getCellEditorValue() {

            return label;
        }
    }
}
