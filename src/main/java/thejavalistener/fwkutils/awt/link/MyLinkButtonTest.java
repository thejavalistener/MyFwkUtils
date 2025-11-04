package thejavalistener.fwkutils.awt.link;

import thejavalistener.fwkutils.awt.panel.MyCenterLayout;
import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyLinkButtonTest 
{
	public static void main(String[] args)
	{
		MyAwt.setWindowsLookAndFeel();
		
		MyCenterLayout c = new MyCenterLayout();
		
		MyLink lnk = new MyLink("Selectable",MyLink.TOGGLE_ONOF);
		lnk.setActionListener(l->System.out.println("click"));
		
		
		c.add(lnk.c());		
		MyTestUI.test(c)
			.addButton("Select/Unselect",l->lnk.setSelected(!lnk.isSelected()))
			.run();
	}	
}
