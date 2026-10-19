
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
import java.sql.SQLException;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("userId") instanceof Number)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = ((Number) session.getAttribute("userId")).intValue();
        int productId;
        int rating;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            rating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product or rating.");
            return;
        }

        if (productId <= 0 || rating < 1 || rating > 5) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Rating must be between 1 and 5.");
            return;
        }

        String reviewText = request.getParameter("reviewText");
        if (reviewText != null && reviewText.length() > 2000) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Review must be 2000 characters or fewer.");
            return;
        }

        String sql = """
                INSERT INTO product_reviews
                    (user_id, product_id, rating, review_text)
                SELECT ?, p.product_id, ?, ?
                FROM products p
                WHERE p.product_id = ? AND p.status = 1
                ON DUPLICATE KEY UPDATE
                    rating = VALUES(rating),
                    review_text = VALUES(review_text)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, rating);
            ps.setString(3, reviewText == null ? "" : reviewText.trim());
            ps.setInt(4, productId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Product not found.");
                return;
            }

            response.sendRedirect(request.getContextPath()
                    + "/products?review=success");

        } catch (SQLException e) {
            throw new ServletException("Unable to save product review.", e);
        }
    }
}
