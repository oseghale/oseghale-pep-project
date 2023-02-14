package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;
import io.javalin.http.BadRequestResponse;


public class MessageDAO {
    // create new account
    public Message createMessage(Message message) {
        System.out.println("6");
       int posted_by = 0;

        System.out.println(message);
        if(message.message_text.length() > 0 && message.message_text.length() < 255 && message.posted_by > posted_by){
        
            /**   
            *  System.out.println("8");
            *  return null; 
            */
        System.out.println("7");
        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL logic
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //preparedStatement's setString and setInt methods
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pKeyResultSet = preparedStatement.getGeneratedKeys();
            if(pKeyResultSet.next()){
                int generated_message_id = (int) pKeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
        return null;
    }

    // to get all messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // get message by Id
    public Message getMessageById(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * from message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // delete message by Id
    public Message deleteMessageById(int message_id){
        System.out.println("%");
        Connection connection = ConnectionUtil.getConnection();
        // write SQL logic
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM message WHERE message_id = ?")) {
            System.out.println("^");
            //write preparedStatement's setInt method here.
            statement.setInt(1, message_id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    System.out.println("&");
                    Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                    try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM message WHERE message_id = ?")) {
                        System.out.println("*");
                        deleteStatement.setInt(1, message_id);
                        deleteStatement.executeUpdate();
                        System.out.println("()");
                        return message;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // patch/update a message

    private static List<Message> messages = new ArrayList<>();
    public Message updateMessage(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection(); 
        try{  
        if (message_text == null || message_text.length() == 0 || message_text.length() > 255) {
            return null;
         }
         
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql); {
            statement.setString(1, message_text);
            statement.setInt(2, message_id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
               throw new SQLException("The message with id " + message_id + " could not be found");
            }
         }
        }catch(SQLException e){
            e.printStackTrace();

        }
        return null;
    }
    

    public Message getMessage(int message_id) {
        System.out.println("@");
        for (Message message : messages) {
            System.out.println("#");
            if (message.getMessage_id() == message_id) {
                return message;
            }
        }
        return null;
    }
    

    // get all messages by user/account_id

    public List<Message> getMessages(int account_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
           statement.setInt(1, account_id);
           try (ResultSet rs = statement.executeQuery()) {
              while (rs.next()) {
                 Message message = new Message(rs.getInt("message_id"),
                 rs.getInt("posted_by"),
                 rs.getString("message_text"),
                 rs.getLong("time_posted_epoch"));
                 userMessages.add(message);
              }
           }
        }
        return userMessages;
     }
    
}
