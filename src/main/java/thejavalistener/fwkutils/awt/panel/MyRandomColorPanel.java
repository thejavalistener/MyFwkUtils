package thejavalistener.fwkutils.awt.panel;

import javax.swing.JPanel;

import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyRandomColorPanel extends MyPanel
{
	public MyRandomColorPanel()
	{
		super(0,0,0,0);
		setBackground(MyAwt.randomColor());
		setBorder(null);
	}
}
