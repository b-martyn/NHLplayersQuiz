package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JPanelListAllTeam extends JPanelListAll {
	
	JPanelListAllTeam(ListOfRows<Player> team){
		super(team);
	}
	
	@Override
	protected JPanelControls setPanelControls(){
		return new JPanelControls(1, 2, 3, 5, 6);
	}
	
	@Override
	protected void setTableData(){
		List<Player> activePlayers = new ArrayList<Player>();
		for(Player player : team.getList()){
			if(player.isActive()){
				activePlayers.add(player);
			}
		}
		TableModel model = new JDefaultTableModel(convertData(activePlayers));
		tableMain.setModel(model);
		tableMain.setRowSorter(new TableRowSorter<TableModel>(model));
		setColumnRenderers();
	}
	
	@Override
	protected void setHeaderLabel(){
		lblHeader.setText(team.getRandomRow().getFranchise().getProvince() + " " + team.getRandomRow().getFranchise().getTeamName());
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
