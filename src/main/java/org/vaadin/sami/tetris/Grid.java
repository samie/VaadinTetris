package org.vaadin.sami.tetris;

/**
 * Generic grid for integer values and operations.
 * 
 * @author Sami Ekblad / Vaadin
 * 
 */
public class Grid {

	private static final int EMPTY_VALUE = 0;
	private int grid[][];
	private int grid_w, grid_h;

	public Grid(int w, int h) {
		grid_w = w;
		grid_h = h;
		grid = new int[grid_w][grid_h];
	}

	public Grid(Grid g) {
		grid_w = g.grid_w;
		grid_h = g.grid_h;
		grid = new int[grid_w][grid_h];
		for (int x = 0; x < grid_w; x++)
			for (int y = 0; y < grid_h; y++)
				grid[x][y] = g.grid[x][y];
	}

	public Grid(int[][] data) {
		grid_h = data.length;
		grid_w = data[0].length;
		grid = data;
	}

	public void fill(int px, int py, int w, int h, int value) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if ((px + x < grid_w) && (py + y < grid_h) && (px + x >= 0)
						&& (py + y >= 0)) {
					grid[px + x][py + y] = value;
				}
			}
		}
	}

	public void rotateCW() {
		int rotated[][] = new int[grid_h][grid_w];
		int s = grid_h - 1;
		for (int i = 0; i < grid_h; i++) {
			for (int j = 0; j < grid_w; j++) {
				rotated[i][j] = grid[j][s - i];
			}
		}
		grid = rotated;
		int tmp = grid_w;
		grid_w = grid_h;
		grid_h = tmp;
	}

	public void rotateCCW() {
		int rotated[][] = new int[grid_h][grid_w];

		int s = grid_w - 1;
		for (int i = 0; i < grid_h; i++) {
			for (int j = 0; j < grid_w; j++) {
				rotated[i][j] = grid[s - j][i];
			}
		}

		grid = rotated;
		int tmp = grid_w;
		grid_w = grid_h;
		grid_h = tmp;
	}

	public int getWidth() {
		return grid_w;
	}

	public int getHeight() {
		return grid_h;
	}

	public void copy(Grid other, int px, int py) {

		int max_x = other.grid_w;
		if (other.grid_w + px > grid_w)
			max_x = grid_w - px;

		int max_y = other.grid_h;
		if (max_y + py > grid_h)
			max_y = grid_h - py;

		for (int x = 0; x < max_x; x++)
			for (int y = 0; y < max_y; y++)
				if ((px + x < grid_w) && (py + y < grid_h) && (px + x >= 0)
						&& (py + y >= 0) && other.grid[x][y] != EMPTY_VALUE)
					grid[px + x][py + y] = other.grid[x][y];
	}

	public boolean fitsInto(Grid other, int px, int py) {

		for (int x = 0; x < other.grid_w; x++)
			for (int y = 0; y < other.grid_h; y++) {
				if ((px + x < grid_w) && (py + y < grid_h) && (px + x >= 0)
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
