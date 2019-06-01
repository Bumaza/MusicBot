package music.bumaza.musicbot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioRecord;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;
import music.bumaza.musicbot.adapters.TonePageAdapter;
import music.bumaza.musicbot.audio.AudioRecordingHandler;
import music.bumaza.musicbot.audio.AudioRecordingThread;
import music.bumaza.musicbot.data.Tone;
import music.bumaza.musicbot.utils.AppUtils;
import music.bumaza.musicbot.utils.StorageUtils;
import music.bumaza.musicbot.view.BarGraphRenderer;
import music.bumaza.musicbot.view.MusicSheetView;
import music.bumaza.musicbot.view.NoteAnimation;
import music.bumaza.musicbot.view.VisualizerView;

import static music.bumaza.musicbot.utils.AppUtils.MY_PERMISSIONS_RECORD_AUDIO;
import static music.bumaza.musicbot.utils.AppUtils.RECORDER_AUDIO_ENCODING;
import static music.bumaza.musicbot.utils.AppUtils.RECORDER_CHANNELS;
import static music.bumaza.musicbot.utils.AppUtils.SAMPLING_RATE;
import static music.bumaza.musicbot.utils.AppUtils.convertToPx;
import static music.bumaza.musicbot.utils.AppUtils.resources;

public class MusicBotActivity extends MyActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private boolean hasBackground = false;
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
    private VusikView musicDynamicBackground;
    private Animation noteAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_bot);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        resources = this.getResources();

        setButtonHandlers();

        bufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING) * 3;

        audioData = new short[bufferSize];

        visualizerView = findViewById(R.id.visualizerView);
        setupVisualizer();

        frequncyTv = findViewById(R.id.frequency);
        musicDynamicBackground = findViewById(R.id.music_dynamic_background);

        if(!checkPermissionFromDevice()){
            reqeustPermission();
        }

        musicSheetView = findViewById(R.id.my_sheets);
        noteAnimation = new NoteAnimation(musicSheetView);
        noteAnimation.setDuration(1000);
        noteAnimation.setRepeatCount(Animation.INFINITE);
        musicSheetView.setAnimation(noteAnimation);

        updateSheet();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_music){
            setTitle(getString(R.string.menu_music));

        }else if(id == R.id.nav_recognize){
            setTitle(getString(R.string.menu_record_and_recognize));
        }else if(id == R.id.nav_history){
            setTitle(getString(R.string.menu_history));
        }else if(id == R.id.nav_app){
            setTitle(getString(R.string.menu_about));
        }else if(id == R.id.nav_equalizer){
            setTitle(getString(R.string.menu_equalizer));
        }else if(id == R.id.nav_library){
            setTitle(getString(R.string.menu_library));
        }else if(id == R.id.nav_settings){
            setTitle(getString(R.string.menu_settings));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    private void startRecording() {

        recordingThread = new AudioRecordingThread(StorageUtils.getFileName(), new AudioRecordingHandler() {
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes);
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

    private void updateSheet(){
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(isRecording) {

                    if(barGraphRenderer.getIndexOfTone() != null){
                        int index = barGraphRenderer.getIndexOfTone();
                        Log.d("RECORDING",String.format("ADD NOTE INDEX: %d", index));
                        viewPager.setCurrentItem(index);
                        frequncyTv.setText(getString(R.string.frequency_text, barGraphRenderer.getFrequency()));
                        musicSheetView.addNote(Tone.getDistanceFromMid(index));
                    }

                }

            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void stopRecording() {
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread = null;
        }
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
                if (isRecording) {
                    stopRecording();
                    viewPager.setVisibility(View.GONE);
                } else{
                    startRecording();
                    viewPager.setVisibility(View.VISIBLE);
                }
                //handleBackground();
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

    private void handleBackground(){
        if(isRecording){
            musicDynamicBackground.pauseNotesFall();
            musicDynamicBackground.clearAnimation();
            return;
        }

        if(!hasBackground){
            musicDynamicBackground.start();
            musicDynamicBackground.startNotesFall();
        }else{
            hasBackground = true;
            musicDynamicBackground.resumeNotesFall();
        }
    }

    private void setupVisualizer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 255, 255, 255));
        barGraphRenderer = new BarGraphRenderer(2, paint, false);
        visualizerView.addRenderer(barGraphRenderer);
    }
}
