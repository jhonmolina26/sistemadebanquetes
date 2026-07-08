package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestion de Banquetes y Eventos");
        setSize(1320, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UiStyle.SOFT);
        setLayout(new BorderLayout(16, 16));

        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);

        ContratacionesPanel contratacionesPanel = new ContratacionesPanel();
        VSalones salonesPanel = new VSalones();
        AnfitrionesPanel anfitrionesPanel = new AnfitrionesPanel();
        MenusPanel menusPanel = new MenusPanel();
        PagosPanel pagosPanel = new PagosPanel();

        tabs.addTab("Contrataciones", contratacionesPanel);
        tabs.addTab("Salones", salonesPanel);
        tabs.addTab("Anfitriones", anfitrionesPanel);
        tabs.addTab("Menus", menusPanel);
        tabs.addTab("Pagos", pagosPanel);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 18, 18, 18));
        content.add(tabs, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel banner = new JPanel(new GridBagLayout());
        banner.setBackground(Color.WHITE);
        banner.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        banner.add(UiStyle.title("Sistema de Gestion de Banquetes y Eventos"), gbc);

        gbc.gridy = 1;
        banner.add(UiStyle.subtitle("Panel ejecutivo para salones, contrataciones, anfitriones, menus y pagos."), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel status = new JLabel("Operacion del dia lista", SwingConstants.CENTER);
        status.setOpaque(true);
        status.setBackground(new Color(230, 244, 234));
        status.setForeground(new Color(36, 97, 56));
        status.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        banner.add(status, gbc);
        return banner;
    }
}