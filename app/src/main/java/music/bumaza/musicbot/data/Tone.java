package music.bumaza.musicbot.data;

import java.util.Arrays;
import java.util.List;

public enum Tone {

    /**
     * Tunning 440hz
     */

    C0("C", 16.35, Octava.CONTRA),
    CIS0("C#", 17.32, Octava.CONTRA),
    D0("D", 18.35, Octava.CONTRA),
    DIS0("D#", 19.45	, Octava.CONTRA),
    E0("E", 20.60, Octava.CONTRA),
    F0("F", 21.83, Octava.CONTRA),
    FIS0("F#", 23.12, Octava.CONTRA),
    G0("G", 24.50	, Octava.CONTRA),
    GIS0("G#", 25.96, Octava.CONTRA),
    A0("A", 27.50, Octava.CONTRA),
    B0("A#", 29.14, Octava.CONTRA),
    H0("H", 30.87, Octava.CONTRA),

    C("C", 32.70, Octava.GREAT),
    CIS("C#", 34.65, Octava.GREAT),
    D("D", 36.71, Octava.GREAT),
    DIS("D#", 38.89	, Octava.GREAT),
    E("E", 41.20, Octava.GREAT),
    F("F", 43.65, Octava.GREAT),
    FIS("F#", 46.25, Octava.GREAT),
    G("G", 49.00, Octava.GREAT),
    GIS("G#", 51.91, Octava.GREAT),
    A("A", 55.00, Octava.GREAT),
    B("A#", 58.27, Octava.GREAT),
    H("H", 61.74, Octava.GREAT),

    c("C", 65.41, Octava.SMALL),
    cis("C#", 69.30, Octava.SMALL),
    d("D", 73.42, Octava.SMALL),
    dis("D#", 77.78	, Octava.SMALL),
    e("E", 82.41, Octava.SMALL),
    f("F", 87.31, Octava.SMALL),
    fis("F#", 92.50, Octava.SMALL),
    g("G", 98.00, Octava.SMALL),
    gis("G#", 103.83, Octava.SMALL),
    a("A", 110.00, Octava.SMALL),
    b("A#", 116.54, Octava.SMALL),
    h("H", 123.47, Octava.SMALL),

    C1("C", 130.81, Octava.LINE_1),
    CIS1("C#", 138.59, Octava.LINE_1),
    D1("D", 146.83, Octava.LINE_1),
    DIS1("D#", 155.56, Octava.LINE_1),
    E1("E", 164.81, Octava.LINE_1),
    F1("F", 174.61, Octava.LINE_1),
    FIS1("F#", 185.00, Octava.LINE_1),
    G1("G", 196.00, Octava.LINE_1),
    GIS1("G#", 207.65, Octava.LINE_1),
    A1("A", 220.00, Octava.LINE_1),
    B1("A#", 233.08, Octava.LINE_1),
    H1("H", 246.94, Octava.LINE_1),

    C2("C", 261.63, Octava.LINE_2),
    CIS2("C#", 277.18, Octava.LINE_2),
    D2("D", 293.66, Octava.LINE_2),
    DIS2("D#", 311.13, Octava.LINE_2),
    E2("E", 329.63, Octava.LINE_2),
    F2("F", 349.23, Octava.LINE_2),
    FIS2("F#", 369.99, Octava.LINE_2),
    G2("G", 392.00, Octava.LINE_2),
    GIS2("G#", 415.30, Octava.LINE_2),
    A2("A", 440.0000, Octava.LINE_2),
    B2("A#", 466.16, Octava.LINE_2),
    H2("H", 493.88, Octava.LINE_2),

    C3("C", 523.25, Octava.LINE_3),
    CIS3("C#", 554.37	, Octava.LINE_3),
    D3("D", 587.33, Octava.LINE_3),
    DIS3("D#", 622.25, Octava.LINE_3),
    E3("E", 659.25, Octava.LINE_3),
    F3("F", 698.46, Octava.LINE_3),
    FIS3("F#", 739.99, Octava.LINE_3),
    G3("G", 783.99, Octava.LINE_3),
    GIS3("G#", 830.61	, Octava.LINE_3),
    A3("A", 880.00, Octava.LINE_3),
    B3("A#", 932.33	, Octava.LINE_3),
    H3("H", 987.77, Octava.LINE_3),

    C4("C",1046.50, Octava.LINE_4);

    private enum Octava{
        SUBCONTRA("Subcontra"), CONTRA("Contra"),
        GREAT("Great"), SMALL("Small"),
        LINE_1("1 Line"), LINE_2("2 Line"),
        LINE_3("3 Line"), LINE_4("4 Line");

        private String name;

        Octava(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public static List<Tone> tones = Arrays.asList(
            //CONTRA
            Tone.C0, Tone.CIS0, Tone.D0, Tone.DIS0, Tone.E0,Tone.F0, Tone.FIS0, Tone.G0, Tone.GIS0, Tone.A0, Tone.B0, Tone.H0,
            Tone.C, Tone.CIS, Tone.D, Tone.DIS, Tone.E, Tone.F, Tone.FIS, Tone.G, Tone.GIS, Tone.A, Tone.B, Tone.H,
            Tone.c, Tone.cis, Tone.d, Tone.dis, Tone.e, Tone.f, Tone.fis, Tone.g, Tone.gis, Tone.a, Tone.b, Tone.h,
            Tone.C1, Tone.CIS1, Tone.D1, Tone.DIS1, Tone.E1, Tone.F1, Tone.FIS1, Tone.G1, Tone.GIS1, Tone.A1, Tone.B1, Tone.H1,
            Tone.C2, Tone.CIS2, Tone.D2, Tone.DIS2, Tone.E2, Tone.F2, Tone.FIS2, Tone.G2, Tone.GIS2, Tone.A2, Tone.B2, Tone.H2,
            Tone.C3, Tone.CIS3, Tone.D3, Tone.DIS3, Tone.E3, Tone.F3, Tone.FIS3, Tone.G3, Tone.GIS3, Tone.A3, Tone.B3, Tone.H3, Tone.C4);

    private double frequency;
    private String name;
    private Octava octava;

    Tone(String name, double frequency, Octava octava){
        this.name = name;
        this.frequency = frequency;
        this.octava = octava;
    }

    public Octava getOctava() {
        return octava;
    }

    public String getOctaveName(){
        return octava.getName();
    }

    public String getName() {
        return name;
    }

    public double getFrequency(){
        return frequency;
    }

    public static String getToneFromFrequency(double value) {

        int length = tones.size();
        int low = getLow(value);

        if(low >= length)
            return tones.get(length-1).name;

        if(low > 0 && Math.abs(value - tones.get(low-1).frequency) < Math.abs(value - tones.get(low).frequency))
            return tones.get(low - 1).name;

        return tones.get(low).name;
    }

    private static int getLow(double value){
        int length = tones.size();
        int low = 0;
        int high = length;
        while (low < high) {
            final int mid = (low + high) / 2;
            if (value <= tones.get(mid).frequency) high = mid;
            else low = mid + 1;
        }
        return low;
    }

    public static int getIndexOfTone(double value) {

        int length = tones.size();
        int low = getLow(value);

        if(low >= length)
            return length-1;

        if(low > 0 && Math.abs(value - tones.get(low-1).frequency) < Math.abs(value - tones.get(low).frequency))
            return low - 1;

        return low;
    }
}