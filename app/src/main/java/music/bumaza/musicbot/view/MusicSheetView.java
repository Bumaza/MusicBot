package music.bumaza.musicbot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import music.bumaza.musicbot.R;
import music.bumaza.musicbot.data.Note;
import music.bumaza.musicbot.data.Tone;
import music.bumaza.musicbot.utils.Pair;

import static music.bumaza.musicbot.utils.AppUtils.*;

public class MusicSheetView extends View{

    /**
     * Java
     */
    private int lineWidthDP = 2;
    private int gap = convertToPx(3);
    private int height, centerY;
    private int noteWidth, noteHeight, noteOffset, noteLegSize;

    private ArrayList<Note> myNotes = new ArrayList<>();

    /**
     * Android
     */
    private Paint blackPenStroke, whitePenStroke, whitePenFill;


    public MusicSheetView(Context context) {
        super(context);
        initView();
    }

    public MusicSheetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        osnova(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawNotes(canvas);
        }
    }

    private void initView(){
        blackPenStroke = new Paint();
        blackPenStroke.setAntiAlias(true);
        blackPenStroke.setStyle(Paint.Style.STROKE);
        blackPenStroke.setStrokeWidth(convertToPx(lineWidthDP));
        blackPenStroke.setColor(0xFF000000);

        whitePenStroke = new Paint();
        whitePenStroke.setAntiAlias(true);
        whitePenStroke.setStyle(Paint.Style.STROKE);
        whitePenStroke.setStrokeWidth(convertToPx(lineWidthDP));
        whitePenStroke.setColor(0xFFFFFFFF);

        whitePenFill = new Paint();
        whitePenFill.setAntiAlias(true);
        whitePenFill.setStyle(Paint.Style.FILL);
        whitePenFill.setStrokeWidth(convertToPx(lineWidthDP));
        whitePenFill.setColor(0xFFFFFFFF);

        noteLegSize = (int) (convertDpToPixel(50, getContext()) / 1.5);
        gap = convertDpToPixel(10, getContext()); //10dp
        height = convertDpToPixel(100, getContext());
        centerY = height / 2;


        noteWidth = convertDpToPixel(14, getContext());
        noteHeight= convertDpToPixel(8, getContext());
        noteOffset = convertDpToPixel(2, getContext());

        demoNotes();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawNotes(Canvas canvas){
        for(Note note : myNotes)
            note.draw(canvas, whitePenFill, whitePenStroke);
    }

    public void update(float interpolatedTime){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myNotes.removeIf(n -> n.getX()+n.getWidth() < 0);
        }
        for(Note note : myNotes){
            note.setX(note.getX()- (note.getSpeed() * 1));
        }
    }

    public void addNote(Pair<Integer, Tone> tonePair){
        myNotes.add(new Note(getWidth(), centerY + (tonePair.getLeft() * gap/2), noteWidth, noteHeight, noteLegSize, noteOffset, centerY, tonePair.getRight()));
    }

    private void osnova(Canvas canvas){
        for(int l  : new int[]{-2, -1, 0, 1, 2}){
            canvas.drawLine(0, centerY + (gap * l) , getWidth(), centerY + (gap * l), whitePenStroke);
        }
    }

    public void clear(){
        myNotes.clear();
    }

    private void demoNotes(){
        myNotes.add(new Note(1000, height /2 - gap/2, noteWidth, noteHeight, noteLegSize, noteOffset, centerY));
        myNotes.add(new Note(1200, height /2 + gap/2, noteWidth, noteHeight, noteLegSize, noteOffset, centerY));
        myNotes.add(new Note(1450, 150, noteWidth, noteHeight, noteLegSize, noteOffset, centerY));
        myNotes.add(new Note(1600, 50, noteWidth, noteHeight, noteLegSize, noteOffset, centerY));
    }
}

