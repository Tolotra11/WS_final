package cloud.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cloud.model.Categorie;
import cloud.model.Categorie_desactive;
import cloud.model.Error;
import cloud.model.Success;
import cloud.util.TokenUtil;

@RestController
public class CategorieController {
    @CrossOrigin
    @PostMapping("/categories")
    public HashMap<String,Object> addCategorie(@RequestBody Categorie cat ,@RequestHeader(name="token",required=false) String token){
        HashMap<String,Object> map = null;
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
                map = new HashMap<>();
                cat.insert(null);
                Success success = new Success();
                success.setMessage("Insertion effectué avec succes");
                map.put("success",success);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }
    @CrossOrigin
    @GetMapping("/categories")
    public HashMap<String,Object> listeCategorie(@RequestHeader(name="token",required=false) String token){
        HashMap<String,Object> map = null;
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
                map = new HashMap<>();
                Object [] lcat = new Categorie().find(null);
                map.put("data",lcat);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }
    @CrossOrigin
    @GetMapping("/categories/{idCat}")
    public HashMap<String,Object> oneCategorie(@RequestHeader(name="token",required=false) String token, @PathVariable int idCat){
        HashMap<String,Object> map = null;
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
                map = new HashMap<>();
                Categorie cat = new Categorie();
                cat.setId(idCat);
                Object [] lcat = cat.find(null);
                map.put("data",lcat[0]);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }
    @CrossOrigin
    @GetMapping("/categoriesActive/")
    public HashMap<String,Object> categorieActive() throws Exception{
        HashMap<String,Object> map = new HashMap<>();
        Object [] lCategorie = new Categorie().find(null," id NOT IN(SELECT categorieid FROM categorie_desactive)");
        map.put("date", lCategorie);
        return map;
    }
    @CrossOrigin
    @DeleteMapping("/categories/{id}")
    public HashMap<String,Object> deleteCategorie(@RequestHeader(name="token",required=false) String token, @PathVariable int id) throws Exception{
        HashMap<String,Object> map = null;
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
                map = new HashMap<>();
                Categorie_desactive cat = new Categorie_desactive();
                cat.setCategorieId(id);
                cat.insert(null);
                Success success = new Success();
                success.setMessage("Suppression effectue avec succes");
                map.put("success",success);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }
    @CrossOrigin
    @PutMapping("/categories/{id}")
    public HashMap<String,Object> updateCategorie(@RequestBody Categorie cat ,@RequestHeader(name="token",required=false) String token, @PathVariable int id){
        HashMap<String,Object> map = null;
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
                map = new HashMap<>();
                cat.setId(id);
                cat.update("id", null);;
                Success success = new Success();
                success.setMessage("mise à jour effectué avec succes");
                map.put("success",success);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }

   
}
