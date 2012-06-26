import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * CRO.java
 * 
 * @version 1.1 01/1/2002
 * @author Abhijeet Iraj
 * @author Nilesh Shirke
 */

public class CRO extends JFrame implements ActionListener
{

    CoKit cokit;
    JButton bl, br, bd, bu, bz, bz1;

    int i, l = 130, r = 150, arr[][] = new int[500][500];
    int voltpdiv = 2, timepdiv = 2, w = 480, ht = 480;

    String message = "Nil";
    int accu[] =
        { 20, 10, 10, 10, 10, 10, 10, 10, 20, 10, 10, 10, 10, 10, 10, 10, 20, 10, 10, 10, 10, 10, 10, 10, 20, 10, 10, 10, 10, 10, 10, 10, 20, 10, 10,
                10, 10, 10, 10, 10, };

    screen s = new screen();
    static int datacro = 0;


    public static void main(String[] args)
    {
        CRO demo = new CRO(null);
        // demo.mainImpl();
    }

    public CRO(String title)
    {
        super(title);

        JPanel demo = new JPanel();
        demo.setLayout(null);

        JPanel controls = new JPanel();
        controls.setLayout(null);
        controls.setBounds(5, 5, 160, 600);
        controls.setBackground(Color.white);

        demo.setBounds(170, 0, 700, 600);
        // getContentPane().setLayout(null);
        demo.setBackground(Color.black);

        bl = new JButton(createImageIcon("buttons/voltdiv+.gif", ""));
        bl.setPressedIcon(createImageIcon("buttons/voltdiv+2.gif", ""));
        bl.setSize(140, 50);
        bl.setLocation(20, 20);
        bl.setFocusPainted(false);
        bl.setBorderPainted(false);
        bl.setContentAreaFilled(false);
        bl.setMargin(new Insets(0, 0, 0, 0));
        bl.addActionListener(this);
        controls.add(bl);

        br = new JButton(createImageIcon("buttons/voltdiv-.gif", ""));
        br.setPressedIcon(createImageIcon("buttons/voltdiv-1.gif", ""));
        br.setSize(140, 50);
        br.setLocation(20, 80);
        br.setFocusPainted(false);
        br.setBorderPainted(false);
        br.setContentAreaFilled(false);
        br.setMargin(new Insets(0, 0, 0, 0));
        br.addActionListener(this);
        controls.add(br);

        bu = new JButton(createImageIcon("buttons/timediv+.gif", ""));
        bu.setPressedIcon(createImageIcon("buttons/timediv+1.gif", ""));
        bu.setSize(140, 50);
        bu.setLocation(20, 140);
        bu.setFocusPainted(false);
        bu.setBorderPainted(false);
        bu.setContentAreaFilled(false);
        bu.setMargin(new Insets(0, 0, 0, 0));
        bu.addActionListener(this);
        controls.add(bu);

        bd = new JButton(createImageIcon("buttons/timediv-1.gif", ""));
        bd.setPressedIcon(createImageIcon("buttons/timediv-.gif", ""));
        bd.setSize(140, 50);
        bd.setLocation(20, 200);
        bd.setFocusPainted(false);
        bd.setBorderPainted(false);
        bd.setContentAreaFilled(false);
        bd.setMargin(new Insets(0, 0, 0, 0));
        bd.addActionListener(this);
        controls.add(bd);

        bz = new JButton(createImageIcon("buttons/zoom+1.gif", ""));
        bz.setPressedIcon(createImageIcon("buttons/zoom+2.gif", ""));
        bz.setSize(140, 50);
        bz.setLocation(20, 260);
        bz.setFocusPainted(false);
        bz.setBorderPainted(false);
        bz.setContentAreaFilled(false);
        bz.setMargin(new Insets(0, 0, 0, 0));
        bz.addActionListener(this);
        controls.add(bz);

        bz1 = new JButton(createImageIcon("/buttons/zoom-2.gif", ""));
        bz1.setPressedIcon(createImageIcon("/buttons/zoom-1.gif", ""));
        bz1.setSize(140, 50);
        bz1.setLocation(20, 320);
        bz1.setFocusPainted(false);
        bz1.setBorderPainted(false);
        bz1.setContentAreaFilled(false);
        bz1.setMargin(new Insets(0, 0, 0, 0));
        bz1.addActionListener(this);
        controls.add(bz1);

        for (int h = 0; h < 40; h += 1)
        {
            for (int v = 0; v < 40; v += 1)
            {
                arr[h][v] = 0;
            }
        }

        s = new screen();
        s.setSize(1000, 1000);
        s.setLocation(170, -30);

        demo.add(s);
        getContentPane().add(controls);
        getContentPane().add(demo);

    }

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public ImageIcon createImageIcon(String filename, String description)
    {
        String path = "/resources/images/" + filename;
        return new ImageIcon(getClass().getResource(path));
    }




    public void actionPerformed(ActionEvent e)
    {



        if (e.getSource() == bl)
        {
            message = "volt/div+";
            voltpdiv += 1;
            for (int h = 0; h < 40; h += 1)
            {
                for (int v = 0; v < 40; v += 1)
                {
                    arr[h][v] = 0;
                }
            }
        }

        if (e.getSource() == br)
        {
            message = "volt/div-";
            voltpdiv -= 1;
            for (int h = 0; h < 40; h += 1)
            {
                for (int v = 0; v < 40; v += 1)
                {
                    arr[h][v] = 0;
                }
            }
        }

        if (e.getSource() == bu)
        {
            message = "time/div+";
            timepdiv += 2;
            for (int h = 0; h < 40; h += 1)
            {
                for (int v = 0; v < 40; v += 1)
                {
                    arr[h][v] = 0;
                }
            }
        }

        if (e.getSource() == bd)
        {
            message = "time/div-";
            timepdiv -= 2;
            for (int h = 0; h < 40; h += 1)
            {
                for (int v = 0; v < 40; v += 1)
                {
                    arr[h][v] = 0;
                }
            }
        }

        if (e.getSource() == bz)
        {
            message = "zoom+";
            ht += 120;
            w += 120;
        }

        if (e.getSource() == bz1)
        {
            message = "zoom-";
            ht -= 120;
            w -= 120;
            if (ht <= 120)
            {
                ht = 120;
                w = 120;
            }

        }

        s.repaint();
    }


    public void outData(int regA)
    {
        System.out.println("In Outdata");
        datacro = regA;
        s.out();
    }



    class screen extends JPanel
    {


        public screen()
        {
        }

        public void out()
        {
            for (int t = 0; t < 39; t++)
            {
                accu[t] = accu[t + 1];

            }
            System.out.println("datacro" + datacro);
            accu[39] = datacro;
            if (isVisible())
                repaint();

        }




        public void paint(Graphics g)
        {
            g.setColor(Color.gray);
            g.fillRect(0, 0, 1000, 1000);

            System.out.println("Painting");
            int cl = 50, ct = 50, w1, ht1, t = 40, no = 40;// no=no. of sq.
            int valuev[] = new int[50];

            for (int h = 0; h < 40; h += 1)
            {
                for (int v = 0; v < 40; v += 1)
                {
                    arr[h][v] = 0;
                }
            }

            try
            {
                t = no / timepdiv;
            }
            catch (ArithmeticException e)
            {
                timepdiv = 2;
            }


            try
            {
                for (int h = 0; h < t; h++)
                {
                    valuev[h] = no / 2 - accu[h] / voltpdiv;
                }
            }
            catch (ArithmeticException e)
            {
                voltpdiv = 2;
            }

            for (int h = 0, k = 0, k2 = 0; h < no & k2 < t; k2++)
            {
                for (int k1 = 0; k1 < timepdiv; k1++)
                {
                    arr[h][valuev[k2]] = 1;
                    h++;
                }
            }
            w1 = w / no;
            ht1 = ht / no;

            g.drawString(message, 50, 30);
            g.setColor(Color.green);
            g.drawRect(cl, ct, w, ht);
            g.setColor(Color.blue);
            g.fillRect(cl, ct, w, ht);


            g.setColor(Color.red);
            for (int i1 = cl; i1 <= cl + w; i1 += w1)
            {
                g.drawLine(i1, cl, i1, cl + ht);
            }
            for (int i1 = ct; i1 <= ct + ht; i1 += ht1)
            {
                g.drawLine(ct, i1, ct + ht, i1);
            }
            for (int h = cl; h < cl + w; h += w1)
            {
                for (int v = ct; v < ct + ht; v += ht1)
                {
                    g.setColor(new Color(0, 64, 0));// Color.blue);
                    g.drawRect(h, v, w1, ht1);
                    g.fillRect(h, v, w1, ht1);
                }
            }
            for (int h = 0; h < no; h++)
            {
                for (int v = 0; v < no; v++)
                {
                    g.setColor(new Color(0, 100, 0).brighter());// Color.red);
                    g.drawRect((h * w1) + cl, (v * ht1) + ct, w1, ht1);
                    if (arr[h][v] == 1)
                    {
                        g.setColor(new Color(0, 213, 0).brighter());
                        g.fillRect((h * w1) + cl, (v * ht1) + ct, w1, ht1);
                    }
                }
            }
            g.setColor(Color.green);
            g.drawLine(cl + w / 2, ct, cl + w / 2, ct + ht);
            g.drawLine(cl, ct + ht / 2, cl + w, ct + ht / 2);

        }

    }// screen

}
