package Tetris;

// code copied from Simon Lucas
// code copied by Udo Kruschwitz
// code completed by Michael Barham

import Utilities.JEasyFrame;

import java.util.Observable;
import java.util.Random;

public class TetrisViewTest implements TetrisViewTestInterface {
    static TetrisView tv;

    public static void main(String[] args) {
        // test the view component
        //Random r = new Random();
        int w = 10;
        int h = 20;
        int time_interval = 500;
        boolean game_going = true;
        int[][] a = new int[w][h];

        tv = new TetrisView(w, h, time_interval);
        //tv.startTimer(tv.time_interval);
        JEasyFrame Frame= new JEasyFrame(tv/*, w, h, tv.size*/, "Michael Barham 1304335");
        //new JAppletView(tv, w, h, TetrisView.size);
        tv.startTimer(tv.time_interval, game_going);
    }
}