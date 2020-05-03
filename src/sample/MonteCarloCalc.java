package sample;

import javafx.scene.paint.Color;
import sample.interfaces.NeighborhoodFunction;
import sample.interfaces.PeriodFunction;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MonteCarloCalc {

    public static void newBacteriaArrayMC(Cell[][] cells, double kt, int radius, PeriodFunction periodFunction
            , NeighborhoodFunction neighborhoodFunction) {
        Random random = new Random();
        Color newColor, oldColor;
        int newEnergy, oldEnergy;
        Set<Color> oldNeighbourColorsSet;

        List<Cell> cellList = new ArrayList<>();
        for (Cell[] cs : cells) cellList.addAll(Arrays.asList(cs));
        Collections.shuffle(cellList);

        for (Cell cell : cellList) {

            oldColor = cell.getColor();
            setOtherNeighbourNumberAndSet(cell, cells, radius, periodFunction, neighborhoodFunction);
            oldEnergy = cell.getOtherColorNeighboursNumber();
            oldNeighbourColorsSet = new HashSet<>(cell.getNeighboursColorsSet());

            int cellColorSetSize = cell.getNeighboursColorsSet().size();
            if (cellColorSetSize > 1) {
                newColor = oldNeighbourColorsSet.stream()
                        .skip(random.nextInt(cellColorSetSize)).findFirst().get();
            } else if (cellColorSetSize == 1) {
                newColor = oldNeighbourColorsSet.stream().findFirst().get();
            } else continue;

            cell.setColor(newColor);
            setOtherNeighbourNumberAndSet(cell, cells, radius, periodFunction, neighborhoodFunction);
            newEnergy = cell.getOtherColorNeighboursNumber();

            int deltaE = newEnergy - oldEnergy;

            if (deltaE <= 0) {
                cells[cell.getRow()][cell.getCol()].setColor(newColor);
                cells[cell.getRow()][cell.getCol()].setOtherColorNeighboursNumber(cell.getOtherColorNeighboursNumber());
                cells[cell.getRow()][cell.getCol()].setNeighboursColorsSet(cell.getNeighboursColorsSet());
            } else {
                double probability = Math.exp(-deltaE / kt);
                if (random.nextDouble() <= probability) {
                    cells[cell.getRow()][cell.getCol()].setColor(newColor);
                    cells[cell.getRow()][cell.getCol()].setOtherColorNeighboursNumber(cell.getOtherColorNeighboursNumber());
                    cells[cell.getRow()][cell.getCol()].setNeighboursColorsSet(cell.getNeighboursColorsSet());
                } else {
                    cell.setColor(oldColor);
                    cell.setOtherColorNeighboursNumber(oldEnergy);
                    cell.setNeighboursColorsSet(oldNeighbourColorsSet);
                }
            }

        }
    }
    public static void setOtherNeighbourNumberAndSet(Cell cell, Cell[][] cells, int radius,
                                               PeriodFunction periodFunction, NeighborhoodFunction neighborhoodFunction) {
        List<UpdateTrio> neighbors = neighborhoodFunction.neighborsCoordinates(cell.getRow(), cell.getCol(),
                cells, radius, periodFunction);
        cell.setNeighboursColorsSet(new HashSet<>());
        int i = 0;
        for (UpdateTrio neighbor : neighbors) {
            Color neighbourColor = neighbor.getColor();
            if (neighbourColor != cell.getColor() && neighbourColor != Color.WHITE) {
                cell.addNeighboursColorToSet(neighbourColor);
                i++;
            }
        }
        cell.setOtherColorNeighboursNumber(i);
    }

}
