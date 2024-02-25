package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import service.DonsService;

import java.sql.SQLException;

public class AjouterEtatStatutController {
    @FXML
    private ComboBox<String> emailComboBox;

    @FXML
    private ComboBox<String> etatComboBox;

    private DonsService donsService; // Vous devez injecter une instance de DonsService ici

    public void setDonsService(DonsService donsService) {
        this.donsService = donsService;
    }

    public AjouterEtatStatutController(){
        donsService = new DonsService();

    }
    @FXML
    void initialize() {
        try {
            // Initialise la ComboBox avec les emails des utilisateurs
            ObservableList<String> emails = FXCollections.observableArrayList(donsService.getAllUserEmailsAndPoints());
            emailComboBox.setItems(emails);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des emails des utilisateurs : " + e.getMessage());
        }
    }

    @FXML
    void handleAjouterEtatStatut() {
        String selectedEmail = emailComboBox.getValue();
        String selectedEtat = etatComboBox.getValue();

        if (selectedEmail != null && selectedEtat != null) {
            try {
                int donsId = donsService.getDonsIdByEmail(selectedEmail);
                if (donsId != -1) {
                    donsService.addEtatStatutDons(donsId, selectedEtat);
                    System.out.println("État du statut de don ajouté avec succès.");
                } else {
                    System.out.println("Aucun don associé à cet email.");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'état du statut de don : " + e.getMessage());
            }
        } else {
            System.out.println("Veuillez sélectionner un utilisateur et un état.");
        }
    }
}