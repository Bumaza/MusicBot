package music.bumaza.musicbot;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import music.bumaza.musicbot.data.Tone;

import static music.bumaza.musicbot.utils.AppUtils.*;

public class ToneGenerator {


    /**
     * Java
     */
    private static final int duration = 3; // seconds
    private static final int sampleRate = SAMPLING_RATE;
    private static final int numSamples = duration * sampleRate;
    private static final double sample[] = new double[numSamples];

    private static double freqOfTone = 440; // hz

    private static final byte generatedSnd[] = new byte[10 * numSamples];


    /**
     * Android
     */
    private static void genTone(Tone tone){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/ tone.getFrequency()));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    public static void play(){
        genTone(Tone.A);
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, RECORDER_CHANNELS,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

    public static void play(Tone tone){
        genTone(tone);
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, RECORDER_CHANNELS,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
}
