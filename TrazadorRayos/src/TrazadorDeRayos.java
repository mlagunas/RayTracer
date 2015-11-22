import java.util.ArrayList;

public class TrazadorDeRayos {
	
	public ArrayList<Objeto> createObjects(){
		return null;
	}
	public TrazadorDeRayos(int imageHeight, int imageWidth){
		
		ArrayList<Objeto>objects=createObjects();
		
		// for each pixel of the image
		for (int j = 0; j < imageHeight; ++j) {
			for (int i = 0; i < imageWidth; ++i) {
				Camera cam = new Camera();
				
				cam.setEye(new Point3D(3,1,3));
				cam.setLookVector(new Vector3D(1,0,0));
				cam.setUpVector(new Vector3D(1,0,0));
				cam.setScreenDistance(6);
				cam.setScreenWidth(5);
				cam.setScreenHeight(3);
				
				cam.calculateVectors();;
				
				/*
				 * Construye el rayo que pasa por el pixel i,j
				 */
				
				Rayo primRay=cam.constructRayThroughPixel(i, j);
				
				/*
				 * Mira  si intersecta y devuelve el punto a pintar
				 */
//				pixels[i][j] = primRay.trace(objects);
			} 
		}
	}
	
}
