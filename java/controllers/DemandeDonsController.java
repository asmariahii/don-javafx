package controllers;

import entities.DemandeDons;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import service.DemandeDonsService;

import java.awt.event.ActionEvent;
import java.util.Optional;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;


public class DemandeDonsController {

    @FXML
    private TextArea contenuTextArea;

    @FXML
    private Button posterDemandeButton;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<DemandeDons> demandeListView;

    private DemandeDonsService demandeDonsService;

    public DemandeDonsController() {
        demandeDonsService = new DemandeDonsService();
    }
    @FXML
    public void initialize() {
        // Personnaliser l'affichage des demandes dans la ListView
        demandeListView.setCellFactory(param -> new ListCell<DemandeDons>() {
            @Override
            protected void updateItem(DemandeDons demande, boolean empty) {
                super.updateItem(demande, empty);
                if (empty || demande == null) {
                    setText(null);
                } else {
                    // Construire le texte à afficher dans la cellule
                    StringBuilder sb = new StringBuilder();
                    sb.append("Utilisateur: ").append(demande.getNomUser()).append(" ").append(demande.getPrenomUser());
                    sb.append("\nContenu: ").append(demande.getContenu());
                    sb.append("\nDate de publication: ").append(demande.getDatePublication()); // Affichage de la date de publication
                    sb.append("\nPoints gagnés: ").append(demande.getNbPoints()); // Utilisez la méthode getNbPoints() pour obtenir les points gagnés

                    // Vérifier si l'utilisateur connecté est l'auteur de la demande
                    int userIdConnecte = 2; // ID de l'utilisateur connecté (à remplacer par l'ID réel de l'utilisateur connecté)
                    if (demande.getIdUtilisateur() == userIdConnecte) {
                        // Ajouter un bouton Supprimer
                        Button deleteButton = new Button("Supprimer");
                        deleteButton.setOnAction(event -> deleteDemande(demande));

                        // Créer un conteneur pour afficher le texte et le bouton Supprimer
                        VBox container = new VBox(new Label(sb.toString()), deleteButton);
                        setGraphic(container);
                    } else {
                        // Afficher uniquement le texte
                        setText(sb.toString());
                        setGraphic(null); // Assurer que le bouton Supprimer n'est pas affiché
                    }
                }
            }
        });

        // Charger les demandes existantes lors de l'initialisation
        loadDemandes();

        // Ajouter un écouteur d'événement au bouton "Poster Demande"
        posterDemandeButton.setOnAction(event -> posterDemande());
    }





    private void deleteDemande(DemandeDons demande) {
        // Vérifier si l'utilisateur connecté est l'auteur de la demande
        int userIdConnecte = 3; // ID de l'utilisateur connecté (à remplacer par l'ID réel de l'utilisateur connecté)
        if (demande.getIdUtilisateur() == userIdConnecte) {
            // Confirmer la suppression avec une boîte de dialogue
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation de suppression");
            confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette demande ?");
            confirmationDialog.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Appeler le service pour supprimer la demande
                if (demandeDonsService.supprimerDemande(demande.getIdDemande())) {
                    // Recharger les demandes après la suppression
                    loadDemandes();
                } else {
                    afficherAlerte("Erreur", "Erreur lors de la suppression de la demande.");
                }
            }
        } else {
            afficherAlerte("Erreur", "Vous n'êtes pas autorisé à supprimer cette demande.");
        }
    }




    @FXML
    public void posterDemande() {
        // Récupérer le contenu de la demande
        String contenu = contenuTextArea.getText();

        // Créer un objet DemandeDons avec les informations
        DemandeDons nouvelleDemande = new DemandeDons();
        nouvelleDemande.setIdUtilisateur(3); // ID de l'utilisateur 3
        nouvelleDemande.setContenu(contenu);

        // Poster la demande en utilisant le service
        demandeDonsService.posterDemande(nouvelleDemande);

        // Recharger les demandes pour afficher la nouvelle demande
        loadDemandes();
    }
    @FXML
    public void transferPoints() {
        DemandeDons selectedDemande = demandeListView.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            // Demander à l'utilisateur le nombre de points à transférer
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Transférer des points");
            dialog.setHeaderText("Transférer des points à l'utilisateur");
            dialog.setContentText("Entrez le nombre de points à transférer:");
            Optional<String> result = dialog.showAndWait();

            // Vérifier si l'utilisateur a saisi un nombre valide
            if (result.isPresent()) {
                try {
                    int donPoints = Integer.parseInt(result.get());
                    if (donPoints > 0) {
                        int userId = selectedDemande.getIdUtilisateur(); // Récupérer l'ID de l'utilisateur associé à la demande
                        int idDemande = selectedDemande.getIdDemande(); // Récupérer l'ID de la demande

                        // Appeler la méthode pour ajouter les dons pour la demande sélectionnée
                        int donId = demandeDonsService.addDonsForDemande(userId, donPoints, idDemande);

                        // Mettre à jour les points gagnés dans la demande sélectionnée en ajoutant les nouveaux points aux points existants
                        if (donId != -1) { // Vérifier si l'ajout des dons a réussi
                            selectedDemande.setNbPoints(selectedDemande.getNbPoints() + donPoints);
                            // Rafraîchir l'affichage des demandes pour refléter les modifications
                            loadDemandes();
                        } else {
                            afficherAlerte("Erreur", "Erreur lors de l'ajout des points pour la demande.");
                        }
                    } else {
                        afficherAlerte("Erreur", "Veuillez saisir un nombre valide de points.");
                    }
                } catch (NumberFormatException e) {
                    afficherAlerte("Erreur", "Veuillez saisir un nombre entier valide.");
                }
            }
        } else {
            afficherAlerte("Erreur", "Veuillez sélectionner une demande pour transférer des points.");
        }
    }



    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void loadDemandes() {
        demandeListView.getItems().clear();
        demandeListView.getItems().addAll(demandeDonsService.getDemandesAvecUtilisateurs());
    }
}
