package thejavalistener.fwkutils.console;

public class ProgressMeter extends Progress
{
	public ProgressMeter(MyConsoleBase c,long top)
	{
		super(c);
		super.top = top;

	}
	
	@Override
	public Progress begin()
	{
		initProgressTime=System.currentTimeMillis();
		curr=0;
		console.print("00%");
		console.skipBkp(3);
		return this;
	}

	@Override
	public void increase(String mssg)
	{
		_verifyThread();

		curr++;
		int porc=(int)Math.floor(((double)curr/top)*100);

		if(porc<100)
		{
			console.print((porc<10?"0":"")+porc+"%");
			int pos = console.getCaretPosition();
			console.print(" "+mssg);
			console.setCaretPosition(pos);
			console.skipBkp(3);
		}
		else
		{
			console.print("100% ");//.print("%");
			console.skipFwd();
		}		
	}

	@Override
	public void increase()
	{
		_verifyThread();
		
		curr++;
		int porc=(int)Math.floor(((double)curr/top)*100);

		if(porc<100)
		{
			console.print((porc<10?"0":"")+porc+"%");			
			console.skipBkp(3);
		}
		else
		{
			console.print("100").print("%");
			console.skipFwd();
		}
	}
	
	
	@Override
	public void setPercent(int pct, String mssg)
	{
		throw new RuntimeException("No implementado");
	}
}


