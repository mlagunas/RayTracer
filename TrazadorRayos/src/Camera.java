import java.util.ArrayList;

public class Camera {

	//instance variables
	private Point3D eye = null;
	private Vector3D lookVector = null;
	private Vector3D upVector = null;
	private Vector3D uVector = null;
	private Vector3D wVector = null;
	private Vector3D vVector = null;

	private double screenDistance;
	private double realStepU;
	private double realStepV;
	
	private Viewport viewport;
	

	private boolean initialized;

	public Camera () {
		initialized=false;
	}

    public void calculateVectors() {
    	
      wVector=Vector3D.scale(1/lookVector.length(), Vector3D.scale(-1, lookVector));
      uVector = Vector3D.scale(1/(Vector3D.crossProd(upVector, wVector).length()),Vector3D.crossProd(upVector, wVector));
      vVector = Vector3D.crossProd(wVector, uVector);
    	

       /* System.out.println("Eye: "+eye.toString());
        System.out.println("LookVector: "+lookVector.toString());
        System.out.println("uVector: "+uVector.toString());
        System.out.println("upVector: "+upVector.toString());
        System.out.println("wVector: "+wVector.toString());
        System.out.println("vVector: "+vVector.toString());*/
        
        realStepU = viewport.getWidth() / (viewport.getColPixels()-1);
        realStepV = viewport.getHeight() / (viewport.getFilPixels()-1);

        initialized=true;
    }

	public Rayo constructRayThroughPixel(double i, double j) {
		
		if (!initialized) { 
			calculateVectors();
		}
		
		Point3D lookAt = new Point3D(eye);
		Vector3D towards = new Vector3D(wVector);
		Vector3D up = new Vector3D(vVector);
		Vector3D right = new Vector3D(uVector);

        towards.scale(-screenDistance);
        final double du=i*(viewport.getWidth() / ((double) viewport.getColPixels()-1));
        final double dv=j*(viewport.getHeight() / ((double) viewport.getFilPixels()-1));
		right.scale(du);
        up.scale(dv);

		lookAt.add(towards);
		lookAt.add(right);
		lookAt.add(up);
		/*System.out.println(j+","+i);
		System.out.println(lookAt.toString());*/
        
        Vector3D rayDirection = new Vector3D(lookAt, eye);

		return new Rayo(eye, rayDirection);
	}
	
	public ArrayList<Rayo> getPixelPositionSuperSampledList(double i, double j, int samplingRadius){
		if (!initialized) { 
			calculateVectors();
		}
		
		final int count = getSuperSampledCount(samplingRadius);
	    final ArrayList<Rayo> pixelRayList = new ArrayList<>(count);
	    
	    Point3D lookAt = null;
		Vector3D towards = null;
		Vector3D up = null;
		Vector3D right = null;

	    final double du = realStepU / (3 * samplingRadius + 1);
	    /*System.out.println(du);*/
	    final double dv = realStepV / (3 * samplingRadius + 1);
	    /*System.out.println(dv);*/
	    
	    for (int k = -samplingRadius; k <= samplingRadius; k++) {
	    	for (int l = -samplingRadius; l <= samplingRadius; l++) {
	            lookAt = new Point3D(eye);
	            towards= new Vector3D(lookVector);
	            up = new Vector3D(vVector);
	    		right = new Vector3D(uVector);
	            
	            double u = i*(viewport.getWidth() / (double) viewport.getColPixels()-1) + k * du;
	            double v = j*(viewport.getHeight() / (double) viewport.getFilPixels()-1) + l * dv;
	            
	            towards.scale(-screenDistance);
	            right.scale(u);
	            up.scale(v);
	            
	            lookAt.add(towards);
	    		lookAt.add(right);
	    		lookAt.add(up);
	    		/*System.out.println(j+","+i);
	            System.out.println(lookAt.toString());*/
	            pixelRayList.add(new Rayo(eye, new Vector3D(lookAt, eye)));
	        }
	    }
		return pixelRayList;	
	}
	
//	public ArrayList<Rayo> getPimaryRaySuperSampledList(int i, int j, int r) {
//	      final int count = getSuperSampledCount(r);
//	      final ArrayList<Point3D> rayOriginList = getRayOriginSuperSampledList(i, j, r);
//	      final ArrayList<Vector3D> rayDirectionList = getRayDirectionSuperSampledList(i, j, r);
//	      if (rayDirectionList != null) {
//	         final ArrayList<Rayo> primaryRayList = new ArrayList<>(count);
//	         Vector3D currentDirection = null;
//	         for (int k = 0; k < count; k++) {
//	            currentDirection = rayDirectionList.get(k);
//	            if (currentDirection != null) {
//	               primaryRayList.add(new Rayo(rayOriginList.get(k), currentDirection));
//	            } else {
//	               primaryRayList.add(null);
//	            }
//	         }
//	         return primaryRayList;
//	      } else {
//	         return null;
//	      }
//	}
//
//	protected ArrayList<Vector3D> getRayDirectionSuperSampledList(int i, int j, int r) {
//		final int count = getSuperSampledCount(r);
//		final ArrayList<Vector3D> directionList = new ArrayList<>(count);
//		for (int k = 0; k < count; k++) {
//			directionList.add(lookVector);
//		}
//		return directionList;
//	}
//
//	protected ArrayList<Point3D> getRayOriginSuperSampledList(int i, int j, int r) {
//		return getPixelPositionSuperSampledList(i, j, r);
//	}

	public static int getSuperSampledCount(int r) {
		return (2 * r + 1) * (2 * r + 1);
	}

//	public float getSuperSampledDU(int r) {
//		return uVector / (3 * r + 1);
//	}
//
//	public float getSuperSampledDV(int r) {
//		return vVector / (3 * r + 1);
//	}

	public Viewport getViewport() {
		return viewport;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	public Point3D getEye() {
		return eye;
	}

	public void setEye(Point3D eye) {
		this.eye = eye;
	}

	public Vector3D getLookVector() {
		return lookVector;
	}

	public void setLookVector(Vector3D lookVector) {
		this.lookVector = lookVector;
	}

	public Vector3D getUpVector() {
		return upVector;
	}

	public void setUpVector(Vector3D upVector) {
		this.upVector = upVector;
	}

	public Vector3D getuVector() {
		return uVector;
	}

	public void setuVector(Vector3D uVector) {
		this.uVector = uVector;
	}

	public Vector3D getwVector() {
		return wVector;
	}

	public void setwVector(Vector3D wVector) {
		this.wVector = wVector;
	}

	public Vector3D getvVector() {
		return vVector;
	}

	public void setvVector(Vector3D vVector) {
		this.vVector = vVector;
	}

	public double getScreenDistance() {
		return screenDistance;
	}

	public void setScreenDistance(double screenDistance) {
		this.screenDistance = screenDistance;
	}

}
