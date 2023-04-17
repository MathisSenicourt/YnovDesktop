package com.example.ynovdesktopapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ExercicesShowViewController {

    private Connection connexion;
    public List<Exercices> exercices = new ArrayList<>();

    public void init() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("Exercice"));
        uRLPhoto.setCellValueFactory(new PropertyValueFactory<>("URLPhoto"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        basDuCorps.setCellValueFactory(new PropertyValueFactory<>("hautDuCorps"));
        hautDuCorps.setCellValueFactory(new PropertyValueFactory<>("basDuCorps"));
        uRLPhoto.setCellFactory(column -> {
            return new TableCell<Exercices, String>() {
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
                        imageView.setFitHeight(50); // ajuster la hauteur de l'image
                        imageView.setPreserveRatio(true); // conserver le ratio de l'image
                        setGraphic(imageView);
                    }
                }
            };
        });

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

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Go Muscu");
    }











    public List<Exercices> recupererDonnees() {
        List<Exercices> listeDonnees = new ArrayList<>();
        try {
            Statement statement = connexion.createStatement();
            String requete = "SELECT * FROM Exercices";
            ResultSet resultat = statement.executeQuery(requete);
            while (resultat.next()) {
                int id = resultat.getInt("Id");
                String exercice = resultat.getString("Nom_exercice");
                String urlPhoto = resultat.getString("URL_Photo");
                String description = resultat.getString("Description");
                Boolean hautDuCorps = resultat.getBoolean("Haut_du_corps");
                Boolean basDuCorps = resultat.getBoolean("Bas_du_corps");

                Exercices objet = new Exercices(id, exercice, urlPhoto, description, hautDuCorps, basDuCorps);
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
    private TableView<Exercices> tableView;
    @FXML
    private TableColumn<Exercices, String> nomColumn;
    @FXML
    private TableColumn<Exercices, String> uRLPhoto;
    @FXML
    private TableColumn<Exercices, String> descriptionColumn;
    @FXML
    private TableColumn<Exercices, String> hautDuCorps;
    @FXML
    private TableColumn<Exercices, String> basDuCorps;
    private ObservableList<Exercices> exercicesToViewList = FXCollections.observableArrayList();

    public void ajouterExercice(Exercices exerciceToViewElement) {
        System.out.println(exerciceToViewElement);
        exercicesToViewList.add(exerciceToViewElement);
        System.out.println(exercicesToViewList);
//        tableView.getColumns().get(1).setCellFactory(new PropertyValueFactory<Exercices, String>("Description"));

        tableView.setItems(exercicesToViewList);
        tableView.sort();
    }

}
