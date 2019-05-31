package music.bumaza.musicbot.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class NoteAnimation extends Animation {

    private MusicSheetView view;

    public NoteAnimation(MusicSheetView view) {
        this.view = view;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.update(interpolatedTime);
        view.requestLayout();
    }
}
