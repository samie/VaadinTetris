package org.vaadin.sami.javaday;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Application shell configuration for Vaadin Tetris.
 * This class configures global application settings like Push and PWA.
 */
@Push
@PWA(name = "Vaadin Tetris", shortName = "Tetris")
@StyleSheet(Lumo.STYLESHEET)
public class AppShell implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "icons/icon.svg", "any");
        settings.addFavIcon("icon", "icons/icon.png", "512x512");
        settings.addFavIcon("icon", "icons/icon-32x32.png", "32x32");

        AppShellConfigurator.super.configurePage(settings);
    }
}
