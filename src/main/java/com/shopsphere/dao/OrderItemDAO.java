
package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO {

    public void addOrderItem(Connection connection,
                             int orderId,
                             int productId,
                             int quantity,
                             java.math.BigDecimal price)
            throws SQLException {

        String sql = "INSERT INTO order_items " +
                "(order_id, product_id, quantity, price) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.setBigDecimal(4, price);

            statement.executeUpdate();
        }
    }
}
