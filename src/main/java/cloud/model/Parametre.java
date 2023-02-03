package cloud.model;

import java.sql.Connection;


import cloud.DAO.ObjectBDD;
import cloud.util.Util;

public class Parametre extends ObjectBDD{
    private Integer id;
    private Double commission;
    private Double dureeEnchereMin;
    private Double dureeEnchereMax;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Double getCommission() {
        return commission;
    }
    public void setCommission(Double comission) throws Exception {
        if(commission < 0 || commission > 100){
            throw new Exception("Commission invalide");
        }
        this.commission = comission;
    }
    public Double getDureeEnchereMin() {
        return dureeEnchereMin;
    }
    public void setDureeEnchereMin(Double dureeEnchereMin) throws Exception {
        if(dureeEnchereMin <= 0){
            throw new Exception("duree invalide");
        }
        this.dureeEnchereMin = dureeEnchereMin;
    }
    public Double getDureeEnchereMax() {
        return dureeEnchereMax;
    }
    public void setDureeEnchereMax(Double dureeEnchereMax) throws Exception {
        if(dureeEnchereMax <= 0){
            throw new Exception("duree invalide");
        }
        this.dureeEnchereMax = dureeEnchereMax;
    }
    public void init(){
        this.setNomDeTable("parametre");
        this.setPkey("id");
    }
    public Parametre() {
        this.init();
    }
    public void update() throws Exception{
        Connection con = null;
        try{
            con = Util.getConnection();
            this.update("id",con);
        }
        catch(Exception e){
            throw e;
        }
        finally{
            if(con != null){
                con.close();
            }
        }
    }

    
}
