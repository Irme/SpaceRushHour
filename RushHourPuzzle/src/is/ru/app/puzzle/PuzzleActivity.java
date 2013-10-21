package is.ru.app.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PuzzleActivity extends Activity{
	
	//<setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)</setup>
	private Integer[][] board = {{1, 1, 1, 1, 1, 1},
								 {1, 0, 2, 0, 0, 0},
								 {0, 0, 2, 0, 0, 1},
								 {0, 1, 1, 1, 0, 1},
								 {0, 0, 0, 0, 1, 1},
								 {1, 1, 1, 0, 1, 0}};
	
	public PuzzleActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	//	setContentView(R.layout.puzzle); //Reads from puzzle.xml
		


	    // Set the text view as the activity layout
	   // setContentView(textView);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				sb.append(board[i][j]);
			}
			sb.append("\n");
		}
		
	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(400);
	    textView.setText(sb.toString());
	    
	    System.out.println("TEST");
	}

}
