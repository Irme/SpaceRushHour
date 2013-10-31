package is.ru.app.puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;

public class LoadPuzzles {

	private static LoadPuzzles puzzles = null;
	private static List<Puzzle> puzzleList = new ArrayList<Puzzle>();
	private static final String puzzleFile = "challenge_classic40.xml";
	
	
	private LoadPuzzles(){
		init();
	}
	
	public static LoadPuzzles getInstance(){
		
		if(puzzles == null){
			puzzles = new LoadPuzzles();
		}
		return puzzles;	
	}
	
	private void init(){
		readInPuzzle(puzzleFile);
	}
	
	public void readInPuzzle(String puzzleFile){
		PuzzleXmlParser xmlParser = new PuzzleXmlParser();
		try {
			InputStream in = null; //getApplicationContext().getAssets().open(puzzleFile);
			xmlParser.parse(in);
			puzzleList = xmlParser.getPuzzles();
		} catch (IOException e ) {
			e.printStackTrace();
		} catch ( XmlPullParserException xmlEx) {
			xmlEx.printStackTrace();
		}
	}

	public List<Puzzle> getPuzzleList() {
		return puzzleList;
	}
}
