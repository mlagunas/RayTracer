import java.awt.Color;

public class Vector3D {

	/**
	 * Vector data. Allowed to be accessed publicly for performance reasons
	 */
	public double x, y, z;

	/**
	 * Initialize vector to (0,0,0)
	 */
	public Vector3D() {
		x = 0;
        y = 0;
        z = 0;
	}

	/**
	 * Initialize vector to given coordinates
	 * 
	 * @param x
	 *            Scalar
	 * @param y
	 *            Scalar
	 * @param z
	 *            Scalar
	 */
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Initialize vector values to given vector (copy by value)
	 * 
	 * @param v
	 *            Vector
	 */
	public Vector3D(Vector3D v) {
		x = v.x;
        y = v.y;
        z = v.z;
	}

    public Vector3D(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }
	
	public Vector3D(Color color) {
		// we can either divide getRed/getGreen/getBlue by 255 or do this.
		float[] colors = new float[3];
		color.getRGBColorComponents(colors);
		x = colors[0];
		y = colors[1];
		z = colors[2];
	}
    
    public Vector3D(Point3D dest, Point3D origin) {
    	x = dest.x - origin.x;
    	y = dest.y - origin.y;
    	z = dest.z - origin.z;
    	
    }

    /**
	 * Calculates the reflection of the vector in relation to a given surface
	 * normal. The vector points at the surface and the result points away.
	 * 
	 * @return The reflected vector
	 */
	public Vector3D reflect(Vector3D normal) {
        double dotProduct = dotProd(normal);

        Vector3D reflectionNormal = new Vector3D(normal);
        Vector3D reflection = new Vector3D(this);

        reflectionNormal.scale(dotProduct);

        reflectionNormal.negate();

        reflection.add(reflectionNormal);
        reflectionNormal.add(reflection);
        return reflectionNormal;
	}
    
    public Vector3D(String values) {
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

    private float limitColor(double color) {
        if (color > 1) {
            return 1;
        }
        if (color < 0) {
            return 0;
        }
        return (float)color;
    }
    
    public Color toColor() {
        return new Color(limitColor(x), limitColor(y), limitColor(z));
    }
    
	/**
	 * Adds a to vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void add(Vector3D a) {
		x += a.x;
        y += a.y;
        z += a.z;
	}

	/**
	 * Subtracts from vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void sub(Vector3D a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;
	}
	

	/**
	 * Multiplies vector with scalar. v := s*v
	 * 
	 * @param s
	 *            Scalar
	 */
	public void scale(double s) {
		x *= s;
        y *= s;
        z *= s;
	}

	/**
	 * Pairwise multiplies with another vector
	 * 
	 * @param a
	 *            Vector
	 */
	public void scale(Vector3D a) {
		x *= a.x;
        y *= a.y;
        z *= a.z;
	}

	/**
	 * Inverses vector
	 * 
	 * @return Vector
	 */
	public void negate() {
		this.x = -1 * this.x;
        this.y = -1 * this.y;
        this.z = -1 * this.z;
	}

	/**
	 * Computes the vector's magnitude
	 * 
	 * @return Scalar
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Computes the vector's magnitude squared. Used for performance gain.
	 * 
	 * @return Scalar
	 */
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Computes the dot product between two vectors
	 * 
	 * @param a
	 *            Vector
	 * @return Scalar
	 */
	public double dotProd(Vector3D a) {
		return x * a.x + y * a.y + z * a.z;
	}

	/**
	 * Normalizes the vector to have length 1. Throws exception if magnitude is zero.
	 * 
	 * @throws ArithmeticException
	 */
	public void normalize() throws ArithmeticException {
		double len = length();

        if (len == 0) {
            throw new ArithmeticException();
        }

        this.x = this.x / len;
        this.y = this.y / len;
        this.z = this.z / len;
	}

	/**
	 * Compares to a given vector
	 * 
	 * @param a
	 *            Vector
	 * @return True if have same values, false otherwise
	 */
	public boolean equals(Vector3D a) {
		return ((a.x == x) && (a.y == y) && (a.z == z));
	}

	/**
	 * Returns the angle in radians between this vector and the vector
	 * parameter; the return value is constrained to the range [0,PI].
	 * 
	 * @param v1
	 *            the other vector
	 * @return the angle in radians in the range [0,PI]
	 */
	public final double angle(Vector3D v1) {
		return Math.acos(dotProd(v1)/(v1.length()*length()));
	}

	/**
	 * Computes the cross product between two vectors using the right hand rule
	 * 
	 * @param a
	 *            Vector1
	 * @param b
	 *            Vector2
	 * @return Vector1 x Vector2
	 */
	public static Vector3D crossProd(Vector3D a, Vector3D b) {	
		return new Vector3D(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x);
	}

	/**
	 * Adds vectors a and b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a+b
	 */
	public static Vector3D add(Vector3D a, Vector3D b) {
		Vector3D v = new Vector3D(a);
        v.add(b);
        return v;
	}

	/**
	 * Subtracts vector b from a
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a-b
	 */
	public static Vector3D sub(Vector3D a, Vector3D b) {
        Vector3D v = new Vector3D(a);
        v.sub(b);
        return v;
	}

	/**
	 * Inverses vector's direction
	 * 
	 * @param a
	 *            Vector
	 * @return -1*a
	 */
	public static Vector3D negate(Vector3D a) {
        Vector3D v = new Vector3D(a);
        v.negate();
        return v;
	}

	/**
	 * Scales vector a by scalar s
	 * 
	 * @param s
	 *            Scalar
	 * @param a
	 *            Vector
	 * @return s*a
	 */
	public static Vector3D scale(double s, Vector3D a) {
        Vector3D v = new Vector3D(a);
        v.scale(s);
        return v;
	}

	/**
	 * Pair-wise scales vector a by vector b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a.*b
	 */
	public static Vector3D scale(Vector3D a, Vector3D b) {
        Vector3D v = new Vector3D(a);
        v.scale(b);
        return v;
	}

	/**
	 * Compares vector a to vector b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a==b
	 */
	public static boolean equals(Vector3D a, Vector3D b) {
        return a.equals(b);
	}

	/**
	 * Dot product of a and b
	 * 
	 * @param a
	 *            Vector
	 * @param b
	 *            Vector
	 * @return a.b
	 */
	public static double dotProd(Vector3D a, Vector3D b) {
        Vector3D v = new Vector3D(a);
        return v.dotProd(b);
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

    public Point3D toPoint() {
        return new Point3D(x, y, z);
    }

	public void limit() {
		x = limitColor(x);
		y = limitColor(y);
		z = limitColor(z);
	}
}