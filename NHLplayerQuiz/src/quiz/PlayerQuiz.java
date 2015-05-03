package quiz;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class PlayerQuiz extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new PlayerQuiz();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PlayerQuiz() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewTeam = new JMenuItem("New Team");
		mntmNewTeam.setActionCommand("newTeam");
		mntmNewTeam.addActionListener(this);
		mnFile.add(mntmNewTeam);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmTeam = new JMenuItem("Team");
		mntmTeam.setActionCommand("viewTeam");
		mntmTeam.addActionListener(this);
		mnView.add(mntmTeam);
		
		JMenuItem mntmQuiz = new JMenuItem("Quiz");
		mntmQuiz.setActionCommand("viewQuiz");
		mntmQuiz.addActionListener(this);
		mnView.add(mntmQuiz);
		
		controller = new Controller();
		getContentPane().add(controller);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		switch(actionEvent.getActionCommand()){
			case "newTeam":
				controller.showTeamNames();
				break;
			case "viewTeam":
				controller.showFranchise();
				break;
			case "viewQuiz":
				controller.showQuiz();
				break;
			default:
				System.out.println("Missed Action Event: " + actionEvent.getActionCommand());
				break;
		}
	}
}
