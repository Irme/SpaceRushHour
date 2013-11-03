package is.ru.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	public static final String DB_NAME = "PUZZLE_DB";
	public static final int DB_VERSION = 1;
	public static final String TablePuzzles = "puzzles";
	public static final String TableOptions = "options";
	public static final String[] TableOptionsCols = { "_id", "sid", "value"};
	public static final String[] TablePuzzlesCols = { "_id", "sid", "setup", "level", "solved", "playing"};
	
	private static final String sqlCreateTablePuzzles = "CREATE TABLE puzzles " +
														"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
														"sid INTEGER NOT NULL," + 
														"setup TEXT," + 
														"level INTEGER, " +
														"solved INTEGER, " +
														"playing INTEGER);";
	
	private static final String sqlDropTableOptions = "DROP TABLE IF EXISTS options";
	
	private static final String sqlCreateTableOPtions = "CREATE TABLE options " +
			"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"sid INTEGER NOT NULL," + 
			"value INTEGER);";

private static final String sqlDropTablePuzzles = "DROP TABLE IF EXISTS puzzles";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sqlCreateTablePuzzles);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(sqlDropTablePuzzles);
		onCreate(db);
	}
}
