import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

	public boolean intersectRefraction(Rayo ray) {
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
		double t1 = v - (-Math.sqrt(res));
		if (round(t, 7) == 0 && round(t1, 7) == 0)
			return false;
		if (round(t, 7) == 0)
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
		double t1 = v - (-Math.sqrt(res));
		if ((t > ray.t) || (t < 0))
			return false;
		if (round(t, 7) == 0)
			ray.t = t1;
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

		// 1. (p) Punto de intersección rayo-objeto
		double px = (r.origin.x + r.t * r.direction.x);
		double py = (r.origin.y + r.t * r.direction.y);
		double pz = (r.origin.z + r.t * r.direction.z);

		Point3D p = new Point3D(px, py, pz);

		// 2. (n) Normal a la superficie
		Vector3D n = new Vector3D(px - center.x, py - center.y, pz - center.z);
		n.normalize();
//
//		// 3. (l) Rayo con dirección y sentido al foco de luz
//		Vector3D l = new Vector3D(-r.direction.x, -r.direction.y,
//				-r.direction.z);
//		l.normalize();

		// 4. (v) Rayo al ojo
		Vector3D v = new Vector3D(r.origin.x-px, r.origin.y-py, 
				r.origin.z-pz);
		v.normalize();
		Vector3D ref = null;
		
//		if (isMirror) {
//			// 5. (ref) Rayo reflejado
//			double twice = 2 * Vector3D.dotProd(v, n);
//			ref = Vector3D.sub(v, Vector3D.scale(twice, n));
//			ref.normalize();
//			double x = round(ref.x, 10);
//			double y = round(ref.y, 10);
//			double z = round(ref.z, 10);
//			if (x == 0 && y == 0 && z == 0) {
//				ref = null;
//			}
//			//ref = v.reflect(n);
//
//		}
	

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
					if (rayo.t != 0) {
						px = (rayo.origin.x + rayo.t * rayo.direction.x);
						py = (rayo.origin.y + rayo.t * rayo.direction.y);
						pz = (rayo.origin.z + rayo.t * rayo.direction.z);

						p1 = new Point3D(px, py, pz);

						// Normal a la superficie
						Vector3D n1 = new Vector3D(px - center.x,
								py - center.y, pz - center.z);
						n1.normalize();
						NiNr = m.index / currentRef;
						cosI = Vector3D.dotProd(n1, rayo.direction);
						cosR = Math
								.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
						if (cosR > 0.0) {
							frac = Vector3D.sub(
									Vector3D.scale((NiNr * cosI) - cosR, n1),
									Vector3D.scale(NiNr, rayo.direction));
							frac.normalize();
						}
					} else
						frac = null;
				}
			}
		}
		// Hacemos el calculo del color en ese pixel
		return m.calculo(color, bgnd, lights, objects, null, p, p1, n, v,
				r.origin, isMirror, ref, isTransparent, frac, nRayos,
				currentRef);
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
