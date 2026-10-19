
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .login-box {
            max-width: 400px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .login-box h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        .login-box label {
            display: block;
            margin: 12px 0 6px;
        }

        .login-box input {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .login-box button {
            width: 100%;
            margin-top: 20px;
            padding: 12px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .login-box p {
            text-align: center;
            margin-top: 18px;
        }

        .message {
            text-align: center;
            margin: 12px 0;
        }
    </style>
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

<div class="login-box">
    <h2>Welcome Back!</h2>

    <% if ("true".equals(request.getParameter("registered"))) { %>
        <p class="message">Registration successful! Please log in.</p>
    <% } %>

    <% if ("true".equals(request.getParameter("error"))) { %>
        <p class="message">Invalid email or password. Try again.</p>
    <% } %>

    <form action="${pageContext.request.contextPath}/login"
          method="post">

        <label for="email">Email Address</label>
        <input type="email" id="email" name="email"
               required maxlength="150" autocomplete="email">

        <label for="password">Password</label>
        <input type="password" id="password" name="password"
               required autocomplete="current-password">

        <button type="submit">Login</button>
    </form>

    <p>Don't have an account?
        <a href="${pageContext.request.contextPath}/register">
            Register here
        </a>
    </p>
</div>

<footer>
    <p>&copy; 2026 ShopSphere. All rights reserved.</p>
</footer>

</body>
</html>
