package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ScrollTeam {
	private ListOfRows<Player> team;
	private JPanelScrollWithTable panelMain;
	private JTable tableMain;
	private JLabel lblHeader;

	ScrollTeam(ListOfRows<Player> team) {
		super();
		this.team = team;
		this.panelMain = new JPanelScrollWithTable();
		this.tableMain = panelMain.getTable();
		this.lblHeader = panelMain.getHeader();
		initialize();
	}

	JPanelScrollWithTable getMainPanel() {
		return panelMain;
	}

	private void initialize() {
		TableModel model = setTableData();
		panelMain.setTableModel(model);
		setColumnRenderers();
		lblHeader.setText(team.getRandomRow().getFranchise().getProvince()
				+ " " + team.getRandomRow().getFranchise().getTeamName());
	}

	private TableModel setTableData() {
		String[] headerRow = { "id", "franchise", "Number", "First Name",
				"Last Name", "Position" };
		List<Player> activePlayers = new ArrayList<Player>();
		for (Player player : team.getList()) {
			if (player.isActive()) {
				activePlayers.add(player);
			}
		}
		return new DefaultTableModel(convertData(activePlayers), headerRow) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private void setColumnRenderers() {
		for (int i = 0; i < 2; i++) {
			tableMain.getColumnModel().getColumn(i).setMinWidth(0);
			tableMain.getColumnModel().getColumn(i).setMaxWidth(0);
		}

		tableMain.getColumnModel().getColumn(2)
				.setCellRenderer(new JDefaultTableCellRenderer());
		tableMain.getColumnModel().getColumn(2)
				.setMinWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);
		tableMain.getColumnModel().getColumn(2)
				.setMaxWidth(JDefaultTableCellRenderer.NUMBER_CELL_SIZE.width);

		for (int i = 3; i < tableMain.getColumnCount(); i++) {
			TableColumn column = tableMain
					.getColumn(tableMain.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer());
		}
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
			dataHolder.add(new Object[] { id, franchise, number, firstName,
					lastName, position });
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}

	private static class JDefaultTableCellRenderer extends
			DefaultTableCellRenderer {
		final static Dimension NUMBER_CELL_SIZE = new Dimension(50, 50);

		JDefaultTableCellRenderer() {
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
