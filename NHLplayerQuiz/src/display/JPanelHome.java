package display;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JScrollPane;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import core.Franchise;
import core.TeamName;

public class JPanelHome extends JPanel {
	private static final long serialVersionUID = -1252823792236489008L;

	public JPanelHome() {
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JScrollPane scrollPaneMain = new JScrollPane();
		scrollPaneMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPaneMain = new GridBagConstraints();
		gbc_scrollPaneMain.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMain.gridx = 0;
		gbc_scrollPaneMain.gridy = 0;
		add(scrollPaneMain, gbc_scrollPaneMain);

		JPanel panel = new JPanel();
		panel.setLayout(new WrapLayout(FlowLayout.CENTER, 10, 10));

		for (TeamName teamName : TeamName.values()) {
			JButton button = new JButtonTeam(new Franchise(teamName));
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButtonTeam source = (JButtonTeam) e.getSource();
					firePropertyChange("teamSelected", "empty value", source.getFranchise());
				}

			});
			panel.add(button);
		}
		
		scrollPaneMain.setViewportView(panel);
	}

}
