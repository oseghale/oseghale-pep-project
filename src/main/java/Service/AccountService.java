package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
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
    private AccountDAO accountDAO;

    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for a AccountService when a AccountDAO is provided.
     * This is used for when a mock AccountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AccountService independently of AccountDAO.
     * There is no need to modify this constructor.
     * @param authorDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * TODO: Use the AccountDAO to persist an account. The given Account will not have an id provided.
     *
     * @param account an account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.registerAccount(account);
    }

    /**
     * TODO: Use the AccountDAO to login an account. The given Account will not have an id provided.
     *
     * @param account an account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account login(Account account) {
        return accountDAO.login(account);
    }


    
}
