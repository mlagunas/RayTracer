
public class Viewport {

	private double width;
	private double height;
	
	private Point3D upperLeft;
	private Point3D upperRight;
	private Point3D lowerLeft;
	private Point3D lowerRight;
	
	private Point3D center;
	
	private int colPixels;
	private int filPixels;

	
	
	public Viewport (double width, double height, Point3D center, int col, int fil) {
		this.setDimensions(width, height, center);
		colPixels=col;
		filPixels=fil;
	}
	
	public Viewport (Point3D upperLeft, Point3D upperRight, Point3D lowerLeft, Point3D lowerRight, int col, int fil) {
		this.setCornerPoints(upperLeft, upperRight, lowerLeft, lowerRight);
		colPixels=col;
		filPixels=fil;
	}
	
	public void setDimensions (double width, double height, Point3D center) {
		this.width = width;
		this.height = height;
		this.center = center;
		
		// Calculate the four corners of this viewport.
		double right = (width / 2.0) + center.x;
		double left = center.x - (width / 2.0);
		double up = (height / 2.0) + center.y;
		double down = center.y - (height / 2.0);
		
		this.upperLeft = new Point3D(left, up, center.z);
		this.upperRight = new Point3D(right, up, center.z);
		this.lowerLeft = new Point3D(left, down, center.z);
		this.lowerRight = new Point3D(right, down, center.z);
	}
	
	public void setCornerPoints (Point3D upperLeft, Point3D upperRight, Point3D lowerLeft, Point3D lowerRight) {
		this.upperLeft = upperLeft;
		this.upperRight = upperRight;
		this.lowerLeft = lowerLeft;
		this.lowerRight = lowerRight;
		
		this.width = upperRight.x - upperLeft.x;
		this.height = upperLeft.y - lowerLeft.y;
		this.center = new Point3D(this.width / 2.0, this.height / 2.0, upperLeft.z);
	}
	
	
	public double getWidth () {
		return this.width;
	}

	public double getHeight () {
		return this.height;
	}
	
	public Point3D getCenter () {
		return this.center;
	}

	public Point3D getUpperLeft () {
		return this.upperLeft;
	}

	public Point3D getUpperRight () {
		return this.upperRight;
	}

	public Point3D getLowerLeft () {
		return this.lowerLeft;
	}

	public Point3D getLowerRight () {
		return this.lowerRight;
	}
	
	
	public int getColPixels() {
		return colPixels;
	}

	public void setColPixels(int colPixels) {
		this.colPixels = colPixels;
	}

	public int getFilPixels() {
		return filPixels;
	}

	public void setFilPixels(int filPixels) {
		this.filPixels = filPixels;
	}

	@Override
	public String toString () {
		String output = "Viewport: ";
		
		output += "\n\tWidth: " + this.width + " height: " + this.height;
		output += "\n\tCenter: " + this.center;
		output += "\n\tUpper left coordinates: " + this.upperLeft;
		output += "\n\tUpper right coordinates: " + this.upperRight;
		output += "\n\tLower left coordinates: " + this.lowerLeft;
		output += "\n\tUpper right coordinates: " + this.lowerRight;

		return output;
	}

}
