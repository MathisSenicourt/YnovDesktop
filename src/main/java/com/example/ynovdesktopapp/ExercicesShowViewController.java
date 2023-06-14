package com.example.ynovdesktopapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class ExercicesShowViewController {

    private Connection connexion;
    private String condition ="";
    public List<Exercice> exercices = new ArrayList<>();

    // Récupération des dimensions de l'écran
    double tabWidth = (Screen.getPrimary().getVisualBounds().getWidth() * 65 / 100);
    double tabHeight = (Screen.getPrimary().getVisualBounds().getHeight() * 90 / 100);

    public void init() {
        tableView.setPrefHeight(tabHeight);
        tableView.setPrefWidth(tabWidth);
        tableView.setMaxSize(tabWidth,tabHeight);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        nomColumn.setCellValueFactory(new PropertyValueFactory<>("Exercice"));
        uRLPhotoColumn.setCellValueFactory(new PropertyValueFactory<>("URLPhoto"));
        musclesSolicites.setCellValueFactory(new PropertyValueFactory<>("musclesSolicites"));
        basDuCorps.setCellValueFactory(new PropertyValueFactory<>("hautDuCorps"));
        hautDuCorps.setCellValueFactory(new PropertyValueFactory<>("basDuCorps"));
        uRLPhotoColumn.setCellFactory(column -> {
            return new TableCell<Exercice, String>() {
                @Override
                protected void updateItem(String url, boolean empty) {
                    super.updateItem(url, empty);
                    if (url == null || empty) {
                        // Si la valeur de la cellule est null ou vide, ne rien afficher
                        setGraphic(null);
                    } else {
                        // Sinon, charger l'image depuis l'URL et l'afficher dans un ImageView
                        Image image = new Image(url);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(tabWidth * 12 / 100); // ajuster la hauteur de l'image
                        imageView.setPreserveRatio(true); // conserver le ratio de l'image
                        setGraphic(imageView);
                    }
                }
            };
        });

        nomColumn.setMinWidth(tabWidth * 25 / 100.0);
        uRLPhotoColumn.setMinWidth(tabWidth * 20 / 100.0);
        musclesSolicites.setMinWidth(tabWidth * 31 / 100.0);
        hautDuCorps.setMinWidth(tabWidth * 12 / 100.0);
        basDuCorps.setMinWidth(tabWidth * 12 / 100.0);

        nomColumn.setStyle("-fx-font-size: 26; -fx-alignment: center; -fx-wrap-text: true;");
        uRLPhotoColumn.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-wrap-text: true;");
        musclesSolicites.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-wrap-text: true;");
        hautDuCorps.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-wrap-text: true;");
        basDuCorps.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-wrap-text: true;");
        basDuCorps.setStyle("-fx-font-size: 18; -fx-alignment: center; -fx-wrap-text: true;");

        this.doQuerry();

    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Go Muscu");
    }

    @FXML
    protected void onAllBoddyClick() {
        this.condition = "";
        this.doQuerry();
        welcomeText.setText("Exercices pour tous le corps :");
    }

    @FXML
    protected void onTopBoddyClick() {
        this.condition = " WHERE Haut_du_corps = 0";
        this.doQuerry();
        welcomeText.setText("Exercices pour le haut de corps :");
    }

    @FXML
    protected void onBotBoddyClick() {
        this.condition = " WHERE Bas_du_corps = 0";
        this.doQuerry();
        welcomeText.setText("Exercices pour le bas de corps :");
    }

    public void doQuerry() {
        exercicesToViewList.clear();
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            this.recupererDonnees();
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
    }









    public List<Exercice> recupererDonnees() {
        List<Exercice> listeDonnees = new ArrayList<>();
        try {
            Statement statement = connexion.createStatement();
            String requete = "SELECT * FROM Exercices"+condition;
            ResultSet resultat = statement.executeQuery(requete);
            while (resultat.next()) {
                int id = resultat.getInt("Id");
                String exercice = resultat.getString("Nom_exercice");
                String urlPhoto = resultat.getString("URL_Photo");
                String musclesSolicites = resultat.getString("Muscles_solicites");
                Boolean hautDuCorps = resultat.getBoolean("Haut_du_corps");
                Boolean basDuCorps = resultat.getBoolean("Bas_du_corps");

                Exercice objet = new Exercice(id, exercice, urlPhoto, musclesSolicites, hautDuCorps, basDuCorps);
                System.out.println("objet ok");
                ajouterExercice(objet);
                listeDonnees.add(objet);
            }
            resultat.close();
            statement.close();
            System.out.println("Donnees recuperees avec succes");
        } catch (Exception e) {
            System.err.println("Erreur lors de la recuperation des donnees : " + e.getMessage());
        }
        return listeDonnees;
    }

    public void fermerConnexion() {
        try {
            if (connexion != null) {
                connexion.close();
                System.out.println("Connexion a la base de donnees SQLite fermee");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }


    @FXML
    private TableView<Exercice> tableView;
    @FXML
    private TableColumn<Exercice, String> nomColumn;
    @FXML
    private TableColumn<Exercice, String> uRLPhotoColumn;
    @FXML
    private TableColumn<Exercice, String> musclesSolicites;
    @FXML
    private TableColumn<Exercice, String> hautDuCorps;
    @FXML
    private TableColumn<Exercice, String> basDuCorps;
    private ObservableList<Exercice> exercicesToViewList = FXCollections.observableArrayList();

    public void ajouterExercice(Exercice exerciceToViewElement) {
        System.out.println("ajout in view de l'objet");
        exercicesToViewList.add(exerciceToViewElement);
        tableView.setItems(exercicesToViewList);
    }

}
