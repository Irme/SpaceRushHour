package is.ru.app.puzzle;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class PuzzleActivity extends Activity{
	
	//<setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)</setup>
	private Integer[][] board = {{1, 1, 1, 1, 1, 1},
								 {1, 0, 2, 0, 0, 0},
								 {0, 0, 2, 0, 0, 1},
								 {0, 1, 1, 1, 0, 1},
								 {0, 0, 0, 0, 1, 1},
								 {1, 1, 1, 0, 1, 0}};
	
	//private GLSurfaceView mGLView;
	BoardDrawableView mBoardDrawableView;
	
	public PuzzleActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	//	setContentView(R.layout.puzzle); //Reads from puzzle.xml
		
	    // Set the text view as the activity layout
	   // 
	//	StringBuffer sb = new StringBuffer();
	//	for (int i = 0; i < board.length; i++) {
	//		for (int j = 0; j < board.length; j++) {
	//			sb.append(board[i][j]);
	//		}
	//		sb.append("\n");
	//	}
		
	    // Create the text view
	 //   TextView textView = new TextView(this);
	 //   textView.setTextSize(40);
	//    textView.setText(sb.toString());
	 //   setContentView(textView);
	  //  System.out.println("TEST");
	    
		mBoardDrawableView = new BoardDrawableView(this);
	      setContentView(mBoardDrawableView);
	}
	   @Override
	    protected void onPause() {
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	    }
	}