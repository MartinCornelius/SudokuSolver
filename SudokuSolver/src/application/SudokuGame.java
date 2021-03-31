package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class SudokuGame extends Application {

	TextField[][] tiles = new TextField[9][9];
	int tileSize = 50;
	
	@Override
	public void start(Stage Game) {
		Game.setTitle("Sudoku Solver");
		Label lblTitle = new Label("Sudoku Solver");
		lblTitle.setStyle("-fx-font-size: 48; -fx-text-fill: #fff; -fx-font-weight: bold;");
		HBox titleContainer = new HBox(lblTitle);
		titleContainer.setAlignment(Pos.TOP_CENTER);
		
		GridPane gameGrid = new GridPane();
		gameGrid.setAlignment(Pos.CENTER);
		gameGrid.setHgap(4);
		gameGrid.setVgap(4);
		gameGrid.setPadding(new Insets(10,0,10,0));
		
		// Grid Setup
		int tileID = 0;
		PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// Create new cell and tile
				StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, i == 2 || i == 5);
                cell.pseudoClassStateChanged(bottom, j == 2 || j == 5);
				
				TextField tile = new TextField();
				tile.setId(String.valueOf(tileID));
				
				tileID++;
				
				tile.setAlignment(Pos.CENTER);
				tile.setPrefSize(tileSize, tileSize);
				tile.setStyle("-fx-background-color: rgb(255,255,255); -fx-font-size: 18;");
				
				cell.getChildren().add(tile);
				
				// Add tile to tiles and game grid
				tiles[i][j] = tile;
				gameGrid.add(cell, i, j);
			}
		}
		
		// Import button
		Button btnImport = new Button("Import Board");
		btnImport.setStyle("-fx-background-color: #85d2e3; -fx-text-fill: #fff; -fx-font-size: 16; -fx-font-weight: bold;");
		btnImport.setOnMouseClicked((event) -> importBoard());
		
		// Clear button
		Button btnClear = new Button("Clear Board");
		btnClear.setStyle("-fx-background-color: #85d2e3; -fx-text-fill: #fff; -fx-font-size: 16; -fx-font-weight: bold;");
		btnClear.setOnMouseClicked((event) -> clearBoard());
		
		// Solve button
		Button btnSolve = new Button("Solve Board");
		btnSolve.setStyle("-fx-background-color: #85d2e3; -fx-text-fill: #fff; -fx-font-size: 16; -fx-font-weight: bold;");
		btnSolve.setOnMouseClicked((event) -> solveBoard());

		// Container to hold import, clear and solve button
		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.BOTTOM_CENTER);
		btnContainer.getChildren().add(btnImport);
		btnContainer.getChildren().add(btnClear);
		btnContainer.getChildren().add(btnSolve);
		
		// Vertical box for holding title, grid and buttons
		VBox gameContainer = new VBox(titleContainer, gameGrid, btnContainer);
		gameContainer.setPadding(new Insets(25,25,25,25));
		gameContainer.setStyle("-fx-background-color: #5ca4b3;");
		
		// Main Scene and game setup
		Scene gameScene = new Scene(gameContainer, 750, 800);
		gameScene.getStylesheets().add(getClass().getResource("sudoku.css").toString());
		Game.setScene(gameScene);
		Game.show();
	}
	
	private void importBoard() {
		clearBoard();
		
		FileChooser boardFile = new FileChooser();
		boardFile.setTitle("Import Board");
		boardFile.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Sudoku Boards | *.txt", "*.txt")
		);
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		boardFile.setInitialDirectory(new File(currentPath));
		File file = boardFile.showOpenDialog(null);
		
		try (Scanner scanner = new Scanner(file)) {
			char[] newBoard = new char[81];
			while (scanner.hasNextLine()) {
				String s = scanner.nextLine();
				for (int i = 0; i < 81; i++) {
					newBoard[i] = s.charAt(i);				
				}
			}
			
			// Populate board
			int currentTile = 0;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (newBoard[currentTile] != '.') {
						tiles[j][i].setText(String.valueOf(newBoard[currentTile]));
					}
					else {
						tiles[j][i].setText("");
					}
					currentTile++;
				}
			}
			
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void clearBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tiles[i][j].setText("");
				tiles[i][j].setStyle("-fx-font-size: 18; -fx-background-color: rgb(255,255,255);");
			}
		}
	}
	
	private void solveBoard() {
		Solver s = new Solver();
		Board partBoard = new Board();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				try {
					Integer x = Integer.valueOf(tiles[i][j].getText().trim());
					partBoard.set(i, j, x);
				}
				catch (NumberFormatException e) {
				}
			}
		}
		
		Board solution = s.solve(partBoard);
		
		// Update UI
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (tiles[i][j].getText().trim().equals("")) {
					if (solution == null) {
						tiles[i][j].setStyle("-fx-background-color: rgb(255,200,200); -fx-font-size: 18;");
					}
					else {
						tiles[i][j].setStyle("-fx-background-color: rgb(200,255,200); -fx-font-size: 18;");
						tiles[i][j].setText(String.valueOf(solution.get(i, j)));
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
