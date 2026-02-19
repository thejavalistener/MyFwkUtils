//package thejavalistener.fwkutils.console;
//
//import javax.swing.SwingUtilities;
//
////import javax.swing.SwingUtilities;
////
////import thejavalistener.fwkutils.string.MyString;
////
////public class ProgressBar extends Progress
////{
////	private int size;
////	private long top;
////
////	private int lastCorch;
////
////	
////	private int currPercent=0;
////	
////	public void setPercent(int pct, String mssg)
////	{
////		
//////	    _verifyThread();
////
////		final int fpct = pct;
////		if (!SwingUtilities.isEventDispatchThread())
////	    {
////	        SwingUtilities.invokeLater(() -> setPercent(fpct, mssg));
////	        return;
////	    }
////		
////	    if (pct < 0) pct = 0;
////	    if (pct > 100) pct = 100;
////	    if( pct <=currPercent) return;
////
////	    int prevBlocks = (currPercent * size) / 100;
////	    int newBlocks  = (pct * size) / 100;
////
////	    int toPrint = newBlocks - prevBlocks;
////
////	    if (toPrint > 0)
////	    {
////	        console.cs(console.getStyle().progressStyle);
////	        String popo = MyString.replicate(console.getStyle().progressFill, toPrint);
////	        System.out.println(popo.length());
////	        console.print(popo);
////	        console.X();
////	    }
////
////	    currPercent = pct;
////
////	    if (mssg != null)
////	    {
////	        int x = console.getCaretPosition();
////	        console.setCaretPosition(lastCorch);
////	        console.print(" " + mssg);
////	        console.setCaretPosition(x);
////	    }
////	    
////	    if( currPercent==100 )
////	    {
////	    	console.skipFwd(2);
////	    }
////	}
////
////	public ProgressBar(MyConsoleBase c,int size,long top)
////	{
////		super(c);
////		this.size = size;
////		this.top = top;
////	}
////	
////	public Progress begin()
////	{
////		curr=0;
////		console.print("[");
////		for(int i=0; i<size; i++)
////		{
////			console.print(" ");
////		}
////		
////		console.print("]");
////		lastCorch = console.getCaretPosition();
////		
////		console.skipBkp(size+1);
////		console.cs(console.getStyle().progressStyle);
////
////		initProgressTime=System.currentTimeMillis();
////		
////		return this;
////	}
////
//////	public void increase(String mssg)
//////	{
//////		_verifyThread();
//////		
//////		curr++;
//////		double porc=((double)curr/top)*size;
//////
//////		if(ant!=(int)porc)
//////		{				
//////			console.cs(console.getStyle().progressStyle);
//////			console.print(console.getStyle().progressFill);
//////			console.X();
//////			int x = console.getCaretPosition();
//////			console.setCaretPosition(lastCorch);
//////			console.print(" "+mssg);
//////			console.setCaretPosition(x);
//////			
//////			ant=(int)porc;
//////		}
//////
//////		if(ant==size)
//////		{
//////			ant=0;
//////			console.skipFwd();
//////			console.X();
//////		}
//////	}
////
////	public void increase(String mssg)
////	{
////	    _verifyThread();
////
////	    curr++;
////
////	    if (top <= 0) return;
////
////	    int pct = (int)((curr * 100L) / top);
////
////	    setPercent(pct, mssg);
////	}
////
////	public void increase()
////	{
////		increase("");
////	}
////	
////	
////}
//
//import thejavalistener.fwkutils.string.MyString;
//
//public class ProgressBar extends Progress
//{
//	private int size;
//	private long top;
//
//	private int lastCorch;
//
//	
//	private int currPercent=0;
//	
//	public void setPercent(int pct, String mssg)
//	{
////	    _verifyThread();
//		
//		final int fpct = pct;
//		if (!SwingUtilities.isEventDispatchThread())
//	    {
//	        SwingUtilities.invokeLater(() -> setPercent(fpct, mssg));
//	        return;
//	    }
//
//	    if (pct < 0) pct = 0;
//	    if (pct > 100) pct = 100;
//	    if( pct <=currPercent) return;
//
//	    int prevBlocks = (currPercent * size) / 100;
//	    int newBlocks  = (pct * size) / 100;
//
//	    int toPrint = newBlocks - prevBlocks;
//
//	    if (toPrint > 0)
//	    {
//	        console.cs(console.getStyle().progressStyle);
//	        String popo = MyString.replicate(console.getStyle().progressFill, toPrint);
//	        System.out.println(popo.length());
//	        console.print(popo);
//	        console.X();
//	    }
//
//	    currPercent = pct;
//
//	    if (mssg != null)
//	    {
//	        int x = console.getCaretPosition();
//	        console.setCaretPosition(lastCorch);
//	        console.print(" " + mssg);
//	        console.setCaretPosition(x);
//	    }
//	    
//	    if( currPercent==100 )
//	    {
//	    	console.skipFwd(2);
//	    }
//	}
//
//	public ProgressBar(MyConsoleBase c,int size,long top)
//	{
//		super(c);
//		this.size = size;
//		this.top = top;
//	}
//	
//	public Progress begin()
//	{
//		curr=0;
//		console.print("[");
//		for(int i=0; i<size; i++)
//		{
//			console.print(" ");
//		}
//		
//		console.print("]");
//		lastCorch = console.getCaretPosition();
//		
//		console.skipBkp(size+1);
//		console.cs(console.getStyle().progressStyle);
//
//		initProgressTime=System.currentTimeMillis();
//		
//		return this;
//	}
//
////	public void increase(String mssg)
////	{
////		_verifyThread();
////		
////		curr++;
////		double porc=((double)curr/top)*size;
////
////		if(ant!=(int)porc)
////		{				
////			console.cs(console.getStyle().progressStyle);
////			console.print(console.getStyle().progressFill);
////			console.X();
////			int x = console.getCaretPosition();
////			console.setCaretPosition(lastCorch);
////			console.print(" "+mssg);
////			console.setCaretPosition(x);
////			
////			ant=(int)porc;
////		}
////
////		if(ant==size)
////		{
////			ant=0;
////			console.skipFwd();
////			console.X();
////		}
////	}
//
//	public void increase(String mssg)
//	{
//	    _verifyThread();
//
//	    curr++;
//
//	    if (top <= 0) return;
//
//	    int pct = (int)((curr * 100L) / top);
//
//	    setPercent(pct, mssg);
//	}
//
//	public void increase()
//	{
//		increase("");
//	}
//	
//	
//}

package thejavalistener.fwkutils.console;

import thejavalistener.fwkutils.string.MyString;

public class ProgressBar extends Progress
{
	private int size;
	private long top;

	private int lastCorch;

	public ProgressBar(MyConsoleBase c,int size,long top)
	{
		super(c);
		this.size=size;
		this.top=top;
	}

	public Progress begin()
	{
		curr=0;
		console.print("[");
		for(int i=0; i<size; i++)
		{
			console.print(" ");
		}

		console.print("]");
		lastCorch=console.getCaretPosition();

		console.skipBkp(size+1);
		console.cs(console.getStyle().progressStyle);

		initProgressTime=System.currentTimeMillis();

		return this;
	}

	public void increase(String mssg)
	{
		_verifyThread();

		curr++;
		double porc=((double)curr/top)*size;
		setPercent((int)porc,"");
	}

	public void increase()
	{
		increase("");
	}

	private int currPercent=-1;
	public void setPercent(int pct, String mssg)
	{

		// _verifyThread();

		if(pct<0) pct=0;
		if(pct>100) pct=100;
		if(pct<=currPercent) return;

		int prevBlocks=(currPercent*size)/100;
		int newBlocks=(pct*size)/100;

		int toPrint=newBlocks-prevBlocks;

		if(toPrint>0)
		{
			console.cs(console.getStyle().progressStyle);
			String popo=MyString.replicate(console.getStyle().progressFill,toPrint);
			System.out.println(popo.length());
			console.print(popo);
			console.X();
		}

		currPercent=pct;

		if(mssg!=null)
		{
			int x=console.getCaretPosition();
			console.setCaretPosition(lastCorch);
			console.print(" "+mssg);
			console.setCaretPosition(x);
		}

		if(currPercent==100)
		{
			console.skipFwd(2);
		}
	}

}