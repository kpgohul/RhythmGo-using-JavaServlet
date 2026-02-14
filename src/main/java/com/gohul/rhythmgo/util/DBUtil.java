package com.gohul.rhythmgo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

    private static final String URL = PropertiesReader.get("db.url");
    private static final String USER = PropertiesReader.get("db.username");
    private static final String PASSWORD = PropertiesReader.get("db.password");
    private static final String DRIVER = PropertiesReader.get("db.driver");

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) rs.close();
        } catch (Exception ignored) {}

        try {
            if (ps != null && !ps.isClosed()) ps.close();
        } catch (Exception ignored) {}

        try {
            if (con != null && !con.isClosed()) con.close();
        } catch (Exception ignored) {}
    }

    // Close Connection + PreparedStatement
    public static void close(Connection con, PreparedStatement ps) {
        close(con, ps, null);
    }

    // Close ONLY Connection
    public static void close(Connection con) {
        close(con, null, null);
    }
}
