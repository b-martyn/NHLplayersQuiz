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
	
	JDefaultTableModel(Object[][] data, Object[] header){
		super(data, header);
	}
	
	JDefaultTableModel(Object[][] data){
		this(data, IDENTIFIERS);
	}
	
	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}
	
	
}

class JDefaultTableCellRenderer extends DefaultTableCellRenderer {
	static final Dimension NUMBER_CELL_SIZE = new Dimension(50, 50);
	private HashMap<Integer[], String[]> cellsToHighlight;
	
	JDefaultTableCellRenderer(){
		this(new HashMap<Integer[], String[]>());
	}
	
	JDefaultTableCellRenderer (HashMap<Integer[], String[]> cellsToHighlight){
		this.cellsToHighlight = cellsToHighlight;
		this.setHorizontalAlignment(CENTER);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column){
		Component component = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
		
		component.setFont(new Font("Serif", Font.PLAIN, 20));
		int franchiseRow = 0;
		for(int i = 0; i < table.getColumnCount(); i++){
			if(table.getColumnName(i).equalsIgnoreCase("franchise")){
				franchiseRow = i;
			}
		}
		Franchise rowFranchise = (Franchise) table.getValueAt(row, franchiseRow);
		
		if(cellsToHighlight.size() != 0){
			int suggestionIdsRow = 0;
			for(int i = 0; i < table.getColumnCount(); i++){
				if(table.getColumnName(i).equalsIgnoreCase("suggestionIds")){
					suggestionIdsRow = i;
				}
			}
			
			Iterator it = cellsToHighlight.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry)it.next();
				Integer[] cell = (Integer[]) entry.getKey();
				String[] value_id = (String[]) entry.getValue();
				if(cell[0] == row && cell[1] == column){
					
					table.setValueAt(value_id[1], row, suggestionIdsRow);
					if(value_id[0].equals("new")){
						if(isSelected){
							component.setBackground(new Color(50,205,50));
						}else{
							component.setBackground(Color.GREEN);
						}
						component.setForeground(Color.WHITE);
					}else if(value_id[0].equals("delete")){
						if(isSelected){
							component.setBackground(new Color(205,50,50));
						}else{
							component.setBackground(Color.RED);
						}
						component.setForeground(Color.WHITE);
					}else{
						setText(value_id[0]);
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