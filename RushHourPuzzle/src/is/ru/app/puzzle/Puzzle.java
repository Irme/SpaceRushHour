package is.ru.app.puzzle;

public class Puzzle {
	
	public final String id;
    public final String level;
    public final String length;
    public final String setup;
    public final boolean solved;
    public final boolean playing;

    public Puzzle(String id, String setup, String level, String length, boolean solved, boolean playing) {
    	this.id = id;
        this.level = level;
        this.length = length;
        this.setup = setup;
        this.solved = solved;
        this.playing = playing;
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Puzzle nr. : ").append(id).append("\n");
    	sb.append("level : ").append(level).append("\n");
    	//sb.append("length : ").append(length).append("\n");
    	sb.append("solved : ").append(solved).append("\n");
    	sb.append("playing : ").append(playing).append("\n");
    	return sb.toString();
    }
}
