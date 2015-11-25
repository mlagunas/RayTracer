import java.awt.Color;
import java.util.ArrayList;

public class Programa {

	public static void main (String[] args){

		final int NUM_FILAS=516;
		final int NUM_COL=516;
		
		Viewport viewport=new Viewport(100, 50, new Point3D(9,1,3), NUM_COL, NUM_FILAS);
		
		Camera cam = new Camera();
		cam.setEye(new Point3D(0,0,0));
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
		Esfera e = new Esfera(new ModeloLuz(0.5, 0.1, 1, 0, 200), new Vector3D(10,0,0), 3, Color.green);
		objects.add(e);
		escena.setObjects(objects);
		Luz luz = new Luz(Luz.DIRECTIONAL, new Vector3D(1,2,3), new Color(1,4,2));
		escena.setLight(0, luz);
		
		
		TrazadorDeRayos rayTracer = new TrazadorDeRayos();
		rayTracer.setCamara(cam);
		rayTracer.setPantalla(viewport);
		rayTracer.setEscena(escena);
		
		rayTracer.trazadorDeRayos(NUM_COL,NUM_FILAS); 
	}
}
