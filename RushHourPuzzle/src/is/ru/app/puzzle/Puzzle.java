package is.ru.app.puzzle;

public class Puzzle {
	
	public final String id;
    public final String level;
    public final String length;
    public final String setup;

    public Puzzle(String id, String level, String length, String setup) {
    	this.id = id;
        this.level = level;
        this.length = length;
        this.setup = setup;
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Puzzle nr. : ").append(id).append("\n");
    	sb.append("level : ").append(level).append("\n");
    	//sb.append("length : ").append(length).append("\n");
    	return sb.toString();
    }
}
