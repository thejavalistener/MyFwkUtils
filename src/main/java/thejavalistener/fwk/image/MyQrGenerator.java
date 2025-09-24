package thejavalistener.fwk.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MyQrGenerator
{
	public static void generateQR(String url, String targetFilename, int px, int pxFromBorder, int radio, Color fg, Color bg, boolean rewrite) {
	    File file = new File(targetFilename);
	    if (file.exists() && !rewrite) return;

	    try {
	        Map<EncodeHintType, Object> hints = new HashMap<>();
	        hints.put(EncodeHintType.MARGIN, 0);
	        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

	        BitMatrix rawMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 0, 0, hints);
	        int qrWidth = rawMatrix.getWidth();
	        int qrHeight = rawMatrix.getHeight();
	        float scaleX = (float)(px - 2 * pxFromBorder) / qrWidth;
	        float scaleY = (float)(px - 2 * pxFromBorder) / qrHeight;

	        BufferedImage qrImage = new BufferedImage(px, px, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = qrImage.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

	        g.setColor(bg);
	        if (radio > 0) {
	            g.fillRoundRect(0, 0, px, px, radio * 2, radio * 2);
	        } else {
	            g.fillRect(0, 0, px, px);
	        }

	        g.setColor(fg);
	        for (int y = 0; y < qrHeight; y++) {
	            for (int x = 0; x < qrWidth; x++) {
	                if (rawMatrix.get(x, y)) {
	                    int scaledX = pxFromBorder + Math.round(x * scaleX);
	                    int scaledY = pxFromBorder + Math.round(y * scaleY);
	                    int scaledW = Math.max(1, Math.round(scaleX));
	                    int scaledH = Math.max(1, Math.round(scaleY));
	                    g.fillRect(scaledX, scaledY, scaledW, scaledH);
	                }
	            }
	        }

	        g.dispose();
	        ImageIO.write(qrImage, "png", file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}	
////	public static void generateQRDecorado(String url, String targetFilename, int px, int n, int r, Color fg, Color bg)
////	{
////		try
////		{
////			// Tamaño del QR interno
////			int qrSize=px-n;
////
////			// Generar QR sin márgenes
////			Map<EncodeHintType,Object> hints=new HashMap<>();
////			hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
////			hints.put(EncodeHintType.MARGIN,0);
////
////			BitMatrix matrix=new QRCodeWriter().encode(url,BarcodeFormat.QR_CODE,qrSize,qrSize,hints);
////			MatrixToImageConfig config=new MatrixToImageConfig(fg.getRGB(),bg.getRGB());
////			BufferedImage qrImage=MatrixToImageWriter.toBufferedImage(matrix,config);
////
////			// Crear imagen final
////			BufferedImage finalImage=new BufferedImage(px,px,BufferedImage.TYPE_INT_ARGB);
////			Graphics2D g=finalImage.createGraphics();
////
////			// Fondo transparente o rojo (si querés cambiarlo)
////			g.setColor(bg);
////			g.fillRect(0,0,px,px);
////
////			// Dibujar marco blanco con esquinas redondeadas
////			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
////			g.setColor(Color.WHITE);
////			Shape marco=new RoundRectangle2D.Float(0,0,px,px,r*2,r*2);
////			g.fill(marco);
////
////			// Insertar QR centrado
////			int offset=n/2;
////			g.drawImage(qrImage,offset,offset,null);
////
////			g.dispose();
////			ImageIO.write(finalImage,"png",new File(targetFilename));
////		}
////		catch(Exception e)
////		{
////			System.err.println("Error al generar QR decorado: "+e.getMessage());
////		}
////	}
//	
//	public static void generateQRDecorado(String url, String targetFilename, int px, Color fg, Color bg) {
//	    int n = Math.round(px * 0.1f);
//	    int r = Math.round(px * 0.1f);
//	    generateQRDecorado(url, targetFilename, px, n, r, fg, bg);
//	}
//
//	public static void generateQRDecorado(String url, String targetFilename, int px, int n, int r, Color fg, Color bg) {
//	    try {
//	        int qrSize = px - n;
//
//	        Map<EncodeHintType, Object> hints = new HashMap<>();
//	        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//	        hints.put(EncodeHintType.MARGIN, 0);
//
//	        BitMatrix matrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, qrSize, qrSize, hints);
//	        MatrixToImageConfig config = new MatrixToImageConfig(fg.getRGB(), bg.getRGB());
//	        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix, config);
//
//	        // Escalar QR al tamaño exacto
//	        Image scaledQR = qrImage.getScaledInstance(qrSize, qrSize, Image.SCALE_SMOOTH);
//	        BufferedImage qrFinal = new BufferedImage(qrSize, qrSize, BufferedImage.TYPE_INT_ARGB);
//	        Graphics2D gQR = qrFinal.createGraphics();
//	        gQR.drawImage(scaledQR, 0, 0, null);
//	        gQR.dispose();
//
//	        BufferedImage finalImage = new BufferedImage(px, px, BufferedImage.TYPE_INT_ARGB);
//	        Graphics2D g = finalImage.createGraphics();
//
//	        // Fondo transparente
//	        g.setComposite(AlphaComposite.Clear);
//	        g.fillRect(0, 0, px, px);
//
//	        // Activar antialiasing
//	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//	        // Dibujar marco blanco con esquinas redondeadas
//	        g.setComposite(AlphaComposite.Src);
//	        g.setColor(Color.WHITE);
//	        Shape marco = new RoundRectangle2D.Float(0, 0, px, px, r * 2, r * 2);
//	        g.fill(marco);
//
//	        // Insertar QR centrado
//	        int offset = (px - qrSize) / 2;
//	        g.drawImage(qrFinal, offset, offset, null);
//
//	        g.dispose();	        
//	        
////	        BufferedImage finalImage = new BufferedImage(px, px, BufferedImage.TYPE_INT_ARGB);
////	        Graphics2D g = finalImage.createGraphics();
////
////	        // Activar antialiasing
////	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////
////	        // Crear área redondeada como clip
////	        Shape marco = new RoundRectangle2D.Float(0, 0, px, px, r * 2, r * 2);
////	        g.setClip(marco);
////
////	        // Fondo blanco dentro del marco
////	        g.setColor(Color.WHITE);
////	        g.fillRect(0, 0, px, px);
////
////	        // Insertar QR centrado
////	        int offset = (px - qrSize) / 2;
////	        g.drawImage(qrFinal, offset, offset, null);
////
////	        g.dispose();
//
////	        BufferedImage finalImage = new BufferedImage(px, px, BufferedImage.TYPE_INT_ARGB);
////	        Graphics2D g = finalImage.createGraphics();
////
////	        // Fondo base
////	        g.setColor(bg);
////	        g.fillRect(0, 0, px, px);
////
////	        // Marco blanco con esquinas redondeadas
////	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////	        g.setColor(Color.WHITE);
////	        Shape marco = new RoundRectangle2D.Float(0, 0, px, px, r * 2, r * 2);
////	        g.fill(marco);
////
////	        // Insertar QR centrado
////	        int offset = (px - qrSize) / 2;
////	        g.drawImage(qrFinal, offset, offset, null);
////
////	        g.dispose();
//	        ImageIO.write(finalImage, "png", new File(targetFilename));
//	    } catch (Exception e) {
//	        System.err.println("Error al generar QR decorado: " + e.getMessage());
//	    }
//	}
//	
//	// public static void generateQR(String url, String targetFilename, int px,
//	// boolean rewriteIfExists, Color fg, Color bg)
//	// {
//	// File f=new File(targetFilename);
//	// if(f.exists()&&!rewriteIfExists)
//	// {
//	// return;
//	// }
//	//
//	// try
//	// {
//	// Map<EncodeHintType,Object> hints=new HashMap<>();
//	// hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
//	// hints.put(EncodeHintType.MARGIN,0); // eliminar margen
//	//
//	// BitMatrix fullMatrix=new
//	// QRCodeWriter().encode(url,BarcodeFormat.QR_CODE,px,px,hints);
//	//
//	// // Detectar los bordes útiles del QR
//	// int[] rect=fullMatrix.getEnclosingRectangle(); // [x, y, width,
//	// // height]
//	// int x=rect[0];
//	// int y=rect[1];
//	// int width=rect[2];
//	// int height=rect[3];
//	//
//	// // Recortar el BitMatrix
//	// BitMatrix croppedMatrix=new BitMatrix(width,height);
//	// for(int i=0; i<width; i++)
//	// {
//	// for(int j=0; j<height; j++)
//	// {
//	// if(fullMatrix.get(i+x,j+y))
//	// {
//	// croppedMatrix.set(i,j);
//	// }
//	// }
//	// }
//	//
//	// // Configurar colores personalizados
//	// MatrixToImageConfig config=new MatrixToImageConfig(fg.getRGB(), // color
//	// // del
//	// // código
//	// bg.getRGB() // color de fondo
//	// );
//	//
//	// BufferedImage
//	// qrImage=MatrixToImageWriter.toBufferedImage(croppedMatrix,config);
//	//
//	// _ensureParent(new File(targetFilename));
//	// ImageIO.write(qrImage,"png",new File(targetFilename));
//	// }
//	// catch(WriterException|IOException e)
//	// {
//	// System.err.println("Error al generar QR: "+e.getMessage());
//	// }
//	// }
//
//	private static void _ensureParent(File f)
//	{
//		File p=f.getParentFile();
//		if(p!=null&&!p.exists()) p.mkdirs();
//	}
//
}
