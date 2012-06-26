
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 * RegisterDemo.java
 * 
 * @version 1.1 17/01/2002
 * @author Abhijeet Dhanpal Iraj
 * @Nilesh Madhukar Shirke
 */

public class RegisterDemo extends DemoModule implements ItemListener
{

    RegisterDesignPanel registerDesign = new RegisterDesignPanel();
    waveformDesignPanel waveformDesign = new waveformDesignPanel();
    JButton clock, apply;
    JToggleButton clear, preset;
    JTextField input, soutput, poutput;
    String pinput = new String("00000000");
    int D0 = 0, D1 = 0, D2 = 0, D3 = 0, D4 = 0, D5 = 0, D6 = 0, D7 = 0;
    JCheckBox d0, d1, d2, d3, d4, d5, d6, d7;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args)
    {
        RegisterDemo demo = new RegisterDemo(null);
        demo.mainImpl();
    }


    /**
     * RegisterDemo Constructor
     */

    public RegisterDemo(CoKit cokit)
    {
        super(cokit, "RegisterDemo", "toolbar/JColorChooser.gif");
        createRegister();

    }

    public void createRegister()
    {
        JPanel demo = getDemoPanel();

        JPanel demoPanel = getDemoPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(null);// new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        registerDesign.setSize(790, 500);
        registerDesign.setLocation(0, 0);

        clock = new JButton("Clk");
        clock.setSize(60, 25);
        clock.setLocation(5, 390);
        clock.addActionListener(new ShowAction(this));

        clear = new JToggleButton("Clr", false);
        clear.setSize(60, 25);
        clear.setLocation(5, 345);
        clear.addItemListener(this);

        preset = new JToggleButton("Pr", false);
        preset.setSize(60, 25);
        preset.setLocation(5, 90);
        preset.addItemListener(this);

        apply = new JButton("Apply");
        apply.setSize(80, 25);
        apply.setLocation(5, 450);
        apply.addActionListener(new ShowAction(this));

        innerPanel.add(clock);
        innerPanel.add(clear);
        innerPanel.add(preset);

        JLabel inputLabel = new JLabel("Input");
        inputLabel.setBounds(200, 430, 80, 25);

        input = new JTextField(20);
        input.setSize(80, 25);
        input.setLocation(200, 450);


        innerPanel.add(inputLabel);

        innerPanel.add(input);
        innerPanel.add(apply);

        innerPanel.setBackground(Color.darkGray);
        innerPanel.add(registerDesign);

        demoPanel.add(innerPanel);

    }


    public void itemStateChanged(ItemEvent e)
    {
        System.out.println("Hello");

        if (e.getItemSelectable() == clear)
        {
            if (e.getStateChange() == 1)
                registerDesign.toggleClear(1);
            else
                registerDesign.toggleClear(0);
        }

        if (e.getItemSelectable() == preset)
        {
            // System.exit(0);
            if (e.getStateChange() == 1)
                registerDesign.togglePreset(1);
            else
                registerDesign.togglePreset(0);
        }



    }


    class ShowAction extends AbstractAction
    {
        RegisterDemo demo;


        public ShowAction(RegisterDemo demo)
        {
            this.demo = demo;
        }

        public void actionPerformed(ActionEvent e)
        {
            int inputs = 0;
            if (e.getSource() == clock)
            {
                String binaryInput = new String(input.getText());
                try
                {
                    inputs = Integer.parseInt(binaryInput, 2);
                }
                catch (Exception e1)
                {
                    inputs = 65535;
                }

                if (inputs > 65535)
                    inputs = 65535;
                binaryInput = Integer.toBinaryString(inputs);

                if (!clear.isSelected())
                    registerDesign.giveClock();
            }

            if (e.getSource() == apply)
            {
                String binaryInput = new String(input.getText());
                System.out.println(binaryInput);
                // int inputs=0;
                try
                {
                    inputs = Integer.parseInt(binaryInput, 2);
                }
                catch (Exception e2)
                {
                    inputs = 65535;
                }
                if (inputs > 65535)
                    inputs = 65535;
                binaryInput = Integer.toBinaryString(inputs);
                registerDesign.applyData(binaryInput);

            }
            if (e.getSource() == d0 || e.getSource() == d1 || e.getSource() == d2 || e.getSource() == d3 || e.getSource() == d4
                    || e.getSource() == d5 || e.getSource() == d6 || e.getSource() == d7)
            {
                if (d0.isSelected())
                    D0 = 1;
                else
                    D0 = 0;

                registerDesign.changePinput(D0, D1, D2, D3, D4, D5, D6, D7);
            }

        }


    }


}
