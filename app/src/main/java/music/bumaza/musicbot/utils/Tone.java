package music.bumaza.musicbot.utils;

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
    A("A", 415.30),
    B("A#", 466.16),
    H("H", 493.88),
    C3("C3", 523.25);

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
}
