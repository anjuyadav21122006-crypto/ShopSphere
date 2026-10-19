
import com.shopsphere.util.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest {

    public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("MySQL Connected Successfully!");
            System.out.println("ShopSphere Products:");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT product_id, name, price FROM products"
            );

            while (rs.next()) {
                System.out.println(
                    rs.getInt("product_id") + " | " +
                    rs.getString("name") + " | Rs. " +
                    rs.getBigDecimal("price")
                );
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
}
