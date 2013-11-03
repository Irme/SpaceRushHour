package is.ru.app.puzzle;

import is.ru.app.db.PuzzleAdapter;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.LinearLayout;


public class PuzzleActivity extends Activity{
	
	BoardDrawableView mBoardDrawableView;
	private PuzzleAdapter mPuzzlesAdapter = new PuzzleAdapter( this );
	private int puzzleId = 1;
	private Button m_button_prev;
	private Button m_button_next;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.puzzle);
		Cursor cursor = mPuzzlesAdapter.queryPuzzle(true);
		while(cursor.moveToNext()){
			puzzleId = cursor.getInt(1);
			System.out.println("first id" + puzzleId);
		}
		cursor.close();
		layout = (LinearLayout) findViewById(R.id.puzzle_layout);
		//mBoardDrawableView = new BoardDrawableView(this);
	  //  setContentView(mBoardDrawableView);
	    System.out.println("onCreate");
	    mBoardDrawableView = (BoardDrawableView) findViewById( R.id.boardview );
	    mBoardDrawableView.setUp(puzzleId);
	    if ( savedInstanceState != null ) {
            String state = savedInstanceState.getString( "stateSAVED" );
            mBoardDrawableView.resetShapes(state);
            puzzleId = savedInstanceState.getInt("puzzleId");
        }
	    
	    m_button_prev = (Button) findViewById( R.id.buttonPrevious );
	    m_button_next = (Button) findViewById( R.id.buttonNext );
	    
	}
	
	public void buttonNext( View view )
    {
		if ( puzzleId < 40 ){
			System.out.println("Next " + puzzleId);
			mBoardDrawableView.setUp(++puzzleId);
			mBoardDrawableView.invalidate();
		}
    }
	
	public void buttonPrevious( View view )
    {
		if ( puzzleId > 1 ) {
			mBoardDrawableView.setUp(--puzzleId);
			mBoardDrawableView.invalidate();
		}
    }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mPuzzlesAdapter.close();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cursor cursor = mPuzzlesAdapter.queryPuzzle(true);
		while(cursor.moveToNext()){
			puzzleId = cursor.getInt(1);
		}
		cursor.close();
		mPuzzlesAdapter.close();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mPuzzlesAdapter.close();
	}
    
    @Override
    protected void onDestroy() {
		super.onDestroy();
		mPuzzlesAdapter.close();
	}
    
    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ) {
        super.onSaveInstanceState(savedInstanceState);
        if(mBoardDrawableView == null){
        	System.out.println("Empty view");
        }
        if(mBoardDrawableView.toString() != null){
        savedInstanceState.putString( "stateSAVED",mBoardDrawableView.toString());
        savedInstanceState.putString( "puzzleId", String.valueOf(puzzleId));
        } else {
        	System.out.println("Empty state.");
        }
    }
    @Override
       public void onContentChanged() {
         // TODO Auto-generated method stub
        //super.onContentChanged();
      }
   
	}