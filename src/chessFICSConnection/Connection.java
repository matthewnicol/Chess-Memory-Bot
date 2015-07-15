package chessFICSConnection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection extends ServerOutputParser {
	String myGame;
	String myName;
	String human;
	boolean parserDebug;
	
	public Connection(String serverIP, String humanName, String botName, boolean d) throws Exception {
		super(serverIP);
		parserDebug = true;	
		myName = botName;
		human = humanName;
		runLogin();
			
	}
	
	public Matcher nextLine() throws IOException {
		return waitForMoveOrTell(myGame, human);
	}
	
	public String myName() {
		return myName;
	}
	
	public String humanName() {
		return human;
	}
	
	public String gameNumber() {
		return myGame;
	}
	
	public void tellHuman(String s) {
		tellHuman(human, s);
	}

	protected void runLogin() throws Exception {
		enterLoginDetails();
		handleConnect();
		startNewScratchGame();
		waitForUser();
	}
	
	protected void enterLoginDetails() {
		write(myName);  //name to log into the server as 
		write("");      //null string to confirm connection

		//set whatever variables are useful
		write("set silence true");
	}

	//Read server response to our connection attempt.
	//Throw error if we can't get in.
	private void handleConnect() throws Exception {
		debug(parserDebug,"Trying to login as " + myName + ".");
		assert (!myName.matches(waitForPattern(RegexPatterns.serverLogin, 1)));
		debug(parserDebug, "Success logging in.");
	}
	
	//Start a new scratch game on an empty board, find out the board number.
	private void startNewScratchGame() throws IOException {
		debug(parserDebug, "Trying to start a new game... ");
		write("examine");
		Matcher m = waitForPattern(RegexPatterns.examineStarted);
		assert(m.group(2).equals(m.group(3)));
		assert(m.group(3).equals(myName));
		myGame = m.group(1);
		debug(parserDebug, "Got board: " + myGame + ".");
	}

	
	private void waitForUser() throws IOException {
		tellHuman(human, "To get started: \"observe " + myGame + "\" and then \"tell " + myName + " hi\"");
		waitForTell(human, "hi");
		debug(parserDebug, human + " made contact. Ensuring he is following me.");
		if (!isUserObserving()) {
			tellHuman(human, "You're not observing me! Type \"observe " + myGame + "\"");
			waitForUser();
		}
		else {
			debug(parserDebug, "Connection and contact established.");
			tellHuman(human, "Connection established. Nice to see you again :)");
			write("mex " + human);
		}
	}
	
	private boolean isUserObserving() throws IOException {
		write("allobservers " + myGame);
		String users = waitForPattern(RegexPatterns.observers, 2);
		Pattern humanName = Pattern.compile(human);
		Matcher m = humanName.matcher(users);
		return m.find();
	}
	
}
