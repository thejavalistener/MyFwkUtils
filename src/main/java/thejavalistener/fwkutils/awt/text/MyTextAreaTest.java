package thejavalistener.fwkutils.awt.text;

import thejavalistener.fwkutils.awt.testui.MyTestUI;

public class MyTextAreaTest
{
	public static void main(String[] args)
	{
		MyTextArea mta = new MyTextArea(true);
		mta.setText("Soy ${nombre} y mi color favorito es ${color}");
		MyTestUI.test(mta.c()).run();
	}
}
