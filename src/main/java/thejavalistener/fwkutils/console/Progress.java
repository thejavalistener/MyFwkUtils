package thejavalistener.fwkutils.console;

import java.awt.EventQueue;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;

import thejavalistener.fwkutils.string.MyString;
import thejavalistener.fwkutils.various.MyCollection;
import thejavalistener.fwkutils.various.MyThread;

public abstract class Progress
{
	protected MyConsoleBase console;
	private EventQueue eventQueue;
	private SecondaryLoop secondaryLoop;

	protected int ant;
	protected int curr;
	protected long top;

	protected long initProgressTime;
	protected Long finishProgressTime;
	
	protected boolean inOwnThread = false;
	private boolean usingThread = true;
	
	public abstract Progress begin();

	public void increase()
	{
		increase("");
	}
	
	public abstract void increase(String mssg);
	
	protected void _verifyThread()
	{
		if( usingThread && !inOwnThread )
		{
			throw new IllegalStateException("Debe incrementar el progreso dentro del método execute()");
		}
	}
	
	public void  setUsingThread(boolean b)
	{
		usingThread = b;
	}
	
	
	protected Progress(MyConsoleBase c)
	{
		this.console=c;
		eventQueue=Toolkit.getDefaultToolkit().getSystemEventQueue();
		secondaryLoop=eventQueue.createSecondaryLoop();
	}
	
	public MyConsoleBase execute(Runnable r) {
	    EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
	    SecondaryLoop loop = eventQueue.createSecondaryLoop();

	    // Aviso: estamos "ocupados"
	    MyCollection.invoke(console.getListeners(), t -> t.waitingForUserInput(true));

	    MyThread.start(() -> {
	        inOwnThread = true;
	        try {
	            // begin() en EDT (bloquea hasta que termina, asegura que la barra se dibuje)
	            SwingUtilities.invokeAndWait(() -> begin());

	            // Trabajo pesado en worker
	            r.run();

	            // finish() + loop.exit() en EDT y en orden
	            SwingUtilities.invokeLater(() -> {
	                try {
	                    finish();
	                } finally {
	                    loop.exit(); // salimos del loop DESPUÉS de terminar de pintar
	                    MyCollection.invoke(console.getListeners(), t -> t.waitingForUserInput(false));
	                }
	            });
	        } catch (Throwable ex) {
	            // En caso de error, aseguramos salida del loop y fin en EDT
	            SwingUtilities.invokeLater(() -> {
	                try {
	                    try { finish(); } catch (Throwable ignore) {}
	                } finally {
	                    loop.exit();
	                    MyCollection.invoke(console.getListeners(), t -> t.waitingForUserInput(false));
	                }
	            });
	            throw new RuntimeException(ex);
	        } finally {
	            inOwnThread = false;
	        }
	    });

	    // Este enter() se hace en el EDT; quedamos "bloqueados" hasta loop.exit()
	    loop.enter();
	    return console;
	}
	
	
	public void finish()
	{
//		while(curr<top)
//		{
//			increase();
//		}

		setPercent(100,"");
		
		console.X();

		console.skipFwd();

		finishProgressTime=System.currentTimeMillis()-initProgressTime;

		secondaryLoop.exit();
	}

	public long elapsedTime()
	{
		finishProgressTime=System.currentTimeMillis()-initProgressTime;		
		return finishProgressTime;
	}
	
	private int currPercent=0;
	
	public void setPercent(int pct)
	{
		setPercent(pct,"");
	}
	
	public abstract void setPercent(int pct,String mssg);
}
