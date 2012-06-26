import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class Chip8255 extends JFrame
{
    JTextField aPort, bPort, cPort, ctlRegister;
    public int portA, portB, portC, CWR;
    JCheckBox portAzero, portAone, portAtwo, portAthree, portAfour, portAfive, portAsix, portAseven;
    JCheckBox portBzero, portBone, portBtwo, portBthree, portBfour, portBfive, portBsix, portBseven;
    JCheckBox portCzero, portCone, portCtwo, portCthree, portCfour, portCfive, portCsix, portCseven;
    JPanel panel;
    String binaryRegC = new String("00000000");
    String hexCWR, hexRegA, hexRegB, hexRegC, modeSelectGroupA, modeSelectGroupB;
    String binaryCWR, binaryRegA, binaryRegB;
    int selectModeGroupA, selectModeGroupB;

    Chip8255(String title)
    {
        super(title);
        portA = 80;
        portB = 81;
        portC = 82;
        CWR = 0;

        Container container = new Container();
        container = getContentPane();

        panel = new JPanel();
        panel.setLayout(null);

        aPort = new JTextField("A-00H", 7);
        aPort.setSize(90, 35);
        aPort.setLocation(370, 100);
        aPort.setForeground(Color.green.brighter());
        aPort.setBackground(Color.black);
        aPort.setEditable(false);
        aPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(aPort);

        bPort = new JTextField("B-00H", 7);
        bPort.setSize(90, 35);
        bPort.setLocation(370, 200);
        bPort.setForeground(Color.green.brighter());
        bPort.setBackground(Color.black);
        bPort.setEditable(false);
        bPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(bPort);

        cPort = new JTextField("C-00H", 7);
        cPort.setSize(90, 35);
        cPort.setLocation(370, 300);
        cPort.setForeground(Color.green.brighter());
        cPort.setBackground(Color.black);
        cPort.setEditable(false);
        cPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(cPort);


        ctlRegister = new JTextField("00H", 7);
        ctlRegister.setSize(90, 30);
        ctlRegister.setLocation(260, 400);
        ctlRegister.setForeground(Color.green.brighter());
        ctlRegister.setBackground(Color.black);
        ctlRegister.setEditable(false);
        ctlRegister.setHorizontalAlignment(JTextField.CENTER);
        panel.add(ctlRegister);

        // port A
        portAzero = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAzero.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAzero.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAzero.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAzero.setSize(30, 40);
        portAzero.setLocation(500, 100);
        portAzero.setBackground(Color.white);
        panel.add(portAzero);

        portAone = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAone.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAone.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAone.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAone.setSize(30, 40);
        portAone.setLocation(530, 100);
        portAone.setBackground(Color.white);
        panel.add(portAone);

        portAtwo = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAtwo.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAtwo.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAtwo.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAtwo.setSize(30, 40);
        portAtwo.setLocation(560, 100);
        portAtwo.setBackground(Color.white);
        panel.add(portAtwo);

        portAthree = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAthree.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAthree.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAthree.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAthree.setSize(30, 40);
        portAthree.setLocation(590, 100);
        portAthree.setBackground(Color.white);
        panel.add(portAthree);

        portAfour = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAfour.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAfour.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAfour.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAfour.setSize(30, 40);
        portAfour.setLocation(620, 100);
        portAfour.setBackground(Color.white);
        panel.add(portAfour);

        portAfive = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAfive.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAfive.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAfive.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAfive.setSize(30, 40);
        portAfive.setLocation(650, 100);
        portAfive.setBackground(Color.white);
        panel.add(portAfive);

        portAsix = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAsix.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAsix.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAsix.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAsix.setSize(30, 40);
        portAsix.setLocation(680, 100);
        portAsix.setBackground(Color.white);
        panel.add(portAsix);

        portAseven = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portAseven.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portAseven.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAseven.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portAseven.setSize(30, 40);
        portAseven.setLocation(710, 100);
        portAseven.setBackground(Color.white);
        panel.add(portAseven);

        // port B
        portBzero = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBzero.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBzero.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBzero.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBzero.setSize(30, 40);
        portBzero.setLocation(500, 200);
        portBzero.setBackground(Color.white);
        panel.add(portBzero);

        portBone = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBone.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBone.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBone.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBone.setSize(30, 40);
        portBone.setLocation(530, 200);
        portBone.setBackground(Color.white);
        panel.add(portBone);

        portBtwo = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBtwo.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBtwo.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBtwo.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBtwo.setSize(30, 40);
        portBtwo.setLocation(560, 200);
        portBtwo.setBackground(Color.white);
        panel.add(portBtwo);

        portBthree = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBthree.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBthree.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBthree.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBthree.setSize(30, 40);
        portBthree.setLocation(590, 200);
        portBthree.setBackground(Color.white);
        panel.add(portBthree);

        portBfour = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBfour.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBfour.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBfour.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBfour.setSize(30, 40);
        portBfour.setLocation(620, 200);
        portBfour.setBackground(Color.white);
        panel.add(portBfour);

        portBfive = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBfive.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBfive.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBfive.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBfive.setSize(30, 40);
        portBfive.setLocation(650, 200);
        portBfive.setBackground(Color.white);
        panel.add(portBfive);

        portBsix = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBsix.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBsix.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBsix.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBsix.setSize(30, 40);
        portBsix.setLocation(680, 200);
        portBsix.setBackground(Color.white);
        panel.add(portBsix);

        portBseven = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portBseven.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portBseven.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBseven.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portBseven.setSize(30, 40);
        portBseven.setLocation(710, 200);
        portBseven.setBackground(Color.white);
        panel.add(portBseven);

        // port C
        portCzero = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCzero.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCzero.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCzero.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCzero.setSize(30, 40);
        portCzero.setLocation(500, 300);
        portCzero.setBackground(Color.white);
        panel.add(portCzero);

        portCone = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCone.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCone.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCone.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCone.setSize(30, 40);
        portCone.setLocation(530, 300);
        portCone.setBackground(Color.white);
        panel.add(portCone);

        portCtwo = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCtwo.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCtwo.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCtwo.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCtwo.setSize(30, 40);
        portCtwo.setLocation(560, 300);
        portCtwo.setBackground(Color.white);
        panel.add(portCtwo);

        portCthree = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCthree.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCthree.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCthree.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCthree.setSize(30, 40);
        portCthree.setLocation(590, 300);
        portCthree.setBackground(Color.white);
        panel.add(portCthree);

        portCfour = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCfour.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCfour.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCfour.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCfour.setSize(30, 40);
        portCfour.setLocation(620, 300);
        portCfour.setBackground(Color.white);
        panel.add(portCfour);

        portCfive = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCfive.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCfive.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCfive.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCfive.setSize(30, 40);
        portCfive.setLocation(650, 300);
        portCfive.setBackground(Color.white);
        panel.add(portCfive);

        portCsix = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCsix.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCsix.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCsix.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCsix.setSize(30, 40);
        portCsix.setLocation(680, 300);
        portCsix.setBackground(Color.white);
        panel.add(portCsix);

        portCseven = new JCheckBox("", createImageIcon("/buttons/bulb1.gif", ""));
        portCseven.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        portCseven.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCseven.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        portCseven.setSize(30, 40);
        portCseven.setLocation(710, 300);
        portCseven.setBackground(Color.white);
        panel.add(portCseven);

        design8255Panel mc = new design8255Panel();
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



    public int inData(int regA, int portAddress)
    {
        modeSelectGroupA = new String(binaryRegA.substring(1, 3));
        selectModeGroupA = java.lang.Integer.parseInt(modeSelectGroupA, 2);
        modeSelectGroupB = new String(binaryRegA.substring(5, 6));
        selectModeGroupB = java.lang.Integer.parseInt(modeSelectGroupB, 2);
        switch (portAddress)
        {
            case 80:
                if (selectModeGroupA == 0)
                {
                    if (binaryCWR.charAt(3) == '1')
                        return portA;
                }
                break;
            case 81:
                if (selectModeGroupB == 0)
                {
                    if (binaryCWR.charAt(6) == '1')
                        return portB;
                }
                break;
            case 82:
                if (selectModeGroupB == 0)
                {
                    return portC;
                }
                break;

        }
        return regA;
    }

    public void outData(int regA, int portAddress)
    {
        System.out.println(portAddress);
        switch (portAddress)
        {
            case 80:

                break;
            case 81:
                break;
            case 82:
                break;
            case 83:
                binaryCWR = toBinary(regA);
                if (binaryCWR.charAt(0) == '0')
                    bsrMode(binaryCWR);
                else
                {
                    CWR = regA;
                    hexCWR = java.lang.Integer.toHexString(CWR);
                    if (hexCWR.length() == 1)
                        hexCWR = new String("0".concat(hexCWR));
                    hexCWR = new String(hexCWR.concat("H"));
                    ctlRegister.setText(hexCWR);
                }


        }

    }


    // BSR Mode

    public void bsrMode(String binaryCWR)
    {
        char arrayRegC[] = new char[8];
        binaryRegC.getChars(0, binaryRegC.length(), arrayRegC, 0);

        String bitSelect = new String(binaryCWR.substring(4, 7));
        int select = java.lang.Integer.parseInt(bitSelect, 2);
        System.out.println(select);

        switch (select)
        {
            case 0:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCzero.setSelected(true);
                    arrayRegC[0] = '1';
                }
                else
                {
                    portCzero.setSelected(false);
                    arrayRegC[0] = '0';
                }
                break;
            case 1:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCone.setSelected(true);
                    arrayRegC[1] = '1';
                }
                else
                {
                    portCone.setSelected(false);
                    arrayRegC[1] = '0';
                }
                break;
            case 2:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCtwo.setSelected(true);
                    arrayRegC[2] = '1';
                }
                else
                {
                    portCtwo.setSelected(false);
                    arrayRegC[2] = '0';
                }
                break;
            case 3:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCthree.setSelected(true);
                    arrayRegC[3] = '1';
                }
                else
                {
                    portCthree.setSelected(false);
                    arrayRegC[3] = '0';
                }
                break;
            case 4:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCfour.setSelected(true);
                    arrayRegC[4] = '1';
                }
                else
                {
                    portCfour.setSelected(false);
                    arrayRegC[4] = '0';
                }
                break;
            case 5:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCfive.setSelected(true);
                    arrayRegC[5] = '1';
                }
                else
                {
                    portCfive.setSelected(false);
                    arrayRegC[5] = '0';
                }
                break;
            case 6:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCsix.setSelected(true);
                    arrayRegC[6] = '1';
                }
                else
                {
                    portCsix.setSelected(false);
                    arrayRegC[6] = '0';
                }
                break;
            case 7:
                if (binaryCWR.charAt(7) == '1')
                {
                    portCseven.setSelected(true);
                    arrayRegC[7] = '1';
                }
                else
                {
                    portCseven.setSelected(false);
                    arrayRegC[7] = '0';
                }
                break;
        }
        binaryRegC = new String(arrayRegC);
        portC = java.lang.Integer.parseInt(binaryRegC, 2);
        hexRegC = new String(java.lang.Integer.toHexString(portC));
        if (hexRegC.length() == 1)
            hexRegC = new String("0".concat(hexRegC));
        hexRegC = new String(hexRegC.concat("H"));
        cPort.setText(hexRegC);
    }


    // IO Mode
    public void ioMode(String binaryCWR)
    {





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

class design8255Panel extends JPanel
{

    design8255Panel()
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
        g.fillRect(230, 70, 250, 400);

        // 74373
        g.setColor(Color.gray);
        g.fillRect(40, 60, 80, 110);

        // 8205
        g.fillRect(40, 270, 70, 100);

        // 8205
        g.setColor(Color.gray);
        g.fillRect(50, 420, 70, 100);


        g.setColor(new Color(128, 255, 255));
        g.drawString("Port A(80H)", 370, 115);
        g.drawString("Port B(81H)", 370, 215);
        g.drawString("Port C(82H)", 370, 315);
        g.drawString("Control Reg.(83H)", 260, 415);

        g.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        g.drawString("A0", 235, 100);
        g.drawString("A1", 235, 120);

        g.drawString("D0", 235, 190);
        g.drawString("D7", 235, 210);

        g.drawString("RESET", 235, 240);
        g.drawLine(235, 305, 245, 305);
        g.drawString("RD", 235, 315);
        g.drawLine(235, 325, 245, 325);
        g.drawString("WR", 235, 335);

        g.drawLine(235, 455, 250, 455);
        g.drawString("CS", 235, 465);

        g.drawString("1 D", 45, 100);
        g.drawString("8 D", 45, 125);

        g.drawString("1 Q", 95, 100);
        g.drawString("8 Q", 95, 125);

        g.drawString("G", 45, 165);
        g.drawLine(95, 155, 110, 155);
        g.drawString("OC", 95, 165);

        g.drawLine(50, 275, 60, 275);
        g.drawString("E1", 50, 285);
        g.drawLine(70, 275, 80, 275);
        g.drawString("E2", 70, 285);
        g.drawString("E3", 90, 285);

        g.drawString("A0", 45, 300);
        g.drawString("A1", 45, 320);
        g.drawString("A2", 45, 340);

        g.drawString("O1", 90, 310);
        g.drawString("O2", 90, 330);

        g.drawLine(60, 425, 70, 425);
        g.drawString("E1", 60, 435);
        g.drawLine(80, 425, 90, 425);
        g.drawString("E2", 80, 435);
        g.drawString("E3", 100, 435);

        g.drawString("A4", 55, 450);
        g.drawString("A3", 55, 470);
        g.drawString("A2", 55, 490);

        g.drawString("O0", 100, 460);

        g.setFont(new Font("Times new Roman", Font.PLAIN, 14));
        g.setColor(new Color(172, 172, 89));

        // ad0-ad7
        g.drawLine(5, 100, 30, 100);
        g.drawLine(5, 120, 30, 120);
        g.drawLine(30, 100, 30, 90);
        g.drawLine(30, 120, 30, 130);
        g.drawLine(30, 90, 40, 110);
        g.drawLine(30, 130, 40, 110);

        g.drawLine(120, 100, 218, 100);
        g.drawLine(120, 120, 218, 120);
        g.drawLine(218, 100, 218, 90);
        g.drawLine(218, 120, 218, 130);
        g.drawLine(218, 90, 230, 110);
        g.drawLine(218, 130, 230, 110);

        g.fillOval(17, 97, 6, 6);
        g.drawLine(20, 100, 20, 190);
        g.fillOval(7, 117, 6, 6);
        g.drawLine(10, 120, 10, 210);
        g.drawLine(20, 190, 218, 190);
        g.drawLine(10, 210, 218, 210);
        g.drawLine(218, 190, 218, 180);
        g.drawLine(218, 210, 218, 220);
        g.drawLine(218, 180, 230, 200);
        g.drawLine(218, 220, 230, 200);

        // ALE
        g.drawLine(5, 160, 40, 160);

        // Vcc
        g.drawLine(70, 60, 70, 45);
        g.drawOval(66, 37, 8, 8);

        // OC
        g.drawLine(100, 170, 100, 180);
        g.drawLine(95, 180, 105, 180);
        g.drawLine(97, 184, 103, 184);

        // RESET
        g.drawLine(5, 235, 230, 235);
        g.drawString(">", 220, 240);

        // IO/M,RD,WR
        g.drawLine(5, 295, 40, 295);
        g.drawLine(5, 315, 40, 315);
        g.drawLine(5, 335, 40, 335);

        // MEMW,MEMR
        g.drawOval(110, 305, 8, 8);
        g.drawOval(110, 325, 8, 8);
        g.drawLine(118, 309, 230, 309);
        g.drawLine(118, 329, 230, 329);

        // E1,E2,E3
        g.drawOval(50, 262, 8, 8);
        g.drawOval(70, 262, 8, 8);
        g.drawLine(54, 262, 54, 260);
        g.drawLine(54, 260, 24, 260);
        g.drawLine(74, 262, 74, 250);
        g.drawLine(74, 250, 24, 250);
        g.drawLine(24, 250, 24, 270);
        g.drawLine(14, 270, 34, 270);
        g.drawLine(17, 272, 31, 272);
        g.drawLine(20, 274, 28, 274);

        // Vcc
        g.drawLine(105, 270, 105, 250);
        g.drawOval(101, 242, 8, 8);
        g.drawLine(90, 270, 90, 260);
        g.drawLine(90, 260, 105, 260);

        // A2,A3,A4
        g.drawLine(5, 450, 50, 450);
        g.drawLine(5, 470, 50, 470);
        g.drawLine(5, 490, 50, 490);

        // A13,14,15
        g.drawOval(60, 412, 8, 8);
        g.drawOval(80, 412, 8, 8);
        g.drawLine(64, 412, 64, 408);
        g.drawLine(64, 408, 5, 408);
        g.drawLine(84, 412, 84, 395);
        g.drawLine(84, 395, 5, 395);
        g.drawLine(104, 420, 104, 382);
        g.drawLine(104, 382, 5, 382);

        // Vcc
        g.drawLine(114, 420, 114, 400);
        g.drawOval(110, 392, 8, 8);

        // O4
        g.drawOval(120, 454, 8, 8);
        g.drawLine(128, 458, 222, 458);
        g.drawOval(222, 454, 8, 8);

        g.setFont(new Font("Times new Roman", Font.PLAIN, 10));
        g.setColor(Color.darkGray);

        g.drawString("AD0-AD1", 5, 90);
        g.drawString("RESET OUT", 5, 230);
        g.drawLine(20, 285, 30, 285);
        g.drawString("IO/M", 6, 294);
        g.drawLine(6, 304, 20, 304);
        g.drawString("RD", 6, 314);
        g.drawLine(6, 324, 20, 324);
        g.drawString("WR", 6, 334);

        g.drawString("A15", 6, 380);
        g.drawString("A14", 6, 393);
        g.drawString("A13", 6, 406);

        g.drawString("A4", 6, 447);
        g.drawString("A3", 6, 467);
        g.drawString("A2", 6, 487);

        g.drawLine(160, 295, 195, 295);
        g.drawString("MEMR", 160, 305);
        g.drawLine(160, 315, 195, 315);
        g.drawString("MEMW", 160, 325);


        g.setFont(new Font("Times new Roman", Font.BOLD, 12));
        g.setColor(Color.blue);
        // g.drawString("8205",105,450);

        g.setFont(new Font("Times new Roman", Font.BOLD, 24));
        g.setColor(new Color(128, 128, 0));
        g.drawString("8255", 330, 60);
        g.drawLine(350, 63, 395, 63);

    }
}
