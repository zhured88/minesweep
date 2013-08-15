package com.example.myminesweep;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TableLayout mineField; // table layout to add mines to
	private int NUM;
	private Block blocks[][];
	private ImageButton btnSmile;
	private TextView txMineCount;
	private TextView tvTimer;

	private int blockDimension;
	private int blockPadding = 2;
	private int mineNum;// 雷的个数
	private int minesToFind; // number of mines yet to be discovered
	private int hardLevel;

	// timer to keep track of time elapsed
	private Handler timer = new Handler();
	private int secondsPassed;// 过去的时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txMineCount = (TextView) findViewById(R.id.minecount);
		tvTimer = (TextView) findViewById(R.id.timer);
		btnSmile = (ImageButton) findViewById(R.id.smiley);
		btnSmile.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				initView();
				statrTimer();
				initBlocks();
				
			}
		});
		initSet();
		initView();
		createBlocks();
		showMineField();

	}

	private void initSet() {
		NUM = 7;
		hardLevel = 8;
		int windowWidth = this.getWindowManager().getDefaultDisplay()
				.getWidth();
		blockDimension = windowWidth / (NUM + 2) - blockPadding;
		mineNum = NUM * NUM / hardLevel;

	}

	private void initView() {
		minesToFind = mineNum;
		updateMineCountDisplay();
		secondsPassed = 0;
		updateTimerDisplay();
	}

	private void updateMineCountDisplay() {
		if (minesToFind >= 0) {
			String CountString;
			if (minesToFind < 10) {
				CountString = "00" + minesToFind;
			} else if (minesToFind < 100) {
				CountString = "0" + minesToFind;
			} else {
				CountString = "" + minesToFind;
			}
			txMineCount.setText(CountString);
		}

	}

	private void updateTimerDisplay() {

		if (secondsPassed >= 0) {
			String CountString;
			if (secondsPassed < 10) {
				CountString = "00" + secondsPassed;
			} else if (secondsPassed < 100) {
				CountString = "0" + secondsPassed;
			} else {
				CountString = "" + secondsPassed;
			}
			tvTimer.setText(CountString);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void createBlocks() {
		blocks = new Block[NUM][NUM];
		for (int row = 0; row < NUM; row++) {
			for (int column = 0; column < NUM; column++) {
				blocks[row][column] = new Block(MainActivity.this);

			}
		}
	}

	private void initBlocks() {

		int[] minePistion = Helper.getRandomArray(mineNum, NUM * NUM - 1);
		int[][] mineNumofSorround = Helper.getNumberOfMinesInSurrounding(
				minePistion, NUM);
		for (int row = 0; row < NUM; row++) {
			for (int column = 0; column < NUM; column++) {
				blocks[row][column].setEnabled(true);
				blocks[row][column].setDefaults();
				blocks[row][column]
						.setNumberOfMinesInSurrounding(mineNumofSorround[row + 1][column + 1]);
				blocks[row][column].setRow(row);
				blocks[row][column].setColumn(column);
				blocks[row][column].setOnClickListener(blockListener);
				blocks[row][column].setOnLongClickListener(blockLongListener);

			}
		}
		for (int i = 0; i < minePistion.length; i++) {
			int row = minePistion[i] / NUM;
			int column = minePistion[i] % NUM;
			blocks[row][column].triggerMine();
		}
	}

	private void showMineField() {
		mineField = (TableLayout) findViewById(R.id.MineField);
		initBlocks();

		for (int row = 0; row < NUM; row++) {
			TableRow tableRow = new TableRow(this);
			// tableRow.setLayoutParams(new LayoutParams((blockDimension + 2 *
			// blockPadding) * (NUM+1), blockDimension + 2 * blockPadding));
			for (int column = 0; column < NUM; column++) {

				blocks[row][column].setLayoutParams(new LayoutParams(
						blockDimension, blockDimension));
				blocks[row][column].setPadding(blockPadding, blockPadding,
						blockPadding, blockPadding);
				tableRow.addView(blocks[row][column]);
			}
			mineField.addView(tableRow);
		}

	}

	private OnClickListener blockListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Block block = (Block) v;
			int rowClicked = block.getRow();
			int columnClicked = block.getColumn();
			rippleUncover(rowClicked, columnClicked);
			if (block.isMined()) {
				finishGame();
			}

			if (checkGameWin()) {
				winGame();
			}

		}

	};

	private OnLongClickListener blockLongListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			Block block = (Block) v;
			if (block.isClickable()) {
				if (!block.isFlagged()) {
					block.setFlagIcon(true);
					block.setFlagged(true);
					minesToFind--;
					updateMineCountDisplay();
				} else if (block.isFlagged() && !block.isQuestionMarked()) {
					block.setQuestionMarkIcon(true);
					block.setQuestionMarked(true);
				} else {
					block.setBlockAsDisabled(true);
					block.clearAllIcons();
					block.setQuestionMarked(false);
					block.setFlagged(false);
					minesToFind++;
					updateMineCountDisplay();
				}

			}

			return true;
		}
	};

	private void rippleUncover(int rowClicked, int columnClicked) {

		if (blocks[rowClicked][columnClicked].isFlagged()) {
			return;
		}
		// open clicked block
		blocks[rowClicked][columnClicked].OpenBlock();
		// if clicked block have nearby mines then don't open further
		if (blocks[rowClicked][columnClicked].getNumberOfMinesInSurrounding() != 0) {
			return;
		}

		// open next 3 rows and 3 columns recursively
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				// check all the above checked conditions
				// if met then open subsequent blocks
				if ((rowClicked + row - 1 >= 0)
						&& (columnClicked + column - 1 >= 0)
						&& (rowClicked + row - 1 < NUM)
						&& (columnClicked + column - 1 < NUM)
						&& blocks[rowClicked + row - 1][columnClicked + column
								- 1].isCovered()) {
					rippleUncover(rowClicked + row - 1, columnClicked + column
							- 1);
				}
			}
		}
		return;

	}

	private void finishGame() {
		// TODO Auto-generated method stub
		stopTimer();
		for (int row = 0; row < NUM; row++) {
			for (int column = 0; column < NUM; column++) {
				blocks[row][column].setEnabled(false);
				if (blocks[row][column].isMined()) {
					blocks[row][column].setMineIcon(false);
				} else {
					blocks[row][column].visableButton();
				}

			}
		}
		btnSmile.setBackgroundResource(R.drawable.sad);
		Toast.makeText(this, "很遗憾，您踩到雷了！", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	private boolean checkGameWin() {
		for (int row = 0; row < NUM; row++) {
			for (int column = 0; column < NUM; column++) {
				if (!blocks[row][column].isMined()
						&& blocks[row][column].isCovered()) {
					return false;
				}
			}
		}
		return true;
	}

	private void winGame() {
		stopTimer();
		// set icon to cool dude
		btnSmile.setBackgroundResource(R.drawable.cool);

		// disable all buttons
		// set flagged all un-flagged blocks
		for (int row = 0; row < NUM; row++) {
			for (int column = 0; column < NUM; column++) {
				blocks[row][column].setClickable(false);
				if (blocks[row][column].isMined()) {
					blocks[row][column].setBlockAsDisabled(false);
					blocks[row][column].setFlagIcon(true);
				}
			}
		}

		// show message
		Toast.makeText(this, "恭喜你赢了！", Toast.LENGTH_LONG).show();
	}
	
	/*******timer**************/
	private void statrTimer(){
		if(secondsPassed==0){
			timer.removeCallbacks(updateTimer);
			timer.postDelayed(updateTimer, 1000);
		}
	}
	
	private void stopTimer(){
		timer.removeCallbacks(updateTimer);
	}
	private Runnable updateTimer =new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			secondsPassed+=1;
			updateTimerDisplay();
			timer.postDelayed(updateTimer, 1000);
		}
		
	};

}
