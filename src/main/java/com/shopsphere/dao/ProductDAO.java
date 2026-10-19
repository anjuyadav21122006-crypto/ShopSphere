
package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> findAllActive() throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, name, brand, description, " +
                     "price, discount, stock, image_url " +
                     "FROM products WHERE status = 1 " +
                     "ORDER BY product_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }

        return products;
    }

    public List<Product> searchFilterSort(
            String search, String category, String sort)
            throws SQLException {

        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT p.product_id, p.name, p.brand, p.description, " +
            "p.price, p.discount, p.stock, p.image_url " +
            "FROM products p " +
            "WHERE p.status = 1 "
        );

        if (search != null && !search.trim().isEmpty()) {
            sql.append(
                "AND (p.name LIKE ? OR p.brand LIKE ? " +
                "OR p.description LIKE ?) "
            );
        }

        boolean filterCategory =
            category != null && !category.trim().isEmpty();

        if (filterCategory) {
            sql.append("AND p.category_id = ? ");
        }

        switch (sort == null ? "default" : sort) {
            case "price_low":
                sql.append("ORDER BY p.price ASC");
                break;

            case "price_high":
                sql.append("ORDER BY p.price DESC");
                break;

            case "name_asc":
                sql.append("ORDER BY p.name ASC");
                break;

            default:
                sql.append("ORDER BY p.product_id DESC");
                break;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                 con.prepareStatement(sql.toString())) {

            int index = 1;

            if (search != null && !search.trim().isEmpty()) {
                String keyword = "%" + search.trim() + "%";

                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }

            if (filterCategory) {
                try {
                    ps.setInt(index, Integer.parseInt(category.trim()));
                } catch (NumberFormatException e) {
                    return products;
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }

        return products;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("brand"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getBigDecimal("discount"),
            rs.getInt("stock"),
            rs.getString("image_url")
        );
    }
}
