package thejavalistener.fwkutils.awt.tabbedpane;

import thejavalistener.fwkutils.awt.panel.MyRandomColorPanel;
import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyTabbedPaneTest
{
	public static void main(String[] args)
	{
		MyAwt.setWindowsLookAndFeel();

		MyTabbedPane mtp = new MyTabbedPane();
		mtp.addTab(new MyRandomColorPanel(),true);
		mtp.addTab(new MyRandomColorPanel(),true);
		mtp.addTab(new MyRandomColorPanel(),true);
		mtp.addTab(new MyRandomColorPanel(),true);
		
		MyTestUI.test(mtp.c()).addButton("Add",l->mtp.addTab(new MyRandomColorPanel(),true)).run();
	}
}
