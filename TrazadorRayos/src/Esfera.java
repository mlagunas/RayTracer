
import java.awt.Color;
import java.util.Vector;

public class Esfera implements Objeto {
	Superficie surface;
	Vector3D center;
	float radius;
	float radSqr;

	public Esfera(Superficie s, Vector3D c, float r) {
		surface = s;
		center = c;
		radius = r;
		radSqr = r * r;
	}

	public boolean intersect(Rayo ray) {
		float dx = center.x - ray.origin.x;
		float dy = center.y - ray.origin.y;
		float dz = center.z - ray.origin.z;
		float v = ray.direction.escalar(dx, dy, dz);

		// Do the following quick check to see if there is even a chance
		// that an intersection here might be closer than a previous one
		if (v - radius > ray.t)
			return false;

		// Test if the ray actually intersects the sphere
		float t = radSqr + v * v - dx * dx - dy * dy - dz * dz;
		if (t < 0)
			return false;

		// Test if the intersection is in the positive
		// ray direction and it is the closest so far
		t = v - ((float) Math.sqrt(t));
		if ((t > ray.t) || (t < 0))
			return false;

		ray.t = t;
		ray.object = this;
		return true;
	}

	public Color Shade(Rayo ray, Vector lights, Vector objects, Color bgnd) {
		// An object shader doesn't really do too much other than
		// supply a few critical bits of geometric information
		// for a surface shader. It must must compute:
		//
		// 1. the point of intersection (p)
		// 2. a unit-length surface normal (n)
		// 3. a unit-length vector towards the ray's origin (v)
		//
		float px = ray.origin.x + ray.t * ray.direction.x;
		float py = ray.origin.y + ray.t * ray.direction.y;
		float pz = ray.origin.z + ray.t * ray.direction.z;

		Vector3D p = new Vector3D(px, py, pz);
		Vector3D v = new Vector3D(-ray.direction.x, -ray.direction.y,
				-ray.direction.z);
		Vector3D n = new Vector3D(px - center.x, py - center.y, pz - center.z);
		n.normalize();

		// The illumination model is applied
		// by the surface's Shade() method
		return surface.Shade(p, n, v, lights, objects, bgnd);
	}

	public String toString() {
		return ("sphere " + center + " " + radius);
	}
}
