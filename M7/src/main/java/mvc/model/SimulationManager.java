package mvc.model;

public class SimulationManager extends Manager {

    public SimulationManager(Account account) {
        this(account, false);
    }

    public SimulationManager(Account account, boolean hasDisability) {
        super(account, hasDisability);
    }
}
