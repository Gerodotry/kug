package org.example.Sockets.Server;

import org.example.Core.Models.Group;
import org.example.Core.Models.Student;
import org.example.Core.Repositories.Repository;
import org.example.Utils.Serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class RemoteServerRepository implements Repository {
    private final ServerSocket serverSocket;

    private final DataInputStream in;
    private final DataOutputStream out;

    private final Repository repository;

    public RemoteServerRepository(Repository repository, int port) {
        try {
            serverSocket = new ServerSocket(8080);

            Socket socket = serverSocket.accept();

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.repository = repository;
    }

    @Override
    public int countGroups() throws RemoteException {
        return repository.countGroups();
    }

    @Override
    public int countStudents() throws RemoteException {
        return repository.countStudents();
    }

    @Override
    public void updateGroup(Group group) throws RemoteException {
        repository.updateGroup(group);
    }

    @Override
    public void updateStudent(Student student) throws RemoteException {
        repository.updateStudent(student);
    }

    @Override
    public void moveStudentToGroup(int studentId, int groupId) throws RemoteException {
        repository.moveStudentToGroup(studentId, groupId);
    }

    @Override
    public void insertGroup(Group group) throws RemoteException {
        repository.insertGroup(group);
    }

    @Override
    public void insertStudent(int groupId, Student student) throws RemoteException {
        repository.insertStudent(groupId, student);
    }

    @Override
    public void deleteGroup(int id) throws RemoteException {
        repository.deleteGroup(id);
    }

    @Override
    public void deleteStudent(int id) throws RemoteException {
        repository.deleteStudent(id);
    }

    @Override
    public Group getGroup(int id) throws RemoteException {
        return repository.getGroup(id);
    }

    @Override
    public Student getStudent(int id) throws RemoteException {
        return repository.getStudent(id);
    }

    @Override
    public List<Group> getGroups() throws RemoteException {
        return repository.getGroups();
    }

    @Override
    public List<Student> getStudents() throws RemoteException {
        return repository.getStudents();
    }

    void start() {
        try {
            while (true) {
                int operation = in.readInt();
                System.out.println("Received operation: " + operation);

                boolean isOk = switch (operation) {
                    case 0 -> {
                        out.writeInt(repository.countGroups());

                        yield true;
                    }
                    case 1 -> {
                        out.writeInt(repository.countStudents());

                        yield true;
                    }
                    case 2 -> {
                        int size = in.readInt();
                        byte[] bytes = new byte[size];
                        in.readFully(bytes);

                        repository.updateGroup((Group) Serialization.fromBytes(bytes));

                        yield true;
                    }
                    case 3 -> {
                        int size = in.readInt();
                        byte[] bytes = new byte[size];
                        in.readFully(bytes);

                        repository.updateStudent((Student) Serialization.fromBytes(bytes));

                        yield true;
                    }
                    case 4 -> {
                        int studentId = in.readInt();
                        int groupId = in.readInt();

                        repository.moveStudentToGroup(studentId, groupId);

                        yield true;
                    }
                    case 5 -> {
                        int size = in.readInt();
                        byte[] bytes = new byte[size];
                        in.readFully(bytes);

                        repository.insertGroup((Group) Serialization.fromBytes(bytes));

                        yield true;
                    }
                    case 6 -> {
                        int groupId = in.readInt();

                        int size = in.readInt();
                        byte[] bytes = new byte[size];
                        in.readFully(bytes);

                        repository.insertStudent(groupId, (Student) Serialization.fromBytes(bytes));

                        yield true;
                    }
                    case 7 -> {
                        int id = in.readInt();

                        repository.deleteGroup(id);

                        yield true;
                    }
                    case 8 -> {
                        int id = in.readInt();

                        repository.deleteStudent(id);

                        yield true;
                    }
                    case 9 -> {
                        int id = in.readInt();

                        Group group = repository.getGroup(id);
                        String bytes = Serialization.toString(group);
                        out.writeInt(bytes.length());
                        out.writeBytes(bytes);

                        yield true;
                    }
                    case 10 -> {
                        int id = in.readInt();

                        Student student = repository.getStudent(id);
                        String bytes = Serialization.toString(student);
                        out.writeInt(bytes.length());
                        out.writeBytes(bytes);

                        yield true;
                    }
                    case 11 -> {
                        List<Group> groups = repository.getGroups();

                        out.writeInt(groups.size());

                        for (Group group: groups) {
                            String bytes = Serialization.toString(group);
                            out.writeInt(bytes.length());
                            out.writeBytes(bytes);
                        }

                        yield true;
                    }
                    case 12 -> {
                        List<Student> students = repository.getStudents();

                        out.writeInt(students.size());

                        for (Student student: students) {
                            String bytes = Serialization.toString(student);
                            out.writeInt(bytes.length());
                            out.writeBytes(bytes);
                        }

                        yield true;
                    }
                    default -> {
                        System.out.println("Unsupported operation");
                        yield false;
                    }
                };

                if (!isOk) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
