package player;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import franchise.Franchise;

public class PlayerView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel panelHeader;
	private JLabel labelTeamName;
	private JPanel panelMain;
	private JLabel lblNumber;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblPosition;
	
	private Player player;
	
	private boolean isHidden = true;
	
	public PlayerView(){
		initialize();
	}
	
	private void initialize(){
		setSize(new Dimension(556, 349));
		setVisible(true);
		addMouseListener(new PlayerViewMouseAdapter());
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 342, 0 };
		gridBagLayout.rowHeights = new int[] { 51, 251, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		{
			panelHeader = new JPanel();
			GridBagConstraints gbc_panelHeader = new GridBagConstraints();
			gbc_panelHeader.insets = new Insets(0, 0, 0, 0);
			gbc_panelHeader.fill = GridBagConstraints.BOTH;
			gbc_panelHeader.gridx = 0;
			gbc_panelHeader.gridy = 0;
			panelHeader.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			labelTeamName = new JLabel();
			labelTeamName.setFont(new Font("Tahoma", Font.PLAIN, 30));
			labelTeamName.setHorizontalAlignment(SwingConstants.CENTER);
			panelHeader.add(labelTeamName);
			
			add(panelHeader, gbc_panelHeader);
		}
		{
			panelMain = new JPanel();
			GridBagLayout gbl_panelMain = new GridBagLayout();
			gbl_panelMain.columnWidths = new int[] { 25, 150, 20, 250, 25, 0 };
			gbl_panelMain.rowHeights = new int[] { 0, 85, 0, 0 };
			gbl_panelMain.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
			gbl_panelMain.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
			panelMain.setLayout(gbl_panelMain);
			
			lblNumber = new JLabel();
			lblNumber.setHorizontalAlignment(SwingConstants.CENTER);
			lblNumber.setFont(new Font("Tahoma", Font.PLAIN, 95));
			GridBagConstraints gbc_lblNumber = new GridBagConstraints();
			gbc_lblNumber.anchor = GridBagConstraints.WEST;
			gbc_lblNumber.fill = GridBagConstraints.VERTICAL;
			gbc_lblNumber.gridheight = 3;
			gbc_lblNumber.insets = new Insets(0, 0, 0, 5);
			gbc_lblNumber.gridx = 1;
			gbc_lblNumber.gridy = 0;
			panelMain.add(lblNumber, gbc_lblNumber);
			
			lblFirstName = new JLabel();
			lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 40));
			GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
			gbc_lblFirstName.anchor = GridBagConstraints.NORTHEAST;
			gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_lblFirstName.gridx = 3;
			gbc_lblFirstName.gridy = 0;
			panelMain.add(lblFirstName, gbc_lblFirstName);
			
			lblLastName = new JLabel();
			lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 60));
			lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
			GridBagConstraints gbc_lblLastName = new GridBagConstraints();
			gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
			gbc_lblLastName.gridx = 3;
			gbc_lblLastName.gridy = 1;
			panelMain.add(lblLastName, gbc_lblLastName);

			lblPosition = new JLabel();
			lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 30));
			lblPosition.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_lblPosition = new GridBagConstraints();
			gbc_lblPosition.anchor = GridBagConstraints.SOUTH;
			gbc_lblPosition.insets = new Insets(0, 0, 0, 5);
			gbc_lblPosition.gridx = 3;
			gbc_lblPosition.gridy = 2;
			panelMain.add(lblPosition, gbc_lblPosition);
			
			GridBagConstraints gbc_panelMain = new GridBagConstraints();
			gbc_panelMain.fill = GridBagConstraints.BOTH;
			gbc_panelMain.gridx = 0;
			gbc_panelMain.gridy = 1;
			add(panelMain, gbc_panelMain);
		}
		
	}
	
	public void setFranchise(Franchise franchise){
		panelHeader.setBackground(franchise.getTeamName().getMainColor());
		labelTeamName.setText(franchise.getTeamName().toString());
		labelTeamName.setForeground(franchise.getTeamName().getBaseColor());
		
		panelMain.setBackground(franchise.getTeamName().getBaseColor());
		lblNumber.setBackground(franchise.getTeamName().getBaseColor());
		lblNumber.setForeground(franchise.getTeamName().getMainColor());
		lblFirstName.setForeground(franchise.getTeamName().getMainColor());
		lblLastName.setBackground(franchise.getTeamName().getBaseColor());
		lblLastName.setForeground(franchise.getTeamName().getMainColor());
		lblPosition.setBackground(franchise.getTeamName().getBaseColor());
		lblPosition.setForeground(franchise.getTeamName().getMainColor());
	}
	
	public void displayPlayer(){
		lblFirstName.setVisible(true);
		lblLastName.setVisible(true);
		lblPosition.setVisible(true);
		isHidden = false;
	}
	
	public void setPlayer(Player player){
		this.player = player;
		reset();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	private void reset(){
		lblNumber.setText(String.valueOf(player.getNumber()));
		lblFirstName.setVisible(false);
		lblFirstName.setText(player.getFirstName());
		lblLastName.setVisible(false);
		lblLastName.setText(player.getLastName());
		lblPosition.setVisible(false);
		lblPosition.setText(player.getPosition().toString());
		isHidden = true;
	}
	
	public boolean isHidden(){
		return isHidden;
	}
	
	public class PlayerViewMouseAdapter extends MouseAdapter{
		public void MouseClicked(MouseEvent mouseEvent){
			if(isHidden){
				displayPlayer();
			}else{
				reset();
			}
		}
	}
}
