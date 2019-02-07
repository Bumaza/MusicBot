package music.bumaza.musicbot.utils;

import android.content.res.Resources;
import android.media.AudioFormat;

public class AppUtils {


    public static Resources resources;

    /**
     * AudioRecorder constants
     */
    public static final int RECORDER_BPP = 16;
    public static final int SAMPLING_RATE = 44100;
    public static final int FFT_POINTS = 1024;
    public static final int MAGIC_SCALE = 10;
    public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    public static final String AUDIO_RECORDER_FOLDER = "MusicBotAudio";
    public static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";


    public static final int MY_PERMISSIONS_RECORD_AUDIO = 20;


    /**
     * Log tags
     */
    public static final String WRITE_TAG = "WRITING";

    public static int convertToPx(int dp) {
        if(resources == null) return 0;
        final float scale = resources.getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
