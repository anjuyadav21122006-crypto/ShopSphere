
package com.shopsphere.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.shopsphere.dao.AdminDashboardDAO;

import java.io.IOException;


@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final AdminDashboardDAO dashboardDAO =
        new AdminDashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request,
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
try {
    request.setAttribute("stats", dashboardDAO.getStatistics());
} catch (java.sql.SQLException e) {
    throw new ServletException("Unable to load dashboard statistics.", e);
}
        request.getRequestDispatcher(
            "/WEB-INF/views/admin-dashboard.jsp"
        ).forward(request, response);
    }
}
