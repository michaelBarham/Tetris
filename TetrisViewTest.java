package Tetris;

import Utilities.JEasyFrame;

public class TetrisViewTest{
    static TetrisView tv;

    public static void main(String[] args) {
        // test the view component
        tv = new TetrisView(0);
        JEasyFrame Frame= new JEasyFrame(tv, "Michael Barham 1304335");
        //new JAppletView(tv, w, h, TetrisView.size);
        tv.startFallingTimer(tv.time_interval);
    }
}