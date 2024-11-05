package com.pratik.insta.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(JwtSecurityConfig.HEADER);

		if (jwt != null) {
			try {
				jwt = jwt.substring(7);
				SecretKey key = Keys.hmacShaKeyFor(JwtSecurityConfig.JWT_KEY.getBytes());
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

				logger.info("Validating JWT for user: {}", username);
				logger.info("Authorities: {}", auths);
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, auths);
				SecurityContextHolder.getContext().setAuthentication(auth);

			} catch (Exception e) {
				logger.error("Invalid token: {}", e.getMessage());
				throw new BadCredentialsException("Invalid token...");
			}
		}

		filterChain.doFilter(request, response);
	}

	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
		String path = req.getServletPath();
		return path.startsWith("/actuator") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
				|| path.equals("/signin");

	}

}
