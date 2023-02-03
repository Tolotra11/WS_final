package cloud.model;

import java.sql.Date;

import cloud.DAO.ObjectBDD;

public class Demande_credit extends ObjectBDD{
    private Integer id;
    private Date dateDemande;
    private Double valeur;
    private Integer utilisateurId;
    private Integer etat;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDateDemande() {
        return dateDemande;
    }
    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }
    public Double getValeur() {
        return valeur;
    }
    public void setValeur(Double valeur) {
        if(valeur <= 0){
            
        }
        this.valeur = valeur;
    }
    public Integer getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
    public void init(){
        this.setNomDeTable("demande_credit");
        this.setPkey("id");
    }
    public Demande_credit() {
        this.init();
    }
    public Integer getEtat() {
        return etat;
    }
    public void setEtat(Integer etat) {
        this.etat = etat;
    }
    
}
