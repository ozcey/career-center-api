package com.careercenter.security;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Slf4j
@Component
public class JWTTokenProvider {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private int jwtExpiration;

	public String generateJWTToken(Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		return Jwts.builder().setSubject(principal.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJWTToken(String jwt) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
			log.error("Invalid JWT token - {}", e);
		}
		return false;
	}
}
