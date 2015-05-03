/*
 *  Returns an array of URL's that make up a players page on www.nhl.com
 *  
 *  PlayerURLFinderHelper - Finds all URLs for PlayerURLFinder
 *  PlayerURLFinder - Finds all player ids assigned by www.nhl.com on a jsoup.document and returns all URLs for PlayerURLCreator
 *  PlayerURLCreator - Screen scrapes a jsoup.document and extracts the player
 */

package player;

import java.net.URL;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import screenScraper.DataExtractor;
import screenScraper.DocumentProducer;
import screenScraper.ElementFinder;
import screenScraper.URLFinder;

public class PlayerURLFinder extends URLFinder{
	
	private static final String URL_PREFIX = "http://www.nhl.com/ice/player.htm?id=";
	
	public PlayerURLFinder(Document document){
		super(document, new PlayerIDfinder(), new PlayerIDextractor());
		setPrefix(URL_PREFIX);
	}
	
	public static class PlayerIDfinder implements ElementFinder{
		
		@Override
		public Elements find(Document document) {
			return document.getElementsByAttributeValueStarting("href", "/ice/player.htm");
		}
	}
	
	public static class PlayerIDextractor implements DataExtractor {
		
		// player ID is nested in href link in node
		// Example: href="/ice/player.htm?id=8477317"
		// substring attribute from last '=' to end of string to get player id
		@Override
		public String extract(Element element) {
			String link = element.attr("href");
			return link.substring(link.lastIndexOf(61) + 1, link.length());
		}
	}
	
	// Because of high wait time for each GET, callable class can be used to run this class
	public static class PlayerURLFinderCallable implements Callable<URL[]> {
		
		private URL url;
		
		public PlayerURLFinderCallable(URL url){
			this.url = url;
		}
		
		@Override
		public URL[] call() throws Exception {
			URLFinder finder = new PlayerURLFinder(DocumentProducer.create(url));
			return finder.find();
		}
	}
	
	public static void main(String[] args) throws Exception{
		PlayerURLFinder finder = new PlayerURLFinder(Jsoup.connect("http://www.nhl.com/ice/playersearch.htm?letter=A&pg=1").get());
		URL[] urls = finder.find();
		for(URL url : urls){
			System.out.println(url);
		}
		System.out.println(urls.length);
		//ElementFinder elementFinder = finder.new PlayerIDfinder();
		//elementFinder.find(finder.document);
	}
}
