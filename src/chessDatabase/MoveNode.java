package chessDatabase;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class MoveNode {
	TreeMap<String, MoveNode> moves;
	
	public MoveNode() {
		moves = new TreeMap<>();
	}
	
	public boolean hasMoves() {
		return moves.size() != 0;
	}
	
	public boolean hasMove(String move) {
		return moves.containsKey(move);
	}
	
	public int moveCount() {
		return moves.size();
	}
	
	public MoveNode child(String label) {
		return moves.get(label);
	}
	
	public String randomChild() {
		Random r = new Random();
		int randint = r.nextInt(moves.size());
		return new ArrayList<String>(moves.keySet()).get(randint);
	}
	
	public String allMoves() {
		StringBuilder s = new StringBuilder();
		String[] a = (String[])moves.keySet().toArray();
		for (int i = 0; i < a.length; i++) {
			s.append(a[i]);
			s.append(" ");
		}
		return s.toString();
	}
	
	public void insert(ArrayList<String> gameMoves, String result) {
		if (gameMoves.size() == 0) {
			return;
		}
		else {
			ArrayList<String> nextMoves = new ArrayList<String>(gameMoves);
			nextMoves.remove(0);
			
			if (!moves.containsKey(gameMoves.get(0))) {
				MoveNode n = new MoveNode();
				if (!nextMoves.isEmpty()) {
					n.insert(nextMoves, result);
				}
				moves.put(gameMoves.get(0), n);
			}
			else {
				moves.get(gameMoves.get(0)).insert(nextMoves, result);
			}
		}
		
		
	}
}
