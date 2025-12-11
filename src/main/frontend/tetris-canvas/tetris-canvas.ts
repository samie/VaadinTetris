import { LitElement, html, css, PropertyValues } from 'lit';
import { customElement, property, query } from 'lit/decorators.js';
import { DrawCommand } from './types.js';

/**
 * Tetris Canvas Web Component
 *
 * A Lit-based custom element that wraps an HTML5 Canvas and executes
 * drawing commands received from the server.
 */
@customElement('tetris-canvas')
export class TetrisCanvas extends LitElement {
  static styles = css`
    :host {
      display: block;
      position: relative;
    }

    canvas {
      display: block;
      border: 1px solid var(--lumo-contrast-30pct, #ccc);
      background-color: #000;
    }
  `;

  @property({ type: Number })
  canvasWidth = 300;

  @property({ type: Number })
  canvasHeight = 600;

  @query('canvas')
  private canvas!: HTMLCanvasElement;

  private ctx: CanvasRenderingContext2D | null = null;

  protected firstUpdated(_changedProperties: PropertyValues): void {
    super.firstUpdated(_changedProperties);
    this.ctx = this.canvas.getContext('2d');
    this.setupCanvas();
  }

  private setupCanvas(): void {
    if (!this.canvas || !this.ctx) return;

    // Set canvas internal dimensions
    this.canvas.width = this.canvasWidth;
    this.canvas.height = this.canvasHeight;

    // Initial clear
    this.ctx.fillStyle = '#000';
    this.ctx.fillRect(0, 0, this.canvasWidth, this.canvasHeight);
  }

  /**
   * Execute a batch of drawing commands
   * Called from server-side Java via element.callJsFunction()
   */
  public executeCommands(commands: DrawCommand[]): void {
    if (!this.ctx) {
      console.warn('Canvas context not ready');
      return;
    }

    commands.forEach(cmd => {
      switch (cmd.type) {
        case 'clear':
          this.ctx!.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
          break;

        case 'setFillStyle':
          this.ctx!.fillStyle = cmd.params![0];
          break;

        case 'fillRect':
          const [x, y, w, h] = cmd.params!;
          this.ctx!.fillRect(x, y, w, h);
          break;

        case 'beginFrame':
          // Optional: save context state
          this.ctx!.save();
          break;

        case 'endFrame':
          // Optional: restore context state
          this.ctx!.restore();
          break;

        default:
          console.warn('Unknown command type:', (cmd as any).type);
      }
    });
  }

  protected updated(changedProperties: PropertyValues): void {
    if (changedProperties.has('canvasWidth') || changedProperties.has('canvasHeight')) {
      this.setupCanvas();
    }
  }

  render() {
    return html`
      <canvas
        width="${this.canvasWidth}"
        height="${this.canvasHeight}">
      </canvas>
    `;
  }
}

declare global {
  interface HTMLElementTagNameMap {
    'tetris-canvas': TetrisCanvas;
  }
}
