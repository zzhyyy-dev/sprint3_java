package org.example;

import org.example.entitys.Users;
import org.example.entitys.Administrator;
import org.example.entitys.Teacher;
import org.example.entitys.Student;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Users user = new Users();
        int loginAttempts = 0;

        while (loginAttempts < 3) {
            System.out.println("===== Sistema de Login =====");
            System.out.print("Digite o tipo de usuário (teacher, student, administrator): ");
            String userType = scanner.nextLine().toLowerCase();


            if (!userType.equals("teacher") && !userType.equals("student") && !userType.equals("administrator")) {
                System.out.println("Tipo de usuário inválido. Tente novamente.");
                continue;
            }

            System.out.print("Digite seu email: ");
            String email = scanner.nextLine();

            System.out.print("Digite sua senha: ");
            String password = scanner.nextLine();


            if (user.login(userType, email, password)) {

                switch (user.getUserType()) {
                    case "administrator":
                        administratorMenu(scanner);
                        break;
                    case "teacher":
                        teacherMenu(scanner, user.getUserId());
                        break;
                    case "student":
                        studentMenu(scanner, user.getUserId());
                        break;
                    default:
                        System.out.println("Tipo de usuário desconhecido.");
                }
                return;
            } else {
                loginAttempts++;
                System.out.println("Falha no login. Tentativa " + loginAttempts + " de 3.");
            }
        }

        System.out.println("Número máximo de tentativas alcançado. Programa encerrado.");
    }


    private static void administratorMenu(Scanner scanner) {
        Administrator admin = new Administrator();
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Menu do Administrador =====");
            System.out.println("1 - Adicionar usuário (teacher ou student)");
            System.out.println("2 - Ler informações de um usuário");
            System.out.println("3 - Atualizar informações de um usuário");
            System.out.println("4 - Deletar usuário");
            System.out.println("5 - Criar uma nova classe");
            System.out.println("6 - Deletar uma classe");
            System.out.println("7 - Atualizar professor de uma classe");
            System.out.println("8 - Visualizar todas as classes");
            System.out.println("9 - Atualizar a classe de um aluno");
            System.out.println("10 - Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Digite o tipo de usuário para adicionar (teacher, student): ");
                    String userType = scanner.nextLine().toLowerCase();
                    System.out.print("Digite o nome do usuário: ");
                    String name = scanner.nextLine();
                    System.out.print("Digite o email do usuário: ");
                    String email = scanner.nextLine();
                    System.out.print("Digite a senha do usuário: ");
                    String password = scanner.nextLine();

                    Integer classId = null;
                    if (userType.equals("student")) {
                        System.out.print("Digite o ID da classe para associar ao aluno (ou 0 para nenhum): ");
                        int inputClassId = scanner.nextInt();
                        scanner.nextLine();
                        classId = (inputClassId != 0) ? inputClassId : null;
                    }

                    admin.createUser(userType, name, email, password, classId);
                    break;

                case 2:
                    System.out.print("Digite o tipo de usuário (teacher, student): ");
                    userType = scanner.nextLine().toLowerCase();
                    System.out.print("Digite o ID do usuário: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    admin.readUser(userType, userId);
                    break;

                case 3:
                    System.out.print("Digite o tipo de usuário (teacher, student): ");
                    userType = scanner.nextLine().toLowerCase();
                    System.out.print("Digite o ID do usuário: ");
                    userId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Digite o novo nome: ");
                    String newName = scanner.nextLine();
                    System.out.print("Digite o novo email: ");
                    String newEmail = scanner.nextLine();
                    System.out.print("Digite a nova senha: ");
                    String newPassword = scanner.nextLine();
                    admin.updateUser(userType, userId, newName, newEmail, newPassword);
                    break;

                case 4:
                    System.out.print("Digite o tipo de usuário (teacher, student): ");
                    userType = scanner.nextLine().toLowerCase();
                    System.out.print("Digite o ID do usuário: ");
                    userId = scanner.nextInt();
                    scanner.nextLine();
                    admin.deleteUser(userType, userId);
                    break;

                case 5:
                    System.out.print("Digite o nome da classe: ");
                    String className = scanner.nextLine();
                    System.out.print("Digite a descrição da classe: ");
                    String description = scanner.nextLine();
                    System.out.print("Digite o ID do professor para associar à classe: ");
                    int teacherId = scanner.nextInt();
                    scanner.nextLine();

                    admin.createClass(className, description, teacherId);
                    break;

                case 6:
                    System.out.print("Digite o ID da classe a ser deletada: ");
                    int classIdToDelete = scanner.nextInt();
                    scanner.nextLine();
                    admin.deleteClass(classIdToDelete);
                    break;

                case 7:
                    System.out.print("Digite o ID da classe a ser atualizada: ");
                    int classIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Digite o novo ID do professor: ");
                    int newTeacherId = scanner.nextInt();
                    scanner.nextLine();
                    Integer teacherIdToUpdate = (newTeacherId != 0) ? newTeacherId : null;

                    admin.updateClass(classIdToUpdate, teacherIdToUpdate);
                    break;

                case 8:
                    admin.viewAllClasses();
                    break;

                case 9:
                    System.out.print("Digite o ID do aluno: ");
                    int studentIdToUpdate = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Digite o novo ID da classe para associar ao aluno (ou 0 para nenhum): ");
                    int newClassId = scanner.nextInt();
                    scanner.nextLine();
                    Integer studentClassId = (newClassId != 0) ? newClassId : null;
                    admin.updateStudentClass(studentIdToUpdate, studentClassId);
                    break;

                case 10:
                    exit = true;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void teacherMenu(Scanner scanner, int teacherId) {
        Teacher teacher = new Teacher(teacherId);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Menu do Professor =====");
            System.out.println("1 - Ler informações dos alunos");
            System.out.println("2 - Ver exercícios disponíveis");
            System.out.println("3 - Criar uma nova Challenge");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    teacher.readStudents();
                    break;

                case 2:

                    teacher.listExercises();
                    break;

                case 3:

                    teacher.createChallenge();
                    break;

                case 4:

                    exit = true;
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void studentMenu(Scanner scanner, int studentId) {
        Student student = new Student(studentId);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== Menu do Estudante =====");
            System.out.println("1 - Ver informações do próprio cadastro");
            System.out.println("2 - Ver competências e scores");
            System.out.println("3 - Ver scores dos Challenges");
            System.out.println("4 - Ver classe");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    student.viewProfile();
                    break;
                case 2:
                    student.viewCompetences();
                    break;
                case 3:
                    student.viewChallengeScores();
                    break;
                case 4:
                    student.viewClass();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


}
