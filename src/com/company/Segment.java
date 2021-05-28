package com.company;

public class Segment {
    Direction direction;
    int distance;

    public Segment(Direction d, int dist) {
        this.direction = d;
        this.distance = dist;
    }
    public Segment(Segment segment) {
        this.direction = segment.direction;
        this.distance = segment.distance;
    }
}
