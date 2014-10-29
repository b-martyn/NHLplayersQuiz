package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class JDialogEdit extends JDialog {
	private Player player;
	private Player editedPlayer;
	private Border defaultBorder;
	private final JPanel panelMain = new JPanel();
	private JTextField txtFieldPosition;
	private JTextField txtFieldTeam;
	private JTextField txtFieldNumber;
	private JTextField txtFieldFirstName;
	private JTextField txtFieldLastName;
	
	JDialogEdit(){
		this(null);
	}
	
	JDialogEdit(Player player){
		this.player = player;
		initialize();
		setFields();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void initialize() {
		setResizable(false);
		setBounds(100, 100, 228, 193);
		getContentPane().setLayout(new BorderLayout());
		panelMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panelMain, BorderLayout.CENTER);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[]{0, 0, 0};
		gbl_panelMain.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelMain.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelMain.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelMain.setLayout(gbl_panelMain);
		{
			JLabel lblTeam = new JLabel("Team");
			GridBagConstraints gbc_lblTeam = new GridBagConstraints();
			gbc_lblTeam.anchor = GridBagConstraints.EAST;
			gbc_lblTeam.insets = new Insets(0, 0, 5, 5);
			gbc_lblTeam.gridx = 0;
			gbc_lblTeam.gridy = 0;
			panelMain.add(lblTeam, gbc_lblTeam);
		}
		{
			txtFieldTeam = new JTextField();
			txtFieldTeam.addFocusListener(new TxtFieldFocusAdapter());
			txtFieldTeam.setEditable(false);
			defaultBorder = txtFieldTeam.getBorder();
			{
				JPopupMenu popupMenuTeam = new JPopupMenu();
				for(TeamName teamName : TeamName.values()){
					JTextField menuItem = new JTextField(teamName.toString());
					menuItem.setBackground(Color.WHITE);
					menuItem.setEditable(false);
					menuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							JTextField field = (JTextField) e.getSource();
							field.setBackground(Color.LIGHT_GRAY);
						}
						
						@Override
						public void mouseExited(MouseEvent e) {
							JTextField field = (JTextField) e.getSource();
							field.setBackground(Color.WHITE);
						}
						
						@Override
						public void mouseReleased(MouseEvent e){
							JTextField field = (JTextField) e.getSource();
							TeamName teamName = TeamName.valueOf(field.getText().toUpperCase());
							txtFieldTeam.setText(teamName.value() + " " + teamName.toString());
							field.getParent().setVisible(false);
						}
					});
					popupMenuTeam.add(menuItem);
				}
				addPopup(txtFieldTeam, popupMenuTeam);
			}
			GridBagConstraints gbc_txtFieldTeam = new GridBagConstraints();
			gbc_txtFieldTeam.insets = new Insets(0, 0, 5, 0);
			gbc_txtFieldTeam.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFieldTeam.gridx = 1;
			gbc_txtFieldTeam.gridy = 0;
			panelMain.add(txtFieldTeam, gbc_txtFieldTeam);
			txtFieldTeam.setColumns(10);
		}
		{
			JLabel lblNumber = new JLabel("Number");
			GridBagConstraints gbc_lblNumber = new GridBagConstraints();
			gbc_lblNumber.anchor = GridBagConstraints.EAST;
			gbc_lblNumber.insets = new Insets(0, 0, 5, 5);
			gbc_lblNumber.gridx = 0;
			gbc_lblNumber.gridy = 1;
			panelMain.add(lblNumber, gbc_lblNumber);
		}
		{
			txtFieldNumber = new JTextField();
			txtFieldNumber.addFocusListener(new TxtFieldFocusAdapter());
			GridBagConstraints gbc_txtFieldNumber = new GridBagConstraints();
			gbc_txtFieldNumber.insets = new Insets(0, 0, 5, 0);
			gbc_txtFieldNumber.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFieldNumber.gridx = 1;
			gbc_txtFieldNumber.gridy = 1;
			panelMain.add(txtFieldNumber, gbc_txtFieldNumber);
			txtFieldNumber.setColumns(10);
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
			gbc_lblFirstName.anchor = GridBagConstraints.EAST;
			gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
			gbc_lblFirstName.gridx = 0;
			gbc_lblFirstName.gridy = 2;
			panelMain.add(lblFirstName, gbc_lblFirstName);
		}
		{
			txtFieldFirstName = new JTextField();
			txtFieldFirstName.addFocusListener(new TxtFieldFocusAdapter());
			GridBagConstraints gbc_txtFieldFirstName = new GridBagConstraints();
			gbc_txtFieldFirstName.insets = new Insets(0, 0, 5, 0);
			gbc_txtFieldFirstName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFieldFirstName.gridx = 1;
			gbc_txtFieldFirstName.gridy = 2;
			panelMain.add(txtFieldFirstName, gbc_txtFieldFirstName);
			txtFieldFirstName.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			GridBagConstraints gbc_lblLastName = new GridBagConstraints();
			gbc_lblLastName.anchor = GridBagConstraints.EAST;
			gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
			gbc_lblLastName.gridx = 0;
			gbc_lblLastName.gridy = 3;
			panelMain.add(lblLastName, gbc_lblLastName);
		}
		{
			txtFieldLastName = new JTextField();
			txtFieldLastName.addFocusListener(new TxtFieldFocusAdapter());
			GridBagConstraints gbc_txtFieldLastName = new GridBagConstraints();
			gbc_txtFieldLastName.insets = new Insets(0, 0, 5, 0);
			gbc_txtFieldLastName.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFieldLastName.gridx = 1;
			gbc_txtFieldLastName.gridy = 3;
			panelMain.add(txtFieldLastName, gbc_txtFieldLastName);
			txtFieldLastName.setColumns(10);
		}
		{
			JLabel lblPosition = new JLabel("Position");
			GridBagConstraints gbc_lblPosition = new GridBagConstraints();
			gbc_lblPosition.insets = new Insets(0, 0, 0, 5);
			gbc_lblPosition.anchor = GridBagConstraints.EAST;
			gbc_lblPosition.gridx = 0;
			gbc_lblPosition.gridy = 4;
			panelMain.add(lblPosition, gbc_lblPosition);
		}
		{
			txtFieldPosition = new JTextField();
			txtFieldPosition.addFocusListener(new TxtFieldFocusAdapter());
			txtFieldPosition.setEditable(false);
			{
				JPopupMenu popupMenuPosition = new JPopupMenu();
				for(Position position : Position.values()){
					JTextField menuItem = new JTextField(position.toString());
					menuItem.setBackground(Color.WHITE);
					menuItem.setEditable(false);
					menuItem.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							JTextField field = (JTextField) e.getSource();
							field.setBackground(Color.LIGHT_GRAY);
						}
						
						@Override
						public void mouseExited(MouseEvent e) {
							JTextField field = (JTextField) e.getSource();
							field.setBackground(Color.WHITE);
						}
						
						@Override
						public void mouseReleased(MouseEvent e){
							JTextField field = (JTextField) e.getSource();
							txtFieldPosition.setText(field.getText());
							field.getParent().setVisible(false);
						}
					});
					popupMenuPosition.add(menuItem);
				}
				addPopup(txtFieldPosition, popupMenuPosition);
			}
			GridBagConstraints gbc_txtFieldPosition = new GridBagConstraints();
			gbc_txtFieldPosition.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtFieldPosition.gridx = 1;
			gbc_txtFieldPosition.gridy = 4;
			panelMain.add(txtFieldPosition, gbc_txtFieldPosition);
			txtFieldPosition.setColumns(10);
		}
		{
			JPanel panelControls = new JPanel();
			panelControls.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(panelControls, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(fieldsAreValid()){
							if(player != null){
								if(!player.equals(editedPlayer)){
									firePropertyChange("playerEdited", getChanges(player, editedPlayer), editedPlayer);
								}
							}else{
								String[] changes = {"active", "TRUE"};
								firePropertyChange("playerEdited", changes, editedPlayer);
							}
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				panelControls.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				panelControls.add(cancelButton);
			}
		}
	}
	
	private void setFields(){
		if(player != null){
			txtFieldTeam.setText(player.getFranchise().getProvince() + " " + player.getFranchise().getTeamName());
			txtFieldNumber.setText(String.valueOf(player.getNumber()));
			txtFieldFirstName.setText(player.getFirstName());
			txtFieldLastName.setText(player.getLastName());
			txtFieldPosition.setText(player.getPosition().toString());
		}else{
			txtFieldTeam.setText("Select a Team");
			txtFieldPosition.setText("Select a Position");
		}
	}
	
	private boolean fieldsAreValid(){
		boolean result = true;
		String firstName = "";
		String lastName = "";
		TeamName teamName = null;
		Position position = null;
		int number = 0;
		try{
			String[] strings = txtFieldTeam.getText().split("\\s");
			teamName = TeamName.valueOf(strings[1].toUpperCase());
		}catch(IllegalArgumentException e){
			result = false;
			txtFieldTeam.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			txtFieldTeam.setForeground(Color.RED);
		}
		
		try{
			number = Integer.parseInt(txtFieldNumber.getText());
			if(number < 1 || number > 99){
				throw new NumberFormatException("Number is outside of the bounds for a player number");
			}
			
		}catch(NumberFormatException e){
			result = false;
			txtFieldNumber.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			txtFieldNumber.setForeground(Color.RED);
			txtFieldNumber.setText("Enter a number 1-99");
		}
		
		try{
			if(txtFieldFirstName.getText().isEmpty()){
				throw new IllegalArgumentException("This field can't be empty");
			}
			firstName = formatString(txtFieldFirstName.getText());
		}catch(IllegalArgumentException e){
			result = false;
			txtFieldFirstName.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			txtFieldFirstName.setForeground(Color.RED);
			txtFieldFirstName.setText(e.getMessage());
		}
		
		try{
			if(txtFieldLastName.getText().isEmpty()){
				throw new IllegalArgumentException("This field can't be empty");
			}
			lastName = formatString(txtFieldLastName.getText());
		}catch(IllegalArgumentException e){
			result = false;
			txtFieldLastName.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			txtFieldLastName.setForeground(Color.RED);
			txtFieldLastName.setText(e.getMessage());
		}
		
		try{
			position = Position.valueOf(txtFieldPosition.getText());
		}catch(IllegalArgumentException e){
			result = false;
			txtFieldPosition.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			txtFieldPosition.setForeground(Color.RED);
		}
		if(result){
			if(player != null){
				editedPlayer = new Player(player.getId(), firstName, lastName, teamName, position, number, true);
			}else{
				editedPlayer = new Player(firstName, lastName, teamName, position, number);
			}
		}
		return result;
	}

	private class TxtFieldFocusAdapter extends FocusAdapter{
		@Override
		public void focusGained(FocusEvent e) {
			JTextField field = (JTextField)e.getSource();
			if(field.getForeground() == Color.RED){
				field.setText("");
				field.setForeground(Color.BLACK);
				field.setBorder(defaultBorder);
			}
		}
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showMenu(e);
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	private String formatString(String s){
		String[] strings = s.split("\\W");
		
		for(int i = 0; i < strings.length; i++){
			if(strings[i].length() > 0){
				StringBuilder sb = new StringBuilder(strings[i].toLowerCase());
				sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
				strings[i] = sb.toString();
			}else{
				StringBuilder sb = new StringBuilder();
				for(int j = 0; j < strings.length; j++){
					if(i == j){
						continue;
					}
					sb.append(strings[j] + " ");
				}
				return formatString(sb.toString());
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for(String string : strings){
			sb.append(string + " ");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	private String[] getChanges(Player original, Player edited){
		List<String> changes = new ArrayList<String>();
		if(!original.getFirstName().equals(edited.getFirstName())){
			changes.add("firstName");
			changes.add(edited.getFirstName());
		}
		if(!original.getLastName().equals(edited.getLastName())){
			changes.add("lastName");
			changes.add(edited.getLastName());
		}
		if(!original.getFranchise().getTeamName().equals(edited.getFranchise().getTeamName())){
			changes.add("team");
			changes.add(edited.getFranchise().getTeamName());
		}
		if(!original.getPosition().equals(edited.getPosition())){
			changes.add("position");
			changes.add(edited.getPosition().toString());
		}
		if(!(original.getNumber() == edited.getNumber())){
			changes.add("number");
			changes.add(String.valueOf(edited.getNumber()));
		}
		return changes.toArray(new String[changes.size()]);
	}
}