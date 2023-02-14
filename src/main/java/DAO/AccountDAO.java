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
        if(account.password.length() >= 4 && account.username.length() > 0){
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


    // to login account
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
        if (!rs.next()) {
            return null;
        }
        // Extract the user information from the result set
        int accountId = rs.getInt("account_id");
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
