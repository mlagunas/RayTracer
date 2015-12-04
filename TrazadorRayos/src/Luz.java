public class Luz {
	public static final int AMBIENT = 0;
	public static final int DIRECTIONAL = 1;
	public static final int POINT = 2;

	public int lightType;
	public Vector3D lvec; // the position of a point light or
							// the direction to a directional light
	public float r, g, b; // intensity of the light source

	public Luz(int type, Vector3D v, float r, float g, float b) {
		lightType = type;
		this.r = r;
		this.g = g;
		this.b = b;
		if (type != AMBIENT) {
			lvec = v;
			if (type == DIRECTIONAL) {
				lvec.normalize();
			}
		}
	}

	public int getLightType() {
		return lightType;
	}

	public void setLightType(int lightType) {
		this.lightType = lightType;
	}

	public Vector3D getLvec() {
		return lvec;
	}

	public void setLvec(Vector3D lvec) {
		this.lvec = lvec;
	}

}
