import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrazadorDeRayos {
	public ArrayList<Objeto> createObjects(){
		return null;
	}
	public TrazadorDeRayos(Punto eyePosition, int imageHeight, int imageWidth){
		
		ArrayList<Objeto>objects=createObjects();
		
		// for each pixel of the image
		for (int j = 0; j < imageHeight; ++j) {
			for (int i = 0; i < imageWidth; ++i) {
				
				Map<String,String> parameters = new HashMap<String,String>();
				parameters.put("eye", "aa");
				parameters.put("direction", "aa");
				parameters.put("up-direction", "aa");
				parameters.put("look-at", "aa");
				parameters.put("screen-width", "aa");
				parameters.put("sreen-dist", "aa");

				Camera cam = new Camera(1,1);
				cam.init(parameters);
				
				/*
				 * Construye el rayo que pasa por el pixel i,j
				 */
				
				Rayo  primRay=cam.constructRayThroughPixel(i, j);
				
				/*
				 * Mira  si intersecta y devuelve el punto a pintar
				 */
//				pixels[i][j] = primRay.trace(objects);
			} 
		}
	}
	
}
