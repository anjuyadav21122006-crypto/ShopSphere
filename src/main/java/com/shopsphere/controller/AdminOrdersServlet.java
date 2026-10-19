
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
import java.util.List;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<AdminOrder> orders = new ArrayList<>();

        String sql = "SELECT order_id, user_id, total_amount, order_status "
                   + "FROM orders ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                orders.add(new AdminOrder(
                    result.getInt("order_id"),
                    result.getInt("user_id"),
                    result.getBigDecimal("total_amount"),
                    result.getString("order_status")
                ));
            }

        } catch (SQLException e) {
            throw new ServletException("Unable to load orders.", e);
        }

        request.setAttribute("orders", orders);
        request.getRequestDispatcher(
            "/WEB-INF/views/admin-orders.jsp"
        ).forward(request, response);
    }

    public static class AdminOrder {
        private final int orderId;
        private final int userId;
        private final java.math.BigDecimal totalAmount;
        private final String orderStatus;

        public AdminOrder(int orderId, int userId,
                          java.math.BigDecimal totalAmount,
                          String orderStatus) {
            this.orderId = orderId;
            this.userId = userId;
            this.totalAmount = totalAmount;
            this.orderStatus = orderStatus;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getUserId() {
            return userId;
        }

        public java.math.BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public String getOrderStatus() {
            return orderStatus;
        }
    }
}
