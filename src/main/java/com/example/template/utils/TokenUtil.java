package com.example.template.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {

    private static final long EXPIRATION_TIME = 60000;
    private static final int KEY_SIZE = 2048;

    private static Algorithm algorithm;
    private static Map<String, RSAKey> keys;

    private static String privateRSA;
    private static String publicRSA;

    @Autowired
    private void setRSAKeys(Environment env) {
        privateRSA = env.getProperty("rsa.privateKey");
        publicRSA = env.getProperty("rsa.publicKey");
    }

    public static void initializeTokenUtil() {
        Map<String, RSAKey> keys = new HashMap<>();
        keys.put("public", getPublicKeyFromString(publicRSA));
        keys.put("private", getPrivateKeyFromString(privateRSA));
        algorithm = Algorithm.RSA256((RSAPublicKey) keys.get("public"), (RSAPrivateKey) keys.get("private"));
    }

    /**
     *
     * @param payload data que va a ser almacenada en el jwt
     * @return jwt creado a partir de la data y el algoritmo establecido
     */
    public static String generateToken(Map<String, String> payload, int time) {
        return JWT.create().withPayload(payload).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME * time)).sign(algorithm);
    }

    /**
     *
     * @param token token recibido del usuario
     * @return estado de la validacion del token
     */
    public static Map<String, Claim> validateToken(String token) {
        JWTVerifier jwt = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwt.verify(token);
        return decodedJWT.getClaims();
    }

    /** !!!! SOLO USAR EN CASO DE NECESITAR CLAVES RSA
     * Genera las claves rsa necesarias para generar el algoritmo de encripcion
     * @return claves rsa
     */
    @Deprecated
    private static Map<String, RSAKey> generateRSAKey() {
        Map<String, RSAKey> keys = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            System.out.println(publicKeyStr);
            System.out.println(privateKeyStr);

            keys.put("public", (RSAKey) publicKey);
            keys.put("private", (RSAKey) privateKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return keys;
    }

    public static RSAPublicKey getPublicKeyFromString(String publicKeyStr) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static RSAPrivateKey getPrivateKeyFromString(String privateKeyStr) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
