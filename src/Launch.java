import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import javax.swing.event.*;
public class Launch extends JFrame implements ActionListener, ChangeListener
{
    JButton proton, electron, neutron, elecField, gravField, magField;
    MainControl m = new MainControl ();
    static JLabel dispCount = new JLabel ("Particle Count: " + MainControl.particleCount);
    JSlider speedControl = new JSlider (JSlider.HORIZONTAL, (int) (Math.log (MainControl.SIM_MIN / 100.0) / Math.log (4)), (int) (Math.log (MainControl.SIM_MAX / 100.0) / Math.log (4)), 0);
    public Launch ()
    {
	Color darkGrey = new Color (238, 238, 238);
	Color lightGrey = new Color (244, 244, 244);
	JPanel GUI = new JPanel (new GridLayout (4, 1));
	GUI.setPreferredSize (new Dimension (250, 800));
	JPanel emitters = new JPanel (new GridLayout (2, 2));
	emitters.setBorder (BorderFactory.createTitledBorder ("Emitters"));
	emitters.setBackground (lightGrey);
	proton = new JButton ("Proton");
	electron = new JButton ("Electron");
	neutron = new JButton ("Neutron");
	emitters.add (proton);
	proton.addActionListener (this);
	proton.setBackground (darkGrey);
	proton.setForeground (Color.BLUE);
	electron.addActionListener (this);
	electron.setBackground (darkGrey);
	electron.setForeground (Color.YELLOW);
	neutron.addActionListener (this);
	neutron.setBackground (darkGrey);
	emitters.add (electron);
	emitters.add (neutron);

	JPanel forces = new JPanel (new GridLayout (2, 2));
	forces.setBorder (BorderFactory.createTitledBorder ("Forces"));
	forces.setBackground (lightGrey);
	elecField = new JButton ("Electric");
	gravField = new JButton ("Gravitational");
	magField = new JButton ("Magnetic");
	elecField.addActionListener (this);
	elecField.setBackground (darkGrey);
	gravField.addActionListener (this);
	gravField.setBackground (darkGrey);
	magField.addActionListener (this);
	magField.setBackground (darkGrey);
	forces.add (elecField);
	forces.add (gravField);
	forces.add (magField);

	JPanel info = new JPanel (new GridLayout (3, 1));
	info.setBorder (BorderFactory.createTitledBorder ("Information"));
	info.setBackground (lightGrey);
	info.add (dispCount);

	JPanel control = new JPanel (new GridLayout (3, 1));
	control.setBorder (BorderFactory.createTitledBorder ("Control"));
	control.setBackground (lightGrey);
	speedControl.addChangeListener (this);
	Hashtable labelTable = new Hashtable ();
	labelTable.put (new Integer (-1), new JLabel ("" + MainControl.SIM_MAX / 100.0 + "x"));
	labelTable.put (new Integer (1), new JLabel ("    " + MainControl.SIM_MIN / 100.0 + "x"));
	labelTable.put (new Integer (0), new JLabel ("1x"));
	speedControl.setLabelTable (labelTable);
	speedControl.setPaintLabels (true);
	speedControl.setBorder (BorderFactory.createTitledBorder ("SpeedControl"));
	speedControl.setBackground (lightGrey);
	speedControl.setInverted (true);
	control.add (speedControl);

	GUI.add (emitters);
	GUI.add (forces);
	GUI.add (control);
	GUI.add (info);

	getContentPane ().add (GUI, BorderLayout.EAST);
	getContentPane ().add (m, BorderLayout.CENTER);
	setSize (1050 , 800);
	setVisible (true);
	setDefaultCloseOperation (EXIT_ON_CLOSE);
	setLocationRelativeTo (null);
	setResizable (false);
	setTitle ("Particle Simulation");
    }


    public static void main (String[] args)
    {
	new Launch ();
    }


    public void actionPerformed (ActionEvent e)
    {
	Object cause = e.getSource ();
	if (cause == proton)
	    m.creationChange ('p');
	else if (cause == electron)
	    m.creationChange ('e');
	else if (cause == neutron)
	    m.creationChange ('n');
	//else if (cause == elecField)
	    //m.creationChange ('
    }


    public void stateChanged (ChangeEvent e)
    {
	JSlider source = (JSlider) e.getSource ();
	if (!source.getValueIsAdjusting ())
	    MainControl.simSpeed = (int) (100 * Math.pow (4, source.getValue ()));
    }
}
