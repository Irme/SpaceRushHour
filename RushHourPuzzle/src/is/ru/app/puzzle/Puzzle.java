package is.ru.app.puzzle;

public class Puzzle {
    public final String level;
    public final String length;
    public final String setup;

    public Puzzle(String level, String length, String setup) {
        this.level = level;
        this.length = length;
        this.setup = setup;
    }
}
