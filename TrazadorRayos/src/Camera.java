public class Camera {

	//instance variables
	private Point3D eye = null;
	private Vector3D lookVector = null;
	private Vector3D upVector = null;
	private Vector3D uVector = null;
	private Vector3D wVector = null;
	private Vector3D vVector = null;

	private double screenDistance;
	
	private double screenWidth;
	private double screenHeight;

	private boolean initialized;

	public Camera () {
		initialized=false;
	}

    public void calculateVectors() {
    	
      wVector=Vector3D.scale(1/lookVector.length(), Vector3D.scale(-1, lookVector));
      uVector = Vector3D.scale(Vector3D.crossProd(upVector, wVector).length(),Vector3D.crossProd(upVector, wVector));
      vVector = Vector3D.crossProd(wVector, uVector);
    	

        System.out.println("Eye: "+eye.toString());
        System.out.println("LookVector: "+lookVector.toString());
        System.out.println("uVector: "+uVector.toString());
        System.out.println("upVector: "+upVector.toString());
        System.out.println("wVector: "+wVector.toString());
        System.out.println("vVector: "+vVector.toString());



//        // the vector size should be the size of one pixel in the 3 world instead of 1.
//        // first, normalize the size according to the canvas width.
//        double pixelSize = 1 / (double) canvasWidth;
//
//        // then, increase the pixel size according to the viewport width.
//        pixelSize *= viewportWidth;
//
//        uVector.scale(pixelSize);
//        upVector.scale(pixelSize);
        initialized=true;
    }

	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Rayo constructRayThroughPixel(double x, double y) {
		
		if (!initialized) { 
			calculateVectors();
		}
		
		Point3D lookAt = new Point3D(eye);
		Vector3D towards = new Vector3D(lookVector);
		Vector3D up = new Vector3D(vVector);
		Vector3D right = new Vector3D(uVector);

        towards.scale(screenDistance);
		right.scale(x*screenWidth - (double) screenWidth *0.5);
        up.scale(y*screenHeight - (double) screenHeight *0.5);

		lookAt.add(towards);
		lookAt.add(right);
		lookAt.add(up);
		
		System.out.println(lookAt.toString());
        
        Vector3D rayDirection = new Vector3D(lookAt, eye);

		// Note - this is a trivial Orthographic camera
		return new Rayo(eye, rayDirection);
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

	public double getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(double screenHeight) {
		this.screenHeight = screenHeight;
	}
}
