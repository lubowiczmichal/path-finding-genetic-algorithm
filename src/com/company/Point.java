package com.company;

public class Point {
    public int X;
    public int Y;

    public Point(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public Point(Point point) {
        this.X=new Integer(point.X);
        this.Y=new Integer(point.Y);
    }

    boolean equals (Point p){
        if(this.X==p.X && this.Y==p.Y){
            return true;
        }
        else {
            return false;
        }
    }


}