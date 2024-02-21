package entities;

public class utilisateur {
    private int idUser;
    private int nbPoints;
    private String nomUser;
    private String prenomUser;
    private String emailUser;
    private String numTel;


    public utilisateur(int idUser, int nbPoints, String nomUser, String prenomUser, String emailUser, String numTel) {
        this.idUser = idUser;
        this.nbPoints = nbPoints;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.numTel = numTel;
    }

    public utilisateur(int nbPoints, String nomUser, String prenomUser, String emailUser, String numTel) {
        this.nbPoints = nbPoints;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.numTel = numTel;
    }

    public utilisateur() {

    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getNbPoints() {
        return nbPoints;
    }

    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUser=" + idUser +
                ", nbPoints=" + nbPoints +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", numTel='" + numTel + '\'' +
                '}';
    }
}
