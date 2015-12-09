package Objects;
import java.awt.Color;
import java.util.ArrayList;

import Model.Luz;
import Model.Rayo;

// An object must implement a Renderable interface in order to
// be ray traced. Using this interface it is straight forward
// to add new objects
public abstract interface Objeto {
    public abstract boolean intersect(Rayo r);
    public String toString();
	public Color Shade(Rayo r, ArrayList<Luz> lights, ArrayList<Objeto> objects, Color bgnd, int nRayos,double currentRefr);
	
}
