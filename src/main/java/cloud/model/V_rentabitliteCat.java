package cloud.model;

import cloud.DAO.ObjectBDD;

public class V_rentabitliteCat extends ObjectBDD{
    private Double somme;
    private Integer categorieId;
    private String nomCat;
    public Double getSomme() {
        return somme;
    }
    public void setSomme(Double somme) {
        this.somme = somme;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public String getNomCat() {
        return nomCat;
    }
    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }
    public void init(){
        this.setNomDeTable("v_rentabiliteCat");
    }
    public V_rentabitliteCat() {
        this.init();
    }
    
}
