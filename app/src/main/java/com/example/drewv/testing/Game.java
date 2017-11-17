package com.example.drewv.testing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.constraint.solver.widgets.Rectangle;

/**
 * Created by drewv on 11/16/2017.
 */

public class Game extends Object {
    public Level level;
    public int goal;
    public int currentValue = 1;
    public Player player;
    public Point size;
    public Point topLeft;
    public Block lastBlockTouched;
    public GameState state;
    public Context context;

    public Game(Level lvl, Point screenSize, Context context){
        level = lvl;
        player = new Player(50,50);
        goal = lvl.goal;
        state = GameState.PAUSE;
        this.context = context;

        //make bounce off the walls accurate
        size = screenSize;
        size.x -= player.radius;
        size.y -= player.radius;
        size.y -= 200;

        topLeft = new Point((int) player.radius, (int) player.radius);
        showMessage("Welcome to Balls and Blocks\nPress Play to Start", "Play");
    }

    public void updatePlayerAccel(float x, float y){
        player.xAccel = x;
        player.yAccel = y;
    }

    public void updatePlayerPos(){
        //can change frameTime to slow game down
        //float frameTime = .666f;
        float frameTime = .2f;

        player.xVel += ( player.xAccel * frameTime);
        player.yVel += ( player.yAccel * frameTime);

        float xS = ( player.xVel / 2) * frameTime;
        float yS = ( player.yVel / 2) * frameTime;

        player.xPos -= xS;
        player.yPos -= yS;


        //check for collision with screen
        //if they do collide then reverse velocity
        if ( player.xPos >  size.x) {
            player.xPos =  size.x;
            player.xVel *= -1;
        } else if ( player.xPos < topLeft.x) {
            player.xPos = topLeft.x;
            player.xVel *= -1;
        }

        if ( player.yPos >  size.y) {
            player.yPos =  size.y;
            player.yVel *=-1;
        } else if ( player.yPos < topLeft.y) {
            player.yPos = topLeft.y;
            player.yVel *=-1;
        }

        handleCollisions();

    }

    public void handleCollisions(){
        //check every block in the game for collisions
        for (Block block: level.blocks) {
            int collisionResultCode = isColliding(block);
            if (collisionResultCode == 1 || collisionResultCode == 2 || collisionResultCode == 3)
            {
                //this is where your current score is updated after hitting a new block
                if (!block.equals(lastBlockTouched)){
                    setCurrentScore( block.performFunction(currentValue));

                    System.out.println("CURRENT SCORE IS " + currentValue);
                    if (currentValue == goal){
                        System.out.println("YOU WIN!!!!!!!!!!!!!!!");
                        state = GameState.WIN;
                        resetGame(new Level(level.levelNum+1));
                    }
                }
                lastBlockTouched = block;
            }

            //change velocity depending on what side of a block we hit
            if (collisionResultCode == 1){
                player.yVel *= -1;
            }else if (collisionResultCode == 2){
                player.xVel *= -1;
            }else if (collisionResultCode == 3){
                player.xVel *= -1;
                player.yVel *= -1;
            }
        }

    }

    /**
     * See if a block is hit and what side was hit
     * @param b the potential collision block
     * @return 0 if no collision, 1 for x axis collision, 2 for y axis collison,
     *          and 3 for corner collision
     */
    public int isColliding(Block b){
        float distX = Math.abs(player.xPos - b.x-b.width/2);
        float distY = Math.abs(player.yPos - b.y-b.height/2);

        if (distX > (b.width/2 + player.radius)) { return 0; }
        if (distY > (b.height/2 + player.radius)) { return 0; }

        if (distX <= (b.width/2)) { return 1; }
        if (distY <= (b.height/2)) { return 2; }

        float dx=distX-b.width/2;
        float dy=distY-b.height/2;
        if (dx*dx+dy*dy<=(player.radius*player.radius)){
            System.out.println("CORNER COLLISION INFO, dx="+dx +" dy="+dy+ " hypot= "+Math.sqrt((dx*dx+dy*dy)));
            System.out.println("VELOCITY VECTORS, xVel=" + player.xVel + " yVel=" + player.yVel );
            float actX = Math.abs(player.xPos - b.x-b.width/2);
            float actY = Math.abs(player.yPos - b.y-b.height/2);
            float ax=actX-b.width/2;
            float ay=actY-b.height/2;
            System.out.println("CORNER COLLISION INFO, ax="+ax +" ay="+ay+ " hypot= "+Math.sqrt((ax*ax+ay*ay)));
            return 3;
        }
        return 0;
    }

    void setCurrentScore(int score){
        if (score  > 9999){
            currentValue = 9999;
        }
        else
        {
            currentValue = score;
        }
    }

    void resetGame(Level lvl){
        level = lvl;
        player = new Player(50,50);
        goal = lvl.goal;
        currentValue = 1;
        state = GameState.PAUSE;
        showMessage("Press Play","Play");
    }

    public void showMessage(String message, String textOfOkayButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(textOfOkayButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        state = GameState.RUNNING;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
