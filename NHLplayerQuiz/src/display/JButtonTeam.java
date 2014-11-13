package display;

import javax.swing.JButton;

import core.Franchise;

public class JButtonTeam extends JButton {
	private static final long serialVersionUID = -119285711301762675L;
	private Franchise franchise;

	JButtonTeam(Franchise franchise) {
		this.franchise = franchise;
		initialize();
	}

	private void initialize() {
		setText(franchise.getProvince() + " " + franchise.getTeamName());
		setBackground(franchise.getBaseColor());
		setForeground(franchise.getMainColor());
	}

	Franchise getFranchise() {
		return franchise;
	}
}
