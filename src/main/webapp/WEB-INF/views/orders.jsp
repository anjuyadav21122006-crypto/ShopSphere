
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Orders - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <h1>ShopSphere - My Orders</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/orders">My Orders</a>
    </nav>
</header>

<main class="container">
    <h2>My Order History</h2>

    <c:choose>
        <c:when test="${empty orders}">
            <p>You haven't placed any orders yet.</p>
            <a href="${pageContext.request.contextPath}/products">
                Start Shopping
            </a>
        </c:when>

        <c:otherwise>
            <table>
               <thead>
    <tr>
        <th>Order ID</th>
        <th>Date</th>
        <th>Total</th>
        <th>Payment Method</th>
        <th>Payment Status</th>
        <th>Order Status</th>
        <th>Last Updated</th>
        <th>Action</th>
    </tr>
</thead>
                <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>#<c:out value="${order.orderId}"/></td>
                        <td><c:out value="${order.createdAt}"/></td>
                        <td>₹<c:out value="${order.totalAmount}"/></td>
                        <td><c:out value="${order.paymentMethod}"/></td>
                        <td><c:out value="${order.paymentStatus}"/></td>
                        <td>
    <span class="status-badge">
        <c:out value="${order.orderStatus}"/>
    </span>
</td>
                        <td>${order.updatedAt}</td>
                        <td>
    <a href="${pageContext.request.contextPath}/order-details?id=${order.orderId}"
       class="add-cart">View Details</a>
</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</main>
</body>
</html>
