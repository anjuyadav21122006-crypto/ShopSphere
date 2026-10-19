
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <h1>ShopSphere - Checkout</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
    </nav>
</header>

<main class="container">
    <h2>Complete Your Order</h2>

    <c:choose>
        <c:when test="${empty sessionScope.cart}">
            <p>Your cart is empty.</p>
            <a href="${pageContext.request.contextPath}/products">
                Continue Shopping
            </a>
        </c:when>

        <c:otherwise>
            <p>Review your shopping cart before placing your order.</p>

            <a href="${pageContext.request.contextPath}/cart">
                Review Cart
            </a>

            <form action="${pageContext.request.contextPath}/checkout"
                  method="post">

                <label for="paymentMethod">Payment Method:</label>

                <select name="paymentMethod" id="paymentMethod" required>
                    <option value="">Select payment method</option>
                    <option value="COD">Cash on Delivery</option>
                </select>

                <br><br>

                <button type="submit" class="add-cart">
                    Place Order
                </button>
            </form>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
