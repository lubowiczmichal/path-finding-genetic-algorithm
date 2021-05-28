package com.company;

import javax.swing.*;
import java.util.ArrayList;

public class Solution {
    ArrayList <Path> paths;
    int rozmX;
    int rozmY;

    public Solution(ArrayList<Path> paths, int rozmX, int rozmY) {
        this.paths = new ArrayList<Path>();
        for (Path p:paths) {
            this.paths.add(new Path(p));
        }
        this.rozmX=rozmX;
        this.rozmY=rozmY;
    }

    public Solution(Solution solution) {
        this.paths = new ArrayList<Path>();
        for (Path p:solution.paths) {
            this.paths.add(new Path(p));
        }
        this.rozmX = solution.rozmX;
        this.rozmY = solution.rozmY;
    }

    public Solution(int rozmX, int rozmY) {
        this.paths = new ArrayList<Path>();
        this.rozmX=rozmX;
        this.rozmY=rozmY;
    }

    public int fitness(){
        int fit = 0;
        int penaltyLenght = 2;
        int penaltySegments = 1;
        int penaltyCrosses = 100;
        int penaltyPointsOutOfBoard = 100;
        int penaltyExitsFromBoard = 250;

        fit += lenght()*penaltyLenght;
        fit += segments()*penaltySegments;
        fit += crosses()*penaltyCrosses;
        //fit += pointsOutOfBoard()*penaltyPointsOutOfBoard;
        //fit += exitsFromBoard()*penaltyExitsFromBoard;
        return fit;
    }

    public void fitnessINFO(){
        int fit = 0;
        int penaltyLenght = 2;
        int penaltySegments = 1;
        int penaltyCrosses = 100;
        int penaltyPointsOutOfBoard = 100;
        int penaltyExitsFromBoard = 250;

        fit += lenght()*penaltyLenght;
        fit += segments()*penaltySegments;
        fit += crosses()*penaltyCrosses;
        fit += pointsOutOfBoard()*penaltyPointsOutOfBoard;
        fit += exitsFromBoard()*penaltyExitsFromBoard;

        System.out.println("Przecięć " + crosses());
        System.out.println("Dlugość " + lenght());
        System.out.println("Segmentów " + segments());
        System.out.println("Pkt za granicą " + pointsOutOfBoard());
        System.out.println("Wyjść za granice " + exitsFromBoard());
        System.out.println("Dopasowanie: " + fit);
    }

    public void draw()
    {
        JFrame frame = new JFrame("My drawing");
        Draw canvas = new Draw(this);
        canvas.setSize(600,600);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        canvas.paint(canvas.getGraphics());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public int lenght(){
        int dlugsc = 0;
        for(int i=0;i<this.paths.size();i++){
            for(int j = 0; j<this.paths.get(i).segments.size(); j++){
                dlugsc+=this.paths.get(i).segments.get(j).distance;
            }
        }
        return dlugsc;
    }
    public int segments(){
        int segmentow = 0;
        for(int i=0;i<this.paths.size();i++){
            segmentow+=this.paths.get(i).segments.size();
        }
        return segmentow;
    }

    public int exitsFromBoard(){
        int wyjsc = 0;
        boolean poprzedniZaGranica = false;
        for(int i=0;i<this.paths.size();i++){
            Point now = new Point(this.paths.get(i).start);
            for(int j = 0; j<this.paths.get(i).segments.size(); j++){
                if(now.X>=0 || now.X<rozmX || now.Y>=0 || now.Y < rozmY){
                    poprzedniZaGranica = false;
                }else {
                    if(!poprzedniZaGranica){
                        wyjsc++;
                    }
                    poprzedniZaGranica=true;
                }
                if(this.paths.get(i).segments.get(j).direction ==Direction.up){
                    now.Y++;
                } else if(this.paths.get(i).segments.get(j).direction ==Direction.down){
                    now.Y--;
                } else if (this.paths.get(i).segments.get(j).direction ==Direction.left){
                    now.X--;
                } else if(this.paths.get(i).segments.get(j).direction ==Direction.right){
                    now.X++;
                }
            }
        }
        return wyjsc;
    }

    public int pointsOutOfBoard(){
        int pkt = 0;
        for(int i=0;i<this.paths.size();i++){
            Point now = new Point(this.paths.get(i).start);
            for(int j = 0; j<this.paths.get(i).segments.size(); j++){
                for(int k = 0; k<this.paths.get(i).segments.get(j).distance; k++){
                    if(now.X<0 || now.X>=rozmX || now.Y<0 || now.Y >= rozmY){
                        pkt++;
                    }
                    if(this.paths.get(i).segments.get(j).direction ==Direction.up){
                        now.Y++;
                    } else if(this.paths.get(i).segments.get(j).direction ==Direction.down){
                        now.Y--;
                    } else if (this.paths.get(i).segments.get(j).direction ==Direction.left){
                        now.X--;
                    } else if(this.paths.get(i).segments.get(j).direction ==Direction.right){
                        now.X++;
                    }
                }
            }
        }
        return pkt;
    }

    public int crosses(){
        int [][]punkty = new int[rozmX][rozmY];
        for(int i=0;i<rozmX;i++){
            for(int j=0;j<rozmY;j++){
                punkty[i][j]=0;
            }
        }
        ArrayList<Point> punktyPoza = new ArrayList<Point>();
        for(int i=0;i<this.paths.size();i++){
            Point now = new Point(this.paths.get(i).start);
            for(int j = 0; j<this.paths.get(i).segments.size(); j++){
                for(int k = 0; k<this.paths.get(i).segments.get(j).distance; k++){
                    if(now.X<0 || now.X>=rozmX || now.Y<0 || now.Y>= rozmY){
                        punktyPoza.add(now);
                    }else {
                        punkty[now.X][now.Y]++;
                    }
                    if(this.paths.get(i).segments.get(j).direction ==Direction.up){
                        now.Y++;
                    } else if(this.paths.get(i).segments.get(j).direction ==Direction.down){
                        now.Y--;
                    } else if (this.paths.get(i).segments.get(j).direction ==Direction.left){
                        now.X--;
                    } else if(this.paths.get(i).segments.get(j).direction ==Direction.right){
                        now.X++;
                    }
                }
            }
            if(now.X<0 || now.X>=rozmX || now.Y<0 || now.Y>= rozmY){
                punktyPoza.add(now);
            }else {
                punkty[now.X][now.Y]++;
            }
        }

        int sum = 0;
        for(int i=0;i<rozmX;i++){
            for(int j=0;j<rozmY;j++){
                if(punkty[i][j]>1){
                    sum+=punkty[i][j]-1;
                }
            }
        }
        return sum;
    }

    public void writeSegments(){
        for (int i=0; i<this.paths.size();i++) {
            for (int j = 0; j < this.paths.get(i).segments.size(); j++) {
                System.out.print("(" + this.paths.get(i).segments.get(j).direction + ", " + this.paths.get(i).segments.get(j).distance + ") ");
            }
            System.out.println("\n"+this.paths.get(i).segments.size());
        }
    }
}
