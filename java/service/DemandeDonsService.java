package service;

import entities.DemandeDons;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DemandeDonsService {

    private Connection conn;
    private PreparedStatement pst;

    public DemandeDonsService() {
        conn = DataSource.getInstance().getCnx();
    }

    public boolean posterDemande(DemandeDons demandeDons) {
        String query = "INSERT INTO demandedons (idDemande, idUtilisateur, contenu, image, datePublication) VALUES (?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, demandeDons.getIdDemande());
            pst.setInt(2, demandeDons.getIdUtilisateur());
            pst.setString(3, demandeDons.getContenu());
            pst.setString(4, demandeDons.getImage());
            pst.setTimestamp(5, demandeDons.getDatePublication());
            int rowsInserted = pst.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierDemande(DemandeDons demandeDons) {
        String query = "UPDATE demandedons SET contenu = ?, image = ? WHERE idDemande = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, demandeDons.getContenu());
            pst.setString(2, demandeDons.getImage());
            pst.setInt(3, demandeDons.getIdDemande());
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerDemande(int idDemande) {
        String query = "DELETE FROM demandedons WHERE idDemande = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idDemande);
            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
