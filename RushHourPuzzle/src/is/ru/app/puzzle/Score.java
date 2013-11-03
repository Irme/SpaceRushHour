package is.ru.app.puzzle;

import android.util.SparseArray;

public class Score {
	
	private SparseArray<int[]> puzzleScore = new SparseArray<int[]>();
	
	public Score(int puzzleId, int level, int score, int solved){
		puzzleScore.put(puzzleId, new int[]{level, score, solved});
	}
	
	public String getScore(int puzzleId){
		int[] score = puzzleScore.get(puzzleId);
		return score[0] + " " + score[0] + " " + score[0];
	}
}
