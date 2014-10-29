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
	//number of votes it takes to update player db.
	private static final int VOTE_LIMIT = 10;
	
	private TeamName teamName;
	private JdbcRowSet playerData;
	private JdbcRowSet updatePlayerData;
	
	DbConnection(TeamName teamName) throws SQLException {
		this.teamName = teamName;
		initializeConnections(teamName);
	}
	
	private void initializeConnections(TeamName teamName) throws SQLException{
		initializePlayerData(teamName);
		initializePlayerUpdates();
	}

	private void initializePlayerData(TeamName teamName) throws SQLException {
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		playerData = rowSetFactory.createJdbcRowSet();
		playerData.setUrl(URL);
		playerData.setUsername(USER_NAME);
		playerData.setPassword(PASSWORD);
		playerData.setCommand("SELECT * FROM " + TABLE_PLAYERS + " WHERE team=\"" + teamName.toString() + "\"");
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

	ListOfRows<Player> getTeam() throws SQLException{
		List<Player> players = new ArrayList<Player>();
		playerData.beforeFirst();
		while(playerData.next()){
			int id = playerData.getInt("id");
			String firstName = playerData.getString("firstName");
			String lastName = playerData.getString("lastName");
			String position = playerData.getString("position");
			int number = playerData.getInt("number");
			boolean active = playerData.getBoolean("active");
			Player player = new Player(id, firstName, lastName, teamName, Position.valueOf(position), number, active);
			players.add(player);
		}
		return new ListOfRows<Player>(players);
	}
	
	ListOfRows<Suggestion> getSuggestions(TeamName teamName) throws SQLException{
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		playerData.beforeFirst();
		while(playerData.next()){
			updatePlayerData.beforeFirst();
			while(updatePlayerData.next()){
				if(playerData.getInt("id")==updatePlayerData.getInt("playerId") && playerData.getString("team").equals(teamName.toString())){
					int idNumber = updatePlayerData.getInt("id");
					int playerId = updatePlayerData.getInt("playerId");
					DbPlayerField field = DbPlayerField.valueOf(updatePlayerData.getString("playerField"));
					String value = updatePlayerData.getString("playerNewValue");
					int numOfVotes = updatePlayerData.getInt("numOfVotes");
					suggestions.add(new Suggestion(idNumber, playerId, field, value, numOfVotes));
				}
			}
		}
		return new ListOfRows<Suggestion>(suggestions);
	}
	
	ListOfRows<Suggestion> getSuggestions() throws SQLException{
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		updatePlayerData.beforeFirst();
		while(updatePlayerData.next()){
			int idNumber = updatePlayerData.getInt("id");
			int playerId = updatePlayerData.getInt("playerId");
			DbPlayerField field = DbPlayerField.valueOf(updatePlayerData.getString("playerField"));
			String value = updatePlayerData.getString("playerNewValue");
			int numOfVotes = updatePlayerData.getInt("numOfVotes");
			suggestions.add(new Suggestion(idNumber, playerId, field, value, numOfVotes));
		}
		return new ListOfRows<Suggestion>(suggestions);
	}
	
	void vote(Suggestion suggestion) throws SQLException{
		if(checkOccurance(suggestion)){
			updatePlayerData.beforeFirst();
			while(updatePlayerData.next()){
				if(updatePlayerData.getInt("id") == suggestion.getId()){
					int numOfVotes = updatePlayerData.getInt("numOfVotes");
					if(numOfVotes < VOTE_LIMIT){
						updatePlayerData.updateInt("numOfVotes", numOfVotes + 1);
						updatePlayerData.updateRow();
						return;
					}else{
						playerData.beforeFirst();
						while(playerData.next()){
							if(suggestion.getPlayerId() == playerData.getInt("id")){
								switch(suggestion.getField()){
									case active:
										playerData.updateBoolean("active", Boolean.valueOf(suggestion.getValue()));
										return;
									case firstName:
										playerData.updateString("firstName", suggestion.getValue());
										return;
									case lastName:
										playerData.updateString("lastName", suggestion.getValue());
										return;
									case team:
										playerData.updateString("team", suggestion.getValue());
										return;
									case position:
										playerData.updateString("position", suggestion.getValue());
										return;
									case number:
										playerData.updateInt("number", Integer.valueOf(suggestion.getValue()));
										return;
									default:
										throw new SQLException("suggestion update field not set accurately:" + suggestion.getField() + ", id=" + suggestion.getId());
								}
							}
						}
					}
				}
			}
		}else{
			addNewSuggestion(getPlayer(playerData.getInt("id")), suggestion);
		}
	}

	void addNewSuggestion(Player player, Suggestion suggestion) throws SQLException{
		if(suggestion.getField() == DbPlayerField.active && suggestion.getValue().equalsIgnoreCase("true")){
			if(!checkOccurance(player)){
				createNewPlayer(player);
				suggestion.setPlayerId(player.getId());
			}
		}
		if(checkOccurance(suggestion)){
			System.out.println("checkOccurance of suggestion true");
			vote(suggestion);
		}else{
			updatePlayerData.moveToInsertRow();
			updatePlayerData.updateInt("playerId", player.getId());
			updatePlayerData.updateString("playerField", suggestion.getField().toString());
			updatePlayerData.updateString("playerNewValue", suggestion.getValue());
			updatePlayerData.updateInt("numOfVotes", 1);
			updatePlayerData.insertRow();
		}
	}
	
	Player getPlayer(int playerId) throws SQLException{
		playerData.beforeFirst();
		while(playerData.next()){
			if(playerData.getInt("id") == playerId){
				int id = playerData.getInt("id");
				String firstName = playerData.getString("firstName");
				String lastName = playerData.getString("lastName");
				TeamName teamName = TeamName.valueOf(playerData.getString("team").toUpperCase());
				Position position = Position.valueOf(playerData.getString("position"));
				int number = playerData.getInt("number");
				boolean active = playerData.getBoolean("active");
				return new Player(id, firstName, lastName, teamName, position, number, active);
			}
		}
		return null;
	}
	
	private void createNewPlayer(Player player) throws SQLException{
		if(!checkOccurance(player)){
			if(player.getFranchise().getTeamName().equals(teamName.toString())){
				insertPlayer(playerData, player);
			}else{
				DbConnection newConnection = new DbConnection(TeamName.valueOf(player.getFranchise().getTeamName()));
				insertPlayer(newConnection.playerData, player);
				newConnection.close();
			}
		}
	}
	
	private void insertPlayer(JdbcRowSet rowSet, Player player) throws SQLException{
		rowSet.moveToInsertRow();
		rowSet.updateBoolean("active", true);
		rowSet.updateString("firstName", player.getFirstName());
		rowSet.updateString("lastName", player.getLastName());
		rowSet.updateString("team", player.getFranchise().getTeamName());
		rowSet.updateString("position", player.getPosition().toString());
		rowSet.updateInt("number", player.getNumber());
		rowSet.insertRow();
		rowSet.last();
		player.setId(rowSet.getInt("id"));
	}
	
	boolean checkOccurance(Player player) throws SQLException{
		if(player.getFranchise().getTeamName().equals(teamName.toString())){
			playerData.beforeFirst();
			while(playerData.next()){
				if(playerData.getString("firstName").equals(player.getFirstName()) && playerData.getString("lastName").equals(player.getLastName())){
					if(player.getId() == 0){
						player.setId(playerData.getInt("id"));
					}
					return true;
				}
			}
			return false;
		}else{
			DbConnection newConnection = new DbConnection(TeamName.valueOf(player.getFranchise().getTeamName().toUpperCase()));
			boolean result = newConnection.checkOccurance(player);
			newConnection.close();
			return result;
		}
	}
	
	boolean checkOccurance(Suggestion suggestion) throws SQLException{
		int suggestionId = suggestion.getId();
		int suggestionPlayerId = suggestion.getPlayerId();
		String suggestionField = suggestion.getField().toString();
		String suggestionValue = suggestion.getValue();
		updatePlayerData.beforeFirst();
		while(updatePlayerData.next()){
			if(suggestionId == updatePlayerData.getInt("id")){
				return true;
			}else if(suggestionPlayerId == updatePlayerData.getInt("playerId") && suggestionField.equals(updatePlayerData.getString("playerField")) && suggestionValue.equals(updatePlayerData.getString("playerNewValue"))){
				if(suggestionId == 0){
					suggestion.setId(updatePlayerData.getInt("id"));
				}
				return true;
			}
		}
		return false;
	}
	
	void close() throws SQLException{
		playerData.close();
		updatePlayerData.close();
	}
}
