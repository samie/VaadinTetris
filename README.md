# Vaadin Tetris

A server-side Tetris game built with Vaadin, demonstrating that Java can handle real-time interactive applications entirely on the backend. Originally presented during a talk "Vaadin and HTML5" at JavaDay Riga, November 2011.

![Screenshot](https://raw.githubusercontent.com/mstahv/VaadinTetris/master/vaadin-tetris.png)

## Features

- **Server-side architecture** - All game logic runs on the server in Java
- **Real-time updates** - WebSocket push via Vaadin Push (with automatic fallback to long polling)
- **HTML5 Canvas rendering** - Graphics drawn using Canvas element
- **Official Tetris guidelines** - Follows game rules from https://tetris.fandom.com/wiki/Tetris
- **Modern UI** - Styled with Vaadin's Valo theme

## Prerequisites

- **Java 8** or higher
- **Maven 3.x** - Build and dependency management
- **Modern web browser** - With WebSocket support (Chrome, Firefox, Safari, Edge)
- **Git** - For cloning the repository

## Technologies

- **Vaadin 8.14.1** - Server-side web framework
- **Vaadin Push** - WebSocket communication for real-time updates
- **Canvas Add-on 2.3.0** - HTML5 Canvas integration
- **Viritin 2.0** - Vaadin utility components
- **Maven** - Build and dependency management
- **Jetty 9.3.9** - Development server (embedded)

## Building and Running Locally

### Quick Start

```bash
git clone https://github.com/samie/VaadinTetris.git
cd VaadinTetris
mvn jetty:run
```

Then open http://localhost:8080/ in your browser.

### Other Build Commands

```bash
# Build WAR package for deployment
mvn package

# Clean and rebuild
mvn clean install

# Run on a different port
mvn jetty:run -Djetty.http.port=9090
```

### What to Expect

When you run `mvn jetty:run`, Maven will:
1. Download dependencies (first time only)
2. Compile Java sources
3. Start Jetty server on port 8080
4. Hot-reload code changes every 2 seconds

The application will be available at http://localhost:8080/ with the Tetris game ready to play.

## How to Play

### Controls

- **Arrow Left / Right** - Move piece horizontally
- **Arrow Up** - Rotate piece counter-clockwise
- **Arrow Down** - Rotate piece clockwise
- **Spacebar** - Drop piece instantly
- **Play/Stop button** - Start or stop the game

### Scoring

- **10 points** per line cleared
- Multiple lines can be cleared simultaneously

## Project Structure

```
src/main/java/org/vaadin/sami/
├── javaday/
│   ├── TetrisUI.java      # Main UI class and game controller
│   └── About.java          # About panel component
└── tetris/
    ├── Game.java           # Core game logic (movement, collision, scoring)
    ├── Grid.java           # Grid data structure with rotation support
    └── Tetromino.java      # Tetris piece definitions (I, J, L, O, S, T, Z)
```

### Key Components

- **`org.vaadin.sami.javaday`** - UI layer with canvas rendering and user input handling
- **`org.vaadin.sami.tetris`** - Pure game engine with no UI dependencies

## Architecture Highlights

VaadinTetris demonstrates an unconventional approach where the entire game runs server-side. The game state is maintained in Java, with every state change triggering a full canvas redraw that's pushed to the browser via WebSocket. This showcases Vaadin's push capabilities and proves that server-side Java can handle interactive real-time applications, though client-side rendering would be more efficient for production games.

**Key technical points:**
- Separate game thread runs at 500ms intervals
- Vaadin's `@Push` annotation enables automatic WebSocket communication
- Full canvas redraw on each state change keeps code simple
- Thread-safe UI updates using Vaadin's `access()` method

## Deployment

### Build Production WAR

```bash
mvn package
```

The WAR file will be created at `target/tetris-2.0-SNAPSHOT.war`.

### Deploy to Application Server

Deploy the WAR file to any Servlet 3.0+ compatible container:
- Apache Tomcat 7+
- Jetty 8+
- WildFly / JBoss
- GlassFish
- Any Java EE application server

**Note:** The application is currently configured with `productionMode=false` in TetrisUI.java. For production deployment, consider enabling production mode for optimized performance.

## Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following Java naming conventions (camelCase for variables)
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

Please report issues at: https://github.com/samie/VaadinTetris/issues

## History

This project has been updated over the years:
- **2011** - Original version presented at JavaDay Riga, November 2011
- **2013** - Updated to use official Vaadin Push (version 7.1) and Valo theme
- **2024** - Updated to Vaadin 8.14.1 with code quality improvements
- **Recent** - Code review fixes including bug corrections and style improvements

The game engine follows the guidelines provided at https://tetris.fandom.com/wiki/Tetris

## License

See the repository for license information.
