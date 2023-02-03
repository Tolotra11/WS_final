package cloud.model;

import java.sql.Connection;
import java.sql.PreparedStatement;


import cloud.DAO.ObjectBDD;
import cloud.util.Util;

public class Categorie extends ObjectBDD{
    private Integer id;
    private String nomCat;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomCat() {
        return nomCat;
    }
    public void setNomCat(String nomCat) throws Exception {
        if(nomCat.equals("")){
            throw new Exception("La case nom categorie doit être remplie");
        }
        this.nomCat = nomCat;
    }
    public Categorie() {
        this.init();
    }
    public void init(){
        this.setNomDeTable("categorie");
        this.setPkey("id");
    }
    public static void delete(int idCategorie) throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = Util.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("DELETE FROM Categorie WHERE id=?");
            stmt.setInt(1,idCategorie);
            stmt.execute();
            con.commit();
        }
        catch(Exception e){
            con.rollback();
            throw e;
        }
        finally{
            if(stmt!=null){
                stmt.close();
            }
            if(con != null){
                con.close();
            }
        }

    }
}
