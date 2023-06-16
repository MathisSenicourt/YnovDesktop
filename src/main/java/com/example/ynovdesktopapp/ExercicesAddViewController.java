package com.example.ynovdesktopapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javafx.stage.Stage;

public class ExercicesAddViewController extends Stage {

    public ExercicesAddViewController() {
    }

    public void init() {

    }
    private Connection connexion;
    @FXML
    private VBox vbox;
    @FXML
    private HBox hbox;
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
    private Button ajouterExerciceButton;

    @FXML
    private void ajouterExercice(ActionEvent event) {
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            try {
                // Récupérer les valeurs des champs
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

                String requete = "INSERT INTO Exercices (Nom_exercice, Muscles_solicites, URL_Photo, Haut_du_corps, Bas_du_corps) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement statement = connexion.prepareStatement(requete);
                statement.setString(1, nomExercice);
                statement.setString(2, musclesSolicites);
                statement.setString(3, urlPhoto);
                statement.setInt(4, hautDuCorps);
                statement.setInt(5, basDuCorps);

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

    public void reinitialiserComposants() {
        nomExerciceField.setText("");
        musclesSolicitesField.setText("");
        urlPhotoField.setText("");

        hautDuCorpsCheckbox.setSelected(false);
        basDuCorpsCheckbox.setSelected(false);
    }


}
