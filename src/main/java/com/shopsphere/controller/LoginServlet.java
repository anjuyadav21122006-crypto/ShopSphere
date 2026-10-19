
package com.shopsphere.controller;

import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    
@Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    request.getRequestDispatcher(
        "/WEB-INF/views/login.jsp"
    ).forward(request, response);
}


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isBlank()
                || password == null || password.isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/login?error=true");
            return;
        }

       String sql = "SELECT user_id, name, email, password, role "
           + "FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(
                1, email.trim().toLowerCase(java.util.Locale.ROOT));

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    String storedHash = result.getString("password");

                    if (BCrypt.checkpw(password, storedHash)) {
                        HttpSession oldSession = request.getSession(false);

                        if (oldSession != null) {
                            oldSession.invalidate();
                        }

                        HttpSession session = request.getSession(true);
                        session.setAttribute("userId",
                                result.getInt("user_id"));
                        session.setAttribute("userName",
                                result.getString("name"));
                        session.setAttribute("userEmail",
                                result.getString("email"));
                                session.setAttribute("userRole",
        result.getString("role"));

                        session.setMaxInactiveInterval(30 * 60);

                        response.sendRedirect(
                            request.getContextPath() + "/products");
                        return;
                    }
                }
            }

            response.sendRedirect(
                request.getContextPath() + "/login?error=true");

        } catch (SQLException e) {
            throw new ServletException("Login failed.", e);
        }
    }
}
