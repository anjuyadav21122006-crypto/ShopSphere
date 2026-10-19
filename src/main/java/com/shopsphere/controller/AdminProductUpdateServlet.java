
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
import java.sql.SQLException;

@WebServlet("/admin/product-update")
public class AdminProductUpdateServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int productId = Integer.parseInt(
                    request.getParameter("productId"));
            int categoryId = Integer.parseInt(
                    request.getParameter("categoryId"));
            int stock = Integer.parseInt(
                    request.getParameter("stock"));

            String name = request.getParameter("name");
            String brand = request.getParameter("brand");
            String description = request.getParameter("description");
            String imageUrl = request.getParameter("imageUrl");

            BigDecimal price = new BigDecimal(
                    request.getParameter("price"));
            BigDecimal discount = new BigDecimal(
                    request.getParameter("discount"));

            if (productId <= 0 || categoryId <= 0
                    || name == null || name.isBlank()
                    || name.length() > 150
                    || stock < 0
                    || price.signum() <= 0
                    || discount.signum() < 0
                    || discount.compareTo(new BigDecimal("100")) > 0
                    || (brand != null && brand.length() > 100)
                    || (imageUrl != null && imageUrl.length() > 500)) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product details.");
                return;
            }

            String sql = "UPDATE products SET category_id = ?, "
                    + "name = ?, brand = ?, description = ?, "
                    + "price = ?, discount = ?, stock = ?, image_url = ? "
                    + "WHERE product_id = ? AND status = 1";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, categoryId);
                ps.setString(2, name.trim());
                ps.setString(3, brand);
                ps.setString(4, description);
                ps.setBigDecimal(5, price);
                ps.setBigDecimal(6, discount);
                ps.setInt(7, stock);
                ps.setString(8, imageUrl);
                ps.setInt(9, productId);

                int updated = ps.executeUpdate();

                if (updated == 0) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Active product not found.");
                    return;
                }
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/products");

        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please enter valid product details.");
        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to update product.", e);
        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid price or discount.");
        }
    }
}
