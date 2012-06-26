import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;


public class C8087Panel extends DemoModule
{
    // The Frame
    CoKit cokit;
    JButton go;
    JButton trace;
    JTextField instructionField;
    JTextField st0, st1, st2, st3, st4, st5, st6, st7;
    String s, temp, instruction;
    FileReader fr;
    BufferedReader br;
    int dataSegment[] = new int[65535];
    int codeSegment[] = new int[65535];
    int stackSegment[] = new int[65535];
    int extraSsegment[] = new int[65535];
    String dataSegStr[][] = new String[65535][2];
    int posOfDataSegStr;
    Stack stack = new Stack();
    Stack tempStack = new Stack();

    public static void main(String[] args)
    {
        C8087Panel demo = new C8087Panel(null);
        demo.mainImpl();
    }

    public C8087Panel(CoKit cokit)
    {
        super(cokit, "C8087Panel", "toolbar/C8087Panel.gif");


        JPanel demo = getDemoPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));
        demo.setBackground(Color.white);

        posOfDataSegStr = 0;

        try
        {
            fr = new FileReader("vol.asm");
            br = new BufferedReader(fr);
        }
        catch (IOException e)
        {
        }

        // setBorder(new CompoundBorder(cokit.loweredBorder, cokit.emptyBorder10));
        JPanel textFields = createVerticalPanel(false);
        setBackground(Color.white);
        // setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        go = new JButton("Go");
        trace = new JButton("Trace");
        go.setBackground(new Color(204, 204, 255));
        trace.setBackground(new Color(204, 204, 255));

        go.setMnemonic('G');
        trace.setMnemonic('t');

        JToolBar toolBar = new JToolBar();
        addTool(toolBar, "open");
        toolBar.setBackground(Color.white);

        JPanel p = createVerticalPanel(true);
        // p.setBorder(cokit.emptyBorder10);
        p.setBackground(Color.white);

        /*
         * JMenuBar menuBar = new JMenuBar(); menuBar.setAlignmentX(LEFT_ALIGNMENT);
         * menuBar.setAlignmentY(TOP_ALIGNMENT); p.add(menuBar);
         * 
         * // File JMenu file = (JMenu) menuBar.add(new JMenu("Open File")); file.setBackground(Color.white);
         * file.setMnemonic('i'); JMenuItem newItem = file.add(new JMenuItem("New",new
         * ImageIcon("images/new.gif","New"))); newItem.setHorizontalTextPosition(JButton.RIGHT);
         * newItem.setMnemonic('N'); newItem.setEnabled(false);
         * 
         * JMenuItem open = (JMenuItem) file.add(new JMenuItem("Open",new ImageIcon("images/open.gif","Open")));
         * open.setHorizontalTextPosition(JButton.RIGHT); open.setMnemonic('O');
         * 
         * 
         * JMenuItem save = (JMenuItem) file.add(new JMenuItem("Save",new ImageIcon("images/save.gif","Save")));
         * save.setHorizontalTextPosition(JButton.RIGHT); save.setMnemonic('S'); save.setEnabled(false);
         * 
         * ActionListener startFileChooser = new ActionListener() { public void actionPerformed(ActionEvent e) {
         * JFileChooser chooser = new JFileChooser(); ExampleFileFilter filter = new ExampleFileFilter( new String[]
         * {"asm"}, "asm files"); ExampleFileView fileView = new ExampleFileView(); chooser.setFileView(fileView);
         * chooser.addChoosableFileFilter(filter); chooser.setFileFilter(filter); File swingFile = new
         * File("NDP_prog/fact.asm"); if(swingFile.exists()) { chooser.setCurrentDirectory(swingFile);
         * chooser.setSelectedFile(swingFile); }
         * 
         * int retval = chooser.showOpenDialog(cokit); if(retval == 0) { File theFile = chooser.getSelectedFile();
         * if(theFile != null) { JOptionPane.showMessageDialog(cokit, "You chose this file: " +
         * chooser.getSelectedFile().getAbsolutePath()); return; } } JOptionPane.showMessageDialog(cokit,
         * "No file chosen"); } }; // open.addActionListener(startFileChooser);
         */

        st0 = new FixedTextField("Empty  st(0)-", 25);
        st0.setForeground(Color.green.brighter());
        st0.setBackground(Color.black);

        st1 = new FixedTextField("Empty  st(1)-", 25);
        st1.setForeground(Color.green.brighter());
        st1.setBackground(Color.black);

        st2 = new FixedTextField("Empty  st(2)-", 25);
        st2.setForeground(Color.green.brighter());
        st2.setBackground(Color.black);

        st3 = new FixedTextField("Empty  st(3)-", 25);
        st3.setForeground(Color.green.brighter());
        st3.setBackground(Color.black);

        st4 = new FixedTextField("Empty  st(4)-", 25);
        st4.setForeground(Color.green.brighter());
        st4.setBackground(Color.black);

        st5 = new FixedTextField("Empty  st(5)-", 25);
        st5.setForeground(Color.green.brighter());
        st5.setBackground(Color.black);

        st6 = new FixedTextField("Empty  st(6)-", 25);
        st6.setForeground(Color.green.brighter());
        st6.setBackground(Color.black);

        st7 = new FixedTextField("Empty  st(7)-", 25);
        st7.setForeground(Color.green.brighter());
        st7.setBackground(Color.black);

        instructionField = new FixedTextField("", 25);
        instructionField.setForeground(Color.green.brighter());
        instructionField.setBackground(Color.black);


        JLabel label = (JLabel)textFields.add(new JLabel("8-register stack:"));
        textFields.add(p);
        textFields.add(Box.createRigidArea(VGAP10));
        textFields.add(label);
        textFields.add(Box.createRigidArea(VGAP10));
        textFields.add(st0);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st1);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st2);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st3);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st4);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st5);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st6);
        textFields.add(Box.createRigidArea(VGAP5));
        textFields.add(st7);
        textFields.add(Box.createRigidArea(VGAP10));
        label = (JLabel)textFields.add(new JLabel("Instruction:"));
        textFields.add(label);
        textFields.add(Box.createRigidArea(VGAP10));
        textFields.add(instructionField);

        textFields.setBackground(Color.white);

        textFields.add(Box.createRigidArea(VGAP10));
        textFields.add(go);

        textFields.add(Box.createRigidArea(VGAP10));
        textFields.add(trace);

        String text = LoadFile("Inputs/vol.asm");

        JPanel textAreaPanel = createVerticalPanel(false);
        label = (JLabel)textAreaPanel.add(new JLabel("Program:"));
        // label.setFont(cokit.boldFont);
        textAreaPanel.add(Box.createRigidArea(VGAP10));

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setAlignmentX(LEFT_ALIGNMENT);
        textWrapper.setBorder(cokit.loweredBorder);

        textAreaPanel.add(textWrapper);

        JTextArea textArea = new JTextArea(text);
        textArea.setForeground(Color.green.brighter());
        textArea.setBackground(Color.black); // cornflower blue

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
        textArea.getAccessibleContext().setAccessibleName("Editable text area");
        label.setLabelFor(textArea);
        textWrapper.add(scroller, BorderLayout.CENTER);

        textAreaPanel.setBackground(Color.white);

        JPanel flagPanel = createVerticalPanel(false);


        JTextField fl0 = new FixedTextField("IE-0", 5);
        fl0.setForeground(Color.green.brighter());
        fl0.setBackground(Color.black);

        JTextField fl1 = new FixedTextField("DE-0", 5);
        fl1.setForeground(Color.green.brighter());
        fl1.setBackground(Color.black);

        JTextField fl2 = new FixedTextField("ZE-0", 5);
        fl2.setForeground(Color.green.brighter());
        fl2.setBackground(Color.black);

        JTextField fl3 = new FixedTextField("OE-0", 5);
        fl3.setForeground(Color.green.brighter());
        fl3.setBackground(Color.black);

        JTextField fl4 = new FixedTextField("UE-0", 5);
        fl4.setForeground(Color.green.brighter());
        fl4.setBackground(Color.black);

        JTextField fl5 = new FixedTextField("PE-0", 5);
        fl5.setForeground(Color.green.brighter());
        fl5.setBackground(Color.black);

        JTextField fl6 = new FixedTextField("IR-0", 5);
        fl6.setForeground(Color.green.brighter());
        fl6.setBackground(Color.black);

        JTextField fl7 = new FixedTextField("CC-1", 5);
        fl7.setForeground(Color.green.brighter());
        fl7.setBackground(Color.black);

        JTextField fl8 = new FixedTextField("ST-0", 5);
        fl8.setForeground(Color.green.brighter());
        fl8.setBackground(Color.black);

        JTextField fl9 = new FixedTextField("IM-1", 5);
        fl9.setForeground(Color.green.brighter());
        fl9.setBackground(Color.black);

        JTextField fl10 = new FixedTextField("DM-1", 5);
        fl10.setForeground(Color.green.brighter());
        fl10.setBackground(Color.black);

        JTextField fl11 = new FixedTextField("ZM-1", 5);
        fl11.setForeground(Color.green.brighter());
        fl11.setBackground(Color.black);

        JTextField fl12 = new FixedTextField("OM-1", 5);
        fl12.setForeground(Color.green.brighter());
        fl12.setBackground(Color.black);

        JTextField fl13 = new FixedTextField("UM-1", 5);
        fl13.setForeground(Color.green.brighter());
        fl13.setBackground(Color.black);

        JTextField fl14 = new FixedTextField("PM-1", 5);
        fl14.setForeground(Color.green.brighter());
        fl14.setBackground(Color.black);

        JTextField fl15 = new FixedTextField("IEM-3", 5);
        fl15.setForeground(Color.green.brighter());
        fl15.setBackground(Color.black);

        JTextField fl16 = new FixedTextField("PC-0", 5);
        fl16.setForeground(Color.green.brighter());
        fl16.setBackground(Color.black);

        JTextField fl17 = new FixedTextField("RC-0", 5);
        fl17.setForeground(Color.green.brighter());
        fl17.setBackground(Color.black);

        JTextField fl18 = new FixedTextField("IC-0", 5);
        fl18.setForeground(Color.green.brighter());
        fl18.setBackground(Color.black);



        flagPanel.setBackground(Color.white);
        label = (JLabel)textFields.add(new JLabel("Flags:"));
        flagPanel.add(label);
        flagPanel.add(fl0);
        flagPanel.add(fl1);
        flagPanel.add(fl2);
        flagPanel.add(fl3);
        flagPanel.add(fl4);
        flagPanel.add(fl5);
        flagPanel.add(fl6);
        flagPanel.add(fl7);
        flagPanel.add(fl8);
        flagPanel.add(fl9);
        flagPanel.add(fl10);
        flagPanel.add(fl11);
        flagPanel.add(fl12);
        flagPanel.add(fl13);
        flagPanel.add(fl14);
        flagPanel.add(fl15);
        flagPanel.add(fl16);
        flagPanel.add(fl17);
        flagPanel.add(fl18);










        demo.add(Box.createRigidArea(HGAP20));
        demo.add(textFields);
        demo.add(Box.createRigidArea(HGAP30));
        demo.add(textAreaPanel);
        demo.add(Box.createRigidArea(HGAP30));
        demo.add(flagPanel);
        demo.add(Box.createRigidArea(HGAP20));



        // go.addActionListener(this);
        // trace.addActionListener(this);



    }

    public void kit8087() throws Exception
    {

        if ((temp = br.readLine()) != null)
        {
            instruction = temp.trim();
            instruction = instruction.toUpperCase();
            if (instruction.equals("DATA SEGMENT"))
            {
                while (!instruction.equals("DATA ENDS"))
                {
                    temp = br.readLine();
                    instruction = temp.trim();
                    int db = instruction.indexOf("DD");
                    int dw = instruction.indexOf("DW");

                    if (db != -1)
                    {


                        String variable = instruction.substring(0, db - 1);
                        variable = variable.trim();
                        dataSegStr[posOfDataSegStr][0] = new String(variable);
                        dataSegStr[posOfDataSegStr][1] = new String("DD");

                        String value = instruction.substring(db + 2, instruction.length());
                        value = value.trim();
                        int valueOfVariable = java.lang.Integer.parseInt(value);
                        dataSegment[posOfDataSegStr++] = valueOfVariable;

                        instructionField.setText(dataSegStr[posOfDataSegStr - 1][0] + " " + dataSegment[posOfDataSegStr - 1] + " "
                                + dataSegStr[posOfDataSegStr - 1][1]);
                    }

                    else if (dw != -1)
                    {
                        String variable = instruction.substring(0, dw - 1);
                        variable = variable.trim();
                        dataSegStr[posOfDataSegStr][0] = new String(variable);
                        dataSegStr[posOfDataSegStr][1] = new String("DW");

                        String value = instruction.substring(dw + 2, instruction.length());
                        value = value.trim();
                        int valueOfVariable = java.lang.Integer.parseInt(value);
                        dataSegment[posOfDataSegStr++] = valueOfVariable;

                        instructionField.setText(dataSegStr[posOfDataSegStr - 1][0] + " " + dataSegment[posOfDataSegStr - 1] + " "
                                + dataSegStr[posOfDataSegStr - 1][1]);

                    }
                }// while
            }// if

            if (instruction.equals("FLD ST(1)"))
            {








            }





            else
            {
                String firstPartInstr = instruction.substring(0, 4);
                firstPartInstr = firstPartInstr.trim();


                if (firstPartInstr.equals("FLD"))
                {

                    String secondPartInstr = instruction.substring(4, instruction.length());
                    int pos;
                    for (pos = 0; pos < posOfDataSegStr; pos++)
                    {
                        if (secondPartInstr.equals(dataSegStr[pos][0]))
                            break;
                    }

                    int secondOperand = dataSegment[pos];

                    stack.push(new Integer(secondOperand));
                    int count = 0;
                    while (!stack.empty())
                    {
                        Integer temp = (Integer)stack.pop();
                        tempStack.push(temp);
                        int second = temp.intValue();

                        String hex = new String("");
                        hex = hex + second;

                        if (count == 0)
                            st0.setText("Valid  st(0)-" + hex);
                        if (count == 1)
                            st1.setText("Valid  st(1)-" + hex);
                        if (count == 2)
                            st2.setText("Valid  st(2)-" + hex);
                        if (count == 3)
                            st3.setText("Valid  st(3)-" + hex);
                        if (count == 4)
                            st4.setText("Valid  st(4)-" + hex);
                        if (count == 5)
                            st5.setText("Valid  st(5)-" + hex);
                        if (count == 6)
                            st6.setText("Valid  st(6)-" + hex);
                        if (count == 7)
                            st7.setText("Valid  st(7)-" + hex);
                        count++;
                    }

                    while (!tempStack.empty())
                    {
                        Integer temp = (Integer)tempStack.pop();
                        stack.push(temp);
                    }
                }

                else if (firstPartInstr.equals("FST"))
                {
                    String secondPartInstr = instruction.substring(4, instruction.length());
                    secondPartInstr = secondPartInstr.trim();
                    int pos;
                    for (pos = 0; pos < posOfDataSegStr; pos++)
                    {
                        if (secondPartInstr.equals(dataSegStr[pos][0]))
                            break;
                    }

                    Integer temp = (Integer)stack.peek();
                    dataSegment[pos] = temp.intValue();
                }

                else if (firstPartInstr.equals("FSTP"))
                {
                    String secondPartInstr = instruction.substring(5, instruction.length());
                    secondPartInstr = secondPartInstr.trim();
                    int pos;
                    for (pos = 0; pos < posOfDataSegStr; pos++)
                    {
                        if (secondPartInstr.equals(dataSegStr[pos][0]))
                            break;
                    }

                    Integer temp = (Integer)stack.pop();
                    dataSegment[pos] = temp.intValue();
                }




            }

            instructionField.setText(instruction);
        }
        else
            fr.close();

    }


    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("Trace"))
        {
            try
            {
                kit8087();
            }
            catch (Exception e)
            {
            }
        }
    }




    public void addTool(JToolBar toolBar, String name)
    {
        JButton b = (JButton)toolBar.add(new JButton(new ImageIcon("images/open.gif", name)));
        b.setToolTipText(name);
        // b.setMargin(cokit.insets0);
    }



    class FixedTextField extends JTextField
    {
        public FixedTextField(String text, int columns)
        {
            super(text, columns);
        }

        public Dimension getMaximumSize()
        {
            return getPreferredSize();
        }

        public float getAlignmentX()
        {
            return LEFT_ALIGNMENT;
        }
    }

    public static String contentsOfFile(String filename)
    {
        try
        {
            int size;
            InputStream f = new FileInputStream(filename);
            size = f.available();
            byte b[] = new byte[size];
            f.read(b);
            filename = new String(b, 0, size);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error");
        }
        return filename;
    }



    public String LoadFile(String filename)
    {
        return contentsOfFile(filename);
    }

}
