package sample;

import javafx.scene.paint.Color;
import sample.interfaces.PeriodFunction;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class InitialStructTypes {


    public static void manualNucleation(Cell[][] cells, Set<Color> colorSet,
                                        int row, int col) {
        Color newColor;
        do {
            newColor = Color.color(Math.random(), Math.random(), Math.random());
            colorSet.remove(cells[row][col].getColor());
        }
        while (!colorSet.add(newColor));
        cells[row][col].setColor(newColor);
    }

    public static void randomNucleation(Cell[][] cells, Set<Color> colorSet,
                                        int budNumber) {
        Random generator = new Random();
        int row, col;
        for (int i = 0; i < budNumber; i++) {
            do {
                row = generator.nextInt(cells.length);
                col = generator.nextInt(cells[0].length);
            } while (tryChangeBacteriaArray(row, col, cells, colorSet));
        }
    }

    public static void homogeneouslyNucleation(Cell[][] cells, Set<Color> colorSet,
                                               int inColNumber, int inRowNumber) {
        int rowDistance = (int) cells.length / (inColNumber);
        int colDistance = (int) cells[0].length / (inRowNumber);
        int rowIndex = rowDistance / 2, colIndex;
        for (int checkCol = 0; checkCol < inColNumber; checkCol++) {
            colIndex = colDistance / 2;
            for (int checkRow = 0; checkRow < inRowNumber; checkRow++) {
                tryChangeBacteriaArray(rowIndex, colIndex, cells, colorSet);
                colIndex += colDistance;
            }
            rowIndex += rowDistance;
        }
    }


    public static void radiusNucleation(Cell[][] cells, Set<Color> colorSet,
                                        int budNumber, int radius, PeriodFunction periodFunction) {
        Random generator = new Random();
        int row, col;

        for (int i = 0; i < budNumber; i++) {
            int iterator = 0;
            do {
                do {
                    row = generator.nextInt(cells.length);
                    col = generator.nextInt(cells[0].length);
                    iterator++;
                    if (iterator == 1000) break;
                } while (checkRadius(row, col, radius, cells, periodFunction));
                if (iterator == 1000) break;
            } while (tryChangeBacteriaArray(row, col, cells, colorSet));
        }
    }


    private static boolean tryChangeBacteriaArray(int row, int col, Cell[][] cells, Set<Color> colorSet) {
        Color newColor;
        do {
            newColor = Color.color(Math.random(), Math.random(), Math.random());
        }
        while (!colorSet.add(newColor));
        if (cells[row][col].getColor() == Color.WHITE) {
            cells[row][col].setColor(newColor);
            cells[row][col].setState(true);
            return false;
        }
        return true;
    }


    private static boolean checkRadius(int row, int col, int radius, Cell[][] cells, PeriodFunction periodFunction) {

        for (int checkRow = row - radius; checkRow <= row + radius; checkRow++) {
            for (int checkCol = col - radius; checkCol <= col + radius; checkCol++) {
                if (Math.sqrt(((row - checkRow) * (row - checkRow) + (col - checkCol) * (col - checkCol))) <= radius + 0.5) {
                    if (periodFunction.cellColorByPeriodType(cells, checkRow, checkCol).getColor() != Color.WHITE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
