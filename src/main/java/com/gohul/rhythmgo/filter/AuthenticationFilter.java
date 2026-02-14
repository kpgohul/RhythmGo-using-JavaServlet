package com.gohul.rhythmgo.filter;

import com.gohul.rhythmgo.dao.UserDAO;
import com.gohul.rhythmgo.util.JSONUtil;
import com.gohul.rhythmgo.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final UserDAO userDAO = new UserDAO();


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        if (path.equals("/signup") || path.equals("/login")) {
            chain.doFilter(req, res);
            return;
        }

        String token = JWTUtil.extractBearerToken(request);

        if (token == null) {
            sendUnauthorized(response, "Missing or invalid Authorization header");
            return;
        }

        try {
            Jws<Claims> jws = JWTUtil.validateToken(token);
            Claims claims = jws.getBody();

            Integer userId = claims.get("userId", Integer.class);
            String email = claims.getSubject();

            Object rolesObj = claims.get("roles");
            List<String> roles = null;
            if (rolesObj instanceof List<?>) {
                roles = ((List<?>) rolesObj)
                        .stream()
                        .map(Object::toString)
                        .toList();
            }


            if (userId == null || email == null) {
                sendUnauthorized(response, "Invalid token payload");
                return;
            }

            if(!userDAO.existsById(userId)){
                log.warn("Token validation failed | userId={} | reason=User not found in database", userId);
                sendUnauthorized(response, "Invalid or expired token");
                return;
            }

            request.setAttribute("userId", userId);
            request.setAttribute("email", email);
            request.setAttribute("roles", (roles != null)? roles : List.of("USER"));

            chain.doFilter(req, res);

        } catch (Exception e) {
            log.error("Token validation failed: ex: {}", e.getMessage());
            sendUnauthorized(response, "Invalid or expired JWT");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String json = JSONUtil.toJson(Map.of("error", message));

        response.getWriter().write(json);
    }
}
