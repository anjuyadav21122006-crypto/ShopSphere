
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>Manage Categories</h1>

    <p>
        <a href="${pageContext.request.contextPath}/admin/dashboard">
            Back to Dashboard
        </a>
    </p>

    <h2>Add New Category</h2>

    <form action="${pageContext.request.contextPath}/admin/categories"
          method="post">

        <input type="hidden" name="action" value="add">

        <label>Category Name</label>
        <input type="text" name="name" maxlength="100" required>

        <label>Description</label>
        <textarea name="description" maxlength="500"></textarea>

        <button type="submit">Add Category</button>
    </form>

    <hr>

    <h2>Existing Categories</h2>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="c" items="${categories}">
            <tr>
                <td>${c.category_id}</td>
                <td><c:out value="${c.name}"/></td>
                <td><c:out value="${c.description}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${c.status == 1}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${c.status == 1}">
                        <form action="${pageContext.request.contextPath}/admin/categories"
                              method="post"
                              onsubmit="return confirm('Deactivate this category?')">
                            <input type="hidden" name="action"
                                   value="delete">
                            <input type="hidden" name="categoryId"
                                   value="${c.category_id}">
                            <button type="submit">Deactivate</button>
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
