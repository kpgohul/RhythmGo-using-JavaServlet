package com.gohul.rhythmgo.filter;


import com.gohul.rhythmgo.config.ConnectionManager;
import com.gohul.rhythmgo.util.DBUtil;
import jakarta.servlet.*;

import java.io.IOException;
import java.sql.Connection;

public class ConnectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        try (Connection connection = DBUtil.getConnection()) {

            connection.setAutoCommit(false);
            ConnectionManager.setConnection(connection);
            chain.doFilter(req, res);
            connection.commit();

        } catch (Exception e) {

            Connection connection = ConnectionManager.getConnection();
            if (connection != null) {
                try { connection.rollback(); } catch (Exception ignored) {}
            }
            throw new ServletException(e);

        } finally {
            ConnectionManager.clear();
        }
    }
}
