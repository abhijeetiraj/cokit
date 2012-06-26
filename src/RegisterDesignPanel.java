import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;


class RegisterDesignPanel extends JPanel
{

    Thread t;
    int clock = 0;
    Color high = new Color(0, 128, 255);
    Color low = new Color(183, 183, 183);// Color(255,140,0);
    int clr[] = new int[8];
    int flipflop[] = new int[8];
    Color flpColor[] = new Color[8];
    Color color[] = new Color[8];
    String binaryInput = new String();
    String colorStr = new String();
    String parInput = new String();
    Color inputColor[] = new Color[8];
    Color preset = low;
    Color clear = high;

    public RegisterDesignPanel()
    {
        for (int i = 0; i < 8; i++)
        {
            clr[i] = 0;
            color[i] = low;
            flipflop[i] = 0;
            flpColor[i] = low;
        }
        repaint();
    }

    public void applyData(String inputData)
    {

        int check = 0;
        try
        {
            check = java.lang.Integer.parseInt(inputData, 2);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Error: " + check);
        }

        System.out.println(check + " " + binaryInput);
        binaryInput = new String(toBinary(check));
        colorStr = new String("00000000");
        parInput = new String(toBinary(check));

        for (int i = 0; i < 8; i++)
        {
            if (binaryInput.charAt(i) == '1')
                inputColor[i] = high;
            else
                inputColor[i] = low;
        }

    }

    public String toBinary(int regA)
    {
        String binary = new String(java.lang.Integer.toBinaryString(regA));
        if (binary.length() == 1)
            binary = new String("0000000".concat(binary));
        if (binary.length() == 2)
            binary = new String("000000".concat(binary));
        if (binary.length() == 3)
            binary = new String("00000".concat(binary));
        if (binary.length() == 4)
            binary = new String("0000".concat(binary));
        if (binary.length() == 5)
            binary = new String("000".concat(binary));
        if (binary.length() == 6)
            binary = new String("00".concat(binary));
        if (binary.length() == 7)
            binary = new String("0".concat(binary));
        return binary;
    }


    public void giveClock()
    {

        if (clock == 8)
            clock = 0;

        colorStr = new String(binaryInput.substring(7, 8) + colorStr.substring(0, 7));
        binaryInput = new String("0" + binaryInput.substring(0, 7));


        System.out.println("colorstr: " + colorStr + " binaryinput: " + binaryInput);
        System.out.println(clock);

        for (int i = 0; i < 8; i++)
        {
            if (colorStr.charAt(i) == '1')
                flpColor[i] = high;
            else
                flpColor[i] = low;
        }

        repaint();
        System.out.println("Clock paint");
        clock++;

    }


    public void toggleClear(int state)
    {
        System.out.println("claer");
        if (state == 1)
        {
            clear = low;
            colorStr = new String("00000000");

            for (int i = 0; i < 8; i++)
            {
                if (colorStr.charAt(i) == '1')
                    flpColor[i] = high;
                else
                    flpColor[i] = low;
            }
        }
        else
        {
            clear = high;
        }

        repaint();

    }


    public void togglePreset(int state)
    {

        if (state == 1)
        {
            System.out.println("preset");
            System.out.println(binaryInput);
            colorStr = new String(binaryInput);
            preset = high;
            colorStr = new String(parInput);
            binaryInput = new String("00000000");

            for (int i = 0; i < 8; i++)
            {
                if (parInput.charAt(i) == '1')
                    flpColor[i] = high;
                else
                    flpColor[i] = low;
            }
        }
        else
        {
            preset = low;
        }

        repaint();
    }


    public void changePinput(int D0, int D1, int D2, int D3, int D4, int D5, int D6, int D7)
    {
        if (D0 == 1)
            inputColor[0] = high;
        else
            inputColor[0] = low;

        if (D1 == 1)
            inputColor[1] = high;
        else
            inputColor[1] = low;

        if (D2 == 1)
            inputColor[2] = high;
        else
            inputColor[2] = low;

        if (D0 == 3)
            inputColor[3] = high;
        else
            inputColor[3] = low;

        if (D0 == 4)
            inputColor[4] = high;
        else
            inputColor[4] = low;

        if (D0 == 5)
            inputColor[5] = high;
        else
            inputColor[5] = low;

        if (D0 == 6)
            inputColor[6] = high;
        else
            inputColor[6] = low;

        if (D0 == 7)
            inputColor[7] = high;
        else
            inputColor[7] = low;
        repaint();

    }

    public void paint(Graphics g)
    {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 100, 50);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
        g.setColor(Color.green);

        int x = 100, y = 230;
        for (int i = 0; i < 8; i++)
        {
            drawFlipFlop(g, x, y, i);
            x = x + 80;
        }

        g.setColor(flpColor[0]);
        g.drawLine(40, y + 20, 100, y + 20);
        g.drawOval(32, y + 16, 8, 8);
        g.drawLine(50, y + 20, 50, y + 35);

        g.drawLine(45, y + 35, 55, y + 35);
        g.drawLine(45, y + 35, 50, y + 45);
        g.drawLine(55, y + 35, 50, y + 45);
        g.drawOval(46, y + 45, 8, 8);
        if (flpColor[0] == high)
            g.setColor(low);
        else
            g.setColor(high);
        g.drawLine(50, y + 53, 50, y + 80);
        g.drawLine(50, y + 80, 100, y + 80);

        g.setColor(Color.yellow);
        g.drawOval(740, 245, 8, 8);

    }

    void drawFlipFlop(Graphics g, int x, int y, int index)
    {
        String strS = new String();
        String strR = new String();
        String strQ = new String();
        String strQneg = new String();
        String flpflp = new String();
        String pin = new String();

        switch (index)
        {
            case 0:
                strS = new String("S7");
                strR = new String("R7");
                strQ = new String("Q7");
                strQneg = new String("~Q7");
                flpflp = new String("FF7");
                pin = new String("Di7");
                break;
            case 1:
                strS = new String("S6");
                strR = new String("R6");
                strQ = new String("Q6");
                strQneg = new String("~Q6");
                flpflp = new String("FF6");
                pin = new String("Di6");
                break;
            case 2:
                strS = new String("S5");
                strR = new String("R5");
                strQ = new String("Q5");
                strQneg = new String("~Q5");
                flpflp = new String("FF5");
                pin = new String("Di5");
                break;
            case 3:
                strS = new String("S4");
                strR = new String("R4");
                strQ = new String("Q4");
                strQneg = new String("~Q4");
                flpflp = new String("FF4");
                pin = new String("Di4");
                break;
            case 4:
                strS = new String("S3");
                strR = new String("R3");
                strQ = new String("Q3");
                strQneg = new String("~Q3");
                flpflp = new String("FF3");
                pin = new String("Di3");
                break;
            case 5:
                strS = new String("S2");
                strR = new String("R2");
                strQ = new String("Q2");
                strQneg = new String("~Q2");
                flpflp = new String("FF2");
                pin = new String("Di2");
                break;
            case 6:
                strS = new String("S1");
                strR = new String("R1");
                strQ = new String("Q1");
                strQneg = new String("~Q1");
                flpflp = new String("FF1");
                pin = new String("Di1");
                break;
            case 7:
                strS = new String("S0");
                strR = new String("R0");
                strQ = new String("Q0");
                strQneg = new String("~Q0");
                flpflp = new String("FF0");
                pin = new String("Di0");
                break;

        }


        g.setColor(flpColor[index]);
        g.fillRoundRect(x, y, 60, 100, 4, 4);

        // g.setColor(high);
        g.drawOval(x + 31, y - 8, 8, 8);
        g.drawLine(x + 35, y - 8, x + 35, y - 28);
        g.drawOval(x + 31, y - 36, 8, 8);

        g.setColor(new Color(122, 122, 61));
        g.fillRect(x + 15, y - 80, 40, 25);
        g.fillOval(x + 15, y - 80, 40, 45);

        g.setColor(preset);

        g.drawLine(x + 45, y - 80, x + 45, y - 125);
        g.fillOval(x + 42, y - 128, 6, 6);
        g.drawLine(x + 45, y - 125, x - 45, y - 125);

        g.setColor(inputColor[index]);
        g.drawLine(x + 25, y - 80, x + 25, y - 160); // gate
        g.drawOval(x + 21, y - 168, 8, 8);

        g.setColor(clear);
        g.drawOval(x + 31, y + 100, 8, 8);
        g.drawLine(x + 35, y + 108, x + 35, y + 128);
        g.fillOval(x + 32, y + 125, 6, 6);
        g.drawLine(x + 35, y + 128, x - 45, y + 128);

        g.setColor(flpColor[index]);
        g.drawLine(x + 60, y + 20, x + 80, y + 20);
        if (flpColor[index] == high)
            g.setColor(low);
        else
            g.setColor(high);
        g.drawLine(x + 60, y + 80, x + 80, y + 80);

        g.setColor(flpColor[index]);
        g.drawLine(x + 70, y + 20, x + 70, y - 180);
        g.drawOval(x + 66, y - 188, 8, 8);

        g.setColor(Color.green);
        g.drawOval(x - 8, y + 46, 8, 8);
        g.drawLine(x, y + 46, x + 10, y + 50);
        g.drawLine(x, y + 54, x + 10, y + 50);
        g.drawLine(x - 8, y + 50, x - 13, y + 50);
        g.drawLine(x - 13, y + 50, x - 13, y + 175);
        g.drawLine(x - 13, y + 175, x - 93, y + 175);

        g.setColor(new Color(110, 228, 128));
        g.setFont(new Font("Times New Roman", Font.BOLD, 10));
        g.drawString(strS, x + 5, y + 24);
        g.drawString(strR, x + 5, y + 80);
        g.drawString(strQ, x + 42, y + 24);
        g.drawString(strQneg, x + 42, y + 80);
        g.drawString(flpflp, x + 22, y + 54);
        g.drawString("Pr", x + 32, y + 15);
        g.drawString("Cr", x + 32, y + 95);
        g.drawString(strQ, x + 63, y - 200);
        g.drawString(pin, x + 20, y - 180);


    }
}


class waveformDesignPanel extends JPanel
{

    waveformDesignPanel()
    {

    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);
        g.setColor(Color.yellow);
        Font font = new Font("Times New Roman", Font.BOLD, 12);
        g.setFont(font);
    }


}
