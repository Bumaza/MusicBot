package music.bumaza.musicbot.data;

import java.util.Arrays;
import java.util.List;

public enum Tone {

    C("C", 261.63),
    CIS("C#", 277.18),
    D("D", 293.66),
    DIS("D#", 311.13),
    E("E", 329.63),
    F("F", 349.23),
    FIS("F#", 369.99),
    G("G", 392.00),
    GIS("G#", 415.30),
    A("A", 440.0000),
    B("A#", 466.16),
    H("H", 493.88),
    C3("C3", 523.25);

    public static List<Tone> tones = Arrays.asList(Tone.C, Tone.CIS, Tone.D, Tone.DIS, Tone.E, Tone.F,
            Tone.FIS, Tone.G, Tone.GIS, Tone.A, Tone.B, Tone.H, Tone.C3);


    private double frequency;
    private String name;

    Tone(String name, double frequency){
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public double getFrequency(){
        return frequency;
    }

    public static String getToneFromFrequency(double value) {

        int length = tones.size();
        int low = 0;
        int high = length;
        while (low < high) {
            final int mid = (low + high) / 2;
            if (value <= tones.get(mid).frequency) high = mid;
            else low = mid + 1;
        }

        if(low >= length)
            return tones.get(length-1).name;

        if(low > 0 && Math.abs(value - tones.get(low-1).frequency) < Math.abs(value - tones.get(low).frequency))
            return tones.get(low - 1).name;

        return tones.get(low).name;
    }

    public static int getIndexOfTone(double value) {

        int length = tones.size();
        int low = 0;
        int high = length;
        while (low < high) {
            final int mid = (low + high) / 2;
            if (value <= tones.get(mid).frequency) high = mid;
            else low = mid + 1;
        }

        if(low >= length)
            return length-1;

        if(low > 0 && Math.abs(value - tones.get(low-1).frequency) < Math.abs(value - tones.get(low).frequency))
            return low - 1;

        return low;
    }
}
