package com.gohul.rhythmgo.servlet;

import com.gohul.rhythmgo.dto.response.ErrorResponse;
import com.gohul.rhythmgo.exception.*;
import com.gohul.rhythmgo.util.JSONUtil;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class BaseServlet extends HttpServlet {

    protected JsonObject getJson(HttpServletRequest req) throws IOException {
        return JSONUtil.readJson(req);
    }

    protected <T> void writeSuccess(HttpServletResponse res, T data)
            throws IOException {

        res.setContentType("application/json");
        res.getWriter().write(JSONUtil.toJson(data));
    }

    protected void writeError(HttpServletRequest req, HttpServletResponse res, Exception ex) throws IOException {

        String errorCode;
        int status;

        if (ex instanceof ValidationException) {
            status = 400;
            errorCode = "VALIDATION_ERROR";

        } else if (ex instanceof AuthenticationException) {
            status = 401;
            errorCode = "AUTH_ERROR";

        } else if (ex instanceof ResourceNotFoundException) {
            status = 404;
            errorCode = "NOT_FOUND";

        } else if (ex instanceof ResourceAlreadyExistException) {
            status = 409;   // Conflict
            errorCode = "RESOURCE_ALREADY_EXISTS";

        } else if (ex instanceof GlobalException) {
            status = 500;
            errorCode = "UNEXPECTED_ERROR";

        } else {
            status = 500;
            errorCode = "INTERNAL_SERVER_ERROR";
        }

        ErrorResponse api = new ErrorResponse(
                req.getRequestURI(),        // apiPath
                errorCode,                  // errorCode
                ex.getMessage(),            // errorMessage
                LocalDateTime.now().toString() // errorTime
        );

        res.setStatus(status);
        res.setContentType("application/json");
        res.getWriter().write(JSONUtil.toJson(api));
    }



    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        try {
            super.service(req, res);
        } catch (Exception ex) {
            writeError(req, res, ex);
        }
    }
}
