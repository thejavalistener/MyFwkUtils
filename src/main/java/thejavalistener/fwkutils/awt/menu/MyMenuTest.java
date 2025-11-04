package thejavalistener.fwkutils.awt.menu;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyMenuTest
{
    public static void main(String[] args)
    {
        MyAwt.setWindowsLookAndFeel();

        JButton button = new JButton("Mostrar Menú con íconos");
        MyMenu menu = new MyMenu();

        menu.addItem("/Archivo/Abrir", "a.png", e -> System.out.println("Abrir"));
        menu.addItem("/Archivo/Guardar", "b.png", e -> System.out.println("Guardar"));
        menu.addItem("/Archivo/-----", null);
        menu.addItem("/Archivo/Salir", e -> System.exit(0));
        menu.addItem("/Edición/Deshacer", e -> System.out.println("Deshacer"));
        menu.addItem("/Edición/-----", null);
        menu.addItem("/Edición/Copiar", "/Java64/Workspace/MyJavaMusicLibrary/imagenes/roles/piano.png", e -> System.out.println("Copiar"));
        menu.addItem("/Edición/Pegar", "d.png", e -> System.out.println("Pegar"));

        button.addActionListener(e -> menu.show(button, 1, 10, 0)); // 1: derecha

        JFrame frame = new JFrame("Custom Menu Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(button);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
