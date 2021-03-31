package application;

import java.util.HashSet;
import java.util.Set;

public class Solver {
	private Set<Integer> allowedNumbers;
	
	public Solver() {
		allowedNumbers = new HashSet<Integer>();	
		allowedNumbers.add(1);
		allowedNumbers.add(2);
		allowedNumbers.add(3);
		allowedNumbers.add(4);
		allowedNumbers.add(5);
		allowedNumbers.add(6);
		allowedNumbers.add(7);
		allowedNumbers.add(8);
		allowedNumbers.add(9);
	}
	
	public boolean checkRow(Board board, int row) {
		Set<Integer> numbers = new HashSet<Integer>(allowedNumbers);
		
		/* Going through each tile in the row and comparing it to numbers.
		 * If the number already have been removed from the numbers, 
		 * then it means the current number is a duplicate, and it will return false. */
		for (int i = 0; i < 9; i++) {
			// Gets value of the current tile in the row
			Integer value = board.get(i, row);
			if (value != null) {
				if (!numbers.contains(value)) {
					return false;
				}
				else {
					numbers.remove(value);
				}
			}
		}
		return true;
	}
	
	// Checks all rows to see if any duplicate numbers
	public boolean checkAllRows(Board board) {
		for (int i = 0; i < 9; i++) {
			if (!checkRow(board, i)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkColumn(Board board, int col) {
		Set<Integer> numbers = new HashSet<Integer>(allowedNumbers);
		
		/* Going through each tile in the column and comparing it to numbers.
		 * If the number already have been removed from the numbers, 
		 * then it means the current number is a duplicate, and it will return false. */
		for (int i = 0; i < 9; i++) {
			// Gets value of the current tile in the column
			Integer value = board.get(col, i);
			if (value != null) {
				if (!numbers.contains(value)) {
					return false;
				}
				else {
					numbers.remove(value);
				}
			}
		}
		return true;
	}
	
	// Checks all columns to see if any duplicate numbers
	public boolean checkAllColumns(Board board) {
		for (int i = 0; i < 9; i++) {
			if (!checkColumn(board, i)) {
				return false;
			}
		}
		return true;
	}
		
	public boolean check3x3(Board board, int startCol, int startRow) {
		Set<Integer> numbers = new HashSet<Integer>(allowedNumbers);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Integer value = board.get(startCol + i, startRow + j);
				if (value != null) {
					if (!numbers.contains(value)) {
						return false;
					}
					else {
						numbers.remove(value);
					}
				}
			}
		}
		return true;
	}
	
	// Checks all 3x3 squares to see if any duplicate numbers
	public boolean checkAll3x3(Board board) {
		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				if (!check3x3(board, i, j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Checks if all values on the board is correct
	public boolean checkAll(Board board) {
		return checkAllRows(board) && checkAllColumns(board) && checkAll3x3(board);
	}
	
	// Checks if board is full
	public boolean isBoardFull(Board board) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board.get(i, j) == null) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Checks if board is completely solved
	public boolean isBoardCompletelySolved(Board board) {
		return checkAll(board) && isBoardFull(board);
	}
	
	Board solve(Board partBoard ) {
		// Checks if board is already filled 
		if (!checkAll(partBoard)) {
			return null;
		}
		if (isBoardCompletelySolved(partBoard)) {
			return partBoard;
		}
		
		// If board is not filled and solved make a copy,
		// which we will use backtracking algorithm on
		Board board = new Board(partBoard);
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Integer current = board.get(i, j);
				if (current == null) {
					Set<Integer> tryNumbers = new HashSet<Integer>(allowedNumbers);
					for (Integer number : tryNumbers) {
						board.set(i, j, number);
						Board solution = solve(board);
						if (solution != null) {
							return solution;
						}
					}
					return null;
				}
			}
		}
		return null;
	}
}
