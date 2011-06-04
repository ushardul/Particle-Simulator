import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.EtchedBorder;

public class MainControl extends JPanel implements Runnable, MouseMotionListener, MouseListener
{
    Thread t;
    boolean create = false, right = false;
    static int particleCount = 0;
    static int simSpeed = 100;
    final static int SIM_MIN = 25, SIM_MAX = 400;
    final static double D_RATIO = 10; // D_RATIO = x <- X PIXELS = 1 METER
    Vector emitters = new Vector ();
    int mouseX, mouseY;
    char eType = 'p';
    public MainControl ()
    {
	this.setFocusable (true);
	this.setLayout (null);
	this.setPreferredSize (new Dimension (800, 800));
	this.setBorder (BorderFactory.createEtchedBorder (EtchedBorder.LOWERED));
	addMouseListener (this);
	addMouseMotionListener (this);
	// Emitter e = new Emitter (300, 100, 'n');
	// e.setUp (310, 90);
	// emitters.add (e);
	// e = new Emitter (200, 200, 'n');
	// e.setUp (190, 210);
	// emitters.add (e);
	t = new Thread (this);
	t.start ();
    }


    public void run ()
    {
	long tm = System.currentTimeMillis (); // gets current system time
	while (true)
	{
	    for (int i = 0 ; i < emitters.size () ; i++)
	    {
		Emitter e = (Emitter) emitters.get (i);
		for (int i2 = 0 ; i2 < e.particles.size () ; i2++)
		{
		    for (int i3 = i ; i3 < emitters.size () ; i3++)
		    {
			Emitter e2 = (Emitter) emitters.get (i3);
			for (int i4 = 0 ; i4 < e2.particles.size () ; i4++)
			{
			    if ((i3 == i && i4 > i2) || i3 != i)
			    {
				Particle p1 = (Particle) e.particles.get (i2);
				Particle p2 = (Particle) e2.particles.get (i4);
				p1.collision (p2);
				p1.calculateForce (p2);
			    }
			}
		    }
		}
	    }
	    for (int i = 0 ; i < emitters.size () ; i++)
	    {
		Emitter e = ((Emitter) (emitters.get (i)));
		if (e.delete ())
		    emitters.remove (i);
		else
		{
		    e.calculate ();

		}
	    }
	    repaint ();
	    Launch.dispCount.setText ("Particle Count: " + particleCount);
	    if (particleCount > 100)
		Launch.dispCount.setForeground (Color.RED);
	    else
		Launch.dispCount.setForeground (Color.BLACK);
	    particleCount = 0;
	    // Tries to sleep the thread
	    try
	    {
		// delay is added to timeout
		tm += 33;
		// Thread sleeps for the maximum time between 0 or the difference between the timeout and current time
		Thread.sleep (Math.max (0, tm - System.currentTimeMillis ()));
	    }
	    catch (InterruptedException e)  // if the thread is interuppted
	    {
		break; //display error message
	    }
	}
    }


    public void mousePressed (MouseEvent e)
    {
	if (create == false && e.getButton () == e.BUTTON1)
	{
	    boolean occupied = false;
	    mouseX = e.getX ();
	    mouseY = e.getY ();
	    for (int i = 0 ; i < emitters.size () ; i++)
	    {
		Emitter check = (Emitter) emitters.get (i);
		if (distance (check.getX (), check.getY (), mouseX, mouseY) < (check.getRadius () * 2))
		{
		    occupied = true;
		    return;
		}
	    }
	    if (occupied == false)
	    {
		emitters.add (new Emitter (e.getX (), e.getY (), eType));
		create = true;
	    }
	}
	else
	    right = true;
    }


    public void mouseReleased (MouseEvent e)
    {
	if (create == true && e.getButton () == e.BUTTON1)
	{
	    ((Emitter) (emitters.get (emitters.size () - 1))).setUp (e.getX (), e.getY ());
	    create = false;
	}
	else
	    right = false;
    }


    public void mouseClicked (MouseEvent e)
    {
    }


    public void mouseEntered (MouseEvent e)
    {
    }


    public void mouseExited (MouseEvent e)
    {
    }


    public void mouseDragged (MouseEvent e)
    {
	mouseX = e.getX ();
	mouseY = e.getY ();
	if (right)
	{
	    boolean occupied = false;
	    mouseX = e.getX ();
	    mouseY = e.getY ();
	    for (int i = 0 ; i < emitters.size () ; i++)
	    {
		Emitter check = (Emitter) emitters.get (i);
		if (distance (check.getX (), check.getY (), mouseX, mouseY) < (check.getRadius () * 2))
		    emitters.remove (i);
	    }
	}
    }


    public void mouseMoved (MouseEvent e)
    {
    }


    public void paintComponent (Graphics g)
    {
	super.paintComponent (g); // Calls paintComponent method in upper level class
	Graphics2D g2 = (Graphics2D) g; //g2 becomes g object
	for (int i = 0 ; i < emitters.size () ; i++)
	    ((Emitter) (emitters.get (i))).draw (g2);
	if (create)
	{
	    Emitter e = (Emitter) (emitters.get (emitters.size () - 1));
	    g2.drawLine ((int) e.getX (), (int) e.getY (), mouseX, mouseY);
	}
    }


    public static double distance (double x1, double y1, double x2, double y2)
    {
	return Math.sqrt ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }


    public void creationChange (char e)
    {
	this.eType = e;
    }
}


