package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {

    public void viewProfile(int studentId) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM student WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setInt(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        System.out.println("ID: " + studentId + ", Name: " + name + ", Email: " + email);
                    } else {
                        System.out.println("Estudante com ID " + studentId + " não encontrado.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }
}
