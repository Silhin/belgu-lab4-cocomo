package ru.silhin.cocomo;

import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import ru.silhin.cocomo.layer.AdvancedLayer;
import ru.silhin.cocomo.layer.BasicLayer;
import ru.silhin.cocomo.layer.IntermediateLayer;
import ru.silhin.cocomo.layer.Layer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    private static Layer LAYER;

    public ToggleGroup projectTypeGroup;
    public ToggleGroup scoreTypeGroup;
    public ComboBox<ProjectType> projectType;
    public TextField size;
    public GridPane gridPane;
    public TextField complexityField;
    public TextField timeField;

    private final List<ToggleGroup> factorGroups = new ArrayList<>();
    private final List<ToggleGroup> scoreFactorGroups = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LAYER = new BasicLayer();

        gridPane.getColumnConstraints().addAll(
                new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.LEFT, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false),
                new ColumnConstraints(-1, -1, -1, Priority.SOMETIMES, HPos.CENTER, false)
        );
        gridPane.setAlignment(Pos.TOP_CENTER);

        projectType.getItems().addAll(ProjectType.values());
        projectType.getSelectionModel().selectedItemProperty().addListener((projectType, oldValue, newValue) -> calc());

        projectTypeGroup.selectedToggleProperty().addListener((toggle, oldValue, newValue) -> {
            gridPane.getChildren().clear();
            factorGroups.clear();
            int id = Integer.parseInt((String) newValue.getUserData());
            switch (id) {
                case 0 -> {
                    LAYER = new BasicLayer();
                    this.setBasicPageElements();
                }
                case 1 -> {
                    LAYER = new IntermediateLayer();
                    this.setIntermediatePageElements();
                }
                case 2 -> {
                    AdvancedLayer layer = new AdvancedLayer();
                    layer.setScoreType(AdvancedScoreType.values()[Integer.parseInt((String) scoreTypeGroup.getSelectedToggle().getUserData())]);
                    LAYER = layer;
                    this.setAdvancedPageElements();
                }
            }
            calc();
        });
        scoreTypeGroup.selectedToggleProperty().addListener((toggle, oldValue, newValue) -> {
            if(LAYER instanceof AdvancedLayer layer) {
                gridPane.getChildren().clear();
                factorGroups.clear();
                layer.setScoreType(AdvancedScoreType.values()[Integer.parseInt((String) scoreTypeGroup.getSelectedToggle().getUserData())]);
                this.setAdvancedPageElements();
                calc();
            }
        });
    }

    public void calc() {
        if (!size.getText().isEmpty() && projectType.getValue() != null) {
            LAYER.setSize(Long.parseLong(size.getText()));
            LAYER.setType(projectType.getValue());

            if (LAYER instanceof IntermediateLayer layer) {
                for (int i = 0; i < factorGroups.size(); ++i) {
                    layer.setFactor(i, (Double) factorGroups.get(i).getSelectedToggle().getUserData());
                }
                if(layer instanceof AdvancedLayer advancedLayer) {
                    for (int i = 0; i < scoreFactorGroups.size(); ++i) {
                        advancedLayer.setScaleFactor(i, (Double) scoreFactorGroups.get(i).getSelectedToggle().getUserData());
                    }
                }
            }

            complexityField.setText(String.format("%.2f", LAYER.getComplexity()));
            timeField.setText(String.format("%.2f", LAYER.getTime()));
        }
    }

    private void setBasicPageElements() {
        projectType.setDisable(false);
        LaunchApplication.getInstance().resize(-1D, 240D);
    }

    private void setIntermediatePageElements() {
        projectType.setDisable(false);
        LaunchApplication.getInstance().resize(-1D, 550D);
        gridPane.add(new Label("Атрибуты стоимости"), 0, 0);
        gridPane.add(new Label("Очень низкий"), 1, 0);
        gridPane.add(new Label("Низкий"), 2, 0);
        gridPane.add(new Label("Средний"), 3, 0);
        gridPane.add(new Label("Высокий"), 4, 0);
        gridPane.add(new Label("Очень высокий"), 5, 0);
        gridPane.add(new Label("Критический"), 6, 0);
        try {
            this.createTable(factorGroups, 0,
                Files.readAllLines(Path.of(this.getClass().getClassLoader().getResource("intermediate_texts.txt").toURI())),
                this.getAllCoeff("intermediate_coeffs.csv")
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAdvancedPageElements() {
        projectType.setDisable(true);
        LaunchApplication.getInstance().resize(-1D, 720D);
        if (LAYER instanceof AdvancedLayer layer) {
            scoreFactorGroups.clear();
            gridPane.add(new Label("Факторы масштаба"), 0, 0);
            gridPane.add(new Label("Очень низкий"), 1, 0);
            gridPane.add(new Label("Низкий"), 2, 0);
            gridPane.add(new Label("Средний"), 3, 0);
            gridPane.add(new Label("Высокий"), 4, 0);
            gridPane.add(new Label("Очень высокий"), 5, 0);
            gridPane.add(new Label("Критический"), 6, 0);
            try {
                List<String> texts = Files.readAllLines(Path.of(this.getClass().getClassLoader().getResource("advanced_sf_texts.txt").toURI()));
                this.createTable(scoreFactorGroups, 0, texts, this.getAllCoeff("advanced_sf_coeffs.csv"));
                gridPane.add(new Label(""), 0, texts.size() + 1, 6, 1);
                gridPane.add(new Label("Множитель трудоемкости"), 0, texts.size() + 2);
                gridPane.add(new Label("Очень низкий"), 1, texts.size() + 2);
                gridPane.add(new Label("Низкий"), 2, texts.size() + 2);
                gridPane.add(new Label("Средний"), 3, texts.size() + 2);
                gridPane.add(new Label("Высокий"), 4, texts.size() + 2);
                gridPane.add(new Label("Очень высокий"), 5, texts.size() + 2);
                gridPane.add(new Label("Критический"), 6, texts.size() + 2);
                this.createTable(factorGroups, texts.size() + 3,
                        Files.readAllLines(Path.of(this.getClass().getClassLoader().getResource("advanced_em_" + layer.getScoreType().n + "_texts.txt").toURI())),
                        this.getAllCoeff("advanced_em_" + layer.getScoreType().n + "_coeffs.csv")
                );
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<List<Double>> getAllCoeff(String path) {
        try {
            return Files.readAllLines(Path.of(this.getClass().getClassLoader().getResource(path).toURI()))
                    .stream()
                    .map(s -> s.split(";"))
                    .map(s -> Arrays.stream(s).map(Double::parseDouble).toList())
                    .toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable(List<ToggleGroup> factorList, int offsetRow, List<String> texts, List<List<Double>> coeffs) {
        for (int i = 0; i < texts.size(); ++i) {
            gridPane.add(new Label(texts.get(i)), 0, i + offsetRow + 1);
        }
        for (int row = 0; row < texts.size(); ++row) {
            ToggleGroup factorGroup = new ToggleGroup();
            factorGroup.selectedToggleProperty().addListener(observable -> calc());
            for (int col = 0; col < 6; ++col) {
                RadioButton button = new RadioButton();
                button.setToggleGroup(factorGroup);
                button.setSelected(col == 2);

                Double coeff = coeffs.get(row).get(col);
                button.setUserData(coeff);
                button.setDisable(coeff < 0);
                gridPane.add(button, col + 1, row + offsetRow + 1);
            }
            factorList.add(factorGroup);
        }
    }

}
