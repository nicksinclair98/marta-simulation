package mvc.controller;

import mvc.model.*;

public class LoginObject extends Account {
    private Account[] validAccounts;


    public LoginObject() {
        this(null);
    }

    /**
     * Creates an instance of LoginObject with an array of valid accounts
     *
     * @param validAccounts valid accounts accepted by login page
     */
    public LoginObject(Account[] validAccounts) { //array of existing valid
        // accounts
        this.validAccounts = validAccounts;
    }


    /**
     * Checks is account being used to login is valid
     *
     * @param account the account being used to login
     * @return true or false depending on if the account is a valid account
     */
    public boolean checkLogin(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        for (int i = 0; i < validAccounts.length; i++) {
            if (username.equals(validAccounts[i].getUsername())
                    && password.equals(validAccounts[i].getPassword())) {
                return true;
            }
        }
        return false;

    }



    
}
