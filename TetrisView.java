package Tetris;

/*
created by Michael Barham
*/

import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.awt.*;
import java.util.*;
import static java.awt.Color.*;
import java.util.Random;

public class TetrisView extends JComponent implements Observer{
    // selector of shapes
    Random r = new Random();

    // current falling shapes and XY of all previous still visible
    static Shape CurrentFallingShape;
    static Shape AllPreviousShapeBlocks;

    // used as holder for tetris shapes
    static ArrayList<Shape> TetrisShapes = new ArrayList<>();

    int w;
    int h;
    static int size = 30;//increased because i thought it was to small
    int time_interval;
    static TetrisView observeShapesView = new TetrisView();

    public TetrisView(){}

    public TetrisView(int NullVariable) {
        this.w = 10;
        this.h = 20;
        this.time_interval = 300;

        TetrisShapes.add(new Shape("O", new ArrayList<>(Arrays.asList(yellow)), new int[][]{{0, 0},  {-1, -1},{-1, 0}, {0, -1}}));
        TetrisShapes.add(new Shape("N", new ArrayList<>(Arrays.asList(blue)), new int[][]{{0, -1}, {0, 0},  {-1, 0}, {-1, 1}}));
        TetrisShapes.add(new Shape("Z", new ArrayList<>(Arrays.asList(magenta)), new int[][]{{0, -1}, {0, 0},  {1, 0},  {1, 1}}));
        TetrisShapes.add(new Shape("I", new ArrayList<>(Arrays.asList(red)), new int[][]{{0, -1}, {0, 0},  {0, 1},  {0, 2}}));
        TetrisShapes.add(new Shape("T", new ArrayList<>(Arrays.asList(pink)), new int[][]{{-1, 0}, {0, 0},  {1, 0},  {0, 1}}));
        TetrisShapes.add(new Shape("L", new ArrayList<>(Arrays.asList(cyan)), new int[][]{{-1, -1},{0, -1}, {0, 0},  {0, 1}}));
        TetrisShapes.add(new Shape("J", new ArrayList<>(Arrays.asList(green)), new int[][]{{1, -1}, {0, -1}, {0, 0},  {0, 1}}));

        // select random shape
        CurrentFallingShape = new Shape(TetrisShapes.get(r.nextInt(TetrisShapes.size())));
        CurrentFallingShape.addObserver(observeShapesView);

        // create (almost) blank shape to add previous shapes to
        AllPreviousShapeBlocks = new Shape("AllPreviousShapes");

        //The MouseClicked method is used to determine the actions required on a mouse event.
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.NOBUTTON) {
                    System.out.println("No button clicked...");
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    //System.out.println("Button 1 clicked...");
                    CurrentFallingShape.MoveShapeLeft(AllPreviousShapeBlocks);
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    //System.out.println("Button 2 clicked...");
                    //CurrentShapes[0].AttemptMoveShapeDown(CurrentShapes[1]);
                    CurrentFallingShape.rotateLeft(AllPreviousShapeBlocks);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //System.out.println("Button 3 clicked...");
                    CurrentFallingShape.MoveShapeRight(AllPreviousShapeBlocks);
                }
            }
        });

        //this.addKeyListener(new MyKeyListener());
        /*this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                System.out.println(e.getKeyChar() + "!!!");
                if (e.getKeyCode() == 0) {
                    CurrentFallingShape.MoveShapeRight(AllPreviousShapeBlocks);
                    System.out.println("Right key typed");
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    CurrentFallingShape.MoveShapeLeft(AllPreviousShapeBlocks);
                    System.out.println("Left key typed");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("down key typed");
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    CurrentFallingShape.rotateLeft(AllPreviousShapeBlocks);
                    System.out.println("up key typed");
                }
            }
        });*/
    }


    public void startFallingTimer(int time_interval) {
        // creating timer task, timer
        Timer timer = new Timer();
        TimerTask taskNew = new TimerTask() {
            @Override
            public void run() {
                runTimerEvent();
            }
        };
        // scheduling the task at interval
        timer.scheduleAtFixedRate(taskNew, 100, time_interval);
    }

    public void runTimerEvent(){
        // Test Successful move
        if(!CurrentFallingShape.AttemptMoveShapeDown(AllPreviousShapeBlocks)){
            morphFallingShapesToOldShapesHolder();
            testIfRowsFilled();
            CurrentFallingShape = new Shape(TetrisShapes.get(r.nextInt(TetrisShapes.size())));
        }
        repaint();
    }

    private void testIfRowsFilled(){
        // now delete rows if filled
        // create temporary array to save get pulling values all the time
        ArrayList<int[]> tempXY = AllPreviousShapeBlocks.getXandY();
        ArrayList<Color> tempCol = AllPreviousShapeBlocks.getColor();

        // set blank array to false
        boolean[][] grid = new boolean[20][10];
        for(boolean[] row : grid){
            for(boolean column : row){
                column = false;
            }
        }

        // identify all locations with blockes
        for(int[] item : tempXY){
            grid[item[1]-1][item[0] +4] = true;
            /*try{
                grid[item[1]-1][item[0] +4] = true;
            } catch(Exception e) {
                System.out.println(e.getStackTrace());
                System.out.println(e.fillInStackTrace());
                System.out.println(item[1]-1 + "Max:" + 20);
                System.out.println(item[0] +4 + "Max:" + 10);
            }*/
        }

        for(int row = 0; row < 20; row++){
            boolean rowFull = true;
            for(boolean column : grid[row]){
                if(!column){
                    rowFull = false;
                    break;
                }
            }

            if(rowFull){
                // remove shapes within the row
                for(int shape = 0; shape < tempXY.size(); shape++) {
                    // check if each shape block is within row
                    if(tempXY.get(shape)[1] == row){
                        tempXY.remove(shape);
                        tempCol.remove(shape);
                        shape--;
                    }
                }

                // move all shapes above down
                for(int shape = 0; shape < tempXY.size(); shape++) {
                    if(tempXY.get(shape)[1] < row){
                        tempXY.get(shape)[1]++;
                    }
                }

                // set them back again
                AllPreviousShapeBlocks.setColor(tempCol);
                AllPreviousShapeBlocks.setXandY(tempXY);

                for(boolean[] column : grid){
                    for(boolean item : column){
                        item = false;
                    }
                }

                // identify all locations with blockes
                // reset grid due to reshake
                for(int[] item : tempXY){
                    grid[item[1]-1][item[0] +4] = true;
                /*try{
                    grid[item[1]-1][item[0] +4] = true;
                } catch(Exception e) {
                    System.out.println(e.getStackTrace());
                    System.out.println(e.fillInStackTrace());
                    System.out.println(item[1]-1 + "ResetMax:" + 20);
                    System.out.println(item[0] +4 + "ResetMax:" + 10);
                }*/
                }
            }
        }
    }

    private void morphFallingShapesToOldShapesHolder() {
        Color Col = CurrentFallingShape.getColor().get(0);
        int[] relationPosition = CurrentFallingShape.getRP();

        for(int[] coordinates : CurrentFallingShape.getXandY()){
            AllPreviousShapeBlocks.addShapeBlock(Col,
                    new int[][]{{(coordinates[0] + relationPosition[0]),
                            (coordinates[1] + relationPosition[1])}});
        }
    }

    public void paintComponent(Graphics g) {
        // create background
        g.setColor(BLACK);
        g.fill3DRect(0, 0, w * size, h * size, true);

        PaintItem(g, CurrentFallingShape);
        PaintItem(g, AllPreviousShapeBlocks);
    }

    public void PaintItem(Graphics g, Shape shape) {
        ArrayList<Color> tempCol = shape.getColor();
        ArrayList<int[]> tempXY = shape.getXandY();
        // if given single shape run with copied colour; if multiple run each; else print error
        if(tempCol.size() == 1){
            g.setColor(tempCol.get(0));
            for(int[] item : tempXY){
                g.fill3DRect((4*size) + (size * (item[0] + shape.getRP()[0])),
                        (size * (item[1] + shape.getRP()[1])), size, size, true);
            }
        } else if (tempXY.size() == tempCol.size()) {
            for(int i = 0; i < tempXY.size(); i++){
                g.setColor(tempCol.get(i));
                g.fill3DRect((4*size) + (size * (tempXY.get(i)[0] + shape.getRP()[0])),
                        (size * (tempXY.get(i)[1] + shape.getRP()[1])), size, size, true);
            }
        } else {
            System.out.println("Error occured in PaintComponent\nInvalid number of colours submitted");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(w * size, h * size);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}

class MyKeyListener implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key typed: " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released: " + e.getKeyChar());
    }
}
