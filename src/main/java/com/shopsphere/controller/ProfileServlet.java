
package com.shopsphere.controller;

import com.shopsphere.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String sql = "SELECT name, email, mobile, address, city, state, pincode "
                   + "FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("name", rs.getString("name"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("mobile", rs.getString("mobile"));
                    request.setAttribute("address", rs.getString("address"));
                    request.setAttribute("city", rs.getString("city"));
                    request.setAttribute("state", rs.getString("state"));
                    request.setAttribute("pincode", rs.getString("pincode"));
                }
            }

            request.getRequestDispatcher(
                "/WEB-INF/views/profile.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load customer profile.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String name = request.getParameter("name");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");

        if (name == null || name.trim().isEmpty()
                || mobile == null || mobile.trim().isEmpty()) {
            response.sendRedirect(
                request.getContextPath() + "/profile?error=required"
            );
            return;
        }

        String sql = "UPDATE users SET name = ?, mobile = ?, address = ?, "
                   + "city = ?, state = ?, pincode = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, mobile.trim());
            ps.setString(3, address);
            ps.setString(4, city);
            ps.setString(5, state);
            ps.setString(6, pincode);
            ps.setInt(7, userId);

            ps.executeUpdate();

            session.setAttribute("userName", name.trim());

            response.sendRedirect(
                request.getContextPath() + "/profile?success=1"
            );

        } catch (SQLException e) {
            throw new ServletException("Unable to update customer profile.", e);
        }
    }
}
