package com.gohul.rhythmgo.filter;

import com.gohul.rhythmgo.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


//@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

    private static final UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        if (path.equals("/signup") || path.equals("/login")) {
            chain.doFilter(req, res);
            return;
        }

        List<String> roles = (List<String>) request.getAttribute("roles");

        if (roles == null) {
            sendForbidden(response);
            return;
        }

        String method = request.getMethod();

        if (!isAuthorized(path, method, roles)) {
            sendForbidden(response);
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isAuthorized(String path, String method, List<String> roles) {

        if (roles.contains("ADMIN")) {
            return true;
        }

        if (roles.contains("USER")) {

            if (path.startsWith("/artist") && !method.equals("GET"))
                return false;

            if (path.startsWith("/songs") &&
                    (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")))
                return false;

//            if (path.startsWith("/users") && method.equals("DELETE"))
//                return false;

            return true;
        }

        return false;
    }

    private void sendForbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Access denied\"}");
    }
}
