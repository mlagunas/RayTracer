import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TrazadorDeRayos {

	private final double kref=1.00029;
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

	public void setPantalla(int width, int length) {
		this.pantalla = pantalla;
		canvas = new BufferedImage(width,
				length, BufferedImage.TYPE_INT_RGB);

	}

	public Camera getCamara() {
		return camara;
	}

	public void setCamara(Camera camara) {
		this.camara = camara;
	}

	public void trazadorDeRayos(int imageWidth, int imageHeight) {
		Color finalColor = null;
		int background = 0;
		int hitpixels = 0;
		int nohit = 0;
		// for each pixel of the image
		for (int j = 0; j < imageWidth; ++j) {
			for (int i = 0; i < imageHeight; ++i) {
				/* Construye el rayo que pasa por el pixel i,j */
				Rayo primRay = camara.constructRayThroughPixel(i
						- (imageHeight / 2), j - (imageWidth / 2));
				if (primRay != null) {
					/* Mira si intersecta y devuelve el punto a pintar */
					if (primRay.trace(escena.getObjects())) {
						hitpixels++;
						finalColor = primRay.Shade(escena.getLights(),
								escena.getObjects(), new Color(background),kref);
					} else {
						nohit++;
						finalColor = new Color(background);
					}
				}
				canvas.setRGB(j, i, finalColor.getRGB()); // Columna,fila
			}
		}
		System.out.println("HIT " + hitpixels);
		System.out.println("NO HIT " + nohit);
		new Render(canvas);
	}

	public void trazadorDeRayosSuperSampled(int imageHeight, int imageWidth,
			int radio, String type) {
		int count = Camera.getSuperSampledCount(radio);
		ArrayList<Rayo> currentPrimaryRayList = new ArrayList<>(count);
		Rayo currentPrimaryRaySuperSample = null;
		Color finalColor = null;
		Color currentColor = null;
		float rSum = 0;
		float gSum = 0;
		float bSum = 0;
		int background = 0;
		int innerCount = 0;
		for (int j = 0; j < imageWidth; ++j) {
			for (int i = 0; i < imageHeight; ++i) {
				if(type.equalsIgnoreCase("regular")){
					currentPrimaryRayList = camara
							.getPixelPositionSuperSampledList(
									i - (imageHeight / 2), j - (imageWidth / 2),
									radio);	
				}else{
					currentPrimaryRayList = camara
							.getPixelPositionSuperSampledListJittered(
									i - (imageHeight / 2), j - (imageWidth / 2),
									radio);	
				}
			
				rSum = 0;
				gSum = 0;
				bSum = 0;
				innerCount = 0;
				for (int k = 0; k < count; k++) {
					currentPrimaryRaySuperSample = currentPrimaryRayList.get(k);
					if(currentPrimaryRaySuperSample!=null){
						if (currentPrimaryRaySuperSample.trace(escena.getObjects())) {
							currentColor = currentPrimaryRaySuperSample.Shade(escena.getLights(),
									escena.getObjects(), new Color(background),kref);
							
								rSum += currentColor.getRed();
								gSum += currentColor.getGreen();
								bSum += currentColor.getBlue();
								innerCount++;
						}
						else{
							currentColor= new Color(background);
							rSum += currentColor.getRed();
							gSum += currentColor.getGreen();
							bSum += currentColor.getBlue();
							innerCount++;
						}
					}		
				}
				finalColor = new Color( (int) rSum / innerCount, (int) gSum
							/ innerCount, (int) bSum / innerCount);
				
				canvas.setRGB(j, i, finalColor.getRGB());
			}
		}
		new Render(canvas);

	}

}
