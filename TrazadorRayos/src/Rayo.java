import java.awt.Color;
import java.util.ArrayList;


public class Rayo {
	 public static final double MAX_T = Double.MAX_VALUE;
	    Vector3D origin;
	    Vector3D direction;
	    double t;
	    Objeto object;

	    public Rayo(Point3D point3d, Vector3D dir) {
	        origin = new Vector3D(point3d);
	        dir.normalize();
	        direction = dir;
	    }

	    public boolean trace(ArrayList<Objeto> objects) {
	        t = MAX_T;
	        object = null;
	        for(Objeto object:objects) {
	            object.intersect(this);
	        }
	        return (object != null);
	    }

	    // The following method is not strictly needed, and most likely
	    // adds unnecessary overhead, but I prefered the syntax
	    //
	    //            ray.Shade(...)
	    // to
	    //            ray.object.Shade(ray, ...)
	    //
	    public final Color Shade(ArrayList<Luz> lights, ArrayList<Objeto> objects, Color bgnd,double kref) {
	        return object.Shade(this, lights, objects, bgnd, 0,kref);
	    }
	    
	    public final Color Shade(ArrayList<Luz> lights, ArrayList<Objeto> objects, Color bgnd, int nRayos,double kref) {
	        return object.Shade(this, lights, objects, bgnd, nRayos,kref);
	    }
	    
	    public boolean trace(Objeto object) {
	        t = MAX_T;
	        object.intersect(this);
	        return (object != null);
	    }

	    // The following method is not strictly needed, and most likely
	    // adds unnecessary overhead, but I prefered the syntax
	    //
	    //            ray.Shade(...)
	    // to
	    //            ray.object.Shade(ray, ...)
	    //

	    public String toString() {
	        return ("ray origin = "+origin+"  direction = "+direction+"  t = "+t);
	    }
}