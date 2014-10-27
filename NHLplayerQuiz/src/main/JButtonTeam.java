package main;

import javax.swing.JButton;

public class JButtonTeam extends JButton {
	private Franchise franchise;
	
	JButtonTeam(Franchise franchise){
		this.franchise = franchise;
		initialize();
	}
	
	private void initialize(){
		setText(franchise.getProvince() + " " + franchise.getTeamName());
		setBackground(franchise.getBaseColor());
		setForeground(franchise.getMainColor());
	}
	
	Franchise getFranchise(){
		return franchise;
	}
}
