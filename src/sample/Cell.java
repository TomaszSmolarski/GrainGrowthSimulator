package sample;

import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Cell {
    private final int Id;
    private boolean state;
    private Color color;

    private final double centerX;
    private final double centerY;

    private int otherColorNeighboursNumber;
    private Set<Color> neighboursColorsSet;

    private final int row;
    private final int col;

    Cell(int id, Color color, int row, int col) {
        Random random = new Random();
        Id = id;
        this.state = false;
        this.color = color;
        this.centerX = random.nextDouble();
        this.centerY = random.nextDouble();
        this.otherColorNeighboursNumber = 0;
        this.row = row;
        this.col = col;
    }

    public void addNeighboursColorToSet(Color color){
        neighboursColorsSet.add(color);
    }

    public Set<Color> getNeighboursColorsSet() {
        return neighboursColorsSet;
    }

    public void setNeighboursColorsSet(Set<Color> neighboursColorsSet) {
        this.neighboursColorsSet = neighboursColorsSet;
    }

    public int getId() {
        return Id;
    }

    public boolean isState() {
        return state;
    }

    public Color getColor() {
        return color;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public int getOtherColorNeighboursNumber() {
        return otherColorNeighboursNumber;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setOtherColorNeighboursNumber(int otherColorNeighbours) {
        this.otherColorNeighboursNumber = otherColorNeighbours;
    }

}
