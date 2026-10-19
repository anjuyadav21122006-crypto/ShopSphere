
package com.shopsphere.controller;

import com.shopsphere.dao.OrderStatusDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/order-status")
public class AdminOrderStatusServlet extends HttpServlet {

    private final OrderStatusDAO orderStatusDAO = new OrderStatusDAO();

    @Override
    protected void doPost(HttpServletRequest request,
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

        String orderIdText = request.getParameter("orderId");
        String status = request.getParameter("status");

        if (orderIdText == null || !orderIdText.matches("\\d+")
                || status == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdText);

            if (!orderStatusDAO.updateOrderStatus(orderId, status)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid status or order not found.");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/orders");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new ServletException("Unable to update order status.", e);
        }
    }
}
