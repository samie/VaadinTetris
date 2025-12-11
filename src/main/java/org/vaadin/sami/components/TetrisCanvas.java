package org.vaadin.sami.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Server-side Java wrapper for the Tetris Canvas Lit component.
 * Provides a type-safe API for HTML5 Canvas drawing operations.
 *
 * This component batches drawing commands and sends them to the client-side
 * Lit component for efficient rendering.
 */
@Tag("tetris-canvas")
@JsModule("./tetris-canvas/tetris-canvas.ts")
@NpmPackage(value = "lit", version = "3.1.0")
public class TetrisCanvas extends Component {

    private int width;
    private int height;
    private List<DrawCommand> commandBuffer = new ArrayList<>();
    private boolean batchMode = false;

    /**
     * Drawing command wrapper for JSON serialization
     */
    private static class DrawCommand {
        String type;
        Object[] params;

        DrawCommand(String type, Object... params) {
            this.type = type;
            this.params = params;
        }

        String toJson() {
            StringBuilder json = new StringBuilder();
            json.append("{\"type\":\"").append(type).append("\"");

            if (params != null && params.length > 0) {
                json.append(",\"params\":[");
                for (int i = 0; i < params.length; i++) {
                    if (i > 0) json.append(",");
                    Object param = params[i];
                    if (param instanceof String) {
                        json.append("\"").append(param).append("\"");
                    } else {
                        json.append(param);
                    }
                }
                json.append("]");
            }

            json.append("}");
            return json.toString();
        }
    }

    /**
     * Create a new Tetris Canvas component
     * @param width Canvas width in pixels
     * @param height Canvas height in pixels
     */
    public TetrisCanvas(int width, int height) {
        this.width = width;
        this.height = height;
        getElement().setProperty("canvasWidth", width);
        getElement().setProperty("canvasHeight", height);
    }

    /**
     * Clear the entire canvas
     */
    public void clear() {
        addCommand("clear");
    }

    /**
     * Set the fill style (color)
     * @param color CSS color string (e.g., "#FF0000" or "rgb(255, 0, 0)")
     */
    public void setFillStyle(String color) {
        addCommand("setFillStyle", color);
    }

    /**
     * Draw a filled rectangle
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Rectangle width
     * @param height Rectangle height
     */
    public void fillRect(double x, double y, double width, double height) {
        addCommand("fillRect", x, y, width, height);
    }

    /**
     * Begin a batch of drawing commands.
     * All commands will be buffered until endBatch() is called.
     * This significantly improves performance by reducing network traffic.
     */
    public void beginBatch() {
        batchMode = true;
        commandBuffer.clear();
        addCommand("beginFrame");
    }

    /**
     * End a batch and flush all commands to client.
     * This sends all buffered commands in a single server-to-client call.
     */
    public void endBatch() {
        if (batchMode) {
            addCommand("endFrame");
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
            String jsonArray = "[" + cmd.toJson() + "]";
            getElement().executeJs("this.executeCommands($0)", jsonArray);
        }
    }

    /**
     * Flush all buffered commands to the client
     */
    private void flushCommands() {
        if (commandBuffer.isEmpty()) {
            return;
        }

        String jsonArray = "[" +
            commandBuffer.stream()
                .map(DrawCommand::toJson)
                .collect(Collectors.joining(",")) +
            "]";

        getElement().executeJs("this.executeCommands(JSON.parse($0))", jsonArray);
        commandBuffer.clear();
    }

    /**
     * Get canvas width
     * @return Width in pixels
     */
    public int getCanvasWidth() {
        return width;
    }

    /**
     * Get canvas height
     * @return Height in pixels
     */
    public int getCanvasHeight() {
        return height;
    }

    /**
     * Resize the canvas
     * @param width New width in pixels
     * @param height New height in pixels
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        getElement().setProperty("canvasWidth", width);
        getElement().setProperty("canvasHeight", height);
    }
}
