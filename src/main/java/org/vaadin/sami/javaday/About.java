package org.vaadin.sami.javaday;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.markdown.Markdown;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * About component that displays information about the Tetris game.
 * Uses Vaadin 25 Markdown component to render markdown content.
 *
 * @author Matti Tahvonen
 */
public class About extends Details {

    public About() {
        setMaxWidth(40, Unit.EM);
        setSummaryText("Server side Tetris game? Read more »");

        // Load markdown content from resources
        String markdownContent = loadMarkdownResource("/about.md");

        // Use Vaadin 25 Markdown component
        Markdown markdown = new Markdown(markdownContent);
        add(markdown);
    }

    /**
     * Load markdown content from classpath resource
     */
    private String loadMarkdownResource(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                return "About content not found.";
            }
            return new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "Error loading about content: " + e.getMessage();
        }
    }
}
