package controllers;

import entities.DemandeDons;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.DemandeDonsService;

import java.text.SimpleDateFormat;
import java.util.List;
import javafx.util.Callback;

public class GestionDemandeDonsController {

    @FXML
    private TableView<DemandeDons> demandeDonsTableView;

    @FXML
    private TableColumn<DemandeDons, String> contenuColumn;

    @FXML
    private TableColumn<DemandeDons, String> datePublicationColumn;

    @FXML
    private TableColumn<DemandeDons, Integer> nbPointsColumn;

    @FXML
    private TableColumn<DemandeDons, String> nomUserColumn;

    @FXML
    private TableColumn<DemandeDons, String> prenomUserColumn;

    private DemandeDonsService demandeDonsService;
    private Stage primaryStage;

    public GestionDemandeDonsController() {
        demandeDonsService = new DemandeDonsService();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void initialize() {
        contenuColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContenu()));
        datePublicationColumn.setCellValueFactory(data -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new SimpleStringProperty(dateFormat.format(data.getValue().getDatePublication()));
        });
        nbPointsColumn.setCellValueFactory(new PropertyValueFactory<>("nbPoints"));
        nomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomUser()));
        prenomUserColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenomUser()));

        loadDemandes();

    }

    private void loadDemandes() {
        List<DemandeDons> demandes = demandeDonsService.getDemandesAvecUsers();
        ObservableList<DemandeDons> observableList = FXCollections.observableArrayList(demandes);
        demandeDonsTableView.setItems(observableList);
    }



}
