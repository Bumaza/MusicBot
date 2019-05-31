package music.bumaza.musicbot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import music.bumaza.musicbot.data.Note;

import static music.bumaza.musicbot.utils.AppUtils.*;

public class MusicSheetView extends View{

    /**
     * Java
     */
    private int lineWidthDP = 2;
    private int gap = convertToPx(3);
    private int legSize;

    private ArrayList<Note> myNotes = new ArrayList<>();

    private float radiusOfNotes;

    /**
     * Android
     */
    private Paint blackPenStroke, whitePenStroke, whitePenFill;
    private RectF stage;

    private float point = 0;


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

        radiusOfNotes = convertToPx(5); //height 50 dp

        osnova(canvas);

        drawDemoNotes(canvas);

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

        stage = new RectF(0, 0, getWidth(), getHeight());

        legSize = (int) (convertToPx(50) / 1.5);


        myNotes.add(new Note(1000, 100, 15, 100));
        myNotes.add(new Note(1200, 200, 15, 100));
        myNotes.add(new Note(1450, 150, 15, 100));
        myNotes.add(new Note(1600, 50, 15, 100));


    }

    private void drawDemoNotes(Canvas canvas){
        //notes(canvas, true);
        for(Note note : myNotes)
            note.draw(canvas);
    }

    public void update(float interpolatedTime){
        for(Note note : myNotes){
            note.setX(note.getX()- (note.getSpeed() * interpolatedTime));
        }
    }

    private void notes(Canvas canvas, boolean fill){
        canvas.drawCircle(getWidth()/2, getHeight()/2, radiusOfNotes, fill ? whitePenFill : whitePenStroke);
        canvas.drawLine(getWidth()/2 + radiusOfNotes , getHeight()/2, getWidth()/2 + radiusOfNotes, getMeasuredHeight() ,  whitePenStroke);
    }

    private void osnova(Canvas canvas){
        canvas.drawLine(0,  getHeight() , getWidth(), getHeight(), whitePenStroke);
        canvas.drawLine(0,  getHeight() / 4  , getWidth(), getHeight() / 4 , whitePenStroke);
        canvas.drawLine(0,  0 , getWidth(), 0 , whitePenStroke);
        canvas.drawLine(0,  getHeight() - getHeight() / 4  , getWidth(), getHeight() - getHeight() / 4  , whitePenStroke);
        canvas.drawLine(0,  getHeight() / 2, getWidth(), getHeight() /2  , whitePenStroke);
    }
}

