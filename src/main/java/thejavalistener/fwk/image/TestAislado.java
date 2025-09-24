package thejavalistener.fwk.image;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestAislado{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Canvas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            MyImageCanvas canvas = new MyImageCanvas();

            // Cargar imagen inicial
            canvas.setImageFromFile("test1.jpg");

            // Botón para cambiar imagen
            JButton cambiar = new JButton("Cambiar imagen");
            cambiar.addActionListener(e -> canvas.setImageFromFile("test2.png"));

            // Layout manual
            JPanel root = new JPanel(null); // sin layout manager
            JPanel panel = canvas.c();
            panel.setBounds(10, 10, 280, 280); // tamaño fijo
            cambiar.setBounds(10, 300, 150, 30);

            root.add(panel);
            root.add(cambiar);

            frame.setContentPane(root);
            frame.setSize(320, 400);
            frame.setVisible(true);
        });
    }
}