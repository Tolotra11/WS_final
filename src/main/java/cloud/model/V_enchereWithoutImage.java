package cloud.model;

import java.sql.Timestamp;

import cloud.DAO.ObjectBDD;

public class V_enchereWithoutImage extends ObjectBDD{
    private Integer id;
    private String titre;
    private String description;
    private Double prixMinimal;
    private Double duree;
    private Timestamp dateEnchere;
    private Integer categorieId;
    private Integer utilisateurId;
    private Integer statut;
    private String nom;
    private String prenom;
    private String nomCat;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    public Timestamp getDateEnchere() {
        return dateEnchere;
    }
    public void setDateEnchere(Timestamp dateEnchere) {
        this.dateEnchere = dateEnchere;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public Integer getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
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
     public void init(){
        this.setNomDeTable("v_enchere");
        this.setPkey("id");
     } 
     public V_enchereWithoutImage(){
        this.init();
     }


}
