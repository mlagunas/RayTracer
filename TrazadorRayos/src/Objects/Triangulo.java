package Objects;

import java.awt.Color;
import java.util.ArrayList;

import Model.Luz;
import Model.ModeloLuz;
import Model.Point3D;
import Model.Rayo;
import Model.Vector3D;

public class Triangulo implements Objeto {

	protected Point3D vert[];
	protected Vector3D N;
	protected ModeloLuz m;
	private Vector3D p3p1, p2p1, p3p2; // vectores de los lados
	private Color color;
	boolean isMirror;
	boolean isTransparent;

	public Triangulo(Point3D vert1, Point3D vert2, Point3D vert3,
			ModeloLuz modeloLuz, Color color) {
		super();
		vert = new Point3D[3];
		vert[0] = vert1;
		vert[1] = vert2;
		vert[2] = vert3;
		p2p1 = vert[1].to(vert[0]);
		p3p2 = vert[2].to(vert[1]);
		p3p1 = vert[2].to(vert[0]);
		// Plane equation: normal . x = offset
		N = Vector3D.crossProd(p2p1, p3p1);
		N.normalize();
		this.m = modeloLuz;
		this.color = color;
	}

	@Override
	public boolean intersect(Rayo r) {
		double d1, dn, t;

		/* Calculo de D�N */
		dn = Vector3D.dotProd(r.direction, N);

		/* Rayo paralelo al plano o no intersecta detr�s de la pantalla */
		if (dn == 0) {
			return false;
		}

		Vector3D p1 = vert[0].toVec();
		Vector3D p1a = Vector3D.sub(p1, new Vector3D(r.origin.x, r.origin.y,
				r.origin.z));

		d1 = Vector3D.dotProd(p1a, N);
		t = d1 / dn;
		if (t > r.t || t <= 0.0006)
			return false;

		Vector3D pointOfIntersection = new Vector3D(r.direction);
		pointOfIntersection.scale(t);
		pointOfIntersection.add(r.origin);

		// Determinar si el punto de intersecci�n pertenece al tri�ngulo
		if (pointBelongs(pointOfIntersection.toPoint())) {
			r.object = this;
			r.t = t;
			return true;
		}
		return false;
	}

	public boolean pointBelongs(Point3D point) {
		Vector3D p = new Vector3D(point);

		Vector3D aux = Vector3D.sub(p, vert[0].toVec());
		Vector3D aux2 = Vector3D.crossProd(p2p1, aux);
		double s1 = Vector3D.dotProd(aux2, N);

		aux = Vector3D.sub(p, vert[1].toVec());
		aux2 = Vector3D.crossProd(p3p2, aux);
		double s2 = Vector3D.dotProd(aux2, N);

		aux = Vector3D.sub(p, vert[2].toVec());
		Vector3D p1p3 = vert[0].to(vert[2]);
		aux2 = Vector3D.crossProd(p1p3, aux);
		double s3 = Vector3D.dotProd(aux2, N);

		/*
		 * Si s1,s2,s3 tienen el mismo signo---> pertecene al triangulo
		 */
		if (s1 >= 0 && s2 >= 0 && s3 >= 0) {
			return true;
		} else if (s1 < 0 && s2 < 0 && s3 < 0) {
			return true;
		}

		return false;
	}

	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos,
			double currentRefr) {
		Point3D p1 = null;
		// 0. (r) opuesto de L

		// 1. (p) Punto de intersecci�n rayo-objeto
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
