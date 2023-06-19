package com.example.ynovdesktopapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
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
        vueTableau.setPrefHeight(tabHeight);
        vueTableau.setPrefWidth(tabWidth);
        vueTableau.setMaxSize(tabWidth,tabHeight);
        vueTableau.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


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

        this.doInitQuerry();

    }

    @FXML
    private Label titre;

    @FXML
    protected void onAllBoddyClick() {
        this.condition = "";
        this.doQuerry();
        titre.setText("Exercices pour tous le corps :");
    }

    @FXML
    protected void onTopBoddyClick() {
        this.condition = " WHERE Haut_du_corps = 0";
        this.doQuerry();
        titre.setText("Exercices pour le haut de corps :");
    }

    @FXML
    protected void onBotBoddyClick() {
        this.condition = " WHERE Bas_du_corps = 0";
        this.doQuerry();
        titre.setText("Exercices pour le bas de corps :");
    }

    @FXML
    public void onAddWindowClick()throws IOException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        ExercicesAddViewController nouvelleFenetreAjoutExercices = new ExercicesAddViewController();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("exercices-add-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  bounds.getWidth()/4, bounds.getHeight()/5);

        nouvelleFenetreAjoutExercices.setTitle("Ajout d'exercices");
        nouvelleFenetreAjoutExercices.setScene(scene);

        ExercicesAddViewController controller = fxmlLoader.getController();

        controller.init();

        nouvelleFenetreAjoutExercices.show();
    }

    @FXML
    public void onSystem()throws IOException {

        SystemInfoController nouvelleFenetreInfosSystem = new SystemInfoController();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("systemInfoView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  400, 600);

        nouvelleFenetreInfosSystem.setTitle("Infos system");
        nouvelleFenetreInfosSystem.setScene(scene);

        SystemInfoController controller = fxmlLoader.getController();

        controller.init();

        nouvelleFenetreInfosSystem.show();
    }

    @FXML
    public void onEditWindowClick()throws IOException {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        ExercicesEditViewController nouvelleFenetreEditExercices = new ExercicesEditViewController();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("exercices-edit-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  bounds.getWidth()-75, bounds.getHeight()-75);

        nouvelleFenetreEditExercices.setTitle("Modification d'exercices");
        nouvelleFenetreEditExercices.setScene(scene);

        ExercicesEditViewController controller = fxmlLoader.getController();

        controller.init();

        nouvelleFenetreEditExercices.show();
    }


    public void doInitQuerry() {
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            this.condition=recupererClauseDemarrage();
            modifierTitreSelonClauseDemarrage();
            this.recupererDonneesExercices();
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
    }

    public void doQuerry() {
        listeExercicesPourVue.clear();
        try {
            Class.forName("org.sqlite.JDBC");
            connexion = DriverManager.getConnection("jdbc:sqlite:C:desktopMuscu.sqlite");
            System.out.println("Connexion a la base de donnees SQLite etablie");
            this.modifierClauseDemarrage();
            this.recupererDonneesExercices();
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion a la base de donnees SQLite : " + e.getMessage());
        }
        fermerConnexion();
    }

    public String recupererClauseDemarrage() {
        String conditionDemarrage = "";
        try {
            Statement statement = connexion.createStatement();
            String requete = "SELECT ClauseWherePersistante FROM Demarrage where Id = 1";
            ResultSet resultat = statement.executeQuery(requete);
            while (resultat.next()) {
                conditionDemarrage = resultat.getString("ClauseWherePersistante");
            }
            resultat.close();
            statement.close();
            System.out.println("Donnees recuperees avec succes");
        } catch (Exception e) {
            System.err.println("Erreur lors de la recuperation des donnees : " + e.getMessage());
        }
        return conditionDemarrage;
    }

    public void modifierTitreSelonClauseDemarrage() {
        switch (this.condition) {
            case "":
                titre.setText("Exercices pour tous le corps :");
                break;
            case " WHERE Haut_du_corps = 0":
                titre.setText("Exercices pour le haut de corps :");
                break;
            case " WHERE Bas_du_corps = 0":
                titre.setText("Exercices pour le bas de corps :");
                break;
            default:
                titre.setText("");
                break;
        }
    }

    public void modifierClauseDemarrage() {
        try {
            Statement statement = connexion.createStatement();
            String requete = "UPDATE Demarrage SET ClauseWherePersistante = '"+this.condition+"' WHERE Id = 1";
            statement.executeUpdate(requete);
            statement.close();
            System.out.println("Donnee modifiee avec succes");
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification de la donnee : " + e.getMessage());
        }
    }

    public List<Exercice> recupererDonneesExercices() {
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

}
