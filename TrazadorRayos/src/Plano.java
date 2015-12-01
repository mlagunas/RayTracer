import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

// Ecuacion plano
// vector normalizado perpendicular a la superficie del mismo N = (nx, ny, nz),
// y la distancia d que le separa del origen de coordenadas
// nx * x + ny * y + nz * z + d = 0
public class Plano implements Objeto {

	Vector3D N;
	ModeloLuz m;
	double d;
	private Color color;
	boolean isMirror;
	boolean isTransparent;

	public Plano(ModeloLuz m, double d, float x, float y, float z, Color color,
			boolean mirror, boolean transparent) {
		this.color = color;
		this.m = m;
		this.N = new Vector3D(x, y, z);
		this.d = d;
		this.isMirror = mirror;
		this.isTransparent = transparent;
	}

	// Rayo = R(t) = O + D * t, con su origen O = (ox, oy, oz) y vector
	// dirección normalizado D = (dx, dy, dz)
	// Verificará nx * (ox + dx * t) + ny * (oy + dy * t) + nz * (oz + dz * t) +
	// d = 0
	// nx * dx + ny * dy + nz * dz = N · D
	// t = - (nx * ox + ny * oy + nz * oz + d) / (nx * dx + ny * dy + nz * dz)
	/*
	 * @Override public boolean intersect(Rayo r) { Vector3D o = r.origin;
	 * Vector3D d = r.direction; double a = (n.x * o.x + n.y * o.y + n.z * o.z)
	 * + this.d; double b = (n.x * d.x + n.y * d.y + n.z * d.z);
	 * 
	 * if (b == 0) return false; double t = -a / b; // if(t<0) // return false;
	 * return true;
	 * 
	 * }
	 */

	/**
	 * Find the intersection of the plane and a given ray.
	 *
	 * The return value is positive is the intersection is found and this value
	 * gives the distance along the ray. Negative values imply that the
	 * intersection was either not successful or the intersection point was
	 * before the origin. This value can be used with the pointAt method of the
	 * Ray class (@see Ray#pointAt)
	 *
	 * @param ray
	 *            the ray to intersect with
	 * @return a <code>double</code> value that gives the distance along the
	 *         ray.
	 */
	public boolean intersect(Rayo ray) {
		double d1, dn, t;

		/* Calculo de D·N */
		dn = Vector3D.dotProd(ray.direction, N);

		/* Rayo paralelo al plano o no intersecta detrás de la pantalla */
		if (dn == 0) {
			return false;
		}

		d1 = Vector3D.dotProd(new Vector3D(ray.origin.x, ray.origin.y,
				ray.origin.z), N);
		t = (d - d1) / dn;
		if (t > ray.t)
			return false;
		ray.t = t;
		ray.object = this;
		return true;
	}

	public double distancia(Rayo ray) {
		// Distancia Punto-Plano
		Vector3D punto = ray.origin;
		double a = Math.abs(punto.x * N.x + punto.y * N.y + punto.z * N.z + d);
		double b = Math.sqrt(N.x * N.x + N.y * N.y + N.z * N.z);

		return a / b;
	}

	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos,
			double currentRefr) {
		Point3D p1 =null;
		// 0. (r) opuesto de L

		// 1. (p) Punto de intersección rayo-objeto
		double px = (r.origin.x + r.t * r.direction.x);
		double py = (r.origin.y + r.t * r.direction.y);
		double pz = (r.origin.z + r.t * r.direction.z);

		Point3D p = new Point3D(px, py, pz);

		// 2. (n) Normal a la superficie igual a n

		// 3. (l) Rayo con dirección y sentido al foco de luz
		Vector3D l = new Vector3D(-r.direction.x, -r.direction.y,
				-r.direction.z);

		// 4. (v) Rayo al ojo
		Vector3D v = new Vector3D(px - r.origin.x, py - r.origin.y, pz
				- r.origin.z);

		Vector3D ref = null;
		if (isMirror) {
			// 5. (ref) Rayo reflejado
			ref = r.origin.reflect(N);
		}

		Vector3D frac = null;
		if (isTransparent) {
			// Snell: sin(i)/sin(r) = nr/ni
			double NiNr = currentRefr / m.index;
			double cosI = Vector3D.dotProd(N, r.direction);
			double cosR = Math
					.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));

			if (cosR > 0.0) {
				// frac =
				// Vector3D.add(Vector3D.scale(NiNr,r.direction),Vector3D.scale((NiNr*cosI)-cosR,
				// n));
				// frac=Vector3D.sub(Vector3D.scale((NiNr*cosI-Math.sqrt(1-NiNr*NiNr*(1-(cosI*cosI)))),n),Vector3D.scale(NiNr,r.direction));
				Vector3D frac1 = Vector3D.sub(
						Vector3D.scale((NiNr * cosI) - cosR, N),
						Vector3D.scale(NiNr, r.direction));
				frac1.normalize();

				Rayo rayo = new Rayo(p, frac1);
				if (this.intersect(rayo)) {
					px = (rayo.origin.x + rayo.t * rayo.direction.x);
					py = (rayo.origin.y + rayo.t * rayo.direction.y);
					pz = (rayo.origin.z + rayo.t * rayo.direction.z);

					p1 = new Point3D(px, py, pz);

					// Normal a la superficie
					N.normalize();
					NiNr = m.index / currentRefr;
					cosI = Vector3D.dotProd(N, rayo.direction);
					cosR = Math
							.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
					if (cosR > 0.0) {
						frac = Vector3D.sub(
								Vector3D.scale((NiNr * cosI) - cosR, N),
								Vector3D.scale(NiNr, rayo.direction));
						frac.normalize();
					}
				}
			}
		}
		

		// The illumination model is applied
		// by the surface's Shade() method
		return m.calculo(color, bgnd, lights, objects, l, p, null, N, v,
				r.origin, isMirror, ref, isTransparent, frac, nRayos,
				currentRefr);
	}

}
