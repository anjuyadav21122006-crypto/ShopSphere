
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Successful - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <h1>ShopSphere</h1>
</header>

<main class="container">
    <h2>Order Placed Successfully!</h2>

    <p>Thank you for shopping with ShopSphere.</p>

    <p>Your Order ID is:
        <strong>${orderId}</strong>
    </p>

    <p>Payment Method: Cash on Delivery</p>
    <p>Payment Status: Pending</p>

    <a href="${pageContext.request.contextPath}/products">
        Continue Shopping
    </a>

    <br><br>

    <a href="${pageContext.request.contextPath}/orders">
        View My Orders
    </a>
</main>

</body>
</html>
