package main;

import java.util.List;

public class ListOfRows<T> {
	private List<T> rows;
	
	public ListOfRows(List<T> rows){
		this.rows = rows;
	}
	
	public List<T> getList() {
		return rows;
	}
	
	public T getRandomRow(){
		int numOfRows = rows.size();
		int randomRow = (int) Math.floor((Math.random() * (numOfRows - 1)));
		return rows.get(randomRow);
	}
}
