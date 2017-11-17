package com.example.drewv.testing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by drewv on 11/16/2017.
 */

public class Player extends Object {
    public float xPos, xAccel, xVel = 0.0f;
    public float yPos, yAccel, yVel = 0.0f;
    public float radius = 50.0f;
    Player(){

    }
    Player(float startingXPos, float startingYPos){
        xPos = startingXPos;
        yPos = startingYPos;
    }
}

