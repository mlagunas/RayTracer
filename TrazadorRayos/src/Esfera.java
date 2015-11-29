import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

public class Esfera implements Objeto {
	Vector3D center;
	double radius;
	double radSqr;
	ModeloLuz m;
	Color color;
	boolean isMirror;
	boolean isTransparent;

	public Esfera(ModeloLuz m, Vector3D c, double r, Color color,
			boolean mirror, boolean transp) {
		this.color = color;
		this.m = m;
		center = c;
		radius = r;
		radSqr = r * r;
		this.isMirror = mirror;
		this.isTransparent = transp;
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

	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos, double currentRef) {
		// Punto de interseccion entre el refractado y la esfera(Por el interior
		// de la esfera)
		Point3D p1 = null;

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
		Vector3D v = new Vector3D(px - r.origin.x, py - r.origin.y, pz
				- r.origin.z);

		Vector3D ref = null;
		if (isMirror) {
			// 5. (ref) Rayo reflejado
			double twice = 2 * Vector3D.dotProd(v, n);
			ref = Vector3D.sub(v, Vector3D.scale(twice, n));
		}

		// 6. (frac) Rayo refractado
		Vector3D frac = null;
		if (isTransparent) {
			// Snell: sin(i)/sin(r) = nr/ni
			double NiNr = currentRef / m.index;
			double cosI = Vector3D.dotProd(n, r.direction);
			double cosR = Math
					.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));

			if (cosR > 0.0) {
				// frac =
				// Vector3D.add(Vector3D.scale(NiNr,r.direction),Vector3D.scale((NiNr*cosI)-cosR,
				// n));
				// frac=Vector3D.sub(Vector3D.scale((NiNr*cosI-Math.sqrt(1-NiNr*NiNr*(1-(cosI*cosI)))),n),Vector3D.scale(NiNr,r.direction));
				Vector3D frac1 = Vector3D.sub(
						Vector3D.scale((NiNr * cosI) - cosR, n),
						Vector3D.scale(NiNr, r.direction));
				frac1.normalize();

				Rayo rayo = new Rayo(p, frac1);
				if (this.intersect(rayo)) {
					px = (rayo.origin.x + rayo.t * rayo.direction.x);
					py = (rayo.origin.y + rayo.t * rayo.direction.y);
					pz = (rayo.origin.z + rayo.t * rayo.direction.z);

					p1 = new Point3D(px, py, pz);

					// Normal a la superficie
					n = new Vector3D(px - center.x, py - center.y, pz
							- center.z);
					n.normalize();
					NiNr = m.index / currentRef;
					cosI = Vector3D.dotProd(n, rayo.direction);
					cosR = Math
							.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
					if (cosR > 0.0) {
						frac = Vector3D.sub(
								Vector3D.scale((NiNr * cosI) - cosR, n),
								Vector3D.scale(NiNr, rayo.direction));
						frac.normalize();
					}
				}
			}
		}

		// Hacemos el calculo del color en ese pixel
		return m.calculo(color, bgnd, lights, objects, l, p, p1, n, v,
				r.origin, isMirror, ref, isTransparent, frac, nRayos,
				currentRef);
	}

	public String toString() {
		return ("sphere " + center + " " + radius);
	}
}
