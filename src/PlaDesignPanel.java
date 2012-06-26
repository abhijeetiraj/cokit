import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class PlaDesignPanel extends JPanel
{

    String equation, originalEqn, reducedEqn;
    Stack stack = new Stack();
    int gateChoice;
    int varChoice;
    int inverterX;
    int inverterY;
    int ymax;


    PlaDesignPanel(String originalEq, String reducedEq, int Choice)
    {
        varChoice = Choice;
        setBackground(Color.black);
        equation = new String(reducedEq);
        System.out.println(equation);
        originalEqn = new String(originalEq);
        try
        {
            reducedEqn = new String(reducedEq.substring(0, reducedEq.length() - 1));
        }
        catch (Exception e)
        {
        }

        reducedEqn = new String(reducedEq.substring(0, reducedEq.length() - 1));
        repaint();
    }


    public void paint(Graphics g)
    {
        int gateCount = 0;
        int x = 300, y = 50;
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);

        g.setColor(Color.blue.brighter());

        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));


        int vdiff = 50, diff = 20, index = 0, originh = 100, originv = 122;
        // equation=new String(equation+"*");
        System.out.println(equation);
        char ch1;
        index = 0;
        int i1 = 0;
        while (i1 < equation.length())
        {
            ch1 = equation.charAt(i1++);
            if (ch1 == '+')
                originv = originv + 50;


            // index+=vdiff;
            while (ch1 != '+')
            {
                // boolean state[]=new boolean[];


                if (ch1 != '~')
                {
                    if (ch1 == 'A')
                    {
                        g.drawString("X", originh + 6, originv);
                    }
                    if (ch1 == 'B')
                    {
                        g.drawString("X", originh + 6 + 42, originv);
                    }
                    if (ch1 == 'C')
                    {
                        g.drawString("X", originh + 6 + 42 + 42, originv);
                    }
                    if (ch1 == 'D')
                    {
                        g.drawString("X", originh + 6 + 42 + 42 + 42, originv);
                    }
                }
                else
                {
                    ch1 = equation.charAt(i1++);
                    if (ch1 == '+')
                        originv = originv + 50;


                    /*
                     * if(i>=equation.length()) System.exit(0);
                     */

                    if (ch1 == 'A')
                    {
                        g.drawString("X", originh + 24, originv);
                    }
                    if (ch1 == 'B')
                    {
                        g.drawString("X", originh + 24 + 42, originv);
                    }
                    if (ch1 == 'C')
                    {
                        g.drawString("X", originh + 24 + 42 + 42, originv);
                    }
                    if (ch1 == 'D')
                    {
                        g.drawString("X", originh + 24 + 42 + 42 + 42, originv);
                    }
                }// else ends
                ch1 = equation.charAt(i1++);
                if (ch1 == '+')
                    originv = originv + 50;


                // if(i>=equation.length())
                // System.exit(0);
            }// while + ends

        }// while ends




        for (int i = 0; i < equation.length(); i++)
        {
            char ch = equation.charAt(i);
            if (ch != '+')
            {
                stack.push(new Character(ch));
            }
            else if (ch == '+')
            {
                gateCount++;
                // System.out.println("gate");
                y = y + 50;
                g.setColor(new Color(122, 122, 61));
                g.fillRoundRect(x, y, 35, 35, 4, 4);
                g.fillOval(x + 15, y, 35, 35);
                g.drawLine(x + 50, y + 17, x + 89, y + 17);
                g.drawLine(x, y + 17, x - 240, y + 17);

                String inputVar = new String();
                ;
                int invertX = 100;
                int invertY = 100;

                while (!stack.empty())
                {
                    Character temp = new Character('@');
                    temp = (Character)stack.pop();
                    ch = temp.charValue();
                    inputVar = new String(inputVar + ch);
                }
                g.setColor(Color.blue);
                g.drawString(inputVar, x + 95, y + 20);
                g.setColor(Color.black);
                g.drawString("X", x + 65, y + 20);
                invertY += 50;
                // y=y+100;
            }


            inverterX = 100;
            inverterY = 50;
            ymax = gateCount * 70 + 100;

            for (int inverter = 0; inverter < varChoice; inverter++)
            {
                g.setColor(new Color(122, 122, 61));
                g.drawLine(inverterX, inverterY, inverterX + 30, inverterY);
                g.drawLine(inverterX, inverterY, inverterX + 15, inverterY + 30);
                g.drawLine(inverterX + 30, inverterY, inverterX + 15, inverterY + 30);


                g.setColor(Color.darkGray);
                g.drawLine(inverterX + 15, inverterY, inverterX + 15, inverterY - 12);
                g.drawLine(inverterX + 6, inverterY + 10, inverterX + 6, ymax);
                g.drawOval(inverterX + 24, inverterY + 10, 6, 6);
                g.drawLine(inverterX + 27, inverterY + 16, inverterX + 27, ymax);
                inverterX += 42;
            }
            /*
             * int vdiff=50,diff=30,index=0,originh=100,originv=100; // equation=new String(equation+"*");
             * System.out.println(equation); char ch1; index=0; while(i<equation.length()) { ch1=equation.charAt(i++);
             * index+=vdiff; while(ch1!='+') { //boolean state[]=new boolean[];
             * 
             * if(ch1!='~') { if(ch1=='A'){g.drawString("X",originh+(1*diff),originv+index);}
             * if(ch1=='B'){g.drawString("X",originh+(3*diff),originv+index);}
             * if(ch1=='C'){g.drawString("X",originh+(5*diff),originv+index);}
             * if(ch1=='D'){g.drawString("X",originh+(7*diff),originv+index);} } else { ch1=equation.charAt(i++);
             * if(i>=equation.length()) System.exit(0);
             * 
             * if(ch1=='A'){g.drawString("X",originh+(2*diff),originv+index);}
             * if(ch1=='B'){g.drawString("X",originh+(4*diff),originv+index);}
             * if(ch1=='C'){g.drawString("X",originh+(6*diff),originv+index);}
             * if(ch1=='D'){g.drawString("X",originh+(8*diff),originv+index);} }//else ends ch1=equation.charAt(i++);
             * //if(i>=equation.length()) //System.exit(0); }//while + ends
             * 
             * }//while ends
             */



        }// paint
        g.setColor(Color.darkGray);
        g.drawLine(x + 70, 50, x + 70, ymax);
        g.drawLine(x + 70, ymax + 53, x + 70, ymax + 75);
        int xpoints[] = new int[5];
        int ypoints[] = new int[5];

        g.setColor(new Color(122, 122, 61));

        xpoints[0] = x + 50;
        ypoints[0] = ymax;
        xpoints[1] = x + 90;
        ypoints[1] = ymax;
        xpoints[2] = x + 90;
        ypoints[2] = ymax + 30;
        xpoints[3] = x + 70;
        ypoints[3] = ymax + 53;
        xpoints[4] = x + 50;
        ypoints[4] = ymax + 30;


        // g.fillRect(x+35,ymax,35,35);
        g.fillPolygon(xpoints, ypoints, 5);
        g.setColor(Color.blue);
        g.drawString("A", 110, 30);
        g.drawString("B", 150, 30);
        g.drawString("C", 200, 30);
        g.drawString("D", 250, 30);


    }



    class gateDocPanel extends JPanel
    {

        String equation, originalEqn, reducedEqn;
        Stack stack = new Stack();
        int gateChoice;

        gateDocPanel(String originalEq, int truthTable[], int varChoice, int select_rows_8[][], int sel_rows_8_lim, int select_rows_4[][],
                int sel_rows_4_lim, int select_rows_2[][], int sel_rows_2_lim, int flags[])
        {
            // setBackground(Color.white);
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


            // repaint();
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
}
