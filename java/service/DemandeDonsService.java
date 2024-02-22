package service;

import entities.DemandeDons;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeDonsService {

    private Connection conn;
    private PreparedStatement pst;

    public DemandeDonsService() {
        conn = DataSource.getInstance().getCnx();
    }

    public boolean posterDemande(DemandeDons demandeDons) {
        String query = "INSERT INTO demandedons (idDemande, idUtilisateur, contenu, image, datePublication, nomUser, prenomUser) " +
                "SELECT ?, d.idUtilisateur, ?, ?, ?, u.nomUser, u.prenomUser " +
                "FROM demandedons d " +
                "JOIN utilisateur u ON d.idUtilisateur = u.idUser " +
                "WHERE d.idUtilisateur = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, demandeDons.getIdDemande());
            pst.setString(2, demandeDons.getContenu());
            pst.setString(3, demandeDons.getImage());
            pst.setTimestamp(4, demandeDons.getDatePublication());
            pst.setInt(5, demandeDons.getIdUtilisateur());

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

    public List<DemandeDons> getDemandesAvecUtilisateurs() {
        List<DemandeDons> demandesAvecUtilisateurs = new ArrayList<>();
        String query = "SELECT d.*, u.nomUser, u.prenomUser, d.nbpoints " + // Ajoutez d.nbpoints à votre requête
                "FROM demandedons d " +
                "JOIN utilisateur u ON d.idUtilisateur = u.idUser";
        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int idDemande = rs.getInt("idDemande");
                int idUtilisateur = rs.getInt("idUtilisateur");
                String contenu = rs.getString("contenu");
                String image = rs.getString("image");
                Timestamp datePublication = rs.getTimestamp("datePublication");
                String nomUser = rs.getString("nomUser");
                String prenomUser = rs.getString("prenomUser");
                int nbPoints = rs.getInt("nbpoints"); // Récupérer le nombre de points gagnés

                DemandeDons demande = new DemandeDons();
                demande.setIdDemande(idDemande);
                demande.setIdUtilisateur(idUtilisateur);
                demande.setContenu(contenu);
                demande.setImage(image);
                demande.setDatePublication(datePublication);
                demande.setNomUser(nomUser);
                demande.setPrenomUser(prenomUser);
                demande.setNbPoints(nbPoints); // Définir le nombre de points gagnés pour cette demande

                demandesAvecUtilisateurs.add(demande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandesAvecUtilisateurs;
    }


    public int addDonsForDemande(int userId, int donPoints, int idDemande) {
        int donId = -1;
        try {
            // Insérer le don dans la table Dons avec l'ID de l'utilisateur et les points de don
            String insertDonQuery = "INSERT INTO dons (idUser, nbpoints) VALUES (?, ?)";
            pst = conn.prepareStatement(insertDonQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, userId);
            pst.setInt(2, donPoints);
            pst.executeUpdate();

            // Récupérer l'ID du don inséré
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                donId = generatedKeys.getInt(1);
            } else {
                System.out.println("Erreur lors de la récupération de l'ID du don inséré.");
                return -1;
            }

            // Mettre à jour les points dans la table demandedons en ajoutant les nouveaux points aux points existants
            String updateDemandeQuery = "UPDATE demandedons SET idDons = ?, nbpoints = nbpoints + ? WHERE idDemande = ?";
            pst = conn.prepareStatement(updateDemandeQuery);
            pst.setInt(1, donId);
            pst.setInt(2, donPoints);
            pst.setInt(3, idDemande);
            pst.executeUpdate();

            System.out.println("Don ajouté avec succès pour la demande avec l'ID : " + idDemande);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du don pour la demande : " + e.getMessage());
        }
        return donId;
    }

    public List<DemandeDons> getDemandesAvecUsers() {
        List<DemandeDons> demandesAvecUsers = new ArrayList<>();
        String query = "SELECT d.contenu, d.datePublication, d.nbpoints, u.nomUser, u.prenomUser " +
                "FROM demandedons d " +
                "JOIN utilisateur u ON d.idUtilisateur = u.idUser";
        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String contenu = rs.getString("contenu");
                Timestamp datePublication = rs.getTimestamp("datePublication");
                int nbPoints = rs.getInt("nbpoints");
                String nomUser = rs.getString("nomUser");
                String prenomUser = rs.getString("prenomUser");

                DemandeDons demande = new DemandeDons();
                demande.setContenu(contenu);
                demande.setDatePublication(datePublication);
                demande.setNbPoints(nbPoints);
                demande.setNomUser(nomUser);
                demande.setPrenomUser(prenomUser);

                demandesAvecUsers.add(demande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandesAvecUsers;
    }

    public boolean supprimerDemandes(int idDemande, int idUtilisateur, String contenu, String image, Timestamp datePublication, int idDons, int nbPoints, String nomUser, String prenomUser) {
        String query = "DELETE FROM demandedons WHERE idDemande = ? AND idUtilisateur = ? AND contenu = ? AND image = ? AND datePublication = ? AND idDons = ? AND nbPoints = ? AND nomUser = ? AND prenomUser = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idDemande);
            pst.setInt(2, idUtilisateur);
            pst.setString(3, contenu);
            pst.setString(4, image);
            pst.setTimestamp(5, datePublication);
            pst.setInt(6, idDons);
            pst.setInt(7, nbPoints);
            pst.setString(8, nomUser);
            pst.setString(9, prenomUser);

            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }






}
