import java.awt.*;
public class Particle
{
    private double x = 0, y = 0, vx, vy, charge = 0, mass = 0, radius = 3, Fxnet = 0, Fynet = 0;
    private Color colType;
    private char type;
    public Particle (double x, double y, double dir, double speed, double charge, double mass, double radius, Color colType, char type)
    {
	this.x = x;
	this.y = y;
	this.vx = Math.cos (dir) * speed;
	this.vy = Math.sin (dir) * speed;
	this.charge = charge;
	this.mass = mass;
	this.radius = radius;
	this.colType = colType;
	this.type = type;
    }


    public void calculate ()
    {
	vx += Fxnet * (2.30714e-28) * (3.0 / MainControl.simSpeed) / mass;
	vy += Fynet * (2.30714e-28) * (3.0 / MainControl.simSpeed) / mass;
	this.x += vx * (3.0 / MainControl.simSpeed) * MainControl.D_RATIO;
	this.y += vy * (3.0 / MainControl.simSpeed) * MainControl.D_RATIO;
	Fxnet = 0;
	Fynet = 0;
    }


    public void draw (Graphics2D g)
    {
	g.setColor (colType);
	g.fillOval ((int) (x - radius), (int) (y - radius), (int) radius * 2, (int) radius * 2);
    }


    public boolean delete ()
    {
	if (x < 0 || x > 800 || y < 0 || y > 800)
	    return true;
	return false;
    }


    public void calculateForce (Particle p)
    {
	double distance = MainControl.distance (x, y, p.x, p.y);
	// Uncomment code for performance considerations
	//if (((type == 'p' || type == 'n') && distance > 12) || (type == 'e' && distance > 500))
	//return;
	//else
	//{
	if (distance < radius + p.radius)
	    distance = radius + p.radius;
	distance /= MainControl.D_RATIO;
	double theta = Math.atan2 (p.y - y, p.x - x), theta1, theta2;
	if (type == p.type)
	{
	    theta1 = Math.PI + theta;
	    theta2 = theta;
	}
	else
	{
	    theta1 = theta;
	    theta2 = Math.PI + theta;
	}
	distance = 1 / (distance * distance);
	Fxnet += Math.cos (theta1) * distance;
	Fynet += Math.sin (theta1) * distance;
	p.Fxnet += Math.cos (theta2) * distance;
	p.Fynet += Math.sin (theta2) * distance;
	//}
    }


    public void collision (Particle p)
    {
	double distance = MainControl.distance (x, y, p.x, p.y);
	if (distance <= p.radius + radius)
	{
	    distance /= MainControl.D_RATIO;
	    double theta = Math.atan2 (p.y - y, p.x - x), theta1 = Math.atan2 (vy, vx) - theta, theta2 = Math.atan2 (p.vy, p.vx) - theta;
	    double v1 = Math.cos (theta1) * Math.sqrt (vx * vx + vy * vy), v2 = Math.cos (theta2) * Math.sqrt (p.vx * p.vx + p.vy * p.vy), v1buf,
		v1perp = Math.sin (theta1) * Math.sqrt (vx * vx + vy * vy), v2perp = Math.sin (theta2) * Math.sqrt (p.vx * p.vx + p.vy * p.vy),
		comCalc1 = mass * v1 + p.mass * v2, comCalc2 = v2 - v1, comCalc3 = mass + p.mass;
	    v1buf = (comCalc1 + p.mass * comCalc2) / (comCalc3);
	    v2 = (comCalc1 - mass * comCalc2) / (comCalc3);
	    v1 = v1buf;
	    theta1 = Math.atan2 (v1perp, v1) + theta;
	    theta2 = Math.atan2 (v2perp, v2) + theta;
	    v1 = Math.sqrt (v1 * v1 + v1perp * v1perp);
	    v2 = Math.sqrt (v2 * v2 + v2perp * v2perp);
	    vx = Math.cos (theta1) * v1;
	    vy = Math.sin (theta1) * v1;
	    p.vx = Math.cos (theta2) * v2;
	    p.vy = Math.sin (theta2) * v2;
	}
    }
}
