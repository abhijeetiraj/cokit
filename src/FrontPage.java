
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * FrontPage.java
 * 
 * @version 1.1 12/01/2002
 * @author Abhijit Dhanpal Iraj`
 * 
 */

public class FrontPage extends DemoModule
{


    /**
     * main method allows us to run as a standalone demo.
     */

    public static void main(String[] args)
    {
        FrontPage demo = new FrontPage(null);
        demo.mainImpl();
    }

    /**
     * FrontPage Constructor
     */
    public FrontPage(CoKit cokit)
    {
        super(cokit, "FrontPage", "toolbar/FrontPage.gif");

        getDemoPanel().setLayout(new BorderLayout());
        JPanel logoPanel = createLogo();
        getDemoPanel().add(logoPanel, BorderLayout.NORTH);

    }

    /**
     * create Logo
     */
    JPanel createLogo()
    {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        ImageIcon logo = loadImageIcon("D:/CoKit/resources/images/CoKit.gif", "CoKit!");
        JLabel logoLabel = new JLabel(logo);
        p.add(logoLabel, BorderLayout.CENTER);

        return p;
    }

    public ImageIcon loadImageIcon(String filename, String description)
    {
        return new ImageIcon(filename, description);
    }


}
