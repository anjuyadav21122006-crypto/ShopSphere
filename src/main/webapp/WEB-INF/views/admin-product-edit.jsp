
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>Edit Product</h1>

    <form action="${pageContext.request.contextPath}/admin/product-update"
          method="post">

        <input type="hidden" name="productId" value="${productId}">

        <label>Product Name</label>
        <input type="text" name="name" value="<c:out value='${name}'/>"
               maxlength="150" required>

        <label>Brand</label>
        <input type="text" name="brand" value="<c:out value='${brand}'/>"
               maxlength="100">

        <label>Description</label>
        <textarea name="description"><c:out value="${description}"/></textarea>

        <label>Category</label>
        <select name="categoryId" required>
            <option value="1" ${categoryId == 1 ? 'selected' : ''}>Electronics</option>
            <option value="2" ${categoryId == 2 ? 'selected' : ''}>Fashion</option>
            <option value="3" ${categoryId == 3 ? 'selected' : ''}>Home</option>
            <option value="4" ${categoryId == 4 ? 'selected' : ''}>Books</option>
        </select>

        <label>Price (₹)</label>
        <input type="number" name="price" value="${price}"
               min="0.01" step="0.01" required>

        <label>Discount (%)</label>
        <input type="number" name="discount" value="${discount}"
               min="0" max="100" step="0.01" required>

        <label>Stock</label>
        <input type="number" name="stock" value="${stock}"
               min="0" required>

        <label>Image URL</label>
        <input type="url" name="imageUrl"
               value="<c:out value='${imageUrl}'/>" maxlength="500">

        <button type="submit">Save Changes</button>
    </form>

    <p>
        <a href="${pageContext.request.contextPath}/admin/products">
            Cancel and Return
        </a>
    </p>
</div>

</body>
</html>
