# Serverside Tetris - using plain web technologies

This is a technology stunt that proves server side Java can work even for
interactive games. Game code runs in servlet engine and the UI is built using
[Vaadin](http://vaadin.com/). The automatic push connection in Vaadin uses 
WebSocket communication if it suits for both client and server (and proxies), 
else falls back to long polling, or streaming. 

Graphics are drawn with the [Canvas addon](http://vaadin.com/directory#addon/canvas) - on 
each and every game state change. This naturally causes lots of traffic
traffic, but this could be really easily optimized. E.g. SVG and based 
solution or just simple html table based solution the amount of transfered data
 would be much smaller and only the changes would need to be sent. 
Still the game is playable, even over mobile GSM network.

	
