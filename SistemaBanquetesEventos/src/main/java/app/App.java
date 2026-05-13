package app;

import javax.swing.SwingUtilities;
import Vista.MenuPrincipal;
import Vista.UiStyle;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyle.installLookAndFeel();
            new MenuPrincipal().setVisible(true);
        });
    }
}


