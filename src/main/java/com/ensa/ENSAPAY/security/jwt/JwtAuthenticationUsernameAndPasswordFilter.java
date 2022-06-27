package com.ensa.ENSAPAY.security.jwt;

import com.ensa.ENSAPAY.entities.User;
import com.ensa.ENSAPAY.security.auth.ApplicationUser;
import com.ensa.ENSAPAY.security.auth.ApplicationUserDaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Date;

public class JwtAuthenticationUsernameAndPasswordFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationUsernameAndPasswordFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {

        UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usernameAndPasswordAuthenticationRequest.getUsername(),
                usernameAndPasswordAuthenticationRequest.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        return authenticate;
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor("securesecuresecuresecuresecuresecuresecuresecure".getBytes()))
                .compact();

        String username = authResult.getName();
        ApplicationUser user = (ApplicationUser) authResult.getPrincipal();

        response.addHeader("Authorization", "Bearer "+token);
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper= new ObjectMapper();
        JSONObject json = new JSONObject();
        json.put("token",response.getHeader("Authorization"));
        json.put("passwordChanged",user.getUser().isPasswordChanged());
        out.print(json.toString());
        out.flush();

    }
}
