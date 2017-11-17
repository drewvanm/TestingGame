package com.example.drewv.testing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.constraint.solver.widgets.Rectangle;
import android.widget.Switch;

import java.lang.reflect.Type;

/**
 * Created by drewv on 11/16/2017.
 */

public class Block extends Rectangle {
    public int value;
    public Func func;
    public boolean lastHit = false;
    public Paint back_color;
    public Paint text_color;

    public Block(int x, int y, int width, int height, Func func, int val){
        super();
        this.setBounds(x,y,width,height);

        value = val;
        this.func = func;

        back_color = new Paint();
        back_color.setColor(Color.BLUE);

        text_color = new Paint();
        text_color.setColor(Color.WHITE);
        int smallest = Math.min(width,height);
        text_color.setTextSize(smallest/1.5f);
        text_color.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        text_color.setTextAlign(Paint.Align.CENTER);


    }

    /**
     *
     * @return a Rect conversion to use for drawing
     */
    public Rect toRect(){
        Rect rec = new Rect();
        int left, top, right, bottom;
        left = this.x;
        top = this.y;
        right = this.x +(this.width);
        bottom = this.y + (this.height);
        rec.set(left,top,right,bottom);
        return rec;
    }


    public void drawBlock(Canvas canvas)
    {
        canvas.drawRect(toRect(), back_color);
        canvas.drawText(getText(), x+width/2, (y+height/2)+(text_color.getTextSize()/2.4f), text_color);
    }

    public String getText(){
        String symbol;
        switch (func){
            case ADD:
                symbol = "+";
                break;
            case SUBTRACT:
                symbol = "-";
                break;
            case MULTIPLY:
                symbol = "*";
                break;
            case EXPONENT:
                symbol = "^";
                break;
            default:
                symbol = "?";
                break;
        }
        return symbol + value;
    }

    public int performFunction(int currentValue){
        switch (func){
            case ADD:
                return currentValue + value;
            case SUBTRACT:
                return currentValue - value;
            case MULTIPLY:
                return currentValue * value;
            case EXPONENT:
                return (int) Math.pow(currentValue,value);
            default:
                return currentValue;
        }
    }
}
