package org.example.JJ.Sockets.Client;

import org.example.JJ.Models.Group;
import org.example.JJ.Models.Student;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try {
            RemoteClientRepository repository = new RemoteClientRepository("localhost", 8080);

            System.out.println("Groups: " + repository.countGroups());
            System.out.println("Students: " + repository.countStudents());

            Group group = new Group();
            group.setName("Calculus");
            group.setYear(20);

            //repository.insertGroup(group);

            Student student = new Student();
            student.setName("Andrew");
            //repository.insertStudent(2, student);

            //repository.deleteGroup(1);
            //repository.deleteStudent(10);

            System.out.println("Group: " + repository.getGroup(0));
            System.out.println("Student: " + repository.getStudent(13));

            System.out.println("Groups: " + repository.getGroups());
            System.out.println("Students: " + repository.getStudents());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
