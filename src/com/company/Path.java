package com.company;
import java.util.ArrayList;

public class Path {
    Point start;
    Point end;
    ArrayList<Segment> segments;

    public Path(Point start, Point end, ArrayList<Segment> segments) {
        this.start = start;
        this.end = end;
        this.segments = new ArrayList<Segment>();
        for(Segment s:segments){
            this.segments.add(new Segment(s));
        }
    }
    public Path(Point start, Point end) {
        this.start = start;
        this.end = end;
        this.segments = new ArrayList<Segment>();
    }
    public Path(Path path) {
        this.start = path.start;
        this.end = path.end;
        this.segments = new ArrayList<Segment>();
        for(Segment s:path.segments){
            this.segments.add(new Segment(s));
        }
    }
}
