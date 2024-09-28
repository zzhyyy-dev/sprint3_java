package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {

    public static void main(String[] args) {

//        getAllStudents();
        createStudent("gustavo", "email@example.com", "password123", "1");
//        readStudent(2);
//        updateStudent(3, "novo_nome", "new_email@example.com", "new_password123");
//        deleteStudent(4);
    }


    public static void getAllStudents() {
        Connection connection = null;
        try {
            String entity = "Student";
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM " + entity;
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");

                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Password: " + password);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


    public static void createStudent(String name, String email, String password, String classWork) {
        Connection connection = null;
        try {
            String entity = "Student";
            connection = DatabaseUtil.getConnection();

            String insertSQL = "INSERT INTO " + entity + " (name, email, password, class) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, classWork);

                preparedStatement.executeUpdate();
                System.out.println(entity + " inserido com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public static void readStudent(int id) {
        Connection connection = null;
        try {
            String entity = "Student";
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM " + entity + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Password: " + password);
                    } else {
                        System.out.println(entity + " com ID " + id + " não encontrado.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public static void updateStudent(int id, String newName, String newEmail, String newPassword) {
        Connection connection = null;
        try {
            String entity = "Student";
            connection = DatabaseUtil.getConnection();

            String updateSQL = "UPDATE " + entity + " SET name = ?, email = ?, password = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newEmail);
                preparedStatement.setString(3, newPassword);
                preparedStatement.setInt(4, id);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println(entity + " atualizado com sucesso.");
                } else {
                    System.out.println(entity + " com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public static void deleteStudent(int id) {
        Connection connection = null;
        try {
            String entity = "Student";
            connection = DatabaseUtil.getConnection();

            String deleteSQL = "DELETE FROM " + entity + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, id);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println(entity + " deletado com sucesso.");
                } else {
                    System.out.println(entity + " com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


}
