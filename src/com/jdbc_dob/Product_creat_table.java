package com.jdbc_dob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Product_creat_table {
		public static void main(String[] args) {
			try {
				Class.forName("org.postgresql.Driver");
				String url = "jdbc:postgresql://localhost:5432/student";
				String user = "postgres";
				String password = "root";
				Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				
				String create = "create table if not exists product (product_id serial primary key,productname varchar(50),description varchar(50),price bigint,quantityinstock bigint,manufacturer varchar(50),category varchar)";
				statement.execute(create);
				statement.close();
				connection.close();
				System.out.println("Table 'book' created successfully.");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
