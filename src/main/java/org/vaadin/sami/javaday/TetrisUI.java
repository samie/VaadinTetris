package org.vaadin.sami.javaday;

import org.vaadin.hezamu.canvas.Canvas;
import org.vaadin.sami.tetris.Game;
import org.vaadin.sami.tetris.Grid;
import org.vaadin.sami.tetris.Tetromino;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

@Push(transport = Transport.LONG_POLLING)
@Theme("valo")
public class TetrisUI extends UI {

	private static final int PAUSE_TIME_MS = 500;

	private static final long serialVersionUID = -152735180021558969L;

	// Tile size in pixels
	protected static final int TILE_SIZE = 30;

	// Playfield width in tiles
	private static final int PLAYFIELD_W = 10;

	// Playfield height in tiles
	private static final int PLAYFIELD_H = 20;

	// Playfield background color
	private static final String PLAYFIELD_COLOR = "#000";

	private VerticalLayout layout;
	private Canvas canvas;
	protected boolean running;
	protected Game game;

	private Label scoreLabel;

	@Override
	protected void init(VaadinRequest request) {
		Page.getCurrent().setTitle("Vaadin Tetris");
		layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		setContent(layout);

		layout.addComponent(new Label(
				"<h1>Serverside Tetris - using plain web technologies</h1>"
						+ "This is a demo application that "
						+ "proves server side java can work for even "
						+ "interactive games. Game code runs in server side using "
						+ "<a href='http://vaadin.com/'>Vaadin</a>, constantly open "
						+ "communication channel is provided Vaadin 7.1 Push"
						+ "and graphics are drawn with <a href='http://vaadin.com/directory#addon/canvas'>Canvas addon"
						+ " widget</a>. Note that the communication is not even optimized "
						+ "anyhow. With e.g. SVG and based solution the amount of "
						+ "transfered data would be much smaller. Still the game is playable, even over mobile GSM network.",
				ContentMode.HTML));

		// Button for restarting the game
		final Button restartBtn;
		layout.addComponent(restartBtn = new Button("start",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						running = !running;
						if (running) {
							game = new Game(10, 20);
							startGameThread();
							event.getButton().setCaption("stop");
						} else {
							event.getButton().setCaption("start");
							gameOver();
						}

					}
				}));
		restartBtn.setClickShortcut(KeyCode.ESCAPE);

		// Layout for control buttons
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setMargin(false);
		buttons.setSpacing(true);
		layout.addComponent(buttons);

		// Button for moving left
		final Button leftBtn;
		buttons.addComponent(leftBtn = new Button("left",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						game.moveLeft();
						drawGameState();
					}
				}));
		leftBtn.setClickShortcut(KeyCode.ARROW_LEFT);

		// Button for moving right
		final Button rightBtn;
		buttons.addComponent(rightBtn = new Button("right",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						game.moveRight();
						drawGameState();
					}
				}));
		rightBtn.setClickShortcut(KeyCode.ARROW_RIGHT);

		// Button for rotating clockwise
		final Button rotateCWBtn;
		buttons.addComponent(rotateCWBtn = new Button("cw",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						game.rotateCW();
						drawGameState();
					}
				}));
		rotateCWBtn.setClickShortcut(KeyCode.ARROW_DOWN);

		// Button for rotating counter clockwise
		final Button rotateCCWBtn;
		buttons.addComponent(rotateCCWBtn = new Button("ccw",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						game.rotateCCW();
						drawGameState();
					}
				}));
		rotateCCWBtn.setClickShortcut(KeyCode.ARROW_UP);

		// Button for dropping the piece
		final Button dropBtn;
		buttons.addComponent(dropBtn = new Button("drop",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						game.drop();
						drawGameState();
					}
				}));
		dropBtn.setClickShortcut(KeyCode.SPACEBAR);

		// Canvas for the game
		canvas = new Canvas();
		layout.addComponent(canvas);
		canvas.setWidth((TILE_SIZE * PLAYFIELD_W) + "px");
		canvas.setHeight((TILE_SIZE * PLAYFIELD_H) + "px");
		// canvas.setBackgroundColor(PLAYFIELD_COLOR);

		// Label for score
		scoreLabel = new Label("");
		layout.addComponent(scoreLabel);

	}

	/**
	 * Start the game thread that updates the game periodically.
	 * 
	 */
	protected synchronized void startGameThread() {
		Thread t = new Thread() {
			public void run() {

				// Continue until stopped or game is over
				while (running && !game.isOver()) {

					// Draw the state
					access(new Runnable() {
						
						@Override
						public void run() {
							drawGameState();
						}
					});

					// Pause for a while
					try {
						sleep(PAUSE_TIME_MS);
					} catch (InterruptedException igmored) {
					}

					// Step the game forward and update score
					game.step();
					updateScore();

				}

				// Notify user that game is over
				access(new Runnable() {
					
					@Override
					public void run() {
						gameOver();
					}
				});
			}
		};
		t.start();

	}

	/**
	 * Update the score display.
	 * 
	 */
	protected synchronized void updateScore() {
		access(new Runnable() {
			
			@Override
			public void run() {
				scoreLabel.setValue("Score: " + game.getScore());
			}
		});
	}

	/**
	 * Quit the game.
	 * 
	 */
	protected synchronized void gameOver() {
		running = false;
		Notification.show("Game Over", "Your score: " + game.getScore(),
				Type.HUMANIZED_MESSAGE);
	}

	/**
	 * Draw the current game state.
	 * 
	 */
	protected synchronized void drawGameState() {

		// Reset and clear canvas
		canvas.clear();
		canvas.setFillStyle(PLAYFIELD_COLOR);
		canvas.fillRect(0, 0, game.getWidth() * TILE_SIZE + 2, game.getHeight()
				* TILE_SIZE + 2);

		// Draw the tetrominoes
		Grid state = game.getCurrentState();
		for (int x = 0; x < state.getWidth(); x++) {
			for (int y = 0; y < state.getHeight(); y++) {

				int tile = state.get(x, y);
				if (tile > 0) {

					String color = Tetromino.get(tile).getColor();
					canvas.setFillStyle(color);
					canvas.fillRect(x * TILE_SIZE + 1, y * TILE_SIZE + 1,
							TILE_SIZE - 2, TILE_SIZE - 2);
				}

			}
		}
	}
    
    @WebServlet(urlPatterns = "/*", asyncSupported = true)
    @VaadinServletConfiguration(ui = TetrisUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
