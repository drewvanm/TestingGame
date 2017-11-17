package com.example.drewv.testing;

import java.util.ArrayList;

/**
 * Created by drewv on 11/16/2017.
 */

public class Level extends Object {
    public int goal;
    public ArrayList<Block> blocks = new ArrayList<Block>();
    public int levelNum;

    public Level(int lvlNum){
        levelNum = lvlNum;

        if (lvlNum == 1)
        {
            goal = 4;
            blocks.add(new Block(200, 100, 400, 200, Func.ADD, 1));
            blocks.add(new Block(0, 1000, 40, 200, Func.ADD, 2));

        }
        else if (lvlNum == 2)
        {
            goal = 32;
            blocks.add(new Block(400, 100, 700, 500, Func.ADD, 1));
            blocks.add(new Block(100, 1400, 200, 200, Func.MULTIPLY, 0));
            blocks.add(new Block(800, 1200, 200, 200, Func.MULTIPLY, 2));
            blocks.add(new Block(0, 1000, 400, 200, Func.MULTIPLY, 1));
        }
        else{
            goal = -1;
        }
    }
}
