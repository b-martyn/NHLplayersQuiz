/*
 * int values for buttons:
 * 		1: btnHome
 * 		2: btnVote
 * 		3: btnQuiz
 * 		4: btnTeam
 * 		5: btnEdit
 * 		6: btnNew
 */

package main;

import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

class JPanelControls extends JPanel {
	private Player player;

	private JButton btnHome, btnVote, btnQuiz, btnTeam, btnEdit, btnNew;

	public JPanelControls() {
		this(1, 2, 3, 4, 5, 6);
	}

	public JPanelControls(int... buttons) {
		initialize();
		showButtons(buttons);
	}

	private void initialize() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnHome = new JButton("Pick My Team");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firePropertyChange("buttonClicked", false, "home");
			}
		});
		btnHome.setVisible(false);
		add(btnHome);

		btnVote = new JButton("Approve Changes");
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firePropertyChange("buttonClicked", false, "vote");
			}
		});
		btnVote.setVisible(false);
		add(btnVote);

		btnQuiz = new JButton("Test my Knowlege");
		btnQuiz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firePropertyChange("buttonClicked", false, "quiz");
			}
		});
		btnQuiz.setVisible(false);
		add(btnQuiz);

		btnTeam = new JButton("Show my Team");
		btnTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				firePropertyChange("buttonClicked", false, "team");
			}
		});
		btnTeam.setVisible(false);
		add(btnTeam);

		btnEdit = new JButton("This Player is Wrong");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (player != null) {
					JDialogEdit editPlayerDialog = new JDialogEdit(player);
					editPlayerDialog.addPropertyChangeListener("playerEdited",
							new PropertyChangeListener() {
								@Override
								public void propertyChange(PropertyChangeEvent e) {
									firePropertyChange("playerEdited",
											e.getOldValue(), e.getNewValue());
								}
							});
				}
			}
		});
		btnEdit.setVisible(false);
		add(btnEdit);

		btnNew = new JButton("Add Missing Player");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialogEdit newPlayerDialog = new JDialogEdit();
				newPlayerDialog.addPropertyChangeListener("playerEdited",
						new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent e) {
								firePropertyChange("playerEdited",
										e.getOldValue(), e.getNewValue());
							}
						});
			}
		});
		btnNew.setVisible(false);
		add(btnNew);
	}

	void showButtons() {
		showButtons(1, 2, 3, 4, 5, 6);
	}

	void showButtons(int... buttons) {
		btnHome.setVisible(false);
		btnVote.setVisible(false);
		btnQuiz.setVisible(false);
		btnTeam.setVisible(false);
		btnEdit.setVisible(false);
		btnNew.setVisible(false);
		for (int i = 0; i < buttons.length; i++) {
			switch (buttons[i]) {
			case 1:
				btnHome.setVisible(true);
				break;
			case 2:
				btnVote.setVisible(true);
				break;
			case 3:
				btnQuiz.setVisible(true);
				break;
			case 4:
				btnTeam.setVisible(true);
				break;
			case 5:
				btnEdit.setVisible(true);
				break;
			case 6:
				btnNew.setVisible(true);
				break;
			default:
				break;
			}
		}
	}

	void setPlayer(Player player) {
		this.player = player;
		btnEdit.setText(player.getLastName() + " is Wrong");
	}
}