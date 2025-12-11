package org.vaadin.sami.javaday;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.vaadin.sami.components.TetrisCanvas;
import org.vaadin.sami.tetris.Game;
import org.vaadin.sami.tetris.Grid;
import org.vaadin.sami.tetris.Tetromino;

/**
 * Main Tetris game view for Vaadin 25
 *
 * This is a server-side implementation of Tetris where:
 * - Game logic runs on the server (Java)
 * - Canvas rendering happens on the client (Lit component)
 * - Server sends drawing commands to client
 * - WebSocket Push enables real-time updates (configured in AppShell)
 */
@Route("")
@PageTitle("Vaadin Tetris")
public class TetrisView extends VerticalLayout {

    private static final int PAUSE_TIME_MS = 500;
    protected static final int TILE_SIZE = 30;
    private static final int PLAYFIELD_W = 10;
    private static final int PLAYFIELD_H = 20;
    private static final String PLAYFIELD_COLOR = "#000";

    private TetrisCanvas canvas;
    protected boolean running;
    protected Game game;
    private Span scoreLabel;
    private UI ui;

    public TetrisView() {
        this.ui = UI.getCurrent();
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        // Add About component
        add(new About());

        // Button for moving left
        Button leftBtn = new Button(VaadinIcon.ARROW_LEFT.create());
        leftBtn.addClickListener(e -> {
            if (game != null) {
                game.moveLeft();
                drawGameState();
            }
        });
        leftBtn.addClickShortcut(Key.ARROW_LEFT);

        // Button for moving right
        Button rightBtn = new Button(VaadinIcon.ARROW_RIGHT.create());
        rightBtn.addClickListener(e -> {
            if (game != null) {
                game.moveRight();
                drawGameState();
            }
        });
        rightBtn.addClickShortcut(Key.ARROW_RIGHT);

        // Button for rotating clockwise
        Button rotateCWBtn = new Button("[key down]", VaadinIcon.ROTATE_RIGHT.create());
        rotateCWBtn.addClickListener(e -> {
            if (game != null) {
                game.rotateCW();
                drawGameState();
            }
        });
        rotateCWBtn.addClickShortcut(Key.ARROW_DOWN);

        // Button for rotating counter clockwise
        Button rotateCCWBtn = new Button("[key up]", VaadinIcon.ROTATE_LEFT.create());
        rotateCCWBtn.addClickListener(e -> {
            if (game != null) {
                game.rotateCCW();
                drawGameState();
            }
        });
        rotateCCWBtn.addClickShortcut(Key.ARROW_UP);

        // Button for dropping the piece
        Button dropBtn = new Button("[space]", VaadinIcon.ARROW_DOWN.create());
        dropBtn.addClickListener(e -> {
            if (game != null) {
                game.drop();
                drawGameState();
            }
        });
        dropBtn.addClickShortcut(Key.SPACE);

        // Button for restarting the game
        Button restartBtn = new Button(VaadinIcon.PLAY.create());
        restartBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        restartBtn.addClickListener(e -> {
            running = !running;
            if (running) {
                game = new Game(10, 20);
                startGameThread();
                restartBtn.setIcon(VaadinIcon.STOP.create());
                dropBtn.focus();
            } else {
                restartBtn.setIcon(VaadinIcon.PLAY.create());
                gameOver();
            }
        });

        // Control buttons layout
        HorizontalLayout controls = new HorizontalLayout(
            restartBtn, leftBtn, rightBtn, rotateCCWBtn, rotateCWBtn, dropBtn
        );
        controls.setSpacing(true);
        add(controls);

        // Canvas for the game
        canvas = new TetrisCanvas(
            TILE_SIZE * PLAYFIELD_W,
            TILE_SIZE * PLAYFIELD_H
        );
        add(canvas);

        // Label for score
        scoreLabel = new Span("");
        add(scoreLabel);
    }

    /**
     * Start the game thread that updates the game periodically.
     */
    protected synchronized void startGameThread() {
        Thread t = new Thread() {
            public void run() {
                // Draw initial state immediately
                drawGameState();
                updateScore();

                // Continue until stopped or game is over
                while (running && !game.isOver()) {
                    // Pause for a while
                    try {
                        sleep(PAUSE_TIME_MS);
                    } catch (InterruptedException ignored) {
                    }

                    // Step the game forward
                    game.step();

                    // Then draw new state and update score
                    drawGameState();
                    updateScore();
                }

                // Notify user that game is over
                gameOver();
            }
        };
        t.start();
    }

    /**
     * Update the score display.
     */
    protected synchronized void updateScore() {
        ui.access(() -> {
            scoreLabel.setText("Score: " + game.getScore());
        });
    }

    /**
     * Quit the game.
     */
    protected synchronized void gameOver() {
        running = false;

        ui.access(() -> {
            Notification notification = new Notification(
                "Game Over - Your score: " + game.getScore(),
                3000,
                Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            notification.open();
        });
    }

    /**
     * Draw the current game state using batched commands for efficiency.
     */
    protected synchronized void drawGameState() {
        ui.access(() -> {
            // Use batch mode for efficient rendering
            canvas.beginBatch();

            // Clear and draw background
            canvas.clear();
            canvas.setFillStyle(PLAYFIELD_COLOR);
            canvas.fillRect(0, 0, game.getWidth() * TILE_SIZE,
                          game.getHeight() * TILE_SIZE);

            // Draw tetrominoes
            Grid state = game.getCurrentState();
            for (int x = 0; x < state.getWidth(); x++) {
                for (int y = 0; y < state.getHeight(); y++) {
                    int tile = state.get(x, y);
                    if (tile > 0) {
                        String color = Tetromino.get(tile).getColor();
                        canvas.setFillStyle(color);
                        canvas.fillRect(
                            x * TILE_SIZE + 1,
                            y * TILE_SIZE + 1,
                            TILE_SIZE - 2,
                            TILE_SIZE - 2
                        );
                    }
                }
            }

            canvas.endBatch();
        });
    }
}
