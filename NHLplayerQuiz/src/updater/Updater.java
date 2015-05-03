/*
 *  www.nhl.com screen scraper that returns a list of all active players
 */

package updater;

import ignore.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import franchise.Franchise;
import franchise.FranchiseFactory;
import player.Player;
import player.PlayerURLFinder;
import player.PlayerURLFinderHelper;
import teamName.TeamName;
import util.Arrays;

public class Updater {
	// Random guess.  About half the amount of threads needed to have a single document per thread to return a Player
	private int threadPoolSize = 1000;
	
	private Franchise[] dbFranchises;
	private Franchise[] websiteFranchises;
	private Player[] dbPlayers;
	private Player[] websitePlayers;
	
	private Connection connection;
	
	public static void main(String[] args) throws Exception{
		Updater updater = new Updater(MyConnection.getConnection());
		updater.update();
		System.out.println("Done!");
	}
	
	public Updater(Connection connection) throws Exception{
		this.connection = connection;
		initialize();
	}
	
	private void initialize() throws Exception{
		// Setup arrays from MySQL DB
		dbFranchises = new Franchise[TeamName.values().length];
		int index = 0;
		for(TeamName teamName : TeamName.values()){
			dbFranchises[index] = FranchiseFactory.getFranchise(teamName);
			index++;
		}
		// Create full list of players from each Franchise
		List<Player> dbPlayersList = new ArrayList<>();
		for(Franchise franchise : dbFranchises){
			for(Player player : franchise.getPlayers()){
				dbPlayersList.add(player);
			}
		}
		dbPlayers = dbPlayersList.toArray(new Player[dbPlayersList.size()]);
		// Create all franchises and players from www.nhl.com
		createAllFranchisesFromWebsite();
		List<Player> websitePlayersList = new ArrayList<>();
		for(Franchise franchise : websiteFranchises){
			for(Player player : franchise.getPlayers()){
				websitePlayersList.add(player);
			}
		}
		websitePlayers = websitePlayersList.toArray(new Player[websitePlayersList.size()]);
	}
	// Thread pools to create array of urls that are converted into a Franchise[]
	private void createAllFranchisesFromWebsite() throws Exception{
		Map<TeamName, List<Player>> franchises = new HashMap<>();
		for(TeamName teamName : TeamName.values()){
			franchises.put(teamName, new ArrayList<Player>());
		}
		URL[] urls = getPlayerPages();
		
		ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);
		List<Future<Entry<TeamName, Player>>> futures = new ArrayList<>();
		for(int i = 0; i < urls.length; i++){
			futures.add(pool.submit(new EntryCreator(urls[i])));
		}
		for(Future<Entry<TeamName, Player>> future : futures){
			if(future.get().getKey() == null){// Player with no team, disregard
				continue;
			}	
			franchises.get(future.get().getKey()).add(future.get().getValue());
		}
		pool.shutdown();
		
		websiteFranchises = new Franchise[TeamName.values().length];
		int index = 0;
		for(TeamName teamName : TeamName.values()){
			Player[] players = franchises.get(teamName).toArray(new Player[franchises.get(teamName).size()]);
			websiteFranchises[index] = new Franchise(teamName, players);
			index++;
		}
	}
	private URL[] getPlayerPages() throws Exception{
		ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);
		List<Future<URL[]>> futures = new ArrayList<>();
		URL[] urls = new PlayerURLFinderHelper().find();
		for(int i = 0; i < urls.length; i++){
			futures.add(pool.submit(new PlayerURLFinder.PlayerURLFinderCallable(urls[i])));
		}
		URL[] playerPages = new URL[0];
		for(Future<URL[]> future : futures){
			playerPages = Arrays.<URL>arrayCombine(playerPages, future.get());
		}
		pool.shutdown();
		return playerPages;
	}
	
	
	public void update() throws SQLException{
		DAOHelper updater = new DAOHelper(connection);
		for(int i = 0; i < dbFranchises.length; i++){
			updater.addNewFranchisePlayers(dbFranchises[i], websiteFranchises[i]);
			updater.deleteOldFranchisePlayers(dbFranchises[i], websiteFranchises[i]);
		}
		
		updater.addNewPlayers(dbPlayers, websitePlayers);
		updater.deleteOldPlayers(dbPlayers, websitePlayers);
	}

	public Franchise[] getDbFranchises() {
		return dbFranchises;
	}

	public void setDbFranchises(Franchise[] dbFranchises) {
		this.dbFranchises = dbFranchises;
	}

	public Franchise[] getWebsiteFranchises() {
		return websiteFranchises;
	}

	public void setWebsiteFranchises(Franchise[] websiteFranchises) {
		this.websiteFranchises = websiteFranchises;
	}

	public Player[] getDbPlayers() {
		return dbPlayers;
	}

	public void setDbPlayers(Player[] dbPlayers) {
		this.dbPlayers = dbPlayers;
	}

	public Player[] getWebsitePlayers() {
		return websitePlayers;
	}

	public void setWebsitePlayers(Player[] websitePlayers) {
		this.websitePlayers = websitePlayers;
	}
}
