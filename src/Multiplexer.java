import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


/**
 * Multiplexer.java
 * 
 * @version 1.1 05/01/2002
 * @author Abhijeet Iraj
 */


public class Multiplexer extends DemoModule
{

    Icon icon;
    JDesktopPane desktop = null;
    int muxChoice = 1, varChoice = 4;
    muxDesignPanel temp;
    int layer = 0;
    int choice = 1, noOfVariables = 2;
    Stack stackTruth;
    Stack stackPost;
    boolean und = true;
    int charOfPost = 0;
    Character topSymbol = new Character('~');
    int postPos = 0;
    int truthTable[] = new int[16];

    JPanel p;
    muxDesignPanel muxdesign;

    JRadioButton Mux2_1, Mux4_1, Mux8_1, Mux16_1;

    public Integer FIRST_FRAME_LAYER = new Integer(1);
    public Integer DEMO_FRAME_LAYER = new Integer(2);
    public Integer PALETTE_LAYER = new Integer(3);

    public int FRAME0_X = 15;
    public int FRAME0_Y = 280;

    public int FRAME0_WIDTH = 320;
    public int FRAME0_HEIGHT = 230;

    public int FRAME_WIDTH = 225;
    public int FRAME_HEIGHT = 150;

    public int PALETTE_X = 250;
    public int PALETTE_Y = 80;

    public int PALETTE_WIDTH = 475;
    public int PALETTE_HEIGHT = 325;


    JTextField windowTitleField = null;
    JLabel windowTitleLabel = null;

    JButton design;


    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        Multiplexer demo = new Multiplexer(null);
        demo.mainImpl();
    }

    /**
     * Multiplexer Constructor
     */
    public Multiplexer(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "Multiplexer", "toolbar/Mux.gif");

        // Create the desktop pane
        desktop = new JDesktopPane();
        getDemoPanel().add(desktop, BorderLayout.CENTER);

        // Create the "frame maker" palette
        createInternalFramePalette();

    }


    /* Start of Minimaization of Logical Function */

    public boolean prcd(Character topSymbol, char symb)
    {
        char topsymb = topSymbol.charValue();

        if ((topsymb == '(') && ((symb == '~') || (symb == '&') || (symb == '+') || (symb == '(') || (symb == ')')))
            return false;
        if (((topsymb == '~') || (topsymb == '&') || (topsymb == '+') || (topsymb == '(')) && (symb == '('))
            return false;
        if (((topsymb == '~') || (topsymb == '&') || (topsymb == '+') || (topsymb == ')')) && (symb == ')'))
            return true;
        if ((topsymb == '~') && ((symb == '&') || (symb == '+')))
            return true;
        if ((topsymb == '&') && (symb == '+'))
            return true;
        if ((topsymb == '+') && ((symb == '~') || (symb == '&')))
            return false;
        if ((topsymb == '&') && (symb == '~'))
            return false;
        if ((topsymb == '+') && (symb == '+'))
            return true;
        if ((topsymb == '&') && (symb == '&'))
            return true;
        if ((topsymb == '~') && (symb == '~'))
            return true;
        return false;
    }


    public void popandTest()
    {
        if (stackPost.empty())
        {
            und = true;
            return;
        }
        und = false;
        topSymbol = (Character)stackPost.pop();
        return;
    }

    public String infixToPostfix(String infix, String postfix)
    { // 1
        stackPost = new Stack();
        char symb = 'A';
        char infx[] = new char[300];
        char postfx[] = new char[300];
        infix.getChars(0, infix.length(), infx, 0);

        for (int charPos = 0; charPos < infix.length(); charPos++)
        {
            symb = infx[charPos];
            if (symb == 'A' || symb == 'B' || symb == 'C' || symb == 'D')
                postfx[postPos++] = symb;
            else
            {
                popandTest();
                while (!und && prcd(topSymbol, symb))
                {
                    char ch = topSymbol.charValue();
                    postfx[postPos++] = ch;
                    popandTest();
                } // while
                if (!und)
                {
                    stackPost.push((Character)topSymbol);
                }
                if (und || (symb != ')'))
                {
                    stackPost.push(new Character(symb));
                }
                else
                    topSymbol = (Character)stackPost.pop();
            } // else
        } // for

        while (!stackPost.empty())
        { // 3
            Character temp = new Character('~');
            temp = (Character)stackPost.pop();
            char ch = temp.charValue();
            postfx[postPos++] = ch;
        } // while

        postfx[postPos] = '\0';
        String post = new String(postfx);

        return post;
    }

    public int process(char ch, int f, int s)
    {
        switch (ch)
        {
            case '&':
                if (f == 1 && s == 1)
                    return (1);
                else
                    return (0);
            case '+':
                if (f == 0 && s == 0)
                    return (0);
                else
                    return (1);
            default:
                JOptionPane.showMessageDialog(getDemoPanel(), "PROCESS-Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        return 2;
    }


    public void findTruthTable(String equation, int noOfVariables)
    {
        String infix = new String(equation);
        String postfix = new String();

        int i = 0, n;
        try
        {
            postfix = infixToPostfix(infix, postfix); // convert function from infix to postfix
        }
        catch (EmptyStackException e)
        {
            JOptionPane.showMessageDialog(getDemoPanel(), "postfix Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }


        int k = 0;
        stackTruth = new Stack();
        char postfx[] = new char[postPos];
        postfix.getChars(0, postPos, postfx, 0);
        System.out.println(postfx);

        switch (noOfVariables)
        {
            case 1:
                for (i = 0; i < 2; i++)
                {
                    for (n = 0; n < postPos; n++)
                    {
                        char ch = postfx[n];

                        if (ch == 'A')
                            stackTruth.push(new Integer(i));
                        else if (ch == '~')
                        {
                            Integer temp = new Integer(1);
                            temp = (Integer)stackTruth.pop();
                            int val = temp.intValue();
                            if (val == 1)
                                stackTruth.push(new Integer(0));
                            if (val == 0)
                                stackTruth.push(new Integer(1));
                        }
                        else
                        {
                            Integer temp = new Integer(1);
                            temp = (Integer)stackTruth.pop();
                            int second = temp.intValue();
                            temp = (Integer)stackTruth.pop();
                            int first = temp.intValue();
                            int val = process(ch, first, second);
                            stackTruth.push(new Integer(val));
                        } // else
                    }
                    // for3
                    Integer temp = new Integer(1);
                    temp = (Integer)stackTruth.pop();
                    int val = temp.intValue();
                    truthTable[k++] = val;
                } // for1

                break;
            case 2:
                for (i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (n = 0; n < postPos; n++)
                        {
                            char ch = postfx[n];

                            if (ch == 'A')
                                stackTruth.push(new Integer(i));
                            else if (ch == 'B')
                                stackTruth.push(new Integer(j));
                            else if (ch == '~')
                            {
                                Integer temp = new Integer(1);
                                temp = (Integer)stackTruth.pop();
                                int val = temp.intValue();
                                if (val == 1)
                                    stackTruth.push(new Integer(0));
                                if (val == 0)
                                    stackTruth.push(new Integer(1));
                            }
                            else
                            {
                                Integer temp = new Integer(1);
                                temp = (Integer)stackTruth.pop();
                                int second = temp.intValue();
                                temp = (Integer)stackTruth.pop();
                                int first = temp.intValue();
                                int val = process(ch, first, second);
                                stackTruth.push(new Integer(val));
                            } // else
                        }
                        // for3
                        Integer temp = new Integer(1);
                        temp = (Integer)stackTruth.pop();
                        int val = temp.intValue();
                        truthTable[k++] = val;
                    } // for2
                } // for1

                break;
            case 3:
                for (i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int l = 0; l < 2; l++)
                        {
                            for (n = 0; n < postPos; n++)
                            {
                                char ch = postfx[n];

                                if (ch == 'A')
                                    stackTruth.push(new Integer(i));
                                else if (ch == 'B')
                                    stackTruth.push(new Integer(j));
                                else if (ch == 'C')
                                    stackTruth.push(new Integer(l));
                                else if (ch == '~')
                                {
                                    Integer temp = new Integer(1);
                                    temp = (Integer)stackTruth.pop();
                                    int val = temp.intValue();
                                    if (val == 1)
                                        stackTruth.push(new Integer(0));
                                    if (val == 0)
                                        stackTruth.push(new Integer(1));
                                }
                                else
                                {
                                    Integer temp = new Integer(1);
                                    temp = (Integer)stackTruth.pop();
                                    int second = temp.intValue();
                                    temp = (Integer)stackTruth.pop();
                                    int first = temp.intValue();
                                    int val = process(ch, first, second);
                                    stackTruth.push(new Integer(val));
                                } // else
                            } // for3
                            Integer temp = new Integer(1);
                            temp = (Integer)stackTruth.pop();
                            int val = temp.intValue();
                            truthTable[k++] = val;
                        } // for2
                    } // for1
                }
                break;

            case 4:

                for (i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int l = 0; l < 2; l++)
                        {
                            for (int m = 0; m < 2; m++)
                            {
                                for (n = 0; n < postPos; n++)
                                {
                                    char ch = postfx[n];

                                    if (ch == 'A')
                                        stackTruth.push(new Integer(i));
                                    else if (ch == 'B')
                                        stackTruth.push(new Integer(j));
                                    else if (ch == 'C')
                                        stackTruth.push(new Integer(l));
                                    else if (ch == 'D')
                                        stackTruth.push(new Integer(m));
                                    else if (ch == '~')
                                    {
                                        Integer temp = new Integer(1);
                                        temp = (Integer)stackTruth.pop();
                                        int val = temp.intValue();
                                        if (val == 1)
                                            stackTruth.push(new Integer(0));
                                        if (val == 0)
                                            stackTruth.push(new Integer(1));
                                    }
                                    else
                                    {
                                        Integer temp = new Integer(1);
                                        temp = (Integer)stackTruth.pop();
                                        int second = temp.intValue();
                                        temp = (Integer)stackTruth.pop();
                                        int first = temp.intValue();
                                        int val = process(ch, first, second);
                                        stackTruth.push(new Integer(val));
                                    } // else

                                } // for3
                                Integer temp = new Integer(1);
                                temp = (Integer)stackTruth.pop();
                                int val = temp.intValue();
                                truthTable[k++] = val;
                            } // for2
                        } // for1
                    }
                }
                break;

            default:
                // System.exit(0);
        } // switch

    }


    /* End of Minimaization of Logical Function */



    /**
     * Create an internal frame
     */
    public JInternalFrame createInternalFrame(Integer layer, int width, int height)
    {
        JInternalFrame jif;
        String equation;
        equation = windowTitleField.getText(); // set properties

        jif = new JInternalFrame();

        JMenuBar jmenubar = new JMenuBar();
        JMenu jmenu = new JMenu("Select...");
        JMenuItem item1 = new JMenuItem("IC Color");
        JMenuItem item2 = new JMenuItem("Print");
        item1.addActionListener(new ShowFrameAction(this));
        item2.addActionListener(new ShowFrameAction(this));
        jmenu.add(item1);
        jmenu.add(item2);
        jmenubar.add(jmenu);


        if (equation.equals(""))
            return jif;
        else
        {
            switch (muxChoice)
            {
                case 1:
                    try
                    {
                        findTruthTable(equation, varChoice);
                    }
                    catch (EmptyStackException e)
                    {
                        JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return jif;
                    }
                    muxdesign = new muxDesignPanel(equation, truthTable, varChoice, muxChoice);
                    jif.setTitle("2:1 Multiplexer");
                    break;
                case 2:
                    try
                    {
                        findTruthTable(equation, varChoice);
                    }
                    catch (EmptyStackException e)
                    {
                        JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return jif;
                    }
                    muxdesign = new muxDesignPanel(equation, truthTable, varChoice, muxChoice);
                    jif.setTitle("4:1 Multiplexer");
                    break;
                case 3:
                    try
                    {
                        findTruthTable(equation, varChoice);
                    }
                    catch (EmptyStackException e)
                    {
                        JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return jif;
                    }
                    muxdesign = new muxDesignPanel(equation, truthTable, varChoice, muxChoice);

                    jif.setTitle("8:1 Multiplexer");
                    break;
                case 4:
                    try
                    {
                        findTruthTable(equation, varChoice);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return jif;
                    }
                    muxdesign = new muxDesignPanel(equation, truthTable, varChoice, muxChoice);
                    jif.setTitle("16:1 Multiplexer");
                    break;
            }
        }

        p = new JPanel();
        p.setLayout(new BorderLayout());

        p.add(muxdesign, BorderLayout.CENTER);

        JScrollPane scroller = new JScrollPane();
        scroller.getViewport().add(p);

        JScrollBar vsb = scroller.getVerticalScrollBar();
        JScrollBar hsb = scroller.getHorizontalScrollBar();

        vsb.setValue(50);
        hsb.setValue(50);

        jif.getContentPane().add(scroller);
        jif.setClosable(true);
        jif.setMaximizable(true);
        jif.setIconifiable(true);
        jif.setResizable(true);
        jif.setVisible(true);
        jif.setBounds(0, 0, width, height);

        jif.setJMenuBar(jmenubar);

        desktop.add(jif, layer);
        jif.show();
        return jif;

    }


    public JInternalFrame createInternalFramePalette()
    {
        JInternalFrame palette = new JInternalFrame(getString("GateLevelGates.palette_label"));
        palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        palette.getContentPane().setLayout(new BorderLayout());
        palette.setBounds(PALETTE_X, PALETTE_Y, PALETTE_WIDTH, PALETTE_HEIGHT);
        palette.setResizable(true);
        palette.setIconifiable(true);
        palette.setBackground(new Color(195, 195, 195));
        desktop.add(palette, PALETTE_LAYER);

        // *************************************
        // * Create create frame maker buttons *
        // *************************************


        // Create Radiobuttons for number of variables

        JPanel temp = new JPanel();
        temp.setBackground(new Color(195, 195, 195));
        ButtonGroup group = new ButtonGroup();

        JRadioButton oneVariable;
        String description = new String("Multiplexer buttons");
        oneVariable = new JRadioButton("1 Variable", createImageIcon("buttons/rb.gif", description));
        oneVariable.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        oneVariable.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        oneVariable.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        oneVariable.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        oneVariable.setFocusPainted(false);
        oneVariable.setBorderPainted(false);
        oneVariable.setContentAreaFilled(false);
        oneVariable.setMargin(new Insets(0, 0, 0, 0));
        group.add(oneVariable);

        JRadioButton twoVariable;
        twoVariable = new JRadioButton("2 Variable", createImageIcon("buttons/rb.gif", description));
        twoVariable.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        twoVariable.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        twoVariable.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        twoVariable.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        twoVariable.setFocusPainted(false);
        twoVariable.setBorderPainted(false);
        twoVariable.setContentAreaFilled(false);
        twoVariable.setMargin(new Insets(0, 0, 0, 0));
        group.add(twoVariable);

        JRadioButton threeVariable;
        threeVariable = new JRadioButton("3 Variable", createImageIcon("buttons/rb.gif", description));
        threeVariable.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        threeVariable.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        threeVariable.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        threeVariable.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        threeVariable.setFocusPainted(false);
        threeVariable.setBorderPainted(false);
        threeVariable.setContentAreaFilled(false);
        threeVariable.setMargin(new Insets(0, 0, 0, 0));
        group.add(threeVariable);

        JRadioButton fourVariable;
        fourVariable = new JRadioButton("4 Variable", createImageIcon("buttons/rb.gif", description));
        fourVariable.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        fourVariable.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        fourVariable.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        fourVariable.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        fourVariable.setFocusPainted(false);
        fourVariable.setBorderPainted(false);
        fourVariable.setContentAreaFilled(false);
        fourVariable.setMargin(new Insets(0, 0, 0, 0));
        fourVariable.setSelected(true);
        group.add(fourVariable);

        // add variable actions
        oneVariable.addActionListener(new ShowFrameAction(this));
        twoVariable.addActionListener(new ShowFrameAction(this));
        threeVariable.addActionListener(new ShowFrameAction(this));
        fourVariable.addActionListener(new ShowFrameAction(this));

        // add Radiobuttons to panel
        JPanel p = new JPanel();
        p.setBackground(new Color(195, 195, 195));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel buttons1 = createVerticalPanel(false);// JPanel();
        buttons1.setBackground(new Color(195, 195, 195));
        buttons1.setBorder(new TitledBorder(null, "No.of variables", TitledBorder.LEFT, TitledBorder.TOP));

        buttons1.add(oneVariable);
        buttons1.add(Box.createRigidArea(VGAP10));
        buttons1.add(twoVariable);
        buttons1.add(Box.createRigidArea(VGAP10));
        buttons1.add(threeVariable);
        buttons1.add(Box.createRigidArea(VGAP10));
        buttons1.add(fourVariable);

        buttons1.add(Box.createRigidArea(HGAP20));

        p.add(Box.createRigidArea(VGAP10));
        p.add(buttons1);
        p.add(Box.createRigidArea(VGAP15));


        // Create Radiobuttons for gate Selection

        ButtonGroup group1 = new ButtonGroup();

        Mux2_1 = new JRadioButton(" 2:1 Mux", createImageIcon("buttons/rb.gif", description));
        Mux2_1.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        Mux2_1.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        Mux2_1.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        Mux2_1.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        Mux2_1.setFocusPainted(false);
        Mux2_1.setBorderPainted(false);
        Mux2_1.setContentAreaFilled(false);
        Mux2_1.setMargin(new Insets(0, 0, 0, 0));
        Mux2_1.setSelected(true);
        group1.add(Mux2_1);

        Mux4_1 = new JRadioButton(" 4:1 Mux", createImageIcon("buttons/rb.gif", description));
        Mux4_1.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        Mux4_1.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        Mux4_1.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        Mux4_1.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        Mux4_1.setFocusPainted(false);
        Mux4_1.setBorderPainted(false);
        Mux4_1.setContentAreaFilled(false);
        Mux4_1.setMargin(new Insets(0, 0, 0, 0));
        group1.add(Mux4_1);

        Mux8_1 = new JRadioButton(" 8:1 Mux", createImageIcon("buttons/rb.gif", description));
        Mux8_1.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        Mux8_1.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        Mux8_1.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        Mux8_1.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        Mux8_1.setFocusPainted(false);
        Mux8_1.setBorderPainted(false);
        Mux8_1.setContentAreaFilled(false);
        Mux8_1.setMargin(new Insets(0, 0, 0, 0));
        group1.add(Mux8_1);

        Mux16_1 = new JRadioButton(" 16:1 Mux", createImageIcon("buttons/rb.gif", description));
        Mux16_1.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        Mux16_1.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        Mux16_1.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        Mux16_1.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        Mux16_1.setFocusPainted(false);
        Mux16_1.setBorderPainted(false);
        Mux16_1.setContentAreaFilled(false);
        Mux16_1.setMargin(new Insets(0, 0, 0, 0));
        group1.add(Mux16_1);

        // add gate selection actions
        Mux2_1.addActionListener(new ShowFrameAction(this));
        Mux4_1.addActionListener(new ShowFrameAction(this));
        Mux8_1.addActionListener(new ShowFrameAction(this));
        Mux16_1.addActionListener(new ShowFrameAction(this));

        // add Radiobuttons to panel
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(195, 195, 195));
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

        JPanel gates = createVerticalPanel(false);
        gates.setBackground(new Color(195, 195, 195));
        gates.setBorder(new TitledBorder(null, "Multiplexer Type ", TitledBorder.LEFT, TitledBorder.TOP));

        gates.add(Mux2_1);
        gates.add(Box.createRigidArea(HGAP20));
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(Mux4_1);
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(Mux8_1);
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(Mux16_1);

        p1.add(Box.createRigidArea(VGAP10));
        p1.add(gates);
        p1.add(Box.createRigidArea(VGAP15));

        // Create JBututtons
        JPanel selectPanel = createHorizontalPanel(false);// JPanel();
        selectPanel.setBackground(new Color(195, 195, 195));

        JButton logicalFunction = new JButton(createImageIcon("buttons/logicalmux1.gif", ""));
        logicalFunction.setPressedIcon(createImageIcon("buttons/logicalmux2.gif", ""));
        logicalFunction.setActionCommand("Logical Function");
        logicalFunction.setFocusPainted(false);
        logicalFunction.setBorderPainted(false);
        logicalFunction.setContentAreaFilled(false);
        logicalFunction.setMargin(new Insets(0, 0, 0, 0));
        logicalFunction.addActionListener(new ShowFrameAction(this));

        JButton kMap = new JButton(createImageIcon("buttons/truth1.gif", ""));
        kMap.setPressedIcon(createImageIcon("buttons/truth2.gif", ""));
        kMap.setActionCommand("K-Map");
        kMap.setFocusPainted(false);
        kMap.setBorderPainted(false);
        kMap.setContentAreaFilled(false);
        kMap.setMargin(new Insets(0, 0, 0, 0));
        kMap.addActionListener(new ShowFrameAction(this));

        JButton design = new JButton(createImageIcon("buttons/ic1.gif", ""));
        design.setPressedIcon(createImageIcon("buttons/ic2.gif", ""));
        design.setActionCommand("Design Ckt");
        design.setFocusPainted(false);
        design.setBorderPainted(false);
        design.setContentAreaFilled(false);
        design.setMargin(new Insets(0, 0, 0, 0));
        design.addActionListener(new ShowFrameAction(this));


        selectPanel.add(logicalFunction);
        selectPanel.add(Box.createRigidArea(HGAP5));
        selectPanel.add(design);
        selectPanel.add(Box.createRigidArea(HGAP5));
        selectPanel.add(kMap);


        temp.add(p);
        p1.add(Box.createRigidArea(HGAP25));
        temp.add(p1);
        temp.add(selectPanel);

        palette.getContentPane().add(temp);


        // ************************************
        // * Create Frame title textfield *
        // ************************************

        p = new JPanel()
        {
            Insets insets = new Insets(0, 0, 10, 0);

            public Insets getInsets()
            {
                return insets;
            }
        };

        p.setBackground(new Color(195, 195, 195));

        windowTitleField = new JTextField("");
        windowTitleField.setEnabled(false);
        windowTitleLabel = new JLabel("Logical Function");

        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleLabel, BorderLayout.NORTH);
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleField, BorderLayout.SOUTH);
        p.add(Box.createRigidArea(HGAP5));

        palette.getContentPane().add(p, BorderLayout.SOUTH);

        palette.show();

        return palette;

    }

    class ShowFrameAction extends AbstractAction
    {
        Multiplexer demo;


        public ShowFrameAction(Multiplexer demo)
        {
            this.demo = demo;

        }

        public void actionPerformed(ActionEvent e)
        {


            String str = e.getActionCommand();
            if (str.equals("IC Color"))
            {
                Color color = JColorChooser.showDialog(Multiplexer.this, "Select a new color..", Color.blue);
                muxdesign.setcolor(color);
            }
            if (str.equals("Print"))
            {
                try
                {
                    muxdesign.printPage();
                }
                catch (Exception print)
                {
                    System.out.println(print);
                }
            }
            if (str.equals(" 2:1 Mux"))
                muxChoice = 1;
            if (str.equals(" 4:1 Mux"))
                muxChoice = 2;
            if (str.equals(" 8:1 Mux"))
                muxChoice = 3;
            if (str.equals(" 16:1 Mux"))
                muxChoice = 4;
            if (str.equals("1 Variable"))
            {
                Mux16_1.setEnabled(false);
                Mux8_1.setEnabled(false);
                Mux4_1.setEnabled(false);
                Mux2_1.setEnabled(true);
                varChoice = 1;
            }
            if (str.equals("2 Variable"))
            {
                Mux16_1.setEnabled(false);
                Mux8_1.setEnabled(false);
                Mux4_1.setEnabled(true);
                Mux2_1.setEnabled(true);
                varChoice = 2;
            }
            if (str.equals("3 Variable"))
            {
                Mux16_1.setEnabled(false);
                Mux8_1.setEnabled(true);
                Mux4_1.setEnabled(true);
                Mux2_1.setEnabled(true);
                varChoice = 3;
            }
            if (str.equals("4 Variable"))
            {
                Mux16_1.setEnabled(true);
                Mux8_1.setEnabled(true);
                Mux4_1.setEnabled(true);
                Mux2_1.setEnabled(true);
                varChoice = 4;
            }
            if (str.equals("Logical Function"))
                windowTitleField.setEnabled(true);
            if (str.equals("K-Map"))
            {

                windowTitleField.setEnabled(false);
            }
            else if (str.equals("Design Ckt"))
            {

                demo.createInternalFrame(getDemoFrameLayer(), getFrameWidth(), getFrameHeight());
            }
        }
    }

    public int getFrameWidth()
    {
        return FRAME_WIDTH;
    }

    public int getFrameHeight()
    {
        return FRAME_HEIGHT;
    }

    public Integer getDemoFrameLayer()
    {
        return DEMO_FRAME_LAYER;
    }







}
