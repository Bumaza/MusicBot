package music.bumaza.musicbot.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static music.bumaza.musicbot.utils.AppUtils.convertToPx;

public class Note {

    private float x, y, radius, width, height;
    private boolean isFill = true;
    private boolean hasLeg = true;
    private boolean isDownLeg;

    private float speed = 5.55f;
    private float offSet;
    private float legSize;
    private float rotateAngle = 35.00f;

    public Note(float x, float y, float width, float height, float legSize, float offSet, float centerY) {
        this.x = x;
        this.y = y;
        this.width = width / 2;
        this.height = height / 2;
        this.legSize = legSize;
        this.offSet = offSet;
        this.isDownLeg = y < centerY;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas canvas, Paint whitePenFill, Paint whitePenStroke){
        canvas.rotate(-rotateAngle, x, y);
        canvas.drawOval(x-width,y-height,x+width, y+height, isFill ? whitePenFill : whitePenStroke);
        canvas.rotate(rotateAngle, x, y);
        if(hasLeg){
            if(isDownLeg) canvas.drawRect(x-width+offSet/2,y+height, x-width+offSet, y+height+legSize, whitePenFill);
            else canvas.drawRect(x+width-offSet, y-legSize-height, x+width-offSet/2, y-height+offSet, whitePenFill);
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public boolean isHasLeg() {
        return hasLeg;
    }

    public void setHasLeg(boolean hasLeg) {
        this.hasLeg = hasLeg;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getLegSize() {
        return legSize;
    }

    public void setLegSize(float legSize) {
        this.legSize = legSize;
    }

    public float getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(float rotateAngle) {
        this.rotateAngle = rotateAngle;
    }
}
