package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.TableColumn;

public class JPanelListSuggestions extends JPanelListAll {
	private HashMap<List<Integer>, String> cellsToHighlight;
	private ListOfRows<Suggestion> suggestions;
	
	public JPanelListSuggestions(ListOfRows<Player> team, ListOfRows<Suggestion> suggestions) {
		super(team);
		this.suggestions = suggestions;
		setHighlightedTableData();
	}
	
	@Override
	protected JPanelControls setPanelControls(){
		return new JPanelControls(1, 3, 4, 5, 6);
	}
	
	@Override
	protected void setTableData(){
	}
	
	private void setHighlightedTableData(){
		Object[][] data = convertData(team.getList());
		tableMain.setModel(new JDefaultTableModel(data));
		highlightFields();
		setColumnRenderers();
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
			column.setCellRenderer(new JDefaultTableCellRenderer(cellsToHighlight));
		}
	}
	
	private void highlightFields(){
		HashMap<List<Integer>, String> highlightCellMap = new HashMap<List<Integer>, String>();
		suggestionLoop:
		for(Suggestion suggestion : suggestions.getList()){
			int column = 0;
			int row = getCellMatch(suggestion.getPlayerId());
			String newValue = suggestion.getValue();
			switch (suggestion.getField()){
				case active:
					if(newValue.equals("TRUE")){
						newValue = "new";
					}else{
						newValue = "delete";
					}
					highlightCellMap.put(Arrays.asList(row, 2), newValue);
					highlightCellMap.put(Arrays.asList(row, 3), newValue);
					highlightCellMap.put(Arrays.asList(row, 4), newValue);
					highlightCellMap.put(Arrays.asList(row, 5), newValue);
					continue suggestionLoop;
				case firstName:
					column = 3;
					break;
				case lastName:
					column = 4;
					break;
				case team:
					column = 2;
					break;
				case position:
					column = 5;
					break;
				case number:
					column = 2;
					break;
			}
			highlightCellMap.put(Arrays.asList(row, column), newValue);
		}
		cellsToHighlight = highlightCellMap;
		// TODO set cell text as newValue
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
}
