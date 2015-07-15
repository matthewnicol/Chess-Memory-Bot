package chessDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MoveTree {
	MoveNode root;
	MoveNode currentPos;
	GameMode mode;
	public int inserts;
	public MoveTree() {
		root = new MoveNode();
		currentPos = root;
		inserts = 0;
	}
	
	public boolean tryToPlay(String move) {
		if (currentPos.hasMove(move)) {
			currentPos = currentPos.child(move);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void reset() {
		currentPos = root;
	}
	
	public boolean hasNext() {
		return currentPos.hasMoves();
	}
	
	private void insertVariation(ArrayList<String> moves, String result) {
		root.insert(moves, result);
	}
	
	public void insertVariation(String game, String result) {
		insertVariation(new ArrayList<String>(Arrays.asList(game.split(" "))), result);
		System.out.print(++inserts);
		System.out.println(" inserted");
	}
	
	public boolean hasNext(String game) {
		return currentPos.hasMoves();
	}
	
	public String nextMoves() {
		return currentPos.allMoves();
	}
	
	public String randomMove() {
		ArrayList<String> m = (ArrayList<String>) Arrays.asList(nextMoves().split(" "));
		Random r = new Random();
		return m.get( r.nextInt(m.size()) );
	}
	
}
