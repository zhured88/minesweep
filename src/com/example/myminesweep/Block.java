package com.example.myminesweep;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class Block extends Button {

	private boolean isCovered; // is block covered yet
	private boolean isMined; // does the block has a mine underneath
	private boolean isFlagged; // is block flagged as a potential mine
	private boolean isQuestionMarked; // is block question marked
	private boolean isClickable; // can block accept click events
	private int numberOfMinesInSurrounding; // number of mines in nearby blocks
	private int column;
	private int row;

	public Block(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}
	

	public Block(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Block(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	

	public int getColumn() {
		return column;
	}


	public void setColumn(int column) {
		this.column = column;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	// set default properties for the block
	public void setDefaults() {
		isCovered = true;
		isMined = false;
		isFlagged = false;
		isQuestionMarked = false;
		isClickable = true;
		numberOfMinesInSurrounding = 0;
		this.setText("");
		this.setBackgroundResource(R.drawable.square_blue);
		setBoldFont();
	}

	// set font as bold
	private void setBoldFont() {
		this.setTypeface(null, Typeface.BOLD);
	}

	public boolean isCovered() {
		return isCovered;
	}

	public boolean isMined() {
		return isMined;
	}

	public void setMined(boolean isMined) {
		this.isMined = isMined;

	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged =isFlagged;
		
	}

	public boolean isQuestionMarked() {
		return isQuestionMarked;
	}

	public void setQuestionMarked(boolean isQuestionMarked) {
		this.isQuestionMarked =isQuestionMarked;
		
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}

	public int getNumberOfMinesInSurrounding() {
		return numberOfMinesInSurrounding;
	}

	public void setNumberOfMinesInSurrounding(int numberOfMinesInSurrounding) {
		this.numberOfMinesInSurrounding = numberOfMinesInSurrounding;
	}

	// uncover this block
	public void OpenBlock() {
		if (!isCovered) {
			return;
		}
		setBlockAsDisabled(false);
		setCovered(false);
		// check if it has mine
		if (isMined) {
			setMineIcon(false);
		} else {
			visableButton();
		}

	}

	// set mine icon for block
	// set block as disabled/opened if false is passed
	public void setMineIcon(boolean enabled) {
		this.setText("M");

		if (!enabled) {
			this.setBackgroundResource(R.drawable.square_grey);
			this.setTextColor(Color.RED);
		} else {
			this.setTextColor(Color.BLACK);
		}
	}

	public void visableButton() {
		if (numberOfMinesInSurrounding > 0) {
			updateNumber(numberOfMinesInSurrounding);
		}
	}

	public void setBlockAsDisabled(boolean flag) {
		if (!flag) {
			this.setBackgroundResource(R.drawable.square_grey);
		} else {
			this.setBackgroundResource(R.drawable.square_blue);
		}
	}

	// set text as nearby mine count
	public void updateNumber(int text) {
		if (text != 0) {
			this.setText(Integer.toString(text));

			// select different color for each number
			// we have already skipped 0 mine count
			switch (text) {
			case 1:
				this.setTextColor(Color.BLUE);
				break;
			case 2:
				this.setTextColor(Color.rgb(0, 100, 0));
				break;
			case 3:
				this.setTextColor(Color.RED);
				break;
			case 4:
				this.setTextColor(Color.rgb(85, 26, 139));
				break;
			case 5:
				this.setTextColor(Color.rgb(139, 28, 98));
				break;
			case 6:
				this.setTextColor(Color.rgb(238, 173, 14));
				break;
			case 7:
				this.setTextColor(Color.rgb(47, 79, 79));
				break;
			case 8:
				this.setTextColor(Color.rgb(71, 71, 71));
				break;
			case 9:
				this.setTextColor(Color.rgb(205, 205, 0));
				break;
			}
		}
	}

	// set block as a mine underneath
	public void plantMine() {
		isMined = true;
	}

	// mine was opened
	// change the block icon and color
	public void triggerMine() {
		setMined(true);
		this.setTextColor(Color.RED);
	}

	public void setCovered(boolean isCovered) {
		this.isCovered = isCovered;
	}
	
	// set mine as flagged
		// set block as disabled/opened if false is passed
		public void setFlagIcon(boolean enabled)
		{
			this.setText("F");

			if (!enabled)
			{
				this.setBackgroundResource(R.drawable.square_grey);
				this.setTextColor(Color.RED);
			}
			else
			{
				this.setTextColor(Color.BLACK);
			}
		}

		// set mine as question mark
		// set block as disabled/opened if false is passed
		public void setQuestionMarkIcon(boolean enabled)
		{
			this.setText("?");
			
			if (!enabled)
			{
				this.setBackgroundResource(R.drawable.square_grey);
				this.setTextColor(Color.RED);
			}
			else
			{
				this.setTextColor(Color.BLACK);
			}
		}

		// clear all icons/text
		public void clearAllIcons()
		{
			this.setText("");
		}
}
