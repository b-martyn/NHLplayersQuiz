package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

class DbConnection {
	private static final String URL = "jdbc:mysql://173.48.157.224:3306/nhlPlayerQuiz";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	private static final String TABLE_PLAYERS = "players";
	private static final String TABLE_SUGGESTIONS = "suggestions";
	
	private TeamName teamName;
	private JdbcRowSet playerData;
	private JdbcRowSet updatePlayerData;
	
	DbConnection(TeamName teamName) throws SQLException {
		this.teamName = teamName;
		initializeConnections(teamName);
	}
	
	private void initializeConnections(TeamName teamName) throws SQLException{
		createPlayerData(teamName);
		createUpdates();
	}

	private void createPlayerData(TeamName teamName) throws SQLException {
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		playerData = rowSetFactory.createJdbcRowSet();
		playerData.setUrl(URL);
		playerData.setUsername(USER_NAME);
		playerData.setPassword(PASSWORD);
	}
	
	private void createUpdates() throws SQLException {
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		updatePlayerData = rowSetFactory.createJdbcRowSet();
		updatePlayerData.setUrl(URL);
		updatePlayerData.setUsername(USER_NAME);
		updatePlayerData.setPassword(PASSWORD);
	}

	ListOfRows<Player> getTeam() throws SQLException{
		playerData.setCommand("SELECT * FROM " + TABLE_PLAYERS + " WHERE team=\"" + teamName.toString() + "\"");
		playerData.execute();
		List<Player> players = new ArrayList<Player>();
		playerData.beforeFirst();
		while(playerData.next()){
			int id = playerData.getInt("id");
			String firstName = playerData.getString("firstName");
			String lastName = playerData.getString("lastName");
			String position = playerData.getString("position");
			int number = playerData.getInt("number");
			Player player = new Player(id, firstName, lastName, teamName, Position.valueOf(position), number);
			players.add(player);
		}
		playerData.close();
		updatePlayerData.close();
		return new ListOfRows<Player>(players);
	}
	
	ListOfRows<Suggestion> getSuggestions() throws SQLException{
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		updatePlayerData.setCommand("SELECT * FROM " + TABLE_SUGGESTIONS + "WHERE playerId=?");
		playerData.beforeFirst();
		while(playerData.next()){
			updatePlayerData.setInt(1, playerData.getInt("id"));
			updatePlayerData.execute();
			updatePlayerData.next();
			int idNumber = updatePlayerData.getInt("id");
			int playerId = updatePlayerData.getInt("playerId");
			DbPlayerField field = DbPlayerField.valueOf(updatePlayerData.getString("playerField"));
			String value = updatePlayerData.getString("playerNewValue");
			int numOfVotes = updatePlayerData.getInt("numOfVotes");
			suggestions.add(new Suggestion(idNumber, playerId, field, value, numOfVotes));
		}
		updatePlayerData.close();
		playerData.close();
		return new ListOfRows<Suggestion>(suggestions);
	}
	
	void vote(Suggestion suggestion) throws SQLException{
		if(checkOccurance(suggestion)){
			updatePlayerData.setCommand("UPDATE " + TABLE_SUGGESTIONS + " SET numOfVotes=? WHERE id=?");
			updatePlayerData.setInt(1, (suggestion.getNumOfVotes() + 1));
			updatePlayerData.setInt(2, suggestion.getId());
			updatePlayerData.execute();
		}else{
			playerData.setCommand("SELECT id FROM " + TABLE_PLAYERS + " WHERE id=" + suggestion.getPlayerId());
			playerData.execute();
			playerData.beforeFirst();
			if(playerData.next()){
				addNewSuggestion(getPlayer(playerData.getInt("id")), suggestion);
			}
		}
	}
	
	void addNewSuggestion(Player player, Suggestion suggestion) throws SQLException{
		if(checkOccurance(suggestion)){
			vote(suggestion);
		}else{
			updatePlayerData.setCommand("INSERT INTO " + TABLE_SUGGESTIONS + "(id, playerId, playerField, playerNewValue, numOfVotes VALUES(default, ?, ?, ?, 1)");
			if(suggestion.getField() == DbPlayerField.active && suggestion.getValue().equalsIgnoreCase("true")){
				createNewPlayer(player);
			}
			updatePlayerData.setInt(1, player.getId());
			updatePlayerData.setString(2, suggestion.getField().toString());
			updatePlayerData.setString(3, suggestion.getValue());
		}
	}
	
	Player getPlayer(int playerId) throws SQLException{
		playerData.setCommand("SELECT * FROM " + TABLE_PLAYERS + " WHERE id=" + playerId);
		playerData.beforeFirst();
		int id = playerData.getInt("id");
		String firstName = playerData.getString("firstName");
		String lastName = playerData.getString("lastName");
		TeamName teamName = TeamName.valueOf(playerData.getString("team").toUpperCase());
		Position position = Position.valueOf(playerData.getString("position"));
		int number = playerData.getInt("number");
		return new Player(id, firstName, lastName, teamName, position, number);
	}
	
	private void createNewPlayer(Player player) throws SQLException{
		if(!checkOccurance(player)){
			if(player.getFranchise().getTeamName().equals(teamName.toString())){
				insertPlayer(playerData, player);
			}else{
				insertPlayer(new DbConnection(TeamName.valueOf(player.getFranchise().getTeamName())).playerData, player);
			}
		}
	}
	
	private void insertPlayer(JdbcRowSet rowSet, Player player) throws SQLException{
		rowSet.moveToInsertRow();
		rowSet.setCommand("INSERT INTO " + TABLE_PLAYERS + " (id, active, firstName, lastName, team, position, number) VALUES (default, FALSE, ?, ?, ?, ?, ?)");
		rowSet.setString(1, player.getFirstName());
		rowSet.setString(2, player.getLastName());
		rowSet.setString(3, player.getFranchise().getTeamName());
		rowSet.setString(4, player.getPosition().toString());
		rowSet.setInt(5, player.getNumber());
		rowSet.execute();
		rowSet.setCommand("SELECT id FROM " + TABLE_PLAYERS + " WHERE firstName=\"" + player.getFirstName() + "\" AND lastName=\"" + player.getLastName() + "\" AND team=\"" + player.getFranchise().getTeamName() + "\"");
		rowSet.execute();
		rowSet.beforeFirst();
		if(rowSet.next()){
			player.setId(rowSet.getInt("id"));
		}
		rowSet.close();
	}
	
	boolean checkOccurance(Player player) throws SQLException{
		if(player.getFranchise().getTeamName().equals(teamName.toString())){
			playerData.beforeFirst();
			while(playerData.next()){
				if(playerData.getString("firstName").equals(player.getFirstName()) && playerData.getString("lastName").equals(player.getLastName())){
					return true;
				}
			}
			return false;
		}else{
			DbConnection newConnection = new DbConnection(TeamName.valueOf(player.getFranchise().getTeamName().toUpperCase()));
			return newConnection.checkOccurance(player);
		}
	}
	
	boolean checkOccurance(Suggestion suggestion) throws SQLException{
		RowSetFactory rowSetFactory = RowSetProvider.newFactory();
		JdbcRowSet rowSet = rowSetFactory.createJdbcRowSet();
		rowSet.setUrl(URL);
		rowSet.setUsername(USER_NAME);
		rowSet.setPassword(PASSWORD);
		rowSet.setCommand("SELECT id FROM " + TABLE_SUGGESTIONS + " WHERE playerId=? AND playerField=? AND playerNewValue=?");
		rowSet.setInt(1, suggestion.getPlayerId());
		rowSet.setString(2, suggestion.getField().toString());
		rowSet.setString(3, suggestion.getValue());
		rowSet.execute();
		rowSet.beforeFirst();
		if(rowSet.next()){
			suggestion.setId(rowSet.getInt("id"));
			rowSet.close();
			return true;
		}else{
			rowSet.close();
			return false;
		}
	}
}

/*
 * protected String table = null;
	
	protected DbConnection(String table){
		this.table = table;
	}
	
	protected Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://173.48.157.224:3306/";
		String database = "nhlPlayerQuiz";
		String userName = "root";
		String password = "root";
		return DriverManager.getConnection(url + database, userName, password);
	}
	
	protected boolean checkOccurance(int id) throws SQLException{
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE id=" + id)){
			return resultSet.next();
		}
	}
	*/
