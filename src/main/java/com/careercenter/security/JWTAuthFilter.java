package com.careercenter.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTTokenProvider jwtTokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getToken(request);
			if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJWTToken(jwt)) {
				String username = jwtTokenProvider.getUsername(jwt);
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Could not set authentication in security context", e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		return StringUtils.hasText(header) && header.startsWith("Bearer ") ? header.substring(7) : null;
	}

}
