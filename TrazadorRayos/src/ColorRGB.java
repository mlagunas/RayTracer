public class ColorRGB {

   public static ColorRGB white = new ColorRGB(255, 255, 255);

   public static ColorRGB red = new ColorRGB(255, 0, 0);

   public static ColorRGB green = new ColorRGB(0, 255, 0);

   public static ColorRGB blue = new ColorRGB(0, 0, 255);

   public static ColorRGB black = new ColorRGB(0, 0, 0);

   public static ColorRGB yellow = new ColorRGB(255, 255, 0);

   public static ColorRGB gray = new ColorRGB(127, 127, 127);

   
   private float x;
   private float y;
   private float z;
 
   public ColorRGB() {
   }


   public ColorRGB(float x, float y, float z) {
      super();
   }

   public int toInt() {
      int r = (int) x;
      int g = (int) y;
      int b = (int) z;
      return (r << 16) + (g << 8) + b;
   }

   public ColorRGB mul(float m) {
      return new ColorRGB(x*m, y*m, z*m);
   }

   public ColorRGB add(ColorRGB color) {
      return new ColorRGB(x + color.x, y + color.y, z + color.z);
   }

   public static ColorRGB saturateRGB(ColorRGB color) {
      return new ColorRGB(Math.min(255, Math.max(color.x, 0)), 
    		  Math.min(255, Math.max(color.y, 0)), Math.min(255, Math.max(color.z, 0)));
   }

   public static ColorRGB getRandomColor() {

      return new ColorRGB((float) Math.random() * 255, (float) Math.random() * 255, 
    		  (float) Math.random() * 255);
   }


public float getX() {
	return x;
}


public void setX(float x) {
	this.x = x;
}


public float getY() {
	return y;
}


public void setY(float y) {
	this.y = y;
}


public float getZ() {
	return z;
}


public void setZ(float z) {
	this.z = z;
}
   
   
}