
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

@WebServlet("/admin/product-edit")
public class AdminProductEditServlet extends HttpServlet {

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

        String id = request.getParameter("id");

        if (id == null || !id.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID.");
            return;
        }

        String sql = "SELECT product_id, category_id, name, brand, "
                   + "description, price, discount, stock, image_url "
                   + "FROM products WHERE product_id = ? AND status = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found.");
                    return;
                }

                request.setAttribute("productId",
                        rs.getInt("product_id"));
                request.setAttribute("categoryId",
                        rs.getInt("category_id"));
                request.setAttribute("name", rs.getString("name"));
                request.setAttribute("brand", rs.getString("brand"));
                request.setAttribute("description",
                        rs.getString("description"));
                request.setAttribute("price", rs.getBigDecimal("price"));
                request.setAttribute("discount",
                        rs.getBigDecimal("discount"));
                request.setAttribute("stock", rs.getInt("stock"));
                request.setAttribute("imageUrl",
                        rs.getString("image_url"));
            }

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin-product-edit.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID.");
        } catch (java.sql.SQLException e) {
            throw new ServletException(
                    "Unable to load product.", e);
        }
    }
}
