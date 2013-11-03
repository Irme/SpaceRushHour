package is.ru.app.puzzle;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import is.ru.app.db.PuzzleAdapter;
import is.ru.app.db.DBHelper;


public class ListPuzzlesActivity extends ListActivity{
	
    private PuzzleAdapter mPuzzlesAdapter = new PuzzleAdapter( this );
    private SimpleCursorAdapter mCursorAdapter;
	
	public ListPuzzlesActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.puzzle_list);
   
        Cursor cursor = mPuzzlesAdapter.queryPuzzles();
  
        String[] cols = DBHelper.TablePuzzlesCols;
        String from[] = { cols[1], cols[3], cols[4] };
        int to[] = { R.id.s_sid, R.id.s_level, R.id.s_solved };
        startManagingCursor( cursor );
        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to );
        mCursorAdapter.setViewBinder( new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if ( i==4 ) {
                    ((ImageView) view).setImageResource(
                            (cursor.getInt(i) == 0) ? R.drawable.btn_check_buttonless_off : R.drawable.btn_check_buttonless_on );
                    return true;
                }
                return false;
            }
        });
        setListAdapter( mCursorAdapter );
        cursor.close();
	 }
	
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		 String playing = R.string.playing_text + " " + (position + 1);
		 Toast.makeText(getApplicationContext(), playing, Toast.LENGTH_LONG).show();
		 mPuzzlesAdapter.updatePuzzleRestPlaying();
		 mPuzzlesAdapter.updatePuzzle((int)(position + 1), true);
		 Intent intent = new Intent(this, PuzzleActivity.class);
		 startActivity(intent);
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
	        mCursorAdapter.getCursor().requery();
	    }
	    
	    @Override
	    protected void onDestroy() {
			super.onDestroy();
			mPuzzlesAdapter.close();
		}
	
	 
	/* private class PuzzleCursorAdapter extends SimpleCursorAdapter {
		 
		 private List<Puzzle> puzzles = new ArrayList<Puzzle>();

		public PuzzleCursorAdapter(Context context, int textViewResourceId, Cursor cursor, String[] from, int[] to ) {
			super(context, textViewResourceId, cursor, from, to);
			for(int i = 0; i < cursor.getCount(); i++){//String id, String level, String length, String setup
				Puzzle puzzle = new Puzzle(String.valueOf(cursor.getInt(i)), String.valueOf(cursor.getInt(i)), "0", cursor.getString(i));
				this.puzzles.add(puzzle);
			}
		}
			
		@Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			TextView result;
			System.out.println("dfshfksdhfskdhf");
            if (convertView == null) {
                result = (TextView) getLayoutInflater().inflate(R.layout.row, parent, false);
            } else {
                result = (TextView) convertView;
            }

            final Puzzle puzzle = getItem(position);
            result.setText(puzzle.toString());

            return result;
		}
		
		@Override
		public Puzzle getItem(int pos){
			return puzzles.get(pos);
		}
	 }*/
}
