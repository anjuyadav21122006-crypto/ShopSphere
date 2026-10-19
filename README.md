# ShopSphere – E-Commerce Web Application

ShopSphere is a Java-based e-commerce web application developed using Java Servlets, JSP, JDBC, MySQL, and Apache Tomcat. It provides a platform for customers to browse products, manage their shopping cart, place orders, and manage their profiles.

## Features

### Customer Features
- User registration and login
- Product browsing, searching, filtering, and sorting
- Shopping cart management
- Checkout with Cash on Delivery
- Order history and order details
- Customer profile and address management
- Wishlist functionality
- Product reviews and ratings
- Coupon code discounts

### Admin Features
- Admin dashboard
- Product management
- Category management
- Order management and status updates

## Technologies Used

- **Backend:** Java, Java Servlets, JDBC
- **Frontend:** JSP, HTML, CSS
- **Database:** MySQL
- **Server:** Apache Tomcat 11
- **Build Tool:** Apache Maven
- **Password Security:** BCrypt

## Prerequisites

Install the following before running the project:

- JDK 17 or compatible newer JDK
- Apache Maven
- MySQL Server and MySQL Workbench
- Apache Tomcat 11
- Java-compatible IDE, such as Visual Studio Code

## Setup and Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/anjuyadav21122006-crypto/ShopSphere.git
   ```

2. Open the project directory:

   ```bash
   cd ShopSphere
   ```

3. Create the required MySQL database and tables. Use the database name `shopsphere` and configure the database connection in `DBConnection.java`.

4. Update the database connection settings for your local MySQL installation. Do not commit real passwords or secret credentials.

5. Build the project:

   ```bash
   mvn clean package
   ```

6. Deploy `target/ShopSphere.war` to the `webapps` directory of Apache Tomcat 11.

7. Start Tomcat and open:

   `http://localhost:8080/ShopSphere/`

## Database

The application uses MySQL to store application data, including users, products, categories, orders, reviews, wishlists, and coupons.

**Note:** The database schema and initial data must be configured before running the application.

## Future Enhancements

- Online payment gateway integration
- Product image uploads
- Email notifications
- Order tracking
- Improved security and deployment configuration

## Author

**Anju Yadav**

## Repository

[ShopSphere on GitHub](https://github.com/anjuyadav21122006-crypto/ShopSphere)
