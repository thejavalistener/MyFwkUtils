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
	    curr = 0;
	    currPercent = 0;

	    console.print("[");
	    int startBarPos = console.getCaretPosition();

	    for (int i = 0; i < size; i++)
	    {
	        console.print(" ");
	    }

	    console.print("]");

	    lastCorch = console.getCaretPosition() - 1; // posición real del ]

	    console.setCaretPosition(startBarPos);

	    initProgressTime = System.currentTimeMillis();
	    return this;
	}

	public void increase(String mssg)
	{
	    curr++;
	    int pct = (int)((curr * 100.0) / top);
	    setPercent(pct, mssg);
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
		if(pct<=currPercent)
		{
			_printMssg(mssg);
			return;			
		}

		int prevBlocks=(currPercent*size)/100;
		int newBlocks=(pct*size)/100;

		int toPrint=newBlocks-prevBlocks;

		if(toPrint>0)
		{
			console.cs(console.getStyle().progressStyle);
			String fillChars=MyString.replicate(console.getStyle().progressFill,toPrint);
			console.print(fillChars);
			console.X();
		}

		currPercent=pct;

		_printMssg(mssg);

		if(currPercent==100)
		{
		    console.setCaretPosition(lastCorch + 1);
		}
	}
	
	private void _printMssg(String mssg)
	{
		if(mssg!=null && !mssg.isEmpty())
		{
			int x=console.getCaretPosition();
			console.setCaretPosition(lastCorch+1);
			console.print(mssg);
			console.setCaretPosition(x);
		}
	}
}