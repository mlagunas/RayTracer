
public class Point3D {
	 public double x, y, z;
	    
	    public Point3D() {
	        x = 0;
	        y = 0;
	        z = 0;
	    }
	    
	    public Point3D(double x, double y, double z) {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	    }
	    
	    public Point3D(Point3D point) {
	        this.x = point.x;
	        this.y = point.y;
	        this.z = point.z;
	    }
	    
	    public Point3D(Vector3D vector) {
	        this.x = vector.x;
	        this.y = vector.y;
	        this.z = vector.z;
	    }

	    public Point3D(String values) {
			if (values == null) {
				x = 0;
				y = 0;
				z = 0;
				return;
			}

	        String[] valueArray = values.split("\\s+");

	        if (valueArray.length != 3) {
	            throw new IllegalArgumentException("Wrong number of parameters, got " + valueArray.length + " parameters.");
	        }

	        x = Double.parseDouble(valueArray[0]);
	        y = Double.parseDouble(valueArray[1]);
	        z = Double.parseDouble(valueArray[2]);
	    }

	    public void add(Point3D point) {
	        this.x += point.x;
	        this.y += point.y;
	        this.z += point.z;
	    }

	    public void add(Vector3D v) {
	        this.x += v.x;
	        this.y += v.y;
	        this.z += v.z;
	    }

	    
	    public void sub(Point3D point) {
	        this.x -= point.x;
	        this.y -= point.y;
	        this.z -= point.z;        
	    }
	    
	    public void scale(double s) {
	        this.x *= s;
	        this.y *= s;
	        this.z *= s;
	    }
	    
	    public void scale(Vector3D v) {
	        this.x *= v.x;
	        this.y *= v.y;
	        this.z *= v.z;
	    }
	    

	    /**
	     * Returns a string that contains the values of this vector. The form is
	     * (x,y,z).
	     *
	     * @return the String representation
	     */
	    public String toString() {
	        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	    }

	    public Vector3D toVec() {
	        return new Vector3D(x, y, z);
	    }
	    
	    public Point3D clone() {
	        return new Point3D(x, y, z);
	    }

}
