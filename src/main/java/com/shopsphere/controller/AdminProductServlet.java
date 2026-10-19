
package com.shopsphere.controller;

import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private boolean isAdmin(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"ADMIN".equalsIgnoreCase(
                    String.valueOf(session.getAttribute("userRole")))) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required.");
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

    java.util.List<java.util.Map<String, Object>> products =
            new java.util.ArrayList<>();

    String sql = "SELECT p.*, c.name AS category_name "
               + "FROM products p "
               + "LEFT JOIN categories c "
               + "ON p.category_id = c.category_id "
               + "ORDER BY p.product_id DESC";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            java.util.Map<String, Object> product =
                    new java.util.HashMap<>();

            product.put("product_id", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("category_name",
                    rs.getString("category_name"));
            product.put("price", rs.getBigDecimal("price"));
            product.put("stock", rs.getInt("stock"));
            product.put("status", rs.getInt("status"));

            products.add(product);
        }

        request.setAttribute("products", products);

        request.getRequestDispatcher(
                "/WEB-INF/views/admin-products.jsp")
                .forward(request, response);

    } catch (SQLException e) {
        throw new ServletException(
                "Unable to load admin products.", e);
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

            if ("delete".equals(action)) {
                int productId = Integer.parseInt(
                        request.getParameter("productId"));

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE products SET status = 0 "
                      + "WHERE product_id = ?")) {
                    ps.setInt(1, productId);
                    ps.executeUpdate();
                }

            } else if ("add".equals(action)) {
                String name = request.getParameter("name");
                String brand = request.getParameter("brand");
                String description = request.getParameter("description");
                String imageUrl = request.getParameter("imageUrl");

                int categoryId = Integer.parseInt(
                        request.getParameter("categoryId"));
                int stock = Integer.parseInt(
                        request.getParameter("stock"));

                BigDecimal price = new BigDecimal(
                        request.getParameter("price"));
                BigDecimal discount = new BigDecimal(
                        request.getParameter("discount"));

                if (name == null || name.isBlank()
                        || price.signum() <= 0
                        || discount.signum() < 0
                        || discount.compareTo(new BigDecimal("100")) > 0
                        || stock < 0) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid product details.");
                    return;
                }

                String sql = "INSERT INTO products "
                        + "(category_id, name, brand, description, "
                        + "price, discount, stock, image_url, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";

                try (PreparedStatement ps =
                             con.prepareStatement(sql)) {

                    ps.setInt(1, categoryId);
                    ps.setString(2, name.trim());
                    ps.setString(3, brand);
                    ps.setString(4, description);
                    ps.setBigDecimal(5, price);
                    ps.setBigDecimal(6, discount);
                    ps.setInt(7, stock);
                    ps.setString(8, imageUrl);

                    ps.executeUpdate();
                }

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid action.");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/products");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid number entered.");
        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to update products.", e);
        }
    }
}
