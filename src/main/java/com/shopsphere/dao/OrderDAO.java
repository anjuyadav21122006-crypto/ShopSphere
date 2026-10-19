
package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    public int createOrder(Connection connection, int userId,
                           BigDecimal totalAmount,
                           String paymentMethod) throws SQLException {

        String sql = "INSERT INTO orders " +
                "(user_id, total_amount, payment_method, " +
                "payment_status, order_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setBigDecimal(2, totalAmount);
            statement.setString(3, paymentMethod);
            statement.setString(4, "PENDING");
            statement.setString(5, "PLACED");

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not create order.");
    }
}
