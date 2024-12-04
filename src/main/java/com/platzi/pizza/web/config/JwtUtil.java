package com.platzi.pizza.web.config;

import com.auth0.jwt.JWT; // Importa la clase JWT para crear y manipular tokens JWT
import com.auth0.jwt.algorithms.Algorithm; // Importa la clase Algorithm para definir el algoritmo de firma
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component; // Marca esta clase como un componente de Spring para que sea gestionada por el contenedor de Spring

import java.util.Date; // Proporciona funcionalidades para trabajar con fechas
import java.util.concurrent.TimeUnit; // Permite trabajar con unidades de tiempo

@Component // Declara esta clase como un componente de Spring para ser utilizada como un bean en el contexto de la aplicación
public class JwtUtil {
    // Constante para la clave secreta utilizada en la firma de los tokens
    private static final String SECRET_KEY = "pl4tzi_pizz4";

    // Configuración del algoritmo de firma HMAC256 utilizando la clave secreta
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    /**
     * Crea un token JWT basado en el nombre de usuario.
     *
     * @param username El nombre de usuario que será incluido en el token.
     * @return Un token JWT firmado.
     */
    public String create(String username) {
        return JWT.create() // Inicia la construcción del token JWT
                .withSubject(username) // Define el "subject" (sujeto) del token como el nombre de usuario
                .withIssuer("platzi-pizza") // Establece el "issuer" (emisor) del token
                .withIssuedAt(new Date()) // Añade la fecha de emisión del token
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
                // Establece la fecha de expiración del token como 15 días después del momento actual
                .sign(ALGORITHM); // Firma el token utilizando el algoritmo configurado
    }

    public boolean isValid(String jwt){
        try {
            JWT.require(ALGORITHM).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e){
            return false;
        }
    }

    public String getUsername(String jwt){
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}