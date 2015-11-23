
import java.awt.Color;
import java.util.Vector;


// An object must implement a Renderable interface in order to
// be ray traced. Using this interface it is straight forward
// to add new objects
abstract interface Objeto {
    public abstract RayHit intersect(Rayo r);
    public abstract Color Shade(Rayo r, Vector lights, Vector objects, Color bgnd);
    public String toString();
}
