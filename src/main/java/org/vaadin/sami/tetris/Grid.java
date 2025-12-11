package org.vaadin.sami.tetris;

/**
 * Generic grid for integer values and operations.
 * 
 * @author Sami Ekblad / Vaadin
 * 
 */
public class Grid {

	private static final int EMPTY_VALUE = 0;
	private int[][] grid;
	private int gridW, gridH;

	public Grid(int w, int h) {
		gridW = w;
		gridH = h;
		grid = new int[gridW][gridH];
	}

	public Grid(Grid g) {
		gridW = g.gridW;
		gridH = g.gridH;
		grid = new int[gridW][gridH];
		for (int x = 0; x < gridW; x++)
			for (int y = 0; y < gridH; y++)
				grid[x][y] = g.grid[x][y];
	}

	public Grid(int[][] data) {
		gridH = data.length;
		gridW = data[0].length;
		grid = data;
	}

	public void fill(int px, int py, int w, int h, int value) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if ((px + x < gridW) && (py + y < gridH) && (px + x >= 0)
						&& (py + y >= 0)) {
					grid[px + x][py + y] = value;
				}
			}
		}
	}

	public void rotateCW() {
		int rotated[][] = new int[gridH][gridW];
		int s = gridH - 1;
		for (int i = 0; i < gridH; i++) {
			for (int j = 0; j < gridW; j++) {
				rotated[i][j] = grid[j][s - i];
			}
		}
		grid = rotated;
		int tmp = gridW;
		gridW = gridH;
		gridH = tmp;
	}

	public void rotateCCW() {
		int rotated[][] = new int[gridH][gridW];

		int s = gridW - 1;
		for (int i = 0; i < gridH; i++) {
			for (int j = 0; j < gridW; j++) {
				rotated[i][j] = grid[s - j][i];
			}
		}

		grid = rotated;
		int tmp = gridW;
		gridW = gridH;
		gridH = tmp;
	}

	public int getWidth() {
		return gridW;
	}

	public int getHeight() {
		return gridH;
	}

	public void copy(Grid other, int px, int py) {

		int max_x = other.gridW;
		if (other.gridW + px > gridW)
			max_x = gridW - px;

		int max_y = other.gridH;
		if (max_y + py > gridH)
			max_y = gridH - py;

		for (int x = 0; x < max_x; x++)
			for (int y = 0; y < max_y; y++)
				if ((px + x < gridW) && (py + y < gridH) && (px + x >= 0)
						&& (py + y >= 0) && other.grid[x][y] != EMPTY_VALUE)
					grid[px + x][py + y] = other.grid[x][y];
	}

	public boolean fitsInto(Grid other, int px, int py) {

		for (int x = 0; x < other.gridW; x++)
			for (int y = 0; y < other.gridH; y++) {
				if ((px + x < gridW) && (py + y < gridH) && (px + x >= 0)
						&& (py + y >= 0)) {
					if (grid[px + x][py + y] != EMPTY_VALUE
							&& other.grid[x][y] != EMPTY_VALUE) {
						return false;
					}
				} else if (other.grid[x][y] != EMPTY_VALUE) {
					return false;
				}
			}
		return true;
	}

	public int get(int x, int y) {
		return grid[x][y];
	}

	public void set(int x, int y, int value) {
		grid[x][y] = value;
	}

	public boolean isEmpty(int x, int y) {
		return grid[x][y] == EMPTY_VALUE;
	}
}
