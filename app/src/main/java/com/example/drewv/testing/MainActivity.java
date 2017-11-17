package com.example.drewv.testing;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private float xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;
    private long curT = 0;
    private int currentLevel = 1;
    private Game g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //used to see how big screen is
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        //make a new game with Level(lvlNum X) where X is which level we are on
        g = new Game(new Level(currentLevel), size, this);
        GameView gameView = new GameView(this);
        setContentView(gameView);

        //keep screen in portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        curT = System.nanoTime();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        g.resetGame(new Level(g.level.levelNum));
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && g.state == GameState.RUNNING) {
            float newXAccel = sensorEvent.values[0] /2;
            float newYAccel = -sensorEvent.values[1] /2;
            g.updatePlayerAccel(newXAccel,newYAccel);
            g.updatePlayerPos();
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //don't worry about accuracy
    }


    private class GameView extends View {

        public GameView(Context context) {
            super(context);

            //get the bitmap for the ball to represent the player object
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = (int) (2*g.player.radius);
            final int dstHeight = (int) (2*g.player.radius);
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //draw the bitmap so the image's center is aligned with the center of the player object
            canvas.drawBitmap(ball,  g.player.xPos-g.player.radius,  g.player.yPos-g.player.radius, null);

            Paint colorOfLastBlockTouched = new Paint();
            colorOfLastBlockTouched.setColor(Color.RED);

            for (Block b: g.level.blocks) {
                if (b.equals(g.lastBlockTouched))
                    b.back_color.setColor(Color.RED);
                else
                    b.back_color.setColor(Color.BLUE);

                b.drawBlock(canvas);
            }
            setActionBar( "Goal: " + g.goal + " CurrentValue: "+ g.currentValue );

            invalidate();
        }
    }

    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        actionBar.setTitle(heading);
        actionBar.show();

    }


}


