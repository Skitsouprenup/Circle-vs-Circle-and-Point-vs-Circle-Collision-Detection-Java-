import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.EventQueue;

public class CircCirc1 {
	
	CircObject c1,c2;
	boolean isColliding;
	float c1OffsetX,c1OffsetY,c2CenterX,c2CenterY;
	float totalRadiusX;
	
	public static void main(String[] args) {
		new CircCirc1();
	}
	
	public CircCirc1() {
		
		c1 = new CircObject(0f,0f,60f,60f);
		c2 = new CircObject(150f,150f,90f,90f);
		c2CenterX = c2.getX() + c2.getWidth() * 0.5f;
		c2CenterY = c2.getY() + c2.getHeight() * 0.5f;
		totalRadiusX = c1.getRadX() + c2.getRadX();
		
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				JFrame jf = new JFrame("CircCirc1");
				Panel pnl = new Panel();
				pnl.addMouseMotionListener(new MouseMotion());
				jf.add(pnl);
				jf.pack();
				jf.setResizable(false);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setLocationRelativeTo(null);
				jf.setVisible(true);
			}
			
		});
		
	}
	
	void updateData(){
		//System.out.println("updating...");
		c1OffsetX = c1.getX() - c1.getWidth() * 0.5f;
		c1OffsetY = c1.getY() - c1.getHeight() * 0.5f;

	}
	
	void drawObjects(Graphics2D g2d){
		//System.out.println("drawing objects...");
		g2d.setPaint(Color.BLUE);
		g2d.fill(c2.getCirc());
		c1.getCirc().setFrame(c1OffsetX,c1OffsetY,c1.getWidth(),c1.getHeight());
		g2d.setPaint(Color.YELLOW);
		g2d.fill(c1.getCirc());
	}
	
	class Panel extends JPanel {
		
		Panel(){
			Timer timer = new Timer(16, new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e){
					updateData();
					repaint();
				}
			});
			timer.start();
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400,400);
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0,0,getWidth(),getHeight());
			drawObjects(g2d);
			g2d.setPaint(Color.WHITE);
			g2d.drawString("Mouse-controlled circle will turn red if there's a collision", 50f, 20f);
			g2d.drawLine((int)c1.getX(),(int)c1.getY(),(int)c2CenterX,(int)c2CenterY);
			g2d.dispose();
		}
	}
	
	class CircObject {
		private float x,y,width,height;
		private Ellipse2D circ;
		
		CircObject(float x, float y, float width, float height){
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			circ = new Ellipse2D.Float(x,y,width,height);
		}
		
		float getX(){return x;}
		float getY(){return y;}
		float getWidth(){return width;}
		float getHeight(){return height;}
		float getRadX(){return width * 0.5f;}
		float getRadY(){return height * 0.5f;}
		Ellipse2D getCirc(){return circ;}
		
		void setX(float x){this.x = x;}
		void setY(float y){this.y = y;}
	}
	
	class MouseMotion implements MouseMotionListener {
	
		@Override
		public void mouseDragged(MouseEvent e){}
	
		@Override
		public void mouseMoved(MouseEvent e){
			c1.setX(e.getX());
			c1.setY(e.getY());
		}
	}
	
}

