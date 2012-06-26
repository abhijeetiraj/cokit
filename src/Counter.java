/**
 * Counter.java-CoKit
 * 
 * @version 1.1 2002/01/01
 * @author Abhijeet Dhanpal Iraj
 */

public class Counter extends DemoModule
{




    /**
     * main method allows us to run as a standalone demo.
     */

    public static void main(String[] args)
    {
        Counter demo = new Counter(null);
        demo.mainImpl();
    }

    /**
     * Counter Constructor
     */

    public Counter(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "Counter", "toolbar/Counter.gif");





    }
}
