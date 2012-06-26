import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * MicroComputer.java
 * 
 * @version 1.1 01/1/2002
 * @author Abhijeet Iraj
 * @author Nilesh Shirke
 */

public class MicroComputer extends DemoModule implements Runnable
{

    // final CoKit cokit;
    JTextField aField, bField, cField, dField, eField, hField, lField;
    JTextField signField, zeroField, acField, parityField, carryField;
    JTextField spField, pcField, instructionField, tempField;
    JButton assemble, go, trace, Reset;
    JTextArea textArea, errorReporting;
    JTextArea memoryArea;
    JList memoryList;
    JMenuBar jmenubar, fileMenubar;
    JMenu peripherals, file;
    JMenuItem p8155, p8253, p8255, p8279, menuCRO, p8275, open, save, New;
    JButton Open;

    int A, B, C, D, E, H, L, SP, PC, signFlg, zeroFlg, acFlg, parityFlg, carryFlg, PSW;
    int tempReg;
    int memory[];
    int memoryPtr, stackPtr;
    String symbolTable[][] = new String[100][2];
    String[] items = new String[65536];
    int symbolTablePtr;

    FileWriter f1, memoryFr;
    BufferedReader br, memoryBr;
    FileReader fr, memoryFrd;

    FileWriter errorWrt;
    BufferedReader errorRead;

    int startingAddress = 0;
    int opcodeToExplore;
    String oneInstruction;
    int count;
    char errorBuffer[] = new char[2000];
    String errorStr = new String();
    int lineCount = 0;
    StringTokenizer st;
    Thread runthread;
    public static MicroComputer demo;
    File prgFile;
    String fileContents = new String("");;

    Chip8155 frame8155 = new Chip8155("8155(Memory with I/O and Timer)");
    Chip8255 frame8255 = new Chip8255("8255(Programmable Peripheral Interface)");
    Chip8253 frame8253 = new Chip8253("8253(Programmable Interval Timer)");
    Chip8279 frame8279 = new Chip8279("8279(Keyboard and Display Interface)");

    DAC objDAC = new DAC(null);
    CRO frameCRO = new CRO("CRO");

    public static void main(String[] args)
    {
        demo = new MicroComputer(null);
        demo.mainImpl();
    }

    public MicroComputer(CoKit cokit)
    {
        super(cokit, "MicroComputer", "toolbar/MicroComputer.gif");

        // this.cokit=cokit;

        A = 255;
        B = 100;
        C = 30;
        D = 40;
        E = 50;
        H = 60;
        L = 70;
        SP = 2000;
        PC = 0000;
        signFlg = 0;
        zeroFlg = 0;
        acFlg = 0;
        parityFlg = 0;
        carryFlg = 0;
        memory = new int[65536];
        memoryPtr = 0;
        startingAddress = 0;
        stackPtr = 65535;
        symbolTablePtr = 0;
        count = 0;

        JPanel demo = getDemoPanel();
        demo.setLayout(null);

        JPanel debugPanel = new JPanel();
        debugPanel.setLayout(null);

        jmenubar = new JMenuBar();
        jmenubar.setBackground(Color.white);
        jmenubar.setSize(80, 27);
        jmenubar.setLocation(10, 10);

        peripherals = new JMenu("Peripherals");
        peripherals.setBackground(Color.white);
        p8155 = new JMenuItem("8155-Memory with I/O and Timer");
        p8253 = new JMenuItem("8253-Programmable Interval Timer");
        p8255 = new JMenuItem("8255-Programmable Peripheral Interface");
        p8279 = new JMenuItem("8279-Keyboard and Display Interface");
        menuCRO = new JMenuItem("CRO-Cathode Ray Oscillater");
        p8275 = new JMenuItem("8275-Cathode Ray Tube Controller");

        p8155.setActionCommand("8155");
        p8155.addActionListener(new ShowAction(this));
        p8253.setActionCommand("8253");
        p8253.addActionListener(new ShowAction(this));
        p8255.setActionCommand("8255");
        p8255.addActionListener(new ShowAction(this));
        p8279.setActionCommand("8279");
        p8279.addActionListener(new ShowAction(this));
        menuCRO.setActionCommand("menuCRO");
        menuCRO.addActionListener(new ShowAction(this));

        peripherals.add(p8253);
        peripherals.add(p8255);
        peripherals.add(p8279);
        peripherals.add(menuCRO);
        peripherals.addSeparator();
        peripherals.add(p8155);
        peripherals.add(p8275);

        jmenubar.add(peripherals);
        debugPanel.add(jmenubar);

        // accumulator
        aField = new JTextField("A-00H", 6);
        aField.setSize(80, 30);
        aField.setLocation(40, 160);
        aField.setForeground(Color.green.brighter());
        aField.setBackground(Color.black);
        aField.setEditable(false);
        aField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(aField);

        // Temperory
        tempField = new JTextField("T-00H", 6);
        tempField.setSize(80, 30);
        tempField.setLocation(140, 160);
        tempField.setForeground(Color.green.brighter());
        tempField.setBackground(Color.black);
        tempField.setEditable(false);
        tempField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(tempField);

        // Flags
        signField = new JTextField("S-0", 3);
        signField.setSize(35, 25);
        signField.setLocation(40, 400);
        signField.setForeground(Color.green.brighter());
        signField.setBackground(Color.black);
        signField.setEditable(false);
        signField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(signField);

        zeroField = new JTextField("Z-0", 3);
        zeroField.setSize(35, 25);
        zeroField.setLocation(75, 400);
        zeroField.setForeground(Color.green.brighter());
        zeroField.setBackground(Color.black);
        zeroField.setEditable(false);
        zeroField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(zeroField);

        acField = new JTextField("AC-0", 3);
        acField.setSize(35, 25);
        acField.setLocation(110, 400);
        acField.setForeground(Color.green.brighter());
        acField.setBackground(Color.black);
        acField.setEditable(false);
        acField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(acField);

        parityField = new JTextField("P-0", 3);
        parityField.setSize(35, 25);
        parityField.setLocation(145, 400);
        parityField.setForeground(Color.green.brighter());
        parityField.setBackground(Color.black);
        parityField.setEditable(false);
        parityField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(parityField);

        carryField = new JTextField("C-0", 3);
        carryField.setSize(35, 25);
        carryField.setLocation(180, 400);
        carryField.setForeground(Color.green.brighter());
        carryField.setBackground(Color.black);
        carryField.setEditable(false);
        carryField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(carryField);

        bField = new JTextField("B-00H", 7);
        bField.setSize(80, 30);
        bField.setLocation(405, 185);
        bField.setForeground(Color.green.brighter());
        bField.setBackground(Color.black);
        bField.setEditable(false);
        bField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(bField);

        cField = new JTextField("C-00H", 7);
        cField.setSize(80, 30);
        cField.setLocation(490, 185);
        cField.setForeground(Color.green.brighter());
        cField.setBackground(Color.black);
        cField.setEditable(false);
        cField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(cField);

        dField = new JTextField("D-00H", 7);
        dField.setSize(80, 30);
        dField.setLocation(405, 220);
        dField.setForeground(Color.green.brighter());
        dField.setBackground(Color.black);
        dField.setEditable(false);
        dField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(dField);

        eField = new JTextField("E-00H", 7);
        eField.setSize(80, 30);
        eField.setLocation(490, 220);
        eField.setForeground(Color.green.brighter());
        eField.setBackground(Color.black);
        eField.setEditable(false);
        eField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(eField);

        hField = new JTextField("H-00H", 10);
        hField.setSize(80, 30);
        hField.setLocation(405, 255);
        hField.setForeground(Color.green.brighter());
        hField.setBackground(Color.black);
        hField.setEditable(false);
        hField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(hField);

        lField = new JTextField("L-00H", 10);
        lField.setSize(80, 30);
        lField.setLocation(490, 255);
        lField.setForeground(Color.green.brighter());
        lField.setBackground(Color.black);
        lField.setEditable(false);
        lField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(lField);

        // special purpose registers
        spField = new JTextField("SP-FFFFH", 10);
        spField.setSize(165, 30);
        spField.setLocation(405, 290);
        spField.setForeground(new Color(128, 128, 255));
        spField.setBackground(Color.black);
        spField.setEditable(false);
        spField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(spField);

        pcField = new JTextField("PC-0000H", 10);
        pcField.setSize(165, 30);
        pcField.setLocation(405, 325);
        pcField.setForeground(new Color(128, 128, 255));
        pcField.setBackground(Color.black);
        pcField.setEditable(false);
        pcField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(pcField);

        instructionField = new JTextField("ORG 2000", 15);
        instructionField.setSize(165, 30);
        instructionField.setLocation(610, 400);
        instructionField.setForeground(Color.yellow);
        instructionField.setBackground(Color.black);
        instructionField.setEditable(false);
        instructionField.setHorizontalAlignment(JTextField.CENTER);
        debugPanel.add(instructionField);

        go = new JButton(createImageIcon("buttons/mcgo1.gif", ""));
        go.setPressedIcon(createImageIcon("buttons/mcgo2.gif", ""));
        go.setSize(100, 70);
        go.setLocation(670, 10);
        go.setActionCommand("Go");
        go.setFocusPainted(false);
        go.setBorderPainted(false);
        go.setContentAreaFilled(false);
        go.setMargin(new Insets(0, 0, 0, 0));
        go.addActionListener(new ShowAction(this));
        debugPanel.add(go);

        trace = new JButton(createImageIcon("buttons/mctrace1.gif", ""));
        trace.setPressedIcon(createImageIcon("buttons/mctrace2.gif", ""));
        trace.setSize(100, 70);
        trace.setLocation(675, 60);
        trace.setActionCommand("Trace");
        trace.setFocusPainted(false);
        trace.setBorderPainted(false);
        trace.setContentAreaFilled(false);
        trace.setMargin(new Insets(0, 0, 0, 0));
        trace.addActionListener(new ShowAction(this));
        debugPanel.add(trace);

        Reset = new JButton(createImageIcon("buttons/reset1.gif", ""));
        Reset.setPressedIcon(createImageIcon("buttons/reset2.gif", ""));
        Reset.setSize(100, 70);
        Reset.setLocation(475, 10);
        Reset.setActionCommand("Reset");
        Reset.setFocusPainted(false);
        Reset.setBorderPainted(false);
        Reset.setContentAreaFilled(false);
        Reset.setMargin(new Insets(0, 0, 0, 0));
        Reset.addActionListener(new ShowAction(this));
        debugPanel.add(Reset);

        memoryWrite();

        memoryList = new JList(items);

        MouseListener mouseListener = new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    int index = memoryList.locationToIndex(e.getPoint());
                    System.out.println("Double clicked on Item " + index);
                    String opcodeStr = JOptionPane.showInputDialog(getDemoPanel(), "Edit Memory:");
                    int opcoden = 0;
                    try
                    {
                        opcoden = java.lang.Integer.parseInt(opcodeStr, 16);
                    }
                    catch (NumberFormatException nfe)
                    {
                        opcoden = 0;
                    }
                    memory[index] = opcoden;
                    memoryWrite();
                    memoryList = new JList(items);
                    startingAddress = 0;

                }
            }
        };
        memoryList.addMouseListener(mouseListener);


        memoryList.setVisibleRowCount(5);
        memoryList.setFixedCellWidth(4);
        memoryList.setBackground(Color.black);
        memoryList.setForeground(Color.green.brighter());
        JScrollPane jscrollpane = new JScrollPane(memoryList);
        jscrollpane.setSize(130, 210);
        jscrollpane.setLocation(630, 130);
        debugPanel.add(jscrollpane);

        // add background
        mcDebugDesignPanel mcDebug = new mcDebugDesignPanel();
        mcDebug.setSize(800, 520);
        mcDebug.setLocation(0, -20);
        debugPanel.add(mcDebug);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(null);

        String text = new String("");
        textArea = new JTextArea(text);
        textArea.setForeground(new Color(128, 255, 255));
        textArea.setBackground(new Color(100, 110, 120));
        Font font = new Font("Times-Roman", Font.BOLD, 18);
        textArea.setFont(font);

        JScrollPane scroller = new JScrollPane()
        {
            public Dimension getPreferredSize()
            {
                return new Dimension(200, 100);
            }

            public float getAlignmentX()
            {
                return LEFT_ALIGNMENT;
            }
        };

        scroller.getViewport().add(textArea);
        textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        scroller.setSize(230, 400);
        scroller.setLocation(100, 40);
        editPanel.add(scroller);

        fileMenubar = new JMenuBar();
        fileMenubar.setBackground(Color.white);
        fileMenubar.setSize(40, 27);
        fileMenubar.setLocation(100, 10);

        file = new JMenu("File");
        file.setBackground(Color.white);
        New = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        New.addActionListener(new ShowAction(this));
        open.addActionListener(new ShowAction(this));
        save.addActionListener(new ShowAction(this));
        file.add(New);
        file.add(open);
        file.add(save);
        fileMenubar.add(file);

        editPanel.add(fileMenubar);


        errorReporting = new JTextArea("Error Reporting");
        errorReporting.setForeground(new Color(128, 255, 255));
        errorReporting.setBackground(new Color(100, 110, 120));

        JScrollPane scroller1 = new JScrollPane()
        {
            public Dimension getPreferredSize()
            {
                return new Dimension(200, 100);
            }

            public float getAlignmentX()
            {
                return LEFT_ALIGNMENT;
            }
        };

        scroller1.getViewport().add(errorReporting);
        scroller1.setSize(230, 400);
        scroller1.setLocation(490, 40);
        editPanel.add(scroller1);

        assemble = new JButton(createImageIcon("buttons/assemble2.gif", ""));
        assemble.setPressedIcon(createImageIcon("buttons/assemble1.gif", ""));
        assemble.setSize(140, 100);
        assemble.setLocation(140, 420);
        assemble.setActionCommand("Assemble");
        assemble.setFocusPainted(false);
        assemble.setBorderPainted(false);
        assemble.setContentAreaFilled(false);
        assemble.setMargin(new Insets(0, 0, 0, 0));
        assemble.addActionListener(new ShowAction(this));
        editPanel.add(assemble);

        mcEditDesignPanel mcEdit = new mcEditDesignPanel();
        mcEdit.setSize(800, 490);
        mcEdit.setLocation(0, 0);
        editPanel.add(mcEdit);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, editPanel, debugPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0);
        splitPane.setSize(800, 500);
        splitPane.setLocation(0, 0);

        demo.add(splitPane);
    }

    public JFileChooser createFileChooser()
    {
        // create a filechooser
        JFileChooser fc = new JFileChooser();

        // set the current directory to be the images directory
        File swingFile = new File("resources/cokit.gif");
        if (swingFile.exists())
        {
            fc.setCurrentDirectory(swingFile);
            fc.setSelectedFile(swingFile);
        }

        return fc;
    }


    // Event Handling start

    class ShowAction extends AbstractAction
    {
        MicroComputer demo;


        public ShowAction(MicroComputer demo)
        {
            this.demo = demo;
        }

        public void OpenFile()
        {
            // create a filechooser
            JFileChooser fc = new JFileChooser();

            int result = fc.showOpenDialog(getDemoPanel());

            System.out.println("Result" + result);
            if (result == JFileChooser.APPROVE_OPTION)
            {


                // set the current directory to be the images directory
                prgFile = null;

                String str = new String();
                prgFile = fc.getSelectedFile();

                try
                {
                    fr = new FileReader(prgFile);
                    br = new BufferedReader(fr);
                }
                catch (IOException rioa)
                {
                }

                fileContents = "";
                while (true)
                {
                    try
                    {
                        String temp = br.readLine();
                        if (temp == null)
                            break;
                        fileContents += temp + "\n";
                    }
                    catch (Exception e)
                    {
                        break;
                    }

                }
                textArea.setText(fileContents);

            }

        }




        public void actionPerformed(ActionEvent e)
        {
            String str = e.getActionCommand();

            if (str.equals("Open"))
            {
                OpenFile();
                lineCount = 0;
                errorStr = new String("");

            }
            if (str.equals("New"))
                textArea.setText("");

            if (str.equals("Save"))
            {
                String Filename = new String();
                String temp1 = new String(textArea.getText());
                File f = new File(Filename);

                JFileChooser fc = new JFileChooser();
                int result = fc.showSaveDialog(getDemoPanel());

                prgFile = fc.getSelectedFile();

                try
                {
                    OutputStream os = new FileOutputStream(prgFile);
                    byte b[] = temp1.getBytes();
                    os.write(b);
                    os.close();
                }
                catch (Exception ie)
                {
                    System.out.println(ie);
                }
            }

            if (str.equals("Assemble"))
            {
                count = 0;
                symbolTablePtr = 0;
                lineCount = 0;
                errorStr = new String("");
                String source = new String(textArea.getText());
                char buffer[] = new char[source.length()];
                source.getChars(0, source.length(), buffer, 0);

                try
                {
                    f1 = new FileWriter("prgo.8085");
                    f1.write(buffer);
                    f1.close();
                }
                catch (IOException wio)
                {
                }

                try
                {
                    fr = new FileReader("prgo.8085");
                    br = new BufferedReader(fr);
                }
                catch (IOException rio)
                {
                }

                // try
                // {
                prgAssemble(1);
                // }catch(Exception passOne){System.out.println("Pass 1:"+passOne);} //First pass of assembler

                // symbolTable[symbolTablePtr][0]=new String(symbol);


                try
                {
                    errorWrt = new FileWriter("error.txt");
                }
                catch (IOException abhya)
                {
                }


                try
                {
                    fr = new FileReader("prgo.8085");
                    br = new BufferedReader(fr);
                }
                catch (IOException rioa)
                {
                }

                lineCount = 0;
                errorStr = new String("");

                symbolTablePtr = 0;
                // try
                // {
                prgAssemble(2);
                // }catch(Exception passTwo){System.out.println("Pass 2:"+passTwo);} //Second pass of assembler
                // }

                // Runtime.getRuntime().gc();


                try
                {
                    errorStr.getChars(0, errorStr.length(), errorBuffer, 0);
                    errorWrt.write(errorBuffer);
                    errorWrt.close();
                }
                catch (Exception asde)
                {
                }

                errorReporting.setText(errorStr);

                for (int i = 0; i < symbolTablePtr; i++)
                    System.out.println(symbolTable[i][0] + " " + symbolTable[i][1] + " " + symbolTablePtr);

                stackPtr = 65535;
                frame8253.outCount = 0;
                update(startingAddress, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "");

                memoryWrite();
                memoryList.setListData(items);
                memoryList.setSelectedIndex(startingAddress);

                memoryPtr = startingAddress;
            }

            if (str.equals("Trace"))
            {
                opcodeToExplore = memory[memoryPtr];
                runPrg(opcodeToExplore, true);
            }

            if (str.equals("Go"))
            {
                runthread = new Thread(demo);
                runthread.start();
            }

            if (str.equals("Reset"))
            {
                memoryList.setSelectedIndex(startingAddress);
                memoryPtr = startingAddress;
            }

            if (str.equals("8155"))
            {
                frame8255.setVisible(false);
                frame8253.setVisible(false);

                frame8155.setBounds(10, 15, 780, 520);
                // frame8155.setResizable(false);
                frame8155.setVisible(true);
                frame8155.show();
                frame8155.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        frame8155.setVisible(false);
                    }
                });
            }

            if (str.equals("8279"))
            {
                frame8255.setVisible(false);
                frame8253.setVisible(false);

                frame8279.setBounds(10, 0, 780, 600);
                // frame8279.setResizable(false);
                frame8279.setVisible(true);
                frame8279.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        frame8279.setVisible(false);
                    }
                });
                frame8279.show();

            }

            if (str.equals("8253"))
            {
                frame8255.setVisible(false);
                frame8155.setVisible(false);

                frame8253.setBounds(10, 15, 780, 550);
                // frame8253.setResizable(false);
                frame8253.setVisible(true);
                frame8253.show();
                frame8253.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        frame8253.setVisible(false);
                    }
                });
            }

            if (str.equals("8255"))
            {
                frame8155.setVisible(false);
                frame8253.setVisible(false);

                frame8255.setBounds(10, 15, 780, 550);
                // frame8255.setResizable(false);
                frame8255.setVisible(true);
                frame8255.show();

                frame8255.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        frame8255.setVisible(false);
                    }
                });

            }

            if (str.equals("menuCRO"))
            {
                frame8155.setVisible(false);
                frame8253.setVisible(false);

                frameCRO.setBounds(10, 15, 780, 550);
                // frameCRO.setResizable(false);
                frameCRO.setVisible(true);
                frameCRO.show();

                frameCRO.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        menuCRO.setVisible(false);
                    }
                });

            }


        }
    }

    public void run()
    {
        memoryPtr = startingAddress;
        System.out.println("memoryPtr: " + memoryPtr);
        while (true)
        {

            opcodeToExplore = memory[memoryPtr];
            if (opcodeToExplore == 118)
                break;
            System.out.println("memoryPtr: " + memoryPtr + " OPCODE: " + opcodeToExplore);
            runPrg(opcodeToExplore, false);
        }
        memoryWrite();
        memoryList.setListData(items);
        update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,A");
        System.out.println("abhya");
    }



    void prgAssemble(int pass)// throws Exception
    {
        while (true)
        {
            try
            {
                oneInstruction = new String(br.readLine());
                lineCount++;
            }
            catch (Exception e)
            {
                return;
            }

            oneInstruction = new String(oneInstruction.trim());
            oneInstruction = new String(oneInstruction.toUpperCase());

            if (oneInstruction.equals(""))
                continue;

            // if(pass==1)
            {
                // SCAN DATA SEGMENT

                if (oneInstruction.equals("START DATA"))
                {
                    String dataOrigin = new String();
                    try
                    {
                        dataOrigin = new String(br.readLine());
                    }
                    catch (Exception e)
                    {
                    }

                    dataOrigin = new String(dataOrigin.trim());
                    dataOrigin = new String(dataOrigin.toUpperCase());

                    int org = dataOrigin.indexOf("ORG");
                    if (org == -1)
                    {
                        errorStr = new String(errorStr + "** Warning ** " + oneInstruction + "(" + lineCount + ")" + "Missing Origin " + "\n");
                        continue;
                    }
                    else
                    {
                        String secondPart = new String(dataOrigin.substring(3, dataOrigin.length()));
                        secondPart = new String(secondPart.trim());
                        secondPart = new String(secondPart.toUpperCase());

                        if (secondPart.charAt(secondPart.length() - 1) == 'H')
                        {
                            secondPart = new String(secondPart.substring(0, secondPart.length() - 1));
                            if (java.lang.Integer.parseInt(secondPart, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "Address out of range "
                                        + "\n");
                                continue;
                            }
                            memoryPtr = java.lang.Integer.parseInt(secondPart, 16);
                        }
                        else
                        {
                            secondPart = new String(secondPart.substring(0, secondPart.length()));
                            if (java.lang.Integer.parseInt(secondPart, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "Address out of range "
                                        + "\n");
                                continue;
                            }
                            memoryPtr = java.lang.Integer.parseInt(secondPart, 16);
                        }
                    }

                    while (true)
                    {
                        String operandDeclaration = new String();
                        try
                        {
                            operandDeclaration = new String(br.readLine());
                        }
                        catch (Exception e)
                        {
                        }

                        operandDeclaration = new String(operandDeclaration.trim());
                        operandDeclaration = new String(operandDeclaration.toUpperCase());

                        if (operandDeclaration.equals("END DATA"))
                            break;

                        int opType = operandDeclaration.indexOf("DB");

                        // System.out.println(opType+" "+operandDeclaration.substring(0,opType)+" "+operandDeclaration.substring(opType+2,operandDeclaration.length()));

                        String symbol = new String();
                        String operand = new String();

                        try
                        {
                            symbol = new String(operandDeclaration.substring(0, opType));
                            operand = new String(operandDeclaration.substring(opType + 2, operandDeclaration.length()));
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception: " + e);
                        }

                        symbol = new String(symbol.trim());
                        symbol = new String(symbol.toUpperCase());

                        operand = new String(operand.trim());
                        operand = new String(operand.toUpperCase());

                        // System.exit(0);
                        if (operand.charAt(operand.length() - 1) == 'H')
                        {
                            operand = new String(operand.substring(0, operand.length() - 1));
                            symbolTable[symbolTablePtr][1] = "" + memoryPtr;
                            if (java.lang.Integer.parseInt(operand, 16) > 255)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "operand out of range "
                                        + "\n");
                                continue;
                            }
                            memory[memoryPtr++] = java.lang.Integer.parseInt(operand, 16);
                        }
                        else
                        {
                            operand = new String(operand.substring(0, operand.length()));
                            symbolTable[symbolTablePtr][1] = "" + memoryPtr;
                            if (java.lang.Integer.parseInt(operand, 16) > 255)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "operand out of range "
                                        + "\n");
                                continue;
                            }
                            memory[memoryPtr++] = java.lang.Integer.parseInt(operand);
                        }

                        symbolTable[symbolTablePtr++][0] = symbol;
                    }
                    continue;
                }


                // CHECK FOR LOOP SYMBOL
                int loopSymbolIndex = oneInstruction.indexOf(':');
                if (loopSymbolIndex != -1)
                {
                    // if(pass==1)
                    {
                        String symbol = new String(oneInstruction.substring(0, loopSymbolIndex));
                        symbol = new String(symbol.trim());
                        symbol = new String(symbol.toUpperCase());
                        symbolTable[symbolTablePtr][0] = new String(symbol.trim());
                        symbolTable[symbolTablePtr++][1] = new String("" + memoryPtr);
                    }
                    oneInstruction = new String(oneInstruction.substring(loopSymbolIndex + 1, oneInstruction.length()));
                    oneInstruction = new String(oneInstruction.trim());
                    oneInstruction = new String(oneInstruction.toUpperCase());
                }

                // CHECK FOR COMMENTS
                int commentIndex = oneInstruction.indexOf(";");
                if (commentIndex != -1)
                {
                    oneInstruction = new String(oneInstruction.substring(0, commentIndex));
                    oneInstruction = new String(oneInstruction.trim());
                    oneInstruction = new String(oneInstruction.toUpperCase());
                }

                // CHECK FOR SUBROUTINE CALL
                int callSymbolIndex = oneInstruction.indexOf("SUBS");
                if (callSymbolIndex != -1)
                {
                    String symbol = new String(oneInstruction.substring(callSymbolIndex + 4, oneInstruction.length()));
                    symbol = new String(symbol.trim());
                    symbol = new String(symbol.toUpperCase());
                    symbolTable[symbolTablePtr][0] = new String(symbol);

                    String callOrigin = new String();
                    try
                    {
                        callOrigin = new String(br.readLine());
                    }
                    catch (Exception e)
                    {
                    }

                    callOrigin = new String(callOrigin.trim());
                    callOrigin = new String(callOrigin.toUpperCase());

                    int org = callOrigin.indexOf("ORG");
                    if (org == -1)
                    {
                        errorStr = new String(errorStr + "** Warning ** " + oneInstruction + "(" + lineCount + ")" + "Missing Origin " + "\n");
                        continue;
                    }
                    else
                    {
                        String secondPart = new String(callOrigin.substring(3, callOrigin.length()));
                        secondPart = new String(secondPart.trim());
                        secondPart = new String(secondPart.toUpperCase());

                        if (secondPart.charAt(secondPart.length() - 1) == 'H')
                        {
                            secondPart = new String(secondPart.substring(0, secondPart.length() - 1));
                            if (java.lang.Integer.parseInt(secondPart, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "Address out of range "
                                        + "\n");
                                continue;
                            }
                            memoryPtr = java.lang.Integer.parseInt(secondPart, 16);
                        }
                        else
                        {
                            secondPart = new String(secondPart.substring(0, secondPart.length()));
                            if (java.lang.Integer.parseInt(secondPart, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** ERROR ** " + oneInstruction + "(" + lineCount + ")" + "Address out of range "
                                        + "\n");
                                continue;
                            }
                            memoryPtr = java.lang.Integer.parseInt(secondPart);
                        }
                    }
                    symbolTable[symbolTablePtr++][1] = new String("" + memoryPtr);

                    try
                    {
                        oneInstruction = new String(br.readLine());
                        lineCount++;
                    }
                    catch (Exception e)
                    {
                        return;
                    }

                    oneInstruction = new String(oneInstruction.trim());
                    oneInstruction = new String(oneInstruction.toUpperCase());


                }


            }



            /*
             * if(pass==2) { int loopSymbolIndex=oneInstruction.indexOf(':'); if(loopSymbolIndex!=-1) {
             * oneInstruction=new String(oneInstruction.substring(loopSymbolIndex+1,oneInstruction.length()));
             * oneInstruction=new String(oneInstruction.trim()); oneInstruction=new
             * String(oneInstruction.toUpperCase()); } }
             */

            /*** ONE BYTE INSTRUCTION'S ***/

            // MOV A,A
            if (oneInstruction.equals("MOV A,A"))
            {
                String hexa = new String("7F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,B
            else if (oneInstruction.equals("MOV A,B"))
            {
                String hexa = new String("78");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,C
            else if (oneInstruction.equals("MOV A,C"))
            {
                String hexa = new String("79");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,D
            else if (oneInstruction.equals("MOV A,D"))
            {
                String hexa = new String("7A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,E
            else if (oneInstruction.equals("MOV A,E"))
            {
                String hexa = new String("7B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,H
            else if (oneInstruction.equals("MOV A,H"))
            {
                String hexa = new String("7C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,L
            else if (oneInstruction.equals("MOV A,L"))
            {
                String hexa = new String("7D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV A,M
            else if (oneInstruction.equals("MOV A,M"))
            {
                String hexa = new String("7E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,A
            else if (oneInstruction.equals("MOV B,A"))
            {
                String hexa = new String("47");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,B
            else if (oneInstruction.equals("MOV B,B"))
            {
                String hexa = new String("40");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,C
            else if (oneInstruction.equals("MOV B,C"))
            {
                String hexa = new String("41");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,D
            else if (oneInstruction.equals("MOV B,D"))
            {
                String hexa = new String("42");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,E
            else if (oneInstruction.equals("MOV B,E"))
            {
                String hexa = new String("43");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,H
            else if (oneInstruction.equals("MOV B,H"))
            {
                String hexa = new String("44");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,L
            else if (oneInstruction.equals("MOV B,L"))
            {
                String hexa = new String("45");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV B,M
            else if (oneInstruction.equals("MOV B,M"))
            {
                String hexa = new String("46");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,A
            else if (oneInstruction.equals("MOV C,A"))
            {
                String hexa = new String("4F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,B
            else if (oneInstruction.equals("MOV C,B"))
            {
                String hexa = new String("48");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,C
            else if (oneInstruction.equals("MOV C,C"))
            {
                String hexa = new String("49");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,D
            else if (oneInstruction.equals("MOV C,D"))
            {
                String hexa = new String("4A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,E
            else if (oneInstruction.equals("MOV C,E"))
            {
                String hexa = new String("4B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,H
            else if (oneInstruction.equals("MOV C,H"))
            {
                String hexa = new String("4C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,L
            else if (oneInstruction.equals("MOV C,L"))
            {
                String hexa = new String("4D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV C,M
            else if (oneInstruction.equals("MOV C,M"))
            {
                String hexa = new String("4E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,A
            else if (oneInstruction.equals("MOV D,A"))
            {
                String hexa = new String("57");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,B
            else if (oneInstruction.equals("MOV D,B"))
            {
                String hexa = new String("50");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,C
            else if (oneInstruction.equals("MOV D,C"))
            {
                String hexa = new String("51");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,D
            else if (oneInstruction.equals("MOV D,D"))
            {
                String hexa = new String("52");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,E
            else if (oneInstruction.equals("MOV D,E"))
            {
                String hexa = new String("53");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,H
            else if (oneInstruction.equals("MOV D,H"))
            {
                String hexa = new String("54");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,L
            else if (oneInstruction.equals("MOV D,L"))
            {
                String hexa = new String("55");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV D,M
            else if (oneInstruction.equals("MOV D,M"))
            {
                String hexa = new String("56");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,A
            else if (oneInstruction.equals("MOV E,A"))
            {
                String hexa = new String("5F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,B
            else if (oneInstruction.equals("MOV E,B"))
            {
                String hexa = new String("58");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,C
            else if (oneInstruction.equals("MOV E,C"))
            {
                String hexa = new String("59");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,D
            else if (oneInstruction.equals("MOV E,D"))
            {
                String hexa = new String("5A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,E
            else if (oneInstruction.equals("MOV E,E"))
            {
                String hexa = new String("5B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,H
            else if (oneInstruction.equals("MOV E,H"))
            {
                String hexa = new String("5C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,L
            else if (oneInstruction.equals("MOV E,L"))
            {
                String hexa = new String("5D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV E,M
            else if (oneInstruction.equals("MOV E,M"))
            {
                String hexa = new String("5E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,A
            else if (oneInstruction.equals("MOV H,A"))
            {
                String hexa = new String("67");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,B
            else if (oneInstruction.equals("MOV H,B"))
            {
                String hexa = new String("60");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,C
            else if (oneInstruction.equals("MOV H,C"))
            {
                String hexa = new String("61");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,D
            else if (oneInstruction.equals("MOV H,D"))
            {
                String hexa = new String("62");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,E
            else if (oneInstruction.equals("MOV H,E"))
            {
                String hexa = new String("63");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,H
            else if (oneInstruction.equals("MOV H,H"))
            {
                String hexa = new String("64");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,L
            else if (oneInstruction.equals("MOV H,L"))
            {
                String hexa = new String("65");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV H,M
            else if (oneInstruction.equals("MOV H,M"))
            {
                String hexa = new String("66");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,A
            else if (oneInstruction.equals("MOV L,A"))
            {
                String hexa = new String("6F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,B
            else if (oneInstruction.equals("MOV L,B"))
            {
                String hexa = new String("68");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,C
            else if (oneInstruction.equals("MOV L,C"))
            {
                String hexa = new String("69");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,D
            else if (oneInstruction.equals("MOV L,D"))
            {
                String hexa = new String("6A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,E
            else if (oneInstruction.equals("MOV L,E"))
            {
                String hexa = new String("6B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,H
            else if (oneInstruction.equals("MOV L,H"))
            {
                String hexa = new String("6C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,L
            else if (oneInstruction.equals("MOV L,L"))
            {
                String hexa = new String("6D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV L,M
            else if (oneInstruction.equals("MOV L,M"))
            {
                String hexa = new String("6E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,A
            else if (oneInstruction.equals("MOV M,A"))
            {
                String hexa = new String("77");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,B
            else if (oneInstruction.equals("MOV M,B"))
            {
                String hexa = new String("70");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,C
            else if (oneInstruction.equals("MOV M,C"))
            {
                String hexa = new String("71");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,D
            else if (oneInstruction.equals("MOV M,D"))
            {
                String hexa = new String("72");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,E
            else if (oneInstruction.equals("MOV M,E"))
            {
                String hexa = new String("73");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,H
            else if (oneInstruction.equals("MOV M,H"))
            {
                String hexa = new String("74");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // MOV M,L
            else if (oneInstruction.equals("MOV M,L"))
            {
                String hexa = new String("75");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC A
            else if (oneInstruction.equals("ADC A"))
            {
                String hexa = new String("8F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC B
            else if (oneInstruction.equals("ADC B"))
            {
                String hexa = new String("88");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC C
            else if (oneInstruction.equals("ADC C"))
            {
                String hexa = new String("89");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC D
            else if (oneInstruction.equals("ADC D"))
            {
                String hexa = new String("8A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC E
            else if (oneInstruction.equals("ADC E"))
            {
                String hexa = new String("8B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC H
            else if (oneInstruction.equals("ADC H"))
            {
                String hexa = new String("8C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC L
            else if (oneInstruction.equals("ADC L"))
            {
                String hexa = new String("8D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADC M
            else if (oneInstruction.equals("ADC M"))
            {
                String hexa = new String("8E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD A
            else if (oneInstruction.equals("ADD A"))
            {
                String hexa = new String("87");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD B
            else if (oneInstruction.equals("ADD B"))
            {
                String hexa = new String("80");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD C
            else if (oneInstruction.equals("ADD C"))
            {
                String hexa = new String("81");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD D
            else if (oneInstruction.equals("ADD D"))
            {
                String hexa = new String("82");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD E
            else if (oneInstruction.equals("ADD E"))
            {
                String hexa = new String("83");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD H
            else if (oneInstruction.equals("ADD H"))
            {
                String hexa = new String("84");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD L
            else if (oneInstruction.equals("ADD L"))
            {
                String hexa = new String("85");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ADD M
            else if (oneInstruction.equals("ADD M"))
            {
                String hexa = new String("86");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA A
            else if (oneInstruction.equals("ANA A"))
            {
                String hexa = new String("A7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA B
            else if (oneInstruction.equals("ANA B"))
            {
                String hexa = new String("A0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA C
            else if (oneInstruction.equals("ANA C"))
            {
                String hexa = new String("A1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA D
            else if (oneInstruction.equals("ANA D"))
            {
                String hexa = new String("A2");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA E
            else if (oneInstruction.equals("ANA E"))
            {
                String hexa = new String("A3");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA H
            else if (oneInstruction.equals("ANA H"))
            {
                String hexa = new String("A4");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA L
            else if (oneInstruction.equals("ANA L"))
            {
                String hexa = new String("A5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ANA M
            else if (oneInstruction.equals("ANA M"))
            {
                String hexa = new String("A6");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMA
            else if (oneInstruction.equals("CMA"))
            {
                String hexa = new String("2F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMC
            else if (oneInstruction.equals("CMC"))
            {

                String hexa = new String("3F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP A
            else if (oneInstruction.equals("CMP A"))
            {

                String hexa = new String("BF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP B
            else if (oneInstruction.equals("CMP B"))
            {
                String hexa = new String("B8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP C
            else if (oneInstruction.equals("CMP C"))
            {
                String hexa = new String("B9");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP D
            else if (oneInstruction.equals("CMP D"))
            {
                String hexa = new String("BA");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP E
            else if (oneInstruction.equals("CMP E"))
            {
                String hexa = new String("BB");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP H
            else if (oneInstruction.equals("CMP H"))
            {
                String hexa = new String("BC");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP L
            else if (oneInstruction.equals("CMP L"))
            {
                String hexa = new String("BD");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // CMP M
            else if (oneInstruction.equals("CMP M"))
            {
                String hexa = new String("BE");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DAA
            else if (oneInstruction.equals("DAA"))
            {
                String hexa = new String("27");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DAD B
            else if (oneInstruction.equals("DAD B"))
            {
                String hexa = new String("09");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DAD D
            else if (oneInstruction.equals("DAD D"))
            {
                String hexa = new String("19");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DAD H
            else if (oneInstruction.equals("DAD H"))
            {
                String hexa = new String("29");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DAD SP
            else if (oneInstruction.equals("DAD SP"))
            {
                String hexa = new String("39");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR A
            else if (oneInstruction.equals("DCR A"))
            {
                String hexa = new String("3D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR B
            else if (oneInstruction.equals("DCR B"))
            {
                String hexa = new String("05");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR C
            else if (oneInstruction.equals("DCR C"))
            {
                String hexa = new String("0D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR D
            else if (oneInstruction.equals("DCR D"))
            {
                String hexa = new String("15");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR E
            else if (oneInstruction.equals("DCR E"))
            {
                String hexa = new String("1D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR H
            else if (oneInstruction.equals("DCR H"))
            {
                String hexa = new String("25");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR L
            else if (oneInstruction.equals("DCR L"))
            {
                String hexa = new String("2D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCR M
            else if (oneInstruction.equals("DCR M"))
            {
                String hexa = new String("35");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCX B
            else if (oneInstruction.equals("DCX B"))
            {
                String hexa = new String("0B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCX D
            else if (oneInstruction.equals("DCX D"))
            {
                String hexa = new String("1B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCX H
            else if (oneInstruction.equals("DCX H"))
            {
                String hexa = new String("2B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DCX SP
            else if (oneInstruction.equals("DCX SP"))
            {
                String hexa = new String("3B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // DI
            else if (oneInstruction.equals("DI"))
            {
                String hexa = new String("F3");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // EI
            else if (oneInstruction.equals("EI"))
            {
                String hexa = new String("FB");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // HLT
            else if (oneInstruction.equals("HLT"))
            {
                String hexa = new String("76");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
                // return;
            }

            // INR A
            else if (oneInstruction.equals("INR A"))
            {
                String hexa = new String("3C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR B
            else if (oneInstruction.equals("INR B"))
            {
                String hexa = new String("04");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR C
            else if (oneInstruction.equals("INR C"))
            {
                String hexa = new String("0C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR D
            else if (oneInstruction.equals("INR D"))
            {
                String hexa = new String("14");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR E
            else if (oneInstruction.equals("INR E"))
            {
                String hexa = new String("1C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR H
            else if (oneInstruction.equals("INR H"))
            {
                String hexa = new String("24");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR L
            else if (oneInstruction.equals("INR L"))
            {
                String hexa = new String("2C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INR M
            else if (oneInstruction.equals("INR M"))
            {
                String hexa = new String("34");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INX B
            else if (oneInstruction.equals("INX B"))
            {
                String hexa = new String("03");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INX D
            else if (oneInstruction.equals("INX D"))
            {
                String hexa = new String("13");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INX H
            else if (oneInstruction.equals("INX H"))
            {
                String hexa = new String("23");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // INX SP
            else if (oneInstruction.equals("INX SP"))
            {
                String hexa = new String("33");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // LDAX B
            else if (oneInstruction.equals("LDAX B"))
            {
                String hexa = new String("0A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // LDAX D
            else if (oneInstruction.equals("LDAX D"))
            {
                String hexa = new String("1A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // NOP
            else if (oneInstruction.equals("NOP"))
            {
                String hexa = new String("00");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA A
            else if (oneInstruction.equals("ORA A"))
            {
                String hexa = new String("B7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA B
            else if (oneInstruction.equals("ORA B"))
            {
                String hexa = new String("B0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA C
            else if (oneInstruction.equals("ORA C"))
            {
                String hexa = new String("B1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA D
            else if (oneInstruction.equals("ORA D"))
            {
                String hexa = new String("B2");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA E
            else if (oneInstruction.equals("ORA E"))
            {
                String hexa = new String("B3");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA H
            else if (oneInstruction.equals("ORA H"))
            {
                String hexa = new String("B4");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA L
            else if (oneInstruction.equals("ORA L"))
            {
                String hexa = new String("B5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // ORA M
            else if (oneInstruction.equals("ORA M"))
            {
                String hexa = new String("B6");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // PCHL
            else if (oneInstruction.equals("PCHL"))
            {
                String hexa = new String("E9");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // POP B
            else if (oneInstruction.equals("POP B"))
            {
                String hexa = new String("C1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // POP D
            else if (oneInstruction.equals("POP D"))
            {
                String hexa = new String("D1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // POP H
            else if (oneInstruction.equals("POP H"))
            {
                String hexa = new String("E1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // POP PSW
            else if (oneInstruction.equals("POP PSW"))
            {
                String hexa = new String("F1");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // PUSH B
            else if (oneInstruction.equals("PUSH B"))
            {
                String hexa = new String("C5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // PUSH D
            else if (oneInstruction.equals("PUSH D"))
            {
                String hexa = new String("D5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // PUSH H
            else if (oneInstruction.equals("PUSH H"))
            {
                String hexa = new String("E5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // PUSH PSW
            else if (oneInstruction.equals("PUSH PSW"))
            {
                String hexa = new String("F5");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RAL
            else if (oneInstruction.equals("RAL"))
            {
                String hexa = new String("17");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RAR
            else if (oneInstruction.equals("RAR"))
            {
                String hexa = new String("1F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RC
            else if (oneInstruction.equals("RC"))
            {
                String hexa = new String("D8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RET
            else if (oneInstruction.equals("RET"))
            {
                String hexa = new String("C9");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RIM
            else if (oneInstruction.equals("RIM"))
            {
                String hexa = new String("20");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RLC
            else if (oneInstruction.equals("RLC"))
            {
                String hexa = new String("07");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RM
            else if (oneInstruction.equals("RM"))
            {
                String hexa = new String("F8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RNC
            else if (oneInstruction.equals("RNC"))
            {
                String hexa = new String("D0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RNZ
            else if (oneInstruction.equals("RNZ"))
            {
                String hexa = new String("C0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RP
            else if (oneInstruction.equals("RP"))
            {
                String hexa = new String("F0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RPE
            else if (oneInstruction.equals("RPE"))
            {
                String hexa = new String("E8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RPO
            else if (oneInstruction.equals("RPO"))
            {
                String hexa = new String("E0");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RRC
            else if (oneInstruction.equals("RRC"))
            {
                String hexa = new String("0F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST0
            else if (oneInstruction.equals("RST0"))
            {
                String hexa = new String("C7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST1
            else if (oneInstruction.equals("RST1"))
            {
                String hexa = new String("CF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST2
            else if (oneInstruction.equals("RST2"))
            {
                String hexa = new String("D7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST3
            else if (oneInstruction.equals("RST3"))
            {
                String hexa = new String("DF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST4
            else if (oneInstruction.equals("RST4"))
            {
                String hexa = new String("E7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST5
            else if (oneInstruction.equals("RST5"))
            {
                String hexa = new String("EF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST6
            else if (oneInstruction.equals("RST6"))
            {
                String hexa = new String("F7");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RST7
            else if (oneInstruction.equals("RST7"))
            {
                String hexa = new String("FF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // RZ
            else if (oneInstruction.equals("RZ"))
            {
                String hexa = new String("C8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB A
            else if (oneInstruction.equals("SBB A"))
            {
                String hexa = new String("9F");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB B
            else if (oneInstruction.equals("SBB B"))
            {
                String hexa = new String("98");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB C
            else if (oneInstruction.equals("SBB C"))
            {
                String hexa = new String("99");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB D
            else if (oneInstruction.equals("SBB D"))
            {
                String hexa = new String("9A");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB E
            else if (oneInstruction.equals("SBB E"))
            {
                String hexa = new String("9B");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB H
            else if (oneInstruction.equals("SBB H"))
            {
                String hexa = new String("9C");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB L
            else if (oneInstruction.equals("SBB L"))
            {
                String hexa = new String("9D");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SBB M
            else if (oneInstruction.equals("SBB M"))
            {
                String hexa = new String("9E");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SIM
            else if (oneInstruction.equals("SIM"))
            {
                String hexa = new String("30");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SPHL
            else if (oneInstruction.equals("SPHL"))
            {
                String hexa = new String("F9");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // STAX B
            else if (oneInstruction.equals("STAX B"))
            {
                String hexa = new String("02");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // STAX D
            else if (oneInstruction.equals("STAX D"))
            {
                String hexa = new String("12");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // STC
            else if (oneInstruction.equals("STC"))
            {
                String hexa = new String("37");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB A
            else if (oneInstruction.equals("SUB A"))
            {
                String hexa = new String("97");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB B
            else if (oneInstruction.equals("SUB B"))
            {
                System.out.println("aasgd");
                String hexa = new String("90");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB C
            else if (oneInstruction.equals("SUB C"))
            {
                String hexa = new String("91");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB D
            else if (oneInstruction.equals("SUB D"))
            {
                String hexa = new String("92");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB E
            else if (oneInstruction.equals("SUB E"))
            {
                String hexa = new String("93");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB H
            else if (oneInstruction.equals("SUB H"))
            {
                String hexa = new String("94");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB L
            else if (oneInstruction.equals("SUB L"))
            {
                String hexa = new String("95");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // SUB M
            else if (oneInstruction.equals("SUB M"))
            {
                String hexa = new String("96");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XCHG
            else if (oneInstruction.equals("XCHG"))
            {
                String hexa = new String("EB");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA A
            else if (oneInstruction.equals("XRA A"))
            {
                String hexa = new String("AF");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA B
            else if (oneInstruction.equals("XRA B"))
            {
                String hexa = new String("A8");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA C
            else if (oneInstruction.equals("XRA C"))
            {
                String hexa = new String("A9");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA D
            else if (oneInstruction.equals("XRA D"))
            {
                String hexa = new String("AA");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA E
            else if (oneInstruction.equals("XRA E"))
            {
                String hexa = new String("AB");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA H
            else if (oneInstruction.equals("XRA H"))
            {
                String hexa = new String("AC");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA L
            else if (oneInstruction.equals("XRA L"))
            {
                String hexa = new String("AD");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XRA M
            else if (oneInstruction.equals("XRA M"))
            {
                String hexa = new String("AE");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            // XTHL
            else if (oneInstruction.equals("XTHL"))
            {
                String hexa = new String("E3");
                int n = java.lang.Integer.parseInt(hexa, 16);
                memory[memoryPtr++] = n;
            }

            else
            {
                String firstPartInstr = new String();
                try
                {
                    firstPartInstr = new String(oneInstruction.substring(0, 4));
                }
                catch (Exception e)
                {
                    errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Illegel Instruction " + "\n");
                }

                firstPartInstr = firstPartInstr.trim();

                /*** TWO BYTE INSTRUCTIONS ***/


                if (firstPartInstr.equals("CALL"))
                {
                    int addr;
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    secondPartInstr = new String(secondPartInstr.trim());
                    secondPartInstr = new String(secondPartInstr.toUpperCase());
                    if (pass == 2)
                    {
                        String address = new String(searchAddress(secondPartInstr.trim()));

                        System.out.println(secondPartInstr + "addres: " + address);
                        // System.exit(0);
                        try
                        {
                            addr = java.lang.Integer.parseInt(address);
                        }
                        catch (NumberFormatException e)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Invalid Call" + "\n");
                            continue;
                        }

                        if (addr > 65535)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Address out of Range"
                                    + "\n");
                            continue;
                        }

                        String hexaddr = new String(java.lang.Integer.toHexString(addr));

                        if (hexaddr.length() == 1)
                            hexaddr = "000" + hexaddr;
                        if (hexaddr.length() == 2)
                            hexaddr = "00" + hexaddr;
                        if (hexaddr.length() == 3)
                            hexaddr = "0" + hexaddr;

                        int opcode = java.lang.Integer.parseInt("CD", 16);
                        memory[memoryPtr++] = opcode;
                        String lowerHexAddr = new String(hexaddr.substring(2, 4));
                        int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                        memory[memoryPtr++] = lowerAddr;
                        String higherHexAddr = new String(hexaddr.substring(0, 2));
                        int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                        memory[memoryPtr++] = higherAddr;
                    }
                    else
                        memoryPtr = memoryPtr + 3;
                }

                else if (firstPartInstr.equals("LHLD"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    secondPartInstr = new String(secondPartInstr.trim());

                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);

                    if (c == 'H')
                    {
                        secondPartInstr = new String(secondPartInstr.substring(0, secondPartInstr.length() - 1));
                        secondPartInstr = new String(secondPartInstr.trim());

                        if (secondPartInstr.length() == 1)
                            secondPartInstr = new String("000".concat(secondPartInstr));
                        if (secondPartInstr.length() == 2)
                            secondPartInstr = new String("00".concat(secondPartInstr));
                        if (secondPartInstr.length() == 3)
                            secondPartInstr = new String("0".concat(secondPartInstr));
                        int loadAddress;
                        try
                        {
                            loadAddress = java.lang.Integer.parseInt(secondPartInstr, 16);
                        }
                        catch (NumberFormatException e)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "Illegel Address" + "\n");
                            continue;
                        }

                        if (loadAddress > 65535)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range" + "\n");
                            continue;
                        }

                        int opcode = java.lang.Integer.parseInt("2A", 16);
                        memory[memoryPtr++] = opcode;
                        String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                        int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                        memory[memoryPtr++] = lowerAddr;
                        String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                        int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                        memory[memoryPtr++] = higherAddr;
                    }
                    else
                    {
                        secondPartInstr = new String(secondPartInstr.substring(0, secondPartInstr.length()));
                        secondPartInstr = new String(secondPartInstr.trim());

                        if (secondPartInstr.length() == 1)
                            secondPartInstr = new String("000".concat(secondPartInstr));
                        if (secondPartInstr.length() == 2)
                            secondPartInstr = new String("00".concat(secondPartInstr));
                        if (secondPartInstr.length() == 3)
                            secondPartInstr = new String("0".concat(secondPartInstr));
                        int loadAddress;
                        try
                        {
                            loadAddress = java.lang.Integer.parseInt(secondPartInstr, 16);
                        }
                        catch (NumberFormatException e)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "Illegel Address" + "\n");
                            continue;
                        }

                        if (loadAddress > 65535)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range" + "\n");
                            continue;
                        }

                        int opcode = java.lang.Integer.parseInt("2A", 16);
                        memory[memoryPtr++] = opcode;
                        String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                        int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr);
                        memory[memoryPtr++] = lowerAddr;
                        String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                        int higherAddr = java.lang.Integer.parseInt(higherHexAddr);
                        memory[memoryPtr++] = higherAddr;
                    }
                }

                else if (firstPartInstr.equals("SHLD"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    secondPartInstr = new String(secondPartInstr.trim());

                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);

                    if (c == 'H')
                    {
                        secondPartInstr = new String(secondPartInstr.substring(0, secondPartInstr.length() - 1));
                        secondPartInstr = new String(secondPartInstr.trim());

                        if (secondPartInstr.length() == 1)
                            secondPartInstr = new String("000".concat(secondPartInstr));
                        if (secondPartInstr.length() == 2)
                            secondPartInstr = new String("00".concat(secondPartInstr));
                        if (secondPartInstr.length() == 3)
                            secondPartInstr = new String("0".concat(secondPartInstr));
                        int loadAddress;
                        try
                        {
                            loadAddress = java.lang.Integer.parseInt(secondPartInstr, 16);
                        }
                        catch (NumberFormatException e)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "Illegel Address" + "\n");
                            continue;
                        }

                        if (loadAddress > 65535)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range" + "\n");
                            continue;
                        }

                        int opcode = java.lang.Integer.parseInt("22", 16);
                        memory[memoryPtr++] = opcode;
                        String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                        int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                        memory[memoryPtr++] = lowerAddr;
                        String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                        int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                        memory[memoryPtr++] = higherAddr;
                    }
                    else
                    {
                        secondPartInstr = new String(secondPartInstr.substring(0, secondPartInstr.length()));
                        secondPartInstr = new String(secondPartInstr.trim());

                        if (secondPartInstr.length() == 1)
                            secondPartInstr = new String("000".concat(secondPartInstr));
                        if (secondPartInstr.length() == 2)
                            secondPartInstr = new String("00".concat(secondPartInstr));
                        if (secondPartInstr.length() == 3)
                            secondPartInstr = new String("0".concat(secondPartInstr));
                        int loadAddress;
                        try
                        {
                            loadAddress = java.lang.Integer.parseInt(secondPartInstr, 16);
                        }
                        catch (NumberFormatException e)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "Illegel Address" + "\n");
                            continue;
                        }

                        if (loadAddress > 65535)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range" + "\n");
                            continue;
                        }

                        int opcode = java.lang.Integer.parseInt("22", 16);
                        memory[memoryPtr++] = opcode;
                        String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                        int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr);
                        memory[memoryPtr++] = lowerAddr;
                        String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                        int higherAddr = java.lang.Integer.parseInt(higherHexAddr);
                        memory[memoryPtr++] = higherAddr;
                    }
                }

                else if (firstPartInstr.equals("ORG"))
                {
                    if (pass == 2 && count == 0)
                    {
                        count++;
                        String secondPartInstr = new String(oneInstruction.substring(3, oneInstruction.length()));
                        secondPartInstr = new String(secondPartInstr.trim());

                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        // System.out.println(secondPartInstr+" "+c);

                        if (c == 'H')
                        {
                            secondPartInstr = new String(secondPartInstr.substring(0, secondPartInstr.length() - 1));
                            secondPartInstr = new String(secondPartInstr.trim());
                            int startAddress;
                            try
                            {
                                startAddress = java.lang.Integer.parseInt(secondPartInstr, 16);
                            }
                            catch (NumberFormatException e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "Illegel Address" + "\n");
                                continue;
                            }

                            if (startAddress > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range"
                                        + "\n");
                                continue;
                            }

                            startingAddress = memoryPtr = startAddress;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            secondPartInstr = new String(secondPartInstr.trim());
                            int startAddress = java.lang.Integer.parseInt(secondPartInstr, 10);
                            if (startAddress > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + " Address out of Range"
                                        + "\n");
                                continue;
                            }

                            startingAddress = memoryPtr = startAddress;
                        }
                    }
                }

                else if (firstPartInstr.equals("ACI"))
                {

                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("CE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }
                        memory[memoryPtr++] = operand;

                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("CE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                }

                else if (firstPartInstr.equals("ADI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("C6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("C6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;

                    }
                }

                else if (firstPartInstr.equals("ANI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("E6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("E6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = java.lang.Integer.parseInt(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                }

                else if (firstPartInstr.equals("CPI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("FE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }
                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("FE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                }

                else if (firstPartInstr.equals("ORI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("F6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }
                        memory[memoryPtr++] = operand;

                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("F6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }
                        memory[memoryPtr++] = operand;

                    }
                }

                else if (firstPartInstr.equals("OUT"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("D3", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("D3", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;

                    }
                }


                else if (firstPartInstr.equals("SBI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("DE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("DE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                }

                else if (firstPartInstr.equals("SUI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("D6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("D6", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }
                        memory[memoryPtr++] = operand;
                    }
                }

                else if (firstPartInstr.equals("XRI"))
                {
                    String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                    char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                    if (c == 'H')
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                        int opcode = java.lang.Integer.parseInt("EE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 16);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;
                    }
                    else
                    {
                        secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                        int opcode = java.lang.Integer.parseInt("EE", 16);
                        memory[memoryPtr++] = opcode;
                        int operand = checkOperand(secondPartInstr, 10);
                        if (operand > 255)
                        {
                            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                    + "\n");
                            continue;
                        }

                        memory[memoryPtr++] = operand;

                    }
                }

                else if (firstPartInstr.equals("MVI"))
                {
                    String firstInstr = oneInstruction.substring(0, 6);

                    if (firstInstr.equals("MVI A,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);

                            int opcode = java.lang.Integer.parseInt("3E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());

                            int opcode = java.lang.Integer.parseInt("3E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI B,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);

                            int opcode = java.lang.Integer.parseInt("06", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());

                            int opcode = java.lang.Integer.parseInt("06", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI C,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("0E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("0E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI D,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("16", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("16", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI E,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("1E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("1E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI H,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("26", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("26", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI L,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("2E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("2E", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }

                    else if (firstInstr.equals("MVI M,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("36", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 16);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("36", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = checkOperand(secondPartInstr, 10);
                            if (operand > 255)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            memory[memoryPtr++] = operand;

                        }
                    }
                    else
                    {
                        System.out.println(oneInstruction);
                        errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel instruction" + "\n");
                    }
                }
                /*** THREE BYTE INSTRUCTIONS ***/
                else if (firstPartInstr.equals("LXI"))
                {
                    String firstInstr = oneInstruction.substring(0, 6);

                    if (firstInstr.equals("LXI B,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("01", 16);
                            memory[memoryPtr++] = opcode;
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("01", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr);
                            secondPartInstr = java.lang.Integer.toHexString(operand);
                            // System.out.println(operand+" "+secondPartInstr);

                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }


                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                    }

                    else if (firstInstr.equals("LXI D,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("11", 16);
                            memory[memoryPtr++] = opcode;
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("11", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr);
                            secondPartInstr = java.lang.Integer.toHexString(operand);
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                    }

                    else if (firstInstr.equals("LXI H,"))
                    {
                        String secondPartInstr = oneInstruction.substring(6, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("21", 16);
                            memory[memoryPtr++] = opcode;
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("21", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr);
                            secondPartInstr = java.lang.Integer.toHexString(operand);
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                    }

                    else if (firstInstr.equals("LXI SP"))
                    {
                        String secondPartInstr = oneInstruction.substring(7, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("31", 16);
                            memory[memoryPtr++] = opcode;
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("31", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr);
                            secondPartInstr = java.lang.Integer.toHexString(operand);
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;
                            if (java.lang.Integer.parseInt(secondPartInstr, 16) > 65535)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Operand out of Range"
                                        + "\n");
                                continue;
                            }

                            int operandOne = java.lang.Integer.parseInt(secondPartInstr.substring(0, 2), 16);
                            memory[memoryPtr++] = operandOne;
                            int operandTwo = java.lang.Integer.parseInt(secondPartInstr.substring(2, 4), 16);
                            memory[memoryPtr++] = operandTwo;
                        }
                    }

                }


                else
                {
                    firstPartInstr = new String(oneInstruction.substring(0, 3));
                    firstPartInstr = new String(firstPartInstr.trim());


                    if (firstPartInstr.equals("JC"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            // for(int i=0;i<symbolTablePtr;i++)
                            // System.out.println(symbolTable[i][0]+" "+symbolTable[i][1]+" "+symbolTablePtr);

                            String address = searchAddress(secondPartInstr);
                            // System.out.println("secondPartInstr: "+secondPartInstr+"address: "+address);
                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("DA", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("JM"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }


                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("FA", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("JMP"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("C3", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;


                    }

                    else if (firstPartInstr.equals("JNC"))
                    {

                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("D2", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("JNZ"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = new String(searchAddress(secondPartInstr.trim()));
                            address = new String(address.trim());

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("C2", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("JP"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;

                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("F2", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("JPE"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("EA", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("JPO"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("E2", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("JZ"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("CA", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("LDA"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());

                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);

                            int addr;
                            try
                            {
                                addr = java.lang.Integer.parseInt(address);
                            }
                            catch (Exception e)
                            {
                                errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel jump" + "\n");
                                continue;
                            }

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));
                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("3A", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;

                    }

                    else if (firstPartInstr.equals("STA"))
                    {

                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());

                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;

                            int opcode = java.lang.Integer.parseInt("32", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            if (secondPartInstr.length() == 1)
                                secondPartInstr = "000" + secondPartInstr;
                            if (secondPartInstr.length() == 2)
                                secondPartInstr = "00" + secondPartInstr;
                            if (secondPartInstr.length() == 3)
                                secondPartInstr = "0" + secondPartInstr;

                            int opcode = java.lang.Integer.parseInt("32", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(secondPartInstr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(secondPartInstr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr);
                            memory[memoryPtr++] = higherAddr;
                        }
                    }

                    else if (firstPartInstr.equals("CC"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("DC", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CM"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("FC", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CNC"))
                    {
                        String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("D4", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CNZ"))
                    {
                        String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("C4", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CP"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("F4", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CPE"))
                    {
                        String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("EC", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CPO"))
                    {
                        String secondPartInstr = oneInstruction.substring(4, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("E4", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("CZ"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        secondPartInstr = new String(secondPartInstr.trim());
                        secondPartInstr = new String(secondPartInstr.toUpperCase());
                        if (pass == 2)
                        {
                            String address = searchAddress(secondPartInstr);
                            System.out.println(secondPartInstr);
                            int addr = java.lang.Integer.parseInt(address);

                            String hexaddr = new String(java.lang.Integer.toHexString(addr));

                            if (hexaddr.length() == 1)
                                hexaddr = "000" + hexaddr;
                            if (hexaddr.length() == 2)
                                hexaddr = "00" + hexaddr;
                            if (hexaddr.length() == 3)
                                hexaddr = "0" + hexaddr;

                            int opcode = java.lang.Integer.parseInt("CC", 16);
                            memory[memoryPtr++] = opcode;
                            String lowerHexAddr = new String(hexaddr.substring(2, 4));
                            int lowerAddr = java.lang.Integer.parseInt(lowerHexAddr, 16);
                            memory[memoryPtr++] = lowerAddr;
                            String higherHexAddr = new String(hexaddr.substring(0, 2));
                            int higherAddr = java.lang.Integer.parseInt(higherHexAddr, 16);
                            memory[memoryPtr++] = higherAddr;
                        }
                        else
                            memoryPtr = memoryPtr + 3;
                    }

                    else if (firstPartInstr.equals("IN"))
                    {
                        String secondPartInstr = oneInstruction.substring(3, oneInstruction.length());
                        char c = secondPartInstr.charAt(secondPartInstr.length() - 1);
                        if (c == 'H')
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length() - 1);
                            int opcode = java.lang.Integer.parseInt("DB", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr, 16);
                            memory[memoryPtr++] = operand;
                        }
                        else
                        {
                            secondPartInstr = secondPartInstr.substring(0, secondPartInstr.length());
                            int opcode = java.lang.Integer.parseInt("DB", 16);
                            memory[memoryPtr++] = opcode;
                            int operand = java.lang.Integer.parseInt(secondPartInstr, 10);
                            memory[memoryPtr++] = operand;
                        }
                    }
                    else
                    {
                        System.out.println(oneInstruction);
                        errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Illegel instruction" + "\n");
                    }
                }


            }



        }

    } // End of assembling



    public int checkOperand(String secondPartInstr, int base)
    {
        int operand = 0;
        try
        {
            operand = java.lang.Integer.parseInt(secondPartInstr, base);

        }
        catch (NumberFormatException e)
        {
            errorStr = new String(errorStr + "** Error ** " + oneInstruction + "(" + lineCount + ")" + "  Invalid operand" + "\n");
            operand = 0;
        }

        if (operand > 255)
        {
            errorStr = new String(errorStr + "*** " + oneInstruction + "(" + lineCount + ")" + " Operand Range Invalid" + "\n");
            operand = 0;
        }
        return operand;
    }


    void runPrg(int opcodeToExplore, boolean flag)// throws Exception
    {

        switch (opcodeToExplore)
        {
            case 127: // MOV A,A (7F)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,A");
                break;
            case 120: // MOV A,B (78)
                memoryPtr++;
                A = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,B");
                break;
            case 121: // MOV A,C (79)
                memoryPtr++;
                A = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,C");
                break;
            case 122: // MOV A,D (7A)
                memoryPtr++;
                A = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,D");
                break;
            case 123: // MOV A,E (7B)
                memoryPtr++;
                A = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,E");
                break;
            case 124: // MOV A,H (7C)
                memoryPtr++;
                A = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,H");
                break;
            case 125: // MOV A,L (7D)
                memoryPtr++;
                A = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,L");
                break;
            case 126: // MOV A,M (7E)
                memoryPtr++;
                String h = new String(java.lang.Integer.toHexString(H));
                String l = new String(java.lang.Integer.toHexString(L));
                String hlhex = new String(h.concat(l));
                int hl = java.lang.Integer.parseInt(hlhex, 16);
                A = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV A,M");
                break;
            case 71: // MOV B,A (47)
                memoryPtr++;
                B = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,A");
                break;
            case 64: // MOV B,B (40)
                memoryPtr++;
                B = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,B");
                break;
            case 65: // MOV B,C (41)
                memoryPtr++;
                B = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,C");
                break;
            case 66: // MOV B,D (42)
                memoryPtr++;
                B = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,D");
                break;
            case 67: // MOV B,E (43)
                memoryPtr++;
                B = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,E");
                break;
            case 68: // MOV B,H (44)
                memoryPtr++;
                B = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,H");
                break;
            case 69: // MOV B,L (45)
                memoryPtr++;
                B = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,L");
                break;
            case 70: // MOV B,M (46)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                B = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV B,M");
                break;
            case 79: // MOV C,A (4F)
                memoryPtr++;
                C = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,A");
                break;
            case 72: // MOV C,B (48)
                memoryPtr++;
                C = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,B");
                break;
            case 73: // MOV C,C (49)
                memoryPtr++;
                C = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,C");
                break;
            case 74: // MOV C,D (4A)
                memoryPtr++;
                C = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,D");
                break;
            case 75: // MOV C,E (4B)
                memoryPtr++;
                C = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,E");
                break;
            case 76: // MOV C,H (4C)
                memoryPtr++;
                C = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,H");
                break;
            case 77: // MOV C,L (4D)
                memoryPtr++;
                C = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,L");
                break;
            case 78: // MOV C,M (4E)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                C = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV C,M");
                break;
            case 87: // MOV D,A (57)
                memoryPtr++;
                D = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,A");
                break;
            case 80: // MOV D,B (50)
                memoryPtr++;
                D = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,B");
                break;
            case 81: // MOV D,C (51)
                memoryPtr++;
                D = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,C");
                break;
            case 82: // MOV D,D (52)
                memoryPtr++;
                D = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,D");
                break;
            case 83: // MOV D,E (53)
                memoryPtr++;
                D = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,E");
                break;
            case 84: // MOV D,H (54)
                memoryPtr++;
                D = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,H");
                break;
            case 85: // MOV D,L (55)
                memoryPtr++;
                D = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,L");
                break;
            case 86: // MOV D,M (56)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                D = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV D,M");
                break;
            case 95: // MOV E,A (5F)
                memoryPtr++;
                E = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,A");
                break;
            case 88: // MOV E,B (58)
                memoryPtr++;
                E = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,B");
                break;
            case 89: // MOV E,C (59)
                memoryPtr++;
                E = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,C");
                break;
            case 90: // MOV E,D (5A)
                memoryPtr++;
                E = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,D");
                break;
            case 91: // MOV E,E (5B)
                memoryPtr++;
                E = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,E");
                break;
            case 92: // MOV E,H (5C)
                memoryPtr++;
                E = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,H");
                break;
            case 93: // MOV E,L (5D)
                memoryPtr++;
                E = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,L");
                break;
            case 94: // MOV E,M (5E)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                E = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV E,M");
                break;
            case 103: // MOV H,A (67)
                memoryPtr++;
                H = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,A");
                break;
            case 96: // MOV H,B (60)
                memoryPtr++;
                H = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,B");
                break;
            case 97: // MOV H,C (61)
                memoryPtr++;
                H = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,C");
                break;
            case 98: // MOV H,D (62)
                memoryPtr++;
                H = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,D");
                break;
            case 99: // MOV H,E (63)
                memoryPtr++;
                H = E;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,E");
                break;
            case 100: // MOV H,H (64)
                memoryPtr++;
                H = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,H");
                break;
            case 101: // MOV H,L (65)
                memoryPtr++;
                H = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,L");
                break;
            case 102: // MOV H,M (66)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                H = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV H,M");
                break;
            case 111: // MOV L,A (6F)
                memoryPtr++;
                L = A;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,A");
                break;
            case 104: // MOV L,B (68)
                memoryPtr++;
                L = B;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,B");
                break;
            case 105: // MOV L,C (69)
                memoryPtr++;
                L = C;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,C");
                break;
            case 106: // MOV L,D (6A)
                memoryPtr++;
                L = D;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,D");
                break;
            case 107: // MOV L,E (6B)
                memoryPtr++;
                L = E;
                update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,E");
                break;
            case 108: // MOV L,H (6C)
                memoryPtr++;
                L = H;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,H");
                break;
            case 109: // MOV L,L (6D)
                memoryPtr++;
                L = L;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,L");
                break;
            case 110: // MOV L,M (6E)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                L = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV L,M");
                break;
            case 119: // MOV M,A (77)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = A;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,A");
                }
                break;
            case 112: // MOV M,B (70)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = B;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,B");
                }
                break;
            case 113: // MOV M,C (71)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = C;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,C");
                }
                break;
            case 114: // MOV M,D (72)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = D;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,D");
                }
                break;
            case 115: // MOV M,E (73)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = E;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,E");
                }
                break;
            case 116: // MOV M,H (74)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = H;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,H");
                }
                break;
            case 117: // MOV M,L (75)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = L;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MOV M,L");
                }
                break;
            case 62: // MVI A,DATA (3E)
                memoryPtr++;
                A = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI A,DATA");
                break;
            case 06: // MVI B,DATA (06)
                memoryPtr++;
                B = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI B,DATA");
                break;
            case 14: // MVI C,DATA (OE)
                memoryPtr++;
                C = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI C,DATA");
                break;
            case 22: // MVI D,DATA (16)
                memoryPtr++;
                D = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI D,DATA");
                break;
            case 30: // MVI E,DATA (1E)
                memoryPtr++;
                E = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI E,DATA");
                break;
            case 38: // MVI H,DATA (26)
                memoryPtr++;
                H = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI H,DATA");
                break;
            case 46: // MVI L,DATA (2E)
                memoryPtr++;
                L = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI L,DATA");
                break;
            case 54: // MVI M,DATA (36)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = memory[memoryPtr++];
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "MVI M,DATA");
                }
                break;
            case 206: // ACI DATA (CE)
                memoryPtr++;

                acFlg = checkAcFlg(A, memory[memoryPtr] + 1);

                A = A + memory[memoryPtr++] + carryFlg;

                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                String binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC A");
                break;
            case 143: // ADC A (8F)
                memoryPtr++;

                acFlg = checkAcFlg(A, A + carryFlg);

                A = A + A + carryFlg;


                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC A");
                break;
            case 136: // ADC B (88)
                memoryPtr++;

                acFlg = checkAcFlg(A, B + carryFlg);

                A = A + B + carryFlg;

                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC B");
                break;
            case 137: // ADC C (89)
                memoryPtr++;

                acFlg = checkAcFlg(A, C + carryFlg);

                A = A + C + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC C");

                break;
            case 138: // ADC D (8A)
                memoryPtr++;

                acFlg = checkAcFlg(A, D + carryFlg);

                A = A + D + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC D");
                break;
            case 139: // ADC E (8B)
                memoryPtr++;

                acFlg = checkAcFlg(A, E + carryFlg);

                A = A + E + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC E");
                break;
            case 140: // ADC H (8C)
                memoryPtr++;

                acFlg = checkAcFlg(A, H + carryFlg);

                A = A + H + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC H");

                break;
            case 141: // ADC L (8D)
                memoryPtr++;

                acFlg = checkAcFlg(A, L + carryFlg);

                A = A + L + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC L");
                break;
            case 142: // ADC M (8E)
                memoryPtr++;


                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = checkAcFlg(A, memory[hl] + carryFlg);

                A = A + memory[hl] + carryFlg;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;


                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADC M");
                break;
            case 135: // ADD A (87)
                memoryPtr++;

                acFlg = checkAcFlg(A, A);

                A = A + A;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD A");
                break;
            case 128: // ADD B (80)
                memoryPtr++;

                acFlg = checkAcFlg(A, B);

                A = A + B;

                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD B");
                break;
            case 129: // ADD C (81)
                memoryPtr++;

                acFlg = checkAcFlg(A, C);

                A = A + C;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD C");
                break;
            case 130: // ADD D (82)
                memoryPtr++;

                acFlg = checkAcFlg(A, D);

                A = A + D;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD D");
                break;
            case 131: // ADD E (83)
                memoryPtr++;

                acFlg = checkAcFlg(A, E);

                A = A + E;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD E");

                break;
            case 132: // ADD H (84)
                memoryPtr++;

                acFlg = checkAcFlg(A, H);

                A = A + H;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD H");
                break;
            case 133: // ADD L (85)
                memoryPtr++;

                acFlg = checkAcFlg(A, L);

                A = A + L;
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;


                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD L");
                break;
            case 134: // ADD M (86)
                memoryPtr++;

                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = checkAcFlg(A, memory[hl]);

                A = A + memory[hl];
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;


                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADD M");
                break;
            case 198: // ADI DATA (C6)
                memoryPtr++;

                acFlg = checkAcFlg(A, memory[memoryPtr]);

                A = A + memory[memoryPtr++];
                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;


                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ADI DATA");
                break;
            case 167: // ANA A (A7)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & A;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA A");
                break;
            case 160: // ANA B (A0)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & B;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA B");
                break;
            case 161: // ANA C (A1)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & C;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA C");
                break;
            case 162: // ANA D (A2)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & D;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA D");
                break;
            case 163: // ANA E (A3)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & E;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA E");
                break;
            case 164: // ANA H (A4)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & H;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA H");
                break;
            case 165: // ANA L (A5)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & L;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA L");
                break;
            case 166: // ANA M (A6)
                memoryPtr++;

                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = 1;
                carryFlg = 0;

                A = A & memory[hl];

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANA M");
                break;
            case 230: // ANI DATA (E6)
                memoryPtr++;

                acFlg = 1;
                carryFlg = 0;

                A = A & memory[memoryPtr++];

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ANI DATA");
                break;
            case 205: // CALL PPQQ (CD)
                memoryPtr++;

                String lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                String higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                String address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                memoryPtr = java.lang.Integer.parseInt(address);

                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);

                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CALL " + address);
                }
                break;
            case 220: // CC PPQQ (DC)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (carryFlg == 1)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CC " + address);
                break;
            case 252: // CM PPQQ (FC)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (signFlg == 1)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CM " + address);
                break;
            case 212: // CNC PPQQ (D4)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (carryFlg == 0)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CNC " + address);
                break;
            case 196: // CNZ PPQQ (C4)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (zeroFlg == 0)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CNZ " + address);
                break;
            case 244: // CP PPQQ (F4)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (signFlg == 0)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CP " + address);
                break;

            case 236: // CPE PPQQ (EC)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (parityFlg == 1)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CPE " + address);
                break;

            case 228: // CPO PPQQ (E4)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (parityFlg == 0)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CPO " + address);
                break;

            case 204: // CZ PPQQ (CC)
                memoryPtr++;

                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);

                hlhex = new String(java.lang.Integer.toHexString(memoryPtr));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                if (zeroFlg == 1)
                {
                    memory[--stackPtr] = java.lang.Integer.parseInt(h, 16);
                    memory[--stackPtr] = java.lang.Integer.parseInt(l, 16);

                    memoryPtr = java.lang.Integer.parseInt(address);

                    if (flag == true)
                    {
                        memoryWrite();
                        memoryList.setListData(items);
                    }
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CZ " + address);
                break;

            case 47: // CMA (2F)
                memoryPtr++;
                int num = 255;
                A = A ^ num;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMA");
                break;
            case 63: // CMC (3F)
                memoryPtr++;
                if (carryFlg == 0)
                    carryFlg = 1;
                else
                    carryFlg = 0;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMC");
                break;
            case 191: // CMP A (BF)
                memoryPtr++;
                signFlg = 0;
                carryFlg = 0;
                parityFlg = 1;
                zeroFlg = 1;
                acFlg = 0;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP A");
                break;
            case 184: // CMP B (B8)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, B);

                if (A > B)
                {
                    tempReg = A;
                    tempReg = tempReg - B;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == B)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < B)
                {
                    tempReg = B;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP B");
                break;
            case 185: // CMP C (B9)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, C);

                if (A > C)
                {
                    tempReg = A;
                    tempReg = tempReg - C;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == C)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < C)
                {
                    tempReg = C;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP C");
                break;
            case 186: // CMP D (BA)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, D);

                if (A > D)
                {
                    tempReg = A;
                    tempReg = tempReg - D;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == D)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < D)
                {
                    tempReg = D;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP D");
                break;
            case 187: // CMP E (BB)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, E);

                if (A > E)
                {
                    tempReg = A;
                    tempReg = tempReg - E;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == E)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < E)
                {
                    tempReg = E;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP E");
                break;
            case 188: // CMP H (BC)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, H);

                if (A > H)
                {
                    tempReg = A;
                    tempReg = tempReg - H;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == H)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < H)
                {
                    tempReg = H;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP H");
                break;
            case 189: // CMP L (BD)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, L);

                if (A > L)
                {
                    tempReg = A;
                    tempReg = tempReg - L;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == L)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < L)
                {
                    tempReg = L;
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP L");
                break;
            case 190: // CMP M (BE)
                memoryPtr++;

                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = checkAcFlgTwo(A, memory[hl]);

                if (A > memory[hl])
                {
                    tempReg = A;
                    tempReg = tempReg - memory[hl];
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == memory[hl])
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < memory[hl])
                {
                    tempReg = memory[hl];
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CMP M");
                break;
            case 254: // CPI DATA (FE)
                memoryPtr++;

                tempReg = memory[memoryPtr++];

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    tempReg = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    tempReg = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    tempReg = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    tempReg = 256 - tempReg;
                }

                parityFlg = sumdig(tempReg);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(tempReg));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "CPI DATA");
                break;
            case 39: // DAA (27)
                memoryPtr++;
                String hexa = new String(java.lang.Integer.toHexString(A));
                if (hexa.length() == 1)
                    hexa = "0" + hexa;
                int lower = java.lang.Integer.parseInt(hexa.substring(1, 2), 16);
                int higher = java.lang.Integer.parseInt(hexa.substring(0, 1), 16);

                if (lower > 9 || acFlg == 1)
                    A = A + 6;

                hexa = new String(java.lang.Integer.toHexString(A));
                if (hexa.length() == 1)
                    hexa = "0" + hexa;
                higher = java.lang.Integer.parseInt(hexa.substring(0, 1), 16);

                if (higher > 9 || carryFlg == 1)
                    A = A + 96;

                if (A > 255)
                {
                    A = A - 256;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DAA");
                break;
            case 9: // DAD B (09)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                String b = new String(java.lang.Integer.toHexString(B));
                String c = new String(java.lang.Integer.toHexString(C));
                String bchex = new String(b.concat(c));
                int bc = java.lang.Integer.parseInt(bchex, 16);

                hl = hl + bc;
                if (hl > 65535)
                {
                    hl = hl - 65536;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                hlhex = new String(java.lang.Integer.toHexString(hl));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                H = java.lang.Integer.parseInt(h, 16);
                L = java.lang.Integer.parseInt(l, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DAD B");
                break;
            case 25: // DAD D (19)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                String d = new String(java.lang.Integer.toHexString(D));
                String e = new String(java.lang.Integer.toHexString(E));
                String dehex = new String(d.concat(e));
                int de = java.lang.Integer.parseInt(dehex, 16);

                hl = hl + de;
                if (hl > 65535)
                {
                    hl = hl - 65536;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                hlhex = new String(java.lang.Integer.toHexString(hl));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                H = java.lang.Integer.parseInt(h, 16);
                L = java.lang.Integer.parseInt(l, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DAD D");
                break;
            case 41: // DAD H (29)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                hl = hl + hl;
                if (hl > 65535)
                {
                    hl = hl - 65536;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                hlhex = new String(java.lang.Integer.toHexString(hl));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                H = java.lang.Integer.parseInt(h, 16);
                L = java.lang.Integer.parseInt(l, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DAD H");
                break;
            case 57: // DAD SP (39)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                hl = hl + stackPtr;

                if (hl > 65535)
                {
                    hl = hl - 65536;
                    carryFlg = 1;
                }
                else
                    carryFlg = 0;

                hlhex = new String(java.lang.Integer.toHexString(hl));
                if (hlhex.length() == 1)
                    hlhex = "000" + hlhex;
                if (hlhex.length() == 2)
                    hlhex = "00" + hlhex;
                if (hlhex.length() == 3)
                    hlhex = "0" + hlhex;

                h = new String(hlhex.substring(0, 2));
                l = new String(hlhex.substring(2, 4));

                H = java.lang.Integer.parseInt(h, 16);
                L = java.lang.Integer.parseInt(l, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DAD SP");
                break;
            case 61: // DCR A (3D)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, 1);

                if (A == 0)
                    A = 255;
                else
                    A = A - 1;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR A");
                break;
            case 5: // DCR B (05)
                memoryPtr++;

                acFlg = checkAcFlgTwo(B, 1);

                if (B == 0)
                    B = 255;
                else
                    B = B - 1;

                if (B == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(B);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(B));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR B");
                break;
            case 13: // DCR C (0D)
                memoryPtr++;

                acFlg = checkAcFlgTwo(C, 1);

                if (C == 0)
                    C = 255;
                else
                    C = C - 1;

                if (C == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(C);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(C));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR C");
                break;
            case 21: // DCR D (15)
                memoryPtr++;

                acFlg = checkAcFlgTwo(D, 1);

                if (D == 0)
                    D = 255;
                else
                    D = D - 1;

                if (D == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(D);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(D));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR D");
                break;
            case 29: // DCR E (1D)
                memoryPtr++;

                acFlg = checkAcFlgTwo(E, 1);

                if (E == 0)
                    E = 255;
                else
                    E = E - 1;

                if (E == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(E);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(E));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR E");
                break;
            case 37: // DCR H (25)
                memoryPtr++;

                acFlg = checkAcFlgTwo(H, 1);

                if (H == 0)
                    H = 255;
                else
                    H = H - 1;

                if (H == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(H);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(H));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR H");
                break;
            case 45: // DCR L (2D)
                memoryPtr++;

                acFlg = checkAcFlgTwo(L, 1);

                if (L == 0)
                    L = 255;
                else
                    L = L - 1;

                if (L == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(L);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(L));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR L");
                break;
            case 53: // DCR M (35)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = checkAcFlgTwo(memory[hl], 1);

                if (memory[hl] == 0)
                    memory[hl] = 255;
                else
                    memory[hl] = memory[hl] - 1;

                if (memory[hl] == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(memory[hl]);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(memory[hl]));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR M");
                }
                break;
            case 11: // DCX B (0B)
                memoryPtr++;

                String hexb = new String(java.lang.Integer.toHexString(B));
                String hexc = new String(java.lang.Integer.toHexString(C));
                String hexbc = new String(hexb.concat(hexc));

                bc = java.lang.Integer.parseInt(hexbc, 16);

                if (bc == 0)
                    bc = 65535;
                else
                    --bc;


                hexbc = java.lang.Integer.toHexString(bc);
                hexbc = hexbc.toUpperCase();
                if (hexbc.length() == 3)
                    hexbc = "0".concat(hexbc);
                if (hexbc.length() == 2)
                    hexbc = "00".concat(hexbc);
                if (hexbc.length() == 1)
                    hexbc = "000".concat(hexbc);

                hexb = hexbc.substring(0, 2);
                hexc = hexbc.substring(2, 4);

                B = java.lang.Integer.parseInt(hexb, 16);
                C = java.lang.Integer.parseInt(hexc, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCX B");
                break;
            case 27: // DCX D (1B)
                memoryPtr++;

                String hexd = new String(java.lang.Integer.toHexString(D));
                String hexe = new String(java.lang.Integer.toHexString(E));
                String hexde = new String(hexd.concat(hexe));

                de = java.lang.Integer.parseInt(hexde, 16);

                if (de == 0)
                    de = 65535;
                else
                    --de;
                hexde = java.lang.Integer.toHexString(de);
                hexde = hexde.toUpperCase();
                if (hexde.length() == 3)
                    hexde = "0".concat(hexde);
                if (hexde.length() == 2)
                    hexde = "00".concat(hexde);
                if (hexde.length() == 1)
                    hexde = "000".concat(hexde);

                hexd = hexde.substring(0, 2);
                hexe = hexde.substring(2, 4);

                D = java.lang.Integer.parseInt(hexd, 16);
                E = java.lang.Integer.parseInt(hexe, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCX D");
                break;
            case 43: // DCX H (2B)
                memoryPtr++;

                String hexh = new String(java.lang.Integer.toHexString(H));
                String hexl = new String(java.lang.Integer.toHexString(L));
                String hexhl = new String(hexh.concat(hexl));

                hl = java.lang.Integer.parseInt(hexhl, 16);
                if (hl == 0)
                    hl = 65535;
                else
                    --hl;

                hexhl = java.lang.Integer.toHexString(hl);
                hexhl = hexhl.toUpperCase();
                if (hexhl.length() == 3)
                    hexhl = "0".concat(hexhl);
                if (hexhl.length() == 2)
                    hexhl = "00".concat(hexhl);
                if (hexhl.length() == 1)
                    hexhl = "000".concat(hexhl);

                hexh = hexhl.substring(0, 2);
                hexl = hexhl.substring(2, 4);

                H = java.lang.Integer.parseInt(hexh, 16);
                L = java.lang.Integer.parseInt(hexl, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCX H");
                break;
            case 59: // DCX SP (3B)
                memoryPtr++;
                if (stackPtr == 0)
                    stackPtr = 65535;
                else
                    --stackPtr;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCX SP");
                break;
            case 243: // DI (F3)
                memoryPtr++;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DI");
                break;
            case 251: // EI (FB)
                memoryPtr++;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "EI");
                break;
            case 118: // HLT (76)
                memoryPtr++;
                update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "HLT");
                getToolkit().beep();
                JOptionPane.showMessageDialog(getDemoPanel(), "Program Terminated!", "Warning!", JOptionPane.WARNING_MESSAGE);
                break;
            case 219: // IN PORT (DB)
                memoryPtr++;
                int portAddress = memory[memoryPtr++];
                /* int portData=frame8255.inData(A,portAddress); */

                int portData = frame8279.inData(A, portAddress);

                if (portAddress >= 40 && portAddress <= 41)
                {
                    objDAC.outData(A, portAddress);
                }

                A = portData;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "IN PORT");
                break;
            case 211: // OUT PORT (D3)
                memoryPtr++;
                portAddress = memory[memoryPtr++];


                if (portAddress >= 136 && portAddress <= 141)
                {
                    // frame8251.outData(A,portAddress);
                    // frame8253.setVisible(true);
                }


                if (portAddress >= 192 && portAddress <= 195)
                {
                    frame8253.outData(A, portAddress);
                    // frame8253.setVisible(true);
                }

                if (portAddress >= 30 && portAddress <= 31)
                {
                    // frame8279.setVisible(true);

                    /*
                     * if(!frame8279.isVisible()) { frame8279.setSize(700,500); frame8279.setVisible(true);
                     * frame8279.show();
                     * 
                     * }
                     */
                    frame8279.outData(A, portAddress);

                }

                if (portAddress >= 40 && portAddress <= 41)
                {
                    frameCRO.outData(A);
                }

                if (portAddress >= 80 && portAddress <= 83)
                    frame8255.outData(A, portAddress);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "OUT PORT");
                break;
            case 60: // INR A (3C)
                memoryPtr++;

                acFlg = checkAcFlg(A, 1);

                if (A == 255)
                    A = 0;
                else
                    A = A + 1;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR A");
                break;
            case 4: // INR B (04)
                memoryPtr++;

                acFlg = checkAcFlg(B, 1);

                if (B == 255)
                    B = 0;
                else
                    B = B + 1;

                if (B == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(B);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(B));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR B");
                break;
            case 12: // INR C (0C)
                memoryPtr++;

                acFlg = checkAcFlg(C, 1);

                if (C == 255)
                    C = 0;
                else
                    C = C + 1;

                if (C == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(C);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(C));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR C");
                break;
            case 20: // INR D (14)
                memoryPtr++;

                acFlg = checkAcFlg(D, 1);

                if (D == 255)
                    D = 0;
                else
                    D = D + 1;

                if (D == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(D);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(D));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR D");
                break;
            case 28: // INR E (1C)
                memoryPtr++;

                acFlg = checkAcFlg(E, 1);

                if (E == 255)
                    E = 0;
                else
                    E = E + 1;

                if (E == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(E);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(E));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR E");
                break;
            case 36: // INR H (24)
                memoryPtr++;

                acFlg = checkAcFlg(H, 1);

                if (H == 255)
                    H = 0;
                else
                    H = H + 1;

                if (H == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(H);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(H));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR H");
                break;
            case 44: // INR L (2C)
                memoryPtr++;

                acFlg = checkAcFlg(L, 1);

                if (L == 255)
                    L = 0;
                else
                    L = L + 1;

                if (L == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(L);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(L));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INR L");
                break;
            case 52: // INR M (34)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                acFlg = checkAcFlg(memory[hl], 1);

                if (memory[hl] == 255)
                    memory[hl] = 0;
                else
                    memory[hl] = memory[hl] + 1;

                if (memory[hl] == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(memory[hl]);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(memory[hl]));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);

                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "DCR M");
                }
                break;
            case 3: // INX B (03)
                memoryPtr++;

                String hexbinc = new String(java.lang.Integer.toHexString(B));
                String hexcinc = new String(java.lang.Integer.toHexString(C));
                String hexbcinc = new String(hexbinc.concat(hexcinc));

                bc = java.lang.Integer.parseInt(hexbcinc, 16);

                if (bc == 65535)
                    bc = 0;
                else
                    ++bc;

                hexbcinc = java.lang.Integer.toHexString(bc);
                hexbcinc = hexbcinc.toUpperCase();
                if (hexbcinc.length() == 3)
                    hexbcinc = "0".concat(hexbcinc);
                if (hexbcinc.length() == 2)
                    hexbcinc = "00".concat(hexbcinc);
                if (hexbcinc.length() == 1)
                    hexbcinc = "000".concat(hexbcinc);

                hexbinc = hexbcinc.substring(0, 2);
                hexcinc = hexbcinc.substring(2, 4);

                B = java.lang.Integer.parseInt(hexbinc, 16);
                C = java.lang.Integer.parseInt(hexcinc, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INX B");
                break;
            case 19: // INX D (13)
                memoryPtr++;

                String hexdinc = new String(java.lang.Integer.toHexString(D));
                String hexeinc = new String(java.lang.Integer.toHexString(E));
                String hexdeinc = new String(hexdinc.concat(hexeinc));

                de = java.lang.Integer.parseInt(hexdeinc, 16);

                if (de == 65535)
                    de = 0;
                else
                    ++de;

                hexdeinc = java.lang.Integer.toHexString(de);
                hexdeinc = hexdeinc.toUpperCase();
                if (hexdeinc.length() == 3)
                    hexdeinc = "0".concat(hexdeinc);
                if (hexdeinc.length() == 2)
                    hexdeinc = "00".concat(hexdeinc);
                if (hexdeinc.length() == 1)
                    hexdeinc = "000".concat(hexdeinc);

                hexdinc = hexdeinc.substring(0, 2);
                hexeinc = hexdeinc.substring(2, 4);

                D = java.lang.Integer.parseInt(hexdinc, 16);
                E = java.lang.Integer.parseInt(hexeinc, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INX D");
                break;
            case 35: // INX H (23)
                memoryPtr++;

                String hexhinc = new String(java.lang.Integer.toHexString(H));
                String hexlinc = new String(java.lang.Integer.toHexString(L));
                String hexhlinc = new String(hexhinc.concat(hexlinc));

                hl = java.lang.Integer.parseInt(hexhlinc, 16);

                if (hl == 65535)
                    hl = 0;
                else
                    ++hl;

                hexhlinc = java.lang.Integer.toHexString(hl);
                hexhlinc = hexhlinc.toUpperCase();
                if (hexhlinc.length() == 3)
                    hexhlinc = "0".concat(hexhlinc);
                if (hexhlinc.length() == 2)
                    hexhlinc = "00".concat(hexhlinc);
                if (hexhlinc.length() == 1)
                    hexhlinc = "000".concat(hexhlinc);

                hexhinc = hexhlinc.substring(0, 2);
                hexlinc = hexhlinc.substring(2, 4);

                H = java.lang.Integer.parseInt(hexhinc, 16);
                L = java.lang.Integer.parseInt(hexlinc, 16);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INX H");
                break;
            case 51: // INX SP (33)
                memoryPtr++;
                if (stackPtr == 65535)
                    stackPtr = 0;
                else
                    ++stackPtr;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "INX SP");
                break;
            case 218: // JC PPQQ (DA)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (carryFlg == 1)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JC " + address);
                break;
            case 250: // JM PPQQ (FA)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (signFlg == 1)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JM " + address);
                break;
            case 195: // JMP PPQQ (C3)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JMP " + address);
                break;
            case 210: // JNC PPQQ (D2)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (carryFlg == 0)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JNC " + address);
                break;
            case 194: // JNZ PPQQ (C2)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (zeroFlg == 0)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JNZ " + address);
                break;
            case 242: // JP PPQQ (F2)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (signFlg == 0)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JP " + address);
                break;
            case 234: // JPE PPQQ (EA)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (parityFlg == 1)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JPE " + address);
                break;
            case 226: // JPO PPQQ (E2)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (parityFlg == 0)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JPO " + address);
                break;
            case 202: // JZ PPQQ (CA)
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                if (zeroFlg == 1)
                    memoryPtr = java.lang.Integer.parseInt(address);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "JZ " + address);
                break;
            case 58: // LDA PPQQ
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                int addr = java.lang.Integer.parseInt(address);
                A = memory[addr];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LDA PPQQ");
                break;
            case 50: // STA PPQQ
                memoryPtr++;
                lowerAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                higherAddr = new String(java.lang.Integer.toString(memory[memoryPtr++]));
                address = new String(higherAddr + lowerAddr);
                addr = java.lang.Integer.parseInt(address);
                memory[addr] = A;
                memoryWrite();
                memoryList.setListData(items);
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "STA PPQQ");
                break;
            case 10: // LDAX B (0A)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(B));
                l = new String(java.lang.Integer.toHexString(C));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                A = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LDAX B");
                break;
            case 26: // LDAX D (1A)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(D));
                l = new String(java.lang.Integer.toHexString(E));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                A = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LDAX D");
                break;
            case 42: // LHLD PPQQ
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                l = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                hlhex = new String(l.concat(h));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                L = memory[hl++];
                H = memory[hl];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LHLD " + hlhex + "H");
                break;
            case 34: // SHLD PPQQ
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                l = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                hlhex = new String(l.concat(h));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl++] = L;
                memory[hl] = H;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SHLD " + hlhex + "H");
                }
                break;
            case 1: // LXI B(01)
                memoryPtr++;
                B = memory[memoryPtr++];
                C = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LXI B");
                break;
            case 17: // LXI D(11)
                memoryPtr++;
                D = memory[memoryPtr++];
                E = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LXI D");
                break;
            case 33: // LXI H(21)
                memoryPtr++;
                H = memory[memoryPtr++];
                L = memory[memoryPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LXI H");
                break;
            case 49: // LXI SP(31)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                l = new String(java.lang.Integer.toHexString(memory[memoryPtr++]));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                stackPtr = hl;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "LDAX D");
                break;
            case 0: // NOP (00)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "NOP");
                break;
            case 183: // ORA A (B7)
                memoryPtr++;

                acFlg = 0;
                carryFlg = 0;

                A = A | A;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA A");
                break;
            case 176: // ORA B (B0)
                memoryPtr++;

                A = A | B;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA B");
                break;
            case 177: // ORA C (B1)
                memoryPtr++;

                A = A | C;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA C");
                break;
            case 178: // ORA D (B2)
                memoryPtr++;

                A = A | D;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA D");
                break;
            case 179: // ORA E (B3)
                memoryPtr++;

                A = A | E;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA E");

                break;
            case 180: // ORA H (B4)
                memoryPtr++;

                A = A | H;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA H");
                break;
            case 181: // ORA L (B5)
                memoryPtr++;

                A = A | L;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA L");
                break;
            case 182: // ORA M (B6)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                tempReg = memory[hl];

                A = A | tempReg;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORA M");
                break;
            case 246: // ORI DATA (F6)
                memoryPtr++;

                A = A | memory[memoryPtr++];

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    char first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "ORI DATA");
                break;
            case 233: // PCHL (E9)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memoryPtr = hl;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "PCHL");
                break;
            case 193: // POP B (C1)
                memoryPtr++;
                C = memory[stackPtr++];
                B = memory[stackPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "POP B");
                break;
            case 209: // POP D (D1)
                memoryPtr++;
                E = memory[stackPtr++];
                D = memory[stackPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "POP D");
                break;
            case 225: // POP H (E1)
                memoryPtr++;
                L = memory[stackPtr++];
                H = memory[stackPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "POP H");
                break;
            case 241: // POP PSW (F1)
                memoryPtr++;
                A = memory[stackPtr++];
                PSW = memory[stackPtr++];
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "POP PSW");
                break;
            case 197: // PUSH B (C5)
                memoryPtr++;
                memory[--stackPtr] = B;
                memory[--stackPtr] = C;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "PUSH B");
                }
                break;
            case 213: // PUSH D (D5)
                memoryPtr++;
                memory[--stackPtr] = D;
                memory[--stackPtr] = E;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "PUSH D");
                }
                break;
            case 229: // PUSH H (E5)
                memoryPtr++;
                memory[--stackPtr] = H;
                memory[--stackPtr] = L;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "PUSH H");
                }
                break;
            case 245: // PUSH PSW(F5)
                memoryPtr++;
                String binaryPsw = new String(signFlg + "" + zeroFlg + "0" + acFlg + "0" + parityFlg + "0" + carryFlg);
                PSW = java.lang.Integer.parseInt(binaryPsw, 2);
                memory[--stackPtr] = A;
                memory[--stackPtr] = PSW;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "PUSH PSW");
                }
                break;
            case 32: // RIM (20)
                memoryPtr++;

                break;
            case 23: // RAL (17)
                memoryPtr++;
                binary = new String(toBinary(A));

                tempReg = carryFlg;
                char first = binary.charAt(0);
                if (first == '1')
                    carryFlg = 1;
                else
                    carryFlg = 0;

                binary = new String(binary.substring(1, 8));
                binary = new String(binary.concat(java.lang.Integer.toString(tempReg)));
                A = java.lang.Integer.parseInt(binary, 2);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RAL");
                break;
            case 31: // RAR (1F)
                memoryPtr++;
                binary = new String(toBinary(A));

                tempReg = carryFlg;
                first = binary.charAt(7);
                if (first == '1')
                    carryFlg = 1;
                else
                    carryFlg = 0;

                binary = new String(binary.substring(0, 7));
                binary = new String(java.lang.Integer.toString(tempReg).concat(binary));

                A = java.lang.Integer.parseInt(binary, 2);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RAR");
                break;
            case 7: // RLC (07)
                memoryPtr++;
                binary = new String(toBinary(A));

                first = binary.charAt(0);
                if (first == '1')
                    carryFlg = 1;
                else
                    carryFlg = 0;

                binary = new String(binary.substring(1, 8).concat(binary.substring(0, 1)));
                A = java.lang.Integer.parseInt(binary, 2);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RLC");
                break;
            case 15: // RRC (0F)
                memoryPtr++;
                binary = new String(toBinary(A));

                first = binary.charAt(7);
                if (first == '1')
                    carryFlg = 1;
                else
                    carryFlg = 0;

                binary = new String(binary.substring(7, 8).concat(binary.substring(0, 7)));
                A = java.lang.Integer.parseInt(binary, 2);

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RRC");
                break;
            case 201: // RET (C9)
                memoryPtr++;

                System.out.println(stackPtr);

                l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                memoryPtr = hl;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RET");
                break;
            case 216: // RC (D8)
                memoryPtr++;

                if (carryFlg == 1)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RC");
                break;
            case 248: // RM (F8)
                memoryPtr++;

                if (signFlg == 1)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RM");
                break;
            case 208: // RNC (D0)
                memoryPtr++;

                if (carryFlg == 0)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RNC");
                break;
            case 192: // RNZ (C0)
                memoryPtr++;

                if (zeroFlg == 0)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RNZ");
                break;
            case 240: // RP (F0)
                memoryPtr++;

                if (signFlg == 0)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RP");
                break;
            case 232: // RPE (E8)
                memoryPtr++;

                if (parityFlg == 1)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RPE");
                break;
            case 224: // RPO (E0)
                memoryPtr++;

                if (parityFlg == 0)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RPO");
                break;
            case 200: // RZ (C8)
                memoryPtr++;

                if (zeroFlg == 1)
                {
                    l = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    h = new String(java.lang.Integer.toHexString(memory[stackPtr++]));
                    hlhex = new String(h.concat(l));
                    hl = java.lang.Integer.parseInt(hlhex, 16);

                    memoryPtr = hl;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RZ");
                break;
            case 199: // RST0 (C7)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST0");
                break;
            case 207: // RST1 (CF)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST1");
                break;
            case 215: // RST2 (D7)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST2");
                break;
            case 223: // RST3 (DF)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST3");
                break;
            case 231: // RST4 (E7)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST4");
                break;
            case 239: // RST5 (EF)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST5");
                break;
            case 247: // RST6 (F7)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST6");
                break;
            case 255: // RST7 (FF)
                memoryPtr++;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "RST7");
                break;
            case 159: // SBB A (9F)
                memoryPtr++;

                tempReg = A + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB A");
                break;
            case 152: // SBB B (98)
                memoryPtr++;
                tempReg = B + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB B");
                break;
            case 153: // SBB C (99)
                memoryPtr++;
                tempReg = C + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB C");
                break;
            case 154: // SBB D (9A)
                memoryPtr++;
                tempReg = D + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB D");
                break;
            case 155: // SBB E (9B)
                memoryPtr++;
                tempReg = E + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB E");
                break;
            case 156: // SBB H (9C)
                memoryPtr++;
                tempReg = H + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB H");
                break;
            case 157: // SBB L (9D)
                memoryPtr++;
                tempReg = L + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB L");
                break;
            case 158: // SBB M (9E)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                tempReg = memory[hl];

                tempReg = tempReg + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBB M");
                break;
            case 222: // SBI DATA (DE)
                memoryPtr++;
                tempReg = memory[memoryPtr++];

                tempReg = tempReg + carryFlg;

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SBI DATA");
                break;
            case 48: // SIM (30)
                memoryPtr++;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SIM");
                break;
            case 249: // SPHL (F9)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);

                stackPtr = hl;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SPHL");
                break;
            case 227: // XTHL (E3)
                memoryPtr++;

                tempReg = memory[stackPtr];
                memory[stackPtr] = L;
                L = tempReg;

                int tempPoint = stackPtr + 1;
                tempReg = memory[tempPoint];
                memory[tempPoint] = H;
                H = tempReg;

                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);

                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XTHL");
                }
                break;
            case 2: // STAX B (02)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(B));
                l = new String(java.lang.Integer.toHexString(C));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = A;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "STAX B");
                }
                break;
            case 18: // STAX D (12)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(D));
                l = new String(java.lang.Integer.toHexString(E));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                memory[hl] = A;
                if (flag == true)
                {
                    memoryWrite();
                    memoryList.setListData(items);
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "STAX D");
                }
                break;
            case 55: // STC (37)
                memoryPtr++;
                carryFlg = 1;
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "STC");
                break;
            case 151: // SUB A (97)
                memoryPtr++;

                A = 0;
                zeroFlg = 1;
                carryFlg = 0;
                acFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB A");
                break;
            case 144: // SUB B (90)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, B);

                if (A > B)
                {
                    A = A - B;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == B)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < B)
                {
                    A = B - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB B");
                break;
            case 145: // SUB C (91)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, C);

                if (A > C)
                {
                    A = A - C;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == C)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < C)
                {
                    A = C - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB C");
                break;
            case 146: // SUB D (92)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, D);

                if (A > D)
                {
                    A = A - D;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == D)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < D)
                {
                    A = D - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB D");
                break;
            case 147: // SUB E (93)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, E);

                if (A > E)
                {
                    A = A - E;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == E)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < E)
                {
                    A = E - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB E");
                break;
            case 148: // SUB H (94)
                memoryPtr++;

                acFlg = checkAcFlgTwo(A, H);

                if (A > H)
                {
                    A = A - H;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == H)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < H)
                {
                    A = H - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB H");
                break;
            case 149: // SUB L (95)
                memoryPtr++;
                tempReg = L + carryFlg;

                acFlg = checkAcFlgTwo(A, L);

                if (A > L)
                {
                    A = A - L;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == L)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < L)
                {
                    A = L - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB L");
                break;
            case 150: // SUB M (96)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                tempReg = memory[hl];

                acFlg = checkAcFlgTwo(A, tempReg);

                tempReg = tempReg;

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUB M");
                break;
            case 214: // SUI DATA (D6)
                memoryPtr++;
                tempReg = memory[memoryPtr++];

                acFlg = checkAcFlgTwo(A, tempReg);

                if (A > tempReg)
                {
                    A = A - tempReg;
                    zeroFlg = 0;
                    carryFlg = 0;
                }
                else if (A == tempReg)
                {
                    A = 0;
                    carryFlg = 0;
                    zeroFlg = 1;
                }
                else if (A < tempReg)
                {
                    A = tempReg - A;
                    zeroFlg = 0;
                    carryFlg = 1;
                    A = 256 - A;
                }

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "SUI DATA");
                break;

            case 235: // XCHG (EB)
                memoryPtr++;
                tempReg = D;
                D = H;
                H = tempReg;
                tempReg = E;
                E = L;
                L = tempReg;

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XCHG");
                break;
            case 175: // XRA A (AF)
                memoryPtr++;

                A = A ^ A;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA A");
                break;
            case 168: // XRA B (A8)
                memoryPtr++;

                A = A ^ B;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA B");
                break;
            case 169: // XRA C (A9)
                memoryPtr++;

                A = A ^ C;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA C");
                break;
            case 170: // XRA D (AA)
                memoryPtr++;

                A = A ^ D;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA D");
                break;
            case 171: // XRA E (AB)
                memoryPtr++;

                A = A ^ E;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA E");
                break;
            case 172: // XRA H (AC)
                memoryPtr++;

                A = A ^ H;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA H");
                break;
            case 173: // XRA L (AD)
                memoryPtr++;

                A = A ^ L;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA L");
                break;
            case 174: // XRA M (AE)
                memoryPtr++;
                h = new String(java.lang.Integer.toHexString(H));
                l = new String(java.lang.Integer.toHexString(L));
                hlhex = new String(h.concat(l));
                hl = java.lang.Integer.parseInt(hlhex, 16);
                tempReg = memory[hl];

                A = A ^ tempReg;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }

                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRA M");
                break;
            case 238: // XRI DATA (EE)
                memoryPtr++;
                tempReg = memory[memoryPtr++];

                A = A ^ tempReg;

                acFlg = 0;
                carryFlg = 0;

                if (A == 0)
                    zeroFlg = 1;
                else
                    zeroFlg = 0;

                parityFlg = sumdig(A);
                if (parityFlg % 2 == 0)
                    parityFlg = 1;
                else
                    parityFlg = 0;

                binary = new String(java.lang.Integer.toBinaryString(A));
                if (binary.length() != 8)
                    signFlg = 0;
                else
                {
                    first = binary.charAt(0);
                    if (first == '1')
                        signFlg = 1;
                    else
                        signFlg = 0;
                }
                if (flag == true)
                    update(memoryPtr, A, B, C, D, E, H, L, signFlg, carryFlg, parityFlg, acFlg, zeroFlg, stackPtr, "XRI DATA");
                break;




        }
    }


    void update(int pc, int a, int b, int c, int d, int e, int h, int l, int sf, int cf, int pf, int acf, int zf, int sp, String instr)
    {
        memoryList.setSelectedIndex(pc);

        String hex = new String(java.lang.Integer.toHexString(pc));
        if (hex.length() == 1)
            hex = new String("000".concat(hex));
        if (hex.length() == 2)
            hex = new String("00".concat(hex));
        if (hex.length() == 3)
            hex = new String("0".concat(hex));

        hex = new String(hex.concat("H"));
        hex = new String(hex.toUpperCase());

        pcField.setText("PC-" + hex);
        instructionField.setText(instr);

        hex = new String(java.lang.Integer.toHexString(a));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        aField.setText("A-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(b));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        bField.setText("B-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(c));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        cField.setText("C-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(d));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        dField.setText("D-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(e));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        eField.setText("E-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(h));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        hField.setText("H-" + hex + "H");

        hex = new String(java.lang.Integer.toHexString(l));
        if (hex.length() == 1)
            hex = "0".concat(hex);
        hex = hex.toUpperCase();
        lField.setText("L-" + hex + "H");

        String carry = new String(java.lang.Integer.toString(cf));
        String zero = new String(java.lang.Integer.toString(zf));
        String sign = new String(java.lang.Integer.toString(sf));
        String parity = new String(java.lang.Integer.toString(pf));
        String auxillary = new String(java.lang.Integer.toString(acf));

        carryField.setText("C-" + carry);
        zeroField.setText("Z-" + zero);
        signField.setText("S-" + sign);
        parityField.setText("P-" + parity);
        acField.setText("AC-" + auxillary);

        hex = new String(java.lang.Integer.toHexString(sp));
        if (hex.length() == 1)
            hex = "000".concat(hex);
        if (hex.length() == 2)
            hex = "00".concat(hex);
        if (hex.length() == 3)
            hex = "0".concat(hex);

        hex = hex.toUpperCase();
        spField.setText("SP-" + hex + "H");


    }

    public int checkAcFlg(int one, int two)
    {
        String achexOne = new String(java.lang.Integer.toHexString(one));
        if (achexOne.length() == 1)
            achexOne = new String("0".concat(achexOne));
        String achexTwo = new String(java.lang.Integer.toHexString(two));
        if (achexTwo.length() == 1)
            achexTwo = new String("0".concat(achexTwo));

        int firstOperand = java.lang.Integer.parseInt(achexOne.substring(1, 2), 16);
        int secondOperand = java.lang.Integer.parseInt(achexTwo.substring(1, 2), 16);

        int result = firstOperand + secondOperand;
        if (result > 15)
            return 1;
        else
            return 0;
    }

    public int checkAcFlgTwo(int one, int two)
    {
        String achexOne = new String(java.lang.Integer.toHexString(one));
        if (achexOne.length() == 1)
            achexOne = new String("0".concat(achexOne));
        String achexTwo = new String(java.lang.Integer.toHexString(two));
        if (achexTwo.length() == 1)
            achexTwo = new String("0".concat(achexTwo));

        int firstOperand = java.lang.Integer.parseInt(achexOne.substring(1, 2), 16);
        int secondOperand = java.lang.Integer.parseInt(achexTwo.substring(1, 2), 16);

        int result;
        if (firstOperand > secondOperand)
            return 0;
        if (firstOperand == secondOperand)
            return 0;
        else
            return 1;
    }


    // calculate sum of number of ones
    public int sumdig(int no)
    {
        int sum = 0;
        int quotient;
        while (no != 0)
        {
            int rem = no % 2;
            quotient = no / 2;
            no = quotient;
            sum = sum + rem;
        }
        return sum;
    }


    public String searchAddress(String symbol)
    {

        for (int i = 0; i <= symbolTablePtr; i++)
        {
            if (symbol.equals(symbolTable[i][0]))
            {
                System.out.println("Search=" + symbol + " Data=" + symbolTable[i][0]);
                return (symbolTable[i][1]);
            }
        }
        return ("");
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





    public void memoryWrite()
    {
        for (int i = 0; i < 65536; i++)
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



class mcEditDesignPanel extends JPanel
{


    mcEditDesignPanel()
    {

        repaint();
    }


    public void paint(Graphics g)
    {
        g.setColor(Color.white);// new Color(211,211,169));
        g.fillRoundRect(5, 5, 780, 485, 5, 5);

        g.setColor(new Color(172, 172, 89));
        // g.drawLine(68,0,68,500);

        int x = 75;
        int y = 50;
        int incr = 19;
        for (int i = 0; i < 40; i++)
        {
            g.fillRect(x, y, 25, 14);
            y = y + incr;
            if (i == 19)
            {
                x = 330;
                y = 50;
            }
        }

        g.setColor(Color.blue);
        Font font = new Font("Times New Roman", Font.BOLD, 10);
        g.setFont(font);

        g.drawString("X1", 57, 60);
        g.drawString("X2", 57, 79);
        g.drawString("RESET OUT", 10, 98);
        g.drawString("SOD", 48, 117);
        g.drawString("SID", 53, 136);
        g.drawString("TRAP", 43, 155);
        g.drawString("RST 7.5", 32, 174);
        g.drawString("RST 6.5", 32, 193);
        g.drawString("RST 5.5", 32, 212);
        g.drawString("INTR", 43, 231);
        g.drawLine(40, 240, 67, 240);
        g.drawString("INTR", 43, 250);
        g.drawString("AD0", 50, 269);
        g.drawString("AD1", 50, 288);
        g.drawString("AD2", 50, 307);
        g.drawString("AD3", 50, 326);
        g.drawString("AD4", 50, 345);
        g.drawString("AD5", 50, 364);
        g.drawString("AD6", 50, 383);
        g.drawString("AD7", 50, 402);
        g.drawString("Vss", 55, 421);

        g.drawString("Vcc", 365, 60);
        g.drawString("HOLD", 365, 79);
        g.drawString("HLDA", 365, 98);
        g.drawString("CLK(OUT)", 365, 117);
        g.drawLine(365, 126, 410, 126);
        g.drawString("RESET IN", 365, 136);
        g.drawString("READY", 365, 155);
        g.drawLine(381, 164, 389, 164);
        g.drawString("IO/M", 365, 174);
        g.drawString("S1", 365, 193);
        g.drawLine(365, 202, 380, 202);
        g.drawString("RD", 365, 212);
        g.drawLine(365, 221, 380, 221);
        g.drawString("WR", 365, 231);
        // g.drawLine(40,240,67,240);
        g.drawString("ALE", 365, 250);
        g.drawString("S0", 365, 269);
        g.drawString("A15", 365, 288);
        g.drawString("A14", 365, 307);
        g.drawString("A13", 365, 326);
        g.drawString("A12", 365, 345);
        g.drawString("A11", 365, 364);
        g.drawString("A10", 365, 383);
        g.drawString("A9", 365, 402);
        g.drawString("A8", 365, 421);

        g.setColor(new Color(100, 110, 120));
        g.fillRect(490, 50, 220, 380);

        g.setColor(new Color(172, 172, 89).darker());


        // A8-A15
        g.drawLine(710, 70, 750, 70);
        g.drawLine(710, 90, 750, 90);

        g.drawLine(750, 70, 750, 60);
        g.drawLine(750, 90, 750, 100);
        g.drawLine(750, 60, 765, 80);
        g.drawLine(750, 100, 765, 80);

        // AD0-AD7
        g.drawLine(710, 170, 750, 170);
        g.drawLine(710, 190, 750, 190);

        g.drawLine(750, 170, 750, 160);
        g.drawLine(750, 190, 750, 200);
        g.drawLine(750, 160, 765, 180);
        g.drawLine(750, 200, 765, 180);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        x = 710;
        y = 250;
        for (int i = 0; i < 6; i++)
        {
            g.drawLine(x, y, x + 40, y);
            g.drawString(">", x + 35, y + 5);
            y = y + 30;
        }

        // SID,SOD
        g.drawLine(430, 70, 490, 70);
        g.drawString(">", 485, 75);
        g.drawLine(430, 95, 490, 95);
        g.drawString("<", 430, 100);


        // INTR
        x = 430;
        y = 140;
        for (int i = 0; i < 5; i++)
        {
            g.drawLine(x, y, x + 60, y);
            g.drawString(">", x + 55, y + 5);
            y = y + 25;
        }

        // READY
        g.drawLine(430, 285, 490, 285);
        g.drawString(">", 485, 290);

        // HOLD
        g.drawLine(430, 315, 490, 315);
        g.drawString(">", 485, 320);

        // RESET IN
        g.drawLine(430, 345, 490, 345);
        g.drawString(">", 485, 350);

        // INTA
        g.drawLine(430, 390, 490, 390);
        g.drawString("<", 430, 395);

        // HLDA
        g.drawLine(430, 420, 490, 420);
        g.drawString("<", 430, 425);

        // RESET OUT
        g.drawLine(530, 430, 530, 470);
        g.drawString("v", 527, 470);

        // CLK OUT
        g.drawLine(670, 430, 670, 470);
        g.drawString("v", 667, 470);

        // GND
        g.drawLine(670, 50, 670, 25);
        g.drawLine(660, 25, 680, 25);
        g.drawLine(663, 22, 677, 22);
        g.drawLine(666, 19, 674, 19);

        // Vcc
        g.drawLine(620, 50, 620, 25);


        // X1,X2
        g.drawLine(550, 50, 550, 25);
        g.drawLine(520, 50, 520, 25);
        g.drawLine(550, 25, 540, 25);
        g.drawLine(520, 25, 530, 25);
        g.drawLine(540, 20, 540, 30);
        g.drawLine(530, 20, 530, 30);

        g.setFont(new Font("Times New Roman", Font.BOLD, 10));
        g.setColor(Color.darkGray);

        g.drawString("A15-A8", 720, 85);
        g.drawString("AD0-AD7", 720, 185);
        g.drawString("ALE", 720, 245);
        g.drawString("S0", 720, 275);
        g.drawString("S1", 720, 305);
        g.drawLine(735, 325, 745, 325);
        g.drawString("IO/M", 720, 335);
        g.drawLine(720, 355, 735, 355);
        g.drawString("RD", 720, 365);
        g.drawLine(720, 385, 735, 385);
        g.drawString("WR", 720, 395);

        g.drawString("SID", 440, 65);
        g.drawString("SOD", 440, 90);
        g.drawString("TRAP", 440, 135);
        g.drawString("RST 7.5", 440, 160);
        g.drawString("RST 6.5", 440, 185);
        g.drawString("RST 5.5", 440, 210);
        g.drawString("INTR", 440, 235);

        g.drawString("READY", 440, 280);
        g.drawString("HOLD", 440, 310);
        g.drawString("RESET IN", 440, 340);

        g.drawString("INTA", 440, 385);
        g.drawString("HLDA", 440, 415);

        g.drawString("RESET OUT", 535, 450);
        g.drawString("CLK OUT", 675, 450);

        g.drawString("GND", 665, 15);
        g.drawString("+5 V", 615, 20);
        g.drawString("X1", 505, 40);
        g.drawString("X2", 560, 40);

        g.setFont(new Font("Times New Roman", Font.BOLD, 14));
        g.drawString("Program:", 20, 30);


    }
}


class mcDebugDesignPanel extends JPanel
{


    mcDebugDesignPanel()
    {
        repaint();
    }


    public void paint(Graphics g)
    {
        g.setColor(Color.white);// new Color(211,211,169));
        g.fillRoundRect(5, 25, 780, 505, 5, 5);

        // 8085 Microprocessor
        g.setColor(new Color(100, 110, 120));
        g.fillRect(20, 94, 580, 375);

        // ALU
        g.setColor(new Color(192, 192, 192));// Color.yellow);
        g.fillRect(30, 160, 200, 300);
        g.setColor(Color.black);// new Color(128,128,255));
        g.fillRect(50, 250, 160, 130);
        g.setColor(Color.white);
        g.drawRect(50, 250, 160, 130);
        g.drawRect(30, 160, 200, 300);

        // Instruction decoder
        g.setColor(new Color(192, 192, 192));
        g.fillRect(250, 160, 120, 80);
        g.setColor(Color.white);
        g.drawRect(250, 160, 120, 80);

        // Control Unit
        g.setColor(new Color(192, 192, 192));
        g.fillRect(250, 282, 120, 60);
        g.setColor(Color.white);
        g.drawRect(250, 282, 120, 60);

        // Registers
        g.setColor(new Color(192, 192, 192));
        g.fillRect(400, 200, 174, 200);
        g.setColor(Color.white);
        g.drawRect(400, 200, 174, 200);


        g.setColor(Color.blue);//
        Font font = new Font("Times New Roman", Font.PLAIN, 12);
        g.setFont(font);

        g.drawString("Accumulator:", 40, 175);
        g.drawString("Temp.Register:", 140, 175);

        g.setColor(new Color(128, 128, 255));
        g.setFont(new Font("Times New Roman", Font.BOLD, 16));
        g.drawString("Arithmatic/Logic", 80, 300);
        g.drawString("Unit", 110, 325);
        g.drawString("Memory", 700, 390);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        g.setColor(Color.blue);
        g.drawString("Instruction", 275, 190);
        g.drawString("Decoder", 280, 210);

        g.drawString("Control Unit", 270, 310);

        // data bus
        // g.setColor(Color.gray);
        g.setColor(new Color(172, 172, 89));

        g.drawLine(640, 133, 640, 60);
        g.drawLine(664, 133, 664, 36);
        g.drawLine(640, 133, 630, 133);
        g.drawLine(664, 133, 674, 133);
        g.drawLine(630, 133, 652, 145);
        g.drawLine(674, 133, 652, 145);

        g.drawLine(664, 36, 304, 36);
        g.drawLine(640, 60, 328, 60);

        g.drawLine(304, 36, 304, 80);
        g.drawLine(328, 60, 328, 80);
        g.drawLine(304, 80, 294, 80);
        g.drawLine(328, 80, 338, 80);
        g.drawLine(294, 80, 316, 92);
        g.drawLine(338, 80, 316, 92);

        // Internal data bus

        // g.setColor(new Color(128,255,255));
        g.setColor(new Color(172, 172, 89));

        g.drawLine(20, 110, 304, 110);
        g.drawLine(20, 134, 304, 134);
        g.drawLine(304, 110, 304, 94);
        g.drawLine(304, 134, 304, 145);
        g.drawLine(328, 134, 328, 145);
        g.drawLine(328, 134, 600, 134);
        g.drawLine(328, 110, 328, 94);
        g.drawLine(328, 110, 600, 110);
        g.drawLine(304, 145, 294, 145);
        g.drawLine(328, 145, 338, 145);
        g.drawLine(294, 145, 316, 157);
        g.drawLine(338, 145, 316, 157);

        g.drawLine(470, 146, 470, 188);
        g.drawLine(494, 146, 494, 188);
        g.drawLine(470, 188, 460, 188);
        g.drawLine(494, 188, 504, 188);
        g.drawLine(460, 188, 482, 200);
        g.drawLine(504, 188, 482, 200);
        g.drawLine(460, 146, 470, 146);
        g.drawLine(494, 146, 504, 146);
        g.drawLine(460, 146, 482, 134);
        g.drawLine(504, 146, 482, 134);

        // address bus
        g.drawLine(574, 376, 600, 376);
        g.drawLine(574, 400, 600, 400);

        // g.setColor(Color.gray);
        g.drawLine(600, 376, 640, 376);
        g.drawLine(600, 400, 664, 400);
        g.drawLine(640, 376, 640, 374);
        g.drawLine(664, 400, 664, 374);
        g.drawLine(640, 374, 630, 374);
        g.drawLine(664, 374, 674, 374);
        g.drawLine(630, 374, 652, 362);
        g.drawLine(674, 374, 652, 362);

        // g.setColor(new Color(128,255,255));
        g.drawLine(304, 240, 304, 268);
        g.drawLine(326, 240, 326, 268);
        g.drawLine(304, 268, 294, 268);
        g.drawLine(326, 268, 336, 268);
        g.drawLine(294, 268, 316, 280);
        g.drawLine(336, 268, 316, 280);

        g.drawLine(304, 342, 304, 445);
        g.drawLine(326, 342, 326, 421);
        g.drawLine(304, 445, 600, 445);
        g.drawLine(326, 421, 600, 421);

        g.drawLine(270, 342, 270, 370);
        g.drawLine(270, 370, 230, 370);
        g.drawString("<", 230, 375);

        g.drawLine(370, 310, 400, 310);
        g.drawString(">", 395, 315);
        g.drawLine(385, 310, 385, 200);
        g.drawLine(385, 200, 370, 200);
        g.drawString("<", 370, 205);

        g.setColor(new Color(80, 80, 80));
        g.fillRect(35, 415, 185, 36);

        g.drawLine(120, 405, 120, 390);
        g.drawLine(135, 405, 135, 390);
        g.drawLine(120, 405, 112, 405);
        g.drawLine(135, 405, 142, 405);
        g.drawLine(112, 405, 127, 415);
        g.drawLine(142, 405, 127, 415);
        g.drawLine(120, 390, 112, 390);
        g.drawLine(135, 390, 142, 390);
        g.drawLine(112, 390, 127, 380);
        g.drawLine(142, 390, 127, 380);

        g.drawLine(70, 240, 70, 210);
        g.drawLine(85, 240, 85, 210);

        g.drawLine(70, 240, 62, 240);
        g.drawLine(85, 240, 92, 240);
        g.drawLine(62, 240, 77, 250);
        g.drawLine(92, 240, 77, 250);

        g.drawLine(170, 240, 170, 210);
        g.drawLine(185, 240, 185, 210);

        g.drawLine(170, 240, 162, 240);
        g.drawLine(185, 240, 192, 240);
        g.drawLine(162, 240, 177, 250);
        g.drawLine(192, 240, 177, 250);

        g.setColor(new Color(172, 172, 89));
        g.drawLine(120, 152, 120, 143);
        g.drawLine(135, 152, 135, 143);

        g.drawLine(120, 152, 114, 152);
        g.drawLine(135, 152, 141, 152);
        g.drawLine(114, 152, 127, 160);
        g.drawLine(141, 152, 127, 160);

        g.drawLine(120, 143, 114, 143);
        g.drawLine(135, 143, 141, 143);
        g.drawLine(114, 143, 127, 135);
        g.drawLine(141, 143, 127, 135);

        g.setColor(new Color(70, 0, 0));
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.drawString("8085 Microprocessor", 90, 85);

        g.setColor(new Color(128, 255, 255));
        g.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        g.drawString("Internal Data Bus", 260, 130);
        g.drawString("Control Signals", 350, 440);





    }
}
