package com.example.apisessoes.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;
	
	@Value("${api.security.token.secret}")
	private String secret;	
	

    @Bean
    public SecretKey jwtSecretKey() {
    	 byte[] keyBytes = Base64.getUrlDecoder().decode(secret);

    	    if (keyBytes.length < 32) {
    	        throw new IllegalArgumentException(
    	            "A chave JWT precisa ter pelo menos 32 bytes."
    	        );
    	    }

    	    return new SecretKeySpec(
    	        keyBytes,
    	        "HmacSHA256"
    	    );
    }
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(
	            HttpSecurity http) throws Exception {

	        http
	            .cors(cors -> cors.configurationSource(corsConfigurationSource))

	            .csrf(csrf -> csrf.disable())

	            .sessionManagement(session ->
	                session.sessionCreationPolicy(
	                    SessionCreationPolicy.STATELESS
	                )
	            )

	            .authorizeHttpRequests(auth -> auth

	                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

	                .requestMatchers(
	                    HttpMethod.GET,
	                    "/especialities/**"
	                ).permitAll()

	                .requestMatchers(
	                    HttpMethod.POST,
	                    "/patient/registersessions"
	                ).permitAll()

	                .requestMatchers(
	                    HttpMethod.POST,	                   
	                    "/auth/login"
	                ).permitAll()
	                .requestMatchers("/patient/me").authenticated()
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
	    public JwtEncoder jwtEncoder(SecretKey secretKey) {

	        return new NimbusJwtEncoder(
	            new ImmutableSecret<>(secretKey)
	        );
	    }

	    @Bean
	    public JwtDecoder jwtDecoder(SecretKey secretKey) {

	        return NimbusJwtDecoder
	            .withSecretKey(secretKey)
	            .macAlgorithm(MacAlgorithm.HS256)
	            .build();
	    }
	    
	  
}
