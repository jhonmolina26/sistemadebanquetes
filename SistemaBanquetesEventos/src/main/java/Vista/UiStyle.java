package Vista;

import java.awt.Color;
import java.awt.Font;
import javax.swing.LookAndFeel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public final class UiStyle {
    public static final Color BRAND = new Color(24, 119, 242);
    public static final Color ACCENT = new Color(15, 157, 88);
    public static final Color SOFT = new Color(244, 247, 251);
    public static final Color TEXT = new Color(39, 52, 67);

    private UiStyle() {
    }

    public static void installLookAndFeel() {
        try {
            Class<?> lafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            LookAndFeel lookAndFeel = (LookAndFeel) lafClass.getDeclaredConstructor().newInstance();
            UIManager.setLookAndFeel(lookAndFeel);
            UIManager.put("Button.arc", 18);
            UIManager.put("Component.arc", 16);
            UIManager.put("TextComponent.arc", 16);
            UIManager.put("ScrollBar.showButtons", true);
            UIManager.put("TabbedPane.tabHeight", 40);
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }

    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 228, 236)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(110, 120, 136));
        return label;
    }

    public static JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel badge(String text, Color background) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(background);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        return label;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 8));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 234, 255));
        table.setSelectionForeground(TEXT);
    }
}
