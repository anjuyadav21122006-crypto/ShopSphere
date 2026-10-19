
package com.shopsphere.controller;

import com.shopsphere.dao.OrderHistoryDAO;
import com.shopsphere.dao.OrderHistoryDAO.OrderRecord;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/orders")
public class OrderHistoryServlet extends HttpServlet {

    private final OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            List<OrderRecord> orders =
                    orderHistoryDAO.findOrdersByUser(userId);

            request.setAttribute("orders", orders);

            request.getRequestDispatcher(
                    "/WEB-INF/views/orders.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load order history.", e);
        }
    }
}
