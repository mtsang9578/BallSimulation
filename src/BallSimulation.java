import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BallSimulation extends JPanel implements Runnable, MouseListener {

	private boolean running = true;
	private Thread animation;
	private ArrayList<GraphicBall> balls;

	//Constructor
	public BallSimulation() {
		setSize(500, 550);
		balls = new ArrayList<GraphicBall> ();
		animation = new Thread(this);
		animation.start();
	}

	//paints all the balls in the array
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i=0; i<balls.size(); i++) {
			balls.get(i).draw(g);
		}
		for (int i=0; i<balls.size(); i++) {
			for (int j=0; j<balls.size(); j++) {
				if (balls.get(i).detectBallCollision(balls.get(j))) {
					balls.get(i).collide(balls.get(j));
					balls.get(j).collide(balls.get(i));
				}
			}
		}
	}


	//Routine method called by the thread
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		balls.add( new GraphicBall (e.getX(), e.getY()) );
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}


	//Main method for instantiating simulation
	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.setSize(500,550);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		BallSimulation simulation = new BallSimulation();
		frame.add(simulation);
		frame.addMouseListener(simulation);
	}
}
