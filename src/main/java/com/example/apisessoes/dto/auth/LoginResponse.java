package com.example.apisessoes.dto.auth;

public record LoginResponse(
		String accessToken,

	    String tokenType,

	    long expiresIn
	    ) {

}
