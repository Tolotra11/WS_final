package cloud.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import java.util.List;
import cloud.model.Enchere;
import cloud.model.Error;
import cloud.model.Image;
import cloud.model.Success;
import cloud.model.Utilisateur;
import cloud.model.V_enchere;
import cloud.model.V_enchereWithoutImage;
import cloud.util.TokenUtil;
import cloud.util.Util;

@RestController
public class EnchereController {
    @CrossOrigin
    @GetMapping("/aencheres")
    public HashMap<String,Object> getAllEnchere() throws SQLException{
        HashMap<String,Object> map = null;
        Connection con = null;
        try{
            con = Util.getConnection();
            Enchere.checkStatut(con);
            map = new HashMap<>();
            V_enchere ve = new V_enchere();
            ve.setStatut(0);
            Object [] lE = ve.find(con,"1=1 Order BY dateEnchere DESC");
            for(int i = 0; i<lE.length; i++){
                ((V_enchere)lE[i]).getApercuImage();
            }
            map.put("data",lE);
        }
        catch(Exception e){
            Error err = new Error();
            e.printStackTrace();
            return err.getError(e.getMessage());
        }  
        finally{
            if(con != null){
                con.close();
            }
        }      
        return map;
    }
    @CrossOrigin
    @GetMapping("/encheres")
    public HashMap<String,Object> getAll(@RequestHeader(name = "token",required = false) String token) throws SQLException{
        HashMap<String,Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
        Connection con = null;
        try{
            String user = new TokenUtil().getUserByToken(token); 
                if(user == null ){
                    Error err = new Error();
                    return err.getError("You're not autorizhed");
                }
                con = Util.getConnection();  
                Enchere.checkStatut(con);   
                int idUser = Integer.parseInt(user.split(" ")[1]);
            map = new HashMap<>();
            V_enchere ve = new V_enchere();
            ve.setUtilisateurId(idUser);
            Object [] lE = ve.find(null, "1=1 Order BY dateEnchere DESC");
            for(int i = 0; i<lE.length; i++){
                ((V_enchere)lE[i]).getApercuImage();
            }
            map.put("data",lE);
        }
        catch(Exception e){
            Error err = new Error();
            e.printStackTrace();
            return err.getError(e.getMessage());
        }
        finally{
            if(con != null){
                con.close();
            }
        }
    }
        return map;
    }
    @CrossOrigin
    @PostMapping("/encheres/{idEnchere}/rencherir")
    public HashMap<String,Object> rencherir(@PathVariable int idEnchere,@RequestHeader(name = "token",required = false) String token,@RequestHeader(name = "mise",required = true) double mise){
        HashMap<String,Object> map = new HashMap<>();
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                String user = new TokenUtil().getUserByToken(token); 
                if(user == null ){
                    Error err = new Error();
                    return err.getError("You're not autorizhed");
                }     
                int idUser = Integer.parseInt(user.split(" ")[1]);
                Utilisateur myUser = new Utilisateur();
                myUser.setId(idUser);
                myUser = (Utilisateur)myUser.find(null)[0];
                myUser.renchechir(mise, idEnchere);
                Success success = new Success();
                success.setMessage("reussi");
                map.put("success",success );
            }
            catch(Exception e){
                Error err = new Error();
                e.printStackTrace();
                return err.getError(e.getMessage());

            }
        }
        return map;
    }
    @CrossOrigin
    @PostMapping("/encheres")
    public HashMap<String,Object> saveNewEnchere(@RequestHeader(name="description", required=false) String description,
                               @RequestHeader(name="prixMinimal", required=false) Double prixMinimal, 
                               @RequestHeader(name="duree", required=false) Double duree,
                               @RequestHeader(name="categorieId", required=false) Integer categorieId,
                               @RequestHeader(name="token", required=false) String token,
                               @RequestHeader(name="titre", required=false) String titre,
                               @RequestBody Image [] images
                             ) throws Exception{
                HashMap<String,Object> map = new HashMap<>();
                if(token == null || token.equals("")){
                    Error err = new Error();
                    return err.getError("You're not autorizhed");
                }
                else{
                    Connection con = null;
                    try{
                        con = cloud.util.Util.getConnection();
                        con.setAutoCommit(false);
                        String user = new TokenUtil().getUserByToken(token); 
                        if(user == null ){
                            Error err = new Error();
                            return err.getError("You're not autorizhed");
                        }     
                        int idUser = Integer.parseInt(user.split(" ")[1]);
                        Enchere v = new Enchere();
                        v.setDescription(description);
                        v.setPrixMinimal(prixMinimal);
                        v.setDuree(duree);
                        v.setDateEnchere(Timestamp.valueOf(LocalDateTime.now()));
                        v.setCategorieId(categorieId);
                        v.setUtilisateurId(idUser);
                        v.setStatut(0);
                        v.setTitre(titre);
                        int idEnchere = v.insertReturningId(con);
                        v.setId(idEnchere);
                        v.create(con);
                        for(int i = 0;i<images.length;i++){
                            images[i].setEnchereId(idEnchere);
                            images[i].setPhoto(images[i].getBase64String());
                            images[i].insert(con);
                        }
                        Success success = new Success();
                        success.setMessage("Insertion effectué avec succès");
                        map.put("success",success);
                        con.commit();
                    }
                    catch(Exception e){
                        con.rollback();
                        Error error = new Error();
                        e.printStackTrace();
                        return error.getError(e.getMessage());
                    }
                    finally{
                        if(con!= null){
                            con.close();
                        }
                    }
                }     
                return map; 
        }
    	
         @CrossOrigin
         @GetMapping("/fiches/{id}")
         public HashMap<String,Object> getenchere(@PathVariable int id) throws Exception{	      
              String json = null;
              HashMap <String,Object>map = new HashMap<String,Object>();
              try {
                  json =Enchere.getListeEnchere(id); 
                  Object [] images = new Image(id).find(null);
                  map.put("data", json);
                  map.put("photo",images);
             } catch (Exception e) {
                 e.printStackTrace();
                 Error err = new Error();
                 return err.getError(e.getMessage());             
                }             
              return map;
          }
        @CrossOrigin
	    @GetMapping("/enchere")	        
	    public HashMap<String,Object> recherehe( @RequestHeader(name="titre",defaultValue = "") String titre,
	    		   @RequestHeader(name="description", defaultValue = "") String description,
				   @RequestHeader(name="prixMinimal",defaultValue = "") String prixMinimal, 
				   @RequestHeader(name="prixMaximal", defaultValue = "") String prixMaximal, 
				   @RequestHeader(name="dateDebut", defaultValue = "") String dateDebut,
				   @RequestHeader(name="dateFin", defaultValue = "") String dateFin,
				   @RequestHeader(name="categorieId", defaultValue = "") String categorieId,  
				   @RequestHeader(name="statut", defaultValue = "") String statut
	    			) throws Exception{	      
	 		String requete = Enchere.construct_request(titre, description, prixMinimal, prixMaximal, dateDebut, dateFin, categorieId, statut);
	 		HashMap<String,Object> map = new HashMap<>();
            map.put("data",new V_enchereWithoutImage().find(null, requete));
	 		 return map;
	 	}
        @CrossOrigin
        @GetMapping("/mise/{idEnchere}")
        public HashMap<String,Object> maxForMise(@PathVariable int idEnchere){
            HashMap<String,Object> map = null;
            try{
                map = new HashMap<>();
                V_enchere ench = new V_enchere();
                ench.setId(idEnchere);
                ench = (V_enchere)ench.find(null)[0];
                double max = ench.getMaxEnchereForMise();
                map.put("data", max);
            }
            catch(Exception e){
                Error err = new Error();
                e.printStackTrace();;
                err.getError(e.getMessage());
            }
            return map;
        }
}
