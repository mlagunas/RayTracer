import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

public class Esfera implements Objeto {
	Vector3D center;
	double radius;
	double radSqr;
	ModeloLuz m;

	public Esfera(ModeloLuz m, Vector3D c, double r) {
		this.m = m;
		center = c;
		radius = r;
		radSqr = r * r;
	}

	public boolean intersect(Rayo ray) {
		double dx = (center.x - ray.origin.x);
		double dy = center.y - ray.origin.y;
		double dz = center.z - ray.origin.z;
		double v = ray.direction.dotProd(new Vector3D(dx, dy, dz));

		// Do the following quick check to see if there is even a chance
		// that an intersection here might be closer than a previous one
		if (v - radius > ray.t)
			return false;

		// Test if the ray actually intersects the sphere
		double t = radSqr + v * v - dx * dx - dy * dy - dz * dz;
		if (t < 0)
			return false;

		// Test if the intersection is in the positive
		// ray direction and it is the closest so far
		t = v - (Math.sqrt(t));
		if ((t > ray.t) || (t < 0))
			return false;

		ray.t = t;
		ray.object = this;
		return true;
	}

	public Color Shade(Rayo r, Point3D eye, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd) {

		// 0. (r) opuesto de L

		// 1. (p) Punto de intersección rayo-objeto
		double px = (r.origin.x + r.t * r.direction.x);
		double py = (r.origin.y + r.t * r.direction.y);
		double pz = (r.origin.z + r.t * r.direction.z);

		Point3D p = new Point3D(px, py, pz);

		// 2. (n) Normal a la superficie
		Vector3D n = new Vector3D(px - center.x, py - center.y, pz - center.z);
		n.normalize();

		// 3. (l) Rayo con dirección y sentido al foco de luz
		Vector3D l = new Vector3D(-r.direction.x, -r.direction.y,
				-r.direction.z);

		// 4. (v) Rayo al ojo
		Vector3D v = new Vector3D(px - eye.x, py - eye.y, pz - eye.z);

		// 5. (ref) Rayo reflejado
		double twice = 2 * Vector3D.dotProd(v, n);
		Vector3D ref = Vector3D.sub(v, Vector3D.scale(twice, n));

		// 6. (frac) Rayo refractado
		Vector3D frac = null;

		// Hacemos el calculo del color en ese pixel
		return m.calculo(bgnd, lights, objects, l, p, n, v, r.origin, ref,frac);
	}

	public String toString() {
		return ("sphere " + center + " " + radius);
	}
}
