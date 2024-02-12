package ru.silhin.cocomo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LaunchApplication extends Application {
    private Stage stage;

    public LaunchApplication() {
        INSTANCE = this;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = fxmlLoader.load();
        stage.setTitle("COCOMO Calc | Silhin");
        stage.setScene(scene);

        this.stage = stage;
        this.resize(900.0, 240.0);
        stage.show();
    }

    public void resize(double width, double height) {
        if(width > 0) {
            this.stage.setWidth(width);
        }

        if (height > 0) {
            this.stage.setHeight(height);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private static LaunchApplication INSTANCE;
    public static LaunchApplication getInstance() {
        return INSTANCE;
    }
}