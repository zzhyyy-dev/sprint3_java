package org.example.exams;

import org.example.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Challenge {
    private int id;
    private String name;
    private String description;
    private int teacherId;
    private int challengeSessionId;

    // Construtor
    public Challenge(String name, String description, int teacherId, int challengeSessionId) {
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
        this.challengeSessionId = challengeSessionId;
    }

    public int createChallenge() {
        Connection connection = null;
        int challengeId = -1; // Armazena o ID da challenge

        try {
            connection = DatabaseUtil.getConnection();
            String insertSQL = "INSERT INTO challenge (name, description, teacher_id, challenge_session_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, new String[]{"id"})) {
                preparedStatement.setString(1, this.name);
                preparedStatement.setString(2, this.description);
                preparedStatement.setInt(3, this.teacherId);
                preparedStatement.setInt(4, this.challengeSessionId);
                preparedStatement.executeUpdate();


                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    challengeId = generatedKeys.getInt(1);
                    System.out.println("Challenge criada com sucesso! ID da Challenge: " + challengeId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }

        return challengeId;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getChallengeSessionId() {
        return challengeSessionId;
    }

    public void setChallengeSessionId(int challengeSessionId) {
        this.challengeSessionId = challengeSessionId;
    }
}
