/*
 * Suggestions DB
 * +---+--------+-----------+--------------+----------+
 * |id |playerId|playerField|playerNewValue|numOfVotes|
 * +---+--------+-----------+--------------+----------+
 * |int|  int   |   enum    |  varchar(30) |    int   |
 * +---+--------+-----------+--------------+----------+
 * 
 * enum playerField: active, firstName, lastName, team, position, number
 */

package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class DbSuggestions extends DbConnection{
	
	private final String SQL_INSERT = "INSERT INTO " + this.table + " (id, playerId, playerField, playerNewValue, numOfVotes) VALUES (default, ?, ?, ?, 1)";
	private final String SQL_UPDATE_NUMOFVOTES = "UPDATE " + this.table + " SET numOfVotes=? WHERE id=?";
	private final String SQL_SELECT = "SELECT * FROM " + this.table + " WHERE id=?";
	
	DbSuggestions(){
		super("suggestions");
	}
	
	boolean update(Player player, DbPlayerField field, String value) throws SQLException{
		boolean result = false;
		if(player.getId() == 0){
			DbTeam playersDb = new DbTeam();
			playersDb.addNewPlayer(player);
			update(player, DbPlayerField.active, "TRUE");
		}else{
			if(checkOccurance(player.getId())){
				return result;
			}
		}
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)){
			preparedStatement.setInt(1, player.getId());
			preparedStatement.setString(2, field.toString());
			preparedStatement.setString(3, value);
			result = preparedStatement.execute();
		}
		return result;
	}
	
	ListOfRows<Suggestion> getSuggestions() throws SQLException{
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table)){
			return new ListOfRows<Suggestion>(createSuggestions(resultSet));
		}
	}
	
	ListOfRows<Suggestion> getSuggestions(ListOfRows<Player> team) throws SQLException{
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table)){
			List<Suggestion> suggestions = new ArrayList<Suggestion>();
			while(resultSet.next()){
				for(Player player : team.getList()){
					int playerIdResultSet = resultSet.getInt("playerId");
					if(playerIdResultSet == player.getId()){
						int suggestionId = resultSet.getInt("id");
						suggestions.add(getSuggestion(suggestionId));
					}
				}
			}
			return new ListOfRows<Suggestion>(suggestions);
		}
	}
	
	boolean vote(int id) throws SQLException{
		boolean result = false;
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_NUMOFVOTES)){
			Suggestion suggestion = getSuggestion(id);
			preparedStatement.setInt(1, (suggestion.getNumOfVotes() + 1));
			preparedStatement.setInt(2, suggestion.getId());
			result = preparedStatement.execute();
		}
		return result;
	}
	
	private Suggestion getSuggestion(int id) throws SQLException{
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT)){
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			Suggestion suggestion = createSuggestions(resultSet).get(0);
			resultSet.close();
			return suggestion;
		}
	}
	
	private List<Suggestion> createSuggestions(ResultSet resultSet) throws SQLException{
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		while(resultSet.next()){
			int idNumber = resultSet.getInt("id");
			int playerId = resultSet.getInt("playerId");
			DbPlayerField field = DbPlayerField.valueOf(resultSet.getString("playerField"));
			String value = resultSet.getString("playerNewValue");
			int numOfVotes = resultSet.getInt("numOfVotes");
			suggestions.add(new Suggestion(idNumber, playerId, field, value, numOfVotes));
		}
		return suggestions;
	}
}
