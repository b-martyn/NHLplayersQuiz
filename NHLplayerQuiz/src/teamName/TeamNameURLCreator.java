package teamName;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import screenScraper.DocumentProducer;

public class TeamNameURLCreator {
	
	private Document document;
	
	public static void main(String[] args) throws Exception{
		Document document = DocumentProducer.create(new URL("http://www.nhl.com/ice/player.htm?id=8474748"));
		TeamNameURLCreator creator = new TeamNameURLCreator(document);
		System.out.println(creator.create());
	}
	
	public TeamNameURLCreator(Document document) {
		this.document = document;
	}
	
	public TeamName create() {
		Pattern patter = Pattern.compile(",\\s(\\w+\\s){1,3}-");
		Matcher matcher = patter.matcher(document.title());
		if(!matcher.find()){
			return null;
		}
		String teamName = matcher.group();
		// Trim the match to only include the name
		teamName = matcher.group().substring(2, teamName.length() - 2);
		// Replace all white space with underscore
		teamName = teamName.toUpperCase();
		teamName = teamName.replaceAll(" ", "_");
		
		return TeamName.valueOf(teamName);
	}
}
