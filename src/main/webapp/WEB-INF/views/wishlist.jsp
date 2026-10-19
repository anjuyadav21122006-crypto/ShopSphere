
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist | ShopSphere</title>
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
        <a href="${pageContext.request.contextPath}/profile">My Profile</a>
        <a href="${pageContext.request.contextPath}/wishlist">My Wishlist</a>
    </nav>
</header>

<h2 class="page-title">❤️ My Wishlist</h2>

<div class="product-container">

    <c:choose>
        <c:when test="${not empty wishlistProducts}">
            <c:forEach var="product" items="${wishlistProducts}">
                <div class="product-card">

                    <img
                        src="${pageContext.request.contextPath}/images/product-placeholder.jpg"
                        alt="Product image">

                    <h3><c:out value="${product.name}"/></h3>

                    <p>
                        <strong>Brand:</strong>
                        <c:out value="${product.brand}"/>
                    </p>

                    <p class="price">
                        ₹<c:out value="${product.price}"/>
                    </p>

                    <c:choose>
                        <c:when test="${product.stock gt 0}">
                            <form action="${pageContext.request.contextPath}/cart"
                                  method="post">
                                <input type="hidden"
                                       name="productId"
                                       value="${product.productId}">
                                <button type="submit" class="add-cart">
                                    Add to Cart
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p>Out of Stock</p>
                        </c:otherwise>
                    </c:choose>

                    <form action="${pageContext.request.contextPath}/wishlist"
                          method="post"
                          style="margin-top:10px;">
                        <input type="hidden" name="action" value="remove">
                        <input type="hidden"
                               name="wishlistId"
                               value="${product.wishlistId}">
                        <button type="submit" class="add-cart">
                            Remove ❤️
                        </button>
                    </form>

                </div>
            </c:forEach>
        </c:when>

        <c:otherwise>
            <p style="width:100%; text-align:center;">
                Your wishlist is empty.
                <a href="${pageContext.request.contextPath}/products">
                    Explore products
                </a>
            </p>
        </c:otherwise>
    </c:choose>

</div>

<footer>
    <p>© 2026 ShopSphere | Your Online Shopping Store</p>
</footer>

</body>
</html>
