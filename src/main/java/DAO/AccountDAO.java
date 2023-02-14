package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AccountDAO {
    // register new account
    public Account registerAccount(Account account) {
        System.out.println("6");
        if(account.password.length() >= 4 && account.username.length() > 0){
            /**   
            *  System.out.println("8");
            *  return null; 
            */
        System.out.println("7");
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic
            String sql = "INSERT INTO account(username, password) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //preparedStatement's setString and setInt methods
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int generated_account_id = (int) pKeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
        return null;
    } 
    
    public Account login(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            // SQL logic
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //preparedStatement's setString and setInt methods
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            // Validate user, return null
        System.out.println("a");
        if (!rs.next()) {
            System.out.println("b");
            return null;
        }
        // Extract the user information from the result set
        System.out.println("c");
        int accountId = rs.getInt("account_id");
        System.out.println("d");
        String username = rs.getString("username");
        String password = rs.getString("password");
        System.out.println(accountId);
        System.out.println(username);

        // Return the user information
        return new Account(accountId, username,  password);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
