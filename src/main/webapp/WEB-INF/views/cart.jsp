
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart | ShopSphere</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header>
    <h1>ShopSphere</h1>
    <nav>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
    </nav>
</header>

<h2 class="page-title">Your Shopping Cart</h2>

<c:choose>
    <c:when test="${empty cartItems}">
        <p style="text-align:center; margin:30px;">
            Your cart is empty.
        </p>
        <p style="text-align:center;">
            <a class="add-cart"
               href="${pageContext.request.contextPath}/products">
                Browse Products
            </a>
        </p>
    </c:when>

    <c:otherwise>
        <div style="padding:20px 6%;">
            <table style="width:100%; background:white; border-collapse:collapse;">
                <thead>
                    <tr>
                        <th style="padding:12px;">Product</th>
                        <th style="padding:12px;">Price</th>
                        <th style="padding:12px;">Quantity</th>
                       <th style="padding:12px;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cartItems}">
                        <tr>
                            <td style="padding:12px; text-align:center;">
                                <c:out value="${item.product.name}"/>
                            </td>
                            
<td style="padding:12px; text-align:center;">
    <form action="${pageContext.request.contextPath}/cart"
          method="post">
        <input type="hidden" name="action" value="remove">
        <input type="hidden" name="productId"
               value="${item.product.productId}">

        <button class="add-cart" type="submit">
            Remove
        </button>
    </form>
</td>

                            <td style="padding:12px; text-align:center;">
                                ₹<c:out value="${item.product.price}"/>
                            </td>
                           
<td style="padding:12px; text-align:center;">
    <form action="${pageContext.request.contextPath}/cart"
          method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="productId"
               value="${item.product.productId}">

        <input type="number" name="quantity"
               value="${item.quantity}" min="1"
               max="${item.product.stock}" required
               style="width:60px; padding:6px;">

        <button class="add-cart" type="submit">
            Update
        </button>
    </form>
</td>

                            <td style="padding:12px; text-align:center;">
                                ₹<c:out value="${item.subtotal}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <p style="text-align:right; margin-top:25px; font-size:22px;">
    <strong>Total: ₹<c:out value="${cartTotal}"/></strong>
</p>

<form action="${pageContext.request.contextPath}/apply-coupon"
      method="post"
      style="text-align:right; margin:20px 0;">

    <input type="text"
           name="couponCode"
           placeholder="Enter coupon code"
           required
           style="padding:10px; width:200px;">

    <button type="submit" class="add-cart">
        Apply Coupon
    </button>
</form>

<c:if test="${param.couponSuccess == 'true'}">
    <p style="color:green; text-align:right;">
        Coupon applied successfully!
    </p>
</c:if>

<c:if test="${not empty param.couponError}">
    <p style="color:red; text-align:right;">
        Coupon could not be applied. Check the code and minimum order amount.
    </p>
</c:if>

<c:if test="${not empty sessionScope.appliedCoupon}">
    <p style="text-align:right; color:green;">
        Coupon: <c:out value="${sessionScope.appliedCoupon}"/>
    </p>

    <p style="text-align:right;">
        Discount: ₹<c:out value="${sessionScope.couponDiscount}"/>
    </p>

    <p style="text-align:right; font-size:20px;">
        <strong>
            Payable Total: ₹<c:out value="${sessionScope.discountedTotal}"/>
        </strong>
    </p>
</c:if>

            
<c:if test="${not empty sessionScope.cart}">
    <a href="${pageContext.request.contextPath}/checkout"
       class="add-cart">
        Proceed to Checkout
    </a>
</c:if>

            <p style="text-align:right; margin-top:20px;">
                <a class="add-cart"
                   href="${pageContext.request.contextPath}/products">
                    Continue Shopping
                </a>
            </p>
        </div>
    </c:otherwise>
</c:choose>

<footer>
    <p>© 2026 ShopSphere | Your Online Shopping Store</p>
</footer>

</body>
</html>
