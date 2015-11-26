import java.awt.Color;
import java.util.ArrayList;

public class ModeloLuz {
	double ka;
	double kd;
	double ks;
	double kr;
	double n;

	public ModeloLuz(double ka, double kd, double ks, double kr, double n) {
		this.ka = ka; // Coeficiente ambiental
		this.kd = kd; // Coeficiente difusa
		this.ks = ks; // Coeficiente especular
		this.kr = kr; // Coeficiente reflexion
		this.n = n;
	}

	public Color calculo(Color color, ArrayList<Luz> lightSources,
			ArrayList<Objeto> objects, Vector3D L, Point3D p, Vector3D N,
			Vector3D V, Vector3D R, Vector3D Ref, Vector3D frac) {
		double r = 0;
		double g = 0;
		double b = 0;
		for (Luz light : lightSources) {
			/*
			 * CALCULO DE LA LUZ AMBIENTAL
			 */
			if (light.getLightType() == Luz.AMBIENT) {
				r += ka * light.getIr() / 255 * color.getRed() / 255;
				g += ka * light.getIg() / 255 * color.getGreen() / 255;
				b += ka * light.getIb() / 255 * color.getBlue() / 255;

			} else {
				/*
				 * else { if (light.lightType == Light.POINT) { l = new
				 * Vector3dd(light.lvec.x - p.x, light.lvec.y - p.y,
				 * light.lvec.z - p.z); l.normalize(); } else { l = new
				 * Vector3dd(-light.lvec.x, -light.lvec.y, -light.lvec.z); }
				 */

				/*
				 * CALCULO SI ES UN PUNTO EN SOMBRA
				 */
				Point3D poffset = new Point3D(p.x * L.x, p.y * L.y, p.z * L.z);
				Rayo shadowRay = new Rayo(poffset, L);
				if (shadowRay.trace(objects))
					return null;

				/*
				 * CALCULO LUZ DIFUSA Kd*Id Id = I*cos(N·L)
				 */
				if (kd > 0) {
					// cos(N·L)
					double lambert = Math.cos(Vector3D.dotProd(N, L));
					if (lambert > 0) {
						// Kd*cos(N·L)*I
						r += kd * lambert * light.getIr() / 255
								* color.getRed() / 255;
						g += kd * lambert * light.getIg() / 255
								* color.getGreen() / 255;
						b += kd * lambert * light.getIb() / 255
								* color.getBlue() / 255;
					}
				}

				/*
				 * CALCULO LUZ ESPECULAR Ks*Is Is = I*cos(R·V)^n
				 */
				if (ks > 0) {
					// cos(R·V)^n
					double spec = Math.pow(n, Math.cos(Vector3D.dotProd(V,R)));
					if (spec > 0) {
						//
						r += ks * spec * light.getIr() / 255 * color.getRed()
								/ 255;
						g += ks * spec * light.getIg() / 255 * color.getGreen()
								/ 255;
						b += ks * spec * light.getIb() / 255 * color.getBlue()
								/ 255;
					}
				}
			}
		}

		/*
		 * CALCULO DE LA REFLEXION
		 */

		// Calculo del Rayo reflejado
		if (kr > 0) {
			Rayo reflejado = new Rayo(new Point3D(p.x, p.y, p.z), Ref);
			for (Objeto o : objects) {

				if (reflejado.trace(o)) {
					// Reflejado(origen en p pasando por la interseccion con el
					// objeto)
					// Calculamos el color del objeto intersectado y lo añadimos
					Color c = o.Shade(reflejado, p, lightSources, objects);
					r += kr * c.getRed();
					g += kr * c.getGreen();
					b += kr * c.getBlue();
				} else {
					// En caso contrario chocara con el fondo, añadimos su color
					r += kr * color.getRed();
					g += kr * color.getGreen();
					b += kr * color.getBlue();
				}
			}
		}

		/*
		 * CALCULO DE LA REFLEXION
		 */
		// Si el color es mayor de 1 se devuelve 1
		r = (r > 1f) ? 1f : r;
		g = (g > 1f) ? 1f : g;
		b = (b > 1f) ? 1f : b;
		return new Color((float) r, (float) g, (float) b);
	}

	/*
	 * METODOS PRIVADOS
	 */
}
