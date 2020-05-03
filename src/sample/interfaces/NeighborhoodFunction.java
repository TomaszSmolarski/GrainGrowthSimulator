package sample.interfaces;


import sample.Cell;
import sample.UpdateTrio;

import java.awt.*;
import java.util.List;

public interface NeighborhoodFunction {
    List<UpdateTrio> neighborsCoordinates(int row, int col, Cell[][] cells, int radius, PeriodFunction periodFunction);
}
