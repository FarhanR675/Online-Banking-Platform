package Backend;


public interface UserCache {

    void addAccount(Account account);

    Account getAccount(long bankId, long bankPin);

    boolean existingAccount(long bankId, long bankPin);

    boolean accountExists(long bankId, long bankPin, String firstName, String lastName);

    double updateBalance(long bankId, double balance);

    long generateBankId();

    long generateBankPin();

}
