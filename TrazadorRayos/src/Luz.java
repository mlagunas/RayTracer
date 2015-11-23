public class Luz {
	public static final int AMBIENT = 0;
	public static final int DIRECTIONAL = 1;
	public static final int POINT = 2;

	public int lightType;
	public Vector3D lvec; // the position of a point light or
							// the direction to a directional light
	public float ir, ig, ib; // intensity of the light source
	ColorRGB colorRGB;

	public Luz(int type, Vector3D v, ColorRGB rgb) {
		lightType = type;
		colorRGB=rgb;
		if (type != AMBIENT) {
			lvec = v;
			if (type == DIRECTIONAL) {
				lvec.normalize();
			}
		}
	}
}
