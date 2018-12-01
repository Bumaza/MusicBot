package music.bumaza.musicbot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static music.bumaza.musicbot.utils.AppConstants.*;

public class MusicSheetView extends View{

    /**
     * Java
     */
    private int lineWidthDP = 2;
    private int gap = convertToPx(3);
    private int legSize;

    private float radiusOfNotes;

    /**
     * Android
     */
    private Paint blackPenStroke, whitePenStroke, whitePenFill;
    private RectF stage;


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

        radiusOfNotes = getHeight() / 10;

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


        legSize = getHeight() / 2;
    }

    private void drawDemoNotes(Canvas canvas){
        //canvas.drawCircle(getWidth()/4, getHeight() / 4, radiusOfNotes, whitePenFill);
        notes(canvas, true);
        //canvas.drawCircle(getWidth() - getWidth()/4, getHeight() - getHeight() / 4, radiusOfNotes, whitePenStroke);
    }

    private void notes(Canvas canvas, boolean fill){
        canvas.drawCircle(getWidth()/2, getHeight()/2, radiusOfNotes, fill ? whitePenFill : whitePenStroke);
        canvas.drawLine(getWidth()/2 + radiusOfNotes, getHeight()/2 - legSize, getWidth()/2+radiusOfNotes, getHeight()/2, fill ? whitePenFill : whitePenStroke);
    }

    private void osnova(Canvas canvas){
        canvas.drawLine(0,  getHeight() , getWidth(), getHeight(), whitePenStroke);
        canvas.drawLine(0,  getHeight() / 4  , getWidth(), getHeight() / 4 , whitePenStroke);
        canvas.drawLine(0,  0 , getWidth(), 0 , whitePenStroke);
        canvas.drawLine(0,  getHeight() - getHeight() / 4  , getWidth(), getHeight() - getHeight() / 4  , whitePenStroke);
        canvas.drawLine(0,  getHeight() / 2, getWidth(), getHeight() /2  , whitePenStroke);
    }
}

