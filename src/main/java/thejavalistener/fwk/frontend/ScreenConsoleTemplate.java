package thejavalistener.fwk.frontend;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import thejavalistener.fwk.awt.MyAwt;
import thejavalistener.fwk.console.MyConsole;
import thejavalistener.fwk.console.MyConsoleListener;
import thejavalistener.fwk.util.MyLog;
import thejavalistener.fwk.util.MyThread;

@Component
public abstract class ScreenConsoleTemplate extends MyAbstractScreen
{
	protected MyConsole console = null;
				
	public ScreenConsoleTemplate()
	{
		setLayout(new BorderLayout());

		// instancio la consola y la agrego al center
		console = new MyConsole();
		console.addListener(new EscuchaConsola());
		add(console.c(),BorderLayout.CENTER);
	}	
	
	protected MyConsole getConsole()
	{
		return console;
	}
			
	class EscuchaConsola implements MyConsoleListener
	{
		@Override
		public void waitingForUserInput(boolean waiting)
		{
			setDisabledTemporally(waiting,console.c());
			console.getTextPane().setEnabled(true);
		}
	}
}
