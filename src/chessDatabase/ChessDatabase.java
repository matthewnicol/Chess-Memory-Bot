package chessDatabase;

import java.util.TreeMap;

public class ChessDatabase {
	TreeMap<String, MoveList> wholeGames;
	TreeMap<String, MoveTree> variationGames;

	public ChessDatabase() {
		wholeGames = new TreeMap<>();
		variationGames = new TreeMap<>();
	}
	
	public void insertVariation(String label, String moves, String result) {
		if (!variationGames.containsKey(label)) {
			MoveTree t = new MoveTree();
			t.insertVariation(moves, result);
			variationGames.put(label, t);
		}
		else {
			variationGames.get(label).insertVariation(moves, result);
		}
	}
	
	public boolean hasNext(String label) {
		return variationGames.get(label) != null && variationGames.get(label).hasNext();
	}
	
	public boolean playMove(String label, String move) {
		MoveTree play = variationGames.get(label);
		if (play.tryToPlay(move)) {
			return true;
		}
		return false;
	}
	
	public String allMoves(String label) {
		return variationGames.get(label).nextMoves();
	}
	
	public String randomMove(String label) {
		String m = variationGames.get(label).currentPos.randomChild();
		playMove(label, m);
		return m;
	}
	
	public void resetPosition(String label) {
		variationGames.get(label).currentPos = variationGames.get(label).root;
	}
}
