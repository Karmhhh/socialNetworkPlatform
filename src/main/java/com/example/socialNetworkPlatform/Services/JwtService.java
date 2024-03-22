package com.example.socialNetworkPlatform.Services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Map;
import java.util.function.Function;
import java.util.HashMap;
import java.security.Key;
import java.util.Date;

/*
 * Questa classe gestisce la creazione e la validazione di token JWT (Json Web Token)
 */

@Component
public class JwtService {

    /*
     * Chiave con il quale verranno creati i vari token
     */
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /*
     * Questo metodo genera un token JWT utilizzando il metodo createToken
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /*
     * Il metodo createToken crea un token JWT impostando le informazioni come
     * claim, soggetto, data di emissione e data di scadenza + firma del token con
     * chiave segreta.
     */

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 720))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /*
     * Restituisce la chiave di firma utilizzata per firmare e verificare i token
     */

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * Estrae l'username dal token
     */

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Estrae la data di scadenza dal token
     */

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
     * Estrae una generica claim dal token
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
     * Estrae tutte le claim dal token
     */

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    /*
     * Controllo sulla scadenza del token
     */

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
     * Controllo sulla validità del token
     */

    public Boolean validateToken(String token, UserDetails data) {
        final String username = extractUsername(token);
        return (username.equals(data.getUsername()) && !isTokenExpired(token));
    }

}