package franchise;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import player.Player;
import player.Position;
import teamName.TeamName;

public class Franchise {
	private TeamName teamName = null;
	private Player[] players = null;

	public Franchise() {
	}

	public Franchise(TeamName teamName, Player[] players) {
		this.teamName = teamName;
		this.players = players;
	}

	public void setTeamName(TeamName teamName) {
		this.teamName = teamName;
	}

	public TeamName getTeamName() {
		return teamName;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public Player[] getPlayers() {
		return players;
	}

	public class FranchiseTableModel extends AbstractTableModel implements TableModel {
		private static final long serialVersionUID = 1L;

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
				case 0:
					return Integer.class;
				case 1:
					return Integer.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return Position.class;
				default:
					throw new IndexOutOfBoundsException(
							"Column Index out of bounds: " + columnIndex);
			}
		}

		@Override
		public int getColumnCount() {
			// 1 - Player ID
			// 2 - Player Number
			// 3 - Player FirstName
			// 4 - Player LastName
			// 5 - Player Position
			return 5;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "ID";
			case 1:
				return "Number";
			case 2:
				return "First Name";
			case 3:
				return "Last Name";
			case 4:
				return "Position";
			default:
				throw new IndexOutOfBoundsException(
						"Column Index out of bounds: " + columnIndex);
			}
		}

		@Override
		public int getRowCount() {
			return players.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return players[rowIndex].getId();
			case 1:
				return players[rowIndex].getNumber();
			case 2:
				return players[rowIndex].getFirstName();
			case 3:
				return players[rowIndex].getLastName();
			case 4:
				return players[rowIndex].getPosition();
			default:
				throw new IndexOutOfBoundsException(
						"Column Index out of bounds: " + columnIndex);
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			Class<?> rightClass = getColumnClass(columnIndex);
			if (value.getClass() == rightClass) {
				switch (columnIndex) {
				case 0:
					players[rowIndex].setId((int) value);
					break;
				case 1:
					players[rowIndex].setNumber((int) value);
					break;
				case 2:
					players[rowIndex].setFirstName((String) value);
					break;
				case 3:
					players[rowIndex].setLastName((String) value);
					break;
				case 4:
					players[rowIndex].setPosition((Position) value);
					break;
				default:
					throw new IndexOutOfBoundsException(
							"Column Index out of bounds: " + columnIndex);
				}
			} else {
				throw new IllegalArgumentException("Expecting: " + rightClass
						+ " , Received: " + value.getClass());
			}
		}

	}
}
