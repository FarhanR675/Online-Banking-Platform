package Backend;

public class Account {

    UserCache userCache = new MongoDbCache();


        private long bankId;
        private double balance;
        private String firstName;
        private String lastName;
        private long bankPin;

        public Account() {}

        public Account (long bankId, String firstName, String lastName, long bankPin) { // Used for logging in and for creating new users
            this.bankId = bankId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.bankPin = bankPin;
        }
        public Account (long bankId, double balance, String firstName, String lastName, long bankPin) { // Existing Users
            this.bankId = bankId;
            this.balance = balance;
            this.firstName = firstName;
            this.lastName = lastName;
            this.bankPin = bankPin;
        }

        public long getId () {
            return bankId;
        }
        public void setId (long bankId) {
            this.bankId = bankId;
        }
        public long getBankPin () {
            return bankPin;
        }
        public void setBankPin (long bankPin) {
            this.bankPin = bankPin;
        }
        public double getBalance () {
            return balance;
        }
        public void setBalance (double balance) {
            this.balance = balance;
        }
        public String getFirstName () {
            return firstName;
        }
        public void setFirstName (String firstName) {
            this.firstName = firstName;
        }
        public String getLastName () {
            return lastName;
        }
        public void setLastName (String lastName) {
            this.lastName = lastName;
        }

    @Override
    public String toString() {
        return "\nAccount Details \n------------------------" +
                "\nbank id = " + bankId +
                "\nbalance = " + userCache.getAccount(bankId,bankPin).balance +
                "\nfirstName = '" + firstName + '\'' +
                "\nlastName = '" + lastName + '\'' +
                "\nbank Pin = " + bankPin + "\n------------------------";
    }
}

