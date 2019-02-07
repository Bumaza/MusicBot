package music.bumaza.musicbot.view;

import android.graphics.Canvas;
import android.graphics.Rect;

import music.bumaza.musicbot.data.AudioData;
import music.bumaza.musicbot.data.FFTData;


abstract public class Renderer {


  protected float[] mPoints, mFFTPoints;

  /**
   * Implement this method to render the audio data onto the canvas
   * @param canvas - Canvas to draw on
   * @param data - Data to render
   * @param rect - Rect to render into
   */
  abstract public void onRender(Canvas canvas, AudioData data, Rect rect);

  /**
   * Implement this method to render the FFT audio data onto the canvas
   * @param canvas - Canvas to draw on
   * @param data - Data to render
   * @param rect - Rect to render into
   */
  abstract public void onRender(Canvas canvas, FFTData data, Rect rect);

  /**
   * Render the audio data onto the canvas
   * @param canvas - Canvas to draw on
   * @param data - Data to render
   * @param rect - Rect to render into
   */
  final public void render(Canvas canvas, AudioData data, Rect rect) {
    if (mPoints == null || mPoints.length < data.getBytes().length * 4)
      mPoints = new float[data.getBytes().length * 4];

    onRender(canvas, data, rect);
  }

  /**
   * Render the FFT data onto the canvas
   * @param canvas - Canvas to draw on
   * @param data - Data to render
   * @param rect - Rect to render into
   */
  final public void render(Canvas canvas, FFTData data, Rect rect) {
    if (mFFTPoints == null || mFFTPoints.length < data.getBytes().length * 4)
      mFFTPoints = new float[data.getBytes().length * 4];

    onRender(canvas, data, rect);
  }


}
