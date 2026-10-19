
package com.shopsphere.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String category = request.getParameter("category");
        String sort = request.getParameter("sort");

        if (search == null) search = "";
        if (category == null) category = "";
        if (sort == null) sort = "default";

        search = search.trim();
        category = category.trim();

        try {
            List<Product> products;

            if (!search.isEmpty()
                    || !category.isEmpty()
                    || !"default".equals(sort)) {

                products = productDAO.searchFilterSort(
                        search, category, sort);

            } else {
                products = productDAO.findAllActive();
            }

            request.setAttribute("products", products);
            request.setAttribute("search", search);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("selectedSort", sort);

            request.getRequestDispatcher(
                    "/WEB-INF/views/products.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load products from the database.", e);
        }
    }
}
