//This algorithm is purely based in circle-circle collision with minor changes.

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.awt.image.BufferStrategy;
import java.awt.geom.*;

import java.lang.reflect.InvocationTargetException;


class PointObject
{
	private float x,y;
	
	PointObject(float x, float y)
	{
		this.x = x;
		this.y = y;
		
	}
	
	float getX(){return x;}
	float getY(){return y;}
	
	void setX(float x){this.x = x;}
	void setY(float y){this.y = y;}
}

class CircleObject
{
	//To get the radius of a circle divide the diameter(width/height) by 2
	float x;
	float y;
	float width = 50f;
	float height = 50f;
	
	//To get the center coordinates of the circle, get the position
	//and the radius(divide the diameter(width/height) by 2) of the circle and add them both.
	float centerX,centerY;
	
	
	CircleObject(float x, float y)
	{
		//we minus x and y to its width and height, to remove the extra adjustment
		//that the width and height made. So, the shape is much closer to the center of
		//the screen
		this.x = x - width;
		this.y = y - height;
		
		centerX = this.x + (width/2);
		centerY = this.y + (height/2);
	}
	
	//we need to add the radius of the circle and point. We need to do this because when we test our
	//circle for collision, we will base on the radius length of
	//the circle and point.
	//This is the total radius of the colliding circle and point.
	//Since points have no size(width/height) we only need to get the
	//radius of the circle. You may use width/2 or height/2 to get
	//the circle radius, because we just want the magnitude of the radius, 
	//the results are the same.
	float totalRadius = width/2;
	
	float getTotalRadius(){return totalRadius;}
	
}



public class PointCircle extends JFrame implements MouseMotionListener
{
    private static PointCircle pPoint;
	PointObject pointOne;
	CircleObject circleOne;
	Color bgColor = Color.BLACK; 
	
	//This will be our back buffer graphics. All objects that have been drawn
	//into this graphics2d will be drawn to the BufferedImage().
	private Graphics2D g2dbf;
	
	//
	private Graphics g;
	
	//this will be our back buffer image.
	private VolatileImage bgImage;
	
	//Canvas
	private Canvas frameCanvas;
	
	//JFrame size
	private int frameWidth,frameHeight;
	
	
	//Identity transform
	private AffineTransform identityTrans;
	
	//
	private BufferStrategy bufferS;
	
	//platform's graphics attributes...
    GraphicsEnvironment ge;
    GraphicsDevice gd;
    GraphicsConfiguration gc;
	
	
	PointCircle()
	{
		setTitle("Point Point Collision Test");
		setIgnoreRepaint(true);
		setResizable(false);
		setUndecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set transform to identity transform
		identityTrans = new AffineTransform();
		
		// Get platform's graphics attributes...
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		gc = gd.getDefaultConfiguration();
		
		//Simulated Full Screen Mode
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //frameWidth = (int)screenSize.getWidth();
        //frameHeight = (int)screenSize.getHeight();
		
		//Windowed Mode
		frameWidth = 640;
		frameHeight = 480;
		setUndecorated(false);
		
		setVisible(true);
		
		//initialize colliding objects
		pointOne = new PointObject(0f,0f);
	    circleOne = new CircleObject(frameWidth/2,frameHeight/2);
		
		//Canvas
		frameCanvas = new Canvas(gc);
		frameCanvas.setIgnoreRepaint( true );
        frameCanvas.setSize( frameWidth, frameHeight );
		
		add(frameCanvas);
		pack();
		frameCanvas.requestFocus();
		//setVisible(true);
		
		
		frameCanvas.createBufferStrategy( 2 );
        bufferS = frameCanvas.getBufferStrategy();
		
		//back buffer image
		bgImage = gc.createCompatibleVolatileImage(frameWidth,frameHeight);	
		
		frameCanvas.addMouseMotionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void mouseDragged(MouseEvent e){}
	
	@Override
	public void mouseMoved(MouseEvent e){
	 pointOne.setX(e.getX());
	 pointOne.setY(e.getY());
	}
	
	void drawGraphics()
	{
		try
		{
			g2dbf = bgImage.createGraphics();
		    //Rendering Hints to improve graphics quality
		    g2dbf.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2dbf.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    g2dbf.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g2dbf.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
            g2dbf.setTransform(identityTrans);
            //set background
            g2dbf.setPaint(bgColor);
            g2dbf.fillRect(0,0,frameWidth,frameHeight);
			
			//draw circle
			g2dbf.setPaint(Color.GREEN);
			g2dbf.fill(new Ellipse2D.Float(circleOne.x,circleOne.y,circleOne.width,circleOne.height));
			
			g2dbf.setColor(Color.WHITE);
			g2dbf.drawString("Try to put point(mouse pointer) one to the circle for the collision to happen", frameWidth/5, 20);
			g2dbf.drawString("The background will turn blue once collision happens.",frameWidth/4,40);
			g2dbf.drawString("Point One X: " + pointOne.getX() + " Point One Y: " + pointOne.getY(), 5, 80);
			
			
		    g = bufferS.getDrawGraphics();
		    g.drawImage(bgImage,0,0,null);
			
		    if( !bufferS.contentsLost() ) bufferS.show();
		}
		finally
		{
			if(g2dbf != null) g2dbf.dispose();
			if(g != null) g.dispose();
		}
	}
	
	
	boolean collisionHappens()
	{
		//We just want the magnitude of x and y side, So, we need to get the absolute value of
		//x and y
		//We want to get the x and y length from pointOne to circleOne center-point
		
		//Adjacent
		float xSide = Math.abs(circleOne.centerX - pointOne.getX());
		//Opposite
		float ySide = Math.abs(circleOne.centerY - pointOne.getY());
		
		//hypotenuse
		float distance = (float)Math.sqrt(xSide * xSide + ySide * ySide);
		
		if(distance < circleOne.totalRadius) return true;
		
		return false;
	}
	
	
	public static void main(String[]args) throws InterruptedException,InvocationTargetException
	{
		
		SwingUtilities.invokeAndWait(new Runnable(){
			@Override
			public void run()
			{
		       pPoint = new PointCircle(); 		   
			}
		});
		
		
		//Physics Steps
		new Thread(new Runnable(){
			
			//Variables for fps(Frames Per Second)
            int frames = 0,yieldThread = 0;
            long totalTime = 0,t2 = 0;
            long initialTime = System.nanoTime();
            long currentTime = 0;
            final int FRAMES_PER_SECOND = 60;
		    final int FRAMES_PER_YIELD = (int)(FRAMES_PER_SECOND * 0.2); //Yield when processed frames reaches a portion of Frames per second
            final int NANOS_PER_SECOND = 1_000_000_000;
            final int FRAMES_PER_NANO = NANOS_PER_SECOND / FRAMES_PER_SECOND;
			
			//This millis variables are use to compute dt in millis format. Using nanos format when computing physics elements render a high
			//value.
			final float FRAMES_PER_MILLIS = 1000f/FRAMES_PER_SECOND; 
		    int count = 0;
			
			
			
			@Override
			public void run()
			{
				while(true)
				{
					currentTime = System.nanoTime();
			        totalTime += currentTime - initialTime;
                    t2 += currentTime - initialTime;
                    initialTime = currentTime;
			  
                    if( totalTime > NANOS_PER_SECOND ) {
					  //System.out.println("FPS: " + frames);
                      totalTime -= NANOS_PER_SECOND;
                      frames = 0;
                    } 
            
                    if(t2 < FRAMES_PER_NANO) continue;
					
					
					pPoint.drawGraphics();
					if(pPoint.collisionHappens()) pPoint.bgColor = Color.BLUE;
					else pPoint.bgColor = Color.BLACK;
						
                    frames += 1;
                    t2 -= FRAMES_PER_NANO;
		            yieldThread++;

			  
			        if(yieldThread >= (FRAMES_PER_YIELD))
			        {
				        yieldThread = 0;
				        Thread.yield();
			        }
				}
			}
			
		}).start();

		
	}
	
}