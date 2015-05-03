package franchise;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import player.Position;
import player.Player;
import teamName.TeamName;

public class FranchiseView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Franchise franchise;
	private JTable table;
	private JLabel headerLabel;
	
	public static void main(String[] args){
		Player[] players = new Player[50];
		for(int i = 0; i < players.length; i++){
			players[i] = new Player(i + 1, "Players" + i, i * 5 + "", Position.CENTER, i + 1);
		}
		Franchise franchise = new Franchise(TeamName.BRUINS, players);
		FranchiseView view = new FranchiseView();
		view.setFranchise(franchise);
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 733, 439);
		frame.getContentPane().add(view);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public FranchiseView(){
		initialize();
	}
	
	private void initialize(){
		setPreferredSize(new Dimension(633, 339));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		{
			JPanel headerPanel = new JPanel();
			GridBagLayout gbl_headerPanel = new GridBagLayout();
			gbl_headerPanel.columnWidths = new int[] { 0, 0 };
			gbl_headerPanel.rowHeights = new int[] { 0, 0 };
			gbl_headerPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_headerPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			headerPanel.setLayout(gbl_headerPanel);
			
			headerLabel = new JLabel("This is the Header Label");
			headerLabel.setFont(new Font("Tahoma", Font.PLAIN, 55));
			headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_headerLabel = new GridBagConstraints();
			gbc_headerLabel.gridx = 0;
			gbc_headerLabel.gridy = 0;
			headerPanel.add(headerLabel, gbc_headerLabel);
			
			GridBagConstraints gbc_headerPanel = new GridBagConstraints();
			gbc_headerPanel.anchor = GridBagConstraints.NORTH;
			gbc_headerPanel.insets = new Insets(0, 0, 5, 0);
			gbc_headerPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_headerPanel.gridx = 0;
			gbc_headerPanel.gridy = 0;
			add(headerPanel, gbc_headerPanel);
		}
		{
			JPanel scrollPanePanel = new JPanel();
			GridBagLayout gbl_scrollPanePanel = new GridBagLayout();
			gbl_scrollPanePanel.columnWidths = new int[] { 0, 0 };
			gbl_scrollPanePanel.rowHeights = new int[] { 0, 0 };
			gbl_scrollPanePanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_scrollPanePanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			scrollPanePanel.setLayout(gbl_scrollPanePanel);
			
			JScrollPane scrollPane = new JScrollPane();
			table = new JTable();
			table.setDefaultRenderer(Integer.class, new FranchiseCellRenderer());
			table.setDefaultRenderer(Object.class, new FranchiseCellRenderer());
			scrollPane.setViewportView(table);
			scrollPane.getVerticalScrollBar().setUnitIncrement(table.getRowHeight() / 2);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			scrollPanePanel.add(scrollPane, gbc_scrollPane);
			
			GridBagConstraints gbc_scrollPanePanel = new GridBagConstraints();
			gbc_scrollPanePanel.fill = GridBagConstraints.BOTH;
			gbc_scrollPanePanel.gridx = 0;
			gbc_scrollPanePanel.gridy = 1;
			add(scrollPanePanel, gbc_scrollPanePanel);
		}
	}
	
	public void setFranchise(Franchise franchise){
		this.franchise = franchise;
		
		TeamName teamName = franchise.getTeamName();
		headerLabel.setText(teamName.getProvince() + " " + teamName.toString());
		
		table.setModel(franchise.new FranchiseTableModel());
		// Hide ID column
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
		// Limit size of Player Number column (which is column 0 after id column is removed
		TableColumn numberColumn = table.getColumnModel().getColumn(0);
		numberColumn.setMaxWidth(50);
		numberColumn.setMinWidth(50);
	}

	public Franchise getFranchise() {
		return franchise;
	}
	
	
	public class FranchiseCellRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
			
			if (isSelected) {
				component.setBackground(colorCompliment(franchise.getTeamName().getBaseColor()));
			} else {
				component.setBackground(franchise.getTeamName().getBaseColor());
			}
			component.setForeground(franchise.getTeamName().getMainColor());
			
			return component;
		}
		
		private Color colorCompliment(Color color){
			int[] colors = { color.getRed(), color.getGreen(), color.getBlue() };
			for (int i = 0; i < colors.length; i++) {
				if (colors[i] > 127) {
					colors[i] -= 100;
				} else {
					colors[i] += 100;
				}
			}
			return new Color(colors[0], colors[1],	colors[2]);
		}
	}
	
}
