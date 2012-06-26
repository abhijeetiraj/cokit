import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * FlipFlop.java
 * 
 * @version 1.1 11/01/2002
 * @author Abhijeet Dhanpal Iraj
 */

public class FlipFlop extends DemoModule implements ActionListener
{

    int basicFlipFlopChoice = 0;

    Hashtable parts = new Hashtable();
    flipflopInfo flipflopTruthTable = new flipflopInfo();
    FlipFlopDesignPanel flipflopDesign = new FlipFlopDesignPanel();
    JRadioButton srFlipFlop, jkFlipFlop, dFlipFlop, tFlipFlop;
    JCheckBox oneInput, twoInput, preset, clear, Q, Qbar;
    JButton output;
    boolean first, second, set, clr;


    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        FlipFlop demo = new FlipFlop(null);
        demo.mainImpl();
    }

    /**
     * FlipFlop Constructor
     */

    public FlipFlop(CoKit cokit)
    {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the CoKit app.
        super(cokit, "FlipFlop", "toolbar/flipflop.gif");

        createFlipFlop();
    }

    public void createFlipFlop()
    {
        JPanel demo = getDemoPanel();

        JPanel demoPanel = getDemoPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

        demoPanel.add(Box.createRigidArea(VGAP20));
        demoPanel.add(innerPanel);
        demoPanel.add(Box.createRigidArea(VGAP20));

        innerPanel.add(Box.createRigidArea(HGAP20));

        // Create a panel to hold buttons
        JPanel comboBoxPanel = new JPanel()
        {
            public Dimension getMaximumSize()
            {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));

        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        JLabel l = (JLabel)comboBoxPanel.add(new JLabel(getString("FlipFlop.basic")));
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();


        String description = new String("");
        srFlipFlop = new JRadioButton("S-R FLIP-FLOP", createImageIcon("buttons/rb.gif", description));
        srFlipFlop.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        srFlipFlop.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        srFlipFlop.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        srFlipFlop.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        srFlipFlop.setFocusPainted(false);
        srFlipFlop.setBorderPainted(false);
        srFlipFlop.setContentAreaFilled(false);
        srFlipFlop.setMargin(new Insets(0, 0, 0, 0));
        srFlipFlop.setSelected(true);
        group.add(srFlipFlop);

        jkFlipFlop = new JRadioButton("J-K FLIP-FLOP", createImageIcon("buttons/rb.gif", description));
        jkFlipFlop.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        jkFlipFlop.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        jkFlipFlop.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        jkFlipFlop.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        jkFlipFlop.setFocusPainted(false);
        jkFlipFlop.setBorderPainted(false);
        jkFlipFlop.setContentAreaFilled(false);
        jkFlipFlop.setMargin(new Insets(0, 0, 0, 0));
        group.add(jkFlipFlop);

        dFlipFlop = new JRadioButton("D FLIP-FLOP", createImageIcon("buttons/rb.gif", description));
        dFlipFlop.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        dFlipFlop.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        dFlipFlop.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        dFlipFlop.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        dFlipFlop.setFocusPainted(false);
        dFlipFlop.setBorderPainted(false);
        dFlipFlop.setContentAreaFilled(false);
        dFlipFlop.setMargin(new Insets(0, 0, 0, 0));
        group.add(dFlipFlop);

        tFlipFlop = new JRadioButton("T FLIP-FLOP", createImageIcon("buttons/rb.gif", description));
        tFlipFlop.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        tFlipFlop.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        tFlipFlop.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        tFlipFlop.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        tFlipFlop.setFocusPainted(false);
        tFlipFlop.setBorderPainted(false);
        tFlipFlop.setContentAreaFilled(false);
        tFlipFlop.setMargin(new Insets(0, 0, 0, 0));
        group.add(tFlipFlop);

        // add variable actions
        srFlipFlop.addActionListener(this);
        jkFlipFlop.addActionListener(this);
        dFlipFlop.addActionListener(this);
        tFlipFlop.addActionListener(this);

        comboBoxPanel.add(Box.createRigidArea(VGAP10));
        comboBoxPanel.add(srFlipFlop);
        comboBoxPanel.add(Box.createRigidArea(VGAP10));
        comboBoxPanel.add(jkFlipFlop);
        comboBoxPanel.add(Box.createRigidArea(VGAP10));
        comboBoxPanel.add(dFlipFlop);
        comboBoxPanel.add(Box.createRigidArea(VGAP10));
        comboBoxPanel.add(tFlipFlop);


        // l.setLabelFor(fromFlipFlop);

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, flipflopDesign, flipflopTruthTable);
        pane.setOneTouchExpandable(true);
        pane.setDividerLocation(500);

        JPanel facePanel = new JPanel();
        facePanel.setLayout(new BorderLayout());
        facePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        facePanel.add(pane, BorderLayout.CENTER);

        JPanel controlPanel = createHorizontalPanel(false);
        controlPanel.setBackground(new Color(210, 210, 210));

        JLabel firstInput = (JLabel)controlPanel.add(new JLabel("  First Input"));
        oneInput = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        oneInput.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        oneInput.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        oneInput.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        controlPanel.add(oneInput);

        JLabel secondInput = (JLabel)controlPanel.add(new JLabel("Second Input"));
        twoInput = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        twoInput.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        twoInput.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        twoInput.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        controlPanel.add(twoInput);

        JLabel presetInput = (JLabel)controlPanel.add(new JLabel("Preset:"));
        preset = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        preset.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        preset.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        preset.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        preset.setSelected(true);
        controlPanel.add(preset);

        JLabel clearInput = (JLabel)controlPanel.add(new JLabel("Clear:"));
        clear = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        clear.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        clear.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        clear.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        clear.setSelected(true);
        controlPanel.add(clear);

        output = new JButton(createImageIcon("buttons/clock1.gif", ""));
        output.setPressedIcon(createImageIcon("buttons/clock2.gif", ""));
        output.setActionCommand("output");
        output.setFocusPainted(false);
        output.setBorderPainted(false);
        output.setContentAreaFilled(false);
        output.setMargin(new Insets(0, 0, 0, 0));
        output.addActionListener(this);
        controlPanel.add(output);

        JLabel outputOne = (JLabel)controlPanel.add(new JLabel("Q:"));
        Q = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        Q.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        Q.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        Q.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        Q.setSelected(true);
        controlPanel.add(Q);

        JLabel labelout = (JLabel)controlPanel.add(new JLabel("Q~:"));
        Qbar = new JCheckBox("", createImageIcon("buttons/bulb1.gif", ""));
        Qbar.setRolloverIcon(createImageIcon("buttons/bulb1.gif", ""));
        Qbar.setRolloverSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        Qbar.setSelectedIcon(createImageIcon("buttons/bulb2.gif", ""));
        controlPanel.add(Qbar);

        facePanel.add(controlPanel, BorderLayout.SOUTH);

        // add buttons and image panels to inner panel*/
        innerPanel.add(comboBoxPanel);
        innerPanel.add(Box.createRigidArea(HGAP30));
        innerPanel.add(facePanel);
        innerPanel.add(Box.createRigidArea(HGAP20));
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == srFlipFlop)
        {
            twoInput.setEnabled(true);
            flipflopDesign.setSelection(1);
            flipflopTruthTable.setSelection(1);
            flipflopDesign.repaint();
            flipflopTruthTable.repaint();
            first = oneInput.isSelected();
            second = twoInput.isSelected();
            set = preset.isSelected();
            clr = clear.isSelected();
            int state = flipflopDesign.setcolors(first, second, set, clr);
            if (state == 1)
            {
                Q.setSelected(true);
                Qbar.setSelected(false);
            }
            else
            {
                Q.setSelected(false);
                Qbar.setSelected(true);
            }
        }
        if (e.getSource() == jkFlipFlop)
        {
            twoInput.setEnabled(true);
            flipflopDesign.setSelection(2);
            flipflopTruthTable.setSelection(2);
            flipflopDesign.repaint();
            flipflopTruthTable.repaint();
            first = oneInput.isSelected();
            second = twoInput.isSelected();
            set = preset.isSelected();
            clr = clear.isSelected();
            int state = flipflopDesign.setcolors(first, second, set, clr);
            if (state == 1)
            {
                Q.setSelected(true);
                Qbar.setSelected(false);
            }
            else
            {
                Q.setSelected(false);
                Qbar.setSelected(true);
            }
        }
        if (e.getSource() == dFlipFlop)
        {
            twoInput.setEnabled(false);
            flipflopDesign.setSelection(4);
            flipflopTruthTable.setSelection(4);
            flipflopDesign.repaint();
            flipflopTruthTable.repaint();
            first = oneInput.isSelected();
            second = twoInput.isSelected();
            set = preset.isSelected();
            clr = clear.isSelected();
            int state = flipflopDesign.setcolors(first, second, set, clr);
            if (state == 1)
            {
                Q.setSelected(true);
                Qbar.setSelected(false);
            }
            else
            {
                Q.setSelected(false);
                Qbar.setSelected(true);
            }
        }
        if (e.getSource() == tFlipFlop)
        {
            twoInput.setEnabled(false);
            flipflopDesign.setSelection(3);
            flipflopTruthTable.setSelection(3);
            flipflopDesign.repaint();
            flipflopTruthTable.repaint();
            first = oneInput.isSelected();
            second = twoInput.isSelected();
            set = preset.isSelected();
            clr = clear.isSelected();
            int state = flipflopDesign.setcolors(first, second, set, clr);
            if (state == 1)
            {
                Q.setSelected(true);
                Qbar.setSelected(false);
            }
            else
            {
                Q.setSelected(false);
                Qbar.setSelected(true);
            }
        }
        if (e.getSource() == output)
        {
            first = oneInput.isSelected();
            second = twoInput.isSelected();
            set = preset.isSelected();
            clr = clear.isSelected();
            int state = flipflopDesign.setcolors(first, second, set, clr);
            if (state == 1)
            {
                Q.setSelected(true);
                Qbar.setSelected(false);
            }
            else
            {
                Q.setSelected(false);
                Qbar.setSelected(true);
            }

        }

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
