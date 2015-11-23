import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TrazadorDeRayos {
	
	private Scene escena;
	
	private BufferedImage canvas;
	
	private Viewport pantalla;
	
	private int recursionProf;
	
	private Camera camara;
	
	
	public TrazadorDeRayos(){
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
		canvas = new BufferedImage(pantalla.getColPixels(), pantalla.getFilPixels(), BufferedImage.TYPE_INT_RGB);

	}
	public Camera getCamara() {
		return camara;
	}
	public void setCamara(Camera camara) {
		this.camara = camara;
	}
	
	public void trazadorDeRayos(int imageHeight, int imageWidth){
		int pixelColor=0;
		ColorRGB finalColor=null;
	    int background = 0;
		
		// for each pixel of the image
		for (int j = -((imageHeight/2)); j < (imageHeight/2); ++j) {
			for (int i = -((imageWidth/2)); i < (imageHeight/2); ++i) {			
				/* Construye el rayo que pasa por el pixel i,j*/
				System.out.println("Pixel "+j+i);
				Rayo primRay=camara.constructRayThroughPixel(i, j);
				pixelColor=background;
				if(primRay!=null){
					/* Mira  si intersecta y devuelve el punto a pintar*/
					//finalColor = primRay.trace(escena.getObjects());
					if(finalColor!=null){
						pixelColor = ColorRGB.saturateRGB(finalColor).toInt();
					}
				}
				canvas.setRGB(i, j, pixelColor);
			} 
		}
	}
	
	public void trazadorDeRayosSuperSampled(int imageHeight, int imageWidth, int radio) {
		int count = Camera.getSuperSampledCount(radio);
		ArrayList<Rayo> currentPrimaryRayList = new ArrayList<>(count);
		Rayo currentPrimaryRaySuperSample = null;
		ColorRGB finalColor;
		ColorRGB currentColor=null;
		float rSum = 0;
		float gSum = 0;
		float bSum = 0;

		//int background = scene.getBackgroundColor().toInt();
		int background=0;
		int pixelColor = -1;
		


		int innerCount = 0;
		for (int j = -((imageHeight/2)-1); j < (imageHeight/2); ++j) {
			for (int i = -((imageWidth/2)-1); i < (imageHeight/2); ++i) {
				currentPrimaryRayList = camara.getPimaryRaySuperSampledList(i, j, radio);
				pixelColor = background;
				rSum = 0;
				gSum = 0;
				bSum = 0;
				innerCount = 0;
				for (int k = 0; k < count; k++) {
					currentPrimaryRaySuperSample = currentPrimaryRayList.get(k);
					if (currentPrimaryRaySuperSample != null) {
						// currentColor = traceRay(currentPrimaryRaySuperSample,null, recursionProf);
						rSum += currentColor.getR();
						gSum += currentColor.getG();
						bSum += currentColor.getB();
						innerCount++;
					}
				}

				finalColor = new ColorRGB(rSum / innerCount, gSum /innerCount, bSum / innerCount);
				
				if (finalColor != null) {
					pixelColor = ColorRGB.saturateRGB(finalColor).toInt();
				}
				canvas.setRGB(i, j, pixelColor);
			}

		}
	}
	
}
