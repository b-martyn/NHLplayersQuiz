package franchise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import player.Player;

public class FranchiseDAO {
	// All parameters are int for statements
	private static final String UPDATE_STATEMENT = "UPDATE franchise SET idfranchise=?, number=? WHERE idplayer=?";
	private static final String DELETE_STATEMENT = "DELETE FROM franchise WHERE idplayer=?";
	private static final String INSERT_STATEMENT = "INSERT INTO franchise VALUES(?, ?, ?)";
	
	private Connection connection;
	
	public FranchiseDAO(Connection connection) throws SQLException{
		this.connection = connection;
		this.connection.setAutoCommit(false);
	}
	
	public void updateFranchise(Franchise...teams) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
		for(Franchise franchise : teams){
			statement.setInt(1, FranchiseFactory.getFranchiseID(franchise.getTeamName()));
			for(Player player : franchise.getPlayers()){
				statement.setInt(2, player.getNumber());
				statement.setInt(3, player.getId());
				statement.executeUpdate();
			}
		}
		connection.commit();
	}
	
	public void deleteFranchise(Franchise...teams) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(DELETE_STATEMENT);
		for(Franchise franchise : teams){
			for(Player player : franchise.getPlayers()){
				statement.setInt(1, player.getId());
				statement.executeUpdate();
			}
		}
		connection.commit();
	}
	
	public void addFranchise(Franchise...teams) throws SQLException{
		PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
		for(Franchise franchise : teams){
			statement.setInt(1, FranchiseFactory.getFranchiseID(franchise.getTeamName()));
			for(Player player : franchise.getPlayers()){
				statement.setInt(2, player.getId());
				statement.setInt(3, player.getNumber());
				statement.executeUpdate();
			}
		}
		connection.commit();
	}
}
