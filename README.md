Vaadin Tetris
=============

This is the source code for a Vaadin based Tetris game. It was originally presented during my talk "Vaadin and HTML5" in JavaDay Riga, November 2011.
![Screenshot](https://github.com/samie/JavaDayRiga2011/raw/60f157a842f8473f45f5436c1890da1aafc73779/vaadin-tetris-javadayriga2011.png)

The code is a bit cleaned up and commented for better readability and adds keyboard support for game control.

This implementation uses Vaadin 6.7.2 together with CanvasWidget for rendering and DontPush OzoneLayer for websocket based communication. The used add-ons are still experimental 
implementations and may not work with all server-browser combinations. 

The Tetris game engine tries to follow the guidelines given at  http://tetris.wikia.com/

