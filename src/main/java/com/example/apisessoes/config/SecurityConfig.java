package com.example.apisessoes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	 private final javax.crypto.SecretKey jwtSecretKey = 
		        new javax.crypto.spec.SecretKeySpec(
		            "sua-chave-secreta-estavel-com-pelo-menos-32-caracteres".getBytes(), 
		            "HmacSHA256"
		        );
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(
	            HttpSecurity http) throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())

	            .sessionManagement(session ->
	                session.sessionCreationPolicy(
	                    SessionCreationPolicy.STATELESS
	                )
	            )

	            .authorizeHttpRequests(auth -> auth

	                .requestMatchers(
	                    HttpMethod.POST,
	                    "/patients/register",
	                    "/auth/login"
	                ).permitAll()

	                .anyRequest().authenticated()
	            )

	            .oauth2ResourceServer(oauth2 ->
	                oauth2.jwt(Customizer.withDefaults())
	            );

	        return http.build();
	    }
	 
	 @Bean
	 public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	 }
	 
	  @Bean
	  public AuthenticationManager authenticationManager(
	     AuthenticationConfiguration configuration)
	     throws Exception {

	     return configuration.getAuthenticationManager();
	  }
	  
	  
	    @Bean
	    public JwtEncoder jwtEncoder() {
	        
	        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getEncoded()));
	    }

	    @Bean
	    public JwtDecoder jwtDecoder() {
	        return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
	    }
}
