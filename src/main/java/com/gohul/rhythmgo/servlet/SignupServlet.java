package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.dto.request.UserCreateRequest;
import com.gohul.rhythmgo.service.UserService;
import com.gohul.rhythmgo.util.JSONUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "SignupServlet", urlPatterns = "/signup")
public class SignupServlet extends BaseServlet {

    private static final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            UserCreateRequest request = JSONUtil.readJson(req, UserCreateRequest.class);
            int userId = userService.createUser(request);
            writeSuccess(res, "User created with ID: "+ userId);

        } catch (Exception ex) {
            writeError(req, res, ex);
        }
    }
}

/*
POST /signup
 */