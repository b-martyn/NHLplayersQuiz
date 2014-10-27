package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class Tester {

	public static void main(String[] args) throws SQLException {
		
		ListOfRows team = new DbTeam().getTeam(TeamName.BRUINS);
		testPane(team);
	}
	
	private static void testPane(ListOfRows team){
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(556, 349));
		


		
		frame.add(new JPanelListAllTeam(team));
		frame.setVisible(true);
	}

}
