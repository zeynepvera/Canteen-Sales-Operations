/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canteen.sales.operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author WINCHESTER
 */
public class DatabaseManager {

    static ArrayList<Person> personlist = new ArrayList();

    static Person loggedin = null;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/myschema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";

    static {
        loadUsersFromDatabase();
    }

    private static void loadUsersFromDatabase() {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {

            String sql = "SELECT*FROM USERS_INFO";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {

                    Person person = new Person();
                    
                    person.name = resultSet.getString("NAME");
                    person.surname = resultSet.getString("SURNAME");
                    person.username = resultSet.getString("USERNAME");
                    person.phonenumber = resultSet.getString("PHONE_NUMBER");
                    person.password = resultSet.getString("PASSWORD");
                    person.foodQuestion = resultSet.getString("FAVORITE_FOOD");
                    personlist.add(person);

                }
            }
        } catch (SQLException ex) {

            ex.printStackTrace();
            System.out.println("Error loading users from the database");
        }

    }
    public static Person getloggedInUser(){
        
        return loggedin;
    }

    public static boolean AddPerson(Person person) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {

            String sql = "INSERT INTO USERS_INFO (NAME,SURNAME,USERNAME,PHONE_NUMBER,PASSWORD,FAVORITE_FOOD) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, person.name);
                preparedStatement.setString(2, person.surname);
                preparedStatement.setString(3, person.username);
                preparedStatement.setString(4, person.phonenumber);
                preparedStatement.setString(5, person.password);
                preparedStatement.setString(6, person.foodQuestion);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {

                    personlist.add(person); //add in memory list
                    return true;
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error adding a person to the database chekck!");
        }
        return false;

    }
    


    public static boolean deletePerson(String username) {

        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM USERS_INFO WHERE USERNAME= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    //remove from the in-memory list

                    personlist.removeIf(person -> person.username.equals(username));
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error deleting a person from the database.");
        }
        return false;
    }

    public static Person login(String username, String password) {
        for (Person p : personlist) {
            if (p.username.equals(username) && p.password.equals(password)) {
                return p;
            }
        }
        return null;
    }

    
    
    public static Person deleteKullanıcı(String username, String password){
        for(Person p : personlist){
            
            if (p.username.equals(username) && p.password.equals(password)) {
                
                personlist.remove(p);
            }
            
        }
        return null;
    }
}
