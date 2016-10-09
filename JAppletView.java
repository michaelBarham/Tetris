package Utilities;

import javax.swing.*;
import java.awt.*;

/*
 * Created by User on 08/12/2014.
*/

public class JAppletView extends JApplet {
    public Component comp;
    public int h, w, size;

    public void init() {
        add(BorderLayout.CENTER, comp);
        setSize(h*size, w*size);
        repaint();
    }
}
