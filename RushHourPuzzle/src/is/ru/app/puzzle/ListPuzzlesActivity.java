package is.ru.app.puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class ListPuzzlesActivity extends ListActivity{
	
	private List<Puzzle> puzzles = new ArrayList<Puzzle>();
	private static final String puzzleFile = "challenge_classic40.xml";
	
	public ListPuzzlesActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.puzzle_list);
		readInPuzzle(puzzleFile);
	//	final ListView listview = (ListView) findViewById(R.id.list);

		PuzzleAdapter adapter = new PuzzleAdapter(this, android.R.layout.simple_list_item_1, puzzles);
	    setListAdapter(adapter);
	    
	 /*   listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				 Toast.makeText(getApplicationContext(),"Playing Puzzle " + (id + 1), Toast.LENGTH_LONG).show();
				 Intent intent = new Intent(ListPuzzlesActivity.this, PuzzleActivity.class);
				 intent.putExtra("id", (int)id);
				 startActivity(intent);
				}
	    });*/
	 }
	
	 @Override
	 public void onListItemClick(ListView l, View v, int position, long id) {
		 Toast.makeText(getApplicationContext(),"Playing Puzzle " + (id + 1), Toast.LENGTH_LONG).show();
		 Intent intent = new Intent(this, PuzzleActivity.class);
		 intent.putExtra("id", (int)id);
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
	    }
	
	 public void readInPuzzle(String puzzleFile){
		PuzzleXmlParser xmlParser = new PuzzleXmlParser();
		try {
			InputStream in = getBaseContext().getAssets().open(puzzleFile);
			xmlParser.parse(in);
			puzzles = xmlParser.getPuzzles();
		} catch (IOException e ) {
			e.printStackTrace();
		} catch ( XmlPullParserException xmlEx) {
			xmlEx.printStackTrace();
		}
	}
	 
	 private class PuzzleAdapter extends ArrayAdapter<Puzzle> {
		 
		 private List<Puzzle> puzzles = new ArrayList<Puzzle>();

		public PuzzleAdapter(Context context, int textViewResourceId, List<Puzzle> objects) {
			super(context, textViewResourceId, objects);
			this.puzzles = objects;
		}
		
	
		@Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			TextView result;

            if (convertView == null) {
                result = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                result = (TextView) convertView;
            }

            final Puzzle puzzle = getItem(position);
            result.setText(puzzle.toString() );

            return result;
			
		}
		
		@Override
		public Puzzle getItem(int pos){
			return puzzles.get(pos);
		}
		 
	 }

}
