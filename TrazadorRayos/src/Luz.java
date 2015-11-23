import java.awt.Color;

public class Luz {
	public static final int AMBIENT = 0;
	public static final int DIRECTIONAL = 1;
	public static final int POINT = 2;

	private int lightType;
	private Vector3D lvec; // the position of a point light or
							// the direction to a directional light
	private Color colorRGB;	 // intensity of the light source

	public Luz(int type, Vector3D v, Color rgb) {
		lightType = type;
		colorRGB=rgb;
		if (type != AMBIENT) {
			lvec = v;
			if (type == DIRECTIONAL) {
				lvec.normalize();
			}
		}
	}
	
	public Luz(int type, Vector3D v, float r, float g, float b) {
		lightType = type;
		colorRGB=new Color(r,g,b);
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

	public float getIr() {
		return colorRGB.getRed();
	}

	public float getIg() {
		return colorRGB.getGreen();
	}


	public float getIb() {
		return colorRGB.getBlue();
	}


	public Color getColorRGB() {
		return colorRGB;
	}

	public void setColorRGB(Color colorRGB) {
		this.colorRGB = colorRGB;
	}
	
	public void setColorRGB(float r, float g, float b) {
		this.colorRGB = new Color(r,g,b);
	}
}
