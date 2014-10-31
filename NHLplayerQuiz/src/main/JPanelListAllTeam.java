package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JPanelListAllTeam extends JPanelListAll {
	private ListOfRows<Player> team;
	
	JPanelListAllTeam(ListOfRows<Player> team){
		super();
		this.team = team;
		setTableData();
	}
	
	@Override
	protected JPanelControls setPanelControls(){
		return new JPanelControls(1, 2, 3, 5, 6);
	}
	
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
	
	protected void setHeaderLabel(){
		lblHeader.setText(team.getRandomRow().getFranchise().getProvince() + " " + team.getRandomRow().getFranchise().getTeamName());
	}
	
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
	
	protected Object[][] convertData(List<Player> players){
		List<Object[]> dataHolder = new ArrayList<Object[]>();
		for(Player player : players){
			int id = player.getId();
			Franchise franchise = player.getFranchise();
			String number = String.valueOf(player.getNumber());
			String firstName = player.getFirstName();
			String lastName = player.getLastName();
			String position = player.getPosition().toString();
			dataHolder.add(new Object[]{id, franchise, number, firstName, lastName, position});
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}
}
