package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class Reserva extends JPanel {

    private PlaceholderTextField txtCodigo;
    private PlaceholderTextField txtFecha;
    private PlaceholderTextField txtInvitados;
    private PlaceholderTextField txtHorario;
    private PlaceholderTextField txtContacto;
    private JComboBox<String> cbTipoEvento;
    private JComboBox<String> cbSalon;
    private JComboBox<String> cbPaquete;

    public Reserva() {
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
        panel.add(UiStyle.subtitle("Registro visual de eventos, fechas, servicios y configuracion operativa."), BorderLayout.CENTER);
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
        card.setPreferredSize(new java.awt.Dimension(430, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(UiStyle.sectionTitle("Ficha de contratacion"), gbc);

        txtCodigo = new PlaceholderTextField("Ej. EVT-2026-042", 18);
        txtFecha = new PlaceholderTextField("27/04/2026", 18);
        txtInvitados = new PlaceholderTextField("180 invitados", 18);
        txtHorario = new PlaceholderTextField("18:00 - 01:00", 18);
        txtContacto = new PlaceholderTextField("Nombre del coordinador", 18);
        cbTipoEvento = new JComboBox<>(new String[] { "Boda", "Corporativo", "XV Anos", "Graduacion", "Cena privada" });
        cbSalon = new JComboBox<>(new String[] { "Gran Salon Imperial", "Salon Jardin", "Terraza Vista Rio", "Salon Ejecutivo" });
        cbPaquete = new JComboBox<>(new String[] { "Premium", "Tradicional", "Corporativo", "Personalizado" });

        addField(card, gbc, 1, "Codigo", txtCodigo);
        addField(card, gbc, 2, "Fecha", txtFecha);
        addField(card, gbc, 3, "Tipo de evento", cbTipoEvento);
        addField(card, gbc, 4, "Salon", cbSalon);
        addField(card, gbc, 5, "Invitados", txtInvitados);
        addField(card, gbc, 6, "Horario", txtHorario);
        addField(card, gbc, 7, "Paquete", cbPaquete);
        addField(card, gbc, 8, "Contacto", txtContacto);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        card.add(new JLabel("Servicios contratados"), gbc);

        gbc.gridy = 10;
        JTextArea servicios = new JTextArea(5, 24);
        servicios.setLineWrap(true);
        servicios.setWrapStyleWord(true);
        servicios.setText("Catering premium, pista de baile, sonido profesional, decoracion floral y coordinacion general.");
        servicios.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        card.add(new JScrollPane(servicios), gbc);

        gbc.gridy = 11;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(new RoundedButton("Guardar borrador"));
        actions.add(new RoundedButton("Confirmar evento", UiStyle.ACCENT, java.awt.Color.WHITE));
        card.add(actions, gbc);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Agenda de eventos"), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Codigo", "Evento", "Fecha", "Salon", "Invitados", "Estado" }, 0);
        model.addRow(new Object[] { "EVT-042", "Boda de gala", "27/04/2026", "Gran Salon Imperial", "220", "Confirmado" });
        model.addRow(new Object[] { "EVT-043", "Lanzamiento marca", "29/04/2026", "Salon Ejecutivo", "95", "En propuesta" });
        model.addRow(new Object[] { "EVT-044", "Graduacion", "02/05/2026", "Terraza Vista Rio", "180", "Pendiente anticipo" });
        model.addRow(new Object[] { "EVT-045", "Cena empresarial", "04/05/2026", "Salon Jardin", "80", "Bloqueado" });

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

    public double precioPorLugar(String lugar) {
        return 20;
    }

    public double calcularTotal(String lugar, int personas) {
        if (personas < 0) {
            throw new IllegalArgumentException("La cantidad de invitados no puede ser negativa");
        }
        return precioPorLugar(lugar) * personas;
    }
}
