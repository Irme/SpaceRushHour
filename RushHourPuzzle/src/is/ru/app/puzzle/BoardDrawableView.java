package is.ru.app.puzzle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BoardDrawableView extends View {
	private List<ShapeDrawable> shapes = new ArrayList<ShapeDrawable>();

	//private Integer[] m_colors = { 0xff74AC23, 0xff74AB73, 0xff74DD26,
	//		0xff74AC13, 0xff84AC13, 0xffffBC13, 0xff74AC00, 0xffAAAC73 };
	
	//Blue colors
	private Integer[] m_colors = { 0xff99CCCC, 0xff0099CC, 0xff3399CC, 0xff6699CC, 0xff6633FF, 
								   0xff0000CC, 0xff0033CC, 0xff6633CC, 0xff663366 };
	
	private Integer[] m_green_colors = { 0xff33CC99, 0xff66CC99, 0xff99CC99, 0xff009966,
										 0xff339966, 0xff669966, 0xff999966, 0xff999933,
										 0xff999900, 0xff669933, 0xff666633, 0xff666600,
										 0xff336633, 0xff336600, 0xff006633, 0xff006600,
										 0xff006666, 0xff336666 };
	
	Toast toast = Toast.makeText(getContext(), "BOING", Toast.LENGTH_SHORT);

	
	
	int heightScreen = 0;
	int widthScreen = 0;
	boolean moving = false;
	int startX = 0, startY = 0, endX = 0, endY = 0;
	int deltaX = 0, deltaY = 0;
	
	private static final String puzzleFile = "challenge_classic40.xml";
	
	// <setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2),
	// (H 4 4 2), (V 5 0 3)</setup>
	Map<String, ArrayList<Integer>> boxes = new HashMap<String, ArrayList<Integer>>();

	public BoardDrawableView(Context context) {
		super(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		widthScreen = metrics.widthPixels;
		heightScreen = metrics.heightPixels;
		readInPuzzle(puzzleFile);
		init();
	}
	
	public void readInPuzzle(String puzzleFile){
		PuzzleXmlParser xmlParser = new PuzzleXmlParser();
		try {
			InputStream in = getContext().getAssets().open(puzzleFile);
			xmlParser.parse(in);
			createBoxes(xmlParser);
		} catch (IOException e ) {
			e.printStackTrace();
		} catch ( XmlPullParserException xmlEx) {
			xmlEx.printStackTrace();
		}
	}

	// <setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2),
	// (H 4 4 2), (V 5 0 3)</setup>
	public void createBoxes(PuzzleXmlParser xmlParser) {
		List<Puzzle> puzzles = xmlParser.getPuzzles();
		Pattern pattern = Pattern.compile("\\d+");
		for(Puzzle p : puzzles){
			String setup = p.setup;
			ArrayList<String> items = new  ArrayList<String>(Arrays.asList(setup.split(",")));
			
			for(String s : items){
				ArrayList<Integer> box = new ArrayList<Integer>();
				Matcher m = pattern.matcher(s);
				while (m.find()) {
					String v = m.group();
					box.add(Integer.valueOf(v));
				}
				boxes.put(s.trim(), box);
			}
			break; //We are at the moment only reading the first puzzle!!!
		}
		
	/*	Iterator<Map.Entry<String, ArrayList<Integer>>> i = boxes.entrySet().iterator(); 
		while(i.hasNext()){
		    String key = i.next().getKey();
		    
		    System.out.println(key + ", "+boxes.get(key) );
		}*/
		
	}

	protected void init() {
		int width = widthScreen / 6;
		int height = width;
		int x = 0, y = 0;
		int xOffset = width;
		int yOffset = width;
		int count = 0;
		
		for (Map.Entry<String, ArrayList<Integer>> entry : boxes.entrySet()) {
			String key = entry.getKey();
			ArrayList<Integer> values = entry.getValue();
			x = values.get(0);
			y = values.get(1);
			int span = values.get(2);
			span = span * width;
			Rect shape = new Rect(); // Or other Shape
			ShapeDrawable shapeD = new ShapeDrawable();
			y = y * yOffset;
			x = x * xOffset;
			if (key.startsWith("(H")) {
				shape.set(x, y, x + span, y + height);
				shapeD.setBounds(shape);
			} else if (key.startsWith("(V")) {
				shape.set(x, y, x + width, y + span);
				shapeD.setBounds(shape);
			}
			shapeD.getPaint().setColor(m_colors[count]); 
			// shapeD.setPadding(20, 20, 20, 20);
			shapes.add(shapeD);
			count++;
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (ShapeDrawable shape : shapes) {
			shape.draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int x = (int)event.getX();
		final int y = (int)event.getY();
		final ShapeDrawable bounds = isHitBlock(x, y);
		switch( event.getAction() ){
		case MotionEvent.ACTION_DOWN:
			if(bounds != null){
				Rect rect = new Rect();
				rect.set(bounds.getBounds().left, bounds.getBounds().top, bounds.getBounds().right, bounds.getBounds().bottom);
				moving = rect.intersects(x, y, x+1, y+1);
				invalidate();
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			if( bounds != null && moving ){
				final int x_new = (int)event.getX();
				final int y_new = (int)event.getY();
				if(bounds != null ){
					if(bounds.getBounds() != null){
						int with = Math.abs(bounds.getBounds().left - bounds.getBounds().right);
						int height = Math.abs(bounds.getBounds().bottom - bounds.getBounds().top);
						//TODO: fix bounds
						for (ShapeDrawable shape  : shapes){
							if(collision(bounds, shape)){
								moving = false;
							}
							else{	
								if(horizontal(bounds)){
								bounds.setBounds(
										x_new - with/2,
										bounds.getBounds().top,
										x_new + with/2,
										bounds.getBounds().bottom);
								invalidate();
								}
								else{
									bounds.setBounds(
											bounds.getBounds().left,
											y_new - height/2,
											bounds.getBounds().right,
											y_new + height/2);
									invalidate();
									
								}
							}

						}
					} 
				}
			}
			return true;
		case MotionEvent.ACTION_UP:
			moving = false;
			return true;
		}
		return false;
	}        
	@SuppressWarnings("static-access")
	private boolean collision(ShapeDrawable shape1, ShapeDrawable shape2){
		if(shape1.getBounds() != shape2.getBounds()){
			Rect rect1 = new Rect(shape1.getBounds().left, shape1.getBounds().top, shape1.getBounds().right, shape1.getBounds().bottom);
			Rect rect2 = new Rect(shape2.getBounds().left, shape2.getBounds().top, shape2.getBounds().right, shape2.getBounds().bottom);
			
			return rect1.intersects(rect1, rect2);
		}

		return false;

	}

	private ShapeDrawable isHitBlock(int x, int y) {
		for (ShapeDrawable shape : shapes) {
			Rect bounds = shape.getBounds();
			if (bounds.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}
	
	/*
	 * Simple method to see if a block is horizontally positioned or vertically.
	 * returns true if it is horizontal.
	 */
	private boolean horizontal(ShapeDrawable rect1){
		int diffh = Math.abs(rect1.getBounds().left - rect1.getBounds().right);
		int diffv = Math.abs(rect1.getBounds().bottom - rect1.getBounds().top);
		if(diffv > diffh){
			return false;
		} else{
			return true;
		}
	}
}
