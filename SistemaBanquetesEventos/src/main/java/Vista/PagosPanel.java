package Vista;

import controller.PagoController;
import models.Pago;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class PagosPanel extends JPanel {

    private final PagoController controller;
    private JComboBox<String> cbEstado;
    private JComboBox<String> cbContratacion;
    private PlaceholderTextField txtAnticipo;
    private PlaceholderTextField txtSaldo;
    private PlaceholderTextField txtFactura;
    private PlaceholderTextField txtMetodo;
    private JTable table;
    private DefaultTableModel tableModel;

    public PagosPanel() {
        controller = new PagoController();
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);
        add(buildHeader(), BorderLayout.NORTH);
        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildControlCard(), BorderLayout.WEST);
        content.add(buildTableCard(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
        cargarDatosTabla();
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
        card.setPreferredSize(new java.awt.Dimension(450, 0));

        cbEstado = new JComboBox<>(new String[]{"Pendiente", "Parcial", "Facturado"});
        cbContratacion = new JComboBox<>();
        txtAnticipo = new PlaceholderTextField("0.00", 16);
        txtSaldo = new PlaceholderTextField("0.00", 16);
        txtSaldo.setEditable(false);
        txtFactura = new PlaceholderTextField("Ej. FAC-001245", 16);
        txtMetodo = new PlaceholderTextField("Transferencia / Tarjeta / Efectivo", 16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Panel de cobros"), gbc);

        addField(card, gbc, 1, "Estado", cbEstado);
        addField(card, gbc, 2, "Contratación", cbContratacion);
        addField(card, gbc, 3, "Anticipo", txtAnticipo);
        addField(card, gbc, 4, "Saldo pendiente", txtSaldo);
        addField(card, gbc, 5, "Factura", txtFactura);
        addField(card, gbc, 6, "Metodo", txtMetodo);

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        
        JPanel actionsGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        actionsGrid.setOpaque(false);

        RoundedButton btnRegistrar = new RoundedButton("Registrar");
        RoundedButton btnActualizar = new RoundedButton("Actualizar", new java.awt.Color(13, 110, 253), java.awt.Color.WHITE);
        RoundedButton btnFactura = new RoundedButton("Emitir factura", UiStyle.ACCENT, java.awt.Color.WHITE);
        RoundedButton btnEliminar = new RoundedButton("Eliminar", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);

        btnRegistrar.addActionListener(e -> guardarCambiosBD(true));
        btnActualizar.addActionListener(e -> guardarCambiosBD(false));
        btnEliminar.addActionListener(e -> eliminarRegistroBD());
        btnFactura.addActionListener(e -> emitirFacturaProceso());

        actionsGrid.add(btnRegistrar);
        actionsGrid.add(btnActualizar);
        actionsGrid.add(btnFactura);
        actionsGrid.add(btnEliminar);
        
        card.add(actionsGrid, gbc);
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

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Estado financiero por evento"), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(
                new String[]{"ID Pago", "ID Evento", "Evento", "Total", "Anticipo", "Saldo", "Factura", "Estado", "Metodo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UiStyle.styleTable(table);
        ocultarColumna(0);
        ocultarColumna(1);
        ocultarColumna(8);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                llenarFormularioDesdeTabla();
            }
        });
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private void ocultarColumna(int index) {
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setWidth(0);
    }

    private void cargarDatosTabla() {
        tableModel.setRowCount(0);
        cbContratacion.removeAllItems();
        List<Pago> listaPagos = controller.obtenerTodos();
        List<String> eventosAgregados = new ArrayList<>();
        for (Pago pago : listaPagos) {
            String codigo = pago.getCodigoEvento();
            if (codigo != null && !eventosAgregados.contains(codigo)) {
                eventosAgregados.add(codigo);
                cbContratacion.addItem(codigo);
            }
            tableModel.addRow(new Object[]{pago.getId(), pago.getEventoId(), codigo, pago.getTotal(), pago.getAnticipo(), pago.getSaldo(), pago.getFactura(), pago.getEstado(), pago.getMetodo()});
        }
    }

    private void llenarFormularioDesdeTabla() {
        int fila = table.getSelectedRow();
        cbContratacion.setSelectedItem(table.getValueAt(fila, 2).toString());
        txtAnticipo.setText(table.getValueAt(fila, 4).toString());
        txtSaldo.setText(table.getValueAt(fila, 5).toString());
        txtFactura.setText(table.getValueAt(fila, 6).toString());
        cbEstado.setSelectedItem(table.getValueAt(fila, 7).toString());
        txtMetodo.setText(table.getValueAt(fila, 8).toString());
    }

    private void guardarCambiosBD(boolean esNuevo) {
        int fila = table.getSelectedRow();
        if (!esNuevo && fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.");
            return;
        }
        try {
            Pago pago = new Pago();
            if (!esNuevo) pago.setId((int) table.getValueAt(fila, 0));
            pago.setEventoId((int) table.getValueAt(fila, 1));
            pago.setTotal((BigDecimal) table.getValueAt(fila, 3));
            pago.setAnticipo(new BigDecimal(txtAnticipo.getText().trim()));
            pago.setFactura(txtFactura.getText().trim());
            pago.setMetodo(txtMetodo.getText().trim());
            pago.setEstado(cbEstado.getSelectedItem().toString());

            if (controller.guardarPago(pago, esNuevo)) {
                JOptionPane.showMessageDialog(this, esNuevo ? "Registro creado." : "Registro actualizado.");
                cargarDatosTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Verifique que el monto sea numérico.");
        }
    }

    private void emitirFacturaProceso() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro primero.");
            return;
        }
        String numFactura = "FAC-" + (int) (Math.random() * 100000);
        txtFactura.setText(numFactura);
        cbEstado.setSelectedItem("Facturado");

        String resumen = "DETALLES DE FACTURACIÓN\n\n"
                + "Evento: " + cbContratacion.getSelectedItem() + "\n"
                + "Factura: " + numFactura + "\n"
                + "Monto Total: $" + table.getValueAt(fila, 3) + "\n"
                + "Anticipo: $" + txtAnticipo.getText() + "\n"
                + "Estado: Facturado";

        JOptionPane.showMessageDialog(this, resumen, "Factura Emitida", JOptionPane.INFORMATION_MESSAGE);
        guardarCambiosBD(false);
    }

    private void eliminarRegistroBD() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminarPago((int) table.getValueAt(fila, 0))) {
                JOptionPane.showMessageDialog(this, "Registro eliminado.");
                cargarDatosTabla();
            }
        }
    }
}