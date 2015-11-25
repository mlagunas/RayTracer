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
					}
					if (finalColor == null) {
						finalColor = new Color(background);
					}

				}
				canvas.setRGB(j, i, finalColor.getRGB()); // Columna,fila
			}
		}
		new Render(canvas);
	}

	public void trazadorDeRayosSuperSampled(int imageHeight, int imageWidth,
			int radio) {
		int count = Camera.getSuperSampledCount(radio);
		ArrayList<Rayo> currentPrimaryRayList = new ArrayList<>(count);
		Rayo currentPrimaryRaySuperSample = null;
		ColorRGB finalColor;
		ColorRGB currentColor = null;
		float rSum = 0;
		float gSum = 0;
		float bSum = 0;

		// int background = scene.getBackgroundColor().toInt();
		int background = 0;
		int pixelColor = -1;

		int innerCount = 0;
		for (int j = -((imageHeight / 2)); j < (imageHeight / 2); ++j) {
			for (int i = -((imageWidth / 2)); i < (imageHeight / 2); ++i) {
				currentPrimaryRayList = camara.getPimaryRaySuperSampledList(i,
						j, radio);
				pixelColor = background;
				rSum = 0;
				gSum = 0;
				bSum = 0;
				innerCount = 0;
				for (int k = 0; k < count; k++) {
					currentPrimaryRaySuperSample = currentPrimaryRayList.get(k);
					if (currentPrimaryRaySuperSample != null) {
						// currentColor =
						// traceRay(currentPrimaryRaySuperSample,null,
						// recursionProf);
						rSum += currentColor.getR();
						gSum += currentColor.getG();
						bSum += currentColor.getB();
						innerCount++;
					}
				}

				finalColor = new ColorRGB(rSum / innerCount, gSum / innerCount,
						bSum / innerCount);

				if (finalColor != null) {
					pixelColor = ColorRGB.saturateRGB(finalColor).toInt();
				}
				canvas.setRGB(i, j, pixelColor);
			}

		}
	}

}
