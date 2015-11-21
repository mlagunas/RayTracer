public class Vector3D {

	public float x, y, z;

	// Constructores
	public Vector3D() {
	}

	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Vector3D v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	// Metodos

	/**
	 * Producto escalar entre el Vector de la clase y uno dado
	 * 
	 * @return
	 */
	public final float escalar(Vector3D B) {
		return (x * B.x + y * B.y + z * B.z);
	}

	/**
	 * Producto escalar entre el Vector de la clase y las coordenadas de un
	 * Vector
	 * 
	 * @return
	 */
	public final float escalar(float Bx, float By, float Bz) {
		return (x * Bx + y * By + z * Bz);
	}

	/**
	 * Producto escalar entre dos vectores dados
	 * 
	 * @return
	 */
	public static final float escalar(Vector3D A, Vector3D B) {
		return (A.x * B.x + A.y * B.y + A.z * B.z);
	}

	/**
	 * Producto vectorial entre el Vector B y el Vector creado en la propia
	 * clase
	 * 
	 * @param B
	 * @return
	 */
	public final Vector3D productoVectorial(Vector3D B) {
		return new Vector3D(y * B.z - z * B.y, z * B.x - x * B.z, x * B.y - y
				* B.x);
	}

	/**
	 * Producto vectorial dadas las coordenadas
	 * 
	 * @param B
	 * @return
	 */
	public final Vector3D productoVectorial(float Bx, float By, float Bz) {
		return new Vector3D(y * Bz - z * By, z * Bx - x * Bz, x * By - y * Bx);
	}

	/**
	 * Producto vectorial entre dos vectores dados
	 * 
	 * @param B
	 * @return
	 */
	public final static Vector3D productoVectorial(Vector3D A, Vector3D B) {
		return new Vector3D(A.y * B.z - A.z * B.y, A.z * B.x - A.x * B.z, A.x
				* B.y - A.y * B.x);
	}

	/**
	 * Modulo del vector de la clase propia
	 * 
	 * @return
	 */
	public final float modulo() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Modulo de un Vector dado
	 * 
	 * @return
	 */
	public final static float modulo(Vector3D A) {
		return (float) Math.sqrt(A.x * A.x + A.y * A.y + A.z * A.z);
	}

	/**
	 * Normalizar el Vector de la clase
	 * 
	 * @return
	 */
	public final void normalize() {
		float t = x * x + y * y + z * z;
		if (t != 0 && t != 1)
			t = (float) (1 / Math.sqrt(t));
		x *= t;
		y *= t;
		z *= t;
	}

	/**
	 * Normalizar un Vector dado
	 * 
	 * @return
	 */
	public final static Vector3D normalize(Vector3D A) {
		float t = A.x * A.x + A.y * A.y + A.z * A.z;
		if (t != 0 && t != 1)
			t = (float) (1 / Math.sqrt(t));
		return new Vector3D(A.x * t, A.y * t, A.z * t);
	}

	public String toString() {
		return new String("[" + x + ", " + y + ", " + z + "]");
	}
}
