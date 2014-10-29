package main;

public class Suggestion {
	private int id;
	private int playerId;
	private DbPlayerField field;
	private String value;
	private int numOfVotes;

	public Suggestion(int playerId, DbPlayerField field, String value){
		this(0, playerId, field, value, 1);
	}
	
	public Suggestion(int id, int playerId, DbPlayerField field, String value, int numOfVotes) {
		this.id = id;
		this.playerId = playerId;
		this.field = field;
		this.value = value;
		this.numOfVotes = numOfVotes;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public DbPlayerField getField() {
		return field;
	}

	public void setField(DbPlayerField field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getNumOfVotes() {
		return numOfVotes;
	}

	public void setNumOfVotes(int numOfVotes) {
		this.numOfVotes = numOfVotes;
	}
	
	@Override
	public String toString(){
		return "id: " + this.id + " , playerId: " + this.playerId + " , field: " + this.field + " , value: " + this.value + " , numOfVotes: " + this.numOfVotes;
	}
}
