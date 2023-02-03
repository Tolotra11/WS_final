package cloud.controller;

import java.sql.Connection;
import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cloud.model.Admin;
import cloud.model.Demande_credit;
import cloud.model.Error;
import cloud.model.Parametre;
import cloud.model.Statistique;
import cloud.model.Success;
import cloud.model.V_categorie_parEnchere;
import cloud.model.V_demande_credit;
import cloud.model.V_rentabitliteCat;
import cloud.util.TokenUtil;
import cloud.util.Util;

@RestController
public class AdminController {
    @CrossOrigin
    @PostMapping("/loginAdmin")
    public HashMap<String,Object> login(@RequestBody Admin admin){
        HashMap<String,Object> map = null;
       
            try{
                map = admin.login();
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError("Email ou mot de passe incorrect");
            }
         
        return map;
    }
    @CrossOrigin
    @PutMapping("/parametres")
    public HashMap<String,Object> updateParametre(@RequestBody Parametre parametre,@RequestHeader(name="token",required=false) String token){
        HashMap<String,Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                map = new HashMap<> ();
                    String user = new TokenUtil().getUserByToken(token);
                    if(user == null ){
                        Error err = new Error();
                        return err.getError("You're not autorizhed");
                    }
                parametre.setId(1);
                if(parametre.getCommission()<0 || parametre.getCommission()>100){
                    Error err = new Error();
                    return err.getError("Commission invalide");
                }
                if(parametre.getDureeEnchereMax()<0){
                    Error err = new Error();
                    return err.getError("Durre invalide");
                }
                if(parametre.getDureeEnchereMin()<0){
                    Error err = new Error();
                    return err.getError("Durre invalide");
                }
                parametre.update();
                Success success = new Success();
                success.setMessage("Configuration effectuée avec succès");
                map.put("success", success);
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
    @GetMapping("/parametres")
    public HashMap<String,Object> listeParametre(@RequestHeader(name="token",required=false) String token){
        HashMap<String,Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                map = new HashMap<>();
                String user = new TokenUtil().getUserByToken(token);
                    if(user == null ){
                        Error err = new Error();
                        return err.getError("You're not autorizhed");
                    }
                Parametre parametre = (Parametre)new Parametre().find(null)[0];
                map.put("data",parametre);
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
    @PutMapping("/validationCredits/{id}")
    public HashMap<String,Object> validationCredit(@RequestHeader(name="token",required=false) String token, @PathVariable int id){
        HashMap<String, Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                map = new HashMap<>();
                String user = new TokenUtil().getUserByToken(token);
                    if(user == null ){
                        Error err = new Error();
                        return err.getError("You're not autorizhed");
                    }
                Admin.validationCredit(id);
                Success success = new Success();
                success.setMessage("Succesful");
                map.put("Succes",success);
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
    @PutMapping("/refuserCredits/{id}")
    public HashMap<String,Object> refuserCredit(@RequestHeader(name="token",required=false) String token, @PathVariable int id){
        HashMap<String, Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                map = new HashMap<>();
                String user = new TokenUtil().getUserByToken(token);
                    if(user == null ){
                        Error err = new Error();
                        return err.getError("You're not autorizhed");
                    }
                Demande_credit dc = new Demande_credit();
                dc.setId(id);
                dc = (Demande_credit)dc.find(null)[0];
                dc.setEtat(2);
                dc.update("id", null);
                Success success = new Success();
                success.setMessage("Succesful");
                map.put("Succes",success);
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
    @GetMapping("/demandeCredits")
    public HashMap<String,Object> listeDemandeCredit(@RequestHeader(name="token",required=false) String token) throws Exception{
        HashMap<String, Object> map = null;
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            try{
                    map = new HashMap<>();
                    String user = new TokenUtil().getUserByToken(token);
                        if(user == null ){
                            Error err = new Error();
                            return err.getError("You're not autorizhed");
                        }
                        V_demande_credit ve = new V_demande_credit();
                        ve.setEtat(0);
                    Object [] listeDemande = ve.find(null);
                    map.put("data", listeDemande);
            }
            catch(Exception e){
                Error err = new Error();
                e.printStackTrace();
                return err.getError(e.getMessage());

        }
        return map;
    }
   
}
    @CrossOrigin
    @GetMapping("/statistiques")
    public HashMap<String,Object> statistique(@RequestHeader(name="token",required=false) String token) throws Exception{
        HashMap<String,Object>  map = new HashMap<>();
        if(token == null || token.equals("")){
            Error err = new Error();
            return err.getError("You're not autorizhed");
        }
        else{
            String user = new TokenUtil().getUserByToken(token);
            if(user == null ){
                Error err = new Error();
                return err.getError("You're not autorizhed");
            }
            Connection con = null;
            try{
                con = Util.getConnection();
                Object [] stat1 = new V_categorie_parEnchere().find(con,"1=1 ORDER BY nombre DESC");
                Object [] stat2 = new V_rentabitliteCat().find(con, "1=1 ORDER BY somme DESC");
                Statistique [] stats = new Statistique[2];
                stats[0] = new Statistique(stat1);
                stats[1] = new Statistique(stat2);
                map.put("data", stats);
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


}
