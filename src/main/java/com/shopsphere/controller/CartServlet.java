
package com.shopsphere.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        return cart;
    }

   
@Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();
    List<CartItem> cart = getCart(session);

    java.math.BigDecimal cartTotal = java.math.BigDecimal.ZERO;

    for (CartItem item : cart) {
        cartTotal = cartTotal.add(item.getSubtotal());
    }

    request.setAttribute("cartItems", cart);
    request.setAttribute("cartTotal", cartTotal);

    request.getRequestDispatcher(
            "/WEB-INF/views/cart.jsp"
    ).forward(request, response);
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

    String action = request.getParameter("action");

    if (action == null) {
        action = "add";
    }

    List<CartItem> cart = getCart(request.getSession());

    try {
        int productId = getProductId(request);

        if ("remove".equals(action)) {
            cart.removeIf(item ->
                    item.getProduct().getProductId() == productId);

        } else if ("update".equals(action)) {
            int quantity = getQuantity(request);

            for (CartItem item : cart) {
                if (item.getProduct().getProductId() == productId) {
                    if (quantity > item.getProduct().getStock()) {
                        response.sendError(
                                HttpServletResponse.SC_CONFLICT,
                                "Quantity exceeds available stock.");
                        return;
                    }

                    item.setQuantity(quantity);
                    break;
                }
            }

        } else if ("add".equals(action)) {
            List<Product> products = productDAO.findAllActive();
            Product selectedProduct = null;

            for (Product product : products) {
                if (product.getProductId() == productId) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found.");
                return;
            }

            if (selectedProduct.getStock() < 1) {
                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "Product is out of stock.");
                return;
            }

            CartItem existingItem = null;

            for (CartItem item : cart) {
                if (item.getProduct().getProductId() == productId) {
                    existingItem = item;
                    break;
                }
            }

            if (existingItem == null) {
                cart.add(new CartItem(selectedProduct, 1));
            } else if (existingItem.getQuantity()
                    < selectedProduct.getStock()) {
                existingItem.setQuantity(
                        existingItem.getQuantity() + 1);
            }

        } else {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Unknown cart action.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");

    } catch (SQLException e) {
        throw new ServletException(
                "Unable to update shopping cart.", e);
    }
}


    
private int getProductId(HttpServletRequest request)
        throws ServletException {
    try {
        return Integer.parseInt(request.getParameter("productId"));
    } catch (NumberFormatException | NullPointerException e) {
        throw new ServletException("Invalid product ID.", e);
    }
}

private int getQuantity(HttpServletRequest request)
        throws ServletException {
    try {
        int quantity = Integer.parseInt(
                request.getParameter("quantity"));

        if (quantity < 1) {
            throw new ServletException(
                    "Quantity must be at least 1.");
        }

        return quantity;
    } catch (NumberFormatException | NullPointerException e) {
        throw new ServletException("Invalid quantity.", e);
    }
}

}
