import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


class FlipFlopDesignPanel extends JPanel
{

    int flipflopChoice = 1;
    Thread t;
    Color s1, r1, s2, r2, ck, set, clr, negq, q, d1, d2, t1;
    Color j1, k1, j2, k2;
    Color high, low;
    int output = 1;

    FlipFlopDesignPanel()
    {
        low = new Color(255, 140, 0);
        high = new Color(0x10, 0x38, 0xA0);


        s1 = low;
        r1 = low;
        s2 = high;
        r2 = high;
        ck = low;
        set = high;
        clr = high;
        negq = low;
        q = high;

        j1 = low;
        k1 = low;
        j2 = low;
        k2 = low;

        d1 = high;
        d2 = low;

        t1 = high;

        repaint();
    }

    public void setSelection(int choice)
    {
        flipflopChoice = choice;

    }

    public int setcolors(boolean S, boolean R, boolean Pr, boolean Cr)
    {


        switch (flipflopChoice)
        {
            case 1:
                if (S == true)
                {
                    s1 = high;
                    s2 = low;
                }
                else
                {
                    s1 = low;
                    s2 = high;
                }

                if (R == true)
                {
                    r1 = high;
                    r2 = low;
                }
                else
                {
                    r1 = low;
                    r2 = high;
                }

                if (Pr == false && Cr == true)
                {
                    ck = low;
                    set = low;
                    clr = high;
                    q = high;
                    negq = low;
                    output = 1;
                    System.out.println("output=1");
                }

                if (Pr == true && Cr == false)
                {
                    ck = low;
                    set = high;
                    clr = low;
                    q = low;
                    negq = high;
                    output = 0;
                }

                if (Pr == false && Cr == false)
                {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog((Component)null, "State of circuit is undefined!", "Warning!", JOptionPane.WARNING_MESSAGE);

                }

                if (Pr == true && Cr == true)
                {
                    ck = low;
                    set = high;
                    clr = high;

                    if (S == true && R == false)
                    {
                        q = high;
                        output = 1;
                        negq = low;
                        s1 = high;
                        r1 = low;
                        s2 = low;
                        r2 = high;
                    }
                    if (S == false && R == true)
                    {
                        q = low;
                        output = 0;
                        negq = high;
                        s1 = low;
                        r1 = high;
                        s2 = high;
                        r2 = low;
                    }
                    if (S == true && R == true)
                    {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog((Component)null, "State of circuit is undefined!", "Warning!", JOptionPane.WARNING_MESSAGE);
                    }
                    if (S == false && R == false)
                    {

                    }

                }

                repaint();
                return output;
            case 2:
                if (S == true)
                {
                    j1 = high;
                    j2 = low;
                }
                else
                {
                    j1 = low;
                    j2 = low;
                }

                if (R == true)
                {
                    k1 = high;
                    k2 = low;
                }
                else
                {
                    k1 = low;
                    k2 = low;
                }

                if (Pr == false && Cr == true)
                {
                    ck = low;
                    set = low;
                    clr = high;
                    q = high;
                    negq = low;
                    output = 1;
                }

                if (Pr == true && Cr == false)
                {
                    ck = low;
                    set = high;
                    clr = low;
                    q = low;
                    negq = high;
                    output = 0;
                }

                if (Pr == false && Cr == false)
                {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog((Component)null, "State of circuit is undefined!", "Warning!", JOptionPane.WARNING_MESSAGE);

                }

                if (Pr == true && Cr == true)
                {
                    ck = low;
                    set = high;
                    clr = high;

                    if (S == true && R == false)
                    {
                        q = high;
                        output = 1;
                        negq = low;
                        j1 = high;
                        k1 = low;
                        j2 = low;
                        k2 = low;
                    }
                    if (S == false && R == true)
                    {
                        q = low;
                        output = 0;
                        negq = high;
                        j1 = low;
                        k1 = high;
                        j2 = low;
                        k2 = low;
                    }
                    if (S == true && R == true)
                    {
                        j1 = high;
                        k1 = high;

                        if (output == 1)
                        {
                            System.out.println("output:" + output);

                            j2 = high;
                            k2 = low;
                            q = low;
                            negq = high;
                            output = 0;
                            repaint();
                            // System.out.println(output);
                            return output;

                        }
                        if (output == 0)
                        {
                            System.out.println("output:" + output);

                            j2 = low;
                            k2 = high;
                            q = high;
                            negq = low;
                            output = 1;
                            repaint();
                            // System.out.println(output);
                            return output;

                        }
                    }
                }
                break;
            case 3:
                if (Pr == false && Cr == true)
                {
                    ck = low;
                    set = low;
                    clr = high;
                    q = high;
                    negq = low;
                    output = 1;
                }

                if (Pr == true && Cr == false)
                {
                    ck = low;
                    set = high;
                    clr = low;
                    q = low;
                    negq = high;
                    output = 0;
                }

                if (Pr == false && Cr == false)
                {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog((Component)null, "State of circuit is undefined!", "Warning!", JOptionPane.WARNING_MESSAGE);

                }

                if (Pr == true && Cr == true)
                {
                    if (S == true)
                    {
                        d1 = high;
                        d2 = low;
                        q = high;
                        negq = low;
                        if (output == 1)
                            output = 0;
                        else
                            output = 1;

                    }
                    else
                    {
                        d1 = low;
                        d2 = high;
                        q = low;
                        negq = high;
                        // output=0;
                    }
                }
                repaint();
                return output;
            case 4:
                if (Pr == false && Cr == true)
                {
                    ck = low;
                    set = low;
                    clr = high;
                    q = high;
                    negq = low;
                    output = 1;
                }

                if (Pr == true && Cr == false)
                {
                    ck = low;
                    set = high;
                    clr = low;
                    q = low;
                    negq = high;
                    output = 0;
                }

                if (Pr == false && Cr == false)
                {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog((Component)null, "State of circuit is undefined!", "Warning!", JOptionPane.WARNING_MESSAGE);

                }

                if (Pr == true && Cr == true)
                {
                    if (S == true)
                    {
                        d1 = high;
                        q = high;
                        negq = low;
                        output = 1;
                    }
                    else
                    {
                        d1 = low;
                        q = low;
                        negq = high;
                        output = 0;
                    }
                }
                repaint();
                return output;




        }// switch
        return output;

    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);

        g.setColor(new Color(255, 140, 0));
        g.drawLine(100, 330, 200, 330);
        g.drawString("State 0", 220, 340);

        g.setColor(new Color(0x10, 0x38, 0xA0));
        g.drawLine(100, 360, 200, 360);
        g.drawString("State 1", 220, 370);

        switch (flipflopChoice)
        {
            case 1: // S-R FLIPFLOP

                drawgate(g, 80, 80);
                g.setColor(s1);
                g.drawLine(80, 90, 40, 90);
                g.drawOval(32, 87, 8, 8);

                g.setColor(s2);
                g.drawLine(200, 102, 158, 102);

                drawgate(g, 80, 180);
                g.setColor(r1);
                g.drawLine(80, 210, 40, 210);
                g.drawOval(32, 207, 8, 8);

                g.setColor(r2);
                g.drawLine(200, 202, 158, 202);

                g.setColor(ck);
                g.drawLine(80, 110, 60, 110);
                g.drawLine(80, 190, 60, 190);
                g.drawLine(60, 110, 60, 190);
                g.drawLine(60, 150, 40, 150);
                g.drawOval(32, 147, 8, 8);

                drawgate(g, 200, 80);
                g.setColor(set);
                g.drawLine(200, 90, 178, 90);
                g.drawLine(178, 90, 178, 60);
                g.drawOval(175, 53, 8, 8);

                drawgate(g, 200, 180);
                g.setColor(clr);
                g.drawLine(200, 215, 178, 215);
                g.drawLine(178, 215, 178, 245);
                g.drawOval(175, 245, 8, 8);

                g.setColor(q);
                g.drawLine(277, 102, 325, 102);
                g.drawOval(325, 99, 8, 8);


                g.drawLine(200, 190, 178, 190);
                g.drawLine(178, 190, 178, 180);
                g.drawLine(178, 180, 290, 112);
                g.drawLine(290, 112, 290, 102);

                g.setColor(negq);
                g.drawLine(277, 202, 325, 202);
                g.drawOval(325, 199, 8, 8);

                g.drawLine(200, 115, 178, 115);
                g.drawLine(178, 115, 178, 125);
                g.drawLine(178, 125, 290, 192);
                g.drawLine(290, 192, 290, 202);

                g.setColor(Color.gray.darker());
                g.drawString("S", 20, 93);
                g.drawString("R", 20, 213);
                g.drawString("CK", 10, 153);
                g.drawString("Preset(Pr)", 160, 50);
                g.drawString("Clear(Cr)", 168, 266);
                g.drawString("Q", 335, 105);
                g.drawString("~Q", 335, 205);

                break;
            case 2: // J-K FLIPFLOP
                g.setColor(Color.yellow);

                g.setColor(new Color(122, 122, 61));
                g.fillRoundRect(100, 100, 45, 45, 4, 4); // gate
                g.fillOval(125, 100, 45, 45);

                // g.setColor(Color.darkGray);
                g.setColor(j1);
                g.drawLine(100, 130, 60, 130);
                g.drawOval(52, 127, 8, 8);


                g.setColor(new Color(122, 122, 61));
                g.fillRoundRect(100, 200, 45, 45, 4, 4); // gate
                g.fillOval(125, 200, 45, 45);

                // g.setColor(Color.darkGray);
                g.setColor(k1);
                g.drawLine(100, 215, 60, 215);
                g.drawOval(52, 212, 8, 8);

                g.setColor(new Color(184, 184, 112));
                g.fillRect(250, 95, 100, 150);
                g.setColor(new Color(122, 122, 61));
                g.drawRect(250, 95, 100, 150);

                // g.setColor(Color.darkGray);
                g.setColor(j2);
                g.drawLine(170, 122, 250, 122);
                g.fillOval(210, 119, 6, 6);

                g.setColor(k2);
                g.drawLine(170, 222, 250, 222);
                g.fillOval(210, 219, 6, 6);

                g.setColor(ck);
                g.drawLine(250, 172, 210, 172);
                g.drawOval(202, 169, 8, 8);

                g.setColor(q);
                g.drawLine(350, 122, 420, 122);
                g.drawOval(419, 119, 8, 8);

                g.drawLine(400, 122, 400, 314);
                g.drawLine(400, 314, 80, 314);
                g.drawLine(80, 314, 80, 235);
                g.drawLine(80, 235, 100, 235);

                g.setColor(negq);
                g.drawLine(350, 222, 420, 222);
                g.drawOval(419, 219, 8, 8);

                g.drawLine(380, 222, 380, 30);
                g.drawLine(380, 30, 80, 30);
                g.drawLine(80, 30, 80, 110);
                g.drawLine(80, 110, 100, 110);

                g.setColor(set);
                g.drawOval(300, 88, 8, 8);
                g.drawLine(304, 88, 304, 70);
                g.drawOval(301, 62, 8, 8);

                g.setColor(clr);
                g.drawOval(300, 245, 8, 8);
                g.drawLine(304, 253, 304, 271);
                g.drawOval(300, 271, 8, 8);

                g.setColor(new Color(0, 64, 64));
                g.drawString("S-R FlipFlop", 270, 150);

                g.setColor(Color.gray.darker());
                g.drawString("J", 42, 135);
                g.drawString("K", 42, 220);
                g.drawString("S=J.~Q", 200, 110);
                g.drawString("R=K.Q", 200, 240);
                g.drawString("CK", 170, 175);
                g.drawString("Q", 430, 124);
                g.drawString("~Q", 430, 224);
                g.drawString("Pr", 300, 55);
                g.drawString("Cr", 300, 290);
                break;
            case 4: // D Flip-Flop
                g.setColor(new Color(184, 184, 112));
                g.fillRect(180, 95, 100, 150);
                g.setColor(new Color(122, 122, 61));
                g.drawRect(180, 95, 100, 150);

                g.setColor(d1);
                g.drawLine(180, 120, 90, 120);
                g.drawOval(82, 117, 8, 8);


                g.fillOval(110, 117, 6, 6);
                g.drawLine(113, 120, 113, 155);
                g.fillOval(140, 117, 6, 6);

                g.setColor(Color.darkGray);
                g.drawLine(103, 155, 123, 155);
                g.drawLine(103, 155, 113, 175);
                g.drawLine(123, 155, 113, 175);
                g.drawOval(109, 175, 8, 8);
                g.drawLine(181, 170, 190, 174);
                g.drawLine(181, 178, 190, 174);

                g.setColor(d2);
                g.drawLine(113, 183, 113, 225);
                g.drawLine(113, 225, 180, 225);
                g.fillOval(140, 222, 6, 6);

                g.setColor(ck);
                g.drawOval(173, 170, 8, 8);
                g.drawLine(173, 174, 160, 174);
                g.drawOval(154, 170, 8, 8);

                g.setColor(set);
                g.drawOval(226, 88, 8, 8);
                g.drawLine(230, 88, 230, 70);
                g.drawOval(227, 62, 8, 8);

                g.setColor(clr);
                g.drawOval(226, 245, 8, 8);
                g.drawLine(230, 253, 230, 271);
                g.drawOval(227, 271, 8, 8);

                g.setColor(q);
                g.drawLine(280, 120, 350, 120);
                g.drawOval(350, 116, 8, 8);

                g.setColor(negq);
                g.drawLine(280, 225, 350, 225);
                g.drawOval(350, 221, 8, 8);

                g.setColor(new Color(0, 64, 64));
                g.drawString("D", 65, 122);
                g.drawString("CK", 130, 177);
                g.drawString("Pr", 225, 60);
                g.drawString("Cr", 225, 295);
                g.drawString("Q", 360, 122);
                g.drawString("~Q", 360, 227);
                g.drawString("J or S", 135, 110);
                g.drawString("K or R", 135, 245);
                g.drawString("J-K or", 210, 150);
                g.drawString("S-R FlipFlop", 200, 170);
                break;
            case 3: // T Flip-Flop
                g.setColor(new Color(184, 184, 112));
                g.fillRect(180, 95, 100, 150);
                g.setColor(new Color(122, 122, 61));
                g.drawRect(180, 95, 100, 150);

                g.setColor(t1);
                g.drawLine(180, 120, 98, 120);
                g.drawOval(90, 117, 8, 8);
                g.drawString("T", 75, 122);
                g.fillOval(140, 117, 6, 6);
                g.fillOval(140, 222, 6, 6);


                g.fillOval(110, 117, 6, 6);
                g.drawLine(113, 120, 113, 225);
                g.drawLine(113, 225, 180, 225);

                g.setColor(ck);
                g.drawLine(181, 170, 190, 174);
                g.drawLine(181, 178, 190, 174);
                g.drawLine(173, 174, 160, 174);
                g.drawOval(154, 171, 6, 6);
                g.drawOval(173, 170, 8, 8);

                g.setColor(set);
                g.drawOval(226, 88, 8, 8);
                g.drawLine(230, 88, 230, 70);
                g.drawOval(227, 64, 6, 6);
                g.drawString("Pr", 225, 60);

                g.setColor(clr);
                g.drawOval(226, 245, 8, 8);
                g.drawLine(230, 253, 230, 271);
                g.drawOval(227, 271, 6, 6);
                g.drawString("Cr", 225, 290);

                g.setColor(q);
                g.drawLine(280, 120, 350, 120);
                g.drawOval(350, 116, 8, 8);
                g.drawString("Q", 360, 122);

                g.setColor(negq);
                g.drawLine(280, 225, 350, 225);
                g.drawOval(350, 221, 8, 8);
                g.drawString("~Q", 360, 227);

                g.setColor(Color.darkGray);
                g.drawString("J ", 135, 110);
                g.drawString("K ", 135, 245);
                g.drawString("CK", 130, 177);
                g.setColor(new Color(0, 64, 64));
                g.drawString("J-K FlipFlop", 200, 170);





        }

    }

    void drawgate(Graphics g, int x, int y)
    {
        g.setColor(new Color(122, 122, 61));
        g.fillRoundRect(x, y, 45, 45, 4, 4);
        g.fillOval(x + 25, y, 45, 45);
        g.drawOval(x + 70, y + 18, 8, 10);
    }


}

class flipflopInfo extends JPanel
{

    int flipflopChoice = 1;

    flipflopInfo()
    {
        repaint();
    }

    public void setSelection(int choice)
    {
        flipflopChoice = choice;

    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        g.setColor(Color.yellow);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);

        switch (flipflopChoice)
        {
            case 1: // S-R FLIPFLOP
                g.setColor(new Color(180, 180, 255));
                g.fillRect(70, 100, 100, 150);

                g.setColor(Color.darkGray);
                g.drawRect(70, 100, 100, 150);

                g.drawLine(70, 140, 30, 140);
                g.drawOval(22, 137, 8, 8);
                g.drawString("S", 10, 143);

                g.drawLine(70, 180, 30, 180);
                g.drawOval(22, 177, 8, 8);
                g.drawString("CK", 2, 183);

                g.drawLine(70, 220, 30, 220);
                g.drawOval(22, 217, 8, 8);
                g.drawString("R", 10, 223);

                g.drawOval(120, 91, 8, 8);
                g.drawLine(124, 93, 124, 80);
                g.drawOval(121, 72, 8, 8);
                g.drawString("Pr", 120, 64);

                g.drawOval(120, 250, 8, 8);
                g.drawLine(124, 258, 124, 270);
                g.drawOval(121, 270, 8, 8);
                g.drawString("Cr", 120, 290);

                g.drawLine(170, 150, 225, 150);
                g.drawOval(225, 146, 8, 8);
                g.drawString("Q", 235, 153);

                g.drawLine(170, 200, 225, 200);
                g.drawOval(225, 196, 8, 8);
                g.drawString("~Q", 235, 203);

                g.drawString("S-R FlipFlop", 90, 160);


                g.setColor(Color.blue);
                g.drawString("Truth Table for S-R Flip-Flop", 350, 50);
                g.setColor(Color.darkGray);
                g.drawString("Inputs                Outputs", 370, 80);
                g.drawString("Sn     Rn             Qn+1", 370, 100);
                g.drawString(" 0      0                   Qn", 370, 120);
                g.drawString(" 1      0                    1", 370, 140);
                g.drawString(" 0      1                    0", 370, 160);
                g.drawString(" 1      1                    ?", 370, 180);

                g.setColor(Color.blue);
                g.drawString("Summary of operation of S-R Flip-Flop", 320, 220);
                g.setColor(Color.darkGray);
                g.drawString("Inputs                Outputs        Operation Performed", 320, 250);
                g.drawString("CK    Cr    Pr              Q", 320, 270);
                g.drawString(" 1     1     1              Qn+1       Normal Flip-Flop", 320, 290);
                g.drawString(" 0     0     1               0              Clear", 320, 310);
                g.drawString(" 0     1     0               1              Preset", 320, 330);
                break;

            case 2: // J-K FLIPFLOP
                g.setColor(new Color(180, 180, 255));
                g.fillRect(70, 100, 100, 150);
                g.setColor(Color.darkGray);
                g.drawRect(70, 100, 100, 150);

                g.drawLine(70, 140, 30, 140);
                g.drawOval(22, 137, 8, 8);
                g.drawString("J", 10, 143);

                g.drawLine(70, 180, 30, 180);
                g.drawOval(22, 177, 8, 8);
                g.drawString("CK", 5, 183);

                g.drawLine(70, 220, 30, 220);
                g.drawOval(22, 217, 8, 8);
                g.drawString("K", 10, 223);

                g.drawOval(120, 93, 8, 8);
                g.drawLine(124, 93, 124, 80);
                g.drawOval(121, 74, 8, 8);
                g.drawString("Pr", 120, 64);

                g.drawOval(120, 250, 8, 8);
                g.drawLine(124, 258, 124, 270);
                g.drawOval(120, 270, 8, 8);
                g.drawString("Cr", 120, 290);

                g.drawLine(170, 150, 225, 150);
                g.drawOval(225, 146, 8, 8);
                g.drawString("Q", 235, 153);

                g.drawLine(170, 200, 225, 200);
                g.drawOval(225, 196, 8, 8);
                g.drawString("~Q", 235, 203);

                g.drawString("J-K FlipFlop", 90, 160);


                g.setColor(Color.blue);
                g.drawString("Truth Table for J-K Flip-Flop", 350, 90);
                g.setColor(Color.darkGray);
                g.drawString("Inputs                Outputs", 370, 120);
                g.drawString("Jn     Kn             Qn+1", 370, 140);
                g.drawString(" 0      0                   Qn", 370, 160);
                g.drawString(" 1      0                    1", 370, 180);
                g.drawString(" 0      1                    0", 370, 200);
                g.drawString(" 1      1                    ~Qn", 370, 220);
                break;
            case 4: // D FLIPFLOP
                g.setColor(new Color(180, 180, 255));
                g.fillRect(70, 100, 100, 150);
                g.setColor(Color.darkGray);
                g.drawRect(70, 100, 100, 150);

                g.drawLine(70, 140, 30, 140);
                g.drawOval(22, 137, 8, 8);
                g.drawString("D", 10, 143);

                g.drawLine(62, 180, 30, 180);
                g.drawOval(22, 177, 8, 8);
                g.drawOval(62, 176, 8, 8);
                g.drawLine(70, 173, 80, 180);
                g.drawLine(70, 183, 80, 180);
                g.drawString("CK", 3, 183);

                g.drawOval(120, 93, 8, 8);
                g.drawLine(124, 93, 124, 80);
                g.drawOval(121, 74, 6, 6);
                g.drawString("Pr", 120, 64);

                g.drawOval(120, 250, 8, 8);
                g.drawLine(124, 258, 124, 270);
                g.drawOval(121, 270, 6, 6);
                g.drawString("Cr", 120, 290);

                g.drawLine(170, 150, 225, 150);
                g.drawOval(225, 146, 8, 8);
                g.drawString("Q", 235, 153);

                g.drawLine(170, 200, 225, 200);
                g.drawOval(225, 196, 8, 8);
                g.drawString("~Q", 235, 203);

                g.drawString("D FlipFlop", 90, 160);


                g.setColor(Color.blue);
                g.drawString("Truth Table of a D Flip-Flop", 350, 90);
                g.setColor(Color.darkGray);
                g.drawString("Inputs         Outputs", 370, 120);
                g.drawString(" Dn               Qn+1", 370, 140);
                g.drawString(" 0                   0", 370, 160);
                g.drawString(" 1                   1", 370, 180);
                break;
            case 3: // T FLIPFLOP
                g.setColor(new Color(180, 180, 255));
                g.fillRect(70, 100, 100, 150);
                g.setColor(Color.darkGray);
                g.drawRect(70, 100, 100, 150);

                g.drawLine(70, 140, 30, 140);
                g.drawOval(22, 137, 8, 8);
                g.drawString("T", 10, 143);

                g.drawLine(62, 180, 30, 180);
                g.drawOval(22, 177, 8, 8);
                g.drawOval(62, 176, 8, 8);
                g.drawLine(70, 173, 80, 180);
                g.drawLine(70, 183, 80, 180);
                g.drawString("CK", 3, 183);

                g.drawOval(120, 93, 8, 8);
                g.drawLine(124, 93, 124, 80);
                g.drawOval(121, 74, 6, 6);
                g.drawString("Pr", 120, 64);

                g.drawOval(120, 250, 8, 8);
                g.drawLine(124, 258, 124, 270);
                g.drawOval(121, 270, 6, 6);
                g.drawString("Cr", 120, 290);

                g.drawLine(170, 150, 225, 150);
                g.drawOval(225, 146, 8, 8);
                g.drawString("Q", 235, 153);

                g.drawLine(170, 200, 225, 200);
                g.drawOval(225, 196, 8, 8);
                g.drawString("~Q", 235, 203);

                g.drawString("T FlipFlop", 90, 160);


                g.setColor(Color.blue);
                g.drawString("Truth Table of a T Flip-Flop", 350, 90);
                g.setColor(Color.darkGray);
                g.drawString("Inputs         Outputs", 370, 120);
                g.drawString(" Tn               Qn+1", 370, 140);
                g.drawString(" 0                  Qn", 370, 160);
                g.drawString(" 1                 ~Qn", 370, 180);










        }
    }


}
