package cloud.model;

import java.sql.Connection;
import java.util.HashMap;

import cloud.DAO.ObjectBDD;
import cloud.util.TokenUtil;
import cloud.util.Util;

public class Admin extends ObjectBDD{
    private Integer id;
    private String identifiant;
    private String password;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIdentifiant() {
        return identifiant;
    }
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void init(){
        this.setNomDeTable("admin");
        this.setPkey("id");
    }
    public Admin(){
        this.init();
    }
    public HashMap<String,Object> login() throws Exception{
        HashMap<String,Object> val = new HashMap<>();
        HashMap<String,Object> fin = new HashMap<>();
        TokenUtil utilToken = new TokenUtil();
        this.setPassword(Util.getMd5(this.getPassword()));
        Admin result = (Admin)this.find(null)[0];
        if(result == null){
            throw new Exception("Mot de passe ou identifiant incorrect");
        }
        else{
            String token = utilToken.generateToken(result);
            val.put("token", token);
            val.put("idAdmin",result.getId());
            val.put("identifiant", result.getIdentifiant());
            fin.put("data", val);
        }
        return fin;
    }
    public static void validationCredit(int idDemande) throws Exception{
        Connection con = null;
        try{
            con = Util.getConnection();
            con.setAutoCommit(false);
            Demande_credit dc = new Demande_credit();
            dc.setId(idDemande);
            dc  = (Demande_credit)dc.find(null)[0];
            Utilisateur user = new Utilisateur();
            user.setId(dc.getUtilisateurId());
            user = (Utilisateur)user.find(con)[0];
            user.setCredit(user.getCredit()+dc.getValeur());
            user.update("id", con);
            dc.setEtat(1);
            dc.update("id", con);
            con.commit();
        }
        catch(Exception e){
            con.rollback();
            throw e;
        } 
        finally{
            if(con != null){
                con.close();
            }
        }
     }
}
