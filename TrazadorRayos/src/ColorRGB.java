public class ColorRGB {

   public static ColorRGB white = new ColorRGB(255, 255, 255);

   public static ColorRGB red = new ColorRGB(255, 0, 0);

   public static ColorRGB green = new ColorRGB(0, 255, 0);

   public static ColorRGB blue = new ColorRGB(0, 0, 255);

   public static ColorRGB black = new ColorRGB(0, 0, 0);

   public static ColorRGB yellow = new ColorRGB(255, 255, 0);

   public static ColorRGB gray = new ColorRGB(127, 127, 127);

   
   private float r;
   private float g;
   private float b;
 
   public ColorRGB() {
   }


   public ColorRGB(float x, float y, float z) {
      super();
   }

   public int toInt() {
      int ir = (int) r;
      int ig = (int) g;
      int ib = (int) b;
      return (ir << 16) + (ig << 8) + ib;
   }

   public ColorRGB mul(float m) {
      return new ColorRGB(r*m, g*m, b*m);
   }

   public ColorRGB add(ColorRGB color) {
      return new ColorRGB(r + color.r, g + color.g, b + color.b);
   }

   public static ColorRGB saturateRGB(ColorRGB color) {
      return new ColorRGB(Math.min(255, Math.max(color.r, 0)), 
    		  Math.min(255, Math.max(color.g, 0)), Math.min(255, Math.max(color.b, 0)));
   }

   public static ColorRGB getRandomColor() {

      return new ColorRGB((float) Math.random() * 255, (float) Math.random() * 255, 
    		  (float) Math.random() * 255);
   }


public float getR() {
	return r;
}


public void setR(float x) {
	this.r = x;
}


public float getG() {
	return g;
}


public void setG(float y) {
	this.g = y;
}


public float getB() {
	return b;
}


public void setB(float z) {
	this.b = z;
}
   
   
}