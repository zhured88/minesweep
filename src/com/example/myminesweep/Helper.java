package com.example.myminesweep;

import java.util.Arrays;
import java.util.Random;

public class Helper {
	/**
	 * 生成一个不重复的随机数组
	 * 
	 * @param length
	 *            数组长度
	 * @param max
	 *            随机数范围，大于数组长度
	 * @return
	 */
	public static int[] getRandomArray(int length, int max) {
		int[] randArray = new int[length];
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			randArray[i] = rand.nextInt(max);
		}
		Arrays.sort(randArray);
		for (int i = 1; i < length; i++) {
			if (randArray[i] == randArray[i - 1]) {
				randArray[i] += 1;
			}
		}
		for (int i = length - 1; i > 0; i--) {
			if (randArray[i] == randArray[i - 1]) {
				randArray[i - 1] -= 1;
			}
		}
		return randArray;

	}

	public static int[][] getNumberOfMinesInSurrounding(int[] flagArray,
			int width) {
		// we take one row extra row for each side
				// overall two extra rows and two extra columns
				// first and last row/column are used for calculations purposes only
				//	 x|xxxxxxxxxxxxxx|x
				//	 ------------------
				//	 x|              |x
				//	 x|              |x
				//	 ------------------
				//	 x|xxxxxxxxxxxxxx|x
				// the row and columns marked as x are just used to keep counts of near by mines
		 
		int[][] NumofSurround = new int[width+2][width+2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				NumofSurround[i][j] = 0;
			}
		}
		for (int i = 0; i < flagArray.length; i++) {
			int row = flagArray[i] / width;
			int colum = flagArray[i] % width;
			row +=1;
			colum+=1; 
			NumofSurround[row - 1][colum - 1] += 1;
			NumofSurround[row - 1][colum] += 1;
			NumofSurround[row - 1][colum + 1] += 1;
			NumofSurround[row][colum - 1] += 1;
			NumofSurround[row][colum] += 1;
			NumofSurround[row][colum + 1] += 1;
			NumofSurround[row + 1][colum - 1] += 1;
			NumofSurround[row + 1][colum] += 1;
			NumofSurround[row + 1][colum + 1] += 1;

		}

		return NumofSurround;

	}

}
