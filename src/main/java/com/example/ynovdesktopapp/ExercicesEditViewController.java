package com.example.ynovdesktopapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExercicesEditViewController extends Stage {

    public ExercicesEditViewController() {
    }

    private Connection connexion;
    public List<Exercice> exercices = new ArrayList<>();

    double tabWidth = (Screen.getPrimary().getVisualBounds().getWidth() * 65 / 100);
    double tabHeight = (Screen.getPrimary().getVisualBounds().getHeight() * 90 / 100);

    public void init() {
        vueTableau.setPrefHeight(tabHeight);
        vueTableau.setPrefWidth(tabWidth);
        vueTableau.setMaxSize(tabWidth,tabHeight);
        vueTableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
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

        idColumn.setMinWidth(tabWidth * 4 / 100.0);
        nomColumn.setMinWidth(tabWidth * 25 / 100.0);
        uRLPhotoColumn.setMinWidth(tabWidth * 20 / 100.0);
        musclesSolicites.setMinWidth(tabWidth * 27 / 100.0);
        hautDuCorps.setMinWidth(tabWidth * 12 / 100.0);
        basDuCorps.setMinWidth(tabWidth * 12 / 100.0);

        this.doQuerry();

    }

    public void doQuerry() {
        listeExercicesPourVue.clear();
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            this.recupererDonneesExercices();
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
    }

    public List<Exercice> recupererDonneesExercices() {
        List<Exercice> listeDonnees = new ArrayList<>();
        try {
            Statement statement = connexion.createStatement();
            String requete = "SELECT * FROM Exercices";
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
                ajouterExerciceView(objet);
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
    private TableView<Exercice> vueTableau;
    @FXML
    private TableColumn<Exercice, String> idColumn;
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
    private ObservableList<Exercice> listeExercicesPourVue = FXCollections.observableArrayList();

    public void ajouterExerciceView(Exercice exerciceToViewElement) {
        System.out.println("ajout in view de l'objet");
        listeExercicesPourVue.add(exerciceToViewElement);
        vueTableau.setItems(listeExercicesPourVue);
    }

    @FXML
    private TextField idField;
    @FXML
    private TextField nomExerciceField;
    @FXML
    private TextField musclesSolicitesField;
    @FXML
    private TextField urlPhotoField;
    @FXML
    private CheckBox hautDuCorpsCheckbox;
    @FXML
    private CheckBox basDuCorpsCheckbox;

    @FXML
    private void editerExercice(ActionEvent event) {
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            try {
                // Récupérer les valeurs des champs
                String idExercice = idField.getText();
                String nomExercice = nomExerciceField.getText();
                String musclesSolicites = musclesSolicitesField.getText();
                String urlPhoto = urlPhotoField.getText();

                int hautDuCorps;
                if(hautDuCorpsCheckbox.isSelected()){
                    hautDuCorps = 0;
                }
                else {
                    hautDuCorps = 1;
                }
                int basDuCorps;
                if(basDuCorpsCheckbox.isSelected()){
                    basDuCorps = 0;
                }
                else {
                    basDuCorps = 1;
                }

                String requete = "UPDATE Exercices SET Nom_exercice = ?, Muscles_solicites = ?, URL_Photo = ?, Haut_du_corps = ?, Bas_du_corps = ? WHERE Id = ?";

                PreparedStatement statement = connexion.prepareStatement(requete);
                statement.setString(1, nomExercice);
                statement.setString(2, musclesSolicites);
                statement.setString(3, urlPhoto);
                statement.setInt(4, hautDuCorps);
                statement.setInt(5, basDuCorps);
                statement.setString(6, idExercice);

                int lignesModifiees = statement.executeUpdate();
                statement.close();

                reinitialiserComposants();

                System.out.println("Données insérées avec succès. Nombre de lignes modifiées : " + lignesModifiees);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'insertion des données : " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
        reinitialiserComposants();
        doQuerry();
    }

    @FXML
    private void supprimerExercice(ActionEvent event) {
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            try {
                // Récupérer les valeurs des champs
                String idExercice = idField.getText();


                String requete = "DELETE FROM Exercices WHERE Id = ?";

                PreparedStatement statement = connexion.prepareStatement(requete);

                statement.setString(1, idExercice);

                int lignesModifiees = statement.executeUpdate();
                statement.close();

                reinitialiserComposants();

                System.out.println("Données supprimés avec succès. Nombre de lignes modifiées : " + lignesModifiees);
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression des données : " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
        reinitialiserComposants();
        doQuerry();
    }

    public void reinitialiserComposants() {
        idField.setText("");
        nomExerciceField.setText("");
        musclesSolicitesField.setText("");
        urlPhotoField.setText("");

        hautDuCorpsCheckbox.setSelected(false);
        basDuCorpsCheckbox.setSelected(false);
    }

}
