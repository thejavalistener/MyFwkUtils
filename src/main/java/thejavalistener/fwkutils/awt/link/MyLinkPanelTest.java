package thejavalistener.fwkutils.awt.link;

import thejavalistener.fwkutils.awt.testui.MyTestUI;

public class MyLinkPanelTest
{
	public static void main(String[] args)
	{
		MyLinkGroup lgrp = new MyLinkGroup();
		MyLink lnk;
		MyLinkPanel mlp = new MyLinkPanel(MyLinkPanel.HORIZONTAL);
		mlp.addLink(lnk=new MyLink("Boca",MyLink.LINK));
		mlp.addLink(lnk=new MyLink("River",MyLink.LINK));
		mlp.addLink(new MyLink("Velez",MyLink.LINK));
		mlp.addLink(new MyLink("Sacashispas",MyLink.LINK));
		
		MyTestUI.test(mlp.c())
	    .run();
		
	}
}
