package mvc.model;

public class Manager extends Rider {

    public Manager(Account account) {
        this(account, false);
    }

    public Manager(Account account, boolean hasDisability) {
        super(account, hasDisability);
    }
}
