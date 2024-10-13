package org.example.entitys;
import org.example.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private String userType;
    private int userId;

    public String getUserType() {
        return userType;
    }

    public int getUserId() {
        return userId;
    }

    public boolean login(String userType, String email, String password) {
        Connection connection = null;
        try {
            connection = DatabaseUtil.getConnection();

            String selectSQL = "SELECT * FROM " + userType + " WHERE EMAIL = ? AND PASSWORD = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        this.userType = userType;
                        this.userId = resultSet.getInt("id");
                        System.out.println("Login bem-sucedido! Usuário é do tipo: " + userType);
                        return true;
                    } else {
                        System.out.println("Credenciais inválidas.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeConnection(connection);
        }
        return false;
    }

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
            } else if (userType.equalsIgnoreCase("teacher")) {
                String updateSchoolClassSQL = "UPDATE SchoolClass SET teacher_id = NULL WHERE teacher_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateSchoolClassSQL)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Associações do professor na tabela SchoolClass removidas.");
                }


                String updateChallengeSQL = "UPDATE challenge SET teacher_id = NULL WHERE teacher_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateChallengeSQL)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                    System.out.println("Associações do professor na tabela challenge removidas.");
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

}
