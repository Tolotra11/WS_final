package cloud.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cloud.model.Error;
import cloud.model.Success;
import cloud.model.Utilisateur;
import cloud.model.V_enchere;
import cloud.util.TokenUtil;

@RestController
public class UtilisateurController {
   
    @CrossOrigin
    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody Utilisateur admin){
        HashMap<String,Object> map = null;
       
            try{
                map = admin.login();
            }
            catch(Exception e){
                Error err = new Error();
                e.printStackTrace();
                return err.getError("Email ou mot de passe incorrect");
            }
         
        return map;
    }
    @CrossOrigin
    @PutMapping("/rechargements")
    public HashMap<String,Object> recharger(@RequestHeader(name = "token",required = false) String token,@RequestHeader(name = "credit",required = true)double credit) throws Exception{
        HashMap<String,Object> map = new HashMap<>();
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
                int idUser = Integer.parseInt(user.split(" ")[1]);
                Utilisateur myUser = new Utilisateur();
                myUser.setId(idUser);
                try{                  
                    myUser.recharger(credit);
                    Success success = new Success();
                    success.setMessage("Demande envoyé avec succès");
                    map.put("success",success);
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
    @PostMapping("/inscription")
    public HashMap<String,Object> inscription(@RequestBody Utilisateur user){
        HashMap<String,Object> map = null;
       
            try{
                Utilisateur.inscription(user);
                Success succes = new Success();
                succes.setMessage("Inscription reussi");
                map = new HashMap<>();
                map.put("success", succes);
            }
            catch(Exception e){
                Error err = new Error();
                e.printStackTrace();
                return err.getError(e.getMessage());
            }
         
        return map;
    }
    @CrossOrigin
    @GetMapping("/token")
    public HashMap<String,Object> validateToken(@RequestHeader("token") String token){
        HashMap<String,Object> map = new HashMap<>();
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
                try{                  
                    Success success = new Success();
                    success.setMessage("Validate");
                    map.put("success",success);
                }
                catch(Exception e){
                    Error err = new Error();
                    e.printStackTrace();
                    return err.getError(e.getMessage());
                }
        }
        return map;   
    }
    
    
}
