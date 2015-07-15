package chessFICScommunicator;

import java.util.regex.Matcher;

import chessFICSConnection.OutputType;

public class OverTheBoard extends ChessCommands {
	final static String FICSIP = "167.114.65.195";
	final static String USERNAME = "YouRookNice";
	final static String MYNAME = "MemoryBot";
	
	boolean verbose;
	boolean live;
	
	public OverTheBoard(String ip, String human, String myname, boolean verbose) throws Exception {
		super(ip, human, myname);
		this.verbose = verbose;
		live = true;
	}

	public void mainLoop() throws Exception {
		while (live) {
			Matcher outputMatcher = connection.nextLine();
			OutputType type = findOutputType(outputMatcher.group(0));
			
			switch (type) {
			case TELL:
				handleTell(outputMatcher.group(2));	break;
			case MOVE:
				handleMove(outputMatcher.group(3));	break;
			default:
				
				break;
			}
		}
	}	
	
	private OutputType findOutputType(String data) throws Exception {
		if (data.startsWith(connection.humanName())) {
			return OutputType.TELL;
		}
		else if (data.startsWith("Game " + connection.gameNumber() + ": " + connection.humanName() + " moves:")) {
			return OutputType.MOVE;
		}
		else if (data.startsWith("Game " + connection.gameNumber() + ": " + connection.myName() + " moves:")) {
			return OutputType.IGNORE;
		}
		else {
			throw new Exception("Not prepared for this kind of output.");
		}
	}
	
	private void handleTell(String tell) {
		debug(verbose, "Handling tell: [" + tell + "]");
		if (tell.startsWith("addgame")) {
			addGame();
		}
		else if (tell.startsWith("save")) {
			saveGame();
		}
		else if (tell.startsWith("title ")) {
			setTitle(tell.substring(6));
		}
		else if (tell.startsWith("memorize")) {
			memorize();
		}
	}
	
	private void debug(boolean verbose2, String string) {
		if (verbose2)
			System.out.println("DEBUG: " + string);
	}

	private void handleMove(String move) throws InterruptedException {
		debug(verbose, "Handling move: [" + move + "]");
			playMove(move);
	}

	public static void main(String[] args) throws Exception {
		OverTheBoard board = new OverTheBoard(FICSIP, USERNAME, MYNAME, true);
		board.mainLoop();
	}

}
