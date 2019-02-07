package music.bumaza.musicbot.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import music.bumaza.musicbot.data.AudioData;
import music.bumaza.musicbot.data.FFTData;
import music.bumaza.musicbot.data.Tone;
import music.bumaza.musicbot.fft.FFT;

import static music.bumaza.musicbot.utils.AppUtils.*;

public class BarGraphRenderer extends Renderer {


  /**
   * Java
   */
  private int mDivisions;
  private int maxIndex = 0;

  private float frequency = 0;
  private float prevMagnitude = 0;

  private boolean mTop;

  private Integer indexOfTone = null;


  /**
   * Android
   */
  private Paint mPaint;


  public BarGraphRenderer(int divisions, Paint paint, boolean top) {
    super();
    mDivisions = divisions;
    mPaint = paint;
    mTop = top;
  }

  @Override
  public void onRender(Canvas canvas, AudioData data, Rect rect) {

  }

  @Override
  public void onRender(Canvas canvas, FFTData data, Rect rect) {
    prevMagnitude = 0;

    for (int i = 0; i < data.getBytes().length / mDivisions; i++) {
      mFFTPoints[i * 4] = i * 4 * mDivisions;
      mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
      byte rfk = data.getBytes()[mDivisions * i];
      byte ifk = data.getBytes()[mDivisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (10 * Math.log10(magnitude));

      if(magnitude >= prevMagnitude){
          prevMagnitude = magnitude;
          maxIndex = i;
      }

      if(mTop) {
        mFFTPoints[i * 4 + 1] = 0;
        mFFTPoints[i * 4 + 3] = (dbValue * 2 - 10);
      } else {
        mFFTPoints[i * 4 + 1] = rect.height();
        mFFTPoints[i * 4 + 3] = rect.height() - (dbValue * 2 - 10);
      }
    }


    frequency = (44100 - (maxIndex * SAMPLING_RATE / FFT_POINTS));
    double f = FFT.Index2Freq(maxIndex, SAMPLING_RATE, FFT_POINTS);
    indexOfTone = Tone.getIndexOfTone(frequency);
    System.out.println("Frequency: " +  frequency +" [Hz] " + (44100 - frequency));
    System.out.println("#Frequency: " + f + " [Hz");


    canvas.drawLines(mFFTPoints, mPaint);
  }

  public Integer getIndexOfTone() {
    return indexOfTone;
  }

  public float getFrequency() {
    return frequency;
  }
}
