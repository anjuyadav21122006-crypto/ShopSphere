
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Orders - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>Admin - Manage Orders</h1>
    <p>Update order statuses below.</p>

    <table>
        <thead>
            <tr>
                <th>Order ID</th>
                <th>Customer ID</th>
                <th>Total</th>
                <th>Current Status</th>
                <th>Update Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>${order.orderId}</td>
                    <td>${order.userId}</td>
                    <td>₹${order.totalAmount}</td>
                    <td>
                        <c:out value="${order.orderStatus}"/>
                    </td>
                    <td>
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/order-status">
                            <input type="hidden" name="orderId"
                                   value="${order.orderId}">

                            <select name="status" required>
                                <option value="PROCESSING">Processing</option>
                                <option value="SHIPPED">Shipped</option>
                                <option value="DELIVERED">Delivered</option>
                                <option value="CANCELLED">Cancelled</option>
                            </select>

                            <button type="submit">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <p>
        <a href="${pageContext.request.contextPath}/products">
            Back to Shop
        </a>
    </p>
</div>

</body>
</html>
