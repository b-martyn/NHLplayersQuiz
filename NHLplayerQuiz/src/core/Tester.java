package core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tester {
	private static List<String> lines;
	
	public static void main(String[] args) throws IOException, SQLException {
		/*
		StringBuilder changes = new StringBuilder();
		changes.append("position");
		changes.append("|");
		changes.append("test");
		String bigString = changes.toString();
		System.out.println(bigString);
		String[] strings = bigString.split("\\W");
		for(String string : strings){
			System.out.println(string);
		}
		*/
		PlayerDBupdater test = new PlayerDBupdater();
		test.execute();
		
		/* Working to get every player
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
			URLInstructionSet instructionsZZ = new URLInstructionSet(new URL("http://www.nhl.com/ice/player.htm?id=" + "8474056"), 50);
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
		System.out.println("Number\tPosition\tFirst Name\tLast Name\tTeam");
		for(List<String> playerString : playerStrings){
			Player player = createPlayer(playerString);
			System.out.printf("%s\t%s\t\t%s\t\t%s\t\t%s\n", player.getNumber(), player.getPosition(), player.getFirstName(), player.getLastName(), player.getFranchise());
		}
		*/
		
		
		
		//System.out.println(playerIds.size());
		
		
		/*
		URLInstructionSet instructions = new URLInstructionSet(new URL("http://www.nhl.com/ice/playersearch.htm?letter=B"), 50);
		instructions.addInstruction(38, 45, "shape=\"rect\" href=\"/ice/player.htm");
		URLlineMatcher matcher = new URLlineMatcher(instructions);
		List<String> matches = matcher.getMatches();
		for(String string: matches){
			System.out.println(string);
		}
		*/
		
		/*
		URLInstructionSet instructions = new URLInstructionSet(new URL("http://www.nhl.com/ice/playersearch.htm?letter=B&pg=1"), 75);
		instructions.addInstruction(6, 7, "pg=2\">2</a>");
		instructions.addInstruction(6, 7, "pg=3\">3</a>");
		instructions.addInstruction(6, 7, "pg=4\">4</a>");
		instructions.addInstruction(6, 7, "pg=5\">5</a>");
		instructions.addInstruction(6, 7, "pg=6\">6</a>");
		instructions.addInstruction(6, 7, "pg=7\">7</a>");
		instructions.addInstruction(6, 7, "pg=8\">8</a>");
		URLlineMatcher matcher = new URLlineMatcher(instructions);
		List<String> matches = matcher.getMatches();
		for(String string: matches){
			System.out.println(string);
		}
		*/
		
		/*
		 URLlineMatcher matcher = new URLlineMatcher(new URL("http://www.nhl.com/ice/playersearch.htm?letter=B"), 50);
		 
		 lines = matcher.getLinesMatching("pg=");
		 PlayerExtractor playerExtractor = new PlayerExtractor();
		 
		 String[] strings = playerExtractor.getSubPages(lines.get(0));
		 lines = new ArrayList<String>();
		 for(int i = 0; i < strings.length + 1; i++){
			 matcher = new URLlineMatcher(new URL("http://www.nhl.com/ice/playersearch.htm?letter=B&pg=" + (i + 1)), 50);
			 List<String> playerIds = matcher.getLinesMatching("ice/player", "text-align: left");
			 for(String string : playerIds){
				 lines.add(playerExtractor.getPlayerId(string));
			 }
		 }
		 
		 HashMap<String, String[]> map = new HashMap<String, String[]>();
		 
		 for(String string : lines){
			 matcher = new URLlineMatcher(new URL("http://www.nhl.com/ice/player.htm?id=" + string), 50);
			 
			 List<String> nameAndPosition = matcher.getAllLinesMatching("sweater", "color: #666");
			 if(nameAndPosition.size() == 2){
				 map.put(string, new String[]{nameAndPosition.get(0),nameAndPosition.get(1)});
			 }
		 }
		 */
		 
		 /*for(String key : map.keySet()){
			 System.out.println(playerExtractor.getPlayer(map.get(key)[0], map.get(key)[1]));
		 }*/
		 
		 //System.out.println(map.size());
		 
		 
		 /*
		 lines = matcher.getLinesMatching("ice/player", "text-align: left");

		 for(String string : lines){
			 System.out.println(playerExtractor.getPlayerId(string));
		 }
		 */
		 
		 //System.out.println(playerExtractor.getPlayer(lines.get(0), lines.get(1)));
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
	
	private static void showLines(){
		for(String string : lines){
			System.out.println(string);
		}
		System.out.println(lines.size());
	}
}
