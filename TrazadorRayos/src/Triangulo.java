import java.awt.Color;
import java.util.ArrayList;

public class Triangulo implements Objeto {

	protected Point3D vert[];
	protected Vector3D normal;
	protected ModeloLuz model;
	private Vector3D p3p1, p2p1, p3p2; // vectores de los lados
	private Color color;
	boolean isMirror;
	boolean isTransparent;
	
	
	public Triangulo(Point3D vert1, Point3D vert2, Point3D vert3, ModeloLuz modeloLuz, Color color) {
		super();
		vert = new Point3D[3];
	    vert[0] = vert1;
	    vert[1] = vert2;
	    vert[2] = vert3;
	    p2p1 = vert[1].to(vert[0]);
	    p3p2 = vert[2].to(vert[1]);
	    p3p1  = vert[2].to(vert[0]);
	    // Plane equation: normal . x = offset
	    normal = Vector3D.crossProd(p2p1, p3p1);
	    normal.normalize();
		this.model = modeloLuz;
		this.color=color;
	}

	@Override
	public boolean intersect(Rayo r) {
		double d1, dn, t;

		/* Calculo de D·N */
		dn = Vector3D.dotProd(r.direction, normal);

		/* Rayo paralelo al plano o no intersecta detrás de la pantalla */
		if (dn == 0) {
			return false;
		}

		Vector3D p1=vert[0].toVec();
		Vector3D p1a = Vector3D.sub(p1,new Vector3D(r.origin.x, r.origin.y,r.origin.z));
		
		d1 = Vector3D.dotProd(p1a, normal);
		t = d1 / dn;
		if (t > r.t || t <= 0.0006)
			return false;
		
		Vector3D pointOfIntersection = new Vector3D(r.direction);
		pointOfIntersection.scale(t);
		pointOfIntersection.add(r.origin);
		
		// Determinar si el punto de intersección pertenece al triángulo
		if (pointBelongs(pointOfIntersection.toPoint())) {
			r.object=this;
			r.t = t;			
			return true;
		}		
		return false;
	}
	
	public boolean pointBelongs(Point3D point) {
		Vector3D p = new Vector3D(point);
		
		Vector3D aux= Vector3D.sub(p,vert[0].toVec());
		Vector3D aux2=Vector3D.crossProd(p2p1, aux);
		double s1=Vector3D.dotProd(aux2,normal);
		
		
		aux= Vector3D.sub(p,vert[1].toVec());
		aux2=Vector3D.crossProd(p3p2, aux);
		double s2=Vector3D.dotProd(aux2,normal);
		
		aux= Vector3D.sub(p,vert[2].toVec());
		Vector3D p1p3=vert[0].to(vert[2]);
		aux2=Vector3D.crossProd(p1p3, aux);
		double s3=Vector3D.dotProd(aux2,normal);
		
		/*
		 * Si s1,s2,s3 tienen el mismo signo---> pertecene al triangulo
		 */
		if(s1>=0 && s2 >=0 && s3>=0){
			return true;
		}else if (s1<0 && s2<0 && s3<0){
			return true;
		}
		
		return false;
	}
		
	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos, double kref) {
				Point3D p1=null;
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
				Vector3D v = new Vector3D(r.origin.x-px, r.origin.y -py, r.origin.z-pz);
				v.normalize();
				Vector3D ref =null;
//				if (isMirror){
//					// 5. (ref) Rayo reflejado
//					ref = r.origin.reflect(normal);
//				}
				
				Vector3D frac = null;
//				if (isTransparent) {
//					// Snell: sin(i)/sin(r) = nr/ni
//					double NiNr = kref / model.index;
//					double cosI = Vector3D.dotProd(normal, r.direction);
//					double cosR = Math
//							.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
//
//					if (cosR > 0.0) {
//						// frac =
//						// Vector3D.add(Vector3D.scale(NiNr,r.direction),Vector3D.scale((NiNr*cosI)-cosR,
//						// n));
//						// frac=Vector3D.sub(Vector3D.scale((NiNr*cosI-Math.sqrt(1-NiNr*NiNr*(1-(cosI*cosI)))),n),Vector3D.scale(NiNr,r.direction));
//						Vector3D frac1 = Vector3D.sub(
//								Vector3D.scale((NiNr * cosI) - cosR, normal),
//								Vector3D.scale(NiNr, r.direction));
//						frac1.normalize();
//
//						Rayo rayo = new Rayo(p, frac1);
//						if (this.intersect(rayo)) {
//							px = (rayo.origin.x + rayo.t * rayo.direction.x);
//							py = (rayo.origin.y + rayo.t * rayo.direction.y);
//							pz = (rayo.origin.z + rayo.t * rayo.direction.z);
//
//							p1 = new Point3D(px, py, pz);
//
//							NiNr = model.index / kref;
//							cosI = Vector3D.dotProd(normal, rayo.direction);
//							cosR = Math
//									.sqrt(1.0 - ((1.0 - (cosI * cosI)) * (NiNr * NiNr)));
//							if (cosR > 0.0) {
//								frac = Vector3D.sub(
//										Vector3D.scale((NiNr * cosI) - cosR, normal),
//										Vector3D.scale(NiNr, rayo.direction));
//								frac.normalize();
//							}
//						}
//					}
//				}
				
				// The illumination model is applied
				// by the surface's Shade() method
				return model.calculo(color,bgnd, lights, objects, p,p1, normal, v, 
						r.origin,null,nRayos,kref);
	}

}
