
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopSphere | Online Shopping</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f6fa;
            color: #222;
        }

        header {
            background: #172554;
            color: white;
            padding: 22px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        header h1 {
            font-size: 27px;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 18px;
        }

        .hero {
            background: #dbeafe;
            padding: 55px 7%;
            text-align: center;
        }

        .hero h2 {
            font-size: 36px;
            margin-bottom: 12px;
        }

        .hero p {
            margin-bottom: 22px;
        }

        .hero a {
            background: #172554;
            color: white;
            padding: 12px 22px;
            border-radius: 6px;
            text-decoration: none;
            display: inline-block;
        }

        .products {
            padding: 35px 7%;
        }

        .products h2 {
            margin-bottom: 25px;
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
            gap: 22px;
        }

        .product-card {
            background: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 3px 12px #00000012;
        }

        .product-card h3 {
            margin: 12px 0;
        }

        .product-card p {
            color: #555;
            margin-bottom: 12px;
        }

        .price {
            font-size: 21px;
            font-weight: bold;
            color: #172554;
        }

        footer {
            background: #172554;
            color: white;
            padding: 20px;
            text-align: center;
            margin-top: 30px;
        }

        @media (max-width: 500px) {
            header {
                flex-direction: column;
            }

            nav a {
                margin: 5px;
            }

            .hero h2 {
                font-size: 28px;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>ShopSphere</h1>

    <nav>
        <a href="index.jsp">Home</a>
        <a href="products">Products</a>
        <a href="login.jsp">Login</a>
        <a href="register.jsp">Register</a>
    </nav>
</header>

<section class="hero">
    <h2>Welcome to ShopSphere!</h2>
    <p>Discover amazing products at great prices.</p>
    <a href="products">Explore Products</a>
</section>

<section class="products">
    <h2>Why Shop With Us?</h2>

    <div class="product-grid">
        <div class="product-card">
            <h3>Quality Products</h3>
            <p>Explore products across different categories.</p>
        </div>

        <div class="product-card">
            <h3>Great Prices</h3>
            <p>Find products that fit your budget.</p>
        </div>

        <div class="product-card">
            <h3>Easy Shopping</h3>
            <p>Shop, manage your cart, and track your orders.</p>
        </div>
    </div>
</section>

<footer>
    <p>© 2026 ShopSphere | Java E-Commerce Project</p>
</footer>

</body>
</html>
