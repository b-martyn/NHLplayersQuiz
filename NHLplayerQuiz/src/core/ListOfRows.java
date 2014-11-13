package core;

import java.util.List;

public class ListOfRows<T extends Searchable> {
	private List<T> rows;

	public ListOfRows(List<T> rows) {
		this.rows = rows;
	}

	public List<T> getList() {
		return rows;
	}

	public T getRandomRow() {
		int numOfRows = rows.size();
		int randomRow = (int) Math.floor((Math.random() * (numOfRows - 1)));
		return rows.get(randomRow);
	}

	public T getRow(int id) {
		for (T row : rows) {
			if (row.getId() == id) {
				return row;
			}
		}
		return null;
	}
	
	public boolean deleteRow(int id){
		for(T row : rows){
			if(row.getId() == id){
				rows.remove(row);
				return true;
			}
		}
		return false;
	}
}
