import java.util.Vector;
import java.awt.*;

public class Emitter
{
    private double x = 0, y = 0, dir = 0, charge = 0, mass = 1.6749e-27, speed = 1, partRadius = 3;
    private int radius = 5;
    private Color colType = Color.BLACK;
    private double timer, reload = 100;
    public Vector particles = new Vector ();
    private char type;

    private static final double MAX_SPEED = 100;

    boolean ready = false;

    public Emitter (double x, double y, char type)
    {
	this.x = x;
	this.y = y;
	this.type = type;
	if (type == 'p')
	{
	    mass = 1.6726e-27;
	    charge = 1.6022e-19;
	    colType = Color.BLUE;
	    partRadius = 3;
	}
	else if (type == 'e')
	{
	    mass = 9.1094e-31;
	    charge = -1.6022e-19;
	    colType = Color.YELLOW;
	    partRadius = 3;
	}
    }


    public void setUp (double x2, double y2)
    {
	this.speed = Math.sqrt ((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y)) / MainControl.D_RATIO;
	if (speed > MAX_SPEED)
	    speed = MAX_SPEED;
	this.dir = Math.PI + Math.atan2 (y2 - y, x2 - x);
	this.timer = reload;
	this.ready = true;
    }


    public void calculate ()
    {
	if (ready)
	{
	    if (this.timer != 0)
		timer--;
	    else
	    {
		particles.add (new Particle (x, y, dir, speed, charge, mass, partRadius, colType, type));
		timer = reload;
		//timer = -1;
	    }
	    for (int i = 0 ; i < particles.size () ; i++)
	    {
		Particle p = (Particle) (particles.get (i));
		if (p.delete ())
		    particles.remove (i);
		else
		{
		    p.calculate ();
		    MainControl.particleCount++;
		}
	    }
	}
    }


    public void draw (Graphics2D g)
    {
	for (int i = 0 ; i < particles.size () ; i++)
	    ((Particle) (particles.get (i))).draw (g);
	g.setColor (colType);
	g.fillOval ((int) (x - radius), (int) (y - radius), radius * 2, radius * 2);
    }

    public boolean delete ()
    {
	if (speed < 0.1)
	    return true;
	return false;
    }


    public double getX ()
    {
	return x;
    }


    public double getY ()
    {
	return y;
    }


    public double getRadius ()
    {
	return this.radius;
    }
}
