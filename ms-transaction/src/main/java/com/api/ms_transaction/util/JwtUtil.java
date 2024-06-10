package com.api.ms_transaction.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtUtil {

    public static String extractPreferredUsername(String jwtString) {
        try {

            PublicKey publicKey = getPublicKeyFromPEM(System.getenv("PUBLIC_KEY"));
            // Verificar el token JWT
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwtString);

            // Extraer el nombre de usuario del token JWT
            Claims claims = claimsJws.getBody();
            return claims.get("preferred_username", String.class); // "preferred_username" puede variar dependiendo de cómo se haya definido en el token
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PublicKey getPublicKeyFromPEM(String publicKeyPEM) throws Exception {
        // Quitar los encabezados y retornos de carro
        // Decodificar la clave pública del formato Base64
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
        // Crear objeto PublicKey a partir de los bytes decodificados
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}

