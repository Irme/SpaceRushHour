package is.ru.app.puzzle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	/* 
	 your implementation of onCreate() should define the user interface and 
	 possibly instantiate some class-scope variables.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void buttonPlay(View view) {
		//open puzzle from here
	    Intent intent = new Intent(this, PuzzleActivity.class);
	    startActivity(intent);
	}
	
	public void buttonPuzzleList(View view) {
		//open puzzle from here
	    Intent intent = new Intent(this, ListPuzzlesActivity.class);
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
}
