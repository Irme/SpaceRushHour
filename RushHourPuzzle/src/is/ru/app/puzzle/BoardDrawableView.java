package is.ru.app.puzzle;

import is.ru.app.db.PuzzleAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.spec.IvParameterSpec;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BoardDrawableView extends View {

	private List<ShapeDrawable> shapes = new ArrayList<ShapeDrawable>();
	private PuzzleAdapter mPuzzlesAdapter = new PuzzleAdapter( this.getContext() );

	private int width;
	private boolean rotated = false;
	private int rotations = 0;
	public int puzzleId = 1;

	// Blue colors
	private Integer[] m_colors = { 0xffCC0000, 0xff0099CC, 0xff3399CC,
			0xff6699CC, 0xff6633FF, 0xff0000CC, 0xff0033CC, 0xff6633CC,
			0xff663366, 0xff33FFFF, 0xff339999, 0xff33CCFF, 0xff9999CC, 
			0xff0066FF, 0xff3300FF, 0xff0066CC, 0xff333399, 0xff330066 };

	Toast toast = Toast.makeText(getContext(), "BOING", Toast.LENGTH_SHORT);

	private int heightScreen = 0;
	private int widthScreen = 0;
	private boolean moving = false;
	private int startX = 0, startY = 0, endX = 0, endY = 0;
	private int deltaX = 0, deltaY = 0;
	private Vibrator v;
	private double ratio = 0.85;
	private boolean isSolved = false;

	private static final String puzzleFile = "challenge_classic40.xml";

	// <setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2),
	// (H 4 4 2), (V 5 0 3)</setup>
	Map<String, LinkedList<Integer>> boxes = new LinkedHashMap<String, LinkedList<Integer>>();

	public BoardDrawableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		v = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		//OptionActivity opt = new OptionActivity();

		widthScreen = metrics.widthPixels;
		heightScreen = metrics.heightPixels;
		if(widthScreen > heightScreen){
			heightScreen = (int) (heightScreen);
			widthScreen = heightScreen;
			rotated = true;
		} else {
			heightScreen = widthScreen;
			rotated = false;
		}
	}

	public void setUp(int id){
		puzzleId = id;
		readInPuzzle(id);
		init();
	}

	public void readInPuzzle(int id){
		Cursor cursor = mPuzzlesAdapter.queryPuzzle(id);
		System.out.println("id " + id);
		while(cursor.moveToNext())
		{
			int s_id = cursor.getInt(1);
			System.out.println("s_id " + s_id);
			String s_setup = cursor.getString(2);
			String s_level = String.valueOf(cursor.getInt(3));
			String s_length = String.valueOf(cursor.getInt(4));
			boolean s_solved = cursor.getInt(4) == 0 ? false : true;
			Puzzle puzzle = new Puzzle(String.valueOf(id), s_setup, s_level, s_length, s_solved, true);
		//	mPuzzlesAdapter.updatePuzzleRestPlaying();
			mPuzzlesAdapter.updatePuzzle(s_id, true);
			createBoxes(puzzle);
		}
		cursor.close();
	}


	// <setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2),
	// (H 4 4 2), (V 5 0 3)</setup>
	public void createBoxes(Puzzle puzzle) {
		boxes.clear();
		Pattern pattern = Pattern.compile("\\d+");
		String setup = puzzle.setup;
		LinkedList<String> items = new LinkedList<String>(Arrays.asList(setup.split(",")));

		for (String s : items) {
			LinkedList<Integer> box = new LinkedList<Integer>();
			Matcher m = pattern.matcher(s);
			while (m.find()) {
				String v = m.group();
				box.add(Integer.valueOf(v));
			}
			boxes.put(s.trim(), box);
		}
	}


	protected void init() {
		width = (int)((widthScreen / 6));
		int height = width;
		int x = 0, y = 0;
		int xOffset = width;
		int yOffset = width;
		int count = 0;
		shapes.clear();
		for (Map.Entry<String, LinkedList<Integer>> entry : boxes.entrySet()) {
			String key = entry.getKey();
			LinkedList<Integer> values = entry.getValue();
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
			///	shapeD.getPaint().setShader(makeLinear(m_colors[count++], height));
			} else if (key.startsWith("(V")) {
				shape.set(x, y, x + width, y + span);
				shapeD.setBounds(shape);
			//	shapeD.getPaint().setShader(makeLinear(m_colors[count++], width));
			}
	
			shapeD.getPaint().setColor(m_colors[count++]);
			
			shapes.add(shapeD);
		}
		
	
	/*	ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
		    @Override
		    public Shader resize(int width, int height) {
		        LinearGradient lg = new LinearGradient(0, 0, 0, height,
		            new int[] { 
		                Color.GREEN, 
		                Color.WHITE, 
		                Color.LTGRAY, 
		                Color.DKGRAY }, //substitute the correct colors for these
		            new float[] { 0, 0.45f, 0.55f, 1 },
		            Shader.TileMode.REPEAT);
		         return lg;
		    }
		};
		PaintDrawable p = new PaintDrawable();
		p.setShape(new RectShape());
		p.setShaderFactory(sf);
		shape.setBackground((Drawable)p);*/
	}
	
	private static Shader makeLinear(int color, int height) {
           return new LinearGradient(0, 0, 0, height,
                           new int[] { 0xFFFF0000, color, 0xFF0000FF },
                           new float[] { 0, 0.45f, 1 },
                           Shader.TileMode.REPEAT);
   }

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (ShapeDrawable shape : shapes) {
			shape.draw(canvas);
		}
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            if(isHitBlockTrue(x, y)){
                    final ShapeDrawable bounds = isHitBlock(x, y);
                    int range[] = maxRange(bounds);
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                   // 	v.vibrate(50); //vibrates when blocks are pressed
                            if (bounds != null) {
                                    //        Rect rect = new Rect();
                                    //        rect.set(bounds.getBounds().left, bounds.getBounds().top,
                                    //                        bounds.getBounds().right, bounds.getBounds().bottom);
                                if (bounds.getPaint().getColor() == 0xffCC0000) {
                                	isSolved = isSolved(bounds);
                                	if(isSolved){
                                		puzzleId = puzzleId + 1;
                                		setUp(puzzleId);
                                		invalidate();
                                		return true;
                                	}
                                }
                                    moving = (bounds.getBounds().intersects(x, y, x + 1, y + 1));
                                    invalidate();
                                    

                            }
                            return true;
                    case MotionEvent.ACTION_MOVE:
                            int old_left = bounds.getBounds().left;
                            int old_right = bounds.getBounds().right;
                            int old_top = bounds.getBounds().top;
                            int old_bottom = bounds.getBounds().bottom;
                            if (bounds.getPaint().getColor() == 0xffCC0000) {
                            	isSolved = isSolved(bounds);
                            	if(isSolved){
                            		puzzleId = puzzleId + 1;
                            		setUp(puzzleId);
                            		invalidate();
                            		return true;
                            	}
                            }
                            if( bounds != null && moving ){
                                    final int x_new = (int)event.getX();
                                    final int y_new = (int)event.getY();
                                    if(bounds != null ){
                                            if(bounds.getBounds() != null){
                                                    //width = (int)((widthScreen / 6)*ratio);
                                                    width = Math.abs(bounds.getBounds().left - bounds.getBounds().right);
                                                    int height = Math.abs(bounds.getBounds().bottom - bounds.getBounds().top);
                                                    //TODO: fix bounds

                                                    boolean onlyL = false;
                                                    boolean onlyR = false;
                                                    boolean onlyU = false;
                                                    boolean onlyD = false;
                                            
                                                    if(horizontal(bounds)){
                                                       //     System.out.println(range[0] +" , "+ range[1]);
                                                     //       if(((x_new - width/2) > (x_new - width/2- range[0])) && ((x_new + width/2) <(x_new + width/2 + range[1])) && !onlyR && !onlyL && !onlyU && !onlyD){
                                                                    bounds.setBounds(x_new - width/2,
                                                                                    old_top,
                                                                                    x_new + width/2,
                                                                                    old_bottom);
                                                                    invalidate();
//                                                            } if ((range[0]<=0) && (x_new > bounds.getBounds().centerX()))  {
//                                                                    bounds.setBounds(x_new - width/2,
//                                                                                    old_top,
//                                                                                    x_new + width/2,
//                                                                                    old_bottom);
//                                                                    invalidate();
//                                                                    return true;
//                                                            } else if((range[1]<=0) && (x_new < bounds.getBounds().centerX())){
//                                                                    bounds.setBounds(x_new - width/2,
//                                                                                    old_top,
//                                                                                    x_new + width/2,
//                                                                                    old_bottom);
//                                                                    invalidate();
//                                                                    return true;
//
//                                                            }
                                                    }else{
                                                           // System.out.println(range[0] +" , "+ range[1]);
                                                         //   if((y_new - height/2 > y_new - height/2 - range[0]) &&(y_new + height/2<  y_new + height/2 + range[1])){
                                                                    bounds.setBounds(old_left,
                                                                                    y_new - height/2, 
                                                                                    old_right, 
                                                                                    y_new+ height/2);
                                                                    invalidate();
//                                                            }
//                                                            else if((range[0]<=0) && (y_new > bounds.getBounds().centerY())){
//                                                                    bounds.setBounds(old_left,
//                                                                                    y_new - height/2, 
//                                                                                    old_right, 
//                                                                                    y_new+ height/2);
//                                                                    invalidate();
//                                                                    return true;
//
//                                                            } else if((range[1]<=0) && (y_new < bounds.getBounds().centerY())) {
//                                                                    bounds.setBounds(old_left,
//                                                                                    y_new - height/2, 
//                                                                                    old_right, 
//                                                                                    y_new+ height/2);
//                                                                    invalidate();
//                                                                    return true;
//                                                            }

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
            else{
                    return false;
            }
    }     


	
	private int [] maxRange(ShapeDrawable shape){
		//First entry is left/up moving range, second entry is right/down moving range.
		if(shape != null){
			int maxleft = widthScreen;
			int maxright = widthScreen;
			int maxtop = 0;
			int maxbottom = getHeight();
			int new_max_left = Integer.MAX_VALUE;
			int new_max_right = Integer.MAX_VALUE;
			int [] range = new int[2];
			for (ShapeDrawable shape2 : shapes){
				if(!shape.equals(shape2)){

					if(horizontal(shape) == true){
								
							if((shape.getBounds().top < shape2.getBounds().bottom) && (shape.getBounds().bottom > shape2.getBounds().top) || 
								(shape.getBounds().top < shape2.getBounds().bottom) && (shape.getBounds().bottom < shape2.getBounds().top)){
								//It's to the right from our current brick

									maxleft = Math.min(maxleft, (shape.getBounds().left - shape2.getBounds().right));
									//System.out.println("horizontal right bound detected");
									new_max_left = Math.min(maxleft, new_max_left);
																
									maxright = Math.min(maxright, shape.getBounds().right - shape2.getBounds().left);
									
									new_max_right = Math.min(maxright, new_max_right);
									
									range [0] = new_max_left;
									range [1] = new_max_right;
							}
							
					} else {
						maxtop = getHeight();
						maxbottom = getHeight();

							if((shape.getBounds().top < shape2.getBounds().bottom) && (shape.getBounds().bottom > shape2.getBounds().top)){
								//It's to the right from our current brick
								maxbottom = Math.min(maxbottom, Math.abs((shape2.getBounds().top - shape2.getBounds().top)));
								//System.out.println("horizontal right bound detected");

								maxtop = Math.min(maxtop, Math.abs((shape2.getBounds().bottom - shape.getBounds().top)));
								range [0] = maxtop;
								range [1] = maxbottom;
							}

					}

				}

			}
			return range;
		}
		return null;
	}
	/*
	private int [] maxRange(ShapeDrawable shape){
		//First entry is left/up moving range, second entry is right/down moving range.
		if(shape != null){
			int maxleft = 100000;
			int maxright = 10000;
			int [] range = new int[2];
			for (ShapeDrawable shape2 : shapes){
				if(!shape.equals(shape2)){

					if(horizontal(shape) == true){
						maxleft = shape.getBounds().left;
						maxright = widthScreen - shape.getBounds().right;
						if(((shape.getBounds().top <= shape2.getBounds().top) && (shape2.getBounds().top < shape.getBounds().bottom))|| 
								((shape.getBounds().top < shape2.getBounds().bottom) && (shape2.getBounds().bottom <=shape.getBounds().bottom))||
								(((shape2.getBounds().top <= shape.getBounds().top) && (shape.getBounds().bottom <= shape2.getBounds().bottom)))){



							if((shape2.getBounds().left - shape.getBounds().right) < (shape.getBounds().left - shape2.getBounds().right)){
								//It's to the right from our current brick
								maxleft = Math.min(maxleft,Math.abs((shape.getBounds().left-shape2.getBounds().right)));
								//System.out.println("horizontal right bound detected");
							} else {
								// It's to the left obviously

								maxright = Math.min(maxright, Math.abs((shape2.getBounds().left-shape.getBounds().right)));
							}

						}


					} else {
						maxleft = shape.getBounds().top;
						maxright = widthScreen - shape.getBounds().bottom;
						if(((shape.getBounds().left <= shape2.getBounds().left) && (shape2.getBounds().left <shape.getBounds().right))||
								((shape.getBounds().left < shape2.getBounds().right) && (shape2.getBounds().right <=shape.getBounds().right))
								||((shape2.getBounds().left <= shape.getBounds().left)&&(shape.getBounds().right <= shape2.getBounds().right))){


							if((shape2.getBounds().bottom -shape.getBounds().top)<(shape.getBounds().bottom -shape2.getBounds().top)){
								//It's above from our current brick
								maxleft = Math.min(maxleft, Math.abs((shape2.getBounds().bottom-shape.getBounds().top)));

							} else{

								// It's below
								maxright = Math.min(maxright, Math.abs((shape.getBounds().bottom-shape2.getBounds().top)));

							}

						}

					}
					range [0] = maxleft;
					range [1] = maxright;
				}

			}
			return range;
		}
		return null;
	}*/

	private ShapeDrawable isHitBlock(int x, int y) {
		for (ShapeDrawable shape : shapes) {
			Rect bounds = shape.getBounds();
			if (bounds.contains(x, y)) {
				return shape;
			}
		}
		return null;
	}

	private boolean isHitBlockTrue(int x, int y) {
		for (ShapeDrawable shape : shapes) {
			Rect bounds = shape.getBounds();
			if (bounds.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Simple method to see if a block is horizontally positioned or vertically.
	 * returns true if it is horizontal.
	 */
	private boolean horizontal(ShapeDrawable rect1) {
		int diffh = Math.abs(rect1.getBounds().left - rect1.getBounds().right);
		int diffv = Math.abs(rect1.getBounds().bottom - rect1.getBounds().top);
		if (diffv > diffh) {
			return false;
		} else {
			return true;
		}

	}

	public String toString(){
		StringBuilder s = new StringBuilder();
		for(ShapeDrawable shape : shapes){
			s.append(shape.getBounds().left);
			s.append(":");
			s.append(shape.getBounds().top);
			s.append(":");
			s.append(shape.getBounds().right);
			s.append(":");
			s.append(shape.getBounds().bottom);
			s.append(",");
		}
		
		return s.toString();

	}

	public void resetShapes (String s){

		//ArrayList<ShapeDrawable> sh = new ArrayList<ShapeDrawable>();
		String[] temp1 = s.split(",");
		shapes.clear();

		int count = 0;
			for(int j = 0; j < temp1.length; j++){
				//System.out.println(temp1[j]);
				String[] temp2 = temp1[j].split(":");
				for (int i = 0; i < temp2.length; i = i+4) {
					ShapeDrawable tempshape = new ShapeDrawable();
					tempshape.setBounds(Integer.parseInt(temp2[i]), Integer.parseInt(temp2[i+1]), Integer.parseInt(temp2[i+2]),Integer.parseInt(temp2[i+3]));
					//System.out.println("Left : " + temp2[i]);
					//System.out.println("top : " + temp2[i+1]);
					//System.out.println("right : " + temp2[i+2]);
					//System.out.println("Bottom : " + temp2[i+3]);
					tempshape.getPaint().setColor(m_colors[count++]);
					//System.out.println(i);
					//System.out.println(count);
					shapes.add(tempshape);
					//temp2 = null;

				}
			}


		invalidate();


	}


	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		System.out.println(rotations);
		if((rotations%2 == 1)){

			for (ShapeDrawable shape : shapes) {
				shape.setBounds((int)(shape.getBounds().left*ratio), (int)(shape.getBounds().top*ratio), (int)(shape.getBounds().right*ratio), (int)(shape.getBounds().bottom*ratio));
			} 

		} else if ((rotations%2 == 0) && rotations > 0){
			for (ShapeDrawable shape : shapes) {
				shape.setBounds((int)(shape.getBounds().left*(1/ratio)), (int)(shape.getBounds().top*(1/ratio)), (int)(shape.getBounds().right*(1/ratio)), (int)(shape.getBounds().bottom*(1/ratio)));
			}
		}
	}

	public boolean isSolved(ShapeDrawable shape){
		
		if(shape.getBounds().right > widthScreen){
			mPuzzlesAdapter.updatePuzzleSolved(puzzleId);
			mPuzzlesAdapter.close();
			return true;
		}
		return false;
	}
}
