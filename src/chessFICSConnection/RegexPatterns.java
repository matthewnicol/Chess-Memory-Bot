package chessFICSConnection;

import java.util.regex.Pattern;

public final class RegexPatterns {
	//whole, board, whoMadeMove, move
	static Pattern userMoves = Pattern.compile("Game\\s(\\d+):\\s(\\w+)\\smoves:\\s(([a-h[BQRNK]][a-h[1-8]]?x?[a-h]?\\d\\+?[#]?)|(O-O\\+?[#]?)|(O-O-O\\+?[#]?))");
	//whole, whoSentTell, theTell
	static Pattern ficsTell = Pattern.compile("(\\w+)\\stells\\syou:\\s((\\S| )+)");
	//whole, whoILoggedInAs
	static Pattern serverLogin = Pattern.compile("Press return to enter the server as \"(\\w+)(\\(U\\))??\":");
	//whole, gameNumber, listOfUsers, userCount
	static Pattern observers = Pattern.compile("Examining\\s(\\d+)\\s\\(scratch\\):\\s(.+)\\s\\((\\d+)\\suser[s]?\\)");
	//whole, gameID, white, black
	static Pattern examineStarted = Pattern.compile("Game ([0-9]+) .?(\\w+) vs. (\\w+).?");
}
