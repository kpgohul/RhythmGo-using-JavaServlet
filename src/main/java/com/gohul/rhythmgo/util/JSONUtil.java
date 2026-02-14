package com.gohul.rhythmgo.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class JSONUtil {

    private static final Gson gson = new Gson();

    public static String readBody(HttpServletRequest req) throws IOException {
        return req.getReader()
                .lines()
                .reduce("", (a, b) -> a + b);
    }

    public static JsonObject parse(String jsonString) {
        return JsonParser.parseString(jsonString).getAsJsonObject();
    }

    public static JsonObject readJson(HttpServletRequest req) throws IOException {
        return parse(readBody(req));
    }

    public static <T> T readJson(HttpServletRequest req, Class<T> clazz) throws IOException {
        String json = readBody(req);
        return gson.fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

}
