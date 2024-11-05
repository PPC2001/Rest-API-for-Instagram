package com.pratik.insta.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratik.insta.config.JwtSecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtTokenProvider {

//	@Autowired
//	private JwtSecurityConfig jwtSecurityConfig;

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	/**
	 * This method extracts the claims (specifically the username) from a JWT token.
	 * It validates the token using the secret key and then parses the claims.
	 * 
	 * @param token the JWT token string
	 * @return JwtTokenClaims object containing the username extracted from the
	 *         token
	 */
	public JwtTokenClaims getClaimsFromToken(String token) {
		logger.info("Starting to parse JWT token...");
		SecretKey key = Keys.hmacShaKeyFor(JwtSecurityConfig.JWT_KEY.getBytes());
		logger.debug("Secret key successfully generated for JWT parsing");

		// Parse the token to extract claims (header, payload, and signature)
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		String username = String.valueOf(claims.get("username"));
		logger.info("JwtTokenProvider :: Extracted username from JWT: {}", username);

		JwtTokenClaims jwtTokenClaims = new JwtTokenClaims();
		jwtTokenClaims.setUsername(username);

		return jwtTokenClaims;
	}
}
