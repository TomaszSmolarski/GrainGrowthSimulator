package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.interfaces.NeighborhoodFunction;
import sample.interfaces.PeriodFunction;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class Controller {
    private static final int NTHREADS = 8;

    private Cell[][] cells;
    private List<UpdateTrio> bacteriaToUpdateArray;

    private Point rectangleSize;
    private Set<Color> colorSet;
    private PeriodFunction periodFunction;
    private NeighborhoodFunction neighborhoodFunction;
    private Timer delayBetweenUpdates;
    private Timer energyMCSimulation;
    private GraphicsContext gc;
    private GraphicsContext gcEnergy;
    private GraphicsContext gcMC;
    @FXML
    Canvas CAcanvas;
    @FXML
    Canvas MCcanvas;
    @FXML
    Canvas energyCanvas;

    @FXML
    Spinner<Integer> delayBetweenIterations;
    @FXML
    Spinner<Integer> widthOfGrid;
    @FXML
    Spinner<Integer> heightOfGrid;
    @FXML
    Spinner<Integer> budNumber;
    @FXML
    Spinner<Integer> inColNumber;
    @FXML
    Spinner<Integer> inRowNumber;
    @FXML
    Spinner<Integer> radiusSpinner;
    @FXML
    Spinner<Integer> MCIterationsSpinner;
    @FXML
    Spinner<Double> KtSpinner;

    @FXML
    ComboBox<String> initialStruct;
    @FXML
    ComboBox<String> initialNeighborhoodType;
    @FXML

    CheckBox periodBC;

    @FXML

    public void initialize() {

        delayBetweenIterations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 1000));
        widthOfGrid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 600));
        heightOfGrid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 600));
        MCIterationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
        KtSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 6, 1, 0.1));
        initialStruct.getItems().addAll("Random", "Manual", "Homogeneously", "Radius");
        initialStruct.setValue("Manual");
        initialNeighborhoodType.getItems().addAll("Von Neumann", "Moore", "Pentagonal", "Hexagonal",
                "Hexagonal Left", "Hexagonal Right", "With Radius");
        initialNeighborhoodType.setValue("Moore");
        initValues();

        delayBetweenUpdates = new Timer(delayBetweenIterations.getValue(), new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent actionEvent) {
                if (delayBetweenIterations.getValue() != delayBetweenUpdates.getDelay()) {
                    delayBetweenUpdates.setDelay(delayBetweenIterations.getValue());
                }
                changeGeneration();
                Platform.runLater(() -> {
                    if (bacteriaToUpdateArray.isEmpty()) {
                        delayBetweenUpdates.stop();

                        Arrays.stream(cells).forEach(cellsRow -> Arrays.stream(cellsRow).
                                forEach(cell -> MonteCarloCalc.setOtherNeighbourNumberAndSet(cell,
                                        cells, radiusSpinner.getValue(), periodFunction, neighborhoodFunction)));

                        DrawingMethods.drawEnergyGrid(gcEnergy, cells, rectangleSize);
                        DrawingMethods.drawCustomGrid(gc, cells, rectangleSize);
                        DrawingMethods.drawCustomGrid(gcMC, cells, rectangleSize);
                    } else {
                        try {
                            for (UpdateTrio b : bacteriaToUpdateArray) {
                                cells[b.getRowIndex()][b.getColIndex()].setColor(b.getColor());
                                cells[b.getRowIndex()][b.getColIndex()].setState(true);
                            }
                            DrawingMethods.updateGrid(gc, bacteriaToUpdateArray, rectangleSize);
                        } catch (ConcurrentModificationException | NoSuchElementException ignored) {
                        }
                    }

                });
            }
        });
        delayBetweenUpdates.setRepeats(true);

        energyMCSimulation = new Timer(delayBetweenIterations.getValue(), new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent actionEvent) {
                if (MCIterationsSpinner.getValue() == 0) energyMCSimulation.stop();
                MonteCarloCalc.newBacteriaArrayMC(cells, KtSpinner.getValue(), radiusSpinner.getValue()
                        , periodFunction, neighborhoodFunction);
                Platform.runLater(() -> {
                    DrawingMethods.drawCustomGrid(gcMC, cells, rectangleSize);
                    DrawingMethods.drawEnergyGrid(gcEnergy, cells, rectangleSize);
                    MCIterationsSpinner.getValueFactory().setValue(MCIterationsSpinner.getValue() - 1);
                });

            }
        });
        energyMCSimulation.setRepeats(true);
        defineArray();

        widthOfGrid.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!delayBetweenUpdates.isRunning()) {
                clearButtonOnAction();
            }
        });

        heightOfGrid.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (!delayBetweenUpdates.isRunning()) {
                clearButtonOnAction();
            }
        });

        periodBC.selectedProperty().addListener((observableValue, aBoolean, t1)
                ->periodFunction = InterfacesImplementation.checkPeriod(t1));

        initialNeighborhoodType.valueProperty().addListener((observableValue, s, t1) ->
            neighborhoodFunction = InterfacesImplementation.initialNeighbourhoodTypeSwitch(t1));


        CAcanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int row = (int) (mouseEvent.getY() / rectangleSize.getY());
            int col = (int) (mouseEvent.getX() / rectangleSize.getX());

            if (col < cells[0].length && row < cells.length) {
                switch (initialStruct.getValue()) {
                    case "Manual":
                        InitialStructTypes.manualNucleation(cells, colorSet, row, col);
                        break;
                    case "Random": {
                        defineArray();
                        InitialStructTypes.randomNucleation(cells, colorSet, budNumber.getValue());
                        break;
                    }
                    case "Homogeneously": {
                        defineArray();
                        InitialStructTypes.homogeneouslyNucleation(cells, colorSet, inColNumber.getValue(),
                                inRowNumber.getValue());
                        break;
                    }
                    case "Radius": {
                        defineArray();
                        InitialStructTypes.radiusNucleation(cells, colorSet, budNumber.getValue(),
                                radiusSpinner.getValue(), periodFunction);
                        break;
                    }
                }
                DrawingMethods.drawCustomGrid(gc, cells, rectangleSize);
            }
        });
    }

    @FXML
    public void MCstartButtonOnAction() {
        if (Arrays.stream(cells).
                allMatch(cells -> Arrays.stream(cells).
                        allMatch(Cell::isState))) {
            energyMCSimulation.start();
        }
    }

    @FXML
    public void startButtonOnAction() {
        delayBetweenUpdates.start();
    }

    @FXML
    public void stopButtonOnAction() {
        delayBetweenUpdates.stop();
        energyMCSimulation.stop();
    }

    @FXML
    public void clearGridButtonOnAction() {
        if (delayBetweenUpdates.isRunning()) {
            delayBetweenUpdates.stop();
        }
        if (energyMCSimulation.isRunning()) {
            energyMCSimulation.stop();
        }
        defineArray();
    }

    @FXML
    public void clearButtonOnAction() {
        if (delayBetweenUpdates.isRunning()) {
            delayBetweenUpdates.stop();
        }
        if (energyMCSimulation.isRunning()) {
            energyMCSimulation.stop();
        }
        setSpinners();
        defineArray();

    }

    private void initValues() {
        delayBetweenIterations.getValueFactory().setValue(50);
        widthOfGrid.getValueFactory().setValue(30);
        heightOfGrid.getValueFactory().setValue(30);
        MCIterationsSpinner.getValueFactory().setValue(100);
        setSpinners();
    }

    private void setSpinners() {
        budNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, (int) widthOfGrid.getValue() * heightOfGrid.getValue()));
        inRowNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, (int) heightOfGrid.getValue() / 2));
        inColNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, (int) widthOfGrid.getValue() / 2));
        radiusSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, (int) widthOfGrid.getValue() / 3));
    }

    private void defineArray() {
        gc = CAcanvas.getGraphicsContext2D();
        gcEnergy = energyCanvas.getGraphicsContext2D();
        gcMC = MCcanvas.getGraphicsContext2D();
        rectangleSize = DrawingMethods.setRectangleSize(CAcanvas, widthOfGrid.getValue(), heightOfGrid.getValue());
        colorSet = new HashSet<>();
        bacteriaToUpdateArray = new ArrayList<>();
        cells = new Cell[heightOfGrid.getValue()][widthOfGrid.getValue()];
        periodFunction = InterfacesImplementation.checkPeriod(periodBC.isSelected());
        neighborhoodFunction = InterfacesImplementation.initialNeighbourhoodTypeSwitch(initialNeighborhoodType.getValue());
        for (int row = 0; row < heightOfGrid.getValue(); row++) {
            for (int column = 0; column < widthOfGrid.getValue(); column++) {
                cells[row][column] = new Cell(column + row * widthOfGrid.getValue(), Color.WHITE, row, column);
            }
        }
        DrawingMethods.drawStartGrid(CAcanvas);
        DrawingMethods.drawStartGrid(MCcanvas);
        DrawingMethods.drawStartGrid(energyCanvas);
    }


    private void changeGeneration() {
        bacteriaToUpdateArray.clear();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NTHREADS);
        List<Future<List<UpdateTrio>>> resultList = new ArrayList<>();
        for (int i = 0; i < cells.length; i++) {
            Callable<List<UpdateTrio>> callable = new ChangeGenerationCallable(i, cells, periodFunction,
                    neighborhoodFunction, radiusSpinner.getValue());
            Future<List<UpdateTrio>> result = executor.submit(callable);
            resultList.add(result);
        }
        for (Future<List<UpdateTrio>> listFuture : resultList) {
            try {
                bacteriaToUpdateArray.addAll(listFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


}
