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

	public Plano(ModeloLuz m, double d, float x, float y, float z, Color color) {
		this.color= color;
		this.m = m;
		this.N = new Vector3D(x, y, z);
		this.d = d;
	}

	public Plano(ModeloLuz m, Vector3D v, double d) {
		this.m = m;
		this.N = v;
		this.d = d;
	}

	// Rayo = R(t) = O + D * t, con su origen O = (ox, oy, oz) y vector
	// dirección normalizado D = (dx, dy, dz)
	// Verificará nx * (ox + dx * t) + ny * (oy + dy * t) + nz * (oz + dz * t) +
	// d = 0
	// nx * dx + ny * dy + nz * dz = N · D
	// t = - (nx * ox + ny * oy + nz * oz + d) / (nx * dx + ny * dy + nz * dz)
	/*@Override
	public boolean intersect(Rayo r) {
		Vector3D o = r.origin;
		Vector3D d = r.direction;
		double a = (n.x * o.x + n.y * o.y + n.z * o.z) + this.d;
		double b = (n.x * d.x + n.y * d.y + n.z * d.z);

		if (b == 0)
			return false;
		double t = -a / b;
		// if(t<0)
		// return false;
		return true;

	}*/
	
	  /**
	    * Find the intersection of the plane and a given ray.
	    *
	    * The return value is positive is the intersection is found and
	    * this value gives the distance along the ray. Negative values
	    * imply that the intersection was either not successful or the
	    * intersection point was before the origin. This value can be used
	    * with the pointAt method of the Ray class (@see Ray#pointAt)
	    *
	    * @param ray the ray to intersect with 
	    * @return a <code>double</code> value that gives the distance
	    * along the ray. 
	    */
	  public boolean intersect(Rayo ray) {
	    Vector3D tmp; 
	    double d1,d2,t;

	    tmp = new Vector3D (ray.origin.x, ray.origin.y, ray.origin.z);

	    /* Find angle between plane and direction of ray*/
	    d2 = Vector3D.dotProd(ray.direction, N);
		
	    /* Is ray parallel to plane */
	    if (Math.abs(d2) < 0.00000001) {
	      return false;
	    }
		
	    d1 = Vector3D.dotProd(tmp,N);
	    t = (d - d1)/d2;
		ray.t = t;
		ray.object = this;
	    return true;
	  }

	@Override
	public Color Shade(Rayo r, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd) {
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
		Vector3D ref = r.origin.reflect(N);
		
		// 6. (frac) Rayo refractado
		Vector3D frac = null;
		
		// The illumination model is applied
		// by the surface's Shade() method
		return m.calculo(color,bgnd, lights, objects, l, p, N, v, r.origin, ref, frac);
	}

}
