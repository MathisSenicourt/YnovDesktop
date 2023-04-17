package com.example.ynovdesktopapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Recuperer l'ecran principal
        Screen screen = Screen.getPrimary();

        // Recuperer les dimensions de l'ecran
        Rectangle2D bounds = screen.getVisualBounds();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        FXMLLoader fxmlLoaderr = new FXMLLoader(MainApplication.class.getResource("exercices-show-view.fxml"));
        Scene scene = new Scene(fxmlLoaderr.load(),  bounds.getWidth()-75, bounds.getHeight()-75);
        primaryStage.setTitle("My Muscless");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);

        // Obtenir le contrôleur après le chargement du fichier FXML
        ExercicesShowViewController controller = fxmlLoaderr.getController();

        // Appeler la methode initialize() du contrôleur manuellement
        controller.init();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}