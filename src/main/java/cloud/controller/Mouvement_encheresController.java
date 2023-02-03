package cloud.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import cloud.model.Error;
import cloud.model.Mouvement_encheres;
import cloud.model.V_mouvement_encheres;
import cloud.util.TokenUtil;

@RestController
public class Mouvement_encheresController {
    @CrossOrigin
    @GetMapping("/historiques")
    public HashMap<String,Object> getHistorique(@RequestHeader(name = "token")String token){
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
               V_mouvement_encheres mves = new V_mouvement_encheres();
                mves.setIdClient(idUser);
                Object [] lmves = mves.find(null);
                map.put("data",lmves);
            }
            catch(Exception e){
                Error err = new Error();
                return err.getError(e.getMessage());
            }
        }
        return map;
    }
}
