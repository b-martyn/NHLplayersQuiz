package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ScrollSuggestions {
	private HashMap<Integer[], String[]> cellsToHighlight;
	private ListOfRows<Suggestion> suggestions;
	private ListOfRows<Player> team;
	private JPanelScrollWithTableAndButtons panelMain;
	private JTable tableMain;
	private JLabel lblHeader;

	ScrollSuggestions(ListOfRows<Player> players, ListOfRows<Suggestion> suggestions) {
		super();
		this.suggestions = suggestions;
		this.team = players;
		this.panelMain = new JPanelScrollWithTableAndButtons();
		this.tableMain = panelMain.getTable();
		this.lblHeader = panelMain.getHeader();
		initialize();
	}

	JPanelScrollWithTableAndButtons getMainPanel() {
		return panelMain;
	}

	private void initialize() {
		TableModel model = setTableModel();
		panelMain.setTableModel(model);
		highlightFields();
		setColumnRenderers();
		lblHeader.setText("Approve Changes to Players");
	}

	private TableModel setTableModel() {
		String[] headerRow = { "id", "franchise", "suggestionIds", "Number",
				"First Name", "Last Name", "Position" };
		return new DefaultTableModel(convertData(team.getList()), headerRow) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private void setColumnRenderers() {
		for (int i = 0; i < 3; i++) {
			tableMain.getColumnModel().getColumn(i).setMinWidth(0);
			tableMain.getColumnModel().getColumn(i).setMaxWidth(0);
		}

		tableMain.getColumnModel().getColumn(3)
				.setCellRenderer(new JDefaultTableCellRenderer());
		tableMain.getColumnModel().getColumn(3)
				.setMinWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		tableMain.getColumnModel().getColumn(3)
				.setMaxWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);

		for (int i = 4; i < tableMain.getColumnCount(); i++) {
			TableColumn column = tableMain
					.getColumn(tableMain.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer(
					cellsToHighlight));
		}
	}

	private void highlightFields() {
		HashMap<Integer[], String[]> highlightCellMap = new HashMap<Integer[], String[]>();
		suggestionLoop: for (Suggestion suggestion : suggestions.getList()) {
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
				cell[1] = 2;
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
		cellsToHighlight = highlightCellMap;
	}

	private int getCellMatch(int playerId) {
		int row = -1;

		for (int i = 0; i < tableMain.getRowCount(); i++) {
			if ((int) tableMain.getValueAt(i, 0) == playerId) {
				row = i;
			}
		}

		return row;
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
			dataHolder.add(new Object[] { id, franchise, "", number, firstName,
					lastName, position });
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}

	private static class JDefaultTableCellRenderer extends
			DefaultTableCellRenderer {
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
		public Component getTableCellRendererComponent(JTable table,
				Object object, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component component = super.getTableCellRendererComponent(table,
					object, isSelected, hasFocus, row, column);

			component.setFont(new Font("Serif", Font.PLAIN, 20));
			int franchiseRow = 0;
			for (int i = 0; i < table.getColumnCount(); i++) {
				if (table.getColumnName(i).equalsIgnoreCase("franchise")) {
					franchiseRow = i;
				}
			}
			Franchise rowFranchise = (Franchise) table.getValueAt(row,
					franchiseRow);

			if (cellsToHighlight.size() != 0) {
				int suggestionIdsRow = 0;
				for (int i = 0; i < table.getColumnCount(); i++) {
					if (table.getColumnName(i)
							.equalsIgnoreCase("suggestionIds")) {
						suggestionIdsRow = i;
					}
				}

				Iterator it = cellsToHighlight.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Integer[] cell = (Integer[]) entry.getKey();
					String[] value_id = (String[]) entry.getValue();
					if (cell[0] == row && cell[1] == column) {

						table.setValueAt(value_id[1], row, suggestionIdsRow);
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
								int[] colors = { color.getRed(),
										color.getGreen(), color.getBlue() };
								for (int i = 0; i < colors.length; i++) {
									if (colors[i] > 127) {
										colors[i] -= 100;
									} else {
										colors[i] += 100;
									}
								}
								component.setBackground(new Color(colors[0],
										colors[1], colors[2]));
							} else {
								component.setBackground(rowFranchise
										.getMainColor());
							}
							component
									.setForeground(rowFranchise.getBaseColor());
						}
						return component;
					}
				}
			}
			if (isSelected) {
				Color color = rowFranchise.getBaseColor();
				int[] colors = { color.getRed(), color.getGreen(),
						color.getBlue() };
				for (int i = 0; i < colors.length; i++) {
					if (colors[i] > 127) {
						colors[i] -= 100;
					} else {
						colors[i] += 100;
					}
				}
				component.setBackground(new Color(colors[0], colors[1],
						colors[2]));
			} else {
				component.setBackground(rowFranchise.getBaseColor());
			}
			component.setForeground(rowFranchise.getMainColor());
			return component;
		}
	}
}
