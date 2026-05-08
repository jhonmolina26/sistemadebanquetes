package ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RoundedButton extends JButton {
    public RoundedButton(String text) {
        this(text, new Color(24, 119, 242), Color.WHITE);
    }

    public RoundedButton(String text, Color background, Color foreground) {
        super(text);
        setBackground(background);
        setForeground(foreground);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, 40));
        putClientProperty("JButton.buttonType", "roundRect");
    }
}
