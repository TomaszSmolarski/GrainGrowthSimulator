package sample;

import javafx.scene.paint.Color;

import java.util.Objects;

public class UpdateTrio {
    private int rowIndex;
    private int colIndex;
    private Color color;

    public UpdateTrio(int rowIndex, int colIndex, Color color) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.color = color;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateTrio that = (UpdateTrio) o;
        return rowIndex == that.rowIndex &&
                colIndex == that.colIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, colIndex);
    }
}
