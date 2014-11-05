package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JMainFrame {
	private static DbConnection DB_Connection;
	private static ListOfRows<Player> All_Players;
	private static ListOfRows<Suggestion> Player_Updates;
	private Franchise franchise;

	private JFrame frame;
	private JPanel panelContainer = new JPanel();
	private JPanelHome panelHome = new JPanelHome();
	private JPanelQuiz panelQuiz;
	
	private JPanel panelTeam;
	private JPanel panelSuggestions;
	
	private JPanelControls panelControls;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMainFrame window = new JMainFrame();
					window.frame.setVisible(true);
					DB_Connection = new DbConnection();
					All_Players = DB_Connection.getPlayers();
					Player_Updates = DB_Connection.getSuggestions();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private JMainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					DB_Connection.close();
				} catch (SQLException | NullPointerException e) {
					System.exit(-1);
				}
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
		frame.setBounds(100, 100, 733, 439);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 717, 0 };
		gridBagLayout.rowHeights = new int[] { 380, 20, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		panelContainer.setLayout(new CardLayout());
		GridBagConstraints gbc_panelContainer = new GridBagConstraints();
		gbc_panelContainer.fill = GridBagConstraints.BOTH;
		gbc_panelContainer.insets = new Insets(0, 0, 5, 0);
		gbc_panelContainer.gridx = 0;
		gbc_panelContainer.gridy = 0;

		panelHome.addPropertyChangeListener("teamSelected",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						franchise = (Franchise) e.getNewValue();
						teamChange();
					}

				});
		panelContainer.add(panelHome, "home");

		frame.getContentPane().add(panelContainer, gbc_panelContainer);

		panelControls = new JPanelControls(0);
		panelControls.addPropertyChangeListener("buttonClicked",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						changePanel((String) e.getNewValue());
					}
				});
		panelControls.addPropertyChangeListener("playerEdited",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						Player player = (Player) e.getNewValue();
						String[] fieldAndValue = (String[]) e.getOldValue();
						try {
							for (int i = 0; i < fieldAndValue.length; i += 2) {
								DbPlayerField field = DbPlayerField
										.valueOf(fieldAndValue[i]);
								String value = fieldAndValue[i + 1];
								Suggestion suggestion = new Suggestion(player
										.getId(), field, value);
								DB_Connection.addNewSuggestion(player,
										suggestion);
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
		GridBagConstraints gbc_panelControls = new GridBagConstraints();
		gbc_panelControls.anchor = GridBagConstraints.SOUTH;
		gbc_panelControls.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelControls.gridx = 0;
		gbc_panelControls.gridy = 1;

		frame.getContentPane().add(panelControls, gbc_panelControls);
	}

	private void teamChange() {
		TeamName teamName = TeamName.valueOf(franchise.getTeamName().toUpperCase());

		panelQuiz = new JPanelQuiz(createTeam(teamName));
		panelTeam = new ScrollPaneFactory(createTeam(teamName)).getScrollPanel();
		panelSuggestions = new ScrollPaneFactory(getEditedPlayerList(), Player_Updates).getScrollPanel();
		panelSuggestions.setName("vote");
		panelSuggestions.addPropertyChangeListener("vote", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				int voteCount = (int) e.getOldValue();
				int suggestionId = (int) e.getNewValue();
				if(suggestionId == 0){
					suggestionId = 1;
				}
				System.out.println(suggestionId);
				try {
					DB_Connection.vote(Player_Updates.getRow(suggestionId), voteCount);
					Player_Updates.deleteRow(suggestionId);
					panelSuggestions = new ScrollPaneFactory(getEditedPlayerList(), Player_Updates).getScrollPanel();
					panelContainer.remove(3);
					panelContainer.add(panelSuggestions, "vote");
					changePanel("vote");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		addListeners(panelQuiz);
		addListeners(panelTeam);
		addListeners(panelSuggestions);
		addListeners(panelHome);

		panelContainer.add(panelQuiz, "quiz");
		panelContainer.add(panelTeam, "team");
		panelContainer.add(panelSuggestions, "vote");

		GridBagConstraints gbc_panelContainer = new GridBagConstraints();
		gbc_panelContainer.insets = new Insets(0, 0, 0, 0);
		gbc_panelContainer.fill = GridBagConstraints.BOTH;
		gbc_panelContainer.gridx = 0;
		gbc_panelContainer.gridy = 0;
		frame.getContentPane().add(panelContainer, gbc_panelContainer);

		changePanel("quiz");
		panelQuiz.start();
	}

	private ListOfRows<Player> getEditedPlayerList() {
		List<Player> playersWithChanges = new ArrayList<Player>();
		for (Suggestion suggestion : Player_Updates.getList()) {
			Player player = All_Players.getRow(suggestion.getPlayerId());
			if (!playersWithChanges.contains(player)) {
				playersWithChanges.add(player);
			}
		}
		return new ListOfRows<Player>(playersWithChanges);
	}

	private void changePanel(String panelName) {
		CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
		cardLayout.show(panelContainer, panelName);

		switch (panelName) {
		case "quiz":
			panelControls.showButtons(1, 4, 5);
			break;
		case "team":
			panelControls.showButtons(1, 2, 3, 5, 6);
			break;
		case "vote":
			panelControls.showButtons(1, 3, 4, 5, 6);
			break;
		case "home":
			panelControls.showButtons(0);
			break;
		default:
			panelControls.showButtons();
			break;
		}
	}

	private void addListeners(JPanel panel) {
		panel.addPropertyChangeListener("buttonClicked", new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						String newValue = (String) e.getNewValue();
						changePanel(newValue);
					}

				});

		panel.addPropertyChangeListener("playerChange",	new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						int playerId = (int) e.getNewValue();
						panelControls.setPlayer(All_Players.getRow(playerId));
					}
				});

	}

	private ListOfRows<Player> createTeam(TeamName teamName) {
		List<Player> team = new ArrayList<Player>();
		for (Player player : All_Players.getList()) {
			if (player.getFranchise().getTeamName()
					.equalsIgnoreCase(teamName.toString())) {
				team.add(player);
			}
		}
		return new ListOfRows<Player>(team);
	}
}
