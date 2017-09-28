import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class GraphicBall implements Runnable{
	final int IMMUNITY_FRAMES = 10;

	private boolean isRunning;
	private boolean collided;
	private double x;
	private double y;
	private double speed;
	private double xSpeed;
	private double ySpeed;
	private int radius;
	private Vector vector;

	private ArrayList<GraphicBall> ballsImmune; 
	private ArrayList<Integer> framesSinceHit;

	private Thread ballThread;

	public GraphicBall(double x, double y) {
		isRunning = true;
		collided = false;
		this.x = x;
		this.y = y;
		speed = 5;

		//Initializes a random direction on creation
		double rad = 2 * Math.PI * Math.random(); 
		xSpeed = speed * Math.cos(rad);
		ySpeed = speed * Math.sin(rad);
		radius = 10;	
		vector = new Vector (xSpeed, ySpeed);

		//Instantiates array lists
		ballsImmune = new ArrayList<GraphicBall> ();
		framesSinceHit = new ArrayList<Integer> ();

		ballThread = new Thread (this);
		ballThread.start();
	}

	public Vector getVector() { return vector; }
	public double getX() { return x; }
	public double getY() { return y; }

	//Checks if a ball is colliding with another ball
	public boolean detectBallCollision (GraphicBall otherBall)
	{
		if (this != otherBall) {
			double xDist = x - otherBall.getX();
			double yDist = y - otherBall.getY();
			double distance = Math.sqrt( Math.pow(xDist, 2) + Math.pow(yDist, 2)); //calculate the distance between the balls

			if (distance < radius * 2 )
			{
				return true;
			}
		}
		return false;
	}

	//Changes the ball's direction based on the angle of the surface it hits
	public void collide(Vector v)
	{
		//get the ball's current angle
		double ballAngle = vector.getAngle();

		//get the surface angle
		double surfaceAngle = v.getAngle();

		//get the angle between the ball and the surface
		double angleBetween = ballAngle - surfaceAngle;

		//get the angle of reflection
		double reflection = surfaceAngle - angleBetween;


		xSpeed = speed * Math.cos( Math.toRadians(reflection) );
		ySpeed = speed * Math.sin( Math.toRadians(reflection) );

		vector.setDirection(xSpeed, ySpeed);
	}

	//Changes the ball's direction based on the angle of a ball it hits
	public void collide(GraphicBall otherBall) {
		if (!ballsImmune.contains(otherBall)) {
			framesSinceHit.add(0);
			ballsImmune.add(otherBall);

			Vector v = otherBall.getVector();
			//get the ball's current angle
			double ballAngle = vector.getAngle();

			//get the surface angle
			double surfaceAngle = v.getAngle();

			//get the angle between the ball and the surface
			double angleBetween = ballAngle - surfaceAngle;

			//get the angle of reflection
			double reflection = surfaceAngle - angleBetween;

			xSpeed = speed * Math.cos( Math.toRadians(reflection) );
			ySpeed = speed * Math.sin( Math.toRadians(reflection) );

			vector.setDirection(xSpeed, ySpeed);
		}
	}

	//Draw the graphic at the desired location
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillOval((int) x, 505, radius * 2 , radius);
		g.setColor(Color.blue);
		g.fillOval((int) x, (int) y, radius * 2, radius * 2);

	}

	//Handles collisions with the wall
	public void detectWallCollision () {
		if (x > 490 && xSpeed > 0) {
			collide(new Vector (0,1));
		}

		if (x < 0 && xSpeed < 0) {
			collide(new Vector (0,1));
		}

		if (y < 0  && ySpeed < 0) {
			collide(new Vector (1,0));
		}

		if (y > 490 && ySpeed> 0) {
			collide (new Vector (1,0));
		}
	}

	public void update() { 
		//Updates the immunity list so that it cannot hit the same ball within a certain number of frames
		for (int i = 0; i < ballsImmune.size(); i++) {
			if (framesSinceHit.get(i) > IMMUNITY_FRAMES) {
				ballsImmune.remove(i);
				framesSinceHit.remove(i);
				i--;
			} else {
				framesSinceHit.set(i, framesSinceHit.get(i) + 1);
			}
		}

		detectWallCollision();
		x += xSpeed;
		y += ySpeed;
	}

	@Override
	public void run() {

		while (isRunning)
		{
			update();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
