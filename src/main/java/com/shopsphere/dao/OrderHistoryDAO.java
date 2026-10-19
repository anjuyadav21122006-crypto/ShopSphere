
package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDAO {

    public List<OrderRecord> findOrdersByUser(int userId)
            throws SQLException {

        List<OrderRecord> orders = new ArrayList<>();

       String sql = "SELECT order_id, total_amount, payment_method, " +
             "payment_status, order_status, created_at, updated_at " +
             "FROM orders WHERE user_id = ? " +
             "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                   orders.add(new OrderRecord(
        rs.getInt("order_id"),
        rs.getBigDecimal("total_amount"),
        rs.getString("payment_method"),
        rs.getString("payment_status"),
        rs.getString("order_status"),
        rs.getTimestamp("created_at"),
        rs.getTimestamp("updated_at")
));
                }
            }
        }

        return orders;
    }

    public static class OrderRecord {
        private final int orderId;
        private final java.math.BigDecimal totalAmount;
        private final String paymentMethod;
        private final String paymentStatus;
        private final String orderStatus;
        private final java.sql.Timestamp createdAt;
        private final java.sql.Timestamp updatedAt;

        public OrderRecord(int orderId,
                   java.math.BigDecimal totalAmount,
                   String paymentMethod,
                   String paymentStatus,
                   String orderStatus,
                   java.sql.Timestamp createdAt,
                   java.sql.Timestamp updatedAt) {
    this.orderId = orderId;
    this.totalAmount = totalAmount;
    this.paymentMethod = paymentMethod;
    this.paymentStatus = paymentStatus;
    this.orderStatus = orderStatus;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
}

        public int getOrderId() { return orderId; }
        public java.math.BigDecimal getTotalAmount() { return totalAmount; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getPaymentStatus() { return paymentStatus; }
        public String getOrderStatus() { return orderStatus; }
        public java.sql.Timestamp getCreatedAt() { return createdAt; }
        public java.sql.Timestamp getUpdatedAt() {
    return updatedAt;
}
    }
}
