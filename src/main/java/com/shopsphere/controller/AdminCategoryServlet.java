
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private boolean isAdmin(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"ADMIN".equalsIgnoreCase(
                    String.valueOf(session.getAttribute("userRole")))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) {
            return;
        }

        List<Map<String, Object>> categories = new ArrayList<>();

        String sql = "SELECT category_id, name, description, status "
                   + "FROM categories ORDER BY category_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("category_id", rs.getInt("category_id"));
                category.put("name", rs.getString("name"));
                category.put("description",
                        rs.getString("description"));
                category.put("status", rs.getInt("status"));
                categories.add(category);
            }

            request.setAttribute("categories", categories);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin-categories.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load categories.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try (Connection con = DBConnection.getConnection()) {

            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String description =
                        request.getParameter("description");

                if (name == null || name.isBlank()
                        || name.trim().length() > 100
                        || (description != null
                            && description.length() > 500)) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid category details.");
                    return;
                }

                String sql = "INSERT INTO categories "
                           + "(name, description, status) "
                           + "VALUES (?, ?, 1)";

                try (PreparedStatement ps =
                             con.prepareStatement(sql)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, description);
                    ps.executeUpdate();
                }

            } else if ("delete".equals(action)) {
                int categoryId = Integer.parseInt(
                        request.getParameter("categoryId"));

                // Preserve categories that are already used by products.
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE categories SET status = 0 "
                      + "WHERE category_id = ?")) {
                    ps.setInt(1, categoryId);
                    ps.executeUpdate();
                }

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid action.");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/categories");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid category ID.");
        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to update categories.", e);
        }
    }
}
