package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultRowSorter;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class JDefaultTableModel extends DefaultTableModel {
	private static final String[] IDENTIFIERS = {"id", "franchise", "Number", "First Name", "Last Name", "Position"};
	{
		Vector<String> identifiersVector = new Vector<String>();
		identifiersVector.addAll(Arrays.asList(IDENTIFIERS));
		columnIdentifiers=identifiersVector;
	}
	
	JDefaultTableModel(Object[][] data){
		super(data, IDENTIFIERS);
	}
	
	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}
	
	
}

class JDefaultTableCellRenderer extends DefaultTableCellRenderer {
	static final Dimension NUMBER_CELL_SIZE = new Dimension(50, 50);
	private HashMap<List<Integer>, String> cellsToHighlight;
	
	JDefaultTableCellRenderer(){
		this(new HashMap<List<Integer>, String>());
	}
	
	JDefaultTableCellRenderer (HashMap<List<Integer>, String> cellsToHighlight){
		this.cellsToHighlight = cellsToHighlight;
		runSetup();
	}
	
	private void runSetup(){
		this.setHorizontalAlignment(CENTER);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column){
		Component component = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
		
		component.setFont(new Font("Serif", Font.PLAIN, 20));
		
		Franchise rowFranchise = (Franchise) table.getValueAt(row, 1);
		if(cellsToHighlight.size() != 0){
			Iterator it = cellsToHighlight.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				List<Integer> cell = (List<Integer>) entry.getKey();
				String value = (String) entry.getValue();
				if(cell.get(0) == row && cell.get(1) == column){
					if(value.equals("new")){
						if(isSelected){
							component.setBackground(new Color(50,205,50));
						}else{
							component.setBackground(Color.GREEN);
						}
						component.setForeground(Color.WHITE);
					}else if(value.equals("delete")){
						if(isSelected){
							component.setBackground(new Color(205,50,50));
						}else{
							component.setBackground(Color.RED);
						}
						component.setForeground(Color.WHITE);
					}else{
						setText(value);
						if(isSelected){
							Color color = rowFranchise.getMainColor();
							int[] colors = {color.getRed(), color.getGreen(), color.getBlue()};
							for(int i = 0; i < colors.length; i++){
								if(colors[i] > 127){
									colors[i] -= 100;
								}else{
									colors[i] += 100;
								}
							}
							component.setBackground(new Color(colors[0], colors[1], colors[2]));
						}else{
							component.setBackground(rowFranchise.getMainColor());
						}
						component.setForeground(rowFranchise.getBaseColor());
					}
					return component;
				}
			}
		}
		if(isSelected){
			Color color = rowFranchise.getBaseColor();
			int[] colors = {color.getRed(), color.getGreen(), color.getBlue()};
			for(int i = 0; i < colors.length; i++){
				if(colors[i] > 127){
					colors[i] -= 100;
				}else{
					colors[i] += 100;
				}
			}
			component.setBackground(new Color(colors[0], colors[1], colors[2]));
		}else{
			component.setBackground(rowFranchise.getBaseColor());
		}
		component.setForeground(rowFranchise.getMainColor());
		return component;
	}
}