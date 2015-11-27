import java.awt.Color;
import java.util.ArrayList;

import java.awt.Color;
import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {

		final int NUM_FILAS = 516;
		final int NUM_COL = 516;

		Viewport viewport = new Viewport(50, 50, new Point3D(9, 1, 3), NUM_COL,
				NUM_FILAS);

		Camera cam = new Camera();
		cam.setEye(new Point3D(0, 0, 0));
		cam.setLookVector(new Vector3D(1, 0, 0));
		cam.setUpVector(new Vector3D(0, 1, 0));
		cam.setScreenDistance(6);
		cam.setViewport(viewport);
		cam.calculateVectors();

		Scene escena = new Scene();
		Color backgroundColor = Color.black;
		escena.setBackgroundColor(backgroundColor);
		ArrayList<Objeto> objects = new ArrayList<>();
		// Plano e = new Plano(new ModeloLuz(0.5, 0.5, 0.5, 0, 100), 5, 5, 2, 1,
		// Color.white);
		Esfera e = new Esfera(new ModeloLuz(0.3, 0.2, 0.2, 0, 50), new Vector3D(10,
				0, 0), 5, Color.green);
		objects.add(e);
		/*e = new Esfera(new ModeloLuz(0.2, 0.2, 0.2, 0, 5),
				new Vector3D(7, 0, 0), 3, Color.red);
		objects.add(e);*/

		escena.setObjects(objects);
		ArrayList<Luz> lights = new ArrayList<Luz>();
		Luz luzA = new Luz(Luz.AMBIENT, new Vector3D(0, 0, 0), new Color(255,
				255, 255));
		lights.add(luzA);
		Luz luz = new Luz(Luz.DIRECTIONAL, new Vector3D(1, 2, 3), new Color(1,
				4, 2));
		lights.add(luz);
		escena.setLights(lights);

		TrazadorDeRayos rayTracer = new TrazadorDeRayos();
		rayTracer.setCamara(cam);
		rayTracer.setPantalla(viewport);
		rayTracer.setEscena(escena);

		//rayTracer.trazadorDeRayos(NUM_COL, NUM_FILAS);

		rayTracer.trazadorDeRayosSuperSampled(NUM_COL, NUM_FILAS,1);
	}
}
