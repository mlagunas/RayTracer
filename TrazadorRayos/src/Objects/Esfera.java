package Objects;
import java.awt.Color;
import java.util.ArrayList;

import Model.Luz;
import Model.ModeloLuz;
import Model.Point3D;
import Model.Rayo;
import Model.Vector3D;

public class Esfera implements Objeto {
	Vector3D center;
	double radius;
	double radSqr;
	ModeloLuz m;
	Color color;

	public Esfera(ModeloLuz m, Vector3D c, double r, Color color) {
		this.color = color;
		this.m = m;
		center = c;
		radius = r;
		radSqr = r * r;

	}

	public boolean intersectRefraction(Rayo ray) {
		double dx = (center.x - ray.origin.x);
		double dy = center.y - ray.origin.y;
		double dz = center.z - ray.origin.z;
		double v = ray.direction.dotProd(new Vector3D(dx, dy, dz));

		// Test if the ray actually intersects the sphere
		double res = radSqr + v * v - dx * dx - dy * dy - dz * dz;
		if (res < 0)
			return false;

		double t = v - (Math.sqrt(res));
		double t1 = v + (Math.sqrt(res));
		if (round(t, 7) <= 0 && round(t1, 7) <= 0)
			return false;
		if (round(t, 7) <= 0)
			ray.t = t1;
		else
			ray.t = t;

		ray.object = this;
		return true;
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
		double res = radSqr + v * v - dx * dx - dy * dy - dz * dz;
		if (res < 0)
			return false;

		// Test if the intersection is in the positive
		// ray direction and it is the closest so far
		double t = v - (Math.sqrt(res));

		if ((t > ray.t) || (t < 0.00006))
			return false;
		else
			ray.t = t;

		ray.object = this;
		return true;
	}

	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos, double currentRef) {
		// Punto de interseccion entre el refractado y la esfera(Por el interior
		// de la esfera)
		Point3D p1 = null;

		// 1. (p) Punto de intersecci�n rayo-objeto
		double px = (r.origin.x + r.t * r.direction.x);
		double py = (r.origin.y + r.t * r.direction.y);
		double pz = (r.origin.z + r.t * r.direction.z);

		Point3D p = new Point3D(px, py, pz);

		// 2. (n) Normal a la superficie
		Vector3D n = new Vector3D(px - center.x, py - center.y, pz - center.z);
		n.normalize();

		// 3. (v) Vector al ojo
		Vector3D v = new Vector3D(r.origin.x - px, r.origin.y - py, r.origin.z
				- pz);
		v.normalize();

		// 4. (frac) Rayo refractado
		Rayo rfrac = null;

		// if (m.kt > 0) {
		// // Calculo de la interseccion del rayo con el objeto mas cercano el
		// // cual no es la
		// // esfera ya intersectada anteriormente.
		//
		// // R ira del punto P al ojo -> Refractado sentido opuesto
		// rfrac = new Rayo(p, r.direction);
		// rfrac.trace(objects);
		// System.out.println(rfrac.object);
		//
		// }
		Vector3D frac = null;
		if (m.kt > 0) {
			// Snell: sin(i)/sin(r) = nr/ni
			double NiNr = currentRef / m.index;
			double cosI = Vector3D.dotProd(n, v);
			double cosR = (1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
			double root = Math.sqrt(cosR);
			if (root >= 0) {
				Vector3D frac1 = Vector3D.sub(
						Vector3D.scale((NiNr * cosI) - root, n),
						Vector3D.scale(NiNr, v));
				frac1.normalize();

				Rayo rayo = new Rayo(p, frac1);
				if (intersectRefraction(rayo)) {
					px = (rayo.origin.x + rayo.t * rayo.direction.x);
					py = (rayo.origin.y + rayo.t * rayo.direction.y);
					pz = (rayo.origin.z + rayo.t * rayo.direction.z);

					p1 = new Point3D(px, py, pz);

					// Vector al ojo
					Vector3D v2 = new Vector3D(rayo.origin.x - px,
							rayo.origin.y - py, rayo.origin.z - pz);
					v2.normalize();
					// Normal a la superficie
					Vector3D n1 = new Vector3D(px - center.x, py - center.y, pz
							- center.z);
					n1.normalize();

					NiNr = m.index / currentRef;
					cosI = Vector3D.dotProd(n1, v2);
					cosR = (1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
					root = Math.sqrt(cosR);
					frac = Vector3D.sub(
							Vector3D.scale((NiNr * cosI) - root, n1),
							Vector3D.scale(NiNr, v2));
					frac.normalize();
					rfrac = new Rayo(p1, frac);
					rfrac.trace(objects);

				}
			} else {
				rfrac = null;
			}
		}

		// Hacemos el calculo del color en ese punto
		return m.calculo(color, bgnd, lights, objects, p, p1, n, v, r.origin,
				rfrac, nRayos, currentRef);
	}

	public String toString() {
		return ("sphere " + center + " " + radius);
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
