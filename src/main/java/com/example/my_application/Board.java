package com.example.my_application;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Board extends View {

    ArrayList<point> enemyList = new ArrayList<point>(); // מערך האויבים במשחק

    Random rand = new Random();
    float x,y; // המקום של הקוף
    int lives = 3;
    int score = 0;
    int startUp = 0; // משמש לביצוע פעולה בהרצה ראשונה
    int yz, xz; // מכיל את גודל המסך, אורך ורוחב
    int size = 270; // גודל התמונה של הקוף
    Bitmap bitmap; // התמונה של הקוף, יוגדר בהמשך
    int enemySpd = 10;
    public static boolean isPaused = false;

    moveThread moveThread; // פועל כל 0.05 שניות, מאפס את הondraw
    enemyThread enemyThread; // פועל כל שנייה, יוצר אויבים

    Handler handler, scoreHandler; // מבצע את הפעולות של הThreads

    boolean  isRelease = true;

    Bitmap beeBM = BitmapFactory.decodeResource(getResources(),R.drawable.minibee);
    Bitmap birdBM = BitmapFactory.decodeResource(getResources(),R.drawable.banana);

    Context context;


    public Board(final Context context ,Handler scoreHandler) {

        super(context);
        this.context=context;
        this.scoreHandler = scoreHandler;
        moveThread = new moveThread();
        moveThread.start();
        enemyThread = new enemyThread();
        enemyThread.start();
        x = 20; // מגדיר את המקום ההתחלתי של הקוף
        y = 700;

        handler = new Handler(new Handler.Callback() { /////////// HANDLER
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0 && !isPaused) { // כל 0.05 שניות
                    invalidate();
                }

                if (message.what == 1 && !isPaused) // כל שנייה
                {
                    int r = (rand.nextInt(5) + 1); // יוצר אויב ובננות ומכניס אותו למערך
                    if (r == 1) {
                        enemyList.add(new point(xz - 50, (rand.nextInt(yz - 50) + 50), beeBM, "enemy"));
                    }
                    if ((r == 2||r==5) ) {
                        enemyList.add(new point(xz - 50, (rand.nextInt(yz - 50) + 50), birdBM, "food"));
                    }
                }
                return true;
            }
        });
    }

    protected void onDraw(Canvas canvas) { // מרפרש כל 0.05 שניות
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(80);
        canvas.drawLine(0,40,xz,40,paint);

        if(y<=700 && isRelease)
        {
            y=y+30;
        }
         else if(y>= 80 && !isRelease) y -= 30;

        if (startUp == 0) { // פעולה התחלתית, מגדיר את גודל המסך
            xz = getWidth();
            yz = getHeight()-150;
            startUp = 1;
        }

        Paint scoreText = new Paint(); // הגדרת הטקסטים שמופיעים על המסך; נקודות ופסילות
        Paint pauseText = new Paint();
        pauseText.setTextAlign(Paint.Align.CENTER);
        scoreText.setTextSize(50);
        pauseText.setTextSize(50);
        scoreText.isFakeBoldText();
        pauseText.isFakeBoldText();
        canvas.drawText("Score: " + score, 250, 60, scoreText);
        canvas.drawText("Speed: " + enemySpd, this.getWidth() - 350, 60, scoreText);



        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.monkeynewnew);
        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);
        canvas.drawBitmap(bitmap,x ,y,null );


        for(int i = 0; i < enemyList.size();i++) // מציג את כל האויבים
        {
            enemyList.get(i).draw(canvas);
            enemyList.get(i).setX(enemyList.get(i).getX()- enemySpd);
            if(!enemyList.isEmpty()) // כי אם הורגים את האחרון זה יקרוס ברגע שאין את הif הזה
            {
                if (onCollision(enemyList.get(i))) { // מוסיף נקודות אם הגרזן 'הורג' את האויבים כשהוא עובר דרכם
                    if(enemyList.get(i).getType() == "enemy")
                    {
                        lives--;
                    }
                    else
                    if(enemyList.get(i).getType() == "food") {
                        if (score >= 0) {
                            score += 10;
                        }
                    }
                    enemyList.remove(i);
                }
                switch(score)
                {
                    case 50:
                        enemySpd = 15;

                        break;
                    case 100:
                        enemySpd = 20;

                        break;
                    case 140:
                        enemySpd = 35;
                        ;
                        break;
                    case 170:
                        enemySpd = 45;

                        break;
                    case 200:
                        enemySpd = 60;

                        break;
                }
            }
        }


        for (int i = 0; i < enemyList.size(); i++) //מאתר אויבים שהגיעו לצד השמאלי ומוריד פסילה
        {
            if (!enemyList.isEmpty()) {
                if (enemyList.get(i).getX() < 200) {
                    if (enemyList.get(i).getType() == "food") {
                        if (score != 0) {
                            score -= 5; }
                    }
                    enemyList.remove(i);
                }
            }
            if (lives == 0 && !isPaused)
            {
                canvas.drawText("Lives: " + lives, 20, 60, scoreText);
                isPaused = true;
                scoreHandler.sendEmptyMessage(score);
            }
        }
        canvas.drawText("Lives: " + lives, 20, 60, scoreText);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isRelease = false;

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isRelease = true;
        }
        return true;
    }


    public class moveThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

    public class enemyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(500);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean onCollision(point p)
    {
        if(distance(x,y, p.getX(),p.getY()) < 230)
        {return true;}
        return false;
    }


    public double distance(float x, float y, float x2, float y2) // נוסחת מרחק
    {
        double xs, ys, d;
        xs = Math.pow((x - x2), 2);
        ys = Math.pow((y - y2), 2);
        d = Math.sqrt(xs + ys);
        return d;
    }

}



