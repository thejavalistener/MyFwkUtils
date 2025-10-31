package thejavalistener.fwkutils.awt.testui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import thejavalistener.fwkutils.awt.GridLayout2;
import thejavalistener.fwkutils.awt.MyAwt;

public class MyTestUI implements MyTestUIInterface
{
	private JFrame frame;
	private List<ButtonUI> buttons;
	private Map<String,JTextField> textfields;
	private JPanel pSouth;
	private JPanel pNorth;

	private MyTestUI component(Component c)
	{
		buttons=new ArrayList<>();
		textfields=new LinkedHashMap<>();

		frame=new JFrame("Testing "+c.getClass().getSimpleName());
		frame.add(c,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(pNorth=new JPanel(new GridLayout2(1,0)),BorderLayout.NORTH);
		frame.add(pSouth=new JPanel(new FlowLayout()),BorderLayout.SOUTH);
		frame.setSize(480,350);
		return this;
	}
	
	public boolean isVisible()
	{
		return frame.isVisible();
	}
	
	public void setVisible(boolean b)
	{
		frame.setVisible(b);
	}

	public MyTestUI size(int w, int h)
	{
		frame.setSize(w,h);
		return this;
	}
	
	public MyTestUI pack()
	{
		frame.pack();
		return this;
	}

	public MyTestUI addButton(String label, ActionUIListener mt)
	{
		ButtonUI b = new ButtonUI(label,this);
		b.setActionUIListener(mt);
		pSouth.add(b);
		return this;
	}
	
	public static MyTestUI test(Component c)
	{
		return new MyTestUI().component(c);
	}
	
	public JTextField getTextField(String tfname)
	{
		return (JTextField)textfields.get(tfname);
	}

	public MyTestUI run()
	{
		repack(frame);
		frame.setVisible(true);
		return this;
	}
	
	public MyTestUI addTextField(String string)
	{
		return addTextField(string,10);
	}

	public int getInt(String tfName)
	{
		return Integer.parseInt(textfields.get(tfName).getText());
	}

	public String getString(String tfName)
	{
		return textfields.get(tfName).getText();
	}

	public MyTestUI addTextField(String tfName, int length)
	{
		JTextField tf=new JTextField(length);
		
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(tfName));
		pNorth.add(p);
		pNorth.add(tf);
		textfields.put(tfName,tf);
		return this;
	}

	public void repack(JFrame frame)
	{
		// Calcula el ancho necesario para los componentes
		int sumW=0;
		for(Component component:frame.getContentPane().getComponents())
		{
			sumW+=component.getPreferredSize().width;
		}

		int sumH=0;
		for(Component component:frame.getContentPane().getComponents())
		{
			sumH+=component.getPreferredSize().height;
		}

		// Establece el nuevo tamaño del JFrame
		Dimension curr = frame.getSize();
		Dimension newSize=new Dimension(Math.max(curr.width,sumW),Math.max(curr.height,sumH));
		frame.setSize(newSize);
	}

	@Override
	public void show(String mssg)
	{
		JOptionPane.showMessageDialog(frame,mssg);
	}

	public MyTestUI maximize()
	{
		MyAwt.maximize(frame);
		return this;
	}
}
