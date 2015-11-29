import java.awt.Color;
import java.awt.Point;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;

public class Triangulo implements Objeto {

	protected Point3D vert[];
	protected Vector3D normal;
	protected ModeloLuz model;
	private Vector3D p1p0, p2p1, p0p2; // vectores de los lados
	private Color color;
	boolean isMirror;
	boolean isTransparent;
	
	
	public Triangulo(Point3D vert1, Point3D vert2, Point3D vert3, ModeloLuz modeloLuz, Color color,boolean m,boolean t) {
		super();
		vert = new Point3D[3];
	    vert[0] = vert1;
	    vert[1] = vert2;
	    vert[2] = vert3;
	    p1p0 = vert[0].to(vert[1]);
	    p2p1 = vert[1].to(vert[2]);
	    p0p2  = vert[2].to(vert[0]);
	    // Plane equation: normal . x = offset
	    normal = Vector3D.crossProd(p1p0, p2p1);
	    normal.normalize();
		this.model = modeloLuz;
		this.color=color;
		this.isMirror=m;
		this.isTransparent=t;
	}

	@Override
	public boolean intersect(Rayo r) {
		Vector3D normal = new Vector3D(this.normal);
		
		// Si no pertenece al plano del triángulo, no hay intersección
		double auxDot = Vector3D.dotProd(normal,r.direction);
		if (auxDot < 0) {
			normal.scale(-1);
		}
		
		if (Vector3D.dotProd(normal,r.direction) < 0.000000001) {
			return false;
		}
		Vector3D a = new Vector3D(vert[0]);
		a.sub(r.origin);
		double t = Vector3D.dotProd(a,normal) / Vector3D.dotProd(r.direction,normal);
		if (!(t >= 0)) {
//			&& t < withinDistance)
			return false;
		}
		Vector3D pointOfIntersection = new Vector3D(r.direction);
		pointOfIntersection.scale(t);
		pointOfIntersection.add(r.origin);
		
		// Determinar si el punto de intersección pertenece al triángulo
		if (pointBelongs(pointOfIntersection.toPoint())) {
			r.object=this;
			
			return true;
		}		
		return false;
	}
	
	public boolean pointBelongs(Point3D point) {
		Vector3D aux = new Vector3D(point);
		aux.sub(vert[0].toVec());
		
		// Si no pertenece al plano, return false
		if (Math.abs(Vector3D.dotProd(aux,normal)) > 0.00000001) {
			return false;
		}
		
		Vector3D aux2 = new Vector3D(point);
		aux2.sub(vert[0].toVec());
		aux2=Vector3D.crossProd(p1p0, aux2);
		if (Vector3D.dotProd(aux2,normal) < 0) {
			return false;
		}
		
		aux2 = new Vector3D(point);
		aux2.sub(vert[1].toVec());
		aux2= Vector3D.crossProd(p2p1, aux2);
		if (Vector3D.dotProd(aux2,normal) < 0) {
			return false;
		}
		
		aux2 = new Vector3D(point);
		aux2.sub(vert[2].toVec());
		aux=Vector3D.crossProd(p0p2, aux2);
		if (Vector3D.dotProd(aux2,normal) < 0) {
			return false;
		}
		return true;
	}
		
	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd, int nRayos, double kref) {
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
				Vector3D v = new Vector3D(px - r.origin.x, py - r.origin.y, pz - r.origin.z);

				// 5. (ref) Rayo reflejado
				Vector3D ref = r.origin.reflect(normal);
				
				// 6. (frac) Rayo refractado
				Vector3D frac=null;
				if(isTransparent){ //Snell: sin(i)/sin(r) = nr/ni
					
					double NiNr=kref/model.index; 
					double cosI=-Vector3D.dotProd(normal, r.direction);
					double cosR=Math.sqrt(1.0-((1.0-(cosI*cosI))*(NiNr*NiNr)));
					
					if (cosR>0.0){
//						 frac = Vector3D.add(Vector3D.scale(NiNr,r.direction),Vector3D.scale((NiNr*cosI)-cosR, n));
						frac=Vector3D.sub(Vector3D.scale((NiNr*cosI-Math.sqrt(1-NiNr*NiNr*(1-(cosI*cosI)))),normal),Vector3D.scale(NiNr,r.direction)); 
						frac.normalize();
					}
					else{
						frac=null;
					}
				}
				
				// The illumination model is applied
				// by the surface's Shade() method
				return model.calculo(color,bgnd, lights, objects, l, p, normal, v, r.origin, isMirror,ref, isTransparent,frac, nRayos);
	}

}
