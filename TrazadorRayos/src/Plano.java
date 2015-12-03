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
		if (t > ray.t || t <= 0.0006)
			return false;
		ray.t = t;
		ray.object = this;
		return true;
	}

	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos,
			double currentRefr) {
		Point3D p1 = null;

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
		Vector3D v = new Vector3D(r.origin.x-px, r.origin.y-py, r.origin.z-pz);
		v.normalize();
		

		Vector3D ref = null;
		if (isMirror) {
			// 5. (ref) Rayo reflejado
			double twice = 2 * Vector3D.dotProd(v, N);
			ref = Vector3D.sub(v, Vector3D.scale(twice, N));
			

		}

		Vector3D frac = null;
		if (isTransparent) { // Snell: sin(i)/sin(r) = nr/ni
			// 6. (frac) Rayo refractado

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
		}
		return m.calculo(color, bgnd, lights, objects, l, p, p1, N, v,
				r.origin, isMirror, ref, isTransparent, frac, nRayos,
				currentRefr);
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
