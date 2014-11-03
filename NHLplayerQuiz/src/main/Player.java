package main;

public class Player implements Searchable {
	private int id;
	private String firstName;
	private String lastName;
	private Franchise franchise;
	private Position position;
	private int number;
	private boolean active;

	public Player(int id, String firstName, String lastName, TeamName teamName,
			Position position, int number, boolean active) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.franchise = new Franchise(teamName);
		this.position = position;
		this.number = number;
		this.active = active;
	}

	public Player(String firstName, String lastName, TeamName teamName,
			Position position, int number) {
		this(0, firstName, lastName, teamName, position, number, true);
	}

	public int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Franchise getFranchise() {
		return franchise;
	}

	void setFranchise(TeamName teamName) {
		Franchise franchise = new Franchise(teamName);
		this.franchise = franchise;
	}

	public Position getPosition() {
		return position;
	}

	void setPosition(Position position) {
		this.position = position;
	}

	public int getNumber() {
		return number;
	}

	void setNumber(int number) {
		this.number = number;
	}

	void setActive(boolean active) {
		this.active = active;
	}

	boolean isActive() {
		return active;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			Player compare = (Player) o;
			if (firstName.equals(compare.getFirstName())
					&& lastName.equals(compare.getLastName())
					&& franchise.getTeamName().equals(
							compare.getFranchise().getTeamName())
					&& position.equals(compare.getPosition())
					&& number == compare.getNumber()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= number;
		hash *= firstName.length();
		hash *= number;
		hash *= lastName.length();
		hash *= number;
		hash *= position.toString().length();
		hash *= number;
		hash *= franchise.toString().length();
		return hash;
	}

	@Override
	public String toString() {
		return "ID: " + this.id + ", First Name: " + this.firstName
				+ " Last Name: " + this.lastName + ", Team: "
				+ this.franchise.toString() + ", Position: " + this.position
				+ ", Number: " + this.number;
	}
}
