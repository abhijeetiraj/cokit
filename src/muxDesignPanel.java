import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

class muxDesignPanel extends JPanel
{
    Color clr = new Color(122, 122, 61);
    int truth[] = new int[16];
    int choice;
    String equation = new String();
    int truthLimit = 0;

    muxDesignPanel(String eqn, int truthTable[], int varChoice, int muxChoice)
    {
        switch (varChoice)
        {
            case 1:
                choice = 1;
                truthLimit = 2;
                break;
            case 2:
                if (muxChoice == 1)
                    choice = 2;
                else
                    choice = 3;
                truthLimit = 4;
                break;
            case 3:
                if (muxChoice == 1)
                    choice = 4;
                else if (muxChoice == 2)
                    choice = 5;
                else
                    choice = 6;
                truthLimit = 8;
                break;
            case 4:
                if (muxChoice == 1)
                    choice = 7;
                else if (muxChoice == 2)
                    choice = 8;
                else if (muxChoice == 3)
                    choice = 9;
                else
                    choice = 10;
                truthLimit = 16;
                break;
        }



        for (int i = 0; i < truthLimit; i++)
            truth[i] = truthTable[i];
        // choice=varChoice;
        equation = new String(eqn);
    }

    public void printPage()
    {
        /*
         * PrintJob printjob=getToolkit().getPrintJob(); Graphics g=printjob.getGraphics(); repaint(); g.dispose();
         * printjob.end();
         */
        getToolkit().beep();

    }

    public void setcolor(Color color)
    {
        clr = color;
        repaint();

    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);

        switch (choice)
        {
            case 1: // 1 Variable 2:1 Mux
                g.setColor(clr);
                g.fillRect(200, 150, 90, 120);
                int y = 180;
                int incr = 120 / 4;

                g.setColor(Color.gray);
                g.drawLine(170, 210, 170, 150);
                g.drawLine(140, 210, 140, 150);
                g.drawOval(166, 142, 8, 8);
                g.drawOval(136, 142, 8, 8);
                g.setColor(Color.darkGray);
                g.drawLine(290, 200, 340, 200);
                g.drawOval(340, 196, 8, 8);


                Font font = new Font("Times New Roman", Font.BOLD, 14);
                g.setFont(font);
                g.drawString("f", 360, 200);
                g.drawString("Output", 350, 220);

                font = new Font("Times New Roman", Font.BOLD, 10);
                g.setFont(font);
                g.setColor(Color.blue);
                g.drawString("Logic 1", 120, 130);
                g.drawString("Logic 0", 160, 130);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                for (int i = 0; i < 2; i++)
                {
                    String number = new String(java.lang.Integer.toString(i));
                    g.setColor(Color.gray);
                    if (truth[i] == 1)
                    {
                        g.drawLine(140, y, 200, y);
                        g.fillOval(137, y - 3, 6, 6);
                    }
                    else
                    {
                        g.drawLine(170, y, 200, y);
                        g.fillOval(167, y - 3, 6, 6);
                    }
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(number, 205, y + 2);
                    y = y + incr;
                }

                g.setColor(new Color(192, 192, 192));
                g.drawString("S0", 240, 260);
                g.setColor(Color.darkGray);
                g.drawString("A", 240, 320);
                g.setColor(Color.gray);
                g.drawLine(245, 270, 245, 300);
                g.drawOval(241, 300, 8, 8);

                g.setColor(Color.darkGray);
                g.drawOval(192, 250, 8, 8);
                g.drawLine(192, 254, 172, 254);
                g.drawLine(172, 254, 172, 270);
                g.drawLine(165, 270, 179, 270);
                g.drawLine(167, 274, 177, 274);
                g.drawLine(169, 278, 175, 278);
                g.drawString("G", 145, 274);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                g.setColor(Color.yellow);
                g.drawString("2:1", 235, 200);
                g.drawString("Mux", 230, 220);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                g.setColor(Color.darkGray);
                g.drawString("Y=>> " + equation, 500, 60);
                String input = new String("A           Y");
                g.drawString(input, 530, 150);

                y = 150;
                for (int i = 0; i < 2; i++)
                {
                    input = new String(i + "           " + truth[i]);
                    g.drawString(input, 532, y += 20);
                }

                break;
            case 3: // 2 Variable 4:1 Mux
                g.setColor(clr);
                g.fillRoundRect(200, 100, 120, 160, 3, 3);
                y = 120;
                incr = 160 / 6;
                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                for (int i = 0; i < 4; i++)
                {
                    String number = new String(java.lang.Integer.toString(i));
                    g.setColor(Color.gray);
                    if (truth[i] == 1)
                    {
                        g.drawLine(140, y, 200, y);
                        g.fillOval(137, y - 3, 6, 6);
                    }
                    else
                    {
                        g.drawLine(170, y, 200, y);
                        g.fillOval(167, y - 3, 6, 6);
                    }
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(number, 205, y + 2);

                    y = y + incr;
                }

                int x = 200;
                incr = 120 / 3;
                String temp1 = new String();
                String temp2 = new String();
                for (int i = 0; i < 2; i++)
                {
                    switch (i)
                    {
                        case 0:
                            temp1 = new String("A");
                            temp2 = new String("S1");
                            break;
                        case 1:
                            temp1 = new String("B");
                            temp2 = new String("S0");
                            break;
                    }

                    x = x + incr;
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(temp2, x - 5, 250);
                    g.setColor(Color.darkGray);
                    g.drawString(temp1, x - 5, 310);
                    g.setColor(Color.gray);
                    g.drawLine(x, 260, x, 290);
                    g.drawOval(x - 4, 290, 8, 8);
                }

                g.drawLine(140, 95, 140, 198);
                g.drawLine(170, 95, 170, 198);
                g.drawOval(136, 87, 8, 8);
                g.drawOval(166, 87, 8, 8);
                font = new Font("Times New Roman", Font.BOLD, 10);
                g.setFont(font);

                g.setColor(Color.darkGray);
                g.drawOval(192, 230, 8, 8);
                g.drawLine(192, 234, 172, 234);
                g.drawLine(172, 234, 172, 250);
                g.drawLine(165, 250, 179, 250);
                g.drawLine(167, 254, 177, 254);
                g.drawLine(169, 258, 175, 258);
                g.drawString("G", 145, 254);

                g.setColor(Color.blue);
                g.drawString("Logic 1", 120, 70);
                g.drawString("Logic 0", 160, 70);

                font = new Font("Times New Roman", Font.BOLD, 16);
                g.setFont(font);
                g.setColor(Color.yellow);
                g.drawString("4:1 Mux", 230, 180);

                g.setColor(Color.darkGray);
                g.drawLine(320, 180, 370, 180);
                g.drawOval(370, 176, 8, 8);
                g.drawString("f", 385, 180);
                g.drawString("Output", 360, 200);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                g.drawString("Y=>> " + equation, 500, 20);
                input = new String("A    B       Y");
                g.drawString(input, 530, 110);

                font = new Font("Times New Roman", Font.PLAIN, 12);
                g.setFont(font);
                y = 120;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        input = new String(i + "     " + j);
                        g.drawString(input, 530, y += 20);
                    }
                }

                y = 120;
                for (int i = 0; i < 4; i++)
                {
                    String output = new String("" + truth[i]);
                    g.drawString(output, 580, y += 20);
                }

                break;
            case 6: // 3 Variable 8:1 Mux
                g.setColor(clr);
                g.fillRoundRect(200, 80, 135, 230, 3, 3);
                y = 100;
                incr = 230 / 9;
                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                for (int i = 0; i < 8; i++)
                {
                    String number = new String(java.lang.Integer.toString(i));
                    g.setColor(Color.gray);
                    if (truth[i] == 1)
                    {
                        g.drawLine(140, y, 200, y);
                        g.fillOval(137, y - 3, 6, 6);
                    }
                    else
                    {
                        g.drawLine(170, y, 200, y);
                        g.fillOval(167, y - 3, 6, 6);
                    }
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(number, 205, y + 2);

                    y = y + incr;
                }

                x = 200;
                incr = 135 / 4;
                temp1 = new String();
                temp2 = new String();
                for (int i = 0; i < 3; i++)
                {
                    switch (i)
                    {
                        case 0:
                            temp1 = new String("A");
                            temp2 = new String("S2");
                            break;
                        case 1:
                            temp1 = new String("B");
                            temp2 = new String("S1");
                            break;
                        case 2:
                            temp1 = new String("C");
                            temp2 = new String("S0");
                            break;

                    }

                    x = x + incr;
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(temp2, x - 5, 300);
                    g.setColor(Color.darkGray);
                    g.drawString(temp1, x - 5, 360);
                    g.setColor(Color.gray);
                    g.drawLine(x, 310, x, 340);
                    g.drawOval(x - 4, 340, 8, 8);
                }

                g.drawLine(140, 80, 140, 275);
                g.drawLine(170, 80, 170, 275);
                g.drawOval(136, 72, 8, 8);
                g.drawOval(166, 72, 8, 8);
                font = new Font("Times New Roman", Font.BOLD, 10);
                g.setFont(font);

                g.setColor(Color.darkGray);
                g.drawOval(192, 290, 8, 8);
                g.drawLine(192, 294, 172, 294);
                g.drawLine(172, 294, 172, 310);
                g.drawLine(165, 310, 179, 310);
                g.drawLine(167, 314, 177, 314);
                g.drawLine(169, 318, 175, 318);
                g.drawString("G", 145, 314);

                g.setColor(Color.blue);
                g.drawString("Logic 1", 120, 60);
                g.drawString("Logic 0", 160, 60);

                font = new Font("Times New Roman", Font.BOLD, 16);
                g.setFont(font);
                g.setColor(Color.yellow);
                g.drawString("8:1 Mux", 240, 200);

                g.setColor(Color.darkGray);
                g.drawLine(335, 200, 380, 200);
                g.drawOval(380, 196, 8, 8);
                g.drawString("f", 400, 200);
                g.drawString("Output", 380, 220);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                g.drawString("Y=>> " + equation, 500, 30);
                input = new String("A    B    C         Y");
                g.drawString(input, 530, 90);

                font = new Font("Times New Roman", Font.PLAIN, 12);
                g.setFont(font);
                y = 100;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int k = 0; k < 2; k++)
                        {
                            input = new String(i + "     " + j + "     " + k);
                            g.drawString(input, 530, y += 20);
                        }
                    }
                }

                y = 100;
                for (int i = 0; i < 8; i++)
                {
                    String output = new String("" + truth[i]);
                    g.drawString(output, 610, y += 20);
                }

                break;
            case 10: // 4 Variable 16:1 Mux
                g.setColor(clr);
                g.fillRoundRect(200, 50, 150, 300, 3, 3);
                y = 60;
                incr = 300 / 17;
                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                for (int i = 0; i <= 15; i++)
                {
                    String number = new String(java.lang.Integer.toString(i));
                    g.setColor(Color.gray);
                    if (truth[i] == 1)
                    {
                        g.drawLine(140, y, 200, y);
                        g.fillOval(137, y - 3, 6, 6);
                    }
                    else
                    {
                        g.drawLine(170, y, 200, y);
                        g.fillOval(167, y - 3, 6, 6);
                    }
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(number, 205, y + 2);

                    y = y + incr;
                }

                x = 200;
                incr = 150 / 5;
                temp1 = new String();
                temp2 = new String();
                for (int i = 0; i < 4; i++)
                {
                    switch (i)
                    {
                        case 0:
                            temp1 = new String("A");
                            temp2 = new String("S3");
                            break;
                        case 1:
                            temp1 = new String("B");
                            temp2 = new String("S2");
                            break;
                        case 2:
                            temp1 = new String("C");
                            temp2 = new String("S1");
                            break;
                        case 3:
                            temp1 = new String("D");
                            temp2 = new String("S0");
                            break;
                    }

                    x = x + incr;
                    g.setColor(new Color(192, 192, 192));
                    g.drawString(temp2, x - 5, 340);
                    g.setColor(Color.darkGray);
                    g.drawString(temp1, x - 5, 400);
                    g.setColor(Color.gray);
                    g.drawLine(x, 350, x, 380);
                    g.drawOval(x - 4, 380, 8, 8);
                }

                g.drawLine(140, 50, 140, 315);
                g.drawLine(170, 50, 170, 315);
                g.fillOval(136, 46, 8, 8);
                g.fillOval(166, 46, 8, 8);
                font = new Font("Times New Roman", Font.BOLD, 10);
                g.setFont(font);

                g.setColor(Color.darkGray);
                g.drawOval(192, 330, 8, 8);
                g.drawLine(192, 334, 172, 334);
                g.drawLine(172, 334, 172, 350);
                g.drawLine(165, 350, 179, 350);
                g.drawLine(167, 354, 177, 354);
                g.drawLine(169, 358, 175, 358);
                g.drawString("G", 145, 354);

                g.setColor(Color.blue);
                g.drawString("Logic 1", 120, 30);
                g.drawString("Logic 0", 160, 30);

                font = new Font("Times New Roman", Font.BOLD, 16);
                g.setFont(font);
                g.setColor(Color.yellow);
                g.drawString("16:1 Mux", 240, 200);

                g.setColor(Color.darkGray);
                g.drawLine(350, 200, 400, 200);
                g.drawOval(400, 196, 8, 8);
                g.drawString("f", 420, 200);
                g.drawString("Output", 400, 220);

                font = new Font("Times New Roman", Font.BOLD, 12);
                g.setFont(font);
                g.drawString("Y=>> " + equation, 500, 20);
                input = new String("A    B    C    D            Y");
                g.drawString(input, 530, 60);

                font = new Font("Times New Roman", Font.PLAIN, 12);
                g.setFont(font);
                y = 70;
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        for (int k = 0; k < 2; k++)
                        {
                            for (int l = 0; l < 2; l++)
                            {
                                input = new String(i + "     " + j + "     " + k + "     " + l);
                                g.drawString(input, 530, y += 20);
                            }
                        }
                    }
                }

                y = 70;
                for (int i = 0; i < 16; i++)
                {
                    String output = new String("" + truth[i]);
                    g.drawString(output, 640, y += 20);
                }
        }
    }
}
