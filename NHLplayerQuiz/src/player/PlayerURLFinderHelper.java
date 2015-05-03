/*
 *  Helper class for URLPlayerFinder that creates the URLs to find player id's in
 *  The URLs are the player search pages on www.nhl.com from A - Z
 *  This class returns all the pages for each letter.  Each page contains a max of
 *  50 players
 *  
 *  PlayerURLFinderHelper - Finds all URLs for PlayerURLFinder
 *  PlayerURLFinder - Finds all player ids assigned by www.nhl.com on a jsoup.document and returns all URLs for PlayerURLCreator
 *  PlayerURLCreator - Screen scrapes a jsoup.document and extracts the player
 */

package player;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import screenScraper.DataExtractor;
import screenScraper.ElementFinder;
import screenScraper.URLFinder;

public class PlayerURLFinderHelper extends URLFinder {
	private static final String BASE_URL = "http://www.nhl.com/ice/playersearch.htm?letter=";
	private static final String URL_PREFIX = "&pg=";
	
	public static void main(String[] args) throws Exception{
		/*ElementFinder finder = new PlayerIdPageFinder();
		Connection connection = Jsoup.connect(BASE_URL);
		connection.timeout(35000);*/
		PlayerURLFinderHelper finder = new PlayerURLFinderHelper();
		URL[] elements = finder.find();
		for(URL element : elements){
			System.out.println(element);
		}
	}
	
	public PlayerURLFinderHelper(){
		super(new Document(""), new PlayerIdPageFinder(), new PlayerIdPageExtractor());
		setPrefix(URL_PREFIX);
	}
	
	@Override
	public URL[] find(){
		List<URL> urls = new ArrayList<>();
		// Character values from A - Z are between 65 - 90
		for(int i = 65; i < 91; i++){
			try {
				URL firstPlayerLetterURL = new URL(BASE_URL + (char)i);
				urls.add(firstPlayerLetterURL);
				Connection connection = Jsoup.connect(firstPlayerLetterURL.toString());
				connection.timeout(35000);
				setDocument(connection.get());
				setPrefix(firstPlayerLetterURL.toString() + URL_PREFIX);
				for(URL url : super.find()){
					urls.add(url);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return urls.toArray(new URL[urls.size()]);
	}
	
	public static class PlayerIdPageFinder implements ElementFinder{

		@Override
		public Elements find(Document document) {
			// Parent node of links to additional pages for each letter has class 'pageNumbers'
			Elements elements = document.getElementsByClass("pageNumbers");
			// Select all the child nodes who have the link
			elements = elements.select("[shape=\"rect\"]");
			// Remove redundant links which are nodes with 'Next' and 'Last' innerHTML text
			elements = elements.not(":contains(Next)");
			elements = elements.not(":contains(Last)");
			
			return elements;
		}
	}
	
	public static class PlayerIdPageExtractor implements DataExtractor{

		@Override
		public String extract(Element element) {
			// Returns the 
			return element.html();
		}
		
	}
}
