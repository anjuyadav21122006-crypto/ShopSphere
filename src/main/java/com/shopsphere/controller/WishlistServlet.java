
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

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private Integer getUserId(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return null;
        }

        Object id = session.getAttribute("userId");

        if (!(id instanceof Number)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        return ((Number) id).intValue();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserId(request, response);
        if (userId == null) return;

        List<Map<String, Object>> products = new ArrayList<>();

        String sql =
            "SELECT p.product_id, p.name, p.brand, p.price, " +
            "p.stock, p.image_url, w.wishlist_id " +
            "FROM wishlist w " +
            "JOIN products p ON w.product_id = p.product_id " +
            "WHERE w.user_id = ? AND p.status = 1 " +
            "ORDER BY w.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> product = new HashMap<>();

                    product.put("productId", rs.getInt("product_id"));
                    product.put("name", rs.getString("name"));
                    product.put("brand", rs.getString("brand"));
                    product.put("price", rs.getBigDecimal("price"));
                    product.put("stock", rs.getInt("stock"));
                    product.put("imageUrl", rs.getString("image_url"));
                    product.put("wishlistId", rs.getInt("wishlist_id"));

                    products.add(product);
                }
            }

            request.setAttribute("wishlistProducts", products);

            request.getRequestDispatcher(
                "/WEB-INF/views/wishlist.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to load wishlist.", e
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserId(request, response);
        if (userId == null) return;

        String action = request.getParameter("action");

        try (Connection con = DBConnection.getConnection()) {

            if ("add".equals(action)) {
                String productIdText =
                    request.getParameter("productId");

                int productId;

                try {
                    productId = Integer.parseInt(productIdText);
                    if (productId <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product ID."
                    );
                    return;
                }

                String sql =
                    "INSERT INTO wishlist (user_id, product_id) " +
                    "SELECT ?, product_id FROM products " +
                    "WHERE product_id = ? AND status = 1 " +
                    "ON DUPLICATE KEY UPDATE " +
                    "wishlist_id = wishlist_id";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    ps.setInt(2, productId);

                    int rows = ps.executeUpdate();

                    if (rows == 0) {
                        response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found."
                        );
                        return;
                    }
                }

            } else if ("remove".equals(action)) {
                String wishlistIdText =
                    request.getParameter("wishlistId");

                int wishlistId;

                try {
                    wishlistId = Integer.parseInt(wishlistIdText);
                    if (wishlistId <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid wishlist item."
                    );
                    return;
                }

                String sql =
                    "DELETE FROM wishlist " +
                    "WHERE wishlist_id = ? AND user_id = ?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, wishlistId);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }

            } else {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid wishlist action."
                );
                return;
            }

            response.sendRedirect(
                request.getContextPath() + "/wishlist"
            );

        } catch (SQLException e) {
            throw new ServletException(
                "Unable to update wishlist.", e
            );
        }
    }
}
