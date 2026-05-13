package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class PagosPanel extends JPanel {
    JComboBox<String> cbProvincia;
    JComboBox<String> cbLugar;
    private PlaceholderTextField txtAnticipo;
    private PlaceholderTextField txtSaldo;
    private PlaceholderTextField txtFactura;
    private PlaceholderTextField txtMetodo;
    private final Map<String, List<LugarInfo>> data = new HashMap<>();

    public PagosPanel() {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);
        cargarDatosLugares();
        cbProvincia = new JComboBox<>(data.keySet().toArray(new String[0]));
        cbLugar = new JComboBox<>();
        cbProvincia.addActionListener(e -> cargarLugares());
        cargarLugares();

        add(buildHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildControlCard(), BorderLayout.WEST);
        content.add(buildTableCard(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private void cargarDatosLugares() {
        addLugar("Pendiente", "EVT-042 Boda de gala", 4500);
        addLugar("Pendiente", "EVT-044 Graduacion", 2800);
        addLugar("Parcial", "EVT-043 Lanzamiento marca", 1650);
        addLugar("Facturado", "EVT-041 Cena de directorio", 0);
    }

    private void addLugar(String prov, String lugar, double precio) {
        data.computeIfAbsent(prov, k -> new ArrayList<>()).add(new LugarInfo(lugar, precio));
    }

    private void cargarLugares() {
        cbLugar.removeAllItems();
        String prov = (String) cbProvincia.getSelectedItem();
        if (prov == null)
            return;
        for (LugarInfo li : data.get(prov))
            cbLugar.addItem(li.nombre);
    }

    private JPanel buildHeader() {
        JPanel header = UiStyle.createCard();
        header.setLayout(new BorderLayout());
        header.add(UiStyle.title("Pagos"), BorderLayout.NORTH);
        header.add(UiStyle.subtitle("Control visual de anticipos, saldos pendientes y emision de facturas."), BorderLayout.CENTER);
        return header;
    }

    private JPanel buildControlCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new java.awt.Dimension(420, 0));

        txtAnticipo = new PlaceholderTextField("USD 2.500,00", 16);
        txtSaldo = new PlaceholderTextField("USD 4.500,00", 16);
        txtFactura = new PlaceholderTextField("FAC-001245", 16);
        txtMetodo = new PlaceholderTextField("Transferencia / tarjeta / efectivo", 16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Panel de cobros"), gbc);

        addField(card, gbc, 1, "Estado", cbProvincia);
        addField(card, gbc, 2, "Contratacion", cbLugar);
        addField(card, gbc, 3, "Anticipo", txtAnticipo);
        addField(card, gbc, 4, "Saldo pendiente", txtSaldo);
        addField(card, gbc, 5, "Factura", txtFactura);
        addField(card, gbc, 6, "Metodo", txtMetodo);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel summary = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        summary.setOpaque(false);
        summary.add(UiStyle.badge("Anticipo recibido", UiStyle.ACCENT));
        summary.add(UiStyle.badge("Saldo por cobrar", new java.awt.Color(202, 62, 71)));
        card.add(summary, gbc);

        gbc.gridy = 8;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(new RoundedButton("Registrar anticipo"));
        actions.add(new RoundedButton("Emitir factura", UiStyle.ACCENT, java.awt.Color.WHITE));
        card.add(actions, gbc);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Estado financiero por evento"), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Evento", "Total", "Anticipo", "Saldo", "Factura", "Estado" }, 0);
        model.addRow(new Object[] { "EVT-042 Boda de gala", "7.000", "2.500", "4.500", "Pendiente", "Pendiente" });
        model.addRow(new Object[] { "EVT-043 Lanzamiento marca", "3.650", "2.000", "1.650", "FAC-001244", "Parcial" });
        model.addRow(new Object[] { "EVT-041 Cena de directorio", "1.850", "1.850", "0", "FAC-001243", "Facturado" });

        JTable table = new JTable(model);
        UiStyle.styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component component) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private static class LugarInfo {
        String nombre;
        double precio;

        LugarInfo(String n, double p) {
            nombre = n;
            precio = p;
        }
    }

    public int testCantidadLugaresEnProvincia(String prov) {
        if (!data.containsKey(prov)) {
            return 0;
        }
        return data.get(prov).size();
    }

    public java.util.List<String> testObtenerLugaresDeProvincia(String prov) {
        java.util.List<String> lista = new java.util.ArrayList<>();
        if (!data.containsKey(prov)) {
            return lista;
        }

        for (LugarInfo li : data.get(prov)) {
            lista.add(li.nombre);
        }
        return lista;
    }
}
