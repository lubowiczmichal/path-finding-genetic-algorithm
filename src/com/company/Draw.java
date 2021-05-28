package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Draw extends Canvas {

    public Solution solution;
    int x;
    int y;
    int width;
    int height;
    int xDelta;
    int yDelta;
    ArrayList<Point> listaPunktow;

    public Draw(Solution solution)
    {
        this.solution = solution;
        x = y = 50;
        width = height = 500;
        xDelta = width/solution.rozmX;
        yDelta = height/solution.rozmY;
        listaPunktow = new ArrayList<Point>();
        for(int i=0;i<solution.rozmX;i++)
            for(int j=0;j<solution.rozmY;j++)
                listaPunktow.add(new Point(x+i*xDelta, y+j*yDelta));
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for(Point p: listaPunktow)
            g2.fillOval(p.X, p.Y, 5, 5);

        for(Path path: solution.paths)
        {
            Random random = new Random();
            int r = random.nextInt(255);
            int gC = random.nextInt(255);
            int b = random.nextInt(255);
            ArrayList<Point> points = new ArrayList<Point>();
            points.add(path.start);
            Point now = new Point(path.start.X, path.start.Y);
            for (int i = 0; i < path.segments.size(); i++) {
                if (path.segments.get(i).direction == Direction.up) {
                    now.Y += path.segments.get(i).distance;
                } else if (path.segments.get(i).direction == Direction.down) {
                    now.Y -= path.segments.get(i).distance;
                } else if (path.segments.get(i).direction == Direction.left) {
                    now.X -= path.segments.get(i).distance;
                } else if (path.segments.get(i).direction == Direction.right) {
                    now.X += path.segments.get(i).distance;
                }
                points.add(new Point(now.X, now.Y));
            }
            Color c = new Color(r,gC,b);
            g2.setColor(c);
            g2.setStroke(new BasicStroke(3));
            g2.fillOval(x + path.start.X*xDelta, y + (this.solution.rozmY-1)*yDelta - path.start.Y*yDelta,10,10);
            g2.fillOval(x + path.end.X*xDelta, y + (this.solution.rozmY-1)*yDelta - path.end.Y*yDelta,10,10);
            for(int i=0;i<points.size()-1;i++)
            {
                Point p1 = points.get(i);
                Point p2 = points.get(i+1);
                g2.drawLine(x + p1.X*xDelta, y + (this.solution.rozmY-1)*yDelta - p1.Y*yDelta, x + p2.X*xDelta, y + (this.solution.rozmY-1) * yDelta  - p2.Y*yDelta);
            }
        }
    }
}
