import java.awt.Color;
import java.util.ArrayList;

public class Scene {
   private ArrayList<Objeto> objects;

   private ArrayList<Luz> lights;

   private Color backgroundColor;
   
   public Scene() {
      objects = new ArrayList<Objeto>();
      lights = new ArrayList<Luz>();
      backgroundColor = Color.black;
   }

   public ArrayList<Objeto> getObjects() {
      return objects;
   }

   public void setObjects(ArrayList<Objeto> objects) {
      this.objects = objects;
   }

   public Luz getLight(int index) {
      if (index >= 0 && index < 8) {
         return lights.get(index);
      } else {
         return null;
      }
   }

   public void setLight(Luz light) {
      lights.add(light);
   }

   public void addObject(Objeto object) {
      objects.add(object);
   }

   public Color getBackgroundColor() {
      return backgroundColor;
   }

   public void setBackgroundColor(Color backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

	public ArrayList<Luz> getLights() {
		return lights;
	}
	
	public void setLights(ArrayList<Luz> lights) {
		this.lights = lights;
	}

}