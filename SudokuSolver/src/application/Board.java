package application;

public class Board {

	private Integer[][] board = new Integer[9][9];
	
	public Board() {
	}
	
	public Board(Board boardCopy) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = boardCopy.get(i, j);
			}
		}
	}

	// Helper for getting a value of a tile
	public Integer get(int i, int j) {
		return board[i][j];
	}
	
	// Helper for setting a value of a tile
	public void set(int i, int j, Integer value) {
		board[i][j] = value;
	}
}
