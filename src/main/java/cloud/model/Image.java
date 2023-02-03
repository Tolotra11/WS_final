package cloud.model;

import cloud.DAO.ObjectBDD;

public class Image extends ObjectBDD{
    /*base64String
        : 
            "/9j/4SWcRXhpZgAATU0AKgAAAAgADAEAAAMAAAABD8AAAAEBA
            format
        : 
        "jpeg"*/
    private Integer id;
    private String photo;
    private String nomImage;
    private Integer enchereId;
    private String base64String;
    private String format;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomImage() {
        return nomImage;
    }
    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }
    public Integer getEnchereId() {
        return enchereId;
    }
    public void setEnchereId(Integer enchereId) {
        this.enchereId = enchereId;
    }
    public void init(){
        this.setNomDeTable("image");
        this.setPkey("id");
    }
    public Image() {
        this.init();
    }
    public String getBase64String() {
        return base64String;
    }
    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public Image(int enchereId){
        this.init();
        this.enchereId = enchereId;
    }
    
    
}
