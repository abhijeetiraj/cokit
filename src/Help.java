import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * Help.java-CoKit
 * 
 * @version 1.1 2002/01/01
 * @author Abhijeet Dhanpal Iraj
 */

public class Help extends DemoModule
{

    JDesktopPane desktop;
    static final Integer HELPLAYER = new Integer(7);


    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        Help demo = new Help(null);
        demo.mainImpl();
    }

    /**
     * Introduction Constructor
     */
    public Help(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "Help", "toolbar/Introduction.gif");

        // Create the desktop pane
        desktop = new JDesktopPane();
        getDemoPanel().add(desktop, BorderLayout.CENTER);

        JInternalFrame help = new CoKitHelp();
        desktop.add(help, HELPLAYER);
        try
        {
            help.setVisible(true);
            help.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e2)
        {
        }


    }
}
