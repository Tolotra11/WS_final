package cloud.model;

import java.sql.Date;
import java.sql.Timestamp;

import cloud.DAO.ObjectBDD;

public class V_mouvement_encheres extends ObjectBDD{
    private Integer enchereId;
    private String titre;
    private String description;
    private Double prixMinimal;
    private Double duree;
    private Timestamp dateenchere;
    private Integer categorieId;
    private Integer idHote;
    private Integer statut;
    private String nom;
    private String prenom;
    private String nomCat;
    private Timestamp dateMouvement;
    private Double valeurenchere;
    private Integer idClient;
    private String nomClient;
    private String prenomClient;
    public Integer getEnchereId() {
        return enchereId;
    }
    public void setEnchereId(Integer enchereId) {
        this.enchereId = enchereId;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrixMinimal() {
        return prixMinimal;
    }
    public void setPrixMinimal(Double prixMinimal) {
        this.prixMinimal = prixMinimal;
    }
    public Double getDuree() {
        return duree;
    }
    public void setDuree(Double duree) {
        this.duree = duree;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public Integer getIdHote() {
        return idHote;
    }
    public void setIdHote(Integer idHote) {
        this.idHote = idHote;
    }
    public Integer getStatut() {
        return statut;
    }
    public void setStatut(Integer statut) {
        this.statut = statut;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getNomCat() {
        return nomCat;
    }
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }
   
    public Double getValeurenchere() {
        return valeurenchere;
    }
    public void setValeurenchere(Double valeurenchere) {
        this.valeurenchere = valeurenchere;
    }
    public Integer getIdClient() {
        return idClient;
    }
    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }
    public String getNomClient() {
        return nomClient;
    }
    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
    public String getPrenomClient() {
        return prenomClient;
    }
    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }
    public void init(){
        this.setNomDeTable("v_mouvementenchere");
        this.setPkey("id");
    }
    public V_mouvement_encheres(){
        this.init();
    }
    public Timestamp getDateenchere() {
        return dateenchere;
    }
    public void setDateenchere(Timestamp dateenchere) {
        this.dateenchere = dateenchere;
    }
    public Timestamp getDateMouvement() {
        return dateMouvement;
    }
    public void setDateMouvement(Timestamp dateMouvement) {
        this.dateMouvement = dateMouvement;
    }
}
