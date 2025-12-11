# Vaadin Tetris

A server-side Tetris game built with Vaadin, demonstrating that Java can handle real-time interactive applications entirely on the backend. Originally presented during a talk "Vaadin and HTML5" at JavaDay Riga, November 2011.

![Screenshot](https://raw.githubusercontent.com/samie/VaadinTetris/master/vaadin-tetris.png)

## Features

- **Server-side architecture** - All game logic runs on the server in Java
- **Real-time updates** - WebSocket push via Vaadin Push (with automatic fallback)
- **Modern web components** - Custom Lit-based canvas component for rendering
- **Official Tetris guidelines** - Follows game rules from https://tetris.fandom.com/wiki/Tetris
- **Progressive Web App (PWA)** - Installable as a native-like application
- **Vaadin 25 ** - Latest Vaadin Java architecture with React Router

## Prerequisites

- **Java 25** or higher
- **Git** - For cloning the repository
- **Maven 3.x** - Build and dependency management
- **Modern web browser** - With WebSocket support (Chrome, Firefox, Safari, Edge)

## Technologies

### Backend
- **Vaadin 25.0.0-rc1** - Modern Java-based web framework
- **Jetty 12.1.5** - Development server (embedded)

### Frontend technologies managed by Vaadin
- **Node.js** - Automatically managed by Vaadin Maven plugin
- **Lit 3.x** - Web Components library for custom canvas component
- **TypeScript** - Type-safe frontend development
- **Vite** - Fast frontend build tool
- **React Router** - Vaadin client-side routing

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
mvn clean package

# Clean build with full frontend rebuild
mvn clean compile

# Run on a different port
mvn jetty:run -Djetty.http.port=9090

# Production build (optimized)
mvn clean package
```

### What to Expect

When you run `mvn jetty:run`, Maven will:
1. Download dependencies (first time only)
2. Install Node.js and npm dependencies if needed (first time only)
3. Build the frontend bundle with Vite
4. Compile Java sources
5. Start Jetty server on port 8080 

TIP: Enable hot-reload for both Java and frontend changes running in IDE

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

### Java (Backend)

```
src/main/java/org/vaadin/sami/
├── components/
│   └── TetrisCanvas.java       # Lit web component wrapper
├── javaday/
│   ├── TetrisView.java         # Main view with @Route
│   ├── AppShell.java           # PWA and Push configuration
│   └── About.java              # About panel component
└── tetris/
    ├── Game.java               # Core game logic
    ├── Grid.java               # Grid data structure
    └── Tetromino.java          # Tetris piece definitions
```

### Frontend (TypeScript/Lit)
```
src/main/frontend/
├── tetris-canvas/
│   ├── tetris-canvas.ts        # Lit web component for canvas rendering
│   └── types.ts                # TypeScript type definitions
├── index.html                  # Application shell
├── index.ts                    # Entry point (generated)
├── vite.config.ts              # Vite configuration
└── tsconfig.json               # TypeScript configuration
```

### Resources
```
src/main/resources/
└── META-INF/resources/icons/
    ├── icon.svg                # Vector icon (any size)
    ├── icon.png                # 512×512 for PWA manifest
    └── icon-32x32.png          # Browser tab favicon
```

### Key Components
- **`TetrisView.java`** - Main Vaadin view using `@Route("")`
- **`AppShell.java`** - Configures PWA, Push, and favicon
- **`TetrisCanvas.java`** - Java wrapper for the Lit web component
- **`tetris-canvas.ts`** - Lit web component handling canvas rendering on client-side
- **`org.vaadin.sami.tetris`** - Pure game engine with no UI dependencies

## Architecture Highlights

VaadinTetris demonstrates a hybrid architecture where the game logic runs server-side in Java while rendering happens client-side using a custom Lit web component. This showcases Vaadin 25's modern approach to building web applications.

**Key technical points:**

### Server-Side (Java)
- Game state maintained in Java with thread-safe updates
- Separate game thread runs at 500ms intervals
- `@Push` annotation enables automatic WebSocket communication
- Canvas drawing commands sent to client as batched operations
- Uses `UI.access()` for thread-safe UI updates

### Client-Side (TypeScript/Lit)
- Custom `<tetris-canvas>` Lit web component
- Efficient canvas rendering with batched draw operations
- Canvas size and drawing handled entirely on the client
- Type-safe communication with Java backend

### Vaadin 25 Features
- **React Router** - Client-side routing (even for Java-only views)
- **Vite** - Fast frontend build and hot module replacement (HMR)
- **PWA support** - Automatic service worker and manifest generation
- **TypeScript integration** - Full type safety across frontend/backend boundary

### Why This Architecture?

This demonstrates Vaadin's flexibility:
- Game logic benefits from Java's strong typing and security
- Rendering benefits from client-side performance
- WebSocket Push ensures real-time state synchronization
- Lit web components provide modern, standards-based UI custom elements

### Progressive Web App (PWA)

The application can be installed as a PWA on supported devices:
1. Open http://localhost:8080/ in Chrome/Edge/Safari
2. Click the install or share button in the address bar
3. The app will install like a native application
4. Works offline (displays offline page when disconnected)

## Development

### Hot Reload

Vaadin 25 supports hot reload for both backend and frontend:

- **Java changes:** Automatically recompiled and reloaded
- **TypeScript/Lit changes:** Hot module replacement (HMR) via Vite
- **CSS changes:** Instant updates without page reload

### Custom Web Components

The `tetris-canvas` component demonstrates creating custom web components:

1. **Define in TypeScript** (`src/main/frontend/tetris-canvas/tetris-canvas.ts`)
2. **Wrap in Java** (`src/main/java/org/vaadin/sami/components/TetrisCanvas.java`)
3. **Use like any Vaadin component** in your Java views

## Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following Java naming conventions
4. Test your changes thoroughly
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

Please report issues at: https://github.com/samie/VaadinTetris/issues

## History

This project has evolved significantly over the years:

- **2011** - Original version presented at JavaDay Riga, November 2011
- **2013** - Updated to use official Vaadin Push (version 7.1) and Valo theme
- **2024** - Updated to Vaadin 8.14.1 with code quality improvements
- **2025** - **Major upgrade to Vaadin 25**

The game engine follows the guidelines provided at https://tetris.fandom.com/wiki/Tetris

## Branches

- **`master`** - Vaadin 25 version (current)
- **`vaadin8`** - Vaadin 8 version (legacy, GWT-based)
- **`vaadin7`** - Original Vaadin 7 version 

## License
See the repository for license information.

## Acknowledgments
- Original concept and implementation by Sami Ekblad
- Vaadin framework by Vaadin Ltd
- Tetris guidelines from https://tetris.fandom.com/wiki/Tetris
