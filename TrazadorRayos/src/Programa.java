import java.awt.Color;
import java.util.ArrayList;

public class Programa {

	public static void main (String[] args){

		final int NUM_FILAS=3;
		final int NUM_COL=5;
		
		Viewport viewport=new Viewport(100, 50, new Point3D(9,1,3), NUM_COL, NUM_FILAS);
		
		Camera cam = new Camera();
		cam.setEye(new Point3D(3,1,3));
		cam.setLookVector(new Vector3D(1,0,0));
		cam.setUpVector(new Vector3D(0,1,0));
		cam.setScreenDistance(6);
		cam.setViewport(viewport);
		cam.calculateVectors();
		
		Scene escena = new Scene();
		ColorRGB ambientLight = new ColorRGB();
		escena.setAmbientLight(ambientLight);
		ColorRGB backgroundColor = new ColorRGB();
		escena.setBackgroundColor(backgroundColor);
		ArrayList<Objeto> objects = new ArrayList<>();
		escena.setObjects(objects);
		Luz luz = new Luz(Luz.DIRECTIONAL, new Vector3D(), new Color(1,1,1));
		escena.setLight(0, luz);
		
		TrazadorDeRayos rayTracer = new TrazadorDeRayos();
		rayTracer.setCamara(cam);
		rayTracer.setPantalla(viewport);
		rayTracer.setEscena(escena);
		
		rayTracer.trazadorDeRayos(NUM_FILAS, NUM_COL); //filas,columnas
	}
}
