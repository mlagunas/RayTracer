import java.awt.Color;
import java.util.ArrayList;

public class ModeloLuz {
	//Maximo de rayos reflejados lanzados
	private final int MAX_RAYOS = 10;
	private final float MAX_COLOR = 255;
	
	double ka;
	double kd;
	double ks;
	double kr;
	double kt;
	double n;

	public ModeloLuz(double ka, double kd, double ks, double kr, double n, double kt, double index) {
		this.ka = ka; // Coeficiente ambiental
		this.kd = kd; // Coeficiente difusa
		this.ks = ks; // Coeficiente especular
		this.kr = kr; // Coeficiente reflexion
		this.kt = kt; // Coeficiente refraccion
		this.n = n;
	}

	public Color calculo(Color color, Color bgnd, ArrayList<Luz> lightSources,
			ArrayList<Objeto> objects, Vector3D L, Point3D p, Vector3D N,
			Vector3D V, Vector3D R, Vector3D Ref, Vector3D frac, int nRayos) {
		float r = 0;
		float g = 0;
		float b = 0;
		float sr = color.getRed() / (float)MAX_COLOR;
		float sg = color.getGreen() / (float)MAX_COLOR;
		float sb = color.getBlue() / (float)MAX_COLOR;
		for (Luz light : lightSources) {
			/*
			 * CALCULO DE LA LUZ AMBIENTAL
			 */
			if (light.getLightType() == Luz.AMBIENT) {
				r += ka * light.r * sr;
				g += ka * light.g * sg;
				b += ka * light.b * sb;

			} else {

				if (light.lightType == Luz.POINT) {
					L = new Vector3D(light.lvec.x - p.x, light.lvec.y - p.y,
							light.lvec.z - p.z);
					L.normalize();
				} else {
					L = new Vector3D(-light.lvec.x, -light.lvec.y,
							-light.lvec.z);
				}

				/*
				 * CALCULO SI ES UN PUNTO EN SOMBRA
				 */
				Point3D poffset = new Point3D(p.x * L.x, p.y * L.y, p.z * L.z);
				Rayo shadowRay = new Rayo(poffset, L);
				if (shadowRay.trace(objects))
					break;

				/*
				 * CALCULO LUZ DIFUSA Kd*Id Id = I*cos(N·L)
				 */
				float lambert = 0;
				if (kd > 0) {
					// cos(N·L)

					lambert = (float) Vector3D.dotProd(N, L);
					// Al aplicar el coseno los resultados que vemos son peores
					// y no se nota apenas ningun gradiente

					// lambert = (float) Math.cos(lambert);
					if (lambert > 0) {
						// Kd*cos(N·L)*I
						r += kd * lambert * light.r * sr;
						g += kd * lambert * light.g * sg;
						b += kd * lambert * light.b * sb;
					}
				}

				/*
				 * CALCULO LUZ ESPECULAR Ks*Is Is = I*cos(R·V)^n
				 */
				if (ks > 0) {
					// cos(R·V)^n
					lambert *= 2;
					float spec = (float) V.dotProd(new Vector3D(lambert * N.x
							- L.x, lambert * N.y - L.y, lambert * N.z - L.z));
					// double spec = Math.pow(n, Math.cos(Vector3D.dotProd(V,
					// R)));
					if (spec > 0) {
						//
						r += ks * spec * light.r * sr;
						g += ks * spec * light.g * sg;
						b += ks * spec * light.b * sb;
					}
				}
			}
		}

		/*
		 * CALCULO DE LA REFLEXION
		 */

		// Calculo del Rayo reflejado
		if (kr > 0  && nRayos < MAX_RAYOS) {
			Rayo reflejado = new Rayo(new Point3D(p.x, p.y, p.z), Ref);
			
			if (reflejado.trace(objects)) {
				// Reflejado(origen en p pasando por la interseccion con el
				// objeto)
				// Calculamos el color del objeto intersectado y lo añadimos
				Color c = reflejado.Shade(lightSources, objects, bgnd, nRayos+1);
				r += kr * sr * c.getRed()/MAX_COLOR;
				g += kr * sg * c.getGreen()/MAX_COLOR;
				b += kr * sb * c.getBlue()/MAX_COLOR;
			} else {
				// En caso contrario chocara con el fondo, añadimos su color
				r += kr * bgnd.getRed();
				g += kr * bgnd.getGreen();
				b += kr * bgnd.getBlue();
			}

		}

		/*
		 * CALCULO DE LA REFLEXION
		 */
		// Si el color es mayor de 1 se devuelve 1
		r = (r > 1f) ? 1f : r;
		g = (g > 1f) ? 1f : g;
		b = (b > 1f) ? 1f : b;
		return new Color(r, g, b);
	}

	/*
	 * METODOS PRIVADOS
	 */
}
