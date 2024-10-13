package org.example.exams;

import org.example.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private String difficulty;

    public Exercise(int id, String name, String description, String difficulty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
    }

    public static List<Exercise> getAllExercises() {
        Connection connection = null;
        List<Exercise> exercises = new ArrayList<>();
        try {
            connection = DatabaseUtil.getConnection();
            String selectSQL = "SELECT id, name, description, difficulty FROM exercises";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String difficulty = resultSet.getString("difficulty");

                    exercises.add(new Exercise(id, name, description, difficulty));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return exercises;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
