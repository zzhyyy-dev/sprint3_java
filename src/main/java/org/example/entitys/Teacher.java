package org.example.entitys;

import org.example.exams.Challenge;
import org.example.exams.ChallengeSession;
import org.example.exams.Exercise;
import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Teacher {
    private int teacherId;


    public Teacher(int teacherId) {
        this.teacherId = teacherId;
    }


    public void readStudents() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();
            String selectSQL = "SELECT id, name, email, class_id FROM student";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
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


    public void listExercises() {
        List<Exercise> exercises = Exercise.getAllExercises();
        System.out.println("Exercícios disponíveis:");
        for (Exercise exercise : exercises) {
            System.out.println("ID: " + exercise.getId() + ", Name: " + exercise.getName() +
                    ", Description: " + exercise.getDescription() + ", Difficulty: " + exercise.getDifficulty());
        }
    }


    public void createChallenge() {
        Scanner scanner = new Scanner(System.in);
        List<String> selectedExercises = new ArrayList<>();
        int order = 1;
        boolean adding = true;

        while (adding) {
            listExercises();
            System.out.print("Selecione o ID do exercício que deseja adicionar à Challenge: ");
            String exerciseId = scanner.nextLine();


            selectedExercises.add("{\"order\":\"" + order + "\",\"id_exercises\":\"" + exerciseId + "\"}");
            order++;

            System.out.print("Deseja adicionar outro exercício? (S/N): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("N")) {
                adding = false;
            }
        }


        String exercisesJson = "[" + String.join(",", selectedExercises) + "]";


        ChallengeSession session = new ChallengeSession(exercisesJson);
        int sessionId = session.createChallengeSession();

        if (sessionId != -1) {
            System.out.print("Digite o nome da Challenge: ");
            String name = scanner.nextLine();

            System.out.print("Digite a descrição da Challenge: ");
            String description = scanner.nextLine();


            Challenge challenge = new Challenge(name, description, teacherId, sessionId);
            challenge.createChallenge();
        } else {
            System.out.println("Erro ao criar a sessão de Challenge.");
        }
    }
}
