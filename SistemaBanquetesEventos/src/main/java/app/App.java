package app;

import javax.swing.SwingUtilities;
import Vista.VentanaPrincipal;
import Vista.UiStyle;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyle.installLookAndFeel();
            new VentanaPrincipal().setVisible(true);
        });
    }
}