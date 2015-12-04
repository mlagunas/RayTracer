import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Programa {

	private static Color background;
	private static ArrayList<Objeto> objects;
	private static ArrayList<Luz> luzList;
	private static ModeloLuz currentSurface;
	private static Point3D eye;
	private static Vector3D lookat;
	private static Vector3D up;

	public static void main(String[] args) {

		final String PATH = "spheres2.txt";

		final int NUM_FILAS = 516;
		final int NUM_COL = 516;

		luzList = new ArrayList<Luz>();
		objects = new ArrayList<Objeto>();
		Scene escena = new Scene();
		Camera cam = new Camera();
		cam.setEye(new Point3D(02, 0, 0));
		cam.setLookVector(new Vector3D(1, 0, 0));
		cam.setUpVector(new Vector3D(0, 1, 0));
		cam.setScreenDistance(6);

		/*
		 * try { File f = new File(PATH); Scanner s = new Scanner(f);
		 * ReadInput(s); } catch (FileNotFoundException e1) {
		 * e1.printStackTrace(); } catch (IOException e1) {
		 * e1.printStackTrace(); }
		 */

		if (eye == null) {
			eye = new Point3D(10, 10, 10);
			lookat = new Vector3D(0, 0, 0);
			up = new Vector3D(0, 0, 1);
			background = new Color((float) 0.2, (float) 0.8, (float) 0.9);

			/*
			 * LUCES
			 */
			Luz luzA = new Luz(Luz.AMBIENT, null, (float) 0.3, (float) 0.4,
					(float) 0.5);

			Luz luzP = new Luz(Luz.POINT, new Vector3D(2 * 4, 5 * 4, 1 * 4),
					(float) 0.8, (float) 0.8, (float) 0.8);

			Luz luzP2 = new Luz(Luz.POINT, new Vector3D(10, 12, 20),
					(float) 0.5, (float) 0.5, (float) 0.5);

			luzList.add(luzA);
			luzList.add(luzP);

			/*
			 * OBJETOS
			 */
			ModeloLuz m = new ModeloLuz(0.2, 0.4, 0, 0.3, 50, 0, 0);
			Plano p = new Plano(m, 1, 0, 0, 1, Color.green);
			objects.add(p);
			p = new Plano(m, 0, 0, 1, 0, Color.red);
			objects.add(p);
			p = new Plano(m, 0, 1, 0, 0, Color.blue);
			objects.add(p);
			// ModeloLuz (Coeficiente ambiental, difusa, especular, reflejo, ns,
			// refraccion, indice)
			/*
			 * ModeloLuz m1 = new ModeloLuz(0.2, 0.4, 0.8, 1, 125, 0, 0); Esfera
			 * e = new Esfera(m1, new Vector3D(5, 2, 2), 1.5, Color.white, true,
			 * true); objects.add(e); m1 = new ModeloLuz(0.5, 0, 0, 0, 0, 0, 0);
			 * e = new Esfera(m1, new Vector3D(1, 2, 2), 1.5, Color.white, true,
			 * true); objects.add(e); m1 = new ModeloLuz(0.2, 0.5, 0, 0, 50, 0,
			 * 0); e = new Esfera(m1, new Vector3D(5, 5, 2), 1.5, Color.white,
			 * true, true); objects.add(e);
			 */
			ModeloLuz m1 = new ModeloLuz(0.2, 0.5, 0.9, 0.7, 10, 0, 0);
			Esfera e = new Esfera(m1, new Vector3D(1, 5, 2), 1.5, Color.white);
			objects.add(e);
			/*
			 * e = new Esfera(m, new Vector3D(0, -3, -2), 1.5, new Color(
			 * (float) 0.7, (float) 0.2, (float) 0.8),true,true);
			 * objects.add(e); e = new Esfera(m, new Vector3D(2, -3, -2), 1.5,
			 * new Color( (float) 0.7, (float) 0.2, (float) 0.8),true,true);
			 * objects.add(e); e = new Esfera(m, new Vector3D(0, 0, 0), 1.5, new
			 * Color( (float) 0.7, (float) 0.2, (float) 0.8),true,true);
			 * objects.add(e);
			 */

		}

		Viewport viewport = new Viewport(50, 50, new Point3D(0, 1, 5), NUM_COL,
				NUM_FILAS);
		escena.setObjects(objects);
		escena.setLights(luzList);
		cam.setEye(eye);
		Vector3D look = new Vector3D(lookat.x - eye.x, lookat.y - eye.y,
				lookat.z - eye.z);
		look.normalize();
		cam.setLookVector(look);
		cam.setUpVector(up);
		cam.setScreenDistance(35);
		cam.setViewport(viewport);
		cam.calculateVectors();
		escena.setBackgroundColor(background);
		/*
		 * Plano p = new Plano(new ModeloLuz(0.5, 0, 0, 0, 100,0), -100, 0, 100,
		 * 0, Color.white); // Plano p = new Plano(new ModeloLuz(0.3, 0.2, 0.2,
		 * 0, 50), new // Vector3D(1,0,0),0); objects.add(p);
		 */

		TrazadorDeRayos rayTracer = new TrazadorDeRayos();
		rayTracer.setCamara(cam);
		rayTracer.setPantalla(viewport);
		rayTracer.setEscena(escena);
		rayTracer.trazadorDeRayos(NUM_COL, NUM_FILAS);
		// rayTracer.trazadorDeRayosSuperSampled(NUM_COL, NUM_FILAS,0);
	}

	private static double getNumber(Scanner st) throws IOException {
		if (st.hasNext()) {
			Double n = st.nextDouble();
			// System.out.println(n);
			return n;
		}
		throw new IOException(st.toString());
	}

	private static void ReadInput(Scanner st) throws IOException {
		while (st.hasNext()) {
			String instr = st.next();
			System.out.println(instr);
			if (instr.equals("sphere")) {
				Vector3D v = new Vector3D((float) getNumber(st),
						(float) getNumber(st), (float) getNumber(st));
				float r = (float) getNumber(st);
				Color c = new Color((float) getNumber(st),
						(float) getNumber(st), (float) getNumber(st));
				objects.add(new Esfera(currentSurface, v, r, c));
			} else if (instr.equals("eye")) {
				eye = new Point3D((float) getNumber(st), (float) getNumber(st),
						(float) getNumber(st));
			} else if (instr.equals("lookat")) {
				lookat = new Vector3D((float) getNumber(st),
						(float) getNumber(st), (float) getNumber(st));
			} else if (instr.equals("up")) {
				up = new Vector3D((float) getNumber(st), (float) getNumber(st),
						(float) getNumber(st));
			} else if (instr.equals("background")) {
				background = new Color((float) getNumber(st),
						(float) getNumber(st), (float) getNumber(st));
			} else if (instr.equals("plane")) {
				Plano p = new Plano(currentSurface, getNumber(st),
						(float) getNumber(st), (float) getNumber(st),
						(float) getNumber(st), new Color((float) getNumber(st),
								(float) getNumber(st), (float) getNumber(st)));
				objects.add(p);
			} else if (instr.equals("light")) {
				float r = (float) getNumber(st);
				float g = (float) getNumber(st);
				float b = (float) getNumber(st);
				String type = st.next();
				if (type.equals("ambient")) {
					luzList.add(new Luz(Luz.AMBIENT, null, r, g, b));
				} else if (type.equals("directional")) {
					Vector3D v = new Vector3D((float) getNumber(st),
							(float) getNumber(st), (float) getNumber(st));
					luzList.add(new Luz(Luz.DIRECTIONAL, v, r, g, b));
				} else if (type.equals("point")) {
					Vector3D v = new Vector3D((float) getNumber(st),
							(float) getNumber(st), (float) getNumber(st));
					luzList.add(new Luz(Luz.POINT, v, r, g, b));
				} else {
					throw new IOException(st.toString());
				}
			} else if (instr.equals("surface")) {
				float ka = (float) getNumber(st);
				float kd = (float) getNumber(st);
				float ks = (float) getNumber(st);
				float kr = (float) getNumber(st);
				float ns = (float) getNumber(st);
				float kt = (float) getNumber(st);
				float index = (float) getNumber(st);
				currentSurface = new ModeloLuz(ka, kd, ks, ns, kr, kt, index);
			}
		}
		System.out.println("FIN");
	}
}
