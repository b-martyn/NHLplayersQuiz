package main;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JPanelQuiz extends JPanel {
	
	private Franchise franchise;
	private ListOfRows<Player> team;
	private Player currentPlayer;
	
	private JLabel lblNumber;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblPosition;
	
	public JPanelQuiz(ListOfRows<Player> team) {
		this.team = team;
		this.franchise = team.getRandomRow().getFranchise();
		initialize();
	}
	
	private void initialize(){
		setSize(new Dimension(556, 349));
		setVisible(true);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{342, 0};
		gridBagLayout.rowHeights = new int[]{51, 251, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(franchise.getMainColor());
		GridBagConstraints gbc_panelHeader = new GridBagConstraints();
		gbc_panelHeader.insets = new Insets(0, 0, 0, 0);
		gbc_panelHeader.fill = GridBagConstraints.BOTH;
		gbc_panelHeader.gridx = 0;
		gbc_panelHeader.gridy = 0;
		add(panelHeader, gbc_panelHeader);
		panelHeader.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel labelTeamName = new JLabel(franchise.toString());
		labelTeamName.setForeground(franchise.getBaseColor());
		labelTeamName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		labelTeamName.setHorizontalAlignment(SwingConstants.CENTER);
		panelHeader.add(labelTeamName);
		
		JPanel panelMain = new JPanel();
		panelMain.setBackground(franchise.getBaseColor());
		panelMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent click) {
				if(!lblFirstName.isVisible()){
					lblFirstName.setVisible(true);
					lblLastName.setVisible(true);
					lblPosition.setVisible(true);
				}else{
					setQuizDisplay();
				}
			}
		});
		GridBagConstraints gbc_panelMain = new GridBagConstraints();
		gbc_panelMain.fill = GridBagConstraints.BOTH;
		gbc_panelMain.gridx = 0;
		gbc_panelMain.gridy = 1;
		add(panelMain, gbc_panelMain);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[]{150, 20, 250, 25, 0};
		gbl_panelMain.rowHeights = new int[]{0, 85, 0, 0};
		gbl_panelMain.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelMain.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		panelMain.setLayout(gbl_panelMain);
		
		lblNumber = new JLabel();
		lblNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumber.setFont(new Font("Tahoma", Font.PLAIN, 95));
		lblNumber.setBackground(franchise.getBaseColor());
		lblNumber.setForeground(franchise.getMainColor());
		GridBagConstraints gbc_lblNumber = new GridBagConstraints();
		gbc_lblNumber.anchor = GridBagConstraints.WEST;
		gbc_lblNumber.fill = GridBagConstraints.VERTICAL;
		gbc_lblNumber.gridheight = 3;
		gbc_lblNumber.insets = new Insets(0, 0, 0, 5);
		gbc_lblNumber.gridx = 0;
		gbc_lblNumber.gridy = 0;
		panelMain.add(lblNumber, gbc_lblNumber);
		
		lblFirstName = new JLabel();
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblFirstName.setForeground(franchise.getMainColor());
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 2;
		gbc_lblFirstName.gridy = 0;
		panelMain.add(lblFirstName, gbc_lblFirstName);
		
		lblLastName = new JLabel();
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 60));
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setBackground(franchise.getBaseColor());
		lblLastName.setForeground(franchise.getMainColor());
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 2;
		gbc_lblLastName.gridy = 1;
		panelMain.add(lblLastName, gbc_lblLastName);
		
		lblPosition = new JLabel();
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblPosition.setHorizontalAlignment(SwingConstants.CENTER);
		lblPosition.setBackground(franchise.getBaseColor());
		lblPosition.setForeground(franchise.getMainColor());
		GridBagConstraints gbc_lblPosition = new GridBagConstraints();
		gbc_lblPosition.anchor = GridBagConstraints.SOUTH;
		gbc_lblPosition.insets = new Insets(0, 0, 0, 5);
		gbc_lblPosition.gridx = 2;
		gbc_lblPosition.gridy = 2;
		panelMain.add(lblPosition, gbc_lblPosition);
	}
	
	void start(){
		setQuizDisplay();
	}
	
	private void setQuizDisplay(){
		currentPlayer = team.getRandomRow();
		
		lblNumber.setText(String.valueOf(currentPlayer.getNumber()));
		lblFirstName.setVisible(false);
		lblFirstName.setText(currentPlayer.getFirstName());
		lblLastName.setVisible(false);
		lblLastName.setText(currentPlayer.getLastName());
		lblPosition.setVisible(false);
		lblPosition.setText(currentPlayer.getPosition().toString());
		
		firePropertyChange("playerChange", franchise, currentPlayer.getId());
	}
}
