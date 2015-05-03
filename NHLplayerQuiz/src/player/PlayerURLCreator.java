/*
 *  Screen scrapes a players URL on www.nhl.com and extracts their name and position
 *  
 *  PlayerURLFinderHelper - Finds all URLs for PlayerURLFinder
 *  PlayerURLFinder - Finds all player ids assigned by www.nhl.com on a jsoup.document and returns all URLs for PlayerURLCreator
 *  PlayerURLCreator - Screen scrapes a jsoup.document and extracts the player
 */

package player;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PlayerURLCreator {
	
	private Document document;
	private Player player = new Player();
	
	public static void main(String[] args) throws Exception{
		PlayerURLCreator creator = new PlayerURLCreator(Jsoup.connect("http://www.nhl.com/ice/player.htm?id=8477317").get());
		System.out.println(creator.create());
	}
	
	public PlayerURLCreator(Document document){
		this.document = document;
	}
	
	public Player create() {
		setId();
		setNames();
		setPosition();
		setNumber();
		return player;
	}
	
	private void setId(){
		// id is the last 7 characters in the link
		String link = document.baseUri();
		player.setId(Integer.valueOf(link.substring(link.length() - 7)));
	}

	private void setNames() {
		String firstName = "";
		String lastName = "";
		// Full name is the first grouping of letters in title up to: 
		String fullName = "";
		try{// 1) first ',' (character #44)
			fullName = document.title().substring(0, document.title().indexOf(44));
		}catch(StringIndexOutOfBoundsException e){// 2) space before first '-' (character #45)
			fullName = document.title().substring(0, document.title().indexOf(45) - 1);
		}
		// Consisting of first name and last name but have to check for exceptions.
		// No name found consists more than 3 parts
		// Exceptions: van - first part of last name  XXXXX van XXXXX
		//			   yan - first part of first name yan XXXXX XXXXX
		// These are the only found exceptions right now for names consisting of more than 2 parts
		String[] names = fullName.split(" ");
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
		
		player.setFirstName(firstName);
		player.setLastName(lastName);
	}

	private void setPosition() {
		// only node that contains position on page
		// <span style="color: #666;">/*Position*/</span>
		String position = document.getElementsByAttributeValue("style", "color: #666;").html();
		position = position.toUpperCase();
		// Replace spaces in "left wing" and "right wing"
		position = position.replaceAll(" ", "_");
		player.setPosition(Position.valueOf(position));
	}
	
	private void setNumber() {
		// only node that contains position on page
		// <span class="sweater">#/*Number/*</span>
		// Select node, get inner HTML, remove pound sign
		try{
			player.setNumber(Integer.valueOf(document.getElementsByClass("sweater").html().substring(1)));
		}catch(StringIndexOutOfBoundsException e){
			// If no number most likely not an active player, return with no value for number
			return;
		}
	}
}
