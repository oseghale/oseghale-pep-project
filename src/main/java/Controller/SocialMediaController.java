package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesHandler);
        return app;
    }

    /**
     * Handler to post a new account/register.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Author object.
     * If AccountService returns a null author (meaning posting an Author was unsuccessful), the API will return a 400
     * message (client error). There is no need to change anything in this method.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        try{
            Account registerAccount = accountService.addAccount(account);
            if(registerAccount!=null){
                ctx.json(mapper.writeValueAsString(registerAccount));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
        }catch(Exception e){
            e.printStackTrace();
            ctx.status(400);
        
        }
        
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.login(account);
        if(loginAccount!=null){
            ctx.json(mapper.writeValueAsString(loginAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createMessage = MessageService.createMessage(message);
        if(createMessage!=null){
            ctx.json(mapper.writeValueAsString(createMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }

    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages(null);
        ctx.json(messages);
        ctx.status(200);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        System.out.println("!");
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        if(messageService.getMessageById(message_id) != null){
            try{
                Message message = messageService.getMessageById(message_id);
                ctx.json(message);
                ctx.status(200);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
       
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        if(messageService.getMessageById(message_id) != null){
            try{
         Message deletedMessage = MessageService.deleteMessageById(message_id);
            ctx.json(deletedMessage);
            ctx.status(200);
            }catch(Exception e){
                e.printStackTrace();
                ctx.status(200);
            }
        }else{
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        //String body = ctx.body();
        String newMessageText = message.message_text;
        if(newMessageText.length() > 0 && newMessageText.length() <= 255){
        
        try {
            MessageService.updateMessage(message_id, newMessageText);
            ctx.status(400);
            ctx.json(MessageService.getMessage(message_id));
            ctx.status(400);
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(400);
        }
    }else{
        ctx.status(400);
    }
    }

    private void getMessagesHandler(Context ctx) throws JsonProcessingException, SQLException{
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        ctx.json(messages);
        ctx.status(200);

    }


}