package thejavalistener.fwkutils.awt.image;

import java.awt.Color;

import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.image.MyQrGenerator;

public class MyImageCanvasTest
{
	public static void main(String[] args)
	{
		// url 
		String url ="https://i.scdn.co/image/ab67616d0000b273dbeec63ad914c973e75c24df";
		String target = "C:\\Java64\\Workspace\\MyJavaMusicLibrary2\\src\\main\\java\\a\\image\\qr.jpg";
		
		int px = 280;
		int pxFromBorder = 8;
		int radio = 10;
		MyQrGenerator.generateQR(url,target,px,pxFromBorder,radio,Color.BLACK,Color.WHITE,true);
		
		
		MyImageCanvas canvas = new MyImageCanvas();
		MyTestUI t = MyTestUI.test(canvas.c());
		
		t.addButton("Load",a->canvas.setImageFromFile(target))
		.size(450,450)
		.setVisible(true);
	}
}

