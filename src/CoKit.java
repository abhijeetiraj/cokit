import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * A file that shows all of the CoKit components.
 * 
 * @version 1.1 23/01/2002
 * @author Abhijit Dhanpal Iraj
 * @author Nilesh Madhukar Shirke
 */

public class CoKit extends JPanel
{

    public final static Border loweredBorder = new SoftBevelBorder(BevelBorder.LOWERED);
    // java.applet.Applet applet;
    static CoKit instance;


    String[] demos =
        { "Introduction", "GateLevelGates", "FlipFlop", "RegisterDemo", "PLA", "Multiplexer", "Alu", "C8085Panel", "Memory", "MicroComputer", "DAC",
                "Help", "C8086Panel", "C8087Panel", };

    void loadDemos()
    {
        for (int i = 0; i < demos.length;)
        {
            loadDemo(demos[i]);
            i++;
        }
    }

    // Possible Look & Feels
    private String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    private String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
    private String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    private String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    // The current Look & Feel
    private String currentLookAndFeel = metal;

    // List of demos
    private Vector demosVector = new Vector();

    // The preferred size of the demo
    private int PREFERRED_WIDTH = 680;
    private int PREFERRED_HEIGHT = 640;

    // Box spacers
    private Dimension HGAP = new Dimension(1, 5);
    private Dimension VGAP = new Dimension(5, 1);

    // Resource bundle for internationalized and accessible text
    private ResourceBundle bundle = null;

    private JDialog aboutBox = null;

    // A place to hold on to the visible demo
    private DemoModule currentDemo = null;
    private JPanel demoPanel = null;

    // Tool Bar
    private ToggleButtonToolBar toolbar = null;
    private ButtonGroup toolbarGroup = new ButtonGroup();

    // Menus
    private JMenuBar menuBar = null;
    private JMenu themesMenu = null;
    private ButtonGroup lafMenuGroup = new ButtonGroup();
    private ButtonGroup themesMenuGroup = new ButtonGroup();

    private static JFrame frame = null;

    private CoKitApplet applet = null;

    // The tab pane that holds the demo
    private JTabbedPane tabbedPane = null;

    // contentPane cache, saved from the applet or application frame
    Container contentPane = null;


    /**
     * CoKit Constructor
     */

    public CoKit(CoKitApplet applet)
    {
        // Note that the applet may null if this is started as an application
        this.applet = applet;

        setLayout(new BorderLayout());

        // set the preferred size of the demo
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        initializeDemo();
        preloadFirstDemo();

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                showCoKit();
            }
        });

        // Start loading the rest of the demo in the background
        DemoLoadThread demoLoader = new DemoLoadThread(this);
        demoLoader.start();
    }


    /**
     * CoKit Main. Called only if we're an application, not an applet.
     */

    public static void main(String[] args)
    {
        frame = createFrame();
        CoKit cokit = new CoKit(null);
    }

    // *******************************************************
    // *************** Demo Loading Methods ******************
    // *******************************************************



    public void initializeDemo()
    {
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);

        menuBar = createMenus();
        top.add(menuBar, BorderLayout.NORTH);

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BorderLayout());
        toolbar = new ToggleButtonToolBar();
        toolbarPanel.add(toolbar, BorderLayout.CENTER);
        top.add(toolbarPanel, BorderLayout.SOUTH);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.getModel().addChangeListener(new TabListener());

        demoPanel = new JPanel();
        demoPanel.setLayout(new BorderLayout());
        demoPanel.setBorder(new EtchedBorder());
        tabbedPane.addTab("Hi There!", demoPanel);
    }

    DemoModule currentTabDemo = null;

    class TabListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            SingleSelectionModel model = (SingleSelectionModel)e.getSource();
            boolean srcSelected = model.getSelectedIndex() == 1;
            if (currentTabDemo != currentDemo && srcSelected)
            {
                repaint();
            }
            if (currentTabDemo != currentDemo && srcSelected)
            {
                currentTabDemo = currentDemo;
            }
        }
    }


    /**
     * Create menus
     */
    public JMenuBar createMenus()
    {
        JMenuItem mi;
        // ***** create the menubar ****
        JMenuBar menuBar = new JMenuBar();
        menuBar.getAccessibleContext().setAccessibleName(getString("MenuBar.accessible_description"));

        // ***** create File menu
        JMenu fileMenu = (JMenu)menuBar.add(new JMenu(getString("FileMenu.file_label")));
        fileMenu.setMnemonic(getMnemonic("FileMenu.file_mnemonic"));
        fileMenu.getAccessibleContext().setAccessibleDescription(getString("FileMenu.accessible_description"));

        createMenuItem(fileMenu, "FileMenu.about_label", "FileMenu.about_mnemonic", "FileMenu.about_accessible_description", new AboutAction(this));

        fileMenu.addSeparator();

        createMenuItem(fileMenu, "FileMenu.open_label", "FileMenu.open_mnemonic", "FileMenu.open_accessible_description", null);

        createMenuItem(fileMenu, "FileMenu.save_label", "FileMenu.save_mnemonic", "FileMenu.save_accessible_description", null);

        createMenuItem(fileMenu, "FileMenu.save_as_label", "FileMenu.save_as_mnemonic", "FileMenu.save_as_accessible_description", null);


        if (!isApplet())
        {
            fileMenu.addSeparator();

            createMenuItem(fileMenu, "FileMenu.exit_label", "FileMenu.exit_mnemonic", "FileMenu.exit_accessible_description", new ExitAction(this));
        }


        // ***** create laf switcher menu
        JMenu lafMenu = (JMenu)menuBar.add(new JMenu(getString("LafMenu.laf_label")));
        lafMenu.setMnemonic(getMnemonic("LafMenu.laf_mnemonic"));
        lafMenu.getAccessibleContext().setAccessibleDescription(getString("LafMenu.laf_accessible_description"));

        mi = createLafMenuItem(lafMenu, "LafMenu.java_label", "LafMenu.java_mnemonic", "LafMenu.java_accessible_description", metal);
        mi.setSelected(true); // this is the default l&f

        createLafMenuItem(lafMenu, "LafMenu.mac_label", "LafMenu.mac_mnemonic", "LafMenu.mac_accessible_description", mac);

        createLafMenuItem(lafMenu, "LafMenu.motif_label", "LafMenu.motif_mnemonic", "LafMenu.motif_accessible_description", motif);

        createLafMenuItem(lafMenu, "LafMenu.windows_label", "LafMenu.windows_mnemonic", "LafMenu.windows_accessible_description", windows);

        // ***** create themes menu
        themesMenu = (JMenu)menuBar.add(new JMenu(getString("ThemesMenu.themes_label")));
        themesMenu.setMnemonic(getMnemonic("ThemesMenu.themes_mnemonic"));
        themesMenu.getAccessibleContext().setAccessibleDescription(getString("ThemesMenu.themes_accessible_description"));

        mi = createThemesMenuItem(themesMenu, "ThemesMenu.default_label", "ThemesMenu.default_mnemonic", "ThemesMenu.default_accessible_description",
                new DefaultMetalTheme());
        mi.setSelected(true); // This is the default theme

        createThemesMenuItem(themesMenu, "ThemesMenu.aqua_label", "ThemesMenu.aqua_mnemonic", "ThemesMenu.aqua_accessible_description",
                new AquaTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.charcoal_label", "ThemesMenu.charcoal_mnemonic", "ThemesMenu.charcoal_accessible_description",
                new CharcoalTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.contrast_label", "ThemesMenu.contrast_mnemonic", "ThemesMenu.contrast_accessible_description",
                new ContrastTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.emerald_label", "ThemesMenu.emerald_mnemonic", "ThemesMenu.emerald_accessible_description",
                new EmeraldTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.ruby_label", "ThemesMenu.ruby_mnemonic", "ThemesMenu.ruby_accessible_description",
                new RubyTheme());

        return menuBar;
    }

    /**
     * Creates a generic menu item
     */
    public JMenuItem createMenuItem(JMenu menu, String label, String mnemonic, String accessibleDescription, Action action)
    {
        JMenuItem mi = (JMenuItem)menu.add(new JMenuItem(getString(label)));
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(action);
        if (action == null)
        {
            mi.setEnabled(false);
        }
        return mi;
    }

    /**
     * Creates a JRadioButtonMenuItem for the Themes menu
     */
    public JMenuItem createThemesMenuItem(JMenu menu, String label, String mnemonic, String accessibleDescription, DefaultMetalTheme theme)
    {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem)menu.add(new JRadioButtonMenuItem(getString(label)));
        themesMenuGroup.add(mi);
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(new ChangeThemeAction(this, theme));

        return mi;
    }

    /**
     * Creates a JRadioButtonMenuItem for the Look and Feel menu
     */
    public JMenuItem createLafMenuItem(JMenu menu, String label, String mnemonic, String accessibleDescription, String laf)
    {
        JMenuItem mi = (JRadioButtonMenuItem)menu.add(new JRadioButtonMenuItem(getString(label)));
        lafMenuGroup.add(mi);
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(new ChangeLookAndFeelAction(this, laf));

        mi.setEnabled(isAvailableLookAndFeel(laf));

        return mi;
    }

    /**
     * Load the first demo. This is done separately from the remaining demos so that we can get CoKit up and available
     * to the user quickly.
     */
    public void preloadFirstDemo()
    {
        DemoModule demo = addDemo(new FrontPage(this));
        setDemo(demo);
    }


    /**
     * Add a demo to the toolbar
     */
    public DemoModule addDemo(DemoModule demo)
    {
        demosVector.addElement(demo);
        // do the following on the gui thread
        SwingUtilities.invokeLater(new SwingSetRunnable(this, demo)
        {
            public void run()
            {
                SwitchToDemoAction action = new SwitchToDemoAction(cokit, (DemoModule)obj);
                JToggleButton tb = cokit.getToolBar().addToggleButton(action);
                cokit.getToolBarGroup().add(tb);
                if (cokit.getToolBarGroup().getSelection() == null)
                {
                    tb.setSelected(true);
                }
                tb.setText(null);
                tb.setToolTipText(((DemoModule)obj).getToolTip());


            }
        });
        return demo;
    }


    /**
     * Sets the current demo
     */
    public void setDemo(DemoModule demo)
    {
        currentDemo = demo;

        demoPanel.removeAll();
        demoPanel.add(demo.getDemoPanel(), BorderLayout.CENTER);

        tabbedPane.setSelectedIndex(0);
        tabbedPane.setTitleAt(0, demo.getName());
        tabbedPane.setToolTipTextAt(0, demo.getToolTip());
    }


    /**
     * Bring up the CoKit demo by showing the frame (only applicable if coming up as an application,not an applet);
     */
    public void showCoKit()
    {
        if (!isApplet() && getFrame() != null)
        {
            // put cokit in a frame and show it
            JFrame f = getFrame();
            f.setTitle(getString("Frame.title"));
            f.getContentPane().add(this, BorderLayout.CENTER);
            f.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            getFrame().setLocation(screenSize.width / 2 - f.getSize().width / 2, screenSize.height / 2 - f.getSize().height / 2);
            getFrame().show();
        }
    }



    // *******************************************************
    // ****************** Utility Methods ********************
    // *******************************************************

    /**
     * Loads a demo from a classname
     */
    void loadDemo(String classname)
    {
        DemoModule demo = null;
        try
        {
            Class demoClass = Class.forName(classname); // Return class object associated with the class
            Constructor demoConstructor = demoClass.getConstructor(new Class[]
                { CoKit.class });
            demo = (DemoModule)demoConstructor.newInstance(new Object[]
                { this });
            addDemo(demo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error occurred loading demo: " + classname);
        }
    }

    /**
     * A utility function that layers on top of the LookAndFeel's isSupportedLookAndFeel() method. Returns true if the
     * LookAndFeel is supported. Returns false if the LookAndFeel is not supported and/or if there is any kind of error
     * checking if the LookAndFeel is supported.
     * 
     * The L&F menu will use this method to detemine whether the various L&F options should be active or inactive.
     * 
     */
    protected boolean isAvailableLookAndFeel(String laf)
    {
        try
        {
            Class lnfClass = Class.forName(laf);
            LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
            return newLAF.isSupportedLookAndFeel();
        }
        catch (Exception e)
        { // If ANYTHING weird happens, return false
            return false;
        }
    }


    /**
     * Determines if this is an applet or application
     */
    public boolean isApplet()
    {
        return (applet != null);
    }

    /**
     * Returns the applet instance
     */
    public CoKitApplet getApplet()
    {
        return applet;
    }


    /**
     * Returns the frame instance
     */
    public JFrame getFrame()
    {
        return frame;
    }

    /**
     * Returns the menubar
     */
    public JMenuBar getMenuBar()
    {
        return menuBar;
    }

    /**
     * Returns the toolbar
     */
    public ToggleButtonToolBar getToolBar()
    {
        return toolbar;
    }

    /**
     * Returns the toolbar button group
     */
    public ButtonGroup getToolBarGroup()
    {
        return toolbarGroup;
    }

    /**
     * Returns the content pane wether we're in an applet or application
     */
    public Container getContentPane()
    {
        if (contentPane == null)
        {
            if (getFrame() != null)
            {
                contentPane = getFrame().getContentPane();
            }
            else if (getApplet() != null)
            {
                contentPane = getApplet().getContentPane();
            }
        }
        return contentPane;
    }

    /**
     * Create a frame for CoKit to reside in if brought up as an application.
     */
    public static JFrame createFrame()
    {
        JFrame frame = new JFrame();
        WindowListener l = new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);
        return frame;
    }



    /**
     * This method returns a string from the demo's resource bundle.
     */
    public String getString(String key)
    {
        String value = null;
        try
        {
            value = getResourceBundle().getString(key);
        }
        catch (MissingResourceException e)
        {
            System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
        }
        if (value == null)
        {
            value = "Could not find resource: " + key + "  ";
        }
        return value;
    }

    /**
     * Returns the resource bundle associated with this demo. Used to get accessable and internationalized strings.
     */
    public ResourceBundle getResourceBundle()
    {
        if (bundle == null)
        {
            bundle = ResourceBundle.getBundle("resources.cokit");
        }
        return bundle;
    }

    /**
     * Returns a mnemonic from the resource bundle. Typically used as keyboard shortcuts in menu items.
     */
    public char getMnemonic(String key)
    {
        return (getString(key)).charAt(0);
    }

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public ImageIcon createImageIcon(String filename, String description)
    {
        String path = "/resources/images/" + filename;
        return new ImageIcon(getClass().getResource(path));
    }



    /**
     * Stores the current L&F, and calls updateLookAndFeel, below
     */
    public void setLookAndFeel(String laf)
    {
        if (currentLookAndFeel != laf)
        {
            currentLookAndFeel = laf;
            themesMenu.setEnabled(laf == metal);
            updateLookAndFeel();
        }
    }

    /**
     * Sets the current L&F on each demo module
     */
    public void updateLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(currentLookAndFeel);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (Exception ex)
        {
            System.out.println("Failed loading L&F: " + currentLookAndFeel);
            System.out.println(ex);
        }

        // lazily update update the UI's for the remaining demos
        for (int i = 0; i < demosVector.size(); i++)
        {
            DemoModule demo = (DemoModule)demosVector.elementAt(i);
            if (currentDemo != demo)
            {
                // do the following on the gui thread
                SwingUtilities.invokeLater(new SwingSetRunnable(this, demo)
                {
                    public void run()
                    {
                        SwingUtilities.updateComponentTreeUI(((DemoModule)obj).getDemoPanel());
                    }
                });
            }
        }
    }



    // *******************************************************
    // ************** ToggleButtonToolbar *****************
    // *******************************************************
    static Insets zeroInsets = new Insets(1, 1, 1, 1);

    protected class ToggleButtonToolBar extends JToolBar
    {
        public ToggleButtonToolBar()
        {
            super();
        }

        JToggleButton addToggleButton(Action a)
        {
            JToggleButton tb = new JToggleButton((String)a.getValue(Action.NAME), (Icon)a.getValue(Action.SMALL_ICON));
            tb.setMargin(zeroInsets);
            tb.setText(null);
            tb.setEnabled(a.isEnabled());
            tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
            tb.setAction(a);
            add(tb);
            return tb;
        }
    }

    // *******************************************************
    // ****************** Runnables ***********************
    // *******************************************************

    /**
     * Generic CoKit runnable. This is intended to run on the AWT gui event thread so as not to muck things up by doing
     * gui work off the gui thread. Accepts a CoKit and an Object as arguments, which gives subtypes of this class the
     * two "must haves" needed in most runnables for this demo.
     */
    class SwingSetRunnable implements Runnable
    {
        protected CoKit cokit;
        protected Object obj;

        public SwingSetRunnable(CoKit cokit, Object obj)
        {
            this.cokit = cokit;
            this.obj = obj;
        }

        public void run()
        {
        }
    }


    // *******************************************************
    // ******************** Actions ***********************
    // *******************************************************

    public class SwitchToDemoAction extends AbstractAction
    {
        CoKit cokit;
        DemoModule demo;

        public SwitchToDemoAction(CoKit cokit, DemoModule demo)
        {
            super(demo.getName(), demo.getIcon());
            this.cokit = cokit;
            this.demo = demo;
        }

        public void actionPerformed(ActionEvent e)
        {
            cokit.setDemo(demo);
        }
    }

    class OkAction extends AbstractAction
    {
        JDialog aboutBox;

        protected OkAction(JDialog aboutBox)
        {
            super("OkAction");
            this.aboutBox = aboutBox;
        }

        public void actionPerformed(ActionEvent e)
        {
            aboutBox.setVisible(false);
        }
    }

    class ChangeLookAndFeelAction extends AbstractAction
    {
        CoKit cokit;
        String laf;

        protected ChangeLookAndFeelAction(CoKit cokit, String laf)
        {
            super("ChangeTheme");
            this.cokit = cokit;
            this.laf = laf;
        }

        public void actionPerformed(ActionEvent e)
        {
            cokit.setLookAndFeel(laf);
        }
    }

    class ChangeThemeAction extends AbstractAction
    {
        CoKit cokit;
        DefaultMetalTheme theme;

        protected ChangeThemeAction(CoKit cokit, DefaultMetalTheme theme)
        {
            super("ChangeTheme");
            this.cokit = cokit;
            this.theme = theme;
        }

        public void actionPerformed(ActionEvent e)
        {
            MetalLookAndFeel.setCurrentTheme(theme);
            cokit.updateLookAndFeel();
        }
    }

    class ExitAction extends AbstractAction
    {
        CoKit cokit;

        protected ExitAction(CoKit cokit)
        {
            super("ExitAction");
            this.cokit = cokit;
        }

        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }



    class AboutAction extends AbstractAction
    {
        CoKit cokit;

        protected AboutAction(CoKit cokit)
        {
            super("AboutAction");
            this.cokit = cokit;
        }

        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(
                    cokit.getFrame(),
                    "Engineering Team\n  Abhijeet Iraj,Nilesh Shirke\n  Gajanan Kori,Amol Chougule\n T.E.(Computer)\n     Walchand College Of Engg.,Sangli \n Copyrights (C) 2002",
                    "About Us!", JOptionPane.WARNING_MESSAGE);
        }
    }

    // *******************************************************
    // ********************** Misc *************************
    // *******************************************************

    class DemoLoadThread extends Thread
    {
        CoKit cokit;

        public DemoLoadThread(CoKit cokit)
        {
            this.cokit = cokit;
        }

        public void run()
        {
            cokit.loadDemos();
        }
    }

    public static JPanel createHorizontalPanel(boolean threeD)
    {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        if (threeD)
        {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    public static JPanel createVerticalPanel(boolean threeD)
    {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        if (threeD)
        {
            p.setBorder(loweredBorder);
        }
        return p;
    }

}
