package music.bumaza.musicbot.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import music.bumaza.musicbot.data.AudioData;
import music.bumaza.musicbot.data.FFTData;



public class VisualizerView extends View {


  /**
   * FFT
   */
  private FFTData mFftData = null;
  private AudioData mAudioData = null;
  private Set<Renderer> mRenderers;


  /**
   * Java
   */
  private byte[] mBytes, mFFTBytes;


  /**
   * Android
   */
  private Visualizer mVisualizer;
  private Paint mFlashPaint = new Paint();
  private Paint mFadePaint = new Paint();
  private Rect mRect = new Rect();
  private Matrix mMatrix = new Matrix();


  public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    init();
  }

  public VisualizerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VisualizerView(Context context) {
    this(context, null, 0);
  }

  private void init() {
    mBytes = null;
    mFFTBytes = null;

    mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
    mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
    mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

    mRenderers = new HashSet<Renderer>();
  }

  public void link(MediaPlayer player) {

    if(player == null) throw new NullPointerException("MediaPlayer is null");

    mVisualizer = new Visualizer(player.getAudioSessionId());
    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
        updateVisualizer(bytes);
      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
        updateVisualizerFFT(bytes);
      }
    };

    mVisualizer.setDataCaptureListener(captureListener,
        Visualizer.getMaxCaptureRate() / 2, true, true);


    mVisualizer.setEnabled(true);
    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        mVisualizer.setEnabled(false);
      }
    });
  }

  public void addRenderer(Renderer renderer) {

    if(renderer != null) mRenderers.add(renderer);
  }

  public void clearRenderers()
  {
    mRenderers.clear();
  }


  public void release() {
    if (mVisualizer != null) {
    	mVisualizer.release();
    }
  }


  public void updateVisualizer(byte[] bytes) {
    mBytes = bytes;
    invalidate();
  }

  public void updateVisualizerFFT(byte[] bytes) {
    mFFTBytes = bytes;
    invalidate();
  }

  boolean mFlash = false;


  public void flash() {
    mFlash = true;
    invalidate();
  }

  Bitmap mCanvasBitmap;
  Canvas mCanvas;


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    mRect.set(0, 0, getWidth(), getHeight());

    if(mCanvasBitmap == null)
    {
      mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
    }
    if(mCanvas == null)
    {
      mCanvas = new Canvas(mCanvasBitmap);
    }

    mCanvas.drawColor(Color.TRANSPARENT);
    
    if (mBytes != null) {
      // Render all audio renderers
      if (mAudioData == null)
    	  mAudioData = new AudioData();

      mAudioData.setBytes(mBytes);

      for(Renderer r : mRenderers)
        r.render(mCanvas, mAudioData, mRect);

    }

    if (mFFTBytes != null) {
      // Render all FFT renderers
      if (mFftData == null)
    	  mFftData = new FFTData();

      mFftData.setBytes(mFFTBytes);
      for(Renderer r : mRenderers)
        r.render(mCanvas, mFftData, mRect);

    }

    // Fade out old contents
    mCanvas.drawPaint(mFadePaint);

    if(mFlash) {
      mFlash = false;
      mCanvas.drawPaint(mFlashPaint);
    }

    mMatrix.reset();
    canvas.drawBitmap(mCanvasBitmap, mMatrix, null);
  }
}