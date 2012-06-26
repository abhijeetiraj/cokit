

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * C8086Panel
 *
 * @version 1.1 01/1/2002
 * @author Abhijeet Iraj
 * @author Nilesh Shirke
 */

public class C8086Panel extends DemoModule {

    CoKit cokit;

    JButton go,trace;
    JRadioButton ds,cs,es,ss;
    String instruction,temp;
    FileReader fr;
    BufferedReader br;
    JTextField axField,bxField,cxField,dxField,instructionField;
    JTextField dsField,csField,esField,ssField;
    int AX,BX,CX,DX,TEMP;
    int AH,AL,BH,BL,CH,CL,DH,DL;
    int DS,CS,ES,SS,SP,BP,SI,DI;
    int SF,ZF,PF,CF,AF,OF,DF,IF,TF;
    int dataSegment[]=new int[65535];
    int codeSegment[]=new int[65535];
    int stackSegment[]=new int[65535];
    int extraSsegment[]=new int[65535];
    String dataSegStr[][]=new String[65535][2];
    int posOfDataSegStr;
    StringBuffer oneZero=new StringBuffer("0");
    StringBuffer twoZero=new StringBuffer("00");
    StringBuffer threeZero=new StringBuffer("000");
    StringBuffer fourZero=new StringBuffer("0000");

    public static void main(String[] args) {
	C8086Panel demo = new C8086Panel(null);
	demo.mainImpl();
    }

    public C8086Panel(CoKit cokit) {
    super(cokit, "C8086Panel", "toolbar/C8086Panel.gif");


    JPanel demo = getDemoPanel();
    demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));
    demo.setBackground(Color.white);

    JPanel textFields =createVerticalPanel(false);
    setBackground(Color.white);

    go = new JButton("Go");
    go.setBackground(new Color(204, 204, 255));
    trace = new JButton("Trace");
    trace.setBackground(new Color(204, 204, 255));

    ds = new JRadioButton("DS");
    cs = new JRadioButton("CS");
    es = new JRadioButton("ES");
    ss = new JRadioButton("SS");

    go.setMnemonic('G');
       trace.setMnemonic('t');
       ds.setMnemonic('D');
       cs.setMnemonic('C');
       es.setMnemonic('E');
       ss.setMnemonic('S');

       axField = new FixedTextField("AX-15A1H", 7);
       axField.setForeground(Color.green.brighter());
       axField.setBackground(Color.black);
       axField.setEditable(false);

       bxField = new FixedTextField("BX-0000H", 7);
       bxField.setForeground(Color.green.brighter());
       bxField.setBackground(Color.black);
       bxField.setEditable(false);
       
       cxField = new FixedTextField("CX-0000H", 7);
       cxField.setForeground(Color.green.brighter());
       cxField.setBackground(Color.black);
       cxField.setEditable(false);

       dxField = new FixedTextField("DX-0000H", 7);
       dxField.setForeground(Color.green.brighter());
       dxField.setBackground(Color.black);
       dxField.setEditable(false);

       JTextField field9 = new FixedTextField("OF-0, DF-0, IF-0, TF-0", 15);
       field9.setForeground(Color.green.brighter());
       field9.setBackground(Color.black);
       field9.setEditable(false);


       JTextField field10 = new FixedTextField("SF-0, ZF-0, AF-0, PF-0, CF-0", 15);
       field10.setForeground(Color.green.brighter());
       field10.setBackground(Color.black);
       field10.setEditable(false);

       textFields.add(Box.createRigidArea(VGAP10));
       textFields.add(Box.createRigidArea(VGAP10));

       JLabel label = (JLabel) textFields.add(new JLabel("General purpose registers:"));
       label.setLabelFor(axField);
       textFields.add(axField);
       textFields.add(bxField);
       textFields.add(cxField);
       textFields.add(dxField);

       textFields.setBackground(Color.white);
       textFields.add(Box.createRigidArea(VGAP5));


       JPanel textFields1 = createVerticalPanel(false);

       JTextField fild1 = new FixedTextField("SP-0000H", 7);
       fild1.setForeground(Color.green.brighter());
       fild1.setBackground(Color.black);
       fild1.setEditable(false);

       JTextField fild2 = new FixedTextField("BP-0000H", 7);
       fild2.setForeground(Color.green.brighter());
       fild2.setBackground(Color.black);
       fild2.setEditable(false);

       JTextField fild3 = new FixedTextField("SI -0000H", 7);
       fild3.setForeground(Color.green.brighter());
       fild3.setBackground(Color.black);
       fild3.setEditable(false);

       JTextField fild4 = new FixedTextField("DI -0000H", 7);
       fild4.setForeground(Color.green.brighter());
       fild4.setBackground(Color.black);
       fild4.setEditable(false);
        
       JTextField fild5 = new FixedTextField("IP -0000H", 7);
       fild5.setForeground(Color.green.brighter());
       fild5.setBackground(Color.black);
       fild5.setEditable(false);

       textFields.add(Box.createRigidArea(VGAP10));
       label = (JLabel) textFields.add(new JLabel("Pointer and Index registers:"));
       label.setLabelFor(fild1);
       textFields.add(label);
       textFields.add(fild1);
       textFields.add(fild2);
       textFields.add(fild3);
       textFields.add(fild4);
       textFields.add(fild5);

       textFields.add(Box.createRigidArea(VGAP10));

       JLabel label3= (JLabel) textFields.add(new JLabel("Flags-H:"));
       textFields.add(label3);
       textFields.add(field9);

       textFields.add(Box.createRigidArea(VGAP5));
       JLabel label4= (JLabel) textFields.add(new JLabel("Flags-L:"));
       textFields.add(label4);
       textFields.add(field10);

       textFields.add(Box.createRigidArea(VGAP10));
             
       JTextField esField = new FixedTextField("ES-1591H", 7);
       esField.setForeground(Color.green.brighter());
       esField.setBackground(Color.black);
       esField.setEditable(false);
       
       JTextField csField = new FixedTextField("CS-15A2H", 7);
       csField.setForeground(Color.green.brighter());
       csField.setBackground(Color.black);
       csField.setEditable(false);

       JTextField dsField = new FixedTextField("DS-1591H", 7);
       dsField.setForeground(Color.green.brighter());
       dsField.setBackground(Color.black);
       dsField.setEditable(false);

       JTextField ssField = new FixedTextField("SS-15A1H", 7);
       ssField.setForeground(Color.green.brighter());
       ssField.setBackground(Color.black);
       ssField.setEditable(false);

       instructionField = new FixedTextField("",20);
       instructionField.setForeground(Color.green.brighter());
       instructionField.setBackground(Color.black);
       instructionField.setEditable(false);

       JLabel label2 = (JLabel) textFields.add(new JLabel("Segment registers:"));
       label2.setLabelFor(fild1);
       textFields1.add(label2);
       textFields1.add(esField);
       textFields1.add(csField);
       textFields1.add(dsField);
       textFields1.add(ssField);

       textFields1.add(Box.createRigidArea(VGAP10));

       JLabel label5 = (JLabel) textFields.add(new JLabel("Instruction:"));
       textFields1.add(label5);
       textFields1.add(instructionField);

       textFields1.add(Box.createRigidArea(VGAP10));
       textFields1.add(go);

       textFields1.add(Box.createRigidArea(VGAP10));
      //  trace.addActionListener(this);
       textFields1.add(trace);

       textFields1.add(Box.createRigidArea(VGAP10));


       ButtonGroup group = new ButtonGroup();

    ds.setBackground(Color.white);
    ds.setSelected(true);
    group.add(ds);
    textFields1.add(ds);
    textFields1.add(Box.createRigidArea(VGAP5));

    cs.setBackground(Color.white);
    cs.setSelected(false);
    group.add(cs);
    textFields1.add(cs);
    textFields1.add(Box.createRigidArea(VGAP5));

    es.setBackground(Color.white);
    es.setSelected(false);
    group.add(es);
    textFields1.add(es);
    textFields1.add(Box.createRigidArea(VGAP5));

    ss.setBackground(Color.white);
    ss.setSelected(false);
    group.add(ss);
    textFields1.add(ss);
    textFields1.add(Box.createRigidArea(VGAP5));

    textFields1.setBackground(Color.white);

    //START

    String text=new String("");
    text = LoadFile("prog.txt");

    JTextArea textArea1 = new JTextArea(text);
    textArea1.setForeground(Color.green.brighter());
    textArea1.setBackground(Color.black);

    JScrollPane scroller1 = new JScrollPane() {
     public Dimension getPreferredSize() {
	  return new Dimension(300,100);
	  }
	  public float getAlignmentX() {
	  return LEFT_ALIGNMENT;
     }
    };

    scroller1.getViewport().add(textArea1);
    textArea1.setFont(new Font("Dialog", Font.PLAIN, 12));
    textArea1.getAccessibleContext().setAccessibleName("Editable text area");
    textArea1.setEditable(false);
    scroller1.setBackground(Color.white);
    textFields1.add(scroller1);

    textFields1.setBackground(Color.white);

	  //END

    text = LoadFile("fact.asm");

    JPanel textAreaPanel = createVerticalPanel(false);
    label = (JLabel) textAreaPanel.add(new JLabel("Program:"));
    textAreaPanel.add(Box.createRigidArea(VGAP10));

    JPanel textWrapper = new JPanel(new BorderLayout());
    textWrapper.setAlignmentX(LEFT_ALIGNMENT);
    textWrapper.setBorder(cokit.loweredBorder);

    textAreaPanel.add(textWrapper);

    JTextArea textArea = new JTextArea(text);
    
    textArea.setForeground(Color.green.brighter());
    textArea.setBackground(Color.black); // cornflower blue
    textArea.setEditable(false);
textArea.selectAll();    JScrollPane scroller = new JScrollPane() {
    public Dimension getPreferredSize() {
	  return new Dimension(400,100);
	  }
	 public float getAlignmentX() {
	 return LEFT_ALIGNMENT;
     }
    };

    scroller.getViewport().add(textArea);
    textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
    textArea.getAccessibleContext().setAccessibleName("Editable text area");
    //textArea.setEnabled(false);
    label.setLabelFor(textArea);
    textWrapper.add(scroller, BorderLayout.CENTER);

    textAreaPanel.setBackground(Color.white);
    textFields1.setBackground(Color.white);

    demo.add(Box.createRigidArea(HGAP30));
    demo.add(textFields);
    demo.add(Box.createRigidArea(HGAP30));
    demo.add(textAreaPanel);
    demo.add(Box.createRigidArea(HGAP30));
    demo.add(textFields1);
    demo.add(Box.createRigidArea(HGAP30));
   }

    class FixedTextField extends JTextField {
	public FixedTextField(String text, int columns) {
	    super(text, columns);
	}
	public Dimension getMaximumSize() {
	    return getPreferredSize();
	}
	public float getAlignmentX() {
	    return LEFT_ALIGNMENT;
	}
    }


        public static String contentsOfFile(String filename) {
        try{
        int size;
        InputStream f=new FileInputStream(filename);
        size=f.available();
        byte b[]=new byte[size];
        f.read(b);
        filename=new String(b,0,size);
        }catch(IOException e){ System.out.println(e);}
        return filename;
    }

       public String LoadFile(String filename) {
      return contentsOfFile(filename);
    }
   

}