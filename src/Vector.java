public class Vector {
	double x;
	double y;

	//Constructor
	public Vector (double x, double y) {
		this.x = x;
		this.y = y;
	}

	//get x 
	public double getX() {
		return x;
	}

	//get y
	public double getY() {
		return y;
	}

	//get angle of vector
	public double getAngle() {
		return Math.toDegrees( Math.atan2( y, x) );
	}

	//get radians of vector
	public double getRadians() {
		return Math.atan2(y, x);
	}

	//sets the direction of the vector
	public void setDirection(double x2, double y2) {
		this.x = x2;
		this.y = y2;
	}
}
