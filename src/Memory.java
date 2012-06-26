import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Memory.java-CoKit
 * 
 * @version 1.1 2002/01/01
 * @author Abhijeet Dhanpal Iraj
 * @author Nilesh Madhukar Shirke
 */

public class Memory extends DemoModule implements ActionListener
{

    JPanel MainPanel = new JPanel();
    memoryDesign memoryOrganize;

    int tempo = 0x00ff;// specify address here
    int mem = 16, chipsize = 4;

    int no, gflag = 0;
    int jointx[] = new int[50];
    int jointy[] = new int[50];
    int jointix[] = new int[50];
    int jointiy[] = new int[50];
    int i, tmp, others;
    int select = 0, ccc = 0;
    int c = 0, decoderinp, decoderout;
    int nandinp[] = new int[20];
    int cccc = 0;
    String buff = new String();
    String buff1 = new String("A");
    String buff2 = new String("A");
    String buff3 = new String("A");
    String temp1 = new String();
    int start = 0;
    int chipst[][] = new int[50][16];
    int chipend[][] = new int[50][16];
    int address[] = new int[16];
    int cs[] = new int[20];

    int t, t1, i1, counter;
    int dt = 200, dl = 80, db = 400, dr = 160;
    int dt1 = 200, dl1 = 80, db1 = 400, dr1 = 160;
    int h1 = 300, h2 = 500, v = 100, v1 = 0, a = 0;
    int flag[] = new int[16];
    int outer = 0;
    int outer1 = 0;
    int v5 = 0;
    JComboBox chipSize;
    JTextField totalMemory;
    JButton organizeMemory;

    /**
     * main method allows us to run as a standalone demo.
     */

    public static void main(String[] args)
    {
        Memory demo = new Memory(null);
        demo.mainImpl();
    }

    /**
     * Memory Constructor
     */

    public Memory(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "Memory", "toolbar/Memory.gif");

        JPanel demo = getDemoPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));
        demo.setBackground(Color.white);
        v1 = 0;

        MainPanel.setLayout(null);

        memoryOrganize = new memoryDesign();
        memoryOrganize.setBounds(150, 0, 650, 600);

        MainPanel.add(memoryOrganize);

        JLabel memoryLabel = new JLabel("Memory Size");
        memoryLabel.setBounds(10, 15, 100, 10);

        totalMemory = new JTextField(20);
        totalMemory.setBackground(Color.black);
        totalMemory.setForeground(Color.green.brighter());
        totalMemory.setBounds(10, 30, 100, 30);
        totalMemory.addActionListener(this);

        JLabel chipLabel = new JLabel("Chip Size");
        chipLabel.setBounds(10, 115, 100, 10);

        chipSize = new JComboBox();
        chipSize.setBounds(10, 130, 70, 30);
        chipSize.addItem("1");
        chipSize.addItem("2");
        chipSize.addItem("4");
        chipSize.addItem("8");
        chipSize.addItem("16");
        chipSize.addItem("32");
        chipSize.addItem("64");
        chipSize.setSelectedIndex(5);
        chipSize.addActionListener(this);

        organizeMemory = new JButton("Organize Memory");
        organizeMemory.setBounds(10, 250, 100, 30);
        organizeMemory.addActionListener(this);

        MainPanel.add(totalMemory);
        MainPanel.add(memoryLabel);
        MainPanel.add(chipSize);
        MainPanel.add(chipLabel);
        MainPanel.add(organizeMemory);


        demo.add(MainPanel);

    }


    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == totalMemory)
        {
            mem = Integer.parseInt(totalMemory.getText());
            memoryOrganize.reDesign();
        }
        if (e.getSource() instanceof JComboBox)
        {
            chipsize = Integer.parseInt((String)chipSize.getSelectedItem());
            // memoryOrganize.reDesign();
        }
        if (e.getSource() == organizeMemory)
        {
            mem = Integer.parseInt(totalMemory.getText());
            chipsize = Integer.parseInt((String)chipSize.getSelectedItem());
            memoryOrganize.reDesign();
        }

    }


    class memoryDesign extends JPanel
    {

        public memoryDesign()
        {


        }

        public void reDesign()
        {
            System.out.println(" " + mem + " " + chipsize);
            repaint();
        }

        public void small(Graphics g)
        {
            // design nand gate for decodeeselect ground//
            cccc = 0;
            i = 15;
            while (i > tmp)
            {
                nandinp[cccc++] = i;
                i--;
            }

            // here give nand color
            g.setColor(new Color(122, 122, 61));

            g.fillRect(dl - 60, dt - 100, (dr - dl) + 60, 60);
            g.setColor(Color.green);
            g.drawString("NAND", dl - 10 + 10, dt - 100 + 25);
            g.setColor(Color.gray);
            g.drawLine(dl + (dr - dl) / 2, dt, dl + (dr - dl) / 2, dt - 40);// nand output
            int sub = 10, sub1 = 0;

            for (i = cccc - 1; i >= 0; i--)
            {
                g.setColor(Color.gray);
                g.drawOval(dr - sub, dt - 100, 2, 2);
                g.drawOval(dr - sub, dt - 100, 3, 3);
                g.drawOval(dr - sub, dt - 120, 2, 2);
                g.drawLine(dr - sub, dt - 100, dr - sub, dt - 120);
                int tempo2 = nandinp[i];

                String buff3 = new String("A" + tempo2);
                g.setColor(Color.green);
                g.drawString(buff3, dr - 15 - sub1, dt - 130);

                sub += 25;
                sub1 += 25;
            }
            // design of nand ends

            // g.setColor(Color.());
            for (i = 0; i < no; i++)
            {
                v1 = v;
                // here give memory chip color
                g.setColor(new Color(122, 122, 61));

                g.fillRect(h1, v, h2 - h1, 50);// draw memory chip
                int tempo3[] = new int[16];
                int tempo4[] = new int[16];
                int k = 0;
                while (k <= 15)
                {
                    tempo3[k] = chipst[i][k];
                    tempo4[k] = chipend[i][k];
                    k++;
                }
                int t = 0, t1 = 0, i1 = 0;
                while (i1 <= 15)
                {
                    t = (int)(t + tempo3[i1] * Math.pow(2, i1));
                    t1 = (int)(t1 + tempo4[i1] * Math.pow(2, i1));
                    i1++;
                }
                String buff = new String();
                g.setColor(new Color(192, 192, 192));
                g.fillRect(h1 + 50, v + 5, (h2 - h1) - 60, 15);// draw memory chip stsrt address

                g.setColor(Color.green);
                g.drawString("" + Integer.toHexString(t), h1 + 60, v + 18);
                g.setColor(new Color(192, 192, 192));
                g.fillRect(h1 + 50, v + 50 - 20, (h2 - h1) - 60, 15);// draw memor end address
                g.setColor(Color.green);

                g.drawString("" + Integer.toHexString(t1), h1 + 60, v + 50 - 20 + 13);

                g.setColor(Color.blue);
                g.drawString("~cs", h1 + 10, v + 40);
                g.setColor(Color.gray);
                g.drawLine(h1 + 10, v + 50, h1 + 10, v + 100);// cs line1
                g.drawLine(h1 + 10, v + 100, h1 - 80, v + 100);// cs line2
                g.drawLine(dl + (dr - dl) / 2, dt, h1 - 80, v + 100);
                // line(jointx[i],jointy[i],h1-80,v+60);
                g.setColor(Color.blue);

                g.drawString(chipsize + "x8KB", h1 + 10, v + 10);

                g.drawLine(h1 - 50, v1 + 10, h1, v1 + 10);
                g.drawLine(h1 - 60, v1 + 20, h1, v1 + 20);
                v1 += 20;// HORI LINES
                v += 50 + 20;
            }

            g.drawLine(h1 - 50, 80, h1 - 50, v1 - 10);
            g.drawLine(h1 - 60, 80, h1 - 60, v1);// VERT LINES
            g.drawString("A" + tmp, h1 - 80, 70);
            g.drawString("A" + 0, h1 - 50, 70);
            counter = 0;

            others = tmp + 1;
            int gflag = 0;
            outer1 = 0;
            for (i = others; i <= 15 && outer1 != 1; i++)
            {
                if ((address[i]) == 0 && outer1 != 1)
                    gflag = 1;
                else
                {
                    gflag = 0;
                    outer1 = 1;
                }
            }
            outer1 = 0;
            if (gflag == 1)
            {
                /*
                 * setfillstyle(SOLID_FILL,4); floodfill(h1+25,(select*70)+100+20,GREEN);
                 */
                g.setColor(Color.magenta);

                g.drawString("SELECT", h1, (select * 70) + 100 + 25);
                /* circle(h1+25,(select*70)+100+20,10); */
            }

        }




        public void paint(Graphics g)
        {

            if (mem % chipsize != 0)
                no = mem / chipsize + 1;
            else
                no = mem / chipsize;

            g.setColor(Color.white);
            g.fillRect(0, 0, 1000, 1000);

            g.setColor(Color.black.brighter());
            g.fillRect(25, 15, 480, 60);
            g.setColor(Color.gray.darker());
            g.fillRect(20, 20, 480, 60);

            int h1 = 300, h2 = 500, v = 100, v1, a = 0;
            int temp = chipsize * 1024;
            i = 0;
            // int tempo=0x0fff;//specify address here
            while (i <= 15)
            {
                address[i] = tempo % 2;
                tempo = tempo / 2;
                i++;
            }

            tmp = (int)(Math.log(temp) / Math.log(2) - 1);// calc address line end inside a selected chip
            a = 0;
            int j = 0;
            while (j < no)
            {
                i = 0;
                int t = a;
                while (i <= 15)
                {
                    chipst[j][i] = a % 2;
                    a = a / 2;
                    i++;
                }
                a = t;
                int b = a + temp - 1;
                i = 0;
                while (i <= 15)
                {
                    chipend[j][i] = b % 2;
                    b = b / 2;
                    i++;
                }
                a = t;
                a += temp;
                j++;
            }

            for (i = 0; i <= 15; i++)
            {
                for (j = 0; j < no - 1; j++)
                {
                    if (chipst[j][i] == chipst[j + 1][i])
                        flag[i] = 0;
                    else
                    {
                        flag[i] = 1;
                        break;
                    }
                }
            }

            counter = 0;

            for (i = tmp + 1; i <= 15; i++)
            {
                if (flag[i] == 1)
                    cs[counter++] = i;
            }

            if (no == 1)
            {
                small(g);
            }
            else
            { // for big only



                // DESIGN DEMULTIPLEXER OR DECODER//
                outer = 0;
                if (no != 1 && outer != 1)
                {
                    if (counter != 1)
                    {
                        while (c != Math.pow(2, counter) && outer != 1)
                        {
                            if (Math.pow(2, c) >= Math.pow(2, counter))
                            {
                                decoderinp = c;
                                decoderout = (int)(Math.pow(2, c));
                                outer = 1;
                            }
                            else
                                c++;
                        }
                    }
                    else
                    {
                        decoderinp = 1;
                        decoderout = 2;
                    }
                    outer = 0;
                    int dt = 200, dl = 80, db = 400, dr = 160;
                    int dt1 = 200, dl1 = 80, db1 = 400, dr1 = 160;

                    g.setColor(new Color(122, 122, 61));
                    g.drawRect(dl, dt, dr - dl, db - dt);
                    g.fillRect(dl - 5, dt + 5, dr - dl, db - dt);

                    g.drawLine(dl - 5, dt + 5, dl, dt);
                    g.drawLine(dl - 5, db + 5, dl, db);
                    g.drawLine(dr - 5, dt + 5, dr, dt);
                    g.drawLine(dr - 5, db + 5, dr, db);



                    // design nand gate for decodeeselect ground//
                    cccc = 0;
                    i = 15;
                    while (flag[i] != 1)
                    {
                        nandinp[cccc++] = i;
                        i--;
                    }


                    g.fillRect(dl - 50, dt - 100, (dr - dl) + 60, 60);
                    g.setColor(Color.green);
                    g.drawString("NAND", dl - 10 + 10, dt - 100 + 25);

                    g.setColor(Color.gray);
                    g.drawLine(dl + (dr - dl) / 2, dt, dl + (dr - dl) / 2, dt - 40);
                    int sub = 10, sub1 = 0;

                    for (i = cccc - 1; i >= 0; i--)
                    {
                        g.setColor(Color.gray);
                        g.drawOval(dr - sub, dt - 100, 2, 2);
                        g.drawOval(dr - sub, dt - 100, 3, 3);
                        g.drawOval(dr - sub, dt - 120, 2, 2);
                        g.drawLine(dr - sub, dt - 100, dr - sub, dt - 120);

                        int tempo2 = nandinp[i];
                        g.setColor(Color.green);
                        g.drawString("A" + tempo2, dr - 15 - sub1, dt - 130);
                        sub += 25;
                        sub1 += 25;
                    }

                    // design of nand ends
                    g.drawString("G", dl + (dr - dl) / 2, dt + 10);
                    g.setColor(Color.gray);
                    g.drawOval(dl + (dr - dl) / 2, dt, 2, 2);


                    g.setColor(Color.green);
                    g.drawString("D", dl + (dr - dl) / 2, dt + 50);
                    g.drawString("E", dl + (dr - dl) / 2, dt + 70);
                    g.drawString("C", dl + (dr - dl) / 2, dt + 90);
                    g.drawString("O", dl + (dr - dl) / 2, dt + 110);
                    g.drawString("D", dl + (dr - dl) / 2, dt + 130);
                    g.drawString("E", dl + (dr - dl) / 2, dt + 150);
                    g.drawString("R", dl + (dr - dl) / 2, dt + 170);


                    int dtop, dbot;
                    dtop = dt1 + 50;
                    dbot = db1 - 50;
                    dt1 = dtop;

                    g.setColor(Color.gray);

                    for (i = 0; i < decoderinp; i++)
                    {
                        g.drawOval(dl1, dt1, 2, 2);
                        g.drawLine(dl1, dt1, dl1 - 30, dt1);
                        if (no > i)
                        {
                            jointix[i] = dl1 - 30;
                            jointiy[i] = dt1;
                        }
                        if (decoderinp != 1)
                        {
                            dt1 = dt1 + (dbot - dtop) / (decoderinp - 1);
                        }
                        else
                            dt1 = dt1 + (dbot - dtop) / 2;
                    }

                    dtop = dt + 30;
                    dbot = db - 30;
                    dt = dtop;

                    for (i = 0; i < decoderout; i++)
                    {
                        g.drawOval(dr, dt, 2, 2);
                        g.drawLine(dr, dt, dr + 30, dt);
                        if (no > i)
                        {
                            jointx[i] = dr + 30;
                            jointy[i] = dt;
                        }
                        dt = dt + (dbot - dtop) / (decoderout - 1);
                    }

                }// end if

                // g.setColor(Color.gray);


                for (i = 0; i < no; i++)
                {
                    v5 = v;
                    v1 = v;

                    g.setColor(new Color(157, 157, 0));
                    g.fillRect(h1, v, (h2 - h1), 50);// draw memory chip///here is the problem has solved
                    int tempo3[] = new int[16];
                    int tempo4[] = new int[16];
                    int k = 0;
                    while (k <= 15)
                    {
                        tempo3[k] = chipst[i][k];
                        tempo4[k] = chipend[i][k];
                        k++;
                    }
                    t = 0;
                    t1 = 0;
                    i1 = 0;
                    while (i1 <= 15)
                    {
                        t = (int)(t + tempo3[i1] * Math.pow(2, i1));
                        t1 = (int)(t1 + tempo4[i1] * Math.pow(2, i1));
                        i1++;
                    }
                    String buff5;
                    g.setColor(new Color(192, 192, 192));
                    g.fillRect(h1 + 50, v + 5, ((h2 - h1) - 60), 20);// draw memory chip stsrt address//here

                    g.setColor(Color.blue);
                    g.drawString("" + Integer.toHexString(t), h1 + 70, v + 18);


                    String buff6;

                    g.setColor(new Color(192, 192, 192));
                    g.fillRect(h1 + 50, v + 50 - 20, ((h2 - h1) - 60), 15);// draw memor end address

                    g.setColor(Color.blue);
                    g.drawString("" + Integer.toHexString(t1), h1 + 70, v + 50 - 20 + 13);

                    g.setColor(Color.red);
                    g.drawString("~cs", h1 + 10, v + 40);
                    g.drawLine(h1 + 10, v + 50, h1 + 10, v + 60);// cs line1
                    g.drawLine(h1 + 10, v + 60, h1 - 80, v + 60);// cs line2
                    g.drawLine(jointx[i], jointy[i], h1 - 80, v + 60);
                    g.setColor(Color.blue);

                    g.drawString(chipsize + "x8KB", h1 + 10, v + 10);

                    g.drawLine(h1 - 50, v1 + 10, h1, v1 + 10);
                    g.drawLine(h1 - 60, v1 + 20, h1, v1 + 20);
                    v1 += 20;// HORI LINES
                    v += 50 + 20;
                }

                g.drawLine(h1 - 50, 80, h1 - 50, v5 + 10);
                g.drawLine(h1 - 60, 80, h1 - 60, v5 + 20);// VERT LINES
                if (no != 1)
                {
                    String buff1 = new String("A");

                    g.drawString("A" + tmp, h1 - 80, 70); // a=address line
                    g.drawString("A" + 0, h1 - 50, 70); // a=address line
                    counter = 0;


                    for (i = 0; i < decoderinp; i++)
                    {
                        int tempo1 = cs[counter];
                        select = select + (int)(address[tempo1] * Math.pow(2, ccc));
                        ccc++;

                        g.drawString("A" + cs[counter++], jointix[i] - 15, jointiy[i] + 5);// a=address line
                    }



                }// end if
                others = cs[counter - 1];

                gflag = 0;
                for (i = others + 1; i <= 15; i++)
                {
                    if ((address[i]) == 0)
                        gflag = 1;
                    else
                    {
                        gflag = 0;
                        break;
                    }
                }


                if (gflag == 1)
                {
                    // setfillstyle(SOLID_FILL,4);
                    /* floodfill(h1+25,(select*70)+100+20,GREEN); */
                    g.setColor(Color.magenta);
                    g.drawString("SELECT", h1 + 0, (select * 70) + 100 + 25);
                    /* circle(h1+25,(select*70)+100+20,10); */
                }




            }// big else ends
        }// paint

    }// memorydesign

}// memory
