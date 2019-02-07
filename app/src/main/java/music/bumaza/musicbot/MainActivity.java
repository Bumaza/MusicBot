package music.bumaza.musicbot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioRecord;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import music.bumaza.musicbot.adapters.TonePageAdapter;
import music.bumaza.musicbot.audio.AudioRecordingHandler;
import music.bumaza.musicbot.audio.AudioRecordingThread;
import music.bumaza.musicbot.data.Tone;
import music.bumaza.musicbot.utils.StorageUtils;
import music.bumaza.musicbot.view.BarGraphRenderer;
import music.bumaza.musicbot.view.MusicSheetView;
import music.bumaza.musicbot.view.VisualizerView;

import static music.bumaza.musicbot.utils.AppUtils.*;

public class MainActivity extends AppCompatActivity {


    /**
     * FFT
     */
    private AudioRecord recorder = null;
    private AudioRecordingThread recordingThread = null;
    private VisualizerView visualizerView;
    private BarGraphRenderer barGraphRenderer;


    /**
     * Java
     */
    private boolean isRecording = false;
    private int bytesRecorded;
    private int bufferSize;
    private int[] bufferData;
    private short[] audioData;


    /**
     * Android
     */
    private ImageView microphone;
    private MusicSheetView musicSheetView;
    private TextView frequncyTv;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resources = this.getResources();

        setButtonHandlers();

        bufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING) * 3;

        audioData = new short[bufferSize];

        visualizerView = findViewById(R.id.visualizerView);
        setupVisualizer();

        frequncyTv = findViewById(R.id.frequency);

        if(!checkPermissionFromDevice()){
            reqeustPermission();
        }

    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void reqeustPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startRecording() {
        recordingThread = new AudioRecordingThread(StorageUtils.getFileName(), new AudioRecordingHandler() {
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes);
                            if(barGraphRenderer.getIndexOfTone() != null){
                                viewPager.setCurrentItem(barGraphRenderer.getIndexOfTone());
                                frequncyTv.setText(getString(R.string.frequency_text, barGraphRenderer.getFrequency()));
                            }
                        }
                    }
                });
            }

            @Override
            public void onRecordSuccess() {}

            @Override
            public void onRecordingError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        stopRecording();
                    }
                });
            }

            @Override
            public void onRecordSaveError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        stopRecording();
                    }
                });
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopRecording();
    }

    @Override
    protected void onDestroy() {
        stopRecording();
        releaseVisualizer();

        super.onDestroy();
    }

    private void releaseVisualizer() {
        visualizerView.release();
        visualizerView = null;
    }

    private void setButtonHandlers() {
        microphone = findViewById(R.id.button_mid);
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) stopRecording();
                else startRecording();
                isRecording = !isRecording;
                microphone.setImageResource(isRecording ? R.drawable.ic_pause : R.drawable.ic_mic);
                frequncyTv.setVisibility(isRecording ? View.VISIBLE : View.GONE);
                visualizerView.setVisibility(isRecording ? View.VISIBLE : View.GONE);
            }
        });
        musicSheetView = findViewById(R.id.sheets);

        viewPager = findViewById(R.id.toneViewPager);
        TonePageAdapter tonePageAdapter = new TonePageAdapter(this, Tone.tones);
        viewPager.setAdapter(tonePageAdapter);
        viewPager.setCurrentItem(Tone.tones.size()/2);
        //viewPager.setPageMargin(convertToPx(-250));
        viewPager.setClipToPadding(false);
        viewPager.setPadding(convertToPx(100),0,convertToPx(100),0);

    }

    private void setupVisualizer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 255, 255, 255));
        barGraphRenderer = new BarGraphRenderer(2, paint, false);
        visualizerView.addRenderer(barGraphRenderer);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) hideSystemUI();
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}

