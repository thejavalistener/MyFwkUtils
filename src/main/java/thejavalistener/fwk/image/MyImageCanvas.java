package thejavalistener.fwk.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyImageCanvas
{
	private final JPanel contentPane=new JPanel()
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2=(Graphics2D)g.create();

			// Fondo blanco opaco
//			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,getWidth(),getHeight());

			if(currentImage!=null)
			{
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				int x=(getWidth()-currentImage.getWidth())/2;
				int y=(getHeight()-currentImage.getHeight())/2;
				g2.drawImage(currentImage,x,y,null);
			}

			g2.dispose();
		}
	};

	private BufferedImage currentImage;

	public JPanel c()
	{
		return contentPane;
	}
	
	public void setBackground(Color bg)
	{
		contentPane.setBackground(bg);
	}

	public void setBufferedImage(BufferedImage bi)
	{
		setCurrentImageAndRepaint(bi);
	}

	public BufferedImage getBufferedImage()
	{
		return currentImage;
	}


	/*
	 * ======================= API ORIGINAL (COMPORTAMIENTO CORRECTO)
	 * =======================
	 */

	/** 1) SOLO carga desde archivo local. No escala ni descarga. */
	public void setImageFromFile(String fullImageName)
	{
		File f=new File(fullImageName);
		if(!f.exists())
		{
			throw new RuntimeException("No existe el archivo: "+f.getPath());
		}
		try
		{
			BufferedImage bi=ImageIO.read(f);
			if(bi==null) throw new IOException("Formato no soportado/corrupto: "+f.getPath());
			setCurrentImageAndRepaint(bi);
			contentPane.repaint();
		}
		catch(IOException e)
		{
			throw new RuntimeException("Error leyendo imagen: "+f.getPath(),e);
		}
	}

	/** 2) Descarga desde URL y muestra en memoria. No guarda ni escala. */
//	public void setImageFromURL(String urlImage)
//	{
//		try
//		{
//			BufferedImage bi=ImageIO.read(new URL(urlImage));
//			if(bi==null) throw new IOException("Formato no soportado/corrupto desde URL.");
//			setCurrentImageAndRepaint(bi);
//		}
//		catch(IOException e)
//		{
//			throw new RuntimeException("Error cargando imagen desde URL: "+urlImage,e);
//		}
//	}

	/**
	 * 3) Si existe el archivo lo muestra; si no, lo descarga a ese path y lo
	 * muestra. No escala.
	 */
//	public void setImageFromFileOrURL(String urlImage, String fullImageName)
//	{
//		File original=new File(fullImageName);
//		if(original.exists())
//		{
//			setImageFromFile(fullImageName);
//			return;
//		}
//		// Descargar a disco y luego leer desde disco
//		// MyImage.downloadImage(urlImage,original,log);
//		MyImage.downloadToFile(urlImage,original.getPath(),true);
//		setImageFromFile(fullImageName);
//	}

	/**
	 * 4) Flujo con escalado (el que te funciona): descargar si falta → escalar
	 * → cargar escalado.
	 */
//	public void setScaledImageFromFileOrURL(String urlImage, String pathOriginal, String pathScaled, int w, int h)
//	{
//		File original=new File(pathOriginal);
//		File scaled=new File(pathScaled);
//
//		if(!original.exists())
//		{
//			// MyImage.downloadImage(urlImage,original,log);
//			MyImage.downloadToFile(urlImage,original.getPath(),true);
//		}
//
//		int[] newWH= {w, h};
//		if(!scaled.exists()||original.lastModified()>scaled.lastModified())
//		{
//			// newWH = MyImage.scaleImage(original,scaled,w,h,log);
//			MyImage.scaleToFile(original.getPath(),scaled.getPath(),w,h,true);
//			newWH=new int[] {w, h}; // si querés mantener la lógica de tamaño
//		}
//		loadScaledAndRepaint(scaled,newWH[0],newWH[1]);
//	}

//	public void setScaledImageFromFile(String pathOriginal, String pathScaled, int w, int h)
//	{
//		File original=new File(pathOriginal);
//		File scaled=new File(pathScaled);
//		Consumer<String> log=System.out::println;
//
//		if(!original.exists())
//		{
//			log.accept("Imagen original no encontrada: "+pathOriginal);
//			return;
//		}
//
//		int[] newWH= {w, h};
//		if(!scaled.exists()||original.lastModified()>scaled.lastModified())
//		{
//			// newWH = MyImage.scaleImage(original, scaled, w, h, log);
//			MyImage.scaleToFile(original.getPath(),scaled.getPath(),w,h,true);
//			newWH=new int[] {w, h};
//		}
//
//		loadScaledAndRepaint(scaled,newWH[0],newWH[1]);
//	}
	
	public void overlapImage(String pathImage, int pos, int px) {
		try {
			BufferedImage bi = ImageIO.read(new File(pathImage));
			if (bi == null) throw new IOException("Formato no soportado o imagen corrupta: " + pathImage);
			overlapImage(bi, pos, px);
		} catch (IOException e) {
			throw new RuntimeException("Error leyendo imagen desde disco: " + pathImage, e);
		}
	}

	public void overlapImage(BufferedImage bi, int pos, int px)
	{
		if(currentImage==null||bi==null) return;

		int baseW=currentImage.getWidth();
		int baseH=currentImage.getHeight();
		int overlayW=bi.getWidth();
		int overlayH=bi.getHeight();

		// Crear copia de la imagen base para no modificar la original
		BufferedImage result=new BufferedImage(baseW,baseH,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=result.createGraphics();

		// Dibujar imagen base
		g2.drawImage(currentImage,0,0,null);

		// Calcular posición de la imagen superpuesta
		int x=0,y=0;
		switch(pos)
		{
			case 1: // Superior izquierda
				x=px;
				y=px;
				break;
			case 2: // Superior derecha
				x=baseW-overlayW-px;
				y=px;
				break;
			case 3: // Inferior derecha
				x=baseW-overlayW-px;
				y=baseH-overlayH-px;
				break;
			case 4: // Inferior izquierda
				x=px;
				y=baseH-overlayH-px;
				break;
			case 5: // Centrado
				x=(baseW-overlayW)/2;
				y=(baseH-overlayH)/2;
				break;
			default:
				throw new IllegalArgumentException("Posición inválida: "+pos);
		}

		// Dibujar imagen superpuesta
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(bi,x,y,null);
		g2.dispose();

		// Actualizar imagen actual y repintar
		setCurrentImageAndRepaint(result);
	}
	/*
	 * ====================== AUXILIARES ======================
	 */

//	private void loadScaledAndRepaint(File scaled, int w, int h)
//	{
//		try
//		{
//			BufferedImage bi=ImageIO.read(scaled);
//			if(bi==null) throw new IOException("Formato no soportado/corrupto: "+scaled.getPath());
//			setCurrentImageAndRepaint(bi,Math.max(w,10),Math.max(h,10));
//		}
//		catch(IOException e)
//		{
//			throw new RuntimeException("Error cargando imagen escalada: "+scaled.getPath(),e);
//		}
//	}

	private void setCurrentImageAndRepaint(BufferedImage bi)
	{
		setCurrentImageAndRepaint(bi,bi.getWidth(),bi.getHeight());
	}

	private void setCurrentImageAndRepaint(BufferedImage bi, int prefW, int prefH)
	{
		currentImage=bi;

		Dimension d=new Dimension(prefW,prefH);
		contentPane.setPreferredSize(d);
		contentPane.setMinimumSize(d);
		contentPane.setMaximumSize(d);
		contentPane.setSize(d); // ← esto fuerza el tamaño real

		contentPane.revalidate();
		contentPane.repaint();
	}
}
