package sample;

import javafx.scene.paint.Color;
import sample.interfaces.NeighborhoodFunction;
import sample.interfaces.PeriodFunction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class InterfacesImplementation {

    public static PeriodFunction checkPeriod(boolean periodBC) {
        if (periodBC) {
            return (bacteriaArray, row, col) -> {
                int checkRow = row;
                int checkCol = col;
                checkRow = checkRow < 0 ? bacteriaArray.length + checkRow : checkRow % bacteriaArray.length;
                checkCol = checkCol < 0 ? bacteriaArray[0].length + checkCol : checkCol % bacteriaArray[0].length;
                return new UpdateTrio(checkRow, checkCol, bacteriaArray[checkRow][checkCol].getColor());
            };
        } else {
            return (bacteriaArray, row, col) -> {
                if (row < 0 || row >= bacteriaArray.length || col < 0 || col >= bacteriaArray[0].length) {
                    return new UpdateTrio(-1, -1, Color.WHITE);
                }
                return new UpdateTrio(row, col, bacteriaArray[row][col].getColor());
            };
        }
    }

    public static List<UpdateTrio> vonNeumannNeighbourhood(int row, int col,
                                                           Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> mooreNeighbourhood(int row, int col,
                                                      Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col + 1));
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> pentagonalNeighbourhood(int row, int col,
                                                           Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();

        Random generator = new Random();
        int type = generator.nextInt(4);
        switch (type) {
            case 0: {
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
                break;
            }
            case 1: {
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col + 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col + 1));
                break;
            }
            case 2: {
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col + 1));
                break;
            }
            case 3: {
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col + 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
                neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
                break;
            }
        }
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> heksagonalNeighbourhood(int row, int col,
                                                           Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();
        Random generator = new Random();
        int type = generator.nextInt(2);
        if (type == 0) {
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col + 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col - 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
        } else {
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col - 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
            neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col + 1));
        }
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> heksagonalLeftNeighbourhood(int row, int col,
                                                               Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> heksagonalRightNeighbourhood(int row, int col,
                                                                Cell[][] cells, PeriodFunction periodFunction) {
        List<UpdateTrio> neighborsList = new ArrayList<>();
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row - 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col - 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row, col + 1));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col));
        neighborsList.add(periodFunction.cellColorByPeriodType(cells, row + 1, col + 1));
        return neighborsList.stream().
                filter(updateTrio -> updateTrio.getRowIndex() >= 0 && updateTrio.getColIndex() >= 0)
                .collect(Collectors.toList());
    }

    public static List<UpdateTrio> withRadiusNeighbourhood(int row, int col, Cell[][] cells, int radius
            , PeriodFunction periodFunction) {
        List<UpdateTrio> neighbors = new ArrayList<>();
        radius = radius + 1;
        Cell thisCell = cells[row][col];
        double thisCellX = (col + thisCell.getCenterX());
        double thisCellY = (row + thisCell.getCenterY());
        for (int checkCol = col - radius; checkCol <= col + radius; checkCol++) {
            for (int checkRow = row - radius; checkRow <= row + radius; checkRow++) {
                UpdateTrio potentialNeighbour = periodFunction.cellColorByPeriodType(cells, checkRow, checkCol);
                if (potentialNeighbour.getRowIndex() >= 0 && potentialNeighbour.getColIndex() >= 0) {
                    Cell otherCell = cells[potentialNeighbour.getRowIndex()][potentialNeighbour.getColIndex()];
                    double otherCellX = (checkCol + otherCell.getCenterX());
                    double otherCellY = (checkRow + otherCell.getCenterY());
                    if (Math.sqrt((otherCellX - thisCellX) * (otherCellX - thisCellX) +
                            (otherCellY - thisCellY) * (otherCellY - thisCellY)) <= (radius - 1)) {
                        neighbors.add(potentialNeighbour);
                    }
                }
            }
        }
        return neighbors;
    }


    public static NeighborhoodFunction initialNeighbourhoodTypeSwitch(String initialNeighborhoodType) {
        switch (initialNeighborhoodType) {
            case "Von Neumann": {
                return (row, col, cells, radius, periodFunction) -> vonNeumannNeighbourhood(row, col,
                        cells, periodFunction);
            }
            case "Moore": {
                return (row, col, cells, radius, periodFunction) -> mooreNeighbourhood(row, col,
                        cells, periodFunction);
            }
            case "Pentagonal": {
                return (row, col, cells, radius, periodFunction) -> pentagonalNeighbourhood(row, col,
                        cells, periodFunction);

            }
            case "Hexagonal": {
                return (row, col, cells, radius, periodFunction) -> heksagonalNeighbourhood(row, col,
                        cells, periodFunction);

            }
            case "Hexagonal Left": {
                return (row, col, cells, radius, periodFunction) -> heksagonalLeftNeighbourhood(row, col,
                        cells, periodFunction);
            }
            case "Hexagonal Right": {
                return (row, col, cells, radius, periodFunction) -> heksagonalRightNeighbourhood(row, col,
                        cells, periodFunction);
            }
            case "With Radius": {
                return InterfacesImplementation::withRadiusNeighbourhood;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + initialNeighborhoodType);
        }

    }
}
