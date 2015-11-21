import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class Trazador extends Applet implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int CHUNKSIZE = 100;
    Image screen;
    Graphics gc;
    Vector objectList;
    Vector lightList;
    Superficie currentSurface;

    Vector3D eye, lookat, up;
    Vector3D Du, Dv, Vp;
    float fov;

    Color background;

    int width, height;

    public void init( ) {
        // initialize the off-screen rendering buffer
        width = size().width;
        height = size().height;
        screen = createImage(width, height);
        gc = screen.getGraphics();
        gc.setColor(getBackground());
        gc.fillRect(0, 0, width, height);

        fov = 30;               // default horizonal field of view

        // Initialize various lists
        objectList = new Vector(CHUNKSIZE, CHUNKSIZE);
        lightList = new Vector(CHUNKSIZE, CHUNKSIZE);
        currentSurface = new Superficie(0.8f, 0.2f, 0.9f, 0.2f, 0.4f, 0.4f, 10.0f, 0f, 0f, 1f);

        // Parse the scene file
        String filename = getParameter("datafile");
        showStatus("Parsing " + filename);
        InputStream is = null;
        /*try {
            is = new URL(getDocumentBase(), filename).openStream();
            ReadInput(is);
            is.close();
        } catch (IOException e) {
            showStatus("Error reading "+filename);
            System.err.println("Error reading "+filename);
            System.exit(-1);
        }*/

        // Initialize more defaults if they weren't specified
        if (eye == null) eye = new Vector3D(0, 0, 10);
        if (lookat == null) lookat = new Vector3D(0, 0, 0);
        if (up  == null) up = new Vector3D(0, 1, 0);
        if (background == null) background = new Color(0,0,0);

        // Compute viewing matrix that maps a
        // screen coordinate to a ray direction
        Vector3D look = new Vector3D(lookat.x - eye.x, lookat.y - eye.y, lookat.z - eye.z);
        Du = Vector3D.normalize(look.productoVectorial(up));
        Dv = Vector3D.normalize(look.productoVectorial(Du));
        float fl = (float)(width / (2*Math.tan((0.5*fov)*Math.PI/180)));
        Vp = Vector3D.normalize(look);
        Vp.x = Vp.x*fl - 0.5f*(width*Du.x + height*Dv.x);
        Vp.y = Vp.y*fl - 0.5f*(width*Du.y + height*Dv.y);
        Vp.z = Vp.z*fl - 0.5f*(width*Du.z + height*Dv.z);
    }


    double getNumber(StreamTokenizer st) throws IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            System.err.println("ERROR: number expected in line "+st.lineno());
            throw new IOException(st.toString());
        }
        return st.nval;
    }

    void ReadInput(InputStream is) throws IOException {
	    StreamTokenizer st = new StreamTokenizer(is);
    	st.commentChar('#');
        scan: while (true) {
	        switch (st.nextToken()) {
	          default:
		        break scan;
	          case StreamTokenizer.TT_WORD:
	            if (st.sval.equals("sphere")) {
                    Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
		            float r = (float) getNumber(st);
		            objectList.addElement(new Esfera(currentSurface, v, r));
			    } else
			    if (st.sval.equals("eye")) {
		            eye = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("lookat")) {
		            lookat = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("up")) {
		            up = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("fov")) {
                    fov = (float) getNumber(st);
			    } else
			    if (st.sval.equals("background")) {
                    background = new Color((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
			    } else
			    if (st.sval.equals("light")) {
			        float r = (float) getNumber(st);
			        float g = (float) getNumber(st);
			        float b = (float) getNumber(st);
		            if (st.nextToken() != StreamTokenizer.TT_WORD) {
                        throw new IOException(st.toString());
                    }
		            if (st.sval.equals("ambient")) {
		                lightList.addElement(new Luz(Luz.AMBIENT, null, r, g, b));
		            } else
		            if (st.sval.equals("directional")) {
		                Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
		                lightList.addElement(new Luz(Luz.DIRECTIONAL, v, r, g, b));
		            } else
		            if (st.sval.equals("point")) {
		                Vector3D v = new Vector3D((float) getNumber(st), (float) getNumber(st), (float) getNumber(st));
		                lightList.addElement(new Luz(Luz.POINT, v, r, g, b));
		            } else {
		                System.err.println("ERROR: in line "+st.lineno()+" at "+st.sval);
		                throw new IOException(st.toString());
		            }
			    } else
			    if (st.sval.equals("surface")) {
			        float r = (float) getNumber(st);
			        float g = (float) getNumber(st);
			        float b = (float) getNumber(st);
		            float ka = (float) getNumber(st);
		            float kd = (float) getNumber(st);
		            float ks = (float) getNumber(st);
		            float ns = (float) getNumber(st);
		            float kr = (float) getNumber(st);
		            float kt = (float) getNumber(st);
		            float index = (float) getNumber(st);
		            currentSurface = new Superficie(r, g, b, ka, kd, ks, ns, kr, kt, index);
			    }
			    break;
	        }
	    }
        is.close();
	    if (st.ttype != StreamTokenizer.TT_EOF)
	        throw new IOException(st.toString());
	}
    
    boolean finished = false;
    
    public void paint(Graphics g) {
        if (finished)
            g.drawImage(screen, 0, 0, this);
    }

    // this overide avoid the unnecessary clear on each paint()
    public void update(Graphics g) {
        paint(g);
    }


    Thread raytracer;

    public void start() {
        if (raytracer == null) {
            raytracer = new Thread(this);
            raytracer.start();
        } else {
            raytracer.resume();
        }
    }

    public void stop() {
        if (raytracer != null) {
            raytracer.suspend();
        }
    }

    private void renderPixel(int i, int j) {
        Vector3D dir = new Vector3D(
                                i*Du.x + j*Dv.x + Vp.x,
                                i*Du.y + j*Dv.y + Vp.y,
                                i*Du.z + j*Dv.z + Vp.z);
        Rayo ray = new Rayo(eye, dir);
        if (ray.trace(objectList)) {
            gc.setColor(ray.Shade(lightList, objectList, background));
        } else {
            gc.setColor(background);
        }
        gc.drawLine(i, j, i, j);        // oh well, it works.
    }

    public void run() {
        Graphics g = getGraphics();
        long time = System.currentTimeMillis();
        for (int j = 0; j < height; j++) {
            showStatus("Scanline "+j);
            for (int i = 0; i < width; i++) {
                renderPixel(i, j);
            }
            g.drawImage(screen, 0, 0, this);        // doing this less often speed things up a bit
        }
        g.drawImage(screen, 0, 0, this);
        time = System.currentTimeMillis() - time;
        showStatus("Rendered in "+(time/60000)+":"+((time%60000)*0.001));
        finished = true;
    }


    public boolean mouseDown(Event e, int x, int y) {
        renderPixel(x, y);
        repaint();
        return true;
    }

    public boolean mouseDrag(Event e, int x, int y) {
        renderPixel(x, y);
        repaint();
        return true;
    }

    public boolean mouseUp(Event e, int x, int y) {
        renderPixel(x, y);
        repaint();
        return true;
    }
}
