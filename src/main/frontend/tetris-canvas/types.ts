/**
 * Drawing command types for Tetris Canvas communication
 */
export interface DrawCommand {
  type: 'clear' | 'fillRect' | 'setFillStyle' | 'beginFrame' | 'endFrame';
  params?: any[];
}

export interface FillRectCommand extends DrawCommand {
  type: 'fillRect';
  params: [number, number, number, number]; // x, y, width, height
}

export interface SetFillStyleCommand extends DrawCommand {
  type: 'setFillStyle';
  params: [string]; // color
}

export interface CanvasState {
  width: number;
  height: number;
  backgroundColor: string;
}
