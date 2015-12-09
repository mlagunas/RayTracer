package Objects;

import java.awt.Color;
import java.util.ArrayList;

import Model.Luz;
import Model.ModeloLuz;
import Model.Point3D;
import Model.Rayo;
import Model.Vector3D;

// Ecuacion plano
// vector normalizado perpendicular a la superficie del mismo N = (nx, ny, nz),
// y la distancia d que le separa del origen de coordenadas
// nx * x + ny * y + nz * z + d = 0
public class Plano implements Objeto {

	Vector3D N;
	ModeloLuz m;
	double d;
	private Color color;

	public Plano(ModeloLuz m, double d, float x, float y, float z, Color color) {
		this.color = color;
		this.m = m;
		this.N = new Vector3D(x, y, z);
		this.d = d;
		;
	}

	// Rayo = R(t) = O + D * t, con su origen O = (ox, oy, oz) y vector
	// dirección normalizado D = (dx, dy, dz)
	// Verificará nx * (ox + dx * t) + ny * (oy + dy * t) + nz * (oz + dz * t) +
	// d = 0
	// nx * dx + ny * dy + nz * dz = N · D
	// t = - ((nx * ox + ny * oy + nz * oz) + d) / (nx * dx + ny * dy + nz * dz)
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
		t = -(d + d1) / dn;
		if (t > ray.t || t <= 0.00006)
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

		// 3. (v) Rayo al ojo
		Vector3D v = new Vector3D(r.origin.x - px, r.origin.y - py, r.origin.z
				- pz);
		v.normalize();

		Rayo rfrac=null;
		if (m.kt > 0) {
			// Snell: sin(i)/sin(r) = nr/ni
			double NiNr = currentRefr / m.index;
			double cosI = Vector3D.dotProd(N, v);
			double cosR = (1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
			double root = Math.sqrt(cosR);
			if (root >= 0) {
				Vector3D frac1 = Vector3D.sub(
						Vector3D.scale((NiNr * cosI) - root, N),
						Vector3D.scale(NiNr, v));
				frac1.normalize();

				rfrac = new Rayo(p, frac1);		
				rfrac.trace(objects);
			}

		}
		return m.calculo(color, bgnd, lights, objects, p, p1, N, v, r.origin,
				rfrac, nRayos, currentRefr);
	}

}
