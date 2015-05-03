package franchise;

import ignore.MyConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import player.Player;
import player.Position;
import teamName.TeamName;

public class FranchiseFactory {
	
	// franchise_creator(int idfranchise)
	// returns resultset containing players and their numbers for a team
	private static final String CALLABLE_STATEMENT = "{call franchise_creator(?)}";
	
	public static void main(String[] args){
		Franchise franchise = FranchiseFactory.getFranchise(TeamName.BRUINS);
		System.out.println(franchise.getPlayers().length);
	}
	
	public static Franchise getFranchise(TeamName teamName){
		Franchise franchise = null;
		
		try {
			Connection connection = MyConnection.getConnection();
			int franchiseID = getFranchiseID(teamName);
			ResultSet rs = executeStoredProcedure(connection, franchiseID);
			franchise = convertResult(rs);
		} catch (SQLException e) {
			franchise = new Franchise();
			franchise.setPlayers(new Player[0]);
		}
		
		franchise.setTeamName(teamName);
		
		return franchise;
	}

	private static ResultSet executeStoredProcedure(java.sql.Connection connection, int franchiseID) throws SQLException{
		CallableStatement statement = connection.prepareCall(CALLABLE_STATEMENT);
		statement.setInt(1, franchiseID);
		return statement.executeQuery();
	}

	private static Franchise convertResult(ResultSet rs) throws SQLException{
		Franchise newFranchise = new Franchise();
		List<Player> players = new ArrayList<>();
		
		rs.beforeFirst();
		while(rs.next()){
			Player player = new Player();
			player.setId(rs.getInt("idplayer"));
			player.setFirstName(rs.getString("firstName"));
			player.setLastName(rs.getString("lastName"));
			player.setPosition(Position.valueOf(rs.getString("position")));
			player.setNumber(rs.getInt("number"));
			players.add(player);
		}
		newFranchise.setPlayers(players.toArray(new Player[players.size()]));
		
		return newFranchise;
	}

	public static int getFranchiseID(TeamName teamName) {
		int result = 0;
		
		switch(teamName){
			case BRUINS:
				result = 1;
				break;
			case CANADIENS:
				result = 2;
				break;
			case DUCKS:
				result = 3;
				break;
			case COYOTES:
				result = 4;
				break;
			case SABRES:
				result = 5;
				break;
			case FLAMES:
				result = 6;
				break;
			case HURRICANES:
				result = 7;
				break;
			case BLACKHAWKS:
				result = 8;
				break;
			case AVALANCHE:
				result = 9;
				break;
			case BLUE_JACKETS:
				result = 10;
				break;
			case STARS:
				result = 11;
				break;
			case RED_WINGS:
				result = 12;
				break;
			case OILERS:
				result = 13;
				break;
			case PANTHERS:
				result = 14;
				break;
			case KINGS:
				result = 15;
				break;
			case WILD:
				result = 16;
				break;
			case PREDATORS:
				result = 17;
				break;
			case DEVILS:
				result = 18;
				break;
			case ISLANDERS:
				result = 19;
				break;
			case RANGERS:
				result = 20;
				break;
			case SENATORS:
				result = 21;
				break;
			case FLYERS:
				result = 22;
				break;
			case PENGUINS:
				result = 23;
				break;
			case SHARKS:
				result = 24;
				break;
			case BLUES:
				result = 25;
				break;
			case LIGHTNING:
				result = 26;
				break;
			case MAPLE_LEAFS:
				result = 27;
				break;
			case CANUCKS:
				result = 28;
				break;
			case CAPITALS:
				result = 29;
				break;
			case JETS:
				result = 30;
				break;
		}
		
		return result;
	}
}
