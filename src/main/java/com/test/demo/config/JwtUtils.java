/**
 * parser()已有更新的版本 parserBuilder()
 */


package com.test.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

/**
 * validating JWT Token , 參考網上資料
 */
@Component
public class JwtUtils {

    //加在JWT後面的secret key string , 可自由更換
    //private String jwtSigningKey = "secret7983165fvvvf6sdvbghjuy989877gggdfbbyk4yiusad23e6";
    private static final long EXPIRATION_TIME = 1 * 60 * 1000;  //一分鐘

    /*2/6新增 */
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String jwtSigningKey = Jwts.builder().setSubject("Joe").signWith(key).compact();


    // public static SecretKey createSecretKey(SignatureAlgorithm signatureAlgorithm){
    //     return Keys.secretKeyFor(signatureAlgorithm);
    // }

    // public static SecretKey decodSecretKey(byte[] SecretKey){
    //     return Keys.hmacShaKeyFor(SecretKey);
    // }


     //產生的key為隨機合格的key長度 , 再存成String類型(base64key)
      //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //  String base64Key = Encoders.BASE64.encode(key.getEncoded());

     

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);}
    
        
    public boolean hasClaim(String token,String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName)!=null;
    }

    //用以將token中取出我們要的資訊(usename , body)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    
    private Claims extractAllClaims(String token) {
        return 
            Jwts.parser()
             .setSigningKey(jwtSigningKey)
            .parseClaimsJws(token).getBody();

/////////////////////////////////////////////////////////////////////////////////


// Jwts.parserBuilder()
//          //.setSigningKey(jwtSigningKey)
//          .setSigningKey(key)
//          //.requireIssuer("https://issuer.example.com")
//          .build()
//          //.parseClaimsJws(token).getBody();
//          .parseClaimsJws(jwtSigningKey).getBody();

            
/////////////////////////////////////////////////////////////////////////////////

          
             
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

/* return createToken(claims, userDetails.getUsername()); 原為 return createToken(claims, userDetails);*/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

/* return createToken(claims, userDetails.getUsername()); 原為 return createToken(claims, userDetails);*/
    public String generateToken(UserDetails userDetails,Map<String,Object> claims){
        return createToken(claims, userDetails);
    }
/* 
 * 原為
 *  private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return 
                Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256,jwtSigningKey)
                .compact();
    }
*/
    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return 
                Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // .signWith(SignatureAlgorithm.HS256,jwtSigningKey)
                //.signWith(key)
                .compact();
    }

    // private String createToken(Map<String, Object> claims, UserDetails userDetails) {
    //     return 
    //     Jwts.builder().setClaims(claims)
    //             .setSubject(userDetails.getUsername())
    //             .claim("authorities", userDetails.getAuthorities())
    //             .setIssuedAt(new Date(System.currentTimeMillis()))
    //             .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
    //             .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
    //             .compact();

    // }

    //token中的username和userDetails中的username相同, 且isTokenExpired尚未到期的話 , isTokenValid回復true
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
}
