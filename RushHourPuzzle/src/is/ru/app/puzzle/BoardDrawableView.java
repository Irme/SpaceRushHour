package is.ru.app.puzzle;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.DisplayMetrics;
import android.view.View;

public class BoardDrawableView extends View {
	private List<ShapeDrawable> shapes = new ArrayList<ShapeDrawable>();
	private Integer[] mColors =	{ 0xff74AC23, 0xff74AC73, 0xff74AC26, 0xff74AC13, 0xff84AC13, 0xffffBC13 };
	int heightScreen = 0;
	int widthScreen = 0;
	//<setup>(H 1 2 2), (V 0 1 3), (H 0 0 2), (V 3 1 3), (H 2 5 3), (V 0 4 2), (H 4 4 2), (V 5 0 3)</setup>
	private int[][] board = {{1, 1, 1, 1, 1, 1},
			 				 {1, 0, 2, 0, 0, 0},
			 				 {0, 0, 2, 0, 0, 1},
			 				 {0, 1, 1, 1, 0, 1},
			 				 {0, 0, 0, 0, 1, 1},
			 				 {1, 1, 1, 0, 1, 0}};

	public BoardDrawableView(Context context) {
		super(context);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();         
		widthScreen = metrics.widthPixels;
		heightScreen = metrics.heightPixels;
		init();
	}

	protected void init(){
		int width = widthScreen / 6;
		int height = width;
		int x = 0;
		int y = 0;
		int xOffset = width;
		int yOffset = width;
		for (int i = 0; i < board.length; i++) {
		
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j] == 1 || board[i][j] == 2){					
					Shape shape = new RectShape(); // Or other Shape
					ShapeDrawable shapeD = new ShapeDrawable(shape);
		
					shapeD.setBounds(x, y, x + width, y + height);
					shapeD.getPaint().setColor(mColors[i]);
					shapeD.setPadding(20, 20, 20, 20);
					shapes.add(shapeD);
				}
				y =+ y + yOffset;
			}	
			x =+ x + xOffset;
			y = 0;
		}
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for(ShapeDrawable shape: shapes) {
			shape.draw(canvas);
		}
	}
}
