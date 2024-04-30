/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package canteen.sales.operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
/**
 *
 * @author WINCHESTER
 */
public class PasswordResetManager {
    
    private static final String JDBC_URL="jdbc:mysql://localhost:3306/myschema";
    private static final String JDBC_USER="root";
    private static final String JDBC_PASSWORD="123";
    
    
    public boolean resetPassword(String phoneNumber, String favFoodAnswer, String newPassword){
        
if( isValidFavFoodQuestion(phoneNumber,favFoodAnswer)){
          
          return updatePasswordInDatabase(phoneNumber,newPassword);
      }else{
          
          System.out.println("Invalid answer");
      }
        return false;
         
    }
    
    
    private static boolean isValidFavFoodQuestion(String phoneNumber,String favFoodAnswer){
        
        String selectQuery= "SELECT*FROM USERS_INFO WHERE PHONE_NUMBER=? AND FAVORITE_FOOD=?";
        
        try(Connection connection= DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
              PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)){
            
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, favFoodAnswer);
            
            ResultSet resultSet= preparedStatement.executeQuery();
            return resultSet.next();
        } catch(SQLException e){
         e.printStackTrace();
        }
        
        return false; 
    }
    
    private static boolean updatePasswordInDatabase(String phoneNumber,String newPassword){
       
        String updateQuery= "UPDATE USERS_INFO SET PASSWORD = ? WHERE PHONE_NUMBER=?";
        
        try( Connection connection=DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
                PreparedStatement prepareStatement = connection.prepareStatement(updateQuery)){
           
            prepareStatement.setString(1, newPassword);
            prepareStatement.setString(2, phoneNumber);
            
            int rowsAffected=prepareStatement.executeUpdate();
            
            if (rowsAffected>0) {
                System.out.println("Password updated successfully..");
                return true;
            }else{
                System.out.println("Failed to update password. User not found.");
           return false;
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            
        }
        return false;
    }

   
}
