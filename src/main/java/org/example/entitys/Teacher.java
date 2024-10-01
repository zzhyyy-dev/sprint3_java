package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher {

    public void readStudents() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            // Incluindo "class_id" no comando SELECT
            String selectSQL = "SELECT id, name, email, class_id FROM student";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    // Obtendo o "class_id" do aluno
                    int classId = resultSet.getInt("class_id");

                    System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Class ID: " + classId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


    public void getExercises() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "select id, name, exercise_tool_id, difficulty from exercises";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String exercise_tool_id = resultSet.getString("exercise_tool_id");
                        String difficulty = resultSet.getString("exercise_tool_id");

                        System.out.println("ID: " + id + ", Name: " + name + ", exercise_tool_id: " + exercise_tool_id + ", difficulty "+difficulty);
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
