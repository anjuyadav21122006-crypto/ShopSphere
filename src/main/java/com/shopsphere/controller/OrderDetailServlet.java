
package com.shopsphere.controller;

import com.shopsphere.dao.OrderDetailDAO;
import com.shopsphere.dao.OrderDetailDAO.OrderItemRecord;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/order-details")
public class OrderDetailServlet extends HttpServlet {

    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login"
            );
            return;
        }

        String orderIdText = request.getParameter("id");

        if (orderIdText == null || !orderIdText.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdText);
            int userId = (Integer) session.getAttribute("userId");

            List<OrderItemRecord> items =
                orderDetailDAO.findItemsByOrderAndUser(orderId, userId);

            // An empty result must not reveal whether another user's order exists.
            if (items.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("items", items);
            request.setAttribute("orderId", orderId);

            request.getRequestDispatcher(
                "/WEB-INF/views/order-details.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            throw new ServletException("Unable to load order details.", e);
        }
    }
}
