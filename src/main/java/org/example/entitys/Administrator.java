package org.example.entitys;

import org.example.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Administrator {
    private SchoolClass classManager = new SchoolClass();


    public void createUser(String userType, String name, String email, String password, Integer classId) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String insertUserSQL;
            if (userType.equalsIgnoreCase("student")) {
                insertUserSQL = "INSERT INTO student (name, email, password, class_id) VALUES (?, ?, ?, ?)";
            } else if (userType.equalsIgnoreCase("teacher")) {
                insertUserSQL = "INSERT INTO teacher (name, email, password) VALUES (?, ?, ?)";
            } else {
                System.out.println("Tipo de usuário inválido.");
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);

                if (userType.equalsIgnoreCase("student")) {
                    if (classId != null) {
                        preparedStatement.setInt(4, classId);
                    } else {
                        preparedStatement.setNull(4, java.sql.Types.INTEGER);
                    }
                }

                preparedStatement.executeUpdate();
                System.out.println(userType + " criado com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }

    public void readUser(String userType, int id) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM " + userType + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Password: " + password);


                        if (userType.equalsIgnoreCase("student")) {
                            int classId = resultSet.getInt("class_id");
                            System.out.println("Class ID: " + (resultSet.wasNull() ? "None" : classId));
                        }
                    } else {
                        System.out.println(userType + " com ID " + id + " não encontrado.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


    public void updateUser(String userType, int id, String newName, String newEmail, String newPassword) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String updateSQL = "UPDATE " + userType + " SET name = ?, email = ?, password = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newEmail);
                preparedStatement.setString(3, newPassword);
                preparedStatement.setInt(4, id);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println(userType + " atualizado com sucesso.");
                } else {
                    System.out.println(userType + " com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }


    public void deleteUser(String userType, int id) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            if (userType.equalsIgnoreCase("student")) {

                String deleteChallengeSQL = "DELETE FROM challenge_student WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteChallengeSQL)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Associações do aluno na tabela challenge_student removidas.");
                }


                String deleteArcadeSQL = "DELETE FROM arcade WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteArcadeSQL)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Associações do aluno na tabela arcade removidas.");
                }

                String deleteCompetencesSQL = "DELETE FROM student_competences WHERE student_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCompetencesSQL)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Associações do aluno na tabela student_competences removidas.");
                }
            }

            String deleteSQL = "DELETE FROM " + userType + " WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setInt(1, id);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println(userType + " deletado com sucesso.");
                } else {
                    System.out.println(userType + " com ID " + id + " não encontrado.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
    }





    public void createClass(String name, String description, int teacherId) {
        classManager.createClass(name, description, teacherId);
    }


    public void deleteClass(int classId) {
        classManager.deleteClass(classId);
    }


    public void updateClass(int classId, Integer newTeacherId) {
        classManager.updateClass(classId, newTeacherId);
    }

    public void updateStudentClass(int studentId, Integer classId) {
        classManager.updateStudentClass(studentId, classId);
    }


    public void viewAllClasses() {
        classManager.viewAllClasses();
    }
}
