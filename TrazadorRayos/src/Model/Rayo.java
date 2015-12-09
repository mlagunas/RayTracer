package Model;
import java.awt.Color;
import java.util.ArrayList;

import Objects.Objeto;

public class Rayo {
	public static final double MAX_T = Double.MAX_VALUE;
	public Vector3D origin;
	public Vector3D direction;
	public double t;
	double t1;
	public Objeto object;

	public Rayo(Point3D point3d, Vector3D dir) {
		origin = new Vector3D(point3d);
		dir.normalize();
		direction = dir;
	}

	public boolean trace(ArrayList<Objeto> objects) {
		t = MAX_T;
		object = null;
		for (Objeto object : objects) {
			object.intersect(this);
		}
		return (object != null);
	}

	public final Color Shade(ArrayList<Luz> lights, ArrayList<Objeto> objects,
			Color bgnd, double kref) {
		return object.Shade(this, lights, objects, bgnd, 0, kref);
	}

	public final Color Shade(ArrayList<Luz> lights, ArrayList<Objeto> objects,
			Color bgnd, int nRayos, double kref) {
		return object.Shade(this, lights, objects, bgnd, nRayos, kref);
	}

	public boolean trace(Objeto object) {
		t = MAX_T;
		object.intersect(this);
		return (object != null);
	}

	public String toString() {
		return ("ray origin = " + origin + "  direction = " + direction
				+ "  t = " + t);
	}
}