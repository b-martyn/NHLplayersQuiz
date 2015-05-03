package player;

import java.util.Comparator;

public class Player {

	private int id = 0;
	private String firstName = "";
	private String lastName = "";
	private Position position = null;
	private int number = 0;
	
	public Player(){
		
	}

	public Player(int id, String firstName, String lastName, Position position, int number) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.number = number;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	// Comparators
	public static final Comparator<Player> ID_ORDER = new Comparator<Player>(){
		@Override
		public int compare(Player player1, Player player2) {
			int result = 0;
			
			if(player1.getId() > player2.getId()){
				result = 1;
			}else if(player1.getId() < player2.getId()){
				result = -1;
			}
			
			return result;
		}
	};
	
	public static final Comparator<Player> NAME_ORDER = new Comparator<Player>(){
		@Override
		public int compare(Player player1, Player player2) {
			int result = 0;
			
			result = player1.getLastName().compareTo(player2.getLastName());
			// If the last names match compare the first names
			if(result == 0){
				result = player1.getFirstName().compareTo(player2.getFirstName());
			}
			
			return result;
		}
		
	};
	
	public static final Comparator<Player> NUMBER_ORDER = new Comparator<Player>(){

		@Override
		public int compare(Player player1, Player player2) {
			int result = 0;
			
			if(player1.getNumber() > player2.getNumber()){
				result = 1;
			}else if(player1.getNumber() < player2.getNumber()){
				result = -1;
			}
			
			return result;
		}
	};

	@Override
	public String toString() {
		return "Player [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", position=" + position + ", number=" + number
				+ "]";
	}
}
