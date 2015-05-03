package updater;

import java.sql.Connection;
import java.sql.SQLException;
import franchise.Franchise;
import franchise.FranchiseDAO;
import player.Player;
import player.PlayerDAO;
import util.Arrays;

public class DAOHelper {
	
	private FranchiseDAO franchiseDAO;
	private PlayerDAO playerDAO;
	
	public DAOHelper(Connection connection) throws SQLException{
		franchiseDAO = new FranchiseDAO(connection);
		playerDAO = new PlayerDAO(connection);
	}
	
	public Player[] addNewFranchisePlayers(Franchise currentFranchise, Franchise comparingFranchise) throws SQLException{
		if(argumentsAreValid(currentFranchise, comparingFranchise)){
			Player[] newPlayers = Arrays.<Player>findUnique(comparingFranchise.getPlayers(), currentFranchise.getPlayers(), Player.NAME_ORDER);
			currentFranchise.setPlayers(newPlayers);
			franchiseDAO.addFranchise(currentFranchise);
			return newPlayers;
		}else{
			return null;
		}
	}
	
	public Player[] deleteOldFranchisePlayers(Franchise currentFranchise, Franchise comparingFranchise) throws SQLException{
		if(argumentsAreValid(currentFranchise, comparingFranchise)){
			Player[] deleteablePlayers = Arrays.<Player>findUnique(currentFranchise.getPlayers(), comparingFranchise.getPlayers(), Player.NAME_ORDER);
			currentFranchise.setPlayers(deleteablePlayers);
			franchiseDAO.deleteFranchise(currentFranchise);
			return deleteablePlayers;
		}else{
			return null;
		}
	}
	
	public Player[] addNewPlayers(Player[] currentPlayers, Player[] comparingPlayers) throws SQLException{
		Player[] newPlayers = Arrays.<Player>findUnique(comparingPlayers, currentPlayers, Player.NAME_ORDER);
		playerDAO.addPlayer(newPlayers);
		return newPlayers;
	}
	
	public Player[] deleteOldPlayers(Player[] currentPlayers, Player[] comparingPlayers) throws SQLException{
		Player[] deleteablePlayers = Arrays.<Player>findUnique(currentPlayers, comparingPlayers, Player.NAME_ORDER);
		playerDAO.deletePlayer(deleteablePlayers);
		return deleteablePlayers;
	}
	
	// Make sure both arguments are for the same franchise else using deleteOldItems will delete all players
	private boolean argumentsAreValid(Franchise currentFranchise, Franchise comparingFranchise){
		if(currentFranchise.getTeamName() != comparingFranchise.getTeamName()){
			return false;
		}
		return true;
	}
}
