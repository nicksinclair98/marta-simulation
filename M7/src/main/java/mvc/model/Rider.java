package mvc.model;

public class Rider {
    private Account account;
    private boolean hasDisability;

    public Rider(Account account) {
        this(account, false);
    }

    public Rider(Account account, boolean hasDisability) {
        this.account = account;
        this.hasDisability = hasDisability;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isHasDisability() {
        return hasDisability;
    }

    public void setHasDisability(boolean hasDisability) {
        this.hasDisability = hasDisability;
    }
}
