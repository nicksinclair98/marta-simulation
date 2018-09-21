package mvc.model;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String username;
    private String password;
    private long phone;
    private boolean disabled;
    private boolean isManager;

    public Account() {
        this(null, null, -1, true, false);
    }

    /**
     * Creates an Account with a username, password, and phone number. The
     * default status is enabled.
     *
     * @param username the account holder's chosen username
     * @param password the account holder's chosen password
     * @param phone the account holder's phone number
     */

    public Account(String username, String password, long phone) {
        this(username, password, phone, false, false);
    }

    /**
     * Creates an Account with a username, password, and phone number.
     *
     * @param username the Account holder's chosen username
     * @param password the Account holder's chosen password
     * @param phone the Account holder's phone number
     * @param disabled the Account status
     */

    public Account(String username, String password, long phone, boolean
            disabled, boolean isManager) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.disabled = disabled;
        this.isManager = isManager;
    }

    /**
     * @return the Account holder's username
     */

    public String getUsername() {
        return username;
    }

    /**
     * @param username the new username for the Account
     */

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the Account holder's password
     */

    public String getPassword() {
        return password;
    }

    /**
     * @param password the new password for the Account
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the Account holder's phone number
     */

    public long getPhone() {
        return phone;
    }

    /**
     * @param phone the new phone number for the Account
     */

    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * @return the Account status
     */

    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the new account status
     */

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return if the account belongs to a manager
     */

    public boolean isManager() {
        return isManager;
    }

    /**
     * @param manager manager status
     */

    public void setManager(boolean manager) {
        isManager = manager;
    }

    /**
     * Deletes the Account
     */

    public void deleteAccount() {
        username = null;
        password = null;
        phone = -1;
        disabled = true;
    }

    public Map<String, Object> createMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("phone", phone);
        map.put("disabled", disabled);
        map.put("isManager", isManager);
        return map;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Account that = (Account) other;
        return (this.username.equals(that.username));
    }

}