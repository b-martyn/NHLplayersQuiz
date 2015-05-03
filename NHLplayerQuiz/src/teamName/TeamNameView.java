package teamName;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class TeamNameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JPanel buttonPanel;
	private TeamName selectedTeamName;
	
	public static void main(String[] args){
		TeamNameView view = new TeamNameView();
		
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 733, 439);
		frame.getContentPane().add(view);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public TeamNameView() {
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		buttonPanel = new JPanel();
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_buttonPanel.rowHeights = new int[(int)(TeamName.values().length / 3)];
		gbl_buttonPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_buttonPanel.rowWeights = new double[(int)(TeamName.values().length / 3)];
		buttonPanel.setLayout(gbl_buttonPanel);
		
		int column = 0;
		int row = 0;
		for (TeamName teamName : TeamName.values()) {
			JPanel buttonContainer = new JPanel();
			GridBagLayout gbl_buttonContainer = new GridBagLayout();
			gbl_buttonContainer.columnWidths = new int[] {0};
			gbl_buttonContainer.rowHeights = new int[]{0};
			gbl_buttonContainer.columnWeights = new double[] {1.0, Double.MIN_VALUE };
			gbl_buttonContainer.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			buttonContainer.setLayout(gbl_buttonContainer);

			JButton button = new JButton();
			button.setActionCommand(teamName.toString());
			button.addActionListener(this);
			button.setText(teamName.getProvince() + " " + teamName.getTeamName());
			button.setBackground(teamName.getBaseColor());
			button.setForeground(teamName.getMainColor());
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_button.fill = GridBagConstraints.BOTH;
			gbc_button.gridx = 0;
			gbc_button.gridy = 0;
			buttonContainer.add(button, gbc_button);
			
			GridBagConstraints gbc_buttonContainer = new GridBagConstraints();
			gbc_buttonContainer.fill = GridBagConstraints.BOTH;
			switch(column % 3){
				case 0:
					gbc_buttonContainer.gridx = 0;
					gbc_buttonContainer.gridy = row;
					column++;
					break;
				case 1:
					gbc_buttonContainer.gridx = 1;
					gbc_buttonContainer.gridy = row;
					column++;
					break;
				case 2:
					gbc_buttonContainer.gridx = 2;
					gbc_buttonContainer.gridy = row;
					column++;
					row++;
					break;
			}
			buttonPanel.add(buttonContainer, gbc_buttonContainer);
		}
		
		scrollPane.setViewportView(buttonPanel);
	}
	
	public TeamName getSelectedTeamName(){
		return selectedTeamName;
	}
	
	public void setSelectedTeamname(TeamName teamName){
		this.selectedTeamName = teamName;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		TeamName oldValue = selectedTeamName;
		selectedTeamName = TeamName.valueOf(actionEvent.getActionCommand());
		firePropertyChange("newSelectedTeamName", oldValue, selectedTeamName);
	}
}
