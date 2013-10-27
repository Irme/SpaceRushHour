package is.ru.app.puzzle;

import android.app.Activity;
import android.os.Bundle;


public class PuzzleActivity extends Activity{
	
	BoardDrawableView mBoardDrawableView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
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