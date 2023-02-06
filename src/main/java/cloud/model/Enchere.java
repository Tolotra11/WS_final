package cloud.model;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.connection.Time;

import cloud.DAO.ObjectBDD;
import cloud.util.ConnectionMongo;
import cloud.util.Util;


public class Enchere extends ObjectBDD{
    private Integer id;
    private String titre;
    private String  description;
    private Double prixMinimal;
    private Double duree;
    private Timestamp dateEnchere;
    private Integer categorieId;
    private Integer utilisateurId;
    private Integer statut;

    
    private Mouvement_encheres[] me;
    
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    public void setDuree(Double duree) throws Exception {
    	Parametre p=new Parametre();
    	Object[] op = p.find(null);
    	p = (Parametre)op[0];
    	if(duree<p.getDureeEnchereMin()) {
    		throw new Exception("duree trop courte");
    	}if(duree>p.getDureeEnchereMax()) {
    		throw new Exception("duree trop long");
    	}
    	if(duree>=p.getDureeEnchereMin() && duree<=p.getDureeEnchereMax()) {
        this.duree = duree;
    	}
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
    public void init(){
        this.setNomDeTable("enchere");
        this.setPkey("id");
    }
    public Enchere() {
        this.init();
    }
    public boolean haveAlreadyEnchere() throws Exception{
        boolean val = true;
        try{
            int numberOfRows = this.getMe().length;
            if(numberOfRows == 0){
                val = false;
            }
        }
        catch(Exception e){
            throw e;
        }
        return val;
     }
    public Mouvement_encheres[] getMe() throws Exception {
        if(this.me == null){
            Mouvement_encheres mes = new Mouvement_encheres();
            mes.setEnchereId(this.id);
            Object [] liste = mes.find(null);
            this.me = new Mouvement_encheres[liste.length];
            for(int i=0; i<liste.length; i++){
                this.me[i] = (Mouvement_encheres)liste[i];
            }
        }
        return me;
    }
    public void setMe(Mouvement_encheres[] me) {
        this.me = me;
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
            Object [] lmise = m.find(con);
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
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public static String construct_request(String titre, String description, String prixMinimal, String prixMaximal,
			String dateDebut, String dateFin, String categorieId,String statut) {
		String requtes = " 1=1 ";
		try {
			if (!titre.equals("")) {
				requtes += "AND titre like '%" + titre + "%'";
			}
			if (!description.equals("")) {
				requtes += "AND description like '%" + description + "%'";
			}
			if (!statut.equals("")) {
				requtes += " AND statut='" + statut + "'";
			}
			if (!categorieId.equals("")) {
				requtes += " AND categorieid='" + categorieId + "'";
			}
			if ((!dateFin.equals("") || !dateDebut.equals(""))) {
				if (!dateFin.equals("") && !dateDebut.equals("")) {
					requtes += " AND (dateenchere BETWEEN '" + dateDebut + "' and '" + dateFin + "')";
				}
				if (!dateFin.equals("") && !dateDebut.equals("")) {
					requtes += " AND (dateenchere <= '" + dateFin + "')";
				}
				if (dateFin.equals("") && !dateDebut.equals("")) {
					requtes += " AND (dateenchere >= '" + dateDebut + "')";
				}
			}
			if (!prixMaximal.equals("") || !prixMinimal.equals("")) {
				if (!prixMaximal.equals("") && !prixMinimal.equals("")) {
					requtes += " AND (prixminimal BETWEEN '" + prixMinimal + "' and '" + prixMaximal + "')";
				}
				if (!prixMaximal.equals("") && prixMinimal.equals("")) {
					requtes += " AND (prixminimal <= '" + prixMaximal + "')";
				}
				if (prixMaximal.equals("") && !prixMinimal.equals("")) {
					requtes += " AND (prixminimal >= '" + prixMinimal + "')";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return requtes;
	}
	
   
      public Enchere(Integer id, String titre, String description, Double prixMinimal, Double duree, Timestamp dateEnchere,
			Integer categorieId, Integer utilisateurId, Integer statut) {
		super();
        this.init();
		this.id = id;
		this.description = description;
		this.prixMinimal = prixMinimal;
		this.duree = duree;
		this.dateEnchere = dateEnchere;
		this.categorieId = categorieId;
		this.utilisateurId = utilisateurId;
		this.statut = statut;
		this.titre = titre;
	}
	
	public Enchere(String description, Double prixMinimal, Double duree, Timestamp dateEnchere, Integer categorieId,
			Integer utilisateurId, Integer statut) {
		super();
        this.init();
		this.description = description;
		this.prixMinimal = prixMinimal;
		this.duree = duree;
		this.dateEnchere = dateEnchere;
		this.categorieId = categorieId;
		this.utilisateurId = utilisateurId;
		this.statut = statut;
	}
    public void create(Connection c) throws Exception {
		try {
            c.setAutoCommit(false);
			MongoDatabase database = ConnectionMongo.getMongoConnection();
			MongoCollection<Document> collection = database.getCollection("enchere");
			Document filtre = new Document("enchereId", this.getId());
			filtre.append("dateenchere",this.getDateEnchere().toString());
			filtre.append("duree",this.getDuree());
			filtre.append("prixdepart", this.getPrixMinimal());
			filtre.append("description", this.getDescription());
			filtre.append("titre", this.getTitre());
			Categorie ca = new Categorie();
			ca.setId(this.getCategorieId());
			ca = (Categorie) ca.find(c)[0];
			Utilisateur user = new Utilisateur();
			user.setId(this.getUtilisateurId());
			user =(Utilisateur) user.find(c)[0];
			filtre.append("utilisateurid", user.getId());
			filtre.append("email", user.getLogin());
			filtre.append("nom", user.getNom());
			filtre.append("prenom", user.getPrenom());
			filtre.append("credit", user.getCredit());	
			filtre.append("categorie", this.getCategorieId());
			filtre.append("nomCategorie", ca.getNomCat());
			filtre.append("statut", this.getStatut().toString());
			collection.insertOne(filtre);
            c.commit();
		} catch (Exception ex) {
			if (c != null)
				c.rollback();
			throw ex;
		}
	}
	
	
	public static String constructJsonMongo(String jsonInputString) throws Exception{
	     
        String[] ltObject = Enchere.splitObject(jsonInputString, "Document");
        System.out.println("ltobj: " + ltObject.length);
        String data=" ";
        for (int i = 0; i < ltObject.length; i++) {
            System.out.println(ltObject[i]);
        }
        for (int i = 0; i < ltObject.length; i++) {
           String requeteamboarina = ltObject[i].replace("{{_", "{");
           String requeteamboarinafarany = requeteamboarina.replace("}}", "}");   
           ltObject[i] = requeteamboarinafarany;
           }
        for (int i = 0; i < ltObject.length; i++) {
            System.out.println(ltObject[i]);
           data = data + ltObject[i] +";";}
       
        data = data.replace(";", "");
        System.out.println("data: "+data);
        return data;
     }
     
     
      public static String[] splitObject(String str,String var) throws Exception {
        String[] arrOfstr = str.split(var);
        return arrOfstr;
    }

	public static String getListeEnchere(int id) throws Exception {
        Bson filter = Filters.eq("enchereId",id);
		MongoDatabase database = ConnectionMongo.getMongoConnection();
		MongoCollection<Document> collection = database.getCollection("enchere");
		FindIterable<Document> iterDoc = collection.find(filter);
        MongoCursor<Document> cursor = iterDoc.iterator();
        List<String> test = new ArrayList<>();
        while(cursor.hasNext()){
            test.add(cursor.next().toJson());
         
        }
		return test.toString();

	}
    public static void checkStatut(Connection con) throws Exception{
        Encheres ench = new Encheres();
        ench.setStatut(0);
        Object [] listeEnchere = ench.find(con);
        LocalDateTime now = LocalDateTime.now();
        for(int i = 0; i<listeEnchere.length; i++){
            Encheres e = (Encheres)listeEnchere[i];
            LocalDateTime publication = e.getDateEnchere().toLocalDateTime();
            LocalDateTime finalDate = publication.plusHours(e.getDuree().longValue());
            if(now.isAfter(finalDate)){
                e.setStatut(1);
                e.update("id", con);
            }
        }
    }
      
    /**/
}
