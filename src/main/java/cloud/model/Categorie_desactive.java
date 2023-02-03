package cloud.model;

import cloud.DAO.ObjectBDD;

public class Categorie_desactive extends ObjectBDD{
    private Integer id;
    private Integer categorieId;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCategorieId() {
        return categorieId;
    }
    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }
    public void init(){
        this.setNomDeTable("categorie_desactive");
        this.setPkey("id");
    }
    public Categorie_desactive(){
        this.init();
    }
}
