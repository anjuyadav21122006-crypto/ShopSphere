
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | ShopSphere</title>
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
        <a href="${pageContext.request.contextPath}/my-orders">My Orders</a>
    </nav>
</header>

<h2 class="page-title">My Profile</h2>

<div style="max-width:600px; margin:25px auto; padding:25px;
            background:white; border-radius:10px;">

    <c:if test="${param.success eq '1'}">
        <p style="color:green;">Profile updated successfully!</p>
    </c:if>

    <c:if test="${param.error eq 'required'}">
        <p style="color:red;">Name and mobile number are required.</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/profile"
          method="post">

        <label>Full Name</label>
        <input type="text" name="name"
               value="<c:out value='${name}'/>"
               maxlength="100" required
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <label>Email Address (cannot be changed here)</label>
        <input type="email"
               value="<c:out value='${email}'/>"
               readonly
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <label>Mobile Number</label>
        <input type="tel" name="mobile"
               value="<c:out value='${mobile}'/>"
               maxlength="15" required
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <h3>Delivery Address</h3>

        <label>House / Street Address</label>
        <textarea name="address" maxlength="255"
                  style="width:100%; padding:10px; margin:8px 0 15px;"><c:out value="${address}"/></textarea>

        <label>City</label>
        <input type="text" name="city"
               value="<c:out value='${city}'/>"
               maxlength="100"
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <label>State</label>
        <input type="text" name="state"
               value="<c:out value='${state}'/>"
               maxlength="100"
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <label>PIN Code</label>
        <input type="text" name="pincode"
               value="<c:out value='${pincode}'/>"
               maxlength="10"
               style="width:100%; padding:10px; margin:8px 0 15px;">

        <button type="submit" class="add-cart">
            Save Profile
        </button>
    </form>
</div>

<footer>
    <p>© 2026 ShopSphere | Your Online Shopping Store</p>
</footer>

</body>
</html>
