package thejavalistener.fwkutils.awt.link;

import java.awt.Color;

import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.various.MyColor;

public class MyLinkGridTest
{
	public static void main(String[] args)
	{
		MyLinkGrid mlg = new MyLinkGrid();
		
		Color c = MyColor.random();
		mlg.setBackground(c);
		mlg.setDefaultTreatment(lnk->{	
			lnk.setClickeable(true);
			lnk.getStyle().linkBackgroundRolloverUnselected = c;
			lnk.getStyle().linkForegroundRolloverUnselected = Color.BLUE;
		});
		mlg.addNewRow().add("Abbey Road");
		mlg.addNewRow().add("The Beatles");
		mlg.addNewRow().add("1970").add("1969");

		MyTestUI.test(mlg.c()).run();
	}
}
