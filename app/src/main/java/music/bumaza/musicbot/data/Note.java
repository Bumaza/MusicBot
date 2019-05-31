package music.bumaza.musicbot.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import static music.bumaza.musicbot.utils.AppUtils.convertToPx;

public class Note {

    private float x, y, radius, width, height;
    private boolean isFill = true;
    private boolean hasLeg = true;

    private float speed = 5.55f;
    private float legSize;
    private float rotateAngle;

    private Paint whitePenStroke, whitePenFill;


    public Note(float x, float y, float radius, float legSize) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.legSize = legSize;
        this.rotateAngle = -convertToPx(12);

        whitePenStroke = new Paint();
        whitePenStroke.setAntiAlias(true);
        whitePenStroke.setStyle(Paint.Style.STROKE);
        whitePenStroke.setStrokeWidth(convertToPx(2));
        whitePenStroke.setColor(0xFFFFFFFF);

        whitePenFill = new Paint();
        whitePenFill.setAntiAlias(true);
        whitePenFill.setStyle(Paint.Style.FILL);
        whitePenFill.setStrokeWidth(convertToPx(6));
        whitePenFill.setColor(0xFFFFFFFF);
    }

    public void update(){
        x -= speed;
    }

    public void draw(Canvas canvas){
        canvas.rotate(-35, x, y);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(x-25,y-10,x+25, y+10, isFill ? whitePenFill : whitePenStroke);
        }else{
            canvas.drawCircle(x, y, radius, isFill ? whitePenFill : whitePenStroke);
        }
        canvas.rotate(35, x, y);
        if(hasLeg){
            canvas.drawRect(x+22-convertToPx(2), y-legSize-10, x+22, y-10, whitePenFill);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getSpeed() {
        return speed;
    }
}
