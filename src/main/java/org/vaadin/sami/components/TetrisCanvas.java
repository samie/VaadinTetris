package org.vaadin.sami.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Server-side Java wrapper for the Tetris Canvas Lit component.
 * Provides a type-safe API for HTML5 Canvas drawing operations.
 *
 * This component batches drawing commands and sends them to the client-side
 * Lit component for efficient rendering.
 */
@Tag("canvas")
public class TetrisCanvas extends Component implements HasSize {

    private List<DrawCommand> commandBuffer = new ArrayList<>();
    private boolean batchMode = false;

    /**
     * Drawing command wrapper for JSON serialization
     */
    record  DrawCommand(String command, Object[] params) {
    }

    /**
     * Create a new Tetris Canvas component
     * @param width Canvas width in pixels
     * @param height Canvas height in pixels
     */
    public TetrisCanvas(int width, int height) {
        setSize(width, height);

        getStyle().setBorder("1px solid var(--lumo-contrast-30pct, #ccc)");
        getStyle().setBackgroundColor("#000");

        getElement().executeJs("""
              this.ctx = this.getContext('2d');
              this.ctx.fillStyle = '#000';
              this.ctx.fillRect(0, 0, this.width, this.height);
            """);
    }

    /**
     * Clear the entire canvas
     */
    public void clear() {
        addCommand("this.ctx.clearRect(0, 0, this.width, this.height)");
    }

    /**
     * Set the fill style (color)
     * @param color CSS color string (e.g., "#FF0000" or "rgb(255, 0, 0)")
     */
    public void setFillStyle(String color) {
        addCommand("this.ctx.fillStyle = $0", color);
    }

    /**
     * Draw a filled rectangle
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Rectangle width
     * @param height Rectangle height
     */
    public void fillRect(double x, double y, double width, double height) {
        addCommand("this.ctx.fillRect($0, $1, $2, $3)", x, y, width, height);
    }

    /**
     * Begin a batch of drawing commands.
     * All commands will be buffered until endBatch() is called.
     * This significantly improves performance by reducing network traffic.
     */
    public void beginBatch() {
        batchMode = true;
        commandBuffer.clear();
        addCommand("this.ctx.save()");
    }

    /**
     * End a batch and flush all commands to client.
     * This sends all buffered commands in a single server-to-client call.
     */
    public void endBatch() {
        if (batchMode) {
            addCommand("this.ctx.restore()");
            flushCommands();
            batchMode = false;
        }
    }

    /**
     * Add a command to the buffer
     */
    private void addCommand(String type, Object... params) {
        DrawCommand cmd = new DrawCommand(type, params);

        if (batchMode) {
            commandBuffer.add(cmd);
        } else {
            // Immediate execution (single command)
            getElement().executeJs(type, params);
        }
    }

    /**
     * Flush all buffered commands to the client
     */
    private void flushCommands() {
        if (commandBuffer.isEmpty()) {
            return;
        }

        commandBuffer.forEach(action -> {
            getElement().executeJs(action.command, action.params);
        });
        commandBuffer.clear();
    }

    /**
     * Resize the canvas
     * @param width New width in pixels
     * @param height New height in pixels
     */
    public void setSize(int width, int height) {
        setWidth(width, Unit.PIXELS);
        setHeight(height, Unit.PIXELS);

        getElement().executeJs("this.width = $0; this.height = $1", width, height);
    }
}
