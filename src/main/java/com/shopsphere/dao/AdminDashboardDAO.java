
package com.shopsphere.dao;

import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardDAO {

    public DashboardStats getStatistics() throws SQLException {

        String sql =
            "SELECT COUNT(*) AS totalOrders, " +
            "COALESCE(SUM(CASE WHEN order_status = 'PLACED' " +
            "THEN 1 ELSE 0 END), 0) AS placedOrders, " +
            "COALESCE(SUM(CASE WHEN order_status = 'PROCESSING' " +
            "THEN 1 ELSE 0 END), 0) AS processingOrders, " +
            "COALESCE(SUM(CASE WHEN order_status = 'SHIPPED' " +
            "THEN 1 ELSE 0 END), 0) AS shippedOrders, " +
            "COALESCE(SUM(CASE WHEN order_status = 'DELIVERED' " +
            "THEN 1 ELSE 0 END), 0) AS deliveredOrders, " +
            "COALESCE(SUM(CASE WHEN order_status = 'CANCELLED' " +
            "THEN 1 ELSE 0 END), 0) AS cancelledOrders " +
            "FROM orders";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return new DashboardStats(
                    result.getInt("totalOrders"),
                    result.getInt("placedOrders"),
                    result.getInt("processingOrders"),
                    result.getInt("shippedOrders"),
                    result.getInt("deliveredOrders"),
                    result.getInt("cancelledOrders")
                );
            }
        }

        return new DashboardStats(0, 0, 0, 0, 0, 0);
    }

    public static class DashboardStats {

        private final int totalOrders;
        private final int placedOrders;
        private final int processingOrders;
        private final int shippedOrders;
        private final int deliveredOrders;
        private final int cancelledOrders;

        public DashboardStats(int totalOrders, int placedOrders,
                              int processingOrders, int shippedOrders,
                              int deliveredOrders, int cancelledOrders) {
            this.totalOrders = totalOrders;
            this.placedOrders = placedOrders;
            this.processingOrders = processingOrders;
            this.shippedOrders = shippedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public int getPlacedOrders() {
            return placedOrders;
        }

        public int getProcessingOrders() {
            return processingOrders;
        }

        public int getShippedOrders() {
            return shippedOrders;
        }

        public int getDeliveredOrders() {
            return deliveredOrders;
        }

        public int getCancelledOrders() {
            return cancelledOrders;
        }
    }
}
