package cloud.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import cloud.DAO.ObjectBDD;
import cloud.util.Util;

public class V_enchere extends ObjectBDD{
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
    private String apercuImage;
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
     public V_enchere(){
        this.init();
     }
     public String getApercuImage() throws Exception {
        if(this.apercuImage == null){
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet res = null;
            try{
                con = Util.getConnection();
                stmt = con.prepareStatement("SELECT photo FROM image where enchereid=? LIMIT 1");
                stmt.setInt(1,this.getId());
                res = stmt.executeQuery();
                if(res.next()){
                    apercuImage = res.getString("photo");
                }
            }
            catch(Exception e){
                throw e;
            }
            finally{
                if(res != null){
                    res.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(con != null){
                    con.close();
                }
            }
        }
        return apercuImage;
    }
    public Mouvement_encheres getMaxEnchere(Connection con) throws Exception{
        Mouvement_encheres m = new Mouvement_encheres();
        m.setEnchereId(this.id);
        m =(Mouvement_encheres)m.find(con," 1=1 ORDER BY valeurEnchere DESC LIMIT 1")[0];
        return m;
    }
    public double getMaxEnchereForMise() throws Exception{
        Connection con = null;
        double max = 0.0;
        try{
            con = Util.getConnection();
            Mouvement_encheres m = new Mouvement_encheres();
            m.setEnchereId(this.id);
            Object [] lmise = m.find(con," 1=1 ORDER BY valeurEnchere DESC LIMIT 1");
            if(lmise.length == 0){
                max = this.getPrixMinimal();
            }
            else{
                max = ((Mouvement_encheres)lmise[0]).getValeurEnchere();
            }
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(con != null){
                con.close();
            }
        }
        return max;
    }
    public void setApercuImage(String apercuImage) {
        this.apercuImage = apercuImage;
    }
}
