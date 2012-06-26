import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Alu.java-Register Level CoKit
 * 
 * @version 1.1 2002/01/01
 * @author Abhijeet Dhanpal Iraj
 */

public class Alu extends DemoModule
{

    JTextField aField, bField, carryInField, resultField, carryOutField;
    JTextField equalityField, generateField, propagateField, selectField, modeField;
    icDesignPanel icdesign;
    JButton output;
    JEditorPane jeditorpane = new JEditorPane();

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        Alu demo = new Alu(null);
        demo.mainImpl();
    }

    /**
     * Introduction Constructor
     */
    public Alu(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "Alu", "toolbar/JTable.gif");

        JPanel demo = getDemoPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));
        demo.setBackground(Color.white);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(Color.white);

        JPanel inputField = createVerticalPanel(false);
        inputField.setBackground(Color.white);

        JPanel drawingField = createVerticalPanel(false);
        drawingField.setBackground(Color.white);

        JPanel outputField = createVerticalPanel(false);
        outputField.setBackground(Color.white);

        JLabel inputa = new JLabel("Input(A0-A3)");
        aField = new FixedTextField("0000", 10);
        aField.setForeground(Color.green.brighter());
        aField.setBackground(Color.gray);
        aField.setEditable(true);
        aField.setHorizontalAlignment(JTextField.CENTER);

        JLabel inputb = new JLabel("Input(B0-B3)");
        bField = new FixedTextField("0000", 10);
        bField.setForeground(Color.green.brighter());
        bField.setBackground(Color.gray);
        bField.setEditable(true);
        bField.setHorizontalAlignment(JTextField.CENTER);

        JLabel carryIn = new JLabel("Carry Input(~Cn)");
        carryInField = new FixedTextField("0", 10);
        carryInField.setForeground(Color.green.brighter());
        carryInField.setBackground(Color.gray);
        carryInField.setEditable(true);
        carryInField.setHorizontalAlignment(JTextField.CENTER);

        inputField.add(Box.createRigidArea(VGAP30));
        inputField.add(Box.createRigidArea(VGAP30));
        inputField.add(Box.createRigidArea(VGAP20));
        inputField.add(inputa);
        inputField.add(aField);
        inputField.add(Box.createRigidArea(VGAP30));
        inputField.add(inputb);
        inputField.add(bField);
        inputField.add(Box.createRigidArea(VGAP30));
        inputField.add(Box.createRigidArea(VGAP15));
        inputField.add(carryIn);
        inputField.add(carryInField);

        icdesign = new icDesignPanel();

        output = new JButton(createImageIcon("buttons/alu1.gif", ""));
        output.setPressedIcon(createImageIcon("buttons/alu2.gif", ""));
        output.setActionCommand("output");
        output.setFocusPainted(false);
        output.setBorderPainted(false);
        output.setContentAreaFilled(false);
        output.setMargin(new Insets(0, 0, 0, 0));
        output.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                if (event.getSource() == output)
                {
                    int select = java.lang.Integer.parseInt(selectField.getText(), 2);
                    int mode = java.lang.Integer.parseInt(modeField.getText(), 2);
                    int A = java.lang.Integer.parseInt(aField.getText(), 2);
                    int B = java.lang.Integer.parseInt(bField.getText(), 2);
                    int carryin = java.lang.Integer.parseInt(carryInField.getText(), 2);
                    int F = 0, carryout = 0, equality = 0;

                    switch (select)
                    {
                        case 0:
                            if (mode == 1)
                                F = A ^ 15;
                            else if (mode == 0)
                                F = carryin + A;

                            System.out.println(F);

                            if (F > 15)
                            {
                                carryout = 1;
                                F = 16 - F;
                            }
                            else
                                carryout = 0;

                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;
                            break;
                        case 1:
                            if (mode == 1)
                            {
                                F = A + B;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;

                                F = F ^ 15;
                            }
                            else if (mode == 0)
                            {
                                F = A + B + carryin;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }

                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;
                            break;
                        case 2:
                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;

                            if (mode == 1)
                            {
                                A = A ^ 15;
                                F = A & B;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            else if (mode == 0)
                            {
                                B = B ^ 15;

                                F = A + B + carryin;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            break;
                        case 3:
                            if (mode == 1)
                                F = 0;
                            else if (mode == 0)
                            {
                                if (carryin == 0)
                                    F = 15;
                                else if (carryin == 1)
                                    F = 0;
                            }

                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;
                            break;
                        case 4:
                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;

                            if (mode == 1)
                            {
                                F = A & B;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;

                                F = F ^ 15;
                            }
                            else if (mode == 0)
                            {
                                B = B ^ 15;

                                F = A + (A & B) + carryin;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            break;
                        case 5:
                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;

                            if (mode == 1)
                            {
                                F = B ^ 15;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            else if (mode == 0)
                            {
                                F = (A + B) + (A & (B ^ 15)) + carryin;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            break;
                        case 6:
                            if (A == B)
                                equality = 1;
                            else
                                equality = 0;

                            if (mode == 1)
                            {
                                F = A ^ B;

                                if (F > 15)
                                {
                                    carryout = 1;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            else if (mode == 0)
                            {
                                B = B + carryout;

                                if (A > B)
                                {
                                    // A=;
                                    F = 16 - F;
                                }
                                else
                                    carryout = 0;
                            }
                            break;

                    }// switch

                    resultField.setText(java.lang.Integer.toBinaryString(F));
                    equalityField.setText(java.lang.Integer.toBinaryString(equality));
                    carryOutField.setText(java.lang.Integer.toBinaryString(carryout));

                }// getsource()
            }// action performed
        });// actionListener


        JLabel select = new JLabel("Select Input(S0-S3)");
        selectField = new FixedTextField("0000", 10);
        selectField.setForeground(Color.green.brighter());
        selectField.setBackground(Color.gray);
        selectField.setEditable(true);
        selectField.setHorizontalAlignment(JTextField.CENTER);

        JLabel mode = new JLabel("Mode Control(M)");
        modeField = new FixedTextField("0", 5);
        modeField.setForeground(Color.green.brighter());
        modeField.setBackground(Color.gray);
        modeField.setEditable(true);
        modeField.setHorizontalAlignment(JTextField.CENTER);

        JPanel controlField = createHorizontalPanel(false);
        controlField.setBackground(Color.white);

        JPanel labelField = createHorizontalPanel(false);
        labelField.setBackground(Color.white);

        labelField.add(Box.createRigidArea(HGAP30));
        labelField.add(Box.createRigidArea(HGAP15));
        labelField.add(select);
        labelField.add(Box.createRigidArea(HGAP30));
        labelField.add(mode);

        // select.setLabelFor(selectField);
        controlField.add(Box.createRigidArea(HGAP30));
        controlField.add(Box.createRigidArea(HGAP30));
        controlField.add(selectField);
        controlField.add(Box.createRigidArea(HGAP30));
        controlField.add(modeField);
        controlField.add(Box.createRigidArea(VGAP30));
        // controlField.add(Box.createRigidArea(VGAP30));
        // controlField.add(Box.createRigidArea(HGAP10));

        drawingField.add(icdesign);
        drawingField.add(Box.createRigidArea(HGAP30));
        drawingField.add(Box.createRigidArea(HGAP30));
        drawingField.add(controlField);
        // drawingField.add(Box.createRigidArea(VGAP15));
        drawingField.add(labelField);
        drawingField.add(Box.createRigidArea(VGAP30));
        // drawingField.add(output);
        // drawingField.add(Box.createRigidArea(HGAP30));


        JLabel result = new JLabel("Output(F0-F3)");
        resultField = new FixedTextField("0000", 10);
        resultField.setForeground(Color.green.brighter());
        resultField.setBackground(Color.black);
        resultField.setEditable(true);
        resultField.setHorizontalAlignment(JTextField.CENTER);

        JLabel carryOut = new JLabel("Carry Output(~Cn+4)");
        carryOutField = new FixedTextField("0", 5);
        carryOutField.setForeground(Color.green.brighter());
        carryOutField.setBackground(Color.black);
        carryOutField.setEditable(true);
        carryOutField.setHorizontalAlignment(JTextField.CENTER);

        JLabel equality = new JLabel("A=B Equality output");
        equalityField = new FixedTextField("0", 5);
        equalityField.setForeground(Color.green.brighter());
        equalityField.setBackground(Color.black);
        equalityField.setEditable(true);
        equalityField.setHorizontalAlignment(JTextField.CENTER);

        JLabel generate = new JLabel("G=Generate output");
        generateField = new FixedTextField("0", 5);
        generateField.setForeground(Color.green.brighter());
        generateField.setBackground(Color.black);
        generateField.setEditable(true);
        generateField.setHorizontalAlignment(JTextField.CENTER);

        JLabel propagate = new JLabel("P=Propagate output");
        propagateField = new FixedTextField("0", 5);
        propagateField.setForeground(Color.green.brighter());
        propagateField.setBackground(Color.black);
        propagateField.setEditable(true);
        propagateField.setHorizontalAlignment(JTextField.CENTER);

        outputField.add(Box.createRigidArea(VGAP30));
        outputField.add(Box.createRigidArea(VGAP30));
        outputField.add(Box.createRigidArea(VGAP20));
        outputField.add(result);
        outputField.add(resultField);
        outputField.add(Box.createRigidArea(VGAP10));
        outputField.add(carryOut);
        outputField.add(carryOutField);
        outputField.add(Box.createRigidArea(VGAP10));
        outputField.add(equality);
        outputField.add(equalityField);
        outputField.add(Box.createRigidArea(VGAP10));
        outputField.add(generate);
        outputField.add(generateField);
        outputField.add(Box.createRigidArea(VGAP10));
        outputField.add(propagate);
        outputField.add(propagateField);


        innerPanel.add(Box.createRigidArea(VGAP30));
        innerPanel.add(Box.createRigidArea(HGAP30));
        innerPanel.add(Box.createRigidArea(HGAP30));
        innerPanel.add(inputField);
        innerPanel.add(Box.createRigidArea(VGAP30));
        innerPanel.add(drawingField);
        innerPanel.add(Box.createRigidArea(VGAP30));
        innerPanel.add(outputField);
        innerPanel.add(Box.createRigidArea(HGAP30));
        innerPanel.add(output);


        demo.add(innerPanel);



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






}

class icDesignPanel extends JPanel
{


    icDesignPanel()
    {

        repaint();
    }


    public void paint(Graphics g)
    {
        g.setColor(new Color(202, 202, 255));
        g.fillRect(0, 0, 1000, 1000);

        // g.setColor(Color.blue);
        g.setColor(new Color(122, 122, 61));
        g.fillRect(100, 50, 170, 270);
        Font font = new Font("Times New Roman", Font.BOLD, 18);
        g.setFont(font);
        g.setColor(Color.yellow);
        g.drawString("74181", 160, 150);
        g.drawString("ALU", 162, 180);

        g.setColor(Color.darkGray);


        // Input A
        g.drawLine(80, 93, 0, 93);
        g.drawLine(80, 116, 0, 116);

        g.drawLine(80, 93, 80, 80);
        g.drawLine(80, 80, 100, 105);
        g.drawLine(80, 116, 80, 129);
        g.drawLine(80, 129, 100, 105);

        // Input B
        g.drawLine(80, 164, 0, 164);
        g.drawLine(80, 187, 0, 187);

        g.drawLine(80, 164, 80, 151);
        g.drawLine(80, 151, 100, 176);
        g.drawLine(80, 187, 80, 200);
        g.drawLine(80, 200, 100, 176);

        // carry In
        g.drawLine(80, 244, 0, 244);
        g.drawLine(80, 268, 0, 268);

        g.drawLine(80, 244, 80, 231);
        g.drawLine(80, 231, 100, 256);
        g.drawLine(80, 267, 80, 280);
        g.drawLine(80, 280, 100, 256);

        // Result out
        g.drawLine(270, 93, 350, 93);
        g.drawLine(270, 116, 350, 116);

        g.drawLine(350, 93, 350, 80);
        g.drawLine(350, 80, 365, 105);
        g.drawLine(350, 116, 350, 129);
        g.drawLine(350, 129, 365, 105);

        // select in
        g.drawLine(130, 340, 130, 360);
        g.drawLine(154, 340, 154, 360);

        g.drawLine(130, 340, 117, 340);
        g.drawLine(154, 340, 167, 340);
        g.drawLine(117, 340, 142, 320);
        g.drawLine(167, 340, 142, 320);

        // mode in
        g.drawLine(250, 320, 250, 360);
        g.drawString("^", 246, 332);

        // carry out
        g.drawLine(270, 154, 360, 154);
        g.drawString(">", 355, 161);

        // equality out
        g.drawLine(270, 204, 360, 204);
        g.drawString(">", 355, 211);

        // generate out
        g.drawLine(270, 254, 360, 254);
        g.drawString(">", 355, 261);

        // propagate out
        g.drawLine(270, 304, 360, 304);
        g.drawString(">", 355, 311);


    }
}
