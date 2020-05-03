package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

public class DrawingMethods {

    public static Point setRectangleSize(Canvas canvas, int widthSize, int heightSize) {
        int rectangleWidth = 0;
        int rectangleHeight = 0;
        if (widthSize >= heightSize) {
            rectangleWidth = (int) ((canvas.getWidth() / widthSize));
            if (heightSize * rectangleWidth <= canvas.getHeight()) {
                rectangleHeight = rectangleWidth;
            } else {
                rectangleHeight = (int) ((canvas.getHeight() / heightSize));
                if (heightSize * rectangleHeight > canvas.getHeight()) {
                    rectangleHeight--;
                }
            }
        } else {
            rectangleHeight = (int) ((canvas.getHeight() / heightSize));
            if (widthSize * rectangleHeight <= canvas.getWidth()) {
                rectangleWidth = rectangleHeight;
            } else {
                rectangleWidth = (int) ((canvas.getWidth() / widthSize));
                if (widthSize * rectangleWidth > canvas.getWidth()) {
                    rectangleWidth--;
                }
            }
        }
        return new Point(rectangleWidth, rectangleHeight);
    }

    public static void drawStartGrid(Canvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public static void drawCustomGrid(GraphicsContext gc, Cell[][] bacteriaArray, Point rectangleSize) {
        Arrays.stream(bacteriaArray).forEach(cells -> Arrays.stream(cells).
                forEach(cell -> {
                    gc.setFill(cell.getColor());
                    gc.fillRect(cell.getCol() * rectangleSize.getX(), cell.getRow() * rectangleSize.getY(), rectangleSize.getX(), rectangleSize.getY());
                }));
    }

    public static void drawEnergyGrid(GraphicsContext gc, Cell[][] bacteriaArray, Point rectangleSize) {
        Arrays.stream(bacteriaArray).forEach(cells -> Arrays.stream(cells).
                forEach(cell -> {
                    gc.setFill(energyColor(cell));
                    gc.fillRect(cell.getCol() * rectangleSize.getX(), cell.getRow() * rectangleSize.getY(), rectangleSize.getX(), rectangleSize.getY());
                }));
    }

    private static Color energyColor(Cell cell) {
        int energy =cell.getOtherColorNeighboursNumber();
        int rgb =255-energy*10;
        rgb = Math.max(rgb, 0);
        return Color.rgb(rgb,rgb,255);
    }

    public static void updateGrid(GraphicsContext gc, List<UpdateTrio> bacteriaToUpdateList,
                                  Point rectangleSize) {
        try {
            for (UpdateTrio b : bacteriaToUpdateList) {
                gc.setFill(b.getColor());
                gc.fillRect(b.getColIndex() * rectangleSize.getX(), b.getRowIndex() * rectangleSize.getY(), rectangleSize.getX(), rectangleSize.getY());
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }

}
