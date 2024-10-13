package org.example.entitys;

import org.example.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Clob;
import java.io.Reader;
import java.io.StringWriter;

public class Student {
    private int studentId;

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public void viewProfile() {
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

    public void viewCompetences() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT c.name AS competence_name, sc.score " +
                    "FROM student_competences sc " +
                    "JOIN competences c ON sc.competence_id = c.id " +
                    "WHERE sc.student_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setInt(1, studentId); // Passando o studentId correto

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Competências e Scores:");
                    boolean found = false;
                    while (resultSet.next()) {
                        found = true;
                        String competenceName = resultSet.getString("competence_name");
                        double score = resultSet.getDouble("score");
                        System.out.println("Competência: " + competenceName + ", Score: " + score);
                    }

                    if (!found) {
                        System.out.println("Nenhuma competência encontrada para o estudante com ID: " + studentId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


    public void viewChallengeScores() {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DatabaseUtil.getConnection();
            String selectChallengesSQL = "SELECT DISTINCT challenge_id FROM challenge_student WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectChallengesSQL)) {
                preparedStatement.setInt(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Challenges realizados:");
                    while (resultSet.next()) {
                        int challengeId = resultSet.getInt("challenge_id");
                        System.out.println("Challenge ID: " + challengeId);
                    }

                    System.out.println("Deseja ver o score de um Challenge específico ou de todos?");
                    System.out.println("1 - Ver score de um Challenge específico");
                    System.out.println("2 - Ver scores de todos os Challenges");
                    int choice = scanner.nextInt();

                    if (choice == 1) {
                        System.out.print("Digite o Challenge ID: ");
                        int challengeId = scanner.nextInt();
                        viewScoreForChallenge(challengeId, connection);
                    } else if (choice == 2) {
                        viewAllChallengeScores(connection);
                    } else {
                        System.out.println("Opção inválida.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    private void viewScoreForChallenge(int challengeId, Connection connection) throws SQLException {
        String selectScoreSQL = "SELECT score FROM challenge_student WHERE student_id = ? AND challenge_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectScoreSQL)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, challengeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String scoreJson = clobToString(resultSet.getClob("score"));


                    JSONArray scoreArray = new JSONArray(scoreJson);
                    System.out.println("Scores do Challenge ID " + challengeId + ":");
                    for (int i = 0; i < scoreArray.length(); i++) {
                        JSONObject exercise = scoreArray.getJSONObject(i);
                        String exerciseId = exercise.getString("exercise_id");
                        String score = exercise.getString("score");

                        String exerciseName = getExerciseNameById(exerciseId, connection);
                        System.out.println("Exercício: " + exerciseName + ", Score: " + score);
                    }
                } else {
                    System.out.println("Nenhum score encontrado para o Challenge ID " + challengeId);
                }
            }
        }
    }

    private String getExerciseNameById(String exerciseId, Connection connection) throws SQLException {
        String selectExerciseNameSQL = "SELECT name FROM exercises WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectExerciseNameSQL)) {
            preparedStatement.setString(1, exerciseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        }
        return "Exercício não encontrado";
    }

    private String clobToString(Clob clob) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream(); StringWriter writer = new StringWriter()) {
            char[] buffer = new char[2048];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytesRead);
            }
            sb.append(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void viewAllChallengeScores(Connection connection) throws SQLException {
        String selectAllScoresSQL = "SELECT challenge_id, score FROM challenge_student WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllScoresSQL)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Scores de todos os Challenges:");
                while (resultSet.next()) {
                    int challengeId = resultSet.getInt("challenge_id");


                    String scoreJson = clobToString(resultSet.getClob("score"));


                    JSONArray scoreArray = new JSONArray(scoreJson);
                    System.out.println("Challenge ID: " + challengeId);
                    for (int i = 0; i < scoreArray.length(); i++) {
                        JSONObject exercise = scoreArray.getJSONObject(i);
                        String exerciseId = exercise.getString("exercise_id");
                        String score = exercise.getString("score");


                        String exerciseName = getExerciseNameById(exerciseId, connection);
                        System.out.println("  Exercício: " + exerciseName + ", Score: " + score);
                    }
                }
            }
        }
    }


    public void viewClass() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            String selectClassSQL = "SELECT class_id FROM student WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectClassSQL)) {
                preparedStatement.setInt(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int classId = resultSet.getInt("class_id");
                        System.out.println("O estudante está na Classe com ID: " + classId);
                    } else {
                        System.out.println("Nenhuma classe associada ao estudante.");
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
