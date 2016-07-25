package Tetris;

// created by Michael Barham

import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.awt.*;
import java.util.*;

// import all the Colors
import static java.awt.Color.*;
import java.util.Random;

public class TetrisView extends JComponent implements Observer{
    Random r = new Random();
    //static Color[] colors = {black, green, blue, red, yellow, magenta, pink, cyan};
    // used as holder for tetris shapes
    static ArrayList<Shape> TetrisShapes = new ArrayList<Shape>();
    // background black
    static Shape BackGroundFill;
    // current falling shapes and XY of all previous still visible
    static ArrayList<Shape> CurrentShapes = new ArrayList<Shape>();

    int w = 10, h = 20;
    static int[][] backgroundBlack;
    static int size = 30;//increased because i thought it was to small
    int time_interval;
    static TetrisView observeShapesView = new TetrisView();

    public TetrisView() {}

    public TetrisView(int w, int h, int time_interval) {
        TetrisShapes.add(new Shape("O", new int[]{0, 0}));
        TetrisShapes.add(new Shape("N", new int[]{0, 0}));
        TetrisShapes.add(new Shape("Z", new int[]{0, 0}));
        TetrisShapes.add(new Shape("I", new int[]{0, 0}));
        TetrisShapes.add(new Shape("T", new int[]{0, 0}));
        TetrisShapes.add(new Shape("L", new int[]{0, 0}));
        TetrisShapes.add(new Shape("J", new int[]{0, 0}));

        // could be placed back into Shape constructor
        TetrisShapes.get(0).addXandY(new Color[]{yellow}, new int[][]{{0, 0},  {-1, -1},{-1, 0}, {0, -1}});
        TetrisShapes.get(1).addXandY(new Color[]{blue}, new int[][]{{0, -1}, {0, 0},  {-1, 0}, {-1, 1}});
        TetrisShapes.get(2).addXandY(new Color[]{magenta}, new int[][]{{0, -1}, {0, 0},  {1, 0},  {1, 1}});
        TetrisShapes.get(3).addXandY(new Color[]{red}, new int[][]{{0, -1}, {0, 0},  {0, 1},  {0, 2}});
        TetrisShapes.get(4).addXandY(new Color[]{pink}, new int[][]{{-1, 0}, {0, 0},  {1, 0},  {0, 1}});
        TetrisShapes.get(5).addXandY(new Color[]{cyan}, new int[][]{{-1, -1},{0, -1}, {0, 0},  {0, 1}});
        TetrisShapes.get(6).addXandY(new Color[]{green}, new int[][]{{1, -1}, {0, -1}, {0, 0},  {0, 1}});

        // select random shape
        CurrentShapes.add(new Shape(TetrisShapes.get(r.nextInt(TetrisShapes.size()))));
        // create (almost) blank shape to add previous shapes to
        CurrentShapes.add(new Shape("-", new int[]{0, 0}));
        CurrentShapes.get(1).addXandY(new Color[]{black}, new int[][]{{5,0}});

        //background black
        BackGroundFill = new Shape("-", new int[]{0, 0});

        this.w = w;
        this.h = h;
        this.time_interval = time_interval;

        //add observers
        CurrentShapes.get(0).addObserver(observeShapesView);
        for(int i = 0; i < TetrisShapes.size(); i++){
            TetrisShapes.get(i).addObserver(observeShapesView);
        }
        //The MouseClicked method is used to determine the actions required on a mouse event.
        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.NOBUTTON) {
                    System.out.println("No button clicked...");
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    //System.out.println("Button 1 clicked...");
                    CurrentShapes.get(0).MoveShapeLeft(CurrentShapes);
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    //System.out.println("Button 2 clicked...");
                    //CurrentShapes[0].MoveShapeDown(CurrentShapes[1]);
                    CurrentShapes.set(0, CurrentShapes.get(0).rotateLeft());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //System.out.println("Button 3 clicked...");
                    CurrentShapes.get(0).MoveShapeRight(CurrentShapes);
                }
            }
        });
    }

    // move currently active block at fixed intervals:
    public void startTimer(int time_interval, boolean playOn) {
        // creating timer task, timer
        TimerTask taskNew = new TimerTask() {
            @Override
            public void run() {
                runTimerEvent();
            }
        };
        Timer timer = new Timer();
        // scheduling the task at interval
        timer.scheduleAtFixedRate(taskNew, 100, time_interval);
    }

    public void runTimerEvent(){
        // run move down and if current shape block then move XY coordinates to old shapes holder & get new
        if(!CurrentShapes.get(0).MoveShapeDown(CurrentShapes.get(1))){
            CurrentShapes.get(1).addXandY(CurrentShapes.get(0));
            CurrentShapes.set(0, new Shape(TetrisShapes.get(r.nextInt(TetrisShapes.size()))));
            // now delete rows if filled
            // create temporary array to save get pulling values all the time
            ArrayList<int[]> tempXY = CurrentShapes.get(1).getXandY();
            ArrayList<Color> tempCol = CurrentShapes.get(1).getColor();
            boolean[] row = new boolean[10];
            for(int i = 0; i < 20; i++){
                // create blank array set to false
                for(int j = 0; j < row.length; j++){
                    row[j] = false;
                }
                // set boolean array to true if y=i
                for(int j = 0; j < tempXY.size(); j++) {
                    if(tempXY.get(j)[1] == i){
                        row[tempXY.get(j)[0]+ 4] = true;
                    }
                }
                // check if array is full with true
                boolean rowFull = true;
                for(int j = 0; j < row.length; j++) {
                    if(!row[j]){
                        rowFull = false;
                    }
                }
                // if so; delete XY values & colours
                if(rowFull){
                    for(int j = 0; j < tempXY.size(); j++) {
                        if(tempXY.get(j)[1] == i){
                            tempXY.remove(j);
                            tempCol.remove(j);
                            j--;
                        }
                    }
                    for(int j = 0; j < tempXY.size(); j++) {
                        if(tempXY.get(j)[1] < i){
                            tempXY.get(j)[1]++;
                        }
                    }
                    // set them back again
                    CurrentShapes.get(1).setColor(tempCol);
                    CurrentShapes.get(1).setXandY(tempXY);
                }
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        // create background
        g.setColor(BLACK);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                g.fill3DRect(i * size, j * size, size, size, true);
            }
        }
        // display current non-falling shapes
        ArrayList<int[]> tempXY = CurrentShapes.get(1).getXandY();
        if(CurrentShapes.get(1).getColor().size() != 1){
            Iterator<int[]> it = tempXY.iterator();
            for (int i = 0; it.hasNext(); i++) {
                g.setColor(CurrentShapes.get(1).getColor().get(i));
                int[] old = it.next();

                g.fill3DRect((4*size) + (old[0] * size), size + (old[1] * size), size, size, true);
            }
        }
        // display current falling shape
        g.setColor(CurrentShapes.get(0).getColor().get(0));
        tempXY = CurrentShapes.get(0).getXandY();
        Iterator<int[]> it = CurrentShapes.get(0).getXandY().iterator();
        for(;it.hasNext();){
            int[] c =  it.next();
            g.fill3DRect((4*size) + (size * (c[0] + CurrentShapes.get(0).getRP()[0])), (size * (c[1] + CurrentShapes.get(0).getRP()[1])), size, size, true);
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

class Shape extends Observable implements Cloneable{
    //private final colors Color;
    private String ShapeType;
    private ArrayList<Color> myCol = new ArrayList<Color>();
    private ArrayList<int[]> xAndy = new ArrayList<int[]>();
    private int[] RelativePosition;

    @Override
    public String toString() {
        // amazing toString method
        String str = "";
        Iterator<int[]> it = xAndy.iterator();
        for(;it.hasNext();){
            int[] c = it.next();
            str = str + " {" + c[0] + ", " + c[1] + "} ";
        }
        return "Shape{" +
                "ShapeType='" + ShapeType + '\'' +
                //", myCol=" + Arrays.toString(myCol) +
                ", xAndy=" + str +
                ", RP= (" + RelativePosition[0] + ", " + RelativePosition[1] + ") " +
                '}';
    }

    public Shape(String str, int[] RP) {
        // normal constructor
        // should really have add XY and colours
        this.ShapeType = str;
        this.RelativePosition = RP;
    }

    public Shape(Shape shape) {
        // copy constructor
        this.ShapeType = shape.ShapeType;
        this.myCol = shape.getColor();
        this.xAndy = shape.getXandY();
        this.RelativePosition = shape.getRP();
    }


    public ArrayList<Color> getColor() {
        return this.myCol;
    }

    public void setColor(ArrayList<Color> Col) {
        myCol = Col;
    }

    public ArrayList<int[]> getXandY() {
        return this.xAndy;
    }

    public String getType(){
        return this.ShapeType;
    }

    public int[] getRP(){
        return this.RelativePosition;
    }

    public void setXandY(ArrayList<int[]> add){
        xAndy = add;
        //System.out.println(xAndy);
    }

    public void addXandY(Color col[], int[][] add){
        // used for when adding shapes to current non-falling shapes
        for(int i = 0; i < add.length; i++) {
            xAndy.add(new int[]{add[i][0], add[i][1]});
            //System.out.println(i + " " +  add[i][0] + " " + add[i][1]);
        }
        for(int i = 0; i < col.length; i++) {
            myCol.add(col[i]);
            //System.out.println(i + " " +  add[i][0] + " " + add[i][1]);
        }
        //System.out.println(xAndy);
    }

    public void addXandY(Shape CS){
        // used for when adding shapes to current non-falling shapes
        ArrayList<int[]> cs = CS.getXandY();
        Iterator it = cs.iterator();
        for(;it.hasNext();){
            int[] c = (int[]) it.next();
            xAndy.add(new int[]{(c[0] + CS.getRP()[0]), (c[1] + CS.getRP()[1])});
        }
        for(int i = 0; i < 4; i++){
            //System.out.println(i);
            myCol.add(CS.getColor().get(0));
        }
    }

    public boolean MoveShapeDown(Shape CS) {
        boolean CanMove = true;
        // create temp arrays
        ArrayList<int[]> otherXY = CS.getXandY();
        ArrayList<int[]> newXY = new ArrayList<int[]>();

        // increment current positions to test if allowed
        Iterator<int[]> it = xAndy.iterator();
        for (;it.hasNext();) {
            int[] c = it.next();
            newXY.add(new int[]{(c[0] + this.RelativePosition[0]), (c[1] + this.RelativePosition[1] + 1)});
        }

        // test if another shape is in the way
        for (int i = 0; i < otherXY.size(); i++) {
            for (int j = 0; j < newXY.size(); j++) {
                if (otherXY.get(i)[0] == newXY.get(j)[0] && otherXY.get(i)[1] == newXY.get(j)[1]) {
                    CanMove = false;
                    break;
                }
            }
        }

        // test if past maximum depth
        it = newXY.iterator();
        for (;it.hasNext();) {
            int[] c = it.next();
            if (c[1] > 18) {
                CanMove = false;
                break;
            }
        }
        if (CanMove) {
            this.RelativePosition[1]++;
            notifyObservers();
            return CanMove;
        } else {
            return CanMove;
        }
    }

    public boolean MoveShapeLeft(ArrayList<Shape> CS) {
        boolean CanMove = true;
        // create temp arrays
        ArrayList<int[]> otherXY = CS.get(1).getXandY();
        ArrayList<int[]> newXY =  new ArrayList<int[]>();
        Iterator it = xAndy.iterator();
        // increment current positions to test if allowed
        for (;it.hasNext();) {
            //System.out.println(this.xAndy.length + ", " + i);
            int[] c = (int[]) it.next();
            newXY.add(new int[]{(c[0] + this.RelativePosition[0] - 1), (c[1] + this.RelativePosition[1])});
        }
        // test if another shape is in the way
        for (int i = 0; i < otherXY.size(); i++) {
            for (int j = 0; j < newXY.size(); j++) {
                if (otherXY.get(i)[0] == newXY.get(j)[0] && otherXY.get(i)[1] == newXY.get(j)[1]) {
                    CanMove = false;
                    break;
                }
            }
        }
        // test if past maximum width
        it = newXY.iterator();
        for (;it.hasNext();) {
            int[] c = (int[]) it.next();
            if (c[0] < -4) {
                CanMove = false;
                break;
            }
        }
        if (CanMove) {
            this.RelativePosition[0]--;
            notifyObservers();
            //System.out.println(this);
            return CanMove;
        } else {
            return CanMove;
        }
        /*if(xAndy[0][0] > -5){
            for (int i = 0; i < xAndy.length; i++) {
                xAndy[i][0]++;
            }
        }
        notifyObservers();*/
    }

    public boolean MoveShapeRight(ArrayList<Shape> CS) {
        boolean CanMove = true;
        // create temp arrays
        ArrayList<int[]> otherXY = CS.get(1).getXandY();
        ArrayList<int[]> newXY =  new ArrayList<int[]>();
        Iterator it = xAndy.iterator();
        // increment current positions to test if allowed
        for (;it.hasNext();) {
            //System.out.println(this.xAndy.length + ", " + i);
            int[] c = (int[]) it.next();
            newXY.add(new int[]{(c[0] + this.RelativePosition[0] + 1), (c[1] + this.RelativePosition[1])});
        }
        // test if another shape is in the way
        for (int i = 0; i < otherXY.size(); i++) {
            for (int j = 0; j < newXY.size(); j++) {
                if (otherXY.get(i)[0] == newXY.get(j)[0] && otherXY.get(i)[1] == newXY.get(j)[1]) {
                    CanMove = false;
                    break;
                }
            }
        }
        // test if past maximum width
        it = newXY.iterator();
        for (;it.hasNext();) {
            int[] c = (int[]) it.next();
            if (c[0] > 5) {
                CanMove = false;
                break;
            }
        }
        if (CanMove) {
            this.RelativePosition[0]++;
            notifyObservers();
            //System.out.println(this);
            return CanMove;
        } else {
            return CanMove;
        }
        /*if(xAndy[0][0] < 5){
            for(int i = 0; i < xAndy.length; i++){
                xAndy[i][1]--;
            }
        }
        notifyObservers();*/
    }

    public Shape rotateLeft(){
        // if square give up
        if (this.ShapeType == "O") {
            return this;
        }
        // returnable shape
        Shape result = new Shape(this.ShapeType, this.RelativePosition);
        Iterator it = this.xAndy.iterator();
        // swap -x & y
        for (; it.hasNext();) {
            int[] c = (int[]) it.next();
            result.xAndy.add(new int[]{c[1], (-(c[0]))});
        }
        // get colour
        for (int i = 0; i < myCol.size(); i++) {
            result.myCol.add(this.myCol.get(i));

        }
        return result;
    }

    public Shape rotateRight(){
        // if square give up
        if (this.ShapeType == "O") {
            return this;
        }
        // returnable shape
        Shape result = new Shape(this.ShapeType, this.RelativePosition);
        Iterator it = this.xAndy.iterator();
        // swap x & -y
        for (; it.hasNext();) {
            int[] c = (int[]) it.next();
            result.xAndy.add(new int[]{(-c[1]), (c[0])});
        }
        // get colour
        for (int i = 0; i < myCol.size(); i++) {
            result.myCol.add(this.myCol.get(i));

        }
        /*for (int i = 0; i < 4; ++i) {
            result.xAndy.get(i)[0] = -(xAndy.get(i)[1]);
            result.xAndy.get(i)[1] = xAndy.get(i)[0];
        }*/
        return result;
    }
}