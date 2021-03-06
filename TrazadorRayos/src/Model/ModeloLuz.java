package Model;

import java.awt.Color;
import java.util.ArrayList;

import Objects.Objeto;

public class ModeloLuz {
	// Maximo de rayos reflejados lanzados
	private final int MAX_RAYOS = 1;
	private final float MAX_COLOR = 255;

	public double ka;
	public double kd;
	public double ks;
	public double kr;
	public double kt;
	public double n;
	public double index;

	public ModeloLuz(double ka, double kd, double ks, double kr, double n,
			double kt, double index) {
		this.ka = ka; // Coeficiente ambiental
		this.kd = kd; // Coeficiente difusa
		this.ks = ks; // Coeficiente especular
		this.kr = kr; // Coeficiente reflexion
		this.kt = kt; // Coeficiente refraccion
		this.n = n;
		this.index = index;
	}

	public Color calculo(Color color, Color bgnd, ArrayList<Luz> lightSources,
			ArrayList<Objeto> objects, Point3D p, Point3D p1, Vector3D N,
			Vector3D V, Vector3D R, Rayo frac, int nRayos, double kref) {
		float r = 0;
		float g = 0;
		float b = 0;

		float sr = color.getRed() / (float) MAX_COLOR;
		float sg = color.getGreen() / (float) MAX_COLOR;
		float sb = color.getBlue() / (float) MAX_COLOR;

		for (Luz light : lightSources) {
			/*
			 * CALCULO DE LA LUZ AMBIENTAL
			 */
			if (light.getLightType() == Luz.AMBIENT) {
				r += ka * light.r * sr;
				g += ka * light.g * sg;
				b += ka * light.b * sb;

			} else {
				Vector3D L = null;
				if (light.lightType == Luz.POINT) {
					L = new Vector3D(light.lvec.x + p.x, light.lvec.y + p.y,
							light.lvec.z + p.z);
					L.normalize();
				}

				/*
				 * CALCULO SI ES UN PUNTO EN SOMBRA
				 */
				Rayo ra = new Rayo(p, L);
				if (ra.trace(objects))
					break;

				/*
				 * CALCULO LUZ DIFUSA Kd*Id Id = I*cos(N�L)
				 */
				float lambert = 0;
				if (kd > 0) {
					// cos(N�L)
					lambert = (float) Vector3D.dotProd(N, L);
					if (lambert > 0) {
						// Kd*cos(N�L)*I
						r += kd * lambert * sr * light.r;
						g += kd * lambert * sg * light.g;
						b += kd * lambert * sb * light.b;
					}
				}

				/*
				 * CALCULO LUZ ESPECULAR Ks*Is Is = I*cos(R�V)^n
				 */
				if (ks > 0) {
					// cos(R�V)^n
					// lambert *= 2;
					// float spec = (float) V.dotProd(new Vector3D(lambert * N.x
					// - L.x, lambert * N.y - L.y, lambert * N.z - L.z));

					double twice = 2 * lambert;
					Vector3D LR = Vector3D.sub(Vector3D.scale(twice, N), L);

					double dot = Vector3D.dotProd(V, LR);
					if (dot > 0) {
						//
						double spec = Math.pow(dot, n);
						r += spec * light.r * ks * sr;
						g += spec * light.g * ks * sg;
						b += spec * light.b * ks * sb;
					}
				}
			}
		}

		/*
		 * CALCULO DE LA REFLEXION
		 */

		// Calculo del Rayo reflejado

		if (kr > 0 && nRayos < MAX_RAYOS) {
			double twice = 2 * Vector3D.dotProd(V, N);
			Vector3D LR = Vector3D.sub(Vector3D.scale(twice, N), V);
			LR.normalize();
			Rayo reflejado = new Rayo(new Point3D(p.x, p.y, p.z), LR);
			if (reflejado.trace(objects)) {
				Color c = reflejado.Shade(lightSources, objects, bgnd,
						nRayos + 1, kref);
				r += kr * c.getRed() / MAX_COLOR;
				g += kr * c.getGreen() / MAX_COLOR;
				b += kr * c.getBlue() / MAX_COLOR;
			} else {
				// En caso contrario chocara con el fondo, a�adimos su
				// color
				r += kr * bgnd.getRed() / MAX_COLOR;
				g += kr * bgnd.getGreen() / MAX_COLOR;
				b += kr * bgnd.getBlue() / MAX_COLOR;
			}
		}

		/*
		 * CALCULO DE LA REFRACCION
		 */

		if (kt > 0 && frac != null && nRayos < MAX_RAYOS) {
			if (frac.object == null) {
				r += kt * bgnd.getBlue() / MAX_COLOR;
				g += kt * bgnd.getGreen() / MAX_COLOR;
				b += kt * bgnd.getBlue() / MAX_COLOR;
			} else {
				Color c = frac.Shade(lightSources, objects, bgnd, nRayos + 1,
						kref);
				// Calculamos el color del objeto intersectado y lo
				// a�adimos
				r += kt * c.getRed() / MAX_COLOR;
				g += kt * c.getGreen() / MAX_COLOR;
				b += kt * c.getBlue() / MAX_COLOR;
			}
		}

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