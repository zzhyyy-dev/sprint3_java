package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Administrator {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            String entity = "authenticator";
            connection = DatabaseUtil.getConnection();

            String insertAuthenticatorSQL = "INSERT INTO " + entity  + "(mail, password, typeUser, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertAuthenticatorSQL)) {
                preparedStatement.setString(1, "email@example.com");
                preparedStatement.setString(2, "password123");
                preparedStatement.setString(3, "student");
                preparedStatement.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
                System.out.println("Authenticator inserido com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }
}