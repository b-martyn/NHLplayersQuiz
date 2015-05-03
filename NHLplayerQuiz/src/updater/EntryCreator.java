package updater;

import java.net.URL;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;

import player.Player;
import player.PlayerURLCreator;
import screenScraper.DocumentProducer;
import teamName.TeamName;
import teamName.TeamNameURLCreator;

public class EntryCreator implements Callable<Entry<TeamName, Player>>, Entry<TeamName, Player> {
	
	URL url;
	private TeamName key;
	private Player value;
	
	public EntryCreator(URL url){
		this.url = url;
	}
	
	@Override
	public Entry<TeamName, Player> call() throws Exception {
		Document document = DocumentProducer.create(url);
		
		key = new TeamNameURLCreator(document).create();
		value = new PlayerURLCreator(document).create();
		
		return this;
	}

	@Override
	public TeamName getKey() {
		return key;
	}

	@Override
	public Player getValue() {
		return value;
	}

	// Return old value
	@Override
	public Player setValue(Player value) {
		Player oldValue = this.value;
		this.value = value;
		return oldValue;
	}
}
