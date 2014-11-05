/*
 * +++++++++++++++++Table Without Suggestions+++++++++++++++++++
 * +----+-----------+--------+-----------+----------+----------+
 * | id | franchise | number | firstName | lastName | position |
 * +----+-----------+--------+-----------+----------+----------+
 * |int | Franchise | String |   String  |  String  |  String  |
 * +----+-----------+--------+-----------+----------+----------+
 * 
 * +++++++++++++++++++++++++Table With Suggestions++++++++++++++++++++++++++
 * +----+-----------+---------------+--------+---------+--------+----------+
 * | id | franchise | suggestionIds | number |firstName|lastName| position |
 * +----+-----------+---------------+--------+---------+--------+----------+
 * |int | Franchise |    String     | String |  String | String |  String  |
 * +----+-----------+---------------+--------+---------+--------+----------+
 */

package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ScrollPaneFactory {
	private ListOfRows<Suggestion> suggestions;
	private ListOfRows<Player> team;
	private JPanel scrollPanel;
	private JTable table;
	
	//1 for just Table in scroll pane, 2 for buttons and Table in scrollPane
	private int tableNumber;
	
	public ScrollPaneFactory(ListOfRows<Player> team){
		this(team, new ListOfRows<Suggestion>(new ArrayList<Suggestion>()));
		this.tableNumber = 1;
	}
	
	public ScrollPaneFactory(ListOfRows<Player> team, ListOfRows<Suggestion> suggestions){
		super();
		this.team = team;
		this.suggestions = suggestions;
		this.tableNumber = 2;
	}
	
	public JPanel getScrollPanel(){
		return createScrollPanel();
	}
	
	private JPanel createScrollPanel(){
		scrollPanel = new JPanel();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		scrollPanel.setLayout(gridBagLayout);

		JPanel headerPanel = createHeaderPanel();
		GridBagConstraints gbc_headerPanel = new GridBagConstraints();
		gbc_headerPanel.anchor = GridBagConstraints.NORTH;
		gbc_headerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_headerPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_headerPanel.gridx = 0;
		gbc_headerPanel.gridy = 0;
		scrollPanel.add(headerPanel, gbc_headerPanel);

		JScrollPane scrollPane = createScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		scrollPanel.add(scrollPane, gbc_scrollPane);
		
		return scrollPanel;
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		GridBagLayout gbl_headerPanel = new GridBagLayout();
		gbl_headerPanel.columnWidths = new int[] { 0, 0 };
		gbl_headerPanel.rowHeights = new int[] { 0, 0 };
		gbl_headerPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_headerPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		headerPanel.setLayout(gbl_headerPanel);
		
		JLabel headerLabel = new JLabel("This is the Header Label");
		headerLabel.setFont(new Font("Tahoma", Font.PLAIN, 55));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_headerLabel = new GridBagConstraints();
		gbc_headerLabel.gridx = 0;
		gbc_headerLabel.gridy = 0;
		headerPanel.add(headerLabel, gbc_headerLabel);
		
		return headerPanel;
	}

	private JScrollPane createScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel scrollPaneView = new JPanel();
		table = createTable();
		
		int columnCount;
		if(tableNumber == 2){
			columnCount = 3;
			
		}else{
			columnCount = 1;
		}
		GridBagLayout gbl_scrollPaneView = new GridBagLayout();
		int[] columnWidths = new int[columnCount];
		double[] columnWeights = new double[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnWidths[i] = 0;
			if(i == 0){
				columnWeights[i] = 1.0;
			}else{
				columnWeights[i] = 0.0;
			}
		}
		gbl_scrollPaneView.columnWidths = columnWidths;
		gbl_scrollPaneView.columnWeights = columnWeights;
		int[] rowHeights = new int[table.getRowCount()];
		double[] rowWeights = new double[table.getRowCount()];
		for (int i = 0; i < table.getRowCount(); i++) {
			rowHeights[i] = table.getRowHeight();
			rowWeights[i] = 0.0;
		}
		gbl_scrollPaneView.rowHeights = rowHeights;
		gbl_scrollPaneView.rowWeights = rowWeights;
		scrollPaneView.setLayout(gbl_scrollPaneView);
		
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridheight = table.getRowCount();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 0;
		scrollPaneView.add(table, gbc_table);
		
		if(columnCount == 3){
			for (int i = 0; i < table.getRowCount(); i++) {

				Franchise franchise = (Franchise) table.getValueAt(i, 1);

				JButton btnGood = new JButton("\u2714");
				btnGood.setName("" + i);
				btnGood.setBackground(franchise.getBaseColor());
				btnGood.setForeground(Color.GREEN);
				btnGood.setContentAreaFilled(false);
				btnGood.setOpaque(true);
				GridBagConstraints gbc_btnGood = new GridBagConstraints();
				gbc_btnGood.fill = GridBagConstraints.BOTH;
				gbc_btnGood.gridx = 1;
				gbc_btnGood.gridy = i;
				btnGood.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton) e.getSource();
						String[] strings = ((String) table.getValueAt(Integer.parseInt(button.getName()), 2)).split("|");
						System.out.println(strings[0]);
						for (int i = 0; i < strings.length; i++) {
							if (!strings[i].isEmpty()) {
								int suggestionId = Integer.parseInt(strings[i]);
								if(suggestionId == 1){
									scrollPanel.firePropertyChange("vote", 1, 0);
								}
								else if(suggestionId > 1){
									scrollPanel.firePropertyChange("vote", 1, suggestionId);
								}
							}
						}
					}
				});

				JButton btnBad = new JButton("\u2716");
				btnBad.setName("" + i);
				btnBad.setBackground(franchise.getBaseColor());
				btnBad.setForeground(Color.RED);
				btnBad.setContentAreaFilled(false);
				btnBad.setOpaque(true);
				GridBagConstraints gbc_btnBad = new GridBagConstraints();
				gbc_btnBad.fill = GridBagConstraints.BOTH;
				gbc_btnBad.gridx = 2;
				gbc_btnBad.gridy = i;
				btnBad.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton) e.getSource();
						String[] strings = ((String) table.getValueAt(Integer.parseInt(button.getName()), 2)).split("|");
						for (int i = 0; i < strings.length; i++) {
							if (!strings[i].isEmpty()) {
								int suggestionId = Integer.parseInt(strings[i]);
								if(suggestionId > 0){
									scrollPanel.firePropertyChange("vote", -1, suggestionId);
								}
							}
						}
					}
				});

				scrollPaneView.add(btnGood, gbc_btnGood);
				scrollPaneView.add(btnBad, gbc_btnBad);
			}
		}
		
		scrollPane.setViewportView(scrollPaneView);
		return scrollPane;
	}

	private JTable createTable() {
		table = new JTable(){
			@Override
			public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend){
				super.changeSelection(rowIndex, columnIndex, true, extend);
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (table.getSelectedRow() >= 0) {
					int playerId = (int) table.getValueAt(table.getSelectedRow(), 0);
					scrollPanel.firePropertyChange("playerChange", -1, playerId);
				}
			}
		});
		
		// Add pop-up menu to row with the suggested change only on table with suggestions
		if(tableNumber == 2){
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());
					if(e.getButton() == 3){
						if(!source.isRowSelected(row)){
							source.changeSelection(row, column, true, false);
						}
						JDialog popupPanel = createDialog(team.getRow((int)source.getValueAt(row, 0)), suggestions.getRow(Integer.valueOf((String)source.getValueAt(row, 2))));
						popupPanel.setLocation(source.getLocationOnScreen().x, source.getLocationOnScreen().y + (source.getRowHeight() * (source.getSelectedRow() + 1)));
						popupPanel.setVisible(true);
					}
				}
			});
		}
		table.setRowHeight(50);
		table.setModel(getTableModel());
		setColumnRenderers();
		
		return table;
	}
	


	private JDialog createDialog(Player player, Suggestion suggestion) {
		JDialog dialog = new JDialog();
		JPanel contentPanel = new JPanel();
		Color baseColor = player.getFranchise().getBaseColor();
		Color mainColor = player.getFranchise().getMainColor();
		Color secondaryColor = player.getFranchise().getSecondaryColor();
		dialog.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				dialog.dispose();
			}
		});
		dialog.setUndecorated(true);
		dialog.setBounds(100, 100, 450, 73);
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(baseColor);
		contentPanel.setBorder(new LineBorder(secondaryColor, 3));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setForeground(mainColor);
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 1;
		gbc_lblFirstName.gridy = 0;
		contentPanel.add(lblFirstName, gbc_lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(mainColor);
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 2;
		gbc_lblLastName.gridy = 0;
		contentPanel.add(lblLastName, gbc_lblLastName);
		
		JLabel lblPosition = new JLabel("Position");
		lblPosition.setForeground(mainColor);
		GridBagConstraints gbc_lblPosition = new GridBagConstraints();
		gbc_lblPosition.insets = new Insets(0, 0, 5, 5);
		gbc_lblPosition.gridx = 3;
		gbc_lblPosition.gridy = 0;
		contentPanel.add(lblPosition, gbc_lblPosition);
		
		JLabel lblNumber = new JLabel("Number");
		lblNumber.setForeground(mainColor);
		GridBagConstraints gbc_lblNumber = new GridBagConstraints();
		gbc_lblNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumber.gridx = 4;
		gbc_lblNumber.gridy = 0;
		contentPanel.add(lblNumber, gbc_lblNumber);
		
		JLabel lblTeam = new JLabel("Team");
		lblTeam.setForeground(mainColor);
		GridBagConstraints gbc_lblTeam = new GridBagConstraints();
		gbc_lblTeam.insets = new Insets(0, 0, 5, 0);
		gbc_lblTeam.gridx = 5;
		gbc_lblTeam.gridy = 0;
		contentPanel.add(lblTeam, gbc_lblTeam);
		
		JLabel lblCurrent = new JLabel("Current");
		lblCurrent.setForeground(mainColor);
		GridBagConstraints gbc_lblCurrent = new GridBagConstraints();
		gbc_lblCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrent.gridx = 0;
		gbc_lblCurrent.gridy = 1;
		contentPanel.add(lblCurrent, gbc_lblCurrent);
		
		JLabel lblCurrentFirstName = new JLabel(player.getFirstName());
		lblCurrentFirstName.setForeground(secondaryColor);
		GridBagConstraints gbc_lblCurrentFirstName = new GridBagConstraints();
		gbc_lblCurrentFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentFirstName.gridx = 1;
		gbc_lblCurrentFirstName.gridy = 1;
		contentPanel.add(lblCurrentFirstName, gbc_lblCurrentFirstName);
		
		JLabel lblCurrentLastName = new JLabel(player.getLastName());
		lblCurrentLastName.setForeground(secondaryColor);
		GridBagConstraints gbc_lblCurrentLastName = new GridBagConstraints();
		gbc_lblCurrentLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentLastName.gridx = 2;
		gbc_lblCurrentLastName.gridy = 1;
		contentPanel.add(lblCurrentLastName, gbc_lblCurrentLastName);
		
		JLabel lblCurrentPosition = new JLabel(player.getPosition().toString());
		lblCurrentPosition.setForeground(secondaryColor);
		GridBagConstraints gbc_lblCurrentPosition = new GridBagConstraints();
		gbc_lblCurrentPosition.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentPosition.gridx = 3;
		gbc_lblCurrentPosition.gridy = 1;
		contentPanel.add(lblCurrentPosition, gbc_lblCurrentPosition);
		
		JLabel lblCurrentNumber = new JLabel(String.valueOf(player.getNumber()));
		lblCurrentNumber.setForeground(secondaryColor);
		GridBagConstraints gbc_lblCurrentNumber = new GridBagConstraints();
		gbc_lblCurrentNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentNumber.gridx = 4;
		gbc_lblCurrentNumber.gridy = 1;
		contentPanel.add(lblCurrentNumber, gbc_lblCurrentNumber);
		
		JLabel lblCurrentTeam = new JLabel(player.getFranchise().getTeamName());
		lblCurrentTeam.setForeground(secondaryColor);
		GridBagConstraints gbc_lblCurrentTeam = new GridBagConstraints();
		gbc_lblCurrentTeam.insets = new Insets(0, 0, 5, 0);
		gbc_lblCurrentTeam.gridx = 5;
		gbc_lblCurrentTeam.gridy = 1;
		contentPanel.add(lblCurrentTeam, gbc_lblCurrentTeam);
	
		JLabel lblChange = new JLabel("Change");
		lblChange.setForeground(mainColor);
		GridBagConstraints gbc_lblChange = new GridBagConstraints();
		gbc_lblChange.insets = new Insets(0, 0, 0, 5);
		gbc_lblChange.gridx = 0;
		gbc_lblChange.gridy = 2;
		contentPanel.add(lblChange, gbc_lblChange);
		
		JLabel lblNewFirstName = new JLabel("");
		lblNewFirstName.setForeground(secondaryColor);
		GridBagConstraints gbc_lblNewFirstName = new GridBagConstraints();
		gbc_lblNewFirstName.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewFirstName.gridx = 1;
		gbc_lblNewFirstName.gridy = 2;
		contentPanel.add(lblNewFirstName, gbc_lblNewFirstName);
		
		JLabel lblNewLastName = new JLabel("");
		lblNewLastName.setForeground(secondaryColor);
		GridBagConstraints gbc_lblNewLastName = new GridBagConstraints();
		gbc_lblNewLastName.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLastName.gridx = 2;
		gbc_lblNewLastName.gridy = 2;
		contentPanel.add(lblNewLastName, gbc_lblNewLastName);
		
		JLabel lblNewPosition = new JLabel("");
		lblNewPosition.setForeground(secondaryColor);
		GridBagConstraints gbc_lblNewPosition = new GridBagConstraints();
		gbc_lblNewPosition.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewPosition.gridx = 3;
		gbc_lblNewPosition.gridy = 2;
		contentPanel.add(lblNewPosition, gbc_lblNewPosition);
		
		JLabel lblNewNumber = new JLabel("");
		lblNewNumber.setForeground(secondaryColor);
		GridBagConstraints gbc_lblNewNumber = new GridBagConstraints();
		gbc_lblNewNumber.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewNumber.gridx = 4;
		gbc_lblNewNumber.gridy = 2;
		contentPanel.add(lblNewNumber, gbc_lblNewNumber);
	
		JLabel lblNewTeam = new JLabel("");
		lblNewTeam.setForeground(secondaryColor);
		GridBagConstraints gbc_lblNewTeam = new GridBagConstraints();
		gbc_lblNewTeam.gridx = 5;
		gbc_lblNewTeam.gridy = 2;
		contentPanel.add(lblNewTeam, gbc_lblNewTeam);
		
		switch(suggestion.getField()){
			case active:
				lblNewFirstName.setText("New");
				lblNewLastName.setText("Player");
				lblNewTeam.setText("New");
				lblNewPosition.setText("Player");
				lblNewNumber.setText("New");
				break;
			case firstName:
				lblNewFirstName.setText(suggestion.getValue());
				break;
			case lastName:
				lblNewLastName.setText(suggestion.getValue());
				break;
			case team:
				lblNewTeam.setText(suggestion.getValue());
				break;
			case position:
				lblNewPosition.setText(suggestion.getValue());
				break;
			case number:
				lblNewNumber.setText(suggestion.getValue());
				break;
			default:
				break;
		}
		
		return dialog;
	}

	private TableModel getTableModel() {
		String[] headerRow;
		if(tableNumber == 1){
			headerRow = new String[]{ "id", "franchise", "Number", "First Name", "Last Name", "Position" };
		}else if(tableNumber == 2){
			headerRow = new String[]{ "id", "franchise", "suggestionIds", "Number", "First Name", "Last Name", "Position" };
		}else{
			headerRow = new String[]{""};
		}
		return new DefaultTableModel(convertData(team.getList()), headerRow) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}
	
	private Object[][] convertData(List<Player> players) {
		List<Object[]> dataHolder = new ArrayList<Object[]>();
		for (Player player : players) {
			int id = player.getId();
			Franchise franchise = player.getFranchise();
			String number = String.valueOf(player.getNumber());
			String firstName = player.getFirstName();
			String lastName = player.getLastName();
			String position = player.getPosition().toString();
			if(tableNumber == 1){
				dataHolder.add(new Object[] { id, franchise, number, firstName,	lastName, position });
			}else if(tableNumber == 2){
				dataHolder.add(new Object[] { id, franchise, "", number, firstName,	lastName, position });
			}
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}

	private void setColumnRenderers() {
		int i = 0;
		int hiddenColumns = 0;
		HashMap<Integer[], String[]> highlightCellMap = new HashMap<Integer[], String[]>();
		if(tableNumber == 2){
			hiddenColumns = 3;
			highlightCellMap = setFieldsToHighlight();
		}else{
			hiddenColumns = 2;
		}
		// Hide first 2 or 3 columns: id, franchise (suggestionId)
		for (; i < hiddenColumns; i++) {
			table.getColumnModel().getColumn(i).setMinWidth(0);
			table.getColumnModel().getColumn(i).setMaxWidth(0);
		}
		// Number column
		table.getColumnModel().getColumn(i).setCellRenderer(new JDefaultTableCellRenderer(highlightCellMap));
		table.getColumnModel().getColumn(i).setMinWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		table.getColumnModel().getColumn(i).setMaxWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		// First/Last name and position columns
		for (++i; i < table.getColumnCount(); i++) {
			TableColumn column = table.getColumn(table.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer(highlightCellMap));
		}
	}

	private HashMap<Integer[], String[]> setFieldsToHighlight() {
		HashMap<Integer[], String[]> highlightCellMap = new HashMap<Integer[], String[]>();
		suggestionLoop:
		for (Suggestion suggestion : suggestions.getList()) {
			int row = getCellMatch(suggestion.getPlayerId());
			if (row == -1) {
				continue suggestionLoop;
			}
			Integer[] cell = new Integer[2];
			cell[0] = getCellMatch(suggestion.getPlayerId());
			String[] suggestionValue_Id = new String[2];
			suggestionValue_Id[0] = suggestion.getValue();
			suggestionValue_Id[1] = "" + suggestion.getId();
			switch (suggestion.getField()) {
				case active:
					if (suggestion.getValue().equals("TRUE")) {
						suggestionValue_Id[0] = "new";
					} else {
						suggestionValue_Id[0] = "delete";
					}
					for (int i = 4; i < 7; i++) {
						Integer[] newCell = new Integer[2];
						newCell[0] = getCellMatch(suggestion.getPlayerId());
						newCell[1] = i;
						highlightCellMap.put(newCell, suggestionValue_Id);
					}
					continue suggestionLoop;
				case firstName:
					cell[1] = 4;
					break;
				case lastName:
					cell[1] = 5;
					break;
				case team:
					cell[1] = 1;
					break;
				case position:
					cell[1] = 6;
					break;
				case number:
					cell[1] = 3;
					break;
			}
			highlightCellMap.put(cell, suggestionValue_Id);
		}
		return highlightCellMap;
	}
	
	private int getCellMatch(int playerId) {
		int row = -1;
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((int) table.getValueAt(i, 0) == playerId) {
				row = i;
			}
		}
		return row;
	}
	
	private static class JDefaultTableCellRenderer extends	DefaultTableCellRenderer {
		static final Dimension NUMBER_CELL_SIZE = new Dimension(50, 50);
		private HashMap<Integer[], String[]> cellsToHighlight;
	
		JDefaultTableCellRenderer() {
			this(new HashMap<Integer[], String[]>());
		}
		
		JDefaultTableCellRenderer(HashMap<Integer[], String[]> cellsToHighlight) {
			this.cellsToHighlight = cellsToHighlight;
			this.setHorizontalAlignment(CENTER);
		}
	
		@Override
		public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
			component.setFont(new Font("Serif", Font.PLAIN, 20));
			int franchiseRow = 0;
			for (int i = 0; i < table.getColumnCount(); i++) {
				if (table.getColumnName(i).equalsIgnoreCase("franchise")) {
					franchiseRow = i;
				}
			}
			Franchise rowFranchise = (Franchise) table.getValueAt(row, franchiseRow);
			if (cellsToHighlight.size() != 0) {
				int suggestionIdsColumn = 0;
				for (int i = 0; i < table.getColumnCount(); i++) {
					if (table.getColumnName(i).equalsIgnoreCase("suggestionIds")) {
						suggestionIdsColumn = i;
					}
				}
				Iterator it = cellsToHighlight.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Integer[] cell = (Integer[]) entry.getKey();
					String[] value_id = (String[]) entry.getValue();
					if (cell[0] == row && cell[1] == column) {
						table.setValueAt(value_id[1], row, suggestionIdsColumn);
						if (value_id[0].equals("new")) {
							if (isSelected) {
								component.setBackground(new Color(50, 205, 50));
							} else {
								component.setBackground(Color.GREEN);
							}
							component.setForeground(Color.WHITE);
						} else if (value_id[0].equals("delete")) {
							if (isSelected) {
								component.setBackground(new Color(205, 50, 50));
							} else {
								component.setBackground(Color.RED);
							}
							component.setForeground(Color.WHITE);
						} else {
							setText(value_id[0]);
							if (isSelected) {
								Color color = rowFranchise.getMainColor();
								int[] colors = { color.getRed(), color.getGreen(), color.getBlue() };
								for (int i = 0; i < colors.length; i++) {
									if (colors[i] > 127) {
										colors[i] -= 100;
									} else {
										colors[i] += 100;
									}
								}
								component.setBackground(new Color(colors[0], colors[1], colors[2]));
							} else {
								component.setBackground(rowFranchise.getMainColor());
							}
							component.setForeground(rowFranchise.getBaseColor());
						}
						return component;
					}
				}
			}
			if (isSelected) {
				Color color = rowFranchise.getBaseColor();
				int[] colors = { color.getRed(), color.getGreen(), color.getBlue() };
				for (int i = 0; i < colors.length; i++) {
					if (colors[i] > 127) {
						colors[i] -= 100;
					} else {
						colors[i] += 100;
					}
				}
				component.setBackground(new Color(colors[0], colors[1],	colors[2]));
			} else {
				component.setBackground(rowFranchise.getBaseColor());
			}
			component.setForeground(rowFranchise.getMainColor());
			return component;
		}
	}
}
