package cloud.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import cloud.model.Admin;
import cloud.model.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil implements Serializable{
    private static final long serialVersionUID = -2550185165626007488L;
    public  static final long TOKEN_VALIDITY = 5*60*60;
    private String secret = "cloudS5";
    public String getUserByToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    //retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
    private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
    public String generateToken(Utilisateur userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getLogin()+" "+userDetails.getId());
	}
	public String generateToken(Admin userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getIdentifiant()+" "+userDetails.getId());
	}
    private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
    
    public Boolean validateToken(String token, Utilisateur userDetails) {
		final String username = getUserByToken(token);
		return (username.equals(userDetails.getLogin()+" "+userDetails.getId()) && !isTokenExpired(token));
	}
	public Boolean validateToken(String token, Admin userDetails) {
		final String username = getUserByToken(token);
		return (username.equals(userDetails.getIdentifiant()+" "+userDetails.getId()) && !isTokenExpired(token));
	}
    
}
