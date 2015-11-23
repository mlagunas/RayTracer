import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;


public class Rayo {
	 public static final float MAX_T = Float.MAX_VALUE;
	    Vector3D origin;
	    Vector3D direction;
	    float t;
	    Objeto object;

	    public Rayo(Point3D eye, Vector3D dir) {
	        origin = new Vector3D(eye);
	        dir.normalize();
	        direction = dir;
	    }

	    public boolean trace(ArrayList<Objeto> objects) {
	    			 
	    	// shoot prim ray in the scene and search for intersection
			RayHit rayHit;
			float minDistance = Float.MAX_VALUE;
			Object object = null;
			for (int k = 0; k < objects.size(); ++k) {
				 if ((rayHit=(objects.get(k)).intersect(this))!= null) {
					float distance = Distance(origin, rayHit.getPunto());
					if (distance < minDistance) {
						object = objects.get(k);
						minDistance = distance; // update min distance
					}
					break;
				 }
			}
					 
			if (object == null)
				return 0;
			// if the object material is glass, split the ray into a reflection
			// and a refraction ray.
			if (object->isGlass && depth < MAX_RAY_DEPTH) {
				// compute reflection
				Ray reflectionRay;
				reflectionRay = computeReflectionRay(ray.direction, nHit);
				// recurse
				color reflectionColor = Trace(reflectionRay, depth + 1);
				Ray refractioRay;
				refractionRay = computeRefractionRay(object->indexOfRefraction,ray.direction, nHit);
				// recurse
				color refractionColor = Trace(refractionRay, depth + 1);
				float Kr, Kt;
				fresnel(object->indexOfRefraction,nHit,ray.direction, &Kr, &Kt);
				return reflectionColor * Kr + refractionColor * (1-Kr);
			}
					 
			// object is a diffuse opaque object
			// compute illumination
			Ray shadowRay;
			shadowRay.direction = lightPosition - pHit;
			bool isShadow = false;	
			for (int k = 0; k < objects.size(); ++k) {
				if (Intersect(objects[k], shadowRay)) {
					// hit point is in shadow so just return
					return 0;
				}
			}
			// point is illuminated
			return object->color * light.brightness;
	    }

	    // The following method is not strictly needed, and most likely
	    // adds unnecessary overhead, but I prefered the syntax
	    //
	    //            ray.Shade(...)
	    // to
	    //            ray.object.Shade(ray, ...)
	    //
	    public final Color Shade(Vector lights, Vector objects, Color bgnd) {
	        return object.Shade(this, lights, objects, bgnd);
	    }

	    public String toString() {
	        return ("ray origin = "+origin+"  direction = "+direction+"  t = "+t);
	    }
}