package org.vaadin.sami.tetris;

/**
 * Tetriminoes class.
 * 
 * Based on the Tetris Guidelines at
 * http://tetris.wikia.com/wiki/Tetris_Guideline.
 * 
 * @author Sami Ekblad / Vaadin
 * 
 */
public class Tetromino extends Grid {

	private static String LABELS = "IJLOSTZ";

	private static String COLORS[] = new String[] { "#0ff", "#00f", "#ffa500",
			"#ff0", "#0f0", "#800080", "#f00" };

	private static Tetromino[] ALL = new Tetromino[7];

	static {

		// 'I'
		int index = 0;
		Grid g = new Grid(new int[][] { 
				new int[] { 0, 1, 0, 0 },
				new int[] { 0, 1, 0, 0 }, 
				new int[] { 0, 1, 0, 0 },
				new int[] { 0, 1, 0, 0 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'I', index);

		// 'J'
		g = new Grid(new int[][] { 
				new int[] { 2, 2, 0 },
				new int[] { 0, 2, 0 }, 
				new int[] { 0, 2, 0 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'J', index);

		// 'L'
		g = new Grid(new int[][] {
				new int[] { 0, 3, 0 },
				new int[] { 0, 3, 0 }, 
				new int[] { 3, 3, 0 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'L', index);

		// 'O'
		g = new Grid(new int[][] { 
				new int[] { 4, 4 }, 
				new int[] { 4, 4 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'O', index);

		// 'S'
		g = new Grid(new int[][] { 
				new int[] { 0, 5, 0 },
				new int[] { 5, 5, 0 }, 
				new int[] { 5, 0, 0 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'S', index);

		// 'T'
		g = new Grid(new int[][] { 
				new int[] { 0, 6, 0 },
				new int[] { 6, 6, 0 }, 
				new int[] { 0, 6, 0 } });
		ALL[index++] = new Tetromino(g, COLORS[index], 'T', index);

		// 'Z'
		g = new Grid(new int[][] { 
				new int[] { 7, 0, 0 },
				new int[] { 7, 7, 0 }, 
				new int[] { 0, 7, 0 } });
		ALL[index] = new Tetromino(g, COLORS[index], 'Z', index);

	}

	private int index;
	private char label;
	private String color;

	private Tetromino(Grid g, String color, char letter, int index) {
		super(g);
		this.index = index;
		this.label = letter;
		this.color = color;
	}

	/**
	 * Create a copy of another Tetrimino.
	 * 
	 * @param other
	 */
	public Tetromino(Tetromino other) {
		super(other);
		this.index = other.index;
		this.label = other.label;
		this.color = other.color;

	}

	/** Get a tetrimino by type 1 to 7 */
	public static Tetromino get(int type) {
		if (type > 0 && type <= 7) {
			return ALL[type - 1];
		}
		return null;
	}

	/** Get a tetrimino by letter I,J,L,O,S,T,Z */
	public static Tetromino get(char letter) {
		return get(LABELS.indexOf(letter));
	}

	/** Get a random tetrimino */
	public static Tetromino getRandom() {
		return ALL[(int) (Math.random() * 7.0)];
	}

	public char getLabel() {
		return label;
	}

	public String getColor() {
		return color;
	}

}
