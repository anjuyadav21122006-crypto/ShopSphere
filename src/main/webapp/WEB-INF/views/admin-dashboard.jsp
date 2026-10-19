
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - ShopSphere</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h1>ShopSphere Admin Dashboard</h1>
    <p>Welcome to the administration panel.</p>

    <div class="dashboard-cards">

        <div class="dashboard-card">
            <h2>Total Orders</h2>
            <p>${stats.totalOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Placed Orders</h2>
            <p>${stats.placedOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Processing Orders</h2>
            <p>${stats.processingOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Shipped Orders</h2>
            <p>${stats.shippedOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Delivered Orders</h2>
            <p>${stats.deliveredOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Cancelled Orders</h2>
            <p>${stats.cancelledOrders}</p>
        </div>

        <div class="dashboard-card">
            <h2>Manage Orders</h2>
            <p>View orders and update their status.</p>
            <a href="${pageContext.request.contextPath}/admin/orders"
               class="add-cart">Manage Orders</a>
        </div>
        <div class="dashboard-card">
    <h2>Manage Products</h2>
    <p>Add and delete products in your store.</p>
    <a href="${pageContext.request.contextPath}/admin/products"
       class="add-cart">Manage Products</a>
</div>

<div class="dashboard-card">
    <h2>Manage Categories</h2>
    <p>Add and manage your store categories.</p>
    <a href="${pageContext.request.contextPath}/admin/categories"
       class="add-cart">Manage Categories</a>
</div>


        <div class="dashboard-card">
            <h2>Visit Store</h2>
            <p>Browse products in your online store.</p>
            <a href="${pageContext.request.contextPath}/products"
               class="add-cart">Open Store</a>
        </div>

    </div>

    <p>
        <a href="${pageContext.request.contextPath}/products">
            Back to Shop
        </a>
    </p>
</div>

</body>
</html>
