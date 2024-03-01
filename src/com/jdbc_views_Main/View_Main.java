package com.jdbc_views_Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class View_Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/student";
            String user = "postgres";
            String password = "root";
            Connection connection = DriverManager.getConnection(url, user, password);

            while (true) {
                System.out.println("\nChoose operation:");
                System.out.println("1. Create");
                System.out.println("2. Read");
                System.out.println("3. Update");
                System.out.println("4. Delete");
                System.out.println("5. Find by ID Operation:");
                System.out.println("6. Find by all Operation:");
                System.out.println("7. Find by price More than 1000 operation:");
                System.out.println("8. Display in Ascending prodcut By name Operation:");
                System.out.println("9. Display in Descending product name Opeation:");
                System.out.println("10. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createRecord(scanner, connection);
                        break;
                    case 2:
                        readRecords(connection);
                        break;
                    case 3:
                        updateRecord(scanner, connection);
                        break;
                    case 4:
                        deleteRecord(scanner, connection);
                        break;
                    case 5:
                    	findById(scanner, connection);
                    	break;
                    case 6:
                    	findAll(connection);
                    	break;
                    case 7:
                    	findByPriceGreaterThan1000(connection);
                    	break;
                    case 8:
                    	displayAscendingByName(connection);
                    	break;
                    case 9:
                    	displayDescendingByName(connection);
                    	break;
                    case 10:
                        System.out.println("Exiting program.");
                        connection.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createRecord(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter product name: ");
        String productName = scanner.next();
        System.out.print("Enter description: ");
        String description = scanner.next();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity in stock: ");
        int quantityInStock = scanner.nextInt();
        System.out.print("Enter manufacturer: ");
        String manufacturer = scanner.next();
        System.out.print("Enter category: ");
        String category = scanner.next();

        String insertQuery = "INSERT INTO product (productname, description, price, quantityinstock, manufacturer, category) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, productName);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, price);
        preparedStatement.setInt(4, quantityInStock);
        preparedStatement.setString(5, manufacturer);
        preparedStatement.setString(6, category);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Record inserted successfully.");
        } else {
            System.out.println("Failed to insert record.");
        }

        preparedStatement.close();
    }

    private static void readRecords(Connection connection) throws SQLException {
        String query = "SELECT * FROM product";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No records found.");
        } else {
            System.out.println("Product Records:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");

                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }

    private static void updateRecord(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter product ID to update: ");
        int productIdToUpdate = scanner.nextInt();

        System.out.print("Enter new product name: ");
        String newName = scanner.next();
        System.out.print("Enter new description: ");
        String newDescription = scanner.next();
        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();
        System.out.print("Enter new quantity in stock: ");
        int newQuantityInStock = scanner.nextInt();
        System.out.print("Enter new manufacturer: ");
        String newManufacturer = scanner.next();
        System.out.print("Enter new category: ");
        String newCategory = scanner.next();

        String updateQuery = "UPDATE product SET productname = ?, description = ?, price = ?, quantityinstock = ?, manufacturer = ?, category = ? WHERE product_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, newDescription);
        preparedStatement.setDouble(3, newPrice);
        preparedStatement.setInt(4, newQuantityInStock);
        preparedStatement.setString(5, newManufacturer);
        preparedStatement.setString(6, newCategory);
        preparedStatement.setInt(7, productIdToUpdate);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Record updated successfully.");
        } else {
            System.out.println("Failed to update record. Product ID not found.");
        }

        preparedStatement.close();
    }

    private static void deleteRecord(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter product ID to delete: ");
        int productIdToDelete = scanner.nextInt();

        String deleteQuery = "DELETE FROM product WHERE product_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, productIdToDelete);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Record deleted successfully.");
        } else {
            System.out.println("Failed to delete record. Product ID not found.");
        }

        preparedStatement.close();
    }
    
    private static void findById(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter product ID to find: ");
        int productId = scanner.nextInt();

        String query = "SELECT * FROM product WHERE product_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No product found with ID: " + productId);
        } else {
            System.out.println("Product Record:");
            while (resultSet.next()) {
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");

                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }
    private static void findAll(Connection connection) throws SQLException {
        String query = "SELECT * FROM product";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Product Records:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");

                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }
    private static void findByPriceGreaterThan1000(Connection connection) throws SQLException {
        String query = "SELECT * FROM product WHERE price > 1000";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No products found with price greater than 1000.");
        } else {
            System.out.println("Products with Price More than 1000:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");
                
                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }
    private static void displayAscendingByName(Connection connection) throws SQLException {
        String query = "SELECT * FROM product ORDER BY productname ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Products in Ascending Order by Name:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");

                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }
    private static void displayDescendingByName(Connection connection) throws SQLException {
        String query = "SELECT * FROM product ORDER BY productname DESC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Products in Descending Order by Name:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("productname");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantityinstock");
                String manufacturer = resultSet.getString("manufacturer");
                String category = resultSet.getString("category");

                System.out.println("ProductId: "+productId + ", ProductName: " + productName + ", Description: " + description + ", Price: " + price + ", Quantity: " + quantityInStock + ", Manufacturer: " + manufacturer + ", Category: " + category);
            }
        }
        resultSet.close();
        preparedStatement.close();
    }
}
