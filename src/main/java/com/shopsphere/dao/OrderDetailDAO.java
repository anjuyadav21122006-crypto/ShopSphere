
package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {

    public List<OrderItemRecord> findItemsByOrderAndUser(
            int orderId, int userId) throws SQLException {

        List<OrderItemRecord> items = new ArrayList<>();

        String sql =
            "SELECT oi.product_id, p.name, oi.quantity, oi.price " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE oi.order_id = ? AND o.user_id = ? " +
            "ORDER BY oi.order_item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItemRecord(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price")
                    ));
                }
            }
        }

        return items;
    }

    public static class OrderItemRecord {
        private final int productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal price;

        public OrderItemRecord(int productId, String productName,
                               int quantity, BigDecimal price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }

        public BigDecimal getSubtotal() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
