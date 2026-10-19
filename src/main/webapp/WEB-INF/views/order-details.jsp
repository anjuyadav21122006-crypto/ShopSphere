
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>Order Details</h1>
    <h2>Order ID: #${orderId}</h2>

    <c:set var="grandTotal" value="${0}" />

    <c:choose>
        <c:when test="${not empty items}">
            <table>
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td><c:out value="${item.productName}"/></td>
                            <td>₹${item.price}</td>
                            <td>${item.quantity}</td>
                            <td>₹${item.subtotal}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No items found for this order.</p>
        </c:otherwise>
    </c:choose>

    <div style="margin-top: 25px;">
        <a href="${pageContext.request.contextPath}/orders"
           class="add-cart">Back to My Orders</a>

        <a href="${pageContext.request.contextPath}/products"
           class="add-cart">Continue Shopping</a>
    </div>
</div>

</body>
</html>
