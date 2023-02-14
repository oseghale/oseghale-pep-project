package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    /**
    * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
    * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
    * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
    * actions undertaken by the API to a logging file.
    *
    * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
    * application that follows best practices will often have unnecessary code, but this makes the code more
   * readable and maintainable in the long run!
    */
    private static MessageDAO messageDAO;

    /**
     * no-args constructor for creating a new MessageService with a new MessageDAO.
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO.
     * There is no need to modify this constructor.
     * @param authorDAO
     */
    public MessageService(MessageDAO messageDAO){
        MessageService.messageDAO = messageDAO;
    }

    /**
     * TODO: Use the MessageDAO to persist a message. The given Message will not have an id provided.
     *
     * @param account an account object.
     * @return The persisted account if the persistence is successful.
     */
    public static Message createMessage(Message message) {
        System.out.println("5");
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(Message message){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public static Message deleteMessageById(int message_id){
        System.out.println("#");
        return messageDAO.deleteMessageById(message_id);
    }

    public static Message updateMessage(int message_id, String message_text){
        return messageDAO.updateMessage(message_id, message_text);
    }

    public static Message getMessage(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getMessagesByAccountId(int account_id) throws SQLException {
        return messageDAO.getMessages(account_id);
     }
    
}
