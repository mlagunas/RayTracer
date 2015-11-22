import java.awt.Color;
import java.util.ArrayList;

// An object must implement a Renderable interface in order to
// be ray traced. Using this interface it is straight forward
// to add new objects
abstract interface Objeto {
	public abstract boolean intersect(Rayo r);

	public abstract Color Shade(Rayo r, Point3D eye, ArrayList<Luz> lights,
			ArrayList<Objeto> objects, Color bgnd);

	public String toString();

}
