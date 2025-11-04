package thejavalistener.fwkutils.awt.form;

import javax.swing.JButton;

import thejavalistener.fwkutils.awt.link.MyLink;
import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.awt.text.MyTextField;
import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyFormTest
{
	public static void main(String[] args)
	{
		MyAwt.setWindowsLookAndFeel();
		
		MyForm form = new MyForm(300);
		form.addRow().add(new MyLink("Nombre").c()).add(new MyTextField().c()).layout(.3,.7);
		form.addRow().add(new MyLink("Apellido").c()).add(new MyTextField().c()).layout(.3,.7);
		form.addRow().add(new MyLink("Dirección").c()).add(new MyTextField().c()).layout(.3,.7);
		form.addRow("secret").add(new MyLink("Secreto").c()).add(new MyTextField().c()).layout(.3,.7);
		form.setVisibleRow("secret",false);
		form.addSeparator();
		form.addRow().add(new JButton("Aceptar")).add(new JButton("Cancelar"));
		form.makeForm();
		
		MyTestUI.test(form.c())
		        .addButton("Show Secret",l->form.setVisibleRow("secret",true))
		        .addButton("Hide secret",l->form.setVisibleRow("secret",false))
		        .run();
	}
}
