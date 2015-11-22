import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;


public class Rayo {
	 public static final double MAX_T = Double.MAX_VALUE;
	    Vector3D origin;
	    Vector3D direction;
	    double t;
	    Objeto object;

	    public Rayo(Vector3D eye, Vector3D dir) {
	        origin = new Vector3D(eye);
	        dir.normalize();
	        direction = dir;
	    }

	    public boolean trace(ArrayList<Objeto> objects) {
	        t = MAX_T;
	        object = null;
	        for(Objeto object: objects) {
	            object.intersect(this);
	        }
	        return (object != null);
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