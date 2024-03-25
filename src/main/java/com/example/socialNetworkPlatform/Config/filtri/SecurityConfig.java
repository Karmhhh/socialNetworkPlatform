package com.example.socialNetworkPlatform.Config.filtri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.socialNetworkPlatform.Services.ProfileService;
// import com.example.socialNetworkPlatform.Config.filtri.JwtAuthFilter;

@Configuration
@Order(1)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /*
     * Filtro personalizzato ('JwtAuthFilter') utilizzato per l'autenticazione JWT
     */

    private JwtAuthFilter authFilter;

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new ProfileService();
    }

    /*
     * Questo bean configura le regole di sicurezza. In particolare, disabilita
     * CSRF.
     * Vengono specificati quali endpoint devono essere necessariamente contattati
     * con autenticazione.
     * Inoltre, specifica l'Authentication Provider per la gestione
     * dell'autenticazione
     */

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/public/**").permitAll())

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/auth/**").authenticated())

                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated())

                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Questo bean configura l'Authentication Provider che è il responsabile della
     * gestione di autenticazione.
     */

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /*
     * L'Authentication Manager è necessario per una corretta autenticazione. È
     * configurato utilizzando AuthenticationConfiguration, restituendo un oggetto
     * di tipo AuthenticationManager
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Autowired
    public void setJwtAuthFilter(@Lazy JwtAuthFilter authFilter) {
        this.authFilter = authFilter;
    }

}