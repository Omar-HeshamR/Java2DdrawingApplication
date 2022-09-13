package java2ddrawingapplication;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Omar
 */

public class Java2dDrawingApplication
{
    public static void main(String[] args)
    {
        DrawingApplicationFrame frame = new DrawingApplicationFrame();
        frame.setTitle("Java 2D Drawings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(330, 150, 650,500);
        frame.setVisible(true);
    }
    
}
