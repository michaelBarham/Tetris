package Utilities;

// code copied from Simon Lucas
// code copied by Udo Kruschwitz
// code completed by Michael Barham

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class JEasyFrame extends JFrame {
    public Component comp;
    public JEasyFrame(Component comp, String title) {
        super(title);
        this.comp = comp;
        getContentPane().add(BorderLayout.CENTER, comp);
        pack();
        this.setVisible(true);
        //setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();


    }

}
