package thejavalistener.fwkutils.awt.progres;

import thejavalistener.fwkutils.awt.panel.MyRandomColorPanel;
import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.awt.variuos.MyAwt;
import thejavalistener.fwkutils.string.MyString;
import thejavalistener.fwkutils.various.MyThread;

public class Test2
{
	public static void main(String[] args) throws Exception
	{
		MyAwt.setWindowsLookAndFeel();

		MyTestUI mt = MyTestUI.test(new MyRandomColorPanel());		
		MyProgressPane mp=new MyProgressPane("Progreso del proceso","Demo loca",null);
		
		mt.addButton("Start",l->new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int fin = 100;
				for(int i=1; i<=fin; i++)
				{
					int millis=(int)(Math.random()*1000);
					MyThread.sleep(millis);

					try
					{
						mp.increaseValue(MyString.generateRandom('A','Z',5,20)); 
						
						if( i%13==0 )
						{
							mp.increaseValueTo(mp.getProgressValue()+30,"Acelerando...");
						}						
					}
					catch(Exception e)
					{
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
		}).start());

		mt.run();
		
		System.out.println(mp.getProgressValue());
	}
}
