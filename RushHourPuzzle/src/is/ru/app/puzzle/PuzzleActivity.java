package is.ru.app.puzzle;

import android.app.Activity;
import android.os.Bundle;


public class PuzzleActivity extends Activity{
	
	BoardDrawableView mBoardDrawableView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		int id = b == null ? 0 : b.getInt("id");
		mBoardDrawableView = new BoardDrawableView(this, id);
	    setContentView(mBoardDrawableView);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
    @Override
    protected void onRestart() {
        super.onRestart();
    }
	}