import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class Chip8279 extends JFrame
{

    JButton b[] = new JButton[70];
    design8279Panel mc;

    String message = "Nil";
    int portid, addrid, ac, out, in, top;
    int command[] = new int[8];
    int data[] = new int[8];
    int fifo[] = new int[8];
    int fifodis[] = new int[16];
    int status[] = new int[8];
    int xx = 100, yy = 180;
    int end;
    int i, D, D1, n1 = -1, n2 = -1, E = -1, k, k1, k2, mode, lock, count, dataregi, cntr, shift, ai, count1, initialise, key = 0;
    int aa, bb, cc, dd, ee, ff, gg, hh, i3, i4, temp[] = new int[8], temp1;
    JList memoryList;
    String[] items = new String[65536];


    Chip8279(String title)
    {
        super(title);
        Container container = new Container();
        container = getContentPane();
        container.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 800, 800);
        panel.setBackground(Color.white);
        panel.setLayout(null);

        b[0] = new JButton("0");
        b[0].setBackground(new Color(150, 150, 255));
        b[0].setBounds(xx + 50, yy + 40, 50, 39);
        panel.add(b[0]);
        b[0].addActionListener(new ShowAction(this));

        b[1] = new JButton("1");
        b[1].setBackground(new Color(150, 150, 255));
        b[1].setBounds(xx + 100, yy + 40, 50, 39);
        panel.add(b[1]);
        b[1].addActionListener(new ShowAction(this));

        b[2] = new JButton("2");
        b[2].setBackground(new Color(150, 150, 255));
        b[2].setBounds(xx + 150, yy + 40, 50, 39);
        panel.add(b[2]);
        b[2].addActionListener(new ShowAction(this));

        b[3] = new JButton("3");
        b[3].setBackground(new Color(150, 150, 255));
        b[3].setBounds(xx + 200, yy + 40, 50, 39);
        panel.add(b[3]);
        b[3].addActionListener(new ShowAction(this));

        b[4] = new JButton("4");
        b[4].setBackground(new Color(150, 150, 255));
        b[4].setBounds(xx + 250, yy + 40, 50, 39);
        panel.add(b[4]);
        b[4].addActionListener(new ShowAction(this));

        b[5] = new JButton("5");
        b[5].setBackground(new Color(150, 150, 255));
        b[5].setBounds(xx + 300, yy + 40, 50, 39);
        panel.add(b[5]);
        b[5].addActionListener(new ShowAction(this));

        b[6] = new JButton("6");
        b[6].setBackground(new Color(150, 150, 255));
        b[6].setBounds(xx + 350, yy + 40, 50, 39);
        panel.add(b[6]);
        b[6].addActionListener(new ShowAction(this));

        b[7] = new JButton("7");
        b[7].setBackground(new Color(150, 150, 255));
        b[7].setBounds(xx + 400, yy + 40, 50, 39);
        panel.add(b[7]);
        b[7].addActionListener(new ShowAction(this));
        // 8

        b[8] = new JButton("8");
        b[8].setBackground(new Color(150, 150, 255));
        b[8].setBounds(xx + 50, yy + 80, 50, 39);
        panel.add(b[8]);
        b[8].addActionListener(new ShowAction(this));

        b[9] = new JButton("9");
        b[9].setBackground(new Color(150, 150, 255));
        b[9].setBounds(xx + 100, yy + 80, 50, 39);
        panel.add(b[9]);
        b[9].addActionListener(new ShowAction(this));

        b[10] = new JButton("10");
        b[10].setBackground(new Color(150, 150, 255));
        b[10].setBounds(xx + 150, yy + 80, 50, 39);
        panel.add(b[10]);
        b[10].addActionListener(new ShowAction(this));

        b[11] = new JButton("11");
        b[11].setBackground(new Color(150, 150, 255));
        b[11].setBounds(xx + 200, yy + 80, 50, 39);
        panel.add(b[11]);
        b[11].addActionListener(new ShowAction(this));

        b[12] = new JButton("12");
        b[12].setBackground(new Color(150, 150, 255));
        b[12].setBounds(xx + 250, yy + 80, 50, 39);
        panel.add(b[12]);
        b[12].addActionListener(new ShowAction(this));

        b[13] = new JButton("13");
        b[13].setBackground(new Color(150, 150, 255));
        b[13].setBounds(xx + 300, yy + 80, 50, 39);
        panel.add(b[13]);
        b[13].addActionListener(new ShowAction(this));

        b[14] = new JButton("14");
        b[14].setBackground(new Color(150, 150, 255));
        b[14].setBounds(xx + 350, yy + 80, 50, 39);
        panel.add(b[14]);
        b[14].addActionListener(new ShowAction(this));

        b[15] = new JButton("15");
        b[15].setBackground(new Color(150, 150, 255));
        b[15].setBounds(xx + 400, yy + 80, 50, 39);
        panel.add(b[15]);
        b[15].addActionListener(new ShowAction(this));

        // 16

        b[16] = new JButton("16");
        b[16].setBackground(new Color(150, 150, 255));
        b[16].setBounds(xx + 50, yy + 120, 50, 39);
        panel.add(b[16]);
        b[16].addActionListener(new ShowAction(this));

        b[17] = new JButton("17");
        b[17].setBackground(new Color(150, 150, 255));
        b[17].setBounds(xx + 100, yy + 120, 50, 39);
        panel.add(b[17]);
        b[17].addActionListener(new ShowAction(this));

        b[18] = new JButton("18");
        b[18].setBackground(new Color(150, 150, 255));
        b[18].setBounds(xx + 150, yy + 120, 50, 39);
        panel.add(b[18]);
        b[18].addActionListener(new ShowAction(this));

        b[19] = new JButton("19");
        b[19].setBackground(new Color(150, 150, 255));
        b[19].setBounds(xx + 200, yy + 120, 50, 39);
        panel.add(b[19]);
        b[19].addActionListener(new ShowAction(this));

        b[20] = new JButton("20");
        b[20].setBackground(new Color(150, 150, 255));
        b[20].setBounds(xx + 250, yy + 120, 50, 39);
        panel.add(b[20]);
        b[20].addActionListener(new ShowAction(this));

        b[21] = new JButton("21");
        b[21].setBackground(new Color(150, 150, 255));
        b[21].setBounds(xx + 300, yy + 120, 50, 39);
        panel.add(b[21]);
        b[21].addActionListener(new ShowAction(this));

        b[22] = new JButton("22");
        b[22].setBackground(new Color(150, 150, 255));
        b[22].setBounds(xx + 350, yy + 120, 50, 39);
        panel.add(b[22]);
        b[22].addActionListener(new ShowAction(this));

        b[23] = new JButton("23");
        b[23].setBackground(new Color(150, 150, 255));
        b[23].setBounds(xx + 400, yy + 120, 50, 39);
        panel.add(b[23]);
        b[23].addActionListener(new ShowAction(this));

        // 24

        b[24] = new JButton("24");
        b[24].setBackground(new Color(150, 150, 255));
        b[24].setBounds(xx + 50, yy + 160, 50, 39);
        panel.add(b[24]);
        b[24].addActionListener(new ShowAction(this));

        b[25] = new JButton("25");
        b[25].setBackground(new Color(150, 150, 255));
        b[25].setBounds(xx + 100, yy + 160, 50, 39);
        panel.add(b[25]);
        b[25].addActionListener(new ShowAction(this));

        b[26] = new JButton("26");
        b[26].setBackground(new Color(150, 150, 255));
        b[26].setBounds(xx + 150, yy + 160, 50, 39);
        panel.add(b[26]);
        b[26].addActionListener(new ShowAction(this));

        b[27] = new JButton("27");
        b[27].setBackground(new Color(150, 150, 255));
        b[27].setBounds(xx + 200, yy + 160, 50, 39);
        panel.add(b[27]);
        b[27].addActionListener(new ShowAction(this));

        b[28] = new JButton("28");
        b[28].setBackground(new Color(150, 150, 255));
        b[28].setBounds(xx + 250, yy + 160, 50, 39);
        panel.add(b[28]);
        b[28].addActionListener(new ShowAction(this));

        b[29] = new JButton("29");
        b[29].setBackground(new Color(150, 150, 255));
        b[29].setBounds(xx + 300, yy + 160, 50, 39);
        panel.add(b[29]);
        b[29].addActionListener(new ShowAction(this));

        b[30] = new JButton("30");
        b[30].setBackground(new Color(150, 150, 255));
        b[30].setBounds(xx + 350, yy + 160, 50, 39);
        panel.add(b[30]);
        b[30].addActionListener(new ShowAction(this));

        b[31] = new JButton("31");
        b[31].setBackground(new Color(150, 150, 255));
        b[31].setBounds(xx + 400, yy + 160, 50, 39);
        panel.add(b[31]);
        b[31].addActionListener(new ShowAction(this));

        // 32
        b[32] = new JButton("32");
        b[32].setBackground(new Color(150, 150, 255));
        b[32].setBounds(xx + 50, yy + 200, 50, 39);
        panel.add(b[32]);
        b[32].addActionListener(new ShowAction(this));

        b[33] = new JButton("33");
        b[33].setBackground(new Color(150, 150, 255));
        b[33].setBounds(xx + 100, yy + 200, 50, 39);
        panel.add(b[33]);
        b[33].addActionListener(new ShowAction(this));

        b[34] = new JButton("34");
        b[34].setBackground(new Color(150, 150, 255));
        b[34].setBounds(xx + 150, yy + 200, 50, 39);
        panel.add(b[34]);
        b[34].addActionListener(new ShowAction(this));

        b[35] = new JButton("35");
        b[35].setBackground(new Color(150, 150, 255));
        b[35].setBounds(xx + 200, yy + 200, 50, 39);
        panel.add(b[35]);
        b[35].addActionListener(new ShowAction(this));

        b[36] = new JButton("36");
        b[36].setBackground(new Color(150, 150, 255));
        b[36].setBounds(xx + 250, yy + 200, 50, 39);
        panel.add(b[36]);
        b[36].addActionListener(new ShowAction(this));

        b[37] = new JButton("37");
        b[37].setBackground(new Color(150, 150, 255));
        b[37].setBounds(xx + 300, yy + 200, 50, 39);
        panel.add(b[37]);
        b[37].addActionListener(new ShowAction(this));

        b[38] = new JButton("38");
        b[38].setBackground(new Color(150, 150, 255));
        b[38].setBounds(xx + 350, yy + 200, 50, 39);
        panel.add(b[38]);
        b[38].addActionListener(new ShowAction(this));

        b[39] = new JButton("39");
        b[39].setBackground(new Color(150, 150, 255));
        b[39].setBounds(xx + 400, yy + 200, 50, 39);
        panel.add(b[39]);
        b[39].addActionListener(new ShowAction(this));
        // 39
        b[40] = new JButton("40");
        b[40].setBackground(new Color(150, 150, 255));
        b[40].setBounds(xx + 50, yy + 240, 50, 39);
        panel.add(b[40]);
        b[40].addActionListener(new ShowAction(this));

        b[41] = new JButton("41");
        b[41].setBackground(new Color(150, 150, 255));
        b[41].setBounds(xx + 100, yy + 240, 50, 39);
        panel.add(b[41]);
        b[41].addActionListener(new ShowAction(this));

        b[42] = new JButton("42");
        b[42].setBackground(new Color(150, 150, 255));
        b[42].setBounds(xx + 150, yy + 240, 50, 39);
        panel.add(b[42]);
        b[42].addActionListener(new ShowAction(this));

        b[43] = new JButton("43");
        b[43].setBackground(new Color(150, 150, 255));
        b[43].setBounds(xx + 200, yy + 240, 50, 39);
        panel.add(b[43]);
        b[43].addActionListener(new ShowAction(this));

        b[44] = new JButton("44");
        b[44].setBackground(new Color(150, 150, 255));
        b[44].setBounds(xx + 250, yy + 240, 50, 39);
        panel.add(b[44]);
        b[44].addActionListener(new ShowAction(this));

        b[45] = new JButton("45");
        b[45].setBackground(new Color(150, 150, 255));
        b[45].setBounds(xx + 300, yy + 240, 50, 39);
        panel.add(b[45]);
        b[45].addActionListener(new ShowAction(this));

        b[46] = new JButton("46");
        b[46].setBackground(new Color(150, 150, 255));
        b[46].setBounds(xx + 350, yy + 240, 50, 39);
        panel.add(b[46]);
        b[46].addActionListener(new ShowAction(this));

        b[47] = new JButton("47");
        b[47].setBackground(new Color(150, 150, 255));
        b[47].setBounds(xx + 400, yy + 240, 50, 39);
        panel.add(b[47]);
        b[47].addActionListener(new ShowAction(this));
        // 47
        b[48] = new JButton("48");
        b[48].setBackground(new Color(150, 150, 255));
        b[48].setBounds(xx + 50, yy + 280, 50, 39);
        panel.add(b[48]);
        b[48].addActionListener(new ShowAction(this));

        b[49] = new JButton("49");
        b[49].setBackground(new Color(150, 150, 255));
        b[49].setBounds(xx + 100, yy + 280, 50, 39);
        panel.add(b[49]);
        b[49].addActionListener(new ShowAction(this));

        b[50] = new JButton("50");
        b[50].setBackground(new Color(150, 150, 255));
        b[50].setBounds(xx + 150, yy + 280, 50, 39);
        panel.add(b[50]);
        b[50].addActionListener(new ShowAction(this));

        b[51] = new JButton("51");
        b[51].setBackground(new Color(150, 150, 255));
        b[51].setBounds(xx + 200, yy + 280, 50, 39);
        panel.add(b[51]);
        b[51].addActionListener(new ShowAction(this));

        b[52] = new JButton("52");
        b[52].setBackground(new Color(150, 150, 255));
        b[52].setBounds(xx + 250, yy + 280, 50, 39);
        panel.add(b[52]);
        b[52].addActionListener(new ShowAction(this));

        b[53] = new JButton("53");
        b[53].setBackground(new Color(150, 150, 255));
        b[53].setBounds(xx + 300, yy + 280, 50, 39);
        panel.add(b[53]);
        b[53].addActionListener(new ShowAction(this));

        b[54] = new JButton("54");
        b[54].setBackground(new Color(150, 150, 255));
        b[54].setBounds(xx + 350, yy + 280, 50, 39);
        panel.add(b[54]);
        b[54].addActionListener(new ShowAction(this));

        b[55] = new JButton("55");
        b[55].setBackground(new Color(150, 150, 255));
        b[55].setBounds(xx + 400, yy + 280, 50, 39);
        panel.add(b[55]);
        b[55].addActionListener(new ShowAction(this));
        // 55
        b[56] = new JButton("56");
        b[56].setBackground(new Color(150, 150, 255));
        b[56].setBounds(xx + 50, yy + 320, 50, 39);
        panel.add(b[56]);
        b[56].addActionListener(new ShowAction(this));

        b[57] = new JButton("57");
        b[57].setBackground(new Color(150, 150, 255));
        b[57].setBounds(xx + 100, yy + 320, 50, 39);
        panel.add(b[57]);
        b[57].addActionListener(new ShowAction(this));

        b[58] = new JButton("58");
        b[58].setBackground(new Color(150, 150, 255));
        b[58].setBounds(xx + 150, yy + 320, 50, 39);
        panel.add(b[58]);
        b[58].addActionListener(new ShowAction(this));

        b[59] = new JButton("59");
        b[59].setBackground(new Color(150, 150, 255));
        b[59].setBounds(xx + 200, yy + 320, 50, 39);
        panel.add(b[59]);
        b[59].addActionListener(new ShowAction(this));

        b[60] = new JButton("60");
        b[60].setBackground(new Color(150, 150, 255));
        b[60].setBounds(xx + 250, yy + 320, 50, 39);
        panel.add(b[60]);
        b[60].addActionListener(new ShowAction(this));

        b[61] = new JButton("61");
        b[61].setBackground(new Color(150, 150, 255));
        b[61].setBounds(xx + 300, yy + 320, 50, 39);
        panel.add(b[61]);
        b[61].addActionListener(new ShowAction(this));

        b[62] = new JButton("62");
        b[62].setBackground(new Color(150, 150, 255));
        b[62].setBounds(xx + 350, yy + 320, 50, 39);
        panel.add(b[62]);
        b[62].addActionListener(new ShowAction(this));

        b[63] = new JButton("63");
        b[63].setBackground(new Color(150, 150, 255));
        b[63].setBounds(xx + 400, yy + 320, 50, 39);
        panel.add(b[63]);
        b[63].addActionListener(new ShowAction(this));
        // 63
        b[65] = new JButton("cntr");
        b[65].setBackground(new Color(255, 160, 207));
        b[65].setBounds(30, 220, 70, 30);
        panel.add(b[65]);
        b[65].addActionListener(new ShowAction(this));

        b[66] = new JButton("shift");
        b[66].setBackground(new Color(255, 160, 207));
        b[66].setBounds(30, 270, 70, 30);
        panel.add(b[66]);
        b[66].addActionListener(new ShowAction(this));


        memoryList = new JList(items);
        memoryList.setVisibleRowCount(5);
        memoryList.setFixedCellWidth(4);
        memoryList.setBackground(Color.black);
        memoryList.setForeground(Color.green.brighter());
        JScrollPane jscrollpane = new JScrollPane(memoryList);
        jscrollpane.setSize(130, 210);
        jscrollpane.setLocation(600, 170);
        // panel.add(jscrollpane);


        mc = new design8279Panel();
        mc.setBackground(Color.blue);
        mc.setSize(800, 220);
        mc.setLocation(0, 0);
        // panel.add(mc);

        panel.setVisible(true);
        mc.setVisible(true);
        panel.add(mc);
        container.add(panel);
        // container.add(mc);


    }

    public void memoryWrite()
    {
        for (int i = 0; i < 16; i++)
        {
            String memoryByte = new String(java.lang.Integer.toHexString(i));
            memoryByte = memoryByte.toUpperCase();
            if (memoryByte.length() == 1)
                memoryByte = "000".concat(memoryByte);
            if (memoryByte.length() == 2)
                memoryByte = "00".concat(memoryByte);
            if (memoryByte.length() == 3)
                memoryByte = "0".concat(memoryByte);
            String opcode = new String(java.lang.Integer.toHexString(fifodis[i]));
            opcode = opcode.toUpperCase();
            if (opcode.length() == 1)
                opcode = "0".concat(opcode);

            memoryByte = memoryByte.concat("-" + opcode + "H");
            items[i] = "" + memoryByte;
        }
    }




    public int inData(int regA, int portAddress)
    {
        System.out.println("In ac:" + ac + "Port" + portAddress);
        portid = portAddress;
        in = 1;
        out = 0;
        mc.inData();
        System.out.println("Ac = " + ac);
        return ac;
    }

    public void outData(int regA, int portAddress)
    {
        System.out.println("Out ac:" + ac + "Port" + portAddress);
        portid = portAddress;
        ac = regA;
        out = 1;
        in = 0;
        repaint();
        mc.inData();
    }


    class ShowAction extends AbstractAction
    {
        Chip8279 demo;


        public ShowAction(Chip8279 demo)
        {
            this.demo = demo;
        }


        public void actionPerformed(ActionEvent e)
        {

            String str = e.getActionCommand();

            if (str.equals("cntr") || str.equals("shift"))
            {
                if (str.equals("cntr"))
                {
                    message = "cntr";
                    cntr = 1;
                }
                if (str.equals("shift"))
                {
                    message = "shift";
                    shift = 1;
                }
                if (cntr == 1)
                {
                    data[7] = 1;
                }
                if (shift == 1)
                {
                    data[6] = 1;
                }
            }
            else
            {

                if (str.equals("0"))
                {
                    message = "0";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("1"))
                {
                    message = "1";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("2"))
                {
                    message = "2";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("3"))
                {
                    message = "3";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("4"))
                {
                    message = "4";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("5"))
                {
                    message = "5";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("6"))
                {
                    message = "6";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("7"))
                {
                    message = "7";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 7
                if (str.equals("8"))
                {
                    message = "8";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("9"))
                {
                    message = "9";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("10"))
                {
                    message = "10";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("11"))
                {
                    message = "11";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("12"))
                {
                    message = "12";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("13"))
                {
                    message = "13";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("14"))
                {
                    message = "14";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("15"))
                {
                    message = "15";
                    key = 1;
                    data[5] = 0;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 16
                if (str.equals("16"))
                {
                    message = "16";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("17"))
                {
                    message = "17";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("18"))
                {
                    message = "18";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("19"))
                {
                    message = "19";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("20"))
                {
                    message = "20";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("21"))
                {
                    message = "21";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("22"))
                {
                    message = "22";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("23"))
                {
                    message = "23";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 23
                if (str.equals("24"))
                {
                    message = "24";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("25"))
                {
                    message = "25";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("26"))
                {
                    message = "26";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("27"))
                {
                    message = "27";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("28"))
                {
                    message = "28";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("29"))
                {
                    message = "29";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("30"))
                {
                    message = "30";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("31"))
                {
                    message = "31";
                    key = 1;
                    data[5] = 0;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 32
                if (str.equals("32"))
                {
                    message = "32";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("33"))
                {
                    message = "33";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("34"))
                {
                    message = "34";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("35"))
                {
                    message = "35";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("36"))
                {
                    message = "36";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("37"))
                {
                    message = "37";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("38"))
                {
                    message = "38";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("39"))
                {
                    message = "39";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 40
                if (str.equals("40"))
                {
                    message = "40";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("41"))
                {
                    message = "41";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("42"))
                {
                    message = "42";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("43"))
                {
                    message = "43";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("44"))
                {
                    message = "44";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("45"))
                {
                    message = "45";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("46"))
                {
                    message = "46";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("47"))
                {
                    message = "47";
                    key = 1;
                    data[5] = 1;
                    data[4] = 0;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 47
                if (str.equals("48"))
                {
                    message = "48";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("49"))
                {
                    message = "49";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("50"))
                {
                    message = "50";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("51"))
                {
                    message = "51";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("52"))
                {
                    message = "52";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("53"))
                {
                    message = "53";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("54"))
                {
                    message = "54";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("55"))
                {
                    message = "55";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 0;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 55
                if (str.equals("56"))
                {
                    message = "56";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("57"))
                {
                    message = "57";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("58"))
                {
                    message = "58";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("59"))
                {
                    message = "59";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 0;
                    data[1] = 1;
                    data[0] = 1;
                }
                if (str.equals("60"))
                {
                    message = "60";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 0;
                }
                if (str.equals("61"))
                {
                    message = "61";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 0;
                    data[0] = 1;
                }
                if (str.equals("62"))
                {
                    message = "62";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 0;
                }
                if (str.equals("63"))
                {
                    message = "63";
                    key = 1;
                    data[5] = 1;
                    data[4] = 1;
                    data[3] = 1;
                    data[2] = 1;
                    data[1] = 1;
                    data[0] = 1;
                }
                // 63

                // mc.inData();



            }// action performed



        }
    }


    class design8279Panel extends JPanel
    {

        public design8279Panel()
        {

            // repaint();
        }


        public void drawFifo(Graphics g)
        {
            g.setColor(new Color(128, 128, 128));
            g.fillRect(690, 20, 100, 180);
            g.setColor(Color.black.brighter());
            g.fillRect(680, 30, 100, 180);
            g.setColor(Color.green);

            int y = 40;
            for (int i = 0; i < 16; i++)
            {
                g.drawString("0" + Integer.toHexString(i).toUpperCase() + "H -" + Integer.toHexString(fifodis[i]).toUpperCase() + "H", 690, y);
                y = y + 11;
            }
        }

        public void paint(Graphics g)
        {
            g.setColor(new Color(128, 128, 128));
            g.fill3DRect(30, 20, 640, 100, true); // only rect
            g.setColor(Color.black.brighter());
            g.fill3DRect(20, 30, 640, 100, true); // only rect
            drawFifo(g);
            g.setColor(Color.red);
            if (E == 0)
            {
                for (int i1 = 0, i4 = 20; i1 < n1; i1++, i4 += 40)
                {
                    temp1 = fifodis[i1];
                    i3 = i4 + 20;
                    for (int i2 = 0; i2 < 8; i2++)
                    {
                        temp[i2] = temp1 % 2;
                        temp1 = temp1 / 2;
                    }
                    aa = temp[0];
                    bb = temp[1];
                    cc = temp[2];
                    dd = temp[3];
                    ee = temp[4];
                    ff = temp[5];
                    gg = temp[6];
                    hh = temp[7];
                    // f1(a,b,c,d,e,f,g,h,n1,E,i4);

                    // g.drawRect(i+5,50,60,60);
                    if (aa == 1)
                        g.drawLine(i3 - 10, 50, i3 + 10, 50);
                    if (bb == 1)
                        g.drawLine(i3 + 10, 50, i3 + 10, 80);
                    if (cc == 1)
                        g.drawLine(i3 + 10, 80, i3 + 10, 110);
                    if (dd == 1)
                        g.drawLine(i3 - 10, 110, i3 + 10, 110);
                    if (ee == 1)
                        g.drawLine(i3 - 10, 110, i3 - 10, 80);
                    if (ff == 1)
                        g.drawLine(i3 - 10, 80, i3 - 10, 50);
                    if (gg == 1)
                        g.drawLine(i3 - 10, 80, i3 + 10, 80);// g.drawLine(i3,70,i3,130);use as .
                }
            }

            if (E == 1 && n1 == 8)
            {
                i4 = 620;
                // System.out.println("Inside E1N18");
                for (int i1 = n1 - 1; i1 >= 0; i1--, i4 -= 40)
                {
                    temp1 = fifodis[i1];
                    System.out.println("Temp1=" + temp1);
                    i3 = i4 - 20;
                    for (int i2 = 0; i2 < 8; i2++)
                    {
                        temp[i2] = temp1 % 2;
                        temp1 = temp1 / 2;
                    }
                    aa = temp[0];
                    bb = temp[1];
                    cc = temp[2];
                    dd = temp[3];
                    ee = temp[4];
                    ff = temp[5];
                    gg = temp[6];
                    hh = temp[7];
                    // f1(a,b,c,d,e,f,g,h,E,i4);
                    // g.drawRect(i+5,50,60,60);
                    // System.out.println("I3" + i3);
                    if (aa == 1)
                        g.drawLine(i3 - 10, 50, i3 + 10, 50);
                    if (bb == 1)
                        g.drawLine(i3 + 10, 50, i3 + 10, 80);
                    if (cc == 1)
                        g.drawLine(i3 + 10, 80, i3 + 10, 110);
                    if (dd == 1)
                        g.drawLine(i3 - 10, 110, i3 + 10, 110);
                    if (ee == 1)
                        g.drawLine(i3 - 10, 110, i3 - 10, 80);
                    if (ff == 1)
                        g.drawLine(i3 - 10, 80, i3 - 10, 50);
                    if (gg == 1)
                        g.drawLine(i3 - 10, 80, i3 + 10, 80);// g.drawLine(i3,70,i3,130);use as .
                    // g.drawString("Nilesh",50,100);

                }
            }

            if (E == 1 && n1 == 16)
            {
                i4 = 620;
                for (int i1 = n1 - 1; i1 >= 0; i1--, i4 -= 40)
                {
                    i3 = i4 - 20;
                    temp1 = fifodis[i1];
                    for (int i2 = 0; i2 < 8; i2++)
                    {
                        temp[i2] = temp1 % 2;
                        temp1 = temp1 / 2;
                    }
                    aa = temp[0];
                    bb = temp[1];
                    cc = temp[2];
                    dd = temp[3];
                    ee = temp[4];
                    ff = temp[5];
                    gg = temp[6];
                    hh = temp[7];
                    // f1(a,b,c,d,e,f,g,h,E,i4);

                    // g.drawRect(i+5,50,60,60);
                    if (aa == 1)
                        g.drawLine(i3 - 10, 50, i3 + 10, 50);
                    if (bb == 1)
                        g.drawLine(i3 + 10, 50, i3 + 10, 80);
                    if (cc == 1)
                        g.drawLine(i3 + 10, 80, i3 + 10, 110);
                    if (dd == 1)
                        g.drawLine(i3 - 10, 110, i3 + 10, 110);
                    if (ee == 1)
                        g.drawLine(i3 - 10, 110, i3 - 10, 80);
                    if (ff == 1)
                        g.drawLine(i3 - 10, 80, i3 - 10, 50);
                    if (gg == 1)
                        g.drawLine(i3 - 10, 80, i3 + 10, 80);// g.drawLine(i3,70,i3,130);use as .
                }

            }

            g.dispose();
            // System.out.println("End painting");


        }

        public void inData()
        {
            Graphics g = getGraphics();
            drawFifo(g);

            // g.setColor(Color.white);
            // g.fillRect(0,0,1000,1000);
            if (g == null)
                return;// System.exit(0);
            // System.out.println("Inside paint in=" + in);


            if (portid == 31 && out == 1)// out instruction portid ,out ,in ,ac are static integers;
            {
                // System.out.println("Hello");
                for (i = 0; i < 8; i++)
                {
                    command[i] = ac % 2;
                    ac = ac / 2;
                }
                if (command[7] == 0 && command[6] == 0 && command[5] == 0)
                {
                    D1 = command[4];
                    D = command[3]; // 0=left,1=right
                    if (D1 == 0 && D == 0)
                    {
                        n1 = 8;
                        n2 = 8;
                        E = 0;
                    }
                    if (D1 == 0 && D == 1)
                    {
                        n1 = 16;
                        n2 = 8;
                        E = 0;
                    }
                    if (D1 == 1 && D == 0)
                    {
                        n1 = 8;
                        n2 = 8;
                        E = 1;
                    }
                    if (D1 == 1 && D == 1)
                    {
                        n1 = 16;
                        n2 = 8;
                        E = 1;
                    }
                    k = command[2];
                    k1 = command[1];
                    k2 = command[0]; // mode=0,encoded;mode=1,encoded;lock=2,lock=22;
                    if (k == 0 && k1 == 0 && k2 == 0)
                    {
                        mode = 0;
                        lock = 2;
                    }
                    if (k == 0 && k1 == 0 && k2 == 1)
                    {
                        mode = 1;
                        lock = 2;
                    }
                    if (k == 0 && k1 == 1 && k2 == 0)
                    {
                        mode = 0;
                        lock = 22;
                    }
                    if (k == 0 && k1 == 1 && k2 == 1)
                    {
                        mode = 1;
                        lock = 22;
                    }
                    initialise = 1;
                    return;
                }

                if (initialise == 1 && command[7] == 0 && command[6] == 1 && command[5] == 0)
                {
                    dataregi = 0;
                    for (i = 0; i < 8; i++)
                    {
                        dataregi = dataregi + (data[i]) * (int)(Math.pow(2, i));
                    }
                }
                if (initialise == 1 && command[7] == 0 && command[6] == 1 && command[5] == 1)
                {
                    dataregi = 0;
                    ai = command[4];
                    count1 = command[3] * 8 + command[2] * 4 + command[1] * 2 + command[0] * 1;
                    if (ai == 1)
                    { // for(i=0;i<8;i++)
                      // {fifodis[i]=fifodis[i+1];}
                        if (count == 7 || count1 == 15)
                            count1 = 0;
                        else
                            count1++;
                    }
                    if (ai == 0)
                    {
                        dataregi = fifodis[count1];
                    }
                    if (ai == 1)
                    {
                        dataregi = fifodis[count1];
                    }

                }

                if (initialise == 1 && command[7] == 1 && command[6] == 0 && command[5] == 0)
                {
                    ai = command[4];
                    count1 = command[3] * 8 + command[2] * 4 + command[1] * 2 + command[0] * 1;
                    if (ai == 1)
                    {
                        for (i = 0; i < n1 - 1; i++)// use end here to decide ram either 8 || 16
                        {
                            fifodis[i] = fifodis[i + 1];
                        }
                        count1++;
                    }
                    System.out.println("INside write=" + ac + "count1=" + count1);
                }

                drawFifo(g);
                memoryWrite();
                // memoryList.setListData(items);

                if (initialise == 1 && command[7] == 1 && command[6] == 1 && command[5] == 0)
                {
                    if (command[4] == 1)
                    {
                        for (int i8 = 0; i8 < n1; i8++)
                            fifodis[i8] = 0;
                    }
                    if (command[0] == 1)
                    {
                        for (int i8 = 0; i8 < 8; i8++)
                            status[i8] = 0;
                    }
                    if (command[1] == 1)
                    {
                        for (int i8 = 0; i8 < n1; i8++)
                            fifodis[i8] = 0;
                        for (int i8 = 0; i8 < 8; i8++)
                            status[i8] = 0;
                    }
                    if (initialise == 1 && command[7] == 1 && command[6] == 0 && command[5] == 1)
                    {
                        if (command[1] == 0 && E == 0)
                        {
                            for (int i8 = 4; i8 < 8; i8++)
                                fifodis[i8] = 0;
                        }
                        if (command[1] == 1 && E == 1)
                        {
                            for (int i8 = 12; i8 < 16; i8++)
                                fifodis[i8] = 0;
                        }
                        if (command[0] == 0 && E == 0)
                        {
                            for (int i8 = 0; i8 < 4; i8++)
                                fifodis[i8] = 0;
                        }
                        if (command[0] == 1 && E == 1)
                        {
                            for (int i8 = 8; i8 < 12; i8++)
                                fifodis[i8] = 0;
                        }

                        // if(command[3]==0 &&command[2]==1){IWA=0;IWB=1;}
                        // if(command[3]==1 &&command[2]==0){IWA=1;IWB=0;}
                        // if(BLA==1 && BL)
                        boolean BD[] = new boolean[16];
                        if (command[3] == 0 && E == 0)
                        {
                            for (int i8 = 4; i8 < 8; i8++)
                                BD[i8] = false;
                        }
                        if (command[3] == 1 && E == 1)
                        {
                            for (int i8 = 12; i8 < 16; i8++)
                                BD[i8] = true;
                        }
                        if (command[2] == 0 && E == 0)
                        {
                            for (int i8 = 0; i8 < 4; i8++)
                                BD[i8] = false;
                        }
                        if (command[2] == 1 && E == 1)
                        {
                            for (int i8 = 8; i8 < 12; i8++)
                                BD[i8] = true;
                        }

                        memoryWrite();
                        drawFifo(g);

                        // memoryList.setListData(items);




                    }



                }
                return;

            } // out ends

            if (portid == 30 && out == 1)// in instruction
            {
                dataregi = ac;
                System.out.println("Displaying " + ac);
                if (command[4] == 1)
                {
                    count1++;
                }

                count1 = count1 % n1;
                fifodis[count1] = dataregi;
                repaint();
            }
            if (portid == 31 && in == 1)// in instruction
            {
                int tempo, tempo1 = 0;
                // System.out.println("Inside IN");
                if (initialise == 1)
                {// System.out.println("Inside INit");
                    status[7] = 0;
                    status[6] = 0;
                }// du & s/e error flg
                if (key == 1)
                {
                    tempo = top + 1;
                    key = 0;
                    System.out.println("temp0" + tempo);
                }
                else
                    tempo = 0;
                if (tempo == 0)
                {// System.out.println("temp0"+tempo);
                    status[0] = 0;
                    status[1] = 0;
                    status[2] = 0;
                }// initiall there was 000
                else
                {

                    for (i = 0; i < 3; i++) // possibility of zero division
                    {
                        status[i] = tempo % 2;
                        tempo = tempo / 2;
                    }
                }
                status[3] = 0;
                status[4] = 0;
                status[5] = 0;
                for (i = 0; i < 8; i++)
                {
                    tempo1 = tempo1 + (status[i]) * (int)(Math.pow(2, i));
                }
                // System.out.println("tempo1" + tempo1);
                ac = tempo1;
                // return ;
            } // in31 ends



            // System.out.println("dataregi."+i);

            if (initialise == 1 && portid == 30 && in == 1)// in instruction
            { // System.out.println("dataregi."+dataregi);
                if (top == 8)
                {
                    for (i = 0; i < 8; i++)
                        fifo[i] = fifo[i + 1];
                    fifo[8] = dataregi;
                    top = 0;
                }
                fifo[top++] = dataregi;
                count = command[2] * 4 + command[1] * 2 + command[0] * 1;
                // ac=fifo[count];
                ac = dataregi;
                // System.out.println("ac"+ac);

                // return;

            } // in30 ends

            memoryWrite();
            // memoryList.setListData(items);

            // repaint();
            /*
             * if(E==0) { for(int i1=0,i4=20;i1<n1;i1++,i4+=40) { temp1=fifodis[i1];
             * 
             * for(int i2=0;i2<8;i2++) {temp[i2]=temp1%2;temp1=temp1/2;}
             * aa=temp[0];bb=temp[1];cc=temp[2];dd=temp[3];ee=temp[4];ff=temp[5];gg=temp[6];hh=temp[7];
             * //f1(a,b,c,d,e,f,g,h,n1,E,i4); i3=i4+20; // g.drawRect(i+5,50,60,60); if(aa==1)
             * g.drawLine(i3-10,50,i3+10,50); if(bb==1) g.drawLine(i3+10,50,i3+10,80); if(cc==1)
             * g.drawLine(i3+10,80,i3+10,110); if(dd==1) g.drawLine(i3-10,110,i3+10,110); if(ee==1)
             * g.drawLine(i3-10,110,i3-10,80); if(ff==1) g.drawLine(i3-10,80,i3-10,50); if(gg==1)
             * g.drawLine(i3-10,80,i3+10,80);//g.drawLine(i3,70,i3,130);use as . } }
             * 
             * if(E==1 && n1==8) { i4=300; for(int i1=n1-1;i1>=0;i1--,i4-=40) { temp1=fifodis[i1]; for(int
             * i2=0;i2<8;i2++) {temp[i2]=temp1%2;temp1=temp1/2;}
             * aa=temp[0];bb=temp[1];cc=temp[2];dd=temp[3];ee=temp[4];ff=temp[5];gg=temp[6];hh=temp[7]; //
             * f1(a,b,c,d,e,f,g,h,E,i4); // g.drawRect(i+5,50,60,60); if(aa==1) g.drawLine(i3-10,50,i3+10,50); if(bb==1)
             * g.drawLine(i3+10,50,i3+10,80); if(cc==1) g.drawLine(i3+10,80,i3+10,110); if(dd==1)
             * g.drawLine(i3-10,110,i3+10,110); if(ee==1) g.drawLine(i3-10,110,i3-10,80); if(ff==1)
             * g.drawLine(i3-10,80,i3-10,50); if(gg==1) g.drawLine(i3-10,80,i3+10,80);//g.drawLine(i3,70,i3,130);use as
             * . } }
             * 
             * if(E==1 && n1==16) { i4=620; for(int i1=n1-1;i1>=0;i1--,i4-=40) { temp1=fifodis[i1]; for(int
             * i2=0;i2<8;i2++) {temp[i2]=temp1%2;temp1=temp1/2;}
             * aa=temp[0];bb=temp[1];cc=temp[2];dd=temp[3];ee=temp[4];ff=temp[5];gg=temp[6];hh=temp[7]; //
             * f1(a,b,c,d,e,f,g,h,E,i4); i3=i4-20; // g.drawRect(i+5,50,60,60); if(aa==1) g.drawLine(i3-10,50,i3+10,50);
             * if(bb==1) g.drawLine(i3+10,50,i3+10,80); if(cc==1) g.drawLine(i3+10,80,i3+10,110); if(dd==1)
             * g.drawLine(i3-10,110,i3+10,110); if(ee==1) g.drawLine(i3-10,110,i3-10,80); if(ff==1)
             * g.drawLine(i3-10,80,i3-10,50); if(gg==1) g.drawLine(i3-10,80,i3+10,80);//g.drawLine(i3,70,i3,130);use as
             * . }
             * 
             * }
             */
            // g.drawString(message,5,10);
            g.dispose();
        }// paint*


    }



}
