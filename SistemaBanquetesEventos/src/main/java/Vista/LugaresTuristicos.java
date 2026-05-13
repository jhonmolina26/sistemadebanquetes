package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class LugaresTuristicos extends JPanel {
    private DefaultTableModel modelo;
    private PlaceholderTextField txtBuscar;
    private final List<Lugar> lista = new ArrayList<>();
    private String categoriaActiva = "Todos";

    public LugaresTuristicos() {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildCatalogCard(), BorderLayout.CENTER);
        content.add(buildAssignmentCard(), BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = UiStyle.createCard();
        header.setLayout(new BorderLayout());
        header.add(UiStyle.title("Menus"), BorderLayout.NORTH);
        header.add(UiStyle.subtitle("Catalogo de platos, filtros por categoria y asignacion visual por contratacion."), BorderLayout.CENTER);
        return header;
    }

    private JPanel buildCatalogCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));

        JPanel controls = new JPanel(new BorderLayout(12, 12));
        controls.setOpaque(false);
        controls.add(UiStyle.sectionTitle("Catalogo gastronómico"), BorderLayout.WEST);

        txtBuscar = new PlaceholderTextField("Buscar plato, paquete o restriccion", 24);
        controls.add(txtBuscar, BorderLayout.CENTER);

        JPanel categories = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        categories.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        for (String categoria : new String[] { "Todos", "Entradas", "Fuertes", "Postres" }) {
            JRadioButton radio = new JRadioButton(categoria);
            radio.setOpaque(false);
            radio.addActionListener(e -> {
                categoriaActiva = categoria;
                filtrar();
            });
            if ("Todos".equals(categoria)) {
                radio.setSelected(true);
            }
            group.add(radio);
            categories.add(radio);
        }
        controls.add(categories, BorderLayout.SOUTH);
        card.add(controls, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[] { "Categoria", "Plato", "Paquete", "Restriccion", "Precio pax" }, 0);
        JTable tabla = new JTable(modelo);
        UiStyle.styleTable(tabla);
        card.add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarLugares();
        filtrar();
        txtBuscar.addActionListener(e -> filtrar());
        return card;
    }

    private JPanel buildAssignmentCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new java.awt.Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Asignar menu"), gbc);

        addField(card, gbc, 1, "Contratacion", new JComboBox<>(new String[] {
                "EVT-042 Boda de gala", "EVT-043 Lanzamiento marca", "EVT-044 Graduacion"
        }));
        addField(card, gbc, 2, "Paquete", new JComboBox<>(new String[] {
                "Menu Signature", "Menu Tradicional", "Menu Ejecutivo", "Menu Kids"
        }));
        addField(card, gbc, 3, "Servicio", new JComboBox<>(new String[] {
                "Emplatado", "Buffet", "Estaciones", "Coctel reforzado"
        }));
        addField(card, gbc, 4, "Restricciones", new PlaceholderTextField("Vegetariano, sin gluten, sin mariscos", 18));
        addField(card, gbc, 5, "Observaciones", new PlaceholderTextField("Montaje especial para mesa principal", 18));

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        badges.setOpaque(false);
        badges.add(UiStyle.badge("3 menus sugeridos", UiStyle.BRAND));
        badges.add(UiStyle.badge("2 alertas alimentarias", new java.awt.Color(222, 124, 35)));
        card.add(badges, gbc);

        gbc.gridy = 7;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(new RoundedButton("Previsualizar"));
        actions.add(new RoundedButton("Asignar menu", UiStyle.ACCENT, java.awt.Color.WHITE));
        card.add(actions, gbc);
        return card;
    }

    private void cargarLugares() {
        lista.clear();
        añadir("Entradas", "Mini bruschettas", "Menu Signature", "Contiene gluten", 7.50);
        añadir("Entradas", "Ceviche tropical", "Menu Signature", "Mariscos", 8.90);
        añadir("Entradas", "Crema de zapallo", "Menu Tradicional", "Vegetariano", 5.80);
        añadir("Fuertes", "Lomo en salsa de vino", "Menu Premium", "Ninguna", 19.00);
        añadir("Fuertes", "Pollo mediterraneo", "Menu Ejecutivo", "Ninguna", 14.50);
        añadir("Fuertes", "Risotto de hongos", "Menu Veggie", "Vegetariano", 16.20);
        añadir("Postres", "Cheesecake de maracuya", "Menu Signature", "Lacteos", 6.25);
        añadir("Postres", "Brownie tibio", "Menu Kids", "Lacteos y gluten", 5.90);
        añadir("Postres", "Fruta fresca gourmet", "Menu Light", "Apto vegano", 4.75);
    }

    private void añadir(String categoria, String plato, String paquete, String restriccion, double precio) {
        lista.add(new Lugar(categoria, plato, paquete, restriccion, precio));
    }

    private void filtrar() {
        String t = txtBuscar.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        for (Lugar l : lista) {
            boolean categoriaValida = "Todos".equals(categoriaActiva) || l.categoria.equalsIgnoreCase(categoriaActiva);
            boolean textoValido = t.isEmpty()
                    || l.plato.toLowerCase().contains(t)
                    || l.paquete.toLowerCase().contains(t)
                    || l.restriccion.toLowerCase().contains(t);
            if (categoriaValida && textoValido) {
                modelo.addRow(new Object[] { l.categoria, l.plato, l.paquete, l.restriccion, l.precio });
            }
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component component) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private static class Lugar {
        String categoria;
        String plato;
        String paquete;
        String restriccion;
        double precio;

        Lugar(String categoria, String plato, String paquete, String restriccion, double precio) {
            this.categoria = categoria;
            this.plato = plato;
            this.paquete = paquete;
            this.restriccion = restriccion;
            this.precio = precio;
        }
    }
}
