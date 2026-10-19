
package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class OrderStatusDAO {

    private static final List<String> ALLOWED_STATUSES =
            Arrays.asList("PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");

    public boolean updateOrderStatus(int orderId, String status)
            throws SQLException {

        if (status == null
                || !ALLOWED_STATUSES.contains(status.toUpperCase())) {
            return false;
        }

        String sql = "UPDATE orders SET order_status = ? "
                   + "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status.toUpperCase());
            statement.setInt(2, orderId);

            return statement.executeUpdate() > 0;
        }
    }
}
