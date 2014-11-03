/*
 * ++++++++++++++++++++++++++++++Players DB+++++++++++++++++++++++++++++++
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * | id | active | firstName | lastName  |   team    | position | number |
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * |int |boolean |varchar(30)|varchar(30)|varchar(30)|   enum   |  int   |
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * 
 * enum position: Defence, Goalie, Center, Wing
 * 
 * 
 * +++++++++++++++++++Suggestions DB+++++++++++++++++++
 * +---+--------+-----------+--------------+----------+
 * |id |playerId|playerField|playerNewValue|numOfVotes|
 * +---+--------+-----------+--------------+----------+
 * |int|  int   |   enum    |  varchar(30) |    int   |
 * +---+--------+-----------+--------------+----------+
 * 
 * enum playerField: active, firstName, lastName, team, position, number
 */

package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

class DbConnection {
	private static final String URL = "jdbc:mysql://173.48.157.224:3306/nhlPlayerQuiz";
	private static final String USER_NAME = "playerQuizGuest";
	private static final String PASSWORD = "guest";
	private static final String TABLE_PLAYERS = "players";
	private static final String TABLE_SUGGESTIONS = "suggestions";
	// number of votes it takes to update player db.
	private static final int VOTE_LIMIT = 10;

	private static int number = 1;

	private JdbcRowSet playerData;
	private JdbcRowSet updatePlayerData;

	DbConnection() throws SQLException {
		initializePlayerData();
		initializePlayerUpdates();
		System.out.println(number++);
	}

	private void initializePlayerData() throws SQLException {
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		playerData = rowSetFactory.createJdbcRowSet();
		playerData.setUrl(URL);
		playerData.setUsername(USER_NAME);
		playerData.setPassword(PASSWORD);
		playerData.setCommand("SELECT * FROM " + TABLE_PLAYERS);
		playerData.execute();
	}

	private void initializePlayerUpdates() throws SQLException {
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		updatePlayerData = rowSetFactory.createJdbcRowSet();
		updatePlayerData.setUrl(URL);
		updatePlayerData.setUsername(USER_NAME);
		updatePlayerData.setPassword(PASSWORD);
		updatePlayerData.setCommand("SELECT * FROM " + TABLE_SUGGESTIONS);
		updatePlayerData.execute();
	}

	ListOfRows<Player> getPlayers() throws SQLException {
		List<Player> players = new ArrayList<Player>();
		playerData.beforeFirst();
		while (playerData.next()) {
			int id = playerData.getInt("id");
			String firstName = playerData.getString("firstName");
			String lastName = playerData.getString("lastName");
			String position = playerData.getString("position");
			TeamName teamName = TeamName.valueOf(playerData.getString("team")
					.toUpperCase());
			int number = playerData.getInt("number");
			boolean active = playerData.getBoolean("active");
			Player player = new Player(id, firstName, lastName, teamName,
					Position.valueOf(position), number, active);
			players.add(player);
		}
		return new ListOfRows<Player>(players);
	}

	/*
	 * ListOfRows<Suggestion> getSuggestions(TeamName teamName) throws
	 * SQLException{ List<Suggestion> suggestions = new ArrayList<Suggestion>();
	 * playerData.beforeFirst(); while(playerData.next()){
	 * updatePlayerData.beforeFirst(); while(updatePlayerData.next()){
	 * if(playerData.getInt("id")==updatePlayerData.getInt("playerId") &&
	 * playerData.getString("team").equals(teamName.toString())){ int idNumber =
	 * updatePlayerData.getInt("id"); int playerId =
	 * updatePlayerData.getInt("playerId"); DbPlayerField field =
	 * DbPlayerField.valueOf(updatePlayerData.getString("playerField")); String
	 * value = updatePlayerData.getString("playerNewValue"); int numOfVotes =
	 * updatePlayerData.getInt("numOfVotes"); suggestions.add(new
	 * Suggestion(idNumber, playerId, field, value, numOfVotes)); } } } return
	 * new ListOfRows<Suggestion>(suggestions); }
	 */

	ListOfRows<Suggestion> getSuggestions() throws SQLException {
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		updatePlayerData.beforeFirst();
		while (updatePlayerData.next()) {
			int idNumber = updatePlayerData.getInt("id");
			int playerId = updatePlayerData.getInt("playerId");
			DbPlayerField field = DbPlayerField.valueOf(updatePlayerData
					.getString("playerField"));
			String value = updatePlayerData.getString("playerNewValue");
			int numOfVotes = updatePlayerData.getInt("numOfVotes");
			suggestions.add(new Suggestion(idNumber, playerId, field, value,
					numOfVotes));
		}
		return new ListOfRows<Suggestion>(suggestions);
	}

	void vote(Suggestion suggestion, boolean voteUp) throws SQLException {
		if (checkOccurance(suggestion)) {
			updatePlayerData.beforeFirst();
			while (updatePlayerData.next()) {
				if (updatePlayerData.getInt("id") == suggestion.getId()) {
					int numOfVotes = updatePlayerData.getInt("numOfVotes");
					int voteCount = 0;
					if (voteUp) {
						voteCount = 1;
					} else {
						voteCount = -1;
					}
					if (numOfVotes < VOTE_LIMIT) {
						updatePlayerData.updateInt("numOfVotes", numOfVotes
								+ voteCount);
						updatePlayerData.updateRow();
						return;
					} else {
						playerData.beforeFirst();
						while (playerData.next()) {
							if (suggestion.getPlayerId() == playerData
									.getInt("id")) {
								switch (suggestion.getField()) {
								case active:
									playerData.updateBoolean("active", Boolean
											.valueOf(suggestion.getValue()));
									return;
								case firstName:
									playerData.updateString("firstName",
											suggestion.getValue());
									return;
								case lastName:
									playerData.updateString("lastName",
											suggestion.getValue());
									return;
								case team:
									playerData.updateString("team",
											suggestion.getValue());
									return;
								case position:
									playerData.updateString("position",
											suggestion.getValue());
									return;
								case number:
									playerData.updateInt("number", Integer
											.valueOf(suggestion.getValue()));
									return;
								default:
									throw new SQLException(
											"suggestion update field not set accurately:"
													+ suggestion.getField()
													+ ", id="
													+ suggestion.getId());
								}
							}
						}
					}
				}
			}
		} else {
			addNewSuggestion(getPlayer(playerData.getInt("id")), suggestion);
		}
	}

	void addNewSuggestion(Player player, Suggestion suggestion)
			throws SQLException {
		if (suggestion.getField() == DbPlayerField.active
				&& suggestion.getValue().equalsIgnoreCase("true")) {
			if (!checkOccurance(player)) {
				createNewPlayer(player);
				suggestion.setPlayerId(player.getId());
			}
		}
		if (checkOccurance(suggestion)) {
			vote(suggestion, true);
		} else {
			updatePlayerData.moveToInsertRow();
			updatePlayerData.updateInt("playerId", player.getId());
			updatePlayerData.updateString("playerField", suggestion.getField()
					.toString());
			updatePlayerData.updateString("playerNewValue",
					suggestion.getValue());
			updatePlayerData.updateInt("numOfVotes", 1);
			updatePlayerData.insertRow();
		}
	}

	private Player getPlayer(int playerId) throws SQLException {
		playerData.beforeFirst();
		while (playerData.next()) {
			if (playerData.getInt("id") == playerId) {
				int id = playerData.getInt("id");
				String firstName = playerData.getString("firstName");
				String lastName = playerData.getString("lastName");
				TeamName teamName = TeamName.valueOf(playerData.getString(
						"team").toUpperCase());
				Position position = Position.valueOf(playerData
						.getString("position"));
				int number = playerData.getInt("number");
				boolean active = playerData.getBoolean("active");
				return new Player(id, firstName, lastName, teamName, position,
						number, active);
			}
		}
		return null;
	}

	private void createNewPlayer(Player player) throws SQLException {
		if (!checkOccurance(player)) {
			playerData.moveToInsertRow();
			playerData.updateBoolean("active", true);
			playerData.updateString("firstName", player.getFirstName());
			playerData.updateString("lastName", player.getLastName());
			playerData
					.updateString("team", player.getFranchise().getTeamName());
			playerData
					.updateString("position", player.getPosition().toString());
			playerData.updateInt("number", player.getNumber());
			playerData.insertRow();
			playerData.last();
			player.setId(playerData.getInt("id"));
		}
	}

	boolean checkOccurance(Player player) throws SQLException {
		playerData.beforeFirst();
		while (playerData.next()) {
			if (playerData.getString("firstName").equals(player.getFirstName())
					&& playerData.getString("lastName").equals(
							player.getLastName())) {
				if (player.getId() == 0) {
					player.setId(playerData.getInt("id"));
				}
				return true;
			}
		}
		return false;
	}

	boolean checkOccurance(Suggestion suggestion) throws SQLException {
		int suggestionId = suggestion.getId();
		int suggestionPlayerId = suggestion.getPlayerId();
		String suggestionField = suggestion.getField().toString();
		String suggestionValue = suggestion.getValue();
		updatePlayerData.beforeFirst();
		while (updatePlayerData.next()) {
			if (suggestionId == updatePlayerData.getInt("id")) {
				return true;
			} else if (suggestionPlayerId == updatePlayerData
					.getInt("playerId")
					&& suggestionField.equals(updatePlayerData
							.getString("playerField"))
					&& suggestionValue.equals(updatePlayerData
							.getString("playerNewValue"))) {
				if (suggestionId == 0) {
					suggestion.setId(updatePlayerData.getInt("id"));
				}
				return true;
			}
		}
		return false;
	}

	void close() throws SQLException {
		playerData.close();
		updatePlayerData.close();
	}
}
