package com.example.apisessoes.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.apisessoes.dto.auth.LoginRequest;
import com.example.apisessoes.dto.auth.LoginResponse;
import com.example.apisessoes.dto.patient.ForgotPasswordResponse;
import com.example.apisessoes.dto.patient.ResetPasswordRequest;
import com.example.apisessoes.entities.Information;
import com.example.apisessoes.repositories.InformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired	
	private InformationRepository repository_information;
	
	@Autowired
    private JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long expiration;
    
    @Autowired	
	private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("apisessoes")
            .subject(authentication.getName())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiration))
            .claim("scope", "PATIENT")
            .build();

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .build();

        String token = jwtEncoder
            .encode(JwtEncoderParameters.from(
                header,
                claims
            ))
            .getTokenValue();

        return new LoginResponse(
            token,
            "Bearer",
            expiration
        );
    }
    
    public ForgotPasswordResponse findPatientForRecovery(String email) {

        Information patient = repository_information.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Paciente não encontrado!"
            ));

        return new ForgotPasswordResponse(patient.getId());
    }
    
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        Information patient = repository_information
            .findById(request.patientId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Paciente não encontrado!"
            ));

        patient.setPassword(
            passwordEncoder.encode(request.newPassword())
        );

        repository_information.save(patient);
    }

	
}
