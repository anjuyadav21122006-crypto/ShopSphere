
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - ShopSphere</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .register-box {
            max-width: 420px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }

        .register-box h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        .register-box label {
            display: block;
            margin: 12px 0 6px;
        }

        .register-box input {
            width: 100%;
            padding: 11px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .register-box button {
            width: 100%;
            margin-top: 20px;
            padding: 12px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .register-box button:hover {
            background: #0056b3;
        }

        .register-box p {
            text-align: center;
            margin-top: 18px;
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

<div class="register-box">
    <h2>Create Your Account</h2>

    <form action="${pageContext.request.contextPath}/register"
          method="post">

        <label for="name">Full Name</label>
        <input type="text" id="name" name="name"
               required maxlength="100">

        <label for="email">Email Address</label>
        <input type="email" id="email" name="email"
               required maxlength="150">

        <label for="password">Password</label>
        <input type="password" id="password" name="password"
               required minlength="8" maxlength="72"
               autocomplete="new-password">

        <button type="submit">Create Account</button>
    </form>

    <p>Already have an account?
        <a href="${pageContext.request.contextPath}/login">
            Login here
        </a>
    </p>
</div>

<footer>
    <p>&copy; 2026 ShopSphere. All rights reserved.</p>
</footer>

</body>
</html>
