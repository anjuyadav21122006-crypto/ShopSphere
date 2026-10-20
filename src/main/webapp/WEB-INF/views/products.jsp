
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products | ShopSphere</title>

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
        <c:if test="${not empty sessionScope.userId}">
    <a href="${pageContext.request.contextPath}/profile">My Profile</a>
</c:if>

        <c:if test="${sessionScope.userRole eq 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/dashboard">
                Admin Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders">
                Admin Orders
            </a>
        </c:if>

        <c:choose>
            <c:when test="${not empty sessionScope.userId}">
                <span>Hi, <c:out value="${sessionScope.userName}"/>!</span>
                <form action="${pageContext.request.contextPath}/logout"
                      method="post" style="display:inline;">
                    <button type="submit" class="add-cart">Logout</button>
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>

<h2 class="page-title">Explore Our Products</h2>

<!-- Search, Filter and Sort -->
<form action="${pageContext.request.contextPath}/products"
      method="get"
      style="max-width:1000px; margin:20px auto; padding:15px;
             display:flex; gap:10px; flex-wrap:wrap; justify-content:center;">

    <input type="text"
           name="search"
           placeholder="Search products or brands..."
           value="<c:out value='${search}'/>"
           style="padding:10px; flex:1; min-width:180px;">

    <select name="category" style="padding:10px;">
        <option value="">All Categories</option>
        <option value="1" <c:if test="${selectedCategory eq '1'}">selected</c:if>>Electronics</option>
        <option value="2" <c:if test="${selectedCategory eq '2'}">selected</c:if>>Fashion</option>
        <option value="3" <c:if test="${selectedCategory eq '3'}">selected</c:if>>Home</option>
        <option value="4" <c:if test="${selectedCategory eq '4'}">selected</c:if>>Books</option>
        <option value="5" <c:if test="${selectedCategory eq '5'}">selected</c:if>>Accessories</option>
    </select>

    <select name="sort" style="padding:10px;">
        <option value="default" <c:if test="${selectedSort eq 'default'}">selected</c:if>>Newest First</option>
        <option value="price_low" <c:if test="${selectedSort eq 'price_low'}">selected</c:if>>Price: Low to High</option>
        <option value="price_high" <c:if test="${selectedSort eq 'price_high'}">selected</c:if>>Price: High to Low</option>
        <option value="name_asc" <c:if test="${selectedSort eq 'name_asc'}">selected</c:if>>Name: A to Z</option>
    </select>

    <button type="submit" class="add-cart">Apply</button>

    <a href="${pageContext.request.contextPath}/products"
       style="padding:10px;">Clear</a>
</form>

<div class="product-container">
    <c:choose>
        <c:when test="${not empty products}">
            <c:forEach var="product" items="${products}">
                <div class="product-card">

                    <img src="${pageContext.request.contextPath}/images/${product.imageUrl}"
                         alt="Product image">

                    <h3><c:out value="${product.name}"/></h3>

                    <p>
                        <strong>Brand:</strong>
                        <c:out value="${product.brand}"/>
                    </p>

                    <p><c:out value="${product.description}"/></p>

                    <p class="price">
                        ₹<c:out value="${product.price}"/>
                    </p>

                    <p>
                        <strong>Stock:</strong>
                        <c:out value="${product.stock}"/>
                    </p>

                    <c:choose>
                        <c:when test="${product.stock gt 0}">
                            <form action="${pageContext.request.contextPath}/cart"
                                  method="post">
                                <input type="hidden"
                                       name="productId"
                                       value="${product.productId}">
                                <button class="add-cart" type="submit">
                                    Add to Cart
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p>Out of Stock</p>
                        </c:otherwise>
                    </c:choose>
                    
                    <c:if test="${not empty sessionScope.userId}">
                        <form action="${pageContext.request.contextPath}/wishlist"
                              method="post"
                              style="margin-top:10px;">

                            <input type="hidden" name="action" value="add">
                            <input type="hidden"
                                   name="productId"
                                   value="${product.productId}">

                            <button type="submit" class="add-cart">
                                ❤️ Add to Wishlist
                            </button>
                        </form>
                    </c:if>

<c:if test="${not empty sessionScope.userId}">
    <hr>
    <h4>Rate this Product ⭐</h4>

    <form action="${pageContext.request.contextPath}/reviews"
          method="post">

        <input type="hidden" name="productId"
               value="${product.productId}">

        <label for="rating-${product.productId}">
            Your Rating:
        </label>

        <select id="rating-${product.productId}"
                name="rating" required>
            <option value="5">★★★★★ (5)</option>
            <option value="4">★★★★☆ (4)</option>
            <option value="3">★★★☆☆ (3)</option>
            <option value="2">★★☆☆☆ (2)</option>
            <option value="1">★☆☆☆☆ (1)</option>
        </select>

        <label for="review-${product.productId}">
            Your Review:
        </label>

        <textarea id="review-${product.productId}"
                  name="reviewText"
                  maxlength="2000"
                  rows="3"
                  placeholder="Write your review..."></textarea>

        <button type="submit">Submit Review</button>
    </form>
</c:if>


                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p style="text-align:center; width:100%;">
                No products found. Try another search or category.
            </p>
        </c:otherwise>
    </c:choose>
</div>

<footer>
    <p>© 2026 ShopSphere | Your Online Shopping Store</p>
</footer>

</body>
</html>
