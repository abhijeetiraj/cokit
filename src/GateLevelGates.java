// CoKit-GateLeve Design

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * CoKit-GateLevel Design
 * 
 * @version 1.1 Date-12/30/2001
 * @author Abhijeet Iraj
 */

public class GateLevelGates extends DemoModule
{
    int windowCount = 0;
    Icon icon;
    JDesktopPane desktop = null;
    int gateChoice = 2, varChoice = 4;
    GateDesignPanel temp;
    int layer = 0;
    int choice = 1, noOfVariables = 2;
    Quine minimizeByQuine;
    char reducedExpression[] = new char[200];
    String stringExpression = new String("");;
    Stack stack = new Stack();
    boolean und = true;
    int charOfPost = 0;
    Character topSymbol = new Character('~');
    int postPos = 0;
    int truthTable[] = new int[16];
    JDialog kmap;
    JCheckBox t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15;
    JButton ok, cancel;

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

    public int PALETTE_WIDTH = 450;
    public int PALETTE_HEIGHT = 350;


    JTextField windowTitleField = null;
    JLabel windowTitleLabel = null;

    JButton design;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        GateLevelGates demo = new GateLevelGates(null);
        demo.mainImpl();
    }

    /**
     * GateLevelGates Constructor
     */

    public GateLevelGates(CoKit cokit)
    {
        super(cokit, "GateLevelGates", "toolbar/JDesktop.gif");


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

        if ((topsymb == '(') && ((symb == '~') || (symb == '.') || (symb == '+') || (symb == '(') || (symb == ')')))
            return false;
        if (((topsymb == '~') || (topsymb == '.') || (topsymb == '+') || (topsymb == '(')) && (symb == '('))
            return false;
        if (((topsymb == '~') || (topsymb == '.') || (topsymb == '+') || (topsymb == ')')) && (symb == ')'))
            return true;
        if ((topsymb == '~') && ((symb == '.') || (symb == '+')))
            return true;
        if ((topsymb == '.') && (symb == '+'))
            return true;
        if ((topsymb == '+') && ((symb == '~') || (symb == '.')))
            return false;
        if ((topsymb == '.') && (symb == '~'))
            return false;
        if ((topsymb == '+') && (symb == '+'))
            return true;
        if ((topsymb == '.') && (symb == '.'))
            return true;
        if ((topsymb == '~') && (symb == '~'))
            return true;
        return false;
    }


    public void popandTest()
    {
        if (stack.empty())
        {
            und = true;
            return;
        }
        und = false;
        topSymbol = (Character)stack.pop();
        return;
    }

    public String infixToPostfix(String infix, String postfix)
    { // 1
        char symb = 'A';
        char infx[] = new char[300];
        char postfx[] = new char[300];
        infix.getChars(0, infix.length(), infx, 0);
        postPos = 0;

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
                    stack.push((Character)topSymbol);
                }
                if (und || (symb != ')'))
                {
                    stack.push(new Character(symb));
                }
                else
                    topSymbol = (Character)stack.pop();
            } // else
        } // for

        while (!stack.empty())
        { // 3
            Character temp = new Character('~');
            temp = (Character)stack.pop();
            char ch = temp.charValue();
            postfx[postPos++] = ch;
        } // while

        postfx[postPos] = '\0';
        String post = new String(postfx);

        System.out.println(post + "abhya: " + postPos);

        return post;
    }

    public int process(char ch, int f, int s)
    {
        switch (ch)
        {
            case '.':
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
                JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);

        }
        return 2;
    }


    public void findTruthTable(String equation, int noOfVariables)
    {
        String infix = new String(equation);
        String postfix = new String();

        int i = 0, n;

        postfix = new String(infixToPostfix(infix, postfix));

        int k = 0;
        char postfx[] = new char[postPos];
        postfix.getChars(0, postPos, postfx, 0);

        switch (noOfVariables)
        {
            case 2:
                for (i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (n = 0; n < postPos; n++)
                        {
                            char ch = postfx[n];

                            if (ch == 'A')
                                stack.push(new Integer(i));
                            else if (ch == 'B')
                                stack.push(new Integer(j));
                            else if (ch == '~')
                            {
                                Integer temp = new Integer(1);
                                temp = (Integer)stack.pop();
                                int val = temp.intValue();
                                if (val == 1)
                                    stack.push(new Integer(0));
                                if (val == 0)
                                    stack.push(new Integer(1));
                            }
                            else
                            {
                                Integer temp = new Integer(1);
                                temp = (Integer)stack.pop();
                                int second = temp.intValue();
                                temp = (Integer)stack.pop();
                                int first = temp.intValue();
                                int val = process(ch, first, second);
                                stack.push(new Integer(val));
                            } // else
                        }
                        // for3
                        Integer temp = new Integer(1);
                        temp = (Integer)stack.pop();
                        int val = temp.intValue();
                        truthTable[k++] = val;
                    } // for2
                } // for1

                for (int z = 0; z < k; z++)
                    System.out.println(truthTable[z]);
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
                                    stack.push(new Integer(i));
                                else if (ch == 'B')
                                    stack.push(new Integer(j));
                                else if (ch == 'C')
                                    stack.push(new Integer(l));
                                else if (ch == '~')
                                {
                                    Integer temp = new Integer(1);
                                    temp = (Integer)stack.pop();
                                    int val = temp.intValue();
                                    if (val == 1)
                                        stack.push(new Integer(0));
                                    if (val == 0)
                                        stack.push(new Integer(1));
                                }
                                else
                                {
                                    Integer temp = new Integer(1);
                                    temp = (Integer)stack.pop();
                                    int second = temp.intValue();
                                    temp = (Integer)stack.pop();
                                    int first = temp.intValue();
                                    int val = process(ch, first, second);
                                    stack.push(new Integer(val));
                                } // else

                            } // for3
                            Integer temp = new Integer(1);
                            temp = (Integer)stack.pop();
                            int val = temp.intValue();
                            truthTable[k++] = val;
                        } // for2
                    } // for1
                }
                for (int z = 0; z < k; z++)
                    System.out.println(truthTable[z]);
                break;

            case 4:
                while (!stack.empty())
                {
                    Integer t = new Integer(1);
                    t = (Integer)stack.pop();
                }
                String pos = new String(postfx);
                System.out.println(pos);

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
                                        stack.push(new Integer(i));
                                    else if (ch == 'B')
                                        stack.push(new Integer(j));
                                    else if (ch == 'C')
                                        stack.push(new Integer(l));
                                    else if (ch == 'D')
                                        stack.push(new Integer(m));
                                    else if (ch == '~')
                                    {
                                        Integer temp = new Integer(1);
                                        temp = (Integer)stack.pop();
                                        int val = temp.intValue();
                                        if (val == 1)
                                            stack.push(new Integer(0));
                                        if (val == 0)
                                            stack.push(new Integer(1));
                                    }
                                    else
                                    {
                                        Integer temp = new Integer(1);
                                        temp = (Integer)stack.pop();
                                        int second = temp.intValue();
                                        temp = (Integer)stack.pop();
                                        int first = temp.intValue();
                                        int val = process(ch, first, second);
                                        stack.push(new Integer(val));
                                    } // else

                                } // for3
                                Integer temp = new Integer(1);
                                temp = (Integer)stack.pop();
                                int val = temp.intValue();
                                truthTable[k++] = val;
                            } // for2
                        } // for1
                    }
                }
                for (int z = 0; z < k; z++)
                {
                    System.out.println(truthTable[z]);

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
    public JInternalFrame createInternalFrame(Integer layer, int width, int height, boolean state)
    {
        JInternalFrame jif;
        String equation;
        jif = new JInternalFrame();

        JMenuBar jmenubar = new JMenuBar();
        JMenu jmenu = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Save");
        JMenuItem item2 = new JMenuItem("Print");
        item1.addActionListener(new ShowFrameAction(this));
        item2.addActionListener(new ShowFrameAction(this));
        jmenu.add(item1);
        jmenu.add(item2);
        jmenubar.add(jmenu);

        jif.setJMenuBar(jmenubar);

        equation = windowTitleField.getText();
        stack = new Stack();

        {
            switch (gateChoice)
            {
                case 1:
                    if (state == true)
                    {
                        try
                        {
                            findTruthTable(equation, varChoice);
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine = new Quine();
                    minimizeByQuine.gettruth(truthTable, true, varChoice);

                    if (varChoice == 1)
                    {

                        if ((truthTable[0] == 1) && (truthTable[1] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Output=1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 2)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Output=1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 3)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1) && (truthTable[4] == 1)
                                && (truthTable[5] == 1) && (truthTable[6] == 1) && (truthTable[7] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Output=1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 4)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1) && (truthTable[4] == 1)
                                && (truthTable[5] == 1) && (truthTable[6] == 1) && (truthTable[7] == 1) && (truthTable[8] == 1)
                                && (truthTable[9] == 1) && (truthTable[10] == 1) && (truthTable[11] == 1) && (truthTable[12] == 1)
                                && (truthTable[13] == 1) && (truthTable[14] == 1) && (truthTable[15] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Output=1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine.make_groups2();
                    minimizeByQuine.make_groups4();
                    minimizeByQuine.sort_4();

                    minimizeByQuine.cancel_sim_4();
                    minimizeByQuine.selects_valid_4();

                    minimizeByQuine.make_groups8();
                    minimizeByQuine.sort_8();

                    minimizeByQuine.selects_valid_8();
                    minimizeByQuine.select_8_rows();

                    minimizeByQuine.update_flags_sel_valid_8();
                    minimizeByQuine.select_4_rows();

                    minimizeByQuine.select_2_rows();

                    stringExpression = new String(minimizeByQuine.find_expression(reducedExpression, varChoice));

                    jif.setTitle("AND-OR gate realization");
                    break;
                case 2:
                    if (state == true)
                    {
                        try
                        {
                            findTruthTable(equation, varChoice);
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine = new Quine();
                    minimizeByQuine.gettruth(truthTable, true, varChoice);

                    if (varChoice == 1)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 2)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 3)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1) && (truthTable[4] == 1)
                                && (truthTable[5] == 1) && (truthTable[6] == 1) && (truthTable[7] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 4)
                    {
                        if ((truthTable[0] == 1) && (truthTable[1] == 1) && (truthTable[2] == 1) && (truthTable[3] == 1) && (truthTable[4] == 1)
                                && (truthTable[5] == 1) && (truthTable[6] == 1) && (truthTable[7] == 1) && (truthTable[8] == 1)
                                && (truthTable[9] == 1) && (truthTable[10] == 1) && (truthTable[11] == 1) && (truthTable[12] == 1)
                                && (truthTable[13] == 1) && (truthTable[14] == 1) && (truthTable[15] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine.make_groups2();
                    minimizeByQuine.make_groups4();
                    minimizeByQuine.sort_4();

                    minimizeByQuine.cancel_sim_4();
                    minimizeByQuine.selects_valid_4();

                    minimizeByQuine.make_groups8();
                    minimizeByQuine.sort_8();

                    minimizeByQuine.selects_valid_8();
                    minimizeByQuine.select_8_rows();

                    minimizeByQuine.update_flags_sel_valid_8();
                    minimizeByQuine.select_4_rows();

                    minimizeByQuine.select_2_rows();

                    stringExpression = new String(minimizeByQuine.find_expression(reducedExpression, varChoice));

                    jif.setTitle("NAND gate realization");
                    break;
                case 3:
                    if (state == true)
                    {
                        try
                        {
                            findTruthTable(equation, varChoice);
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine = new Quine();
                    minimizeByQuine.gettruth(truthTable, false, varChoice);

                    if (varChoice == 1)
                    {

                        if ((truthTable[0] == 1) && (truthTable[1] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 2)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 3)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0) && (truthTable[4] == 0)
                                && (truthTable[5] == 0) && (truthTable[6] == 0) && (truthTable[7] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 4)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0) && (truthTable[4] == 0)
                                && (truthTable[5] == 0) && (truthTable[6] == 0) && (truthTable[7] == 0) && (truthTable[8] == 0)
                                && (truthTable[9] == 0) && (truthTable[10] == 0) && (truthTable[11] == 0) && (truthTable[12] == 0)
                                && (truthTable[13] == 0) && (truthTable[14] == 0) && (truthTable[15] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine.make_groups2();
                    minimizeByQuine.make_groups4();
                    minimizeByQuine.sort_4();

                    minimizeByQuine.cancel_sim_4();
                    minimizeByQuine.selects_valid_4();

                    minimizeByQuine.make_groups8();
                    minimizeByQuine.sort_8();

                    minimizeByQuine.selects_valid_8();
                    minimizeByQuine.select_8_rows();

                    minimizeByQuine.update_flags_sel_valid_8();
                    minimizeByQuine.select_4_rows();

                    minimizeByQuine.select_2_rows();

                    stringExpression = new String(minimizeByQuine.find_expression_maxterm(reducedExpression, varChoice));

                    jif.setTitle("OR-AND gate realization");
                    break;
                case 4:
                    if (state == true)
                    {
                        try
                        {
                            findTruthTable(equation, varChoice);
                        }
                        catch (Exception e)
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "Invalid Expression!", "Warning!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine = new Quine();
                    minimizeByQuine.gettruth(truthTable, false, varChoice);

                    if (varChoice == 1)
                    {

                        if ((truthTable[0] == 1) && (truthTable[1] == 1))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 2)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 3)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0) && (truthTable[4] == 0)
                                && (truthTable[5] == 0) && (truthTable[6] == 0) && (truthTable[7] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    if (varChoice == 4)
                    {
                        if ((truthTable[0] == 0) && (truthTable[1] == 0) && (truthTable[2] == 0) && (truthTable[3] == 0) && (truthTable[4] == 0)
                                && (truthTable[5] == 0) && (truthTable[6] == 0) && (truthTable[7] == 0) && (truthTable[8] == 0)
                                && (truthTable[9] == 0) && (truthTable[10] == 0) && (truthTable[11] == 0) && (truthTable[12] == 0)
                                && (truthTable[13] == 0) && (truthTable[14] == 0) && (truthTable[15] == 0))
                        {
                            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
                            return jif;
                        }
                    }

                    minimizeByQuine.make_groups2();
                    minimizeByQuine.make_groups4();
                    minimizeByQuine.sort_4();

                    minimizeByQuine.cancel_sim_4();
                    minimizeByQuine.selects_valid_4();

                    minimizeByQuine.make_groups8();
                    minimizeByQuine.sort_8();

                    minimizeByQuine.selects_valid_8();
                    minimizeByQuine.select_8_rows();

                    minimizeByQuine.update_flags_sel_valid_8();
                    minimizeByQuine.select_4_rows();

                    minimizeByQuine.select_2_rows();

                    stringExpression = new String(minimizeByQuine.find_expression_maxterm(reducedExpression, varChoice));

                    jif.setTitle("NOR gate realization");
                    break;
                case 5:
                    JOptionPane.showMessageDialog((Component)null, "Under Construction!", "Message", JOptionPane.PLAIN_MESSAGE);
                    return jif;
            }
        }

        System.out.println("Expression:" + stringExpression);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        if (stringExpression.equals("+"))
        {
            JOptionPane.showMessageDialog(getDemoPanel(), "1", "Message!", JOptionPane.WARNING_MESSAGE);
            return jif;
        }


        p.add(new GateDesignPanel(equation, stringExpression, gateChoice), BorderLayout.CENTER);


        JScrollPane scroller = new JScrollPane();
        scroller.getViewport().add(p);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new gateDocPanel(equation, truthTable, varChoice,
                minimizeByQuine.select_rows_8, minimizeByQuine.sel_rows_8_lim, minimizeByQuine.select_rows_4, minimizeByQuine.sel_rows_4_lim,
                minimizeByQuine.select_rows_2, minimizeByQuine.sel_rows_2_lim, minimizeByQuine.flags), scroller);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0);
        splitPane.setSize(800, 500);
        splitPane.setLocation(0, 0);

        jif.getContentPane().add(splitPane);
        jif.setClosable(true);
        jif.setMaximizable(true);
        jif.setIconifiable(true);
        jif.setResizable(true);
        jif.setVisible(true);
        jif.setBounds(0, 0, width, height);

        desktop.add(jif, layer);

        try
        {
            jif.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e2)
        {
        }


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
        String description = new String("");
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

        JRadioButton andOr = new JRadioButton("And-Or", createImageIcon("buttons/rb.gif", description));
        andOr.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        andOr.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        andOr.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        andOr.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        andOr.setFocusPainted(false);
        andOr.setBorderPainted(false);
        andOr.setContentAreaFilled(false);
        andOr.setMargin(new Insets(0, 0, 0, 0));
        group1.add(andOr);

        JRadioButton nand = new JRadioButton("Nand", createImageIcon("buttons/rb.gif", description));
        nand.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        nand.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        nand.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        nand.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        nand.setFocusPainted(false);
        nand.setBorderPainted(false);
        nand.setContentAreaFilled(false);
        nand.setMargin(new Insets(0, 0, 0, 0));
        nand.setSelected(true);
        group1.add(nand);

        JRadioButton orAnd = new JRadioButton("Or-And", createImageIcon("buttons/rb.gif", description));
        orAnd.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        orAnd.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        orAnd.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        orAnd.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        orAnd.setFocusPainted(false);
        orAnd.setBorderPainted(false);
        orAnd.setContentAreaFilled(false);
        orAnd.setMargin(new Insets(0, 0, 0, 0));
        group1.add(orAnd);


        JRadioButton nor = new JRadioButton("Nor", createImageIcon("buttons/rb.gif", description));
        nor.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        nor.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        nor.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        nor.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        nor.setFocusPainted(false);
        nor.setBorderPainted(false);
        nor.setContentAreaFilled(false);
        nor.setMargin(new Insets(0, 0, 0, 0));
        group1.add(nor);

        JRadioButton xor = new JRadioButton("Xor", createImageIcon("buttons/rb.gif", description));
        xor.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        xor.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        xor.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        xor.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        xor.setFocusPainted(false);
        xor.setBorderPainted(false);
        xor.setContentAreaFilled(false);
        xor.setMargin(new Insets(0, 0, 0, 0));
        // xor.setEnabled(false);
        group1.add(xor);

        // add gate selection actions
        andOr.addActionListener(new ShowFrameAction(this));
        nand.addActionListener(new ShowFrameAction(this));
        orAnd.addActionListener(new ShowFrameAction(this));
        nor.addActionListener(new ShowFrameAction(this));
        xor.addActionListener(new ShowFrameAction(this));


        // add Radiobuttons to panel
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBackground(new Color(195, 195, 195));


        JPanel gates = createVerticalPanel(false);
        gates.setBorder(new TitledBorder(null, "Gate Type ", TitledBorder.LEFT, TitledBorder.TOP));
        gates.setBackground(new Color(195, 195, 195));

        gates.add(andOr);
        gates.add(Box.createRigidArea(HGAP20));
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(nand);
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(orAnd);
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(nor);
        gates.add(Box.createRigidArea(VGAP10));
        gates.add(xor);

        p1.add(Box.createRigidArea(VGAP10));
        p1.add(gates);
        p1.add(Box.createRigidArea(VGAP15));


        // Create JBututtons
        JPanel selectPanel = createHorizontalPanel(false);// JPanel();
        selectPanel.setBackground(new Color(195, 195, 195));

        JButton logicalFunction = new JButton(createImageIcon("buttons/logical1.gif", ""));
        logicalFunction.setPressedIcon(createImageIcon("buttons/logical2.gif", ""));
        logicalFunction.setActionCommand("Logical Function");
        logicalFunction.setFocusPainted(false);
        logicalFunction.setBorderPainted(false);
        logicalFunction.setContentAreaFilled(false);
        logicalFunction.setMargin(new Insets(0, 0, 0, 0));
        logicalFunction.addActionListener(new ShowFrameAction(this));

        JButton kMap = new JButton(createImageIcon("buttons/kmap1.gif", ""));
        kMap.setPressedIcon(createImageIcon("buttons/kmap2.gif", ""));
        kMap.setActionCommand("K-Map");
        kMap.setFocusPainted(false);
        kMap.setBorderPainted(false);
        kMap.setContentAreaFilled(false);
        kMap.setMargin(new Insets(0, 0, 0, 0));
        kMap.addActionListener(new ShowFrameAction(this));

        JButton design = new JButton(createImageIcon("buttons/ckt1.gif", ""));
        design.setPressedIcon(createImageIcon("buttons/ckt2.gif", ""));
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
        GateLevelGates demo;


        public ShowFrameAction(GateLevelGates demo)
        {
            this.demo = demo;

        }

        public void actionPerformed(ActionEvent e)
        {


            String str = e.getActionCommand();

            if (str.equals("And-Or"))
                gateChoice = 1;
            if (str.equals("Nand"))
                gateChoice = 2;
            if (str.equals("Or-And"))
                gateChoice = 3;
            if (str.equals("Nor"))
                gateChoice = 4;
            if (str.equals("Xor"))
                gateChoice = 5;
            if (str.equals("1 Variable"))
                varChoice = 1;
            if (str.equals("2 Variable"))
                varChoice = 2;
            if (str.equals("3 Variable"))
                varChoice = 3;
            if (str.equals("4 Variable"))
                varChoice = 4;
            if (str.equals("Logical Function"))
                windowTitleField.setEnabled(true);
            if (str.equals("K-Map"))
            {
                windowTitleField.setEnabled(false);
                kmap = new JDialog((Frame)null, "K-Map", true);
                kmap.setBounds(200, 200, 270, 330);
                kmap.setResizable(false);

                kmap.getContentPane().setLayout(null);

                t0 = new JCheckBox("");
                t0.setSize(30, 30);
                t0.setBackground(Color.white);

                t4 = new JCheckBox("");
                t4.setSize(30, 30);
                t4.setBackground(Color.white);

                t12 = new JCheckBox("");
                t12.setSize(30, 30);
                t12.setBackground(Color.white);

                t8 = new JCheckBox("");
                t8.setSize(30, 30);
                t8.setBackground(Color.white);

                t1 = new JCheckBox("");
                t1.setSize(30, 30);
                t1.setBackground(Color.white);

                t5 = new JCheckBox("");
                t5.setSize(30, 30);
                t5.setBackground(Color.white);

                t13 = new JCheckBox("");
                t13.setSize(30, 30);
                t13.setBackground(Color.white);

                t9 = new JCheckBox("");
                t9.setSize(30, 30);
                t9.setBackground(Color.white);

                t3 = new JCheckBox("");
                t3.setSize(30, 30);
                t3.setBackground(Color.white);

                t7 = new JCheckBox("");
                t7.setSize(30, 30);
                t7.setBackground(Color.white);

                t15 = new JCheckBox("");
                t15.setSize(30, 30);
                t15.setBackground(Color.white);

                t11 = new JCheckBox("");
                t11.setSize(30, 30);
                t11.setBackground(Color.white);

                t2 = new JCheckBox("");
                t2.setSize(30, 30);
                t2.setBackground(Color.white);

                t6 = new JCheckBox("");
                t6.setSize(30, 30);
                t6.setBackground(Color.white);

                t14 = new JCheckBox("");
                t14.setSize(30, 30);
                t14.setBackground(Color.white);

                t10 = new JCheckBox("");
                t10.setSize(30, 30);
                t10.setBackground(Color.white);

                switch (varChoice)
                {
                    case 1:
                        t0.setLocation(125, 100);
                        t1.setLocation(125, 150);
                        kmap.getContentPane().add(t0);
                        kmap.getContentPane().add(t1);
                        break;
                    case 2:
                        t0.setLocation(100, 75);
                        t2.setLocation(150, 75);
                        t1.setLocation(100, 125);
                        t3.setLocation(150, 125);

                        kmap.getContentPane().add(t0);
                        kmap.getContentPane().add(t2);
                        kmap.getContentPane().add(t1);
                        kmap.getContentPane().add(t3);
                        break;
                    case 3:
                        t0.setLocation(50, 75);
                        t2.setLocation(100, 75);
                        t6.setLocation(150, 75);
                        t4.setLocation(200, 75);
                        t1.setLocation(50, 125);
                        t3.setLocation(100, 125);
                        t7.setLocation(150, 125);
                        t5.setLocation(200, 125);

                        kmap.getContentPane().add(t0);
                        kmap.getContentPane().add(t2);
                        kmap.getContentPane().add(t6);
                        kmap.getContentPane().add(t4);
                        kmap.getContentPane().add(t1);
                        kmap.getContentPane().add(t3);
                        kmap.getContentPane().add(t7);
                        kmap.getContentPane().add(t5);
                        break;
                    case 4:
                        t0.setLocation(50, 50);
                        t4.setLocation(100, 50);
                        t12.setLocation(150, 50);
                        t8.setLocation(200, 50);
                        t1.setLocation(50, 100);
                        t5.setLocation(100, 100);
                        t13.setLocation(150, 100);
                        t9.setLocation(200, 100);
                        t3.setLocation(50, 150);
                        t7.setLocation(100, 150);
                        t15.setLocation(150, 150);
                        t11.setLocation(200, 150);
                        t2.setLocation(50, 200);
                        t6.setLocation(100, 200);
                        t14.setLocation(150, 200);
                        t10.setLocation(200, 200);

                        kmap.getContentPane().add(t0);
                        kmap.getContentPane().add(t1);
                        kmap.getContentPane().add(t2);
                        kmap.getContentPane().add(t3);
                        kmap.getContentPane().add(t4);
                        kmap.getContentPane().add(t5);
                        kmap.getContentPane().add(t6);
                        kmap.getContentPane().add(t7);
                        kmap.getContentPane().add(t8);
                        kmap.getContentPane().add(t9);
                        kmap.getContentPane().add(t10);
                        kmap.getContentPane().add(t11);
                        kmap.getContentPane().add(t12);
                        kmap.getContentPane().add(t13);
                        kmap.getContentPane().add(t14);
                        kmap.getContentPane().add(t15);
                        break;
                }
                ok = new JButton("Ok");
                ok.setSize(100, 30);
                ok.setLocation(25, 250);
                kmap.getContentPane().add(ok);
                ok.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        if (t0.isSelected())
                            truthTable[0] = 1;
                        else
                            truthTable[0] = 0;

                        if (t1.isSelected())
                            truthTable[1] = 1;
                        else
                            truthTable[1] = 0;

                        if (t2.isSelected())
                            truthTable[2] = 1;
                        else
                            truthTable[2] = 0;

                        if (t3.isSelected())
                            truthTable[3] = 1;
                        else
                            truthTable[3] = 0;

                        if (t4.isSelected())
                            truthTable[4] = 1;
                        else
                            truthTable[4] = 0;

                        if (t5.isSelected())
                            truthTable[5] = 1;
                        else
                            truthTable[5] = 0;

                        if (t6.isSelected())
                            truthTable[6] = 1;
                        else
                            truthTable[6] = 0;

                        if (t7.isSelected())
                            truthTable[7] = 1;
                        else
                            truthTable[7] = 0;

                        if (t8.isSelected())
                            truthTable[8] = 1;
                        else
                            truthTable[8] = 0;

                        if (t9.isSelected())
                            truthTable[9] = 1;
                        else
                            truthTable[9] = 0;

                        if (t10.isSelected())
                            truthTable[10] = 1;
                        else
                            truthTable[10] = 0;

                        if (t11.isSelected())
                            truthTable[11] = 1;
                        else
                            truthTable[11] = 0;

                        if (t12.isSelected())
                            truthTable[12] = 1;
                        else
                            truthTable[12] = 0;

                        if (t13.isSelected())
                            truthTable[13] = 1;
                        else
                            truthTable[13] = 0;

                        if (t14.isSelected())
                            truthTable[14] = 1;
                        else
                            truthTable[14] = 0;

                        if (t15.isSelected())
                            truthTable[15] = 1;
                        else
                            truthTable[15] = 0;

                        kmap.dispose();

                        demo.createInternalFrame(getDemoFrameLayer(), getFrameWidth(), getFrameHeight(), false);

                    }
                });

                cancel = new JButton("Cancel");
                cancel.setSize(100, 30);
                cancel.setLocation(135, 250);
                cancel.addActionListener(this);
                kmap.getContentPane().add(cancel);
                cancel.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        kmap.dispose();
                    }
                });

                Backgrnd b = new Backgrnd(varChoice);
                b.setSize(300, 300);
                b.setLocation(0, 0);
                kmap.getContentPane().add(b);

                kmap.show();

                kmap.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        kmap.setVisible(false);
                    }
                });
            }

            else if (str.equals("Design Ckt"))
            {

                demo.createInternalFrame(getDemoFrameLayer(), getFrameWidth(), getFrameHeight(), true);

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



    class ImageScroller extends JScrollPane
    {

        public ImageScroller(GateLevelGates demo, Icon icon, int layer, int count)
        {
            super();
            JPanel p = new JPanel();
            p.setBackground(Color.white);
            p.setLayout(new BorderLayout());

            p.add(new JLabel(icon), BorderLayout.CENTER);

            getViewport().add(p);
        }

        public Dimension getMinimumSize()
        {
            return new Dimension(25, 25);
        }

    }


    class Backgrnd extends JPanel
    {
        int choice;

        Backgrnd(int varChoice)
        {
            setBackground(Color.white);
            choice = varChoice;
            repaint();
        }

        public void paint(Graphics g)
        {
            g.setColor(Color.white);
            g.fillRect(0, 0, 300, 300);

            switch (choice)
            {
                case 1:
                    g.setColor(Color.gray);
                    g.drawRect(105, 90, 50, 50);
                    g.drawRect(105, 140, 50, 50);

                    g.drawLine(105, 90, 80, 65);
                    g.setColor(Color.darkGray);

                    g.drawString("A", 95, 70);
                    g.drawString("1", 95, 155);
                    g.drawString("0", 95, 115);

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
                    g.setColor(Color.blue);
                    g.drawString("0", 108, 100);
                    g.drawString("1", 108, 150);

                    break;
                case 2:
                    g.setColor(Color.gray);
                    g.drawRect(80, 65, 100, 100);

                    g.drawLine(130, 65, 130, 165);
                    g.drawLine(80, 115, 180, 115);

                    g.drawLine(80, 65, 50, 35);
                    g.setColor(Color.darkGray);

                    g.drawString("A", 70, 45);
                    g.drawString("B", 45, 60);

                    g.drawString("0", 105, 63);
                    g.drawString("1", 155, 63);

                    g.drawString("0", 70, 90);
                    g.drawString("1", 70, 140);

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
                    g.setColor(Color.blue);
                    g.drawString("0", 83, 80);
                    g.drawString("1", 83, 130);
                    g.drawString("2", 133, 80);
                    g.drawString("3", 133, 130);

                    break;
                case 3:
                    g.setColor(Color.gray);
                    g.drawRect(30, 65, 200, 100);

                    g.drawLine(30, 115, 230, 115);

                    g.drawLine(80, 65, 80, 165);
                    g.drawLine(130, 65, 130, 165);
                    g.drawLine(180, 65, 180, 165);

                    g.drawLine(30, 65, 10, 35);
                    g.setColor(Color.darkGray);

                    g.drawString("AB", 20, 45);
                    g.drawString("C", 5, 60);

                    g.drawString("00", 50, 62);
                    g.drawString("01", 100, 62);
                    g.drawString("11", 150, 62);
                    g.drawString("10", 200, 62);

                    g.drawString("0", 10, 95);
                    g.drawString("1", 10, 145);

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
                    g.setColor(Color.blue);

                    g.drawString("0", 33, 77);
                    g.drawString("1", 33, 127);

                    g.drawString("2", 83, 77);
                    g.drawString("3", 83, 127);

                    g.drawString("6", 133, 77);
                    g.drawString("7", 133, 127);

                    g.drawString("4", 183, 77);
                    g.drawString("5", 183, 127);

                    break;
                case 4:
                    g.setColor(Color.gray);
                    g.drawRect(30, 40, 200, 200);

                    g.drawLine(30, 90, 230, 90);
                    g.drawLine(30, 140, 230, 140);
                    g.drawLine(30, 190, 230, 190);

                    g.drawLine(80, 40, 80, 240);
                    g.drawLine(130, 40, 130, 240);
                    g.drawLine(180, 40, 180, 240);
                    // g.drawLine(220,40,80,240);

                    g.drawLine(30, 40, 10, 10);
                    g.setColor(Color.darkGray);

                    g.drawString("AB", 20, 20);
                    g.drawString("CD", 0, 35);



                    g.drawString("00", 50, 37);
                    g.drawString("01", 100, 37);
                    g.drawString("11", 150, 37);
                    g.drawString("10", 200, 37);

                    g.drawString("00", 10, 70);
                    g.drawString("01", 10, 120);
                    g.drawString("11", 10, 170);
                    g.drawString("10", 10, 220);

                    g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
                    g.setColor(Color.blue);

                    g.drawString("0", 33, 52);
                    g.drawString("1", 33, 102);
                    g.drawString("3", 33, 152);
                    g.drawString("2", 33, 202);

                    g.drawString("4", 83, 52);
                    g.drawString("5", 83, 102);
                    g.drawString("7", 83, 152);
                    g.drawString("6", 83, 202);

                    g.drawString("12", 133, 52);
                    g.drawString("13", 133, 102);
                    g.drawString("15", 133, 152);
                    g.drawString("14", 133, 202);

                    g.drawString("8", 183, 52);
                    g.drawString("9", 183, 102);
                    g.drawString("11", 183, 152);
                    g.drawString("10", 183, 202);
            }


        }
    }

}
