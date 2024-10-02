package org.example.entitys;


public class Administrator {

    private SchoolClass classManager = new SchoolClass();
    private Users users = new Users();

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

    public void createUser(String userType, String name, String email, String password, Integer classId) {
        users.createUser(userType,name, email,password,classId);
    }

    public void readUser(String userType, int id) {
       users.readUser(userType, id);
    }
    
    public void updateUser(String userType, int id, String newName, String newEmail, String newPassword) {
        users.updateUser(userType, id, newName, newEmail, newPassword);
    }

    public void deleteUser(String userType, int id) {
        users.deleteUser(userType, id);
    }


}
