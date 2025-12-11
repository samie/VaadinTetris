package org.vaadin.sami.tetris;

/**
 * Tetris game class.
 * 
 * This class implements the game control and bookkeeping methods.
 * 
 * Based on the Tetris Guidelines at
 * http://tetris.wikia.com/wiki/Tetris_Guideline.
 * 
 * @author Sami Ekblad / Vaadin
 * 
 */
public class Game {

	private Grid playfield;
	private int score;
	private boolean gameOver;

	private Tetromino activeTetrimino;
	private int posX, posY;

	public Game(int w, int h) {
		playfield = new Grid(w, h);
		score = 0;
		gameOver = false;
		activeTetrimino = Tetromino.getRandom();
		posY = 0;
		posX = (w - activeTetrimino.getWidth()) / 2;
		clear();
	}

	public int getWidth() {
		return playfield.getWidth();
	}

	public int getHeight() {
		return playfield.getHeight();
	}

	private void clear() {
		playfield.fill(0, 0, playfield.getWidth(), playfield.getHeight(), 0);
	}

	public Grid getCurrentState() {
		Grid state = new Grid(playfield);
		state.copy(activeTetrimino, posX, posY);
		return state;
	}

	public boolean step() {
		if (!playfield.fitsInto(activeTetrimino, posX, posY + 1)) {

			playfield.copy(activeTetrimino, posX, posY);

			for (int j = playfield.getHeight() - 1; j >= 0; j--)
				while (isFullLine(j)) {
					clearLine(j);
					score += 10;
				}

			activeTetrimino = Tetromino.getRandom();
			posY = 0;
			posX = (playfield.getWidth() - activeTetrimino.getWidth()) / 2;
			if (!playfield.fitsInto(activeTetrimino, posX, posY))
				this.gameOver = true;
		} else {
			posY++;
		}
		return gameOver;
	}

	private boolean isFullLine(int y) {
		for (int x = 0; x < playfield.getWidth(); x++)
			if (playfield.isEmpty(x, y))
				return false;
		return true;
	}

	private void clearLine(int y) {
		for (int j = y; j > 0; j--)
			for (int i = 0; i < playfield.getWidth(); i++)
				playfield.set(i, j, playfield.get(i, j - 1));
		for (int i = 0; i < playfield.getWidth(); i++)
			playfield.set(i, 0, 0);
	}

	public int getScore() {
		return score;
	}

	public boolean isOver() {
		return gameOver;
	}

	public void moveLeft() {
		if (!playfield.fitsInto(activeTetrimino, posX - 1, posY))
			return;
		posX--;
	}

	public void moveRight() {
		if (!playfield.fitsInto(activeTetrimino, posX + 1, posY))
			return;
		posX++;
	}

	public void rotateCW() {
		Tetromino test = new Tetromino(activeTetrimino);
		test.rotateCW();
		if (playfield.fitsInto(test, posX, posY))
			activeTetrimino = test;
	}

	public void rotateCCW() {
		Tetromino test = new Tetromino(activeTetrimino);
		test.rotateCCW();
		if (playfield.fitsInto(test, posX, posY))
			activeTetrimino = test;
	}

	public void drop() {
		while (playfield.fitsInto(activeTetrimino, posX, posY + 1))
			posY++;
	}

}
