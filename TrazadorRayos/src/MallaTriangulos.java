import java.io.*;
import java.awt.*;
import java.util.*;

// A "Renderable" Triangulo Mesh
// This just holds a bunch of Triangulos, which are the actual objects rendered.
// The Ray.Object will never be a mesh but rather one of the Triangulos.
class MallaTriangulos implements Objeto {
	final static int CHUNKSIZE = 100;

	ArrayList<Vector3D> vertList;
	ArrayList<Triangulo> triList;
	ArrayList<ModeloLuz> modelList;
	int vertices, Triangulos, ModeloLuzs = 0;
	ModeloLuz currentModeloLuz;
	Color currentColor;
	int numf = 0;

	public MallaTriangulos(Scanner st) throws IOException {

		vertList = new ArrayList<Vector3D>();
		vertices = 0;
		triList = new ArrayList<Triangulo>();
		Triangulos = 0;
		modelList = new ArrayList<ModeloLuz>();
		ModeloLuzs = 0;
		int v0 = 0, v1 = 0, v2 = 0;
		try {
			while (st.hasNext()) {
				String instr = st.next();
				if (instr.equals("v")) {
					float x = (float) getNumber(st);
					float y = (float) getNumber(st);
					float z = (float) getNumber(st);
					vertList.add(new Vector3D(x, y, z));
				} else if (instr.equals("f")) {
					v0 = (int) getNumber(st);
					v1 = (int) getNumber(st);
					while (st.hasNextInt() || st.hasNextDouble()) {
						v2 = (int) getNumber(st);
						if (v2 == v0)
							break;
						Point3D a = new Point3D(vertList.get(v0));
						Point3D b = new Point3D(vertList.get(v1));
						Point3D c = new Point3D(vertList.get(v2));
						triList.add(new Triangulo(a, b, c, currentModeloLuz,
								currentColor));
						v1 = v2;
						Triangulos += 1;
					}

				} else if (instr.equals("surface")) {
					float r = (float) getNumber(st);
					float g = (float) getNumber(st);
					float b = (float) getNumber(st);
					float ka = (float) getNumber(st);
					float kd = (float) getNumber(st);
					float ks = (float) getNumber(st);
					float ns = (float) getNumber(st);
					float kt = (float) getNumber(st);
					float kr = (float) getNumber(st);
					float index = (float) getNumber(st);
					currentColor = new Color(r, g, b);
					currentModeloLuz = new ModeloLuz(ka, kd, ks, ns, kt, kr,
							index);
				}
			}
		} catch (Exception e) {
			System.err.println(v0 + " " + v1 + " " + v2);

		}
	}

	/**
	 * xr yr y zr representas tres valores para reajustar la posicion en la que
	 * mostrar la malla de triangulos
	 * 
	 * @param st
	 * @param xr
	 * @param yr
	 * @param zr
	 * @throws IOException
	 */
	public MallaTriangulos(Scanner st, float xr, float yr, float zr)
			throws IOException {

		vertList = new ArrayList<Vector3D>();
		vertices = 0;
		triList = new ArrayList<Triangulo>();
		Triangulos = 0;
		modelList = new ArrayList<ModeloLuz>();
		ModeloLuzs = 0;
		int v0 = 0, v1 = 0, v2 = 0;
		try {
			while (st.hasNext()) {
				String instr = st.next();
				if (instr.equals("v")) {
					float x = (float) getNumber(st);
					float y = (float) getNumber(st);
					float z = (float) getNumber(st);
					vertList.add(new Vector3D(x, y, z));
				} else if (instr.equals("f")) {
					v0 = (int) getNumber(st);
					v1 = (int) getNumber(st);
					while (st.hasNextInt() || st.hasNextDouble()) {
						v2 = (int) getNumber(st);
						if (v2 == v0)
							break;
						Point3D a = new Point3D(vertList.get(v0).x + xr,
								vertList.get(v0).y + yr, vertList.get(v0).z
										+ zr);
						Point3D b = new Point3D(vertList.get(v1).x + xr,
								vertList.get(v1).y + yr, vertList.get(v1).z
										+ zr);
						Point3D c = new Point3D(vertList.get(v2).x + xr,
								vertList.get(v2).y + yr, vertList.get(v2).z
										+ zr);
						triList.add(new Triangulo(a, b, c, currentModeloLuz,
								currentColor));
						v1 = v2;
						Triangulos += 1;
					}

				} else if (instr.equals("surface")) {
					float r = (float) getNumber(st);
					float g = (float) getNumber(st);
					float b = (float) getNumber(st);
					float ka = (float) getNumber(st);
					float kd = (float) getNumber(st);
					float ks = (float) getNumber(st);
					float ns = (float) getNumber(st);
					float kt = (float) getNumber(st);
					float kr = (float) getNumber(st);
					float index = (float) getNumber(st);
					currentColor = new Color(r, g, b);
					currentModeloLuz = new ModeloLuz(ka, kd, ks, ns, kt, kr,
							index);
				}
			}
		} catch (Exception e) {
			System.err.println(v0 + " " + v1 + " " + v2);

		}
	}

	public boolean intersect(Rayo ray) {
		int hit = 0;
		// Just call intersect for each of the Triangulos.
		for (Triangulo t : triList) {
			if (t.intersect(ray))
				hit++;
		}
		return (hit > 0);
	}

	public Color Shade(Rayo ray, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd) {
		// This should not really happen, but just in case.
		return ray.object.Shade(ray, lights, objects, bgnd, 0, 0);
	}

	public String toString() {
		return ("Triangulo mesh " + Triangulos + " Triangulos " + vertices
				+ " vertices " + ModeloLuzs + " ModeloLuzs");
	}

	private static double getNumber(Scanner st) throws IOException {
		if (st.hasNext()) {
			Double n = st.nextDouble();
			return n;
		}
		throw new IOException(st.toString());
	}

	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos,
			double currentRefr) {
		// TODO Auto-generated method stub
		return null;
	}
}
