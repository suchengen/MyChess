package com.example.gobang;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class GobangView extends View implements Runnable{
	
	protected static int GRID_SIZE = 10;     //棋盘的格数
	protected static int GRID_WIDTH = 70;    //棋盘格的宽度
	protected static int CHESS_DIAMETER = 26;//棋子的直径
	protected static int mStartX;            //棋盘定位的左上角X
	protected static int mStartY;            //棋盘定位的左上角Y
	private static int[][] mGridArray;       //网格
	boolean key = false;
	
	int wbflag = 1;
	int mLevel = 1;
	int mWinFlag = 0;
	private final int BLACK = 1;
	private final int WHITE = 2;
	
	static final int GAMESTATE_PRE = 0;
	static final int GAMESTATE_RUN = 1;
	static final int GAMESTATE_PAUSE = 2;
	static final int GAMESTATE_END = 3;
	int mGameState = GAMESTATE_RUN;
	
	private Bitmap btm1;
	private final Paint mPaint = new Paint();
	
	public GobangView(Context context) {
		super(context);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		init();
	}
	
	public void init(){
		
		mGameState = 1;
		wbflag = BLACK;
		mWinFlag = 0;
		mGridArray = new int[GRID_SIZE][GRID_SIZE];
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++){
				mGridArray[i][j] = 0;
			}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(mGameState){
		case GAMESTATE_PRE:
			break;
		case GAMESTATE_RUN:{
			int x;
			int y;
			float x0 = GRID_WIDTH - (event.getX() - mStartX) % GRID_WIDTH;
			float y0 = GRID_WIDTH - (event.getY() - mStartY) % GRID_WIDTH;
			if(x0 < GRID_WIDTH / 2){
				x = (int)((event.getX() - mStartX) / GRID_WIDTH);
			}else{
				x = (int)((event.getX() - mStartX) / GRID_WIDTH) - 1;
			}
			if(y0 < GRID_WIDTH / 2){
				y = (int)((event.getY() - mStartY) / GRID_WIDTH);
			}else{
				y = (int)((event.getY() - mStartY) / GRID_WIDTH) - 1;
			}
			if((x >= 0 && x < GRID_SIZE) &&
					(y >= 0 && y < GRID_SIZE)){
				if(mGridArray[x][y] == 0){
					if(wbflag == BLACK){
						putChess(x, y, BLACK);
						if(checkWin(BLACK)){
							mGameState = GAMESTATE_END;
						}else if(checkFull()){
							mGameState = GAMESTATE_END;
						}
						wbflag = WHITE;
					}else if(wbflag == WHITE){
						putChess(x, y, WHITE);
						if(checkWin(WHITE)){
							mGameState = GAMESTATE_END;
						}else if(checkFull()){
							mGameState = GAMESTATE_END;
						}
						wbflag = BLACK;
					}
				}
			}
			break;
		}
		case GAMESTATE_PAUSE:
			break;
		case GAMESTATE_END:
			break;
		}
		this.invalidate();
		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mStartX = w / 2 - GRID_SIZE * GRID_WIDTH / 2;
		mStartY = h / 2 - GRID_SIZE * GRID_WIDTH / 2;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//画背景
		canvas.drawColor(Color.YELLOW);
		
		Paint paintRect = new Paint();
		paintRect.setColor(Color.GRAY);
		paintRect.setStrokeWidth(2);
		paintRect.setStyle(Style.STROKE);
		//画棋盘
		for(int i = 0; i < GRID_SIZE; i++){
			canvas.drawLine(mStartX, i * GRID_WIDTH + mStartY, 
					GRID_SIZE * GRID_WIDTH + mStartX, i * GRID_WIDTH + mStartY, 
					paintRect);
			canvas.drawLine(i * GRID_WIDTH + mStartX, mStartY, 
					i * GRID_WIDTH + mStartX, GRID_SIZE * GRID_WIDTH + mStartY, 
					paintRect);
		}
		//画边框
		paintRect.setStrokeWidth(4);
		canvas.drawRect(mStartX, mStartY, mStartX + GRID_WIDTH*GRID_SIZE, mStartY + GRID_WIDTH*GRID_SIZE, paintRect);
		//画棋子
		for(int i = 0; i < GRID_SIZE; i++){
			for(int j = 0;j < GRID_SIZE; j++){
				Paint paintCircle = new Paint();
				if(mGridArray[i][j] == BLACK){
					paintCircle.setColor(Color.BLACK);
				}else if(mGridArray[i][j] == WHITE){
					paintCircle.setColor(Color.WHITE);
				}
				canvas.drawCircle(mStartX + (i+1) * GRID_WIDTH, mStartY + (j+1) * GRID_WIDTH,
						CHESS_DIAMETER/2, paintCircle);
			}
		}
	}
	
	public void putChess(int x, int y, int blackwhite){
		mGridArray[x][y] = blackwhite;
	}
	//检测胜利
	public boolean checkWin(int wbflag){
		
		if(mWinFlag == wbflag){
			return true;
		}else{
			return false;
		}
	}
	//检查棋盘是否满了
	public boolean checkFull(){
		int mNotEmpty = 0;
		for(int i = 0; i < GRID_SIZE; i++)
			for(int j = 0; j < GRID_SIZE; j++){
				if(mGridArray[i][j] != 0) mNotEmpty += 1;
			}
		if(mNotEmpty == (GRID_SIZE) * (GRID_SIZE))
			return true;
		else
			return false;
	}
	
}
