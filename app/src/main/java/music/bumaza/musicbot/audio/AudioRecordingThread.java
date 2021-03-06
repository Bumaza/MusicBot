package music.bumaza.musicbot.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import music.bumaza.musicbot.fft.Complex;
import music.bumaza.musicbot.fft.FFT;
import simplesound.pcm.PcmAudioHelper;
import simplesound.pcm.WavAudioFormat;

import static music.bumaza.musicbot.utils.AppUtils.*;


public class AudioRecordingThread extends Thread {

	private String fileName_wav;
    private String fileName_raw;
    
	private int bufferSize;
    private byte[] audioBuffer;
    
    private boolean isRecording = true;

    private AudioRecordingHandler handler = null;
    
    public AudioRecordingThread(String fileWavName, AudioRecordingHandler handler) {
    	this.fileName_wav = fileWavName;
    	this.fileName_raw = getRawName(fileWavName);
    	this.handler = handler;
    	
    	bufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE,
								    			  AudioFormat.CHANNEL_IN_MONO,
								    			  AudioFormat.ENCODING_PCM_16BIT);
    	audioBuffer = new byte[bufferSize];
    }
    
    @Override
    public void run() {
    	FileOutputStream out = prepareWriting();
    	if (out == null) { return; }
    	
    	AudioRecord record = new AudioRecord(AudioSource.VOICE_RECOGNITION, /*AudioSource.MIC*/
							    			 SAMPLING_RATE,
							    			 AudioFormat.CHANNEL_IN_MONO,
							    			 AudioFormat.ENCODING_PCM_16BIT,
							    			 bufferSize);
	    record.startRecording();
	
	    int read = 0;
	    while (isRecording) {
    	    read = record.read(audioBuffer, 0, bufferSize);
        
    	    if ((read == AudioRecord.ERROR_INVALID_OPERATION) || 
    	    	(read == AudioRecord.ERROR_BAD_VALUE) ||
    	    	(read <= 0)) {
    	    	continue;
    	    }
    	    
        	proceed();
    	    write(out);
	    }
	      
	    record.stop();
	    record.release();
	      
	    finishWriting(out);
	    convertRawToWav();
    }
    
    private void proceed() {
    	double temp;
        Complex[] y;
        Complex[] complexSignal = new Complex[FFT_POINTS];

        for (int i=0; i<FFT_POINTS; i++) {
            temp = (double)((audioBuffer[2*i] & 0xFF) | (audioBuffer[2*i+1] << 8)) / 32768.0F;
            complexSignal[i] = new Complex(temp * MAGIC_SCALE, 0d);
        }
    	
        y = FFT.fft(complexSignal);

        double magnitude[] = new double[FFT_POINTS/2];
        for(int i = 0; i < FFT_POINTS/2 -1; i++){
			magnitude[i] = Math.sqrt(y[i].re()*y[i].re() + y[i].im() * y[i].im());
		}
        double maxMag = -1;
        int maxIndex = -1;
        for(int i =0 ; i < FFT_POINTS/2 -1; i++){
        	if(magnitude[i] > maxMag){
        		maxMag = magnitude[i];
        		maxIndex = i;
			}
		}
		double freq = maxIndex * SAMPLING_RATE / FFT_POINTS;



        final byte[] y_byte = new byte[y.length*2];
        y_byte[0] = (byte) y[0].re();
        y_byte[1] = (byte) y[y.length - 1].re();
        for (int i = 1; i < y.length - 1; i++) {
        	y_byte[i*2]   = (byte) y[i].re();
        	y_byte[i*2+1] = (byte) y[i].im();
        }
        
		if (handler != null) {
			handler.onFftDataCapture(y_byte);
		}
    }
    
    private FileOutputStream prepareWriting() {
    	File file = new File(fileName_raw);
	    if (file.exists()) { file.delete(); }
	      
	    FileOutputStream out = null;
        try {
        	out = new FileOutputStream(fileName_raw, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (handler != null) {
				handler.onRecordingError();
			}
        }
        return out;
    }
    
    private void write(FileOutputStream out) {
    	try {
            out.write(audioBuffer);
    	} catch (IOException e) {
            e.printStackTrace();
            if (handler != null) {
				handler.onRecordingError();
			}
    	}
    }
    
    private void finishWriting(FileOutputStream out) {
    	try {
    		out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        if (handler != null) {
				handler.onRecordingError();
			}
	    }
    }
    
    private String getRawName(String fileWavName) {
    	return String.format("%s/%s", getFileDir(fileWavName), AUDIO_RECORDER_TEMP_FILE);
    }
    
    private String getFileDir(String fileWavName) {
    	File file = new File(fileWavName);
    	String dir = file.getParent();
    	return (dir == null) ? "" : dir;
    }
    
    private void convertRawToWav() {
    	File file_raw = new File(fileName_raw);
    	if (!file_raw.exists()) { return; }
    	File file_wav = new File(fileName_wav);
    	if (file_wav.exists()) { file_wav.delete(); }
        try {
			PcmAudioHelper.convertRawToWav(WavAudioFormat.mono16Bit(SAMPLING_RATE), file_raw, file_wav);
			file_raw.delete();
			if (handler != null) {
				handler.onRecordSuccess();
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (handler != null) {
				handler.onRecordSaveError();
			}
		}
    }
    
    public synchronized void stopRecording() {
    	isRecording = false;
    }
}