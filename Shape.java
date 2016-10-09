package Tetris;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

public class Shape extends Observable implements Cloneable{
    //private final colors Color;
    private String ShapeType;
    private ArrayList<Color> myCol = new ArrayList<>();
    private ArrayList<int[]> xAndy = new ArrayList<>();
    private int[] RelativePosition = new int[]{0,0};

    public Shape(String str, ArrayList<Color> col, int[][] add) {
        // primary constructor
        this.ShapeType = str;

        for(int[] item : add) {
            xAndy.add(new int[]{item[0], item[1]});
        }

        myCol.addAll(col);
    }

    public Shape(String str) {
        // empty constructor
        this.ShapeType = str;
    }

    public Shape(Shape shape) {
        // copy constructor
        this.ShapeType = shape.ShapeType;
        this.myCol = shape.getColor();
        this.xAndy = shape.getXandY();
        this.RelativePosition = shape.getRP();
    }

    public void addShapeBlock(Color col, int[][] add){
        // used for when adding shapes to current non-falling shapes
        xAndy.add(add[0]);
        myCol.add(col);
    }

    public void setXandY(ArrayList<int[]> add){
        xAndy = add;
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

    public int[] getRP(){
        return this.RelativePosition;
    }

    public boolean AttemptMoveShapeDown(Shape CurrentShapes){
        // create temp arrays
        ArrayList<int[]> newPositionXY = new ArrayList<>();

        // increment current positions to test if allowed
        for(int[] c : xAndy) {
            newPositionXY.add(new int[]{(c[0] + this.RelativePosition[0]), (c[1] + this.RelativePosition[1] + 1)});
        }

        // test if shape in the way
        boolean CanMove = checkIfShapeInWayOfMovement(newPositionXY, CurrentShapes, true);

        // test if past maximum depth
        CanMove = checkIfOutOfScreen(newPositionXY, CanMove, 1, 19, -5);

        if (CanMove) {
            this.RelativePosition[1]++;
            notifyObservers();
        }
        return CanMove;
    }

    public boolean MoveShapeLeft(Shape CurrentShapes) {
        // create temp arrays
        ArrayList<int[]> newPositionXY =  new ArrayList<>();

        // increment current positions to test if allowed
        for (int[] c : xAndy) {
            newPositionXY.add(new int[]{(c[0] + this.RelativePosition[0] - 1), (c[1] + this.RelativePosition[1])});
        }

        // test if shape in the way
        boolean CanMove = checkIfShapeInWayOfMovement(newPositionXY, CurrentShapes, true);

        // test if past maximum width
        if (CanMove){
            CanMove = checkIfOutOfScreen(newPositionXY, true, 0, 5, -4);
        }

        if (CanMove) {
            this.RelativePosition[0]--;
            notifyObservers();
            //System.out.println(this);
        }
        return CanMove;
    }

    public boolean MoveShapeRight(Shape CurrentShapes) {
        // create temp arrays
        ArrayList<int[]> newPositionXY =  new ArrayList<>();

        // increment current positions to test if allowed
        for (int[] c : xAndy) {
            newPositionXY.add(new int[]{(c[0] + this.RelativePosition[0] + 1), (c[1] + this.RelativePosition[1])});
        }

        // test if shape in the way
        boolean CanMove = checkIfShapeInWayOfMovement(newPositionXY, CurrentShapes, true);

        // test if past maximum width
        if (CanMove){
            CanMove = checkIfOutOfScreen(newPositionXY, true, 0, 5, -4);
        }

        if (CanMove) {
            this.RelativePosition[0]++;
            notifyObservers();
            //System.out.println(this);
        }
        return CanMove;
    }

    public Shape rotateLeft(Shape CurrentShapes){
        // if square give up
        if (this.ShapeType.equals("O")){
            return this;
        }

        // create temp arrays
        ArrayList<int[]> newPositionXY =  new ArrayList<>();
        ArrayList<int[]> newRelativePosition = new ArrayList<>();
        // swap -x & y
        for (int[] c : xAndy) {
            newPositionXY.add(new int[]{c[1], (-(c[0]))});
        }

        for (int[] c : newPositionXY) {
            newRelativePosition.add(new int[]{c[0] + RelativePosition[0], (c[1]) + RelativePosition[1]});
        }

        // test if something in the way
        boolean CanMove = checkIfShapeInWayOfMovement(newPositionXY, CurrentShapes, true);

        // test if past maximum depth
        if(CanMove) {
            CanMove = checkIfOutOfScreen(newRelativePosition, true, 1, 19, 0);
        }

        if(CanMove){
            // test if past maximum width
            CanMove = checkIfOutOfScreen(newRelativePosition, true, 0, 5, -4);
        }

        if (CanMove) {
            notifyObservers();
            this.xAndy = newPositionXY;
        }
        return this;
    }

    public boolean checkIfShapeInWayOfMovement(ArrayList<int[]> newPositionXY, Shape CurrentShapes, boolean CanMove){
        for(int[] shape : CurrentShapes.getXandY()) {
            for(int piece[] : newPositionXY){
                if(shape[0] == piece[0] && shape[1] == piece[1]) {
                    CanMove = false;
                    break;
                }
            }
        }
        return CanMove;
    }

    public boolean checkIfOutOfScreen(ArrayList<int[]> newPositionXY, boolean CanMove, int ArrayLocation, int MaxValue, int MinValue) {
        for (int[] c : newPositionXY) {
            if (c[ArrayLocation] > MaxValue || c[ArrayLocation] < MinValue) {
                CanMove = false;
                break;
            }
        }
        return CanMove;
    }

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
}
