package music.bumaza.musicbot.utils;

public enum Tone {

    C(261.63), CIS(277.18),
    D(293.66), DIS(311.13),
    E(329.63),
    F(349.23), FIS(369.99),
    G(392.00), GIS(415.30),
    A(415.30), B(466.16),
    H(493.88),
    C3(523.25);

    private double frequency;

    Tone(double frequency){
        this.frequency = frequency;
    }

    public double getFrequency(){
        return frequency;
    }
}
