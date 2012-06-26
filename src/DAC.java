import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * DAC.java
 * 
 * @version 1.1 01/1/2002
 * @author Abhijeet Iraj
 * @author Nilesh Shirke
 */

public class DAC extends DemoModule implements ActionListener
{

    CoKit cokit;
    screen s;
    float vref = 26f, Rref = 5f;;
    float a[] = new float[9];
    float current = 0f;
    float rbalance = 2.5f;
    int input = 255;
    float output;
    float outputvolt = 0f;
    int outvolt;
    boolean flag, go;
    JTextField R14, vRef, Rf, outputField;
    JButton outputClick;

    public static void main(String[] args)
    {
        DAC demo = new DAC(null);
        demo.mainImpl();
    }

    public DAC(CoKit cokit)
    {
        super(cokit, "DAC", "toolbar/DAC.gif");

        JPanel demo = getDemoPanel();
        demo.setLayout(null);
        demo.setBackground(Color.white);

        s = new screen();
        s.setSize(1000, 1000);
        s.setLocation(0, 40);

        R14 = new JTextField(10);
        R14.setBackground(Color.black);
        R14.setForeground(Color.green);
        R14.setBounds(430, 130, 60, 30);
        demo.add(R14);

        vRef = new JTextField(10);
        vRef.setBackground(Color.black);
        vRef.setForeground(Color.green);
        vRef.setBounds(560, 130, 60, 30);
        demo.add(vRef);

        Rf = new JTextField(10);
        Rf.setBackground(Color.black);
        Rf.setForeground(Color.green);
        Rf.setBounds(550, 225, 60, 30);
        demo.add(Rf);

        // outputField
        outputField = new JTextField(10);
        outputField.setBackground(Color.black);
        outputField.setForeground(Color.green);
        outputField.setBounds(675, 275, 60, 30);
        demo.add(outputField);

        outputClick = new JButton(createImageIcon("buttons/alu1.gif", ""));
        outputClick.setPressedIcon(createImageIcon("buttons/alu2.gif", ""));
        outputClick.setSize(140, 100);
        outputClick.setLocation(640, 320);
        outputClick.setActionCommand("output");
        outputClick.setFocusPainted(false);
        outputClick.setBorderPainted(false);
        outputClick.setContentAreaFilled(false);
        outputClick.setMargin(new Insets(0, 0, 0, 0));
        outputClick.addActionListener(this);
        demo.add(outputClick);




        demo.add(s);
    }


    public void actionPerformed(ActionEvent a)
    {
        if (a.getSource() == outputClick)
        {
            String strR14 = R14.getText();
            String strvRef = vRef.getText();
            String strRf = Rf.getText();

            float rtake = 0.0f, vtake = 0.0f, Rref1 = 0.0f;

            try
            {
                rtake = java.lang.Float.parseFloat(strR14);
                vtake = java.lang.Float.parseFloat(strvRef);
                Rref1 = java.lang.Float.parseFloat(strRf);

            }
            catch (NumberFormatException e)
            {
            }
            rtake = rtake * 1000;
            Rref1 = Rref1 * 1000;
            float outputOfDac = select(vtake, rtake, Rref1);

            outputField.setText("" + outputOfDac);

        }
    }




    public float select(float vtake, float rtake, float Rref1)
    {
        float a1[] = new float[9];
        float current1 = 0f;
        float rbalance1 = 2.5f;
        int input1 = 255;
        float output1 = 0f, output2 = 0f;
        float outputvolt1 = 0f;



        /*
         * for(int i=8;i>=1;i--) { a1[i]=(float)input1%2;input1=input1/2; }
         * 
         * for(int i=8;i>=1;i--) { if(a1[i]==1) {current1=current1+(float)(a1[i]/Math.pow(2,i));}
         * 
         * }
         */

        output2 = vtake / rtake;
        output1 = (output2 * Rref1 * 255) / 256;
        System.out.println("output1=" + output1);
        outputvolt1 = output1 * Rref1;
        vref = vtake;
        rbalance = rtake;
        Rref = Rref1;
        return output1;
    } // select ends





    /*
     * public int inData(int regA,int portAddress) { // System.out.println("In ac:"+ac + "Port" + portAddress); //
     * portid=portAddress; // in=1; //out=0; //mc.inData(); // System.out.println("Ac = " + ac); // return ac; }
     */

    public synchronized void outData(int regA, int portAddress)
    {
        System.out.println("Out ac:" + regA + "Port" + portAddress);
        // portid=portAddress;
        // input=regA;
        System.out.println("DAC" + regA);

        // outData(regA);//outvolt;
        // mc.inData();
    }




    class screen extends JPanel
    {

        public screen()
        {


        }


        public void drawCircuit(Graphics g)
        {
            g.setColor(new Color(122, 122, 61));
            g.fillRect(200, 70, 200, 300);

            int y = 70;
            int incr = 300 / 9;
            int incr1 = incr;
            String str = new String();

            g.setFont(new Font("Times New Roman", Font.BOLD, 12));


            for (int i = 0; i < 8; i++)
            {
                switch (i)
                {
                    case 0:
                        str = new String("D7                                    A1");
                        break;
                    case 1:
                        str = new String("D6                                    A2");
                        break;
                    case 2:
                        str = new String("D5                                    A3");
                        break;
                    case 3:
                        str = new String("D4                                    A4");
                        break;
                    case 4:
                        str = new String("D3                                    A5");
                        break;
                    case 5:
                        str = new String("D2                                    A6");
                        break;
                    case 6:
                        str = new String("D1                                    A7");
                        break;
                    case 7:
                        str = new String("D0                                    A8");
                        break;
                }
                g.setColor(Color.gray);
                g.drawLine(200, y + incr, 100, y + incr);
                g.setColor(Color.blue);
                g.drawString(str, 80, y + incr + 5);
                incr += incr1;
            }

            g.setColor(Color.gray);
            g.drawLine(300, 70, 300, 20);
            g.drawOval(296, 12, 8, 8);

            g.drawLine(200, 370, 200, 420);
            g.drawLine(230, 370, 230, 450);
            g.fillOval(227, 450, 6, 6);

            g.drawLine(200, 420, 212, 420);
            g.drawLine(218, 420, 230, 420);
            g.drawLine(212, 415, 212, 425);
            g.drawLine(218, 415, 218, 425);

            g.drawLine(300, 370, 300, 440);
            g.drawLine(290, 440, 310, 440);
            g.drawLine(293, 443, 307, 443);
            g.drawLine(296, 446, 304, 446);

            g.drawLine(270, 370, 270, 400);
            g.drawLine(270, 400, 300, 400);

            g.drawLine(400, 350, 430, 350);
            g.drawLine(430, 350, 430, 380);
            g.drawString(">", 430, 387);
            g.drawString("<", 430, 390);
            g.drawString(">", 430, 393);
            g.drawString("<", 430, 396);
            g.drawString(">", 430, 399);
            g.drawLine(430, 397, 430, 440);
            g.drawLine(420, 440, 440, 440);
            g.drawLine(423, 443, 437, 443);
            g.drawLine(426, 443, 434, 443);
            g.drawLine(429, 446, 431, 446);

            g.drawLine(400, 100, 550, 100);
            g.fillOval(550, 97, 6, 6);
            g.drawLine(400, 250, 570, 250);
            g.drawLine(520, 100, 520, 250);

            g.drawLine(570, 225, 570, 275);
            g.drawLine(570, 225, 600, 250);
            g.drawLine(570, 275, 600, 250);

            g.drawLine(600, 250, 630, 250);

            g.drawLine(540, 250, 540, 200);
            g.drawLine(540, 200, 620, 200);
            g.drawLine(620, 200, 620, 250);

            g.drawLine(550, 270, 550, 300);
            g.drawLine(550, 270, 570, 270);

            g.drawLine(540, 300, 560, 300);
            g.drawLine(543, 303, 557, 303);

            g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            g.setColor(new Color(111, 111, 255));
            g.drawString("Vcc  +5 V", 300, 10);
            g.drawString("15 pF", 200, 435);
            g.drawString("Vee", 240, 465);
            g.drawString("-15 V", 300, 470);
            g.drawString("R15 2.5K", 440, 400);
            g.drawString("V Ref  +5 V", 570, 105);
            g.drawString("V0", 640, 255);
            g.drawString("-", 575, 250);
            g.drawString("+", 570, 270);
            g.drawString("741", 575, 260);


        }



        public void paint(Graphics g)
        {

            g.setColor(Color.white);
            g.fillRect(0, 0, 1000, 1000);

            drawCircuit(g);
            //


            /*
             * while(flag) //here organise dac for decided values { //select(1,2,3,g); //if(go) {flag=false;} //}//
             * while ends
             * 
             * for(int i=8;i>=1;i--) { a[i]=(float)input%2;input=input/2; } for(int i=8;i>=1;i--) { if(a[i]==1)
             * {current=current+(float)(a[i]/Math.pow(2,i));}
             * 
             * }
             * 
             * output=(vref/rbalance)*(current); outputvolt=output*Rref; //System.out.println("outputvolt"+outputvolt);
             * outvolt=(int)(outputvolt); g.setColor(Color.red); //g.drawString(""+outvolt,400,400);
             */
        }// paint ends

    }// screen

}
