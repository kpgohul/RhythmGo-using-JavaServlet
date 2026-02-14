package com.gohul.rhythmgo.config;

import java.sql.Connection;

public class ConnectionManager {

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static void setConnection(Connection connection) {
        connectionHolder.set(connection);
    }

    public static Connection getConnection() {
        return connectionHolder.get();
    }

    public static void clear() {
        connectionHolder.remove();
    }
}
