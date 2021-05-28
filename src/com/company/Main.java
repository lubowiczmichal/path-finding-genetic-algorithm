package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static int N=1000;
    private static int generations=1000;
    private static int tournamentSize=10;
    private static double probCross=0.75;
    private static double probMut=0.01;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ArrayList<Solution> population=generatePopulation(N);
        solve(population);
    }

    public static void solve(ArrayList<Solution> p) throws FileNotFoundException, InterruptedException {
        ArrayList<Solution> population = p;
        ArrayList<Integer> bestScores = new ArrayList<Integer>();
        ArrayList<Integer> avgScores = new ArrayList<Integer>();
        ArrayList<Integer> worstScores = new ArrayList<Integer>();
        Random random = new Random();
        ArrayList<Solution> parents;
        Solution parent1;
        Solution parent2;
        Solution child1;
        Solution child2;
        Solution bestSolution = null;
        int bestFitness = population.get(0).fitness();
        ArrayList<Solution> nextPopulation;
        for(int generacja = 0; generacja<generations;generacja++){
            nextPopulation = new ArrayList<Solution>();
            while(nextPopulation.size()!=N){
                parents = new ArrayList<>(tournamentSelection(population,tournamentSize));
                parent1 = new Solution(parents.get(0));
                parent2 = new Solution(parents.get(1));
                if(random.nextDouble()<probCross){
                    child1 = new Solution(crossing(parent1,parent2));
                    child2 = new Solution(crossing(parent1,parent2));
                }else{
                    child1 = new Solution(parent1);
                    child2 = new Solution(parent2);
                }
                if(random.nextDouble()<probMut) {
                    nextPopulation.add(mutation(child1));
                }else {
                    nextPopulation.add(child1);
                }
                if(random.nextDouble()<probMut) {
                    nextPopulation.add(mutation(child2));
                }else {
                    nextPopulation.add(child2);
                }
            }
            int tempBest = population.get(0).fitness();
            int sum = 0;
            int worst = 0;
            ArrayList<Integer> fits = new ArrayList<Integer>();
            for(int i=0;i<population.size();i++){
                fits.add(population.get(i).fitness());
            }

            for(int i=0;i<fits.size();i++){
                sum += fits.get(i);
                if(fits.get(i)<bestFitness){
                    bestFitness = fits.get(i);
                    bestSolution = new Solution(population.get(i));
                }
                if(fits.get(i)<bestFitness){
                    tempBest = fits.get(i);
                }
                if(fits.get(i)>worst){
                    worst=fits.get(i);
                }
            }
            bestScores.add(tempBest);
            avgScores.add(sum/population.size());
            worstScores.add(worst);
            System.out.println("Generacja "+ generacja + " best " + tempBest);
            population = new ArrayList<Solution>();
            for(Solution s: nextPopulation)
                population.add(s);
        }
         bestSolution.fitnessINFO();
       bestSolution.draw();
        System.out.println(bestFitness);
        GraphPanel.createAndShowGui(bestScores);
        saveToFile(bestScores,avgScores,worstScores);


    }

    public static void saveToFile(ArrayList<Integer> best, ArrayList<Integer> avr, ArrayList<Integer> worst) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("wynik.txt"));
        for (int i = 1; i<best.size()+1;i++)
            pw.print(i+",");
        pw.println();
        for (int b : best)
            pw.print(b+",");
        pw.println();
        for (int a : avr)
            pw.print(a+",");
        pw.println();
        for (int w : worst)
            pw.print(w+",");
        pw.close();
    }

    public static ArrayList<Solution> generatePopulation(int rozmiar){
        ArrayList<ArrayList<Point>> points = readFile("zad1.txt");
        int rozmX = points.get(0).get(0).X;
        int rozmY = points.get(0).get(0).Y;
        points.remove(0);
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        for(int i=0;i<rozmiar;i++){
            solutions.add(generateRandomPaths(points, rozmX, rozmY));
        }
        return solutions;
    }

    public static Solution mutation(Solution solution){
        Solution mutation = new Solution(solution);
        Random random = new Random();
        int pathNum = random.nextInt(mutation.paths.size());
        Path path = repair(new Path(mutation.paths.get(pathNum)));
        int segmentNum = random.nextInt(path.segments.size());
        Segment segment = new Segment(path.segments.get(segmentNum));
        Direction direction;
        int distance;
        Point now = new Point(path.start.X, path.start.Y);
        for(int i = 0 ;i<segmentNum;i++){
            if (path.segments.get(i).direction == Direction.up) {
                now.Y += path.segments.get(i).distance;
            } else if (path.segments.get(i).direction == Direction.down) {
                now.Y -= path.segments.get(i).distance;
            } else if (path.segments.get(i).direction == Direction.left) {
                now.X -= path.segments.get(i).distance;
            } else if (path.segments.get(i).direction == Direction.right) {
                now.X += path.segments.get(i).distance;
            }
        }
        Segment segmentA = null;
        Segment segmentB = null;
        boolean find = false;
        while (!find) {
            if (segment.direction == Direction.up || segment.direction == Direction.down) {
                if (random.nextInt(2) == 0 && mutation.rozmX - now.X - 1 > 0) {
                    direction = Direction.right;
                    distance = random.nextInt(mutation.rozmX - now.X - 1) + 1;
                    segmentA = new Segment(direction, distance);
                    segmentB = new Segment(Direction.left, distance);
                    find=true;
                } else if (now.X - 1 > 0) {
                    direction = Direction.left;
                    distance = random.nextInt(now.X) + 1;
                    segmentA = new Segment(direction, distance);
                    segmentB = new Segment(Direction.right, distance);
                    find=true;
                }
            } else {
                if (random.nextInt(2) == 0 && now.Y > 0) {
                    direction = Direction.down;
                    distance = random.nextInt(now.Y) + 1;
                    segmentA = new Segment(direction, distance);
                    segmentB = new Segment(Direction.up, distance);
                    find=true;
                } else if (mutation.rozmY - now.Y - 1 > 0) {
                    direction = Direction.up;
                    distance = random.nextInt(mutation.rozmY - now.Y - 1) + 1;
                    segmentA = new Segment(direction, distance);
                    segmentB = new Segment(Direction.down, distance);
                    find=true;
                }
            }
        }
        Path newPath = new Path(path);


        int x =  random.nextInt(segment.distance);
        int y =  random.nextInt(segment.distance);
        int split = Math.min(x,y);
        int split2 = Math.max(x,y);
        newPath.segments.remove(segmentNum);
        if(random.nextDouble()<1.0/3.0) {
            newPath.segments.add(segmentNum + 0, new Segment(segment.direction, split));
            newPath.segments.add(segmentNum + 1, segmentA);
            newPath.segments.add(segmentNum + 2, new Segment(segment.direction, split2 - split));
            newPath.segments.add(segmentNum + 3, segmentB);
            newPath.segments.add(segmentNum + 4, new Segment(segment.direction, segment.distance - split2));
        }else if(random.nextDouble()<(2.0/3.0))
            if(random.nextBoolean()) {
            newPath.segments.add(segmentNum + 0, new Segment(segment.direction, x));
            newPath.segments.add(segmentNum + 1, segmentA);
            newPath.segments.add(segmentNum + 2, new Segment(segment.direction, segment.distance - x));
            newPath.segments.add(segmentNum + 3, segmentB);
        }else{
            newPath.segments.add(segmentNum + 0, segmentA);
            newPath.segments.add(segmentNum + 1, new Segment(segment.direction, x));
            newPath.segments.add(segmentNum + 2, segmentB);
            newPath.segments.add(segmentNum + 3, new Segment(segment.direction, segment.distance - x));
        }else {
            newPath.segments.add(segmentNum + 0, segmentA);
            newPath.segments.add(segmentNum + 1, new Segment(segment));
            newPath.segments.add(segmentNum + 2, segmentB);
        }
        mutation.paths.remove(pathNum);
        newPath = new Path(repair(newPath));
        mutation.paths.add(pathNum,newPath);
        return mutation;
    }

    public static Path repair(Path path){
        for ( int i = 0; i<path.segments.size();i++) {
            if (path.segments.get(i).distance < 1) {
                path.segments.remove(i);
            }
        }
        for ( int i = 0; i<path.segments.size()-1;i++) {
            Segment s = null;

            if (path.segments.get(i).direction == Direction.up && path.segments.get(i + 1).direction == Direction.up) {
                s = new Segment(Direction.up, path.segments.get(i).distance + path.segments.get(i + 1).distance);
                path.segments.remove(i);
                path.segments.remove(i);
                path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.up && path.segments.get(i + 1).direction == Direction.down) {
                if (path.segments.get(i).distance > path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.up, path.segments.get(i).distance - path.segments.get(i + 1).distance);
                } else if (path.segments.get(i).distance < path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.down, path.segments.get(i + 1).distance - path.segments.get(i).distance);
                }
                path.segments.remove(i);
                path.segments.remove(i);
                if(s!=null)
                    path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.down && path.segments.get(i + 1).direction == Direction.down) {
                s = new Segment(Direction.down, path.segments.get(i).distance + path.segments.get(i + 1).distance);
                path.segments.remove(i);
                path.segments.remove(i);
                path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.down && path.segments.get(i + 1).direction == Direction.up) {
                if (path.segments.get(i).distance > path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.down, path.segments.get(i).distance - path.segments.get(i + 1).distance);
                } else if (path.segments.get(i).distance < path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.up, path.segments.get(i + 1).distance - path.segments.get(i).distance);
                }
                path.segments.remove(i);
                path.segments.remove(i);
                if(s!=null)
                    path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.left && path.segments.get(i + 1).direction == Direction.left) {
                s = new Segment(Direction.left, path.segments.get(i).distance + path.segments.get(i + 1).distance);
                path.segments.remove(i);
                path.segments.remove(i);
                path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.left && path.segments.get(i + 1).direction == Direction.right) {
                if (path.segments.get(i).distance > path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.left, path.segments.get(i).distance - path.segments.get(i + 1).distance);
                } else if (path.segments.get(i).distance < path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.right, path.segments.get(i + 1).distance - path.segments.get(i).distance);
                }
                path.segments.remove(i);
                path.segments.remove(i);
                if(s!=null)
                    path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.right && path.segments.get(i + 1).direction == Direction.right) {
                s = new Segment(Direction.right, path.segments.get(i).distance + path.segments.get(i + 1).distance);
                path.segments.remove(i);
                path.segments.remove(i);
                path.segments.add(i,s);
            } else if (path.segments.get(i).direction == Direction.right && path.segments.get(i + 1).direction == Direction.left) {
                if (path.segments.get(i).distance > path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.right, path.segments.get(i).distance - path.segments.get(i + 1).distance);
                } else if (path.segments.get(i).distance < path.segments.get(i + 1).distance) {
                    s = new Segment(Direction.left, path.segments.get(i + 1).distance - path.segments.get(i).distance);
                }
                path.segments.remove(i);
                path.segments.remove(i);
                if(s!=null)
                    path.segments.add(i,s);
            } else {
                i++;
            }
            i--;
        }
        return path;
    }

    public static ArrayList<Solution> rouletteSelection(ArrayList<Solution>solutions) {
        ArrayList<Integer> chances = new ArrayList<Integer>();
        ArrayList<Integer> fits = new ArrayList<Integer>();
        long sum = 0;
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int i=0 ;i<solutions.size();i++) {
            int temp = solutions.get(i).fitness();
            fits.add(temp);
            if(temp>max)
                max=temp;
            if(temp<min)
                min=temp;
        }
        for(int i=0;i<solutions.size();i++){
            chances.add(min+max-fits.get(i));
            sum += chances.get(i);
        }

        Random random = new Random();
        long r = (long) (random.nextDouble()*sum);
        long temp = 0;
        ArrayList<Solution> winners = new ArrayList<Solution>();
        for(int i =0 ;i<solutions.size();i++){
            temp+=chances.get(i);
            if(temp>=r){
                winners.add(new Solution(solutions.get(i)));
                break;
            }
        }
        do{
            temp = 0;
            r = (long) (random.nextDouble()*sum);
            for(int i=0 ;i<solutions.size();i++){
                temp+=chances.get(i);
                if(temp>=r){
                    winners.add(new Solution(solutions.get(i)));
                    break;
                }
            }
        }while (winners.get(0)==winners.get(1));
        return winners;
    }


    public static ArrayList<Solution> tournamentSelection(ArrayList<Solution>solutions, int tournamentSize){
        ArrayList<Integer> nums = new ArrayList<Integer>();
        Random random = new Random();
        int id = 0;
        for(int i=0 ;i<tournamentSize;i++) {
            do {
                id = random.nextInt(solutions.size());
            } while (nums.contains(id));
            nums.add(id);
        }
        ArrayList<Solution> tournamentSquad = new ArrayList<Solution>();
        for(int i: nums){
            tournamentSquad.add(new Solution(solutions.get(i)));
        }
        double best = tournamentSquad.get(0).fitness();
        double secondBest = tournamentSquad.get(0).fitness()+1;
        id = 0;
        int secondid = 0;
        for(int i = 0 ;i<tournamentSquad.size();i++){
            double temp = tournamentSquad.get(i).fitness();
            if(best>temp){
                secondBest = best;
                secondid = id;
                best = temp;
                id = i;
            }else if(secondBest>temp){
                secondBest = temp;
                secondid = i;
            }
        }
        ArrayList<Solution> winners = new ArrayList<Solution>();
        winners.add(new Solution(tournamentSquad.get(id)));
        winners.add(new Solution(tournamentSquad.get(secondid)));
        return winners;
    }

    public static Solution crossing(Solution s1, Solution s2){
        Random random = new Random();
        Solution x = new Solution(s1);
        Solution y = new Solution(s2);
        int rand = random.nextInt(x.paths.size());
        for(int i=0;i<x.paths.size();i++){
            if(i>=rand){
                x.paths.set(i,y.paths.get(i));
            }
            repair(x.paths.get(i));
        }

        return x;
    }

    public static Solution generateRandomPaths(ArrayList<ArrayList<Point>> points, int rozmX, int rozmY){
        ArrayList<Path> paths = new ArrayList<Path>();
        for(int i = 0;i<points.size();i++){
            paths.add(connect2PointsRandom(points.get(i).get(0),points.get(i).get(1),rozmX,rozmY));
        }
        return new Solution(paths,rozmX,rozmY);
    }


    public static Path connect2PointsRandom(Point start, Point meta, int rozmX, int rozmY){
        Point now = new Point(start);
        Random random = new Random();
        Direction direction;
        Direction prev = null;
        Direction minusPrev = null;
        int distance;
        ArrayList<Segment> hist = new ArrayList<Segment>();
        while(true){
            do {
                direction = Direction.random();
            }while (direction==prev||direction==minusPrev);
            prev = direction;
            if(direction==Direction.up &&rozmY-now.Y-1>0) {
                distance = random.nextInt(rozmY-now.Y-1)+1;
                now.Y += distance;
                hist.add(new Segment(direction, distance));
                minusPrev = Direction.down;
            }else if(direction==Direction.down &&now.Y-1>0) {
                distance = random.nextInt(now.Y)+1;
                now.Y-=distance;
                hist.add(new Segment(direction, distance));
                minusPrev = Direction.up;
            }else if(direction==Direction.left &&now.X>0) {
                distance = random.nextInt(now.X)+1;
                now.X -= distance;
                hist.add(new Segment(direction, distance));
                minusPrev = Direction.right;
            }else if(direction==Direction.right &&rozmX-now.X-1>0) {
                distance = random.nextInt(rozmX-now.X-1)+1;
                now.X += distance;
                hist.add(new Segment(direction, distance));
                minusPrev = Direction.left;
            }
            if (now.equals(meta)){
                return new Path(start,meta,hist);
            }
        }
    }
   public static ArrayList<ArrayList<Point>> readFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            ArrayList<ArrayList<Point>> points = new ArrayList<ArrayList<Point>>();
            String sCurrentLine;
            String [] split;
            while ((sCurrentLine = br.readLine()) != null) {
                split = sCurrentLine.split(";");
                ArrayList<Point> linePoints = new ArrayList<Point>();
                for(int i=0; i<split.length;i+=2){
                    linePoints.add(new Point(Integer.parseInt(split[i]),Integer.parseInt(split[i+1])));
                }
                points.add(linePoints);
            }
            return points;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
