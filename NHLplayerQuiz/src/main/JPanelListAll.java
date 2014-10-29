package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.ListSelectionModel;

public class JPanelListAll extends JPanel {
	protected ListOfRows<Player> team;
	
	protected JTable tableMain;
	protected JLabel lblHeader;

	/**
	 * Create the panel.
	 */
	protected JPanelListAll(ListOfRows<Player> team) {
		this.team = team;
		initialize();
		setTableData();
	}
	
	protected void initialize(){
		setSize(new Dimension(556, 349));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panelHeader = new JPanel();
		GridBagConstraints gbc_panelHeader = new GridBagConstraints();
		gbc_panelHeader.anchor = GridBagConstraints.NORTH;
		gbc_panelHeader.insets = new Insets(0, 0, 5, 0);
		gbc_panelHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelHeader.gridx = 0;
		gbc_panelHeader.gridy = 0;
		add(panelHeader, gbc_panelHeader);
		GridBagLayout gbl_panelHeader = new GridBagLayout();
		gbl_panelHeader.columnWidths = new int[]{0, 0};
		gbl_panelHeader.rowHeights = new int[]{0, 0};
		gbl_panelHeader.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelHeader.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelHeader.setLayout(gbl_panelHeader);
		
		lblHeader = new JLabel();
		lblHeader.setFont(new Font("Tahoma", Font.PLAIN, 55));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.gridx = 0;
		gbc_lblHeader.gridy = 0;
		panelHeader.add(lblHeader, gbc_lblHeader);
		
		JPanel panelMain = new JPanel();
		GridBagConstraints gbc_panelMain = new GridBagConstraints();
		gbc_panelMain.fill = GridBagConstraints.BOTH;
		gbc_panelMain.gridx = 0;
		gbc_panelMain.gridy = 1;
		add(panelMain, gbc_panelMain);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[]{0, 0};
		gbl_panelMain.rowHeights = new int[]{0, 0};
		gbl_panelMain.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelMain.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelMain.setLayout(gbl_panelMain);
		
		JScrollPane scrollPaneMain = new JScrollPane();
		scrollPaneMain.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPaneMain = new GridBagConstraints();
		gbc_scrollPaneMain.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMain.gridx = 0;
		gbc_scrollPaneMain.gridy = 0;
		panelMain.add(scrollPaneMain, gbc_scrollPaneMain);
		
		tableMain = new JTable();
		tableMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableMain.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(tableMain.getSelectedRow() > 0){
					int playerId = (int) tableMain.getValueAt(tableMain.getSelectedRow(), 0);
					Franchise franchaise = (Franchise) tableMain.getValueAt(tableMain.getSelectedRow(), 1);
					firePropertyChange("playerChange", franchaise, playerId);
				}
			}
		});
		tableMain.setRowHeight(50);
		scrollPaneMain.setViewportView(tableMain);
	}
	
	protected JPanelControls setPanelControls(){
		return new JPanelControls();
	}
	
	protected void setTableData(){
	}
	
	protected void setHeaderLabel(){
	}
	
	protected Object[][] convertData(List<Player> players){
		List<Object[]> dataHolder = new ArrayList<Object[]>();
		for(Player player : players){
			int id = player.getId();
			Franchise franchise = player.getFranchise();
			String number = String.valueOf(player.getNumber());
			String firstName = player.getFirstName();
			String lastName = player.getLastName();
			String position = player.getPosition().toString();
			dataHolder.add(new Object[]{id, franchise, number, firstName, lastName, position});
		}
		Object[][] data = new Object[dataHolder.size()][];
		data = dataHolder.toArray(data);
		return data;
	}
	
	protected void setColumnRenderers()	{
		for(int i = 0; i < 2; i++){
			tableMain.getColumnModel().getColumn(i).setMinWidth(0);
			tableMain.getColumnModel().getColumn(i).setMaxWidth(0);
		}
		for(int i = 2; i < tableMain.getColumnCount(); i++){
			TableColumn column = tableMain.getColumn(tableMain.getColumnName(i));
			column.setCellRenderer(new JDefaultTableCellRenderer());
		}
	}
}
