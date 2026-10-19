
package com.shopsphere.controller;

import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.length() < 8
                || password.length() > 72) {
            response.sendError(400, "Please enter valid registration details.");
            return;
        }

       String sql = "INSERT INTO users (name, email, password, role) "
           + "VALUES (?, ?, ?, 'CUSTOMER')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

           String hashedPassword = BCrypt.hashpw(
    password, BCrypt.gensalt(12)
);

statement.setString(1, name.trim());
statement.setString(2, email.trim().toLowerCase(java.util.Locale.ROOT));
statement.setString(3, hashedPassword);

            statement.executeUpdate();

            response.sendRedirect(
                request.getContextPath() + "/login?registered=true");

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                response.sendError(409, "This email may already be registered.");
            } else {
                throw new ServletException("Registration failed.", e);
            }
        }
    }
}
