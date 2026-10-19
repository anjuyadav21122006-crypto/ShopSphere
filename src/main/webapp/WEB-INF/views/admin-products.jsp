
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Products</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>Manage Products</h1>

    <p>
        <a href="${pageContext.request.contextPath}/admin/dashboard">
            Back to Dashboard
        </a>
    </p>

    <h2>Add New Product</h2>

    <form action="${pageContext.request.contextPath}/admin/products"
          method="post">

        <input type="hidden" name="action" value="add">

        <label>Product Name</label>
        <input type="text" name="name" required maxlength="150">

        <label>Brand</label>
        <input type="text" name="brand" maxlength="100">

        <label>Description</label>
        <textarea name="description"></textarea>

        <label>Category</label>
        <select name="categoryId" required>
            <option value="">Select Category</option>
            <option value="1">Electronics</option>
            <option value="2">Fashion</option>
            <option value="3">Home</option>
            <option value="4">Books</option>
        </select>

        <label>Price (₹)</label>
        <input type="number" name="price" min="0.01"
               step="0.01" required>

        <label>Discount (%)</label>
        <input type="number" name="discount" min="0"
               max="100" step="0.01" value="0" required>

        <label>Stock</label>
        <input type="number" name="stock" min="0"
               value="0" required>

        <label>Image URL</label>
        <input type="url" name="imageUrl" maxlength="500">

        <button type="submit">Add Product</button>
    </form>

    <hr>

    <h2>Existing Products</h2>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="p" items="${products}">
            <tr>
                <td>${p.product_id}</td>
                <td><c:out value="${p.name}"/></td>
                <td><c:out value="${p.category_name}"/></td>
                <td>₹${p.price}</td>
                <td>${p.stock}</td>
                <td>
                    <c:choose>
                        <c:when test="${p.status == 1}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
                
<td>
    <c:if test="${p.status == 1}">
        <a href="${pageContext.request.contextPath}/admin/product-edit?id=${p.product_id}">
            Edit
        </a>

        <form action="${pageContext.request.contextPath}/admin/products"
              method="post">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="productId"
                   value="${p.product_id}">
            <button type="submit"
                    onclick="return confirm('Deactivate this product?')">
                Delete
            </button>
        </form>
    </c:if>
</td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
