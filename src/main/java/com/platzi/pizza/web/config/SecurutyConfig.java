package com.platzi.pizza.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean; // Permite definir métodos que producen beans administrados por Spring
import org.springframework.context.annotation.Configuration; // Marca esta clase como una configuración de Spring
import org.springframework.http.HttpMethod; // Enum para los métodos HTTP (GET, POST, PUT, etc.)
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Habilita la seguridad basada en anotaciones a nivel de métodos
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Proporciona una API para configurar la seguridad HTTP
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementación del algoritmo BCrypt para encriptar contraseñas
import org.springframework.security.crypto.password.PasswordEncoder; // Interfaz para encriptar contraseñas
import org.springframework.security.web.SecurityFilterChain; // Representa la cadena de filtros de seguridad
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marca esta clase como una configuración de seguridad
@EnableMethodSecurity(securedEnabled = true) // Habilita la seguridad basada en anotaciones (@Secured, @PreAuthorize)
public class SecurutyConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurutyConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     *
     * @param http El objeto HttpSecurity que permite configurar la seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desactiva la protección CSRF (Cross-Site Request Forgery)
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() // Comienza la configuración de las reglas de autorización
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "CUSTOMER")
                // Permite el acceso a rutas de "customers" solo a usuarios con los roles "ADMIN" o "CUSTOMER"
                .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER")
                // Permite el acceso a GET en "pizzas" a los roles "ADMIN" y "CUSTOMER"
                .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN")
                // Permite el acceso a POST en "pizzas" solo a "ADMIN"
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")
                // Permite el acceso a cualquier método PUT solo a "ADMIN"
                .requestMatchers("/api/orders/random").hasAnyAuthority("random_order")
                // Permite el acceso a "/orders/random" solo a usuarios con la autoridad "random_order"
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole("ADMIN")
                // Permite el acceso a GET en "orders" solo a "ADMIN"
                .anyRequest().authenticated()
                // Requiere autenticación para cualquier otra solicitud
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construye y devuelve la cadena de filtros configurada
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    /**
     * Define el bean para el encriptador de contraseñas.
     *
     * @return Un PasswordEncoder basado en BCrypt.
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(); // Retorna una implementación de BCrypt para encriptar contraseñas
    }
}