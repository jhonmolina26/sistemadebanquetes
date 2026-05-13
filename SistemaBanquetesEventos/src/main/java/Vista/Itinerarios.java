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
import javax.swing.table.DefaultTableModel;
import ui.components.PlaceholderTextField;
import ui.components.RoundedButton;

public class Itinerarios extends JPanel {

    public Itinerarios() {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setBackground(UiStyle.SOFT);

        JPanel content = new JPanel(new BorderLayout(18, 18));
        content.setOpaque(false);
        content.add(buildFilterCard(), BorderLayout.WEST);
        content.add(buildTableCard(), BorderLayout.CENTER);

        add(buildHeader(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = UiStyle.createCard();
        header.setLayout(new BorderLayout());
        header.add(UiStyle.title("Salones"), BorderLayout.NORTH);
        header.add(UiStyle.subtitle("Disponibilidad, capacidad y montajes sugeridos para cada tipo de evento."), BorderLayout.CENTER);
        return header;
    }

    private JPanel buildFilterCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new java.awt.Dimension(390, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel title = UiStyle.sectionTitle("Disponibilidad");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        addField(card, gbc, 1, "Fecha objetivo", new PlaceholderTextField("30/04/2026", 16));
        addField(card, gbc, 2, "Tipo de montaje", new JComboBox<>(new String[] {
                "Banquete", "Auditorio", "Escuela", "Imperial", "Coctel"
        }));
        addField(card, gbc, 3, "Capacidad minima", new PlaceholderTextField("150 invitados", 16));
        addField(card, gbc, 4, "Ubicacion", new JComboBox<>(new String[] {
                "Interior", "Jardin", "Terraza", "VIP"
        }));

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        legend.setOpaque(false);
        legend.add(UiStyle.badge("Disponible", UiStyle.ACCENT));
        legend.add(UiStyle.badge("Mantenimiento", new java.awt.Color(222, 124, 35)));
        legend.add(UiStyle.badge("Reservado", new java.awt.Color(202, 62, 71)));
        card.add(legend, gbc);

        gbc.gridy = 6;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(new RoundedButton("Buscar salones"));
        actions.add(new RoundedButton("Bloquear fecha", UiStyle.ACCENT, java.awt.Color.WHITE));
        card.add(actions, gbc);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = UiStyle.createCard();
        card.setLayout(new BorderLayout(12, 12));
        card.add(UiStyle.sectionTitle("Mapa de ocupacion"), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Salon", "Capacidad", "Montaje ideal", "Ubicacion", "Estado" }, 0);
        model.addRow(new Object[] { "Gran Salon Imperial", "300", "Banquete", "Interior", "Disponible" });
        model.addRow(new Object[] { "Salon Jardin", "160", "Coctel", "Jardin", "Reservado" });
        model.addRow(new Object[] { "Terraza Vista Rio", "120", "Imperial", "Terraza", "Disponible" });
        model.addRow(new Object[] { "Salon Ejecutivo", "80", "Auditorio", "VIP", "Mantenimiento" });

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
}
