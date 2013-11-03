package is.ru.app.puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class PuzzleXmlParser {
	
	private static final String ns = null;
	private List<Puzzle> puzzles = new ArrayList<Puzzle>();
	
	public List<Puzzle> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readChallange(parser);
        } finally {
            in.close();
        }
    }
	
	private List<Puzzle> readChallange(XmlPullParser parser) throws XmlPullParserException, IOException {
	
	    parser.require(XmlPullParser.START_TAG, ns, "challenge");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("puzzle")) {
	        	puzzles.add(readPuzzle(parser));
	        } 
	    }  
	    return puzzles;
	}
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the tag.
	private Puzzle readPuzzle(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "puzzle");
	    String id = parser.getAttributeValue(0);
	    String level = null;
	    String length = null;
	    String setup = null;
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("level")) {
	        	level = readTag(parser, name);
	        } else if (name.equals("length")) {
	        	length = readTag(parser, name);
	        } else if (name.equals("setup")) {
	        	setup = readTag(parser, name);
	        } 
	    }
	    return new Puzzle(id, setup, level, length, false, false);
	}
	
	// Processes title tags in the feed.
	private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, tag);
	    String t = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, tag);
	    return t;
	}
	
	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Puzzle p : puzzles){
			sb.append("level : ").append(p.level).append(" ");
			sb.append("length : ").append(p.length).append(" ");
			sb.append("setup : ").append(p.setup).append(" ");
			sb.append("\n");
		}
		return sb.toString();
	}

	public List<Puzzle> getPuzzles() {
		return puzzles;
	}
}
