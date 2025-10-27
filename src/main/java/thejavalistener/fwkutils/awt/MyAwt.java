package thejavalistener.fwkutils.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MyAwt
{
	public static void beep()
	{
		Toolkit.getDefaultToolkit().beep();
	}

	public static Window getMainWindow(Container c)
	{
		if(c==null)
		{
			return null; // Evita NullPointerException si el contenedor es nulo
		}
		if(c instanceof Window)
		{
			return (Window)c; // Si ya es un Window, lo retornamos
		}
		return getMainWindow(c.getParent()); // Llamada recursiva
	}

	public static Map<Component,Boolean> disableTemporally(Container c)
	{
		return disableTemporally(c,new Component[] {});
	}

	public static Map<Component,Boolean> disableTemporally(Container c, Component... excepted)
	{
		Map<Component,Boolean> stateMap=new HashMap<>();
		Set<Component> excluded=new HashSet<>(Arrays.asList(excepted));
		_disableRecursively(c,stateMap,excluded);
		return stateMap;
	}

	private static void _disableRecursively(Container c, Map<Component,Boolean> stateMap, Set<Component> excluded)
	{
		if(!excluded.contains(c)&&!stateMap.containsKey(c))
		{
			stateMap.put(c,c.isEnabled());
			c.setEnabled(false);
		}

		for(Component comp:c.getComponents())
		{
			if(!excluded.contains(comp))
			{
				if(!stateMap.containsKey(comp))
				{
					stateMap.put(comp,comp.isEnabled());
				}
				comp.setEnabled(false);

				if(comp instanceof Container)
				{
					_disableRecursively((Container)comp,stateMap,excluded);
				}
			}
		}
	}

	// Método para restaurar los estados originales de los componentes
	public static void restoreDisabled(Map<?,?> stateMap)
	{
		if(stateMap==null) return; // Evitar NullPointerException

		for(Map.Entry<?,?> entry:stateMap.entrySet())
		{
			Component comp=(Component)entry.getKey();
			if(comp!=null)
			{
				Boolean enabled=(Boolean)entry.getValue();
				comp.setEnabled(enabled);
			}
		}
	}

	public static void setEnabled(Container c, boolean b)
	{
		setEnabled(c,b,new Component[] {});
	}

	public static void setEnabled(Container c, boolean b, Component... aExcluded)
	{
		// Convertir el array a un Set para búsqueda eficiente
		Set<Component> excluded=new HashSet<>(Arrays.asList(aExcluded));

		// Si el contenedor no está excluido, aplicar setEnabled
		if(!excluded.contains(c))
		{
			c.setEnabled(b);
		}

		// Iterar sobre los componentes internos
		for(Component comp:c.getComponents())
		{
			if(!excluded.contains(comp))
			{
				comp.setEnabled(b);
			}

			// Si es un contenedor, aplicar recursivamente
			if(comp instanceof Container)
			{
				setEnabled((Container)comp,b,aExcluded);
			}
		}
	}

	public static <T> T getParent(Component component, Class<T> parentType)
	{
		Container parent=component.getParent();
		while(parent!=null)
		{
			if(parent.getClass().equals(parentType))
			{
				return (T)parent;
			}

			parent=parent.getParent();
		}
		return null;
	}

	public static void triggerItemEvent(Component cmp)
	{
		if(cmp instanceof JComboBox)
		{
			JComboBox<?> jcmp=(JComboBox<?>)cmp;

			ItemEvent event=new ItemEvent(jcmp,ItemEvent.ITEM_STATE_CHANGED,jcmp.getSelectedItem(),ItemEvent.SELECTED);

			ItemListener[] listeners=jcmp.getItemListeners();
			for(ItemListener listener:listeners)
			{
				listener.itemStateChanged(event);
			}
		}
	}

	public static void triggerActionEvent(Component cmp)
	{
		String actionCommand="";
		if(cmp instanceof JButton)
		{
			actionCommand=((JButton)cmp).getActionCommand();
		}

		ActionEvent e=new ActionEvent(cmp,ActionEvent.ACTION_PERFORMED,actionCommand);
		ActionListener[] listeners=cmp.getListeners(ActionListener.class);
		for(ActionListener listener:listeners)
		{
			listener.actionPerformed(e);
		}
	}

	public static void refreshPanel(Container p)
	{
		p.revalidate();
		p.repaint();
	}

	public static void copyToClipboard(String txt)
	{
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		Clipboard clipboard=toolkit.getSystemClipboard();
		clipboard.setContents(new StringSelection(txt),null);
	}

	public static void setWindowsLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void fontIncrease(Component c, int factor)
	{
		Font currentFont=c.getFont();
		int newSize=currentFont.getSize()+factor;
		Font newFont=currentFont.deriveFont((float)newSize);
		c.setFont(newFont);
	}

	// public static <T> T selectOption(String mssg, String title, List<T>
	// options, Function<T,String> tToString, Container owner)
	// {
	// MyComboBox<T> combo=new MyComboBox<>(tToString);
	// combo.setItems(options);
	// int
	// resultado=JOptionPane.showConfirmDialog(owner,combo.c(),mssg,JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
	//
	// return options.get(resultado);
	//
	// }

	/** Retorna: 0=>no, 1=> si */
	public static int showConfirmYES_NO(String mssg, String title, Container owner)
	{
		String options[]= {"Si", "No"};
		return showConfirmDialog(mssg,title,options,0,owner);
	}
	
	/** Retorna: 0=>no, 1=> si */
	public static int showConfirmNO_YES(String mssg, String title, Container owner)
	{
		String options[]= {"No", "Si"};
		return showConfirmDialog(mssg,title,options,0,owner);
	}

	public static void showErrorMessage(String mssg, String title, Container owner)
	{
		JOptionPane.showMessageDialog(owner,mssg,title,JOptionPane.ERROR_MESSAGE);
	}

	public static void showWarningMessage(String mssg, String title, Container owner)
	{
		JOptionPane.showMessageDialog(owner,mssg,title,JOptionPane.WARNING_MESSAGE);
	}

	public static void showInformationMessage(String mssg, String title, Container owner)
	{
		JOptionPane.showMessageDialog(owner,mssg,title,JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showMessage(String mssg, String title, int mssgType, Container owner)
	{
		JOptionPane.showMessageDialog(owner,mssg,title,mssgType);
	}

	public static String inputText(String mssg, String title, Container parent)
	{
		// return JOptionPane.showInputDialog(parent,mssg,title);
		return (String)JOptionPane.showInputDialog(parent,mssg,title,JOptionPane.PLAIN_MESSAGE,null,null,null);
	}

	public static int showConfirmDialog(String mssg, String title, String[] options, int def, Container owner)
	{
		return JOptionPane.showOptionDialog(owner,mssg,title,JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[def]);
	}

	public static Font underline(Font originalFont, boolean underline)
	{
		Map<TextAttribute,Object> attributes=new HashMap<>(originalFont.getAttributes());

		if(underline)
		{
			attributes.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
		}
		else
		{
			if(attributes.containsKey(TextAttribute.UNDERLINE))
			{
				attributes.remove(TextAttribute.UNDERLINE);
			}
		}

		return originalFont.deriveFont(attributes);
	}

	public static Dimension getScreenSize(double porc)
	{
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension x=new Dimension((int)(d.width*porc),(int)(d.height*porc));
		return x;
	}

	public static void setWidth(Container c, int w)
	{
		c.setPreferredSize(new Dimension(w,c.getPreferredSize().height));
	}

	public static Color randomColor()
	{
		int r=(int)(Math.random()*255);
		int g=(int)(Math.random()*255);
		int b=(int)(Math.random()*255);
		return new Color(r,g,b);
	}

	public static void center(Window child, Window parent)
	{
		if(parent==null)
		{
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			int x=(int)(screenSize.getWidth()-child.getWidth())/2;
			int y=(int)(screenSize.getHeight()-child.getHeight())/2;
			child.setLocation(x,y);
		}
		else
		{
			int x=parent.getX()+(parent.getWidth()-child.getWidth())/2;
			int y=parent.getY()+(parent.getHeight()-child.getHeight())/2;
			child.setLocation(x,y);
		}
	}

	public static void centerV(int offSetFromLeft, Window child, Window parent)
	{
		if(parent==null)
		{
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			int y=(int)(screenSize.getHeight()-child.getHeight())/2;
			child.setLocation(offSetFromLeft,y);
		}
		else
		{
			int y=parent.getY()+(parent.getHeight()-child.getHeight())/2;
			child.setLocation(offSetFromLeft,y);
		}
	}

	public static void centerH(int offsetFromTop, Window child, Window parent)
	{
		if(parent==null)
		{
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			int x=(int)(screenSize.getWidth()-child.getWidth())/2;
			child.setLocation(x,offsetFromTop);
		}
		else
		{
			int x=parent.getX()+(parent.getWidth()-child.getWidth())/2;
			child.setLocation(x,offsetFromTop);
		}
	}

	// public static void setBackground(Container panel, Color color)
	// {
	// // Cambiar el color de fondo del panel actual
	// panel.setBackground(color);
	//
	// // Recorrer todos los componentes del panel actual
	// for(Component component:panel.getComponents())
	// {
	// if(component.getClass().isAssignableFrom(MyPanel.class))
	// {
	// JPanel myPanel=(JPanel)component;
	// if(myPanel.allowChangeBackground())
	// {
	// setBackground((Container)component,color);
	// }
	// }
	// else
	// {
	// setBackground((Container)component,color);
	// }
	// }
	// }

	public static void setPreferredWidth(int pw, Component cmp)
	{
		cmp.setPreferredSize(new Dimension(pw,cmp.getPreferredSize().height));
	}

	public static void setPreferredHeight(int ph, Component cmp)
	{
		cmp.setPreferredSize(new Dimension(cmp.getPreferredSize().width,ph));
	}

	public static void setWidth(int pw, Component cmp)
	{
		cmp.setSize(new Dimension(pw,cmp.getSize().height));
	}

	public static void setHeiht(int ph, Component cmp)
	{
		cmp.setSize(new Dimension(cmp.getSize().width,ph));
	}

	public static Dimension getSize(Container c, double d)
	{
		Dimension dim=new Dimension();
		dim.width=(int)(c.getSize().width*d);
		dim.height=(int)(c.getSize().height*d);
		return dim;
	}

	public static void setProportionalSize(double porc, Window child, Window parent)
	{
		// Obtener el tamaño de b
		Dimension sizeB=parent!=null?parent.getSize():getScreenSize(1);

		// Calcular el nuevo tamaño de a
		int newWidth=(int)(sizeB.width*porc);
		int newHeight=(int)(sizeB.height*porc);

		// Ajustar el tamaño de a
		child.setSize(newWidth,newHeight);
	}

	public static void maximize(JFrame frame)
	{
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width,screenSize.height);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public static void openPath(String path)
	{
		try
		{
			File carpeta=new File(path);
			Desktop.getDesktop().open(carpeta);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void pack(Window f, int maxW, int maxH)
	{
		f.pack();
		Dimension size=f.getSize();

		int width=(int)f.getSize().getWidth();
		if(maxW>0) width=Math.min(size.width,maxW);

		int height=(int)f.getSize().getHeight();
		if(maxH>0) height=Math.min(size.height,maxH);

		f.setSize(width,height);
	}
}
