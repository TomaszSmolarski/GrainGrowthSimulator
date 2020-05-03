package sample.interfaces;

import sample.Cell;
import sample.UpdateTrio;

import java.awt.*;
import java.util.List;


public interface PeriodFunction {
    UpdateTrio cellColorByPeriodType(Cell[][] bacteriaArray, int row, int col);
}
