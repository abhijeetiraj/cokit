import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class Chip8155 extends JFrame implements ActionListener
{
    JTextField aPort, bPort, cPort, msbTimer, lsbTimer, ctlRegister;
    JList memoryList;
    String[] items = new String[255];
    int memory[];
    JButton clock;
    JRadioButton out;


    Chip8155(String title)
    {
        super(title);
        memory = new int[65536];


        Container container = new Container();
        container = getContentPane();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        aPort = new JTextField("A-00H", 7);
        aPort.setSize(80, 30);
        aPort.setLocation(430, 100);
        aPort.setForeground(Color.green.brighter());
        aPort.setBackground(Color.black);
        aPort.setEditable(false);
        aPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(aPort);

        bPort = new JTextField("B-00H", 7);
        bPort.setSize(80, 30);
        bPort.setLocation(430, 200);
        bPort.setForeground(Color.green.brighter());
        bPort.setBackground(Color.black);
        bPort.setEditable(false);
        bPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(bPort);

        cPort = new JTextField("C-00H", 7);
        cPort.setSize(80, 30);
        cPort.setLocation(430, 300);
        cPort.setForeground(Color.green.brighter());
        cPort.setBackground(Color.black);
        cPort.setEditable(false);
        cPort.setHorizontalAlignment(JTextField.CENTER);
        panel.add(cPort);

        msbTimer = new JTextField("MSB-00H", 7);
        msbTimer.setSize(60, 30);
        msbTimer.setLocation(270, 400);
        msbTimer.setForeground(Color.green.brighter());
        msbTimer.setBackground(Color.black);
        msbTimer.setEditable(false);
        msbTimer.setHorizontalAlignment(JTextField.CENTER);
        panel.add(msbTimer);

        lsbTimer = new JTextField("LSB-00H", 7);
        lsbTimer.setSize(60, 30);
        lsbTimer.setLocation(330, 400);
        lsbTimer.setForeground(Color.green.brighter());
        lsbTimer.setBackground(Color.black);
        lsbTimer.setEditable(false);
        lsbTimer.setHorizontalAlignment(JTextField.CENTER);
        panel.add(lsbTimer);

        ctlRegister = new JTextField("00H", 7);
        ctlRegister.setSize(90, 30);
        ctlRegister.setLocation(270, 300);
        ctlRegister.setForeground(Color.green.brighter());
        ctlRegister.setBackground(Color.black);
        ctlRegister.setEditable(false);
        ctlRegister.setHorizontalAlignment(JTextField.CENTER);
        panel.add(ctlRegister);


        // Clock
        clock = new JButton(createImageIcon("/buttons/clock1.gif", ""));
        clock.setPressedIcon(createImageIcon("/buttons/clock2.gif", ""));
        clock.setSize(100, 45);
        clock.setLocation(120, 450);
        clock.setActionCommand("clock");
        clock.setFocusPainted(false);
        clock.setBorderPainted(false);
        clock.setContentAreaFilled(false);
        clock.setMargin(new Insets(0, 0, 0, 0));
        clock.addActionListener(this);
        panel.add(clock);

        // Out
        out = new JRadioButton("", createImageIcon("/buttons/bulb1.gif", ""));
        out.setPressedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        out.setRolloverIcon(createImageIcon("/buttons/bulb1.gif", ""));
        out.setRolloverSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        out.setSelectedIcon(createImageIcon("/buttons/bulb2.gif", ""));
        out.setSize(35, 35);
        out.setLocation(540, 442);
        out.setFocusPainted(false);
        out.setBorderPainted(false);
        out.setContentAreaFilled(false);
        out.setMargin(new Insets(0, 0, 0, 0));
        out.setSelected(false);
        panel.add(out);

        memoryWrite();

        memoryList = new JList(items);
        memoryList.setVisibleRowCount(5);
        memoryList.setFixedCellWidth(4);
        memoryList.setBackground(Color.black);
        memoryList.setForeground(Color.green.brighter());
        JScrollPane jscrollpane = new JScrollPane(memoryList);
        jscrollpane.setSize(130, 150);
        jscrollpane.setLocation(270, 100);
        panel.add(jscrollpane);

        design8155Panel mc = new design8155Panel();
        mc.setSize(800, 520);
        mc.setLocation(0, -20);

        panel.add(mc);
        container.add(panel);


    }

    public void actionPerformed(ActionEvent e)
    {



    }

    public ImageIcon createImageIcon(String filename, String description)
    {
        String path = "/resources/images/" + filename;
        return new ImageIcon(getClass().getResource(path));
    }


    public void memoryWrite()
    {
        for (int i = 0; i < 255; i++)
        {
            String memoryByte = new String(java.lang.Integer.toHexString(i));
            memoryByte = memoryByte.toUpperCase();
            if (memoryByte.length() == 1)
                memoryByte = "000".concat(memoryByte);
            if (memoryByte.length() == 2)
                memoryByte = "00".concat(memoryByte);
            if (memoryByte.length() == 3)
                memoryByte = "0".concat(memoryByte);
            String opcode = new String(java.lang.Integer.toHexString(memory[i]));
            opcode = opcode.toUpperCase();
            if (opcode.length() == 1)
                opcode = "0".concat(opcode);

            memoryByte = memoryByte.concat("-" + opcode + "H");
            items[i] = "" + memoryByte;
        }
    }





}

//

class design8155Panel extends JPanel
{

    design8155Panel()
    {

        repaint();
    }

    public void paint(Graphics g)
    {
        // g.setColor(Color.gray);
        // g.fillRect(0,0,800,800);
        g.setColor(Color.white);// new Color(211,211,169));
        g.fillRect(0, 0, 800, 600);

        g.setColor(new Color(100, 110, 120));
        g.fillRect(230, 70, 300, 400);

        g.setColor(new Color(128, 255, 255));
        g.drawString("Port A", 430, 115);
        g.drawString("Port B", 430, 215);
        g.drawString("Port C", 430, 315);
        g.drawString("Control Reg.", 270, 315);
        g.drawString("TIMER", 310, 415);
        g.drawString("256x8 Static RAM", 270, 115);


        // g.setColor(Color.gray);
        g.setColor(new Color(172, 172, 89));

        // ad0-ad7
        g.drawLine(17, 100, 218, 100);
        g.drawLine(17, 120, 218, 120);
        g.drawLine(218, 100, 218, 90);
        g.drawLine(218, 120, 218, 130);
        g.drawLine(218, 90, 230, 110);
        g.drawLine(218, 130, 230, 110);
        g.drawLine(17, 100, 17, 90);
        g.drawLine(17, 120, 17, 130);
        g.drawLine(17, 90, 5, 110);
        g.drawLine(17, 130, 5, 110);

        // IO/M
        g.drawLine(5, 150, 230, 150);
        g.drawString(">", 222, 155);

        // ALE
        g.drawLine(5, 180, 230, 180);
        g.drawString(">", 222, 185);

        // RD
        g.drawLine(5, 210, 222, 210);
        g.drawOval(222, 206, 8, 8);
        g.drawString(">", 215, 215);

        // WR
        g.drawLine(5, 240, 222, 240);
        g.drawOval(222, 236, 8, 8);
        g.drawString(">", 215, 245);

        // RESET
        g.drawLine(5, 270, 230, 270);
        g.drawString(">", 222, 275);

        // 8205
        g.setColor(Color.gray);
        g.fillRect(70, 325, 90, 140);

        g.setColor(new Color(172, 172, 89));

        // A13
        g.drawLine(5, 360, 70, 360);
        g.drawString(">", 62, 365);

        // A12
        g.drawLine(5, 390, 70, 390);
        g.drawString(">", 62, 395);

        // A11
        g.drawLine(5, 420, 70, 420);
        g.drawString(">", 62, 425);

        // A14
        g.drawOval(90, 317, 8, 8);
        g.drawLine(94, 317, 94, 312);
        g.drawLine(94, 312, 5, 312);

        // A15
        g.drawOval(110, 317, 8, 8);
        g.drawLine(114, 317, 114, 295);
        g.drawLine(114, 295, 5, 295);

        // Vcc
        g.drawLine(144, 325, 144, 290);

        // CE
        g.drawOval(160, 395, 8, 8);
        g.drawLine(168, 399, 222, 399);
        g.drawOval(222, 395, 8, 8);

        // timer clk
        g.drawLine(250, 470, 250, 490);
        g.drawLine(250, 490, 220, 490);
        g.drawString("^", 248, 480);

        // timer out
        g.drawLine(480, 470, 480, 490);
        g.drawLine(480, 490, 510, 490);
        g.drawString("^", 478, 480);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        g.setColor(new Color(128, 255, 255));
        g.drawLine(250, 145, 255, 145);
        g.drawString("IO/M", 233, 155);
        g.drawString("ALE", 233, 185);
        g.drawLine(233, 205, 243, 205);
        g.drawString("RD", 233, 215);
        g.drawLine(233, 235, 243, 235);
        g.drawString("WR", 233, 245);
        g.drawString("RESET", 233, 275);

        g.drawString("E1", 90, 335);
        g.drawString("E2", 110, 335);
        g.drawString("E3", 143, 335);

        g.drawString("A2", 75, 365);
        g.drawString("A1", 75, 395);
        g.drawString("A0", 75, 425);

        g.drawString("O4", 140, 400);
        g.drawString("CE", 237, 405);

        g.setColor(Color.darkGray);
        g.drawString("AD0-AD7", 100, 115);
        g.drawString("A15", 10, 145);
        g.drawString("ALE", 10, 175);
        g.drawLine(10, 195, 25, 195);
        g.drawString("RD", 10, 205);
        g.drawLine(10, 225, 25, 225);
        g.drawString("WR", 10, 235);
        g.drawString("RESET OUT", 10, 265);
        g.drawString("+5v", 150, 300);
        g.drawString("TIMER CLK", 160, 505);
        g.drawLine(520, 495, 570, 495);
        g.drawString("TIMER OUT", 520, 505);


        g.drawString("A15", 10, 290);
        g.drawString("A14", 10, 310);

        g.drawString("A13", 10, 355);
        g.drawString("A12", 10, 385);
        g.drawString("A11", 10, 415);

        g.setFont(new Font("Times New Roman", Font.BOLD, 12));
        g.setColor(Color.blue);
        g.drawString("8205", 105, 450);

        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.setColor(new Color(128, 128, 0));
        g.drawString("8155", 350, 60);
        g.drawLine(350, 63, 395, 63);

    }
}
