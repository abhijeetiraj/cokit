import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;


public class Chip8253 extends JFrame implements ItemListener
{
    JPanel panel;
    JTextField counter0, counter1, counter2, ctlRegister;
    int cntr0, cntr1, cntr2, CWR, initialCntr0, initialCntr1, initialCntr2;
    int halfCntr0, halfCntr1, halfCntr2;
    JRadioButton outCounter0, outCounter1, outCounter2;
    JButton clockCounter0, clockCounter1, clockCounter2;
    JToggleButton gateCounter0, gateCounter1, gateCounter2;
    String hexCWR, binaryCWR;

    String hexCounter0least, hexCounter0most, hexCounter0;
    String hexCounter1least, hexCounter1most, hexCounter1;
    String hexCounter2least, hexCounter2most, hexCounter2;

    String counterSelect, modeSelect;
    int selectCounter, selectMode;
    int counter0Gate = 1;
    int outCount;

    Chip8253(String title)
    {
        super(title);
        cntr0 = 0;
        cntr1 = 0;
        cntr2 = 0;
        CWR = 0;
        outCount = 0;
        initialCntr0 = 0;
        initialCntr1 = 0;
        initialCntr2 = 0;
        halfCntr0 = 0;
        halfCntr1 = 0;
        halfCntr2 = 0;

        Container container = new Container();
        container = getContentPane();

        panel = new JPanel();
        panel.setLayout(null);


        // Counter 0
        counter0 = new JTextField("0000H", 7);
        counter0.setSize(90, 35);
        counter0.setLocation(370 + 70, 100);
        counter0.setForeground(Color.green.brighter());
        counter0.setBackground(Color.black);
        counter0.setEditable(false);
        counter0.setHorizontalAlignment(JTextField.CENTER);
        panel.add(counter0);

        // Clock
        clockCounter0 = new JButton(createImageIcon("/buttons/clock1.gif", ""));
        clockCounter0.setPressedIcon(createImageIcon("/buttons/clock2.gif", ""));
        clockCounter0.setSize(100, 45);
        clockCounter0.setLocation(660, 70);
        clockCounter0.setActionCommand("clockCounter0");
        clockCounter0.setFocusPainted(false);
        clockCounter0.setBorderPainted(false);
        clockCounter0.setContentAreaFilled(false);
        clockCounter0.setMargin(new Insets(0, 0, 0, 0));
        clockCounter0.addActionListener(new ShowAction(this));
        panel.add(clockCounter0);

        // Out
        outCounter0 = new JRadioButton("", createImageIcon("/buttons/bulb1.gif", ""));
        outCounter0.setPressedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter0.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        outCounter0.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter0.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter0.setSize(35, 35);
        outCounter0.setLocation(620, 102);
        outCounter0.setFocusPainted(false);
        outCounter0.setBorderPainted(false);
        outCounter0.setContentAreaFilled(false);
        outCounter0.setMargin(new Insets(0, 0, 0, 0));
        outCounter0.setSelected(false);
        panel.add(outCounter0);

        // Gate
        gateCounter0 = new JToggleButton("Gate 0", false);
        gateCounter0.setSize(70, 30);
        gateCounter0.setLocation(660, 120);
        gateCounter0.addItemListener(this);
        panel.add(gateCounter0);

        // Counter 1
        counter1 = new JTextField("0000H", 7);
        counter1.setSize(90, 35);
        counter1.setLocation(370 + 70, 230);
        counter1.setForeground(Color.green.brighter());
        counter1.setBackground(Color.black);
        counter1.setEditable(false);
        counter1.setHorizontalAlignment(JTextField.CENTER);
        panel.add(counter1);

        // Clock
        clockCounter1 = new JButton(createImageIcon("/buttons/clock1.gif", ""));
        clockCounter1.setPressedIcon(createImageIcon("/buttons/clock2.gif", ""));
        clockCounter1.setSize(100, 45);
        clockCounter1.setLocation(660, 200);
        clockCounter1.setActionCommand("clockCounter1");
        clockCounter1.setFocusPainted(false);
        clockCounter1.setBorderPainted(false);
        clockCounter1.setContentAreaFilled(false);
        clockCounter1.setMargin(new Insets(0, 0, 0, 0));
        clockCounter1.addActionListener(new ShowAction(this));
        panel.add(clockCounter1);

        // Out
        outCounter1 = new JRadioButton("", createImageIcon("/buttons/bulb1.gif", ""));
        outCounter1.setPressedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter1.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        outCounter1.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter1.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter1.setSize(35, 35);
        outCounter1.setLocation(620, 232);
        outCounter1.setFocusPainted(false);
        outCounter1.setBorderPainted(false);
        outCounter1.setContentAreaFilled(false);
        outCounter1.setMargin(new Insets(0, 0, 0, 0));
        outCounter1.setSelected(false);
        panel.add(outCounter1);

        // Gate
        gateCounter1 = new JToggleButton("Gate 1", false);
        gateCounter1.setSize(70, 30);
        gateCounter1.setLocation(660, 250);
        gateCounter1.addItemListener(this);
        panel.add(gateCounter1);



        // Counter 2
        counter2 = new JTextField("0000H", 7);
        counter2.setSize(90, 35);
        counter2.setLocation(370 + 70, 360);
        counter2.setForeground(Color.green.brighter());
        counter2.setBackground(Color.black);
        counter2.setEditable(false);
        counter2.setHorizontalAlignment(JTextField.CENTER);
        panel.add(counter2);

        // Clock
        clockCounter2 = new JButton(createImageIcon("/buttons/clock1.gif", ""));
        clockCounter2.setPressedIcon(createImageIcon("/buttons/clock2.gif", ""));
        clockCounter2.setSize(100, 45);
        clockCounter2.setLocation(660, 330);
        clockCounter2.setActionCommand("clockCounter1");
        clockCounter2.setFocusPainted(false);
        clockCounter2.setBorderPainted(false);
        clockCounter2.setContentAreaFilled(false);
        clockCounter2.setMargin(new Insets(0, 0, 0, 0));
        clockCounter2.addActionListener(new ShowAction(this));
        panel.add(clockCounter2);

        // Out
        outCounter2 = new JRadioButton("", createImageIcon("/buttons/bulb1.gif", ""));
        outCounter2.setPressedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter2.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        outCounter2.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter2.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        outCounter2.setSize(35, 35);
        outCounter2.setLocation(620, 362);
        outCounter2.setFocusPainted(false);
        outCounter2.setBorderPainted(false);
        outCounter2.setContentAreaFilled(false);
        outCounter2.setMargin(new Insets(0, 0, 0, 0));
        outCounter2.setSelected(false);
        panel.add(outCounter2);

        // Gate
        gateCounter2 = new JToggleButton("Gate 2", false);
        gateCounter2.setSize(70, 30);
        gateCounter2.setLocation(660, 380);
        gateCounter2.addItemListener(this);
        panel.add(gateCounter2);


        // CWR
        ctlRegister = new JTextField("00H", 7);
        ctlRegister.setSize(90, 30);
        ctlRegister.setLocation(260 + 70, 400);
        ctlRegister.setForeground(Color.green.brighter());
        ctlRegister.setBackground(Color.black);
        ctlRegister.setEditable(false);
        ctlRegister.setHorizontalAlignment(JTextField.CENTER);
        panel.add(ctlRegister);


        design8253Panel mc = new design8253Panel();
        mc.setSize(800, 540);
        mc.setLocation(0, -20);

        panel.add(mc);
        container.add(panel);
    }



    public ImageIcon createImageIcon(String filename, String description)
    {
        String path = "/resources/images/" + filename;
        return new ImageIcon(getClass().getResource(path));
    }



    public void itemStateChanged(ItemEvent e)
    {
        if (e.getItemSelectable() == gateCounter0)
        {

            switch (selectMode)
            {
                case 0:
                    if (e.getStateChange() == 1)
                        counter0Gate = 0;
                    else
                        counter0Gate = 1;
                    break;
                case 1:
                    if (e.getStateChange() == 1)
                    {
                        // counter0Gate=0;
                    }
                    else
                    {
                        counter0Gate = 1;
                        if (cntr0 != 0)
                            cntr0 = initialCntr0 + 1;
                    }
                    break;
                case 2:
                    if (e.getStateChange() == 1)
                    {
                        outCounter0.setSelected(true);
                        counter0Gate = 0;
                    }
                    else
                    {
                        counter0Gate = 1;
                        cntr0 = initialCntr0 + 1;
                    }
                    break;
                case 3:
                    if (e.getStateChange() == 1)
                    {
                        outCounter0.setSelected(true);
                        counter0Gate = 0;
                    }
                    else
                    {
                        counter0Gate = 1;
                        cntr0 = initialCntr0 + 1;
                    }
                    break;
                case 4:
                    if (e.getStateChange() == 1)
                        counter0Gate = 0;
                    else
                        counter0Gate = 1;
                    break;
                case 5:
                    if (e.getStateChange() == 1)
                        counter0Gate = 0;
                    else
                    {
                        counter0Gate = 1;
                        cntr0 = initialCntr0 + 1;

                    }
                    break;




            }
        }
    }




    class ShowAction extends AbstractAction
    {
        Chip8253 demo;


        public ShowAction(Chip8253 demo)
        {
            this.demo = demo;
        }


        public void actionPerformed(ActionEvent e)
        {
            String str = e.getActionCommand();

            if (str.equals("clockCounter0"))
            {
                switch (selectMode)
                {
                    case 0:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            if (cntr0 < 0)
                                cntr0 = 0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);
                            if (cntr0 == 0)
                                outCounter0.setSelected(true);
                        }
                        break;
                    case 1:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            if (cntr0 < 0)
                                cntr0 = 0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);
                            outCounter0.setSelected(false);
                            if (cntr0 == 0)
                                outCounter0.setSelected(true);
                        }
                        break;
                    case 2:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);
                            outCounter0.setSelected(true);
                            if (cntr0 == 1)
                            {
                                outCounter0.setSelected(false);
                                cntr0 = initialCntr0 + 1;
                            }
                        }
                        break;
                    case 3:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);
                            if (cntr0 == halfCntr0)
                            {
                                System.out.println(cntr0 + "  " + halfCntr0);
                                if (outCounter0.isSelected() == true)
                                    outCounter0.setSelected(false);
                                else
                                    outCounter0.setSelected(true);
                            }

                            if (cntr0 == 0)
                            {
                                System.out.println(cntr0 + "  " + halfCntr0);
                                if (outCounter0.isSelected() == true)
                                    outCounter0.setSelected(false);
                                else
                                    outCounter0.setSelected(true);
                                cntr0 = initialCntr0;
                            }
                        }
                        break;
                    case 4:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);

                            if (cntr0 == 0)
                            {
                                outCounter0.setSelected(false);
                                // cntr0=initialCntr0;
                            }
                            if (cntr0 < 0)
                            {
                                outCounter0.setSelected(true);
                                // cntr0=initialCntr0;
                                counter0.setText("0000H");
                            }


                        }
                        break;
                    case 5:
                        if (counter0Gate == 1)
                        {
                            --cntr0;
                            hexCounter0 = new String(java.lang.Integer.toHexString(cntr0));
                            if (hexCounter0.length() == 1)
                                hexCounter0 = new String("000".concat(hexCounter0));
                            if (hexCounter0.length() == 2)
                                hexCounter0 = new String("00".concat(hexCounter0));
                            if (hexCounter0.length() == 3)
                                hexCounter0 = new String("0".concat(hexCounter0));
                            hexCounter0 = new String(hexCounter0.concat("H"));
                            counter0.setText(hexCounter0);

                            if (cntr0 == 0)
                                outCounter0.setSelected(false);
                            if (cntr0 < 0)
                            {
                                outCounter0.setSelected(true);
                                counter0.setText("0000H");
                            }


                        }
                        break;


                }// switch
            }// equals
        }// actionperformed
    }// showaction


    public int inData(int regA, int portAddress)
    {

        return 1;
    }

    public void outData(int regA, int portAddress)
    {
        switch (portAddress)
        {
            case 192:
                if (outCount % 2 == 0)
                {
                    hexCounter0least = new String(java.lang.Integer.toHexString(regA));
                    if (hexCounter0least.length() == 1)
                    {
                        hexCounter0least = new String("0".concat(hexCounter0least));
                        hexCounter0 = new String("00".concat(hexCounter0least));
                    }
                    else
                        hexCounter0 = new String("00".concat(hexCounter0least));
                    cntr0 = java.lang.Integer.parseInt(hexCounter0, 16);
                    initialCntr0 = cntr0;
                    if (cntr0 % 2 == 0)
                        halfCntr0 = cntr0 / 2;
                    else
                        halfCntr0 = (cntr0 + 2) / 2;
                    // ++cntr0;
                    hexCounter0 = new String(hexCounter0.concat("H"));
                    counter0.setText(hexCounter0);
                    outCount++;
                }
                else
                {
                    hexCounter0most = new String(java.lang.Integer.toHexString(regA));
                    if (hexCounter0most.length() == 1)
                    {
                        hexCounter0most = new String("0".concat(hexCounter0most));
                        hexCounter0 = new String(hexCounter0most.concat(hexCounter0least));
                    }
                    else
                        hexCounter0 = new String(hexCounter0most.concat(hexCounter0least));
                    cntr0 = java.lang.Integer.parseInt(hexCounter0, 16);
                    initialCntr0 = cntr0;
                    if (cntr0 % 2 == 0)
                        halfCntr0 = cntr0 / 2;
                    else
                        halfCntr0 = (cntr0 - 1) / 2;
                    hexCounter0 = new String(hexCounter0.concat("H"));
                    counter0.setText(hexCounter0);
                    outCount++;
                }

                break;
            case 193:
                break;
            case 194:
                break;
            case 195:
                binaryCWR = toBinary(regA);
                CWR = regA;
                hexCWR = java.lang.Integer.toHexString(CWR);
                if (hexCWR.length() == 1)
                    hexCWR = new String("0".concat(hexCWR));
                hexCWR = new String(hexCWR.concat("H"));
                ctlRegister.setText(hexCWR);

                counterSelect = new String(binaryCWR.substring(0, 2));
                selectCounter = java.lang.Integer.parseInt(counterSelect, 2);

                modeSelect = new String(binaryCWR.substring(4, 7));
                selectMode = java.lang.Integer.parseInt(modeSelect, 2);

                switch (selectMode)
                {
                    case 0:
                        outCounter0.setSelected(false);
                        counter0Gate = 1;
                        break;
                    case 1:
                        outCounter0.setSelected(true);
                        counter0Gate = 0;
                        gateCounter0.setSelected(true);
                        break;
                    case 2:
                        outCounter0.setSelected(true);
                        counter0Gate = 1;
                        gateCounter0.setSelected(false);
                        break;
                    case 3:
                        outCounter0.setSelected(true);
                        counter0Gate = 1;
                        gateCounter0.setSelected(false);
                        break;
                    case 4:
                        outCounter0.setSelected(true);
                        counter0Gate = 1;
                        gateCounter0.setSelected(false);
                        break;
                    case 5:
                        outCounter0.setSelected(true);
                        counter0Gate = 1;
                        gateCounter0.setSelected(false);
                        break;


                }


                switch (selectCounter)
                {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }


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





}

//

class design8253Panel extends JPanel
{

    design8253Panel()
    {
        repaint();
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 800, 800);
        g.setColor(Color.white);// new Color(211,211,169));
        g.fillRect(5, 25, 760, 505);

        g.setColor(new Color(100, 110, 120));
        g.fillRect(230 + 70, 70, 250, 400);

        // 74373
        g.setColor(Color.gray);
        g.fillRect(40 + 70, 60, 80, 110);

        // 8205
        g.fillRect(40 + 70, 270, 70, 100);

        // 8205
        g.setColor(Color.gray);
        g.fillRect(50 + 70, 420, 70, 100);


        g.setColor(new Color(128, 255, 255));
        g.drawString("Counter 0(C0H)", 370 + 70, 115);
        g.drawString("Counter 1(C1H)", 370 + 70, 245);
        g.drawString("Counter 2(C2H)", 370 + 70, 375);
        g.drawString("Control Reg.(C3H)", 260 + 70, 415);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        g.drawString("A0", 235 + 70, 100);
        g.drawString("A1", 235 + 70, 120);

        g.drawString("D0", 235 + 70, 190);
        g.drawString("D7", 235 + 70, 210);

        g.drawString("RESET", 235 + 70, 240);
        g.drawLine(235 + 70, 305, 245 + 70, 305);
        g.drawString("RD", 235 + 70, 315);
        g.drawLine(235 + 70, 325, 245 + 70, 325);
        g.drawString("WR", 235 + 70, 335);

        g.drawLine(235 + 70, 455, 250 + 70, 455);
        g.drawString("CS", 235 + 70, 465);

        g.drawString("1 D", 45 + 70, 100);
        g.drawString("8 D", 45 + 70, 125);

        g.drawString("1 Q", 95 + 70, 100);
        g.drawString("8 Q", 95 + 70, 125);

        g.drawString("G", 45 + 70, 165);
        g.drawLine(95 + 70, 155, 110 + 70, 155);
        g.drawString("OC", 95 + 70, 165);

        g.drawLine(50 + 70, 275, 60 + 70, 275);
        g.drawString("E1", 50 + 70, 285);
        g.drawLine(70 + 70, 275, 80 + 70, 275);
        g.drawString("E2", 70 + 70, 285);
        g.drawString("E3", 90 + 70, 285);

        g.drawString("A0", 45 + 70, 300);
        g.drawString("A1", 45 + 70, 320);
        g.drawString("A2", 45 + 70, 340);

        g.drawString("O1", 90 + 70, 310);
        g.drawString("O2", 90 + 70, 330);

        g.drawLine(60 + 70, 425, 70 + 70, 425);
        g.drawString("E1", 60 + 70, 435);
        g.drawLine(80 + 70, 425, 90 + 70, 425);
        g.drawString("E2", 80 + 70, 435);
        g.drawString("E3", 100 + 70, 435);

        g.drawString("A4", 55 + 70, 450);
        g.drawString("A3", 55 + 70, 470);
        g.drawString("A2", 55 + 70, 490);

        g.drawString("O0", 100 + 70, 460);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        g.setColor(new Color(172, 172, 89));

        // ad0-ad7
        g.drawLine(5, 100, 30 + 70, 100);
        g.drawLine(5, 120, 30 + 70, 120);
        g.drawLine(30 + 70, 100, 30 + 70, 90);
        g.drawLine(30 + 70, 120, 30 + 70, 130);
        g.drawLine(30 + 70, 90, 40 + 70, 110);
        g.drawLine(30 + 70, 130, 40 + 70, 110);

        g.drawLine(120 + 70, 100, 218 + 70, 100);
        g.drawLine(120 + 70, 120, 218 + 70, 120);
        g.drawLine(218 + 70, 100, 218 + 70, 90);
        g.drawLine(218 + 70, 120, 218 + 70, 130);
        g.drawLine(218 + 70, 90, 230 + 70, 110);
        g.drawLine(218 + 70, 130, 230 + 70, 110);

        g.fillOval(17 + 70, 97, 6, 6);
        g.drawLine(20 + 70, 100, 20 + 70, 190);
        g.fillOval(7 + 70, 117, 6, 6);
        g.drawLine(10 + 70, 120, 10 + 70, 210);
        g.drawLine(20 + 70, 190, 218 + 70, 190);
        g.drawLine(10 + 70, 210, 218 + 70, 210);
        g.drawLine(218 + 70, 190, 218 + 70, 180);
        g.drawLine(218 + 70, 210, 218 + 70, 220);
        g.drawLine(218 + 70, 180, 230 + 70, 200);
        g.drawLine(218 + 70, 220, 230 + 70, 200);

        // ALE
        g.drawLine(5, 160, 40 + 70, 160);

        // Vcc
        g.drawLine(70 + 70, 60, 70 + 70, 45);
        g.drawOval(66 + 70, 37, 8, 8);

        // OC
        g.drawLine(100 + 70, 170, 100 + 70, 180);
        g.drawLine(95 + 70, 180, 105 + 70, 180);
        g.drawLine(97 + 70, 184, 103 + 70, 184);

        // RESET
        g.drawLine(5, 235, 230 + 70, 235);
        g.drawString(">", 220 + 70, 240);

        // IO/M,RD,WR
        g.drawLine(5, 295, 40 + 70, 295);
        g.drawLine(5, 315, 40 + 70, 315);
        g.drawLine(5, 335, 40 + 70, 335);

        // MEMW,MEMR
        g.drawOval(110 + 70, 305, 8, 8);
        g.drawOval(110 + 70, 325, 8, 8);
        g.drawLine(118 + 70, 309, 230 + 70, 309);
        g.drawLine(118 + 70, 329, 230 + 70, 329);

        // E1,E2,E3
        g.drawOval(50 + 70, 262, 8, 8);
        g.drawOval(70 + 70, 262, 8, 8);
        g.drawLine(54 + 70, 262, 54 + 70, 260);
        g.drawLine(54 + 70, 260, 24 + 70, 260);
        g.drawLine(74 + 70, 262, 74 + 70, 250);
        g.drawLine(74 + 70, 250, 24 + 70, 250);
        g.drawLine(24 + 70, 250, 24 + 70, 270);
        g.drawLine(14 + 70, 270, 34 + 70, 270);
        g.drawLine(17 + 70, 272, 31 + 70, 272);
        g.drawLine(20 + 70, 274, 28 + 70, 274);

        // Vcc
        g.drawLine(105 + 70, 270, 105 + 70, 250);
        g.drawOval(101 + 70, 242, 8, 8);
        g.drawLine(90 + 70, 270, 90 + 70, 260);
        g.drawLine(90 + 70, 260, 105 + 70, 260);

        // A2,A3,A4
        g.drawLine(5, 450, 50 + 70, 450);
        g.drawLine(5, 470, 50 + 70, 470);
        g.drawLine(5, 490, 50 + 70, 490);

        // A13,14,15
        g.drawOval(60 + 70, 412, 8, 8);
        g.drawOval(80 + 70, 412, 8, 8);
        g.drawLine(64 + 70, 412, 64 + 70, 408);
        g.drawLine(64 + 70, 408, 5, 408);
        g.drawLine(84 + 70, 412, 84 + 70, 395);
        g.drawLine(84 + 70, 395, 5, 395);
        g.drawLine(104 + 70, 420, 104 + 70, 382);
        g.drawLine(104 + 70, 382, 5, 382);

        // Vcc
        g.drawLine(114 + 70, 420, 114 + 70, 400);
        g.drawOval(110 + 70, 392, 8, 8);

        // O4
        g.drawOval(120 + 70, 454, 8, 8);
        g.drawLine(128 + 70, 458, 222 + 70, 458);
        g.drawOval(222 + 70, 454, 8, 8);

        // out,clk,gate
        g.drawLine(550, 140, 625, 140);
        g.drawString(">", 620, 145);
        g.drawLine(550, 120, 700, 120);
        g.drawString("<", 550, 125);
        g.drawLine(550, 162, 700, 162);
        g.drawString("<", 550, 167);

        // out,clk,gate
        g.drawLine(550, 270, 625, 270);
        g.drawString(">", 620, 275);
        g.drawLine(550, 250, 700, 250);
        g.drawString("<", 550, 255);
        g.drawLine(550, 292, 700, 292);
        g.drawString("<", 550, 297);

        // out,clk,gate
        g.drawLine(550, 400, 625, 400);
        g.drawString(">", 620, 405);
        g.drawLine(550, 380, 700, 380);
        g.drawString("<", 550, 385);
        g.drawLine(550, 422, 700, 422);
        g.drawString("<", 550, 427);


        g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        g.setColor(Color.darkGray);

        g.drawString("AD0-AD7", 5 + 20, 95);
        g.drawString("RESET OUT", 5 + 20, 230);
        g.drawString("ALE", 5 + 20, 155);
        g.drawLine(20 + 20, 285, 30 + 20, 285);
        g.drawString("IO/M", 6 + 20, 294);
        g.drawLine(6 + 20, 304, 20 + 20, 304);
        g.drawString("RD", 6 + 20, 314);
        g.drawLine(6 + 20, 324, 20 + 20, 324);
        g.drawString("WR", 6 + 20, 334);

        g.drawString("A15", 6 + 20, 380);
        g.drawString("A14", 6 + 20, 393);
        g.drawString("A13", 6 + 20, 406);

        g.drawString("A4", 6 + 20, 447);
        g.drawString("A3", 6 + 20, 467);
        g.drawString("A2", 6 + 20, 487);

        g.drawLine(160 + 70, 295, 195 + 50, 295);
        g.drawString("IOR", 160 + 70, 305);
        g.drawLine(160 + 70, 315, 195 + 50, 315);
        g.drawString("IOW", 160 + 70, 325);


        g.setFont(new Font("Times New Roman", Font.BOLD, 12));
        g.setColor(Color.blue);
        // g.drawString("8205",105,450);

        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.setColor(new Color(128, 128, 0));
        g.drawString("8253", 400, 60);
        g.drawLine(400, 63, 375 + 70, 63);

    }
}
