package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.dto.request.LoginRequest;
import com.gohul.rhythmgo.dto.response.LoginResponse;
import com.gohul.rhythmgo.service.UserService;
import com.gohul.rhythmgo.util.JSONUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends BaseServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            LoginRequest login = JSONUtil.readJson(req, LoginRequest.class);
            String token = userService.login(login.getEmail(), login.getPassword());
            writeSuccess(res, new LoginResponse(token));

        } catch (Exception ex) {
            writeError(req, res, ex);
        }
    }
}

/*
@WebServlet(name = "LoginServlet", urlPatterns = "/login")
 */