package quiz;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import player.PlayerView;
import franchise.Franchise;
import franchise.FranchiseFactory;
import franchise.FranchiseView;
import teamName.TeamName;
import teamName.TeamNameView;
import util.Arrays;

public class Controller extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	
	private PlayerView playerView;
	private TeamNameView teamNameView;
	private FranchiseView franchiseView;
	

	private String strTeamNamePane = "TeamName";
	private String strPlayerPane = "Player";
	private String strFranchisePane = "Franchise";
	private CardLayout layout = new CardLayout(0, 0);
	
	private Map<TeamName, Franchise> franchise;
	
	private int index = 0;
	
	public Controller(){
		franchise = new HashMap<TeamName, Franchise>();
		for(TeamName teamName : TeamName.values()){
			franchise.put(teamName, FranchiseFactory.getFranchise(teamName));
			
			System.out.println(teamName.toString() + "|" + franchise.get(teamName).getPlayers().length);
			
		}
		playerView = new PlayerView();
		playerView.addMouseListener(new ControllorMouseAdapter());
		teamNameView = new TeamNameView();
		teamNameView.addPropertyChangeListener(this);
		franchiseView = new FranchiseView();
		
		setLayout(layout);
		add(teamNameView, strTeamNamePane);
		add(franchiseView, strFranchisePane);
		add(playerView, strPlayerPane);
		
		setFranchise(franchise.get(TeamName.BRUINS));
		mixPlayers();
	}
	
	public void setFranchise(Franchise franchise){
		teamNameView.setSelectedTeamname(franchise.getTeamName());
		playerView.setFranchise(franchise);
		franchiseView.setFranchise(franchise);
		reset();
	}
	
	public void newPlayer(){
		playerView.setPlayer(franchiseView.getFranchise().getPlayers()[index]);
		index++;
	}
	
	public void displayPlayer(){
		playerView.displayPlayer();
	}
	
	public void reset(){
		index = 0;
		mixPlayers();
		newPlayer();
	}
	
	public void mixPlayers(){
		Arrays.arrayShuffle(franchiseView.getFranchise().getPlayers());
	}

	public PlayerView getPlayerView() {
		return playerView;
	}

	public void setPlayerView(PlayerView playerView) {
		this.playerView = playerView;
	}

	public TeamNameView getTeamNameView() {
		return teamNameView;
	}

	public void setTeamNameView(TeamNameView teamNameView) {
		this.teamNameView = teamNameView;
	}

	public FranchiseView getFranchiseView() {
		return franchiseView;
	}

	public void setFranchiseView(FranchiseView franchiseView) {
		this.franchiseView = franchiseView;
	}
	
	public Franchise getFranchise(TeamName teamName){
		return franchise.get(teamName);
	}
	
	public Map<TeamName, Franchise> getAllFranchise(){
		return franchise;
	}
	
	public void showFranchise(){
		layout.show(this, strFranchisePane);
	}
	
	public void showQuiz(){
		layout.show(this, strPlayerPane);
	}
	
	public void showTeamNames(){
		layout.show(this, strTeamNamePane);
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		switch(pce.getPropertyName()){
			case "newSelectedTeamName":
				TeamName teamName = (TeamName)pce.getNewValue();
				System.out.println(teamName);
				if(teamName != null){
					setFranchise(franchise.get(teamName));
					showQuiz();
				}
				break;
		}
	}
	
	private class ControllorMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent mouseEvent){
			if(mouseEvent.getSource() instanceof PlayerView){
				if(playerView.isHidden()){
					playerView.displayPlayer();
				}else{
					newPlayer();
				}
			}
		}
	}
}
