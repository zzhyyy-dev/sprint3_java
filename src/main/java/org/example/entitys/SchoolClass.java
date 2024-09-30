package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SchoolClass {


    public void createClass(String name, String description, int teacherId, List<Integer> studentIds) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String insertClassSQL = "INSERT INTO class (name, description, teacher_id) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertClassSQL, new String[]{"ID"})) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, teacherId);


                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                int classId = -1;
                if (generatedKeys.next()) {
                    classId = generatedKeys.getInt(1);
                }


                if (classId != -1) {
                    System.out.println("Classe criada com sucesso. ID da Classe: " + classId);

                    // Associate students with the new class by updating their class_id
                    for (int studentId : studentIds) {
                        updateStudentClass(studentId, classId);
                    }
                } else {
                    System.out.println("Erro ao criar a classe. ID da classe não gerado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public void updateStudentClass(int studentId, Integer classId) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String updateSQL = "UPDATE student SET class_id = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                if (classId != null) {
                    preparedStatement.setInt(1, classId);
                } else {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                }
                preparedStatement.setInt(2, studentId);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Aluno com ID " + studentId + " associado à classe com ID " + classId);
                } else {
                    System.out.println("Aluno com ID " + studentId + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public void deleteClass(int classId) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String deleteSQL = "DELETE FROM class WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, classId);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Classe deletada com sucesso.");
                } else {
                    System.out.println("Classe não encontrada.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public void updateClass(int classId, Integer newTeacherId, List<Integer> newStudentIds) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            if (newTeacherId != null) {
                String updateTeacherSQL = "UPDATE class SET teacher_id = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateTeacherSQL)) {
                    preparedStatement.setInt(1, newTeacherId);
                    preparedStatement.setInt(2, classId);
                    preparedStatement.executeUpdate();
                    System.out.println("Professor da classe atualizado com sucesso.");
                }
            }

            if (newStudentIds != null && !newStudentIds.isEmpty()) {
                String deleteStudentsSQL = "DELETE FROM class_students WHERE class_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentsSQL)) {
                    preparedStatement.setInt(1, classId);
                    preparedStatement.executeUpdate();
                }

                for (int studentId : newStudentIds) {
                    String addStudentSQL = "INSERT INTO class_students (class_id, student_id) VALUES (?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(addStudentSQL)) {
                        preparedStatement.setInt(1, classId);
                        preparedStatement.setInt(2, studentId);
                        preparedStatement.executeUpdate();
                    }
                }
                System.out.println("Alunos da classe atualizados com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public void viewAllClasses() {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT id, name, description, teacher_id FROM class";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                System.out.println("Lista de todas as classes:");
                while (resultSet.next()) {
                    int classId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int teacherId = resultSet.getInt("teacher_id");
                    System.out.println("Class ID: " + classId + ", Name: " + name + ", Description: " + description + ", Teacher ID: " + teacherId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

}
