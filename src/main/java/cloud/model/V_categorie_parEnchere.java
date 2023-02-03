package cloud.model;

import cloud.DAO.ObjectBDD;

public class V_categorie_parEnchere extends ObjectBDD{
    private Long nombre;
    private String nomCat;
    private Integer categorieId;
    public Long getNombre() {
        return nombre;
    }
    public void setNombre(Long nombre) {
        this.nombre = nombre;
    }
    public String getNomCat() {
        return nomCat;
    }
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public void init(){
        this.setNomDeTable("v_categorie_parEnchere");
        this.setPkey("categorieid");
    }
    public V_categorie_parEnchere() {
        this.init();
    }
    
}
