import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class GateDesignPanel extends JPanel
{

    String equation, originalEqn, reducedEqn;
    Stack stack = new Stack();
    int gateChoice;

    GateDesignPanel(String originalEq, String reducedEq, int choice)
    {
        setBackground(Color.black);
        equation = new String(reducedEq);
        originalEqn = new String(originalEq);
        try
        {
            reducedEqn = new String(reducedEq.substring(0, reducedEq.length() - 1));
        }
        catch (Exception e)
        {
        }
        gateChoice = choice;

        if (gateChoice == 3 || gateChoice == 4)
        {
            equation = new String(reducedEqn.replace('+', ' '));
            equation = new String(equation);
        }
        try
        {
            reducedEqn = new String(reducedEq.substring(0, reducedEq.length() - 1));
        }
        catch (Exception ae)
        {
            System.out.println(ae);
            reducedEqn = new String("null");
        }
        repaint();
    }


    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);

        g.setColor(Color.blue.brighter());

        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        g.drawString("Minimized Logical Function: " + reducedEqn, 10, 20);
        if (reducedEqn.equals("null"))
            return;

        g.setFont(new Font("Times New Roman", Font.BOLD, 10));

        int x = 100, y = 35, yDup = 35, lineCount = 0, gateCount = 0;

        switch (gateChoice)
        {
            case 1:
                for (int i = 0; i < equation.length(); i++)
                {
                    char ch = equation.charAt(i);
                    if (ch != '+')
                    {
                        stack.push(new Character(ch));
                        if (ch != '~')
                            lineCount++;
                    }
                    else if (ch == '+')
                    {
                        gateCount++;
                        y = y + 50;
                        yDup = y + 3;
                        drawAndgate(g, x, y, lineCount);
                        int incr = 45 / (lineCount + 1);
                        while (!stack.empty())
                        {
                            String inputVar = new String();
                            Character temp = new Character('@');
                            temp = (Character)stack.pop();
                            ch = temp.charValue();
                            if (ch == '~')
                            {
                                inputVar = new String("~" + inputVar);
                                g.drawString(inputVar, x - 75, yDup);
                            }
                            else
                            {
                                yDup = yDup + incr;
                                inputVar = new String("" + ch);
                                g.setColor(Color.blue);
                                g.drawString(inputVar, x - 70, yDup);
                            }
                        }
                        lineCount = 0;
                    }

                }

                int centerGatey = 35 + ((++gateCount * 50) / 2);
                drawCenterOrgate(g, 250, centerGatey);
                --gateCount;
                y = 107;
                x = 168;
                int incr = 300 - 235;
                int incr1 = 45 / (gateCount + 1);
                int tempincr1 = incr1;

                g.setColor(Color.darkGray);
                for (int i = 0; i < gateCount; i++)
                {
                    g.drawLine(x, y, x + incr, y);
                    g.drawLine(x + incr, y, x + incr, centerGatey + incr1);
                    g.drawLine(x + incr, centerGatey + incr1, 250, centerGatey + incr1);
                    y = y + 50;
                    incr1 = incr1 + tempincr1;
                    if (i < (gateCount / 2))
                        incr = incr - 5;
                    else
                        incr = incr + 5;
                }

                break;
            case 2:
                for (int i = 0; i < equation.length(); i++)
                {
                    char ch = equation.charAt(i);
                    if (ch != '+')
                    {
                        stack.push(new Character(ch));
                        if (ch != '~')
                            lineCount++;
                    }
                    else if (ch == '+')
                    {
                        gateCount++;
                        y = y + 50;
                        yDup = y + 3;
                        drawNandgate(g, x, y, lineCount);
                        incr = 45 / (lineCount + 1);
                        while (!stack.empty())
                        {
                            String inputVar = new String();
                            Character temp = new Character('@');
                            temp = (Character)stack.pop();
                            ch = temp.charValue();
                            if (ch == '~')
                            {
                                inputVar = new String("~" + inputVar);
                                g.drawString(inputVar, x - 75, yDup);
                            }
                            else
                            {
                                yDup = yDup + incr;
                                inputVar = new String("" + ch);
                                g.setColor(Color.blue);
                                g.drawString(inputVar, x - 70, yDup);
                            }
                        }
                        lineCount = 0;
                    }

                }

                centerGatey = 35 + ((++gateCount * 50) / 2);
                drawCenterNandgate(g, 250, centerGatey);
                --gateCount;
                y = 107;
                x = 178;
                incr = 300 - 235;
                incr1 = 45 / (gateCount + 1);
                tempincr1 = incr1;

                g.setColor(Color.darkGray);
                for (int i = 0; i < gateCount; i++)
                {
                    g.drawLine(x, y, x + incr, y);
                    g.drawLine(x + incr, y, x + incr, centerGatey + incr1);
                    g.drawLine(x + incr, centerGatey + incr1, 250, centerGatey + incr1);
                    y = y + 50;
                    incr1 = incr1 + tempincr1;
                    if (i < (gateCount / 2))
                        incr = incr - 5;
                    else
                        incr = incr + 5;
                }
                break;
            case 3:
                for (int i = 0; i < equation.length(); i++)
                {
                    char ch = equation.charAt(i);
                    if (ch != '.' && ch != ' ' && ch != '(' && ch != ')')
                    {
                        stack.push(new Character(ch));
                        if (ch != '~')
                            lineCount++;
                    }
                    else if (ch == '.')
                    {
                        gateCount++;
                        y = y + 50;
                        yDup = y + 3;
                        drawOrgate(g, x, y, lineCount);
                        incr = 45 / (lineCount + 1);
                        while (!stack.empty())
                        {
                            String inputVar = new String();
                            Character temp = new Character('@');
                            temp = (Character)stack.pop();
                            ch = temp.charValue();
                            if (ch == '~')
                            {
                                inputVar = new String("~" + inputVar);
                                g.drawString(inputVar, x - 75, yDup);
                            }
                            else
                            {
                                yDup = yDup + incr;
                                inputVar = new String("" + ch);
                                g.setColor(Color.blue);
                                g.drawString(inputVar, x - 70, yDup);
                            }
                        }
                        lineCount = 0;
                    }

                }

                centerGatey = 35 + ((++gateCount * 50) / 2);
                drawCenterAndgate(g, 250, centerGatey);
                --gateCount;
                y = 107;
                x = 178;
                incr = 300 - 235;
                incr1 = 45 / (gateCount + 1);
                tempincr1 = incr1;

                g.setColor(Color.darkGray);
                for (int i = 0; i < gateCount; i++)
                {
                    g.drawLine(x, y, x + incr, y);
                    g.drawLine(x + incr, y, x + incr, centerGatey + incr1);
                    g.drawLine(x + incr, centerGatey + incr1, 250, centerGatey + incr1);
                    y = y + 50;
                    incr1 = incr1 + tempincr1;
                    if (i < (gateCount / 2))
                        incr = incr - 5;
                    else
                        incr = incr + 5;
                }
                break;
            case 4:
                for (int i = 0; i < equation.length(); i++)
                {
                    char ch = equation.charAt(i);
                    if (ch != '.' && ch != ' ' && ch != '(' && ch != ')')
                    {
                        stack.push(new Character(ch));
                        if (ch != '~')
                            lineCount++;
                    }
                    else if (ch == '.')
                    {
                        gateCount++;
                        y = y + 50;
                        yDup = y + 3;
                        drawNorgate(g, x, y, lineCount);
                        incr = 45 / (lineCount + 1);
                        while (!stack.empty())
                        {
                            String inputVar = new String();
                            Character temp = new Character('@');
                            temp = (Character)stack.pop();
                            ch = temp.charValue();
                            if (ch == '~')
                            {
                                inputVar = new String("~" + inputVar);
                                g.drawString(inputVar, x - 75, yDup);
                            }
                            else
                            {
                                yDup = yDup + incr;
                                inputVar = new String("" + ch);
                                g.setColor(Color.blue);
                                g.drawString(inputVar, x - 70, yDup);
                            }
                        }
                        lineCount = 0;
                    }

                }

                centerGatey = 35 + ((++gateCount * 50) / 2);
                drawCenterNorgate(g, 250, centerGatey);
                --gateCount;
                y = 107;
                x = 186;
                incr = 300 - 245;
                incr1 = 45 / (gateCount + 1);
                tempincr1 = incr1;

                g.setColor(Color.darkGray);
                for (int i = 0; i < gateCount; i++)
                {
                    g.drawLine(x, y, x + incr, y);
                    g.drawLine(x + incr, y, x + incr, centerGatey + incr1);
                    g.drawLine(x + incr, centerGatey + incr1, 250, centerGatey + incr1);
                    y = y + 50;
                    incr1 = incr1 + tempincr1;
                    if (i < (gateCount / 2))
                        incr = incr - 5;
                    else
                        incr = incr + 5;
                }
                break;

        }


    }// paint


    void drawAndgate(Graphics g, int x, int y, int lineCount)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRoundRect(x, y, 45, 45, 4, 4);
        g.fillOval(x + 25, y, 45, 45);
        // g.drawOval(x+70,y+18,8,10);

        int incr = 45 / (lineCount + 1);

        g.setColor(Color.darkGray);
        for (int i = 0; i < lineCount; i++)
        {
            y = y + incr;
            g.drawLine(x, y, x - 40, y);
            g.drawOval(x - 47, y - 3, 7, 7);
        }
    }

    void drawOrgate(Graphics g, int x, int y, int lineCount)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRect(x, y, 45, 45);
        // g.fillOval(x+25,y,45,45);
        // g.drawOval(x+70,y+18,8,10);

        int xpoints[] = new int[3];
        int ypoints[] = new int[3];
        xpoints[0] = x + 45;
        xpoints[1] = x + 78;
        xpoints[2] = x + 45;
        ypoints[0] = y;
        ypoints[1] = y + 22;
        ypoints[2] = y + 45;


        Polygon p = new Polygon(xpoints, ypoints, 3);
        g.fillPolygon(p);

        int incr = 45 / (lineCount + 1);

        g.setColor(Color.darkGray);
        for (int i = 0; i < lineCount; i++)
        {
            y = y + incr;
            g.drawLine(x, y, x - 40, y);
            g.drawOval(x - 47, y - 3, 7, 7);
        }
    }

    void drawCenterOrgate(Graphics g, int x, int y)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRect(x, y, 45, 45);
        // g.fillOval(x+25,y,45,45);
        // g.drawOval(x+70,y+18,8,10);
        g.drawLine(x + 45, y, x + 78, y + 22);
        g.drawLine(x + 45, y + 45, x + 78, y + 22);

        int xpoints[] = new int[3];
        int ypoints[] = new int[3];
        xpoints[0] = x + 45;
        xpoints[1] = x + 78;
        xpoints[2] = x + 45;
        ypoints[0] = y;
        ypoints[1] = y + 22;
        ypoints[2] = y + 45;


        Polygon p = new Polygon(xpoints, ypoints, 3);
        g.fillPolygon(p);

        g.setColor(Color.darkGray);
        g.drawLine(x + 78, y + 23, x + 120, y + 23);
        g.drawOval(x + 120, y + 19, 8, 8);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
        g.drawString("Y Output", x + 140, y + 20);
    }

    void drawNandgate(Graphics g, int x, int y, int lineCount)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRoundRect(x, y, 45, 45, 4, 4);
        g.fillOval(x + 25, y, 45, 45);
        g.drawOval(x + 70, y + 18, 8, 10);

        int incr = 45 / (lineCount + 1);

        g.setColor(Color.darkGray);
        for (int i = 0; i < lineCount; i++)
        {
            y = y + incr;
            g.drawLine(x, y, x - 40, y);
            g.drawOval(x - 47, y - 3, 7, 7);
        }
    }

    void drawCenterNandgate(Graphics g, int x, int y)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRoundRect(x, y, 45, 45, 4, 4);
        g.fillOval(x + 25, y, 45, 45);
        g.drawOval(x + 70, y + 18, 8, 10);
        g.setColor(Color.darkGray);
        g.drawLine(x + 78, y + 23, x + 120, y + 23);
        g.drawOval(x + 120, y + 19, 8, 8);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
        g.drawString("Y Output", x + 140, y + 20);
    }

    void drawCenterAndgate(Graphics g, int x, int y)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRoundRect(x, y, 45, 45, 4, 4);
        g.fillOval(x + 25, y, 45, 45);
        // g.drawOval(x+70,y+18,8,10);
        g.setColor(Color.darkGray);
        g.drawLine(x + 70, y + 23, x + 120, y + 23);
        g.drawOval(x + 120, y + 19, 8, 8);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
        g.drawString("Y Output", x + 140, y + 20);
    }

    void drawNorgate(Graphics g, int x, int y, int lineCount)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRect(x, y, 45, 45);
        // g.fillOval(x+25,y,45,45);
        // g.drawOval(x+70,y+18,8,10);

        int xpoints[] = new int[3];
        int ypoints[] = new int[3];
        xpoints[0] = x + 45;
        xpoints[1] = x + 78;
        xpoints[2] = x + 45;
        ypoints[0] = y;
        ypoints[1] = y + 22;
        ypoints[2] = y + 45;


        Polygon p = new Polygon(xpoints, ypoints, 3);
        g.fillPolygon(p);

        g.drawOval(x + 78, y + 18, 8, 8);

        int incr = 45 / (lineCount + 1);

        g.setColor(Color.darkGray);
        for (int i = 0; i < lineCount; i++)
        {
            y = y + incr;
            g.drawLine(x, y, x - 40, y);
            g.drawOval(x - 47, y - 3, 7, 7);
        }
    }

    void drawCenterNorgate(Graphics g, int x, int y)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRect(x, y, 45, 45);
        // g.fillOval(x+25,y,45,45);
        // g.drawOval(x+70,y+18,8,10);
        g.drawLine(x + 45, y, x + 78, y + 22);
        g.drawLine(x + 45, y + 45, x + 78, y + 22);

        int xpoints[] = new int[3];
        int ypoints[] = new int[3];
        xpoints[0] = x + 45;
        xpoints[1] = x + 78;
        xpoints[2] = x + 45;
        ypoints[0] = y;
        ypoints[1] = y + 22;
        ypoints[2] = y + 45;


        Polygon p = new Polygon(xpoints, ypoints, 3);
        g.fillPolygon(p);
        g.drawOval(x + 78, y + 18, 8, 8);

        g.setColor(Color.darkGray);

        g.drawLine(x + 86, y + 23, x + 120, y + 23);
        g.drawOval(x + 120, y + 19, 8, 8);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
        g.drawString("Y Output", x + 140, y + 20);
    }





}



class gateDocPanel extends JPanel
{

    String equation, originalEqn, reducedEqn;
    Stack stack = new Stack();
    int gateChoice;

    gateDocPanel(String originalEq, int truthTable[], int varChoice, int select_rows_8[][], int sel_rows_8_lim, int select_rows_4[][],
            int sel_rows_4_lim, int select_rows_2[][], int sel_rows_2_lim, int flags[])
    {
        originalEqn = new String(originalEq);
        Object[] data = new Object[5];

        DefaultTableModel defaulttablemodel = new DefaultTableModel();
        JTable truth = new JTable(defaulttablemodel);

        setLayout(null);

        JTextField LogicaFunction;
        LogicaFunction = new JTextField("Logical Function: " + originalEqn);
        LogicaFunction.setSize(770, 20);
        LogicaFunction.setLocation(5, 2);
        LogicaFunction.setForeground(Color.green.brighter());
        LogicaFunction.setBackground(Color.black);
        LogicaFunction.setEditable(false);
        LogicaFunction.setHorizontalAlignment(JTextField.CENTER);
        add(LogicaFunction);


        switch (varChoice)
        {
            case 4:
                defaulttablemodel.addColumn("A   ");
                defaulttablemodel.addColumn("B   ");
                defaulttablemodel.addColumn("C   ");
                defaulttablemodel.addColumn("D   ");
                defaulttablemodel.addColumn("Y   ");

                data[0] = " A";
                data[1] = " B";
                data[2] = " C";
                data[3] = " D";
                data[4] = " Y";
                defaulttablemodel.addRow(data);

                int t = 0;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int k = 0; k < 2; k++)
                        {
                            for (int l = 0; l < 2; l++)
                            {
                                for (int column = 0; column < 4; column++)
                                {
                                    if (column == 0)
                                        data[column] = "" + i;
                                    if (column == 1)
                                        data[column] = "" + j;
                                    if (column == 2)
                                        data[column] = "" + k;
                                    if (column == 3)
                                        data[column] = "" + k;
                                }
                                data[4] = "" + truthTable[t++];
                                defaulttablemodel.addRow(data);
                            }
                        }
                    }
                }
                truth.setSize(100, 275);
                truth.setLocation(60, 100);
                break;
            case 3:
                defaulttablemodel.addColumn("A   ");
                defaulttablemodel.addColumn("B   ");
                defaulttablemodel.addColumn("C   ");
                defaulttablemodel.addColumn("Y   ");

                data[0] = " A";
                data[1] = " B";
                data[2] = " C";
                data[3] = " Y";
                defaulttablemodel.addRow(data);

                t = 0;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int k = 0; k < 2; k++)
                        {
                            for (int column = 0; column < 3; column++)
                            {
                                if (column == 0)
                                    data[column] = "" + i;
                                if (column == 1)
                                    data[column] = "" + j;
                                if (column == 2)
                                    data[column] = "" + k;
                            }
                            data[3] = "" + truthTable[t++];
                            defaulttablemodel.addRow(data);
                        }
                    }
                }
                truth.setSize(100, 275);
                truth.setLocation(60, 100);
                break;
            case 2:
                defaulttablemodel.addColumn("A   ");
                defaulttablemodel.addColumn("B   ");
                defaulttablemodel.addColumn("Y   ");

                data[0] = " A";
                data[1] = " B";
                data[2] = " Y";
                defaulttablemodel.addRow(data);

                t = 0;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int column = 0; column < 2; column++)
                        {
                            if (column == 0)
                                data[column] = "" + i;
                            if (column == 1)
                                data[column] = "" + j;
                        }
                        data[2] = "" + truthTable[t++];
                        defaulttablemodel.addRow(data);
                    }
                }
                truth.setSize(100, 275);
                truth.setLocation(60, 100);
                break;
            case 1:
                defaulttablemodel.addColumn("A   ");
                defaulttablemodel.addColumn("Y   ");

                data[0] = " A";
                data[1] = " Y";
                defaulttablemodel.addRow(data);

                t = 0;
                for (int i = 0; i < 2; i++)
                {
                    for (int column = 0; column < 1; column++)
                    {
                        if (column == 0)
                            data[column] = "" + i;
                    }
                    data[1] = "" + truthTable[t++];
                    defaulttablemodel.addRow(data);
                }
                truth.setSize(100, 275);
                truth.setLocation(60, 100);
                break;

        }

        add(truth);

        // Groups of 8 elements
        defaulttablemodel = new DefaultTableModel();
        JTable group_8 = new JTable(defaulttablemodel);
        data = new Object[8];

        for (int i = 0; i < 8; i++)
            defaulttablemodel.addColumn(" ");

        for (int row = 0; row < sel_rows_8_lim; row++)
        {
            for (int column = 0; column < 8; column++)
            {
                data[column] = "" + select_rows_8[row][column];
                System.out.print(" " + select_rows_8[row][column]);
            }
            System.out.println("");
            defaulttablemodel.addRow(data);
        }
        group_8.setSize(140, 275);
        group_8.setLocation(200, 100);

        add(group_8);

        // Groups of 4 elements
        defaulttablemodel = new DefaultTableModel();
        JTable group_4 = new JTable(defaulttablemodel);
        data = new Object[4];

        for (int i = 0; i < 4; i++)
            defaulttablemodel.addColumn(" ");

        for (int row = 0; row < sel_rows_4_lim; row++)
        {
            for (int column = 0; column < 4; column++)
                data[column] = "" + select_rows_4[row][column];
            defaulttablemodel.addRow(data);
        }
        group_4.setSize(70, 275);
        group_4.setLocation(370, 100);

        add(group_4);

        // Groups of 2 elements
        defaulttablemodel = new DefaultTableModel();
        JTable group_2 = new JTable(defaulttablemodel);
        data = new Object[2];

        for (int i = 0; i < 2; i++)
            defaulttablemodel.addColumn(" ");

        for (int row = 0; row < sel_rows_2_lim; row++)
        {
            for (int column = 0; column < 2; column++)
                data[column] = "" + select_rows_2[row][column];
            defaulttablemodel.addRow(data);
        }
        group_2.setSize(45, 275);
        group_2.setLocation(530, 100);

        add(group_2);

        // Groups of 1 elements
        defaulttablemodel = new DefaultTableModel();
        JTable group_1 = new JTable(defaulttablemodel);
        data = new Object[1];

        for (int i = 0; i < 1; i++)
            defaulttablemodel.addColumn(" ");

        for (int row = 0; row < 16; row++)
        {
            for (int column = 0; column < 1; column++)
            {
                if (flags[row] == 0)
                {
                    data[column] = "" + row;
                    defaulttablemodel.addRow(data);
                }
            }
        }
        group_1.setSize(20, 275);
        group_1.setLocation(680, 100);

        add(group_1);

        l mc = new l(originalEq);
        mc.setSize(800, 540);
        mc.setLocation(0, 0);

        add(mc);



    }

}

class l extends JPanel
{
    String originalEq;

    l(String Eq)
    {
        setBackground(Color.black);
        originalEq = new String(Eq);
        repaint();
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(5, 25, 770, 475);


        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        g.drawString("Logical Function: " + originalEq, 10, 15);

        g.setColor(Color.blue.brighter());
        g.drawString("Truth Table", 80, 90);
        g.drawString("Groups of 8 elements", 230, 90);
        g.drawString("Groups of 4 elements", 360, 90);
        g.drawString("Groups of 2 elements", 500, 90);
        g.drawString("Groups of 1 elements", 640, 90);


    }
}
