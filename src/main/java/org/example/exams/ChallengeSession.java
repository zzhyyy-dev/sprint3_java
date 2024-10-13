package org.example.exams;

import org.example.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChallengeSession {
    private int id;
    private String exercisesJson;

    public ChallengeSession(String exercisesJson) {
        this.exercisesJson = exercisesJson;
    }

    public int createChallengeSession() {
        Connection connection = null;
        int challengeSessionId = -1;

        try {
            connection = DatabaseUtil.getConnection();
            String insertSQL = "INSERT INTO challenge_session (exercises) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, new String[]{"id"})) {
                preparedStatement.setString(1, exercisesJson);
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    challengeSessionId = generatedKeys.getInt(1);
                }

            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return challengeSessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExercisesJson() {
        return exercisesJson;
    }

    public void setExercisesJson(String exercisesJson) {
        this.exercisesJson = exercisesJson;
    }
}
