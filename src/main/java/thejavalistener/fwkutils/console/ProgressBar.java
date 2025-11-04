package thejavalistener.fwkutils.console;

public class ProgressBar extends Progress
{
	private int size;
	private long top;
	
	private int lastCorch;

	public ProgressBar(MyConsoleBase c,int size,long top)
	{
		super(c);
		this.size = size;
		this.top = top;
	}
	
	protected Progress begin()
	{
		curr=0;
		console.print("[");
		for(int i=0; i<size; i++)
		{
			console.print(" ");
		}
		
		console.print("]");
		lastCorch = console.getCaretPosition();
		
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

		if(ant!=(int)porc)
		{				
			console.cs(console.getStyle().progressStyle);
			console.print(console.getStyle().progressFill);
			console.X();
			int x = console.getCaretPosition();
			console.setCaretPosition(lastCorch);
			console.print(" "+mssg);
			console.setCaretPosition(x);
			
			ant=(int)porc;
		}

		if(ant==size)
		{
			ant=0;
			console.skipFwd();
			console.X();
		}
	}
	
	public void increase()
	{
		increase("");
	}
}