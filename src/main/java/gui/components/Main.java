package gui.components;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame
{
    public Main() throws Exception {
        getContentPane().setLayout(new BorderLayout());

        final JPanel panel = createPanel();
        final JScrollPane scrollpane = new JScrollPane(panel);

        JButton button = new JButton("Scroll to bottom!");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                panel.scrollRectToVisible(
                        new Rectangle(0, panel.getHeight()-1, 1, 1));
            }
        });

        getContentPane().add(BorderLayout.NORTH, button);
        getContentPane().add(BorderLayout.CENTER, scrollpane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public static JPanel createPanel() throws Exception {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(50, 20, 10, 10));

        for (int i=0; i<50; i++) {
            for (int j=0; j<20; j++) {
                JLabel label = new JLabel("label " + i + ", " + j);
                panel.add(label);
            }
        }

        return panel;
    }

    public static void main(String [] args) throws Exception  {
        Main main = new Main();
        main.setSize(600, 600);
        main.setVisible(true);
    }
}
