package com.example.my_application;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class point {

    private float x,y;
    Bitmap bitmap;
    String type;

    public point(float x, float y, Bitmap bitmap, String type) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.type = type;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public synchronized void draw(Canvas canvas){
        canvas.drawBitmap(bitmap,x,y,null);
    }
}
