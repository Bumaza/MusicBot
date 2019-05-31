package music.bumaza.musicbot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import music.bumaza.musicbot.R;
import music.bumaza.musicbot.data.Tone;

import java.util.List;

public class TonePageAdapter extends PagerAdapter{

    private LayoutInflater inflater;
    private List<Tone> notes;

    public TonePageAdapter(Context context, List<Tone> notes){
        inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String note = notes.get(position).getName();
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.tone_item, container, false);

        TextView tvTone = layout.findViewById(R.id.tone);
        TextView tvSharp = layout.findViewById(R.id.sharp);
        TextView tvOctave = layout.findViewById(R.id.octave);


        if(note.length() > 1){
            tvSharp.setVisibility(View.VISIBLE);
            tvTone.setText(String.valueOf(note.charAt(0)));
        }else{
            tvTone.setText(note);
        }
        tvOctave.setText(notes.get(position).getOctaveName());

        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
