package raytracer;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Luz;
import Model.ModeloLuz;
import Model.Point3D;
import Model.Vector3D;
import Objects.Esfera;
import Objects.Objeto;
import Objects.Plano;
import Objects.MallaTriangulos;
import View.Camera;
import View.Scene;

public class Programa {

	private static Color background;
	private static ArrayList<Objeto> objects;
	private static ArrayList<Luz> luzList;
	private static Point3D eye;
	private static Vector3D lookat;
	private static Vector3D up;

	public static void main(String[] args) {

		final int NUM_FILAS = 516;
		final int NUM_COL = 516;

		luzList = new ArrayList<Luz>();
		objects = new ArrayList<Objeto>();
		Scene escena = new Scene();
		Camera cam = new Camera();

		eye = new Point3D(20, 20, 20);
		lookat = new Vector3D(0, 0, 0);
		up = new Vector3D(-1, 0, 1);
		background = new Color((float) 0.2, (float) 0.8, (float) 0.9);

		/*
		 * LUCES
		 */
		Luz luzA = new Luz(Luz.AMBIENT, null, (float) 0.3, (float) 0.4,
				(float) 0.5);

		Luz luzPy = new Luz(Luz.POINT, new Vector3D(1 * 4, 5 * 4, 1 * 4),
				(float) 0.8, (float) 0.8, (float) 0.8);

		Luz luzPz = new Luz(Luz.POINT, new Vector3D(5, 2, 20), (float) 0.5,
				(float) 0.5, (float) 0.5);

		luzList.add(luzA);
		luzList.add(luzPy);
		luzList.add(luzPz);

		/*
		 * OBJETOS
		 */

		// ModeloLuz (Coef* ambiental, difusa, especular, reflejo, ns,
		// refraccion, indice)
		ModeloLuz m = new ModeloLuz(0.4, 0.6, 0, 0, 0, 0, 0);
		Plano p = new Plano(m, -3.65, 0, 0, 1, Color.green);
		objects.add(p);
		p = new Plano(m, -3.65, 0, 1, 0, Color.yellow);
		objects.add(p);
		p = new Plano(m, -3.65, 1, 0, 0, Color.blue);
		objects.add(p);

		ModeloLuz m1;
		Esfera e;
		// Ambiental
		m1 = new ModeloLuz(0.5, 0, 0, 0, 0, 0, 0);
		e = new Esfera(m1, new Vector3D(17, 10, 7), 1.5, Color.white);
		objects.add(e);
		// Ambiental + Difusa
		m1 = new ModeloLuz(0.2, 0.5, 0, 0, 0, 0, 0);
		e = new Esfera(m1, new Vector3D(13.5, 15, 8.5), 1.5, Color.white);
		objects.add(e);
		// Ambiental + Difusa + Especular
		m1 = new ModeloLuz(0.2, 0.5, 0.8, 0, 50, 0, 0);
		e = new Esfera(m1, new Vector3D(10, 19, 10), 1.5, Color.white);
		objects.add(e);
		// Ambiental + Difusa + Especular + Reflexion
		m1 = new ModeloLuz(0.2, 0.5, 0.8, 0.7, 50, 0, 0);
		e = new Esfera(m1, new Vector3D(8.5, 15, 13.5), 1.5, Color.white);
		objects.add(e);
		// Ambiental + Difusa + Especular + Reflexion + Refraccion
		m1 = new ModeloLuz(0.2, 0.4, 0.6, 0, 50, 0.7, 2.3);
		e = new Esfera(m1, new Vector3D(7, 10, 17), 1.5, Color.white);
		objects.add(e);

		// Añadimos el objeto creado con una malla de triangulos a traves de
		// un fichero

		try {
			MallaTriangulos mt = new MallaTriangulos(new Scanner(new File(
					"cow.txt")), 10, 7.5f, 10);
			objects.add(mt);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		escena.setObjects(objects);
		escena.setLights(luzList);
		cam.setEye(eye);
		Vector3D look = new Vector3D(eye.x - lookat.x, eye.y - lookat.y, eye.z
				- lookat.z);
		look.normalize();
		cam.setLookVector(look);
		cam.setUpVector(up);
		cam.setScreenDistance(25);
		cam.setCols(NUM_COL);
		cam.setFils(NUM_FILAS);
		cam.setViewportHeight(50);
		cam.setViewportWidth(50);
		cam.calculateVectors();

		escena.setBackgroundColor(background);

		TrazadorDeRayos rayTracer = new TrazadorDeRayos();
		rayTracer.setCamara(cam);
		rayTracer.setEscena(escena);
		rayTracer.setPantalla(NUM_COL, NUM_FILAS);
		// rayTracer.trazadorDeRayos(NUM_COL, NUM_FILAS);
		/*
		 * < Type of antialiasing: regular --- random
		 */
		// rayTracer.trazadorDeRayosSuperSampled(NUM_COL,
		// NUM_FILAS,1,"regular");
		rayTracer.trazadorDeRayosSuperSampled(NUM_COL, NUM_FILAS, 1, "random");
	}
}
