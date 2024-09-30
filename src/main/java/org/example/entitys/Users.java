package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private String userType;
    private int userId;

    public boolean login(String userType, String email, String password) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM " + userType + " WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        this.userType = userType;
                        this.userId = resultSet.getInt("id");
                        System.out.println("Login bem-sucedido! Usuário é do tipo: " + userType);
                        return true;
                    } else {
                        System.out.println("Credenciais inválidas.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
        return false;
    }

    public String getUserType() {
        return userType;
    }

    public int getUserId() {
        return userId;
    }

}
