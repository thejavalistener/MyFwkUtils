package thejavalistener.fwkutils.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyConsoles
{
	private static MyConsole singleton = null;
	private static JFrame frame = null;
	private static Runnable shutdownHook = null;
	
	public static MyConsole get()
	{
		if( singleton==null )
		{
			singleton = new MyConsole();
		}
		
		return singleton;
	}
	
	public static MyConsole getOnWindow(String title)
	{
		return getOnWindow(title,()->{});
	}
	
	public static MyConsole getOnWindow(String title,Runnable stdhook)
	{
	    // Obtener dimensiones de pantalla
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

	    // Definir proporciones (ejemplo: 30% ancho, 30% alto)
	    
	    // por defecto tomo la proporción de la consola de windows
	    int width = (int) (screen.width * .78);    
	    int height = (int) (screen.height * .715);

	    return getOnWindow(title, width, height,stdhook);
	}
	
	public static MyConsole getOnWindow(String title, int width, int height)
	{
		return getOnWindow(title,width,height,()->{});
	}

	
	public static MyConsole getOnWindow(String title, int width, int height,Runnable stdhook)
	{
		MyConsole c = get();
		
	    frame = new JFrame(title != null ? title : "Console");
	    frame.setLayout(new BorderLayout());
	    frame.addWindowListener(new EscuchaWindow());
	    frame.getContentPane().add(c.c(),BorderLayout.CENTER); // usa el contentPane de la instancia singleton
	    frame.setSize(width > 0 ? width : 600, height > 0 ? height : 400);
	    shutdownHook = stdhook;
	    
	    // listeners para cerrar
	    c.textPane.addKeyListener(new EscuchaCTRLCyESC());
	    
	    MyAwt.center(frame,null);
	    frame.setVisible(true);
	    return c;
	}
	
	public static void closeAndExit(int secs)
	{
		if( frame!=null )
		{
			// notifico al lisener
			shutdownHook.run();
			
			// cuenta regresiva...
			singleton.countdown(secs);
			
			// chau
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}
	}
	
	static class EscuchaWindow extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			if( _confirmaSalir() )
			{
				closeAndExit(0);
			}
		}
	}    

	
	static class EscuchaCTRLCyESC extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_C||e.getKeyCode()==KeyEvent.VK_ESCAPE)
			{
				e.consume();
				if( frame!=null && _confirmaSalir() )
				{
					closeAndExit(0);
				}
			}
		}
	}	

	private static boolean _confirmaSalir()
	{
		int r=JOptionPane.showConfirmDialog(frame,"¿Esta acción finalizará el programa?","Confirmación",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		return r==JOptionPane.YES_OPTION;
	}


}
