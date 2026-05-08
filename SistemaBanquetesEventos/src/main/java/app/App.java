package app;

import javax.swing.SwingUtilities;
import ui.MenuPrincipal;
import ui.UiStyle;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyle.installLookAndFeel();
            new MenuPrincipal().setVisible(true);
        });
    }
}


