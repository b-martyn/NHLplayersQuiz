/*
 * Players DB
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * | id | active | firstName | lastName  |   team    | position | number |
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * |int |boolean |varchar(30)|varchar(30)|varchar(30)|   enum   |  int   |
 * +----+--------+-----------+-----------+-----------+----------+--------+
 * 
 * enum position: Defence, Goalie, Center, Wing
 */

package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbTeam extends DbConnection {
	
	DbTeam(){
		super("players");
	}
	ListOfRows<Player> getTeam(TeamName teamName) throws SQLException{
		List<Player> players = new ArrayList<Player>();
		String team = teamName.toString();
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE team=\"" + team + "\" and active=TRUE")){
			while (resultSet.next()){
				int id = resultSet.getInt("id");
				String firstName = resultSet.getString("firstName");
				String lastName = resultSet.getString("lastName");
				String position = resultSet.getString("position");
				int number = resultSet.getInt("number");
				Player player = new Player(id, firstName, lastName, teamName, Position.valueOf(position), number);
				players.add(player);
			}
		}
		if(players.size() == 0){
			throw new SQLException("No players found for that team");
		}else{
			return new ListOfRows<Player>(players);
		}
	}
	
	boolean addNewPlayer(Player player) throws SQLException{
		boolean result = false;
		if(checkOccurance(player.getFirstName(), player.getLastName())){
			return result;
		}
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + table + " (id, active, firstName, lastName, team, position, number) VALUES (default, FALSE, ?, ?, ?, ?, ?)")){
			preparedStatement.setString(1, player.getFirstName());
			preparedStatement.setString(2, player.getLastName());
			preparedStatement.setString(3, player.getFranchise().getTeamName());
			preparedStatement.setString(4, player.getPosition().toString());
			preparedStatement.setInt(5, player.getNumber());
			result = preparedStatement.execute();
			preparedStatement.close();
			ResultSet newPlayer = connection.createStatement().executeQuery("SELECT id FROM " + table + " WHERE firstName=\"" + player.getFirstName() + "\" AND lastName=\"" + player.getLastName() + "\" AND number=" + player.getNumber());
			if(newPlayer.next()){
				player.setId(newPlayer.getInt("id"));
			}else{
				newPlayer.close();
				throw new SQLException("addNewPlayer failed to insert player into players Database");
			}
			newPlayer.close();
		}
		return result;
	}
	
	Player getPlayer(int playerId) throws SQLException{
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE id=" + playerId)){
			if(resultSet.next()){
				Player player = new Player(resultSet.getInt("id"), resultSet.getString("firstName"), resultSet.getString("lastName"), TeamName.valueOf(resultSet.getString("team").toUpperCase()), Position.valueOf(resultSet.getString("position")), resultSet.getInt("number"));
				return player;
			}
		}
		return null;
	}
	
	private boolean checkOccurance(String firstName, String lastName) throws SQLException{
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE firstName=\"" + firstName + "\" AND lastName=\"" + lastName + "\"")){
			return resultSet.next();
		}
	}
}
