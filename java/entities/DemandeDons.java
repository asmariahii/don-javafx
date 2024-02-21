package entities;

import java.sql.Timestamp;

public class DemandeDons {
    private int idDemande;
    private int idUtilisateur;
    private String contenu;
    private String image;
    private Timestamp datePublication;

    private int idUser;

    public DemandeDons(int idDemande, int idUtilisateur, String contenu, String image, Timestamp datePublication) {
        this.idDemande = idDemande;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.image = image;
        this.datePublication = datePublication;
    }

    public DemandeDons() {

    }

    // Getters and Setters
    public int getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(int idDemande) {
        this.idDemande = idDemande;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Timestamp datePublication) {
        this.datePublication = datePublication;
    }
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
