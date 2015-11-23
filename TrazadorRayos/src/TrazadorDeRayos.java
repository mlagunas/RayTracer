import java.util.ArrayList;

public class TrazadorDeRayos {
	
	public ArrayList<Objeto> createObjects(){
		return null;
	}
	public void trazadorDeRayos(int imageHeight, int imageWidth){
		
		ArrayList<Objeto>objects=createObjects();
		Camera cam = new Camera();
		Viewport viewport=new Viewport(100, 50, new Point3D(9,1,3), 5, 3);
		cam.setEye(new Point3D(3,1,3));
		cam.setLookVector(new Vector3D(1,0,0));
		cam.setUpVector(new Vector3D(0,1,0));
		cam.setScreenDistance(6);
		cam.setViewport(viewport);
		cam.calculateVectors();;
		
		// for each pixel of the image
		for (int j = -((imageHeight/2)); j < (imageHeight/2); ++j) {
			for (int i = -((imageWidth/2)); i < (imageHeight/2); ++i) {			
				/* Construye el rayo que pasa por el pixel i,j*/
				System.out.println("Pixel "+j+i);
				Rayo primRay=cam.constructRayThroughPixel(i, j);
				
				/* Mira  si intersecta y devuelve el punto a pintar*/
//				pixels[i][j] = primRay.trace(objects);
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

		// int background = scene.getBackgroundColor().toInt();
		int pixelColor = -1;

		Viewport viewport=new Viewport(100, 50, new Point3D(9,1,3), 5, 3);
		Camera cam = new Camera();
		cam.setEye(new Point3D(3, 1, 3));
		cam.setLookVector(new Vector3D(1, 0, 0));
		cam.setUpVector(new Vector3D(1, 0, 0));
		cam.setScreenDistance(6);
		cam.setViewport(viewport);

		cam.calculateVectors();

		int innerCount = 0;
		for (int j = -((imageHeight/2)-1); j < (imageHeight/2); ++j) {
			for (int i = -((imageWidth/2)-1); i < (imageHeight/2); ++i) {
				currentPrimaryRayList = cam.getPimaryRaySuperSampledList(i, j, radio);

				// pixelColor = background;
				rSum = 0;
				gSum = 0;
				bSum = 0;
				innerCount = 0;
				for (int k = 0; k < count; k++) {
					currentPrimaryRaySuperSample = currentPrimaryRayList.get(k);
					if (currentPrimaryRaySuperSample != null) {
						// currentColor = traceRay(currentPrimaryRaySuperSample,
						// null, recursionDeep);
						rSum += currentColor.getX();
						gSum += currentColor.getY();
						bSum += currentColor.getZ();
						innerCount++;
					}
				}

				finalColor = new ColorRGB(rSum / innerCount, gSum /innerCount, bSum / innerCount);
				
				if (finalColor != null) {
					pixelColor = ColorRGB.saturateRGB(finalColor).toInt();
				}
				// canvas.setRGB(i, j, pixelColor);
			}

		}
	}
	
}
