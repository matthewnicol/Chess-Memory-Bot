package chessFICSConnection;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ServerOutputParser extends ServerPipes {

	public ServerOutputParser(String serverIPAddress) throws UnknownHostException, IOException {
		super(serverIPAddress, true, true);
	}
	
	//expect message conforming to pattern P coming from the server.
	//will freeze until input is received. Possibly create timeout.
	protected final Matcher waitForPattern(Pattern p) throws IOException {
		Matcher matcher;
		do { matcher = p.matcher(getLine()); }
		while (!matcher.find());
		return matcher;
	}

	//if we don't want to deal with a matcher object, just return the string of the
	//particular group selection we are interested in.
	//Hide messy matcher code!
	protected final String waitForPattern(Pattern p, int group) throws IOException {
		return waitForPattern(p).group(group);
	}

	//wait for any tell by any user on the server.
	protected final Matcher waitForTell() throws IOException {
		return waitForPattern(RegexPatterns.ficsTell);	
	}

	//wait for a tell by user with specific username.
	//return the tell.
	protected final String waitForTell(String username) throws IOException {
		Matcher tell;
		do {
			tell = waitForTell();
		} while (!tell.group(1).equals(username));
		return tell.group(2);
	}
	
	//wait for specific tell by a specific username.
	protected void waitForTell(String username, String tell) throws IOException {
		String serverTell;
		do {
			serverTell = waitForTell(username);
		} while (!serverTell.equals(tell));
		
	}
	
	//wait for any move on an examined game we are observing.
	//return the whole pattern matcher.
	protected final Matcher waitForMove() throws IOException {
		return waitForPattern(RegexPatterns.userMoves);
	}
	
	
	//wait for a move by user with specific username on specific board.
	//just return the move.
	protected final String waitForMove(String username, String board) throws IOException {
		Matcher move = waitForMove();
		if (move.group(1).equals(board) && move.group(2).equals(username)) {
			return move.group(3);
		}
		else {
			return waitForMove(username, board);
		}
	}
	
	protected final Matcher waitForMoveOrTell(String username, String board) throws IOException {
		while (true) {
			String line = getLine();
			Matcher a = RegexPatterns.userMoves.matcher(line);
			Matcher b = RegexPatterns.ficsTell.matcher(line);
			if (a.find()) {
				return a;
			}
			else if (b.find()) {
				return b;
			}
		}
	}
}
