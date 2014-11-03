package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class JPanelScrollWithTableAndButtons extends JPanelScrollWithTable {
	private JPanel scrollPanel;

	JPanelScrollWithTableAndButtons() {
		super();
	}

	private void initialize() {
		int rows = tableMain.getRowCount();

		scrollPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		int[] rowHeights = new int[rows];
		double[] rowWeights = new double[rows];
		for (int i = 0; i < rows; i++) {
			rowHeights[i] = tableMain.getRowHeight();
			rowWeights[i] = 0.0;
		}
		gridBagLayout.rowHeights = rowHeights;
		gridBagLayout.rowWeights = rowWeights;
		scrollPanel.setLayout(gridBagLayout);

		GridBagConstraints gbc_tableMain = new GridBagConstraints();
		gbc_tableMain.gridheight = rows;
		gbc_tableMain.fill = GridBagConstraints.BOTH;
		gbc_tableMain.gridx = 0;
		gbc_tableMain.gridy = 0;
		scrollPanel.add(tableMain, gbc_tableMain);

		for (int i = 0; i < rows; i++) {

			Franchise franchise = (Franchise) tableMain.getValueAt(i, 1);

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
					String[] strings = ((String) tableMain.getValueAt(
							Integer.parseInt(button.getName()), 2)).split("|");
					Integer[] suggestionIds = new Integer[strings.length];
					for (int i = 0; i < strings.length; i++) {
						if (!strings[i].isEmpty()) {
							suggestionIds[i] = Integer.parseInt(strings[i]);
						}
					}
					if (suggestionIds[0] != null) {
						firePropertyChange("vote", true, suggestionIds);
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
					String[] strings = ((String) tableMain.getValueAt(
							Integer.parseInt(button.getName()), 2)).split("|");
					Integer[] suggestionIds = new Integer[strings.length];
					for (int i = 0; i < strings.length; i++) {
						if (!strings[i].isEmpty()) {
							suggestionIds[i] = Integer.parseInt(strings[i]);
						}
					}
					if (suggestionIds[0] != null) {
						firePropertyChange("vote", false, suggestionIds);
					}
				}
			});

			scrollPanel.add(btnGood, gbc_btnGood);
			scrollPanel.add(btnBad, gbc_btnBad);
		}
		scrollPaneMain.setViewportView(scrollPanel);
	}

	@Override
	public void setTableModel(TableModel model) {
		tableMain.setModel(model);
		tableMain.setRowSorter(new TableRowSorter<TableModel>(model));
		initialize();
	}

	public JPanel getScrollPanel() {
		return scrollPanel;
	}
}
