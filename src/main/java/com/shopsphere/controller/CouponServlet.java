
package com.shopsphere.controller;
import com.shopsphere.model.CartItem;
import java.util.List;
import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/apply-coupon")
public class CouponServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !(session.getAttribute("userId") instanceof Number)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String code = request.getParameter("couponCode");

        if (code == null || code.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?couponError=empty");
            return;
        }

        code = code.trim().toUpperCase();

        BigDecimal cartTotal = BigDecimal.ZERO;

Object cartObject = session.getAttribute("cart");

if (!(cartObject instanceof List<?>)) {
    response.sendRedirect(request.getContextPath() + "/cart?couponError=empty");
    return;
}

@SuppressWarnings("unchecked")
List<CartItem> cart = (List<CartItem>) cartObject;

if (cart.isEmpty()) {
    response.sendRedirect(request.getContextPath() + "/cart?couponError=empty");
    return;
}

for (CartItem item : cart) {
    if (item.getQuantity() <= 0) {
        response.sendRedirect(request.getContextPath() + "/cart?couponError=invalid");
        return;
    }

    BigDecimal price = item.getProduct().getPrice();
    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

    cartTotal = cartTotal.add(price.multiply(quantity));
}

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT discount_type, discount_value, min_order_amount, " +
                     "max_discount, expiry_date FROM coupons " +
                     "WHERE code = ? AND status = 1")) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    response.sendRedirect(request.getContextPath() + "/cart?couponError=invalid");
                    return;
                }

                BigDecimal minimum = rs.getBigDecimal("min_order_amount");
                BigDecimal discountValue = rs.getBigDecimal("discount_value");
                BigDecimal maxDiscount = rs.getBigDecimal("max_discount");
                java.sql.Date expiry = rs.getDate("expiry_date");

                if (expiry != null && expiry.toLocalDate().isBefore(LocalDate.now())) {
                    response.sendRedirect(request.getContextPath() + "/cart?couponError=expired");
                    return;
                }

                if (minimum != null && cartTotal.compareTo(minimum) < 0) {
                    response.sendRedirect(request.getContextPath() + "/cart?couponError=minimum");
                    return;
                }

                BigDecimal discount;

                if ("PERCENT".equals(rs.getString("discount_type"))) {
                    discount = cartTotal.multiply(discountValue)
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                } else {
                    discount = discountValue;
                }

                if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
                    discount = maxDiscount;
                }

                if (discount.compareTo(cartTotal) > 0) {
                    discount = cartTotal;
                }

                BigDecimal finalTotal = cartTotal.subtract(discount);

                session.setAttribute("appliedCoupon", code);
                session.setAttribute("couponDiscount", discount);
                session.setAttribute("discountedTotal", finalTotal);

                response.sendRedirect(request.getContextPath() + "/cart?couponSuccess=true");
            }

        } catch (SQLException e) {
            throw new ServletException("Unable to apply coupon.", e);
        }
    }
}
