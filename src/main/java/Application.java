import javax.swing.*;

public class Application extends JFrame {

    public Application() {
        initUI();
    }

    public final void initUI() {

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);
        panel.setToolTipText("A Panel container");

        JButton button = new JButton("Button");
        button.setBounds(100, 60, 100, 30);
        button.setToolTipText("A button component");

        panel.add(button);

        setTitle("Tooltip");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.setVisible(true);
    }
}
