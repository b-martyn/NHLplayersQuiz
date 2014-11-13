package core;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDBupdater {
	private DbConnection dbConnection;
	
	PlayerDBupdater() throws SQLException{
		this.dbConnection = new DbConnection();
	}
	
	public void execute() throws SQLException, IOException{
		List<Player> websitePlayers = getPlayersFromWebsite();
		List<Player> dbPlayers = dbConnection.getPlayers().getList();
		dbLoop:
		for(Player websitePlayer : websitePlayers){
			for(Player dbPlayer : dbPlayers){
				if(dbPlayer.compareTo(websitePlayer) != null){
					String[] changes = dbPlayer.compareTo(websitePlayer);
					for(String change : changes){
						switch(change){
							case "equal":
								dbPlayers.remove(dbPlayer);
								continue dbLoop;
							case "franchise":
								System.out.println(new Suggestion(dbPlayer.getId(), DbPlayerField.team, websitePlayer.getFranchise().toString()));
								//dbConnection.vote(new Suggestion(dbPlayer.getId(), DbPlayerField.team, websitePlayer.getFranchise().toString()), true);
								break;
							case "number":
								System.out.println(new Suggestion(dbPlayer.getId(), DbPlayerField.number, String.valueOf(websitePlayer.getNumber())));
								//dbConnection.vote(new Suggestion(dbPlayer.getId(), DbPlayerField.number, String.valueOf(websitePlayer.getId())), true);
								break;
							case "position":
								System.out.println(new Suggestion(dbPlayer.getId(), DbPlayerField.position, websitePlayer.getPosition().toString()));
								//dbConnection.vote(new Suggestion(dbPlayer.getId(), DbPlayerField.position, websitePlayer.getPosition().toString()), true);
								break;
							default:
								System.out.println(change);
						}
					}
					dbPlayers.remove(dbPlayer);
					continue dbLoop;
				}
			}
			Suggestion newSuggestion = new Suggestion(0, DbPlayerField.active, "true");
			System.out.println(newSuggestion);
			//dbConnection.addNewSuggestion(websitePlayer, newSuggestion);
		}
	}
	
	private List<Player> getPlayersFromWebsite() throws IOException{
		List<Player> players = new ArrayList<Player>();
		
		List<String> playerIds = new ArrayList<String>();
		for(int i = 65; i <= 90; i++){
			URLInstructionSet instructions = new URLInstructionSet(new URL("http://www.nhl.com/ice/playersearch.htm?letter=" + Character.toString((char)i) + "&pg=1"), 75);
			instructions.addInstruction(6, 7, "pg=2\">2</a>");
			instructions.addInstruction(6, 7, "pg=3\">3</a>");
			instructions.addInstruction(6, 7, "pg=4\">4</a>");
			instructions.addInstruction(6, 7, "pg=5\">5</a>");
			instructions.addInstruction(6, 7, "pg=6\">6</a>");
			instructions.addInstruction(6, 7, "pg=7\">7</a>");
			instructions.addInstruction(6, 7, "pg=8\">8</a>");
			URLlineMatcher matcher = new URLlineMatcher(instructions);
			List<String> matches = matcher.getMatches();
			matches.add(0, "1");
			for(String string : matches){
				URLInstructionSet instructionSet = new URLInstructionSet(new URL("http://www.nhl.com/ice/playersearch.htm?letter=" + Character.toString((char)i) + "&pg=" + string), 50);
				instructionSet.addInstruction(38, 45, "shape=\"rect\" href=\"/ice/player.htm");
				URLlineMatcher subMatcher = new URLlineMatcher(instructionSet);
				playerIds.addAll(subMatcher.getMatches());
			}
		}
		List<List<String>> playerStrings = new ArrayList<List<String>>();
		for(String string : playerIds){
			URLInstructionSet instructionsZZ = new URLInstructionSet(new URL("http://www.nhl.com/ice/player.htm?id=" + string), 50);
			instructionsZZ.addInstruction(URLInstructionSet.LINE_BEGINNING, -15, "sweater");
			instructionsZZ.addInstruction(10, URLInstructionSet.OPEN_TAG, "sweater");
			instructionsZZ.addInstruction(false, URLInstructionSet.CLOSE_TAG, 0, "</a>", "sweater");
			instructionsZZ.addInstruction(URLInstructionSet.CLOSE_TAG, URLInstructionSet.OPEN_TAG, "color: #666");
			URLlineMatcher matcherZZ = new URLlineMatcher(instructionsZZ);
			List<String> playerMatch = matcherZZ.getMatches();
			if(playerMatch.size() == 4){
				playerStrings.add(playerMatch);
			}
		}
		
		//System.out.println("Number\tPosition\tFirst Name\tLast Name\tTeam");
		for(List<String> playerString : playerStrings){
			players.add(createPlayer(playerString));
			//System.out.printf("%s\t%s\t\t%s\t\t%s\t\t%s\n", player.getNumber(), player.getPosition(), player.getFirstName(), player.getLastName(), player.getFranchise());
		}
		
		return players;
	}
	
	private static Player createPlayer(List<String> list){
		String[] names = list.get(0).split(" ");
		String firstName;
		String lastName;
		if(names.length == 2){
			firstName = names[0];
			lastName = names[1];
		}else{
			if(names[1].equalsIgnoreCase("van")){
				firstName = names[0];
				lastName = names[1].toLowerCase() + " " + names[2];
			}else if(names[0].equalsIgnoreCase("yan")){
				firstName = names[0] + " " + names[1];
				lastName = names[2];
			}else{
				firstName = names[0];
				lastName = names[1] + " " + names[2];
			}
		}
		int number = Integer.parseInt(list.get(1));
		String[] teamNames = list.get(2).split(" ");
		TeamName teamName;
		switch(teamNames[teamNames.length - 1].toUpperCase()){
			case "LEAFS":
				teamName =  TeamName.MAPLE_LEAFS;
				break;
			case "WINGS":
				teamName =  TeamName.RED_WINGS;
				break;
			case "JACKETS":
				teamName =  TeamName.BLUE_JACKETS;
				break;
			default:
				teamName =  TeamName.valueOf(teamNames[teamNames.length - 1].toUpperCase());
				break;
		}
		Position position;
		switch(list.get(3)){
			case "Center":
				position =  Position.Center;
				break;
			case "Left Wing":
				position =  Position.Wing;
				break;
			case "Right Wing":
				position =  Position.Wing;
				break;
			case "Defenseman":
				position =  Position.Defence;
				break;
			case "Goalie":
				position =  Position.Goalie;
				break;
			default:
				position =  null;
				break;
		}
		return new Player(firstName, lastName, teamName, position, number);
	}
}
