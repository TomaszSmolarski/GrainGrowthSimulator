package sample;

import javafx.scene.paint.Color;
import sample.interfaces.NeighborhoodFunction;
import sample.interfaces.PeriodFunction;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

public class ChangeGenerationCallable implements Callable<List<UpdateTrio>> {
    private int row;
    private final Cell[][] cells;
    private PeriodFunction periodFunction;
    private NeighborhoodFunction neighborhoodFunction;
    private int radius;

    public ChangeGenerationCallable(int row, Cell[][] cells, PeriodFunction periodFunction,
                                    NeighborhoodFunction neighborhoodFunction, int radius) {
        this.row = row;
        this.cells = cells;
        this.periodFunction = periodFunction;
        this.neighborhoodFunction = neighborhoodFunction;
        this.radius = radius;
    }

    public List<UpdateTrio> compute() {

        List<UpdateTrio> bacteriaToUpdate = new ArrayList<>();

        int rowIndex = row;
        for (int colIndex = 0; colIndex < cells[rowIndex].length; colIndex++) {
            if (!cells[rowIndex][colIndex].isState()) {
                Map<Color, Integer> colorMap = new HashMap<>();
                List<UpdateTrio> neighbors = neighborhoodFunction.neighborsCoordinates(rowIndex, colIndex,
                        cells, radius, periodFunction);
                for (UpdateTrio neighbor : neighbors) {
                    colorMap.compute(neighbor.getColor(), (k, v) -> v == null ? 1 : v + 1);
                }
                colorMap.remove(Color.WHITE);
                if (!colorMap.isEmpty()) {
                    Map.Entry<Color, Integer> mostFrequentColor = Collections.max(colorMap.entrySet(), Map.Entry.comparingByValue());
                    bacteriaToUpdate.add(new UpdateTrio(rowIndex, colIndex, mostFrequentColor.getKey()));
                }
            }
        }
        return bacteriaToUpdate;
    }

    @Override
    public List<UpdateTrio> call() throws Exception {
        return compute();
    }
}
