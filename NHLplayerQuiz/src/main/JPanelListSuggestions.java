package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JPanelListSuggestions extends JPanelListAll {
	private HashMap<Integer[], String[]> cellsToHighlight;
	private ListOfRows<Suggestion> suggestions;
	private ListOfRows<Player> team;
	
	public JPanelListSuggestions(ListOfRows<Player> team, ListOfRows<Suggestion> suggestions) {
		super();
		this.suggestions = suggestions;
		this.team = team;
		setTableData();
	}
	
	@Override
	protected JPanelControls setPanelControls(){
		return new JPanelControls(1, 3, 4, 5, 6);
	}
	
	private void setTableData(){
		String[] header = {"id", "franchise", "suggestionIds", "Number", "First Name", "Last Name", "Position"};
		TableModel model = new JDefaultTableModel(convertData(team.getList()), header);
		tableMain.setModel(model);
		tableMain.setRowSorter(new TableRowSorter<TableModel>(model));
		highlightFields();
		setColumnRenderers();
		scrollPaneMain.setViewportView(new JPanelScrollPaneViewPort(tableMain));
	}
	
	protected void setColumnRenderers()	{
		for(int i = 0; i < 3; i++){
			tableMain.getColumnModel().getColumn(i).setMinWidth(0);
			tableMain.getColumnModel().getColumn(i).setMaxWidth(0);
		}
		
		tableMain.getColumnModel().getColumn(3).setCellRenderer(new JDefaultTableCellRenderer());
		tableMain.getColumnModel().getColumn(3).setMinWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		tableMain.getColumnModel().getColumn(3).setMaxWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		
		for(int i = 4; i < tableMain.getColumnCount(); i++){
			TableColumn column = tableMain.getColumn(tableMain.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer(cellsToHighlight));
		}
	}
	
	private void highlightFields(){
		HashMap<Integer[], String[]> highlightCellMap = new HashMap<Integer[], String[]>();
		suggestionLoop:
		for(Suggestion suggestion : suggestions.getList()){
			int row = getCellMatch(suggestion.getPlayerId());
			if(row == -1){
				continue suggestionLoop;
			}
			Integer[] cell = new Integer[2];
			cell[0] = getCellMatch(suggestion.getPlayerId());
			String[] suggestionValue_Id = new String[2];
			suggestionValue_Id[0] = suggestion.getValue();
			suggestionValue_Id[1] = "" + suggestion.getId();
			switch (suggestion.getField()){
				case active:
					if(suggestion.getValue().equals("TRUE")){
						suggestionValue_Id[0] = "new";
					}else{
						suggestionValue_Id[0] = "delete";
					}
					for(int i = 4; i < 7; i++){
						Integer[] newCell = new Integer[2];
						newCell[0] = getCellMatch(suggestion.getPlayerId());
						newCell[1] = i;
						highlightCellMap.put(newCell, suggestionValue_Id);
					}
					continue suggestionLoop;
				case firstName:
					cell[1] = 4;
					break;
				case lastName:
					cell[1] = 5;
					break;
				case team:
					cell[1] = 2;
					break;
				case position:
					cell[1] = 6;
					break;
				case number:
					cell[1] = 3;
					break;
			}
			highlightCellMap.put(cell, suggestionValue_Id);
		}
		cellsToHighlight = highlightCellMap;
	}
	
	private int getCellMatch(int playerId){
		int row = -1;
		
		for(int i = 0; i < tableMain.getRowCount(); i++){
			if((int)tableMain.getValueAt(i, 0) == playerId){
				row = i;
			}
		}
		
		return row;
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
			dataHolder.add(new Object[]{id, franchise, "", number, firstName, lastName, position});
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}
}
