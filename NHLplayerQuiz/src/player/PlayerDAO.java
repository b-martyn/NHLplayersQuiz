package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerDAO {
	
	private static final String UPDATE_STATEMENT = "UPDATE player SET firstName=?, lastName=?, position=? WHERE idplayer=?";
	private static final String DELETE_STATEMENT = "DELETE FROM player WHERE idplayer=?";
	private static final String INSERT_STATEMENT = "INSERT INTO player VALUES(?, ?, ?, ?)";
	
	private Connection connection;
	
	public PlayerDAO(Connection connection) throws SQLException{
		this.connection = connection;
		this.connection.setAutoCommit(false);
	}
	
	public void updatePlayer(Player...players) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
		for(Player player : players){
			statement.setString(1, player.getFirstName());
			statement.setString(2, player.getLastName());
			statement.setString(3, player.getPosition().toString());
			statement.setInt(4, player.getId());
			statement.executeUpdate();
		}
		connection.commit();
	}
	
	public void deletePlayer(Player...players) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT);
		for(Player player : players){
			statement.setInt(1, player.getId());
			statement.executeUpdate();
		}
		connection.commit();
	}
	
	public void addPlayer(Player...players) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
		for(Player player : players){
			statement.setInt(1, player.getId());
			statement.setString(2, player.getFirstName());
			statement.setString(3, player.getLastName());
			statement.setString(4, player.getPosition().toString());
			statement.executeUpdate();
		}
		connection.commit();
	}
}
