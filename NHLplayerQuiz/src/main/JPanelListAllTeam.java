package main;

import javax.swing.table.TableColumn;

public class JPanelListAllTeam extends JPanelListAll {
	
	JPanelListAllTeam(ListOfRows<Player> team){
		super(team);
	}
	
	@Override
	protected JPanelControls setPanelControls(){
		return new JPanelControls(1, 2, 3, 5, 6);
	}
	
	@Override
	protected void setColumnRenderers()	{
		for(int i = 0; i < 2; i++){
			tableMain.getColumnModel().getColumn(i).setMinWidth(0);
			tableMain.getColumnModel().getColumn(i).setMaxWidth(0);
		}
		
		tableMain.getColumnModel().getColumn(2).setCellRenderer(new JDefaultTableCellRenderer());
		tableMain.getColumnModel().getColumn(2).setMinWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		tableMain.getColumnModel().getColumn(2).setMaxWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		
		for(int i = 3; i < tableMain.getColumnCount(); i++){
			TableColumn column = tableMain.getColumn(tableMain.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer());
		}
	}
}
