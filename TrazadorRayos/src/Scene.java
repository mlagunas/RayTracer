import java.util.ArrayList;

public class Scene {
   private ArrayList<Objeto> objects;

   private Luz[] lights;

   private ColorRGB ambientLight;

   private ColorRGB backgroundColor;

  
   public Scene() {
      objects = new ArrayList<Objeto>();
      lights = new Luz[8];
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
         return lights[index];
      } else {
         return null;
      }
   }

   public void setLight(int index, Luz light) {
      lights[index] = light;
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

}