import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StackPanel extends JPanel
{


    public StackPanel()
    {



    }


    public StackPanel(boolean value)
    {
        setLayout(new BorderLayout());

        StackTableModel = new DefaultTableModel();
        StackTable = new JTable(StackTableModel);
        StackTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        StackTable.setGridColor(new Color(255, 128, 128));

        StackTableModel.addColumn("Class");
        StackTableModel.addColumn("Method");
        StackTableModel.addColumn("Line");
        StackTableModel.addColumn("PC");

        add(new JScrollPane(StackTable));

    }


    public void addRows(Object[] data)
    {
        StackTableModel.addRow(data);
    }


    DefaultTableModel StackTableModel;// = new DefaultTableModel();
    JTable StackTable;// = new JTable(threadTableModel);

}
