import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TrazadorDeRayos {

	private Scene escena;

	private BufferedImage canvas;

	private Viewport pantalla;

	private int recursionProf;

	private Camera camara;

	public TrazadorDeRayos() {
	}

	public Scene getEscena() {
		return escena;
	}

	public void setEscena(Scene escena) {
		this.escena = escena;

	}

	public Viewport getPantalla() {
		return pantalla;
	}

	public void setPantalla(Viewport pantalla) {
		this.pantalla = pantalla;
		canvas = new BufferedImage(pantalla.getColPixels(),
				pantalla.getFilPixels(), BufferedImage.TYPE_INT_RGB);

	}

	public Camera getCamara() {
		return camara;
	}

	public void setCamara(Camera camara) {
		this.camara = camara;
	}

	public void trazadorDeRayos(int imageWidth, int imageHeight) {
		int pixelColor = 0;
		Color finalColor = null;
		int background = 0;
		int hitpixels=0;
		int nohit=0;
		// for each pixel of the image
		for (int j = 0; j < imageWidth; ++j) {
			for (int i = 0; i < imageHeight; ++i) {
				/* Construye el rayo que pasa por el pixel i,j */
				Rayo primRay = camara.constructRayThroughPixel(i
						- (imageHeight / 2), j - (imageWidth / 2));
				pixelColor = background;
				if (primRay != null) {
					/* Mira si intersecta y devuelve el punto a pintar */
					for (Objeto o : escena.getObjects()) {
						if (primRay.trace(o)) {
							finalColor = o.Shade(primRay, new Point3D(
									primRay.origin), escena.getLights(), escena
									.getObjects());
						}
						else{
						}
					}
					if (finalColor == null) {
						nohit++;
						finalColor = new Color(background);
					}
					else{
						hitpixels++;
					}

				}
				canvas.setRGB(j, i, finalColor.getRGB()); // Columna,fila
			}
		}
		System.out.println("HIT "+hitpixels);
		System.out.println("NO HIT "+nohit);
		new Render(canvas);
	}

	public void trazadorDeRayosSuperSampled(int imageHeight, int imageWidth,
			int radio) {
		int count = Camera.getSuperSampledCount(radio);
		ArrayList<Rayo> currentPrimaryRayList = new ArrayList<>(count);
		Rayo currentPrimaryRaySuperSample = null;
		Color finalColor=null;
		Color currentColor = null;
		float rSum = 0;
		float gSum = 0;
		float bSum = 0;
		int hitpixels=0;
		int nohit=0;
		int background = 0;
		int innerCount = 0;
		for (int j = 0; j < imageWidth; ++j) {
			for (int i = 0; i < imageHeight; ++i) {
				currentPrimaryRayList = camara.getPixelPositionSuperSampledList(i
						- (imageHeight / 2), j - (imageWidth / 2),radio);
				rSum = 0;
				gSum = 0;
				bSum = 0;
				innerCount = 0;
				for (int k = 0; k < count; k++) {
					currentPrimaryRaySuperSample = currentPrimaryRayList.get(k);
					if (currentPrimaryRaySuperSample != null) {
						for (Objeto o : escena.getObjects()) {
							if (currentPrimaryRaySuperSample.trace(o)) {
								currentColor = o.Shade(currentPrimaryRaySuperSample, new Point3D(
										currentPrimaryRaySuperSample.origin), escena.getLights(), escena.getObjects());
								if (currentColor!=null){
									System.out.println();
									rSum += currentColor.getRed();
									gSum += currentColor.getGreen();
									bSum += currentColor.getBlue();
									innerCount++;	
								}
							}					
						}
					}
				}
				if (innerCount==0) {
					finalColor = new Color(background);
					nohit++;
				}
				else{
					hitpixels++;
					finalColor = new Color((int)rSum / innerCount, (int)gSum / innerCount,
							(int)bSum / innerCount);
				}
				

				canvas.setRGB(j, i, finalColor.getRGB());
			}
		}
		System.out.println("HIT "+hitpixels);
		System.out.println("NO HIT "+nohit);
		new Render(canvas);

	}

}
