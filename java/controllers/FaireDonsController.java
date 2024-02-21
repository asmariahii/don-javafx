package controllers;

import entities.Dons;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.DonsService;
import service.utilisateurService;
import entities.utilisateur;

import java.text.SimpleDateFormat;
import java.util.List;

public class FaireDonsController {
    @FXML
    private TableView<Dons> donsTable;

    @FXML
    private TableColumn<Dons, String> nomUserColumn;

    @FXML
    private TableColumn<Dons, String> prenomUserColumn;



    @FXML
    private TableColumn<Dons, Integer> nbPointsColumn;

    @FXML
    private TableColumn<Dons, String> dateAjoutColumn;

    @FXML
    private TableColumn<Dons, String> etatStatutDonsColumn;


    @FXML
    private Label pointsLabel;

    @FXML
    private TextField donPointsField;

    private DonsService donsService;
    private utilisateurService userService;
    private utilisateur utilisateur;

    public FaireDonsController() {
        donsService = new DonsService();
        userService = new utilisateurService();
    }
    @FXML
    void initialize() {
        // Récupérer l'utilisateur avec l'ID 1
        utilisateur user = userService.getUserById(1);
        if (user != null) {
            // Afficher les points de l'utilisateur dans l'étiquette
            pointsLabel.setText(String.valueOf(user.getNbPoints()));
        } else {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            pointsLabel.setText("Utilisateur non trouvé");
        }
        nomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomUser()));
        prenomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenomUser()));
        nbPointsColumn.setCellValueFactory(new PropertyValueFactory<>("nbPoints"));
        dateAjoutColumn.setCellValueFactory(data -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(data.getValue().getDate_ajout()));
        });
        etatStatutDonsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEtatStatutDons()));


        loadDons();
    }

    public void setUser(utilisateur user) {
        this.utilisateur = user;
        updatePointsLabel();
    }

    private void updatePointsLabel() {
        int pointsUtilisateur = userService.getUserById(utilisateur.getIdUser()).getNbPoints();
        pointsLabel.setText("Points disponibles : " + pointsUtilisateur);
    }

    @FXML
    private void handleAjouterDons(ActionEvent event) {
        // Récupérer l'utilisateur avec l'ID 1
        utilisateur utilisateur = userService.getUserById(1);

        String donPointsText = donPointsField.getText();
        if (donPointsText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champ vide", "Veuillez saisir le nombre de points.");
            return;
        }

        int donPoints = Integer.parseInt(donPointsText);
        if (donPoints <= 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Points invalides", "Veuillez entrer un nombre de points valide.");
            return;
        }
        if (donPoints > utilisateur.getNbPoints()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Points insuffisants", "Vous n'avez pas suffisamment de points pour effectuer ce don.");
            return;
        }
        int remainingPoints = donsService.addDons(utilisateur, donPoints);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Don effectué", "Votre don a été ajouté avec succès.");

        // Mettre à jour l'affichage des points dans l'interface utilisateur
        remainingPoints = utilisateur.getNbPoints() - donPoints; // Réutilisation de la variable existante

        pointsLabel.setText(String.valueOf(remainingPoints));
    }




    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    private void loadDons() {
        donsTable.getItems().clear();
        List<Dons> donsList = donsService.getAllDonsWithUserDetails();
        donsTable.getItems().addAll(donsList);
    }
}
