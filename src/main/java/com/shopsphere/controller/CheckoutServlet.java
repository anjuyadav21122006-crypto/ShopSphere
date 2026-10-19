
package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.OrderItemDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
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
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");

        if (!"COD".equals(paymentMethod)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid payment method.");
            return;
        }

        Object cartObject = session.getAttribute("cart");

        if (!(cartObject instanceof List<?>)) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) cartObject;

        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try (Connection connection = DBConnection.getConnection()) {

            connection.setTransactionIsolation(
                    Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

            try {
               BigDecimal total = BigDecimal.ZERO;
BigDecimal couponDiscount = BigDecimal.ZERO;
String appliedCoupon = (String) session.getAttribute("appliedCoupon");

                // Lock products and validate current stock.
                for (CartItem item : cart) {
                    Product product = item.getProduct();

                    String sql = "SELECT price, stock FROM products " +
                            "WHERE product_id = ? AND status = 1 FOR UPDATE";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(sql)) {

                        ps.setInt(1, product.getProductId());

                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new SQLException(
                                        "A product is no longer available.");
                            }

                            int stock = rs.getInt("stock");

                            if (item.getQuantity() <= 0 ||
                                    item.getQuantity() > stock) {
                                throw new SQLException(
                                        "Insufficient stock for " +
                                                product.getName());
                            }

                            BigDecimal price = rs.getBigDecimal("price");
                            total = total.add(price.multiply(
                                    BigDecimal.valueOf(item.getQuantity())));
                        }
                    }
                }

                if (appliedCoupon != null) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT discount_type, discount_value, min_order_amount, " +
            "max_discount, expiry_date FROM coupons " +
            "WHERE code = ? AND status = 1")) {

        ps.setString(1, appliedCoupon);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal minimum = rs.getBigDecimal("min_order_amount");
                BigDecimal discountValue = rs.getBigDecimal("discount_value");
                BigDecimal maxDiscount = rs.getBigDecimal("max_discount");
                java.sql.Date expiry = rs.getDate("expiry_date");

                if ((expiry == null ||
                        !expiry.toLocalDate().isBefore(
                                java.time.LocalDate.now())) &&
                        (minimum == null || total.compareTo(minimum) >= 0)) {

                    if ("PERCENT".equals(rs.getString("discount_type"))) {
                        couponDiscount = total.multiply(discountValue)
                                .divide(new BigDecimal("100"), 2,
                                        java.math.RoundingMode.HALF_UP);
                    } else {
                        couponDiscount = discountValue;
                    }

                    if (maxDiscount != null &&
                            couponDiscount.compareTo(maxDiscount) > 0) {
                        couponDiscount = maxDiscount;
                    }

                    if (couponDiscount.compareTo(total) > 0) {
                        couponDiscount = total;
                    }
                } else {
                    appliedCoupon = null;
                }
            } else {
                appliedCoupon = null;
            }
        }
    }

    total = total.subtract(couponDiscount);
}

int orderId = orderDAO.createOrder(
        connection, userId, total, paymentMethod);

                for (CartItem item : cart) {
                    int productId = item.getProduct().getProductId();
                    int quantity = item.getQuantity();

                    String sql = "SELECT price FROM products " +
                            "WHERE product_id = ? AND status = 1";

                    BigDecimal currentPrice;

                    try (PreparedStatement ps =
                                 connection.prepareStatement(sql)) {
                        ps.setInt(1, productId);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                throw new SQLException(
                                        "Product became unavailable.");
                            }
                            currentPrice = rs.getBigDecimal("price");
                        }
                    }

                    orderItemDAO.addOrderItem(
                            connection, orderId, productId,
                            quantity, currentPrice);

                    String updateSql = "UPDATE products " +
                            "SET stock = stock - ? " +
                            "WHERE product_id = ? AND stock >= ?";

                    try (PreparedStatement ps =
                                 connection.prepareStatement(updateSql)) {
                        ps.setInt(1, quantity);
                        ps.setInt(2, productId);
                        ps.setInt(3, quantity);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Stock changed. Please try again.");
                        }
                    }
                }

                connection.commit();

session.removeAttribute("cart");
session.removeAttribute("appliedCoupon");
session.removeAttribute("couponDiscount");
session.removeAttribute("discountedTotal");

response.sendRedirect(
        request.getContextPath() +
                "/order-success?id=" + orderId);

            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new ServletException(e);

            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Connection is closing.
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Could not place your order.", e);
        }
    }
}
