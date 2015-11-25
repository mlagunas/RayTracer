import java.util.ArrayList;

public class Scene {
   private ArrayList<Objeto> objects;

   private ArrayList<Luz> lights;

   private ColorRGB ambientLight;

   private ColorRGB backgroundColor;
   
   private ArrayList<Luz> allLights;

  
   public Scene() {
      objects = new ArrayList<Objeto>();
      lights = new ArrayList<Luz>();
      ambientLight = ColorRGB.black;
      backgroundColor = ColorRGB.black;
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

   public void setLight(int index, Luz light) {
      lights.add(light);
   }

   public void addObject(Objeto object) {
      objects.add(object);
   }

   public ColorRGB getAmbientLight() {
      return ambientLight;
   }

   public void setAmbientLight(ColorRGB ambientLight) {
      this.ambientLight = ambientLight;
   }

   public ColorRGB getBackgroundColor() {
      return backgroundColor;
   }

   public void setBackgroundColor(ColorRGB backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

public ArrayList<Luz> getLights() {
	return lights;
}

public void setLights(ArrayList<Luz> lights) {
	this.lights = lights;
}

   

}