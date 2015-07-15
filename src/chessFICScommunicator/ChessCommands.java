package chessFICScommunicator;

import chessDatabase.ChessDatabase;
import chessFICSConnection.Connection;

public abstract class ChessCommands {

	Connection connection;
	GameMode mode;
	String moves;
	String title = "default";
	boolean myTurn = false;
	
	ChessDatabase chessdb;
	
	private String command(String s) {
		return "\"tell " + connection.myName() + " " + s + "\" ";
	}
	
	private void explainClear() {
		connection.tellHuman(command("clear") + "to empty input and start again");
	}
	
	public ChessCommands(String serverIP, String humanName, String robotName) throws Exception {
		connection = new Connection(serverIP, humanName, robotName, true);
		mode = GameMode.MAIN;
		moves = "";
		chessdb = new ChessDatabase();
	}
	
	protected void exit() {
		connection.tellHuman("Now shutting down. Goodbye.");
		connection.write("exit");
	}
	
	protected void addGame() {
		switch (mode) {
		case MAIN: 
			mode = GameMode.INSERTGAME; 
			connection.tellHuman("Ready to accept input");
			break;
		case INSERTGAME: 
			connection.tellHuman("You're already inserting a game.");
			explainClear();
			break;
		default:
			connection.tellHuman("You can't do that here.");
		}
	}
	
	protected void playMove(String move) throws InterruptedException {
		switch (mode) {
		case MAIN:
			connection.write("back 999");
			connection.tellHuman("You're still at the main menu.");
			connection.tellHuman("Please \"tell " + connection.myName() + " addgame\" to add a new game.");
			connection.tellHuman("Please \"tell " + connection.myName() + " memorize\" to memorize a game.");
			break;
		case INSERTGAME:
			moves += move + " ";
			connection.tellHuman("Move accepted: " + move);
			break;
		case PLAYGAME:
			if (chessdb.playMove(title, move)) {
				if (chessdb.hasNext(title)) {
					String themove = chessdb.randomMove(title);
					connection.write(themove);
				}
			}
			else {
				connection.tellHuman("Sorry. I don't know that move.");
				Thread.sleep(1000);
				chessdb.resetPosition(title);
				connection.write("back 999");
			}
			if (!chessdb.hasNext(title)) {
				chessdb.resetPosition(title);
				Thread.sleep(1000);
				connection.tellHuman("We're at the end. Resetting.");
				connection.write("back 999");
			}
			break;
		}
	}
	
	protected void setTitle(String t) {
		title = t;
		connection.tellHuman("This and following variations will be saved to title: " + t);
	}
	
	protected void saveGame() {

		chessdb.insertVariation(title, moves, "");
		moves = "";
		connection.tellHuman("Game saved.");
		connection.write("back 999");
		connection.tellHuman("Ready to insert a new variation under: " + title);
		connection.tellHuman("Or don't.");
	}
	
	protected void memorize() {
		connection.tellHuman("Ready to memorize variation table " + title);
		connection.tellHuman("\"tell " + connection.myName() + " play\" to switch sides");
		connection.write("back 999");
		myTurn = false;
		mode = GameMode.PLAYGAME;
	}
	

}
